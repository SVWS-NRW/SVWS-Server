package de.svws_nrw.mapper.lehrer.einwilligung;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungPatchRequest;

/**
 * Tests für den {@link LehrerEinwilligungMapper}.
 */
class LehrerEinwilligungMapperTest {

	private final LehrerEinwilligungMapper mapper = LehrerEinwilligungMapper.INSTANCE;

	@Test
	@DisplayName("toApi | Mappt alle Felder korrekt")
	void toApi_mapsAllFields() {
		final var entity = new DTOLehrerDatenschutz(1L, 2L, true, false);

		final var result = mapper.toApi(entity);

		assertThat(result)
				.extracting("idLehrer", "idEinwilligungsart", "istZugestimmt", "istAbgefragt")
				.containsExactly(1L, 2L, true, false);
	}

	@Test
	@DisplayName("toApi | Bildet fehlende Werte (null) als false ab")
	void toApi_nullValuesAreFalse() {
		final var entity = new DTOLehrerDatenschutz(1L, 2L, null, null);

		final var result = mapper.toApi(entity);

		assertThat(result)
				.extracting("istZugestimmt", "istAbgefragt")
				.containsExactly(false, false);
	}

	@Test
	@DisplayName("toDomain | Erstellt eine Einwilligung, die weder erteilt noch abgefragt ist")
	void toDomain_createsEntryWithoutConsent() {
		final var result = mapper.toDomain(3L, 4L);

		assertThat(result)
				.extracting("LehrerID", "DatenschutzID", "Status", "Abgefragt")
				.containsExactly(3L, 4L, false, false);
	}

	@Test
	@DisplayName("patch | Aktualisiert alle Felder mit definierten Werten")
	void patch_updatesAllDefinedFields() {
		final var input = new LehrerEinwilligungPatchRequest();
		input.istZugestimmt = JsonNullable.of(true);
		input.istAbgefragt = JsonNullable.of(true);
		final var entity = new DTOLehrerDatenschutz(1L, 2L, false, false);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("Status", "Abgefragt")
				.containsExactly(true, true);
	}

	@Test
	@DisplayName("patch | Lässt undefined Felder unverändert")
	void patch_keepsUndefinedFieldsUnchanged() {
		final var input = new LehrerEinwilligungPatchRequest();
		input.istAbgefragt = JsonNullable.of(false);
		final var entity = new DTOLehrerDatenschutz(1L, 2L, true, true);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("Status", "Abgefragt")
				.containsExactly(true, false);
	}

	@Test
	@DisplayName("patch | Verändert nicht die ID der Einwilligung")
	void patch_doesNotTouchIds() {
		final var input = new LehrerEinwilligungPatchRequest();
		input.istZugestimmt = JsonNullable.of(true);
		final var entity = new DTOLehrerDatenschutz(1L, 2L, false, false);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("LehrerID", "DatenschutzID")
				.containsExactly(1L, 2L);
	}

}
