package de.svws_nrw.module.reporting.html.dialects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.module.reporting.types.person.ReportingPerson;

/**
 * Prüft das Geschlechts-Icon im Icon-Helfer. Jedes Geschlecht hat ein eigenes Icon, ohne Angabe erscheint {@code "nogender"}.
 */
class TestIconExpressionHelperGeschlecht {

	/** Der geprüfte Icon-Helfer. */
	private final IconExpressionHelper icon = new IconExpressionHelper();


	@ParameterizedTest
	@CsvSource({ "M, male", "W, female", "D, diverse", "X, nogender", ", nogender" })
	void testDasIconPasstZumGeschlecht(final Geschlecht geschlecht, final String iconName) {
		final ReportingPerson person = mock(ReportingPerson.class);
		when(person.geschlecht()).thenReturn(geschlecht);

		final String erwartet = icon.get(iconName, 10);
		assertFalse(erwartet.isEmpty(), "Das Icon '" + iconName + "' fehlt im Katalog.");
		assertEquals(erwartet, icon.getGeschlecht(10, person));
	}

	@Test
	void testOhnePersonBleibtDasIconLeer() {
		assertEquals("", icon.getGeschlecht(10, null));
	}

}
