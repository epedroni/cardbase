#!/usr/bin/env python3

import unittest
from lxml import html
import cardparser

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("testcards/sorin", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardparser.getTitle(self.page), "Sorin Markov")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardparser.getCost(self.page), "3BBB")
        
    def test_correctConvertedCostIsParsed(self):
        self.assertEqual(cardparser.getConvertedCost(self.page), "6")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardparser.getColour(self.page), "B")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardparser.getType(self.page), "Planeswalker")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardparser.getSubType(self.page), "Sorin")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardparser.getArtist(self.page), "Michael Komarck")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardparser.getText(self.page), ["+2: Sorin Markov deals 2 damage to target creature or player and you gain 2 life.", "−3: Target opponent's life total becomes 10.", "−7: You control target player during that player's next turn."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardparser.getFlavour(self.page), "")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardparser.getRarity(self.page), "Mythic Rare")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardparser.getPower(self.page), "")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardparser.getToughness(self.page), "")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardparser.getLoyalty(self.page), "4")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardparser.fetchCard("m12", "109")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "m12")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/m12/109.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "109")
        

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
