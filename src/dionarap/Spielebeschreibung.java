package dionarap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/**
 * Klasse realisiert die Darstellung der Spielbeschreibung, abgeleitet von  <code>JDialog</code>
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 5
 */
public class Spielebeschreibung extends JDialog {

	private Hauptfenster hauptfenster;
	private URL url;
    
	
	/**
	 * Konstruktor der Spielebeschreibung vom Typ <code>JDialog</code>
	 * @param parent Vaterfenster vom Typ <code>Hauptfenster</code>
	 */		
	public Spielebeschreibung(Hauptfenster hauptfenster){
		this.hauptfenster = hauptfenster;
		this.setLayout(new BorderLayout());
		this.setTitle("Spielbeschreibung");

		String gamedirectory = Hauptfenster.getGameDirectory();
		String separator = System.getProperty("file.separator");
        
		JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        /* URL Objekt erstellen */
        try {
        	// Windows - file:///
        	// Linux - file://
            url = new URL("file:///" + gamedirectory + "html" + separator + "spielbeschreibung.html");
        }
        catch (MalformedURLException ex) {
            System.err.println("File kann nicht gelesen werden: " + url);
        }
        /* zeige Seite an */
        try {
            editorPane.setPage(url);
        }
        catch (IOException ex) {
            System.err.println("File kann nicht gelesen werden: " + url);
        }
        /* Scrollbalken einfuegen */
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(editorScrollPane, "Center");
        
        final JButton closeButton = new JButton("Schlie\u00dfen");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
            	Spielebeschreibung.this.dispose();
            }
        });
        this.add(closeButton, "South");            
        
        /* Groese, Ausrichtung + anzeigen */
        this.setSize(700, 600);
        this.setLocationRelativeTo(hauptfenster);
        this.setVisible(true);  
	}
}
