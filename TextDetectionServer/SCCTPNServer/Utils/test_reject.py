from unittest import TestCase
import unittest
import Utils

falseRecognitions = ['islamabad', 'ict-islamabad', 'sindh', 'karachi', 'rawalpindi', 'cultus',
                         'suzuki', 'toyota', 'mehran', 'euro', 'punjab']

class TestReject(TestCase):
    '''
    Test cases to check implementation of Text rejection function
    '''


    def test_regex_correct(self):
        self.assertEqual(False,Utils.reject("abc-123", falseRecognitions))
        self.assertEqual(False, Utils.reject("abc123",falseRecognitions))
        self.assertEqual(False, Utils.reject("abcd123",falseRecognitions))
        self.assertEqual(False, Utils.reject("a-123",falseRecognitions))
        self.assertEqual(False, Utils.reject("gd312",falseRecognitions))
        self.assertEqual(False, Utils.reject("abc5432",falseRecognitions))
        self.assertEqual(False, Utils.reject("gfdd1332",falseRecognitions))
        self.assertEqual(False, Utils.reject("abc",falseRecognitions))
        self.assertEqual(False, Utils.reject("ge",falseRecognitions))
        self.assertEqual(False, Utils.reject("542",falseRecognitions))
    def test_regex_fail(self):
        self.assertEqual(True, Utils.reject("542242",falseRecognitions))
        self.assertEqual(True, Utils.reject("hdfd",falseRecognitions))
        self.assertEqual(True, Utils.reject("sd9023kjsd",falseRecognitions))
        self.assertEqual(True, Utils.reject("453dfs",falseRecognitions))
        self.assertEqual(True, Utils.reject("342d3",falseRecognitions))
    def test_editDistance(self):
        self.assertEqual(True, Utils.reject("eur",falseRecognitions))
        self.assertEqual(True, Utils.reject("islamabad",falseRecognitions))
        self.assertEqual(True, Utils.reject("sindth",falseRecognitions))
        self.assertEqual(True, Utils.reject("karachi",falseRecognitions))
        self.assertEqual(True, Utils.reject("rawalpindi",falseRecognitions))


if __name__ == "__main__":
    unittest.main()
