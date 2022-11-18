/* 
Alex Gulewich
Jan, 4, 2021
Brain
All of the AI for the game. Used to be non-static,
But as I'm revising it, moving it over to static.
*/

import java.util.*;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Brain {
	static double[][][] Q_table = new double[5][12][12];
	static double LEARNING_RATE = 0.05;
	static int action;
	static double oldQ;
	static int oldX;
	static int oldY;
	static double epsilon = 0.90;
	
	public static int fight (Player enemy, int x, int y) {
		//Declarations		
		Random rand = new Random();
		
		boolean allEqual = true;
		double max;
		int input = 0;
		x -= enemy.x;
		y -= enemy.y;
		Brain.oldX = x + 6;
		Brain.oldY = y + 6;
		
		//Action
		if (x < 6 && x > -6 && y < 6 && y > -6) {
			//Apply epsilon
			if (epsilon > Math.random()) {
				input = rand.nextInt(5);
			}
			else {
				//Look for the ARGmax
				max = Brain.Q_table[0][y+6][x+6];
				for(int ins = 0; ins < 5; ins++) {
					if (max == Brain.Q_table[input][y+6][x+6] && allEqual) {}
					else {allEqual = false;}
					
					if (max < Brain.Q_table[input][y + 6][x + 6]) {
						max = Brain.Q_table[input][y + 6][x + 6];
						input = ins;
					}
				}
			}
			
			//if all values equal apply epsilon
			if (allEqual) {input = rand.nextInt(5);}
			else {
				oldQ = Brain.Q_table[input][y+6][x+6];
			}
		}
		//If the Q-table can't act
		else {
			//Reset x & y
			x += enemy.x;
			y += enemy.y;
			
			//Move towards player
			if (x < enemy.x) {
				input = 3;
			}
			else if (x > enemy.x) {
				input = 2;
			}
			else if (y > enemy.y) {
				input = 0;
			}
			else if (y < enemy.y) {
				input = 1;
			}
		}
		Brain.action = input;
		return(input);
	}
	
	
	//Remeber to subtract the player off of the cords.
	public static void train (int x, int y, boolean moved, boolean dealtDamage, boolean gotKill, boolean died) {
		//Declarations
		double DISCOUNT = 0.85;
		double Qmax = 1;
		
		//Rewards
		double deathPenalty = -20;
		double movePenalty = -1;
		double damageReward = 100;
		double killReward = 0;
		double reward = 0;
		
		//Look for the MAX(nextState, allActions)
		
		
		//Determine rewards
		if (x < 6 && x > -6 && y < 6 && y > -6 && Brain.oldX < 12 && Brain.oldY < 12) {
			if (gotKill) {
				reward = killReward;
			}
			else if (died) {
				reward = deathPenalty;
			}
			else if (dealtDamage) {
				reward = damageReward;
			}
			else {
				reward = movePenalty;
			}
			
			
			//Apply reward
			
			//Find the max
			for (int i = 0; i < Brain.Q_table.length; i++) {
				if (Brain.Q_table[i][y+6][x+6] > Qmax) {
					Qmax = Brain.Q_table[i][y+6][x+6];
				}
			}
			
			//Update
			
		
		//My originall algorithm
		/*double newQ = (Brain.Q_table[Brain.action][y+6][x+6] + Brain.LEARNING_RATE * (reward + DISCOUNT * Qmax)) - Brain.Q_table[Brain.action][y+6][x+6]; //newQ
		Brain.Q_table[Brain.action][y+6][x+6] = newQ;*/
		
		Brain.oldQ *= (1 - Brain.LEARNING_RATE);
		Brain.Q_table[Brain.action][Brain.oldY][Brain.oldX] += (LEARNING_RATE * (reward + DISCOUNT * Qmax));
		}
	}
	
	//Jan, 14, 2021  *FOR TRAINING PURPOSES*
	public static void exportQtable (int num) {
		try{
			String output;
			String contain;
			File qtable = new File("tables\\table" + num + ".txt");
			FileWriter write = new FileWriter("tables\\table" + num + ".txt");
			
			//round
			/*
			for (int z = 0; z < Brain.Q_table.length; z++) {
				for (int y = 0; y < Brain.Q_table[0].length; y++) {
					for (int x = 0; x < Brain.Q_table[0][0].length; x++) {
						output = String.format("%.2f", Brain.Q_table[z][y][x]);
						Brain.Q_table[z][y][x] = Double.parseDouble(output);
					}
				}
			}
			*/
			//Write qtable to file
			for (int z = 0; z < Brain.Q_table.length; z++) {
				for (int y = 0; y < Brain.Q_table[0].length; y++) {
					for (int x = 0; x < Brain.Q_table[0][0].length; x++) {
						write.write( Double.toString(Brain.Q_table[z][y][x]) + "\n" );
					}
				}
			}
			write.close();
			
		}catch(Exception all){}
	}
	
	public static double[][][] importQtable () {
		//Declarations
		Scanner read = new Scanner(System.in);
		double[][][] qtable = new double[Brain.Q_table.length][Brain.Q_table[0].length][Brain.Q_table[0][0].length];
		String Qvalue;
		
		try {
		read = new Scanner(new File("tables\\table500.txt"));
		} catch(FileNotFoundException a){return(qtable);}

		//Retrieve the table
		for (int z = 0; z < Brain.Q_table.length; z++) {
			for (int y = 0; y < Brain.Q_table[0].length; y++) {
				for (int x = 0; x < Brain.Q_table[0][0].length; x++) {
					Qvalue = read.next();
					qtable[z][y][x] = Double.parseDouble(Qvalue);
				}
			}
		}
		return(qtable);
	}
}