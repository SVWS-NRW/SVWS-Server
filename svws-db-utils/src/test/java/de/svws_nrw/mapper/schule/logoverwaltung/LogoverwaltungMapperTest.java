package de.svws_nrw.mapper.schule.logoverwaltung;

import de.svws_nrw.core.data.schule.Logo;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.service.schule.logoverwaltung.LogoCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests für {@link LogoverwaltungMapper}.
 */
@DisplayName("LogoverwaltungMapper")
class LogoverwaltungMapperTest {

	private static final LogoverwaltungMapper MAPPER = LogoverwaltungMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("toApi: alle gemappten Felder werden korrekt übertragen")
	void toApi_alleGemapptenFelder() {
		final var entity = new DTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD, "base64abc", "2026-01-01");

		final Logo result = MAPPER.toApi(entity);

		assertThat(result.id).isEqualTo(1L);
		assertThat(result.kennung).isEqualTo(ReportingBildDefinition.SCHULLOGO_SCHILD.getKennung());
		assertThat(result.logoBase64).isEqualTo("base64abc");
		assertThat(result.hinzugefuegtAm).isEqualTo("2026-01-01");
	}

	@ParameterizedTest(name = "toApi: kennung wird korrekt gemappt für {0}")
	@EnumSource(ReportingBildDefinition.class)
	@DisplayName("toApi: kennung-Mapping für alle ReportingBildDefinitionen")
	void toApi_kennungFuerAlleDefinitionen(final ReportingBildDefinition definition) {
		final var entity = new DTOLogo(1L, definition, "data", "2026-01-01");

		final Logo result = MAPPER.toApi(entity);

		assertThat(result.kennung).isEqualTo(definition.getKennung());
	}

	@Test
	@DisplayName("toApi: null-Entity liefert null")
	void toApi_nullEntity_liefertNull() {
		assertThat(MAPPER.toApi(null)).isNull();
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("toDomain: alle gemappten Felder werden korrekt übertragen")
	void toDomain_alleGemapptenFelder() {
		final var request = new LogoCreateRequest();
		request.kennung = ReportingBildDefinition.DIN5008_BRIEFKOPF.getKennung();
		request.logoBase64 = "base64xyz";

		final DTOLogo result = MAPPER.toDomain(request);

		assertThat(result.kennung).isEqualTo(ReportingBildDefinition.DIN5008_BRIEFKOPF);
		assertThat(result.logoBase64).isEqualTo("base64xyz");
		assertThat(result.hinzugefuegtAm).isNotNull();
	}

	@ParameterizedTest(name = "toDomain: kennung wird korrekt gemappt für {0}")
	@EnumSource(ReportingBildDefinition.class)
	@DisplayName("toDomain: kennung-Mapping für alle ReportingBildDefinitionen")
	void toDomain_kennungFuerAlleDefinitionen(final ReportingBildDefinition definition) {
		final var request = new LogoCreateRequest();
		request.kennung = definition.getKennung();
		request.logoBase64 = "data";

		final DTOLogo result = MAPPER.toDomain(request);

		assertThat(result.kennung).isEqualTo(definition);
	}

	@Test
	@DisplayName("toDomain: ignoreByDefault – nicht gemappte Felder bleiben null/default")
	void toDomain_nichtGemappteFelder_bleibenDefault() {
		final var request = new LogoCreateRequest();
		request.kennung = ReportingBildDefinition.SCHULLOGO_QUADRATISCH.getKennung();
		request.logoBase64 = "data";

		final DTOLogo result = MAPPER.toDomain(request);

		assertThat(result).hasFieldOrPropertyWithValue("id", 0L);
	}

	@Test
	@DisplayName("toDomain: null-Request liefert null")
	void toDomain_nullRequest_liefertNull() {
		assertThat(MAPPER.toDomain(null)).isNull();
	}

	// -------------------------------------------------------------------------
	// mapKennung (default-Methode direkt)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("mapKennung: gültige Kennung liefert korrekte ReportingBildDefinition")
	void mapKennung_gueltigeKennung() {
		final ReportingBildDefinition result = MAPPER.mapKennung("DIN5008_BRIEFKOPF");
		assertThat(result).isEqualTo(ReportingBildDefinition.DIN5008_BRIEFKOPF);
	}

	@Test
	@DisplayName("mapKennung: ungültige Kennung liefert null")
	void mapKennung_ungueltigeKennung_liefertNull() {
		assertThat(MAPPER.mapKennung("NICHT_VORHANDEN")).isNull();
	}

	@Test
	@DisplayName("mapKennung: null liefert null")
	void mapKennung_null_liefertNull() {
		assertThat(MAPPER.mapKennung(null)).isNull();
	}

	// -------------------------------------------------------------------------
	// mapReportingBildDefinition (default-Methode direkt)
	// -------------------------------------------------------------------------

	@ParameterizedTest(name = "mapReportingBildDefinition: {0} liefert korrekte Kennung")
	@EnumSource(ReportingBildDefinition.class)
	@DisplayName("mapReportingBildDefinition: liefert korrekte Kennung für alle Definitionen")
	void mapReportingBildDefinition_alleDefinitionen(final ReportingBildDefinition definition) {
		assertThat(MAPPER.mapReportingBildDefinition(definition)).isEqualTo(definition.getKennung());
	}
}
