package de.nrw.schule.svws.json;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Diese Klasse stellt Hilfsmethoden zum Zugriff auf JSON-Dateien zur Verfügung. 
 */
public class JsonReader {

	/** Der Jackson2-Objekt-Mapper für das Konvertieren */
	public static ObjectMapper mapper = new ObjectMapper(); 	
	
	/** Eine HashMap zum Zwischenspeicher von Dateisystemen zum Zugriff auf Zip-Ressourcen */
	private static HashMap<String, FileSystem> mapFilesystems = new HashMap<>(); 
	

	/**
	 * Erstellt einen Pfad für den Zugriff auf die übergebene JAR-Resource.   
	 * Dabei wird ggf. ein Dateisystem für den Zugriff auf das JAR angelegt.
	 *  
	 * @param jar_path   der JAR-Pfad
	 * 
	 * @return das Dateisystem zum Zugriff auf die Ressource
	 * 
	 * @throws IOException   falls das Dateisystem für die Ressource nicht erstellt werden kann 
	 */
	@SuppressWarnings("resource")
	private static Path getJarPath(String jar_path, String jar_resource) throws IOException {
		FileSystem result = mapFilesystems.get(jar_path);
		if (result == null) {
			URI uri = URI.create(jar_path);
			try {
				result = FileSystems.getFileSystem(uri);
			} catch (FileSystemNotFoundException e) {
				Map<String, String> env = new HashMap<>(); 
				env.put("create", "true");
				try {
					result = FileSystems.newFileSystem(uri, env);
				} catch (IOException exception) {
					throw new IOException("Fehler beim Erstellen eines Dateisystem für die Ressource '" + jar_resource + "' unter '" + jar_path + "'", exception);
				}
			}
			mapFilesystems.put(jar_path, result);
		}
		return result.getPath(jar_resource);
	}



	/**
	 * Diese Methode ermittelt für den angebenen String location ein 
	 * zugehöriges Path-Objekt aus dem zugehörigen Resource-Ordner.
	 * Dabei wird auch der Zugriff auf ein ZIP-Dateisystem genutzt,
	 * falls sich die Resource in einem JAR-File befindet.
	 * 
	 * @param location   der Pfad der Resource
	 * 
	 * @return das Path-Objekt zum Zugriff auf die Ressource
	 * 
	 * @throws IOException   falls der Zugriff auf die Ressource fehlschlägt.  
	 */
	private static Path getPath(String location) throws IOException {
		try {
	        ClassLoader classLoader = JsonReader.class.getClassLoader();
	        var url = classLoader.getResource(location);
	        if (url == null)
	        	return null;
	        var uri = url.toURI();
			if (uri.toString().contains("jar:file:")) {
				String[] jar_path_elements = uri.toString().split("!");
				return getJarPath(jar_path_elements[0], jar_path_elements[1]);
			}
			return Paths.get(uri);
		} catch (IOException | URISyntaxException e) {
			throw new IOException("Fehler beim Zugriff auf die Ressource '" + location + "'.", e);			
		}
	}


	/**
	 * Liest eine JSON-Datei als UTF-8-String aus der angegebenen Resource
	 * ein.
	 *  
	 * @param location   der Ort, an dem sich die JSON-Resource befindet
	 * 
	 * @return die JSON-Datei als String
	 * 
	 * @throws IOException          falls die Datei nicht erfolgreich gelesen werden kann
	 */
	public static String fromResource(String location) throws IOException {
		Path path = getPath(location);
		return Files.readString(path);
	}


	/**
	 * Erzeugt zu der JSON-Ressource an der angebenen Stelle (location) ein Objekt vom Typ T.
	 *  
	 * @param <T>        der generische Parameter für die Klasse T des Objects
	 * @param location   der Ort, an dem sich die CSV-Resource befindet
	 * @param clazz      das Klassenobjekt zur generischen Klasse T
	 * 
	 * @return die Liste der Objekt vom Typ T
	 */
	public static <T> T fromResource(String location, Class<T> clazz) {
		try {
			String json = fromResource(location);
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
