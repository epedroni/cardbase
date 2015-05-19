class Card():
    def __init__(self):
        self.title = ""
        self.cost = ""
        self.colour = ""
        self.type = ""
        self.subtype = ""
        self.edition = ""
        self.art = ""
        self.artist = ""
        self.text = ""
        self.flavour = ""
        self.rarity = ""
        self.number = ""
        self.power = ""
        self.toughness = ""
        self.foil = ""
        self.loyalty = ""
        
def fetchCard(cardSet, cardNo):
    card = Card()
    card.title = "Shivan Dragon"
    return card
