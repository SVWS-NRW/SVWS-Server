package de.nrw.schule.svws.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;


/**
 * Diese Klasse stellt Hilfsmethoden zum Zugriff auf CSV-Dateien zur Verfügung. 
 */
public class CsvReader {
	
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
	 * @throws URISyntaxException   falls der Pfad der Ressource nicht in eine URI 
	 *                              umgewandelt werden kann.  
	 */
	private static Path getPath(String location) throws URISyntaxException {
        ClassLoader classLoader = CsvReader.class.getClassLoader();
        var url = classLoader.getResource(location);
        if (url == null)
        	return null;
        var uri = url.toURI();
		if (uri.toString().contains("jar:file:")) {
			try {
				String[] jar_path_elements = uri.toString().split("!");
				FileSystem zipfs = null;
				try {
					zipfs = FileSystems.getFileSystem(URI.create(jar_path_elements[0]));
				} catch (FileSystemNotFoundException e) {
					Map<String, String> env = new HashMap<>(); 
					env.put("create", "true");
					zipfs = FileSystems.newFileSystem(URI.create(jar_path_elements[0]), env);
				}
				return zipfs.getPath(jar_path_elements[1]);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return Paths.get(uri);
	}
	
	
	/**
	 * Erzeugt eine Liste von Objekten vom Typ T, indem die CSV-Datei an der Stelle
	 * location eingelesen wird und die einzelnen Einträge in Objekt vom Typ T
	 * konvertiert werden. 
	 *  
	 * @param <T>        der generische Parameter für die Klasse T, von welcher die Objekt-Instanzen erzeugt werden
	 * @param location   der Ort, an dem sich die CSV-Resource befindet
	 * @param clazz      das Klassenobjekt zur generischen Klasse T
	 * 
	 * @return die Liste der Objekt vom Typ T
	 */
	public static <T> List<T> fromResource(String location, Class<T> clazz) {
        try {
			Path path = getPath(location);
			InputStream inputStream = Files.newInputStream(path);
	        CsvMapper mapper = new CsvMapper()
	        		.enable(CsvParser.Feature.WRAP_AS_ARRAY);
	        CsvSchema schema = CsvSchema
	        		.emptySchema()
	    	        .withColumnSeparator(';')
	    	        .withQuoteChar('\"')
	    	        .withNullValue("")
	                .withHeader();
			try (MappingIterator<T> it = mapper
			        .readerFor(clazz)
			        .with(schema)
			        .readValues(inputStream)) {
	        	return it.readAll();
        	}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("ERROR: Cannot access csv resource file '" + location + "'" + e);
			e.printStackTrace();
			return null;			
		}
	}

	
	/**
	 * Erzeugt eine Liste von Objekten vom Typ T, indem die CSV-Datei an der Stelle
	 * location eingelesen wird und die einzelnen Einträge in Objekt vom Typ T
	 * konvertiert werden. 
	 *  
	 * @param <T>        der generische Parameter für die Klasse T, von welcher die Objekt-Instanzen erzeugt werden
	 * @param location   der Ort, an dem sich die CSV-Resource befindet
	 * @param clazz      das Klassenobjekt zur generischen Klasse T
	 * 
	 * @return die Liste der Objekt vom Typ T
	 */
	public static <T> List<T> fromResourceWithEmptyValues(String location, Class<T> clazz) {
        try {
			Path path = getPath(location);
			InputStream inputStream = Files.newInputStream(path);
	        CsvMapper mapper = new CsvMapper()
	        		.enable(CsvParser.Feature.WRAP_AS_ARRAY);
	        CsvSchema schema = CsvSchema
	        		.emptySchema()
	    	        .withColumnSeparator(';')
	    	        .withQuoteChar('\"')
	                .withHeader();
			try (MappingIterator<T> it = mapper
			        .readerFor(clazz)
			        .with(schema)
			        .readValues(inputStream)) {
				return it.readAll();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("ERROR: Cannot access csv resource file '" + location + "'" + e);
			e.printStackTrace();
			return null;			
		}
	}
	
}
