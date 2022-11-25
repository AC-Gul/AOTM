/* 
Alex Gulewich
Jan, 11, 2021
*REVISED x2* main function
Lists have failed me time and time again.
Instead I'll repeatedly redeclare arrays.
I'll most likely have more luck with them.
*/

import java.util.*;
public class AOTM {
	
	public static void game (int mode) {
		//Declarations
		
		// BOTS
		Brain.Q_table = Brain.importQtable(); // Inport old table
		double DECAY = 0.025; // Rate to decay liklihood of random actions
		int SHOW_EVERY = 1;  // How often to show round during training

		// MAP
		Map thai = new Map(12);
		boolean render = true; // whether or not to draw map
		
		// Game controllers
		boolean alive; // If the player or at least one enemy are alive
		boolean game = true;  // if the game is playing
		boolean dead = false; // If the player has died
		
		//Player
		String input;
		
		// Get the player's name
		if (mode == 0) {
		input = event("What is your name?");
		} else {input = "BOT";}
		Player avatar = new Player(input, '@', Map.map.length/2, Map.map.length/2); // set the player in the middle
		
		//Arrays
		ArrayList<Weapon> weapons = new ArrayList<Weapon>(); // create weapons list
		Militant[] army; // The enemies
		
		
		//MAIN GAME LOOP
		while (game) {
			Map.round++;
			
			// If the rate of random isn't too low, decay it
			if (Brain.epsilon > 0.005) {
				Brain.epsilon -= DECAY;
			}
			
			input = "BottyBoi"; //If player is bot
			
			//Reset player
			avatar.hp = 100;
			avatar.x = Map.map.length/2+1;
			avatar.y = Map.map.length/2+1;
			
			//Produce agents
			army = newAgentBatch(mode);
			
			//Set map size
			if (mode == 0) { // If not training
				thai = new Map(Map.round + 14);
			}
			else {
				thai = new Map(50);
			}
			
			// If it's time to draw the map
			if (render) {
				Map.printMap(avatar, army, weapons); // print the map
				Brain.exportQtable(Map.round); // export the current table
			}
			
			//Round loop ( while the player and at least one enemy live
			alive = true;
			while (alive) {
				
				//Determine whether nor not to render during training
				if (mode == 1 && Map.round % SHOW_EVERY != 0) {
					render = false;
				} else {render = true;}
				
				// If the player dies give the optino to go back to main menu
				if(avatar.hp <= 0) {
					alive = false;
					if (mode == 0) {
						dead = true;
						while (dead) {
							Start.updateHighScore(Map.round);
							Start.cls();
							input = event("    Continue?(Y/N)");
							if (input.equals("n") || input.equals("N")) {
								dead = false;
								game = false;
								Map.round = 0;
								Start.main(new String[1]);
							}
							else if (input.equals("y") || input.equals("Y")) {
								dead = false;
								Map.round = 0;
								break;
							}
						}
					}
				}
				
				Map.update(avatar, army, weapons, true, render); //Show current locations
				
				// If the player is playing, let them give input
				if (mode == 0) {
					input = Start.userInput(); //If player is human
					Brain.epsilon = 0.25;
				}
				
				//MOVE
				Map.update(avatar, army, weapons, false, render); //Remove old locations
				Player.move(avatar, input, army, weapons); // Move the player
				
				//AI TURN
				for (int self = 0; self < army.length; self++) {	
					
					//Allow AI to act
					army = checkIfDead(self, army); // Remove the dead
					
					if (self < army.length && army.length > 0) {
						army[self].move( army[self], avatar, army );
					}
				}
				if (army.length == 0) {
					alive = false;
				}
				
			}
		}
	} //END OF THE MAIN FUNCTION
	
	
	
	//Produce a new batch of enemies
	public static Militant[] newAgentBatch (int mode) {
		int x, y, num;
		Militant[] army;
		Random rand = new Random();

		if (mode == 1) {
		num = 1;
		} else {num = Map.round;}
		army = new Militant[num];
		for (int cycle = 0; cycle < num; cycle++) {
			x = rand.nextInt(Map.map.length-2) + 1;
			y = rand.nextInt(Map.map.length-2) + 1;
			army[cycle] = new Militant('E', x, y);
		}
		return(army);
	}
	
	
	//Check to see if AI is dead
	public static Militant[] checkIfDead (int self, Militant[] army) {
		Militant[] copy = new Militant[army.length-1];
		
		if (army[self].hp <= 0) {
			army[self] = army[army.length-1];
			army[army.length-1] = null;
			
			for (int i = 0; i < army.length-1; i++) {
				copy[i] = army[i];
			}
			army = new Militant[army.length-1];
			for (int i = 0; i < copy.length; i++) {
				army[i] = copy[i];
			}
		}
		return (army);
	}
	
	
	//The initial player screen
	public static String event (String happening) {
		//Declarations
		Scanner input = new Scanner(System.in);
		boolean resume = false;
		String name = "no";
		
		//Center input box vertically
		for (int y = 0; y < (Map.map.length/2-3); y++) {
			System.out.println();
		}
		
		while(true) {
			Start.cls();
			for (int y = 0; y <= 5; y++) {
				//Center box horizontally
				System.out.print("\n");
				for (int x = 0; x < (Map.map[0].length/2-20); x++) {
					System.out.print(" ");
				}
				//Build input box
				if (y == 0) {
				System.out.print("____________________");
				}
				if (y == 1) {
					System.out.print("|" + happening + "|");
				}
				if (y == 2) {
					System.out.print("|__________________|");
				}
				if (y == 3) {
					System.out.print("   "); //input layer
					name = input.nextLine();
					if (name.length() < 6) {
						resume = true;
						break;
					}
				}
			}
			if (resume) {
				break;
			}
		}
		return(name);
	}
}
