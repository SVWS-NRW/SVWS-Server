package de.svws_nrw.mapper.schueler.schulbesuch;

import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchPatchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class SchulbesuchMapperTest {

	private final SchulbesuchMapper mapper = SchulbesuchMapper.INSTANCE;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOSchueler createEntity(final long id) {
		return new DTOSchueler(id, "GU-" + id, false);
	}

	private SchulbesuchMappingContext emptyContext() {
		return new SchulbesuchMappingContext(Map.of(), Map.of(), List.of(), List.of(), 2020);
	}

	// -------------------------------------------------------------------------
	// toApi – direkte Feld-Mappings
	// -------------------------------------------------------------------------


	@Test
	@DisplayName("mapAbschlussartVorherigeSchule | Einstelliger Schlüssel setzt nur allgemeinbildend")
	void toApi_mapptEinstelligenAbschlussartSchluessel() {
		final var entity = createEntity(1L);
		entity.LSEntlassArt = "A";

		final var result = mapper.toApi(entity, emptyContext());

		assertThat(result.schluesselAbschlussartAllgemeinbildendVorherigeSchule).isEqualTo("A");
		assertThat(result.schluesselAbschlussartBerufsbildendVorherigeSchule).isNull();
	}

	@Test
	@DisplayName("mapAbschlussartVorherigeSchule | Zweistelliger Schlüssel splittet korrekt auf beide Felder")
	void toApi_mapptZweistelligenAbschlussartSchluessel() {
		final var entity = createEntity(1L);
		entity.LSEntlassArt = "2A";

		final var result = mapper.toApi(entity, emptyContext());

		assertThat(result.schluesselAbschlussartBerufsbildendVorherigeSchule).isEqualTo("2");
		assertThat(result.schluesselAbschlussartAllgemeinbildendVorherigeSchule).isEqualTo("A");
	}

	@ParameterizedTest
	@DisplayName("mapAbschlussartVorherigeSchule | null, blank, einstellige Ziffer auf null")
	@NullSource
	@ValueSource(strings = {" ", "   "})
	void toApi_mapptZuNull(final String input) {
		final var entity = createEntity(1L);
		entity.LSEntlassArt = input;

		final var result = mapper.toApi(entity, emptyContext());

		assertThat(result.schluesselAbschlussartAllgemeinbildendVorherigeSchule).isNull();
		assertThat(result.schluesselAbschlussartBerufsbildendVorherigeSchule).isNull();

	}

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt ID korrekt")
		void toApi_mapptId() {
			final var entity = createEntity(42L);

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.id).isEqualTo(42L);
		}

		@Test
		@DisplayName("Mappt einfache String-Felder der letzten Schule korrekt")
		void toApi_mapptLsStringFelder() {
			final var entity = createEntity(1L);
			entity.LSSchulEntlassDatum = "2020-06-30";
			entity.LSJahrgang = "10";
			entity.LSVersetzung = "V";
			entity.LSBemerkung = "Guter Schüler";
			entity.LSSGL_SIM = "A12";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.entlassdatumVorherigeSchule).isEqualTo("2020-06-30");
			assertThat(result.kuerzelEntlassjahrgangVorherigeSchule).isEqualTo("10");
			assertThat(result.idHerkunftsartVersetzungVorherigeSchule).isEqualTo("V");
			assertThat(result.bemerkungVorherigeSchule).isEqualTo("Guter Schüler");
		}

		@Test
		@DisplayName("Mappt Entlassungs-Felder der eigenen Schule korrekt")
		void toApi_mapptEntlassungFelder() {
			final var entity = createEntity(1L);
			entity.Entlassdatum = "2023-07-01";
			entity.Entlassjahrgang_ID = 5L;

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.entlassdatumDieseSchule).isEqualTo("2023-07-01");
			assertThat(result.idEntlassjahrgangDieseSchule).isEqualTo(5L);
		}

		@Test
		@DisplayName("map | idSchulformVorherigeschule")
		void toApi_idSchulformVorherigeSchule() {
			final var entity = createEntity(1L);
			entity.LSSchulformSIM = "AS";
			entity.LSSchulEntlassDatum = "2020-06-30";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idHerkunftSonstigeVorherigeSchule).isEqualTo(1000);
		}

		@Test
		@DisplayName("Mappt Schulwechsel-Felder korrekt")
		void toApi_mapptSchulwechselFelder() {
			final var entity = createEntity(1L);
			entity.Schulwechseldatum = "2023-08-01";
			entity.WechselBestaetigt = true;

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.wechseldatumAufnehmendeSchule).isEqualTo("2023-08-01");
			assertThat(result.wechselBestaetigtAufnehmendeSchule).isTrue();
		}

		@Test
		@DisplayName("Mappt Grundschul-Felder korrekt")
		void toApi_mapptGrundschulFelder() {
			final var entity = createEntity(1L);
			entity.Einschulungsjahr = 2005;
			entity.JahrWechsel_SI = 2011;
			entity.ErsteSchulform_SI = "GY";
			entity.JahrWechsel_SII = 2014;
			entity.Kindergarten_ID = 7L;

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.einschulungsjahrGrundschule).isEqualTo(2005);
			assertThat(result.wechseljahrSekI).isEqualTo(2011);
			assertThat(result.kuerzelErsteSchulformSek1).isEqualTo("GY");
			assertThat(result.wechseljahrSekII).isEqualTo(2014);
			assertThat(result.idKindergarten).isEqualTo(7L);
		}

		@Test
		@DisplayName("Mappt Sprachförderungs-Felder korrekt")
		void toApi_mapptSprachfoerderungFelder() {
			final var entity = createEntity(1L);
			entity.VerpflichtungSprachfoerderkurs = true;
			entity.TeilnahmeSprachfoerderkurs = false;

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.verpflichtungSprachfoerderkurs).isTrue();
			assertThat(result.teilnahmeSprachfoerderkurs).isFalse();
		}

		@Test
		@DisplayName("mapListen | Setzt Merkmale und bisherige Schulen aus dem Context")
		void toApi_mapptListenAusContext() {
			final var entity = createEntity(1L);
			final var merkmal = new SchuelerSchulbesuchMerkmal();
			final var schule = new SchuelerSchulbesuchSchule();
			final var ctx = new SchulbesuchMappingContext(Map.of(), Map.of(), List.of(merkmal), List.of(schule), 2020);

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.merkmale).containsExactly(merkmal);
			assertThat(result.bisherBesuchteSchulen).containsExactly(schule);
		}

		// --- Lookup-Mappings: Schule ---

		@Test
		@DisplayName("mapIdSchule | Löst bekannte Schulnummer auf ID auf")
		void toApi_mapptBekannteSchulnummer() {
			final var schule = new DTOSchuleNRW(99L, "012345");
			final var entity = createEntity(1L);
			entity.LSSchulNr = "012345";
			final var ctx = new SchulbesuchMappingContext(Map.of(), Map.of("012345", schule), List.of(), List.of(), 2020);

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.idVorherigeSchule).isEqualTo(99L);
		}

		@Test
		@DisplayName("mapIdSchule | Liefert null bei unbekannter Schulnummer")
		void toApi_mapptUnbekannteSchulnummerZuNull() {
			final var entity = createEntity(1L);
			entity.LSSchulNr = "999999";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idVorherigeSchule).isNull();
		}

		@Test
		@DisplayName("mapIdSchule | Liefert null wenn Schulnummer null ist")
		void toApi_mapptNullSchulnummerZuNull() {
			final var entity = createEntity(1L);
			entity.LSSchulNr = null;

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idVorherigeSchule).isNull();
		}

		@Test
		@DisplayName("mapIdSchule | Löst SchulwechselNr auf aufnehmende Schule auf")
		void toApi_mapptSchulwechselNrAufAufnehmendeSchule() {
			final var schule = new DTOSchuleNRW(77L, "054321");
			final var entity = createEntity(1L);
			entity.SchulwechselNr = "054321";
			final var ctx = new SchulbesuchMappingContext(Map.of(), Map.of("054321", schule), List.of(), List.of(), 2020);

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.idAufnehmendeSchule).isEqualTo(77L);
		}

		// --- Lookup-Mappings: Entlassgrund ---

		@Test
		@DisplayName("mapIdEntlassgrund | Löst bekannte Bezeichnung auf ID auf (LSEntlassgrund)")
		void toApi_mapptBekanntenEntlassgrundVorige() {
			final var entlassart = new DTOEntlassarten(3L, "Schulwechsel");
			final var entity = createEntity(1L);
			entity.LSEntlassgrund = "Schulwechsel";
			final var ctx = new SchulbesuchMappingContext(Map.of("Schulwechsel", entlassart), Map.of(), List.of(), List.of(), 2020);

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.idEntlassgrundVorherigeSchule).isEqualTo(3L);
		}

		@Test
		@DisplayName("mapIdEntlassgrund | Löst bekannte Bezeichnung auf ID auf (Entlassgrund)")
		void toApi_mapptBekanntenEntlassgrundEigen() {
			final var entlassart = new DTOEntlassarten(5L, "Abschluss");
			final var entity = createEntity(1L);
			entity.Entlassgrund = "Abschluss";
			final var ctx = new SchulbesuchMappingContext(Map.of("Abschluss", entlassart), Map.of(), List.of(), List.of(), 2020);

			final var result = mapper.toApi(entity, ctx);

			assertThat(result.idEntlassgrundDieseSchule).isEqualTo(5L);
		}

		@Test
		@DisplayName("mapIdEntlassgrund | Liefert null bei unbekannter Bezeichnung")
		void toApi_mapptUnbekanntenEntlassgrundZuNull() {
			final var entity = createEntity(1L);
			entity.LSEntlassgrund = "Gibts nicht";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idEntlassgrundVorherigeSchule).isNull();
		}

		// --- Katalog-Lookups: Null-Pfade (Schlüssel unbekannt) ---

		@Test
		@DisplayName("mapIdEinschulungsart | Liefert null bei unbekanntem Schlüssel")
		void toApi_mapptUnbekannteEinschulungsartZuNull() {
			final var entity = createEntity(1L);
			entity.EinschulungsartASD = "UNBEKANNT_XYZ";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idEinschulungsartGrundschule).isNull();
		}

		@Test
		@DisplayName("mapIdEinschulungsart | Liefert null wenn Schlüssel null ist")
		void toApi_mapptNullEinschulungsartZuNull() {
			final var entity = createEntity(1L);
			entity.EinschulungsartASD = null;

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idEinschulungsartGrundschule).isNull();
		}

		@Test
		@DisplayName("mapIdEingangsphase | Liefert null wenn EPJahre null ist")
		void toApi_mapptNullEingangsphaseZuNull() {
			final var entity = createEntity(1L);
			entity.EPJahre = null;

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idEingangsphaseGrundschule).isNull();
		}

		@Test
		@DisplayName("mapIdUebergangsempfehlung | Liefert null bei unbekanntem Schlüssel")
		void toApi_mapptUnbekannteUebergangsempfehlungZuNull() {
			final var entity = createEntity(1L);
			entity.Uebergangsempfehlung_JG5 = "UNBEKANNT_XYZ";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idUebergangsempfehlungGrundschule).isNull();
		}

		@Test
		@DisplayName("mapIdKindergartenbesuch | Liefert null bei unbekanntem Schlüssel")
		void toApi_mapptUnbekanntenKindergartenbesuchZuNull() {
			final var entity = createEntity(1L);
			entity.DauerKindergartenbesuch = "UNBEKANNT_XYZ";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.idDauerKindergartenbesuch).isNull();
		}

		@Test
		@DisplayName("Alle null-Felder in Entity liefern null im Ergebnis")
		void toApi_nullEntityFelder_liefeRtNullImResult() {
			final var entity = createEntity(2L);
			// alle relevanten Felder bleiben null (Default)

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result)
					.satisfies(r -> {
						assertThat(r.entlassdatumVorherigeSchule).isNull();
						assertThat(r.kuerzelEntlassjahrgangVorherigeSchule).isNull();
						assertThat(r.idVorherigeSchule).isNull();
						assertThat(r.idEntlassgrundVorherigeSchule).isNull();
						assertThat(r.idEntlassgrundDieseSchule).isNull();
						assertThat(r.idAufnehmendeSchule).isNull();
						assertThat(r.idEinschulungsartGrundschule).isNull();
						assertThat(r.idEingangsphaseGrundschule).isNull();
						assertThat(r.idUebergangsempfehlungGrundschule).isNull();
						assertThat(r.idDauerKindergartenbesuch).isNull();
					});
		}

		@Test
		@DisplayName("Rekonstruiert Schlüssel korrekt bei zweistelligem Präfix (10-179-02 -> 10-17902)")
		void toApi_mapptFachklasseMitZweistelligemPraefix() {
			final var entity = createEntity(1L);
			entity.LSFachklKennung = "10-179-02";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.schluesselCoreTypeFachklasseVorherigeSchule).isEqualTo("10-17902");
		}

		@Test
		@DisplayName("Rekonstruiert Schlüssel korrekt bei dreistelligem Präfix (170-179-02 -> 170-17902)")
		void toApi_mapptFachklasseMitDreistelligemPraefix() {
			final var entity = createEntity(1L);
			entity.LSFachklKennung = "170-179-02";

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.schluesselCoreTypeFachklasseVorherigeSchule).isEqualTo("170-17902");
		}

		@ParameterizedTest
		@DisplayName("Liefert null bei null oder leerem LSFachklKennung")
		@NullSource
		@ValueSource(strings = {" ", "   "})
		void toApi_mapptNullOderBlankZuNull(final String input) {
			final var entity = createEntity(1L);
			entity.LSFachklKennung = input;

			final var result = mapper.toApi(entity, emptyContext());

			assertThat(result.schluesselCoreTypeFachklasseVorherigeSchule).isNull();
		}

	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert alle definierten Felder")
		void patch_aktualisiertAlleDefiniertenFelder() {
			final var request = new SchulbesuchPatchRequest();
			request.entlassdatumVorherigeSchule = JsonNullable.of("2021-07-01");
			request.kuerzelEntlassjahrgangVorherigeSchule = JsonNullable.of("10");
			request.idHerkunftsartVersetzungVorherigeSchule = JsonNullable.of("V");
			request.bemerkungVorherigeSchule = JsonNullable.of("Bemerkung");
			request.entlassdatumDieseSchule = JsonNullable.of("2022-08-01");
			request.idEntlassjahrgangDieseSchule = JsonNullable.of(3L);
			request.wechseldatumAufnehmendeSchule = JsonNullable.of("2022-09-01");
			request.wechselBestaetigtAufnehmendeSchule = JsonNullable.of(true);
			request.einschulungsjahrGrundschule = JsonNullable.of(2005);
			request.wechseljahrSekI = JsonNullable.of(2011);
			request.kuerzelErsteSchulformSek1 = JsonNullable.of("GY");
			request.wechseljahrSekII = JsonNullable.of(2014);
			request.idKindergarten = JsonNullable.of(7L);
			request.verpflichtungSprachfoerderkurs = JsonNullable.of(true);
			request.teilnahmeSprachfoerderkurs = JsonNullable.of(false);
			final var entity = createEntity(1L);

			mapper.patch(request, entity);

			assertThat(entity)
					.satisfies(e -> {
						assertThat(e.LSSchulEntlassDatum).isEqualTo("2021-07-01");
						assertThat(e.LSJahrgang).isEqualTo("10");
						assertThat(e.LSVersetzung).isEqualTo("V");
						assertThat(e.LSBemerkung).isEqualTo("Bemerkung");
						assertThat(e.Entlassdatum).isEqualTo("2022-08-01");
						assertThat(e.Entlassjahrgang_ID).isEqualTo(3L);
						assertThat(e.Schulwechseldatum).isEqualTo("2022-09-01");
						assertThat(e.WechselBestaetigt).isTrue();
						assertThat(e.Einschulungsjahr).isEqualTo(2005);
						assertThat(e.JahrWechsel_SI).isEqualTo(2011);
						assertThat(e.ErsteSchulform_SI).isEqualTo("GY");
						assertThat(e.JahrWechsel_SII).isEqualTo(2014);
						assertThat(e.Kindergarten_ID).isEqualTo(7L);
						assertThat(e.VerpflichtungSprachfoerderkurs).isTrue();
						assertThat(e.TeilnahmeSprachfoerderkurs).isFalse();
					});
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesst_undefinedFelder_unveraendert() {
			final var request = new SchulbesuchPatchRequest();
			// alle Felder bleiben JsonNullable.undefined()

			final var entity = createEntity(1L);
			entity.LSSchulEntlassDatum = "2000-01-01";
			entity.LSJahrgang = "05";
			entity.Einschulungsjahr = 1999;

			mapper.patch(request, entity);

			assertThat(entity)
					.satisfies(e -> {
						assertThat(entity.LSSchulEntlassDatum).isEqualTo("2000-01-01");
						assertThat(entity.LSJahrgang).isEqualTo("05");
						assertThat(entity.Einschulungsjahr).isEqualTo(1999);
					});
		}

		@Test
		@DisplayName("Setzt Felder auf null wenn JsonNullable.of(null)")
		void patch_setztNullWerte() {
			final var request = new SchulbesuchPatchRequest();
			request.entlassdatumVorherigeSchule = JsonNullable.of(null);
			request.kuerzelEntlassjahrgangVorherigeSchule = JsonNullable.of(null);
			request.bemerkungVorherigeSchule = JsonNullable.of(null);

			final var entity = createEntity(1L);
			entity.LSSchulEntlassDatum = "2020-06-30";
			entity.LSJahrgang = "10";
			entity.LSBemerkung = "alt";

			mapper.patch(request, entity);
			assertThat(entity)
					.satisfies(e -> {
						assertThat(entity.LSSchulEntlassDatum).isNull();
						assertThat(entity.LSJahrgang).isNull();
						assertThat(entity.LSBemerkung).isNull();
					});
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var request = new SchulbesuchPatchRequest();
			request.entlassdatumVorherigeSchule = JsonNullable.of("2025-01-01");
			// vorigeEntlassjahrgang bleibt undefined
			final var entity = createEntity(1L);
			entity.LSSchulEntlassDatum = "2000-01-01";
			entity.LSJahrgang = "ORIGINAL";

			mapper.patch(request, entity);

			assertThat(entity)
					.satisfies(e -> {
						assertThat(entity.LSSchulEntlassDatum).isEqualTo("2025-01-01");
						assertThat(entity.LSJahrgang).isEqualTo("ORIGINAL");
					});
		}
	}
}
