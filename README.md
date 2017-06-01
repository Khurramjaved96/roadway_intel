# Roadway Intel

### Number Plate Recognition using CRNN 

Number plate recognition from an image has two parts: 
1. Number plate detection 
2. Number plate recognition 

#### Number Plate Detection 
Number plate detection is done using a text detection method called CTPN. We used an off the shelf system that came from recent research for this purpose. However this system has a lot of dependencies. To manage the dependencies easily for easy deployment on a new server, we took help from Docker. 
Dockerfile of text detection can be found at roadway_intel/TextDetectionServer/SCCTPNServer/

Simply running 
```shell
"nvidia-docker build ."
```
in this directory would setup a container with all the dependencies of text detector. The server files in roadway_intel/TextDetectionServer/SCCTPNServer/ can then be copied to this container to run the module. 
NOTE : To run this module, a GPU with at-least 1.5 GB memory is required. This can run on a CPU however inference time is orders of magnitude slower. 

#### Number Plate Recogntion 
Number plate recognition is the next step in the pipeline. Again, number plate recognition module has a lot of dependencies. These dependencies also conflict with those of detection module. 
Consequently, we use another docker container to manage the recognition module. 

Dockerfile for recognition can be found at roadway_intel/TextRecognitionServer/. 

After building the container, the model file has to be replaced with our trained model. Our model can be downloaded from : 

#### Dataset 

We generate data synthetically to mimic the variations found in Pakistan. 

#### Training 
Training for recognition is done using Torch in Lua. 

#### Overall Flow 
We use a Docker Container to contain all the dependencies of our recognition system. The mobile app we are working on will communicate with the server through a simple API.


### Vehicle make and model classification in Tensorflow by fine-tuning VGG16

### Dataset
Training dataset consisted of 841 vehicle make/models from [CompCars dataset](http://mmlab.ie.cuhk.edu.hk/datasets/comp_cars/index.html)[1]

### Architecture
A [VGG16](http://arxiv.org/pdf/1409.1556.pdf) model pre-trained on ImageNet was fine-tuned with CompCars dataset (~100,000 images - 120 images/class)

Training and evaluation pipeline was inspired from [Tensorflow's CIFAR tutorial](https://www.tensorflow.org/versions/r0.10/tutorials/deep_cnn/index.html).
VGG16 model weights from [Caffe Model Zoo](https://github.com/BVLC/caffe/wiki/Model-Zoo) were converted to numpy native fromat using [caffe-tensorflow](https://github.com/ethereon/caffe-tensorflow) tool.


### Example Usage
Make sure you've extracted CompCars dataset to PROJECT_ROOT/.
#### Training:
```shell
python -B main.py -t --batch_size 40 --epochs 200 --base_learning_rate=0.0001
--decay_factor=0.1 --decay_epochs=100 --no_gpus=2
```
#### Evaluation:
```shell
python -B main.py -e --batch_size 40
```

#### Results
Accuracy: **93.12% top-5** after **115 epochs**
Conv5 (Conv5_1, Conv5_2, Conv5_3), FC6, FC7 and FC8 were fine-tuned with vanilla SGD.  
Base learning rate of 0.0001 and batch size of 70 were used. 

### References
[1] Linjie Yang, Ping Luo, Chen Change Loy, Xiaoou Tang. A Large-Scale Car Dataset for Fine-Grained Categorization and Verification, In Computer Vision and Pattern Recognition (CVPR), 2015.
