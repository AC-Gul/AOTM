/* 
Alex Gulewich
Nov, 4, 2021
The militant are the enemies.
They'll be the goons, and event he BBEG
*/

import java.util.*;
public class Militant {
	//Declarations
	int x;
	int y;
	int hp = 100; //Only temporaily hardcoded (Should be dynamic)
	char icon;
	boolean dead = false;
	Weapon weapon = new Weapon('!', "stick", 5, 0, 0);
	
	public Militant(char icon, int x, int y) {
		this.icon = icon;
		this.x = x;
		this.y = y;
	}
	
	
	//Enablwa movemnet
	public static void move (Militant self, Player foe, Militant[] allies) {
		//Declarations
		int newX = self.x;
		int newY = self.y;
		int input = Brain.fight(foe, self.x, self.y);
		
		boolean recievedDamage = false;
		boolean dealtDamage = false;
		boolean allowMove = true;
		boolean moved = false;
		boolean dead = false;
		boolean kill = false;

		
		//Movement
		switch (input) {
			
			//Move up
			case(0):
				newY--;
				break;
				
			//Move down
			case(1):
				newY++;
				break;
				
			//Move left
			case(2):
				newX--;
				break;
				
			//Move right
			case(3):
				newX++;
				break;
		}
		
		//Attack
		if(foe.x == newX && foe.y == newY) {
			foe.hp -= self.weapon.meleeDamage;
			dealtDamage = true;
			allowMove = false;
			if(foe.hp <= 0) {kill = true;}
		}
		
		//Check for ally collisions
		else {
			for (int x = 0; x < allies.length -1; x++) {
				if (allies[x].x == self.x && allies[x].y == self.y) {x++;} //If self
				if (allies[x].x == newX && allies[x].y == newY) {allowMove = false; break;} //If colliding
			}
		}
		
		//check for borders
		if (Map.map[newX][newY] == '#') {
			allowMove = false;
		}
		
		//Move if able
		if (allowMove) {
			self.x = newX;
			self.y = newY;
			//See if movement punishment applicable
			if (self.x == newX && self.y == newY) {/* No change */}
		}
		else {moved = true;}
		
		if(self.hp <= 0) {
			dead = true;
		}
		
		Brain.train((self.x - foe.x), (self.y - foe.y), moved, dealtDamage, kill, dead);
	}
}