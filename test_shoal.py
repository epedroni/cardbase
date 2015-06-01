#!/usr/bin/env python3

import unittest
from lxml import html
import cardparser

class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("testcards/shoal", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardparser.getTitle(self.page), "Disrupting Shoal")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardparser.getCost(self.page), "XUU")
        
    def test_correctConvertedCostIsParsed(self):
        self.assertEqual(cardparser.getConvertedCost(self.page), "2")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardparser.getColour(self.page), "U")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardparser.getType(self.page), "Instant")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardparser.getSubType(self.page), "Arcane")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardparser.getArtist(self.page), "Scott M. Fischer")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardparser.getText(self.page), ["You may exile a blue card with converted mana cost X from your hand rather than pay Disrupting Shoal's mana cost.", "Counter target spell if its converted mana cost is X."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardparser.getFlavour(self.page), "")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardparser.getRarity(self.page), "Rare")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardparser.getPower(self.page), "")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardparser.getToughness(self.page), "")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardparser.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardparser.fetchCard("bok", "33")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "bok")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/bok/33.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "33")
        

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
