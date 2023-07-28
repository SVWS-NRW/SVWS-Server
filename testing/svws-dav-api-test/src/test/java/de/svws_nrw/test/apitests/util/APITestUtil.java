package de.svws_nrw.test.apitests.util;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
/**
 * Utility für API-Tests
 */
public final class APITestUtil {

	/**
	 * Liest eine File-Ressource als String
	 *
	 * @param resourceName der Dateiname inkl. Pfad innerhalb des
	 *                     src/test/resources Ordners
	 * @param o            eine aufrufende Objektinstanz aus diesem Projekt
	 * @return der eingelesene String des Dateiinhalts
	 */
	public static String readStringFromResourceFile(String resourceName, Object o) {
		try (InputStream is = o.getClass().getClassLoader().getResourceAsStream(resourceName)) {
			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			fail(e);
			return null;
		}
	}
}
