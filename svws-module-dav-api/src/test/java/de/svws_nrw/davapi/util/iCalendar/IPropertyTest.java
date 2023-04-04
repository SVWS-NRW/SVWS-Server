package de.svws_nrw.davapi.util.iCalendar;

import org.junit.jupiter.api.Test;

import de.svws_nrw.davapi.util.icalendar.IProperty;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testklasse für {@link IProperty}
 *
 */
class IPropertyTest {

	// VCalendar-Zeile mit einem Wert, der keinen Doppelpunkt als
	// Prop/Value-Delimiter enthält
	private static final String PROP_WITHOUT_MISSING_DOUBLEQUOTE = "DESCRIPTION;ALTREP=\"data:text/html,%3Cbody%3EDas%20ist%20ein%20Test-Termin%";

	// VCalendar-Zeile mit einem Wert, der einen Doppelpunkt als
	// Prop/Value-Delimiter enthält
	private static final String PROP_WITH_COLON_DELIMITER = "BEGIN:VEVENT";

	/**
	 * Testet die Methode {@link IProperty#fromString(String)}} mit einer
	 * VCalendar-Zeile, die keinen Doppelpunkt als Prop/Value-Delimiter enthält
	 */
	@Test
	void parsePropertyWithoutColonDelimiterDoesNotThrowException() {
		// der gegebene String ist nicht nach RFC, daher wird eine Exception erwartet
//		Property parameter values that contain the COLON (US-ASCII decimal
//				   58), SEMICOLON (US-ASCII decimal 59) or COMMA (US-ASCII decimal 44)
//				   character separators MUST be specified as quoted-string text values.
//				   Property parameter values MUST NOT contain the DOUBLE-QUOTE (US-ASCII
//				   decimal 22) character. The DOUBLE-QUOTE (US-ASCII decimal 22)
//				   character is used as a delimiter for parameter values that contain
//				   restricted characters or URI text.
		final IProperty property = IProperty.fromString(PROP_WITH_COLON_DELIMITER);
		assertNotNull(property);
	}

	/**
	 * Testet die Methode {@link IProperty#fromString(String)}} mit einer
	 * VCalendar-Zeile, die einen Doppelpunkt als Prop/Value-Delimiter enthält
	 */
	@Test
	void parsePropertyWithColonDelimiterDoesNotThrowException() {
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> IProperty.fromString(PROP_WITHOUT_MISSING_DOUBLEQUOTE));
	}

}
