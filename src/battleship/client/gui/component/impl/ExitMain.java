package battleship.client.gui.component.impl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;

import javax.swing.JButton;

import battleship.client.gui.Application;
import battleship.client.gui.component.GameComponent;
import battleship.client.gui.screen.impl.GameScreen;
import battleship.client.gui.screen.impl.MainMenu;

/**
 * The exit to main menu button (used on a few screens)
 * ExitMain.java
 * @author Clayton Williams
 * @date Feb 21, 2016
 */
public class ExitMain extends GameComponent {

	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -303238744300638474L;
	
	/*
	 * The button
	 */
	private JButton button = new JButton("Main Menu");

	/**
	 * ExitMain
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 */
	public ExitMain(int x, int y) {
		super(120, 40, x, y);
		button.setSize(120, 40);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (Application.getInstance().getScreen(GameScreen.class) != null) {
					ByteBuffer b = ByteBuffer.allocate(1);
					b.put((byte) 4);
					Application.getInstance().getConnector().write(b);
				} else {
					Application.getInstance().changeScreen(new MainMenu());
				}
			}
		});
		this.add(button);
		
	}
	
	/**
	 * Sets the text of this button
	 * @param text
	 */
	public void setText(String text) {
		button.setText(text);
	}

}
