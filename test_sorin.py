#!/usr/bin/env python3

import sys
import unittest
import cardbase
from lxml import html


class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        with open("sorin", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardbase.getTitle(self.page), "Sorin Markov")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardbase.getCost(self.page), "3BBB")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardbase.getColour(self.page), "B")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardbase.getType(self.page), "Planeswalker")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardbase.getSubType(self.page), "Sorin")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardbase.getArtist(self.page), "Michael Komarck")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardbase.getText(self.page), ["+2: Sorin Markov deals 2 damage to target creature or player and you gain 2 life.", "−3: Target opponent's life total becomes 10.", "−7: You control target player during that player's next turn."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardbase.getFlavour(self.page), "")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardbase.getRarity(self.page), "Mythic Rare")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardbase.getPower(self.page), "")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardbase.getToughness(self.page), "")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardbase.getLoyalty(self.page), "4")

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
