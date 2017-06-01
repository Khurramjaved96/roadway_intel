from unittest import TestCase
import unittest
import Utils

class TestReject(TestCase):
    '''
    Test cases to check implementation of Text rejection function
    '''
    def test_regex_correct(self):
        self.assertEqual(False,Utils.reject("abc-123"))
        self.assertEqual(False, Utils.reject("abc123"))
        self.assertEqual(False, Utils.reject("abcd123"))
        self.assertEqual(False, Utils.reject("a-123"))
        self.assertEqual(False, Utils.reject("gd312"))
        self.assertEqual(False, Utils.reject("abc5432"))
        self.assertEqual(False, Utils.reject("gfdd1332"))
        self.assertEqual(False, Utils.reject("abc"))
        self.assertEqual(False, Utils.reject("ge"))
        self.assertEqual(False, Utils.reject("542"))
    def test_regex_fail(self):
        self.assertEqual(True, Utils.reject("542242"))
        self.assertEqual(True, Utils.reject("hdfd"))
        self.assertEqual(True, Utils.reject("sd9023kjsd"))
        self.assertEqual(True, Utils.reject("453dfs"))
        self.assertEqual(True, Utils.reject("342d3"))
    def test_editDistance(self):
        self.assertEqual(True, Utils.reject("eur"))
        self.assertEqual(True, Utils.reject("islamabad"))
        self.assertEqual(True, Utils.reject("sindth"))
        self.assertEqual(True, Utils.reject("karachi"))
        self.assertEqual(True, Utils.reject("rawalpindi"))


if __name__ == "__main__":
    unittest.main()
