#!/usr/bin/env python3

import unittest
from lxml import html
import cardparser

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("testcards/callow", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardparser.getTitle(self.page), "Callow Jushi")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardparser.getCost(self.page), "1UU")
        
    def test_correctConvertedCostIsParsed(self):
        self.assertEqual(cardparser.getConvertedCost(self.page), "3")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardparser.getColour(self.page), "U")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardparser.getType(self.page), "Creature")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardparser.getSubType(self.page), "Human Wizard")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardparser.getArtist(self.page), "Tsutomu Kawade")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardparser.getText(self.page), ["Whenever you cast a Spirit or Arcane spell, you may put a ki counter on Callow Jushi.", "At the beginning of the end step, if there are two or more ki counters on Callow Jushi, you may flip it."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardparser.getFlavour(self.page), "")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardparser.getRarity(self.page), "Uncommon")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardparser.getPower(self.page), "2")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardparser.getToughness(self.page), "2")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardparser.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardparser.fetchCard("bok", "31a")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "bok")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/bok/31a.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "31a")
        

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
