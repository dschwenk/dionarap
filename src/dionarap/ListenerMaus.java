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


/**
 * Klasse realisiert den Listener fuer Mausereignisse. Die Klasse ist abgeleitet
 * von  <code>MouseAdapter</code> und implementiert
 * das Interface <code>ActionListener</code>. 
 *   
 * @author Daniel Schwenk
 * @version Aufgabe 5
 */
public class ListenerMaus extends MouseAdapter implements ActionListener {

	private Hauptfenster hauptfenster;
	private JLabel[][] labelArray;
	private int playerposition_x;
	private int playerposition_y;
    private DionaRapController DRController;
    private JPopupMenu popupMenu;
    private JMenuItem dracula;
    private JMenuItem spaceWars;
    private JMenuItem squareHead;


    private String gamedirctory = Hauptfenster.getGameDirectory();
    private String separator = System.getProperty("file.separator");

	
    /**
     * Konstruktor der Klasse ListenerMaus
     * @param Hauptfenster
     */
    public ListenerMaus(Hauptfenster hauptfenster){
    	this.hauptfenster = hauptfenster;
    	popupMenu = new JPopupMenu("Thema");
    	/* fuege Eintraege zum Menu hinzu */
        popupMenu.add(dracula = new JMenuItem("Dracula", (Icon) new ImageIcon(gamedirctory + "icons" + separator + "Dracula" + separator + "popup.gif")));
        dracula.setEnabled(false);
        dracula.addActionListener(this);
        popupMenu.add(spaceWars = new JMenuItem("SpaceWars", (Icon) new ImageIcon(gamedirctory + "icons" + separator + "SpaceWars" + separator + "popup.gif")));
        spaceWars.addActionListener(this);
        popupMenu.add(squareHead = new JMenuItem("SquareHead", (Icon) new ImageIcon(gamedirctory + "icons" + separator + "SquareHead" + separator + "popup.gif")));
        squareHead.addActionListener(this);
    }    


    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>
     * setzt das gewaehlte Theme
     */
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == dracula) { // setzen des Themes "Dracula"
			hauptfenster.getSpielfeld().setTheme("Dracula");
			dracula.setEnabled(false);
			spaceWars.setEnabled(true);
			squareHead.setEnabled(true);
		}
		/* setze Theme SpaceWars */
		if(e.getSource() == spaceWars){
			hauptfenster.getSpielfeld().setTheme("SpaceWars");
			spaceWars.setEnabled(false);
			dracula.setEnabled(true);
			squareHead.setEnabled(true);
		}
		/* setze Theme Squarehead */		
		if(e.getSource() == squareHead){
			hauptfenster.getSpielfeld().setTheme("SquareHead");
			squareHead.setEnabled(false);
			dracula.setEnabled(true);
			spaceWars.setEnabled(true);
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
			int size_x = hauptfenster.getSpielfeld().getSpielfeldSize();
			int size_y = hauptfenster.getSpielfeld().getSpielfeldSize();
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
							DRController.shoot();
							break;
						}
						/* es wurde "links unten" geklickt */
						else if(playerposition_y - i == -1 && playerposition_x -j == 1){
							DRController.movePlayer(1);
						}
						/* es wurde "unten" geklickt */
						else if(playerposition_y - i == -1 && playerposition_x -j == 0){
							DRController.movePlayer(2);
						}
						/* es wurde "rechts unten" geklickt */
						else if(playerposition_y - i == -1 && playerposition_x -j == -1){
							DRController.movePlayer(3);
						}						
						/* es wurde "rechts" geklickt */
						else if(playerposition_y - i == 0 && playerposition_x -j == -1){
							DRController.movePlayer(6);
						}
						/* es wurde "rechts oben" geklickt */
						else if(playerposition_y - i == 1 && playerposition_x -j == -1){
							DRController.movePlayer(9);
						}							
						/* es wurde "oben" geklickt */
						else if(playerposition_y - i == 1 && playerposition_x -j == 0){
							DRController.movePlayer(8);
						}
						/* es wurde "links oben" geklickt */
						else if(playerposition_y - i == 1 && playerposition_x -j == 1){
							DRController.movePlayer(7);
						}
						/* es wurde "links" geklickt */
						else if(playerposition_y - i == 0 && playerposition_x -j == 1){
							DRController.movePlayer(4);
						}						
					}
				}
			}
		}
	}
}
