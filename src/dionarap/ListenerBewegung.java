package dionarap;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import de.fhwgt.dionarap.controller.DionaRapController;


/**
 * Klasse realisiert den Listener fuer die Bewegungsbuttons. Die Klasse implementiert
 * dazu das Interface <code>ActionListener</code>. 
 *   
 * @author Daniel Schwenk und Fabian Frick
 * @version Aufgabe 7
 */
public class ListenerBewegung implements ActionListener {

	/**
	 * Eventhanlder fuer das Druecken der Bewegungsbuttons <code>actionPerfomred</code>
	 * Bewegungsbuttons 1-4 und 6-9
	 * @param e Button der das Event ausgeloest hat
	 */
	public void actionPerformed(ActionEvent e){
		JButton button = (JButton) e.getSource();

		/* benoetige DionaRapController um Spieler zu bewegen */
		Hauptfenster hauptfenster = (Hauptfenster) button.getTopLevelAncestor().getParent();
		DionaRapController dr_controller = (DionaRapController) hauptfenster.getDionaRapController();

		/* akutelle Spielerposition */
		int playerposition_x = hauptfenster.getPlayer().getX();
		int playerposition_y = hauptfenster.getPlayer().getY();

		/* bewege Spieler in entsprechende Richtung */
		dr_controller.movePlayer(Integer.parseInt(button.getActionCommand()));

		/* gueltige Bewegung - stoppe blinken der Felder */
		if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
			hauptfenster.stopThreadt_field();
		}		
		else {
			/* Sound fuer Bewegung */
			hauptfenster.getSounds().playSoundMove();			
		}

		/* hat sich die Spielerposition zur vorherigen Positiongeaendert */
		if((playerposition_x == hauptfenster.getPlayer().getX()) && playerposition_y == (hauptfenster.getPlayer().getY())){
            hauptfenster.createThreadt_field();
        }

        System.out.println("Move " + button.getActionCommand());
		hauptfenster.requestFocus();
	}
}