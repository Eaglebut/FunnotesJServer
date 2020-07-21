
import subprocess

code = subprocess.call(['/usr/bin/python3', 
                        '/home/oruskade/Documents/face_detection/application/my_app_for_server/main_script.py',
                        '-p', 
                        '/home/oruskade/Documents/face_detection/images/tut/1_fatigue/16',
                        # "/media/oruskade/Seagate Expansion Drive/Диплом Тоня/faces/dataset_10_persons/all/0_normal/03",
                        # '/home/oruskade/Pictures',
                        '-pm',
                        'CNN'])
print(code)
