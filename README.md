# cardbase

[![Build Status](https://travis-ci.org/epedroni/cardbase.svg)](https://travis-ci.org/epedroni/cardbase)

The primary purpose of cardbase is to create a digital representation of your own MTG collection. A cardbase file (.cb) is simply a JSON file containing a list of card objects and associated unique keys. The card data structure used is derived from that used in [MTG JSON](http://mtgjson.com/), and in fact that is where most of the data comes from right now.

## Command Line Interface (CLI) - cardbasecli

### Loading a Cardbase

To use, first make sure to load the cardbase by passing it as the first argument to the program, e.g. cardbasecli ~/mycardbase.cb. To start a new clean cardbase, run cardbasecli without arguments.


### Adding Cards

To add cards, first use "set" to enter the code of the set to which the card belongs, e.g. to add 2015 Core Set cards, enter "set M15". To see a list of all valid set codes, type "sets".
Once a set is selected, simply type the card's set number and it will be added to the collection. You may add more of the same card one at a time, or by specifying the quantity following the set number.

For example, to add 3 M15 edition Shivan Dragons, do:

    > set m15
    > 281 3

Alternatively, pressing return immediately after adding a card repeats that action. If return was pressed one more time after the example above, three more Shivan Dragons would be added. This can be repeated indefinitely, but only works with card additions.


### Removing Cards

To remove cards, first choose a set and then type "remove" followed by the card's set number. To remove more of the same card, add the desired count after the card number.

For example, to remove 2 of your 3 M15 Shivan Dragons, do:

    > set m15
    > remove 281 2

Note that it is only necessary to choose the set if it wasn't already selected.


### Undo

You may take back your last action by typing "undo". This only works for add and remove actions, and only if the set has not been changed since. Only the most recent action can be undone.


### Viewing the Cardbase

Cardbasecli also offers some very basic commands to view the current state of the cardbase: glance and peruse.

glance: prints a list showing the amount of each card in the cardbase, by name.
peruse: prints the same list as glance, but with more information about each card.

Peruse optionally accepts a card number as an argument. If present, only that card's information is printed.
 

### Committing Changes and Exiting

Any changes made on this tool must be manually written to the file. If you make a mistake, simply exit and your cardbase will not be modified. To write changes, use the "write" command. To save to a new file, type "write" followed by the file name (no spaces allowed). To exit, simply type "exit".

### Version

Use the "version" command to see the executable's version.

## Building

Cardbase now uses [gradle](https://gradle.org/) for building and dependency management. As recommended, use the provided `gradlew` script for best results.
