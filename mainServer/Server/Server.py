
#Import required packages
from flask import Flask, render_template, request, redirect, url_for, send_from_directory
from werkzeug import secure_filename
import socket, sys
import json
import struct
import Utils.Utils as Utils
import os

#Initlizate Flask Application
app = Flask(__name__)

# This is the path to the upload directory
app.config['UPLOAD_FOLDER'] = 'uploads/'
# These are the extension that we are accepting to be uploaded
app.config['ALLOWED_EXTENSIONS'] = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

server_address = ('172.17.0.2', 41920)
# For a given file, return whether it's an allowed type or not

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


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS']

def persist_file(im_base64, filename):
    f = open(os.path.join(app.config['UPLOAD_FOLDER'], filename), "wb")
    f.write(im_base64.decode('base64'))
    f.close()





@app.route('/upload', methods=['GET', 'POST'])
def upload():
    img = request.get_data()
    j = {'filename': 'test.jpg', 'im_base64': img}
    j = json.dumps(j)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect(server_address)
    try:
        send_msg(sock, j)
        # Wait for the process image here
        im_base64 = recv_msg(sock)
        Utils.persist_file(im_base64, filename)
    finally:
        sock.close()
        return im_base64



#Address that is used by client to get back the result.
@app.route('/'+app.config['UPLOAD_FOLDER']+'/<filename>')
def uploaded_file(filename):
    print "This is a test";
    0/0
    return send_from_directory(app.config['UPLOAD_FOLDER'],
                               filename)

if __name__ == '__main__':
    app.run(
        host="0.0.0.0",
        port=int("5000"),
        debug=True
    )

