package de.svws_nrw.base;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse stellt Hilfsmethoden zum Zugriff auf CSV-Dateien zur Verfügung.
 */
public final class CsvReader {

	private CsvReader() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Bestimmt das Zip-Dateisystem anhand des übergebenen Pfades zu der zip-Datei.
	 *
	 * @param zipLocation   der Pfad zu der Zip-Datei
	 *
	 * @return das Zip-Dateisystem
	 *
	 * @throws IOException   wenn die Zip-Datei nicht geöffnet werden konnte
	 * @throws IllegalArgumentException   wenn der Pfad nicht dem RFC 2396 entspricht
	 */
	private static @NotNull FileSystem getZipFileSystem(@NotNull final String zipLocation) throws IOException, IllegalArgumentException {
		final URI uri = URI.create(zipLocation);
		try {
			return FileSystems.getFileSystem(uri);
		} catch (@SuppressWarnings("unused") final FileSystemNotFoundException e) {
			final Map<String, String> env = new HashMap<>();
			env.put("create", "true");
			return FileSystems.newFileSystem(uri, env);
		}
	}

	/**
	 * Diese Methode ermittelt für den angegebenen String location ein
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
	@SuppressWarnings("resource")
	private static Path getPath(final String location) throws URISyntaxException {
		final ClassLoader classLoader = CsvReader.class.getClassLoader();
		final var url = classLoader.getResource(location);
		if (url == null)
			return null;
		final var uri = url.toURI();
		if (uri.toString().contains("jar:file:")) {
			try {
				final String[] jar_path_elements = uri.toString().split("!");
				final FileSystem zipfs = getZipFileSystem(jar_path_elements[0]);
				return zipfs.getPath(jar_path_elements[1]);
			} catch (final IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return Paths.get(uri);
	}


	/**
	 * Erzeugt eine Liste von Objekten vom Typ T, indem die CSV-Datei von dem Pfad
	 * path eingelesen wird und die einzelnen Einträge in Objekt vom Typ T
	 * konvertiert werden.
	 *
	 * @param <T>     der generische Parameter für die Klasse T, von welcher die Objekt-Instanzen erzeugt werden
	 * @param path    der Pfad, unter dem sich die CSV-Resource befindet
	 * @param clazz   das Klassenobjekt zur generischen Klasse T
	 *
	 * @return die Liste der Objekte vom Typ T
	 */
	public static <T> List<T> from(final Path path, final Class<T> clazz) {
		try {
			final InputStream inputStream = Files.newInputStream(path);
			final CsvMapper mapper = new CsvMapper()
					.enable(CsvParser.Feature.WRAP_AS_ARRAY);
			final CsvSchema schema = CsvSchema
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
		} catch (final IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
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
	 * @return die Liste der Objekte vom Typ T
	 */
	public static <T> List<T> fromResource(final String location, final Class<T> clazz) {
		try {
			final Path path = getPath(location);
			return from(path, clazz);
		} catch (final URISyntaxException e) {
			e.printStackTrace();
			return new ArrayList<>();
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
	public static <T> List<T> fromResourceWithEmptyValues(final String location, final Class<T> clazz) {
		final Path path;
		try {
			path = getPath(location);
		} catch (final URISyntaxException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		try (InputStream inputStream = Files.newInputStream(path)) {
			final CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
			final CsvSchema schema = CsvSchema.emptySchema().withColumnSeparator(';').withQuoteChar('\"').withHeader();
			try (MappingIterator<T> it = mapper.readerFor(clazz).with(schema).readValues(inputStream)) {
				return it.readAll();
			}
		} catch (final IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}
