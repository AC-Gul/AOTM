/* 
Alex Gulewich
Dec, 31, 2020
Weapons
A constructor class to allow a variety of weapons to be easily created.
Swords,
Spears,
Bows,
etc
*/

public class Weapon {
	//Delcarations
	String type;
	char icon;
	int range;
	int meleeDamage;
	int rangedDamage;
	
	int x;
	int y;
	public Weapon (char icon, String type, int meleeDamage,int rangedDamage, int range) {
		//Designations
		this.type = type;
		this.range = range;
		this.meleeDamage = meleeDamage;
		this.rangedDamage = rangedDamage;
		this.icon = icon;
		
		//If no range, no ranged damage
		if (this.range == 0) {
			this.rangedDamage = 0;
		}
	}
}