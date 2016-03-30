package battleship.client.gui.component;

import java.util.Random;
import battleship.client.Constants;
import battleship.client.gui.component.impl.Ship;
import battleship.client.gui.structures.ShipType;

public class AutoPosition {
	
	/**
	 * Auto position all ships
	 */
	public static int[][] autoPosition()
	{
		int[][] shipPos = new int[ShipType.values().length][3];
		int[][] grid = new int[10][10];
		for(int i = 0; i < ShipType.values().length; i++) {
			autoPositionShip(grid, shipPos[i], ShipType.values()[i].getTileLength());
		}
	
		// For debugging
		//display(grid);
		//System.out.println("=================================");
		return shipPos;
	}

	
	/**
	 * Auto position a ship
	 * @param grid		the battle grid
	 * @param length	length of the ship
	 * @param index		for displaying purposes
	 */
	private static void autoPositionShip(int [][] grid, int[] shipPos, int length)
	{
		// variable for rotating
		// 0: place ship parallel to y axis
		// 1: place ship parallel to x axis
		int rotate = rand(0,1);
		
		// keep placing the ship until finding a legal place
		boolean flag = true;
		
		// coordinate variable
		// 0: y axis
		// 1: x axis
		int [] coor = new int[2];
		int position = 0;
		
		while(flag)
		{
			flag = false;
			coor[0] = rand(0,9);
			coor[1] = rand(0,9);
			position = coor[rotate];
			for(int i = 0; i < length; i++)
			{
				// if out of the grid or a space is already occupied => find a new space
				if(coor[rotate] > 9 || grid[coor[0]][coor[1]] != 0)
				{
					flag = true;
					continue;
				}
				coor[rotate]++;
			}
		}
		coor[rotate] = position;
		shipPos[0] = coor[1];
		shipPos[1] = coor[0];
		shipPos[2] = rotate;
		
		// place the ship on the grid
		for(int i = 0; i < length; i++)
		{
			grid[coor[0]][coor[1]] = length;
			coor[rotate]++;
		}
	}
	
	/**
	 * Generate a random number with range
	 * @param min		min value for the random number
	 * @param max		max value for the random number
	 * @return			the generated number
	 */
	private static int rand(int min, int max)
	{
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * Display an array
	 * @param arr
	 */
	private static void display(int[][] arr)
	{
		for(int x = 0; x < arr.length; x++)
		{
			for(int y = 0; y < arr[x].length; y++)
			{
				System.out.print(arr[x][y] + " ");
			}
			System.out.println();
		}
	}
}
