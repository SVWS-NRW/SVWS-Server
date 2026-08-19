package de.svws_nrw.mapper.lehrer;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.mapper.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenMapper;
import de.svws_nrw.mapper.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenMappingContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LehrerPersonalabschnittsdatenMapperTest {

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	private final LehrerPersonalabschnittsdatenMapper mapper = LehrerPersonalabschnittsdatenMapper.INSTANCE;

	private DTOLehrerAbschnittsdaten createEntity() {
		final var e = new DTOLehrerAbschnittsdaten(1L, 10L, 20L);
		e.PflichtstdSoll = 18.5;
		e.StammschulNr = "123456";
		e.Rechtsverhaeltnis = "SCHLUESSEL_GIBT_ES_SICHER_NICHT";
		e.Beschaeftigungsart = "SCHLUESSEL_GIBT_ES_SICHER_NICHT";
		e.Einsatzstatus = "SCHLUESSEL_GIBT_ES_SICHER_NICHT";
		return e;
	}

	private LehrerPersonalabschnittsdatenAnrechnungsstunden createAnrechnung(final long id) {
		final var a = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		a.id = id;
		return a;
	}

	private LehrerFunktion createFunktion(final long id) {
		final var f = new LehrerFunktion();
		f.id = id;
		return f;
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt alle Basisfelder korrekt")
		void toApi_mapptAlleBasisfelder() {
			final var entity = createEntity();
			final var ctx = LehrerPersonalabschnittsdatenMappingContext.empty();

			final var result = mapper.toApi(entity, 2024, ctx);

			assertThat(result).isNotNull()
					.isInstanceOf(LehrerPersonalabschnittsdaten.class)
							.hasFieldOrPropertyWithValue("id", 1L)
							.hasFieldOrPropertyWithValue("idLehrer", 10L)
							.hasFieldOrPropertyWithValue("idSchuljahresabschnitt", 20L)
							.hasFieldOrPropertyWithValue("pflichtstundensoll", 18.5)
							.hasFieldOrPropertyWithValue("stammschulnummer", "123456");
		}

		@Test
		@DisplayName("Setzt Listen aus Context via @AfterMapping")
		void toApi_setztListenAusContext() {
			final var entity = createEntity();

			final var a1 = createAnrechnung(101L);
			final var m1 = createAnrechnung(201L);
			final var mi1 = createAnrechnung(301L);

			final var f1 = createFunktion(401L);
			final var f2 = createFunktion(402L);

			final var ctx = new LehrerPersonalabschnittsdatenMappingContext(
					List.of(a1),
					List.of(m1),
					List.of(mi1),
					List.of(f1, f2)
			);

			final var result = mapper.toApi(entity, 2024, ctx);

			assertThat(result.anrechnungen).containsExactly(a1);
			assertThat(result.mehrleistung).containsExactly(m1);
			assertThat(result.minderleistung).containsExactly(mi1);
			assertThat(result.funktionen).containsExactly(f1, f2);
		}

		@Test
		@DisplayName("Ungültige Katalog-Schlüssel führen zu null-IDs (defensiv)")
		void toApi_ungueltigeKatalogSchluesselWerdenNull() {
			final var entity = createEntity();
			final var ctx = LehrerPersonalabschnittsdatenMappingContext.empty();

			final var result = mapper.toApi(entity, 2024, ctx);

			assertThat(result.idRechtsverhaeltnis).isNull();
			assertThat(result.idBeschaeftigungsart).isNull();
			assertThat(result.idEinsatzstatus).isNull();
		}

		@Test
		@DisplayName("Null-Schlüssel werden zu null-IDs gemappt")
		void toApi_nullSchluesselWerdenNull() {
			final var entity = createEntity();
			entity.Rechtsverhaeltnis = null;
			entity.Beschaeftigungsart = null;
			entity.Einsatzstatus = null;

			final var ctx = LehrerPersonalabschnittsdatenMappingContext.empty();

			final var result = mapper.toApi(entity, 2024, ctx);

			assertThat(result.idRechtsverhaeltnis).isNull();
			assertThat(result.idBeschaeftigungsart).isNull();
			assertThat(result.idEinsatzstatus).isNull();
		}
	}
}
