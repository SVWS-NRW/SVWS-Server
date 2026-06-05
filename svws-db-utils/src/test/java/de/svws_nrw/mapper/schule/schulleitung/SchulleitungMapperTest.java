package de.svws_nrw.mapper.schule.schulleitung;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.service.schule.schulleitung.SchulleitungCreateRequest;
import de.svws_nrw.service.schule.schulleitung.SchulleitungPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

class SchulleitungMapperTest {

	private final SchulleitungMapper mapper = SchulleitungMapper.INSTANCE;

	@Test
	@DisplayName("toApi | Mappt alle Felder korrekt")
	void toApi_mapsAllFields() {
		final var entity = new DTOSchulleitung(1L, 2L, "Schulleitung", 42L);
		entity.Von = "2023-08-01";
		entity.Bis = "2024-07-31";

		final var result = mapper.toApi(entity);

		assertThat(result)
				.extracting("idLeitungsfunktion", "bezeichnung", "idLehrer", "datumBeginnLeitungsfunktion", "datumEndeLeitungsfunktion")
				.containsExactly(2L, "Schulleitung", 42L, "2023-08-01", "2024-07-31");
	}

	@Test
	@DisplayName("toApi | Mappt null-Datumsfelder korrekt")
	void toApi_mapsNullDatesCorrectly() {
		final var entity = new DTOSchulleitung(1L, 1L, "Koordination", 10L);
		entity.Von = null;
		entity.Bis = null;

		final var result = mapper.toApi(entity);

		assertThat(result)
				.extracting("datumBeginnLeitungsfunktion", "datumEndeLeitungsfunktion")
				.containsExactly(null, null);
	}

	// -------------------------------------------------------------------------
	// toDomain
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("toDomain | Mappt alle Felder korrekt")
	void toDomain_mapsAllFields() {
		final var dto = new SchulleitungCreateRequest();
		dto.idLeitungsfunktion = 1L;
		dto.bezeichnung = "Schulleitung";
		dto.idLehrer = 42L;
		dto.datumBeginnLeitungsfunktion = "2023-08-01";
		dto.datumEndeLeitungsfunktion = "2024-07-31";

		final var result = mapper.toDomain(dto);

		assertThat(result)
				.extracting("LeitungsfunktionID", "Funktionstext", "LehrerID", "Von", "Bis")
				.containsExactly(1L, "Schulleitung", 42L, "2023-08-01", "2024-07-31");
	}

	@Test
	@DisplayName("toDomain | ID-Feld wird ignoriert")
	void toDomain_ignoresId() {
		final var dto = new SchulleitungCreateRequest();
		dto.idLeitungsfunktion = 1L;
		dto.bezeichnung = "Schulleitung";
		dto.idLehrer = 42L;

		final var result = mapper.toDomain(dto);

		assertThat(result.ID).isZero();
	}

	@Test
	@DisplayName("toDomain | Mappt null-Datumsfelder korrekt")
	void toDomain_mapsNullDatesCorrectly() {
		final var dto = new SchulleitungCreateRequest();
		dto.idLeitungsfunktion = 2L;
		dto.bezeichnung = "Koordination";
		dto.idLehrer = 10L;
		dto.datumBeginnLeitungsfunktion = null;
		dto.datumEndeLeitungsfunktion = null;

		final var result = mapper.toDomain(dto);

		assertThat(result)
				.extracting("Von", "Bis")
				.containsExactly(null, null);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Aktualisiert alle patch-fähigen Felder mit definierten Werten")
	void patch_updatesAllDefinedFields() {
		final var dto = new SchulleitungPatchRequest();
		dto.idLeitungsfunktion = JsonNullable.of(3L);
		dto.bezeichnung = JsonNullable.of("Schulverwaltung");
		dto.datumBeginnLeitungsfunktion = JsonNullable.of("2024-01-01");
		dto.datumEndeLeitungsfunktion = JsonNullable.of("2024-12-31");

		final var entity = new DTOSchulleitung(1L, 1L, "Schulleitung", 42L);
		entity.Von = "2023-08-01";
		entity.Bis = "2024-07-31";

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("LeitungsfunktionID", "Funktionstext", "LehrerID", "Von", "Bis")
				.containsExactly(3L, "Schulverwaltung", 42L, "2024-01-01", "2024-12-31");
	}

	@Test
	@DisplayName("patch | Lässt undefined Felder unverändert")
	void patch_keepsUndefinedFieldsUnchanged() {
		final var dto = new SchulleitungPatchRequest();
		dto.idLeitungsfunktion = JsonNullable.undefined();
		dto.bezeichnung = JsonNullable.undefined();
		dto.datumBeginnLeitungsfunktion = JsonNullable.undefined();
		dto.datumEndeLeitungsfunktion = JsonNullable.undefined();

		final var entity = new DTOSchulleitung(1L, 1L, "Schulleitung", 42L);
		entity.Von = "2023-08-01";
		entity.Bis = "2024-07-31";

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("LeitungsfunktionID", "Funktionstext", "LehrerID", "Von", "Bis")
				.containsExactly(1L, "Schulleitung", 42L, "2023-08-01", "2024-07-31");
	}

	@Test
	@DisplayName("patch | ID-Feld wird nicht verändert")
	void patch_doesNotChangeId() {
		final var dto = new SchulleitungPatchRequest();
		dto.idLeitungsfunktion = JsonNullable.of(2L);
		dto.bezeichnung = JsonNullable.of("Koordination");

		final var entity = new DTOSchulleitung(99L, 1L, "Schulleitung", 42L);

		mapper.patch(dto, entity);

		assertThat(entity.ID).isEqualTo(99L);
	}

	@Test
	@DisplayName("patch | idLehrer wird nicht verändert")
	void patch_doesNotChangeDdLehrer() {
		final var dto = new SchulleitungPatchRequest();
		dto.bezeichnung = JsonNullable.of("Koordination");

		final var entity = new DTOSchulleitung(1L, 1L, "Schulleitung", 42L);

		mapper.patch(dto, entity);

		assertThat(entity.LehrerID).isEqualTo(42L);
	}

	@Test
	@DisplayName("patch | Setzt null-Werte korrekt")
	void patch_setsNullValues() {
		final var dto = new SchulleitungPatchRequest();
		dto.datumBeginnLeitungsfunktion = JsonNullable.of(null);
		dto.datumEndeLeitungsfunktion = JsonNullable.of(null);

		final var entity = new DTOSchulleitung(1L, 1L, "Schulleitung", 42L);
		entity.Von = "2023-08-01";
		entity.Bis = "2024-07-31";

		mapper.patch(dto, entity);

		assertThat(entity)
				.extracting("Von", "Bis")
				.containsExactly(null, null);
	}
}
