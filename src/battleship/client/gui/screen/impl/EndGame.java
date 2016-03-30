package battleship.client.gui.screen.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import battleship.client.gui.screen.Screen;
import battleship.client.system.sound.Sound;

/**
 * The screen that shows who won the game
 * EndGame.java
 * @author Clayton Williams
 * @date Mar 7, 2016
 */
public class EndGame extends Screen {
	
	/**
	 * The win flag
	 */
	private final boolean win;
	
	/**
	 * EndGame
	 * @param win - whether the player won or not
	 */
	public EndGame(boolean win) {
		super(true, true);
		this.win = win;
		if (win) {
			Sound.values()[4].playOnce();
		} else {
			Sound.values()[5].playOnce();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font(g.getFont().getName(), 1, 20));
		g.drawString("You have " + (win ? "won" : "lost") + " the game!", 515, 200);
	}

}
