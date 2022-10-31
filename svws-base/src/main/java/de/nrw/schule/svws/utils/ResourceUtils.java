package de.nrw.schule.svws.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * Eine Klasse mit Hilfsfunktionen zum Zugriff auf Java-Resourcen
 */
public class ResourceUtils {

    /** Eine Map für die bereits geöffneten Jar-Dateisysteme */
    private static HashMap<String, FileSystem> jarFS = new HashMap<>();
	

	/**
	 * Ermittelt alle Dateien, die in dem angebenen Pfad path liegen und zu dem
	 * Package mit dem Name packageName oder einem Sub-Package davon gehören
	 * sowie die angegebene Dateiendung haben.
	 * 
	 * @param path            der Pfad in den Classpath-Resourcen
	 * @param packageName     der Name des Packages
	 * @param fileextension   die Dateiendung
	 * 
	 * @return eine List mit den Pfaden der gefundenen Dateien
	 */
	private static List<Path> getFilesInPath(FileSystem fs, String path, String packagePath, String fileextension) {
		List<Path> classes = new Vector<>();
		Path fullPath = fs.getPath(path + (path.endsWith("/") ? "" : "/") + packagePath);
		if (!Files.isDirectory(fullPath))
			return classes;
		try {
			Files.newDirectoryStream(fullPath).forEach(p -> {
				if (Files.isDirectory(p)) {
					classes.addAll(getFilesInPath(fs, path, packagePath + "/" + p.getFileName(), fileextension));
				} else if (Files.isRegularFile(p) && p.toString().endsWith(fileextension)) {
					classes.add(p);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classes;
	}
	
	
	/**
	 * Erstellt einen Pfad auf das gezippte Dateisystem einer JAR-Datei, die
	 * mit der übergebene uri angegeben wird.
	 *  
	 * @param uri   die URI für das JAR-Dateisystem
	 * 
	 * @return der Pfad in das JAR-Dateisystem 
	 */
	private static FileSystem getJARFileSystem(URI uri) {
		String[] array = uri.toString().split("!");
        FileSystem fs = jarFS.get(array[0]);
        if (fs != null)
            return fs;
		try {
		    fs = FileSystems.getFileSystem(URI.create(array[0]));
		    jarFS.put(array[0], fs);
			return fs;
		} catch (RuntimeException e) {
			// try to create a new Filesystem...
			Map<String, String> env = new HashMap<>();
			try {
			    fs = FileSystems.newFileSystem(URI.create(array[0]), env);
	            jarFS.put(array[0], fs);
				return fs;
			} catch (IOException exc) {
				throw new RuntimeException(exc);
			}
		}
	}	


	/**
	 * Ermittelt das Path-Objekt für den Zugriff auf die Datei mit dem angegebenen Dateinamen.
	 *  
	 * @param filename   der Dateiname
	 * 
	 * @return das Path-Objekt
	 */
	public static Path getFile(String filename) {
        Path path = null;
        try {
            URL url = ResourceUtils.class.getClassLoader().getResource(filename);
            URI uri = url.toURI();
            if ("jar".equals(uri.getScheme())) {
                FileSystem fs = getJARFileSystem(uri);
                String[] array = uri.toString().split("!");
                path = fs.getPath(array[1]);
            } else {
                path = Paths.get(uri);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return path;
	}


	/**
	 * Ermittelt alle Dateien, die mit dem Classloader dieser Klasse in dem Classpath in 
	 * dem Package packageName oder einem Sub-Package davon verfügbar sind sowie
	 * die angegebene Dateiendung haben.
	 *  
	 * @param packageName     das Package
	 * @param fileextension   die Dateiendung
	 * 
	 * @return eine List mit den Pfaden der gefundenen Dateien
	 */
	public static List<Path> getFilesInPackage(String packageName, String fileextension) {
		List<Path> result = new Vector<>();
		Enumeration<URL> res;
		try {
			String packagePath = packageName.replace(".", "/");
			res = ResourceUtils.class.getClassLoader().getResources(packagePath);
			while (res.hasMoreElements()) {
				URI uri;
				try {
					uri = res.nextElement().toURI();
				} catch (URISyntaxException e) {
					continue;
				}
				Path path = null;
				if ("jar".equals(uri.getScheme())) {
					FileSystem fs = getJARFileSystem(uri);
					String[] array = uri.toString().split("!");
					path = fs.getPath(array[1]);
					int j = Paths.get(packagePath).getNameCount();
					for (int i = 0; i < j; i++)
						path = path.getParent();
					result.addAll(getFilesInPath(fs, path.toString(), packagePath, fileextension));
				} else {
					path = Paths.get(uri);
					int j = Paths.get(packagePath).getNameCount();
					for (int i = 0; i < j; i++)
						path = path.getParent();
					result.addAll(getFilesInPath(FileSystems.getDefault(), path.toString(), packagePath, fileextension));
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}			
		return result;
	}
	

	/**
	 * Lädt alle JSON-Dateien aus dem angegebenen Package, welche das angegebene Präfix haben und
	 * die angegebene Dateiendung. Die JSON-Objekt werden dann in Objekte der angebenenen
	 * Klasse gemappt.
	 * 
	 * @param <T>   			die Klasse, von welcher die neuen Objekte erzeugt werden
	 * @param resourcePackage   das Package
	 * @param prefix			das benötigte Präfix im Dateinamen
	 * @param clazz             das Klassenobjekt der Klasse, von welcher neue Objekte ereugt werden
	 * 
	 * @return eine Map, welche dem Teil des Dateinamens ohne Präfix und Endung das neu erzeugte Objekt zuordnet
	 */
	public static <T> Map<String, T> json2Classes(String resourcePackage, String prefix, Class<T> clazz) {
		return json2Classes(resourcePackage, prefix, "", clazz);
	}
	
	
	/**
	 * Lädt alle JSON-Dateien aus dem angegebenen Package, welche das angegebene Präfix haben und
	 * die angegebene Dateiendung. Die JSON-Objekt werden dann in Objekte der angebenenen
	 * Klasse gemappt.
	 * 
	 * @param <T>   			die Klasse, von welcher die neuen Objekte erzeugt werden
	 * @param resourcePackage   das Package
	 * @param prefix			das benötigte Präfix im Dateinamen
	 * @param suffix            das benötigte Suffix im Dateinamen (vor dem .json)
	 * @param clazz             das Klassenobjekt der Klasse, von welcher neue Objekte ereugt werden
	 * 
	 * @return eine Map, welche dem Teil des Dateinamens ohne Präfix und Endung das neu erzeugte Objekt zuordnet
	 */
	public static <T> Map<String, T> json2Classes(String resourcePackage, String prefix, String suffix, Class<T> clazz) {
		Map<String, T> classes = new TreeMap<>();
		List<Path> paths = ResourceUtils.getFilesInPackage(resourcePackage, ".json");
		ObjectMapper mapper = new ObjectMapper();
		for (Path filePath: paths) {
			String filename = filePath.getFileName().toString();
			try {
	            if (filename.toLowerCase().startsWith(prefix.toLowerCase()) && filename.toLowerCase().endsWith((suffix + ".json").toLowerCase())) {
	            	String name = filename.substring(prefix.length(), filename.length() - (suffix + ".json").length());
	            	String json = Files.readString(filePath, StandardCharsets.UTF_8);
	    		    classes.put(name, mapper.readValue(json, clazz));
	            }
			} catch (IOException e) {
				System.out.println("Fehler beim Laden eines Testfalles aus der Datei " + filename + "!");
				e.printStackTrace();
			}
		}
		return classes;
	}

}
