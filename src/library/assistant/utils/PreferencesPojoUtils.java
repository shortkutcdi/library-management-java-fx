package library.assistant.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import library.assistant.ui.settings.PreferencesPojo;

public class PreferencesPojoUtils {
	
	public static char SEPARATOR = File.separatorChar;
	public static String CONFIG_FILE = "preferences.json";
	private static File file;

	
	public static void initConfig() throws IOException {
		
		PreferencesPojo preferencesPojo = new PreferencesPojo();
		Gson gson = new Gson();
		String pathToFile = new File(".").getCanonicalPath() + SEPARATOR + CONFIG_FILE;
		
		if (checkIfFileExistsElseCreate(pathToFile) ) {
			try (FileWriter writer = new FileWriter(pathToFile)) {
				// Java objects to File
				gson.toJson(preferencesPojo, writer );
				
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static boolean checkIfFileExistsElseCreate(String pathToFile) throws IOException {
		file = new File(pathToFile);
		if (file.createNewFile()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void initConfig2() throws IOException {
		// le constructeur par défaut initialise ses attributs
		PreferencesPojo pojo = new PreferencesPojo();
		String pathToFile = new File(".").getCanonicalPath() + SEPARATOR + CONFIG_FILE;
		if (!checkIfFileExistsElseCreate(pathToFile)) {
			serializePojo(pojo);
		}
	}

	public static void serializePojo(PreferencesPojo pojo) throws IOException {
		Gson gson = new Gson();

		String pathToFile = new File(".").getCanonicalPath() + SEPARATOR + CONFIG_FILE;
		// si fichier absent le crée
		checkIfFileExistsElseCreate(pathToFile);
		
		try (FileOutputStream fos = new FileOutputStream(pathToFile);
                OutputStreamWriter isr = new OutputStreamWriter(fos, 
                        StandardCharsets.UTF_8)) 
		{

			gson.toJson(pojo, isr);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PreferencesPojo deserializePreferencesPojo() throws IOException {
		Gson gson = new Gson();
		// constructeur initialise les attributs avec des valeurs par défauts
		PreferencesPojo  defaultPojo = new PreferencesPojo(); 
		PreferencesPojo  deserializedPojo = null;
		
		String pathToFile = new File(".").getCanonicalPath() + SEPARATOR + CONFIG_FILE;
		
		if (checkIfFileExistsElseCreate(pathToFile)) {
			deserializedPojo = gson.fromJson(new FileReader(pathToFile), PreferencesPojo.class);
		}
		
		return deserializedPojo!= null? deserializedPojo : defaultPojo;

	}

}
