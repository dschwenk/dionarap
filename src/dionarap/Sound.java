package dionarap;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;


/**
 * Klasse realisiert den Sound
 * 
 * @author Daniel Schwenk
 * @version Aufgabe 7
 */
public class Sound {

	private Hauptfenster hauptfenster;
	
	/* Audio Streams */
	private AudioInputStream aistream_gameWon;
	private AudioInputStream aistream_gameOver;
	private AudioInputStream aistream_move;
	private AudioInputStream aistream_shoot;
	private AudioInputStream aistream_lasershoot;
	
	/* Clips */
	private Clip clip_gameWon;
	private Clip clip_gameOver;
	private Clip clip_move;
	private Clip clip_shoot;
	private Clip clip_lasershoot;
	
	/**
	 * Konstruktor der Klasse Sound
	 * @throws IOException 
	 */
	public Sound(Hauptfenster hauptfenster){
		
		this.hauptfenster = hauptfenster;
		String sound_dir = this.hauptfenster.getGameDirectory() + "sounds" + File.separator;
		
		/* erzeuge Audiostreams */
		try {
			aistream_gameWon = AudioSystem.getAudioInputStream(new File(sound_dir + "Gewonnen.wav"));			
		} catch (Exception ex){
			System.out.println("Fehler beim Oeffnen der Sounddatei 'GameOver.wav'" + ex);
		}
		try {
			aistream_gameOver = AudioSystem.getAudioInputStream(new File(sound_dir + "GameOver.wav"));			
		} catch (Exception ex){
			System.out.println("Fehler beim Oeffnen der Sounddatei 'Gewonnen.wav'" + ex);
		}
		try {
			aistream_move = AudioSystem.getAudioInputStream(new File(sound_dir + "Maustaste.wav"));			
		} catch (Exception ex){
			System.out.println("Fehler beim Oeffnen der Sounddatei 'Maustaste.wav'" + ex);
		}
		try {
			aistream_shoot = AudioSystem.getAudioInputStream(new File(sound_dir + "TreffenHinderniss.wav"));			
		} catch (Exception ex){
			System.out.println("Fehler beim Oeffnen der Sounddatei 'TreffenHinderniss.wav'" + ex);
		}
		try {
			aistream_lasershoot = AudioSystem.getAudioInputStream(new File(sound_dir + "Beamer.wav"));			
		} catch (Exception ex){
			System.out.println("Fehler beim Oeffnen der Sounddatei 'Beamer.wav'" + ex);
		}
		
		/* Clips aus AudioInputStream in Speicher laden */
		try {
			DataLine.Info info = new DataLine.Info(Clip.class, aistream_gameWon.getFormat());
			clip_gameWon = (Clip) AudioSystem.getLine(info);			
			info = new DataLine.Info(Clip.class, aistream_gameOver.getFormat());
			clip_gameOver = (Clip) AudioSystem.getLine(info);			
			info = new DataLine.Info(Clip.class, aistream_move.getFormat());
			clip_move = (Clip) AudioSystem.getLine(info);			
			info = new DataLine.Info(Clip.class, aistream_shoot.getFormat());
			clip_shoot = (Clip) AudioSystem.getLine(info);			
			info = new DataLine.Info(Clip.class, aistream_lasershoot.getFormat());
			clip_lasershoot = (Clip) AudioSystem.getLine(info);			
		}
		catch (LineUnavailableException luex){
			System.out.println("Fehler beim Erzeugen eines Clip-Objekts" + luex);
		}

		/* Clips oeffnen */
		try {
			clip_gameWon.open(aistream_gameWon);
			clip_gameOver.open(aistream_gameOver);
			clip_move.open(aistream_move);
			clip_shoot.open(aistream_shoot);
			clip_lasershoot.open(aistream_lasershoot);
		}
		catch (LineUnavailableException luex){
			System.out.println("Fehler beim Oeffnen eines Clip-Objekts" + luex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode zum Abspielen des Sounds wenn Spiel gewonnen wurde
	 */
	public void playSoundGameWon(){
		try {
			clip_gameWon.stop();
			clip_gameWon.setFramePosition(0);			
			clip_gameWon.start();
		}
		catch (Exception ex){
			System.out.println("Fehler beim Abspielen des Clips 'GameWon'" + ex);
		}
	}	


	/**
	 * Methode zum Abspielen des Sounds wenn Spiel verloren wurde
	 */
	public void playSoundGameOver(){
		try {
			clip_gameOver.stop();
			clip_gameOver.setFramePosition(0);
			clip_gameOver.start();
		}
		catch (Exception ex){
			System.out.println("Fehler beim Abspielen des Clips 'GameOver'" + ex);
		}
	}


	/**
	 * Methode zum Abspielen des Sounds wenn der Spieler sich bewegt
	 */
	public void playSoundMove(){
		try {
			clip_move.stop();
			clip_move.setFramePosition(0);
			clip_move.start();
		}
		catch (Exception ex){
			System.out.println("Fehler beim Abspielen des Clips 'Maustatste'" + ex);
		}
	}	


	/**
	 * Methode zum Abspielen des Sounds wenn Spiel einen Schuss abgibt
	 */
	public void playSoundShoot(){
		try {			
			clip_shoot.stop();
			clip_shoot.setFramePosition(0);
			clip_lasershoot.stop();
			clip_lasershoot.setFramePosition(0);
			/* Sound an Theme anpassen */
			if(this.hauptfenster.getTheme().equals("Dracula")){
				clip_shoot.start();				
			}
			else {
				clip_lasershoot.start();
			}
		}
		catch (Exception ex){
			System.out.println("Fehler beim Abspielen des Clips 'GameOver'" + ex);
		}
	}


	/**
	 * Methode stopt alle Sounds
	 */
	public void stopAllSounds(){
		clip_gameWon.stop();
		clip_gameOver.stop();
		clip_move.stop();
		clip_shoot.stop();
		clip_lasershoot.stop();
	}
}