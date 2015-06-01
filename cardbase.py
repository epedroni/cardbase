#!/usr/bin/env python3

import requests
import sys
from lxml import html

database = ""

def parseInput(raw):
    if raw == "help":
        print("Need help? too bad")
    elif raw == "

def main(args):

    try:
        database = open(args[1], "w")
    except:
        print("Please provide a valid database file as the first argument.")
        sys.exit(1)

    print("Welcome to cardbase")
    print("For a list of commands, type \"help\"")

    exit = False
    while(not exit):
        try:
            raw = input("> ")
            parseInput(raw)
        except:
            exit = True   


# The entry point
if __name__ == "__main__":
    main(sys.argv)

