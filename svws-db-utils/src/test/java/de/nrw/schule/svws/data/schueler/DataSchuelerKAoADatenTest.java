package de.nrw.schule.svws.data.schueler;

import de.nrw.schule.svws.core.data.schueler.SchuelerKAoADaten;
import de.nrw.schule.svws.core.types.kaoa.KAOAAnschlussoptionen;
import de.nrw.schule.svws.core.types.kaoa.KAOABerufsfeld;
import de.nrw.schule.svws.core.types.kaoa.KAOAKategorie;
import de.nrw.schule.svws.core.types.kaoa.KAOAMerkmal;
import de.nrw.schule.svws.core.types.kaoa.KAOAZusatzmerkmal;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Diese Klasse testet die Klasse {@link DataSchuelerKAoADaten}.
 */
@DisplayName("Diese Klasse testet die Klasse DataSchuelerKAoADaten")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class DataSchuelerKAoADatenTest {

    private SchuelerKAoADaten daten;

    /**
     * Initialisiert eine Instanz von "SchuelerKAoADaten" und setzt Werte für verschiedene Attribute, um sie später in anderen Methoden verwenden zu können.
     */
    @BeforeEach
    void setup() {
        SchuelerKAoADaten daten = new SchuelerKAoADaten();
        daten.id = 135L;
        daten.jahrgang = "09";
        daten.kategorie = 14L;
        daten.merkmal = 69L;
        daten.zusatzmerkmal = 117L;
        daten.anschlussoption = 22L;
        daten.berufsfeld = 12L;
        daten.ebene4 = 4L;
        daten.bemerkung = "test text 123";
        this.daten = daten;
    }

    /**
     * Testet den einen fehlerfreien Fall
     */
    @Test
    void testValidateOK() {
        DataSchuelerKAoADaten.validate(daten);
    }

    /**
     * Testet ob eine Fehler kommt wenn keine Kategorie gefunden wird
     */
    @Test
    void testValidateKategorieNotFound() {
        daten.kategorie = -1L;
        KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateKategorie(daten, kategorie));
    }

    /**
     * Testet ob eine Fehler kommt wenn Kategorie leer ist
     */
    @Test
    void testValidateKategorieNull() {
        daten.kategorie = null;
        KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateKategorie(daten, kategorie));
    }

    /**
     * Testet ob eine Fehler kommt wenn keine Kategorie gefunden wird
     */
    @Test
    void testValidateMerkmalWrong() {
        daten.merkmal = 35L;
        KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
        KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateMerkmal(daten, kategorie, merkmal));
    }

    /**
     * Testet ob eine Fehler kommt wenn Kategorie leer ist
     */
    @Test
    void testValidateMerkmalNotFound() {
        daten.merkmal = -1L;
        KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
        KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateMerkmal(daten, kategorie, merkmal));
    }

    /**
     * Testet ob eine Fehler kommt wenn kein Zusatzmerkmal gefunden wird
     */
    @Test
    void testValidateZusatzmerkmalWrong() {
        daten.zusatzmerkmal = 13L;
        KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
        KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateZusatzmerkmal(daten, merkmal, zusatzmerkmal));
    }

    /**
     * Testet ob eine Fehler kommt wenn Zusatzmerkmal leer ist
     */
    @Test
    void testValidateZusatzmerkmalNotFound() {
        daten.zusatzmerkmal = -1L;
        KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
        KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateZusatzmerkmal(daten, merkmal, zusatzmerkmal));
    }

    /**
     * Testet ob eine Fehler kommt wenn keine Anschlussoptionen gefunden wird
     */
    @Test
    void testValidateAnschlussoptionNotFound() {
        daten.anschlussoption = -1L;
        KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
        KAOAAnschlussoptionen anschlussoptionen = KAOAAnschlussoptionen.getByID(daten.anschlussoption);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateAnschlussoption(daten, zusatzmerkmal, anschlussoptionen));
    }

    /**
     * Testet ob eine Fehler kommt wenn Anschlussoptionen nicht zum Zusatzmerkmal passt
     */
    @Test
    void testValidateAnschlussoptionWrong() {
        daten.anschlussoption = 26L;
        KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
        KAOAAnschlussoptionen anschlussoptionen = KAOAAnschlussoptionen.getByID(daten.anschlussoption);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateAnschlussoption(daten, zusatzmerkmal, anschlussoptionen));
    }

    /**
     * Testet ob eine Fehler kommt wenn Anschlussoptionen nicht zum Zusatzmerkmal passt
     */
    @Test
    void testValidateBerufsfeldNotFound() {
        daten.berufsfeld = -1L;
        daten.zusatzmerkmal = 44L;
        KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
        KAOABerufsfeld berufsfeld = KAOABerufsfeld.getByID(daten.berufsfeld);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateBerufsfeld(daten, zusatzmerkmal, berufsfeld));
    }

    /**
     * Testet ob eine Fehler kommt wenn Jahrgang nicht zur Kategorie passt
     */
    @Test
    void testValidateJahrgang() {
        daten.jahrgang = "xxx";
        KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
        assertThrows(WebApplicationException.class, () -> DataSchuelerKAoADaten.validateJahrgang(daten, kategorie));
    }
}
