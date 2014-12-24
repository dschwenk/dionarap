package dionarap;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.fhwgt.dionarap.model.data.DionaRapModel;
import de.fhwgt.dionarap.model.data.Grid;
import de.fhwgt.dionarap.model.data.MTConfiguration;
import de.fhwgt.dionarap.model.objects.AbstractPawn;
import de.fhwgt.dionarap.model.objects.Player;
import de.fhwgt.dionarap.controller.DionaRapController;

/**
 * Hauptfenster von DionaRap. Enthält die main()-Methode.
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 5
 */
public class Hauptfenster extends JFrame {
	
	/* The serializable class Hauptfenster does not declare a static final serialVersionUID field of type long */
	private static final long serialVersionUID = 1L;

	/* Titel */
	private static String titel = "DionaRap";
	
	/* Spielfeld */
	private Spielfeld spielfeld;
	private static int x = 10;
	private static int y = 10;
	private Grid grid = new Grid(y,x);
	
	private Toolbar toolbar;
	private MenuBar menubar;
	private Navigator navigator;	
	
	/* Model + Controller */
	private DionaRapModel DionaRap_Model;
	private DionaRapController DionaRap_Controller;
	
    /* Multithreading-Konfiguration */
    private static MTConfiguration MTConf = new MTConfiguration();
    /* Flag ob Spieleinstellungen angepasst wurden */
	private boolean game_settings_changed = false;
	
	/**
	 * Konstruktor von <code>Hauptfenster</code>
	 * Instanziiert das Spielfeld und das Navigationsfenster
	 */		
	public Hauptfenster(){
		
		/* Anwendung soll bei Exit beendet werden, Fenstertitel, statische Fenstergroesse, Layout */
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(titel);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		

		
		/* initialisiere DionaRap Model + Controller */
		this.initializeDionaRap();
		
		/* Groesse optimieren und Hauptfenster in Bildschirmmitte platzieren */
		this.pack();
		this.setLocationRelativeTo(null);
		
		/* Navigator hinzufuegen */
		this.navigator = new Navigator(this);
		
		/* Toolbar hinzufuegen */
		toolbar = new Toolbar(this);
		this.add(toolbar, BorderLayout.NORTH);
		
		/* Menuleiste hinzufuegen */
		this.setJMenuBar(menubar = new MenuBar(this));
		
		/* Listener fuer Bewegung des Fensters, Tastendruck, Mausklick  */
		this.addComponentListener(new ListenerFenster(navigator));
		this.addKeyListener(new ListenerKeyEvents());
		this.addMouseListener(new ListenerMaus(this));
		
		/* sichtbar machen */
		this.setVisible(true);
		this.requestFocus();		
	}
	
	
	/**
	 * Programmstart von DionaRap. 
	 * Initialisiert das Hauptfenster
	 * @param args Standardparameter (ungenutzt) 
	 */
	public static void main(String[] args) {	
		// erzeuge Hauptfenster
		new Hauptfenster();
	}


	/**
	 * Platziere Spielelemente auf dem Spielfeld. 
	 */
	public void setSpielfeldElements(){
		/* entferne zuerst alle Elemente vom Spielfeld*/
		this.spielfeld.removeIconsFromSpielfeld();
		/* frage alle Spielfiguren vom DionaRapModel ab und fuelle Array damit */
		AbstractPawn[] DionaRap_Pawns = this.DionaRap_Model.getAllPawns();
		/* setze alle Spielfiguren auf das Spielfeld */
		this.spielfeld.paintPawns(DionaRap_Pawns);
	}
	
	
	/**
	 * Initialisiert die Spielelogik (DionaRap Model und Controller), registriert
	 * den Listener fuer das Model und platziert die Figuren auf dem Spielfeld
	 */
	public void initializeDionaRap(){
		
		/* Initialisierung Model */
		// Defaultkonstruktor Initialisiert eine Spielfeldgroesse von 10x10 mit 4 Gegnern und 4 Hindernissen
		// (int gridSizeY, int gridSizeX, int opponentCount, int obstacleCount) 
		this.DionaRap_Model = new DionaRapModel(this.grid.getGridSizeY(),this.grid.getGridSizeX(),4,4);

		/* Listener fuer das Model registrieren */
		DionaRap_Model.addModelChangedEventListener(new ListenerModel(this,spielfeld));		
		
		/* erzeuge Spielfeld und fuege dieses zum Hauptfenster hinzu */
		this.spielfeld = new Spielfeld(this);
		this.add(spielfeld);
		/* platziere Figuren auf Spielfeld */
		this.setSpielfeldElements();
		
		/* Initialisierung Controller */
		this.DionaRap_Controller = new DionaRapController(DionaRap_Model);
		
		/* Multithreading Konfiguraiton initialisieren und aktivieren */
		if(game_settings_changed == false){
			this.initializeMTConfiguration();
		}
		DionaRap_Controller.setMultiThreaded(MTConf);
	}
	
	/**
	 * Initialisiert die Multithreading-Einstellungen
	 */
	private void initializeMTConfiguration(){
		MTConf.setAlgorithmAStarActive(true);
		MTConf.setAvoidCollisionWithObstacles(true);
		MTConf.setAvoidCollisionWithOpponent(true);
		MTConf.setMinimumTime(800);
		MTConf.setShotGetsOwnThread(true);
		MTConf.setOpponentStartWaitTime(5000);
		MTConf.setShotWaitTime(300);
		MTConf.setRandomOpponentWaitTime(false);
		MTConf.setOpponentWaitTime(1500);
		MTConf.setDynamicOpponentWaitTime(false);
	}
	
	/**
	 * Methode zur Darstellung des Dialogs fuer die Anzeige der
	 * Spielergebnisse ("Game Over" und "Gewonnen")
	 */
	public void drawGameResultDialog(boolean game_lost){
		/* Gewonnen / Verloren Icons, Ausgabestrings, Dialogrueckgabewert */
		String path_icons = "icons"+File.separator+"Dracula"+File.separator;
		ImageIcon gameover = new ImageIcon(path_icons + "gameover.gif");
		ImageIcon win = new ImageIcon(path_icons + "win.gif");		
		int playerchoice;
		String[] playerchoicestrings = {"Neues Spiel", "Abbrechen"};
		
		/* Zeichne Dialog */
		if(game_lost){
			/*
			result = JOptionPane.showOptionDialog(
				this,	//parent component
				"Game Over",	// object message
				"Spiel verloren!",	// string title
				JOptionPane.YES_NO_OPTION,	// int optiontype
				JOptionPane.PLAIN_MESSAGE,	// int messagetype
				gameover,	// icon
				playerchoicestrings, // object[] options
				"Neues Spiel");	// object initial value
			*/
			playerchoice = JOptionPane.showOptionDialog(this, "Game Over", "Spiel verloren!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, gameover, playerchoicestrings, "Neues Spiel");
		}
		else {
			playerchoice = JOptionPane.showOptionDialog(this, "Gewonnen", "Spiel gewonnen!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, win, playerchoicestrings, "Neues Spiel");
		}
		/* Werte gedrueckten Dialogbutton aus */
		if(playerchoice == 0){
			/* neues Spiel -> Model und Controller neu initialisieren + Spielfeld neu darstellen */
			this.initializeDionaRap();
			/* Button "Neues Spiel" deaktivieren, Spielstand / Fortschritt zuruecksetzen */
			this.getToolbar().setButtonNSDisabled();
			this.getToolbar().setScoreFieldText(0);
			this.getToolbar().setProgressBarValue(0);				
		}
		else {
			this.getToolbar().setButtonNSEnabled();
		}
	}


	/**
	 * Methode um die Toolbar zu positionieren
	 * @param top zeige Toolbar oben (true) oder unten an (false)
	 */
	public void setToolbarPosition(boolean top){
		/* Toolbar oben anzeigen */
		if(top){
			this.remove(toolbar);
			this.add(toolbar, BorderLayout.NORTH);
			this.pack();
		}
		/* Toolbar unten anzeigen */
		else {
			this.remove(toolbar);
			this.add(toolbar, BorderLayout.SOUTH);
			this.pack();			
		}
	}
	

	/**
	 * Gibt den Controller zurueck. 
	 * @return DionaRapController
	 */
	public DionaRapController getDionaRapController(){
		return DionaRap_Controller;
	}

	/**
	 * Gibt das Model zurueck. 
	 * @return DionaRapModel
	 */
	public DionaRapModel getDionaRapModel(){
		return DionaRap_Model;
	}
	
	/**
	 * Gibt Array mit Spielfiguren zurueck. 
	 * @return all Pawns
	 */
	public AbstractPawn[] getPawns(){
		return this.DionaRap_Model.getAllPawns();
	}
	
	/**
	 * Gibt Spieler zurueck. 
	 * @return Spieler
	 */
	public Player getPlayer(){
		return this.DionaRap_Model.getPlayer();
	}
	
	/**
	 * Gibt Toolbar zurueck. 
	 * @return Toolbar
	 */
	public Toolbar getToolbar(){
		return this.toolbar;
	}
	
	/**
	 * Gibt Spielfeld zurueck. 
	 * @return Spielfeld
	 */
	public Spielfeld getSpielfeld(){
		return this.spielfeld;
	}
	
	/**
	 * Gibt Navigator zurueck. 
	 * @return Navigator
	 */
	public Navigator getNavigator(){
		return this.navigator;
	}	
	

	/**
	 * Gibt Multithreading Konfiguration zurueck. 
	 * @return MTConf
	 */
	public MTConfiguration getMTConfiguration(){
		return Hauptfenster.MTConf;
	}		
	
	/**
	 * Gibt den aktuellen Spielfortschritt zurueck
	 * @return int progress
	 */
	public int getGameProgress(){
		// Berechnet die Prozent an restlichen Gegnern
		// Standard 4 Gegner --> TODO 4 durch Konstante / Variable ersetzen
		float progress = ((4 - (float)DionaRap_Model.getOpponentCount()) / 4) * 100;
		return (int)progress;
	}
	
	/**
	 * Gibt das Spielverzeichnis zurueck
	 * @return String spieleverzeichnis
	 */
	public static String getGameDirectory(){
		String gamedirectory = System.getProperty("user.dir");
		String separator = System.getProperty("file.separator");
		return (gamedirectory + separator);
	}
}
