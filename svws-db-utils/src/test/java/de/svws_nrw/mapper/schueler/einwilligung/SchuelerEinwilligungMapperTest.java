package de.svws_nrw.mapper.schueler.einwilligung;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungPatchRequest;

/**
 * Tests für den {@link SchuelerEinwilligungMapper}.
 */
class SchuelerEinwilligungMapperTest {

	private final SchuelerEinwilligungMapper mapper = SchuelerEinwilligungMapper.INSTANCE;

	@Test
	@DisplayName("toApi | Mappt alle Felder korrekt")
	void toApi_mapsAllFields() {
		final var entity = new DTOSchuelerDatenschutz(1L, 2L, true, false);

		final var result = mapper.toApi(entity);

		assertThat(result)
				.extracting("idSchueler", "idEinwilligungsart", "status", "abgefragt")
				.containsExactly(1L, 2L, true, false);
	}

	@Test
	@DisplayName("toApi | Bildet fehlende Werte (null) als false ab")
	void toApi_nullValuesAreFalse() {
		// Der Konstruktor lässt für Status kein null zu, daher werden die Werte nachträglich auf null gesetzt
		final var entity = new DTOSchuelerDatenschutz(1L, 2L, true, true);
		entity.Status = null;
		entity.Abgefragt = null;

		final var result = mapper.toApi(entity);

		assertThat(result)
				.extracting("status", "abgefragt")
				.containsExactly(false, false);
	}

	@Test
	@DisplayName("toDomain | Erstellt eine Einwilligung, die weder erteilt noch abgefragt ist")
	void toDomain_createsEntryWithoutConsent() {
		final var result = mapper.toDomain(3L, 4L);

		assertThat(result)
				.extracting("Schueler_ID", "Datenschutz_ID", "Status", "Abgefragt")
				.containsExactly(3L, 4L, false, false);
	}

	@Test
	@DisplayName("patch | Aktualisiert alle Felder mit definierten Werten")
	void patch_updatesAllDefinedFields() {
		final var input = new SchuelerEinwilligungPatchRequest();
		input.status = JsonNullable.of(true);
		input.abgefragt = JsonNullable.of(true);
		final var entity = new DTOSchuelerDatenschutz(1L, 2L, false, false);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("Status", "Abgefragt")
				.containsExactly(true, true);
	}

	@Test
	@DisplayName("patch | Lässt undefined Felder unverändert")
	void patch_keepsUndefinedFieldsUnchanged() {
		final var input = new SchuelerEinwilligungPatchRequest();
		input.abgefragt = JsonNullable.of(false);
		final var entity = new DTOSchuelerDatenschutz(1L, 2L, true, true);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("Status", "Abgefragt")
				.containsExactly(true, false);
	}

	@Test
	@DisplayName("patch | Verändert nicht die ID der Einwilligung")
	void patch_doesNotTouchIds() {
		final var input = new SchuelerEinwilligungPatchRequest();
		input.status = JsonNullable.of(true);
		final var entity = new DTOSchuelerDatenschutz(1L, 2L, false, false);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("Schueler_ID", "Datenschutz_ID")
				.containsExactly(1L, 2L);
	}

}
