package a3;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class P1 {
	public static void main(String[] args) {
		// Ensure folder dir and subfolder sub-dir exists
		new File(".\\dir\\sub-dir").mkdirs();
		
		// Watcher observes changes in folder 'dir' and any of its sub-folders
		Watcher watcher = new Watcher();
		Watched dirWatched = new Watched(".\\dir\\file.txt");
		Watched subDirWatched = new Watched(".\\dir\\sub-dir\\sub-file.txt");

		dirWatched.addObserver(watcher);
		subDirWatched.addObserver(watcher);

		dirWatched.createEmptyFile();		// Create file.txt in ./dir
		subDirWatched.createEmptyFile();	// Create sub-file.txt in ./dir/sub-dir

		System.out.println();

		// Write text to file.txt in ./dir
		dirWatched.updateFile("Writing file");
		dirWatched.updateFile(" in main directory");
		dirWatched.updateFile(". Hello World!");
		
		// Write text to sub-file.txt in ./dir/sub-dir
		subDirWatched.updateFile("Writing in file in sub-directory. Hello World Again!");

		System.out.println();

		dirWatched.deleteFile();		// Delete file.txt in ./dir
		subDirWatched.deleteFile();		// Delete sub-file.txt in ./dir/sub-dir
	}

}

@SuppressWarnings("deprecation")
class Watcher implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		List<Object> arr = (List<Object>) arg;
		int mode = (Integer) arr.get(0);			// 1st args element
		String fileName = arr.get(1).toString();	// 2nd args element
		long changedSize = (long) arr.get(2);		// 3rd args element

		switch (mode) {
		case 1:		// createEmptyFile()
			System.out.println("File created: " + fileName);
			break;
		case 2:		// updateFile()
			System.out.println("File updated: " + fileName + ". " + changedSize + " bytes changed.");
			break;
		case 3:		// deleteFile()
			System.out.println("File deleted: " + fileName + ". " + changedSize + " bytes removed.");
			break;
		}
	}
}

@SuppressWarnings("deprecation")
class Watched extends Observable {
	private String fileName;
	private String path;
	private long fileSize;

	public Watched(String dir) {
		File file = new File(dir);
		this.fileName = file.getName();
		this.path = file.getParentFile().getPath();
		this.fileSize = file.length();
	}

	// Some file manipulation code are copied from
	// https://www.w3schools.com/java/java_files_create.asp
	public void createEmptyFile() {

		try {
			File file = new File(getFileDirectory());
			if (!file.createNewFile())	// Do nothing if file already exists
				return;

			this.fileSize = file.length();	// Set initial file size
			setChanged();
			notifyObservers(Arrays.asList(1, file.getName(), (long) 0));	// Pass in create mode, file name, and 0
			clearChanged();

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void updateFile(String text) {
		try {
			File file = new File(getFileDirectory());
			if (!file.exists())		// Do nothing if file doesn't exist
				return;

			FileWriter writer = new FileWriter(getFileDirectory(), true);	// Write input string to file
			writer.write(text + "\n");	// Add newline at the end
			writer.close();

			long oldSize = this.fileSize;
			this.fileSize = file.length();	// Set new file size

			setChanged();
			notifyObservers(Arrays.asList(2, file.getName(), this.fileSize - oldSize));	// Pass in update mode, file name, and file size difference
			clearChanged();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void deleteFile() {
		File file = new File(getFileDirectory());
		if (!file.exists())		// Do nothing if file doesn't exist
			return;

		file.delete();	// Delete file

		setChanged();
		notifyObservers(Arrays.asList(3, file.getName(), this.fileSize));	// Pass in delete mode, file name, and total file size
		clearChanged();
	}
	
	// Helper method for joining folder path and file name
	private String getFileDirectory() {
		return this.path + "\\" + this.fileName;
	}
}
