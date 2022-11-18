/* 
Alex Gulewich
Jan, 4, 2020
AOTM revised
This is the revision of my game AOTM. It's a buggy mess,
So I decided to just redo it. This time more modularly.
*/

import java.util.*;
import java.io.*;
public class Start {
	static int highScore;
	//TODO clean up my code(it's very messy and unefficent)
	public static void main (String[] args) {
		//Declarations
		boolean[] display = new boolean[4];
		String choice = "a";
		int option = 0;
		
		highScore = getHighScore();
		while (true) {
			cls();
			
			if (choice.equals("w")) {
				option--;
			}
			else if (choice.equals("s")) {
				option++;
			}
			
			if (option > 3) {
				option = 0;
			}
			if (option < 0) {
				option = 3;
			}
			if (choice.equals("d")) {
				break;
			}
			
			display[option] = true; //Move cursor
			
			System.out.println("HIGH SCORE \n    " + highScore); //Display the high score
			button("Start", display[0]);
			button("Train", display[1]);
			button("Guide", display[2]);
			button("Leave", display[3]);
						
			choice = userInput();
			
			display[option] = false; //Remove cursor
		}
		//Send player to desired field
		switch (option) {
			case(0):
				AOTM.game(0);
				break;
			case(1):
				AOTM.game(1);
				break;
			case(2):
				guide();
				break;
			case (3):
				System.exit(0);
				break;
			
		}
	}
	
	
	public static void button (String text, boolean selected) {
		String wrap = "#######";
		
		if (selected) {
			System.out.println(wrap);
			System.out.println("#" + text + "#");
			System.out.println(wrap);
		}
		else{
			System.out.println("\n " + text + "\n");
		}
	}
	
	
	public static String userInput() {
		String choice = "0";
		
		try {
			//user input
			new ProcessBuilder("cmd", "/c", "move.bat").inheritIO().start().waitFor();
			File input = new File("input.txt");
			Scanner scan = new Scanner(input);
			
			//Grab the user's input
			scan = new Scanner(input);
			choice = scan.nextLine();
				
		} catch(Exception FUCK){System.out.println("fishy");};
		
		
		return(choice);
	}
	
	
	public static void cls () {
		try {
			//cls
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}catch(Exception NOTHING){}
	}
	
	
	public static void guide () {
		//Tells the player how to play the game
		cls();
		System.out.println("CONTROLS \n_________ \n\nW - Move up\n A - Move left \nS - Move Down \nD - Move right \nP - MainMenu"); //Tells the user the controls
		System.out.println("\n\nGAMEPLAY \n_________ \n\nEvery round, the arena will grow, \nAnd so will the enemy forces! \nSurvive as for long as you can! \n\n\n"); //Tells the user the game concept
		button("Back ", true);
		userInput();
		
		//Return
		String[] a = {"a", "b"};
		main(a);
	}
	
	
	//Get the high score
	public static int getHighScore () {
		int highScore = -1;
		
		try {
			File score = new File("Saves\\HIGHSCORE.txt");
			Scanner scan = new Scanner(score);
			highScore = Integer.parseInt(scan.nextLine());
		} catch (Exception e){}
		return(highScore);
	}
	
	
	//Update the high score
	public static void updateHighScore(int highScore) {
		String export = Integer.toString(highScore);
		try {
			//Declarations
			FileWriter write = new FileWriter("Saves\\HIGHSCORE.txt");
			
			//SAVE
			write.write(export);
			write.close();
		} catch(IOException e){}
	}
}