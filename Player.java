/* 
Alex Gulewich
Jan, 4, 2021
*/

import java.util.*;
public class Player {
	//Declarations
	Weapon weapon = new Weapon('!', "Sword", 25, 0, 0);
	String name;
	char icon;
	int hp = 100;
	int x;
	int y;
	
	public Player (String name, char icon, int x, int y) {
		this.icon = icon;
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	
	public static void move (Player self, String input, Militant[] foes, ArrayList<Weapon> weapons) {
		//Declarations
		int newX = self.x;
		int newY = self.y;
		boolean allowMove = true;
		String[] c = new String[2];
		
		//If AI
		if (input.equals("BottyBoi") && foes.length > 0) {
			input = decide(self, foes);
		}
		
		//Player movement
		switch(input) {
			//Move up
			case ("w"):
				newY--;
				break;
			//Move left
			case ("a"):
				newX--;
				break;
			//Move down
			case ("s"):
				newY++;
				break;
			//Move right
			case ("d"):
				newX++;
				break;
			//MainMenu
			case ("p"):
				Brain.exportQtable(500);
				Start.updateHighScore(Map.round);
				Start.main(c);
		}
		
		//If colliding with wall
		if (Map.map[newX][newY] == '#') {
			allowMove = false;
		}
		
		//Attack
		for (int x = 0; x < foes.length; x++) {
			if (foes[x].x == newX && foes[x].y == newY) {
				allowMove = false;
				foes[x].hp -= self.weapon.meleeDamage;
			}
		}
		
		if (allowMove) {
			self.x = newX;
			self.y = newY;
		}
	}
	
	
	//If the player is AI
	public static String decide (Player self, Militant[] foes) {
		//Declarations
		int target = 0;
		String input = "0";
		boolean foesNutInRange = true;
		
		//If there are enemies nearby
		for (int x = 0; x < foes.length; x++) {
			if ((self.x - foes[x].x) <= 10 && (self.y - foes[x].y) <= 10) {
				target = fightEnemy(self, foes);
				foesNutInRange = false;
				break;
			}
		}
		
		//If there are no enemies nearby
		if (foesNutInRange) {
			target = seekEnemy(self, foes);
		}
		
		
		//Move
		if (self.x < foes[target].x) {
			input = "d";
		}
		else if (self.x > foes[target].x) {
			input = "a";
		}
		if (self.y < foes[target].y) {
			input = "s";
		}
		else if (self.y > foes[target].y) {
			input = "w";
		}
		return(input);
	}
	
	
	//If there are enemies nearby
	public static int fightEnemy (Player self, Militant[] foes) {
		//Declarations
		int oldTarget = 0;
		
		//Pick closest enemy
		for (int x = 0; x < foes.length; x++) {
			if((self.x - foes[x].x) < (self.x - foes[oldTarget].x) && (self.x - foes[x].x) < (self.y - foes[oldTarget].y)) {
				oldTarget = x;
			}
		}
		return(oldTarget);
	}
	
	
	//If there are no enemies nearby
	public static int seekEnemy (Player self, Militant[] foes) {
		//Declarations
		int[][] groups = new int[foes.length][foes.length]; 
		boolean[] excluded = new boolean[foes.length];
		int counter = 0;
		
		//Assign groups
		for (int y = 0; y < foes.length; y++) {
			counter = 0;
			//Assign unused character
			for (int find = 0; find < excluded.length; find++) {
				if (excluded[find] = false) {
					groups[y][0] = find;
					break;
				}
			}
			//Assign groups
			for (int x = 0; x < foes.length; x++) {
				if (excluded[x]) {
					x++;
				}
				else {
					if ((foes[groups[y][0]].x - foes[x].x) < 7 && (foes[groups[y][0]].x - foes[x].x) < 7) {
						excluded[x] = true;
						groups[y][counter] = x;
						counter++;
					}
				}
			}
		}
		//Find smallest group
		counter = groups[0].length;
		int smallestGroupIndex = 0;
		for (int y = 1; y < groups.length; y++) {
			if (groups[y].length < counter) {
				smallestGroupIndex = y;
				counter = groups[y].length;
			}
		}
		//Pursue smallest
		return(groups[smallestGroupIndex][0]);
	}
}