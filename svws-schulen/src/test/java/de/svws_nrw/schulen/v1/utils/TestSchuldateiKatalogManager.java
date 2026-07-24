package de.svws_nrw.schulen.v1.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.schulen.ResourceUtils;
import de.svws_nrw.schulen.v1.SchuldateiReader;
import de.svws_nrw.schulen.v1.data.SchuldateiEintrag;
import de.svws_nrw.schulen.v1.data.SchuldateiKataloge;
import de.svws_nrw.schulen.v1.data.SchuldateiKatalogeintrag;
import de.svws_nrw.schulen.v1.data.Schuldatei;

/**
 * Tests fuer den {@link SchuldateiKatalogManager}.
 */
@DisplayName("Teste die has-Methoden des Schuldatei-Katalog-Managers.")
class TestSchuldateiKatalogManager {

	/** Der Manager fuer die Schuldatei */
	static final SchuldateiManager manager = createManagerMitTestKatalogEintraegen();


	/**
	 * Erstellt einen Test-Manager mit zwei zusaetzlichen Schulform-Eintraegen fuer Wert 12.
	 * Die produktive JSON-Datei bleibt dabei unveraendert.
	 *
	 * @return der Test-Manager
	 */
	private static SchuldateiManager createManagerMitTestKatalogEintraegen() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonSchuldatei = ResourceUtils.text(SchuldateiReader.filenameSchuldatei);
			final Schuldatei schuldatei = mapper.readValue(jsonSchuldatei, Schuldatei.class);
			final String jsonSchuldateiKataloge = ResourceUtils.text(SchuldateiReader.filenameSchuldateiKataloge);
			final SchuldateiKataloge kataloge = mapper.readValue(jsonSchuldateiKataloge, SchuldateiKataloge.class);

			// gültige SCHULJAHR-Zeiträume für Eintrag 12:
			// 1969-1993 , 1994-1995 , 1998-1999
			kataloge.kataloge.add(createSchulformEintrag12("Abend-Realschule", "01.08.1994", "31.07.1996", "22.07.2023 09:00:00"));
			kataloge.kataloge.add(createSchulformEintrag12("Abend-Sekundarschule", "01.08.1998", "31.07.2000", "23.07.2023 09:00:00"));

			return new SchuldateiManager(schuldatei, kataloge);
		} catch (final IOException e) {
			throw new IllegalStateException("Konnte Testdaten fuer TestSchuldateiKatalogManager nicht laden.", e);
		}
	}


	/**
	 * Erstellt einen Test-Katalogeintrag für den Schulform-Katalog mit dem Wert 12.
	 *
	 * @param bezeichnung  die Bezeichnung des Eintrags
	 * @param gueltigab    das Datum, ab dem der Eintrag gültig ist
	 * @param gueltigbis   das Datum, bis zu dem der Eintrag gültig ist
	 * @param geaendertam  das Datum, an dem der Eintrag zuletzt geändert wurde
	 *
	 * @return der erstellte Katalogeintrag
	 */
	private static SchuldateiKatalogeintrag createSchulformEintrag12(final String bezeichnung, final String gueltigab,
			final String gueltigbis, final String geaendertam) {
		final SchuldateiKatalogeintrag eintrag = new SchuldateiKatalogeintrag();
		eintrag.katalog = "Schulform";
		eintrag.schluessel = "12";
		eintrag.wert = "12";
		eintrag.bezeichnung = bezeichnung;
		eintrag.datentypwert = "";
		eintrag.gueltigab = gueltigab;
		eintrag.gueltigbis = gueltigbis;
		eintrag.geaendertam = geaendertam;
		return eintrag;
	}


	@Test
	@DisplayName("Pruefe hasEintrag(...) fuer den Schulformen-Katalog")
	void testHasEintrag() {
		final SchuldateiKatalogManager schulformen = manager.katalogSchulformen;
		assertTrue(schulformen.hasEintrag("0A"));
		assertTrue(schulformen.hasEintrag(12));
		assertFalse(schulformen.hasEintrag("UNGUELTIG"));
		assertFalse(schulformen.hasEintrag(-9999));
		assertFalse(schulformen.hasEintrag((String) null));
	}


	@Test
	@DisplayName("Pruefe hasEintragBySchuljahr(...) fuer historisch begrenzte Katalogwerte")
	void testHasEintragBySchuljahr() {
		final SchuldateiKatalogManager schulformen = manager.katalogSchulformen;
		assertTrue(schulformen.hasEintragBySchuljahr(1993, 12));
		assertTrue(schulformen.hasEintragBySchuljahr(1994, 12));
		assertTrue(schulformen.hasEintragBySchuljahr(1995, 12));
		assertFalse(schulformen.hasEintragBySchuljahr(1996, 12));
		assertFalse(schulformen.hasEintragBySchuljahr(1997, 12));
		assertTrue(schulformen.hasEintragBySchuljahr(1998, 12));
		assertTrue(schulformen.hasEintragBySchuljahr(1999, 12));
		assertFalse(schulformen.hasEintragBySchuljahr(2000, 12));
		assertTrue(schulformen.hasEintragBySchuljahr(2021, "83"));
		assertTrue(schulformen.hasEintragBySchuljahr(2022, "83"));
	}


	@Test
	@DisplayName("Pruefe hasEintragInZeitraum(...) mit SchuldateiEintrag")
	void testHasEintragInZeitraumEintrag() {
		final SchuldateiKatalogManager schulformen = manager.katalogSchulformen;
		final SchuldateiEintrag zeitraumBis1996 = new SchuldateiEintrag();
		zeitraumBis1996.gueltigab = "01.08.1994";
		zeitraumBis1996.gueltigbis = "31.07.1996";
		assertTrue(schulformen.hasEintragInZeitraum(zeitraumBis1996, "12"));
		assertTrue(schulformen.hasEintragInZeitraum(zeitraumBis1996, 12));

		final SchuldateiEintrag zeitraumOhneEintrag = new SchuldateiEintrag();
		zeitraumOhneEintrag.gueltigab = "01.08.1996";
		zeitraumOhneEintrag.gueltigbis = "31.07.1998";
		assertFalse(schulformen.hasEintragInZeitraum(zeitraumOhneEintrag, "12"));

		final SchuldateiEintrag zeitraumAb1998 = new SchuldateiEintrag();
		zeitraumAb1998.gueltigab = "01.08.1998";
		zeitraumAb1998.gueltigbis = "31.07.2000";
		assertTrue(schulformen.hasEintragInZeitraum(zeitraumAb1998, "12"));

		final SchuldateiEintrag zeitraumMitNullBis = new SchuldateiEintrag();
		zeitraumMitNullBis.gueltigab = "01.08.1992";
		zeitraumMitNullBis.gueltigbis = null;
		assertFalse(schulformen.hasEintragInZeitraum(zeitraumMitNullBis, "12"));

		assertTrue(schulformen.hasEintragInZeitraum(1968, 1969, "12", false));
		assertFalse(schulformen.hasEintragInZeitraum(1996, 1997, "12", false));
		assertFalse(schulformen.hasEintragInZeitraum(2000, 2001, "12", false));
		assertTrue(schulformen.hasEintragInZeitraum(1980, 1993, "12", false));
		assertTrue(schulformen.hasEintragInZeitraum(1980, 1995, "12", false));
		assertFalse(schulformen.hasEintragInZeitraum(1980, 1996, "12", false));
		assertFalse(schulformen.hasEintragInZeitraum(1980, 1998, "12", false));
		assertFalse(schulformen.hasEintragInZeitraum(1980, 2000, "12", false));
		assertTrue(schulformen.hasEintragInZeitraum(1994, 1994, "12", false));
		assertFalse(schulformen.hasEintragInZeitraum(1994, 1996, "12", false));
		assertFalse(schulformen.hasEintragInZeitraum(1996, 1999, "12", false));
		assertFalse(schulformen.hasEintragInZeitraum(1996, 2000, "12", false));
		assertTrue(schulformen.hasEintragInZeitraum(1998, 1998, "12", false));
		assertTrue(schulformen.hasEintragInZeitraum(1998, 1999, "12", false));

		assertFalse(schulformen.hasEintragInZeitraum(1996, 2000, "0B", false));
		assertFalse(schulformen.hasEintragInZeitraum(1996, 2013, "0B", false));
		assertFalse(schulformen.hasEintragInZeitraum(1996, 2014, "0B", false));
		assertTrue(schulformen.hasEintragInZeitraum(2013, 2013, "0B", false));
		assertTrue(schulformen.hasEintragInZeitraum(2013, 3000, "0B", false));
	}


	@Test
	@DisplayName("Pruefe hasEintragInZeitraum(...) mit Teilgueltigkeit")
	void testHasEintragInZeitraumMitTeilgueltigkeit() {
		final SchuldateiKatalogManager schulformen = manager.katalogSchulformen;
		assertFalse(schulformen.hasEintragInZeitraum(2019, 2021, "KG", false));
		assertTrue(schulformen.hasEintragInZeitraum(2019, 2021, "KG", true));
		assertFalse(schulformen.hasEintragInZeitraum(2019, 2021, "UNGUELTIG", true));

		assertFalse(schulformen.hasEintragInZeitraum(1996, 1998, "12", false));
		assertTrue(schulformen.hasEintragInZeitraum(1996, 1998, "12", true));
		assertFalse(schulformen.hasEintragInZeitraum(1996, 1997, "12", true));

		assertTrue(schulformen.hasEintragInZeitraum(1968, 1969, "12", true));
		assertFalse(schulformen.hasEintragInZeitraum(1996, 1997, "12", true));
		assertFalse(schulformen.hasEintragInZeitraum(2000, 2001, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1980, 1993, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1980, 1995, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1980, 1996, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1980, 1998, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1980, 2000, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1994, 1994, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1994, 1996, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1996, 1999, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1996, 2000, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1998, 1998, "12", true));
		assertTrue(schulformen.hasEintragInZeitraum(1998, 1999, "12", true));

		assertFalse(schulformen.hasEintragInZeitraum(1996, 2000, "0B", true));
		assertTrue(schulformen.hasEintragInZeitraum(1996, 2013, "0B", true));
		assertTrue(schulformen.hasEintragInZeitraum(1996, 2014, "0B", true));
		assertTrue(schulformen.hasEintragInZeitraum(2013, 2013, "0B", true));
		assertTrue(schulformen.hasEintragInZeitraum(2013, 3000, "0B", true));
	}

}
