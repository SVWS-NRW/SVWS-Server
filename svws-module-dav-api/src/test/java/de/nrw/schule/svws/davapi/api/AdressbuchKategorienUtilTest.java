package de.nrw.schule.svws.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.nrw.schule.svws.davapi.data.repos.bycategory.AdressbuchKategorienUtil;

import org.junit.jupiter.api.Test;

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
		String format = adressbuchKategorienUtil.formatLehrerFachschaft(FACHSCHAFT);
		assertEquals("Fachschaft Chemie", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Lehrer eines Jahrgangs
	 */
	@Test
	void testFormatLehrerJahrgangsteam() {
		String format = adressbuchKategorienUtil.formatLehrerJahrgangsteam(JAHRGANG);
		assertEquals("Team JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Klassenlehrer einer Klasse
	 *
	 */
	@Test
	void testFormatKlassenlehrer() {
		String format = adressbuchKategorienUtil.formatKlassenlehrer(KLASSE);
		assertEquals("KL 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Klassenlehrer eines
	 * Jahrgangs
	 *
	 */
	@Test
	void testFormatKlassenlehrerJahrgang() {
		String format = adressbuchKategorienUtil.formatKlassenlehrerJahrgang(JAHRGANG);
		assertEquals("KL JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie alle Klassenlehrer
	 */
	@Test
	void testFormatKlassenlehrerAlle() {
		String format = adressbuchKategorienUtil.formatKlassenlehrerAlle();
		assertEquals("Klassenlehrer", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Lehrerteam einer Klasse
	 */
	@Test
	void testFormatLehrerKlasseTeam() {
		String format = adressbuchKategorienUtil.formatLehrerKlasseTeam(KLASSE);
		assertEquals("Team 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Schueler einer Klasse
	 */
	@Test
	void testFormatSchuelerKlasse() {
		String format = adressbuchKategorienUtil.formatSchuelerKlasse(KLASSE);
		assertEquals("Klasse 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Schueler eines Kurs
	 */
	@Test
	void testFormatSchuelerKurs() {
		String format = adressbuchKategorienUtil.formatSchuelerKurs(KURS, JAHRGANG);
		assertEquals("Kurs CH-GK1 09 S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Schueler eines Jahrgangs
	 *
	 */
	@Test
	void testFormatSchuelerJahrgang() {
		String format = adressbuchKategorienUtil.formatSchuelerJahrgang(JAHRGANG);
		assertEquals("JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Neuaufnahmen einer Klasse
	 *
	 */
	@Test
	void testFormatSchuelerNeuaufnahmeKlasse() {
		String format = adressbuchKategorienUtil.formatSchuelerNeuaufnahmeKlasse(KLASSE);
		assertEquals("Neuaufnahmen 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Neuaufnahmen eines Jahrgangs
	 *
	 */
	@Test
	void testFormatSchuelerNeuaufnahmeJahrgang() {
		String format = adressbuchKategorienUtil.formatSchuelerNeuaufnahmeJahrgang(JAHRGANG);
		assertEquals("Neuaufnahmen JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie alle Neuaufnahmen
	 */
	@Test
	void testFormatSchuelerNeuaufnahmenAlle() {
		String format = adressbuchKategorienUtil.formatSchuelerNeuaufnahmenAlle();
		assertEquals("Neuaufnahmen", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher einer Klasse
	 */
	@Test
	void testFormatErzieherKlasse() {
		String format = adressbuchKategorienUtil.formatErzieherKlasse(KLASSE);
		assertEquals("Eltern 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher eines Jahrgangs
	 *
	 */
	@Test
	void testFormatErzieherJahrgang() {
		String format = adressbuchKategorienUtil.formatErzieherJahrgang(JAHRGANG);
		assertEquals("Eltern JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher eines Kurs
	 */
	@Test
	void testFormatErzieherKurs() {
		String format = adressbuchKategorienUtil.formatErzieherKurs(KURS, JAHRGANG);
		assertEquals("Eltern CH-GK1 09 S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie alle Erzieher
	 * 
	 */
	@Test
	void testFormatErzieherAlle() {
		String format = adressbuchKategorienUtil.formatErzieherAlle();
		assertEquals("Eltern", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher der Neuaufnahmen
	 * einer Klasse
	 * 
	 */
	@Test
	void testFormatErzieherNeuaufnahmenKlasse() {
		String format = adressbuchKategorienUtil.formatErzieherNeuaufnahmenKlasse(KLASSE);
		assertEquals("Eltern Neuaufnahmen 9c S2 2022", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie Erzieher der Neuaufnahmen
	 * eines Jahrgangs
	 */
	@Test
	void testFormatErzieherNeuaufnahmenJahrgang() {
		String format = adressbuchKategorienUtil.formatErzieherNeuaufnahmenJahrgang(JAHRGANG);
		assertEquals("Eltern Neuaufnahmen JG 09", format);
	}

	/**
	 * Testet den formatierten String für die Kategorie alle Erzieher von
	 * Neuaufnahmen
	 *
	 */
	@Test
	void testFormatErzieherNeuaufnahmenAlle() {
		String format = adressbuchKategorienUtil.formatErzieherNeuaufnahmenAlle();
		assertEquals("Eltern Neuaufnahmen", format);
	}
}
