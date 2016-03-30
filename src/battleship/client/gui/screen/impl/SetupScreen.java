package battleship.client.gui.screen.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;

import javax.swing.JPanel;

import battleship.client.Constants;
import battleship.client.gui.Application;
import battleship.client.gui.component.AutoPosition;
import battleship.client.gui.component.InteractiveButton;
import battleship.client.gui.component.impl.BattleGrid;
import battleship.client.gui.component.impl.Ship;
import battleship.client.gui.component.impl.ShipBay;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.structures.GridTile;
import battleship.client.gui.structures.Orientation;

/**
 * A screen used for setting up a player's battleship configuration
 * SetupScreen.java
 * @author Clayton Williams
 * @author Nghia Tran
 * @date Feb 9, 2016
 */
public class SetupScreen extends Screen {
	
	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -3323568124290451743L;
	
	/**
	 * Randomize ships button
	 */
	private InteractiveButton randomizeShips = new InteractiveButton("Randomize Ships", "button.png", "button_hover.png", new Point(920, 190));
	
	/**
	 * Find opponent button
	 */
	private InteractiveButton findOpponent = new InteractiveButton("Find Opponent", "button.png", "button_hover.png", new Point(675, 550));
	
	/**
	 * The cancel search button
	 */
	private InteractiveButton cancelSearch = new InteractiveButton("Cancel Search", "button.png", "button_hover.png", new Point(538, 180));
	
	/**
	 * A battlegrid to do our setups on
	 */
	private BattleGrid battleGrid;
	
	/**
	 * The ship bay to select ships from
	 */
	private ShipBay shipBay;
	
	/**
	 * A panel holding all of the setup components (so we can switch to the find opponent...)
	 */
	private JPanel setup;
	
	/**
	 * The elapsed finding opponent time
	 */
	private transient long elapsedTime = 0L;
	
	/**
	 * SetupScreen
	 */
	public SetupScreen() {
		super(true, true);	

		setup = new JPanel();
		setup.add(randomizeShips);

		/** Add a grid to setup ships on */
		battleGrid = new BattleGrid(110, 90);
		battleGrid.getGrid().setShowHoverGuide(false);
		setup.add(battleGrid);	
		
		shipBay = new ShipBay(680, 190);
		setup.add(shipBay);
		
		findOpponent.setVisible(false);
		findOpponent.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ByteBuffer b = ByteBuffer.allocate(3000);
				b.put((byte) 1);
				for (int i = 0; i < battleGrid.getGrid().getShips().length; i++) {
					Ship s = battleGrid.getGrid().getShips()[i];
					b.put((byte) s.getShipType().ordinal());
					GridTile t = GridTile.forMouseLocation(s.getLocation());
					b.put((byte) t.getX());
					b.put((byte) t.getY());
					b.put((byte) s.getOrientation().ordinal());
				}
				b.flip();
				Application.getInstance().getConnector().write(b);
			}
		});
		setup.add(findOpponent);
		
		cancelSearch.setVisible(false);
		cancelSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				cancelSearch.setVisible(false);
				setup.setVisible(true);
				ByteBuffer b = ByteBuffer.allocate(1);
				b.put((byte) 3);
				Application.getInstance().getConnector().write(b);
			}
		});
		this.add(cancelSearch);
		
		randomizeShips.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int [][] shipPos = AutoPosition.autoPosition();
				Ship [] ships = shipBay.getShips();
				for (int i = 0; i < ships.length; i++) {
					int i2 = ships.length - 1 - i;
					if (ships[i2] == null || ships[i2].getParent() == null)
						continue;			
					ships[i2].getParent().remove(ships[i2]);
					ships[i2].setLocation(new GridTile(shipPos[i][0], shipPos[i][1]).getPixelBase());
					
					if (shipPos[i][2] == 0) {
						ships[i2].setOrientation(Orientation.VERTICAL);
					} else {
						ships[i2].setOrientation(Orientation.HORIZONTAL);
					}

					battleGrid.getGrid().addShip(ships[i2]);
					findOpponent.setVisible(true);
				}
			}
		});
		
		setup.setSize(Application.SIZE);
		setup.setLayout(null);
		setup.setOpaque(false);
		setup.setVisible(true);
		this.add(setup);
	}
	
	/**
	 * Sets the find opponent visibility
	 * @param show
	 */
	public void setShowFindOpponent(boolean show) {
		findOpponent.setVisible(show);
	}
	
	/**
	 * Gets the setup panel
	 * @return
	 */
	public JPanel getSetup() {
		return setup;
	}

	/**
	 * Sets the elapsed time
	 * @param elapsedTime
	 */
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
	/**
	 * gets the cancel search button
	 * @return
	 */
	public InteractiveButton getCancelSearch() {
		return this.cancelSearch;
	}
	
	/**
	 * Gets the battlegrid
	 * @return
	 */
	public BattleGrid getBattleGrid() {
		return this.battleGrid;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Comic Sans MS", 1, 20));
		g2.setColor(Color.WHITE);
		if (setup.isVisible()) {
			g2.drawString("Setup Your Battleships:", 680, 140);
			g2.setFont(new Font("Arial", 0, 13));
			g2.drawString("Drag and drop battleships from below over to the grid on the left. After you're done", 680, 160);
			g2.drawString("select 'Find a game' to search for an opponent.", 680, 175);
		} else {
			g2.drawString("Finding Opponent...", 560, 150);
			g2.setFont(new Font("Arial", 0, 12));
			g2.drawString("Elapsed Time: " + (elapsedTime / Constants.FRAME_RATE) + " seconds.", 575, 170);
			elapsedTime++;
		}
	}
}
