#!/usr/bin/env python3

import sys
import unittest
import cardbase
from lxml import html


class Test_cardInformationParsing(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        # fetch shivan dragon info by the card's collector number (281 in M15)
        # cls.page = html.fromstring(requests.get("http://magiccards.info/m15/en/281.html").text)
        
        # but actually, use the pre-fetched file to avoid querying the server too much
        with open("shivandragon", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardbase.getTitle(self.page), "Shivan Dragon")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardbase.getCost(self.page), "4RR")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardbase.getColour(self.page), "R")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardbase.getType(self.page), "Creature")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardbase.getSubType(self.page), "Dragon")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardbase.getArtist(self.page), "Donato Giancola")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardbase.getText(self.page), ["Flying (This creature can't be blocked except by creatures with flying or reach.)", "{R}: Shivan Dragon gets +1/+0 until end of turn."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardbase.getFlavour(self.page), "The undisputed master of the mountains of Shiv.")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardbase.getRarity(self.page), "Rare")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardbase.getPower(self.page), "5")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardbase.getToughness(self.page), "5")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardbase.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardbase.fetchCard("m15", "281")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "m15")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/m15/281.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "281")
        

class Test_cardPageFetching(unittest.TestCase):

    # Tests
    def test_correctUrlIsBuilt(self):
        self.assertEqual(cardbase.makeUrl("m15", "281"), "http://magiccards.info/m15/en/281.html")

def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
