package dionarap;

import java.awt.BorderLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;


/**
 * Klasse realisiert das Navigationsfenster, abgeleitet von <code>JWindow</code>
 * 
 * @author Daniel Schwenk und Fabian Frick
 * @version Aufgabe 7
 */
public class Navigator extends JWindow {

	/**
	 * Konstruktor des Navigationsfensters vom Typ <code>JWindow</code>
	 * @param parent Vaterfenster vom Typ <code>Hauptfenster</code>
	 */	
	public Navigator(JFrame parent){
		
		/* Konstruktor des Vaterfenster */
		super(parent);
		
		/* Polygon mit einer Achteckform initialisieren */
		Polygon achteck = newPolygon();
		/* Achteckform festlegen */
		this.setShape(achteck);		
		
		/* Rahmen erstellen und rot faerben + Rahmendicke - noetig da JWindow keine Rahmen besitzt */
		JPanel rahmen = new JPanel();
		rahmen.setLayout(new BorderLayout());
		// rahmen.setBorder(BorderFactory.createLineBorder(Color.red,1));
		
		/* Tastaturblock zu JPanel hinzufuegen */
		rahmen.add(new Tastatur());
		
		// Rahmen zu JWindow hinzufuegen
		this.getContentPane().add(rahmen);
		
		/* Groesse optimieren */
		this.pack();
		
		/* Tastaturblock rechts neben Spielfeld platzieren */
		this.setNavPosition();
		
		this.setVisible(true);
	}
	
	
	/**
	 * Setze die Position des Navigationsfensters
	 */	
	public void setNavPosition(){
		/* getBounds liefert eine Rectangle Instanz mit Position, Breite und Höhe zurück */
		Rectangle bounds = this.getParent().getBounds(); 
		this.setLocation((bounds.x + bounds.width + 40), bounds.y);		
	}
	

	/**
	 * Methode zum erzeugen des Polygons fuer das Achteck
	 * @return Polygon mit achteckform
	 */
	private Polygon newPolygon(){
		Polygon achteck = new Polygon();
		// Punkte festlegen
		achteck.addPoint(50, 0);
		achteck.addPoint(100, 0);
		achteck.addPoint(150, 50);
		achteck.addPoint(150, 100);
		achteck.addPoint(100, 150);
		achteck.addPoint(50, 150);
		achteck.addPoint(0, 100);
		achteck.addPoint(0, 50);
		return achteck;
	}
	
	
	/**
	 * Methode macht den Navigator sichtbar
	 */
	public void showNavigator(){
		this.setVisible(true);
	}


	/**
	 * Methode macht den Navigator unsichtbar
	 */
	public void hideNavigator(){
		this.setVisible(false);
	}
}