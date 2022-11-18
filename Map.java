/* 
Alex Gulewich
Dec, 18, 2020

The map function
Store the map and support any map related functions
*/
import java.util.*;
public class Map {
	
	//Declarations
	static int round = 0;
	static char[][] map;
	//The constructor, not sure what I'll need
	public Map (int mapSize) { 
		//Declarations
		char[][] mapp = new char[mapSize][mapSize];
		
		//Fill map with open space with wall border
		for (int y = 0; y < mapSize; y++) {
			for (int x = 0; x < mapSize; x++) {
				if (y == 0 || y == mapSize-1 || x == 0 || x == mapSize-1) {
					mapp[x][y] = '#';
				}
				else{
					mapp[x][y] = ' ';
				}
			}
		}
		//Designations
		map = mapp;
	}
	
	public static void printMap (Player avatar, Militant[] horde, ArrayList<Weapon> weapons) {
		try{
		//CLS
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch(Exception HAPPY){};
		
		//Print the round
		for (int x = 0; x < (Map.map.length/2 - 6); x++) {
			System.out.print(" ");
		}
		System.out.print("ROUND: " + Map.round + "\n");
		
		//Print the map
		for (int y = 0; y < Map.map.length; y++) {
			for (int x = 0; x < Map.map.length; x++) {
				
				//Print the militants
				for (int v = 0; v < horde.length; v++) {
					if (x == horde[v].x && y == horde[v].y) {
						System.out.print(horde[v].icon);
						x++;
					}	
				}
				
				//Print the weapons
				if ((x == avatar.x) && (y == avatar.y)) {
				//Due to not making and behave like or if is now if not
				}
				else {
					for (int i = 0; i < weapons.size(); i++) {
						if (x == weapons.get(i).x && y == weapons.get(i).y) {
							System.out.print(weapons.get(i).icon);
							x++;
						}
					}
				}
				
				//Print the player
				if (x == avatar.x && y == avatar.y) {
					System.out.print(avatar.icon);
				}
				
				//Print the map if spot if vacant
				else {
					System.out.print(Map.map[x][y]);
				}
				//Print the info table
				if (y > 4 && y < 10 && x >= (map[0].length-1)) {
					switch (y) {
						case(5):
							System.out.print(" _______________");
							break;
							
						case(6):
							System.out.print(" | NAME: " + avatar.name);
							if (avatar.name.length() < 5) {
							for (int nm = 0; (nm + avatar.name.length()+1) < 6; nm++) {
								System.out.print(" ");
								}
							}
						System.out.print(" |");
							break;
							
						case(7):
							System.out.print(" | HP:   " + avatar.hp);
							if (avatar.hp == 100) {
								System.out.print("   ");
							}
							else if (avatar.hp > 9 && avatar.hp < 100) {
								System.out.print("    ");
							}
							else {
								System.out.print("     ");
							}
							System.out.print("|");
							break;
							
						case(8):
							System.out.print(" |_____________|");
							break;
					}
				}
			}
			System.out.print("\n");
		}
	}
	
	
	//Jan, 14, 2021  *REVISED^  update function    GOTO locations
	public static void update (Player avatar, Militant[] horde, ArrayList<Weapon> weapons, boolean show, boolean render) {
		
		if (render) {
			if (show) {
				//Update player
				System.out.print("\033[" + (avatar.y+2) + ";" + (avatar.x+2) + "H\b" + avatar.icon);
				//Update militants
				for (int m = 0; m < horde.length; m++) {
					System.out.print("\033[" + (horde[m].y+2) + ";" + (horde[m].x+2) + "H\b" + horde[m].icon);
				}
			}
			else {
				//Update player
				System.out.print("\033[" + (avatar.y+2) + ";" + (avatar.x+2) + "H\b ");
				//Update militants
				for (int m = 0; m < horde.length; m++) {
					System.out.print("\033[" + (horde[m].y+2) + ";" + (horde[m].x+2) + "H\b ");
				}
			}
			//Update health 3 is health
			System.out.print("\033[9;" + (Map.map[7].length+10) + "H   \b\b\b" + avatar.hp);
			//Reset the cursor
			System.out.print("\033[" + (Map.map.length+2) + ";1H\033[2K");
		} else{}
	}
}