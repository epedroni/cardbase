import re
import requests
from lxml import html

class Card():
    def __init__(self):
        self.title = ""
        self.cost = ""
        self.convertedCost = ""
        self.colour = ""
        self.type = ""
        self.subtype = ""
        self.edition = ""
        self.scan = ""
        self.artist = ""
        self.text = ""
        self.flavour = ""
        self.rarity = ""
        self.number = ""
        self.power = ""
        self.toughness = ""
        self.loyalty = ""

class CardNotFoundException(Exception):
    pass

# fetching functions
def makeUrl(cardSet, cardNo):
    return "http://magiccards.info/" + cardSet + "/en/" + cardNo + ".html"

def remoteFetch(url):
    return html.fromstring(requests.get(url).text)

def isValid(page):
    notFound = page.xpath("/html/body/h1/text()")
    response404 = page.xpath("/html/body/h1/text()")
    if notFound:
        raise CardNotFoundException()

def setRemoteData(card, url):
    # fetch card from upstream
    page = html.fromstring(requests.get(url).text)
    isValid(page)
    
    # parse and set data
    card.title = getTitle(page)
    card.cost = getCost(page)
    card.convertedCost = getConvertedCost(page)
    card.colour = getColour(page)
    card.type = getType(page)
    card.subtype = getSubType(page)
    card.artist = getArtist(page)
    card.text = getText(page)
    card.flavour = getFlavour(page)
    card.rarity = getRarity(page)
    card.power = getPower(page)
    card.toughness = getToughness(page)
    card.loyalty = getLoyalty(page)


def fetchCard(cardSet, cardNo):
    # build object
    card = Card()
    card.edition = cardSet
    card.scan = "http://magiccards.info/scans/en/" + cardSet + "/" + cardNo + ".jpg"
    card.number = cardNo
    
    setRemoteData(card, makeUrl(cardSet, cardNo))
    
    return card

# parsing functions
def getTitle(page):
    return page.xpath("/html/body/table[3]/tr/td[2]/span/a/text()")[0]

def extractSubTitle(page):
    line = page.xpath("/html/body/table[3]/tr/td[2]/p[1]/text()")[0]
    line = re.sub("\n", "", line)
    line = re.sub(" +", " ", line)
    line = line.strip()
    
    return line

def getCost(page):
    cost = extractSubTitle(page)
    cost = re.search(" ([0-9X]*[WGRBU\{\}/]*) ", cost)
    
    if cost:
        return cost.group(1)
    else:
        return ""

def getConvertedCost(page):
    cost = extractSubTitle(page)
    cost = re.search("\(([0-9+])\)", cost)
    
    if cost:
        return cost.group(1)
    else:
        return ""

def getColour(page):
    colours = extractSubTitle(page)
    colours = re.search(" [0-9X]*([WGRBU\{\}/]*) ", colours)
    
    if colours:
        colours = colours.group(1)
        colours = re.sub("[\{\}/]*", "", colours)
        colours = re.sub(r"(.)\1+", r"\1", colours)
        
        return colours
    else:
        return ""
    
def getType(page):
    types = extractSubTitle(page)
    types = re.search("([A-Za-z ]*)( —)?", types).group(1).strip()
    
    return types

def getSubType(page):
    subtypes = extractSubTitle(page)
    subtypes = re.search("— ([A-Za-z ]*)", subtypes)
    
    if subtypes:
        return subtypes.group(1).strip()
    else:
        return ""

def getArtist(page):
    artist = page.xpath("/html/body/table[3]/tr/td[2]/p[4]/text()")[0]
    artist = re.sub("Illus. ", "", artist)
    
    return artist
    
def getText(page):
    text = page.xpath("/html/body/table[3]/tr/td[2]/p[2]/b/text()")
    
    return text
    
def getFlavour(page):
    flavour = page.xpath("/html/body/table[3]/tr/td[2]/p[3]/i/text()")
    if flavour:
        flavour = re.sub("\n", "", " ".join(flavour))
        return flavour
    else:
        return ""
    
def getRarity(page):
    rarity = page.xpath("/html/body/table[3]/tr/td[3]/small/b[2]/text()")[0]
    rarity = re.search("\(([A-Za-z ]*)\)", rarity).group(1)
    
    return rarity
    
def getPower(page):
    power = extractSubTitle(page)
    power = re.search("([0-9X\*]+)/[0-9X\*]+", power)
    
    if power:
        return power.group(1)
    else:
        return ""
    
def getToughness(page):
    toughness = extractSubTitle(page)
    toughness = re.search("[0-9X\*]+/([0-9X\*]+)", toughness)
    
    if toughness:
        return toughness.group(1)
    else:
        return ""
    
def getLoyalty(page):
    loyalty = extractSubTitle(page)
    loyalty = re.search("\(Loyalty: ([0-9X*]+)\)", loyalty)
    
    if loyalty:
        return loyalty.group(1)
    else:
        return ""
