package dionarap;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Klasse realisiert den Listener fuer die Bewegung des Navigators. Die Klasse implementiert
 * dazu das Interface <code>ComponentListener</code>. 
 *   
 * @author Daniel Schwenk
 * @version Aufgabe 4
 */
public class ListenerFenster implements ComponentListener {
	
	private Navigator navigator;
	
	/**
	 * Konstruktor vom Typ <code>ListenerComponentEvents</code>
	 * @param navigator Navigationsfenster vom Typ <code>Navigator</code>
	 */
	public ListenerFenster(Navigator navigator) {
		this.navigator = navigator;
	}
	
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub		
	}

	/**
	 * Eventhandler fuer das Event <code>componentMoved</code>,
	 * Wird die Position des Hauptfensters veraendert sorgt dieser
	 * Listener dafuer, das der Navigator neu angeordnet wird
	 */
	public void componentMoved(ComponentEvent e) {
		/* rufe Funktion zum Setzen der Position auf */
		this.navigator.setNavPosition();		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub		
	}
}
