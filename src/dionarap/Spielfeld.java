package dionarap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.fhwgt.dionarap.model.objects.AbstractPawn;
import de.fhwgt.dionarap.model.objects.Ammo;
import de.fhwgt.dionarap.model.objects.Destruction;
import de.fhwgt.dionarap.model.objects.Obstacle;
import de.fhwgt.dionarap.model.objects.Opponent;
import de.fhwgt.dionarap.model.objects.Player;
import de.fhwgt.dionarap.model.objects.Vortex;

/**
 * Klasse realisiert das Spielfeld, abgeleitet von <code>JPanel</code>
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 5
 */
public class Spielfeld extends JPanel {

	/* The serializable class Spielfeld does not declare a static final serialVersionUID field of type long */
	private static final long serialVersionUID = 1L;
	
	private Hauptfenster hauptfenster;
	
	/* Array mit Schachbrettfeldern, Zeilen x Spalten */
	private JLabel[][] Schachbrett;
	
    // gibt die Anzahl der Felder in x- / y-Richtung an
    static final int size_spielfeld = 10;
    
    /* Theme */
    private static String theme = "Dracula";

	/* Icons */
	private ImageIcon iconAmmo;
	private ImageIcon iconDestruction;
	private ImageIcon iconGameOver;
	private ImageIcon iconObstacle;	
	private ImageIcon iconOpponent;
	private ImageIcon iconPlayer;
	private ImageIcon iconPlayer1; // links unten
	private ImageIcon iconPlayer2; // unten
	private ImageIcon iconPlayer3;
	private ImageIcon iconPlayer4;
	private ImageIcon iconPlayer7;
	private ImageIcon iconPlayer6;
	private ImageIcon iconPlayer8;
	private ImageIcon iconPlayer9;
	private ImageIcon iconPlayerLost;
	private ImageIcon iconVortex;
	
	
	/**
	 * Konstruktor des Spielfelds vom Type <code>JPanel</code>
	 * Erstellt ein Spielfeld / Schachbrett mit x * x Feldern
	 */		
	public Spielfeld(Hauptfenster hauptfenster){
		
		this.hauptfenster = hauptfenster;
		
		/* GridLayout festlegen */
		this.setLayout(new GridLayout(size_spielfeld,size_spielfeld));

		/* Array fuer Schachbrett erzeugen und Felder hinzufuegen */
		this.Schachbrett = new JLabel[size_spielfeld][size_spielfeld];
		this.addLabelsToSchachbrett(size_spielfeld);

		/* setze Spielfiguricons */
		this.setIcons();
	}



	/**
	 * Fuegt Spielfelder zum Spielfeld hinzu
	 * @param size_spielfeld Groesse des Spielfelds
	 */	
	private void addLabelsToSchachbrett(int size_spielfeld){
		for(int zeile=0;zeile < size_spielfeld;zeile++){
			for(int spalte=0; spalte < size_spielfeld;spalte++){
				/* Label mit Groesse 50x50 anlegen */
				this.Schachbrett[zeile][spalte] = new JLabel();
				this.Schachbrett[zeile][spalte].setPreferredSize(new Dimension(50,50));

				 /* Spielfelder einfaerben -  berechne mit Modolu ob Spielfeld schwarz oder weiss */
				if((zeile % 2) == 0){
					if((spalte % 2) == 0){
						this.Schachbrett[zeile][spalte].setBackground(Color.black);
					}
					else {
						this.Schachbrett[zeile][spalte].setBackground(Color.white);						
					}
				}
				else {
					if((spalte % 2) == 0){
						this.Schachbrett[zeile][spalte].setBackground(Color.white);
					}
					else {
						this.Schachbrett[zeile][spalte].setBackground(Color.black);						
					}					
				}
				
				/* Label deckend darstellen */
				this.Schachbrett[zeile][spalte].setOpaque(true);
				/* Listener registrieren */
				this.Schachbrett[zeile][spalte].addMouseListener(new ListenerMaus(hauptfenster));				
				/* Label zum Panel hinzufuegen */
				this.add(this.Schachbrett[zeile][spalte]);
			}
		}
	}



	/**
	 * Setzt die Icons für die Spielfiguren
	 */
	private void setIcons(){
		// String mit Pfad zu Icons
		String pathIcons = "icons"+File.separator+theme+File.separator;

		iconAmmo = new ImageIcon(pathIcons+"ammo.gif");
		iconDestruction = new ImageIcon(pathIcons+"destruction.gif");
		iconGameOver = new ImageIcon(pathIcons+"gameover.gif");
		iconObstacle = new ImageIcon(pathIcons+"obstacle.gif");
		iconOpponent = new ImageIcon(pathIcons+"opponent.gif");
		iconPlayer = new ImageIcon(pathIcons+"player.gif");
		iconPlayer1 = new ImageIcon(pathIcons+"player1.gif");
		iconPlayer2 = new ImageIcon(pathIcons+"player2.gif");
		iconPlayer3 = new ImageIcon(pathIcons+"player3.gif");
		iconPlayer4 = new ImageIcon(pathIcons+"player4.gif");		
		iconPlayer6 = new ImageIcon(pathIcons+"player6.gif");
		iconPlayer7 = new ImageIcon(pathIcons+"player7.gif");		
		iconPlayer8 = new ImageIcon(pathIcons+"player8.gif");
		iconPlayer9 = new ImageIcon(pathIcons+"player9.gif");
		iconPlayerLost = new ImageIcon(pathIcons+"loss.gif");		
		iconVortex = new ImageIcon(pathIcons+"vortex.gif");		
	}	



	/**
	 * Löscht die Icons vom Spielfeld
	 */
	public void clearGame(){
		/* gehe Felder nacheinander durch */
		for(int zeile = 0; zeile < size_spielfeld; zeile++){		
			for(int spalte = 0; spalte < size_spielfeld; spalte++){
				/* entferne Icon -> setze null */
				this.Schachbrett[zeile][spalte].setIcon(null);
			}			
		}
	}



	/**
	 * Ermittelt Typ und Position der Spielfigur vom Typ <code>AbstractPawn</code> 
	 * und zeichnet das Icon auf das Spielfeld.
	 * @param dionaRap_Pawns Spielfigur vom Typ <code>AbstractPawn</code>
	 */
	public void paintPawns(AbstractPawn[] dionaRap_Pawns){		
		for(int i=0;i < dionaRap_Pawns.length;i++){
			/* erfrage Position der Figur */
			int posX = dionaRap_Pawns[i].getX();
			int posY = dionaRap_Pawns[i].getY();
			
			/* ist die Figur ein Spieler */ 
			if(dionaRap_Pawns[i] instanceof Player){
				
				Player pawnPlayer = (Player)dionaRap_Pawns[i];
				/* in welche Richtung schaut der Spieler */
				int viewDirection = pawnPlayer.getViewDirection();
				
				/* setze Spielfigur auf Spielfeld */				
				if(viewDirection == 1){
					Schachbrett[posY][posX].setIcon(iconPlayer1);
				}
				else if(viewDirection == 2){
					Schachbrett[posY][posX].setIcon(iconPlayer2);
				}
				else if(viewDirection == 3){
					Schachbrett[posY][posX].setIcon(iconPlayer3);
				}
				else if(viewDirection == 4){
					Schachbrett[posY][posX].setIcon(iconPlayer4);
				}			
				else if(viewDirection == 6){
					Schachbrett[posY][posX].setIcon(iconPlayer6);
				}
				else if(viewDirection == 7){
					Schachbrett[posY][posX].setIcon(iconPlayer7);
				}
				else if(viewDirection == 8){
					Schachbrett[posY][posX].setIcon(iconPlayer8);
				}
				else if(viewDirection == 9){
					Schachbrett[posY][posX].setIcon(iconPlayer9);
				}
			}
			
			/* Figur ist ein Gegner */
			else if(dionaRap_Pawns[i] instanceof Opponent){
				//Schachbrett[posY][posX].setText("G");
				Schachbrett[posY][posX].setIcon(iconOpponent);
			}
			/* Figur ist ein Grabstein */		
			else if(dionaRap_Pawns[i] instanceof Obstacle){
				Schachbrett[posY][posX].setIcon(iconObstacle);
			}
			/* Figur ist Munition */		
			else if(dionaRap_Pawns[i] instanceof Ammo){
				Schachbrett[posY][posX].setIcon(iconAmmo);
			}
			/* Figur ist Treppe  */		
			else if(dionaRap_Pawns[i] instanceof Vortex){
				Schachbrett[posY][posX].setIcon(iconVortex);
			}
			/* Figur ist   */		
			else if(dionaRap_Pawns[i] instanceof Destruction){
				Schachbrett[posY][posX].setIcon(iconDestruction);
			}
		}
	}
	
	
	/**
	 * Setzt das Gewonnen / Verloren SpielerIcon auf das Spielfeld
	 * @param Player spieler Spielerfigur
	 * @param boolean game_lost Wurde Spiel gewonnen / verloren
	 */
	public void gameStatusEnd(Player spieler, boolean game_lost){
		/* Spiel wurde verloren */
		if(game_lost){
			this.Schachbrett[spieler.getY()][spieler.getX()].setIcon(iconPlayerLost);
		}
		/* Spiel gewonnen */
		else {
			this.Schachbrett[spieler.getY()][spieler.getX()].setIcon(iconPlayer);
		}
	}
	
	
	/**
	 * Setzt das Theme
	 * @param String theme
	 */
	public void setTheme(String theme){
		this.theme = theme;
		this.setIcons();
		this.clearGame();
		this.paintPawns(hauptfenster.getPawns());
	}
	
	
	/**
	 *Gibt das Theme zurueck
	 * @return String theme 
	 */
	public String getTheme(){
		return this.theme;
	}
	
	/**
	 * Gibt die Spielfeldgroesse zurueck
	 */
	public int getSpielfeldSize(){
		return Spielfeld.size_spielfeld;
	}
	
	/**
	 * Gibt das Spielfeld zurueck
	 */
	public JLabel[][] getSpielfeldArray(){
		return this.Schachbrett;
	}	
}
