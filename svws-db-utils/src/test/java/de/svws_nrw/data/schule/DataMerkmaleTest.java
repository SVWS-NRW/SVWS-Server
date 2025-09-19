package de.svws_nrw.data.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
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
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataMerkmale} */
@DisplayName("diese Klasse testet die Klasse DataMerkmale")
@ExtendWith(MockitoExtension.class)
class DataMerkmaleTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataMerkmale data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesNotPatchable : id")
	void setAttributesNotPatchableTest() {
		final var dto = new DTOMerkmale(1L);
		when(this.conn.queryByKey(DTOMerkmale.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", 99L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.");
		}
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTOTest() {
		final var dto = new DTOMerkmale(1L);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = new DTOMerkmale(1L);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = new DTOMerkmale(1L);
		when(this.conn.queryByKey(DTOMerkmale.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Merkmal.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für das Merkmal darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = catchThrowable(() -> this.data.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Merkmal mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final var dto1 = new DTOMerkmale(1L);
		final var dto2 = new DTOMerkmale(2L);
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(Merkmal.class)
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
		final var dto = new DTOMerkmale(1L);
		dto.Schule = true;
		dto.Schueler = true;
		dto.Kurztext = "kurz";
		dto.Langtext = "lang";

		assertThat(this.data.map(dto))
				.isInstanceOf(Merkmal.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("istSchulmerkmal", dto.Schule)
				.hasFieldOrPropertyWithValue("istSchuelermerkmal", dto.Schueler)
				.hasFieldOrPropertyWithValue("kuerzel", dto.Kurztext)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Langtext);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35),
				arguments("istSchulmerkmal", false),
				arguments("istSchuelermerkmal", false),
				arguments("kuerzel", "hey"),
				arguments("bezeichnung", "ho"),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var dto = new DTOMerkmale(1L);

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "istSchulmerkmal" -> assertThat(dto.Schule).isEqualTo(value);
			case "istSchuelermerkmal" -> assertThat(dto.Schueler).isEqualTo(value);
			case "kuerzel" -> assertThat(dto.Kurztext).isEqualTo(value);
			case "bezeichnung" -> assertThat(dto.Langtext).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | id is correct | nothing thrown")
	void mapAttributeTest_idIsCorrect() {
		final var dto = new DTOMerkmale(1L);
		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("mapAttribute | kuerzel bereits vorhanden")
	void mapAttributeTest_kuerzelDoppeltVergeben() {
		final var merkmalABC = new DTOMerkmale(1L);
		merkmalABC.Kurztext = "abc";
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(List.of(merkmalABC));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOMerkmale(2L), "kuerzel", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Kürzel ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzel case aendert sich")
	void mapAttributeTest_changeCaseOfKuerzel() throws ApiOperationException {
		final var dto = new DTOMerkmale(1L);
		dto.Kurztext = "abc";
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(List.of(dto));

		this.data.mapAttribute(dto, "kuerzel", "ABC", null);

		assertThat(dto.Kurztext).isEqualTo("ABC");
	}

	@Test
	@DisplayName("mapAttribute | Bezeichnung bereits vorhanden")
	void mapAttributeTest_bezeichnungDoppeltVergeben() {
		final var merkmalABC = new DTOMerkmale(1L);
		merkmalABC.Langtext = "abc";
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(List.of(merkmalABC));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOMerkmale(2L), "bezeichnung", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Bezeichnung case aendert sich")
	void mapAttributeTest_changeCaseOfBezeichnung() throws ApiOperationException {
		final var dto = new DTOMerkmale(1L);
		dto.Langtext = "abc";
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(List.of(dto));

		this.data.mapAttribute(dto, "bezeichnung", "ABC", null);

		assertThat(dto.Langtext).isEqualTo("ABC");
	}

}
