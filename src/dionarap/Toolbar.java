package dionarap;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;


/**
 * Klasse realisiert die Toolbar, abgeleitet von <code>JToolBar</code>
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 5
 */
public class Toolbar extends JToolBar {
	
	private static final long serialVersionUID = 1L;
	
	Hauptfenster hauptfenster;

	/* Toolbarelemente */
	JButton neuesspiel;
	JPanel punktestand;
	JTextField punktestandtext;
	JPanel munition;
	JPanel spielfortschritt;
	JProgressBar fortschrittsbalken;
	JButton bestenliste;
	
	
	/**
	 * Konstruktor der Toolbar vom Type <code>JToolBar</code>
	 * Erstellt eine Toolbar mit Punktestand, Spielfortschritt, Munitionsanzeige etc.
	 */
	Toolbar(Hauptfenster hauptfenster){
		this.hauptfenster = hauptfenster;
		
		/* Toolbar soll vom Benutzer nicht bewegent werden koennen */
		this.setFloatable(false);
		
		/* Button neues Spiel */		
		neuesspiel = new JButton("Neues Spiel");
		neuesspiel.setActionCommand("newgame");
		/* Button ist nur aktiv wenn das Spiel gewonnen / verloren wurde */
		neuesspiel.setEnabled(false);
		neuesspiel.addActionListener(new ListenerToolbar());
		this.add(neuesspiel);
		

		/* Feld Punktestand */
        punktestand = new JPanel();
        /* Rahmenbeschriftung, Tooltiptext */
        punktestand.setBorder(BorderFactory.createTitledBorder("Punktestand"));
        punktestand.setToolTipText("Zeigt den aktuellen Punktestand an");
        
        /* Punktestandtext */
        punktestandtext = new JTextField();
        /* Textfeld ist nicht editierbar, Breite von 5 Spalten, aktuellen Punktestand setzen */
        punktestandtext.setEditable(false);
        punktestandtext.setColumns(5);
        setScoreFieldText(hauptfenster.getDionaRapModel().getScore());
        
        /* Text zum Panel, Panel zur Toolbar hinzufuegen */
        punktestand.add(punktestandtext);
        this.add(punktestand);


		/* Munitionsanzeige */
        munition = new JPanel();
        munition.setToolTipText("Zeigt die verfuegbare Munition an");
        munition.setBorder(BorderFactory.createTitledBorder("Munition"));
        munition.setPreferredSize(new Dimension(50, 30));
        this.add(munition);


		/* Spielfortschritt */
        spielfortschritt = new JPanel();
        /* Rahmenbeschriftung, Tooltiptext */
        spielfortschritt.setToolTipText("Zeigt den aktuellen Spielfortschritt an");
        spielfortschritt.setBorder(BorderFactory.createTitledBorder("Spielfortschritt"));
        /* Fortschrittsbalken initialisieren mit min / max Werte */
        fortschrittsbalken = new JProgressBar(0,100);
        /* Wert setzen */
        fortschrittsbalken.setValue(hauptfenster.getGameProgress());
        spielfortschritt.add(fortschrittsbalken);
        this.add(spielfortschritt);
        
        
		/* Bestenliste */
		bestenliste = new JButton("Bestenlist");
		bestenliste.setActionCommand("bestenliste");
		bestenliste.addActionListener(new ListenerToolbar());
		this.add(bestenliste);
	}
	
	/**
	 * Zeige Bestenliste an
	 */
	public void showBestenliste(){
	
		/* Spaltenname der Bestenlistetabelle */
		String[] columnNames = {"Spielername", "Punkte", "Rang"};
		JFrame frameBestenliste = new JFrame("Bestenliste");
		String [] StringArrHighscore = null;
		
		// Auslesen der Bestenliste
		try{
			StringArrHighscore = HighScoreFile.getInstance().readFromHighscore();
		} catch (IOException ex) {
		    System.err.println("Highscoredatei kann nicht gelesen werden: " + ex);
		}
		
		// 2-dimensoniales Array das der JTable uebergeben wird
		String [][] arr_list2d = new String [10][3];
		int entrycounter = 0;
		for (int i = 0; i < 10; i++) { // fuellen des 2D-Arrays
		    for(int k = 0; k < 2; k++) {
		        arr_list2d[i][k] = StringArrHighscore[entrycounter];
		        entrycounter++;
		    }
		    arr_list2d[i][2] = String.valueOf(i+1);
		}
		/* lege Tabelle an */
		JTable table = new JTable(arr_list2d, columnNames);
		frameBestenliste.add(new JScrollPane(table));
		frameBestenliste.setLocationRelativeTo(hauptfenster);
		frameBestenliste.setSize(300,300);
		frameBestenliste.setVisible(true);    
	}
	

	/**
	 * Methode um den Button "Neues Spiel" zu aktivieren
	 * (aktiv wenn Spiel gewonnen / verloren wurde)
	 */
    public void setButtonNSEnabled() {
    	neuesspiel.setEnabled(true);
    }
   

	/**
	 * Methode um den Button "Neues Spiel" zu deaktivieren
	 * (aktiv wenn Spiel gewonnen / verloren wurde)
	 */
    public void setButtonNSDisabled() {
    	neuesspiel.setEnabled(false);
    }


	/**
	 * Methode um den aktuellen Punktestand zu setzen
	 * @param int punktestand aktueller Punktestand
	 */
    public void setScoreFieldText(int punktestand) {
    	punktestandtext.setText(String.valueOf(punktestand));
    }

   
	/**
	 * Methode um den aktuellen Fortschritt zu setzen
	 * @param int progress aktueller Fortschritt
	 */
	public void setProgressBarValue(int progress) {
		fortschrittsbalken.setValue(progress);
	}
	
	/**
	 * Methode macht die Toolbar sichtbar
	 */
	public void showToolbar(){
		this.setVisible(true);
	}
	
	/**
	 * Methode macht die Toolbar unsichtbar
	 */
	public void hideToolbar(){
		this.setVisible(false);
	}
	
}