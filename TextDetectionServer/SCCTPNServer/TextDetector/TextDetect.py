from cfg import Config as cfg
from other import draw_boxes, resize_im, CaffeModel
import cv2, os, caffe, sys
from detectors import TextProposalDetector, TextDetector
import os.path as osp



class TextDetector:
    def __init__(self):
        '''
        @Construction for text detector. 
        This class initiates the constructor for 
        '''
        self.NET_DEF_FILE = "models/deploy.prototxt"
        self.MODEL_FILE = "models/ctpn_trained_model.caffemodel"
        caffe.set_mode_gpu()
        caffe.set_device(cfg.TEST_GPU_ID)
        self.text_proposals_detector = TextProposalDetector(CaffeModel(NET_DEF_FILE, MODEL_FILE))
        self.text_detector = TextDetector(text_proposals_detector)

    def detect(self):
        im = cv2.imread(os.path.join('/CTPN/ctpn_server/', filename))
        im, f = resize_im(im, cfg.SCALE, cfg.MAX_SCALE)
        self.text_lines = self.text_detector.detect(im)
        print "Number of the detected text lines: %s" % len(text_lines)