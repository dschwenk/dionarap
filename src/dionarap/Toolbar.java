package dionarap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
 * @version Aufgabe 7
 */
public class Toolbar extends JToolBar {
	
	private static final long serialVersionUID = 1L;
	
	Hauptfenster hauptfenster;

	/* Toolbarelemente */
	private JButton neuesspiel;
	private JPanel punktestand;
	private JTextField punktestandtext;
	private JPanel munition;
	private JLabel munition_arr[] = new JLabel[3];
	private int ammoCounter = 0;
	private JPanel spielfortschritt;
	private JProgressBar fortschrittsbalken;
	private JButton bestenliste;
	
	
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
        setScoreFieldText();
        
        /* Text zum Panel, Panel zur Toolbar hinzufuegen */
        punktestand.add(punktestandtext);
        this.add(punktestand);


		/* Munitionsanzeige */
        munition = new JPanel();
        munition.setToolTipText("Zeigt die verfuegbare Munition an");
        munition.setBorder(BorderFactory.createTitledBorder("Munition"));
        munition.setLayout(new GridLayout(1, 3, 3, 3));
        for(int i=0;i<3;i++){
        	munition_arr[i] = new JLabel();
        	munition_arr[i].setPreferredSize(new Dimension(30,30));
        }
        paintMunitionsAnzeige();
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
        fortschrittsbalken.setPreferredSize(new Dimension(100,20));
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
	 * Methode stellt die Munitionsanzeige in der Toolbar dar
	 */
	public void paintMunitionsAnzeige(){
		String theme = hauptfenster.getTheme();
		String pathIcon = "icons"+File.separator+theme+File.separator + "ammo.png";
		ImageIcon icon_munition = new ImageIcon(pathIcon);
		int ammocount = hauptfenster.getDionaRapModel().getShootAmount();
		
		/* hat sich die Munitionsanzahl veraendert */
		if(ammocount != this.ammoCounter){
			/* aktualisiere Zaehler + setze Array zurueck*/
			this.ammoCounter = ammocount;
			for(int i=0;i<3;i++){
				munition_arr[i].setText(null);
				munition_arr[i].setIcon(null);
				munition_arr[i].setBorder(null);
				munition.remove(munition_arr[i]);
				munition.add(munition_arr[i]);
			}
			/* Anzahl an Munition < 0 -> unendlich -> zeige alle Icons an */
			if(ammocount < 0){				
				munition_arr[0].setBorder(null);
				munition_arr[0].setText("   *99");
				munition.add(munition_arr[0]);
				for(int i=1;i<3;i++){
					munition_arr[i].setIcon(icon_munition);
					munition_arr[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
					munition.add(munition_arr[i]);
				}
			}
			/* Anzahl an Munition <= 3 && >= 0 -> zeige Anzahl x an Icons an */
			else if(ammocount <= 3 && ammocount >= 0){
				for(int i=0;i<ammocount;i++){
					munition_arr[i].setIcon(icon_munition);
					munition_arr[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
					munition.add(munition_arr[i]);
				}
			}
			/* Anzahl an Munition > 3 -> zeige Zahl + 2 Icons an */
			else {
				munition_arr[0].setBorder(null);
				munition_arr[0].setText("*" + String.valueOf(this.ammoCounter));
				munition.add(munition_arr[0]);
				munition_arr[1].setIcon(icon_munition);
				munition_arr[1].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				munition.add(munition_arr[1]);
				munition_arr[2].setIcon(icon_munition);
				munition_arr[2].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				munition.add(munition_arr[2]);				
			}
		}
		/* Anzeige an Look & Fell anpassen */
		munition.updateUI();
	}


	public void updateToolbar(){
		setScoreFieldText();
		setProgressBarValue();
		paintMunitionsAnzeige();
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
	 */
    public void setScoreFieldText(){
    	punktestandtext.setText(String.valueOf(hauptfenster.getDionaRapModel().getScore()));
    }

  
	/**
	 * Methode um den aktuellen Fortschritt zu setzen
	 */
	public void setProgressBarValue(){
		fortschrittsbalken.setValue(hauptfenster.getGameProgress());
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
	

	/**
	 * Methode gibt das Array fuer die Labels der Munitionsanzeige zurueck
	 * @return JLabel[]
	 */
	public JLabel[] getMuniJLabelArr(){
		return munition_arr;
	}


	/**
	 * Methode gibt das Panel fuer die Munitionsanzeige zurueck
	 * @return JPanel gibt das JPanel der Munitionsanzeige zurueck
	 */
	public JPanel getMuniJPanel(){
		return munition;		
	}	
}