package dionarap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse erm�glich es mittels Methoden Operationen auf die Highscore-Datei
 * durchzuf�hren. Sie besitzt keinen Konstruktor, da sie das Singleton-Pattern umsetzt. 
 * Die Highscore-Datei wird beim ersten Aufruf einer beliebigen Methode dieser Klasse
 * erstellt und mit Default-Werten belegt. 
 * Der Speicherort der Highscore-Datei ist das Verzeichnis, in welchem sich die 
 * ausf�hrbare Datei des Spiels befindet. 
 * 
 * Benutzung: Um eine Instanz der Klasse HighScoreFile zu erstellen ist kein Konstruktor zu verwenden, 
 * sondern die Klassenmethode {@link #getInstance()}.
 * Die Methoden zum Schreiben in die Datei und zum Lesen aus der Datei werden dann �ber die von
 * getInstance() zur�ckgegebene Referenz aufgerufen. Zum Beispiel:
 * 
 * HighScoreFile.getInstance().readFromHighscore()
 * 
 * 
 * @author HS Ravensburg/Weingarten
 *
 */
public class HighScoreFile {
	/**
	 * Name der Highscore-Datei.
	 */
	private final static String fileName = new String("Highscore.tex");
	/**
	 * Absoluter Pfad zur Executable.
	 */
	private final static String userDir = System.getProperty("user.dir");
	/**
	 * Separator-Zeichen
	 */
	private final static String separator = File.separator;
	/**
	 * Absoluter Pfad der Highscore-Datei.
	 * Setzt sich zusammen aus {@link #userDir}+{@link #separator}+{{@link #fileName}.
	 */
	private final static String filePath = userDir+separator+fileName;
	
	/**
	 * Kennzeichnung daf�r, dass ein Punktestand nicht in der Highscore-Datei vorhanden ist.
	 * Kann einzig von der Methode {@link HighScoreFile#getScorePosition(int)} zur�ckgegeben werden.
	 */
	public final static int SCORE_TO_LOW_FOR_HIGHSCORE = -1;
	/**
	 * Dies ist die maximale Anzahl von Bestplatzierten, welche in der Highscore-Datei 
	 * gespeichert sind.
	 */
	public final static int MAX_PLACES = 10;
	/**
	 * Anzahl der Zeilen, die eine Platzierung in der Datei einnimmt. Hier 2:
	 * 
	 * Spalte1: Name
	 * Spalte2: Punktestand
	 * Spalte3: Name
	 * Spalte4: Punktestand
	 *   ...       ...
	 */
	public final static int ROWS_PER_PLACE = 2;
	/**
	 * Ein Name und ein Punktestand k�nnen in der Datei max. 10 Zeichen lang sein.
	 * Gr��ere Namen und Punktest�nde werden abgeschnitten. 
	 */
	private final static int MAX_CHARCTERS_PER_LINE = 10;
	
	/**
	 * Default-Name, welcher in die Highscore-Datei eingetragen wird, sobald dieser erstellt wurde.
	 */
	private final static String defaultName = new String("Anonym");
	/**
	 * Default-Punktestand, welcher in die Highscore-Datei eingetragen wird, sobald dieser erstellt wurde.
	 * Betr�gt 0.
	 */
	private final static String defaultScore = new String("0");
	
	/**
	 * Speichert eine Instanz von HighScoreFile.
	 */
	private static HighScoreFile instance = null;
	
	/**
	 * Leerer Standard-Konstruktor. 
	 * Instanziierung �ber Konstruktor ist hier nicht m�glich, da diese Klasse
	 * das Singleton-Pattern umsetzt.
	 */
	private HighScoreFile(){}
	
	/**
	 * Ersetzt den Konsturktor. Hier wird die Klasse HighScoreFile instanziiert.
	 * Beim ersten Aufruf dieser Methode wird �berpr�ft, 
	 * ob die Highscore-Datei bereits vorhanden ist. Wenn nicht, so wird diese Datei
	 * erstellt und mit Default-Werten belegt. 
	 * 
	 * @return Referenz auf die einmalige Instanz des Typs HighScoreFile 
	 * @throws IOException, falls die Highscore-Datei nicht erstellt werden konnte oder 
	 * aber die Rechte nicht vorhanden sind, um auf die bereits vorhandene Highscore-Datei zuzugreifen.
	 */
	public static HighScoreFile getInstance() throws IOException{
		if(HighScoreFile.instance == null){
			HighScoreFile.instance = new HighScoreFile();
			//Erstelle Highscore-Datei und bef�lle sie mit Initialwerten,
			//sofern noch nicht geschehen
			fillWithDefaultValues();
			
			return HighScoreFile.instance;
		}
		else return HighScoreFile.instance;
	}
	
	/**
	 * �berpr�ft, ob der als Argument �bergebener Punktestand in die Highscore �bernommen werden soll.
	 * Genauer: Ist der �bergebene Punktestand gr��er als ein in der Datei bereits vorhandener Punktestand,
	 * so soll der �bergebene Punktestand in die Highscore-Datei �bernommen werden. 
	 * @param score Punktestand, der darauf �berpr�ft werden soll, ob dieser hoch genug ist, um in die 
	 * Highscore-Datei �bernommen zu werden.
	 * @return Position, die der als Argument �bergebene Punktestand in der Highscore-Datei einnehmen
	 * w�rde. M�gliche Position liegen zwischen 1 und 10. 
	 * Falls allerdings der �bergebene Punktestand nicht hoch genung ist, um die
	 * Highscore-Datei �bernommen zu werden, so ist der R�ckgabewert SCORE_TO_LOW_FOR_HIGHSCORE. 
	 */
	public int getScorePosition(int score) throws IOException{
		//Datei-Stream erstellen (nur lesend)
		FileInputStream inStream = new FileInputStream(filePath);
		/*Einen Bereich in der Datei sperren. 
		 *Die Sperre beginnt bei Datei-Anfang und endet bei Byte (2^63)-1.
		 *Dieser Lock ist �quivalent zum Aufruf von inStream.getChannel().lock().
		 *Der Unterschied hierbei ist, dass lock() die Datei exklusiv sperrt. 
		 *lock(0,Long.MAX_VALUE,true) dagegen errichtet einen shared-lock. 
		 *Beim shared-lock sind parallele Lesezugriffe m�glich. 
		*/
		FileLock fileLock = inStream.getChannel().lock(0,Long.MAX_VALUE,true);
		
		BufferedReader fileReader 
			= new BufferedReader(new InputStreamReader(inStream));
		
		String curScore = new String();
		curScore = fileReader.readLine();
		
		//Liest so lange Werte aus der Datei, bis die max. erlaubte Anzahl an zu lesenden Zeilen erreicht wurde.
		//Die maximale Zeilenanzahl ergibt sich aus dem Produkt der max. m�glichen Platzierungen in der Datei und
		//den Zeilen, die eine Platzierung einnehmen darf.
		int i=0;
		while(curScore!= null 
				&& i<HighScoreFile.MAX_PLACES*HighScoreFile.ROWS_PER_PLACE){
			if(i%HighScoreFile.ROWS_PER_PLACE!=0){ //nur den Punktestand �berpr�fen, nicht den Namen
				if(Integer.valueOf(curScore)<score){
					//Liefert die Position des �bergebenen Punktestandes in der Highscore-Datei
					//Die Platzierungen beginnen bei 1 
					fileLock.close();
					fileReader.close();
					return i/HighScoreFile.ROWS_PER_PLACE+1; 
				}
			}
			curScore = fileReader.readLine();
			++i;
		}
		
		fileLock.close();
		fileReader.close();
		return HighScoreFile.SCORE_TO_LOW_FOR_HIGHSCORE;
	}
	
	/**
	 * Hat die selbe Funktionalit�t wie {@link #getScorePosition(int)}.
	 * Diese Version �ffnet die Highscore-Datei nicht, sondern erwartet einen
	 * bereits offenen Datei-Stream zur Highscore-Datei als Argument.
	 * Es wird ferner kein Datei-Lock erstellt.
	 * 
	 * @param score
	 * @param rAF Ein bereits offener Stream zur Highscore-Datei.
	 * @return Position, die der als Argument �bergebene Punktestand in der Highscore-Datei einnehmen
	 * w�rde.
	 * @throws IOException
	 */
	private int checkScoreIsInTopTen(int score,RandomAccessFile rAF) throws IOException{
		//Liest so lange Werte aus der Datei, bis die max. erlaubte Anzahl an zu lesenden Zeilen erreicht wurde.
		//Die maximale Zeilenanzahl ergibt sich aus dem Produkt der max. m�glichen Platzierungen in der Datei und
		//den Zeilen, die eine Platzierung einnehmen darf.
		int i=0;
		String curScore = new String();
		//Datei wird von Anfang an gelesen
		rAF.seek(0);
		curScore = rAF.readLine();
		while(curScore!= null 
				&& i<HighScoreFile.MAX_PLACES*HighScoreFile.ROWS_PER_PLACE){
			if(i%HighScoreFile.ROWS_PER_PLACE!=0){ //nur den Punktestand �berpr�fen, nicht den Namen
				if(Integer.valueOf(curScore)<score){
					//liefert die  Position des �bergebenen Punktestandes in der Highscore-Datei
					//die Platzierungen beginnen bei 1 (und nicht bei 0)
					return i/HighScoreFile.ROWS_PER_PLACE+1; 
				}
			}
			curScore = rAF.readLine();
			++i;
		}
		
		return HighScoreFile.SCORE_TO_LOW_FOR_HIGHSCORE;
}
	
	/**
	 * F�gt den als den als Argument �bergebenen Bestplatzierten samt seinem Punktestand
	 * an die passende Stelle in Highscore-Datei ein. Ist allerdings der Punktestand 
	 * zu niedrig, um in die Highscore aufgenommen zu werden, so wird nichts eingef�gt.
	 * 
	 * @param name Spielername
	 * @param score Punktestand des Spielers
	 * @throws IOException, falls Zugriff auf die Highscore-Datei verweigert wird, 
	 * weil keine Zugriffsrechte vorhanden sind.
	 */
	public int writeScoreIntoFile(String name, int score) throws IOException{
		
		
		//Name zu gro�? - Dann k�rzen.
		if(name.length()>HighScoreFile.MAX_CHARCTERS_PER_LINE){
			name = name.substring(0, HighScoreFile.MAX_CHARCTERS_PER_LINE);
		}
		
		//Name ist leer?
		if(name.isEmpty()){
			name = HighScoreFile.defaultName;
		}
		
		//Name besteht nur aus Leerzeichen?
		String tmpName = new String(name);
		if((tmpName.replaceAll(" ", "")).isEmpty()){
			name = HighScoreFile.defaultName;
		}
		tmpName = null;
		
		//Datei-Stream �ffnen (zum Lesen und Schreiben)
		RandomAccessFile rAF = new RandomAccessFile(filePath,"rw");
		
		//Datei-Zugriff durch exklusiven Lock sperren.
		//Ab hier an ist sowohl lesender als auch schreibender Zugriff auf die Datei
		//nur durch den eben erstellten Stream m�glich!
		FileLock fileLock = rAF.getChannel().lock();
	
		//Position ermitteln, die der �bergebene Punktestand in der Highscore einnehmen w�rde
		int posInHighscore = checkScoreIsInTopTen(score,rAF);
		//Punktestand zu niedrig f�r die Highscore?
		if(posInHighscore==HighScoreFile.SCORE_TO_LOW_FOR_HIGHSCORE){
			fileLock.close();
			rAF.close();
			return HighScoreFile.SCORE_TO_LOW_FOR_HIGHSCORE;
		}
		
		
		/*
		 * Schreibe alle in der Datei vorhandenen Werte in die Highscore-Datei
		 * bis zur Platzierung, die der Spieler erreicht hat.
		 */
		String[] values = readFromHighscore(rAF);
	   
		//Dateiinhalt l�schen
		rAF.setLength(0);
		//New-Line-Zeichen ermitteln (systemabh�ngig)
		String lineSeparator = System.getProperty("line.separator");
		rAF.seek(0); //Datei-Zeiger auf Dateianfang setzen
		for(int i=0;i<(posInHighscore-1)*HighScoreFile.ROWS_PER_PLACE;i++){
			rAF.writeBytes(values[i]);
			rAF.writeBytes(lineSeparator);
		}
		
		//Trage den Spieler samt seinem Punktestand an die entsprechende Position
		//in die Highscore-Datei ein
		rAF.writeBytes(name);
		rAF.writeBytes(lineSeparator);
		rAF.writeBytes(Integer.toString(score));
		
		
		/*Trage nun die restlichen Platzierungen in die Highscore-Datei ein. 
		 * - Vorausgesetzt nat�rlich, der Spieler hat nicht den letzten Platz erreicht.
		 */
		int maxValue = HighScoreFile.ROWS_PER_PLACE*(HighScoreFile.MAX_PLACES-1);
		for(int i=(posInHighscore-1)*HighScoreFile.ROWS_PER_PLACE;i<maxValue;++i){
			rAF.writeBytes(lineSeparator);
			rAF.writeBytes(values[i]);
		}
		fileLock.close();
		rAF.close();
		
		return posInHighscore;
	}
	
	/**
	 * Liest die in der Highscore-Datei vorhandenen Datens�zte und gibt diese als 
	 * String-Array zur�ck. 
	 * 
	 * @return String-Array mit der Bestenliste. Die Anordnung der Platzierungen im Array ist folgende:
	 * [Name_1][Punktestand_1][Name_2][Punktestand_2]...[Name_10][Punktestand_10]
	 * Die Postion eines Spielers im Array entspricht der Position des Spielers in der Highscore. 
	 * @throws IOException. Wird geworfen, falls Datei nicht vorhanden ist oder aber
	 * die Zugriffsrechte f�r diese Datei fehlen.
	 */
	public String[] readFromHighscore() throws IOException{
		//Datei-Stream �ffnen (lesend)
		FileInputStream inStream = new FileInputStream(filePath);
		/*Einen Bereich in der Datei sperren. 
		 *Die Sperre beginnt bei Datei-Anfang und endet bei Byte (2^63)-1.
		 *Dieser Lock ist �quivalent zum Aufruf von inStream.getChannel().lock().
		 *Der Unterschied hierbei ist, dass lock() die Datei exklusiv sperrt. 
		 *lock(0,Long.MAX_VALUE,true) dagegen errichtet einen shared-lock. 
		 *Beim shared-lock sind parallele Lesezugriffe m�glich. 
		*/
		FileLock fileLock = inStream.getChannel().lock(0,Long.MAX_VALUE,true);
	
		BufferedReader fileReader 
			= new BufferedReader(new InputStreamReader(inStream));
		
		List<String> highscore = new ArrayList<String>();
		String tmpLine = new String();
		int i=0;
		
		//Lies so lange Zeilen aus Highscore-Datei, bis die max. 
		//erlaubte Anzahl an zu lesenden Zeilen erreicht wurde.
		//Speichere jede gelesene Zeile in eine Liste.
		while((tmpLine = fileReader.readLine())!= null && i<HighScoreFile.MAX_PLACES*HighScoreFile.ROWS_PER_PLACE){
			if(tmpLine.length()>HighScoreFile.MAX_CHARCTERS_PER_LINE){
				tmpLine=tmpLine.substring(0, HighScoreFile.MAX_CHARCTERS_PER_LINE);
			}
			highscore.add(tmpLine);
			i++;
		}
		
		fileLock.close();
		fileReader.close();
		return (String[])highscore.toArray(new String[highscore.size()]);
	}
	
	/**
	 * Hat die selbe Funktionalit�t wie {@link #readFromHighscore()}.
	 * Der Unterschied dazu besteht darin, dass hier kein eigener Stream 
	 * ge�ffnet wird. Zugriff auf die Highscore-Datei geschieht ausschlie�lich
	 * �ber den als Argument �bergebenen Datei-Stream.
	 * Es wird ferner kein Datei-Lock erstellt. 
	 * 
	 * @param rAF Datei-Stream zur Highscore-Datei
	 * @return String-Array mit der Bestenliste.
	 * @throws IOException
	 */
	private String[] readFromHighscore(RandomAccessFile rAF) throws IOException{
		//Beginne das Lesen an Dateianfang
		rAF.seek(0);
		List<String> highscore = new ArrayList<String>();
		String tmpLine = new String();
		int i=0;
		
		//Lese so lange Zeilen aus Highscore-Datei, bis die max. 
		//erlaubte Anzahl an zu lesenden Zeilen erreicht wurde.
		//Speichere jede gelesene Zeile in eine Liste.
		while((tmpLine = rAF.readLine())!= null && i<HighScoreFile.MAX_PLACES*HighScoreFile.ROWS_PER_PLACE){
			if(tmpLine.length()>HighScoreFile.MAX_CHARCTERS_PER_LINE){
				tmpLine=tmpLine.substring(0, HighScoreFile.MAX_CHARCTERS_PER_LINE);
			}
			highscore.add(tmpLine);
			i++;
		}
		return (String[])highscore.toArray(new String[highscore.size()]);
	}
	
	
	/**
	 * Bef�llt die Highscore-Datei mit Initialwerten. Wenn noch keine Highscore-Datei
	 * vorhanden ist, dann wird eine erstellt.
	 * Es wird stets die max. erlaubte Anzahl an Platzierungen eingetragen. 
	 * 
	 * @throws IOException, falls Highscore-Datei nicht existiert oder Zugriff auf diese 
	 * wegen fehlenden Rechten nicht gestattet ist.
	 */
	private static void fillWithDefaultValues() throws IOException{
		//Datei-Stream �ffnen (zum Lesen und Schreiben)
		RandomAccessFile rAF = new RandomAccessFile(filePath,"rw");
		FileLock fileLock = null;
		try{
			//Versuche einen Lock auf die Highscore-Datei zu bekommen.
			fileLock = rAF.getChannel().tryLock();
		}
		finally{
			//Lock konnte nicht erstellt werden? Dann h�lt eine andere Spielistanz 
			//bereits den Lock. In diesem Falle muss die Highscore-Datei
			//in dieser Instanz nicht mehr mit Initialwerten gef�llt werden. 
			//Dies erledigt die Instanz, welche den Lock h�lt. 
			if(fileLock==null){
				rAF.close();
				return;
			}
			//Lock erfolgreich stellt aber die Datei ist nicht mehr leer?
			//Dann ist auch kein F�llen der Highscore-Datei mit Initialwerten
			//notwendig.
			if(rAF.length()!= 0){
				fileLock.close();
				rAF.close();
				return;
			}
			
		}
		String lineSeparator = System.getProperty("line.separator");
		rAF.seek(0); //Datei-Zeiger auf Dateianfang setzen
	
		//Datei mit Default-Werten f�llen
		for(int i=0;i<HighScoreFile.MAX_PLACES;i++){
			rAF.writeBytes(defaultName);
			rAF.writeBytes(lineSeparator);
			rAF.writeBytes(defaultScore);
			rAF.writeBytes(lineSeparator);
		}

		fileLock.close();
		rAF.close();
	}
}











