package dionarap;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * Klasse realisiert einen Dateifilter, abgeleitet <code>FileFilter</code>
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 5
 */
public class XMLExtensionFileFilter extends FileFilter {

    private String beschreibung;
    private String dateiextension[];
    
    public XMLExtensionFileFilter(String beschreibung, String dateiendung[]) {
        this.beschreibung = beschreibung;
        this.dateiextension = dateiendung;
    }
    
	/**
	 * Legt fest, dass Verzeichnise und XML-Dateien anagezeigt werden
	 * @return boolean
	 */
	public boolean accept(File file) {
        /* zeige Verzeichnisse an */
		if(file.isDirectory()){
            return true;
        }
		else {
			String path = file.getAbsolutePath();
			for(int i = 0; i < dateiextension.length; i++){
				/* pruefe ob Dateiendung XML */
				String extension = dateiextension[i];
				if((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')){
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Gibt die Beschreibung zurueck
	 * @return String beschreibung
	 */
	public String getDescription() {
		return beschreibung;
	}

}
