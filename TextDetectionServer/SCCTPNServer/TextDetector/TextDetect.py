# THIS CODE IS PROVIDED BY THE AUTHOR OF THE PAPER. IT IS USED IN ROADWAY INTEL FOR DETECTING LISCENSE PLATES
# This code can only work when placed inside the CTPN model


# The codes are used for implementing CTPN for scene text detection, described in:
#
# Z. Tian, W. Huang, T. He, P. He and Y. Qiao: Detecting Text in Natural Image with
# Connectionist Text Proposal Network, ECCV, 2016.
#
# Online demo is available at: textdet.com
#
# These demo codes (with our trained model) are for text-line detection (without
# side-refiement part).
#G
#
# ====== Copyright by Zhi Tian, Weilin Huang, Tong He, Pan He and Yu Qiao==========

#            Email: zhi.tian@siat.ac.cn; wl.huang@siat.ac.cn
#
#   Shenzhen Institutes of Advanced Technology, Chinese Academy of Sciences
#



from cfg import Config as cfg
from other import draw_boxes, resize_im, CaffeModel
import cv2, os, caffe, sys
from detectors import TextProposalDetector, TextDetector
import os.path as osp



class CTPNDetector:
    def __init__(self):
        '''
        @Construction for text detector. 
        This class initiates the constructor for 
        '''
        self.NET_DEF_FILE = "models/deploy.prototxt"
        self.MODEL_FILE = "models/ctpn_trained_model.caffemodel"
        caffe.set_mode_gpu()
        caffe.set_device(cfg.TEST_GPU_ID)
        self.text_proposals_detector = TextProposalDetector(CaffeModel(self.NET_DEF_FILE, self.MODEL_FILE))
        self.text_detector = TextDetector(self.text_proposals_detector)

    def detect(self, filepath):
        """

        :param filepath: File that is opened and then text in this file are detected using CTPN
        :return: List of co-ordinates of text boxes
        """
        im = cv2.imread(filepath)
        im, f = resize_im(im, cfg.SCALE, cfg.MAX_SCALE)
        self.text_lines = self.text_detector.detect(im)
        return self.text_lines