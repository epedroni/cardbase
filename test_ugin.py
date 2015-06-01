#!/usr/bin/env python3

import unittest
from lxml import html
import cardparser

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("testcards/ugincons", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardparser.getTitle(self.page), "Ugin's Construct")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardparser.getCost(self.page), "4")
        
    def test_correctConvertedCostIsParsed(self):
        self.assertEqual(cardparser.getConvertedCost(self.page), "4")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardparser.getColour(self.page), "")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardparser.getType(self.page), "Artifact Creature")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardparser.getSubType(self.page), "Construct")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardparser.getArtist(self.page), "Peter Mohrbacher")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardparser.getText(self.page), ["When Ugin's Construct enters the battlefield, sacrifice a permanent that's one or more colors."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardparser.getFlavour(self.page), "While trapping the Eldrazi on Zendikar, Ugin learned little from Sorin, but he gleaned the rudiments of lithomancy from Nahiri.")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardparser.getRarity(self.page), "Uncommon")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardparser.getPower(self.page), "4")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardparser.getToughness(self.page), "5")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardparser.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardparser.fetchCard("frf", "164")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "frf")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/frf/164.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "164")
        
def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
