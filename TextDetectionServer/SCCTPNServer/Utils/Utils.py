import editdistance
import re


#Strings which are simialr to these strings should be rejected.
falseRecognitions = ['islamabad', 'ict-islamabad', 'sindh', 'karachi', 'rawalpindi', 'cultus',
                        'suzuki', 'toyota', 'mehran', 'euro', 'punjab']

#Simple function to discard those strings which are similar to falseRecognition list. This also uses are regular expression to try to find
#License plate like pattern

def reject(recognition):
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