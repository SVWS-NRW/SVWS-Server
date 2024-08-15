package de.svws_nrw.data.schueler;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.types.kaoa.KAOAAnschlussoption;
import de.svws_nrw.core.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.core.types.kaoa.KAOAKategorie;
import de.svws_nrw.core.types.kaoa.KAOAMerkmal;
import de.svws_nrw.core.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Diese Klasse testet die Klasse {@link DataSchuelerKAoADaten}.
 */
@DisplayName("Diese Klasse testet die Klasse DataSchuelerKAoADaten")
@TestMethodOrder(MethodOrderer.MethodName.class)
class DataSchuelerKAoADatenTest {

	private SchuelerKAoADaten daten;

	/**
	 * Initialisiert eine Instanz von "SchuelerKAoADaten" und setzt Werte für verschiedene Attribute, um sie später in anderen Methoden verwenden zu können.
	 */
	@BeforeEach
	void setup() {
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
	void testValidateOK() throws ApiOperationException {
		DataSchuelerKAoADaten.validate(daten);
	}

	/**
	 * Testet ob eine Fehler kommt wenn Kategorie leer ist
	 */
	@Test
	void testValidateKategorieNull() {
		daten.kategorie = -1L;
		final KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateKategorie(daten, kategorie));
	}

	/**
	 * Testet ob eine Fehler kommt wenn keine Kategorie gefunden wird
	 */
	@Test
	void testValidateMerkmalWrong() {
		daten.merkmal = 35L;
		final KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
		final KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateMerkmal(daten, kategorie, merkmal));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Kategorie leer ist
	 */
	@Test
	void testValidateMerkmalNotFound() {
		daten.merkmal = -1L;
		final KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
		final KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateMerkmal(daten, kategorie, merkmal));
	}

	/**
	 * Testet ob eine Fehler kommt wenn kein Zusatzmerkmal gefunden wird
	 */
	@Test
	void testValidateZusatzmerkmalWrong() {
		daten.zusatzmerkmal = 13L;
		final KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateZusatzmerkmal(daten, merkmal, zusatzmerkmal));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Zusatzmerkmal leer ist
	 */
	@Test
	void testValidateZusatzmerkmalNotFound() {
		daten.zusatzmerkmal = -1L;
		final KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateZusatzmerkmal(daten, merkmal, zusatzmerkmal));
	}

	/**
	 * Testet ob eine Fehler kommt wenn keine Anschlussoptionen gefunden wird
	 */
	@Test
	void testValidateAnschlussoptionNotFound() {
		daten.anschlussoption = -1L;
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
		final KAOAAnschlussoption anschlussoptionen = KAOAAnschlussoption.getByID(daten.anschlussoption);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateAnschlussoption(daten, zusatzmerkmal, anschlussoptionen));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Anschlussoptionen nicht zum Zusatzmerkmal passt
	 */
	@Test
	void testValidateAnschlussoptionWrong() {
		daten.anschlussoption = 26L;
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
		final KAOAAnschlussoption anschlussoptionen = KAOAAnschlussoption.getByID(daten.anschlussoption);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateAnschlussoption(daten, zusatzmerkmal, anschlussoptionen));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Anschlussoptionen nicht zum Zusatzmerkmal passt
	 */
	@Test
	void testValidateBerufsfeldNotFound() {
		daten.berufsfeld = -1L;
		daten.zusatzmerkmal = 44L;
		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
		final KAOABerufsfeld berufsfeld = KAOABerufsfeld.getByID(daten.berufsfeld);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateBerufsfeld(daten, zusatzmerkmal, berufsfeld));
	}

	/**
	 * Testet ob eine Fehler kommt wenn Jahrgang nicht zur Kategorie passt
	 */
	@Test
	void testValidateJahrgang() {
		daten.jahrgang = "xxx";
		final KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
		assertThrows(ApiOperationException.class, () -> DataSchuelerKAoADaten.validateJahrgang(daten, kategorie));
	}
}
