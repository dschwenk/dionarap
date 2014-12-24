package dionarap;

import de.fhwgt.dionarap.model.events.DionaRapChangedEvent;
import de.fhwgt.dionarap.model.events.GameStatusEvent;
import de.fhwgt.dionarap.model.listener.DionaRapListener;


/**
 * Klasse realisiert den Listener fuer Events die durch das Aendern des DionaRapModels entstehen und implementiert
 *  das Interface <code>DionaRapListener</code>. 
 * @author Daniel Schwenk
 * @version Aufgabe 4
 */
public class ListenerModel implements DionaRapListener {

	/* Instanz + Spielfeld */
	Hauptfenster hauptfenster;
	Spielfeld spielfeld;
	
	/* Status Spiel laeuft / wurde gewonnen */
	boolean game_running = true;
	boolean game_lost = false;

	
    /**
     * Konstruktor der Klasse ListenerFenster
     *
     * @param Hauptfenster und das Spielfeld
     */
    ListenerModel(Hauptfenster hauptfenster, Spielfeld spielfeld) {
        this.hauptfenster = hauptfenster;
        this.spielfeld = spielfeld;
    }    
    
	
	/**
	 * Eventhandler fuer das Event <code>modelChanged</code>.
	 * Bei Veraenderungen am <code>DionaRapModel</code> wird das Spielfeld aktualisiert
	 * @param e Aenderungsereignis vom Typ <code>DionaRapChangedEvent</code>
	 */
	public void modelChanged(DionaRapChangedEvent e) {
		/* Spiel laueft */
		if(game_running){
			/* Zeichne Figuren neu, setze aktuellen Spielstand / Fortschritt */
			hauptfenster.setSpielfeldElements();
			hauptfenster.getToolbar().setScoreFieldText(hauptfenster.getDionaRapModel().getScore());
			hauptfenster.getToolbar().setProgressBarValue(hauptfenster.getGameProgress());
		}
	}


	/**
	 * Eventhandler fuer das Event <code>statusChanged</code>.
	 * Event tritt auf wenn das Spiel gewonnen / verloren wurde
	 * @param e Spielstatusereignis vom Typ <code>GameStatusEvent</code>
	 */
	public void statusChanged(GameStatusEvent e) {
		game_running = false;
		/* Spiel wurde gewonnen */
		if(e.isGameWon()){
			game_lost = false;
			System.out.println("Spiel gewonnen");
		}
		/* Spiel wurde verloren */
		else {
			game_lost = true;
			System.out.println("Spiel verloren");			
		}

		/* setze abschliessend Figuren + spezielle Spielerfigur (Gewinner/Verlierer Spielerfigur) */
		hauptfenster.setSpielfeldElements();
		hauptfenster.getToolbar().setScoreFieldText(hauptfenster.getDionaRapModel().getScore());
		hauptfenster.getToolbar().setProgressBarValue(hauptfenster.getGameProgress());
		// TODO fix
		//spielfeld.gameStatusEnd(hauptfenster.getPlayer(), game_lost);
		
		/* zeige Gewonnen / Verloren Dialog an */ 
		hauptfenster.drawGameResultDialog(game_lost);
	}
}