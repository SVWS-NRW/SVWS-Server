package de.svws_nrw.base.email;

import java.util.Objects;

import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beinhaltet die Informationen zu einem Attachment eines E-Mail-Empfängers.
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
		// @NotNull sichert nicht gegen die Übergabe von null. SonarQube denkt aber so und meldet bei Prüfung mittels "== null" immer
		// "java:S2589, Remove this expression which always evaluates to true/false.". Daher hier die Prüfung mittels Objects.requireNonNull.
		try {
			if (Objects.requireNonNull(filename).isBlank() || (Objects.requireNonNull(data).length == 0) || Objects.requireNonNull(mimetype).isBlank()) {
				throw new IllegalArgumentException("Notwendige Parameter für die Erzeugung eines E-Mail-Attachments sind leer.");
			}
		} catch (final NullPointerException e) {
			throw new IllegalArgumentException("Notwendige Parameter für die Erzeugung eines E-Mail-Attachments sind null.");
		}
		this.filename = filename;
		this.data = data;
		this.mimetype = mimetype;
	}

}
