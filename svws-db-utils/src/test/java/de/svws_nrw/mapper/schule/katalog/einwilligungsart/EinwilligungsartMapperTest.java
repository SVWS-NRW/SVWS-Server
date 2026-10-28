package de.svws_nrw.mapper.schule.katalog.einwilligungsart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.dto.current.schild.katalog.DTOEinwilligungsart;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.einwilligungsart.EinwilligungsartPatchRequest;

/**
 * Tests für den {@link EinwilligungsartMapper}.
 */
class EinwilligungsartMapperTest {

	private final EinwilligungsartMapper mapper = EinwilligungsartMapper.INSTANCE;

	private static DTOEinwilligungsart entity() {
		final DTOEinwilligungsart entity = new DTOEinwilligungsart(42L, "Verwendung Foto", true, 5);
		entity.schluessel = "FOTO";
		entity.beschreibung = "Einwilligung zur Verwendung von Fotos";
		entity.personTyp = PersonTyp.SCHUELER;
		return entity;
	}

	@Test
	@DisplayName("toApi | Mappt alle Felder und setzt das Referenz-Flag nicht")
	void toApi_mapsFields() {
		final var result = mapper.toApi(entity());

		assertThat(result.id).isEqualTo(42L);
		assertThat(result.bezeichnung).isEqualTo("Verwendung Foto");
		assertThat(result.schluessel).isEqualTo("FOTO");
		assertThat(result.beschreibung).isEqualTo("Einwilligung zur Verwendung von Fotos");
		assertThat(result.idPersonTyp).isEqualTo(PersonTyp.SCHUELER.id);
		assertThat(result.sortierung).isEqualTo(5);
		assertThat(result.istSichtbar).isTrue();
		assertThat(result.referenziertInAnderenTabellen).isFalse();
	}

	@Test
	@DisplayName("toApi | Bildet fehlende Werte als null und einen fehlenden Personentyp als idPersonTyp -1 ab")
	void toApi_mapsMissingValues() {
		final DTOEinwilligungsart entity = entity();
		entity.schluessel = null;
		entity.beschreibung = null;
		entity.personTyp = null;

		final var result = mapper.toApi(entity);

		assertThat(result.schluessel).isNull();
		assertThat(result.beschreibung).isNull();
		assertThat(result.idPersonTyp).isEqualTo(-1);
	}

	@Test
	@DisplayName("toDomain | Mappt die Felder des Requests und lässt ID und Personentyp unberührt")
	void toDomain_mapsFields() {
		final var dto = new EinwilligungsartCreateRequest();
		dto.bezeichnung = "Verwendung Foto";
		dto.idPersonTyp = PersonTyp.LEHRER.id;
		dto.schluessel = "FOTO";
		dto.beschreibung = "Beschreibung";
		dto.sortierung = 7;
		dto.istSichtbar = false;

		final var result = mapper.toDomain(dto);

		assertThat(result.id).isZero();
		assertThat(result.bezeichnung).isEqualTo("Verwendung Foto");
		assertThat(result.personTyp).isNull();
		assertThat(result.schluessel).isEqualTo("FOTO");
		assertThat(result.beschreibung).isEqualTo("Beschreibung");
		assertThat(result.sortierung).isEqualTo(7);
		assertThat(result.istSichtbar).isFalse();
	}

	@Test
	@DisplayName("toDomain | Verwendet ohne Angabe die Standardwerte für Sortierung und Sichtbarkeit")
	void toDomain_usesDefaults() {
		final var dto = new EinwilligungsartCreateRequest();
		dto.bezeichnung = "Verwendung Foto";
		dto.idPersonTyp = PersonTyp.SCHUELER.id;

		final var result = mapper.toDomain(dto);

		assertThat(result.sortierung).isEqualTo(32000);
		assertThat(result.istSichtbar).isTrue();
		assertThat(result.schluessel).isNull();
		assertThat(result.beschreibung).isNull();
	}

	@Test
	@DisplayName("toDomain | Bildet einen leeren Schlüssel als null ab")
	void toDomain_blankSchluesselToNull() {
		final var dto = new EinwilligungsartCreateRequest();
		dto.bezeichnung = "Verwendung Foto";
		dto.idPersonTyp = PersonTyp.SCHUELER.id;
		dto.schluessel = " ";

		assertThat(mapper.toDomain(dto).schluessel).isNull();
	}

	@Test
	@DisplayName("patch | Aktualisiert alle im Request definierten Felder")
	void patch_updatesDefinedFields() {
		final var dto = new EinwilligungsartPatchRequest();
		dto.bezeichnung = JsonNullable.of("Neu");
		dto.schluessel = JsonNullable.of("NEU");
		dto.beschreibung = JsonNullable.of("Neue Beschreibung");
		dto.sortierung = JsonNullable.of(9);
		dto.istSichtbar = JsonNullable.of(false);
		final DTOEinwilligungsart entity = entity();

		mapper.patch(dto, entity);

		assertThat(entity.id).isEqualTo(42L);
		assertThat(entity.personTyp).isEqualTo(PersonTyp.SCHUELER);
		assertThat(entity.bezeichnung).isEqualTo("Neu");
		assertThat(entity.schluessel).isEqualTo("NEU");
		assertThat(entity.beschreibung).isEqualTo("Neue Beschreibung");
		assertThat(entity.sortierung).isEqualTo(9);
		assertThat(entity.istSichtbar).isFalse();
	}

	@Test
	@DisplayName("patch | Lässt alle Felder unverändert, wenn sie nicht definiert sind")
	void patch_ignoresUndefinedFields() {
		final var dto = new EinwilligungsartPatchRequest();
		final DTOEinwilligungsart entity = entity();

		mapper.patch(dto, entity);

		assertThat(entity.bezeichnung).isEqualTo("Verwendung Foto");
		assertThat(entity.schluessel).isEqualTo("FOTO");
		assertThat(entity.beschreibung).isEqualTo("Einwilligung zur Verwendung von Fotos");
		assertThat(entity.sortierung).isEqualTo(5);
		assertThat(entity.istSichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | Entfernt den Schlüssel, wenn er leer ist")
	void patch_blankSchluesselToNull() {
		final var dto = new EinwilligungsartPatchRequest();
		dto.schluessel = JsonNullable.of("");
		final DTOEinwilligungsart entity = entity();

		mapper.patch(dto, entity);

		assertThat(entity.schluessel).isNull();
	}

	@Test
	@DisplayName("patch | Entfernt den Schlüssel, wenn er explizit null ist")
	void patch_nullSchluesselToNull() {
		final var dto = new EinwilligungsartPatchRequest();
		dto.schluessel = JsonNullable.of(null);
		final DTOEinwilligungsart entity = entity();

		mapper.patch(dto, entity);

		assertThat(entity.schluessel).isNull();
	}

}
