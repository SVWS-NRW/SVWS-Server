package de.svws_nrw.api.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Die Klasse dient als generischer Container um in einem Stream binäre Daten zu empfangen und in einer tmeporären Datei
 * zwischenzuspeichern.
 * Die Klasse implementiert das Interface AutoClosable und die temporäre Datei wird automatisch beim Schließen gelöscht.
 *
 * @param <M> der Typ der zu extrahierenden Json-Metadaten für die Datei
 */
public final class FramedPayload<M> implements AutoCloseable {

	/** Die Json-Metadaten*/
	private final M metadata;

	/** Der Pfad zu der temporären Datei */
	private final Path tempFilePath;

	/**
	 * Erstellt eine neue Payload-Klasse mit den übergebenen Json-Meta-Daten und dem übergebene Pfad für die temporäre Datei.
	 *
	 * @param metadata        die Meta-Daten
	 * @param tempFilePath    der Pfad für die temporäre Datei
	 */
	public FramedPayload(final M metadata, final Path tempFilePath) {
		this.metadata = metadata;
		this.tempFilePath = tempFilePath;
	}

	/**
	 * Gibt die Json-Metadaten zurück.
	 *
	 * @return die Json-Metadaten
	 */
	public M metadata() {
		return this.metadata;
	}

	/**
	 * Gibt das Datei-Objekt für die temporäre Datei zurück.
	 *
	 * @return das Datei-Objekt
	 */
	public File file() {
		return this.tempFilePath.toFile();
	}

	/**
	 * Der Pfad für die temporäre Datei
	 *
	 * @return der Pfad
	 */
	public Path path() {
		return this.tempFilePath;
	}

	@Override
	public void close() throws IOException {
		if (this.tempFilePath != null) {
			Files.deleteIfExists(this.tempFilePath);
		}
	}

}
