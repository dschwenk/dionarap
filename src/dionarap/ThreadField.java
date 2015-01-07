package dionarap;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import de.fhwgt.dionarap.model.objects.AbstractPawn;
import de.fhwgt.dionarap.model.objects.Obstacle;


/**
 * Klasse realisiert den Thread der fuer das Blinken 
 * einzelner Spielfeldfelder zustaendig ist, abgleitet von <code>Thread /code>
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 7
 */
public class ThreadField extends Thread{

	private Hauptfenster hauptfenster;
	
	private static final int blinkDelay = 500;
	
	/* Array das die Felder die blinken sollen enthaelt + counter */
	private JLabel[] blink_arr = new JLabel[8];
	private int count_fields_blink = 0;
	/* Array das das die Felder des Spielfelds enthaelt*/	
	private JLabel[][] spielfeld_arr;	
	
	/* aktuelle Koordinaten des Spielers */
	private int player_pos_x;
	private int player_pos_y;

	/* Flag ob aktuell geblinkt werden soll */
	private boolean run = true;

	
	/**
	 * Konstruktor
	 * @param hauptfenster
	 */
	ThreadField(Hauptfenster hauptfenster) {
		this.hauptfenster = hauptfenster;
		
		/* fuelle Array mit SpielfeldLabels */
		spielfeld_arr = new JLabel [hauptfenster.getDionaRapModel().getGrid().getGridSizeY()][hauptfenster.getDionaRapModel().getGrid().getGridSizeX()];
		spielfeld_arr = hauptfenster.getSpielfeld().getSpielfeldArray();
		
		/* aktuelle Spielerposition */
		player_pos_x = hauptfenster.getPlayer().getX();
		player_pos_y = hauptfenster.getPlayer().getY();
		
		fillFieldArrayWithNeighboursFields();
	}
	
	
	/**
	 * Methode ist fuer das Blinken der Felder zustaendig
	 */
    public void run(){
        /* so lange noch keine 3 Sekunden vergangen sind und geblinkt werden soll (keine gueltige Bewebung in der Zwischenzeit */
    	for(int i=0;(i<6) && run;i++){
            for(int k=0;k<count_fields_blink;k++){
                /* abwechselnd Rand hinzufuegen / entfernen */
            	if((i % 2) == 0){
                	blink_arr[k].setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                else {
                	blink_arr[k].setBorder(BorderFactory.createEmptyBorder());
                }
                hauptfenster.getToolbar().getMuniJPanel().updateUI();
            }
            try {
            	/* Thread schlafen legen */
                Thread.sleep(blinkDelay);
            } catch (InterruptedException ex) {}
        }
    	/* entferne alle Borders am Ende */
    	for(int k=0;k<count_fields_blink;k++){
    		blink_arr[k].setBorder(BorderFactory.createEmptyBorder());
    	}
    }


    /**
     * Methode fuellt das Array mit benachbarten Feldern sofern
     * sofern es sich um ein Hindernis handelt oder es ausserhalb
     * des Spielfelds ist
     */
    public void fillFieldArrayWithNeighboursFields(){
    	/* gehe alle benachbarten Felder durch */
    	
    	/* links oben */
    	if(isNeighbourFieldObstacleOrOutside(player_pos_y+1,player_pos_x-1) == false){
    		addBlinkLabel(player_pos_y+1,player_pos_x-1);
    	}
    	/* oben */
    	if(isNeighbourFieldObstacleOrOutside(player_pos_y+1,player_pos_x) == false){
    		addBlinkLabel(player_pos_y+1,player_pos_x);
    	}
    	/* rechts oben */
    	if(isNeighbourFieldObstacleOrOutside(player_pos_y+1,player_pos_x+1) == false){
    		addBlinkLabel(player_pos_y+1,player_pos_x+1);
    	}
    	if(isNeighbourFieldObstacleOrOutside(player_pos_y,player_pos_x-1) == false){
    		addBlinkLabel(player_pos_y,player_pos_x-1);
    	}
    	if(isNeighbourFieldObstacleOrOutside(player_pos_y,player_pos_x+1) == false){
    		addBlinkLabel(player_pos_y,player_pos_x+1);
    	}
    	if(isNeighbourFieldObstacleOrOutside(player_pos_y-1,player_pos_x-1) == false){
    		addBlinkLabel(player_pos_y-1,player_pos_x-1);
    	}
    	if(isNeighbourFieldObstacleOrOutside(player_pos_y-1,player_pos_x) == false){
    		addBlinkLabel(player_pos_y-1,player_pos_x);
    	}
    	if(isNeighbourFieldObstacleOrOutside(player_pos_y-1,player_pos_x+1) == false){
    		addBlinkLabel(player_pos_y-1,player_pos_x+1);
    	}    	
    }
   

    /**
     * Methode pruef ob das benachbarte Feld ein Hindernis ist
     * oder ob es ausserhalb des Spielfelds ist
     * @param y Koordinate des benachbarten Felds
     * @param x Koordinate des benachbarten Felds
     * @return true falls Feld ein Hindernis ist oder ausserhalb des Spielfeld liegt
     */
    public boolean isNeighbourFieldObstacleOrOutside(int y, int x){
		
    	/* sind Koordinaten ausserhalb des Spielfelds */
		if((y < 0) || (x < 0) || (y > hauptfenster.getDionaRapModel().getGrid().getGridSizeY())	|| (x > hauptfenster.getDionaRapModel().getGrid().getGridSizeX())){
			return true;
		}
		/* ist auf Koordinate ein Hindernis */
    	AbstractPawn[] spielfiguren = hauptfenster.getPawns();
		for(int i=0;i<spielfiguren.length;i++){
			if(spielfiguren[i] instanceof Obstacle){
				if(spielfiguren[i].getY() == y && spielfiguren[i].getX() == x){
					return true;
				}
			}
		}
		return false;    	
    }


    /**
     * Methode fuellt das Array mit den Spielfelder die blinken sollen
     * @param y Koordinaten des benachbarten Felds
     * @param x Koordinaten des benachbarten Felds
     */
    public void addBlinkLabel(int y, int x){
    	this.blink_arr[count_fields_blink] = spielfeld_arr[y][x];
    	this.count_fields_blink++;
    }


    /**
     * Methode setzt das run Flag auf false 
     */
    public void stopBlink(){
    	this.run = false;
    }
}
