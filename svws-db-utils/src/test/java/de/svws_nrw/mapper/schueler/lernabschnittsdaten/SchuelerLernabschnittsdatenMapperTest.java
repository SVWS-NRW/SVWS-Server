package de.svws_nrw.mapper.schueler.lernabschnittsdaten;

import java.util.List;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
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
		final var e = new DTOSchuelerLernabschnittsdaten(1L, 10L, 20L, true, false);
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
