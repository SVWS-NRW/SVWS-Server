package de.svws_nrw.data.schule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Telefonart;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOTelefonArt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
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
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataKatalogTelefonArten")
@ExtendWith(MockitoExtension.class)
class DataTelefonartenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataTelefonarten data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		data = new DataTelefonarten(conn);
		final DTOTelefonArt dto = getDTOTelefonArt();
		final long id = 1L;
		final Map<String, Object> initAttributes = new HashMap<>();

		data.initDTO(dto, id, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", id)
				.hasFieldOrPropertyWithValue("Bezeichnung", "Mobilnummer")
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final DTOTelefonArt dto = getDTOTelefonArt();

		assertThat(data.map(dto))
				.isInstanceOf(Telefonart.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Mobilnummer")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final DTOTelefonArt dto1 = getDTOTelefonArt();
		final DTOTelefonArt dto2 = getDTOTelefonArt();
		dto2.ID = 2L;
		dto2.Bezeichnung = "Testbezeichnung2";

		when(conn.queryAll(DTOTelefonArt.class)).thenReturn(List.of(dto1, dto2));

		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						t1 -> assertThat(t1)
								.isInstanceOf(Telefonart.class)
								.hasFieldOrPropertyWithValue("id", dto1.ID)
								.hasFieldOrPropertyWithValue("bezeichnung", dto1.Bezeichnung)
								.hasFieldOrPropertyWithValue("istSichtbar", dto1.Sichtbar)
								.hasFieldOrPropertyWithValue("sortierung", dto1.Sortierung),
						t2 -> assertThat(t2)
								.isInstanceOf(Telefonart.class)
								.hasFieldOrPropertyWithValue("id", dto2.ID)
								.hasFieldOrPropertyWithValue("bezeichnung", dto2.Bezeichnung)
								.hasFieldOrPropertyWithValue("istSichtbar", dto2.Sichtbar)
								.hasFieldOrPropertyWithValue("sortierung", dto2.Sortierung)
				);
	}

	@Test
	@DisplayName("getAll | referenced in other table")
	void getAllReferencedInOtherTables() {
		final DTOTelefonArt dto1 = getDTOTelefonArt();
		final DTOTelefonArt dto2 = getDTOTelefonArt();
		dto2.ID = 2L;

		when(this.conn.queryAll(DTOTelefonArt.class)).thenReturn(List.of(dto1, dto2));

		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						t1 -> assertThat(t1)
								.isInstanceOf(Telefonart.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						t2 -> assertThat(t2)
								.isInstanceOf(Telefonart.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getById | TelefonArt mit ID existiert nicht.")
	void getByIdTest_notFound() {
		when(conn.queryByKey(DTOTelefonArt.class, 1L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(1L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Telefonart mit der ID 1 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getById | ID darf nicht null sein.")
	void getByIdTest_idNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Eine Anfrage zu einer Telefonart mit der ID null ist unzulässig.")
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
				.hasFieldOrPropertyWithValue("sortierung", 32000);
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
	@DisplayName("mapAttribute | bezeichnung dto is null")
	void mapAttributeTest_bezeichnungDtoISNull() throws ApiOperationException {
		final var dto = new DTOTelefonArt(1L, "123");
		dto.Bezeichnung = null;
		final var newDto = new DTOTelefonArt(1L, "abc");
		when(conn.queryAll(DTOTelefonArt.class)).thenReturn(List.of(dto));

		this.data.mapAttribute(newDto, "bezeichnung", "test", null);

		assertThat(newDto.Bezeichnung).isEqualTo("test");
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
