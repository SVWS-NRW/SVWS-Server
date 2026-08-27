package de.svws_nrw.mapper.schueler.stammdaten;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.service.schueler.stammdaten.SchuelerImportData;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenPatchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class SchuelerStammdatenMapperTest {

	private final SchuelerStammdatenMapper mapper = SchuelerStammdatenMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Gültige CoreType-IDs für Tests
	// -------------------------------------------------------------------------

	/** Geschlecht männlich (ID 3) */
	private static final int GESCHLECHT_M = 3;
	/** Ungültige Geschlechts-ID */
	private static final int GESCHLECHT_INVALID = 999;

	/** Schülerstatus Neuaufnahme (ID 0) */
	private static final int STATUS_NEUAUFNAHME = 0;

	/** Gültige Nationalitäten-ID (Algerien) */
	private static final long NATIONALITAET_DZA = 68090065L;
	/** Ungültige Nationalitäten-ID */
	private static final long NATIONALITAET_INVALID = 999999999L;

	/** Gültige Verkehrssprachen-ID */
	private static final long VERKEHRSSPRACHE_XY = 2L;
	/** Ungültige Verkehrssprachen-ID */
	private static final long VERKEHRSSPRACHE_INVALID = 999999999L;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOSchueler createEntity(final long id) {
		final var entity = new DTOSchueler(id, "GU-" + id, false);
		entity.Geschlecht = Geschlecht.fromValue(GESCHLECHT_M);
		return entity;
	}

	private SchuelerImportData createImportData() {
		return new SchuelerImportData(
				"Mustermann",
				"Max",
				"Max Moritz",
				GESCHLECHT_M,
				"2000-01-01",
				STATUS_NEUAUFNAHME,
				"2024-08-01",
				"2024-09-01",
				"2024-09-01",
				2,
				1L,
				10L
		);
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt ID korrekt")
		void toApi_mapptId() {
			final var entity = createEntity(42L);

			final var result = mapper.toApi(entity);

			assertThat(result.id).isEqualTo(42L);
		}

		@Test
		@DisplayName("Mappt Nachname korrekt")
		void toApi_mapptNachname() {
			final var entity = createEntity(1L);
			entity.Nachname = "Mustermann";

			final var result = mapper.toApi(entity);

			assertThat(result.nachname).isEqualTo("Mustermann");
		}

		@Test
		@DisplayName("Mappt Vorname korrekt")
		void toApi_mapptVorname() {
			final var entity = createEntity(1L);
			entity.Vorname = "Max";

			final var result = mapper.toApi(entity);

			assertThat(result.vorname).isEqualTo("Max");
		}

		@Test
		@DisplayName("Mappt AlleVornamen korrekt")
		void toApi_mapptAlleVornamen() {
			final var entity = createEntity(1L);
			entity.AlleVornamen = "Max Moritz";

			final var result = mapper.toApi(entity);

			assertThat(result.alleVornamen).isEqualTo("Max Moritz");
		}

		@Test
		@DisplayName("Mappt Geschlecht-ID korrekt")
		void toApi_mapptGeschlecht() {
			final var entity = createEntity(1L);
			entity.Geschlecht = Geschlecht.fromValue(GESCHLECHT_M);

			final var result = mapper.toApi(entity);

			assertThat(result.geschlecht).isEqualTo(GESCHLECHT_M);
		}

		@Test
		@DisplayName("Foto wird ignoriert und bleibt null")
		void toApi_fotoWirdIgnoriert() {
			final var entity = createEntity(1L);

			final var result = mapper.toApi(entity);

			assertThat(result.foto).isNull();
		}

		@Test
		@DisplayName("Mappt Nationalitaet (StaatKrz) korrekt auf ID")
		void toApi_mapptNationalitaet() {
			final var entity = createEntity(1L);
			entity.StaatKrz = Nationalitaeten.data().getWertByIDOrNull(NATIONALITAET_DZA);

			final var result = mapper.toApi(entity);

			assertThat(result.idStaatsangehoerigkeit).isEqualTo(NATIONALITAET_DZA);
		}

		@Test
		@DisplayName("Mappt null-Nationalitaet auf null")
		void toApi_mapptNullNationalitaetAufNull() {
			final var entity = createEntity(1L);
			entity.StaatKrz = null;

			final var result = mapper.toApi(entity);

			assertThat(result.idStaatsangehoerigkeit).isNull();
		}

		@Test
		@DisplayName("Mappt zweite Nationalitaet (StaatKrz2) korrekt auf ID")
		void toApi_mapptZweiteNationalitaet() {
			final var entity = createEntity(1L);
			entity.StaatKrz2 = Nationalitaeten.data().getWertByIDOrNull(NATIONALITAET_DZA);

			final var result = mapper.toApi(entity);

			assertThat(result.idStaatsangehoerigkeit2).isEqualTo(NATIONALITAET_DZA);
		}

		@Test
		@DisplayName("Mappt Verkehrssprache korrekt auf ID")
		void toApi_mapptVerkehrssprache() {
			final var entity = createEntity(1L);
			entity.VerkehrsspracheFamilie = Verkehrssprache.data().getWertByIDOrNull(VERKEHRSSPRACHE_XY);

			final var result = mapper.toApi(entity);

			assertThat(result.idVerkehrspracheFamilie).isEqualTo(VERKEHRSSPRACHE_XY);
		}

		@Test
		@DisplayName("Mappt null-Verkehrssprache auf null")
		void toApi_mapptNullVerkehrssprachenAufNull() {
			final var entity = createEntity(1L);
			entity.VerkehrsspracheFamilie = null;

			final var result = mapper.toApi(entity);

			assertThat(result.idVerkehrspracheFamilie).isNull();
		}

		@Test
		@DisplayName("Mappt Geburtsland-Felder korrekt")
		void toApi_mapptGeburtslandFelder() {
			final var entity = createEntity(1L);
			final var nationalitaet = Nationalitaeten.data().getWertByIDOrNull(NATIONALITAET_DZA);
			entity.GeburtslandSchueler = nationalitaet;
			entity.GeburtslandVater = nationalitaet;
			entity.GeburtslandMutter = nationalitaet;

			final var result = mapper.toApi(entity);

			assertThat(result.idGeburtsland).isEqualTo(NATIONALITAET_DZA);
			assertThat(result.idGeburtslandVater).isEqualTo(NATIONALITAET_DZA);
			assertThat(result.idGeburtslandMutter).isEqualTo(NATIONALITAET_DZA);
		}

		@Test
		@DisplayName("Mappt alle einfachen Felder korrekt")
		void toApi_mapptAlleEinfachenFelder() {
			final var entity = createEntity(7L);
			entity.Nachname = "Mustermann";
			entity.Vorname = "Max";
			entity.Geburtsdatum = "2000-01-01";
			entity.Geburtsort = "Berlin";
			entity.Strassenname = "Musterweg";
			entity.HausNr = "4711";
			entity.HausNrZusatz = "a";
			entity.Ort_ID = 100L;
			entity.Ortsteil_ID = 200L;
			entity.Telefon = "0221-123456";
			entity.Fax = "0151-123456";
			entity.Email = "max@home.de";
			entity.SchulEmail = "max@schule.de";
			entity.Religion_ID = 5L;
			entity.KonfDruck = true;
			entity.idStatus = STATUS_NEUAUFNAHME;
			entity.Beruf = "Tischler";
			entity.DauerBildungsgang = 3;

			final var result = mapper.toApi(entity);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("nachname", "Mustermann")
					.hasFieldOrPropertyWithValue("vorname", "Max")
					.hasFieldOrPropertyWithValue("geburtsdatum", "2000-01-01")
					.hasFieldOrPropertyWithValue("geburtsort", "Berlin")
					.hasFieldOrPropertyWithValue("strassenname", "Musterweg")
					.hasFieldOrPropertyWithValue("hausnummer", "4711")
					.hasFieldOrPropertyWithValue("hausnummerZusatz", "a")
					.hasFieldOrPropertyWithValue("wohnortID", 100L)
					.hasFieldOrPropertyWithValue("ortsteilID", 200L)
					.hasFieldOrPropertyWithValue("telefon", "0221-123456")
					.hasFieldOrPropertyWithValue("telefonMobil", "0151-123456")
					.hasFieldOrPropertyWithValue("emailPrivat", "max@home.de")
					.hasFieldOrPropertyWithValue("emailSchule", "max@schule.de")
					.hasFieldOrPropertyWithValue("religionID", 5L)
					.hasFieldOrPropertyWithValue("druckeKonfessionAufZeugnisse", true)
					.hasFieldOrPropertyWithValue("status", STATUS_NEUAUFNAHME)
					.hasFieldOrPropertyWithValue("beruf", "Tischler")
					.hasFieldOrPropertyWithValue("dauerBildungsgang", 3);
		}
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Mappt GU_ID korrekt")
		void toDomain_mapptGuId() {
			final var request = createImportData();

			final var result = mapper.toDomain(request, "TEST-GU-ID");

			assertThat(result.GU_ID).isEqualTo("TEST-GU-ID");
		}

		@Test
		@DisplayName("Mappt Nachname und Vorname korrekt")
		void toDomain_mapptNachnameUndVorname() {
			final var request = createImportData();

			final var result = mapper.toDomain(request, "GU-1");

			assertThat(result)
					.hasFieldOrPropertyWithValue("Nachname", "Mustermann")
					.hasFieldOrPropertyWithValue("Vorname", "Max")
					.hasFieldOrPropertyWithValue("AlleVornamen", "Max Moritz");
		}

		@Test
		@DisplayName("Mappt Geschlecht korrekt aus ID")
		void toDomain_mapptGeschlecht() {
			final var request = createImportData();

			final var result = mapper.toDomain(request, "GU-1");

			assertThat(result.Geschlecht).isEqualTo(Geschlecht.fromValue(GESCHLECHT_M));
		}

		@Test
		@DisplayName("Mappt idStatus korrekt")
		void toDomain_mapptIdStatus() {
			final var request = createImportData();

			final var result = mapper.toDomain(request, "GU-1");

			assertThat(result.idStatus).isEqualTo(STATUS_NEUAUFNAHME);
		}

		@Test
		@DisplayName("Mappt Datums-Felder korrekt")
		void toDomain_mapptDatumsFelder() {
			final var request = createImportData();

			final var result = mapper.toDomain(request, "GU-1");

			assertThat(result)
					.hasFieldOrPropertyWithValue("Geburtsdatum", "2000-01-01")
					.hasFieldOrPropertyWithValue("AnmeldeDatum", "2024-08-01")
					.hasFieldOrPropertyWithValue("Aufnahmedatum", "2024-09-01")
					.hasFieldOrPropertyWithValue("BeginnBildungsgang", "2024-09-01");
		}

		@Test
		@DisplayName("Mappt DauerBildungsgang korrekt")
		void toDomain_mapptDauerBildungsgang() {
			final var request = createImportData();

			final var result = mapper.toDomain(request, "GU-1");

			assertThat(result.DauerBildungsgang).isEqualTo(2);
		}

		@Test
		@DisplayName("Mappt Religion_ID korrekt")
		void toDomain_mapptReligionId() {
			final var request = createImportData();

			final var result = mapper.toDomain(request, "GU-1");

			assertThat(result.Religion_ID).isEqualTo(1L);
		}

		@Test
		@DisplayName("Mappt Schuljahresabschnitts_ID korrekt")
		void toDomain_mapptSchuljahresabschnittsId() {
			final var request = createImportData();

			final var result = mapper.toDomain(request, "GU-1");

			assertThat(result.Schuljahresabschnitts_ID).isEqualTo(10L);
		}

		@Test
		@DisplayName("Mappt null-DauerBildungsgang korrekt")
		void toDomain_mapptNullDauerBildungsgang() {
			final var request = new SchuelerImportData(
					"Mustermann", "Max", null,
					GESCHLECHT_M, "2000-01-01", STATUS_NEUAUFNAHME,
					null, null, null, null, null, null
			);

			final var result = mapper.toDomain(request, "GU-1");

			assertThat(result.DauerBildungsgang).isNull();
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert Nachname korrekt")
		void patch_aktualisiertNachname() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.nachname = JsonNullable.of("Neumann");
			final var entity = createEntity(1L);
			entity.Nachname = "Alt";

			mapper.patch(request, entity);

			assertThat(entity.Nachname).isEqualTo("Neumann");
		}

		@Test
		@DisplayName("Lässt Nachname unverändert wenn undefined")
		void patch_laesst_nachname_unveraendert_bei_undefined() {
			final var request = new SchuelerStammdatenPatchRequest();
			final var entity = createEntity(1L);
			entity.Nachname = "Original";

			mapper.patch(request, entity);

			assertThat(entity.Nachname).isEqualTo("Original");
		}

		@Test
		@DisplayName("Aktualisiert Geschlecht korrekt aus ID")
		void patch_aktualisiertGeschlecht() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.geschlecht = JsonNullable.of(GESCHLECHT_M);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity.Geschlecht).isEqualTo(Geschlecht.fromValue(GESCHLECHT_M));
		}

		@Test
		@DisplayName("Aktualisiert Nationalitaet (StaatKrz) korrekt aus ID")
		void patch_aktualisiertNationalitaet() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.idStaatsangehoerigkeit = JsonNullable.of(NATIONALITAET_DZA);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity.StaatKrz).isEqualTo(Nationalitaeten.data().getWertByIDOrNull(NATIONALITAET_DZA));
		}

		@Test
		@DisplayName("Setzt Nationalitaet auf null bei ungültiger ID")
		void patch_setztNationalitaetAufNullBeiUngueltigerID() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.idStaatsangehoerigkeit = JsonNullable.of(NATIONALITAET_INVALID);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity.StaatKrz).isNull();
		}

		@Test
		@DisplayName("Lässt Nationalitaet unverändert wenn undefined")
		void patch_laesst_nationalitaet_unveraendert_bei_undefined() {
			final var request = new SchuelerStammdatenPatchRequest();
			final var entity = createEntity(1L);
			entity.StaatKrz = Nationalitaeten.data().getWertByIDOrNull(NATIONALITAET_DZA);

			mapper.patch(request, entity);

			assertThat(entity.StaatKrz).isEqualTo(Nationalitaeten.data().getWertByIDOrNull(NATIONALITAET_DZA));
		}

		@Test
		@DisplayName("Aktualisiert Verkehrssprache korrekt aus ID")
		void patch_aktualisiertVerkehrssprache() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.idVerkehrspracheFamilie = JsonNullable.of(VERKEHRSSPRACHE_XY);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity.VerkehrsspracheFamilie).isEqualTo(Verkehrssprache.data().getWertByIDOrNull(VERKEHRSSPRACHE_XY));
		}

		@Test
		@DisplayName("Setzt Verkehrssprache auf null bei ungültiger ID")
		void patch_setztVerkehrssprachenAufNullBeiUngueltigerID() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.idVerkehrspracheFamilie = JsonNullable.of(VERKEHRSSPRACHE_INVALID);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity.VerkehrsspracheFamilie).isNull();
		}

		@Test
		@DisplayName("Aktualisiert Geburtsland-Felder korrekt")
		void patch_aktualisiertGeburtslandFelder() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.idGeburtsland = JsonNullable.of(NATIONALITAET_DZA);
			request.idGeburtslandVater = JsonNullable.of(NATIONALITAET_DZA);
			request.idGeburtslandMutter = JsonNullable.of(NATIONALITAET_DZA);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			final var erwartet = Nationalitaeten.data().getWertByIDOrNull(NATIONALITAET_DZA);
			assertThat(entity)
					.hasFieldOrPropertyWithValue("GeburtslandSchueler", erwartet)
					.hasFieldOrPropertyWithValue("GeburtslandVater", erwartet)
					.hasFieldOrPropertyWithValue("GeburtslandMutter", erwartet);
		}

		@Test
		@DisplayName("Aktualisiert Boolean-Felder korrekt")
		void patch_aktualisiertBooleanFelder() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.istVolljaehrig = JsonNullable.of(true);
			request.istSchulpflichtErfuellt = JsonNullable.of(true);
			request.istBerufsschulpflichtErfuellt = JsonNullable.of(false);
			request.hatMasernimpfnachweis = JsonNullable.of(true);
			request.keineAuskunftAnDritte = JsonNullable.of(false);
			request.erhaeltSchuelerBAFOEG = JsonNullable.of(true);
			request.erhaeltMeisterBAFOEG = JsonNullable.of(false);
			request.druckeKonfessionAufZeugnisse = JsonNullable.of(true);
			request.hatMigrationshintergrund = JsonNullable.of(true);
			request.istDuplikat = JsonNullable.of(false);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Volljaehrig", true)
					.hasFieldOrPropertyWithValue("SchulpflichtErf", true)
					.hasFieldOrPropertyWithValue("BerufsschulpflErf", false)
					.hasFieldOrPropertyWithValue("MasernImpfnachweis", true)
					.hasFieldOrPropertyWithValue("KeineAuskunft", false)
					.hasFieldOrPropertyWithValue("Bafoeg", true)
					.hasFieldOrPropertyWithValue("MeisterBafoeg", false)
					.hasFieldOrPropertyWithValue("KonfDruck", true)
					.hasFieldOrPropertyWithValue("Migrationshintergrund", true)
					.hasFieldOrPropertyWithValue("Duplikat", false);
		}

		@Test
		@DisplayName("Aktualisiert einfache String-Felder korrekt")
		void patch_aktualisiertEinfacheStringFelder() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.nachname = JsonNullable.of("Mustermann");
			request.vorname = JsonNullable.of("Max");
			request.alleVornamen = JsonNullable.of("Max Moritz");
			request.geburtsort = JsonNullable.of("Berlin");
			request.geburtsname = JsonNullable.of("Muster");
			request.strassenname = JsonNullable.of("Musterweg");
			request.hausnummer = JsonNullable.of("4711");
			request.hausnummerZusatz = JsonNullable.of("a");
			request.telefon = JsonNullable.of("0221-123");
			request.telefonMobil = JsonNullable.of("0151-123");
			request.emailPrivat = JsonNullable.of("max@home.de");
			request.emailSchule = JsonNullable.of("max@schule.de");
			request.externeSchulNr = JsonNullable.of("123456");
			request.beruf = JsonNullable.of("Tischler");
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Nachname", "Mustermann")
					.hasFieldOrPropertyWithValue("Vorname", "Max")
					.hasFieldOrPropertyWithValue("AlleVornamen", "Max Moritz")
					.hasFieldOrPropertyWithValue("Geburtsort", "Berlin")
					.hasFieldOrPropertyWithValue("Geburtsname", "Muster")
					.hasFieldOrPropertyWithValue("Strassenname", "Musterweg")
					.hasFieldOrPropertyWithValue("HausNr", "4711")
					.hasFieldOrPropertyWithValue("HausNrZusatz", "a")
					.hasFieldOrPropertyWithValue("Telefon", "0221-123")
					.hasFieldOrPropertyWithValue("Fax", "0151-123")
					.hasFieldOrPropertyWithValue("Email", "max@home.de")
					.hasFieldOrPropertyWithValue("SchulEmail", "max@schule.de")
					.hasFieldOrPropertyWithValue("ExterneSchulNr", "123456")
					.hasFieldOrPropertyWithValue("Beruf", "Tischler");
		}

		@Test
		@DisplayName("Aktualisiert ID-Felder korrekt")
		void patch_aktualisiertIdFelder() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.wohnortID = JsonNullable.of(100L);
			request.ortsteilID = JsonNullable.of(200L);
			request.religionID = JsonNullable.of(5L);
			request.fahrschuelerArtID = JsonNullable.of(3L);
			request.haltestelleID = JsonNullable.of(7L);
			request.status = JsonNullable.of(STATUS_NEUAUFNAHME);
			request.zuzugsjahr = JsonNullable.of(2013);
			request.dauerBildungsgang = JsonNullable.of(3);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Ort_ID", 100L)
					.hasFieldOrPropertyWithValue("Ortsteil_ID", 200L)
					.hasFieldOrPropertyWithValue("Religion_ID", 5L)
					.hasFieldOrPropertyWithValue("Fahrschueler_ID", 3L)
					.hasFieldOrPropertyWithValue("Haltestelle_ID", 7L)
					.hasFieldOrPropertyWithValue("idStatus", STATUS_NEUAUFNAHME)
					.hasFieldOrPropertyWithValue("JahrZuzug", 2013)
					.hasFieldOrPropertyWithValue("DauerBildungsgang", 3);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var request = new SchuelerStammdatenPatchRequest();
			request.nachname = JsonNullable.of("Geändert");
			final var entity = createEntity(1L);
			entity.Nachname = "Alt";
			entity.Vorname = "OriginalVorname";

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Nachname", "Geändert")
					.hasFieldOrPropertyWithValue("Vorname", "OriginalVorname");
		}

		@Test
		@DisplayName("Lässt alle Felder unverändert bei leerem Request")
		void patch_laesst_alle_felder_unveraendert_bei_leerem_request() {
			final var request = new SchuelerStammdatenPatchRequest();
			final var entity = createEntity(1L);
			entity.Nachname = "Original";
			entity.Vorname = "OriginalVorname";
			entity.idStatus = STATUS_NEUAUFNAHME;
			entity.Geschlecht = Geschlecht.fromValue(GESCHLECHT_M);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Nachname", "Original")
					.hasFieldOrPropertyWithValue("Vorname", "OriginalVorname")
					.hasFieldOrPropertyWithValue("idStatus", STATUS_NEUAUFNAHME)
					.hasFieldOrPropertyWithValue("Geschlecht", Geschlecht.fromValue(GESCHLECHT_M));
		}
	}

	// -------------------------------------------------------------------------
	// Named-Konvertierungsmethoden (direkt)
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("mapNationalitaet")
	class MapNationalitaet {

		@Test
		@DisplayName("Gibt ID zurück bei gültigem Wert")
		void mapNationalitaet_gibtIdZurueck() {
			final var nationalitaet = Nationalitaeten.data().getWertByIDOrNull(NATIONALITAET_DZA);

			final var result = mapper.mapNationalitaet(nationalitaet);

			assertThat(result).isEqualTo(NATIONALITAET_DZA);
		}

		@ParameterizedTest
		@NullSource
		@DisplayName("Gibt null zurück bei null")
		void mapNationalitaet_gibtNullZurueckBeiNull(final Nationalitaeten nationalitaet) {
			final var result = mapper.mapNationalitaet(nationalitaet);

			assertThat(result).isNull();
		}
	}

	@Nested
	@DisplayName("mapVerkehrssprache")
	class MapVerkehrssprache {

		@Test
		@DisplayName("Gibt ID zurück bei gültigem Wert")
		void mapVerkehrssprache_gibtIdZurueck() {
			final var verkehrssprache = Verkehrssprache.data().getWertByIDOrNull(VERKEHRSSPRACHE_XY);

			final var result = mapper.mapVerkehrssprache(verkehrssprache);

			assertThat(result).isEqualTo(VERKEHRSSPRACHE_XY);
		}

		@ParameterizedTest
		@NullSource
		@DisplayName("Gibt null zurück bei null")
		void mapVerkehrssprache_gibtNullZurueckBeiNull(final Verkehrssprache verkehrssprache) {
			final var result = mapper.mapVerkehrssprache(verkehrssprache);

			assertThat(result).isNull();
		}
	}

	@Nested
	@DisplayName("mapGeschlecht")
	class MapGeschlecht {

		@Test
		@DisplayName("Gibt numerische ID zurück")
		void mapGeschlecht_gibtIdZurueck() {
			final var result = mapper.mapGeschlecht(Geschlecht.fromValue(GESCHLECHT_M));

			assertThat(result).isEqualTo(GESCHLECHT_M);
		}
	}

	@Nested
	@DisplayName("mapIdGeschlecht")
	class MapIdGeschlecht {

		@Test
		@DisplayName("Löst gültige ID auf Geschlecht auf")
		void mapIdGeschlecht_loestGueltigeIdAuf() {
			final var result = mapper.mapIdGeschlecht(GESCHLECHT_M);

			assertThat(result).isEqualTo(Geschlecht.fromValue(GESCHLECHT_M));
		}

		@Test
		@DisplayName("Gibt null zurück bei ungültiger ID")
		void mapIdGeschlecht_gibtNullBeiUngueltigerID() {
			final var result = mapper.mapIdGeschlecht(GESCHLECHT_INVALID);

			assertThat(result).isNull();
		}
	}

	@Nested
	@DisplayName("mapIdNationalitaet")
	class MapIdNationalitaet {

		@Test
		@DisplayName("Löst gültige ID auf Nationalitaeten auf")
		void mapIdNationalitaet_loestGueltigeIdAuf() {
			final var result = mapper.mapIdNationalitaet(NATIONALITAET_DZA);

			assertThat(result).isNotNull();
		}

		@Test
		@DisplayName("Gibt null zurück bei ungültiger ID")
		void mapIdNationalitaet_gibtNullBeiUngueltigerID() {
			final var result = mapper.mapIdNationalitaet(NATIONALITAET_INVALID);

			assertThat(result).isNull();
		}

		@ParameterizedTest
		@NullSource
		@DisplayName("Gibt null zurück bei null")
		void mapIdNationalitaet_gibtNullBeiNull(final Long id) {
			final var result = mapper.mapIdNationalitaet(id);

			assertThat(result).isNull();
		}
	}

	@Nested
	@DisplayName("mapIdVerkehrssprache")
	class MapIdVerkehrssprache {

		@Test
		@DisplayName("Löst gültige ID auf Verkehrssprache auf")
		void mapIdVerkehrssprache_loestGueltigeIdAuf() {
			final var result = mapper.mapIdVerkehrssprache(VERKEHRSSPRACHE_XY);

			assertThat(result).isNotNull();
		}

		@Test
		@DisplayName("Gibt null zurück bei ungültiger ID")
		void mapIdVerkehrssprache_gibtNullBeiUngueltigerID() {
			final var result = mapper.mapIdVerkehrssprache(VERKEHRSSPRACHE_INVALID);

			assertThat(result).isNull();
		}

		@ParameterizedTest
		@NullSource
		@DisplayName("Gibt null zurück bei null")
		void mapIdVerkehrssprache_gibtNullBeiNull(final Long id) {
			final var result = mapper.mapIdVerkehrssprache(id);

			assertThat(result).isNull();
		}
	}
}
