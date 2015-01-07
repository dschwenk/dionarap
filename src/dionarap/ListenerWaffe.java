package dionarap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import de.fhwgt.dionarap.controller.DionaRapController;


/**
 * Klasse realisiert den Listener der auf einen Schuss reagiert. Die Klasse implementiert
 * das Interface <code>ActionListener</code>. 
 * @author Daniel Schwenk
 * @version Aufgabe 7
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
		
		/* pruefe ob Munition vorhanden - falls nicht erzeuge Thread um Anzeige zum Blinken zu bringen */
		if(hauptfenster.getDionaRapModel().getShootAmount() == 0){
			/* erzeuge neuen Thread falls dieser noch nicht besteht */
			if(hauptfenster.getThreadt_ammo() == null){
				hauptfenster.createThreadt_ammo();
			}
			/* oder nicht mehr aktiv ist */
			else if(!(hauptfenster.getThreadt_ammo().isAlive())){
				hauptfenster.createThreadt_ammo();				
			}
		}

		/* Sound fuer Schuss sofern Munition vorhanden*/
		if(hauptfenster.getDionaRapModel().getShootAmount() >= 0 || hauptfenster.getDionaRapModel().getShootAmount() == -1){
			hauptfenster.getSounds().playSoundShoot();
		}		

		dr_controller.shoot();

        System.out.println("Shoot " + button.getActionCommand());
		hauptfenster.requestFocus();
	}
}