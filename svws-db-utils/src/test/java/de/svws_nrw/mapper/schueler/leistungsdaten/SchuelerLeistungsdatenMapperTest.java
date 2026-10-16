package de.svws_nrw.mapper.schueler.leistungsdaten;

import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class SchuelerLeistungsdatenMapperTest {

	private static final int SCHULJAHR = 2024;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	private final SchuelerLeistungsdatenMapper mapper = SchuelerLeistungsdatenMapper.INSTANCE;

	private DTOSchuelerLeistungsdaten createEntity() {
		return new DTOSchuelerLeistungsdaten(1L, 10L, 20L);
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt alle gesetzten Felder korrekt")
		void toApi_mapptAlleFelder() {
			final var entity = createEntity();
			entity.Kurs_ID = 30L;
			entity.Kursart = "LK1";
			entity.AbiFach = "2";
			entity.Prf10Fach = true;
			entity.SchulNr = 123456;
			entity.Fachlehrer_ID = 40L;
			entity.Wochenstunden = 5;
			entity.Zusatzkraft_ID = 50L;
			entity.WochenstdZusatzkraft = 2;
			entity.AufZeugnis = false;
			entity.NotenKrz = "2+";
			entity.NotenKrzQuartal = "3-";
			entity.Warnung = true;
			entity.Warndatum = "2024-11-15";
			entity.VorherAbgeschl = true;
			entity.AbschlussJahrgang = "09";
			entity.Gewichtung = 2;
			entity.NoteAbschlussBA = "1";
			entity.Lernentw = "Lernentwicklung";
			entity.Umfang = "G";
			entity.FehlStd = 12;
			entity.uFehlStd = 3;

			final var result = mapper.toApi(entity, SCHULJAHR);

			assertThat(result).isNotNull()
					.hasFieldOrPropertyWithValue("id", 1L)
					.hasFieldOrPropertyWithValue("lernabschnittID", 10L)
					.hasFieldOrPropertyWithValue("fachID", 20L)
					.hasFieldOrPropertyWithValue("kursID", 30L)
					.hasFieldOrPropertyWithValue("kursart", "LK1")
					.hasFieldOrPropertyWithValue("abifach", 2)
					.hasFieldOrPropertyWithValue("istZP10oderZKEF", true)
					.hasFieldOrPropertyWithValue("koopSchule", 123456)
					.hasFieldOrPropertyWithValue("lehrerID", 40L)
					.hasFieldOrPropertyWithValue("wochenstunden", 5)
					.hasFieldOrPropertyWithValue("zusatzkraftID", 50L)
					.hasFieldOrPropertyWithValue("zusatzkraftWochenstunden", 2)
					.hasFieldOrPropertyWithValue("aufZeugnis", false)
					.hasFieldOrPropertyWithValue("note", "2+")
					.hasFieldOrPropertyWithValue("noteQuartal", "3-")
					.hasFieldOrPropertyWithValue("istGemahnt", true)
					.hasFieldOrPropertyWithValue("mahndatum", "2024-11-15")
					.hasFieldOrPropertyWithValue("istEpochal", true)
					.hasFieldOrPropertyWithValue("geholtJahrgangAbgeschlossen", "09")
					.hasFieldOrPropertyWithValue("gewichtungAllgemeinbildend", 2)
					.hasFieldOrPropertyWithValue("noteBerufsabschluss", "1")
					.hasFieldOrPropertyWithValue("textFachbezogeneLernentwicklung", "Lernentwicklung")
					.hasFieldOrPropertyWithValue("umfangLernstandsbericht", "G")
					.hasFieldOrPropertyWithValue("fehlstundenGesamt", 12)
					.hasFieldOrPropertyWithValue("fehlstundenUnentschuldigt", 3);
		}

		@Test
		@DisplayName("Setzt Standardwerte, wenn die optionalen Felder null sind")
		void toApi_setztStandardwerteBeiNull() {
			final var entity = createEntity();

			final var result = mapper.toApi(entity, SCHULJAHR);

			assertThat(result).isNotNull()
					.hasFieldOrPropertyWithValue("kursID", null)
					.hasFieldOrPropertyWithValue("kursart", ZulaessigeKursart.PUK.daten(SCHULJAHR).kuerzel)
					.hasFieldOrPropertyWithValue("abifach", null)
					.hasFieldOrPropertyWithValue("istZP10oderZKEF", false)
					.hasFieldOrPropertyWithValue("wochenstunden", 0)
					.hasFieldOrPropertyWithValue("zusatzkraftWochenstunden", 0)
					.hasFieldOrPropertyWithValue("aufZeugnis", true)
					.hasFieldOrPropertyWithValue("note", Note.KEINE.daten(SCHULJAHR).kuerzel)
					.hasFieldOrPropertyWithValue("noteQuartal", Note.KEINE.daten(SCHULJAHR).kuerzel)
					.hasFieldOrPropertyWithValue("istGemahnt", false)
					.hasFieldOrPropertyWithValue("istEpochal", false)
					.hasFieldOrPropertyWithValue("gewichtungAllgemeinbildend", 1)
					.hasFieldOrPropertyWithValue("textFachbezogeneLernentwicklung", "")
					.hasFieldOrPropertyWithValue("umfangLernstandsbericht", "")
					.hasFieldOrPropertyWithValue("fehlstundenGesamt", 0)
					.hasFieldOrPropertyWithValue("fehlstundenUnentschuldigt", 0);
		}

		@Test
		@DisplayName("Gibt null zurück, wenn die Entity null ist")
		void toApi_entityNull() {
			assertThat(mapper.toApi(null, SCHULJAHR)).isNull();
		}
	}

	// -------------------------------------------------------------------------
	// mapKursart
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("mapKursart")
	class MapKursart {

		@Test
		@DisplayName("Übernimmt eine gesetzte Kursart unverändert")
		void mapKursart_gesetzt() {
			assertThat(mapper.mapKursart("GK", SCHULJAHR)).isEqualTo("GK");
		}

		@Test
		@DisplayName("Liefert PUK, wenn keine Kursart gesetzt ist")
		void mapKursart_null() {
			assertThat(mapper.mapKursart(null, SCHULJAHR)).isEqualTo("PUK");
		}
	}

	// -------------------------------------------------------------------------
	// mapAbifach
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("mapAbifach")
	class MapAbifach {

		@ParameterizedTest(name = "\"{0}\" -> {1}")
		@CsvSource({ "1, 1", "2, 2", "3, 3", "4, 4" })
		@DisplayName("Wandelt gültige Zahlen um")
		void mapAbifach_gueltig(final String eingabe, final Integer erwartet) {
			assertThat(mapper.mapAbifach(eingabe)).isEqualTo(erwartet);
		}

		@ParameterizedTest(name = "\"{0}\" -> null")
		@ValueSource(strings = { "", " ", "X", "1a" })
		@DisplayName("Liefert null bei ungültigen Werten")
		void mapAbifach_ungueltig(final String eingabe) {
			assertThat(mapper.mapAbifach(eingabe)).isNull();
		}

		@Test
		@DisplayName("Liefert null, wenn kein Abiturfach gesetzt ist")
		void mapAbifach_null() {
			assertThat(mapper.mapAbifach(null)).isNull();
		}
	}

	// -------------------------------------------------------------------------
	// mapNote
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("mapNote")
	class MapNote {

		@ParameterizedTest(name = "\"{0}\"")
		@ValueSource(strings = { "1+", "2", "4-", "6" })
		@DisplayName("Löst gültige Notenkürzel auf")
		void mapNote_gueltig(final String kuerzel) {
			assertThat(mapper.mapNote(kuerzel, SCHULJAHR)).isEqualTo(kuerzel);
		}

		@Test
		@DisplayName("Liefert das Kürzel von Note.KEINE, wenn kein Kürzel gesetzt ist")
		void mapNote_null() {
			assertThat(mapper.mapNote(null, SCHULJAHR)).isEqualTo(Note.KEINE.daten(SCHULJAHR).kuerzel);
		}
	}
}
