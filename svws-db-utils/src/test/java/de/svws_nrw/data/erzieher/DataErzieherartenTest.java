package de.svws_nrw.data.erzieher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.erzieher.Erzieherart;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOErzieherart;
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

@DisplayName("Diese Testklasse testet die Klasse DataErzieherarten")
@ExtendWith(MockitoExtension.class)
class DataErzieherartenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataErzieherarten dataErzieherarten;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		dataErzieherarten = new DataErzieherarten(conn);
		final DTOErzieherart dto = getDTOErzieherart();
		final long id = 1L;
		final Map<String, Object> initAttributes = new HashMap<>();

		dataErzieherarten.initDTO(dto, id, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", id)
				.hasFieldOrPropertyWithValue("Bezeichnung", "")
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final DTOErzieherart dto = getDTOErzieherart();

		assertThat(dataErzieherarten.map(dto))
				.isInstanceOf(Erzieherart.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Mutter")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("exportBez", "Export");
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() throws ApiOperationException {
		final DTOErzieherart dto1 = getDTOErzieherart();
		final DTOErzieherart dto2 = getDTOErzieherart();
		dto2.ID = 2L;
		dto2.Bezeichnung = "Testbezeichnung2";

		final List<DTOErzieherart> dtoList = new ArrayList<>();
		dtoList.add(dto1);
		dtoList.add(dto2);

		when(conn.queryAll(DTOErzieherart.class)).thenReturn(dtoList);

		final List<Erzieherart> result = dataErzieherarten.getAll();
		final Erzieherart expectedDto1 = result.stream().filter(lFirst -> lFirst.id == dto1.ID).findFirst().orElse(null);
		final Erzieherart expectedDto2 = result.stream().filter(lSecond -> lSecond.id == dto2.ID).findFirst().orElse(null);

		assertThat(expectedDto1)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", dto1.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto1.Bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto1.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto1.Sortierung)
				.hasFieldOrPropertyWithValue("exportBez", "Export");

		assertThat(expectedDto2)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", dto2.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto2.Bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto2.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto2.Sortierung)
				.hasFieldOrPropertyWithValue("exportBez", "Export");
	}

	@Test
	@DisplayName("getById | Erzieherart null")
	void getByIdTest_notFound() {
		when(conn.queryByKey(DTOErzieherart.class, 1L)).thenReturn(null);
		final var throwable = catchThrowable(() -> dataErzieherarten.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Die Erzieherart mit der ID 1 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("getById")
	void getByIdTest() throws ApiOperationException {
		final DTOErzieherart dto = getDTOErzieherart();
		when(conn.queryByKey(DTOErzieherart.class, 1L)).thenReturn(dto);

		assertThat(dataErzieherarten.getById(dto.ID))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Mutter")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("exportBez", "Export");

	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOErzieherart();
		final Map<String, Object> map = new HashMap<>();
		final var throwable = Assertions.catchThrowable(() -> this.dataErzieherarten.mapAttribute(expectedDTO, key, value, map));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "bezeichnung" -> assertThat(expectedDTO.Bezeichnung).isEqualTo(value);
			case "istSichtbar" -> assertThat(expectedDTO.Sichtbar).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.Sortierung).isEqualTo(value);
			case "exportBez" -> assertThat(expectedDTO.ExportBez).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten ein unbekanntes Attribut.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 1L),
				arguments("bezeichnung", "Mutter"),
				arguments("istSichtbar", true),
				arguments("sortierung", 32000),
				arguments("exportBez", "Export")
		);
	}

	private DTOErzieherart getDTOErzieherart() {
		final var dtoErzieherart = new DTOErzieherart(1L, "Mutter");
		dtoErzieherart.ID = 1L;
		dtoErzieherart.Bezeichnung = "Mutter";
		dtoErzieherart.Sichtbar = true;
		dtoErzieherart.Sortierung = 32000;
		dtoErzieherart.ExportBez = "Export";
		return dtoErzieherart;
	}
}
