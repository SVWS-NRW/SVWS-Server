package de.svws_nrw.mapper.schueler.lernabschnittsdaten;

import java.util.List;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittCreateRequest;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittsdatenPatchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class SchuelerLernabschnittsdatenMapperTest {

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	private final SchuelerLernabschnittsdatenMapper mapper = SchuelerLernabschnittsdatenMapper.INSTANCE;

	private DTOSchuelerLernabschnittsdaten createEntity() {
		final var e = new DTOSchuelerLernabschnittsdaten(1L, 10L, 20L, true, false, 1L);
		e.WechselNr = 0;
		e.Klassen_ID = 30L;
		e.PruefOrdnung = "APO-SI";
		e.ZeugnisBem = "Zeugnisbemerkung";
		return e;
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt die Basisfelder korrekt")
		void toApi_mapptBasisfelder() {
			final var entity = createEntity();

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result).isNotNull()
					.hasFieldOrPropertyWithValue("id", 1L)
					.hasFieldOrPropertyWithValue("schuelerID", 10L)
					.hasFieldOrPropertyWithValue("schuljahresabschnitt", 20L)
					.hasFieldOrPropertyWithValue("wechselNr", 0)
					.hasFieldOrPropertyWithValue("klassenID", 30L)
					.hasFieldOrPropertyWithValue("pruefungsOrdnung", "APO-SI")
					.hasFieldOrPropertyWithValue("istFachpraktischerAnteilAusreichend", true);
		}

		@Test
		@DisplayName("Mappt idBetreuung aus der Entity")
		void toApi_idBetreuung() {
			final var entity = createEntity();
			entity.idBetreuung = 4L;

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.idBetreuung).isEqualTo(4L);
		}

		@Test
		@DisplayName("idBetreuung hat den Standardwert 1, wenn die Entity den Datenbank-Default trägt")
		void toApi_idBetreuungStandardwert() {
			final var result = mapper.toApi(createEntity(), 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.idBetreuung).isEqualTo(1L);
		}

		@Test
		@DisplayName("Übernimmt Bemerkungen und Leistungsdaten aus dem Kontext, Zeugnisbemerkung aus der Entity")
		void toApi_setztKontextUndZeugnisBemerkungen() {
			final var entity = createEntity();
			final var bemerkung = new SchuelerLernabschnittBemerkungen();
			bemerkung.zeugnisASV = "ASV-Text";
			final var leistung = new SchuelerLeistungsdaten();
			leistung.id = 100L;
			final var ctx = new SchuelerLernabschnittsdatenMappingContext(bemerkung, List.of(leistung));

			final var result = mapper.toApi(entity, 2024, Schulform.GY, ctx);

			assertThat(result.bemerkungen.zeugnisASV).isEqualTo("ASV-Text");
			assertThat(result.bemerkungen.zeugnisAllgemein).isEqualTo("Zeugnisbemerkung");
			assertThat(result.leistungsdaten).containsExactly(leistung);
		}

		@Test
		@DisplayName("istGewertet ist true, wenn SemesterWertung null ist")
		void toApi_istGewertetDefaultTrue() {
			final var entity = createEntity();
			entity.SemesterWertung = null;

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.istGewertet).isTrue();
		}

		@Test
		@DisplayName("istGewertet bleibt false, wenn SemesterWertung false ist")
		void toApi_istGewertetFalseBleibtFalse() {
			final var entity = createEntity();
			entity.SemesterWertung = false;

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.istGewertet).isFalse();
		}

		@Test
		@DisplayName("nachpruefungen bleibt null, wenn MoeglNPFaecher null ist")
		void toApi_nachpruefungenNullOhneFaecher() {
			final var entity = createEntity();
			entity.MoeglNPFaecher = null;

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.nachpruefungen).isNull();
		}

		@Test
		@DisplayName("nachpruefungen bleibt null, wenn MoeglNPFaecher nur ein Komma enthält")
		void toApi_nachpruefungenNullBeiNurKomma() {
			final var entity = createEntity();
			entity.MoeglNPFaecher = ",";

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.nachpruefungen).isNull();
		}

		@ParameterizedTest
		@ValueSource(strings = { "V", "A", "B" })
		@DisplayName("Mappt eine Nachprüfung mit dem passenden Grund")
		void toApi_mapptNachpruefungJeGrund(final String grund) {
			final var entity = createEntity();
			entity.MoeglNPFaecher = "D,M";
			switch (grund) {
				case "V" -> {
					entity.NPV_Fach_ID = 5L;
					entity.NPV_Datum = "2024-08-20";
					entity.NPV_NoteKrz = "3";
				}
				case "A" -> {
					entity.NPAA_Fach_ID = 5L;
					entity.NPAA_Datum = "2024-08-20";
					entity.NPAA_NoteKrz = "3";
				}
				default -> {
					entity.NPBQ_Fach_ID = 5L;
					entity.NPBQ_Datum = "2024-08-20";
					entity.NPBQ_NoteKrz = "3";
				}
			}

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.nachpruefungen).isNotNull();
			assertThat(result.nachpruefungen.moegliche).containsExactly("D", "M");
			assertThat(result.nachpruefungen.pruefungen).singleElement()
					.hasFieldOrPropertyWithValue("grund", grund)
					.hasFieldOrPropertyWithValue("fachID", 5L)
					.hasFieldOrPropertyWithValue("datum", "2024-08-20")
					.hasFieldOrPropertyWithValue("note", "3");
		}

		@Test
		@DisplayName("idVersetzungsvermerk wird über das Kürzel aufgelöst, nicht über den Schlüssel")
		void toApi_idVersetzungsvermerkUeberKuerzel() {
			final var entity = createEntity();
			entity.VersetzungKrz = "V";

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.idVersetzungsvermerk).isEqualTo(10L);
		}

		@ParameterizedTest
		@NullSource
		@ValueSource(strings = { "GIBT_ES_NICHT" })
		@DisplayName("idVersetzungsvermerk ist -1 bei fehlendem oder unbekanntem Kürzel")
		void toApi_idVersetzungsvermerkMinusEins(final String kuerzel) {
			final var entity = createEntity();
			entity.VersetzungKrz = kuerzel;

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.idVersetzungsvermerk).isEqualTo(-1L);
		}

		@ParameterizedTest
		@CsvSource(nullValues = "NULL", value = {
				"PK,            6001",
				"NULL,          NULL",
				"GIBT_ES_NICHT, NULL"
		})
		@DisplayName("idKlassenart wird über den Katalog aufgelöst, sonst null")
		void toApi_idKlassenart(final String kuerzel, final Long erwartet) {
			final var entity = createEntity();
			entity.Klassenart = kuerzel;

			final var result = mapper.toApi(entity, 2024, Schulform.GY, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.idKlassenart).isEqualTo(erwartet);
		}

		@ParameterizedTest
		@CsvSource(nullValues = "NULL", value = {
				"A01,           BK, 1001000",
				"NULL,          GY, 0",
				"GIBT_ES_NICHT, GY, 0",
				"NULL,          BK, NULL"
		})
		@DisplayName("idSchulgliederung: Katalog, sonst Standard der Schulform, sonst null")
		void toApi_idSchulgliederung(final String kuerzel, final Schulform schulform, final Long erwartet) {
			final var entity = createEntity();
			entity.Schulgliederung = kuerzel;

			final var result = mapper.toApi(entity, 2024, schulform, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.idSchulgliederung).isEqualTo(erwartet);
		}

		@ParameterizedTest
		@CsvSource(nullValues = "NULL", value = {
				"T,             WB, 2001000",
				"T,             BK, 1001000",
				"A,             SB, 1002000",
				"1,             GY, 3001001",
				"NULL,          GY, NULL",
				"GIBT_ES_NICHT, GY, NULL"
		})
		@DisplayName("idOrganisationsform wird je nach Schulform aus dem passenden Katalog aufgelöst")
		void toApi_idOrganisationsform(final String kuerzel, final Schulform schulform, final Long erwartet) {
			final var entity = createEntity();
			entity.OrgFormKrz = kuerzel;

			final var result = mapper.toApi(entity, 2024, schulform, SchuelerLernabschnittsdatenMappingContext.empty());

			assertThat(result.idOrganisationsform).isEqualTo(erwartet);
		}


	}

	// -------------------------------------------------------------------------
	// toDomain (Anlegen)
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		private SchuelerLernabschnittCreateRequest createRequest() {
			final var request = new SchuelerLernabschnittCreateRequest();
			request.schuelerID = 10L;
			request.schuljahresabschnitt = 20L;
			return request;
		}

		@Test
		@DisplayName("Ohne weitere Angaben - IDs werden übernommen und die Standardwerte gesetzt")
		void toDomain_setztStandardwerte() {
			final var result = mapper.toDomain(createRequest(), 2024, Schulform.GY);

			assertThat(result).satisfies(entity -> {
				assertThat(entity.Schueler_ID).isEqualTo(10L);
				assertThat(entity.Schuljahresabschnitts_ID).isEqualTo(20L);
				assertThat(entity.WechselNr).isZero();
				assertThat(entity.idBetreuung).isEqualTo(1L);
				assertThat(entity.SemesterWertung).isTrue();
				assertThat(entity.FachPraktAnteilAusr).isTrue();
				assertThat(entity.meldungBAN).isFalse();
				assertThat(entity.Wiederholung).isFalse();
				assertThat(entity.Schwerbehinderung).isFalse();
				assertThat(entity.AOSF).isFalse();
				assertThat(entity.Autist).isFalse();
				assertThat(entity.ZieldifferentesLernen).isFalse();
				assertThat(entity.Klassen_ID).isNull();
				assertThat(entity.Klassenart).isNull();
				assertThat(entity.VersetzungKrz).isNull();
			});
		}

		@Test
		@DisplayName("Mit allen Angaben - die Werte aus dem Request werden übernommen")
		void toDomain_uebernimmtAngaben() {
			final var request = createRequest();
			request.datumAnfang = "2024-08-01";
			request.datumEnde = "2025-01-31";
			request.datumKonferenz = "2025-01-20";
			request.datumZeugnis = "2025-01-31";
			request.istGewertet = false;
			request.istWiederholung = true;
			request.pruefungsOrdnung = "APO-GOSt";
			request.fehlstundenGesamt = 10;
			request.fehlstundenUnentschuldigt = 2;
			request.fehlstundenGrenzwert = 20;
			request.hatSchwerbehinderungsNachweis = true;
			request.hatAOSF = true;
			request.hatAutismus = true;
			request.hatZieldifferentenUnterricht = true;
			request.istFachpraktischerAnteilAusreichend = false;
			request.noteDurchschnitt = "2,3";
			request.noteLernbereichGSbzwAL = 3;
			request.noteLernbereichNW = 4;
			request.abschlussart = 5;
			request.istAbschlussPrognose = true;
			request.textErgebnisPruefungsalgorithmus = "versetzt";
			request.zeugnisart = "HJ";
			request.tutorID = 11L;
			request.sonderpaedagogeID = 12L;
			request.folgeklassenID = 13L;
			request.klassenID = 14L;
			request.foerderschwerpunkt1ID = 15L;
			request.foerderschwerpunkt2ID = 16L;
			request.schwerpunktID = 17L;
			request.bilingualerZweig = "E";
			request.jahrgangID = 18L;
			request.idBetreuung = 3L;
			request.fachklasseID = 19L;

			final var result = mapper.toDomain(request, 2024, Schulform.GY);

			assertThat(result).isNotNull()
				.hasFieldOrPropertyWithValue("DatumVon", "2024-08-01")
				.hasFieldOrPropertyWithValue("DatumBis", "2025-01-31")
				.hasFieldOrPropertyWithValue("Konferenzdatum", "2025-01-20")
				.hasFieldOrPropertyWithValue("ZeugnisDatum", "2025-01-31")
				.hasFieldOrPropertyWithValue("SemesterWertung", false)
				.hasFieldOrPropertyWithValue("Wiederholung", true)
				.hasFieldOrPropertyWithValue("PruefOrdnung", "APO-GOSt")
				.hasFieldOrPropertyWithValue("SumFehlStd", 10)
				.hasFieldOrPropertyWithValue("SumFehlStdU", 2)
				.hasFieldOrPropertyWithValue("FehlstundenGrenzwert", 20)
				.hasFieldOrPropertyWithValue("Schwerbehinderung", true)
				.hasFieldOrPropertyWithValue("AOSF", true)
				.hasFieldOrPropertyWithValue("Autist", true)
				.hasFieldOrPropertyWithValue("ZieldifferentesLernen", true)
				.hasFieldOrPropertyWithValue("FachPraktAnteilAusr", false)
				.hasFieldOrPropertyWithValue("DSNote", "2,3")
				.hasFieldOrPropertyWithValue("Gesamtnote_GS", 3)
				.hasFieldOrPropertyWithValue("Gesamtnote_NW", 4)
				.hasFieldOrPropertyWithValue("AbschlussArt", 5)
				.hasFieldOrPropertyWithValue("AbschlIstPrognose", true)
				.hasFieldOrPropertyWithValue("PruefAlgoErgebnis", "versetzt")
				.hasFieldOrPropertyWithValue("Zeugnisart", "HJ")
				.hasFieldOrPropertyWithValue("Tutor_ID", 11L)
				.hasFieldOrPropertyWithValue("Sonderpaedagoge_ID", 12L)
				.hasFieldOrPropertyWithValue("Folgeklasse_ID", 13L)
				.hasFieldOrPropertyWithValue("Klassen_ID", 14L)
				.hasFieldOrPropertyWithValue("Foerderschwerpunkt_ID", 15L)
				.hasFieldOrPropertyWithValue("Foerderschwerpunkt2_ID", 16L)
				.hasFieldOrPropertyWithValue("Schwerpunkt_ID", 17L)
				.hasFieldOrPropertyWithValue("BilingualerZweig", "E")
				.hasFieldOrPropertyWithValue("Jahrgang_ID", 18L)
				.hasFieldOrPropertyWithValue("idBetreuung", 3L)
				.hasFieldOrPropertyWithValue("Fachklasse_ID", 19L);
		}

		@Test
		@DisplayName("Ein leeres Datum wird zu null")
		void toDomain_leeresDatumWirdNull() {
			final var request = createRequest();
			request.datumAnfang = " ";

			final var result = mapper.toDomain(request, 2024, Schulform.GY);

			assertThat(result.DatumVon).isNull();
		}

		@Test
		@DisplayName("Klassenart, Schulgliederung und EP-Jahre werden aufgelöst")
		void toDomain_loestKatalogeAuf() {
			final var request = createRequest();
			request.idKlassenart = 6001L;
			request.idSchulgliederung = 1001000L;
			request.idEpJahre = 2L;

			final var result = mapper.toDomain(request, 2024, Schulform.GY);

			assertThat(result).satisfies(entity -> {
				assertThat(entity.Klassenart).isEqualTo("PK");
				assertThat(entity.Schulgliederung).isEqualTo("A01");
				assertThat(entity.EPJahre).isEqualTo(2);
			});
		}

	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("schwerpunktID wird in Schwerpunkt_ID übernommen")
		void patch_schwerpunktIdWirdUebernommen() {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.schwerpunktID = JsonNullable.of(2L);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.Schwerpunkt_ID).isEqualTo(2L);
		}

		@Test
		@DisplayName("schwerpunktID null entfernt den Schwerpunkt")
		void patch_schwerpunktIdNullEntferntSchwerpunkt() {
			final var entity = createEntity();
			entity.Schwerpunkt_ID = 2L;
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.schwerpunktID = JsonNullable.of(null);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.Schwerpunkt_ID).isNull();
		}

		@Test
		@DisplayName("schwerpunktID nicht im Patch - Schwerpunkt bleibt unverändert")
		void patch_schwerpunktIdFehltBleibtUnveraendert() {
			final var entity = createEntity();
			entity.Schwerpunkt_ID = 2L;
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.Schwerpunkt_ID).isEqualTo(2L);
		}

		@ParameterizedTest
		@CsvSource(nullValues = "NULL", value = {
				"2024-08-01, 2024-08-01",
				"'',         NULL",
				"'   ',      NULL",
				"NULL,       NULL"
		})
		@DisplayName("datumAnfang: leerer oder fehlender Wert wird zu null, sonst übernommen")
		void patch_datumAnfang(final String datum, final String erwartet) {
			final var entity = createEntity();
			entity.DatumVon = "2000-01-01";
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.datumAnfang = JsonNullable.of(datum);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.DatumVon).isEqualTo(erwartet);
		}

		@Test
		@DisplayName("idKlassenart wird in das Kürzel umgewandelt")
		void patch_idKlassenartWirdKuerzel() {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idKlassenart = JsonNullable.of(6001L);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.Klassenart).isEqualTo("PK");
		}

		@Test
		@DisplayName("idBetreuung wird in die Entity übernommen")
		void patch_idBetreuungWirdUebernommen() {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idBetreuung = JsonNullable.of(3L);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.idBetreuung).isEqualTo(3L);
		}

		@Test
		@DisplayName("idBetreuung fehlt im Request - Entity bleibt unverändert")
		void patch_idBetreuungFehltBleibtUnveraendert() {
			final var entity = createEntity();
			entity.idBetreuung = 5L;

			mapper.patch(new SchuelerLernabschnittsdatenPatchRequest(), entity, 2024, Schulform.GY);

			assertThat(entity.idBetreuung).isEqualTo(5L);
		}

		@ParameterizedTest(name = "idBetreuung {0} wird übernommen")
		@ValueSource(longs = { 1L, 2L, 6L })
		@DisplayName("idBetreuung: Alle Werte des Katalogs werden unverändert in die Entity übernommen")
		void patch_idBetreuungKatalogwerte(final long id) {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idBetreuung = JsonNullable.of(id);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.idBetreuung).isEqualTo(id);
		}

		@Test
		@DisplayName("idBetreuung zusammen mit anderen Feldern: Beide werden übernommen, nicht angegebene Felder bleiben unverändert")
		void patch_idBetreuungMitAnderenFeldern() {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idBetreuung = JsonNullable.of(2L);
			dto.istGewertet = JsonNullable.of(false);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.idBetreuung).isEqualTo(2L);
			assertThat(entity.SemesterWertung).isFalse();
			assertThat(entity.PruefOrdnung).isEqualTo("APO-SI");
			assertThat(entity.Klassen_ID).isEqualTo(30L);
		}

		@Test
		@DisplayName("idKlassenart null entfernt die Klassenart")
		void patch_idKlassenartNullEntferntKlassenart() {
			final var entity = createEntity();
			entity.Klassenart = "PK";
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idKlassenart = JsonNullable.of(null);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.Klassenart).isNull();
		}

		@Test
		@DisplayName("idSchulgliederung wird in das Kürzel umgewandelt")
		void patch_idSchulgliederungWirdKuerzel() {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idSchulgliederung = JsonNullable.of(1001000L);

			mapper.patch(dto, entity, 2024, Schulform.BK);

			assertThat(entity.Schulgliederung).isEqualTo("A01");
		}

		@ParameterizedTest
		@CsvSource({
				"2001000, WB, T",
				"1001000, BK, T",
				"1002000, SB, A",
				"3001001, GY, 1"
		})
		@DisplayName("idOrganisationsform wird je nach Schulform über den passenden Katalog in das Kürzel umgewandelt")
		void patch_idOrganisationsformWirdKuerzel(final long id, final Schulform schulform, final String erwartet) {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idOrganisationsform = JsonNullable.of(id);

			mapper.patch(dto, entity, 2024, schulform);

			assertThat(entity.OrgFormKrz).isEqualTo(erwartet);
		}

		@Test
		@DisplayName("idEpJahre wird als Anzahl übernommen")
		void patch_idEpJahreWirdAnzahl() {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idEpJahre = JsonNullable.of(2L);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.EPJahre).isEqualTo(2);
		}

		@Test
		@DisplayName("idVersetzungsvermerk wird in das Kürzel umgewandelt, nicht in den Schlüssel")
		void patch_idVersetzungsvermerkWirdKuerzel() {
			final var entity = createEntity();
			final var dto = new SchuelerLernabschnittsdatenPatchRequest();
			dto.idVersetzungsvermerk = JsonNullable.of(10L);

			mapper.patch(dto, entity, 2024, Schulform.GY);

			assertThat(entity.VersetzungKrz).isEqualTo("V");
		}

	}
}
