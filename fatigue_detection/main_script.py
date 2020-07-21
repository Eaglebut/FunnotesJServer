import sys
import argparse
from models_util import load_model_CNN, load_data_for_CNN_form_img_arr, predict_CNN, load_models_CNN_and_RNN, \
    load_data_for_resnet_from_img_arr, predict_cnn_and_rnn
from data_preprocessing import preprocessing
import traceback
import time

DEFAUTL_SHAPE_PREDICTOR_FILE_PATH = 'models/shape_predictor_68_face_landmarks.dat'
DEFAUTL_PREDICTION_MODE = 'CNN'
DEFAUTL_CNN_MODEL_PATH = 'models/(best)model_r2plus1d_18_all.pth'
DEFAUTL_RNN_MODEL_PATH = 'models/(best)model_best_hidden_128_reatures_512_resnet18.pth'
DEFAUTL_RESNET_MODEL_PATH = 'models/resnet_18.pth'
DEFAUTL_RESULT_FILE = 'result.txt'


def load_model_cnn(cnn_model_path):
    print('Loading')
    model_name = 'r2plus1d_18'
    model = load_model_CNN(model_name, cnn_model_path)
    print('Loaded')
    return model


def load_pictures(image_dir_path, shape_predictor_path):
    print('Load pictures ' + time.ctime(time.time()))
    preprocessed_data_array = preprocessing(image_dir_path, shape_predictor_path, (112, 112))
    data_array = load_data_for_CNN_form_img_arr(preprocessed_data_array)
    print('Pictures loaded ' + time.ctime(time.time()))
    return data_array


def predict_by_cnn(cnn_model_path, image_dir_path, shape_predictor_path):
    print('Load model cnn')
    model_name = 'r2plus1d_18'
    model = load_model_CNN(model_name, cnn_model_path)
    print('Load data')
    preprocessed_data_array = preprocessing(image_dir_path, shape_predictor_path, (112, 112))
    data_array = load_data_for_CNN_form_img_arr(preprocessed_data_array)
    print('Start prediction')
    result = predict_CNN(model, data_array)
    print('Result is {}'.format(result))
    return result


def load_model_rnn(rnn_model_path, resnet_model_path):
    print('Load models')
    resnet_name = 'resnet_18'
    model_name = 'lstm_128'
    resnet, model = load_models_CNN_and_RNN(resnet_name, model_name, rnn_model_path, resnet_model_path)
    print('Models loaded')
    return resnet, model


def predict_by_rnn(rnn_model_path, resnet_model_path, image_dir_path, shape_predictor_path):
    print('Load models')
    resnet_name = 'resnet_18'
    model_name = 'lstm_128'
    resnet, model = load_models_CNN_and_RNN(resnet_name, model_name, rnn_model_path, resnet_model_path)
    print('Load data')
    preprocessed_data_array = preprocessing(image_dir_path, shape_predictor_path, (224, 224))
    data_array = load_data_for_resnet_from_img_arr(preprocessed_data_array)
    print('Start prediction')
    result = predict_cnn_and_rnn(model, resnet, data_array)
    print('Result is {}'.format(result))
    return result


def print_result_to_file(result, file_path):
    f = open(file_path, 'w')
    try:
        f.write(str(result))
    finally:
        f.close()


def create_parser():
    parser = argparse.ArgumentParser(prog='Fatigue detection',
                                     description='result 0 - normal, result 1 - fatigue, result 255 - error')
    parser.add_argument('-p', '--path_to_image_folder', required=True,
                        help='Requered parameter. Path to folder with 15 images to predict')
    parser.add_argument('-rf', '--result_file', default=DEFAUTL_RESULT_FILE, help='Path to file for result')
    parser.add_argument('-sp', '--shape_predictor_file_path', default=DEFAUTL_SHAPE_PREDICTOR_FILE_PATH,
                        help='Path to shape_predictor_68_face_landmarks.dat file for dlib')  # for dlib
    parser.add_argument('-pm', '--prediction_mode', choices=['CNN', 'CNN+RNN'], default=DEFAUTL_PREDICTION_MODE,
                        help='Predriction mode sets an approuch to use (one model - CNN, combination of models - CNN+RNN)')
    parser.add_argument('-cnn', '--cnn_model_path', default=DEFAUTL_CNN_MODEL_PATH, help='Path to CNN model')
    parser.add_argument('-rnn', '--rnn_model_path', default=DEFAUTL_RNN_MODEL_PATH, help='Path to RNN model')
    parser.add_argument('-resnet', '--resnet_model_path', default=DEFAUTL_RESNET_MODEL_PATH,
                        help='Path to resNet18 model')

    return parser


if __name__ == '__main__':
    parser = create_parser()
    namespace = parser.parse_args(sys.argv[1:])
    if namespace.prediction_mode == 'CNN':
        model = load_model_cnn(namespace.cnn_model_path)
        while True:
            input()
            data_array = load_pictures(namespace.path_to_image_folder, namespace.shape_predictor_file_path)
            result = predict_CNN(model, data_array)
            print(time.ctime(time.time()))
            print(result)

        # result = predict_by_cnn(namespace.cnn_model_path, namespace.path_to_image_folder,
        #                        namespace.shape_predictor_file_path)
    else:
        result = predict_by_rnn(namespace.rnn_model_path, namespace.resnet_model_path, namespace.path_to_image_folder,
                                namespace.shape_predictor_file_path)
