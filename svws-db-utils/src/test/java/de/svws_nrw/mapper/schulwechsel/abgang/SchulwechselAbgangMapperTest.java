package de.svws_nrw.mapper.schulwechsel.abgang;

import de.svws_nrw.core.data.schule.SchulwechselAbgang;
import de.svws_nrw.core.types.schule.StatusSchulwechselAbgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselAbgang;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangCreateRequest;
import de.svws_nrw.service.schulwechsel.abgang.SchulwechselAbgangPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class SchulwechselAbgangMapperTest {

	private final SchulwechselAbgangMapper mapper = SchulwechselAbgangMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOSchulwechselAbgang createEntity() {
		return new DTOSchulwechselAbgang(1L, 123L, StatusSchulwechselAbgang.BEVORSTEHEND, "2025-04-12 12:34:25");
	}

	private SchulwechselAbgangCreateRequest createRequest() {
		final var dto = new SchulwechselAbgangCreateRequest();
		dto.idSchueler = 123L;
		dto.idStatus = 1;
		dto.lastModified = "2025-04-12 12:34:00";
		dto.idDocument = 456L;
		return dto;
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
			entity.lastModified = "2025-04-12 12:34:00";
			entity.idDocument = 456L;

			assertThat(mapper.toApi(entity))
					.hasFieldOrPropertyWithValue("id", 1L)
					.hasFieldOrPropertyWithValue("idSchueler", 123L)
					.hasFieldOrPropertyWithValue("idStatus", 1)
					.hasFieldOrPropertyWithValue("lastModified", "2025-04-12 12:34:00")
					.hasFieldOrPropertyWithValue("idDocument", 456L);
		}

		@Test
		@DisplayName("Liefert nicht-null Ergebnis")
		void toApi_ergebnisNichtNull() {
			assertThat(mapper.toApi(createEntity()))
					.isNotNull()
					.isInstanceOf(SchulwechselAbgang.class);
		}
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		@Test
		@DisplayName("Mappt alle Felder korrekt, ID wird ignoriert")
		void toDomain_mapptAlleFelder() {
			final var dto = createRequest();

			assertThat(mapper.toDomain(dto))
					.isNotNull()
					.isInstanceOf(DTOSchulwechselAbgang.class)
					.hasFieldOrPropertyWithValue("id", 0L)
					.hasFieldOrPropertyWithValue("idSchueler", 123L)
					.hasFieldOrPropertyWithValue("status", StatusSchulwechselAbgang.BEVORSTEHEND)
					.hasFieldOrPropertyWithValue("lastModified", "2025-04-12 12:34:00")
					.hasFieldOrPropertyWithValue("idDocument", 456L);
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
			final var entity = createEntity();
			final var request = new SchulwechselAbgangPatchRequest();
			request.idStatus = JsonNullable.of(1);
			request.idDocument = JsonNullable.of(456L);
			request.lastModified = JsonNullable.of("2025-04-12 14:34:00");

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("id", 1L)
					.hasFieldOrPropertyWithValue("idSchueler", 123L)
					.hasFieldOrPropertyWithValue("status", StatusSchulwechselAbgang.BEVORSTEHEND)
					.hasFieldOrPropertyWithValue("lastModified", "2025-04-12 14:34:00")
					.hasFieldOrPropertyWithValue("idDocument", 456L);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesst_undefinedFelder_unveraendert() {
			final var entity = createEntity();
			final var request = new SchulwechselAbgangPatchRequest();
			request.idStatus = JsonNullable.of(1);
			// alle anderen Felder bleiben JsonNullable.undefined()

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("idSchueler", 123L)
					.hasFieldOrPropertyWithValue("status", StatusSchulwechselAbgang.BEVORSTEHEND);
		}

		@Test
		@DisplayName("Setzt Felder auf null wenn JsonNullable.of(null)")
		void patch_setztNullWerte() {
			final var entity = createEntity();
			final var request = new SchulwechselAbgangPatchRequest();
			request.idStatus = JsonNullable.of(1);
			request.lastModified = JsonNullable.of(null);
			request.idDocument = JsonNullable.of(null);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("idSchueler", 123L)
					.hasFieldOrPropertyWithValue("status", StatusSchulwechselAbgang.BEVORSTEHEND)
					.hasFieldOrPropertyWithValue("lastModified", null)
					.hasFieldOrPropertyWithValue("idDocument", null);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var entity = createEntity();
			entity.idDocument = 456L;
			final var request = new SchulwechselAbgangPatchRequest();
			request.idStatus = JsonNullable.of(2);
			request.lastModified = JsonNullable.of("2025-04-12 14:34:00");
			// xmlDocument, createdAt, lastModified bleiben undefined

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("idSchueler", 123L)
					.hasFieldOrPropertyWithValue("status", StatusSchulwechselAbgang.GEPLANT)
					.hasFieldOrPropertyWithValue("lastModified", "2025-04-12 14:34:00")
					.hasFieldOrPropertyWithValue("idDocument", 456L);
		}
	}
}
