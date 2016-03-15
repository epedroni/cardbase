package eu.equalparts.cardbase.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.Cardbase;
import eu.equalparts.cardbase.cards.Card;
import eu.equalparts.cardbase.cards.CardSetInformation;
import eu.equalparts.cardbase.cards.FullCardSet;
import eu.equalparts.cardbase.utils.MTGUniverse;

/**
 * This provides a lightweight CLI for interacting with cardbase files. 
 * 
 * @author Eduardo Pedroni
 */
public final class CardbaseCLI {
	
	/**
	 * Enum type to store actions.
	 * 
	 * @author Eduardo Pedroni
	 */
	private enum Action {
		ADD, REMOVE;
		public Card card;
		public int count;
	}

	/**
	 * Location of the help file.
	 */
	private static final String HELP_FILE_PATH = "/help_en";
	/**
	 * Program version.
	 */
	private static final String VERSION = "1.0";
	/**
	 * This is the default remote URL from where card data is queried.
	 */
	private static final String REMOTE_URL = "http://mtgjson.com/json/";
	/**
	 * The last action performed by the user.
	 */
	private Action lastAction = null;
	/**
	 * This is the universe of MTG cards. The URL to query from
	 * is by default {@code REMOTE_URL}, but it can be overridden
	 * in the default constructor.
	 */
	private MTGUniverse mtgUniverse;
	/**
	 * The currently selected set, from which new cards are added.
	 */
	private FullCardSet selectedSet = null;
	/**
	 * The actual cardbase being interfaced with.
	 */
	private Cardbase cardbase;
	/**
	 * Printed to the console when the user enters the help command.
	 */
	private String help = "Not available, check project page on GitHub.";
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
	 * Exit flag, program breaks out of the main loop when true.
	 */
	private boolean exit = false;

	/**
	 * Execute the interface.
	 * 
	 * @param args the first argument is the cardbase file. Further arguments are ignored.
	 */
	public static void main(String... args) {
		try {
			new CardbaseCLI(REMOTE_URL, args).startInterface();
		} catch (JsonParseException e) {
			System.out.println("Error: poorly formatted cardbase, check the syntax and try again.");
			// although the problem could also be with the upstream CardSetList json.
			if (Cardbase.DEBUG) e.printStackTrace();
			System.exit(1);
		} catch (JsonMappingException e) {
			System.out.println("Error: unexpected fields found in cardbase, is it from an old version?");
			if (Cardbase.DEBUG) e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Error: something went wrong reading cardbase file, abort...");
			if (Cardbase.DEBUG) e.printStackTrace();
			System.exit(1);
		} catch (IllegalArgumentException e) {
			System.out.println("Error: the file provided is invalid.");
			if (Cardbase.DEBUG) e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Reads in an optional cardbase JSON and initialises other necessary components.
	 * This does not actually produce the CLI, for that use {@code startInterface()}
	 * on the constructed object.
	 * 
	 * @param remoteURL the remote URL used to query for card and set data.
	 * @param args a list of arguments. Only the first argument is used, as a cardbase JSON.
	 * @throws IOException if something goes wrong while reading the provided file.
	 * @throws JsonMappingException if the provided json did not correspond to the expected format.
	 * @throws JsonParseException if the provided file did not contain valid json.
	 */
	CardbaseCLI(String remoteURL, String... args) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Welcome to Cardbase CLI!");

		// set debug flag if we are debugging
		if (Cardbase.DEBUG) System.out.println("Debug mode is on.");

		// initialise the universe
		mtgUniverse = new MTGUniverse(remoteURL);
		
		// make the Cardbase
		if (args != null && args.length > 0 && !args[0].isEmpty()) {
			cardbaseFile = new File(args[0]);
			if (cardbaseFile.exists() && cardbaseFile.isFile() && cardbaseFile.canRead()) {
				System.out.println("Loading cardbase from \"" + args[0] + "\".");
				cardbase = new Cardbase(cardbaseFile);
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			System.out.println("No cardbase file was provided, creating a clean cardbase.");
			cardbase = new Cardbase();
		}

		// load help information
		InputStream is = CardbaseCLI.class.getResourceAsStream(HELP_FILE_PATH);
		if (is != null) {
			help = new Scanner(is).useDelimiter("\\Z").next();
		} else {
			System.out.println("Help file was not found, check the project page on GitHub for help instead.");
		}
	}
	
	/**
	 * Properly sanitise user input.
	 * 
	 * @param input the raw input from the user.
	 * @return an array of strings, where the first element is the command and subsequent elements are the arguments.
	 */
	private String[] sanitiseInput(String input) {
		return input.trim().split("[ \t]+");
	}

	/**
	 * Read stdin for user input, sanitise and interpret any commands entered.
	 */
	private void startInterface() {
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			// the main loop
			while (!exit) {
				// print prompt
				System.out.print(selectedSet == null ? "> " : selectedSet.code + " > ");
				// interpret input
				interpretInput(consoleReader.readLine());
			}
		} catch (IOException e) {
			System.out.println("Error: something went wrong with stdin, exiting...");
			if (Cardbase.DEBUG) e.printStackTrace();
		}
	}
	
	/**
	 * Sanitises and interprets raw input as it is read from stdin. This method has default-visibility for unit testing.
	 * 
	 * @param rawInput
	 */
	void interpretInput(String rawInput) {
		// condition input
		String[] input = sanitiseInput(rawInput);
		String command = input[0];
		String[] args = Arrays.copyOfRange(input, 1, input.length);

		// interpret
		if (command.equalsIgnoreCase("help")) {
			help();
		} else if (command.equalsIgnoreCase("write")
				|| command.equalsIgnoreCase("save")) {
			write(args);
		} else if (command.equalsIgnoreCase("version")) {
			version();
		} else if (command.equalsIgnoreCase("exit")) {
			exit();
		} else if (command.equalsIgnoreCase("sets")) {
			sets();
		} else if (command.equalsIgnoreCase("set")) {
			set(args);
		} else if (command.equalsIgnoreCase("glance")) {
			glance();
		} else if (command.equalsIgnoreCase("peruse")) {
			peruse(args);
		} else if (command.equalsIgnoreCase("undo")) {
			undo();
		} else if (command.equalsIgnoreCase("remove") 
				|| command.equalsIgnoreCase("rm")) {
			remove(args);
		} else {
			add(command, args);
		}
	}

	/**
	 * Print help to console.
	 */
	private void help() {
		System.out.println(help);
	}

	/**
	 * Write current cardbase to file.
	 *
	 * @param args optionally the file to which to write.
	 */
	private void write(String... args) {
		File outputFile;
		// user-provided file overrides everything else
		if (args != null && args.length > 0) {
			outputFile = new File(sanitiseFileName(args[0]));
		} else {
			outputFile = cardbaseFile;
		}

		if (outputFile != null) {
			if (outputFile.exists() && (!outputFile.isFile() || !outputFile.canWrite())) {
				System.out.println("Could not write to \"" + outputFile.getAbsolutePath() + "\".");
			} else {
				// handle these exceptions locally - they don't necessarily mean the program should exit
				try {
					cardbase.writeCollection(outputFile);
					// we are now working off outputFile, which may or may not be the same as cardbaseFile at this point
					cardbaseFile = outputFile;
					System.out.println("Cardbase was saved to \"" + outputFile.getAbsolutePath() + "\". "
							+ "Subsequent writes will be done to this same file unless otherwise requested.");
					savePrompt = false;
				} catch (JsonGenerationException | JsonMappingException e) {
					System.out.println("Error: something terrible happened to the internal cardbase data structure. Oops.");
					if (Cardbase.DEBUG) e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Error: lost contact with the output file.");
					if (Cardbase.DEBUG) e.printStackTrace();
				}
			}
		} else {
			System.out.println("Please provide a file name.");
		}
	}

	/**
	 * Print program version.
	 */
	private void version() {
		System.out.println("CardbaseCLI v" + VERSION);
	}
	
	/**
	 * Exit procedure.
	 */
	private void exit() {
		if (savePrompt) {
			System.out.println("Don't forget to save. If you really wish to quit without saving, type \"exit\" again.");
			savePrompt = false;
		} else {
			exit = true;
		}
	}

	/**
	 * Print a list of valid set codes.
	 */
	private void sets() {
		for (CardSetInformation set : mtgUniverse.getCardSetList()) {
			// CardSet has an overridden toString()
			System.out.println(set);
		}
	}

	/**
	 * Select a set.
	 * 
	 * @param args the code of the chosen set.
	 */
	private void set(String... args) {
		if (args != null && args.length > 0) {
			try {
				selectedSet = mtgUniverse.getFullCardSet(args[0]);
				// if the set code is invalid, null is returned
				if (selectedSet != null) {
					System.out.println("Selected set: " + selectedSet.name + ".");
					// undoing is not allowed if the set is changed - it would get tricky
					lastAction = null;
				} else {
					System.out.println("\"" + args[0] + "\" does not correspond to any set (use \"sets\" to see all valid set codes).");
				}
			} catch (JsonParseException e) {
				System.out.println("Error: JSON fetched from upstream was not formatted properly.");
				if (Cardbase.DEBUG) e.printStackTrace();
			} catch (JsonMappingException e) {
				System.out.println("Error: JSON fetched from upstream does not match the data structure used internally.");
				if (Cardbase.DEBUG) e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error: JSON could not be fetched from upstream.");
				if (Cardbase.DEBUG) e.printStackTrace();
			}
		} else {
			System.out.println("Please enter a set code (use \"sets\" to see all valid set codes).");
		}
	}

	/**
	 * Print a brief list of the whole cardbase.
	 */
	private void glance() {
		int total = 0;
		for (Card card : cardbase.getCards()) {
			printGlance(card);
			total += cardbase.getCount(card);
		}
		System.out.println("Total: " + total);
	}

	/**
	 * Print detailed information of a single card or the whole cardbase.
	 *
	 * @param args optionally a card within the set (by number) to peruse.
	 */
	private void peruse(String... args) {
		// if a card is specified, peruse only that
		if (args != null && args.length > 0) {
			if (selectedSet != null) {
				Card card = cardbase.getCard(selectedSet.code, args[0]);
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
			int total = 0;
			for (Card card : cardbase.getCards()) {
				printPerusal(card);
				total += cardbase.getCount(card);
			}
			System.out.println("Total: " + total);
		}
	}

	/**
	 * Remove one or more of a card.
	 *
	 * @param args the set number of the card to remove and optionally the count to be removed.
	 */
	private void remove(String... args) {
		if (selectedSet != null) {
			if (args != null && args.length > 0) {
				Card cardToRemove = selectedSet.getCardByNumber(args[0]);
				if (cardToRemove != null) {
					String count = args.length > 1 ? args[1] : "1";
					
					if (count.matches("[-]?[0-9]+")) {
						Integer intCount = Integer.valueOf(count);
						if (intCount > 0) {
							removeCard(cardToRemove, intCount);
						} else {
							System.out.println("Cannot remove " + count + " cards.");
						}
					} else {
						System.out.println(count + " is not a valid number of cards.");
					}
				} else {
					System.out.println(args[0] + " does not correspond to a card in " + selectedSet.name + ".");
				}
			} else {
				System.out.println("Please specify a card number to remove.");
			}
		} else {
			System.out.println("Please select a set before removing cards.");
		}
	}

	/**
	 * Add one or more of a card.
	 *
	 * @param number the number of the card to add.
	 * @param args optionally the count to add.
	 */
	private void add(String number, String... args) {
		if (selectedSet != null) {
			// a blank line after adding a card repeats the addition unlimitedly
			if (number.isEmpty()) {
				if (lastAction == Action.ADD) {
					addCard(lastAction.card, lastAction.count);
				} else {
					System.out.println("Please enter a card number.");
				}
			} else {
				Card cardToAdd = selectedSet.getCardByNumber(number);
				if (cardToAdd != null) {
					String count = args != null && args.length > 0 ? args[0] : "1";

					if (count.matches("[-]?[0-9]+")) {
						
						Integer intCount = Integer.valueOf(count);

						if (intCount > 0) {
							addCard(cardToAdd, intCount);
						} else {
							System.out.println("Cannot add " + intCount + " cards.");
						}
					} else {
						System.out.println(count + " is not a valid number of cards.");
					}
				} else {
					System.out.println(number + " does not correspond to a card in " + selectedSet.name + ".");
				}
			}
		} else {
			System.out.println("Please select a set before adding cards.");
		}
	}
	
	/**
	 * Undo previous action.
	 */
	private void undo() {
		if (lastAction != null) {
			if (lastAction == Action.ADD) {
				removeCard(lastAction.card, lastAction.count);
			} else if (lastAction ==  Action.REMOVE) {
				addCard(lastAction.card, lastAction.count);
			}
			// can only undo once
			lastAction = null;
		} else {
			System.out.println("Nothing to undo.");
		}
	}
	
	/**
	 * Add the specified count of the specified card
	 * to the cardbase.
	 * 
	 * @param card the card to add, set this object's 
	 * count field to determine the count to add.
	 * TODO comment
	 */
	private void addCard(Card card, int count) {
		System.out.println("Added " + count + "x " + card.name + ".");
		cardbase.addCard(card, count);
		savePrompt = true;
		lastAction = Action.ADD;
		lastAction.card = card;
		lastAction.count = count;
	}

	/**
	 * Remove the specified count of the specified card
	 * from the cardbase.
	 * 
	 * @param card the card to remove, set this object's count field
	 * to determine how many of the card to remove.
	 * TODO comment
	 */
	private void removeCard(Card card, int count) {
		Integer removed = cardbase.removeCard(card, count); 
		if (removed > 0) {
			System.out.println("Removed " + removed + "x " + card.name + ".");
			savePrompt = true;
			lastAction = Action.REMOVE;
			lastAction.card = card;
			lastAction.count = removed;
		} else {
			System.out.println(card.name + " is not in the cardbase.");
		}
	}

	/**
	 * Return a {@code String} that is guaranteed to be a legal file name.
	 * 
	 * @param name the file name candidate to sanitise.
	 * @return the sanitised name.
	 */
	private String sanitiseFileName(String name) {
		// POSIX-compliant valid filename characters
		name = name.replaceAll("[^-_./A-Za-z0-9]", "");
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
		System.out.println(String.format("%1$-4d %2$s (%3$s, %4$s)", cardbase.getCount(card), card.name, card.setCode, card.number));
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
