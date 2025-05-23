package de.svws_nrw.data.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private DataKatalogKindergaerten katalogKindergaerten;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		katalogKindergaerten = new DataKatalogKindergaerten(conn);
		final DTOKindergarten dto = getDTOKindergarten();
		final long id = 1L;
		final Map<String, Object> initAttributes = new HashMap<>();

		katalogKindergaerten.initDTO(dto, id, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", id)
				.hasFieldOrPropertyWithValue("Bezeichnung", "")
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final DTOKindergarten dto = getDTOKindergarten();

		assertThat(katalogKindergaerten.map(dto))
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
		final DTOKindergarten dto1 = getDTOKindergarten();
		final DTOKindergarten dto2 = getDTOKindergarten();
		dto2.ID = 2L;
		dto2.Bezeichnung = "Kita Sonnenschein 2";

		final List<DTOKindergarten> dtoList = new ArrayList<>();
		dtoList.add(dto1);
		dtoList.add(dto2);

		when(conn.queryAll(DTOKindergarten.class)).thenReturn(dtoList);

		final List<Kindergarten> result = katalogKindergaerten.getAll();
		final Kindergarten expectedDto1 = result.stream().filter(lFirst -> lFirst.id == dto1.ID).findFirst().orElse(null);
		final Kindergarten expectedDto2 = result.stream().filter(lSecond -> lSecond.id == dto2.ID).findFirst().orElse(null);

		assertThat(expectedDto1)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", dto1.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto1.Bezeichnung)
				.hasFieldOrPropertyWithValue("plz", dto1.PLZ)
				.hasFieldOrPropertyWithValue("ort", dto1.Ort)
				.hasFieldOrPropertyWithValue("strassenname", dto1.Strassenname)
				.hasFieldOrPropertyWithValue("hausNr", dto1.HausNr)
				.hasFieldOrPropertyWithValue("hausNrZusatz", dto1.HausNrZusatz)
				.hasFieldOrPropertyWithValue("tel", dto1.Tel)
				.hasFieldOrPropertyWithValue("email", dto1.Email)
				.hasFieldOrPropertyWithValue("bemerkung", dto1.Bemerkung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto1.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto1.Sortierung);

		assertThat(expectedDto2)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", dto2.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto2.Bezeichnung)
				.hasFieldOrPropertyWithValue("plz", dto2.PLZ)
				.hasFieldOrPropertyWithValue("ort", dto2.Ort)
				.hasFieldOrPropertyWithValue("strassenname", dto2.Strassenname)
				.hasFieldOrPropertyWithValue("hausNr", dto2.HausNr)
				.hasFieldOrPropertyWithValue("hausNrZusatz", dto2.HausNrZusatz)
				.hasFieldOrPropertyWithValue("tel", dto2.Tel)
				.hasFieldOrPropertyWithValue("email", dto2.Email)
				.hasFieldOrPropertyWithValue("bemerkung", dto2.Bemerkung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto2.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto2.Sortierung);
	}

	@Test
	@DisplayName("getById | TelefonArt null")
	void getByIdTest_notFound() {
		when(conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(null);
		final var throwable = catchThrowable(() -> katalogKindergaerten.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Der Kindergarten mit der ID 1 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("getById")
	void getByIdTest() throws ApiOperationException {
		final DTOKindergarten dto = getDTOKindergarten();
		when(conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);

		assertThat(katalogKindergaerten.getById(dto.ID))
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

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOKindergarten();
		final Map<String, Object> map = new HashMap<>();
		final var throwable = Assertions.catchThrowable(() -> this.katalogKindergaerten.mapAttribute(expectedDTO, key, value, map));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
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
					.hasMessageStartingWith("Die Daten des Patches enthalten ein unbekanntes Attribut.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 1L),
				arguments("bezeichnung", "Kita Sonnenschein"),
				arguments("plz", "12345"),
				arguments("ort", "Musterort"),
				arguments("strassenname", "Musterstraße"),
				arguments("hausNr", "12"),
				arguments("hausNrZusatz", "a"),
				arguments("tel", "01148523516"),
				arguments("email", "kita@sonnenschein.de"),
				arguments("bemerkung", "Ist geschlossen"),
				arguments("istSichtbar", true),
				arguments("sortierung", 1)
		);
	}

	private DTOKindergarten getDTOKindergarten() {
		final var dtoKindergarten = new DTOKindergarten(1L);
		dtoKindergarten.ID = 1L;
		dtoKindergarten.Bezeichnung = "Kita Sonnenschein";
		dtoKindergarten.PLZ = "12345";
		dtoKindergarten.Ort = "Musterort";
		dtoKindergarten.Strassenname = "Musterstraße";
		dtoKindergarten.HausNr = "12";
		dtoKindergarten.HausNrZusatz = "a";
		dtoKindergarten.Tel = "01148523516";
		dtoKindergarten.Email = "kita@sonnenschein.de";
		dtoKindergarten.Bemerkung = "Ist geschlossen";
		dtoKindergarten.Sichtbar = true;
		dtoKindergarten.Sortierung = 1;
		return dtoKindergarten;
	}
}
