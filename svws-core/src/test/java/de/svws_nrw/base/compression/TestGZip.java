package de.svws_nrw.base.compression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Diese Klasse prüft die Methoden der {@link GZip}-Klasse.
 */
public class TestGZip {

	/**
	 * Tests für das Komprimieren und Dekomprimieren von Strings mit dem GZip-Verfahren
	 */
	@Test
	void testStringCompression() {
		try {
			final String original = "EineNachricht";
			final String encoded = GZip.encodeBase64(original.getBytes());
			final String decoded = new String(GZip.decodeBase64(encoded));
			assertEquals(original, decoded);
		} catch (final CompressionException e) {
			fail(e);
		}
	}

}
