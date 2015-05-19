#!/usr/bin/env python3

import sys
import unittest

class Test_testSomething(unittest.TestCase):
    
    def setUp(self):
        pass
    
    def tearDown(self):
        pass
    
    def test_something(self):
        pass

def test():

    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
