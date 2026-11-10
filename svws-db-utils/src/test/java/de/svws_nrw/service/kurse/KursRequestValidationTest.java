package de.svws_nrw.service.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.jackson.nullable.JsonNullableModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;

/**
 * Testklasse für die Validierung der {@link KursCreateRequest} und {@link KursPatchRequest}
 */
@DisplayName("Tests für die Validierung der Kurs-Requests")
class KursRequestValidationTest {

	private static KursCreateRequest gueltigerCreateRequest() {
		final var request = new KursCreateRequest();
		request.idSchuljahresabschnitt = 10L;
		request.kuerzel = "D-GK1";
		request.idFach = 3L;
		request.kursartAllg = "GK";
		return request;
	}

	@Test
	@DisplayName("Create: Ein Request mit Pflichtfeldern ist gültig")
	void create_gueltig() {
		assertThatCode(() -> BeanValidator.validate(gueltigerCreateRequest())).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Create: Fehlende Pflichtfelder werden abgelehnt")
	void create_pflichtfelderFehlen() {
		final var request = new KursCreateRequest();

		assertThatThrownBy(() -> BeanValidator.validate(request)).isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("idSchuljahresabschnitt")
				.hasMessageContaining("kuerzel")
				.hasMessageContaining("idFach")
				.hasMessageContaining("kursartAllg");
	}

	@Test
	@DisplayName("Create: Längen und Bereiche werden geprüft")
	void create_laengenUndBereiche() {
		final var request = gueltigerCreateRequest();
		request.kuerzel = "";
		request.sortierung = -1;
		request.wochenstunden = 41;
		request.idKursFortschreibungsart = 4;
		request.schulnummer = 99999;
		request.schienen = List.of(-1);

		assertThatThrownBy(() -> BeanValidator.validate(request)).isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("kuerzel")
				.hasMessageContaining("sortierung")
				.hasMessageContaining("wochenstunden")
				.hasMessageContaining("idKursFortschreibungsart")
				.hasMessageContaining("schulnummer")
				.hasMessageContaining("schienen");
	}

	@Test
	@DisplayName("Create: Die größte Fortschreibungsart-ID 3 ist gültig")
	void create_fortschreibungsartObergrenze() {
		final var request = gueltigerCreateRequest();
		request.idKursFortschreibungsart = 3;

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Patch: Die größte Fortschreibungsart-ID 3 ist gültig")
	void patch_fortschreibungsartObergrenze() {
		final var request = new KursPatchRequest();
		request.idKursFortschreibungsart = JsonNullable.of(3);

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Patch: Ein leerer Request ist gültig")
	void patch_leer() {
		assertThatCode(() -> BeanValidator.validate(new KursPatchRequest())).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Patch: null ist bei Lehrer, Schulnummer, Zeugnisbezeichnung und Lehrer-Wochenstunden erlaubt")
	void patch_nullErlaubt() {
		final var request = new KursPatchRequest();
		request.idLehrer = JsonNullable.of(null);
		request.schulnummer = JsonNullable.of(null);
		request.bezeichnungZeugnis = JsonNullable.of(null);
		request.wochenstundenLehrer = JsonNullable.of(null);

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Patch: null wird bei allen anderen Attributen abgelehnt")
	void patch_nullNichtErlaubt() {
		final var request = new KursPatchRequest();
		request.kuerzel = JsonNullable.of(null);
		request.kursartAllg = JsonNullable.of(null);
		request.idFach = JsonNullable.of(null);
		request.sortierung = JsonNullable.of(null);
		request.istSichtbar = JsonNullable.of(null);
		request.wochenstunden = JsonNullable.of(null);
		request.istEpochalunterricht = JsonNullable.of(null);
		request.idKursFortschreibungsart = JsonNullable.of(null);
		request.schienen = JsonNullable.of(null);
		request.idJahrgaenge = JsonNullable.of(null);

		assertThatThrownBy(() -> BeanValidator.validate(request)).isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("kuerzel")
				.hasMessageContaining("kursartAllg")
				.hasMessageContaining("idFach")
				.hasMessageContaining("sortierung")
				.hasMessageContaining("istSichtbar")
				.hasMessageContaining("wochenstunden")
				.hasMessageContaining("istEpochalunterricht")
				.hasMessageContaining("idKursFortschreibungsart")
				.hasMessageContaining("schienen")
				.hasMessageContaining("idJahrgaenge");
	}

	@Test
	@DisplayName("Patch: Längen und Bereiche werden geprüft")
	void patch_laengenUndBereiche() {
		final var request = new KursPatchRequest();
		request.kuerzel = JsonNullable.of("");
		request.sortierung = JsonNullable.of(-1);
		request.wochenstunden = JsonNullable.of(41);
		request.idKursFortschreibungsart = JsonNullable.of(4);
		request.schulnummer = JsonNullable.of(1000000);
		request.schienen = JsonNullable.of(List.of(-1));

		assertThatThrownBy(() -> BeanValidator.validate(request)).isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("kuerzel")
				.hasMessageContaining("sortierung")
				.hasMessageContaining("wochenstunden")
				.hasMessageContaining("idKursFortschreibungsart")
				.hasMessageContaining("schulnummer")
				.hasMessageContaining("schienen");
	}

	@Test
	@DisplayName("Patch: Die Attribute id und idSchuljahresabschnitt werden beim Einlesen abgelehnt")
	void patch_idUndAbschnittNichtPatchbar() {
		final var mapper = new ObjectMapper().findAndRegisterModules();
		mapper.registerModule(new JsonNullableModule());

		assertThat(List.of("{\"id\": 99}", "{\"idSchuljahresabschnitt\": 99}"))
				.allSatisfy(json -> assertThatThrownBy(() -> mapper.readValue(json, KursPatchRequest.class))
						.isInstanceOf(UnrecognizedPropertyException.class));
	}

}
