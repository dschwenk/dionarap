package dionarap;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.fhwgt.dionarap.model.data.MTConfiguration;

/**
 * Klasse ermoeglicht es Spieleinstellungen vorzunehmen, abgeleitet von <code>JDialog</code>
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 7
 */
public class Spieleinstellungen extends JDialog implements ActionListener {

	private Hauptfenster hauptfenster;
	
    private MTConfiguration conf;
    private JTextField [] text = new JTextField[7];
    private JCheckBox ch_box1_zufaelligeWartezeit;
    private JCheckBox ch_box2_GegnerMeidenHindernis;
    private JCheckBox ch_box3_GegenerMeidenGegner;	
	
	/**
	 * Konstruktor der Spieleinstellungen vom Typ <code>JPanel</code>
	 * @param parent Vaterfenster vom Typ <code>Hauptfenster</code>
	 */
	public Spieleinstellungen(Hauptfenster hauptfenster){
		this.hauptfenster = hauptfenster;
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(11, 2));
		
		/* Textfelder, Checkboxen */
        for(int i=0;i<7;i++){
            text[i] = new JTextField();
        }        
        ch_box1_zufaelligeWartezeit = new JCheckBox("Zuf�llige Wartezeiten der Gegner");
        ch_box2_GegnerMeidenHindernis = new JCheckBox("Gegner meiden Kollisionen mit Hindernis");
        ch_box3_GegenerMeidenGegner = new JCheckBox("Gegner meiden Kollisionen mit anderen Gegnern");
        
        JButton button1 = new JButton("�bernehmen");
        button1.setActionCommand("uebernehmen");
        button1.addActionListener(this);  
        JButton button2 = new JButton("Abbrechen");
        button2.addActionListener(this);        
        button1.setActionCommand("abbrechen");
        
        /* Defaultwerte setzen */
        setCurrentGameSettings();
        
        /* Elemente zum Panel hinzufuegen */
		panel.add(new JLabel("Wartezeit der Gegner zu Beginn: "));
		panel.add(text[0]);
		panel.add(new JLabel("Verz�gerung eines Schuess: "));
		panel.add(text[1]);		
		panel.add(new JLabel("Wartezeit eines Gegner vor jedem Schuss: "));
		panel.add(text[2]);
		
		panel.add(new JLabel());
		panel.add(ch_box1_zufaelligeWartezeit);
		ch_box1_zufaelligeWartezeit.addActionListener(this);

		panel.add(new JLabel());
		panel.add(ch_box2_GegnerMeidenHindernis);
		
		panel.add(new JLabel());
		panel.add(ch_box3_GegenerMeidenGegner);
		
		panel.add(new JLabel("Anzahl Zeilen des Spielfeldes: "));
		panel.add(text[3]);
		panel.add(new JLabel("Anzahl Spalten des Spielfeldes: "));
		panel.add(text[4]);
		panel.add(new JLabel("Anzahl Hindernisse: "));
		panel.add(text[5]);
		panel.add(new JLabel("Anzahl Gegner: "));
		panel.add(text[6]);
		panel.add(button1);
		panel.add(button2);		

		
		/* Panel hinzufuegen, Position, Groesse + Packen + anzeigen */
		this.add(panel);
		this.setLocationRelativeTo(hauptfenster);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}
	
	
	/**
	 * Methode liesst die aktuellen Spieleinstellungen aus und setzt
	 * diese Werte in die Textfelder
	 */
	private void setCurrentGameSettings(){
		
		/* aktuelle Konfiguration auslesen */
		this.conf = this.hauptfenster.getMTConfiguration();
		
		/* Werte setzen */
		text[0].setText(String.valueOf(conf.getOpponentStartWaitTime()));
		text[1].setText(String.valueOf(conf.getShotWaitTime()));
		text[2].setText(String.valueOf(conf.getOpponentWaitTime()));
		text[3].setText(String.valueOf(hauptfenster.getGrid().getGridSizeY()));
		text[4].setText(String.valueOf(hauptfenster.getGrid().getGridSizeX()));
		text[5].setText(String.valueOf(hauptfenster.getObstacleCount()));
		text[6].setText(String.valueOf(hauptfenster.getOpponentCount()));
		
		/* Checkboxen anhaken */
		if(conf.isRandomOpponentWaitTime()){
			ch_box1_zufaelligeWartezeit.setSelected(true);
		    text[2].setEnabled(false);
		}
		if(conf.isAvoidCollisionWithObstacles()){
			ch_box2_GegnerMeidenHindernis.setSelected(true);
		}
		if(conf.isAvoidCollisionWithOpponent()){
			ch_box3_GegenerMeidenGegner.setSelected(true);
		}		
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		/* Aktion auswerten */
		if(e.getActionCommand() == "abbrechen"){
			System.out.println("bla");
			Spieleinstellungen.this.dispose();
		}
	}

}
