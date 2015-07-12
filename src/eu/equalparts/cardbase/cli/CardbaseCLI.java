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
import eu.equalparts.cardbase.data.Card;
import eu.equalparts.cardbase.data.FullCardSet;
import eu.equalparts.cardbase.data.CardSetInformation;
import eu.equalparts.cardbase.utils.MTGUniverse;

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
	 * Location of the help file.
	 */
	private static final String HELP_FILE_PATH = "/help";
	/**
	 * Program version.
	 */
	private static final String VERSION = "1.0";
	/**
	 * The last action performed by the user.
	 */
	private Action lastAction = null;
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
		new CardbaseCLI(args).startInterface();
	}

	/**
	 * Reads in an optional cardbase JSON and initialises other necessary components.
	 * This does not actually produce the CLI, for that use {@code startInterface()}
	 * on the constructed object.
	 * 
	 * @param args a list of arguments. Only the first argument is used, as a cardbase JSON.
	 */
	public CardbaseCLI(String... args) {
		System.out.println("Welcome to Cardbase CLI!");

		// set debug flag if we are debugging
		if (Cardbase.DEBUG) System.out.println("Debug mode is on.");

		// make the Cardbase
		if (args != null && args.length > 0) {
			cardbaseFile = new File(args[0]);
			if (cardbaseFile.exists() && cardbaseFile.isFile() && cardbaseFile.canRead()) {
				System.out.println("Loading cardbase from \"" + args[0] + "\".");
				try {
					cardbase = new Cardbase(cardbaseFile);
				} catch (JsonParseException e) {
					System.out.println("Error: poorly formatted cardbase, check the syntax and try again.");
					// although the problem could also be with the upstream CardSetList json.
					if (Cardbase.DEBUG) e.printStackTrace();
					System.exit(1);
				} catch (JsonMappingException e) {
					System.out.println("Error: unexpected fields found in cardbase, it may be from an old version?");
					if (Cardbase.DEBUG) e.printStackTrace();
					System.exit(1);
				} catch (IOException e) {
					System.out.println("Error: something went wrong reading cardbase file, abort...");
					if (Cardbase.DEBUG) e.printStackTrace();
					System.exit(1);
				}
			} else {
				System.out.println(args[0] + " appears to be an invalid path.");
				System.exit(0);
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
	 * Read stdin for user input, sanitise and interpret any commands entered.
	 */
	private void startInterface() {
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			// the main loop
			while (!exit) {
				// print prompt
				System.out.print(selectedSet == null ? "> " : selectedSet.code + " > ");
				// condition input and interpret
				String[] raw = consoleReader.readLine().trim().split("[ \t]+");
				String command = raw[0];
				String[] args = Arrays.copyOfRange(raw, 1, raw.length);

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
		} catch (IOException e) {
			System.out.println("Error: something went wrong with stdin, exiting...");
			if (Cardbase.DEBUG) e.printStackTrace();
		}
	}

	/**
	 * Print help to console.
	 */
	public void help() {
		System.out.println(help);
	}

	/**
	 * Write current cardbase to file.
	 *
	 * @param args optionally the file to which to write.
	 */
	public void write(String[] args) {
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
					System.out.println("Error: lost contact with the output file, try again?");
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
	public void version() {
		System.out.println("Cardbase v" + VERSION);
	}
	
	/**
	 * Exit procedure.
	 */
	public void exit() {
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
	public void sets() {
		for (CardSetInformation set : MTGUniverse.getCardSetList()) {
			// CardSet has an overridden toString()
			System.out.println(set);
		}
	}

	/**
	 * Select a set.
	 * 
	 * @param args the code of the chosen set.
	 */
	public void set(String[] args) {
		if (args != null && args.length > 0) {
			try {
				selectedSet = MTGUniverse.getFullCardSet(args[0]);
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
	public void glance() {
		int total = 0;
		for (Card card : cardbase.getCards()) {
			printGlance(card);
			total += card.count;
		}
		System.out.println("Total: " + total);
	}

	/**
	 * Print detailed information of a single card or the whole cardbase.
	 *
	 * @param args optionally a card within the set (by number) to peruse.
	 */
	public void peruse(String[] args) {
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
				total += card.count;
			}
			System.out.println("Total: " + total);
		}
	}

	/**
	 * Undo previous action.
	 */
	public void undo() {
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
	 * Remove one or more of a card.
	 *
	 * @param args the set number of the card to remove and optionally the count to be removed.
	 */
	public void remove(String[] args) {
		if (selectedSet != null) {
			if (args != null && args.length > 0) {
				Card cardToRemove = selectedSet.getCardByNumber(args[0]);
				if (cardToRemove != null) {
					Integer count = 1;
					if (args.length > 1 && args[1].matches("[0-9]+")) {
						count = Integer.valueOf(args[1]);
						if (count <= 0) {
							System.out.println("Can't remove " + count + " cards.");
							return;
						}
					}
					removeCard(cardToRemove, count);
				} else {
					System.out.println(args[0] + " does not correspond to a card in " + selectedSet.name + ".");
				}
			} else {
				System.out.println("Please specify a card number to remove.");
			}
		} else {
			System.out.println("Select a set before removing cards.");
		}
	}

	/**
	 * Add one or more of a card.
	 *
	 * @param number the number of the card to add.
	 * @param args optionally the count to add.
	 */
	public void add(String number, String[] args) {
		if (selectedSet != null) {
			// a blank line after adding a card repeats the addition unlimitedly
			if (number.isEmpty()) {
				if (lastAction == Action.ADD)
					addCard(lastAction.card, lastAction.count);
			} else {
				Card cardToAdd = selectedSet.getCardByNumber(number);
				if (cardToAdd != null) {
					Integer count = 1;
					if (args != null && args.length > 0 && args[0].matches("[0-9]+")) {
						count = Integer.valueOf(args[0]);
						if (count <= 0) {
							System.out.println("Can't add " + count + " cards.");
							return;
						}
					}
					addCard(cardToAdd, count);
				} else {
					System.out.println(number + " does not correspond to a card in " + selectedSet.name + ".");
				}
			}
		} else {
			System.out.println("Select a set before adding cards.");
		}
	}
	
	/**
	 * Add the specified count of the specified card
	 * to the cardbase.
	 * 
	 * @param card the card to add.
	 * @param count the number of times to add it.
	 */
	private void addCard(Card card, Integer count) {
		cardbase.addCard(card, count);
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
	private void removeCard(Card card, Integer count) {
		Integer removed = cardbase.removeCard(card, count); 
		if (removed > 0) {
			System.out.println("Removed " + removed + "x " + card.name + ".");
			savePrompt = true;
			lastAction = Action.REMOVE;
			lastAction.set(card, removed);
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
