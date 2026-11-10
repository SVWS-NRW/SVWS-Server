package de.svws_nrw.mapper.schueler.leistungsdaten;

import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenCreateRequest;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenPatchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.jackson.nullable.JsonNullable;

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

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Nur Pflichtfelder - alle übrigen Felder bleiben leer")
		void toDomain_nurPflichtfelder() {
			final var request = new SchuelerLeistungsdatenCreateRequest();
			request.lernabschnittID = 10L;
			request.fachID = 20L;

			final var result = mapper.toDomain(request);

			assertThat(result)
					.hasFieldOrPropertyWithValue("ID", 0L)
					.hasFieldOrPropertyWithValue("Abschnitt_ID", 10L)
					.hasFieldOrPropertyWithValue("Fach_ID", 20L)
					.hasFieldOrPropertyWithValue("NotenKrz", null)
					.hasFieldOrPropertyWithValue("Wochenstunden", null)
					.hasFieldOrPropertyWithValue("Umfang", null)
					.hasFieldOrPropertyWithValue("Kurs_ID", null)
					.hasFieldOrPropertyWithValue("Sortierung", null);
		}

		@Test
		@DisplayName("Übernimmt alle Felder ohne Fachlogik")
		void toDomain_uebernimmtAlleFelder() {
			final var request = new SchuelerLeistungsdatenCreateRequest();
			request.lernabschnittID = 10L;
			request.fachID = 20L;
			request.istZP10oderZKEF = true;
			request.koopSchule = 654321;
			request.lehrerID = 41L;
			request.wochenstunden = 6;
			request.zusatzkraftID = 51L;
			request.zusatzkraftWochenstunden = 3;
			request.aufZeugnis = false;
			request.note = "1-";
			request.noteQuartal = "4+";
			request.istGemahnt = true;
			request.mahndatum = "2025-01-20";
			request.istEpochal = true;
			request.geholtJahrgangAbgeschlossen = "EF";
			request.gewichtungAllgemeinbildend = 3;
			request.textFachbezogeneLernentwicklung = "Text";
			request.umfangLernstandsbericht = "R";
			request.fehlstundenGesamt = 20;
			request.fehlstundenUnentschuldigt = 5;

			final var result = mapper.toDomain(request);

			assertThat(result)
					.hasFieldOrPropertyWithValue("Prf10Fach", true)
					.hasFieldOrPropertyWithValue("SchulNr", 654321)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", 41L)
					.hasFieldOrPropertyWithValue("Wochenstunden", 6)
					.hasFieldOrPropertyWithValue("Zusatzkraft_ID", 51L)
					.hasFieldOrPropertyWithValue("WochenstdZusatzkraft", 3)
					.hasFieldOrPropertyWithValue("AufZeugnis", false)
					.hasFieldOrPropertyWithValue("NotenKrz", "1-")
					.hasFieldOrPropertyWithValue("NotenKrzQuartal", "4+")
					.hasFieldOrPropertyWithValue("Warnung", true)
					.hasFieldOrPropertyWithValue("Warndatum", "2025-01-20")
					.hasFieldOrPropertyWithValue("VorherAbgeschl", true)
					.hasFieldOrPropertyWithValue("AbschlussJahrgang", "EF")
					.hasFieldOrPropertyWithValue("Gewichtung", 3)
					.hasFieldOrPropertyWithValue("Lernentw", "Text")
					.hasFieldOrPropertyWithValue("Umfang", "R")
					.hasFieldOrPropertyWithValue("FehlStd", 20)
					.hasFieldOrPropertyWithValue("uFehlStd", 5);
		}

		@Test
		@DisplayName("Kurs, Kursart und Abiturfach setzt der Service, der Mapper übernimmt sie nicht")
		void toDomain_uebernimmtKeineFelderMitFachlogik() {
			final var request = new SchuelerLeistungsdatenCreateRequest();
			request.lernabschnittID = 10L;
			request.fachID = 20L;
			request.kursID = 30L;
			request.kursart = "LK1";
			request.abifach = 1;

			final var result = mapper.toDomain(request);

			assertThat(result)
					.hasFieldOrPropertyWithValue("Kurs_ID", null)
					.hasFieldOrPropertyWithValue("Kursart", null)
					.hasFieldOrPropertyWithValue("AbiFach", null);
		}

		@Test
		@DisplayName("Ein leerer Umfang des Lernstandsberichts bleibt leer")
		void toDomain_leererUmfang() {
			final var request = new SchuelerLeistungsdatenCreateRequest();
			request.lernabschnittID = 10L;
			request.fachID = 20L;
			request.umfangLernstandsbericht = " ";

			assertThat(mapper.toDomain(request)).hasFieldOrPropertyWithValue("Umfang", null);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		private DTOSchuelerLeistungsdaten createBefuellteEntity() {
			final var entity = createEntity();
			entity.Kurs_ID = 30L;
			entity.Kursart = "LK1";
			entity.KursartAllg = "LK";
			entity.AbiFach = "2";
			entity.Prf10Fach = false;
			entity.SchulNr = 123456;
			entity.Fachlehrer_ID = 40L;
			entity.Wochenstunden = 5;
			entity.Zusatzkraft_ID = 50L;
			entity.WochenstdZusatzkraft = 2;
			entity.AufZeugnis = true;
			entity.NotenKrz = "2+";
			entity.NotenKrzQuartal = "3-";
			entity.Warnung = false;
			entity.Warndatum = "2024-11-15";
			entity.VorherAbgeschl = false;
			entity.AbschlussJahrgang = "09";
			entity.Gewichtung = 2;
			entity.NoteAbschlussBA = "1";
			entity.Lernentw = "Lernentwicklung";
			entity.Umfang = "V";
			entity.FehlStd = 12;
			entity.uFehlStd = 3;
			return entity;
		}

		@Test
		@DisplayName("Leerer Patch ändert nichts")
		void patch_leer() {
			final var entity = createBefuellteEntity();

			mapper.patch(new SchuelerLeistungsdatenPatchRequest(), entity);

			assertThat(entity)
					.usingRecursiveComparison()
					.isEqualTo(createBefuellteEntity());
		}

		@Test
		@DisplayName("Übernimmt alle enthaltenen Felder")
		void patch_uebernimmtAlleFelder() {
			final var entity = createBefuellteEntity();
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.istZP10oderZKEF = JsonNullable.of(true);
			request.koopSchule = JsonNullable.of(654321);
			request.lehrerID = JsonNullable.of(41L);
			request.wochenstunden = JsonNullable.of(6);
			request.zusatzkraftID = JsonNullable.of(51L);
			request.zusatzkraftWochenstunden = JsonNullable.of(3);
			request.aufZeugnis = JsonNullable.of(false);
			request.note = JsonNullable.of("1-");
			request.noteQuartal = JsonNullable.of("4+");
			request.istGemahnt = JsonNullable.of(true);
			request.mahndatum = JsonNullable.of("2025-01-20");
			request.istEpochal = JsonNullable.of(true);
			request.geholtJahrgangAbgeschlossen = JsonNullable.of("EF");
			request.gewichtungAllgemeinbildend = JsonNullable.of(3);
			request.textFachbezogeneLernentwicklung = JsonNullable.of("Neuer Text");
			request.umfangLernstandsbericht = JsonNullable.of("R");
			request.fehlstundenGesamt = JsonNullable.of(20);
			request.fehlstundenUnentschuldigt = JsonNullable.of(5);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Prf10Fach", true)
					.hasFieldOrPropertyWithValue("SchulNr", 654321)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", 41L)
					.hasFieldOrPropertyWithValue("Wochenstunden", 6)
					.hasFieldOrPropertyWithValue("Zusatzkraft_ID", 51L)
					.hasFieldOrPropertyWithValue("WochenstdZusatzkraft", 3)
					.hasFieldOrPropertyWithValue("AufZeugnis", false)
					.hasFieldOrPropertyWithValue("NotenKrz", "1-")
					.hasFieldOrPropertyWithValue("NotenKrzQuartal", "4+")
					.hasFieldOrPropertyWithValue("Warnung", true)
					.hasFieldOrPropertyWithValue("Warndatum", "2025-01-20")
					.hasFieldOrPropertyWithValue("VorherAbgeschl", true)
					.hasFieldOrPropertyWithValue("AbschlussJahrgang", "EF")
					.hasFieldOrPropertyWithValue("Gewichtung", 3)
					.hasFieldOrPropertyWithValue("Lernentw", "Neuer Text")
					.hasFieldOrPropertyWithValue("Umfang", "R")
					.hasFieldOrPropertyWithValue("FehlStd", 20)
					.hasFieldOrPropertyWithValue("uFehlStd", 5)
					.hasFieldOrPropertyWithValue("NoteAbschlussBA", "1");
		}

		@Test
		@DisplayName("Ein enthaltener Wert null entfernt den Wert")
		void patch_nullEntferntWert() {
			final var entity = createBefuellteEntity();
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.lehrerID = JsonNullable.of(null);
			request.zusatzkraftID = JsonNullable.of(null);
			request.note = JsonNullable.of(null);
			request.mahndatum = JsonNullable.of(null);
			request.koopSchule = JsonNullable.of(null);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", null)
					.hasFieldOrPropertyWithValue("Zusatzkraft_ID", null)
					.hasFieldOrPropertyWithValue("NotenKrz", null)
					.hasFieldOrPropertyWithValue("Warndatum", null)
					.hasFieldOrPropertyWithValue("SchulNr", null)
					.hasFieldOrPropertyWithValue("NotenKrzQuartal", "3-");
		}

		@ParameterizedTest(name = "Umfang \"{0}\" wird zu null")
		@ValueSource(strings = { "", " " })
		@DisplayName("Ein leerer Umfang des Lernstandsberichts entfernt den Wert")
		void patch_leererUmfang(final String umfang) {
			final var entity = createBefuellteEntity();
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.umfangLernstandsbericht = JsonNullable.of(umfang);

			mapper.patch(request, entity);

			assertThat(entity.Umfang).isNull();
		}

		@Test
		@DisplayName("Felder mit Fachlogik werden nicht übernommen")
		void patch_ignoriertFelderMitFachlogik() {
			final var entity = createBefuellteEntity();
			final var request = new SchuelerLeistungsdatenPatchRequest();
			request.lernabschnittID = JsonNullable.of(11L);
			request.fachID = JsonNullable.of(21L);
			request.kursID = JsonNullable.of(31L);
			request.kursart = JsonNullable.of("GKM");
			request.abifach = JsonNullable.of(3);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Abschnitt_ID", 10L)
					.hasFieldOrPropertyWithValue("Fach_ID", 20L)
					.hasFieldOrPropertyWithValue("Kurs_ID", 30L)
					.hasFieldOrPropertyWithValue("Kursart", "LK1")
					.hasFieldOrPropertyWithValue("KursartAllg", "LK")
					.hasFieldOrPropertyWithValue("AbiFach", "2");
		}
	}

	// -------------------------------------------------------------------------
	// mapUmfang
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("mapUmfang")
	class MapUmfang {

		@ParameterizedTest(name = "\"{0}\"")
		@ValueSource(strings = { "V", "R" })
		@DisplayName("Behält gültige Umfänge")
		void mapUmfang_gueltig(final String umfang) {
			assertThat(mapper.mapUmfang(umfang)).isEqualTo(umfang);
		}

		@Test
		@DisplayName("Liefert null für null")
		void mapUmfang_null() {
			assertThat(mapper.mapUmfang(null)).isNull();
		}
	}
}
