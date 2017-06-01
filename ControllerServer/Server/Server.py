import os
# We'll render HTML templates and access data sent by POST
# using the request object from flask. Redirect and url_for
# will be used to redirect the user once the upload is done
# and send_from_directory will help us to send/show on the
# browser the file that the user just uploaded
from flask import Flask, render_template, request, redirect, url_for, send_from_directory
from werkzeug import secure_filename
from cStringIO import StringIO
import base64
import socket, sys
import json
import struct
from flask import jsonify
import time
# Initialize the Flask application
app = Flask(__name__)

# This is the path to the upload directory
app.config['UPLOAD_FOLDER'] = 'uploads/'
# These are the extension that we are accepting to be uploaded
app.config['ALLOWED_EXTENSIONS'] = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

server_address = ('172.17.0.2', 41920)
# For a given file, return whether it's an allowed type or not
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS']

def persist_file(im_base64, filename):
    f = open(os.path.join(app.config['UPLOAD_FOLDER'], filename), "wb")
    f.write(im_base64.decode('base64'))
    f.close()

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


@app.route('/upload', methods=['GET', 'POST'])
def upload():
    img = request.get_data()
    j = {'filename': 'test.jpg', 'im_base64': img}
    j = json.dumps(j)
    persist_file(img, 'test.jpg')
    print "Persisting File"
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect(server_address)
    try:
        send_msg(sock, j)
        # Wait for the process image here
        im_base64 = recv_msg(sock)
        persist_file(im_base64, filename)
        #data = sock.recv(65535)
    finally:
        sock.close()
        return im_base64
# Route that will process the file upload
@app.route('/upload2', methods=['POST'])
def upload2():
    # Get the name of the uploaded file
    file = request.files['file']
    #print file
    # Check if the file is one of the allowed types/extensions
    if file and allowed_file(file.filename):
        # Make the filename safe, remove unsupported chars
        start = time.time()
        filename = secure_filename(file.filename)
        inmem_file = StringIO()
        file.save(inmem_file)
        im_base64 = base64.b64encode(inmem_file.getvalue())
        # Move the file form the temporal folder to
        # the upload folder we setup
        #file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        j = {'filename': file.filename, 'im_base64': im_base64}
        j = json.dumps(j)
        persist_file(im_base64, filename)
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(server_address)
        try:
            send_msg(sock, j)
            # Wait for the process image here
            im_base64 = recv_msg(sock)
            persist_file(im_base64, filename)
            #data = sock.recv(65535)
        finally:
            sock.close()
        # Redirect the user to the uploaded_file route, which
        # will basicaly show on the browser the uploaded file
        end = time.time()
        print 'Time: %.2f' % (end-start)
        return jsonify(result={"status": 200,"url":filename})



# This route is expecting a parameter containing the name
# of a file. Then it will locate that file on the upload
# directory and show it on the browser, so if the user uploads
# an image, that image is going to be show after the upload
@app.route('/uploads/<filename>')
def uploaded_file(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'],
                               filename)

if __name__ == '__main__':
    app.run(
        host="0.0.0.0",
        port=int("5000"),
        debug=True
    )

