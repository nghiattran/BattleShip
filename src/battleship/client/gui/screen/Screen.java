package battleship.client.gui.screen;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import battleship.client.gui.Application;
import battleship.client.gui.component.InteractiveButton;
import battleship.client.gui.component.impl.ExitMain;
import battleship.client.gui.screen.impl.GameScreen;
import battleship.client.gui.screen.impl.ServerScreen;
import battleship.client.system.FileSystem;
import battleship.client.system.sound.Sound;
import battleship.client.system.sound.SoundThread;

/**
 * Represents a screen to hold organized panels and components
 * Screen.java
 * @author Clayton Williams
 * @date Feb 7, 2016
 */
public class Screen extends JPanel { 
	
	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -3476544084256420678L;
	
	/**
	 * The background 
	 */
	private BufferedImage background;
	
	/**
	 * The theme song
	 */
	final static SoundThread theme = new SoundThread("theme.wav");
	
	/**
	 * The current opacity
	 */
	private float currentOpacity = 0.0f;
	
	/**
	 * If the screen is loaded
	 */
	protected boolean loaded = false;
	
	/**
	 * An optional main menu button for every screen
	 */
	protected ExitMain mainMenu = new ExitMain(20, 20);
	
	/**
	 * Screen
	 */
	public Screen() {
		this(false, false);
	}
	
	/**
	 * Play the theme song
	 */
	private final static void playTheme() {
		theme.play();
	}
	
	/**
	 * Mute or unmute the theme music
	 */
	public void toggleMuteTheme() {
		theme.toggleMute();
	}
	
	/**
	 *  Toggles the mute buttons
	 */
	private void toogleButton(boolean flag) {
		toggleMuteTheme();
		muteButton.setVisible(flag);
		unmuteButton.setVisible(!flag);
	}
	
	/**
	 * The buttons for muting sounds 
	 */
	private InteractiveButton muteButton = new InteractiveButton("", "volume.png", "mute.png", new Point((int) (Application.SIZE.getWidth() - 40), 10));
	private InteractiveButton unmuteButton = new InteractiveButton("", "mute.png", "volume.png", new Point((int) (Application.SIZE.getWidth() - 40), 10));
	
	/**
	 * Screen
	 * @param skipFade - whether we should skip fading this screen in on load
	 */
	public Screen(boolean skipFade, boolean menuButton) {
		background = FileSystem.getImage("bg.png");
		setPreferredSize(Application.SIZE);
		this.setLocation(0, 0);
		setLayout(null);
		setBounds(0, 0, (int) Application.SIZE.getWidth(), (int) Application.SIZE.getHeight());
		this.setBackground(Color.BLACK);
		
		muteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toogleButton(false);
			}
		});
		
		unmuteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toogleButton(true);
			}
		});
		
		if (skipFade) {
			currentOpacity = 1.0f;
			loaded = true;
		}
		
		muteButton.setVisible(true);
		unmuteButton.setVisible(false);
		
		mainMenu.setVisible(menuButton);
		this.add(mainMenu);
		this.add(muteButton);
		this.add(unmuteButton);
		
		playTheme();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (currentOpacity < 1.0f) {
        	currentOpacity += 0.01f;
        	if (currentOpacity > 1.0f) {
        		currentOpacity = 1.0f;
        		loaded = true;
        	}
            g2d.setComposite(AlphaComposite.SrcOver.derive(currentOpacity));
        }
        g2d.drawImage(background, 0, 0, null);
	}

	
}
