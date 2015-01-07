package dionarap;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;


/**
 * Klasse realisiert den Thread der fuer das Blinken 
 * der Munitionsanzeige zustaendig ist, abgleitet von <code>Thread /code>
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 7
 */
class ThreadAmmo extends Thread {
	
	private Hauptfenster hauptfenster;
	private static final int blinkDelay = 500;


	/**
	 * Konstruktor
	 * @param hauptfenster
	 */
	ThreadAmmo(Hauptfenster hauptfenster) {
		this.hauptfenster = hauptfenster;
	}


	/**
	 * Methode ist fuer das Blinken der Munitionsanzeige zustaendig
	 */
    public void run(){
		/* so lange Munition == 0 und noch keine 3 Sekunden vergangen sind */
		for(int i=0;(i<6) && (hauptfenster.getDionaRapModel().getShootAmount()== 0);i++){
			JLabel[] ammo = hauptfenster.getToolbar().getMuniJLabelArr();
			for(int k=0;k<3;k++){
				/* abwechselnd Rand hinzufuegen / entfernen */
				if((i % 2) == 0){
				    ammo[k].setBorder(BorderFactory.createLineBorder(Color.RED));
				}
				else {
				    ammo[k].setBorder(BorderFactory.createEmptyBorder());
				}
				hauptfenster.getToolbar().getMuniJPanel().updateUI();
			}
			try {
				/* Thread schlafen legen */
				Thread.sleep(blinkDelay);
			} catch (InterruptedException ex) {
				System.out.println("Catched exception in Thread Ammo.");
			}
		}
    }
}