package de.svws_nrw.api.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Utility-Klasse zum sicheren und generischen Parsen von Framed Octet-Streams.
 * Schützt den Server aktiv vor Denial-of-Service-Angriffen durch native Limit-Validierung.
 */
public final class FramedStreamParser {

	/** Ein Zufallszahlen-Generator */
	private static final Random random = new Random();

	/** Die zu nutzende Instanz des Object-Mappers */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** Die maximale Länge in Bytes für die Json-Meta-Daten */
	private static final int MAX_METADATA_LENGTH = 4096;


	private FramedStreamParser() {
		throw new IllegalStateException("Eine Instantiierung dieser Utility-Klasse ist nicht vorgesehen.");
	}


	/**
	 * Erzeugt eine einfache Anwort mit der Angabe, ob die Operation erfolgreich war und
	 * mit dem Log derOperation.
	 *
	 * @param success   gibt an, ob die Operation erfolgreich war oder nicht
	 * @param log       der Log der Operation
	 *
	 * @return das Response-Objekt
	 */
	private static SimpleOperationResponse simpleResponse(final boolean success, final LogConsumerList log) {
		final SimpleOperationResponse response = new SimpleOperationResponse();
		response.success = success;
		response.log = log.getStrings();
		return response;
	}


	/**
	 * Parsed einen Framed-Stream, validiert die enthaltenen Json-Metadaten, prüft die Dateigrößen-Limits
	 * und streamt die Binärdaten direkt in eine temporäre Datei auf der Festplatte.
	 *
	 * @param <M>             der Typ des Metadaten-DTOs
	 * @param logger          der zu verwendende Logger
	 * @param log             die Liste, welche die Meldungen der Loggers mitprotokolliert
	 * @param inputStream     der InputStream
	 * @param metadataClass   die Klasse des Metadaten-DTOs
	 * @param maxBinarySize   das Limit für die Binärdaten in Bytes (z.B. 1 GB)
	 * @param tempPrefix      das Präfix für den Dateinamen der temporären Datei
	 * @param tempSuffix      das Suffix für den Dateinamen der temporären Datei
	 *
	 * @return ein {@link FramedPayload}-Objekt zur Verwendung mit try-with-resources
	 *
	 * @throws ApiOperationException   wenn beim Lesen, Parsen oder Schreiben Fehler auftreten
	 */
	public static <M> FramedPayload<M> parse(final Logger logger, final LogConsumerList log, final InputStream inputStream, final Class<M> metadataClass, final long maxBinarySize,
			final String tempPrefix, final String tempSuffix) throws ApiOperationException {

		logger.logLn("Lese die Metadaten aus den übertragenenen Daten.");

		// JAX-RS kümmert sich um das Schließen des payloadStream, wir verwenden einen DataInputStream für das eigentliche Auslesen
		final DataInputStream dataInputStream = new DataInputStream(inputStream);

		// Lese das Längenpräfix für die Metadaten (4-Byte, Big-Endian)
		int metaLength;
		try {
			metaLength = dataInputStream.readInt();
		} catch (final Exception e) {
			logger.logLn(2, "Fehler: Die Länge der Metadaten konnte nicht bestimmt werden.");
			throw new ApiOperationException(Status.BAD_REQUEST, e, simpleResponse(false, log));
		}

		// Prüfe die Größe der Metadaten-Länge, u.a. zur Vermeidung von DoS ...
		if ((metaLength <= 0) || (metaLength > MAX_METADATA_LENGTH)) {
			logger.logLn(2, "Fehler: Die Länger der Metadaten ist fehlerhaft oder zu groß.");
			throw new ApiOperationException(Status.BAD_REQUEST, simpleResponse(false, log));
		}

		// ... lese dann exakt die Anzahl an Bytes für die Json-Metadaten aus
		final byte[] metaBytes = new byte[metaLength];
		try {
			dataInputStream.readFully(metaBytes);
		} catch (final IOException e) {
			logger.logLn(2, "Fehler: Die Metadaten konnten nicht extrahiert werden.");
			throw new ApiOperationException(Status.BAD_REQUEST, e, simpleResponse(false, log));
		}
		final String jsonStr = new String(metaBytes, StandardCharsets.UTF_8);
		M metadata;
		try {
			metadata = mapper.readValue(jsonStr, metadataClass);
		} catch (final IOException e) {
			logger.logLn(2, "Fehler: Dekodieren der Json-Metadaten fehlgeschlagen.");
			throw new ApiOperationException(Status.BAD_REQUEST, e, simpleResponse(false, log));
		}

		// Erstelle eine temporäre Datei für die Binärdaten und schreibe die Daten aus dem Input-Stream in diese Datei hinein
		final String tempDir = SVWSKonfiguration.get().getTempPath();
		final String tempRandomPart = random.ints(48, 123)  // from 0 to z
				.filter(i -> ((i <= 57) || (i >= 65)) && ((i <= 90) || (i >= 97)))  // filter some unicode characters
				.limit(40)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		final String tempFilename = "%s/%s_%s.%s".formatted(tempDir, tempPrefix, tempRandomPart, tempSuffix);
		logger.logLn("Erstelle eine temporäre Datei unter dem Namen \"" + tempFilename + "\"");
		Path tempFile;
		try {
			Files.createDirectories(Paths.get(tempDir));
			tempFile = Files.createFile(Paths.get(tempFilename));
		} catch (final IOException e) {
			logger.logLn(2, "Fehler: Die temporäre Datei konnte nicht erstellt werden.");
			throw new ApiOperationException(Status.BAD_REQUEST, e, simpleResponse(false, log));
		}
		logger.logLn("Schreibe Daten in die temporäre Datei...");
		try {
			try (OutputStream os = Files.newOutputStream(tempFile)) {
				final byte[] buffer = new byte[16384]; // 16KB Puffergröße für optimierten I/O-Durchsatz unter Java 21
				long totalRead = 0;
				int bytesRead;

				while ((bytesRead = dataInputStream.read(buffer)) != -1) {
					totalRead += bytesRead;

					// Inhaltliche Absicherung (DoS-Schutz): Sofortiger Abbruch bei Limit-Überschreitung
					if (totalRead > maxBinarySize) {
						logger.logLn(2, "Fehler: Die hochgeladene Datei ist zu groß und überschreitet das Limit.");
						throw new ApiOperationException(Status.REQUEST_ENTITY_TOO_LARGE, simpleResponse(false, log));
					}

					os.write(buffer, 0, bytesRead);
				}
			}
			logger.logLn("  [OK]");
			return new FramedPayload<>(metadata, tempFile);
		} catch (final IOException e) {
			// Entferne die (teil-)geschriebene Datei im Fehlerfall sofort wieder
			try {
				Files.deleteIfExists(tempFile);
			} catch (final IOException e2) {
				e.addSuppressed(e2);
				logger.logLn(2, "Fehler: Das Schreiben der Daten in die temporäre Datei war nicht erfolgreich und die temporäre Datei konnte nicht entfernt werden.");
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, simpleResponse(false, log));
			}
			logger.logLn(2, "Fehler: Das Schreiben der Daten in die temporäre Datei war nicht erfolgreich. Die temporäre Datei wurde entfernt.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, simpleResponse(false, log));
		}
	}

}
