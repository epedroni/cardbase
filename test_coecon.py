#!/usr/bin/env python3

import unittest
from lxml import html
import cardparser

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("testcards/coecon", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardparser.getTitle(self.page), "Coerced Confession")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardparser.getCost(self.page), "4{U/B}")
        
    def test_correctConvertedCostIsParsed(self):
        self.assertEqual(cardparser.getConvertedCost(self.page), "5")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardparser.getColour(self.page), "UB")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardparser.getType(self.page), "Sorcery")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardparser.getSubType(self.page), "")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardparser.getArtist(self.page), "Mathias Kollros")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardparser.getText(self.page), ["Target player puts the top four cards of his or her library into his or her graveyard. You draw a card for each creature card put into that graveyard this way."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardparser.getFlavour(self.page), "\"Ask the right questions in the right way and truth is inevitable.\" —Lazav")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardparser.getRarity(self.page), "Uncommon")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardparser.getPower(self.page), "")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardparser.getToughness(self.page), "")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardparser.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardparser.fetchCard("gtc", "217")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "gtc")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/gtc/217.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "217")

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
