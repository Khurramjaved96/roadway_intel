import os




def allowed_file(filename, allowedExtensions):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in allowedExtensions

def persist_file(im_base64, filename, folder):
    f = open(os.path.join(folder, filename), "wb")
    f.write(im_base64.decode('base64'))
    f.close()