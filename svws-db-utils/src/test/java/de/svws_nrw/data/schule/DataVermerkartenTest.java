package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOVermerkArt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerVermerke;
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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataVermerkarten} */
@DisplayName("Diese Klasse testet die Klasse DataVermerkarten")
@ExtendWith(MockitoExtension.class)
class DataVermerkartenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataVermerkarten data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesNotPatchable : id")
	void setAttributesNotPatchableTest() {
		final var dto = new DTOVermerkArt(1L, "abc");
		when(this.conn.queryByKey(DTOVermerkArt.class, 1L)).thenReturn(dto);

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
		final var dto = new DTOVermerkArt(1L, "abc");

		this.data.initDTO(dto, 2L, null);

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = new DTOVermerkArt(1L, "abc");
		when(this.conn.queryByKey(DTOVermerkArt.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(VermerkartEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Anfrage zu einer Vermerkart mit der ID null ist unzulässig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = catchThrowable(() -> this.data.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Vermerkart mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final var dto1 = new DTOVermerkArt(1L, "abc");
		final var dto2 = new DTOVermerkArt(2L, "abc");
		when(this.conn.queryAll(DTOVermerkArt.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(VermerkartEintrag.class)
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
		final var dto = new DTOVermerkArt(1L, "abc");
		dto.Sichtbar = true;
		dto.Sortierung = 1;

		assertThat(this.data.map(dto))
				.isInstanceOf(VermerkartEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 1);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35),
				arguments("bezeichnung", "test"),
				arguments("sortierung", 2),
				arguments("istSichtbar", true),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var dto = new DTOVermerkArt(1L, "abc");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "istSichtbar" -> assertThat(dto.Sichtbar).isTrue();
			case "bezeichnung" -> assertThat(dto.Bezeichnung).isEqualTo("test");
			case "sortierung" -> assertThat(dto.Sortierung).isEqualTo(2);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | id is correct | nothing thrown")
	void mapAttributeTest_idIsCorrect() {
		final var dto = new DTOVermerkArt(1L, "abc");
		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("mapAttribute | Bezeichnung bereits vorhanden")
	void mapAttributeTest_bezeichnungDoppeltVergeben() {
		final var vermerkartABC = new DTOVermerkArt(1L, "abc");
		when(this.conn.queryAll(DTOVermerkArt.class)).thenReturn(List.of(vermerkartABC));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOVermerkArt(2L, "abc"), "bezeichnung", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Bezeichnung case aendert sich")
	void mapAttributeTest_changeCaseOfBezeichnung() throws ApiOperationException {
		final var dto = new DTOVermerkArt(1L, "abc");
		when(this.conn.queryAll(DTOVermerkArt.class)).thenReturn(List.of(dto));

		this.data.mapAttribute(dto, "bezeichnung", "ABC", null);

		assertThat(dto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("deleteMultipleAsResponse | Vermerke vorhanden")
	void deleteMultipleAsResponseTest_vermerkeVorhanden() {
		final List<Long> ids = List.of(1L);
		final var vermerk = new DTOSchuelerVermerke(1L, 2L);
		vermerk.VermerkArt_ID = 1L;
		when(this.conn.queryByKeyList(DTOVermerkArt.class, ids)).thenReturn(List.of(new DTOVermerkArt(1L, "abc")));
		when(this.conn.queryList("SELECT v FROM DTOSchuelerVermerke v WHERE v.VermerkArt_ID IN ?1", DTOSchuelerVermerke.class, ids)).thenReturn(List.of(vermerk));
		try (var response = this.data.deleteMultipleAsResponse(ids)) {
			final var result = ((List<?>) response.getEntity()).stream().map(SimpleOperationResponse.class::cast).toList();

			assertThat(result.getFirst().log.getFirst()).isEqualTo("Vermerkart abc (ID: 1) hat noch 1 verknüpfte(n) Vermerke.");
		}
	}

	@Test
	@DisplayName("deleteMultipleAsResponse | keine Vermerke vorhanden")
	void deleteMultipleAsResponseTest_keineVermerkeVorhanden() {
		final List<Long> ids = List.of(1L);
		when(this.conn.queryByKeyList(DTOVermerkArt.class, ids)).thenReturn(List.of(new DTOVermerkArt(1L, "abc")));
		when(this.conn.queryList("SELECT v FROM DTOSchuelerVermerke v WHERE v.VermerkArt_ID IN ?1", DTOSchuelerVermerke.class, ids)).thenReturn(Collections.emptyList());
		when(this.conn.transactionRemove(any())).thenReturn(true);

		try (var response = this.data.deleteMultipleAsResponse(ids)) {
			final var result = ((List<?>) response.getEntity()).stream().map(SimpleOperationResponse.class::cast).toList();

			assertThat(result.getFirst().log).isEmpty();
			assertThat(result.getFirst().success).isTrue();
		}
	}

	@Test
	@DisplayName("deleteMultipleAsResponse | ids empty")
	void deleteMultipleAsResponseTest_idsEmpty() {
		try (var response = this.data.deleteMultipleAsResponse(Collections.emptyList())) {
			final var result = (SimpleOperationResponse) response.getEntity();

			assertThat(result.log.getFirst()).isEqualTo("Die Liste der zu löschenden Ids ist leer.");
			assertThat(result.success).isFalse();
		}
	}

}
