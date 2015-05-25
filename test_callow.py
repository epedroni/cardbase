#!/usr/bin/env python3

import sys
import unittest
import cardbase
from lxml import html

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("callow", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardbase.getTitle(self.page), "Callow Jushi")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardbase.getCost(self.page), "1UU")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardbase.getColour(self.page), "U")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardbase.getType(self.page), "Creature")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardbase.getSubType(self.page), "Human Wizard")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardbase.getArtist(self.page), "Tsutomu Kawade")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardbase.getText(self.page), ["Whenever you cast a Spirit or Arcane spell, you may put a ki counter on Callow Jushi.", "At the beginning of the end step, if there are two or more ki counters on Callow Jushi, you may flip it."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardbase.getFlavour(self.page), "")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardbase.getRarity(self.page), "Uncommon")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardbase.getPower(self.page), "2")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardbase.getToughness(self.page), "2")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardbase.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardbase.fetchCard("bok", "31a")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "bok")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/bok/31a.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "31a")
        

class Test_cardPageFetching(unittest.TestCase):

    # Tests
    def test_correctUrlIsBuilt(self):
        self.assertEqual(cardbase.makeUrl("bok", "31a"), "http://magiccards.info/bok/en/31a.html")

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
