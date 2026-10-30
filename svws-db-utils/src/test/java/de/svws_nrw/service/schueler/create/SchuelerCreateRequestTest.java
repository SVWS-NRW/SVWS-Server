package de.svws_nrw.service.schueler.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.svws_nrw.asd.data.schueler.SchuelerNeu;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;

/**
 * Tests für den {@link SchuelerCreateRequest}.
 */
class SchuelerCreateRequestTest {

	private static Set<String> publicFieldNames(final Class<?> clazz) {
		return Arrays.stream(clazz.getFields())
				.filter(f -> !Modifier.isStatic(f.getModifiers()))
				.map(Field::getName)
				.collect(Collectors.toSet());
	}

	@Test
	@DisplayName("Die Feldnamen stimmen mit dem Core-DTO SchuelerNeu überein")
	void feldnamenWieCoreDto() {
		assertThat(publicFieldNames(SchuelerCreateRequest.class)).containsExactlyInAnyOrderElementsOf(publicFieldNames(SchuelerNeu.class));
	}

	@Test
	@DisplayName("Ein JSON gemäß SchuelerNeu wird eingelesen und validiert")
	void jsonGemaessCoreDto() throws Exception {
		final var json = """
				{
					"nachname": "Mustermann", "vorname": "Max", "alleVornamen": "Max Moritz", "geschlecht": 3,
					"geburtsdatum": "2005-10-09", "status": 2, "anmeldedatum": "2024-08-01", "aufnahmedatum": "2024-08-15",
					"beginnBildungsgang": "2024-09-01", "dauerBildungsgang": 3, "idReligion": 11,
					"idSchuljahresabschnitt": 1, "idJahrgang": 4, "idKlasse": 10, "idGrundschuleEinschulungsart": 51
				}
				""";

		final var request = new ObjectMapper().readValue(json, SchuelerCreateRequest.class);

		assertThat(request.nachname).isEqualTo("Mustermann");
		assertThat(request.idSchuljahresabschnitt).isEqualTo(1L);
		assertThat(request.idJahrgang).isEqualTo(4L);
		assertThat(request.idKlasse).isEqualTo(10L);
		assertThat(request.idGrundschuleEinschulungsart).isEqualTo(51L);
		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Unbekannte Felder im JSON werden ignoriert")
	void unbekannteFelder() throws Exception {
		final var request = new ObjectMapper().readValue("{\"nachname\": \"Mustermann\", \"unbekannt\": 1}", SchuelerCreateRequest.class);

		assertThat(request.nachname).isEqualTo("Mustermann");
	}

	@Test
	@DisplayName("Fehlende Pflichtfelder aus dem geerbten Stammdaten-Request werden abgelehnt")
	void pflichtfelderGeerbt() {
		final var request = new SchuelerCreateRequest();

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("nachname")
				.hasMessageContaining("idSchuljahresabschnitt");
	}

}
