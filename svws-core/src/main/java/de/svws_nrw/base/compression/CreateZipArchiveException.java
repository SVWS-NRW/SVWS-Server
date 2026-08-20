package de.svws_nrw.base.compression;

public class CreateZipArchiveException extends RuntimeException {

	private static final String EXCEPTION_MESSSAGE = "Fehler beim Erstellen des ZIP-Archivs.";

	/**
	 * Erstellt eine neue Instanz der Exception.
	 *
	 * @param cause Exception
	 */
	CreateZipArchiveException(final Throwable cause) {
		super(String.format("%s Grund: %s", EXCEPTION_MESSSAGE, cause.getMessage()));
	}
}
