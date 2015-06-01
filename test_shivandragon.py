#!/usr/bin/env python3

import unittest
from lxml import html
import cardparser

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("testcards/shivandragon", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardparser.getTitle(self.page), "Shivan Dragon")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardparser.getCost(self.page), "4RR")
        
    def test_correctConvertedCostIsParsed(self):
        self.assertEqual(cardparser.getConvertedCost(self.page), "6")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardparser.getColour(self.page), "R")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardparser.getType(self.page), "Creature")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardparser.getSubType(self.page), "Dragon")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardparser.getArtist(self.page), "Donato Giancola")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardparser.getText(self.page), ["Flying (This creature can't be blocked except by creatures with flying or reach.)", "{R}: Shivan Dragon gets +1/+0 until end of turn."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardparser.getFlavour(self.page), "The undisputed master of the mountains of Shiv.")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardparser.getRarity(self.page), "Rare")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardparser.getPower(self.page), "5")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardparser.getToughness(self.page), "5")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardparser.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardparser.fetchCard("m15", "281")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "m15")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/m15/281.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "281")

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
