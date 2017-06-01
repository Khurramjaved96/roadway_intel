#Get initial image with GPU support
FROM bvlc/caffe:gpu

#Clone CTPN directory
RUN git clone  https://github.com/tianzhi0549/CTPN.git

#Download CTPN trained model
RUN cd CTPN/models && wget http://textdet.com/downloads/ctpn_trained_model.caffemodel

#Configure makefile for compilation
RUN cd CTPN/caffe && cp Makefile.config.example Makefile.config
RUN cd CTPN/caffe && sed -ie 's/# WITH_PYTHON_LAYER/WITH_PYTHON_LAYER/g' Makefile.config
RUN cd CTPN/caffe && echo 'INCLUDE_DIRS := $(PYTHON_INCLUDE) /usr/local/include /usr/include/hdf5/serial/' >> Makefile.config
RUN cd CTPN/caffe && echo 'LIBRARY_DIRS := $(PYTHON_LIB) /usr/local/lib /usr/lib /usr/lib/x86_64-linux-gnu/hdf5/serial/' >> Makefile.config

#Compiling code
RUN cd CTPN/caffe && make -j2 && make pycaffe
RUN cd CTPN && make

#Now we need to compile OpenCV for image IO. For that we first update/upgrade the package manager and packages
RUN apt-get update

RUN apt-get -y update
RUN apt-get -y upgrade

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get -y dist-upgrade
RUN apt-get -y autoremove


# INSTALL THE DEPENDENCIES.

# Build tools:
RUN apt-get install -y build-essential cmake

#OpenCV Dependencies
RUN apt-get install -y qt5-default libvtk6-dev
RUN apt-get install -y zlib1g-dev libjpeg-dev libwebp-dev libpng-dev libtiff5-dev libjasper-dev libopenexr-dev libgdal-dev

RUN apt-get install -y libdc1394-22-dev libavcodec-dev libavformat-dev libswscale-dev libtheora-dev libvorbis-dev libxvidcore-dev libx264-dev yasm libopencore-amrnb-dev libopencore-amrwb-dev libv4l-dev libxine2-dev
RUN apt-get install -y libtbb-dev libeigen3-dev

# Python:
RUN apt-get install -y python-dev python-tk python-numpy python3-dev python3-tk python3-numpy

#Compiling OpenCV with the required dependencies.

RUN apt-get install -y unzip wget
RUN wget https://github.com/opencv/opencv/archive/3.2.0.zip
RUN unzip 3.2.0.zip
RUN rm 3.2.0.zip
RUN mv opencv-3.2.0 OpenCV

#Set Cuda path for linking
RUN export CUDA_LIB_PATH=/usr/local/cuda/lib64/stubs/

#Compile OpenCV. This step can take time.
RUN cd OpenCV && mkdir build && cd build && cmake -D WITH_CUDA=OFF ..
RUN cd OpenCV && cd build && make -j16 && make install && ldconfig
