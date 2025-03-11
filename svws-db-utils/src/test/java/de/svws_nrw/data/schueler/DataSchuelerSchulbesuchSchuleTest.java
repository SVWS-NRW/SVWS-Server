package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Diese Klasse teste die Klasse {@link DataSchuelerSchulbesuchSchule}*/
@DisplayName("Diese Klasse teste die Klasse DataSchuelerSchulbesuchSchule")
class DataSchuelerSchulbesuchSchuleTest {

	private final DBEntityManager conn = mock(DBEntityManager.class);

	private DataSchuelerSchulbesuchSchule data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(List.of(new DTOEntlassarten(7, "Beurlaubung")));
		data = new DataSchuelerSchulbesuchSchule(conn, 123L);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableTest() {
		final var dto = new DTOSchuelerAbgaenge(1L, 1001);
		when(this.conn.queryByKey(DTOSchuelerAbgaenge.class, 1L)).thenReturn(dto);

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
	void initDTOTest() throws ApiOperationException {
		final var dto = getDtoSchuelerAbgaenge();

		this.data.initDTO(dto, 2L, null);

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("initDTO | missing idSchueler")
	void initDTOTest_idSchuelerMissing() {
		final var dto = getDtoSchuelerAbgaenge();
		data = new DataSchuelerSchulbesuchSchule(conn);

		final var throwable = catchThrowable(() -> this.data.initDTO(dto, 2L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID des Schuelers darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = getDtoSchuelerAbgaenge();

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getByID | Erfolg")
	void getByIDTest() throws ApiOperationException {
		final var dto = getDtoSchuelerAbgaenge();
		when(this.conn.queryByKey(DTOSchuelerAbgaenge.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(SchuelerSchulbesuchSchule.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für den Schulbesuch eines Schülers darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = catchThrowable(() -> this.data.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Schulbesuch eines Schülers mit der Id 99 gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() throws ApiOperationException {
		final var dto = getDtoSchuelerAbgaenge();

		assertThat(this.data.map(dto))
				.isInstanceOf(SchuelerSchulbesuchSchule.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("schulgliederung", dto.LSSGL)
				.hasFieldOrPropertyWithValue("entlassgrundID", 7L)
				.hasFieldOrPropertyWithValue("abschlussartID", dto.LSEntlassArt)
				.hasFieldOrPropertyWithValue("organisationsFormID", dto.OrganisationsformKrz)
				.hasFieldOrPropertyWithValue("datumVon", dto.LSBeginnDatum)
				.hasFieldOrPropertyWithValue("datumBis", dto.LSSchulEntlassDatum)
				.hasFieldOrPropertyWithValue("jahrgangVon", dto.LSBeginnJahrgang)
				.hasFieldOrPropertyWithValue("jahrgangBis", dto.LSJahrgang);
	}

	@Test
	@DisplayName("mapMultiple | Erfolg")
	void mapMultipleTest() {
		final var dto = getDtoSchuelerAbgaenge();
		final var entlassart = new DTOEntlassarten(7, "Beurlaubung");

		assertThat(DataSchuelerSchulbesuchSchule.mapMultiple(List.of(dto), Map.of(entlassart.Bezeichnung, entlassart)))
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(SchuelerSchulbesuchSchule.class)
						.hasFieldOrPropertyWithValue("id", dto.ID)
						.hasFieldOrPropertyWithValue("schulgliederung", dto.LSSGL)
						.hasFieldOrPropertyWithValue("entlassgrundID", 7L)
						.hasFieldOrPropertyWithValue("abschlussartID", dto.LSEntlassArt)
						.hasFieldOrPropertyWithValue("organisationsFormID", dto.OrganisationsformKrz)
						.hasFieldOrPropertyWithValue("datumVon", dto.LSBeginnDatum)
						.hasFieldOrPropertyWithValue("datumBis", dto.LSSchulEntlassDatum)
						.hasFieldOrPropertyWithValue("jahrgangVon", dto.LSBeginnJahrgang)
						.hasFieldOrPropertyWithValue("jahrgangBis", dto.LSJahrgang)
				);
	}

	@Test
	@DisplayName("mapMultiple | List is empty")
	void mapMultipleTest_emptyList() {
		assertThat(DataSchuelerSchulbesuchSchule.mapMultiple(Collections.emptyList(), Collections.emptyMap())).isNotNull().isEmpty();
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDtoSchuelerAbgaenge();

		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("PatchId 35 ist ungleich dtoID 1.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "idSchueler" -> assertThat(expectedDTO.Schueler_ID).isEqualTo(value);
			case "schulnummer" -> assertThat(expectedDTO.AbgangsSchulNr).isEqualTo(value);
			case "schulgliederung" -> assertThat(expectedDTO.LSSGL).isEqualTo(value);
			case "entlassgrundID" -> assertThat(expectedDTO.BemerkungIntern).isEqualTo("Beurlaubung");
			case "abschlussartID" -> assertThat(expectedDTO.LSEntlassArt).isEqualTo(value);
			case "organisationsFormID" -> assertThat(expectedDTO.OrganisationsformKrz).isEqualTo(value);
			case "datumVon" -> assertThat(expectedDTO.LSBeginnDatum).isEqualTo(value);
			case "datumBis" -> assertThat(expectedDTO.LSSchulEntlassDatum).isEqualTo(value);
			case "jahrgangVon" -> assertThat(expectedDTO.LSBeginnJahrgang).isEqualTo(value);
			case "jahrgangBis" -> assertThat(expectedDTO.LSJahrgang).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35L),
				arguments("idSchueler", 1001L),
				arguments("schulnummer", "123456"),
				arguments("schulgliederung", "456789"),
				arguments("entlassgrundID", 7L),
				arguments("abschlussartID", "OA"),
				arguments("organisationsFormID", "1"),
				arguments("davonVon", "1907-12-01"),
				arguments("datumBis", "1909-02-03"),
				arguments("jahrgangVon", "07"),
				arguments("jahrgangBis", "08"),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | optional Values Null")
	@MethodSource("provideMappingAttributesWithNullValues")
	void mapAttributeTest_optionalValuesNull(final String key, final Object value) {
		final var expectedDTO = getDtoSchuelerAbgaenge();

		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "schulgliederung" -> assertThat(expectedDTO.LSSGL).isNull();
			case "entlassgrundID" -> assertThat(expectedDTO.BemerkungIntern).isNull();
			case "abschlussartID" -> assertThat(expectedDTO.LSEntlassArt).isNull();
			case "organisationsFormID" -> assertThat(expectedDTO.OrganisationsformKrz).isNull();
			case "datumVon" -> assertThat(expectedDTO.LSBeginnDatum).isNull();
			case "datumBis" -> assertThat(expectedDTO.LSSchulEntlassDatum).isNull();
			case "jahrgangVon" -> assertThat(expectedDTO.LSBeginnJahrgang).isNull();
			case "jahrgangBis" -> assertThat(expectedDTO.LSJahrgang).isNull();
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributesWithNullValues() {
		return Stream.of(
				arguments("schulgliederung", null),
				arguments("entlassgrundID", null),
				arguments("abschlussartID", null),
				arguments("organisationsFormID", null),
				arguments("davonVon", null),
				arguments("datumBis", null),
				arguments("jahrgangVon", null),
				arguments("jahrgangBis", null),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@Test
	@DisplayName("mapAttribute | id is correct | nothing thrown")
	void mapAttributeTest_idIsCorrect() {
		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(getDtoSchuelerAbgaenge(), "id", 1L, null));
	}

	@Test
	@DisplayName("mapAttribute | wrong entlassart")
	void mapAttributeTest_entlassartIncorrect() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDtoSchuelerAbgaenge(), "entlassgrundID", 1L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Zur id 1 existiert keine Entlassart")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private DTOSchuelerAbgaenge getDtoSchuelerAbgaenge() {
		final var dto = new DTOSchuelerAbgaenge(1L, 1001);
		dto.AbgangsSchulNr = "123456";
		dto.LSSGL = "456789";
		dto.BemerkungIntern = "Beurlaubung";
		dto.LSEntlassArt = "OA";
		dto.OrganisationsformKrz = "1";
		dto.LSBeginnDatum = "1907-12-01";
		dto.LSSchulEntlassDatum = "1909-02-03";
		dto.LSBeginnJahrgang = "07";
		dto.LSJahrgang = "08";
		return dto;

	}
}
