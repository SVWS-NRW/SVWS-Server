package de.svws_nrw.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse stellt Hilfsmethoden zum Zugriff auf CSV-Dateien zur Verfügung.
 */
public final class CsvReader {

	/** Das Default CSV-Schema für diese Klasse */
	private static final CsvSchema csvSchemaDefault = CsvSchema.emptySchema().withColumnSeparator(';').withQuoteChar('\"').withNullValue("").withHeader();

	/** Das Default CSV-Schema für diese Klasse zur Verwendung von Default-Werten statt null bei leeren CSV-Einträgen */
	private static final CsvSchema csvSchemaDefaultEmptyValues = CsvSchema.emptySchema().withColumnSeparator(';').withQuoteChar('\"').withHeader();

	/** Das CSV-Schema für Kurs 42 */
	private static final CsvSchema csvSchemaKurs42 = CsvSchema.emptySchema().withColumnSeparator(';').withQuoteChar('\"').withNullValue("").withHeader();


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
	private static @NotNull FileSystem getZipFileSystem(final @NotNull String zipLocation) throws IOException, IllegalArgumentException {
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
			final CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
			try (MappingIterator<T> it = mapper.readerFor(clazz).with(csvSchemaDefault).readValues(inputStream)) {
				return it.readAll();
			}
		} catch (final IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}


	/**
	 * Erzeugt eine Liste von Objekten vom Typ T, indem die CSV-Datei von dem Pfad
	 * path eingelesen wird und die einzelnen Einträge in Objekt vom Typ T
	 * konvertiert werden. Ist bei der UTF8-Datei ein BOM vorhanden, so wird dieses beim Einlesen entfernt.
	 *
	 * @param <T>     der generische Parameter für die Klasse T, von welcher die Objekt-Instanzen erzeugt werden
	 * @param path    der Pfad, unter dem sich die CSV-Resource befindet
	 * @param clazz   das Klassenobjekt zur generischen Klasse T
	 *
	 * @return die Liste der Objekte vom Typ T
	 */
	public static <T> List<T> fromKurs42(final Path path, final Class<T> clazz) {
		try {
			final byte[] data = Files.readAllBytes(path);
			final int offset = ((data.length > 2) && ((data[0] & 0xFF) == 0xEF) && ((data[1] & 0xFF) == 0xBB) && ((data[2] & 0xFF) == 0xBF)) ? 3 : 0;
			final String csvData = new String(data, offset, data.length - offset, StandardCharsets.UTF_8);
			final CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
			try (MappingIterator<T> it = mapper.readerFor(clazz).with(csvSchemaKurs42).readValues(csvData)) {
				return it.readAll();
			}
		} catch (final IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}


	/**
	 * Erzeugt eine Liste von Objekten vom Typ T, indem die CSV-Datei aus dem übergebenen
	 * String eingelesen wird und die einzelnen Einträge in Objekt vom Typ T
	 * konvertiert werden.
	 *
	 * @param <T>     der generische Parameter für die Klasse T, von welcher die Objekt-Instanzen erzeugt werden
	 * @param data    der Inhalt der CSV-Datei
	 * @param clazz   das Klassenobjekt zur generischen Klasse T
	 *
	 * @return die Liste der Objekte vom Typ T
	 *
	 * @throws IOException   im Falle eines Fehlers
	 */
	public static <T> List<T> fromKurs42(final byte[] data, final Class<T> clazz) throws IOException {
		final int offset = ((data.length > 2) && ((data[0] & 0xFF) == 0xEF) && ((data[1] & 0xFF) == 0xBB) && ((data[2] & 0xFF) == 0xBF)) ? 3 : 0;
		final String csvData = new String(data, offset, data.length - offset, StandardCharsets.UTF_8);
		final CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
		try (MappingIterator<T> it = mapper.readerFor(clazz).with(csvSchemaKurs42).readValues(csvData)) {
			return it.readAll();
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
			try (MappingIterator<T> it = mapper.readerFor(clazz).with(csvSchemaDefaultEmptyValues).readValues(inputStream)) {
				return it.readAll();
			}
		} catch (final IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}


	/**
	 * Liest Daten für das übergebene CSV-Schema aus dem übergebenen byte-Array. Dabei wird ggf. ein BOM entfernt.
	 * Das Ergebnis wird in einer Liste der von Objekten der übergebenen Klasse gespeichert.
	 *
	 * @param <T>         der Typ der Klasse der Daten
	 * @param clazz       die Klasse der Daten
	 * @param csvSchema   das Schema der CSV-Daten
	 * @param data        das Byte-Array mit den CSV-Daten
	 *
	 * @return die Liste mit den Daten-Objekten
	 *
	 * @throws IOException falls ein Fehler beim Einlesen der Daten auftritt, z.B. dass das Schema nicht zu den Daten passt
	 */
	public static <T> List<T> fromUntis(final @NotNull Class<T> clazz, final @NotNull CsvSchema csvSchema, final byte[] data) throws IOException {
		final int offset = ((data.length > 2) && ((data[0] & 0xFF) == 0xEF) && ((data[1] & 0xFF) == 0xBB) && ((data[2] & 0xFF) == 0xBF)) ? 3 : 0;
		final String csvData = new String(data, offset, data.length - offset, StandardCharsets.UTF_8);
		final ObjectReader reader = new CsvMapper().readerFor(clazz).with(csvSchema);
		try (MappingIterator<T> it = reader.readValues(csvData)) {
			return it.readAll();
		}
	}

}
