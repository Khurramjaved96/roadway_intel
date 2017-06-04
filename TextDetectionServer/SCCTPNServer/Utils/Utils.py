import editdistance
import re
from PIL import Image
import os




#Simple function to discard those strings which are similar to falseRecognition list. This also uses are regular expression to try to find
#License plate like pattern
def reject(recognition, falseRecognitions):
    """
    Function to reject false detection. This function rejects strings matching a pre-defined list of garbage values, and reject those which
    are 1 : Not of plate pattern. 2 : Not of alphabets 3: Not of digits. 4: Too long. 
    :param recognition: 
    :return boolean:
    """
    recognition=recognition.lower();
    for string in falseRecognitions:
        #If edit distance between input and falseRecognitio  is less than or equal to two, simply reject the sample.
        dist = editdistance.eval(recognition, string)
        if dist <= 2:
            return True

    regexResult = re.match(r'^[a-z,A-Z]{1,4}[-]?[0-9]{1,5}$', recognition)
    if regexResult:
        return False
    if recognition.isdigit() and len(recognition) <=5:
        return False
    if recognition.isalpha() and len(recognition) <= 3:
        return False
    return True


def persist_file(im_base64, filename):
    """

    :param im_base64: Data to be written on file. For our use-case, we expect data to be an image in base_64 encoding.  
    :param filename: Name of the file with which the data would be saved. 
    """
    f = open(os.path.join('/CTPN/ctpn_server/',filename), "wb")
    f.write(im_base64.decode('base64'))
    f.close()

def load_file(filename):
    """
    :param filename: Name of file to be read and returned. 
    :rtype: A string containing data of the file. 
    """
    return Image.open(os.path.join('/CTPN/ctpn_server/', filename))