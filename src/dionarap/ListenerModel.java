package dionarap;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import de.fhwgt.dionarap.model.events.DionaRapChangedEvent;
import de.fhwgt.dionarap.model.events.GameStatusEvent;
import de.fhwgt.dionarap.model.listener.DionaRapListener;


/**
 * Klasse realisiert den Listener fuer Events die durch das Aendern des DionaRapModels entstehen - implementiert
 * das Interface <code>DionaRapListener</code>. 
 * @author Daniel Schwenk
 * @version Aufgabe 7
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
			/* Zeichne Figuren neu, setze aktuellen Spielstand / Fortschritt / Munitionsanzeige */
			hauptfenster.setSpielfeldElements();
			hauptfenster.getToolbar().updateToolbar();
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
			this.hauptfenster.getSounds().playSoundGameWon();
		}
		/* Spiel wurde verloren */
		else {
			game_lost = true;
			System.out.println("Spiel verloren");
			this.hauptfenster.getSounds().playSoundGameOver();
		}

		/* setze abschliessend Figuren + spezielle Spielerfigur (Gewinner/Verlierer Spielerfigur) */
		hauptfenster.setSpielfeldElements();
		hauptfenster.getToolbar().updateToolbar();
		spielfeld.gameStatusEnd(hauptfenster.getPlayer(), game_lost);
		
		/* in Highscore eintragen */
		if(game_lost == false){
			int position = HighScoreFile.SCORE_TO_LOW_FOR_HIGHSCORE;
			try {
				position = HighScoreFile.getInstance().getScorePosition(hauptfenster.getDionaRapModel().getScore());
			}
			catch(IOException ex){
				System.err.println("File kann nicht gelesen werden: " + ex);
			}
			/* pruefe ob Spieler in Bestenliste kommt */
			if(position != HighScoreFile.SCORE_TO_LOW_FOR_HIGHSCORE){
				addPlayerToBestenliste();
			}			
		}

		
		/* zeige Gewonnen / Verloren Dialog an */ 
		hauptfenster.drawGameResultDialog(game_lost);
	}

	
	/**
	 * Methode zeigt Dialog zum Eintagen in die Bestenliste an und schreibt
	 * Punkte + Name in Bestenlistendatei
	 */
	private void addPlayerToBestenliste(){
		String theme = hauptfenster.getTheme();
		String pathIcon = "icons"+File.separator+theme+File.separator + "win.gif";
        String[] choices = {"Eintragen", "Abbrechen"};
        ImageIcon win = new ImageIcon(pathIcon);
        int position = -1;
        
        /* frage Position ab */
        try{
            position = HighScoreFile.getInstance().getScorePosition(hauptfenster.getDionaRapModel().getScore());
        } catch (IOException ex) {
            System.err.println("File kann nicht gelesen werden: " + ex);
        }
        
        /* erstelle Dialog */
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage("Sie haben Platz " + position + " der Bestenliste erreicht. \nBitte tragen Sie ihren Namen ein:");
        optionPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
        optionPane.setOptionType(JOptionPane.YES_NO_OPTION);
        optionPane.setIcon(win);
        optionPane.setWantsInput(true);
        optionPane.setOptions(choices);
        JDialog dialog = optionPane.createDialog(hauptfenster, "Eintrag in Bestenliste!");
        dialog.setVisible(true);
        
        /* Werte Dialog aus */
        if(optionPane.getValue().equals("Eintragen")) {
            String playername;
            if(optionPane.getInputValue().toString().length() != 0) {
                playername = optionPane.getInputValue().toString();
            } 
            else {
                playername = "kein Name";
            }
            /* trage Highscore in Datei ein */
            try {
                HighScoreFile.getInstance().writeScoreIntoFile(playername, hauptfenster.getDionaRapModel().getScore());
            } catch (IOException ex) {
                System.err.println("File kann nicht gelesen werden: " + ex);
            }
        }
	}	
}