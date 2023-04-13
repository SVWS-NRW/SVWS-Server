package de.svws_nrw.base;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import java.util.TreeMap;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * Eine Klasse mit Hilfsfunktionen zum Zugriff auf Java-Resourcen
 */
public final class ResourceUtils {

	/** Eine Map für die bereits geöffneten Jar-Dateisysteme */
	private static final HashMap<String, FileSystem> jarFS = new HashMap<>();

	private static final String FILE_EXTENSION_JSON = ".json";


	private ResourceUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Ermittelt alle Dateien, die in dem angebenen Pfad path liegen und zu dem
	 * Package mit dem Name packageName oder einem Sub-Package davon gehören
	 * sowie die angegebene Dateiendung haben.
	 *
	 * @param fs              das Dateisystem, auf dem die Dateien gesucht werden.
	 * @param path            der Pfad in den Classpath-Resourcen
	 * @param packagePath     der relative Pfad für das Packages
	 * @param fileextension   die Dateiendung
	 *
	 * @return eine List mit den Pfaden der gefundenen Dateien
	 *
	 * @throws IOException wenn ein Fehler beim Lesen der Dateien auftritt
	 */
	private static List<Path> getFilesInPath(final FileSystem fs, final String path, final String packagePath, final String fileextension) throws IOException {
		final List<Path> classes = new ArrayList<>();
		final Path fullPath = fs.getPath(path + (path.endsWith("/") ? "" : "/") + packagePath);
		if (!Files.isDirectory(fullPath))
			return classes;
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(fullPath)) {
			for (final Path p : dirStream) {
				if (Files.isDirectory(p)) {
					classes.addAll(getFilesInPath(fs, path, packagePath + "/" + p.getFileName(), fileextension));
				} else if (Files.isRegularFile(p) && p.toString().endsWith(fileextension)) {
					classes.add(p);
				}
			}
		} catch (final IOException e) {
			throw new IOException("Fehler beim Lesen der Dateien im Pfad " + path, e);
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
	 *
	 * @throws IOException   falls das JAR-Dateisystem nicht erstellt werden kann
	 */
	@SuppressWarnings("resource")
	private static FileSystem getJARFileSystem(final URI uri) throws IOException {
		final String[] array = uri.toString().split("!");
		FileSystem fs = jarFS.get(array[0]);
		if (fs != null)
			return fs;
		try {
			fs = FileSystems.getFileSystem(URI.create(array[0]));
			jarFS.put(array[0], fs);
			return fs;
		} catch (@SuppressWarnings("unused") final RuntimeException e) {
			// try to create a new Filesystem...
			final Map<String, String> env = new HashMap<>();
			fs = FileSystems.newFileSystem(URI.create(array[0]), env);
			jarFS.put(array[0], fs);
			return fs;
		}
	}


	/**
	 * Ermittelt das Path-Objekt für den Zugriff auf die Datei mit dem angegebenen Dateinamen.
	 *
	 * @param filename   der Dateiname
	 *
	 * @return das Path-Objekt
	 */
	public static Path getFile(final String filename) {
		Path path = null;
		try {
			final URL url = ResourceUtils.class.getClassLoader().getResource(filename);
			final URI uri = url.toURI();
			if ("jar".equals(uri.getScheme())) {
				@SuppressWarnings("resource")
				final
				FileSystem fs = getJARFileSystem(uri);
				final String[] array = uri.toString().split("!");
				path = fs.getPath(array[1]);
			} else {
				path = Paths.get(uri);
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return path;
	}


	/**
	 * Hilfsmethode für {@link ResourceUtils#getFilesInPackage(String, String)}. Bestimmt
	 * für die übergebene URL die entsprechenden Dateien und schreibt {@link Path}-Objekt
	 * für diese in die Liste.
	 *
	 * @param url             die URL des Packages
	 * @param packagePath     der Pfad für das Package
	 * @param result          die Liste, wo die {@link Path}-Objekt für die Dateien ergänzt werden
	 * @param fileextension   die Dateiendung
	 *
	 * @throws IOException    bei einem Fehler beim Zugriff auf das Package
	 */
	private static void getFilesInPackageFromURL(final URL url, final String packagePath, final List<Path> result, final String fileextension) throws IOException {
		URI uri;
		try {
			uri = url.toURI();
		} catch (@SuppressWarnings("unused") final URISyntaxException e) {
			return;
		}
		Path path = null;
		if ("jar".equals(uri.getScheme())) {
			@SuppressWarnings("resource")
			final FileSystem fs = getJARFileSystem(uri);
			final String[] array = uri.toString().split("!");
			path = fs.getPath(array[1]);
			final int j = Paths.get(packagePath).getNameCount();
			for (int i = 0; i < j; i++)
				path = path.getParent();
			result.addAll(getFilesInPath(fs, path.toString(), packagePath, fileextension));
		} else {
			path = Paths.get(uri);
			final int j = Paths.get(packagePath).getNameCount();
			for (int i = 0; i < j; i++)
				path = path.getParent();
			@SuppressWarnings("resource")
			final FileSystem fs = FileSystems.getDefault();
			result.addAll(getFilesInPath(fs, path.toString(), packagePath, fileextension));
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
	public static List<Path> getFilesInPackage(final String packageName, final String fileextension) {
		final List<Path> result = new ArrayList<>();
		Enumeration<URL> res;
		try {
			final String packagePath = packageName.replace(".", "/");
			res = ResourceUtils.class.getClassLoader().getResources(packagePath);
			while (res.hasMoreElements())
				getFilesInPackageFromURL(res.nextElement(), packagePath, result, fileextension);
		} catch (final IOException e1) {
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
	 *
	 * @throws IOException bei einem Fehler beim Laden der JSON-Daten
	 */
	public static <T> Map<String, T> json2Classes(final String resourcePackage, final String prefix, final Class<T> clazz) throws IOException {
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
	 *
	 * @throws IOException bei einem Fehler beim Laden der JSON-Daten
	 */
	public static <T> Map<String, T> json2Classes(final String resourcePackage, final String prefix, final String suffix, final Class<T> clazz) throws IOException {
		final Map<String, T> classes = new TreeMap<>();
		final List<Path> paths = ResourceUtils.getFilesInPackage(resourcePackage, FILE_EXTENSION_JSON);
		final ObjectMapper mapper = new ObjectMapper();
		for (final Path filePath: paths) {
			final String filename = filePath.getFileName().toString();
			try {
				if (filename.toLowerCase().startsWith(prefix.toLowerCase()) && filename.toLowerCase().endsWith((suffix + FILE_EXTENSION_JSON).toLowerCase())) {
					final String name = filename.substring(prefix.length(), filename.length() - (suffix + FILE_EXTENSION_JSON).length());
					final String json = Files.readString(filePath, StandardCharsets.UTF_8);
					classes.put(name, mapper.readValue(json, clazz));
				}
			} catch (final IOException e) {
				throw new IOException("Fehler beim Lesen aus der Datei " + filename + "!", e);
			}
		}
		return classes;
	}

}
