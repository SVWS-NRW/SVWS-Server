package de.svws_nrw.data.kataloge;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataHaltestellen}*/
@DisplayName("Diese Klasse testet die Klasse DataHaltestellen")
@ExtendWith(MockitoExtension.class)
class DataHaltestellenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataHaltestellen data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesNotPatchable | id")
	void setAttributesNotPatchableTest() {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOHaltestellen.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", 99L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.");
		}
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation | bezeichnung")
	void setAttributesRequiredOnCreationTest_bezeichnung() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("entfernungSchule", 17, "sortierung", 3));

			final var throwable = catchThrowable(() -> this.data.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (bezeichnung) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTOTest() {
		final var dto = getDto();

		this.data.initDTO(dto, 2L, null);

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = getDto();

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOHaltestellen.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Haltestelle.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für die Haltestelle darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = catchThrowable(() -> this.data.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde keine Haltestelle mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final var dto1 = getDto();
		final var dto2 = getDto();
		when(this.conn.queryAll(DTOHaltestellen.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(Haltestelle.class)
						.hasFieldOrProperty("id"));
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllTest_Empty() {
		assertThat(this.data.getAll()).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() {
		final var dto = getDto();
		dto.EntfernungSchule = 17d;
		dto.Aenderbar = true;
		dto.Sortierung = 25;
		dto.Sichtbar = true;

		assertThat(this.data.map(dto))
				.isInstanceOf(Haltestelle.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("entfernungSchule", dto.EntfernungSchule)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar)
				.hasFieldOrPropertyWithValue("istAenderbar", dto.Aenderbar)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35),
				arguments("bezeichnung", "test"),
				arguments("entfernungSchule", 17d),
				arguments("istSichtbar", true),
				arguments("istAenderbar", true),
				arguments("sortierung", 25),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var dto = getDto();

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "bezeichnung" -> assertThat(dto.Bezeichnung).isEqualTo(value);
			case "entfernungSchule" -> assertThat(dto.EntfernungSchule).isEqualTo(value);
			case "istSichtbar" -> assertThat(dto.Sichtbar).isEqualTo(value);
			case "istAenderbar" -> assertThat(dto.Aenderbar).isEqualTo(value);
			case "sortierung" -> assertThat(dto.Sortierung).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | id is correct | nothing thrown")
	void mapAttributeTest_idIsCorrect() {
		final var dto = getDto();
		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung bereits vorhanden")
	void mapAttributeTest_bezeichnungDoppeltVergeben() {
		final var haltestelle = getDto();
		haltestelle.Bezeichnung = "ABC";
		when(this.conn.queryAll(DTOHaltestellen.class)).thenReturn(List.of(haltestelle));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOHaltestellen(2L, "DEF"), "bezeichnung", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung unverändert")
	void mapAttributeTest_bezeichnungUnchanging() {
		final var dto = getDto();

		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "bezeichnung", "abc", null));

		verifyNoInteractions(this.conn);
		assertThat(dto.Bezeichnung).isEqualTo("abc");
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung null")
	void mapAttributeTest_bezeichnungNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "bezeichnung", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut bezeichnung: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung blank")
	void mapAttributeTest_bezeichnungBlank() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "bezeichnung", "", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private static DTOHaltestellen getDto() {
		return new DTOHaltestellen(1L, "abc");
	}
}
