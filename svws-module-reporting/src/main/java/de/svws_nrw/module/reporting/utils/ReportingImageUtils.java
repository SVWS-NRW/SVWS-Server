package de.svws_nrw.module.reporting.utils;

import java.util.Base64;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;

/**
 * Eine Hilfsklasse für Bildoperationen, insbesondere zur Konvertierung von Base64-kodierten Bildern in HTML-kompatible Quellen.
 * Diese Klasse enthält ausschließlich statische Methoden und kann nicht instanziiert werden.
 */
public final class ReportingImageUtils {

	private ReportingImageUtils() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zum Fehler-Logging. Initialisierung nicht möglich.");
	}

	/**
	 * Die Methode erstellt aus einem base64 encoded Image einen String, welcher als Source in einem HTML-Image-Tag verwendet werden kann.
	 * Dieser String enthält auch die Angabe des Mime-Types des Bildes. Wird dieser bei Aufruf der Methode nicht übergeben,
	 * so wird versucht, diesen anhand von "magic numbers" zu ermitteln. Ist dies nicht möglich, so wird der Typ "unknown" verwendet.
	 *
	 * @param base64Image Das Bild im base64 Format.
	 * @param mimeType Der MimeType des Bildes. Ist diese leer oder null, so wird versucht, die Type auf Basis der Bilddaten zu ermitteln.
	 * @param logger Ein Logger zum Festhalten von Fehlermeldungen. Kann auch null sein.
	 *
	 * @return Das Bild als HTML-image source, inklusive des mimeTypes.
	 */
	public static String base64ImageToHtmlImageSource(final String base64Image, final String mimeType,  final Logger logger) {
		if ((base64Image == null) || base64Image.isBlank())
			return "";

		String type = mimeType;
		if ((type == null) || type.isBlank()) {
			try {
				// Versuche die ersten Bytes zu dekodieren, um den MIME-Type zu erkennen (max. 24 Zeichen Base64 reichen für den Header)
				final byte[] header = Base64.getDecoder().decode(base64Image.substring(0, Math.min(base64Image.length(), 24)));
				type = ermittleMimeTypeAusBase64Daten(header);
			} catch (final IllegalArgumentException e) {
				if (logger != null)
					logger.log(LogLevel.ERROR, 0, "### Fehler beim Dekodieren des Base64-Strings für die MimeType-Erkennung: " + e.getMessage());
				type = "unknown";
			}
		}

		return "data:" + type + ";base64," + base64Image;
	}

	/**
	 * Versucht den MIME-Type anhand der Magic Numbers in den Byte-Daten zu erkennen.
	 *
	 * @param data Die ersten Bytes der Bilddaten.
	 * @return Der erkannte MIME-Type oder "unknown".
	 */
	private static String ermittleMimeTypeAusBase64Daten(final byte[] data) {
		if ((data == null) || (data.length < 4))
			return "unknown";

		// Konvertiere in int-Array für vorzeichenlosen Vergleich
		final int[] bytes = new int[data.length];
		for (int i = 0; i < data.length; i++)
			bytes[i] = data[i] & 0xFF;

		// PNG: 89 50 4E 47 0D 0A 1A 0A
		if (startsWith(bytes, 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)) return "image/png";

		// JPEG: FF D8 FF
		if (startsWith(bytes, 0xFF, 0xD8, 0xFF)) return "image/jpeg";

		// GIF: 47 49 46 38
		if (startsWith(bytes, 0x47, 0x49, 0x46, 0x38)) return "image/gif";

		// BMP: 42 4D
		if (startsWith(bytes, 0x42, 0x4D)) return "image/bmp";

		// SVG: <svg oder <?xml
		if (startsWith(bytes, 0x3C, 0x73, 0x76, 0x67)) return "image/svg+xml";
		if (startsWith(bytes, 0x3C, 0x3F, 0x78, 0x6D, 0x6C)) return "image/svg+xml";

		return "unknown";
	}

	/**
	 * Prüft, ob das Daten-Array mit dem angegebenen Muster beginnt.
	 *
	 * @param data    Das zu prüfende Daten-Array.
	 * @param pattern Das Muster (Magic Numbers).
	 * @return true, wenn das Array mit dem Muster beginnt.
	 */
	private static boolean startsWith(final int[] data, final int... pattern) {
		if (data.length < pattern.length) return false;
		for (int i = 0; i < pattern.length; i++) {
			if (data[i] != pattern[i]) return false;
		}
		return true;
	}

}
