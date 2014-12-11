package dionarap;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import de.fhwgt.dionarap.controller.DionaRapController;


/**
 * Klasse realisiert den Listener fuer die Bewegungsbuttons. Die Klasse implementiert
 * dazu das Interface <code>ActionListener</code>. 
 *   
 * @author Daniel Schwenk
 * @version Aufgabe 4
 */
public class ListenerBewegung implements ActionListener {

	/**
	 * Eventhanlder fuer das Druecken der Bewegungsbuttons <code>actionPerfomred</code>
	 * Bewegungsbuttons 1-4 & 6-9
	 * @param e Button der das Event ausgeloest hat
	 */
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		/* benoetige DionaRapController um Spieler zu bewegen */
		Hauptfenster hauptfenster = (Hauptfenster) button.getTopLevelAncestor().getParent();
		DionaRapController dr_controller = (DionaRapController) hauptfenster.getDionaRapController();
		/* bewege Spieler in entsprechende Richtung */
		dr_controller.movePlayer(Integer.parseInt(button.getActionCommand()));

        System.out.println("Move " + button.getActionCommand());
		hauptfenster.requestFocus();
	}
}
