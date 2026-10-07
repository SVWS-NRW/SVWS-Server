package de.svws_nrw.mapper.schule.katalog.jahrgang;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangPatchRequest;
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

class JahrgangMapperTest {

	private final JahrgangMapper mapper = JahrgangMapper.INSTANCE;

	private static final int SCHULJAHR = 2024;
	private static final Schulform SCHULFORM = Schulform.GY;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOJahrgang createEntity(final long id) {
		return new DTOJahrgang(id);
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt alle einfachen Felder korrekt")
		void toApi_mapptAlleEinfachenFelder() {
			final var entity = createEntity(7L);
			entity.InternKrz = "05a";
			entity.Kurzbezeichnung = "5a";
			entity.ASDBezeichnung = "Jahrgang 5";
			entity.Folgejahrgang_ID = 8L;
			entity.AnzahlRestabschnitte = 3;
			entity.Sortierung = 10;
			entity.Sichtbar = true;
			entity.GueltigVon = 4L;
			entity.GueltigBis = 5L;

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("kuerzel", "05a")
					.hasFieldOrPropertyWithValue("kurzbezeichnung", "5a")
					.hasFieldOrPropertyWithValue("bezeichnung", "Jahrgang 5")
					.hasFieldOrPropertyWithValue("idFolgejahrgang", 8L)
					.hasFieldOrPropertyWithValue("anzahlRestabschnitte", 3)
					.hasFieldOrPropertyWithValue("sortierung", 10)
					.hasFieldOrPropertyWithValue("istSichtbar", true)
					.hasFieldOrPropertyWithValue("gueltigVon", 4L)
					.hasFieldOrPropertyWithValue("gueltigBis", 5L);
		}

		@Test
		@DisplayName("Mappt bezeichnung als Leerstring bei null")
		void toApi_mapptBezeichnungLeerstringBeiNull() {
			final var entity = createEntity(1L);
			entity.ASDBezeichnung = null;

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.bezeichnung).isEmpty();
		}

		@Test
		@DisplayName("Mappt sortierung auf 0 bei null")
		void toApi_mapptSortierungAufNullBeiNull() {
			final var entity = createEntity(1L);
			entity.Sortierung = null;

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.sortierung).isZero();
		}

		@Test
		@DisplayName("Mappt istSichtbar als false bei null")
		void toApi_mapptIstSichtbarFalseBeiNull() {
			final var entity = createEntity(1L);
			entity.Sichtbar = null;

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.istSichtbar).isFalse();
		}

		@ParameterizedTest(name = "Schuljahr {0} -> idJahrgang {1}")
		@DisplayName("Mappt idJahrgang abhängig vom Schuljahr")
		@CsvSource({ "2022, 5000000", "2023, 5000001" })
		void toApi_mapptIdJahrgangAbhaengigVomSchuljahr(final int schuljahr, final long expectedId) {
			final var entity = createEntity(1L);
			entity.ASDJahrgang = "05";

			final var result = mapper.toApi(entity, schuljahr, SCHULFORM);

			assertThat(result.idJahrgang).isEqualTo(expectedId);
		}

		@ParameterizedTest
		@DisplayName("Mappt idJahrgang als null bei unbekanntem oder null Kürzel")
		@NullSource
		@ValueSource(strings = { "UNBEKANNT" })
		void toApi_mapptIdJahrgangNullBeiUnbekanntemKuerzel(final String kuerzel) {
			final var entity = createEntity(1L);
			entity.ASDJahrgang = kuerzel;

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.idJahrgang).isNull();
		}

		@Test
		@DisplayName("Mappt idSchulgliederung korrekt bei bekanntem Kürzel")
		void toApi_mapptIdSchulgliederungBeiBekanntemKuerzel() {
			final var entity = createEntity(1L);
			entity.GliederungKuerzel = "A01";

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.idSchulgliederung).isEqualTo(1001000L);
		}

		@Test
		@DisplayName("Mappt idSchulgliederung auf Default-Gliederung der Schulform bei null Kürzel")
		void toApi_mapptIdSchulgliederungDefaultBeiNullKuerzel() {
			final var entity = createEntity(1L);
			entity.GliederungKuerzel = null;

			final var result = mapper.toApi(entity, SCHULJAHR, Schulform.GY);

			assertThat(result.idSchulgliederung).isZero();
		}

		@Test
		@DisplayName("Mappt idSchulgliederung als null bei null Kürzel und Schulform ohne Default-Gliederung")
		void toApi_mapptIdSchulgliederungNullBeiSchulformOhneDefault() {
			final var entity = createEntity(1L);
			entity.GliederungKuerzel = null;

			final var result = mapper.toApi(entity, SCHULJAHR, Schulform.BK);

			assertThat(result.idSchulgliederung).isNull();
		}

		@Test
		@DisplayName("Mappt idSchulgliederung als null bei unbekanntem Kürzel")
		void toApi_mapptIdSchulgliederungNullBeiUnbekanntemKuerzel() {
			final var entity = createEntity(1L);
			entity.GliederungKuerzel = "UNBEKANNT";

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.idSchulgliederung).isNull();
		}

		@Test
		@DisplayName("Mappt idBildungsstufe korrekt bei bekanntem Kürzel")
		void toApi_mapptIdBildungsstufeBeiBekanntemKuerzel() {
			final var entity = createEntity(1L);
			entity.Sekundarstufe = "SI";

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.idBildungsstufe).isEqualTo(1L);
		}

		@ParameterizedTest
		@DisplayName("Mappt idBildungsstufe als null bei unbekanntem oder null Kürzel")
		@NullSource
		@ValueSource(strings = { "UNBEKANNT" })
		void toApi_mapptIdBildungsstufeNullBeiUnbekanntemKuerzel(final String sekundarstufe) {
			final var entity = createEntity(1L);
			entity.Sekundarstufe = sekundarstufe;

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.idBildungsstufe).isNull();
		}

		@Test
		@DisplayName("referenziertInAnderenTabellen ist nach toApi null — wird im Service gesetzt")
		void toApi_referenziertInAnderenTabellenIstNull() {
			final var entity = createEntity(1L);

			final var result = mapper.toApi(entity, SCHULJAHR, SCHULFORM);

			assertThat(result.referenziertInAnderenTabellen).isNull();
		}

		@Test
		@DisplayName("Gibt null zurück bei null-Entity")
		void toApi_gibtNullZurueckBeiNullEntity() {
			final var result = mapper.toApi(null, SCHULJAHR, SCHULFORM);

			assertThat(result).isNull();
		}
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	private JahrgangCreateRequest createRequest() {
		final var request = new JahrgangCreateRequest();
		request.kuerzel = "05a";
		request.bezeichnung = "Jahrgang 5";
		request.kurzbezeichnung = "5a";
		request.idJahrgang = 5000001L;
		request.sortierung = 10;
		request.idFolgejahrgang = 8L;
		request.anzahlRestabschnitte = 3;
		request.istSichtbar = true;
		request.gueltigVon = 4L;
		request.gueltigBis = 5L;
		return request;
	}

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Mappt alle einfachen Felder korrekt")
		void toDomain_mapptAlleEinfachenFelder() {
			final var request = createRequest();

			final var result = mapper.toDomain(request);

			assertThat(result)
					.hasFieldOrPropertyWithValue("InternKrz", "05a")
					.hasFieldOrPropertyWithValue("ASDBezeichnung", "Jahrgang 5")
					.hasFieldOrPropertyWithValue("Kurzbezeichnung", "5a")
					.hasFieldOrPropertyWithValue("Sortierung", 10)
					.hasFieldOrPropertyWithValue("Folgejahrgang_ID", 8L)
					.hasFieldOrPropertyWithValue("AnzahlRestabschnitte", 3)
					.hasFieldOrPropertyWithValue("Sichtbar", true)
					.hasFieldOrPropertyWithValue("GueltigVon", 4L)
					.hasFieldOrPropertyWithValue("GueltigBis", 5L);
		}

		@Test
		@DisplayName("Setzt ID auf 0 und IstChronologisch auf null — beide werden nicht gemappt")
		void toDomain_setztIdUndIstChronologischNicht() {
			final var request = createRequest();

			final var result = mapper.toDomain(request);

			assertThat(result.ID).isZero();
			assertThat(result.IstChronologisch).isNull();
		}

		@ParameterizedTest
		@DisplayName("Mappt idJahrgang unabhängig vom Schuljahr auf dasselbe Kürzel")
		@ValueSource(longs = {5000000L, 5000001L})
		void toDomain_mapptIdJahrgangSchuljahrunabhaengig(final long idJahrgang) {
			final var request = createRequest();
			request.idJahrgang = idJahrgang;

			final var result = mapper.toDomain(request);

			assertThat(result.ASDJahrgang).isEqualTo("05");
		}

		@Test
		@DisplayName("Mappt idSchulgliederung auf das Kürzel der Schulgliederung")
		void toDomain_mapptIdSchulgliederungAufKuerzel() {
			final var request = createRequest();
			request.idSchulgliederung = 1001000L;

			final var result = mapper.toDomain(request);

			assertThat(result.GliederungKuerzel).isEqualTo("A01");
		}

		@Test
		@DisplayName("Mappt idBildungsstufe auf den Schlüssel der Bildungsstufe")
		void toDomain_mapptIdBildungsstufeAufSchluessel() {
			final var request = createRequest();
			request.idBildungsstufe = 1L;

			final var result = mapper.toDomain(request);

			assertThat(result.Sekundarstufe).isEqualTo("SI");
		}

		@ParameterizedTest
		@DisplayName("Mappt ASDJahrgang als null bei unbekannter oder null idJahrgang")
		@NullSource
		@ValueSource(longs = {-1L})
		void toDomain_mapptASDJahrgangNullBeiUnbekannterId(final Long idJahrgang) {
			final var request = createRequest();
			request.idJahrgang = idJahrgang;

			final var result = mapper.toDomain(request);

			assertThat(result.ASDJahrgang).isNull();
		}

		@ParameterizedTest
		@DisplayName("Mappt GliederungKuerzel als null bei unbekannter oder null idSchulgliederung")
		@NullSource
		@ValueSource(longs = {-1L})
		void toDomain_mapptGliederungKuerzelNullBeiUnbekannterId(final Long idSchulgliederung) {
			final var request = createRequest();
			request.idSchulgliederung = idSchulgliederung;

			final var result = mapper.toDomain(request);

			assertThat(result.GliederungKuerzel).isNull();
		}

		@ParameterizedTest
		@DisplayName("Mappt Sekundarstufe als null bei unbekannter oder null idBildungsstufe")
		@NullSource
		@ValueSource(longs = {-1L})
		void toDomain_mapptSekundarstufeNullBeiUnbekannterId(final Long idBildungsstufe) {
			final var request = createRequest();
			request.idBildungsstufe = idBildungsstufe;

			final var result = mapper.toDomain(request);

			assertThat(result.Sekundarstufe).isNull();
		}

		@Test
		@DisplayName("Gibt null zurück bei null-Request")
		void toDomain_gibtNullZurueckBeiNullRequest() {
			final var result = mapper.toDomain(null);

			assertThat(result).isNull();
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Ändert nur die gesetzten Felder und lässt alle übrigen unverändert")
		void patch_aendertNurGesetzteFelder() {
			final var entity = createFullEntity();
			final var request = new JahrgangPatchRequest();
			request.istSichtbar = JsonNullable.of(false);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Sichtbar", false)
					.hasFieldOrPropertyWithValue("InternKrz", "05a")
					.hasFieldOrPropertyWithValue("ASDBezeichnung", "Jahrgang 5")
					.hasFieldOrPropertyWithValue("Kurzbezeichnung", "5a")
					.hasFieldOrPropertyWithValue("Sortierung", 10)
					.hasFieldOrPropertyWithValue("Folgejahrgang_ID", 8L)
					.hasFieldOrPropertyWithValue("AnzahlRestabschnitte", 3)
					.hasFieldOrPropertyWithValue("GueltigVon", 4L)
					.hasFieldOrPropertyWithValue("GueltigBis", 5L)
					.hasFieldOrPropertyWithValue("ASDJahrgang", "05")
					.hasFieldOrPropertyWithValue("GliederungKuerzel", "A01")
					.hasFieldOrPropertyWithValue("Sekundarstufe", "SI");
		}

		@Test
		@DisplayName("Mappt alle einfachen Felder korrekt")
		void patch_mapptAlleEinfachenFelder() {
			final var entity = createEntity(7L);
			final var request = new JahrgangPatchRequest();
			request.kuerzel = JsonNullable.of("06b");
			request.bezeichnung = JsonNullable.of("Jahrgang 6");
			request.kurzbezeichnung = JsonNullable.of("6b");
			request.sortierung = JsonNullable.of(20);
			request.idFolgejahrgang = JsonNullable.of(9L);
			request.anzahlRestabschnitte = JsonNullable.of(4);
			request.istSichtbar = JsonNullable.of(true);
			request.gueltigVon = JsonNullable.of(6L);
			request.gueltigBis = JsonNullable.of(7L);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("InternKrz", "06b")
					.hasFieldOrPropertyWithValue("ASDBezeichnung", "Jahrgang 6")
					.hasFieldOrPropertyWithValue("Kurzbezeichnung", "6b")
					.hasFieldOrPropertyWithValue("Sortierung", 20)
					.hasFieldOrPropertyWithValue("Folgejahrgang_ID", 9L)
					.hasFieldOrPropertyWithValue("AnzahlRestabschnitte", 4)
					.hasFieldOrPropertyWithValue("Sichtbar", true)
					.hasFieldOrPropertyWithValue("GueltigVon", 6L)
					.hasFieldOrPropertyWithValue("GueltigBis", 7L);
		}

		@Test
		@DisplayName("Mappt idJahrgang auf das ASD-Kürzel des Jahrgangs")
		void patch_mapptIdJahrgangAufKuerzel() {
			final var entity = createEntity(1L);
			final var request = new JahrgangPatchRequest();
			request.idJahrgang = JsonNullable.of(5000001L);

			mapper.patch(request, entity);

			assertThat(entity.ASDJahrgang).isEqualTo("05");
		}

		@Test
		@DisplayName("Mappt idSchulgliederung auf das Kürzel der Schulgliederung")
		void patch_mapptIdSchulgliederungAufKuerzel() {
			final var entity = createEntity(1L);
			final var request = new JahrgangPatchRequest();
			request.idSchulgliederung = JsonNullable.of(1001000L);

			mapper.patch(request, entity);

			assertThat(entity.GliederungKuerzel).isEqualTo("A01");
		}

		@Test
		@DisplayName("Mappt idBildungsstufe auf den Schlüssel der Bildungsstufe")
		void patch_mapptIdBildungsstufeAufSchluessel() {
			final var entity = createEntity(1L);
			final var request = new JahrgangPatchRequest();
			request.idBildungsstufe = JsonNullable.of(1L);

			mapper.patch(request, entity);

			assertThat(entity.Sekundarstufe).isEqualTo("SI");
		}

		@Test
		@DisplayName("Leert Folgejahrgang_ID, wenn idFolgejahrgang explizit auf null gesetzt wird")
		void patch_leertFolgejahrgangBeiExplizitemNull() {
			final var entity = createFullEntity();
			final var request = new JahrgangPatchRequest();
			request.idFolgejahrgang = JsonNullable.of(null);

			mapper.patch(request, entity);

			assertThat(entity.Folgejahrgang_ID).isNull();
		}

		@Test
		@DisplayName("Leert GliederungKuerzel, wenn idSchulgliederung explizit auf null gesetzt wird")
		void patch_leertGliederungKuerzelBeiExplizitemNull() {
			final var entity = createFullEntity();
			final var request = new JahrgangPatchRequest();
			request.idSchulgliederung = JsonNullable.of(null);

			mapper.patch(request, entity);

			assertThat(entity.GliederungKuerzel).isNull();
		}

		@Test
		@DisplayName("Leert Sekundarstufe, wenn idBildungsstufe explizit auf null gesetzt wird")
		void patch_leertSekundarstufeBeiExplizitemNull() {
			final var entity = createFullEntity();
			final var request = new JahrgangPatchRequest();
			request.idBildungsstufe = JsonNullable.of(null);

			mapper.patch(request, entity);

			assertThat(entity.Sekundarstufe).isNull();
		}

		@Test
		@DisplayName("Lässt die Entity unverändert bei null-Request")
		void patch_laesstEntityUnveraendertBeiNullRequest() {
			final var entity = createFullEntity();

			mapper.patch(null, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("InternKrz", "05a")
					.hasFieldOrPropertyWithValue("ASDBezeichnung", "Jahrgang 5")
					.hasFieldOrPropertyWithValue("Folgejahrgang_ID", 8L);
		}

		private DTOJahrgang createFullEntity() {
			final var entity = createEntity(7L);
			entity.InternKrz = "05a";
			entity.ASDBezeichnung = "Jahrgang 5";
			entity.Kurzbezeichnung = "5a";
			entity.Sortierung = 10;
			entity.Folgejahrgang_ID = 8L;
			entity.AnzahlRestabschnitte = 3;
			entity.Sichtbar = true;
			entity.GueltigVon = 4L;
			entity.GueltigBis = 5L;
			entity.ASDJahrgang = "05";
			entity.GliederungKuerzel = "A01";
			entity.Sekundarstufe = "SI";
			return entity;
		}
	}
}
