package de.svws_nrw.data.schueler;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen;
import de.svws_nrw.asd.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.asd.types.kaoa.KAOAKategorie;
import de.svws_nrw.asd.types.kaoa.KAOAMerkmal;
import de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Diese Klasse testet die Klasse {@link DataSchuelerKAoADaten}.
 */
@DisplayName("Diese Klasse testet die Klasse DataSchuelerKAoADaten")
@TestMethodOrder(MethodOrderer.MethodName.class)
class DataSchuelerKAoADatenTest {

	private SchuelerKAoADaten daten;

	/**
	 * Initialisierung der Core-Types
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Initialisiert eine Instanz von "SchuelerKAoADaten" und setzt Werte für verschiedene Attribute, um sie später in anderen Methoden verwenden zu können.
	 */
	@BeforeEach
	void setupEach() {
		final SchuelerKAoADaten kaoa = new SchuelerKAoADaten();
		kaoa.id = 135L;
		kaoa.jahrgang = "09";
		kaoa.kategorie = 14L;
		kaoa.merkmal = 69L;
		kaoa.zusatzmerkmal = 117L;
		kaoa.anschlussoption = 22L;
		kaoa.berufsfeld = 12L;
		kaoa.ebene4 = 4L;
		kaoa.bemerkung = "test text 123";
		this.daten = kaoa;
	}

	/**
	 * Testet den einen fehlerfreien Fall
	 * @throws ApiOperationException
	 */
	@Test
	@Disabled("Mocking must be improved")
	void testValidateOK() throws ApiOperationException {
		DataSchuelerKAoADaten.validate(2024, daten);
	}

	/**
	 * Testet ob eine Fehler kommt wenn Kategorie leer ist
	 */
	@Test
	@Disabled("Mocking must be improved")
	void testValidateKategorieNull() {
		daten.kategorie = -1L;
		final KAOAKategorie kategorie = KAOAKategorie.data().getWertByID(daten.kategorie);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateKategorie(daten, kategorie));
	}

	/**
	 * Testet ob eine Fehler kommt wenn keine Kategorie gefunden wird
	 */
	@Test
	void testValidateMerkmalWrong() {
		daten.merkmal = 35L;
		final KAOAKategorie kategorie = KAOAKategorie.data().getWertByID(daten.kategorie);
		final KAOAMerkmal merkmal = KAOAMerkmal.data().getWertByID(daten.merkmal);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateMerkmal(2024, daten, kategorie, merkmal));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Kategorie leer ist
	 */
	@Test
	@Disabled("Mocking must be improved")
	void testValidateMerkmalNotFound() {
		daten.merkmal = -1L;
		final KAOAKategorie kategorie = KAOAKategorie.data().getWertByID(daten.kategorie);
		final KAOAMerkmal merkmal = KAOAMerkmal.data().getWertByID(daten.merkmal);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateMerkmal(2024, daten, kategorie, merkmal));
	}

	/**
	 * Testet ob eine Fehler kommt wenn kein Zusatzmerkmal gefunden wird
	 */
	@Test
	void testValidateZusatzmerkmalWrong() {
		daten.zusatzmerkmal = 13L;
		final KAOAMerkmal merkmal = KAOAMerkmal.data().getWertByID(daten.merkmal);
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.data().getWertByID(daten.zusatzmerkmal);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateZusatzmerkmal(2024, daten, merkmal, zusatzmerkmal));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Zusatzmerkmal leer ist
	 */
	@Test
	@Disabled("Mocking must be improved")
	void testValidateZusatzmerkmalNotFound() {
		daten.zusatzmerkmal = -1L;
		final KAOAMerkmal merkmal = KAOAMerkmal.data().getWertByID(daten.merkmal);
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.data().getWertByID(daten.zusatzmerkmal);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateZusatzmerkmal(2024, daten, merkmal, zusatzmerkmal));
	}

	/**
	 * Testet ob eine Fehler kommt wenn keine Anschlussoptionen gefunden wird
	 */
	@Test
	@Disabled("Mocking must be improved")
	void testValidateAnschlussoptionNotFound() {
		daten.anschlussoption = -1L;
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.data().getWertByID(daten.zusatzmerkmal);
		final KAOAAnschlussoptionen anschlussoptionen = KAOAAnschlussoptionen.data().getWertByID(daten.anschlussoption);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateAnschlussoption(2024, daten, zusatzmerkmal, anschlussoptionen));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Anschlussoptionen nicht zum Zusatzmerkmal passt
	 */
	@Test
	@Disabled("Mocking must be improved")
	void testValidateAnschlussoptionWrong() {
		daten.anschlussoption = 26L;
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.data().getWertByID(daten.zusatzmerkmal);
		final KAOAAnschlussoptionen anschlussoptionen = KAOAAnschlussoptionen.data().getWertByID(daten.anschlussoption);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateAnschlussoption(2024, daten, zusatzmerkmal, anschlussoptionen));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Anschlussoptionen nicht zum Zusatzmerkmal passt
	 */
	@Test
	@Disabled("Mocking must be improved")
	void testValidateBerufsfeldNotFound() {
		daten.berufsfeld = -1L;
		daten.zusatzmerkmal = 44L;
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.data().getWertByID(daten.zusatzmerkmal);
		final KAOABerufsfeld berufsfeld = KAOABerufsfeld.data().getWertByID(daten.berufsfeld);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateBerufsfeld(2024, daten, zusatzmerkmal, berufsfeld));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Jahrgang nicht zur Kategorie passt
	 */
	@Test
	void testValidateJahrgang() {
		daten.jahrgang = "xxx";
		final KAOAKategorie kategorie = KAOAKategorie.data().getWertByID(daten.kategorie);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateJahrgang(2024, daten, kategorie));
	}
}
