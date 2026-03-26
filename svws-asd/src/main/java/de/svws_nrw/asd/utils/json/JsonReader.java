package de.svws_nrw.asd.utils.json;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.asd.data.CoreTypeData;


/**
 * Diese Klasse stellt Hilfsmethoden zum Zugriff auf JSON-Dateien zur Verfügung.
 */
public final class JsonReader {

	/** Der Jackson2-Objekt-Mapper für das Konvertieren */
	public static final ObjectMapper mapper = new ObjectMapper();

	/** Eine HashMap zum Zwischenspeicher von Dateisystemen zum Zugriff auf Zip-Ressourcen */
	private static HashMap<String, FileSystem> mapFilesystems = new HashMap<>();

	private JsonReader() {
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
	public static String fromResource(final String location) throws IOException {
		final Path path = getPath(location);
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
	public static <T> T fromResource(final String location, final Class<T> clazz) {
		try {
			final String json = fromResource(location);
			return mapper.readValue(json, clazz);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Erzeugt zu der JSON-Ressource an der angebenen Stelle (location) die Informationen
	 * zu einem Core-Type mit den DTOs vom T.
	 *
	 * @param <T>        der Typ der DTOs von dem Core-Type.
	 * @param location   der Ort, an dem sich die CSV-Resource befindet
	 * @param clazz      das Klassenobjekt zur generischen Klasse T
	 *
	 * @return die Daten des Core-Types aus der JSON-Ressource.
	 */
	public static <T extends CoreTypeData> JsonCoreTypeData<T> fromResourceGetCoreTypeData(final String location, final Class<T> clazz) {
		try {
			final String json = fromResource(location);
			return new JsonCoreTypeData<>(json, clazz);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Liest eine JSON-Datei als UTF-8-String aus der angegebenen Resource
	 * ein.
	 *
	 * @param location   der Ort, an dem sich die JSON-Resource befindet
	 *
	 * @return die JSON-Datei als String
	 */
	public static String fromResourceOrEmptyString(final String location) {
		try {
			final Path path = getPath(location);
			return Files.readString(path);
		} catch (@SuppressWarnings("unused") final IOException e) {
			return "";
		}
	}


	/**
	 * Erstellt einen Pfad für den Zugriff auf die übergebene JAR-Resource.
	 * Dabei wird ggf. ein Dateisystem für den Zugriff auf das JAR angelegt.
	 *
	 * @param jarPath      der JAR-Pfad
	 * @param jarResource  der Name der Ressource
	 *
	 * @return das Dateisystem zum Zugriff auf die Ressource
	 *
	 * @throws IOException   falls das Dateisystem für die Ressource nicht erstellt werden kann
	 */
	private static Path getJarPath(final String jarPath, final String jarResource) throws IOException {
		FileSystem result = mapFilesystems.get(jarPath);
		if (result == null) {
			final URI uri = URI.create(jarPath);
			try {
				result = FileSystems.getFileSystem(uri);
			} catch (@SuppressWarnings("unused") final FileSystemNotFoundException e) {
				final Map<String, String> env = new HashMap<>();
				env.put("create", "true");
				try {
					result = FileSystems.newFileSystem(uri, env);
				} catch (final IOException exception) {
					throw new IOException("Fehler beim Erstellen eines Dateisystem für die Ressource '" + jarResource + "' unter '" + jarPath + "'", exception);
				}
			}
			mapFilesystems.put(jarPath, result);
		}
		return result.getPath(jarResource);
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
	private static Path getPath(final String location) throws IOException {
		try {
			final ClassLoader classLoader = JsonReader.class.getClassLoader();
			final var url = classLoader.getResource(location);
			if (url == null) {
				return null;
			}
			final var uri = url.toURI();
			if (uri.toString().contains("jar:file:")) {
				final String[] jar_path_elements = uri.toString().split("!");
				return getJarPath(jar_path_elements[0], jar_path_elements[1]);
			}
			return Paths.get(uri);
		} catch (IOException | URISyntaxException e) {
			throw new IOException("Fehler beim Zugriff auf die Ressource '" + location + "'.", e);
		}
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
		final List<Path> found = new ArrayList<>();
		final String separator = fs.getSeparator();
		final String fullPathString = path + (path.endsWith(separator) ? "" : separator) + packagePath;
		final Path fullPath = fs.getPath(fullPathString);
		if (!Files.isDirectory(fullPath)) {
			return found;
		}
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(fullPath)) {
			for (final Path p : dirStream) {
				if (Files.isDirectory(p)) {
					found.addAll(getFilesInPath(fs, path, packagePath + "/" + p.getFileName(), fileextension));
				} else if (Files.isRegularFile(p) && p.toString().endsWith(fileextension)) {
					found.add(p);
				}
			}
		} catch (final IOException e) {
			throw new IOException("Fehler beim Lesen der Dateien im Pfad " + path, e);
		}
		return found;
	}

	/**
	 * Hilfsmethode für {@link JsonReader#getFilesInPackage(String, String)}. Bestimmt
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
	private static void getFilesInPackageFromURL(final URL url, final String packagePath, final List<Path> result, final String fileextension)
			throws IOException {
		final URI uri;
		try {
			uri = url.toURI();
		} catch (@SuppressWarnings("unused") final URISyntaxException e) {
			return;
		}
		final FileSystem fs;
		final Path resPath;
		if ("jar".equals(uri.getScheme())) {
			final String[] array = uri.toString().split("!");
			final String jarPath = array[0];
			final String jarResource = array[1];
			resPath = getJarPath(jarPath, jarResource);
			fs = resPath.getFileSystem();
		} else {
			resPath = Paths.get(uri);
			fs = FileSystems.getDefault();
		}

		// Bestimme den Teils des Pfad im Dateisystem vor dem Package-Teil
		Path rootPath = resPath;
		final int count = Paths.get(packagePath).getNameCount();
		for (int i = 0; i < count; i++) {
			rootPath = rootPath.getParent();
		}
		result.addAll(getFilesInPath(fs, rootPath.toString(), packagePath, fileextension));
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
		try {
			final String packagePath = packageName.replace(".", "/");
			final Enumeration<URL> res = JsonReader.class.getClassLoader().getResources(packagePath);
			while (res.hasMoreElements()) {
				getFilesInPackageFromURL(res.nextElement(), packagePath, result, fileextension);
			}
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}

}
