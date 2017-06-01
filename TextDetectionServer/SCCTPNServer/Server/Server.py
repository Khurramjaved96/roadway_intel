import socket
from PIL import Image
import base64
import cStringIO
import json
import struct
import os
import sys
import TextDetector.TextDetect as Detector
import Utils.Utils as utils
import cv2

def send_msg(sock, msg):
    # Prefix each message with a 4-byte length (network byte order)
    msg = struct.pack('>I', len(msg)) + msg
    sock.sendall(msg)

def recv_msg(sock):
    # Read message length and unpack it into an integer
    raw_msglen = recvall(sock, 4)
    if not raw_msglen:
        return None
    msglen = struct.unpack('>I', raw_msglen)[0]
    # Read the message data
    return recvall(sock, msglen)

def recvall(sock, n):
    # Helper function to recv n bytes or return None if EOF is hit
    data = ''
    while len(data) < n:
        packet = sock.recv(n - len(data))
        if not packet:
            return None
        data += packet
    return data


def persist_file(im_base64, filename):
    f = open(os.path.join('/CTPN/ctpn_server/',filename), "wb")
    f.write(im_base64.decode('base64'))
    f.close()

def load_file(filename):
    return Image.open(os.path.join('/CTPN/ctpn_server/', filename))




if __name__ == "__main__":
    myDetector = Detector.CTPNDetector()
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_address = (socket.gethostbyname(socket.gethostname()), 41927)
    print socket.gethostbyname(socket.gethostname()), ' ', '41927'
    sock.bind(server_address)
    sock.listen(1)
    print 'Waiting for the connection'
    while True:
            # Wait for a connection
            connection, client_address = sock.accept()

            try:
                print >>sys.stderr, 'connection from', client_address

                # Receive the data in small chunks and retransmit it
                data = recv_msg(connection)
                j = json.loads(data)
                filename = j['filename']
                extension = j['filename'].split(".")[1]
                im_base64 = j['im_base64']
                persist_file(im_base64, filename)
                crnn_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                server_address = ('172.17.0.3', 41926)
                crnn_sock.connect(server_address)
                im = cv2.imread(os.path.join('/CTPN/ctpn_server/', filename))
                im, f=resize_im(im, cfg.SCALE, cfg.MAX_SCALE)
                text_lines=myDetector.text_detector.detect(im)
                print "Number of the detected text lines: %s"%len(text_lines)
                n = 0
                crnn_sock.send(str(len(text_lines)) + "\n")
                OK = crnn_sock.recv(16)
                if OK != "OK":
                    print ('ERROR')
                    sys.exit()
                text_lines = sorted(text_lines, key=lambda x: x[1])
                right=True
                labels = []
                for box in text_lines:
                    buffer = cStringIO.StringIO()
                    detected = im[int(box[1]):int(box[3]), int(box[0]):int(box[2]),:]
                    Image.fromarray(detected).save(buffer, format='JPEG')
                    im_str = base64.b64encode(buffer.getvalue())
                    crnn_sock.send(im_str + "\n")
                    label = crnn_sock.recv(16)
                    labels += [label]
                    n+=1

                for idx, box in enumerate(text_lines):
                    if utils.reject(labels[idx]):
                        continue
                    cv2.rectangle(im, (box[0], box[1]), (box[2], box[3]), (0, 0, 255), 1)
                    if right is True:
                        cv2.rectangle(im, (int(box[2])+15, int(box[1])+5),
                                        ((int(box[2])+15)+12*len(labels[idx]), int(box[1]) - 15), (107, 142, 35), -1)
                        cv2.putText(im, labels[idx].upper(), (int(box[2])+18, int(box[1])), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
                                                                                                (255,255, 255), 1)
                    else:
                        cv2.rectangle(im, (int(box[0])-15-(12*len(labels[idx])), int(box[1])-15),
                                            (int(box[0] - 5), int(box[1]) + 5), (107, 142, 35), -1)
                        cv2.putText(im, labels[idx].upper(), (int(box[0])-12-(12*len(labels[idx])), int(box[1])),
                                            cv2.FONT_HERSHEY_SIMPLEX, 0.5,
                                            (255, 255, 255), 1)
                    right = not right
                cv2.imwrite("/CTPN/results/" + filename.replace(".jpg","") + str(n) + '.jpg', im)
                '''
                Sending the final image back to server.py
                '''
                buffer = cStringIO.StringIO()
                im_format = 'PNG'
                ext = filename.split('.')[1].upper()
                if ext == 'JPG' or ext == 'JPEG':
                    im_format='JPEG'
                im = cv2.cvtColor(im, cv2.COLOR_BGR2RGB)
                Image.fromarray(im).save(buffer, im_format)
                im_str = base64.b64encode(buffer.getvalue())
                send_msg(connection, im_str)
                crnn_sock.close()
            finally:
                connection.close()
