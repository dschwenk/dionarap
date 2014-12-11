package dionarap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import de.fhwgt.dionarap.controller.DionaRapController;


/**
 * Klasse realisiert den Listener der auf einen Schuss reagiert. Die Klasse implementiert
 * das Interface <code>ActionListener</code>. 
 * @author Daniel Schwenk
 * @version Aufgabe 4
 */
public class ListenerWaffe implements ActionListener {

	/**
	 * Eventhanlder fuer das Druecken der Bewegungsbuttons <code>actionPerfomred</code>
	 * Schusstaste 5
	 * @param e Button der das Event ausgeloest hat
	 */
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		/* benoetige DionaRapController um zu Schiessen */
		Hauptfenster hauptfenster = (Hauptfenster) button.getTopLevelAncestor().getParent();
		DionaRapController dr_controller = (DionaRapController) hauptfenster.getDionaRapController();
		
		dr_controller.shoot();
		
        System.out.println("Shoot " + button.getActionCommand());
		hauptfenster.requestFocus();
	}
}
