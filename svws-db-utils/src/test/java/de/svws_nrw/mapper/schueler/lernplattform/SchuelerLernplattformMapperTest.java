package de.svws_nrw.mapper.schueler.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformPatchRequest;

/**
 * Tests für den {@link SchuelerLernplattformMapper}.
 */
class SchuelerLernplattformMapperTest {

	private final SchuelerLernplattformMapper mapper = SchuelerLernplattformMapper.INSTANCE;

	@Test
	@DisplayName("toApi | Mappt alle Felder inklusive Zugangsdaten korrekt")
	void toApi_mapsAllFieldsWithCredentials() {
		final var entity = new DTOSchuelerLernplattform(1L, 2L, true, false, true, false);
		entity.CredentialID = 5L;
		final var credentials = new DTOCredentialsLernplattformen(5L, 2L, "max.mustermann");
		credentials.Initialkennwort = "Geheim123";

		final var result = mapper.toApi(entity, credentials);

		assertThat(result)
				.extracting("idSchueler", "idLernplattform", "idCredential", "einwilligungAbgefragt", "einwilligungNutzung",
						"einwilligungAudiokonferenz", "einwilligungVideokonferenz", "benutzername", "initialKennwort")
				.containsExactly(1L, 2L, 5L, true, false, true, false, "max.mustermann", "Geheim123");
	}

	@Test
	@DisplayName("toApi | Lässt Credential-ID (0), Benutzername und Initialkennwort leer, wenn keine Zugangsdaten existieren")
	void toApi_withoutCredentials() {
		final var entity = new DTOSchuelerLernplattform(1L, 2L, true, true, true, true);

		final var result = mapper.toApi(entity, null);

		assertThat(result)
				.extracting("idSchueler", "idLernplattform", "idCredential", "benutzername", "initialKennwort")
				.containsExactly(1L, 2L, 0L, null, null);
	}

	@Test
	@DisplayName("toApi | Bildet fehlende Einwilligungen (null) als false ab")
	void toApi_nullEinwilligungenAreFalse() {
		final var entity = new DTOSchuelerLernplattform(1L, 2L, null, null, null, null);

		final var result = mapper.toApi(entity, null);

		assertThat(result)
				.extracting("einwilligungAbgefragt", "einwilligungNutzung", "einwilligungAudiokonferenz", "einwilligungVideokonferenz")
				.containsExactly(false, false, false, false);
	}

	@Test
	@DisplayName("toDomain | Erstellt eine Zuordnung ohne Einwilligungen und ohne Zugangsdaten")
	void toDomain_createsEntryWithoutEinwilligungen() {
		final var result = mapper.toDomain(3L, 4L);

		assertThat(result)
				.extracting("SchuelerID", "LernplattformID", "CredentialID", "EinwilligungAbgefragt", "EinwilligungNutzung",
						"EinwilligungAudiokonferenz", "EinwilligungVideokonferenz")
				.containsExactly(3L, 4L, null, false, false, false, false);
	}

	@Test
	@DisplayName("patch | Aktualisiert alle Felder mit definierten Werten")
	void patch_updatesAllDefinedFields() {
		final var input = new SchuelerLernplattformPatchRequest();
		input.einwilligungAbgefragt = JsonNullable.of(true);
		input.einwilligungNutzung = JsonNullable.of(true);
		input.einwilligungAudiokonferenz = JsonNullable.of(true);
		input.einwilligungVideokonferenz = JsonNullable.of(true);
		final var entity = new DTOSchuelerLernplattform(1L, 2L, false, false, false, false);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("EinwilligungAbgefragt", "EinwilligungNutzung", "EinwilligungAudiokonferenz", "EinwilligungVideokonferenz")
				.containsExactly(true, true, true, true);
	}

	@Test
	@DisplayName("patch | Lässt undefined Felder unverändert")
	void patch_keepsUndefinedFieldsUnchanged() {
		final var input = new SchuelerLernplattformPatchRequest();
		input.einwilligungNutzung = JsonNullable.of(false);
		final var entity = new DTOSchuelerLernplattform(1L, 2L, true, true, true, true);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("EinwilligungAbgefragt", "EinwilligungNutzung", "EinwilligungAudiokonferenz", "EinwilligungVideokonferenz")
				.containsExactly(true, false, true, true);
	}

	@Test
	@DisplayName("patch | Verändert weder die ID der Zuordnung noch die Zugangsdaten")
	void patch_doesNotTouchIdsAndCredential() {
		final var input = new SchuelerLernplattformPatchRequest();
		input.einwilligungAbgefragt = JsonNullable.of(true);
		final var entity = new DTOSchuelerLernplattform(1L, 2L, false, false, false, false);
		entity.CredentialID = 9L;

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("SchuelerID", "LernplattformID", "CredentialID")
				.containsExactly(1L, 2L, 9L);
	}

}
