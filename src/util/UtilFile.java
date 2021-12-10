package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.stage.FileChooser;

public class UtilFile {


	public static String chooseFile() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Trace", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getName();
		}
		return null;
	}

	public static void writeFile (String res){
		File file = new File("Analyse.txt");
		try {
			if (!file.createNewFile()) {
				file.delete();
				file = new File("Analyse.txt");
			}	    		  		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(file, true);
				fileWriter.write(res);
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
