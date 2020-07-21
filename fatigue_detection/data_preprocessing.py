import cv2
import dlib
import os
import numpy as np
from PIL import Image


def crop_image(frame, size, detector, predictor):
    grayFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = detector(grayFrame)
    # Обход списка всех лиц попавших на изображение
    if len(faces) != 0:
        i = 0
        # Получение координат вершин прямоугольника и его построение на изображении
        x1 = faces[i].left()
        y1 = faces[i].top()
        x2 = faces[i].right()
        y2 = faces[i].bottom()
        points = np.zeros([2, 68], dtype=int)
        # Получение координат контрольных точек 
        landmarks = predictor(grayFrame, faces[i])
        for n in range(0, 68):
            points[0][n] = landmarks.part(n).x
            points[1][n] = landmarks.part(n).y
            # координаты для обрезки, максимально большая область лица, либо по прямоугольнику, либо по выступающим точкам
        max_x_point = np.max([np.max(points[0]), x2])
        min_x_point = np.min([np.min(points[0]), x1])
        max_y_point = np.max([np.max(points[1]), y2])
        min_y_point = np.min([np.min(points[1]), y1])
        # попробовать сделать всегда квадрат         
        width = max_x_point - min_x_point
        height = max_y_point - min_y_point
        if width >= height:
            rect_x_max = max_x_point
            rect_x_min = min_x_point
            rect_y_min = int((max_y_point + min_y_point) / 2 - width / 2)
            rect_y_max = rect_y_min + width
        else:
            rect_y_max = max_y_point
            rect_y_min = min_y_point
            rect_x_min = int((max_x_point + min_x_point) / 2 - height / 2)
            rect_x_max = rect_x_min + height
        if rect_x_min < 0:
            rect_x_min = 0
        if rect_y_min < 0:
            rect_y_min = 0
        cropped = frame[rect_y_min:rect_y_max, rect_x_min:rect_x_max, :]
        if cropped.shape[0] != cropped.shape[1]:
            max_shape = cropped.shape[0] if cropped.shape[0] > cropped.shape[1] else cropped.shape[1]
            temp_arr = np.zeros([max_shape, max_shape, 3], dtype='uint8')
            index_x, index_y = 0, 0
            if rect_x_min == 0:
                index_x = temp_arr.shape[1] - cropped.shape[1]
            if rect_y_min == 0:
                index_y = temp_arr.shape[0] - cropped.shape[0]
            temp_arr[index_y:index_y + cropped.shape[0], index_x:index_x + cropped.shape[1], :] = np.copy(cropped)
            cropped = np.copy(temp_arr)
            # сохранение лица с каждого target_frame-го кадра
        cropped = cv2.resize(cropped, size)
        cropped = cv2.cvtColor(cropped, cv2.COLOR_BGR2RGB)
        return cropped
    else:
        raise ValueError("Face is not found")


def preprocessing(img_dir_path, shape_predictor_path, size):
    if len(os.listdir(img_dir_path)) >= 15:
        imgs = []
        detector = dlib.get_frontal_face_detector()
        predictor = dlib.shape_predictor(shape_predictor_path)
        paths = [img_dir_path + os.path.sep + img for img in os.listdir(img_dir_path)[-15:]]
        for p in paths:
            img = cv2.imread(p)
            try:
                img = crop_image(img, size, detector, predictor)
                imgs.append(Image.fromarray(img))
            except ValueError:
                raise ValueError("Face is not found in {}".format(p))
        return imgs
    else:
        raise ValueError("Not enougth data")
