package eu.equalparts.cardbase.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.data.CardBaseManager;
import eu.equalparts.cardbase.data.CardSet;
import eu.equalparts.cardbase.data.MetaCardSet;
import eu.equalparts.cardbase.query.IO;

/**
 * This provides a lightweight CLI for interacting with cardbase files. 
 * 
 */
public class CardbaseCLI {

	private enum LastAction {
		ADD, REMOVE;
		public Integer count;
		public Card card;
		
		public void set(Card card, Integer count) {
			this.card = card;
			this.count = count;
		}
		
	}
	private static LastAction lastAction = null;
	
	private static CardSet selectedSet = null;
	private static HashMap<String, CardSet> setCache = new HashMap<String, CardSet>();
	private static CardBaseManager cbm;
	private static boolean exit = false;
	private static String help = "No help file was found";
	private static File cardBaseFile = null;
	private static boolean savePrompt = false;

	/**
	 * Execute the interface.
	 * 
	 * @param args the first argument is the cardbase file. Further arguments are ignored.
	 */
	public static void main(String... args) {

		System.out.println("Welcome to cardbase");

		try {
			// construct the cardbase
			if (args.length > 0) {
				cardBaseFile = new File(args[0]);
				if (cardBaseFile.exists() && cardBaseFile.isFile() && cardBaseFile.canRead()) {
					System.out.println("Loading cardbase from " + args[0]);
					cbm = new CardBaseManager(cardBaseFile);
				} else {
					System.out.println(args[0] + " appears to be invalid");
					System.exit(0);
				}
			} else {
				System.out.println("No cardbase file was provided, initialising a clean cardbase");
				cbm = new CardBaseManager();
			}

			// initialise necessary components
			System.out.println("Fetching card sets from upstream");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Loading externals");
			InputStream is = CardbaseCLI.class.getResourceAsStream("/help");
			if (is != null) {
				help = new Scanner(is).useDelimiter("\\Z").next();
			} else {
				System.out.println("Help file is not available, I hope you know how to use the program!");
			}
			
			while (!exit) {
				System.out.print(selectedSet == null ? "> " : selectedSet.code + " > ");
				String rawInput = br.readLine().trim().toLowerCase();
				String[] commands = rawInput.split("[ \t]+");

				parse(commands);
			}

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void parse(String[] commands) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
		if (commands.length > 0) {
			switch (commands[0]) {
			/*
			 * Show help
			 */
			case "help":
				System.out.println(help);
				break;
			
			/*
			 * Write current CardBase to file
			 */
			case "write":
				File output;
				
				if (commands.length > 1) {
					output = new File(sanitiseFileName(commands[1]));
				} else {
					output = cardBaseFile;
				}
				
				if (output != null) {
					if (output.exists() && (!output.isFile() || !output.canWrite())) {
						System.out.println("Could not write to " + output.getAbsolutePath());
						return;
					}
					
					IO.writeCardBase(output, cbm.cardBase);
					cardBaseFile = output;
					System.out.println("Cardbase was saved to " + output.getAbsolutePath());
					savePrompt = false;
				} else {
					System.out.println("Please provide a file name");
				}
				break;

			/*
			 * Exit procedures
			 */
			case "exit":
				if (savePrompt) {
					System.out.println("Don't forget to save. If you really wish to quit without saving, type exit again.");
					savePrompt = false;
				} else {
					exit = true;
				}
				break;

			/*
			 * Print a list of valid set codes
			 */
			case "sets":
				for (MetaCardSet mcs : cbm.getAllMetaSets()) {
					System.out.println(mcs);
				}
				break;

			/*
			 * Select a set, any card numbers provided will be fetched from that set
			 */
			case "set":
				for (MetaCardSet mcs : cbm.getAllMetaSets()) {
					if (mcs.code.equalsIgnoreCase(commands[1])) {
						// if the set is cached, use that
						if (setCache.containsKey(mcs.code)) {
							selectedSet = setCache.get(mcs.code);
						} else {
							selectedSet = IO.getCardSet(mcs.code);
							setCache.put(mcs.code, selectedSet);
						}
						System.out.println("Selected set: " + mcs.name);
						lastAction = null;
						return;
					}
				}
				System.out.println(commands[1] + " does not correspond to any set (use \"sets\" to see all valid set codes)");
				break;
				
			/*
			 * Print a brief list of the complete cardbase.
			 */
			case "glance":
				Card current;
				int total = 0;
				for (Iterator<Card> i = cbm.cardIterator(); i.hasNext();) {
					current = i.next();
					printGlance(current);
					total += current.count;
				}
				System.out.println("Total: " + total);
				break;
			
			/*
			 * Print a detailed information of a single card or the whole cardbase.
			 */
			case "peruse":
				if (commands.length > 1) {
					if (selectedSet != null) {
						Card card = cbm.getCard(selectedSet.code, commands[1]);
						if (card != null) {
							printPerusal(card);
						} else {
							System.out.println("Card not in cardbase");
						}
					} else {
						System.out.println("Please select a set before perusing a specific card");
					}
				} else {
					for (Iterator<Card> i = cbm.cardIterator(); i.hasNext();) {
						printPerusal(i.next());
					}
				}
				break;
				
			/*
			 * Undo previous action.
			 */
			case "undo":
				if (lastAction != null) {
					if (lastAction == LastAction.ADD) {
						remove(lastAction.card, lastAction.count);
					} else if (lastAction ==  LastAction.REMOVE) {
						add(lastAction.card, lastAction.count);
					}
					lastAction = null;
				} else {
					System.out.println("Nothing to undo");
				}
				break;
				
			/*
			 * Remove one or more cards
			 */
			case "remove":
				if (selectedSet != null) {
					if (commands.length > 1) {
						Card remove = selectedSet.getCardByNumber(commands[1]);
						if (remove != null) {
							Integer count = 1;
							if (commands.length > 2 && commands[2].matches("[0-9]+")) {
								count = Integer.valueOf(commands[2]);
								if (count <= 0) {
									System.out.println("Can't remove " + count + " cards");
									return;
								}
							}
							remove(remove, count);
						} else {
							System.out.println(commands[1] + " does not correspond to a card in " + selectedSet.name);
						}
					} else {
						System.out.println("Please specify a card number to remove");
					}
				} else {
					System.out.println("Select a set before removing cards.");
				}
				break;
				
			/*
			 * Add one or more cards
			 */
			default:
				if (selectedSet != null) {
					if (commands.length == 1 && commands[0].isEmpty()) {
						if (lastAction == LastAction.ADD)
							add(lastAction.card, lastAction.count);
					} else {
						Card newCard = selectedSet.getCardByNumber(commands[0]);
						if (newCard != null) {
							Integer count = 1;
							if (commands.length > 1 && commands[1].matches("[0-9]+")) {
								count = Integer.valueOf(commands[1]);
								if (count <= 0) {
									System.out.println("Can't add " + count + " cards");
									return;
								}
							}
							add(newCard, count);
						} else {
							System.out.println(commands[0] + " does not correspond to a card in " + selectedSet.name);
						}
					}
				} else {
					System.out.println("Select a set before adding cards.");
				}
				break;
			}
		}
	}
	
	private static void add(Card card, Integer count) {
		card.setCode = selectedSet.code;
		cbm.addCard(card, count);
		System.out.println("Added " + count + "x " + card.name);
		savePrompt = true;
		lastAction = LastAction.ADD;
		lastAction.set(card, count);
	}
	
	private static void remove(Card card, Integer count) {
		cbm.removeCard(card, count);
		System.out.println("Removed " + count + "x " + card.name);
		savePrompt = true;
		lastAction = LastAction.REMOVE;
		lastAction.set(card, count);
	}
	
	private static String sanitiseFileName(String name) {
		name = name.replaceAll("[^-_.A-Za-z0-9]", "");
		if (!name.endsWith(".cb")) {
			name = name.concat(".cb");
		}
		return name;
	}

	private static void printPerusal(Card card) {
		printGlance(card);
		if (card.type != null) System.out.println("\t" + card.type);
		if (card.manaCost != null) System.out.println("\tCost: " + card.manaCost);
		if (card.power != null && card.toughness != null) System.out.println("\t" + card.power + "/" + card.toughness);
		if (card.loyalty != null) System.out.println("\tLoyalty: " + card.loyalty);
		
		if (card.text != null) System.out.println("\t" + card.text.replaceAll("\n", "\n\t"));
		if (card.flavor != null) System.out.println("\t" + card.flavor.replaceAll("\n", "\n\t"));
		
		if (card.rarity != null) System.out.println("\t" + card.rarity);
		if (card.multiverseid != null) System.out.println("\tMID: " + card.multiverseid);
		if (card.artist != null) System.out.println("\tIllus. " + card.artist);
	}
	
	private static void printGlance(Card card) {
		System.out.println(String.format("%1$-4d %2$s (%3$s, %4$s)", card.count, card.name, card.setCode, card.number));
	}
}
