package de.svws_nrw.mapper.kurse;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.service.kurse.KurslehrerCreateRequest;
import de.svws_nrw.service.kurse.KurslehrerPatchRequest;

/**
 * Tests für den {@link KurslehrerMapper}.
 */
class KurslehrerMapperTest {

	private final KurslehrerMapper mapper = KurslehrerMapper.INSTANCE;

	@Test
	@DisplayName("toApi | Mappt alle Felder korrekt")
	void toApi_mapsAllFields() {
		final var entity = new DTOKursLehrer(1L, 2L);
		entity.Anteil = 1.5;

		assertThat(mapper.toApi(entity))
				.extracting("idKurs", "idLehrer", "wochenstundenLehrer")
				.containsExactly(1L, 2L, 1.5);
	}

	@Test
	@DisplayName("toApi | Ein nicht gesetzter Anteil wird als 0 Wochenstunden abgebildet")
	void toApi_anteilNullWirdZero() {
		assertThat(mapper.toApi(new DTOKursLehrer(1L, 2L)).wochenstundenLehrer).isZero();
	}

	@Test
	@DisplayName("toDomain | Erstellt die Zuordnung mit der Kurs-ID aus dem Pfad und den Daten des Requests")
	void toDomain_createsEntity() {
		final var request = new KurslehrerCreateRequest();
		request.idLehrer = 2L;
		request.wochenstundenLehrer = 2.0;

		assertThat(mapper.toDomain(1L, request))
				.extracting("Kurs_ID", "Lehrer_ID", "Anteil")
				.containsExactly(1L, 2L, 2.0);
	}

	@Test
	@DisplayName("toDomain | Ohne Wochenstunden bleibt der Anteil nicht gesetzt")
	void toDomain_ohneWochenstunden() {
		final var request = new KurslehrerCreateRequest();
		request.idLehrer = 2L;

		assertThat(mapper.toDomain(1L, request).Anteil).isNull();
	}

	@Test
	@DisplayName("patch | Aktualisiert die Wochenstunden und verändert die IDs nicht")
	void patch_updatesAnteil() {
		final var input = new KurslehrerPatchRequest();
		input.wochenstundenLehrer = JsonNullable.of(4.0);
		final var entity = new DTOKursLehrer(1L, 2L);

		mapper.patch(input, entity);

		assertThat(entity)
				.extracting("Kurs_ID", "Lehrer_ID", "Anteil")
				.containsExactly(1L, 2L, 4.0);
	}

	@Test
	@DisplayName("patch | Lässt undefined Felder unverändert")
	void patch_keepsUndefinedFieldsUnchanged() {
		final var entity = new DTOKursLehrer(1L, 2L);
		entity.Anteil = 3.0;

		mapper.patch(new KurslehrerPatchRequest(), entity);

		assertThat(entity.Anteil).isEqualTo(3.0);
	}

}
