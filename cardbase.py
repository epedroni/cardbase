#!/usr/bin/env python3

import cardparser
import requests
import sys
from lxml import html
import yaml
import re

def exit(msg=""):
    if msg != "":
        print(msg)
    
    #database.close()

    sys.exit()

def main(args):

    try:
        dataFile = args[1]
        #database = open(args[1], "w")
    except Exception as e:
        print("Please provide a valid database file as the first argument.")
        exit(e)

    print("Welcome to cardbase")
    print("For a list of commands, type \"help\"")

    globalSet = ""

    while(True):
        #try:
            raw = input("(" + globalSet + ")> ").strip()
            args = re.split("[\t ]+", raw)
            
            if args[0] == "help":
                print("Need help? try google.com")
                
            elif args[0] == "exit":
                exit()
                
            elif args[0] == "set":
                if args[1] and args[1] != "":
                    globalSet = re.sub("[^0-9A-Za-z]", "", args[1])
                else:
                    globalSet = ""
            
            elif args[0] == "save":
                if args[1] and args[1] != "":
                    dataFile = args[1]
                
            elif args[0]:
                if globalSet != "":
                    cardNo = re.sub("[^0-9A-Za-z]", "", args[0])
                    print("Fetching card " + cardNo)
                    try:
                        pass
                        newCard = cardparser.fetchCard(globalSet, args[0])
                        print(newCard.title)
                    except cardparser.CardNotFoundException as e:
                        print("Card not found.")
                else:
                    print("Select a set with the \"set\" command before adding cards.")
               
            else:
                print("Invalid input")
                
        #except Exception as e:
        #    exit(e)


# The entry point
if __name__ == "__main__":
    main(sys.argv)

