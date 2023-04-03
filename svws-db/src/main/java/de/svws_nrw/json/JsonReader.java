package de.svws_nrw.json;

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
	 * @param jarPath      der JAR-Pfad
	 * @param jarResource  der Name der Ressource
	 *
	 * @return das Dateisystem zum Zugriff auf die Ressource
	 *
	 * @throws IOException   falls das Dateisystem für die Ressource nicht erstellt werden kann
	 */
	@SuppressWarnings("resource")
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
	        if (url == null)
	        	return null;
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

}
