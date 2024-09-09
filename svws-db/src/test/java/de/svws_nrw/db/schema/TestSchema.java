package de.svws_nrw.db.schema;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse dient dem Testen von Funktionen der Schema-Definition
 * der SVWS-Datenbank.
 */
class TestSchema {

	/**
	 * Initialisierung der Core-Types
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Testet, ob die Definition des Schemas der SVWS-Datenbank korrekt ist,
	 * indem diese geladen wird.
	 */
	@Test
	@DisplayName("Prüfe die Definition des SVWS-Datenbank-Schemas")
	void testDefinition() {
		final List<SchemaTabelle> tabellen = Schema.tabellen();
		assertNotEquals(0, tabellen.size(), "Es wurde keine Tabellen-Definition gefunden.");
		for (final SchemaTabelle tab : tabellen) {
			assertNotNull(tab, "In der Map mit den Tabellen darf kein Eintrag null sein.");
			assertNotNull(tab.name(), "Der Name einer Tabelle darf nicht null sein.");
			assertNotEquals("", tab.name(), "Der Name einer Tabelle darff nicht leer sein.");
			// TODO
		}
	}

}
