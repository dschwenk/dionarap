package dionarap;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * Klasse realisiert die Buttons zur Steuerung, abgeleitet von <code>JPanel</code>
 * 
 * @author Daniel Schwenk und Fabian Frick
 * @version Aufgabe 7
 */
public class Tastatur extends JPanel {

	/**
	 * Konstruktor der Tastatur vom Typ <code>JPanel</code>
	 * Legt das Layout fest und ordnet die Steuertasten entprechend des Nummernblocks der Tastatur an.
	 */		
	public Tastatur(){
		
		/* 
		 * Rufe Konstruktor auf, erzeuge JPanel mit GridLayout
		 * GridLayout(int AnzahlZeilen, int AnzahlSpalten, int hAbstand, int vAbstand)
		 */
		super(new GridLayout(3,3,0,0));
		
		/* Elemte von rechts nach links dem Layout hinzufuegen */
		this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		
		/* Fuege Buttons hinzu */
		addButtons();		
	}


	/**
	 * Methode zum erzeugen von Buttons
	 */
	private void addButtons(){
		// String mit Pfad zu Icons
		String path_icons = "icons"+File.separator+"Navigator"+File.separator;
		
		/* Stringarray mit den Namen der Tastaturbuttons */
		String [] button_names = {"taste1.gif", "taste2.gif", "taste3.gif", "taste4.gif", "taste5.gif", "taste6.gif", "taste7.gif", "taste8.gif", "taste9.gif"};		

		/* Array mit Buttons */
		JButton tasten[] = new JButton[10];
		
		/* setze Icons, Groesse, ActionCommand, Abstand und Listener une fuege Button dem Layout hinzu */
		for(int i=8;i>=0;i--){
			tasten[i] = new JButton();
			tasten[i].setIcon(new ImageIcon(path_icons+button_names[i]));
			tasten[i].setActionCommand(String.valueOf(i+1));			
			tasten[i].setPreferredSize(new Dimension(50, 50));
			tasten[i].setMargin(new Insets(0, 0, 0, 0));
			// Schusstaste (Position 5 -> 8 7 6 5 4
			if(i == 4){
				tasten[i].addActionListener(new ListenerWaffe());
			}
			else {
				tasten[i].addActionListener(new ListenerBewegung());
			}
			this.add(tasten[i]);
		}
	}
}