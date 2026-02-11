package de.svws_nrw.base.email;

import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beinhaltet die Informationen zu einem Attachment einer EMail-Empfängers.
 */
public class EmailJobAttachment {

	/** Der Dateiname des Anhangs */
	public final @NotNull String filename;

	/** Die Daten des Attachments als Byte-Array. */
	public final @NotNull byte[] data;

	/** Der Mime-Type des Dateianhangs (z.B. "application/pdf" oder "image/png") */
	public final @NotNull String mimetype;


	/**
	 * Erstellt einen neuen Anhang für den Email-Job
	 *
	 * @param filename   der Dateiname
	 * @param data       die Daten
	 * @param mimetype   der Mime-Type
	 */
	public EmailJobAttachment(final @NotNull String filename, final @NotNull byte[] data, final @NotNull String mimetype) {
		if ((filename == null) || filename.isBlank() || (data == null) || (data.length == 0) || (mimetype == null) || mimetype.isBlank())
			throw new IllegalArgumentException("Notwendige Parameter für die Erzeugung eines E-Mail-Attachments sind null oder leer.");
		this.filename = filename;
		this.data = data;
		this.mimetype = mimetype;
	}

}
