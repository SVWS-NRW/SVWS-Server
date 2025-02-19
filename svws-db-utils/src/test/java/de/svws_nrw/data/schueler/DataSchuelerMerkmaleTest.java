package de.svws_nrw.data.schueler;


import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
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
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/** Diese Klasse testet die Klasse {@link DataSchuelerMerkmale} */
@DisplayName("Diese Klasse testet die Klasse DataSchuelerSchulbesuchMerkmal")
@ExtendWith(MockitoExtension.class)
class DataSchuelerMerkmaleTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchuelerMerkmale dataSchuelerMerkmale;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableTest() {
		final var dto = new DTOSchuelerMerkmale(1L, 1001);
		when(this.conn.queryByKey(DTOSchuelerMerkmale.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", 99L));

			final var throwable = catchThrowable(() -> this.dataSchuelerMerkmale.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idSchueler")
	void setAttributesRequiredOnCreationTest() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("kurztext", "abc"));

			final var throwable = catchThrowable(() -> this.dataSchuelerMerkmale.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (idSchueler) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTOTest() throws ApiOperationException {
		final var dto = getDtoMerkmal();

		this.dataSchuelerMerkmale.initDTO(dto, 2L, null);

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = getDtoMerkmal();

		assertThat(this.dataSchuelerMerkmale.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getByID | Erfolg")
	void getByIDTest() throws ApiOperationException {
		final var dtoMerkmal = getDtoMerkmal();
		when(this.conn.queryByKey(DTOSchuelerMerkmale.class, 1L)).thenReturn(dtoMerkmal);

		assertThat(this.dataSchuelerMerkmale.getById(1L))
				.isInstanceOf(SchuelerSchulbesuchMerkmal.class)
				.hasFieldOrPropertyWithValue("id", dtoMerkmal.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = catchThrowable(() -> this.dataSchuelerMerkmale.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für das Merkmal des Schülers darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = catchThrowable(() -> this.dataSchuelerMerkmale.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Merkmal eines Schülers mit der Id 99 gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() throws ApiOperationException {
		final var dto = getDtoMerkmal();

		assertThat(this.dataSchuelerMerkmale.map(dto))
				.isInstanceOf(SchuelerSchulbesuchMerkmal.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("idSchueler", dto.Schueler_ID)
				.hasFieldOrPropertyWithValue("kurztext", dto.Kurztext)
				.hasFieldOrPropertyWithValue("datumVon", dto.DatumVon)
				.hasFieldOrPropertyWithValue("datumBis", dto.DatumBis);
	}

	@Test
	@DisplayName("mapMultiple | Erfolg")
	void mapMultipleTest() {
		final var dto = getDtoMerkmal();

		assertThat(DataSchuelerMerkmale.mapMultiple(List.of(dto)))
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(SchuelerSchulbesuchMerkmal.class)
						.hasFieldOrPropertyWithValue("id", dto.ID)
						.hasFieldOrPropertyWithValue("idSchueler", dto.Schueler_ID)
						.hasFieldOrPropertyWithValue("kurztext", dto.Kurztext)
						.hasFieldOrPropertyWithValue("datumVon", dto.DatumVon)
						.hasFieldOrPropertyWithValue("datumBis", dto.DatumBis)
				);
	}

	@Test
	@DisplayName("mapMultiple | List is empty")
	void mapMultipleTest_emptyList() {
		assertThat(DataSchuelerMerkmale.mapMultiple(Collections.emptyList())).isNotNull().isEmpty();
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDtoMerkmal();

		final var throwable = catchThrowable(() -> this.dataSchuelerMerkmale.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("IdPatch 35 ist ungleich dtoId 1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "idSchueler" -> assertThat(expectedDTO.Schueler_ID).isEqualTo(value);
			case "kurztext" -> assertThat(expectedDTO.Kurztext).isEqualTo(value);
			case "datumVon" -> assertThat(expectedDTO.DatumVon).isEqualTo(value);
			case "datumBis" -> assertThat(expectedDTO.DatumBis).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35),
				arguments("idSchueler", 1001L),
				arguments("kurztext", "kurztext"),
				arguments("davonVon", "2007-08-01"),
				arguments("datumBis", "2008-07-31"),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | optional Values Null")
	@MethodSource("provideMappingAttributesWithNullValues")
	void mapAttributeTest_optionalValuesNull(final String key, final Object value) {
		final var expectedDTO = getDtoMerkmal();

		final var throwable = catchThrowable(() -> this.dataSchuelerMerkmale.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "kurztext" -> assertThat(expectedDTO.Kurztext).isNull();
			case "datumVon" -> assertThat(expectedDTO.DatumVon).isNull();
			case "datumBis" -> assertThat(expectedDTO.DatumBis).isNull();
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributesWithNullValues() {
		return Stream.of(
				arguments("kurztext", null),
				arguments("davonVon", null),
				arguments("datumBis", null),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@Test
	@DisplayName("mapAttribute | id is correct | nothing thrown")
	void mapAttributeTest_idIsCorrect() {
		assertThatNoException().isThrownBy(() -> this.dataSchuelerMerkmale.mapAttribute(getDtoMerkmal(), "id", 1L, null));
	}

	private DTOSchuelerMerkmale getDtoMerkmal() {
		final var dto = new DTOSchuelerMerkmale(1L, 1001);
		dto.Kurztext = "kurztext";
		dto.DatumVon = "2007-08-01";
		dto.DatumBis = "2008-07-31";
		return dto;
	}
}
