package de.svws_nrw.data.betriebe;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.betrieb.Beschaeftigungsart;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.ThrowableAssert;
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

/** Diese Klasse testet die Klasse {@link DataBeschaeftigungsarten}*/
@DisplayName("Diese Klasse testet die Klasse DataBeschaeftigungsarten.")
@ExtendWith(MockitoExtension.class)
class DataBeschaeftigungsartenTest {


	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataBeschaeftigungsarten data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: bezeichnung")
	void setAttributesRequiredOnCreationTest() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("istSichtbar", true));

			final var throwable = catchThrowable(() -> this.data.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (bezeichnung) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableTest() {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOBeschaeftigungsart.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", 99L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
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
		when(this.conn.queryByKey(DTOBeschaeftigungsart.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Beschaeftigungsart.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für die Beschäftigungsart darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde keine Beschäftigungsart mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final var dto1 = new DTOBeschaeftigungsart(1L, "eins");
		final var dto2 = new DTOBeschaeftigungsart(2L, "zwei");
		when(this.conn.queryAll(DTOBeschaeftigungsart.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(Beschaeftigungsart.class)
						.hasFieldOrProperty("id")
						.hasFieldOrProperty("bezeichnung"));
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
		dto.Sortierung = 1200;
		dto.Sichtbar = true;
		dto.Aenderbar = true;

		assertThat(this.data.map(dto))
				.isInstanceOf(Beschaeftigungsart.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar)
				.hasFieldOrPropertyWithValue("istAenderbar", dto.Aenderbar);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35),
				arguments("bezeichnung", "lalilu"),
				arguments("sortierung", 1),
				arguments("istSichtbar", false),
				arguments("istAenderbar", false),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var dto = getDto();

		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.mapAttribute(dto, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "bezeichnung" -> assertThat(dto.Bezeichnung).isEqualTo(value);
			case "sortierung" -> assertThat(dto.Sortierung).isEqualTo(value);
			case "istSichtbar" -> assertThat(dto.Sichtbar).isEqualTo(value);
			case "istAenderbar" -> assertThat(dto.Aenderbar).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung Null")
	void mapAttributeTest_bezeichnungNull() {
		final var expectedDTO = getDto();

		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.mapAttribute(expectedDTO, "bezeichnung", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut bezeichnung: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
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
		final var beschaeftigungsart = getDto();
		when(this.conn.queryAll(DTOBeschaeftigungsart.class)).thenReturn(List.of(beschaeftigungsart));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOBeschaeftigungsart(2L, "DEF"), "bezeichnung", "TEST", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung TEST ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung unverändert")
	void mapAttributeTest_bezeichnungUnchanging() {
		final var dto = getDto();

		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "bezeichnung", "test", null));

		verifyNoInteractions(this.conn);
		assertThat(dto.Bezeichnung).isEqualTo("test");
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

	private static DTOBeschaeftigungsart getDto() {
		return new DTOBeschaeftigungsart(1L, "test");
	}


}
