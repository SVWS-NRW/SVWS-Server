package de.svws_nrw.mapper.lehrer.personaldaten;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenBatchPatchRequest;
import de.svws_nrw.service.lehrer.personaldaten.LehrerPersonaldatenPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests für den Mapper der Personaldaten von Lehrkräften.
 */
class LehrerPersonaldatenMapperTest {

	private final LehrerPersonaldatenMapper mapper = LehrerPersonaldatenMapper.INSTANCE;

	private static final long ID                       = 4711L;
	private static final String KUERZEL                = "MUS";
	private static final String NACHNAME               = "Mustermann";
	private static final String IDENT_NR_TEIL_1        = "1708593";
	private static final String IDENT_NR_TEIL_2_SERNR  = "12345";
	private static final String PERSONALAKTENNUMMER    = "PA-1";
	private static final String LBV_PERSONALNUMMER     = "LBV-1";
	private static final String LBV_VERGUETUNG         = "2";
	private static final String ZUGANGSDATUM           = "2011-11-11";
	private static final String ZUGANGSGRUND           = "WIEDER";
	private static final String ABGANGSDATUM           = "2020-11-12";
	private static final String ABGANGSGRUND           = "RUHEST";

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOLehrer createEntity() {
		final var entity = new DTOLehrer(ID, KUERZEL, NACHNAME);
		entity.identNrTeil1 = IDENT_NR_TEIL_1;
		entity.identNrTeil2SerNr = IDENT_NR_TEIL_2_SERNR;
		entity.PANr = PERSONALAKTENNUMMER;
		entity.personalNrLBV = LBV_PERSONALNUMMER;
		entity.verguetungsSchluessel = LBV_VERGUETUNG;
		entity.DatumZugang = ZUGANGSDATUM;
		entity.GrundZugang = ZUGANGSGRUND;
		entity.DatumAbgang = ABGANGSDATUM;
		entity.GrundAbgang = ABGANGSGRUND;
		return entity;
	}

	private LehrerPersonaldatenMappingContext createEmptyContext() {
		return new LehrerPersonaldatenMappingContext(List.of(), List.of());
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt alle Felder korrekt")
		void toApi_mapptAlleFelder() {
			final var entity = createEntity();

			assertThat(mapper.toApi(entity, createEmptyContext()))
					.isNotNull()
					.isInstanceOf(LehrerPersonaldaten.class)
					.hasFieldOrPropertyWithValue("id", ID)
					.hasFieldOrPropertyWithValue("identNrTeil1", IDENT_NR_TEIL_1)
					.hasFieldOrPropertyWithValue("identNrTeil2SerNr", IDENT_NR_TEIL_2_SERNR)
					.hasFieldOrPropertyWithValue("personalaktennummer", PERSONALAKTENNUMMER)
					.hasFieldOrPropertyWithValue("lbvPersonalnummer", LBV_PERSONALNUMMER)
					.hasFieldOrPropertyWithValue("lbvVerguetungsschluessel", LBV_VERGUETUNG)
					.hasFieldOrPropertyWithValue("zugangsdatum", ZUGANGSDATUM)
					.hasFieldOrPropertyWithValue("zugangsgrund", ZUGANGSGRUND)
					.hasFieldOrPropertyWithValue("abgangsdatum", ABGANGSDATUM)
					.hasFieldOrPropertyWithValue("abgangsgrund", ABGANGSGRUND);
		}

		@Test
		@DisplayName("Mappt fehlende Werte als null")
		void toApi_mapptFehlendeWerteAlsNull() {
			final var entity = new DTOLehrer(ID, KUERZEL, NACHNAME);

			final var result = mapper.toApi(entity, createEmptyContext());

			assertThat(result.id).isEqualTo(ID);
			assertThat(result.identNrTeil1).isNull();
			assertThat(result.identNrTeil2SerNr).isNull();
			assertThat(result.personalaktennummer).isNull();
			assertThat(result.lbvPersonalnummer).isNull();
			assertThat(result.lbvVerguetungsschluessel).isNull();
			assertThat(result.zugangsdatum).isNull();
			assertThat(result.zugangsgrund).isNull();
			assertThat(result.abgangsdatum).isNull();
			assertThat(result.abgangsgrund).isNull();
		}

		@Test
		@DisplayName("Befüllt abschnittsdaten aus dem Kontext")
		void toApi_befuelltAbschnittsdatenAusKontext() {
			final var abschnitt = new LehrerPersonalabschnittsdaten();
			final var ctx = new LehrerPersonaldatenMappingContext(List.of(abschnitt), List.of());

			final var result = mapper.toApi(createEntity(), ctx);

			assertThat(result.abschnittsdaten).containsExactly(abschnitt);
			assertThat(result.lehraemter).isEmpty();
		}

		@Test
		@DisplayName("Befüllt lehraemter aus dem Kontext")
		void toApi_befuelltLehraemterAusKontext() {
			final var lehramt = new LehrerLehramtEintrag();
			final var ctx = new LehrerPersonaldatenMappingContext(List.of(), List.of(lehramt));

			final var result = mapper.toApi(createEntity(), ctx);

			assertThat(result.lehraemter).containsExactly(lehramt);
			assertThat(result.abschnittsdaten).isEmpty();
		}

		@Test
		@DisplayName("Befüllt abschnittsdaten und lehraemter gleichzeitig aus dem Kontext")
		void toApi_befuelltBeideListenAusKontext() {
			final var abschnitt = new LehrerPersonalabschnittsdaten();
			final var lehramt = new LehrerLehramtEintrag();
			final var ctx = new LehrerPersonaldatenMappingContext(List.of(abschnitt), List.of(lehramt));

			final var result = mapper.toApi(createEntity(), ctx);

			assertThat(result.abschnittsdaten).containsExactly(abschnitt);
			assertThat(result.lehraemter).containsExactly(lehramt);
		}

		@Test
		@DisplayName("Gibt leere Listen zurück bei leerem Kontext")
		void toApi_gibtLeereListenBeiLeeremKontext() {
			final var result = mapper.toApi(createEntity(), createEmptyContext());

			assertThat(result.abschnittsdaten).isEmpty();
			assertThat(result.lehraemter).isEmpty();
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert alle definierten Felder korrekt")
		void patch_aktualisiertAlleDefiniertenFelder() {
			final var request = new LehrerPersonaldatenPatchRequest();
			request.identNrTeil1 = JsonNullable.of("neu1");
			request.identNrTeil2SerNr = JsonNullable.of("neu2");
			request.personalaktennummer = JsonNullable.of("neuPA");
			request.lbvPersonalnummer = JsonNullable.of("neuLBV");
			request.lbvVerguetungsschluessel = JsonNullable.of("9");
			request.zugangsdatum = JsonNullable.of("2001-01-01");
			request.zugangsgrund = JsonNullable.of("NEUZUG");
			request.abgangsdatum = JsonNullable.of("2002-02-02");
			request.abgangsgrund = JsonNullable.of("NEUABG");

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("identNrTeil1", "neu1")
					.hasFieldOrPropertyWithValue("identNrTeil2SerNr", "neu2")
					.hasFieldOrPropertyWithValue("PANr", "neuPA")
					.hasFieldOrPropertyWithValue("personalNrLBV", "neuLBV")
					.hasFieldOrPropertyWithValue("verguetungsSchluessel", "9")
					.hasFieldOrPropertyWithValue("DatumZugang", "2001-01-01")
					.hasFieldOrPropertyWithValue("GrundZugang", "NEUZUG")
					.hasFieldOrPropertyWithValue("DatumAbgang", "2002-02-02")
					.hasFieldOrPropertyWithValue("GrundAbgang", "NEUABG");
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesstUndefinedFelderUnveraendert() {
			final var request = new LehrerPersonaldatenPatchRequest();
			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("identNrTeil1", IDENT_NR_TEIL_1)
					.hasFieldOrPropertyWithValue("identNrTeil2SerNr", IDENT_NR_TEIL_2_SERNR)
					.hasFieldOrPropertyWithValue("PANr", PERSONALAKTENNUMMER)
					.hasFieldOrPropertyWithValue("personalNrLBV", LBV_PERSONALNUMMER)
					.hasFieldOrPropertyWithValue("verguetungsSchluessel", LBV_VERGUETUNG)
					.hasFieldOrPropertyWithValue("DatumZugang", ZUGANGSDATUM)
					.hasFieldOrPropertyWithValue("GrundZugang", ZUGANGSGRUND)
					.hasFieldOrPropertyWithValue("DatumAbgang", ABGANGSDATUM)
					.hasFieldOrPropertyWithValue("GrundAbgang", ABGANGSGRUND);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinedFelder() {
			final var request = new LehrerPersonaldatenPatchRequest();
			request.zugangsgrund = JsonNullable.of("NEUZUG");

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("GrundZugang", "NEUZUG")
					.hasFieldOrPropertyWithValue("DatumZugang", ZUGANGSDATUM)
					.hasFieldOrPropertyWithValue("GrundAbgang", ABGANGSGRUND);
		}

		@Test
		@DisplayName("Setzt Felder bei explizitem null auf null und lässt andere Felder unverändert")
		void patch_setztExplizitesNullAufNull() {
			final var request = new LehrerPersonaldatenPatchRequest();
			request.zugangsgrund = JsonNullable.of(null);
			request.abgangsdatum = JsonNullable.of(null);

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity.GrundZugang).isNull();
			assertThat(entity.DatumAbgang).isNull();
			assertThat(entity)
					.hasFieldOrPropertyWithValue("DatumZugang", ZUGANGSDATUM)
					.hasFieldOrPropertyWithValue("GrundAbgang", ABGANGSGRUND);
		}

		@Test
		@DisplayName("ID wird durch patch nicht verändert, auch nicht über einen Batch-Request")
		void patch_idWirdNichtVeraendert() {
			final var request = new LehrerPersonaldatenBatchPatchRequest();
			request.id = 99L;
			request.zugangsgrund = JsonNullable.of("NEUZUG");

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity.ID).isEqualTo(ID);
		}

		@Test
		@DisplayName("Lässt nicht gemappte Felder des Datenbankobjekts unverändert")
		void patch_laesstNichtGemappteFelderUnveraendert() {
			final var request = new LehrerPersonaldatenPatchRequest();
			request.zugangsdatum = JsonNullable.of("2001-01-01");

			final var entity = createEntity();

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Kuerzel", KUERZEL)
					.hasFieldOrPropertyWithValue("Nachname", NACHNAME);
		}
	}
}
