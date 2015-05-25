#!/usr/bin/env python3

import unittest
import cardbase

class Test_cardClass(unittest.TestCase):
    
    def setUp(self):
        self.card = cardbase.Card()

    # Tests
    def test_cardHasTitle(self):
        self.assertIsNotNone(self.card.title)
        
    def test_cardHasCost(self):
        self.assertIsNotNone(self.card.cost)
        
    def test_cardHasColour(self):
        self.assertIsNotNone(self.card.colour)
        
    def test_cardHasType(self):
        self.assertIsNotNone(self.card.type)
        
    def test_cardHasSubType(self):
        self.assertIsNotNone(self.card.subtype)
        
    def test_cardHasEdition(self):
        self.assertIsNotNone(self.card.edition)
        
    def test_cardHasScan(self):
        self.assertIsNotNone(self.card.scan)
    
    def test_cardHasArtist(self):
        self.assertIsNotNone(self.card.artist)
        
    def test_cardHasText(self):
        self.assertIsNotNone(self.card.text)
        
    def test_cardHasFlavour(self):
        self.assertIsNotNone(self.card.flavour)
    
    def test_cardHasRarity(self):
        self.assertIsNotNone(self.card.rarity)
        
    def test_cardHasNumber(self):
        self.assertIsNotNone(self.card.number)
        
    def test_cardHasPower(self):
        self.assertIsNotNone(self.card.power)
        
    def test_cardHasToughness(self):
        self.assertIsNotNone(self.card.toughness)
        
    def test_cardHasLoyalty(self):
        self.assertIsNotNone(self.card.loyalty)

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
