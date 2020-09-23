package project;

import java.util.Scanner;

/*
 * This will be the entry point for our project.
 * It will ask for commands and call imported methods with each command.
 * 
 * Prompts user for GEDCOM path once, and then repeatedly asks for a command.
 * Passes GEDCOM path to commands.
 */

public class Commander {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);  // Create a Scanner object
	    System.out.println("Enter GEDCOM File Path: ");
	    String gedcomPath = scan.nextLine();  // Read user input
	    
	    String command = "";
	    
	    // Enter "exit" command to quit the CLI
	    while(!command.equals("exit")) {
		    System.out.println("Enter command: ");
		    command = scan.nextLine();
		    
		    // Add commands as cases in this switch-case
		    // One command can cover one or more stories
		    switch(command) {
		    	case "no-bigamy": NoBigamy.check(gedcomPath);
		    	default: continue;
		    }
	    }
	    
	    scan.close();
	    System.out.println("Finished");
	}
}
