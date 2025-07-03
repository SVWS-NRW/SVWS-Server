package de.svws_nrw.data.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
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
	private DataKatalogTelefonArten katalogTelefonArten;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		katalogTelefonArten = new DataKatalogTelefonArten(conn);
		final DTOTelefonArt dto = getDTOTelefonArt();
		final long id = 1L;
		final Map<String, Object> initAttributes = new HashMap<>();

		katalogTelefonArten.initDTO(dto, id, initAttributes);

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

		assertThat(katalogTelefonArten.map(dto))
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

		final List<TelefonArt> result = katalogTelefonArten.getAll();
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
		final var throwable = catchThrowable(() -> katalogTelefonArten.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Die Telefonart mit der ID 1 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("getById")
	void getByIdTest() throws ApiOperationException {
		final DTOTelefonArt dto = getDTOTelefonArt();
		when(conn.queryByKey(DTOTelefonArt.class, 1L)).thenReturn(dto);

		assertThat(katalogTelefonArten.getById(dto.ID))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Mobilnummer")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("anzahlTelefonnummern", 0);

	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOTelefonArt();
		final Map<String, Object> map = new HashMap<>();
		final var throwable = Assertions.catchThrowable(() -> this.katalogTelefonArten.mapAttribute(expectedDTO, key, value, map));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "bezeichnung" -> assertThat(expectedDTO.Bezeichnung).isEqualTo(value);
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
				arguments("bezeichnung", "Mobilnummer"),
				arguments("istSichtbar", true),
				arguments("sortierung", 32000)
		);
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
