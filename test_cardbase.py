#!/usr/bin/env python3

import sys
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
        
    def test_cardHasArt(self):
        self.assertIsNotNone(self.card.art)
    
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
        
    def test_cardHasFoil(self):
        self.assertIsNotNone(self.card.foil)
        
    def test_cardHasLoyalty(self):
        self.assertIsNotNone(self.card.loyalty)

class Test_cardInformationIsFetched(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        # fetch shivan dragon info by the card's collector number (281 in M15)
        cls.card = cardbase.fetchCard("m15", 281)

    # Tests
    def test_somethingIsFetched(self):
        self.assertIsInstance(self.card, cardbase.Card)
    
    def test_correctTitleIsFetched(self):
        self.assertEqual(self.card.title, "Shivan Dragon")

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
