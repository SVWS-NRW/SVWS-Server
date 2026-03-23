package de.svws_nrw.core.utils.encoding;

import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse dient der Umwandlung in Bezug auf die Base45-Kodierung lauf RFC 9285.
 */
public final class Base45 {

	/** Das Base-45-Alphabet */
	private static final @NotNull String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:";

	/**
	 * Eine vordefinierte Tabelle für die Dekodierung. Hier werden
	 * nur die ASCII-Codes erlaubt, da Base45 eine Teilmenge davon ist.
	 */
	private static final @NotNull int[] DECODE_TABLE = {
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43,
		 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 44, -1, -1, -1, -1, -1,
		-1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
		25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
	};

	private Base45() {
		throw new IllegalStateException("Utility-Klasse");
	}

	/**
	 * Dekodiert das übergebene Byte-Array mit einer Base45-Kodierung.
	 *
	 * @param data   das zu kodierende Byte-Array
	 *
	 * @return der String mit der Base45-Kodierung
	 */
	public static @NotNull String encode(final byte[] data) {
		// Leere Daten werden als leerer String kodiert.
		if ((data == null) || (data.length == 0)) {
			return "";
		}

		final @NotNull StringBuilder sb = new StringBuilder();
		final int len = data.length;

		// Blockweise Verarbeitung von 2 Bytes, welche dann 3 Zeichen ergeben
		for (int i = 0; i < len - 1; i += 2) {
			final int val = ((data[i] & 0xFF) << 8) | (data[i + 1] & 0xFF);
			sb.append(ALPHABET.charAt(val % 45));
			sb.append(ALPHABET.charAt((val / 45) % 45));
			sb.append(ALPHABET.charAt((val / 2025) % 45));
		}

		// Wenn eine ungerade Anzahl von Bytes vorhanden ist, dann hänge zwei Zeichen an
		if ((len % 2) != 0) {
			final int val = data[len - 1] & 0xFF;
			sb.append(ALPHABET.charAt(val % 45));
			sb.append(ALPHABET.charAt((val / 45) % 45));
		}

		return sb.toString();
	}


	/**
	 * Dekodiert den übergebenen Base45-String in ein Byte-Array.
	 *
	 * @param base45   der zu dekodierende Base45-String
	 *
	 * @return das Byte-Array mit den dekodierten Daten
	 *
	 * @throws IllegalArgumentException wenn der Base45-String ungültige Zeichen enthält
	 */
	public static @NotNull byte[] decode(final String base45) {
		// Wenn der Base45-String leer ist, dann ist auch das resultierende Byte-Array leer
		if ((base45 == null) || (base45.isEmpty())) {
			return new byte[0];
		}

		// Erstelle das Byte-Array, in welches der Base45-String dekodiert wird
		final @NotNull byte[] bytes = base45.getBytes();
		final int len = bytes.length;
		final int resultLen = ((len / 3) * 2) + ((len % 3 == 2) ? 1 : 0);
		final @NotNull byte[] result = new byte[resultLen];
		int index = 0;

		// Dekodiere den Input-String, indem 3 Zeichen zu 2 Bytes umgewandelt werden
		for (int i = 0; i < len - 2; i += 3) {
			final int v1 = getVal(bytes[i]);
			final int v2 = getVal(bytes[i + 1]);
			final int v3 = getVal(bytes[i + 2]);
			final int val = v1 + (v2 * 45) + (v3 * 2025);
			if (val > 0xFFFF) {
				throw new IllegalArgumentException("Base45-Wert außerhalb des gültigen Bereichs.");
			}
			result[index++] = (byte) ((val >> 8) & 0xFF);
			result[index++] = (byte) (val & 0xFF);
		}

		// Prüfe, ob noch restliche Zeichen vorhanden sind (zwei Zeichen werden zu einem Byte umgewandelt)
		if (len % 3 == 2) {
			final int v1 = getVal(bytes[len - 2]);
			final int v2 = getVal(bytes[len - 1]);
			final int val = v1 + (v2 * 45);
			result[index] = (byte) (val & 0xFF);
		} else if (len % 3 == 1) {
			throw new IllegalArgumentException("Ungültige Base45-String-Länge.");
		}

		return result;
	}


	/**
	 * Wandelt das übergebene Zeichen mit Hilfe der DECODE_TABLE in den Wert 0...44 um.
	 *
	 * @param b   der Byte-Wert des ASCII-Zeichens
	 *
	 * @return der Wert (0...44)
	 */
	private static int getVal(final byte b) {
		final int c = b & 0xFF;
		if (c >= DECODE_TABLE.length || DECODE_TABLE[c] == -1) {
			throw new IllegalArgumentException("Ungültiges Zeichen im Base45-String: " + c);
		}
		return DECODE_TABLE[c];
	}

}
