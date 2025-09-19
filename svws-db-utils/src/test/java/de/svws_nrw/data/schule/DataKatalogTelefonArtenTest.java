package de.svws_nrw.data.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.TelefonArt;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOTelefonArt;
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

@DisplayName("Diese Testklasse testet die Klasse DataKatalogTelefonArten")
@ExtendWith(MockitoExtension.class)
class DataKatalogTelefonArtenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKatalogTelefonArten data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		data = new DataKatalogTelefonArten(conn);
		final DTOTelefonArt dto = getDTOTelefonArt();
		final long id = 1L;
		final Map<String, Object> initAttributes = new HashMap<>();

		data.initDTO(dto, id, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", id)
				.hasFieldOrPropertyWithValue("Bezeichnung", "")
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final DTOTelefonArt dto = getDTOTelefonArt();

		assertThat(data.map(dto))
				.isInstanceOf(TelefonArt.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Mobilnummer")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("anzahlTelefonnummern", 0);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() throws ApiOperationException {
		final DTOTelefonArt dto1 = getDTOTelefonArt();
		final DTOTelefonArt dto2 = getDTOTelefonArt();
		dto2.ID = 2L;
		dto2.Bezeichnung = "Testbezeichnung2";

		final List<DTOTelefonArt> dtoList = new ArrayList<>();
		dtoList.add(dto1);
		dtoList.add(dto2);

		when(conn.queryAll(DTOTelefonArt.class)).thenReturn(dtoList);

		final List<TelefonArt> result = data.getAll();
		final TelefonArt expectedDto1 = result.stream().filter(lFirst -> lFirst.id == dto1.ID).findFirst().orElse(null);
		final TelefonArt expectedDto2 = result.stream().filter(lSecond -> lSecond.id == dto2.ID).findFirst().orElse(null);

		assertThat(expectedDto1)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", dto1.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto1.Bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto1.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto1.Sortierung)
				.hasFieldOrPropertyWithValue("anzahlTelefonnummern", 0);

		assertThat(expectedDto2)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", dto2.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto2.Bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto2.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto2.Sortierung)
				.hasFieldOrPropertyWithValue("anzahlTelefonnummern", 0);
	}

	@Test
	@DisplayName("getById | TelefonArt null")
	void getByIdTest_notFound() {
		when(conn.queryByKey(DTOTelefonArt.class, 1L)).thenReturn(null);
		final var throwable = catchThrowable(() -> data.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Die Telefonart mit der ID 1 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("getById | id null")
	void getByIdTest_idNull() {
		final var throwable = catchThrowable(() -> data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Anfrage zu einer Telefonart mit der ID null ist unzulässig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getById")
	void getByIdTest() throws ApiOperationException {
		final DTOTelefonArt dto = getDTOTelefonArt();
		when(conn.queryByKey(DTOTelefonArt.class, 1L)).thenReturn(dto);

		assertThat(data.getById(dto.ID))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Mobilnummer")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("anzahlTelefonnummern", 0);

	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35L),
				arguments("bezeichnung", "Mobilnummer"),
				arguments("istSichtbar", true),
				arguments("sortierung", 32000),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOTelefonArt();
		final Map<String, Object> map = new HashMap<>();
		final var throwable = Assertions.catchThrowable(() -> this.data.mapAttribute(expectedDTO, key, value, map));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "bezeichnung" -> assertThat(expectedDTO.Bezeichnung).isEqualTo(value);
			case "istSichtbar" -> assertThat(expectedDTO.Sichtbar).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.Sortierung).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknownArgument.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | Bezeichnung bereits vorhanden")
	void mapAttributeTest_bezeichnungDoppeltVergeben() {
		when(this.conn.queryAll(DTOTelefonArt.class)).thenReturn(List.of(new DTOTelefonArt(1L, "abc")));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOTelefonArt(2L, "123"), "bezeichnung", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Bezeichnung case aendert sich")
	void mapAttributeTest_changeCaseOfBezeichnung() throws ApiOperationException {
		final var dto = new DTOTelefonArt(1L, "abc");
		when(this.conn.queryAll(DTOTelefonArt.class)).thenReturn(List.of(dto));

		this.data.mapAttribute(dto, "bezeichnung", "ABC", null);

		assertThat(dto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse")
	void checkBeforeDeletionWithSimpleOperationResponseTest() {
		final var response1 = new SimpleOperationResponse();
		response1.success = true;
		final var response2 = new SimpleOperationResponse();
		response2.success = true;
		when(this.conn.queryList(
				"SELECT t.TelefonArt_ID, COUNT(t) FROM DTOSchuelerTelefon t WHERE t.TelefonArt_ID IN ?1 GROUP BY t.TelefonArt_ID", Object[].class, List.of(1L, 2L)
				)).thenReturn(List.of(new Object[]{1L, 2L}, new Object[]{2L, 0L}));
		this.data.checkBeforeDeletionWithSimpleOperationResponse(
				List.of(new DTOTelefonArt(1L, "abc"), new DTOTelefonArt(2L, "123")),
				Map.of(1L, response1, 2L, response2)
		);

		assertThat(response1.success).isFalse();
		assertThat(response1.log).anyMatch(msg -> msg.contains("abc"));
		assertThat(response2.success).isTrue();
	}

	private DTOTelefonArt getDTOTelefonArt() {
		final var dtoKatalogTelefonArt = new DTOTelefonArt(1L, "Mobilnummer");
		dtoKatalogTelefonArt.ID = 1L;
		dtoKatalogTelefonArt.Bezeichnung = "Mobilnummer";
		dtoKatalogTelefonArt.Sichtbar = true;
		dtoKatalogTelefonArt.Sortierung = 32000;
		return dtoKatalogTelefonArt;
	}
}
