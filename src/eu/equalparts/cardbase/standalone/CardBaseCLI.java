package eu.equalparts.cardbase.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import eu.equalparts.cardbase.data.CardBase;

/**
 * This provides a lightweight CLI for interacting with cardbase files. 
 * 
 */
public class CardBaseCLI {
	
	/**
	 * Execute the interface.
	 * 
	 * @param args the first argument is the cardbase file. Further arguments are ignored.
	 */
	public static void main(String... args) {

		System.out.println("Welcome to cardbase");
		CardBase cb;

		// construct the cardbase
		if (args.length > 0) {
			System.out.println("Building cardbase from " + args[0]);
			cb = new CardBase(new File(args[0]));
		} else {
			System.out.println("No cardbase file was provided, initialising a clean cardbase");
			cb = new CardBase();
		}
		
		// main UI loop
		try {
			// initialise necessary components
			System.out.println("Fetching card sets from upstream");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			boolean exit = false;
			
			while (!exit) {
				String rawInput = br.readLine().trim().toLowerCase();
				String[] commands = rawInput.split("[ \t]+");
				
				if (commands.length > 0) {
					switch (commands[0]) {
					case "exit":
						exit = true;
						break;
					case "sets":
						break;
					case "set":
						break;
					case "write":
						break;
					default:
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	
	}
	
}
