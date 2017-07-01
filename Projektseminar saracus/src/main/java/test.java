import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class test extends FileChooser {

	 public static void main(String[] args) throws IOException {
	        File inF = new File("C:\\Users\\Daniel\\Desktop\\s2t\\ffmpegOrdner\\ffmpeg.exe");
	        File outF = new File("C:\\Users\\Daniel\\Desktop\\s2t\\Gespraeche\\ffmpeg.exe");
	        copyFile(inF, outF);
	    } 
	
	
	@SuppressWarnings("resource")
	public static void copyFile(File in, File out) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(in).getChannel();
            outChannel = new FileOutputStream(out).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            } catch (IOException e) {}
        } 
}
}