#!/usr/bin/env python3

import sys
import unittest
import cardbase
from lxml import html


class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        # but actually, use the pre-fetched file to avoid querying the server too much
        with open("testcards/island", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardbase.getTitle(self.page), "Island")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardbase.getCost(self.page), "")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardbase.getColour(self.page), "")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardbase.getType(self.page), "Basic Land")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardbase.getSubType(self.page), "Island")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardbase.getArtist(self.page), "Florian de Gesincourt")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardbase.getText(self.page), ["({T}: Add {U} to your mana pool.)"])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardbase.getFlavour(self.page), "")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardbase.getRarity(self.page), "Land")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardbase.getPower(self.page), "")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardbase.getToughness(self.page), "")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardbase.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardbase.fetchCard("dtk", "253")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "dtk")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/dtk/253.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "253")

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
