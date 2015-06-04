package eu.equalparts.cardbase.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import eu.equalparts.cardbase.data.CardBaseManager;
import eu.equalparts.cardbase.data.MetaCardSet;

/**
 * This provides a lightweight CLI for interacting with cardbase files. 
 * 
 */
public class CardBaseCLI {
	
	private static String selectedSet = "";
	private static CardBaseManager cbm;
	private static boolean exit = false;

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
				System.out.println("Building cardbase from " + args[0]);
				cbm = new CardBaseManager(new File(args[0]));
			} else {
				System.out.println("No cardbase file was provided, initialising a clean cardbase");
				cbm = new CardBaseManager();
			}

			// initialise necessary components
			System.out.println("Fetching card sets from upstream");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			while (!exit) {
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

	private static void parse(String[] commands) {
		if (commands.length > 0) {
			switch (commands[0]) {
			/*
			 * Exit procedures
			 */
			case "exit":
				exit = true;
				break;
			/*
			 * Show help (externalised?) 
			 */
			case "help":
				System.out.println("google it");
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
					if (mcs.getCode().equalsIgnoreCase(commands[1])) {
						System.out.println("Selected set: " + mcs.getName());
						selectedSet = mcs.getCode();
						return;
					}
				}
				System.out.println(commands[1] + " does not correspond to any set (use \"sets\" to see all valid set codes)");
				break;
			/*
			 * Write current CardBase to file
			 */
			case "write":
				break;
			/*
			 * Try to parse a card number and get it from the set
			 */
			default:
				break;
			}
		}
	}

}
