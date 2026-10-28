package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import de.svws_nrw.api.common.SvwsObjectMapperResolver;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzPatchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Belegt mit dem ObjectMapper der API, dass die Requests unbekannte und nicht patchbare Attribute ablehnen (der Server antwortet dann mit 400)
 * und dass der PatchRequest ein nicht gesetztes Feld von einem expliziten null unterscheidet.
 */
@DisplayName("Ankreuzkompetenz-Requests | JSON")
class AnkreuzkompetenzRequestJsonTest {

	private final ObjectMapper mapper = new SvwsObjectMapperResolver().getContext(Object.class);

	@ParameterizedTest(name = "Create lehnt das Attribut {0} ab")
	@ValueSource(strings = { "id", "referenziertInAnderenTabellen", "jahrgaengezuordnung", "unbekannt" })
	@DisplayName("Create | Unbekannte oder nicht erlaubte Attribute führen zu einem Fehler")
	void create_unbekanntesAttribut(final String attribut) {
		final var json = "{\"istASV\":false,\"idFach\":1,\"floskelText\":\"Text\",\"%s\":1}".formatted(attribut);

		assertThatThrownBy(() -> mapper.readValue(json, AnkreuzkompetenzCreateRequest.class))
				.isInstanceOf(UnrecognizedPropertyException.class);
	}

	@ParameterizedTest(name = "Patch lehnt das Attribut {0} ab")
	@ValueSource(strings = { "id", "referenziertInAnderenTabellen", "jahrgaengezuordnung", "unbekannt" })
	@DisplayName("Patch | Unbekannte oder nicht patchbare Attribute führen zu einem Fehler")
	void patch_unbekanntesAttribut(final String attribut) {
		final var json = "{\"floskelText\":\"Text\",\"%s\":1}".formatted(attribut);

		assertThatThrownBy(() -> mapper.readValue(json, AnkreuzkompetenzPatchRequest.class))
				.isInstanceOf(UnrecognizedPropertyException.class);
	}

	@Test
	@DisplayName("Create | Liest alle Felder mit den Namen des Core-DTOs")
	void create_liestAlleFelder() throws JsonProcessingException {
		final var json = """
				{"istASV":false,"idFach":10,"floskelText":"Text","schulgliederung":"A02","abschnitt":1,
				 "istAktiv":true,"istSichtbar":true,"fachSortierung":5,"sortierung":7}
				""";

		final var result = mapper.readValue(json, AnkreuzkompetenzCreateRequest.class);

		assertThat(result.istASV).isFalse();
		assertThat(result.idFach).isEqualTo(10L);
		assertThat(result.floskelText).isEqualTo("Text");
		assertThat(result.schulgliederung).isEqualTo("A02");
		assertThat(result.abschnitt).isEqualTo(1);
		assertThat(result.istAktiv).isTrue();
		assertThat(result.istSichtbar).isTrue();
		assertThat(result.fachSortierung).isEqualTo(5);
		assertThat(result.sortierung).isEqualTo(7);
	}

	@Test
	@DisplayName("Patch | Unterscheidet ein nicht gesetztes Feld von einem expliziten null")
	void patch_unterscheidetUndefinedUndNull() throws JsonProcessingException {
		final var result = mapper.readValue("{\"idFach\":null,\"floskelText\":\"Text\"}", AnkreuzkompetenzPatchRequest.class);

		assertThat(result.idFach.isPresent()).isTrue();
		assertThat(result.idFach.get()).isNull();
		assertThat(result.floskelText.get()).isEqualTo("Text");
		assertThat(result.schulgliederung.isPresent()).isFalse();
		assertThat(result.istSichtbar.isPresent()).isFalse();
	}

}
