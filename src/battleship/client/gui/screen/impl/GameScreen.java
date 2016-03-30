package battleship.client.gui.screen.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;

import battleship.client.gui.Application;
import battleship.client.gui.component.impl.BattleGrid;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.structures.Alignment;
import battleship.client.gui.structures.GridType;
import battleship.client.gui.util.GUITools;

/**
 * GameScreen.java
 * @author Clayton Williams
 * @author Nghia Tran
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
	 * Who's turn it is
	 */
	private boolean activeTurn = false;

	/**
	 * GameScreen
	 */
	public GameScreen(BattleGrid playerSetup) {
		super(true, true);
		this.playerGrid = playerSetup;
		this.playerGrid.setLocation(10, 15);
		this.add(this.playerGrid);
		enemyGrid = new BattleGrid(730, 15, GridType.ENEMY, Alignment.RIGHT);
		this.add(enemyGrid);
		mainMenu.setText("Leave Game");
		Point p = GUITools.getCenterScreen(mainMenu, this);
		p.translate(0, -210);
		mainMenu.setLocation(p);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.drawString((activeTurn ? "Your" : "Opponent's") + " Turn", 585 + (activeTurn ? 20 : 0), 100);
	}
	
	/**
	 * Sets which turn it is
	 * @param activeTurn
	 */
	public void setActiveTurn(boolean activeTurn) {
		this.activeTurn = activeTurn;
	}
	
	/**
	 * Checks if its the player's turn
	 * @return
	 */
	public boolean isTurn() {
		return this.activeTurn;
	}

	/**
	 * Gets the player grid
	 * @return {BattleGrid}
	 */
	public BattleGrid getPlayerGrid() {
		return playerGrid;
	}

	/**
	 * Gets the enemy grid
	 * @return {BattleGrid}
	 */
	public BattleGrid getEnemyGrid() {
		return enemyGrid;
	}
	
	

}
