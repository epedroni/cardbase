#!/usr/bin/env python3

import unittest
from lxml import html
import cardparser

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("testcards/nightmare", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardparser.getTitle(self.page), "Nightmare")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardparser.getCost(self.page), "5B")
        
    def test_correctConvertedCostIsParsed(self):
        self.assertEqual(cardparser.getConvertedCost(self.page), "6")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardparser.getColour(self.page), "B")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardparser.getType(self.page), "Creature")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardparser.getSubType(self.page), "Nightmare Horse")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardparser.getArtist(self.page), "Vance Kovacs")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardparser.getText(self.page), ["Flying (This creature can't be blocked except by creatures with flying or reach.)", "Nightmare's power and toughness are each equal to the number of Swamps you control."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardparser.getFlavour(self.page), "The thunder of its hooves beats dreams into despair.")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardparser.getRarity(self.page), "Rare")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardparser.getPower(self.page), "*")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardparser.getToughness(self.page), "*")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardparser.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardparser.fetchCard("m15", "276")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "m15")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/m15/276.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "276")
        
def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
