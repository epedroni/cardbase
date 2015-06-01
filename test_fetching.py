#!/usr/bin/env python3

import unittest
import cardparser

class Test_cardPageFetching(unittest.TestCase):

    # Tests
    def test_correctUrlIsBuilt(self):
        self.assertEqual(cardparser.makeUrl("set", "number"), "http://magiccards.info/set/en/number.html")
    
def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
