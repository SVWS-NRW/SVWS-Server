package de.svws_nrw.data.schule;

import java.util.List;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Kindergarten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOKindergarten;
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
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataKatalogKindergaerten")
@ExtendWith(MockitoExtension.class)
class DataKatalogKindergaertenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKatalogKindergaerten data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		final var dto = new DTOKindergarten(1L);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto).hasFieldOrPropertyWithValue("ID", 2L);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final DTOKindergarten dto = getDTOKindergarten();

		assertThat(this.data.map(dto))
				.isInstanceOf(Kindergarten.class)
				.hasFieldOrPropertyWithValue("bezeichnung", "Kita Sonnenschein")
				.hasFieldOrPropertyWithValue("plz", "12345")
				.hasFieldOrPropertyWithValue("ort", "Musterort")
				.hasFieldOrPropertyWithValue("strassenname", "Musterstraße")
				.hasFieldOrPropertyWithValue("hausNr", "12")
				.hasFieldOrPropertyWithValue("hausNrZusatz", "a")
				.hasFieldOrPropertyWithValue("tel", "01148523516")
				.hasFieldOrPropertyWithValue("email", "kita@sonnenschein.de")
				.hasFieldOrPropertyWithValue("bemerkung", "Ist geschlossen")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 1);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() throws ApiOperationException {
		final var dto1 = new DTOKindergarten(1L);
		final var dto2 = new DTOKindergarten(2L);

		when(this.conn.queryAll(DTOKindergarten.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2)
				.allSatisfy(v -> assertThat(dto1)
						.isInstanceOf(DTOKindergarten.class)
						.hasFieldOrProperty("ID"));
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = Assertions.catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für den Kindergarten darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getById | TelefonArt null")
	void getByIdTest_notFound() {
		when(conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(null);
		final var throwable = catchThrowable(() -> this.data.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Es wurde kein Kindergarten mit der ID 1 gefunden.");
	}

	@Test
	@DisplayName("getById")
	void getByIdTest() throws ApiOperationException {
		final var dto = getDTOKindergarten();
		when(conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);

		assertThat(data.getById(dto.ID))
				.isNotNull()
				.hasFieldOrPropertyWithValue("bezeichnung", "Kita Sonnenschein")
				.hasFieldOrPropertyWithValue("plz", "12345")
				.hasFieldOrPropertyWithValue("ort", "Musterort")
				.hasFieldOrPropertyWithValue("strassenname", "Musterstraße")
				.hasFieldOrPropertyWithValue("hausNr", "12")
				.hasFieldOrPropertyWithValue("hausNrZusatz", "a")
				.hasFieldOrPropertyWithValue("tel", "01148523516")
				.hasFieldOrPropertyWithValue("email", "kita@sonnenschein.de")
				.hasFieldOrPropertyWithValue("bemerkung", "Ist geschlossen")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 1);
	}

	@Test
	@DisplayName("mapAttribute | tel muss dem erlaubten Format entsprechen")
	void mapAttributeTest_telFehlerhaft() {
		final var kindergarten = new DTOKindergarten(1L);

		final var throwable = catchThrowable(() -> this.data.mapAttribute(kindergarten, "tel", "abc", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Telefonnummer abc entspricht nicht dem erlaubten Format.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOKindergarten();
		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "bezeichnung" -> assertThat(expectedDTO.Bezeichnung).isEqualTo(value);
			case "plz" -> assertThat(expectedDTO.PLZ).isEqualTo(value);
			case "ort" -> assertThat(expectedDTO.Ort).isEqualTo(value);
			case "strassenname" -> assertThat(expectedDTO.Strassenname).isEqualTo(value);
			case "hausNr" -> assertThat(expectedDTO.HausNr).isEqualTo(value);
			case "hausNrZusatz" -> assertThat(expectedDTO.HausNrZusatz).isEqualTo(value);
			case "tel" -> assertThat(expectedDTO.Tel).isEqualTo(value);
			case "email" -> assertThat(expectedDTO.Email).isEqualTo(value);
			case "bemerkung" -> assertThat(expectedDTO.Bemerkung).isEqualTo(value);
			case "istSichtbar" -> assertThat(expectedDTO.Sichtbar).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.Sortierung).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35),
				arguments("bezeichnung", "Kita Pusteblume"),
				arguments("plz", "12345"),
				arguments("ort", "Musterort"),
				arguments("strassenname", "Musterstraße"),
				arguments("hausNr", "12"),
				arguments("hausNrZusatz", "a"),
				arguments("tel", "01148523516"),
				arguments("email", "kita@sonnenschein.de"),
				arguments("bemerkung", "Ist geschlossen"),
				arguments("istSichtbar", true),
				arguments("sortierung", 1),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	private DTOKindergarten getDTOKindergarten() {
		final var dto = new DTOKindergarten(1L);
		dto.ID = 1L;
		dto.Bezeichnung = "Kita Sonnenschein";
		dto.PLZ = "12345";
		dto.Ort = "Musterort";
		dto.Strassenname = "Musterstraße";
		dto.HausNr = "12";
		dto.HausNrZusatz = "a";
		dto.Tel = "01148523516";
		dto.Email = "kita@sonnenschein.de";
		dto.Bemerkung = "Ist geschlossen";
		dto.Sichtbar = true;
		dto.Sortierung = 1;
		return dto;
	}
}
