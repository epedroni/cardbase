#!/usr/bin/env python3

import unittest
from lxml import html
import cardparser

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("testcards/island", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardparser.getTitle(self.page), "Island")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardparser.getCost(self.page), "")
        
    def test_correctConvertedCostIsParsed(self):
        self.assertEqual(cardparser.getConvertedCost(self.page), "")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardparser.getColour(self.page), "")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardparser.getType(self.page), "Basic Land")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardparser.getSubType(self.page), "Island")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardparser.getArtist(self.page), "Florian de Gesincourt")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardparser.getText(self.page), ["({T}: Add {U} to your mana pool.)"])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardparser.getFlavour(self.page), "")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardparser.getRarity(self.page), "Land")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardparser.getPower(self.page), "")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardparser.getToughness(self.page), "")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardparser.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardparser.fetchCard("dtk", "253")
    
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
