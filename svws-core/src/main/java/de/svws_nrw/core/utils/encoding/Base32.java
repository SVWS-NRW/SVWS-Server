package de.svws_nrw.core.utils.encoding;

import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse dient der Umwandlung in Bezug auf die Base32-Kodierung lauf RFC 4648.
 */
public final class Base32 {

	/** Das Base-32-Alphabet (A-Z und 2-7) */
	private static final @NotNull String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

	/**
	 * Eine vordefinierte Tabelle für die Dekodierung. Hier werden
	 * nur die ASCII-Codes erlaubt, da Base32 eine Teilmenge davon ist.
	 */
	private static final @NotNull int[] DECODE_TABLE = {
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1,
		-1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
		15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	};

	/**
	 * Privater Konstruktor mit Exception, da diese Klasse nicht instatiiert werden soll
	 */
	private Base32() {
		throw new IllegalStateException("Utility-Klasse");
	}

	/**
	 * Kodiert das übergeben Byte-Array mit einer Base32-Kodierung.
	 *
	 * @param data   das zu kodierende Byte-Array
	 *
	 * @return der String mit der Base32-Kodierung
	 */
	public static @NotNull String encode(final byte[] data) {
		// Leere Daten werden als leerer String kodiert.
		if ((data == null) || (data.length == 0)) {
			return "";
		}

		// Nutze einen String-Builder für den Aufbau der Base-32-Kodierung
		final @NotNull StringBuilder sb = new StringBuilder();
		int buffer = 0;
		int bitsLeft = 0;

		// Durchwandere das Byte-Array und nutze Bit-Shifting für effiziente Kodierung
		for (final byte b : data) {
			buffer = (buffer << 8) | (b & 0xFF);
			bitsLeft += 8;
			while (bitsLeft >= 5) {
				sb.append(ALPHABET.charAt((buffer >> (bitsLeft - 5)) & 0x1F));
				bitsLeft -= 5;
			}
		}

		// Kodiere auch noch die letzte Bits, wenn welche übrig geblieben sind
		if (bitsLeft > 0) {
			sb.append(ALPHABET.charAt((buffer << (5 - bitsLeft)) & 0x1F));
		}

		// Ergänze ggf. noch das Padding
		while ((sb.length() % 8) != 0) {
			sb.append('=');
		}

		return sb.toString();
	}


	/**
	 * Dekodiert den übergebenen Base32-String in ein Byte-Array.
	 *
	 * @param base32   der zu dekodierende Base32-String
	 *
	 * @return das Byte-Array mit den dekodierten Daten
	 *
	 * @throws IllegalArgumentException wenn der Base32-String ungültige Zeichen enthält
	 */
	public static @NotNull byte[] decode(final String base32) {
		// Wenn der Base32-String leer ist, dann ist auch das resultierende Byte-Array leer
		if ((base32 == null) || (base32.isEmpty())) {
			return new byte[0];
		}

		// Wandle des Base32-String in Uppercase um, damit die Umwandlung nicht am Case scheitert
		final @NotNull byte[] bytes = base32.replace("=", "").toUpperCase().getBytes();

		// Erstelle das Byte-Array, in welches der Base32-String dekodiert wird
		final int len = bytes.length;
		final int anzahlBytes = (len * 5) / 8;
		final @NotNull byte[] result = new byte[anzahlBytes];

		int buffer = 0;
		int bitsLeft = 0;
		int index = 0;

		// Dekodiere den Input-String und nutze im Ziel Bit-Shifting für das effiziente Schreiben der dekodierten Daten
		for (int i = 0; i < len; i++) {
			final int c = bytes[i] & 0xFF;
			if ((c >= DECODE_TABLE.length) || (DECODE_TABLE[c] == -1)) {
				throw new IllegalArgumentException("Ungültiges Zeichen im Base32-String: " + c);
			}

			buffer = (buffer << 5) | DECODE_TABLE[c];
			bitsLeft += 5;

			if (bitsLeft >= 8) {
				if (index < result.length) {
					result[index++] = (byte) ((buffer >> (bitsLeft - 8)) & 0xFF);
				}
				bitsLeft -= 8;
			}
		}

		return result;
	}

}
