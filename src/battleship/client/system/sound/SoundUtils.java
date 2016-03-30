package battleship.client.system.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 
 * @author Nghia
 * Handle all stuffs with audio file
 */
public class SoundUtils {
    private final int BUFFER_SIZE = 128000;
    private SourceDataLine sourceLine;
    private BooleanControl muteControl;
    private boolean isMute = false;
    
    /**
     * Create AudioInputStream from a file
     */
    public static AudioInputStream getStream(String path) {
    	File soundFile = null;
    	AudioInputStream audioStream = null;
    	
    	try {
    		soundFile = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
        	audioStream = createReusableAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        
        return audioStream;
    }
    
    /**
     * Recreate AudioInputStream to support mark/reset
     */
    private static AudioInputStream createReusableAudioInputStream(File file) throws IOException, UnsupportedAudioFileException
    {
        AudioInputStream ais = null;
        try
        {
            ais = AudioSystem.getAudioInputStream(file);
            byte[] buffer = new byte[1024 * 32];
            int read = 0;
            ByteArrayOutputStream baos = 
                new ByteArrayOutputStream(buffer.length);
            while ((read = ais.read(buffer, 0, buffer.length)) != -1)
            {
                baos.write(buffer, 0, read);
            }
            AudioInputStream reusableAis = 
                new AudioInputStream(
                        new ByteArrayInputStream(baos.toByteArray()),
                        ais.getFormat(),
                        AudioSystem.NOT_SPECIFIED);
            return reusableAis;
        }
        finally
        {
            if (ais != null)
            {
                ais.close();
            }
        }
    }
    
    /**
     * Create SourceDataLine object to play audio
     */
    private SourceDataLine getSource(AudioInputStream audioStream) {
    	AudioFormat audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceLine = null;
        try {
        	sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
           // System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(1);
        }
		return sourceLine;
    }
    
    /**
     * Play the audio
     */
    public void play(AudioInputStream audioStream) {
    	try {
    		audioStream.reset();
    		sourceLine = getSource(audioStream);
	    	sourceLine.start();
	    	muteControl = (BooleanControl) sourceLine
        	        .getControl(BooleanControl.Type.MUTE);
	    	muteControl.setValue(isMute);
	        int nBytesRead = 0;
	        byte[] abData = new byte[BUFFER_SIZE];
	        
	        while (nBytesRead != -1) {
	            try {
	                nBytesRead = audioStream.read(abData, 0, abData.length);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            if (nBytesRead >= 0) {
	                @SuppressWarnings("unused")
	                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
	            }
	        }
	        
	        stop();
    	} catch (Exception e) {
    		/***
    		 * Empty
    		 */
    	}
    }
    
    public void stop() {
    	sourceLine.drain();
        sourceLine.close();
    }
    
    public void toggleMute() {
    	muteControl.setValue(!isMute);
    	isMute = !isMute;
    }
}