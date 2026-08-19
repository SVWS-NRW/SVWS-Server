package de.svws_nrw.mapper.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import de.svws_nrw.mapper.lehrer.funktion.LehrerFunktionMapper;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionBatchPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionCreateRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

public class LehrerFunktionMapperTest {

	private final LehrerFunktionMapper mapper = LehrerFunktionMapper.INSTANCE;

	private DTOLehrerFunktion createEntity(final long id, final long idAbschnittsdaten, final long idFunktion) {
		return new DTOLehrerFunktion(id, idAbschnittsdaten, idFunktion);
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
			final var entity = createEntity(1L, 10L, 20L);

			final var result = mapper.toApi(entity);

			assertThat(result).isNotNull();
			assertThat(result.id).isEqualTo(1L);
			assertThat(result.idAbschnittsdaten).isEqualTo(10L);
			assertThat(result.idFunktion).isEqualTo(20L);
		}

		@Test
		@DisplayName("Liefert korrekten Typ")
		void toApi_liefertKorrektenTyp() {
			final var entity = createEntity(2L, 11L, 21L);

			final var result = mapper.toApi(entity);

			assertThat(result).isInstanceOf(LehrerFunktion.class);
		}
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Mappt alle Felder korrekt")
		void toDomain_mapptAlleFelder() {
			final var request = new LehrerFunktionCreateRequest();
			request.idAbschnittsdaten = 10L;
			request.idFunktion = 20L;

			final var result = mapper.toDomain(request);

			assertThat(result).isNotNull();
			assertThat(result.idAbschnittsdaten).isEqualTo(10L);
			assertThat(result.idFunktion).isEqualTo(20L);
		}

		@Test
		@DisplayName("Ignoriert ID – bleibt 0 bzw. Default")
		void toDomain_ignoriertId() {
			final var request = new LehrerFunktionCreateRequest();
			request.idAbschnittsdaten = 10L;
			request.idFunktion = 20L;

			final var result = mapper.toDomain(request);

			assertThat(result.id).isZero();
		}

		@Test
		@DisplayName("Liefert korrekten Typ")
		void toDomain_liefertKorrektenTyp() {
			final var request = new LehrerFunktionCreateRequest();
			request.idAbschnittsdaten = 10L;
			request.idFunktion = 20L;

			final var result = mapper.toDomain(request);

			assertThat(result).isInstanceOf(DTOLehrerFunktion.class);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert idFunktion wenn definiert")
		void patch_aktualisiertIdFunktion() {
			final var request = new LehrerFunktionPatchRequest();
			request.idFunktion = JsonNullable.of(99L);
			final var entity = createEntity(1L, 10L, 20L);

			mapper.patch(request, entity);

			assertThat(entity.idFunktion).isEqualTo(99L);
		}

		@Test
		@DisplayName("Lässt idFunktion unverändert wenn undefined")
		void patch_laesst_idFunktion_unveraendert_wenn_undefined() {
			final var request = new LehrerFunktionPatchRequest();
			// idFunktion bleibt JsonNullable.undefined()
			final var entity = createEntity(1L, 10L, 20L);

			mapper.patch(request, entity);

			assertThat(entity.idFunktion).isEqualTo(20L);
		}

		@Test
		@DisplayName("Ignoriert id – bleibt unverändert")
		void patch_ignoriertId() {
			final var request = new LehrerFunktionBatchPatchRequest();
			request.id = 999L;
			request.idFunktion = JsonNullable.of(50L);
			final var entity = createEntity(1L, 10L, 20L);

			mapper.patch(request, entity);

			assertThat(entity.id).isEqualTo(1L);
		}

		@Test
		@DisplayName("Ignoriert idAbschnittsdaten – bleibt unverändert")
		void patch_ignoriertIdAbschnittsdaten() {
			final var request = new LehrerFunktionPatchRequest();
			request.idFunktion = JsonNullable.of(50L);
			final var entity = createEntity(1L, 10L, 20L);

			mapper.patch(request, entity);

			assertThat(entity.idAbschnittsdaten).isEqualTo(10L);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var request = new LehrerFunktionPatchRequest();
			request.idFunktion = JsonNullable.of(77L);
			final var entity = createEntity(1L, 10L, 20L);

			mapper.patch(request, entity);

			assertThat(entity.idFunktion).isEqualTo(77L);
			assertThat(entity.idAbschnittsdaten).isEqualTo(10L);
			assertThat(entity.id).isEqualTo(1L);
		}
	}
}
