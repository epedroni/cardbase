#!/usr/bin/env python3

import sys
import unittest
import cardbase
from lxml import html


class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        # but actually, use the pre-fetched file to avoid querying the server too much
        with open("testcards/hydra", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardbase.getTitle(self.page), "Khalni Hydra")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardbase.getCost(self.page), "GGGGGGGG")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardbase.getColour(self.page), "G")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardbase.getType(self.page), "Creature")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardbase.getSubType(self.page), "Hydra")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardbase.getArtist(self.page), "Todd Lockwood")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardbase.getText(self.page), ["Khalni Hydra costs {G} less to cast for each green creature you control.", "Trample"])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardbase.getFlavour(self.page), "\"In ages past, bargains were struck and promises were made. Now we must collect on our debt. Begin the hymns.\" —Moruul, Khalni druid")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardbase.getRarity(self.page), "Mythic Rare")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardbase.getPower(self.page), "8")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardbase.getToughness(self.page), "8")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardbase.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardbase.fetchCard("roe", "192")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "roe")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/roe/192.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "192")
        
def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
