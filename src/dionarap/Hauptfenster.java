package dionarap;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.fhwgt.dionarap.model.data.DionaRapModel;
import de.fhwgt.dionarap.model.objects.AbstractPawn;
import de.fhwgt.dionarap.model.objects.Player;
import de.fhwgt.dionarap.controller.DionaRapController;

/**
 * Hauptfenster von DionaRap. Enthält die main()-Methode.
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 4
 */
public class Hauptfenster extends JFrame {
	
	/* The serializable class Hauptfenster does not declare a static final serialVersionUID field of type long */
	private static final long serialVersionUID = 1L;

	/* Titel */
	private static String titel = "DionaRap";
	
	/* Spielfeld */
	private Spielfeld spielfeld;
		
	/* Toolbar */
	private Toolbar toolbar;
	
	/* Model + Controller */
	private DionaRapModel DionaRap_Model;
	private DionaRapController DionaRap_Controller;	
	

	
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
		
		/* erzeuge Spielfeld und fuege dieses zum Hauptfenster hinzu */
		this.spielfeld = new Spielfeld();
		this.add(spielfeld);
		
		/* initialisiere DionaRap Model + Controller */
		this.initializeDionaRapMC();
		
		/* Groesse optimieren und Hauptfenster in Bildschirmmitte platzieren */
		this.pack();
		this.setLocationRelativeTo(null);
		
		/* Navigator hinzufuegen */
		Navigator navigator = new Navigator(this);
		
		/* Toolbar hinzufuegen */
		toolbar = new Toolbar(this);
		this.add(toolbar, BorderLayout.NORTH);
		
		/* Listener fuer Bewegung des Fensters + Tastendruck  */
		this.addComponentListener(new ListenerFenster(navigator));
		this.addKeyListener(new ListenerKeyEvents());
		
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
		this.spielfeld.clearGame();
		/* frage alle Spielfiguren vom DionaRapModel ab und fuelle Array damit */
		AbstractPawn[] DionaRap_Pawns = this.DionaRap_Model.getAllPawns();
		/* setze alle Spielfiguren auf das Spielfeld */
		this.spielfeld.paintPawns(DionaRap_Pawns);
	}
	
	
	/**
	 * Initialisiert die Spielelogik (DionaRap Model und Controller), registriert
	 * den Listener fuer das Model und platziert die Figuren auf dem Spielfeld
	 */
	public void initializeDionaRapMC(){
		/* Initialisierung Model */
		// Defaultkonstruktor Initialisiert eine Spielfeldgroesse von 10x10 mit 4 Gegnern und 4 Hindernissen
		// (int gridSizeY, int gridSizeX, int opponentCount, int obstacleCount) 
		this.DionaRap_Model = new DionaRapModel();

		/* Listener fuer das Model registrieren */
		DionaRap_Model.addModelChangedEventListener(new ListenerModel(this,spielfeld));		
		
		/* platziere Figuren auf Spielfeld */
		this.setSpielfeldElements();
		
		/* Initialisierung Controller */
		this.DionaRap_Controller = new DionaRapController(DionaRap_Model);
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
			this.initializeDionaRapMC();
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
	 * Gibt den aktuellen Spielfortschritt zurueck
	 * @return int progress
	 */
	public int getGameProgress(){
		// Berechnet die Prozent an restlichen Gegnern
		// Standard 4 Gegner --> TODO 4 durch Konstante / Variable ersetzen
		float progress = ((4 - (float)DionaRap_Model.getOpponentCount()) / 4) * 100;
		return (int)progress;
	}
}
