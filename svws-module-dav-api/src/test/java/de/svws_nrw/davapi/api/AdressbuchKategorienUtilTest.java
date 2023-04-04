package de.svws_nrw.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.svws_nrw.davapi.data.repos.bycategory.AdressbuchKategorienUtil;

/**
 * Testklasse für die Namensgebung von Kategorien
 */
public class AdressbuchKategorienUtilTest {

	private static final String JAHRGANG = "09";
	private static final String KLASSE = "9c";
	private static final String KURS = "CH-GK1";
	private static final String FACHSCHAFT = "Chemie";
	private static final AdressbuchKategorienUtil adressbuchKategorienUtil = new AdressbuchKategorienUtil("S2 2022");

	/**
	 * Testet den formatierten String für die Kategorie Lehrer der Fachschaft
	 */
	@Test
	void testFormatLehrerFachschaft() {
		final String format = adressbuchKategorienUtil.formatLehrerFachschaft(FACHSCHAFT);
		assertEquals("Fachschaft Chemie", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Lehrer eines Jahrgangs
	 */
	@Test
	void testFormatLehrerJahrgangsteam() {
		final String format = adressbuchKategorienUtil.formatLehrerJahrgangsteam(JAHRGANG);
		assertEquals("Team JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Klassenlehrer einer Klasse
	 *
	 */
	@Test
	void testFormatKlassenlehrer() {
		final String format = adressbuchKategorienUtil.formatKlassenlehrer(KLASSE);
		assertEquals("KL 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Klassenlehrer eines
	 * Jahrgangs
	 *
	 */
	@Test
	void testFormatKlassenlehrerJahrgang() {
		final String format = adressbuchKategorienUtil.formatKlassenlehrerJahrgang(JAHRGANG);
		assertEquals("KL JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie alle Klassenlehrer
	 */
	@Test
	void testFormatKlassenlehrerAlle() {
		final String format = adressbuchKategorienUtil.formatKlassenlehrerAlle();
		assertEquals("Klassenlehrer", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Lehrerteam einer Klasse
	 */
	@Test
	void testFormatLehrerKlasseTeam() {
		final String format = adressbuchKategorienUtil.formatLehrerKlasseTeam(KLASSE);
		assertEquals("Team 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Schueler einer Klasse
	 */
	@Test
	void testFormatSchuelerKlasse() {
		final String format = adressbuchKategorienUtil.formatSchuelerKlasse(KLASSE);
		assertEquals("Klasse 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Schueler eines Kurs
	 */
	@Test
	void testFormatSchuelerKurs() {
		final String format = adressbuchKategorienUtil.formatSchuelerKurs(KURS, JAHRGANG);
		assertEquals("Kurs CH-GK1 09 S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Schueler eines Jahrgangs
	 *
	 */
	@Test
	void testFormatSchuelerJahrgang() {
		final String format = adressbuchKategorienUtil.formatSchuelerJahrgang(JAHRGANG);
		assertEquals("JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Neuaufnahmen einer Klasse
	 *
	 */
	@Test
	void testFormatSchuelerNeuaufnahmeKlasse() {
		final String format = adressbuchKategorienUtil.formatSchuelerNeuaufnahmeKlasse(KLASSE);
		assertEquals("Neuaufnahmen 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Neuaufnahmen eines Jahrgangs
	 *
	 */
	@Test
	void testFormatSchuelerNeuaufnahmeJahrgang() {
		final String format = adressbuchKategorienUtil.formatSchuelerNeuaufnahmeJahrgang(JAHRGANG);
		assertEquals("Neuaufnahmen JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie alle Neuaufnahmen
	 */
	@Test
	void testFormatSchuelerNeuaufnahmenAlle() {
		final String format = adressbuchKategorienUtil.formatSchuelerNeuaufnahmenAlle();
		assertEquals("Neuaufnahmen", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher einer Klasse
	 */
	@Test
	void testFormatErzieherKlasse() {
		final String format = adressbuchKategorienUtil.formatErzieherKlasse(KLASSE);
		assertEquals("Eltern 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher eines Jahrgangs
	 *
	 */
	@Test
	void testFormatErzieherJahrgang() {
		final String format = adressbuchKategorienUtil.formatErzieherJahrgang(JAHRGANG);
		assertEquals("Eltern JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher eines Kurs
	 */
	@Test
	void testFormatErzieherKurs() {
		final String format = adressbuchKategorienUtil.formatErzieherKurs(KURS, JAHRGANG);
		assertEquals("Eltern CH-GK1 09 S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie alle Erzieher
	 *
	 */
	@Test
	void testFormatErzieherAlle() {
		final String format = adressbuchKategorienUtil.formatErzieherAlle();
		assertEquals("Eltern", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher der Neuaufnahmen
	 * einer Klasse
	 *
	 */
	@Test
	void testFormatErzieherNeuaufnahmenKlasse() {
		final String format = adressbuchKategorienUtil.formatErzieherNeuaufnahmenKlasse(KLASSE);
		assertEquals("Eltern Neuaufnahmen 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher der Neuaufnahmen
	 * eines Jahrgangs
	 */
	@Test
	void testFormatErzieherNeuaufnahmenJahrgang() {
		final String format = adressbuchKategorienUtil.formatErzieherNeuaufnahmenJahrgang(JAHRGANG);
		assertEquals("Eltern Neuaufnahmen JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie alle Erzieher von
	 * Neuaufnahmen
	 *
	 */
	@Test
	void testFormatErzieherNeuaufnahmenAlle() {
		final String format = adressbuchKategorienUtil.formatErzieherNeuaufnahmenAlle();
		assertEquals("Eltern Neuaufnahmen", format);
	}
}
