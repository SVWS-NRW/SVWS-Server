package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schueler.VersetzungsvermerkKatalogEintrag;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Tests der Auflösung des Versetzungsvermerks in {@link ReportingSchuelerLernabschnitt}.
 * <p>Die ID des Versetzungsvermerks ist ein {@code Long} und fehlt außerhalb des Schuljahresendes regelmäßig. {@code getEintragByID} liefert dann
 * {@code null} — ebenso bei einer ID, zu der der Core-Type keinen Eintrag mehr kennt. Ein direkter Feldzugriff auf dieses Ergebnis brach die gesamte
 * Reportausgabe mit einer {@link NullPointerException} ab.</p>
 * <p>Der Test benötigt keine Datenbank, wohl aber die initialisierten Core-Type-Daten aus den Ressourcen.</p>
 */
class TestReportingSchuelerLernabschnitt {

	/** Eine ID, zu der der Core-Type keinen Eintrag kennt. */
	private static final long ID_UNBEKANNT = 999_999L;


	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@Test
	void testOhneVersetzungsvermerkErgibtEinenLeerenString() {
		// Der Normalfall außerhalb des Schuljahresendes: Der Lernabschnitt trägt keinen Versetzungsvermerk.
		assertEquals("", ReportingSchuelerLernabschnitt.versetzungsvermerkKuerzel(null));
	}

	@Test
	void testEineUnbekannteIdErgibtEinenLeerenString() {
		assertEquals("", ReportingSchuelerLernabschnitt.versetzungsvermerkKuerzel(ID_UNBEKANNT));
	}

	@Test
	void testEineBekannteIdErgibtDasKuerzelDesKatalogeintrags() {
		// Gegenprobe: Ohne sie wäre nicht belegt, dass überhaupt je ein Kürzel geliefert wird.
		final VersetzungsvermerkKatalogEintrag eintrag = Versetzungsvermerk.VERSETZT.historie().getFirst();
		assertNotNull(eintrag, "Der Core-Type muss zum Versetzungsvermerk VERSETZT einen Katalogeintrag liefern.");
		assertFalse(eintrag.kuerzel.isBlank(), "Der Katalogeintrag muss ein Kürzel besitzen.");

		assertEquals(eintrag.kuerzel, ReportingSchuelerLernabschnitt.versetzungsvermerkKuerzel(eintrag.id));
	}

}
