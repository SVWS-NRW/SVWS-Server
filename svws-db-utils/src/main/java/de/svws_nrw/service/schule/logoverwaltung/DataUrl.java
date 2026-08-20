package de.svws_nrw.service.schule.logoverwaltung;


import java.util.Collection;

/**
 * Beschreibt ein Daten-Objekt, das eine DATA-URL mit MIME-Type und Base64-Payload enthält.
 */
public interface DataUrl {

	/**
	 * Enthält die vollständige die DATA-URL.
	 *
	 * @return die DATA-URL
	 */
	String value();

	/**
	 * Gibt den MIME-Type des Base64-Payloads zurück.
	 *
	 * @return der MIME-Type
	 */
	String mimeType();

	/**
	 * Gibt den Base64-Payload ohne DATA-URL-Header zurück.
	 *
	 * @return der Base64-Payload
	 */
	String payload();

	/**
	 * Gibt die Größe des Base64-Payloads in KB zurück.
	 *
	 * @return die Größe in KB
	 */
	double sizeInKB();

	/**
	 * Gibt die zugehörige Dateinamenendung zurück.
	 *
	 * @return die Dateinamenendung
	 */
	String fileExtension();

	/**
	 * Prüft, ob die DATA-URL eines der angegebenen MIME-Types enthält.
	 *
	 * @param mimeTypes Collection von erlaubten MIME-Types (z.B. image/png)
	 *
	 * @return {@code true} wenn ein gültiger MIME-Type gefunden wurde, ansonsten {@code false}
	 */
	boolean hasAnyMimeTypeOf(Collection<String> mimeTypes);
}
