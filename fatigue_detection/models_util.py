import os
import numpy as np
import torch, torch.nn as nn
import matplotlib.pyplot as plt
import transforms as T
from torchvision import transforms, models
import matplotlib.pyplot as plt
from PIL import Image
from torch.autograd import Variable
import torch.nn.functional as F

#For CNN
def load_model_CNN(model_name, model_path, device=None):
  if model_name == 'r3d_18':
    model = models.video.r3d_18(pretrained=False)
  elif model_name == 'r2plus1d_18':
    model = models.video.r2plus1d_18(pretrained=False)
  elif model_name == 'mc3_18':
    model = models.video.mc3_18(pretrained=False)
  else:
    raise ValueError("Unsupported name of model: {}. Expected [r3d_18, r2plus1d_18, mc3_18]".format(model_name))

  if device is None:
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

  model.fc = torch.nn.Linear(model.fc.in_features, 2)  
  model.load_state_dict(torch.load(model_path, map_location=device))
  model.eval()
  
  model = model.to(device)
  return model

def load_data_for_CNN_from_dir(dir_path):
  paths = os.listdir(dir_path)
  paths.sort()
  my_face = paths[:15]
  transforms1 = transforms.Compose([transforms.Resize((112,112)), transforms.ToTensor()])
  transforms2 = transforms.Compose([                                      
            T.ToFloatTensorInZeroOne(),
            T.Normalize(mean = [0.43216, 0.394666, 0.37645], std = [0.22803, 0.22145, 0.216989])
        ])
  clip = torch.Tensor()
  for img_file in [dir_path+os.path.sep+x for x in my_face]:
    img = Image.open(img_file)
    img_tensor = transforms1(img)
    clip = torch.cat((clip, img_tensor.unsqueeze(0)), dim = 0)
  clip = clip.permute(0,2,3,1)
  clip = transforms2(clip)  
  return clip

def load_data_for_CNN_form_img_arr(img_arr):
  transforms1 = transforms.Compose([transforms.Resize((112,112)), transforms.ToTensor()])
  transforms2 = transforms.Compose([                                      
            T.ToFloatTensorInZeroOne(),
            T.Normalize(mean = [0.43216, 0.394666, 0.37645], std = [0.22803, 0.22145, 0.216989])
        ])
  clip = torch.Tensor()
  for img in img_arr:
    img_tensor = transforms1(img)
    clip = torch.cat((clip, img_tensor.unsqueeze(0)), dim = 0)
  clip = clip.permute(0,2,3,1)
  clip = transforms2(clip)  
  return clip

def show_clip_frames_CNN(clip):
  mean = np.array([0.43216, 0.394666, 0.37645])
  std = np.array([0.22803, 0.22145, 0.216989])
  fgs, axis = plt.subplots(3,5, figsize = (20,10))
  for ax, i in zip (axis.ravel(), range(clip.shape[1])):
    frame = clip.permute(1, 0, 2, 3)[i]
    frame = (frame.permute(1,2,0).numpy() * std + mean) * 255
    ax.imshow(frame)  

def predict_CNN(model, clip, device=None):
  if device is None:
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
  with torch.set_grad_enabled(False):
    pred = model(torch.unsqueeze(clip, dim=0).to(device))
    pred = pred.argmax(dim=1).to('cpu').numpy()
    return pred[0]


#For CNN+RNN
class Identity(torch.nn.Module):
  def __init__(self):
    super().__init__()

  def forward(self, x):
    return x

class MyLSTM(nn.Module):
    def __init__(self, num_targets, emb_size, lstm_num_units, dropout = 0.1):
        super(self.__class__, self).__init__()
        self.lstm = nn.LSTM(emb_size, lstm_num_units, batch_first=True)
        self.hid_to_logits = nn.Linear(lstm_num_units, num_targets)
        self.dropout = nn.Dropout(dropout)
        
    def forward(self, x):
        assert isinstance(x, Variable) and isinstance(x.data, torch.FloatTensor)
        x = self.dropout(x)
        h_seq, _ = self.lstm(x)
        next_logits = self.hid_to_logits(h_seq)
        next_logp = F.log_softmax(next_logits, dim=-1)
        return next_logp

    def predict_class(self, sqns):
        with torch.no_grad():
            tags_pred = self(sqns).numpy()
            tags_pred = np.argmax(tags_pred[0][-1], axis=0)  
        return tags_pred   


def load_models_CNN_and_RNN(resnet_name, model_name, model_path,resnet_path=None, device=None):
  if resnet_path is None:
    pretrained = False
  else:
    pretrained = True     
  if resnet_name == 'resnet_18':
    resnet = models.resnet18(pretrained=pretrained)
    len_sq = 512
  elif resnet_name == 'resnet_34':
    resnet = models.resnet34(pretrained=pretrained)
    len_sq = 512
  elif resnet_name == 'resnet_50':
    resnet = models.resnet50(pretrained=pretrained)
    len_sq = 2048
  else:
    raise ValueError("Unsupported name of ResNet model: {}. Expected [resnet_18, resnet_34, resnet_50]".format(resnet_name))
  
  if device is None:
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

  resnet.fc = Identity()
  if resnet_path is not None:
    resnet.load_state_dict(torch.load(resnet_path, map_location=device))
  resnet.eval()
  
  resnet.to(device)

  if model_name == 'lstm_128':
    rnn_num_units = 128
  elif model_name == 'lstm_256':
    rnn_num_units = 256
  else:
    raise ValueError("Unsupported name of LSTM model: {}. Expected [lstm_128, lstm_256]".format(model_name))

  model = MyLSTM(2, len_sq, rnn_num_units)
  model.load_state_dict(torch.load(model_path))
  model.eval()
  # model.to(device)
  return resnet, model

def load_data_for_resnet_from_dir(dir_path):
  paths = os.listdir(dir_path)
  paths.sort()
  my_face = paths[:15]

  arr_transforms = transforms.Compose([
    transforms.Resize((224,224)),                                    
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
  ])

  clip = torch.Tensor()
  for img_file in [dir_path+os.path.sep+x for x in my_face]:
    img = Image.open(img_file)
    img_tensor = arr_transforms(img)
    clip = torch.cat((clip, img_tensor.unsqueeze(0)), dim = 0)
  return clip

def load_data_for_resnet_from_img_arr(img_arr):
  arr_transforms = transforms.Compose([
    transforms.Resize((224,224)), 
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
  ])

  clip = torch.Tensor()
  for img in img_arr:
    img_tensor = arr_transforms(img)
    clip = torch.cat((clip, img_tensor.unsqueeze(0)), dim = 0)
  return clip

def show_clip_frames_CNN_and_RNN(clip):
  mean = np.array([0.43216, 0.394666, 0.37645])
  std = np.array([0.22803, 0.22145, 0.216989])
  fgs, axis = plt.subplots(3,5, figsize = (20,10))
  for ax, i in zip (axis.ravel(), range(clip.shape[0])):
    frame = clip[i]
    frame = (frame.permute(1,2,0).numpy() * std + mean)
    ax.imshow(frame)   

def feature_extraction(resnet, clip, device=None):
  if device is None:
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
  with torch.no_grad():
    features = resnet(clip.to(device))
    return features.to('cpu').numpy()

def data_for_lstm(features):
  arr = []
  for f in features:
    arr.append(f)
  data = torch.tensor(arr, dtype=torch.float)  
  data = data.unsqueeze(0)
  return data  

def predict_cnn_and_rnn(model, resnet, clip, device=None):
  if device is None:
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
  features =  feature_extraction(resnet, clip, device)
  data = data_for_lstm(features)
  pred = model.predict_class(data)
  return pred