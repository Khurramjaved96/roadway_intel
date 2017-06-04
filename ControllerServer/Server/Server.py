from flask import Flask, request
import socket
import json
import struct
import Utilities.Utils as Utils

# Initialize the Flask application
app = Flask(__name__)


# This is the path to the upload directory for testing purposes
app.config['UPLOAD_FOLDER'] = 'uploads/'

# These are the extension that we are accepting to be uploaded
app.config['ALLOWED_EXTENSIONS'] = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

server_address = ('172.17.0.2', 41920)


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


#This routine deals with recieving a file and the processing the file
@app.route('/upload', methods=['GET', 'POST'])
def upload():
    """
    Routine called when a client sends data using POST to /upload 
    :return: 
    """
    #Get data
    img = request.get_data()

    #save data into JSON fornat
    j = {'filename': 'test.jpg', 'im_base64': img}
    j = json.dumps(j)

    Utils.persist_file(img, 'test.jpg',app.config['UPLOAD_FOLDER'])
    print "Persisting File"
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect(server_address)
    try:
        send_msg(sock, j)
        # Wait for the process image here
        im_base64 = recv_msg(sock)
    finally:
        sock.close()
        return im_base64



if __name__ == '__main__':
    app.run(
        host="0.0.0.0",
        port=int("5000"),
        debug=True
    )

