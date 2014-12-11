package dionarap;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.fhwgt.dionarap.controller.DionaRapController;


/**
 * Klasse realisiert den Listener fuer Tastaturereignisse. Die Klasse implementiert
 * dazu das Interface <code>KeyListener</code>. 
 *   
 * @author Daniel Schwenk
 * @version Aufgabe 4
 */
public class ListenerKeyEvents implements KeyListener {

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub		
	}

    /**
     * Eventhandler fuer das Event <code>keyTyped</code>,
     * Events zu den Zifferntasten 1-9 werden behandlet
     * 1-4 + 6-9  fuer die Bewegung,
     * 5 zum Schiessen
     */
	public void keyTyped(KeyEvent e) {
        Hauptfenster hauptfenster = (Hauptfenster) e.getSource();
        DionaRapController dr_controller = hauptfenster.getDionaRapController();

        /* Taste 5 - Schuss */
        if(e.getKeyChar() == '5'){
        	dr_controller.shoot();
            System.out.println("Shot " + e.getKeyChar());
        }
        /* Taste 1-4 + 6-9 */
        else if(e.getKeyChar() != '5' &&  ('1' <= e.getKeyChar() && e.getKeyChar() <= '9')){
        	/* bewege Spieler in entsprechende Richtun */
        	dr_controller.movePlayer(Character.getNumericValue(e.getKeyChar()));
            System.out.println("Move " + e.getKeyChar());
        }
        /* beliebige andere Taste */
        else {
            System.out.println(e.getKeyChar());
        }		
	}
}