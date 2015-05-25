import re

class Card():
    def __init__(self):
        self.title = ""
        self.cost = ""
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
        
def fetchCard(cardSet, cardNo):
    card = Card()
    card.edition = cardSet
    card.scan = "http://magiccards.info/scans/en/" + cardSet + "/" + cardNo + ".jpg"
    card.number = cardNo
    return card

def makeUrl(cardSet, cardNo):
    return "http://magiccards.info/" + cardSet + "/en/" + cardNo + ".html"

def getTitle(page):
    return page.xpath("/html/body/table[3]/tr/td[2]/span/a/text()")[0]

def extractSubTitle(page):
    line = page.xpath("/html/body/table[3]/tr/td[2]/p[1]/text()")[0]
    line = re.sub("\n", "", line)
    line = re.sub(" +", " ", line)
    return line.strip()

def getCost(page):
    cost = extractSubTitle(page)
    
    return re.search(" ([0-9X]*[WGRBU]*) ", cost).group(1)
    
def getColour(page):
    colours = extractSubTitle(page)
    colours = re.search(" [0-9X]*([WGRBU]*) ", colours).group(1)
    
    colours = re.sub("U+", "U", colours)
    colours = re.sub("W+", "W", colours)
    colours = re.sub("R+", "R", colours)
    colours = re.sub("B+", "B", colours)
    colours = re.sub("G+", "G", colours)
    
    return colours
    
def getType(page):
    types = extractSubTitle(page)
    types = re.search("([A-Za-z ]*) —", types).group(1)
    
    return types

def getSubType(page):
    subtypes = extractSubTitle(page)
    subtypes = re.search("— ([A-Za-z ]*) ", subtypes).group(1)
    
    return subtypes

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
        return flavour[0]
    else:
        return ""
    
def getRarity(page):
    rarity = page.xpath("/html/body/table[3]/tr/td[3]/small/b[2]/text()")[0]
    rarity = re.search("\(([A-Za-z ]*)\)", rarity).group(1)
    
    return rarity
    
def getPower(page):
    power = extractSubTitle(page)
    power = re.search("([0-9X\*]+)/[0-9X\*]+", power).group(1)
    
    return power
    
def getToughness(page):
    toughness = extractSubTitle(page)
    toughness = re.search("[0-9X\*]+/([0-9X\*]+)", toughness).group(1)
    
    return toughness
    
def getLoyalty(page):
    loyalty = extractSubTitle(page)
    loyalty = re.search("\(Loyalty: ([0-9X*]+)\)", loyalty)
    
    if loyalty:
        return loyalty.group(1)
    else:
        return ""
    

