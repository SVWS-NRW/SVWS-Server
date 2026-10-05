package de.svws_nrw.mapper.schulwechsel.dokument;

import de.svws_nrw.core.data.schule.SchulwechselDokument;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselDokument;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentCreateRequest;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class SchulwechselDokumentMapperTest {

	private final SchulwechselDokumentMapper mapper = SchulwechselDokumentMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOSchulwechselDokument createEntity() {
		return new DTOSchulwechselDokument(1L, "test.xml", "<xml/>", "2025-01-01", "2025-06-01");
	}

	private SchulwechselDokumentCreateRequest createRequest() {
		final var dto = new SchulwechselDokumentCreateRequest();
		dto.fileName = "schulwechsel.xml";
		dto.xmlDocument = "<xml/>";
		dto.createdAt = "2025-01-01";
		dto.lastModified = "2025-06-01";
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

			assertThat(mapper.toApi(entity))
					.hasFieldOrPropertyWithValue("id", 1L)
					.hasFieldOrPropertyWithValue("fileName", "test.xml")
					.hasFieldOrPropertyWithValue("xmlDocument", "<xml/>")
					.hasFieldOrPropertyWithValue("createdAt", "2025-01-01")
					.hasFieldOrPropertyWithValue("lastModified", "2025-06-01");
		}

		@Test
		@DisplayName("Liefert nicht-null Ergebnis")
		void toApi_ergebnisNichtNull() {
			assertThat(mapper.toApi(createEntity()))
					.isNotNull()
					.isInstanceOf(SchulwechselDokument.class);
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
					.isInstanceOf(DTOSchulwechselDokument.class)
					.hasFieldOrPropertyWithValue("fileName", "schulwechsel.xml")
					.hasFieldOrPropertyWithValue("xmlDocument", "<xml/>")
					.hasFieldOrPropertyWithValue("createdAt", "2025-01-01")
					.hasFieldOrPropertyWithValue("lastModified", "2025-06-01");
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
			final var request = new SchulwechselDokumentPatchRequest();
			request.fileName = JsonNullable.of("neu.xml");
			request.xmlDocument = JsonNullable.of("<neu/>");
			request.createdAt = JsonNullable.of("2024-01-01");
			request.lastModified = JsonNullable.of("2024-06-01");

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("id", 1L)
					.hasFieldOrPropertyWithValue("fileName", "neu.xml")
					.hasFieldOrPropertyWithValue("xmlDocument", "<neu/>")
					.hasFieldOrPropertyWithValue("createdAt", "2024-01-01")
					.hasFieldOrPropertyWithValue("lastModified", "2024-06-01");
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_laesst_undefinedFelder_unveraendert() {
			final var entity = createEntity();
			final var request = new SchulwechselDokumentPatchRequest();
			// alle Felder bleiben JsonNullable.undefined()

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("fileName", "test.xml")
					.hasFieldOrPropertyWithValue("xmlDocument", "<xml/>")
					.hasFieldOrPropertyWithValue("createdAt", "2025-01-01")
					.hasFieldOrPropertyWithValue("lastModified", "2025-06-01");
		}

		@Test
		@DisplayName("Setzt Felder auf null wenn JsonNullable.of(null)")
		void patch_setztNullWerte() {
			final var entity = createEntity();
			final var request = new SchulwechselDokumentPatchRequest();
			request.fileName = JsonNullable.of(null);
			request.xmlDocument = JsonNullable.of(null);

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("fileName", null)
					.hasFieldOrPropertyWithValue("xmlDocument", null)
					.hasFieldOrPropertyWithValue("createdAt", "2025-01-01")
					.hasFieldOrPropertyWithValue("lastModified", "2025-06-01");
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var entity = createEntity();
			final var request = new SchulwechselDokumentPatchRequest();
			request.fileName = JsonNullable.of("geaendert.xml");
			// xmlDocument, createdAt, lastModified bleiben undefined

			mapper.patch(request, entity);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("fileName", "geaendert.xml")
					.hasFieldOrPropertyWithValue("xmlDocument", "<xml/>")
					.hasFieldOrPropertyWithValue("createdAt", "2025-01-01")
					.hasFieldOrPropertyWithValue("lastModified", "2025-06-01");
		}
	}
}
