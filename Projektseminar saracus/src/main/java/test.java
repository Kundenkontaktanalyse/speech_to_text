import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class test extends FileChooser {

	public static void main(String[] args) throws IOException {
		File oldNameDirectory = new File("C:\\Users\\Daniel\\Desktop\\s2t\\Gespraeche\\Gespraech03");
		File newNameDirectory = new File("C:\\Users\\Daniel\\Desktop\\s2t\\Gespraeche\\Hallo01");

		
		test myTest = new test();
		myTest.testMethod(oldNameDirectory, newNameDirectory);
		File[] files = newNameDirectory.listFiles();

		String[] endings = { ".json", ".wav", ".wav" };

		
		
		System.out.println(newNameDirectory.getName());
		for (int i = 0; i < files.length; i++) {
			File newNameFile = new File(
					newNameDirectory.toString() + "\\" + newNameDirectory.getName() + "_" + i + endings[i]);
			myTest.testMethod(files[i], newNameFile);

		}

	}

	public void testMethod(File oldName, File newName) {

		oldName.renameTo(newName);

		System.out.println("cheers");
	}

}