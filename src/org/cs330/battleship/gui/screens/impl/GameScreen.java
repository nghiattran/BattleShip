package org.cs330.battleship.gui.screens.impl;

import java.awt.Graphics;

import org.cs330.battleship.gui.components.impl.BattleGrid;
import org.cs330.battleship.gui.screens.Screen;

/**
 * GameScreen.java
 * @author Clayton Williams
 * @date Feb 7, 2016
 */
public class GameScreen extends Screen {

	/**
	 * generated serial
	 */
	private static final long serialVersionUID = -8971100207819583278L;
	
	/**
	 * The battlegrids for the player and enemy
	 */
	private BattleGrid playerGrid, enemyGrid;
	
	/**
	 * GameScreen
	 */
	public GameScreen() {
//		this(new BattleGrid(30, 80));
		
		playerGrid = new BattleGrid(100, 90);
		this.add(playerGrid);
		
		enemyGrid = new BattleGrid(650, 90);
		this.add(enemyGrid);
	}
	
//	/**
//	 * GameScreen
//	 */
//	public GameScreen(BattleGrid playerGrid) {
//		super(true);
//		this.playerGrid = playerGrid;
//		this.add(this.playerGrid);
//	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
