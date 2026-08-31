package de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz;

import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnkreuzkompetenzJahrgangMapperTest {

	private final AnkreuzkompetenzJahrgangMapper mapper = AnkreuzkompetenzJahrgangMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOAnkreuzkompetenzJahrgang createEntity(final long id) {
		return new DTOAnkreuzkompetenzJahrgang(id, 10L, 3L);
	}

	private AnkreuzkompetenzJahrgangCreateRequest createRequest() {
		final var request = new AnkreuzkompetenzJahrgangCreateRequest();
		request.idAnkreuzkompetenz = 10L;
		request.idJahrgang = 3L;
		return request;
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
			final var entity = createEntity(7L);

			final var result = mapper.toApi(entity);

			assertThat(result)
					.hasFieldOrPropertyWithValue("id", 7L)
					.hasFieldOrPropertyWithValue("idAnkreuzkompetenz", 10L)
					.hasFieldOrPropertyWithValue("idJahrgang", 3L);
		}

		@Test
		@DisplayName("Mappt id korrekt")
		void toApi_mapptIdKorrekt() {
			final var entity = createEntity(42L);

			final var result = mapper.toApi(entity);

			assertThat(result.id).isEqualTo(42L);
		}

		@Test
		@DisplayName("Mappt idAnkreuzkompetenz korrekt")
		void toApi_mapptIdAnkreuzkompetenzKorrekt() {
			final var entity = new DTOAnkreuzkompetenzJahrgang(1L, 99L, 3L);

			final var result = mapper.toApi(entity);

			assertThat(result.idAnkreuzkompetenz).isEqualTo(99L);
		}

		@Test
		@DisplayName("Mappt idJahrgang korrekt")
		void toApi_mapptIdJahrgangKorrekt() {
			final var entity = new DTOAnkreuzkompetenzJahrgang(1L, 10L, 77L);

			final var result = mapper.toApi(entity);

			assertThat(result.idJahrgang).isEqualTo(77L);
		}

		@Test
		@DisplayName("Gibt null zurück bei null-Entity")
		void toApi_gibtNullZurueckBeiNullEntity() {
			final var result = mapper.toApi(null);

			assertThat(result).isNull();
		}

	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Mappt idAnkreuzkompetenz und idJahrgang korrekt")
		void toDomain_mapptEinfacheFelder() {
			final var request = createRequest();

			final var result = mapper.toDomain(request);

			assertThat(result)
					.hasFieldOrPropertyWithValue("idAnkreuzkompetenz", 10L)
					.hasFieldOrPropertyWithValue("idJahrgang", 3L);
		}

		@Test
		@DisplayName("id wird nicht gemappt — bleibt 0")
		void toDomain_idWirdNichtGemappt() {
			final var request = createRequest();

			final var result = mapper.toDomain(request);

			assertThat(result.id).isZero();
		}

		@Test
		@DisplayName("Gibt null zurück bei null-Request")
		void toDomain_gibtNullZurueckBeiNullRequest() {
			final var result = mapper.toDomain(null);

			assertThat(result).isNull();
		}

		@Test
		@DisplayName("Mappt idAnkreuzkompetenz als 0 bei null-Wert")
		void toDomain_mapptIdAnkreuzkompetenzAlsNullDefaultBeiNull() {
			final var request = new AnkreuzkompetenzJahrgangCreateRequest();
			request.idAnkreuzkompetenz = null;
			request.idJahrgang = 3L;

			final var result = mapper.toDomain(request);

			assertThat(result.idAnkreuzkompetenz).isZero();
		}

		@Test
		@DisplayName("Mappt idJahrgang als 0 bei null-Wert")
		void toDomain_mapptIdJahrgangAlsNullDefaultBeiNull() {
			final var request = new AnkreuzkompetenzJahrgangCreateRequest();
			request.idAnkreuzkompetenz = 10L;
			request.idJahrgang = null;

			final var result = mapper.toDomain(request);

			assertThat(result.idJahrgang).isZero();
		}
	}
}
