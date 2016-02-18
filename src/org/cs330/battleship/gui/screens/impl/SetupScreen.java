package org.cs330.battleship.gui.screens.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.JButton;

import org.cs330.battleship.gui.Application;
import org.cs330.battleship.gui.components.impl.BattleGrid;
import org.cs330.battleship.gui.screens.Screen;
import org.cs330.battleship.tools.FileUtils;
import org.cs330.battleship.tools.GUITools;

/**
 * A screen used for setting up a player's battleship configuration
 * SetupScreen.java
 * @author Clayton Williams
 * @date Feb 9, 2016
 */
public class SetupScreen extends Screen implements MouseMotionListener {

	/**
	 * Variables for drag and drop effect
	 */
	final int numShip = 4;
	int desX, desY;
	boolean loaded = false;
	int[] shipX = new int[numShip];
	int[] shipY = new int[numShip];
	int[][] shipPosition = new int[5][3];
	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -3323568124290451743L;
	
	/**
	 * The JButton back
	 */
	private JButton back = new JButton("Back");
	
	/**
	 * The JButton play
	 */
	private JButton playButton = new JButton("Play");
	
	/**
	 * The JButton auto place ship
	 */
	private JButton autoButton = new JButton("Auto setup");
	
	
	/**
	 * A battlegrid to do our setups on
	 */
	private BattleGrid battleGrid;
	
	/**
	 * SetupScreen
	 */
	public SetupScreen() {
		super(true);
		
		addMouseMotionListener(this);
		
		/** Back button to main menu **/
		back.setSize(120, 40);
		back.setLocation(20, 20);
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Application.getInstance().changeScreen(new MainMenu());
			}
		});
		
		this.add(back);
		
		/** Play button to main menu **/
		playButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Application.getInstance().changeScreen(new GameScreen());		
			}
		});
		playButton.setSize(200, 50);
		playButton.setLocation(690, 500);
		this.add(playButton);
		
		/** Play button to main menu **/
		autoButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				autoPosition(shipPosition);
				display(shipPosition);
				System.out.println("=================================");
			}
		});
		autoButton.setSize(200, 50);
		autoButton.setLocation(890, 500);
		this.add(autoButton);
		
		
		/** Add a grid to setup ships on */
		battleGrid = new BattleGrid(120, 90);
		this.add(battleGrid);		
	}
	
//	public SetupScreen(Image i) {
////		image = i;
//		addMouseMotionListener(this);
//	}
	
	public void initShipPositions() {
		for (int i = 0; i < numShip; i++)
		{
			shipX[i] = 690;
			shipY[i] = 190 + 60 * i;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Comic Sans MS", 1, 20));
		g2.setColor(Color.WHITE);
		g2.drawString("Setup Your Battleships:", 690, 140);
		g2.setFont(new Font("Arial", 0, 13));
		g2.drawString("Drag and drop battleships from below over to the grid on the left. Press 'R' while", 690, 160);
		g2.drawString("dragging a ship to rotate it. When finished, select 'Find Player'. ", 690, 175);
		
		if (loaded == false)
		{
			initShipPositions();
		}
		
		for (int i = 0; i < numShip; i++)
		{
			g2.drawImage(FileUtils.getSprite("ship" + i +".jpg"), shipX[i], shipY[i], 100 + i *50, 50, null);
		}
	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
//		desX = e.getX();
//		desY = e.getY();
//		System.out.println(desX + " " + desY);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		desX = e.getX();
		desY = e.getY();
		System.out.println(desX + " " + desY);
	}
		
	/**
	 * Auto position all ships
	 */
	private static void autoPosition(int[][] shipPos)
	{
		int[][] grid = new int[10][10];
		initArray(grid);

		int[] shipLength = {2, 3, 3, 4, 5};
		
		for(int i = 0; i < shipLength.length; i++)
		{
			autoPositionShip(grid, shipPos[i],shipLength[i], i+1);
		}
		
		// display(grid);
	}
	
	/**
	 * Initialize array
	 * @param arr
	 */
	private static void initArray(int[][] arr)
	{
		for(int x = 0; x < arr.length; x++)
		{
			for(int y = 0; y < arr[x].length; y++)
			{
				arr[x][y] = 0;
			}
		}
	}
	
	/**
	 * Auto position a ship
	 * @param grid		the battle grid
	 * @param length	length of the ship
	 * @param index		for displaying purposes
	 */
	private static void autoPositionShip(int [][] grid, int[] shipPos, int length, int index)
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
			grid[coor[0]][coor[1]] = index;
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
