package dionarap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.fhwgt.dionarap.levelreader.LevelReader;
import de.fhwgt.dionarap.model.data.MTConfiguration;

/**
 * Klasse realisiert das Menu, abgeleitet von <code>JMenuBar</code>, implementiert <code>ActionListener</code>
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 6
 */
public class MenuBar extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Hauptfenster hauptfenster;

    /* Flag Toolbar sichtbar */
    private boolean toolbarsichtbar = true;
    /* Flag Toolbar oben / unten */
    private boolean showtoolbarontop = true;
    /* Falg Navigator sichtbar */
    private boolean navigatorsichtbar = true;
    /* Look and Feels Array */
    private UIManager.LookAndFeelInfo[] lookandfeeluimanagerinfoarray;
    /* Look and Feels RadioButton Array */
    private JRadioButtonMenuItem lookandfeelradiobuttonarray[];
    /* aktiver Look and Feel Radiobutton */
    private int activeradiobutton = 0;
    /* Anzahl an vorhandenen Look and Fells */
    private int lookandfeelcounter;

	
    // Menueleiste Elemente
    private JMenu ansicht;
    private JMenu hilfe;
    private JMenu konfiguration;
    
    /* Anicht Elemente */
    private JMenu toolbarposition;
    private JMenu lookandfeelmenu;
    private JMenuItem toolbartop;
    private JMenuItem toolbarbottom;    
    private JCheckBoxMenuItem toolbaranzeigen;
    private JCheckBoxMenuItem navigatoranzeigen;
    
    /* Konfiguration Elemnete */
    private JMenuItem leveleinlesen;
    private JMenuItem spieleinstellungen;  
    
    // Hilfe Elemente
    private JMenuItem spielbeschreibung;


    
	/**
	 * Konstruktor der Menuleiste vom Typ <code>JMenuBar</code>
	 * @param parent Vaterfenster vom Typ <code>Hauptfenster</code>
	 */	
	public MenuBar(Hauptfenster hauptfenster){
		
		this.hauptfenster = hauptfenster;
		
		/* Menuleistenelemente */
		ansicht = new JMenu("Ansicht");
		konfiguration = new JMenu("Konfiguration");
		hilfe = new JMenu("Hilfe");
		
		/* Menu Ansicht */
		
		// Toolbar anzeigen
		toolbaranzeigen = new JCheckBoxMenuItem("Toolbar anzeigen");
		toolbaranzeigen.setState(true);
		toolbaranzeigen.addActionListener(this);
		ansicht.add(toolbaranzeigen);
		
		// Toolbarposition
		toolbarposition = new JMenu("Toolbar Position");
		toolbartop = new JMenuItem("oben");
		toolbarbottom = new JMenuItem("unten");
		toolbarposition.add(toolbartop);
		toolbarposition.add(toolbarbottom);
		toolbartop.addActionListener(this);
		toolbarbottom.addActionListener(this);
		/* deaktivieren da Toolbar standardmaessig oben */
		toolbartop.setEnabled(false);
		ansicht.add(toolbarposition);
		ansicht.add(new JSeparator());
		
		// Navigator anzeigen
		navigatoranzeigen = new JCheckBoxMenuItem("Navigator anzeigen");
		navigatoranzeigen.setState(true);
		navigatoranzeigen.addActionListener(this);
		ansicht.add(navigatoranzeigen);
		ansicht.add(new JSeparator());
		
		// Look and Feel
		lookandfeelmenu = new JMenu("Look and Feel");
		/* Frage vom UIManager alle installierete Look and Feels ab */
		lookandfeeluimanagerinfoarray = UIManager.getInstalledLookAndFeels();
		lookandfeelcounter = lookandfeeluimanagerinfoarray.length;
		lookandfeelradiobuttonarray = new JRadioButtonMenuItem [lookandfeelcounter];
		for(int i = 0; i < lookandfeelcounter; i++) {
			/* fuelle RadioButtonMenu mit installierten Look and Feels */
			lookandfeelradiobuttonarray[i] = new JRadioButtonMenuItem(lookandfeeluimanagerinfoarray[i].getName());
			/* fuege Menupunkt hinzu */
			lookandfeelmenu.add(lookandfeelradiobuttonarray[i]);
			lookandfeelradiobuttonarray[i].addActionListener(this);
			/* lege aktuelles Look and Feel fest */
			if(UIManager.getLookAndFeel().getName().equals(lookandfeeluimanagerinfoarray[i].getName())){
				lookandfeelradiobuttonarray[i].setSelected(true);
				activeradiobutton = i;
			}
        }
        ansicht.add(lookandfeelmenu);		
		
		
		/* Menu Konfiguration */
		
		// Levelreader - MenuItem, Listener, hinzufuegen
		leveleinlesen = new JMenuItem("Level einlesen");
		leveleinlesen.addActionListener(this);
		konfiguration.add(leveleinlesen);
		konfiguration.add(new JSeparator());
		// Spieleinstellungen - MenuItem, Listener, deaktivieren, hinzufuegen
		spieleinstellungen = new JMenuItem("Spieleinstellungen");
		spieleinstellungen.addActionListener(this);
		konfiguration.add(spieleinstellungen);		
		
		/* Menu Hilfe */
		
		// Spielbeschreibung - MenuItem, Listener, hinzufuegen
		spielbeschreibung = new JMenuItem("Spielbeschreibung");
		spielbeschreibung.addActionListener(this);
		hilfe.add(spielbeschreibung);	
		
		/* JMenus zur Menuleiste hinzufuegen */
		this.add(ansicht);
		this.add(konfiguration);
		this.add(hilfe);
	}
	


    /**
     * Eventhandler fuer das Event <code>actionPerformed</code>,
     * Events zu den Elementen in der Menuleisten werden verarbeitet
     */
	public void actionPerformed(ActionEvent e) {

		/* welches Menuleistenelement hat das Event ausgeloest */
		
		/* Toolbar ein- / ausblenden */
		if(e.getSource() == toolbaranzeigen){
			/* Toolbar ausblenden */
			if(toolbarsichtbar){
				hauptfenster.getToolbar().hideToolbar();
				toolbarsichtbar = false;
				toolbarposition.setEnabled(false);
			}
			/* Toolbar einblenden */
			else {
				hauptfenster.getToolbar().showToolbar();
				toolbarsichtbar = true;
				toolbarposition.setEnabled(true);
			}
			hauptfenster.pack();
		}
		
		/* Toolbar oben positionieren */
		if(e.getSource() == toolbartop){
			showtoolbarontop = true;
			hauptfenster.setToolbarPosition(showtoolbarontop);
			toolbartop.setEnabled(false);
			toolbarbottom.setEnabled(true);
			hauptfenster.pack();
		}
		/* Toolbar unten positionieren */
		if(e.getSource() == toolbarbottom){
			showtoolbarontop = false;
			hauptfenster.setToolbarPosition(showtoolbarontop);
			toolbartop.setEnabled(true);
			toolbarbottom.setEnabled(false);
			hauptfenster.pack();
		}		
		
		/* Navigator ein- / ausblenden */
		if(e.getSource() == navigatoranzeigen){
			/* Navigator ausblenden */
			if(navigatorsichtbar){
				hauptfenster.getNavigator().hideNavigator();
				navigatorsichtbar = false;
			}
			/* Navigator einblenden */
			else {
				hauptfenster.getNavigator().showNavigator();
				navigatorsichtbar = true;				
			}
			hauptfenster.pack();
		}
		
		/* Look and Feel */
		/* gehe alle RadioButtons durch */
		for(int i=0;i<lookandfeelcounter;i++){
			if(e.getSource() == lookandfeelradiobuttonarray[i]){
				lookandfeelradiobuttonarray[activeradiobutton].setSelected(false);
				activeradiobutton = i;
				try {
					UIManager.setLookAndFeel(lookandfeeluimanagerinfoarray[i].getClassName());
					SwingUtilities.updateComponentTreeUI(hauptfenster);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				hauptfenster.pack();
			}
		}
		
		/* Levelreader XML einlesen */
		if(e.getSource() == leveleinlesen){
			/* Filechooser mit XML-Filter */
			JFileChooser filechooser = new JFileChooser(Hauptfenster.getGameDirectory() + "levels");
			filechooser.setFileFilter(new XMLExtensionFileFilter("XML", new String[]{"xml"}));
			int returnvalue = filechooser.showOpenDialog(hauptfenster);
			if(returnvalue == JFileChooser.APPROVE_OPTION){
				/* lösche die Labels vom Spielfeld */
				hauptfenster.getSpielfeld().removeSpielfeldLabels();
				/* starte Levelreader (erwartet Instanz der DionaRap Models + MTConfiguration) und uebergebe ausgewaehlte Datei */
				LevelReader levelreader = new LevelReader(hauptfenster.getMTConfiguration(), hauptfenster.getDionaRapModel());
				levelreader.readLevel(filechooser.getSelectedFile().toString());
				/* neues Spielfeld + zeichne Icons */ 
				hauptfenster.getSpielfeld().addLabelsToSchachbrett();
				hauptfenster.getSpielfeld().repaintPawns();
				
				// TODO				
				
				hauptfenster.pack();
				/* Navigtor neu positionieren */
				hauptfenster.getNavigator().setNavPosition();
			}
		}
		
		/* Spieleinstellungen-Dialog anzeigen */
		if(e.getSource() == spieleinstellungen){
			JPanel panel = new JPanel();
			//TODO
		}
		
		/* anzeigen der Spielbeschreibung */
		if(e.getSource() == spielbeschreibung){
				new Spielebeschreibung(hauptfenster);
		}		
	}

}
