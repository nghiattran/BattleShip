package battleship.client.gui.screen.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import battleship.client.gui.component.InteractiveButton;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.util.GUITools;
import battleship.server.Server;

/**
 * ServerScreen.java - represents the server configurations & console
 * @author Clayton Williams
 * @date Feb 21, 2016
 */
public class ServerScreen extends Screen {

	/**
	 * generated serial
	 */
	private static final long serialVersionUID = 5983129779236152865L;
	
	/**
	 * The server console
	 */
	private JTextArea console;
	
	/**
	 * Port number entry
	 */
	private JTextField portNumber;
	
	/**
	 * ServerScreen
	 */
	public ServerScreen() {
		super(true, true);
		
		JLabel portNumberLabel = new JLabel("Port Number:");
		portNumberLabel.setForeground(Color.WHITE);
		portNumberLabel.setSize(150, 20);
		portNumberLabel.setLocation((int) (GUITools.getCenterScreen().getX() - 100), 34);
		portNumberLabel.setFont(new Font(portNumberLabel.getFont().getFontName(), Font.BOLD, 13));
		
		portNumber = new JTextField("43500");
		portNumber.setSize(100, 30);
		portNumber.setLocation((int) (GUITools.getCenterScreen().getX() + 10), 30);
		portNumber.setToolTipText("Enter a port number for the battleship server to run on.");
		
		
		InteractiveButton start = new InteractiveButton("Start Server", "button.png", "button_hover.png");
		Point p = new Point((int) GUITools.getCenterScreen(start).getX(), 95);
		start.setLocation(p);
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				start();
			}
		});
		
		InteractiveButton terminate = new InteractiveButton("Terminate Server", "button.png", "button_hover.png");
		p = new Point((int) GUITools.getCenterScreen(terminate).getX(), 170);
		terminate.setLocation(p);
		terminate.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				terminate();
			}
		});
		
		console = new JTextArea();
		console.setSize(1000, 400);
		console.setLocation(new Point((int) GUITools.getCenterScreen(console).getX(), 280));
		console.setBorder(new LineBorder(Color.GRAY));
		console.setEditable(false);
		logConsole("Welcome, please enter a port number above then press start to activate the server.");
		
		JScrollPane consolePane = new JScrollPane(console);
		consolePane.setSize(1000, 400);
		consolePane.setLocation(new Point((int) GUITools.getCenterScreen(console).getX(), 280));

		this.add(consolePane);		
		this.add(portNumber);
		this.add(start);
		this.add(portNumberLabel);
		this.add(terminate);
	}
	
	/**
	 * Attempts to start a server instance
	 */
	private void start() {
		if (!Server.getInstance().isRunning()) {	
			try {
				int port = Integer.parseInt(portNumber.getText());
				if (port < 1 || port > 65535)
					throw new NumberFormatException();
				Server.getInstance().startServer(port);
				mainMenu.setVisible(false);
			} catch (NumberFormatException e) {
				logConsole("Error: Invalid port number specified!");
				logConsole("Server failed to start.");
				
			}
		} else {
			logConsole("Error! Server is already running.");
			return;
		}
	}
	
	/**
	 * Attempts to terminate the server
	 */
	private void terminate() {
		if (Server.getInstance().terminate()) {
			logConsole("Server successfully terminated.");
			mainMenu.setVisible(true);
		} else {
			logConsole("Error! There's no server instance currently running.");
		}
	}
	
	/**
	 * Logs a message to the console
	 * @param message
	 */
	public void logConsole(String message) {
		console.append("[" + new SimpleDateFormat("E K:mma").format(new Date()) + "]: " + message + "\n");
	}
	
	/**
	 * Logs a console error
	 * @param message
	 */
	public void logConsoleError(String message) {
		//TODO
	}

}
