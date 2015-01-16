package dionarap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.fhwgt.dionarap.controller.DionaRapController;
import de.fhwgt.dionarap.model.objects.AbstractPawn;
import de.fhwgt.dionarap.model.objects.Obstacle;


/**
 * Klasse realisiert den Listener fuer Mausereignisse. Die Klasse ist abgeleitet
 * von  <code>MouseAdapter</code> und implementiert
 * das Interface <code>ActionListener</code>. 
 *   
 * @author Daniel Schwenk und Fabian Frick
 * @version Aufgabe 7
 */
public class ListenerMaus extends MouseAdapter implements ActionListener {

	private Hauptfenster hauptfenster;
    private DionaRapController DRController;
    
	private JLabel[][] labelArray;
	private int playerposition_x;
	private int playerposition_y;

    private JPopupMenu popupMenu;
    private JMenuItem dracula;
    private JMenuItem spaceWars;
    private JMenuItem squareHead;

    private String gamedirctory = Hauptfenster.getGameDirectory();
    private String separator = System.getProperty("file.separator");

	
    /**
     * Konstruktor der Klasse ListenerMaus
     * @param hauptfenster das Vaterfenster
     */
    public ListenerMaus(Hauptfenster hauptfenster){
    	this.hauptfenster = hauptfenster;
    	popupMenu = new JPopupMenu("Thema");
    	/* fuege Eintraege zum Menu hinzu */
        popupMenu.add(dracula = new JMenuItem("Dracula", (Icon) new ImageIcon(gamedirctory + "icons" + separator + "Dracula" + separator + "player.gif")));
        dracula.addActionListener(this);
        popupMenu.add(spaceWars = new JMenuItem("SpaceWars", (Icon) new ImageIcon(gamedirctory + "icons" + separator + "SpaceWars" + separator + "player1.gif")));
        spaceWars.addActionListener(this);
        popupMenu.add(squareHead = new JMenuItem("SquareHead", (Icon) new ImageIcon(gamedirctory + "icons" + separator + "SquareHead" + separator + "player.gif")));
        squareHead.addActionListener(this);

        if(this.hauptfenster.getTheme().equals("Dracula")){
        	dracula.setEnabled(false);
        }
        else if(this.hauptfenster.getTheme().equals("SpaceWars")){
        	spaceWars.setEnabled(false);        	
        }
        else if(this.hauptfenster.getTheme().equals("SquareHead")){
        	squareHead.setEnabled(false);
        }
    }    


    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * setzt das gewaehlte Theme
     */
	public void actionPerformed(ActionEvent e){
		/* setzte Theme Dracula */
		if(e.getSource() == dracula){
			hauptfenster.setTheme("Dracula");
			dracula.setEnabled(false);
			spaceWars.setEnabled(true);
			squareHead.setEnabled(true);
		}
		/* setze Theme SpaceWars */
		if(e.getSource() == spaceWars){
			hauptfenster.setTheme("SpaceWars");
			dracula.setEnabled(true);
			spaceWars.setEnabled(false);
			squareHead.setEnabled(true);
		}
		/* setze Theme Squarehead */		
		if(e.getSource() == squareHead){
			hauptfenster.setTheme("SquareHead");
			dracula.setEnabled(true);
			spaceWars.setEnabled(true);
			squareHead.setEnabled(false);
		}		
	}

	
	/**
	 * Eventhandler fuer das Event <code>mouseClicked</code>
	 * Rechtsklick zeigt PopupMenu an, Linksklick bewegt Spieler / schiesst
	 */
	public void mouseClicked(MouseEvent e){
		/* Rechtsklick */
		if(e.getButton() == 3){
			/* zeige Popupmenu an */
			popupMenu.show(e.getComponent(),e.getX(),e.getY());
		}
		/* Linksklick */
		else if(e.getButton() == 1){
			/* Groesse Spielfeld */
			int size_x = hauptfenster.getDionaRapModel().getGrid().getGridSizeX();
			int size_y = hauptfenster.getDionaRapModel().getGrid().getGridSizeY();	
			/* lege Spielfeld an */
			labelArray = new JLabel[size_x][size_y];
			labelArray = hauptfenster.getSpielfeld().getSpielfeldArray();
			/* aktuelle Spielerposition */
			playerposition_x = hauptfenster.getPlayer().getX();
			playerposition_y = hauptfenster.getPlayer().getY();
			DRController = (DionaRapController) hauptfenster.getDionaRapController();
			/* pruefe auf welches Label der Linksklick gemacht wurde */
			for(int i=0;i<size_y;i++){
				for(int j=0;j<size_x;j++){
					if(e.getSource().equals(labelArray[i][j])){
						/* es wurde auf den Spieler geklickt - schiessen */
						if(i == playerposition_y && j == playerposition_x){
							/* gueltiger Klick - stoppe blinken der Felder */
							if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
								hauptfenster.stopThreadt_field();
							}
							/* pruefe ob Munition vorhanden - falls nicht erzeuge Thread um Anzeige zum Blinken zu bringen */
							if(hauptfenster.getDionaRapModel().getShootAmount() == 0){
								/* erzeuge neuen Thread falls dieser noch nicht besteht */
								if(hauptfenster.getThreadt_ammo() == null){
									hauptfenster.createThreadt_ammo();
								}
								else if(!(hauptfenster.getThreadt_ammo().isAlive())){
									hauptfenster.createThreadt_ammo();				
								}
							}							
							/* Sound fuer Schuss sofern Munition vorhanden*/
							if(hauptfenster.getDionaRapModel().getShootAmount() > 0){
								hauptfenster.getSounds().playSoundShoot();
								DRController.shoot();
							}							
							break;
						}
						/* es wurde "links unten" geklickt */
						else if(playerposition_y - i == -1 && playerposition_x -j == 1){
							/* pruefe ob auf dem Feld ein Hindernis ist */
							if(isThereAnObstacle(i,j)){
								/* ueberpruefe ob es ein Objekt t_field gibt */
								if(hauptfenster.getThreadt_field() == null){
									hauptfenster.createThreadt_field();
								}
								/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
								else if(!(hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.createThreadt_field();
								}
							}
							/* kein Hindernis */
							else {
								/* gueltige Bewegung - stoppe blinken der Felder */
								if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.stopThreadt_field();
								}														
							}
							hauptfenster.getSounds().playSoundMove();
							DRController.movePlayer(1);
						}
						/* es wurde "unten" geklickt */
						else if(playerposition_y - i == -1 && playerposition_x -j == 0){
							/* pruefe ob auf dem Feld ein Hindernis ist */
							if(isThereAnObstacle(i,j)){
								/* ueberpruefe ob es ein Objekt t_field gibt */
								if(hauptfenster.getThreadt_field() == null){
									hauptfenster.createThreadt_field();
								}
								/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
								else if(!(hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.createThreadt_field();
								}
							}
							/* kein Hindernis */
							else {
								/* gueltige Bewegung - stoppe blinken der Felder */
								if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.stopThreadt_field();
								}														
							}
							hauptfenster.getSounds().playSoundMove();
							DRController.movePlayer(2);
						}
						/* es wurde "rechts unten" geklickt */
						else if(playerposition_y - i == -1 && playerposition_x -j == -1){
							/* pruefe ob auf dem Feld ein Hindernis ist */
							if(isThereAnObstacle(i,j)){
								/* ueberpruefe ob es ein Objekt t_field gibt */
								if(hauptfenster.getThreadt_field() == null){
									hauptfenster.createThreadt_field();
								}
								/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
								else if(!(hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.createThreadt_field();
								}
							}
							/* kein Hindernis */
							else {
								/* gueltige Bewegung - stoppe blinken der Felder */
								if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.stopThreadt_field();
								}														
							}
							hauptfenster.getSounds().playSoundMove();
							DRController.movePlayer(3);
						}						
						/* es wurde "rechts" geklickt */
						else if(playerposition_y - i == 0 && playerposition_x -j == -1){
							/* pruefe ob auf dem Feld ein Hindernis ist */
							if(isThereAnObstacle(i,j)){
								/* ueberpruefe ob es ein Objekt t_field gibt */
								if(hauptfenster.getThreadt_field() == null){
									hauptfenster.createThreadt_field();
								}
								/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
								else if(!(hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.createThreadt_field();
								}
							}
							/* kein Hindernis */
							else {
								/* gueltige Bewegung - stoppe blinken der Felder */
								if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.stopThreadt_field();
								}														
							}
							hauptfenster.getSounds().playSoundMove();
							DRController.movePlayer(6);
						}
						/* es wurde "rechts oben" geklickt */
						else if(playerposition_y - i == 1 && playerposition_x -j == -1){
							/* pruefe ob auf dem Feld ein Hindernis ist */
							if(isThereAnObstacle(i,j)){
								/* ueberpruefe ob es ein Objekt t_field gibt */
								if(hauptfenster.getThreadt_field() == null){
									hauptfenster.createThreadt_field();
								}
								/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
								else if(!(hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.createThreadt_field();
								}
							}
							/* kein Hindernis */
							else {
								/* gueltige Bewegung - stoppe blinken der Felder */
								if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.stopThreadt_field();
								}														
							}
							hauptfenster.getSounds().playSoundMove();
							DRController.movePlayer(9);
						}							
						/* es wurde "oben" geklickt */
						else if(playerposition_y - i == 1 && playerposition_x -j == 0){
							/* pruefe ob auf dem Feld ein Hindernis ist */
							if(isThereAnObstacle(i,j)){
								/* ueberpruefe ob es ein Objekt t_field gibt */
								if(hauptfenster.getThreadt_field() == null){
									hauptfenster.createThreadt_field();
								}
								/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
								else if(!(hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.createThreadt_field();
								}
							}
							/* kein Hindernis */
							else {
								/* gueltige Bewegung - stoppe blinken der Felder */
								if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.stopThreadt_field();
								}														
							}
							hauptfenster.getSounds().playSoundMove();
							DRController.movePlayer(8);
						}
						/* es wurde "links oben" geklickt */
						else if(playerposition_y - i == 1 && playerposition_x -j == 1){
							/* pruefe ob auf dem Feld ein Hindernis ist */
							if(isThereAnObstacle(i,j)){
								/* ueberpruefe ob es ein Objekt t_field gibt */
								if(hauptfenster.getThreadt_field() == null){
									hauptfenster.createThreadt_field();
								}
								/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
								else if(!(hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.createThreadt_field();
								}
							}
							/* kein Hindernis */
							else {
								/* gueltige Bewegung - stoppe blinken der Felder */
								if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.stopThreadt_field();
								}														
							}
							hauptfenster.getSounds().playSoundMove();
							DRController.movePlayer(7);
						}
						/* es wurde "links" geklickt */
						else if(playerposition_y - i == 0 && playerposition_x -j == 1){							
							/* pruefe ob auf dem Feld ein Hindernis ist */
							if(isThereAnObstacle(i,j)){
								/* ueberpruefe ob es ein Objekt t_field gibt */
								if(hauptfenster.getThreadt_field() == null){
									hauptfenster.createThreadt_field();
								}
								/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
								else if(!(hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.createThreadt_field();
								}
							}
							/* kein Hindernis */
							else {
								/* gueltige Bewegung - stoppe blinken der Felder */
								if((hauptfenster.getThreadt_field() != null) && (hauptfenster.getThreadt_field().isAlive())){
									hauptfenster.stopThreadt_field();
								}														
							}
							hauptfenster.getSounds().playSoundMove();
							DRController.movePlayer(4);
						}
						/* es wurde auf ein beliebiges anders Feld geklickt */
						else {
							/* ueberpruefe ob es ein Objekt t_field gibt */
							if(hauptfenster.getThreadt_field() == null){
								hauptfenster.createThreadt_field();
							}
							/* es gibt ein Objekt t_field, dieses ist aber nicht mehr aktiv */
							else if(!(hauptfenster.getThreadt_field().isAlive())){
								hauptfenster.createThreadt_field();
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Methode prueft ob an der geklickten Stelle sich ein Hindernis befindet
	 * @param i x-Koordinate des Klicks
	 * @param j j-Koordinate des Klicks
	 * @return true falls sich an der geklickten Stelle ein Hindernis befindet
	 */
	private boolean isThereAnObstacle(int i, int j){
		AbstractPawn[] dionaRap_Pawns = hauptfenster.getPawns();
		for(int k=0;k < dionaRap_Pawns.length;k++){
			if(dionaRap_Pawns[k] instanceof Obstacle){
				/* erfrage Position der Figur */
				int posX = dionaRap_Pawns[k].getX();
				int posY = dionaRap_Pawns[k].getY();							
				if((posY == i) && (posX == j)){
					return true;
				}									
			}
		}
		return false;
	}
}