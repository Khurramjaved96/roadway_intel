import numpy as np
import editdistance
import re


#Strings which are simialr to these strings should be rejected.
falseRecognitions = ['islamabad', 'ict-islamabad', 'sindh', 'karachi', 'rawalpindi', 'cultus',
                        'suzuki', 'toyota', 'mehran', 'euro', 'punjab']

#Simple function to discard those strings which are similar to falseRecognition list. This also uses are regular expression to try to find
#License plate like pattern

def reject(recognition):
    for string in falseRecognitions:
        #If edit distance between input and falseRecognitio  is less than or equal to two, simply reject the sample.
        dist = editdistance.eval(recognition, string)
        if dist <= 2:
            return True

    m = re.match(r'^[a-z]{1,4}[-]?\d{1,5}$', recognition)
    if m:
        return False
    if recognition.isdigit():
        return False
    if recognition.isalpha() and len(recognition) <= 3:
        return False
    return True