package de.svws_nrw.transpiler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;



/**
 * A class to access java resources and to store their content in TranspilerResource
 * objects.
 */
public class ResourceUtils {
	
// TODO language of javadoc
	
	/**
	 * Ermittelt alle Dateien, die in dem angebenen Pfad path liegen und zu dem
	 * Package mit dem Name packageName oder einem Sub-Package davon gehören
	 * sowie die angegebene Dateiendung haben.
	 * 
	 * @param path            der Pfad in den Classpath-Resourcen
	 * @param packageName     der Name des Packages
	 * @param fileextension   die Dateiendung
	 * 
	 * @return eine Liste mit den gefundenen Dateien
	 */
	private static List<TranspilerResource> getFilesInPath(FileSystem fs, String path, String packagePath, String fileextension) {
		List<TranspilerResource> classes = new Vector<>();
		Path fullPath = fs.getPath(path + (path.endsWith("/") ? "" : "/") + packagePath);
		if (!Files.isDirectory(fullPath))
			return classes;
		try {
			try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(fullPath)) { 
				dirStream.forEach(p -> {
					if (Files.isDirectory(p)) {
						classes.addAll(getFilesInPath(fs, path, "".equals(packagePath) ? p.getFileName().toString() : packagePath + "/" + p.getFileName(), fileextension));
					} else if (Files.isRegularFile(p) && p.toString().endsWith(fileextension)) {
						String shortFilename = p.getFileName().toString();
						try {
							classes.add(new TranspilerResource(packagePath.replace('/', '.'), shortFilename.substring(0, shortFilename.length() - fileextension.length()), fileextension, p));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
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
		try {
			return FileSystems.getFileSystem(URI.create(array[0]));
		} catch (@SuppressWarnings("unused") RuntimeException e) {
			// try to create a new Filesystem...
			Map<String, String> env = new HashMap<>();
			try {
				return FileSystems.newFileSystem(URI.create(array[0]), env);
			} catch (IOException exc) {
				throw new RuntimeException(exc);
			}
		}
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
	public static List<TranspilerResource> getFilesInPackage(String packageName, String fileextension) {
		List<TranspilerResource> result = new Vector<>();
		Enumeration<URL> res;
		try {
			String packagePath = packageName.replace(".", "/");
			res = ResourceUtils.class.getClassLoader().getResources(packagePath);
			while (res.hasMoreElements()) {
				URI uri;
				try {
					uri = res.nextElement().toURI();
				} catch (@SuppressWarnings("unused") URISyntaxException e) {
					continue;
				}
				Path path = null;
				if ("jar".equals(uri.getScheme())) {
					try (FileSystem fs = getJARFileSystem(uri)) {
						String[] array = uri.toString().split("!");
						path = fs.getPath(array[1]);
						int j = "".equals(packagePath) ? 0 : Paths.get(packagePath).getNameCount();
						for (int i = 0; i < j; i++)
							path = path.getParent();
						result.addAll(getFilesInPath(fs, path.toString(), packagePath, fileextension));
					}
				} else {
					path = Paths.get(uri);
					int j = "".equals(packagePath) ? 0 : Paths.get(packagePath).getNameCount();
					for (int i = 0; i < j; i++)
						path = path.getParent();
					@SuppressWarnings("resource")
					FileSystem fs = FileSystems.getDefault();
					result.addAll(getFilesInPath(fs, path.toString(), packagePath, fileextension));
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}			
		return result;
	}

}
