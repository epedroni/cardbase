package eu.equalparts.cardbase.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.data.CardbaseManager;
import eu.equalparts.cardbase.data.FullCardSet;
import eu.equalparts.cardbase.data.CardSet;
import eu.equalparts.cardbase.query.IO;

/**
 * This provides a lightweight CLI for interacting with cardbase files. 
 * 
 * @author Eduardo Pedroni
 */
public class CardbaseCLI {

	/**
	 * Enum type to store actions.
	 * 
	 * @author Eduardo Pedroni
	 */
	private enum Action {
		
		ADD, REMOVE;
		public Card card;
		public Integer count;
		
		/**
		 * Sets both fields at once.
		 * 
		 * @param card the card last modified.
		 * @param count the amount that was added or removed.
		 */
		public void set(Card card, Integer count) {
			this.card = card;
			this.count = count;
		}
	}
	
	/**
	 * The last action performed by the user.
	 */
	private Action lastAction = null;
	/**
	 * The currently selected set, from which new cards are added.
	 */
	private FullCardSet selectedSet = null;
	/**
	 * A cache of CardSets to avoid querying the server many times for the same information.
	 */
	private HashMap<String, FullCardSet> setCache = new HashMap<String, FullCardSet>();
	/**
	 * The manager object which provides a façade to the cardbase data structure.
	 */
	private CardbaseManager cbm;
	/**
	 * Exit flag, program breaks out of the main loop when true.
	 */
	private boolean exit = false;
	/**
	 * Printed to the console when the user enter the help command.
	 */
	private String help = "Not available, check project page.";
	/**
	 * The cardbase file off which we are currently working, if any.
	 */
	private File cardbaseFile = null;
	/**
	 * Save flag is raised when cards are added or removed and causes a prompt to be shown
	 * if the user tries to exit with unsaved changed.
	 */
	private boolean savePrompt = false;

	/**
	 * Execute the interface.
	 * 
	 * @param args the first argument is the cardbase file. Further arguments are ignored.
	 */
	public static void main(String... args) {
		try {
			new CardbaseCLI(args).run();
		} catch (JsonParseException e) {
			System.out.println("Error: poorly formatted cardbase, check the syntax and try again.");
			// although the problem could also be with the upstream CardSetList json.
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.out.println("Error: unexpected fields found in cardbase, it may be from an old version?");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error: something went wrong reading a file, abort...");
			e.printStackTrace();
		}
	}
	
	private CardbaseCLI(String... args) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Welcome to Cardbase CLI!");

		// make the CardbaseManager
		if (args.length > 0) {
			cardbaseFile = new File(args[0]);
			if (cardbaseFile.exists() && cardbaseFile.isFile() && cardbaseFile.canRead()) {
				System.out.println("Loading cardbase from \"" + args[0] + "\".");
				cbm = new CardbaseManager(cardbaseFile);
			} else {
				System.out.println(args[0] + " appears to be invalid.");
				System.exit(0);
			}
		} else {
			System.out.println("No cardbase file was provided, creating a clean cardbase.");
			cbm = new CardbaseManager();
		}

		// load help information
		InputStream is = CardbaseCLI.class.getResourceAsStream("/help");
		if (is != null) {
			help = new Scanner(is).useDelimiter("\\Z").next();
		} else {
			System.out.println("Help file was not found, check the project page for help instead.");
		}

	}

	private void run() {
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		// the main loop
		try {
			while (!exit) {
				System.out.print(selectedSet == null ? "> " : selectedSet.code + " > ");
				interpret(consoleReader.readLine().trim().toLowerCase().split("[ \t]+"));
			}
		} catch (IOException e) {
			System.out.println("Error: something went wrong with stdin, exiting...");
			e.printStackTrace();
		}
	}

	/**
	 * Handle console commands appropriately.
	 * 
	 * @param commands an array of {@code String} containing the arguments in order.
	 */
	private void interpret(String[] commands) {
		if (commands.length > 0) {
			switch (commands[0]) {
			/*
			 * Show help.
			 */
			case "help":
				System.out.println(help);
				break;
			
			/*
			 * Write current cardbase to file.
			 */
			case "write":
				File outputFile;
				if (commands.length > 1) {
					outputFile = new File(sanitiseFileName(commands[1]));
				} else {
					outputFile = cardbaseFile;
				}
				
				if (outputFile != null) {
					if (outputFile.exists() && (!outputFile.isFile() || !outputFile.canWrite())) {
						System.out.println("Could not write to \"" + outputFile.getAbsolutePath() + "\".");
						return;
					}
					// handle these exceptions locally - they don't necessarily mean the program should halt.
					try {
						IO.writeCardbase(outputFile, cbm.cardbase);
						// we are now working off outputFile, which may or may not be the same as cardbaseFile at this point
						cardbaseFile = outputFile;
						System.out.println("Cardbase was saved to \"" + outputFile.getAbsolutePath() + "\". "
								+ "Subsequent writes will be done to this same file unless otherwise requested.");
						savePrompt = false;
					} catch (JsonGenerationException | JsonMappingException e) {
						System.out.println("Error: something terrible happened to the internal cardbase data structure. Oops.");
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("Error: lost contact with the output file, try again?");
						e.printStackTrace();
					}
				} else {
					System.out.println("Please provide a file name.");
				}
				break;
				
			/*
			 * Exit procedure.
			 */
			case "exit":
				if (savePrompt) {
					System.out.println("Don't forget to save. If you really wish to quit without saving, type \"exit\" again.");
					savePrompt = false;
				} else {
					exit = true;
				}
				break;

			/*
			 * Print a list of valid set codes.
			 */
			case "sets":
				for (CardSet set : cbm.getCardSetList()) {
					// MetaCardSet has an overridden toString().
					System.out.println(set);
				}
				break;

			/*
			 * Select a set.
			 */
			case "set":
				// first check if the set code is valid
				for (CardSet set : cbm.getCardSetList()) {
					if (set.code.equalsIgnoreCase(commands[1])) {
						// if the set is already cached, use that
						if (setCache.containsKey(set.code)) {
							selectedSet = setCache.get(set.code);
						} else {
							// if not, download it and cache it
							try {
								selectedSet = IO.getCardSet(set.code);
								setCache.put(set.code, selectedSet);
							} catch (JsonParseException e) {
								System.out.println("Error: JSON fetched from upstream was not formatted properly.");
								e.printStackTrace();
								return;
							} catch (JsonMappingException e) {
								System.out.println("Error: JSON fetched from upstream does not match the data structure used internally.");
								e.printStackTrace();
								return;
							} catch (IOException e) {
								System.out.println("Error: JSON could not be fetched from upstream.");
								e.printStackTrace();
								return;
							}
						}
						System.out.println("Selected set: " + set.name + ".");
						// undoing is not allowed if the set is changed - it would get tricky
						lastAction = null;
						return;
					}
				}
				System.out.println("\"" + commands[1] + "\" does not correspond to any set (use \"sets\" to see all valid set codes).");
				break;
				
			/*
			 * Print a brief list of the whole cardbase.
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
			 * Print detailed information of a single card or the whole cardbase.
			 */
			case "peruse":
				// if a card is specified, peruse only that
				if (commands.length > 1) {
					if (selectedSet != null) {
						Card card = cbm.getCard(selectedSet.code, commands[1]);
						if (card != null) {
							printPerusal(card);
						} else {
							System.out.println("Card not in cardbase.");
						}
					} else {
						System.out.println("Please select a set before perusing a specific card.");
					}
				} else {
					// peruse all cards in cardbase
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
					if (lastAction == Action.ADD) {
						remove(lastAction.card, lastAction.count);
					} else if (lastAction ==  Action.REMOVE) {
						add(lastAction.card, lastAction.count);
					}
					// can only undo once
					lastAction = null;
				} else {
					System.out.println("Nothing to undo.");
				}
				break;
				
			/*
			 * Remove one or more of a card.
			 */
			case "remove":
				if (selectedSet != null) {
					if (commands.length > 1) {
						Card cardToRemove = selectedSet.getCardByNumber(commands[1]);
						if (cardToRemove != null) {
							Integer count = 1;
							if (commands.length > 2 && commands[2].matches("[0-9]+")) {
								count = Integer.valueOf(commands[2]);
								if (count <= 0) {
									System.out.println("Can't remove " + count + " cards.");
									return;
								}
							}
							remove(cardToRemove, count);
						} else {
							System.out.println(commands[1] + " does not correspond to a card in " + selectedSet.name + ".");
						}
					} else {
						System.out.println("Please specify a card number to remove.");
					}
				} else {
					System.out.println("Select a set before removing cards.");
				}
				break;
				
			/*
			 * Add one or more of a card.
			 */
			default:
				if (selectedSet != null) {
					// a blank line after adding a card repeats the addition unlimitedly
					if (commands.length == 1 && commands[0].isEmpty()) {
						if (lastAction == Action.ADD)
							add(lastAction.card, lastAction.count);
					} else {
						Card cardToAdd = selectedSet.getCardByNumber(commands[0]);
						if (cardToAdd != null) {
							Integer count = 1;
							if (commands.length > 1 && commands[1].matches("[0-9]+")) {
								count = Integer.valueOf(commands[1]);
								if (count <= 0) {
									System.out.println("Can't add " + count + " cards.");
									return;
								}
							}
							add(cardToAdd, count);
						} else {
							System.out.println(commands[0] + " does not correspond to a card in " + selectedSet.name + ".");
						}
					}
				} else {
					System.out.println("Select a set before adding cards.");
				}
				break;
			}
		}
	}
	
	/**
	 * Add the specified count of the specified card
	 * to the cardbase.
	 * 
	 * @param card the card to add.
	 * @param count the number of times to add it.
	 */
	private void add(Card card, Integer count) {
		// MTG JSON does not contain this information, but it is useful for sorting
		card.setCode = selectedSet.code;
		cbm.addCard(card, count);
		System.out.println("Added " + count + "x " + card.name + ".");
		savePrompt = true;
		lastAction = Action.ADD;
		lastAction.set(card, count);
	}
	
	/**
	 * Remove the specified count of the specified card
	 * from the cardbase.
	 * 
	 * @param card the card to remove.
	 * @param count the number of times to remove it.
	 */
	private void remove(Card card, Integer count) {
		cbm.removeCard(card, count);
		System.out.println("Removed " + count + "x " + card.name + ".");
		savePrompt = true;
		lastAction = Action.REMOVE;
		lastAction.set(card, count);
	}
	
	/**
	 * Return a {@code String} that is guaranteed to be a legal file name.
	 * 
	 * @param name the file name candidate to sanitise.
	 * @return the sanitised name.
	 */
	private String sanitiseFileName(String name) {
		// POSIX-compliant valid filename characters
		name = name.replaceAll("[^-_.A-Za-z0-9]", "");
		// extension is not indispensable, but good practice
		if (!name.endsWith(".cb")) {
			name = name.concat(".cb");
		}
		return name;
	}
	
	/**
	 * Prints a glance of the specified card. A glance contains simply
	 * the card count, name, set code and set number. 
	 * 
	 * @param card the card to glance.
	 */
	private void printGlance(Card card) {
		System.out.println(String.format("%1$-4d %2$s (%3$s, %4$s)", card.count, card.name, card.setCode, card.number));
	}
	
	/**
	 * Prints a perusal of the specified card. A perusal contains more
	 * information than a glance, but not every single field the card has
	 * as that would be too verbose while adding little value.
	 * 
	 * @param card the card to peruse.
	 */
	private void printPerusal(Card card) {
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
}
