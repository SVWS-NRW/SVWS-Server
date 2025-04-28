package de.svws_nrw.data.schueler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerTelefon;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerTelefon;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataSchuelerTelefon")
@ExtendWith(MockitoExtension.class)
class DataSchuelerTelefonTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchuelerTelefon dataSchuelerTelefon;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		dataSchuelerTelefon = new DataSchuelerTelefon(conn, 1L);
		final DTOSchuelerTelefon dto = getDTOSchuelerTelefon();
		final Long id = 1L;
		final Map<String, Object> initAttributes = new HashMap<>();

		dataSchuelerTelefon.initDTO(dto, id, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", id)
				.hasFieldOrPropertyWithValue("Schueler_ID", 1L)
				.hasFieldOrPropertyWithValue("Telefonnummer", "")
				.hasFieldOrPropertyWithValue("Bemerkung", "")
				.hasFieldOrPropertyWithValue("Sortierung", 32000)
				.hasFieldOrPropertyWithValue("Gesperrt", false);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final DTOSchuelerTelefon dto = getDTOSchuelerTelefon();

		assertThat(dataSchuelerTelefon.map(dto))
				.isInstanceOf(SchuelerTelefon.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idSchueler", 1L)
				.hasFieldOrPropertyWithValue("idTelefonArt", 1L)
				.hasFieldOrPropertyWithValue("telefonnummer", "01795265168")
				.hasFieldOrPropertyWithValue("bemerkung", "")
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("istGesperrt", false);

	}

	@Test
	@DisplayName("getByID | Erfolg")
	void getByIDTest() throws ApiOperationException {
		final var dtoSchuelerTelefon = getDTOSchuelerTelefon();
		when(this.conn.queryByKey(DTOSchuelerTelefon.class, 1L)).thenReturn(dtoSchuelerTelefon);

		assertThat(dataSchuelerTelefon.getById(dtoSchuelerTelefon.ID))
				.isInstanceOf(SchuelerTelefon.class)
				.hasFieldOrPropertyWithValue("id", dtoSchuelerTelefon.ID)
				.hasFieldOrPropertyWithValue("idSchueler", dtoSchuelerTelefon.Schueler_ID)
				.hasFieldOrPropertyWithValue("idTelefonArt", dtoSchuelerTelefon.TelefonArt_ID)
				.hasFieldOrPropertyWithValue("telefonnummer", dtoSchuelerTelefon.Telefonnummer)
				.hasFieldOrPropertyWithValue("bemerkung", dtoSchuelerTelefon.Bemerkung)
				.hasFieldOrPropertyWithValue("sortierung", dtoSchuelerTelefon.Sortierung)
				.hasFieldOrPropertyWithValue("istGesperrt", dtoSchuelerTelefon.Gesperrt);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOSchuelerTelefon();

		final var throwable = Assertions.catchThrowable(() -> this.dataSchuelerTelefon.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "idSchueler" -> assertThat(expectedDTO.Schueler_ID).isEqualTo(value);
			case "idTelefonArt" -> assertThat(expectedDTO.TelefonArt_ID).isEqualTo(value);
			case "telefonnummer" -> assertThat(expectedDTO.Telefonnummer).isEqualTo(value);
			case "bemerkung" -> assertThat(expectedDTO.Bemerkung).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.Sortierung).isEqualTo(value);
			case "istGesperrt" -> assertThat(expectedDTO.Gesperrt).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(key))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 1L),
				arguments("idSchueler", 1L),
				arguments("idTelefonArt", 1L),
				arguments("telefonnummer", "01795265168"),
				arguments("bemerkung", ""),
				arguments("sortierung", 32000),
				arguments("istGesperrt", false)
		);
	}

	private DTOSchuelerTelefon getDTOSchuelerTelefon() {
		final var dtoSchuelerTelefon = new DTOSchuelerTelefon(1L, 1L);
		dtoSchuelerTelefon.ID = 1L;
		dtoSchuelerTelefon.TelefonArt_ID = 1L;
		dtoSchuelerTelefon.Schueler_ID = 1L;
		dtoSchuelerTelefon.Telefonnummer = "01795265168";
		dtoSchuelerTelefon.Bemerkung = "";
		dtoSchuelerTelefon.Sortierung = 32000;
		dtoSchuelerTelefon.Gesperrt = false;
		return dtoSchuelerTelefon;
	}

}
