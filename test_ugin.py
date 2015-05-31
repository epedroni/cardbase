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
        with open("testcards/ugincons", "r") as file:
            cls.page = html.fromstring(file.read())

    # Tests
    def test_correctTitleIsParsed(self):
        self.assertEqual(cardbase.getTitle(self.page), "Ugin's Construct")
    
    def test_correctCostIsParsed(self):
        self.assertEqual(cardbase.getCost(self.page), "4")
        
    def test_correctColourIsParsed(self):
        self.assertEqual(cardbase.getColour(self.page), "")
        
    def test_correctTypeIsParsed(self):
        self.assertEqual(cardbase.getType(self.page), "Artifact Creature")
        
    def test_correctSubTypeIsParsed(self):
        self.assertEqual(cardbase.getSubType(self.page), "Construct")
        
    def test_correctArtistIsParsed(self):
        self.assertEqual(cardbase.getArtist(self.page), "Peter Mohrbacher")
        
    def test_correctTextIsParsed(self):
        self.assertEqual(cardbase.getText(self.page), ["When Ugin's Construct enters the battlefield, sacrifice a permanent that's one or more colors."])
        
    def test_correctFlavourIsParsed(self):
        self.assertEqual(cardbase.getFlavour(self.page), "While trapping the Eldrazi on Zendikar, Ugin learned little from Sorin, but he gleaned the rudiments of lithomancy from Nahiri.")
        
    def test_correctRarityIsParsed(self):
        self.assertEqual(cardbase.getRarity(self.page), "Uncommon")
        
    def test_correctPowerIsParsed(self):
        self.assertEqual(cardbase.getPower(self.page), "4")

    def test_correctToughnessIsParsed(self):
        self.assertEqual(cardbase.getToughness(self.page), "5")
        
    def test_correctLoyaltyIsParsed(self):
        self.assertEqual(cardbase.getLoyalty(self.page), "")

class Test_additionalCardData(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        cls.card = cardbase.fetchCard("frf", "164")
    
    def test_cardHasCorrectEdition(self):
        self.assertEqual(self.card.edition, "frf")
    
    def test_cardHasCorrectScan(self):
        self.assertEqual(self.card.scan, "http://magiccards.info/scans/en/frf/164.jpg")

    def test_cardHasCorrectNumber(self):
        self.assertEqual(self.card.number, "164")
        
def test():
    unittest.main(exit=False)
    
# The entry point
if __name__ == "__main__":
    test()
