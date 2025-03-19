package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
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
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataAbteilungenKlassenzuordnungen}
 */
@DisplayName("Diese Klasse testet die Klasse DataAbteilungenKlassenzuordnungen")
@ExtendWith(MockitoExtension.class)
class DataAbteilungenKlassenzuordnungenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataAbteilungenKlassenzuordnungen dut;

	private static final long idAbteilung = 2L;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final DTOAbteilungsKlassen dtoAbteilungsKlassen = createDTOAbteilungsKlassen();

		this.dut.initDTO(dtoAbteilungsKlassen, 14L, null);

		assertThat(dtoAbteilungsKlassen)
				.isInstanceOf(DTOAbteilungsKlassen.class)
				.hasFieldOrPropertyWithValue("ID", 14L)
				.hasFieldOrPropertyWithValue("Abteilung_ID", idAbteilung)
				.hasFieldOrPropertyWithValue("Klassen_ID", 3L);
	}

	@Test
	@DisplayName("getAll | 2 DTO")
	void getAllTest() throws ApiOperationException {
		when(this.conn.queryList(DTOAbteilungsKlassen.QUERY_ALL, DTOAbteilungsKlassen.class))
				.thenReturn(createDTOAbteilungsKlassenList());

		final List<AbteilungKlassenzuordnung> result = this.dut.getAll();

		assertThat(result).hasSize(2)
				.hasOnlyElementsOfType(AbteilungKlassenzuordnung.class);
	}

	@Test
	@DisplayName("getAll| Empty List")
	void getListEmptyListTest() throws ApiOperationException {
		when(this.conn.queryList(DTOAbteilungsKlassen.QUERY_ALL, DTOAbteilungsKlassen.class)).thenReturn(Collections.emptyList());

		final List<AbteilungKlassenzuordnung> result = this.dut.getAll();

		assertThat(result).isEmpty();
	}


	@Test
	@DisplayName("GetById | Erfolg")
	void getByIdTestSuccess() throws ApiOperationException {
		final DTOAbteilungsKlassen dtoAbteilungsKlassen = createDTOAbteilungsKlassen();
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, dtoAbteilungsKlassen.ID)).thenReturn(dtoAbteilungsKlassen);

		assertThat(dut.getById(dtoAbteilungsKlassen.ID))
				.isInstanceOf(AbteilungKlassenzuordnung.class)
				.hasFieldOrPropertyWithValue("id", dtoAbteilungsKlassen.ID)
				.hasFieldOrPropertyWithValue("idAbteilung", dtoAbteilungsKlassen.Abteilung_ID)
				.hasFieldOrPropertyWithValue("idKlasse", dtoAbteilungsKlassen.Klassen_ID);
	}

	@Test
	@DisplayName("GetById | Falsche ID")
	void getByIdTestFailed() {
		final Throwable throwable = catchThrowable(() -> dut.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Zuordnung mit der ID 1 von einer Klasse zu einer Abteilung konnte nicht gefunden werden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("Map | Erfolgreich")
	void testMap() {
		final DTOAbteilungsKlassen dtoAbteilungsKlassen = createDTOAbteilungsKlassen();

		assertThat(this.dut.map(dtoAbteilungsKlassen))
				.isInstanceOf(AbteilungKlassenzuordnung.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idAbteilung", idAbteilung)
				.hasFieldOrPropertyWithValue("idKlasse", 3L);
	}


	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping und unbekannten Daten")
	@MethodSource("provideMappingAttributes")
	void testMapAttribute(final String key, final Object value) {
		final DTOAbteilungsKlassen expectedDTO = createDTOAbteilungsKlassen();

		final Throwable throwable = ThrowableAssert.catchThrowable(() -> this.dut.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "idAbteilung" -> assertThat(expectedDTO.Abteilung_ID).isEqualTo(value);
			case "idKlasse" -> assertThat(expectedDTO.Klassen_ID).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Das Patchen des Attributes lehrer wird nicht unterstützt.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}


	@Test
	@DisplayName("mapAttribute | Mapping von null-Wert für idAbteilung")
	void testMapAttributeWithIdAbteilungNull() {
		final DTOAbteilungsKlassen expectedDTO = createDTOAbteilungsKlassen();

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "idAbteilung", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut idAbteilung: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

	}

	@ParameterizedTest
	@DisplayName("mapAttribute | fehlerhaftes Mapping der ID")
	@MethodSource("provideMappingAttributes")
	void testMapAttributeFalseID(final String key, final Object value) {
		final DTOAbteilungsKlassen expectedDTO = createDTOAbteilungsKlassen();
		expectedDTO.ID = 14L;

		final Throwable throwable = ThrowableAssert.catchThrowable(() -> this.dut.mapAttribute(expectedDTO, key, value, null));

		if (Objects.equals(key, "id"))
			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Id 1 der PatchMap ist ungleich der id 14 vom Dto")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Mapping von null-Wert für idKlasse")
	void testMapAttributeWithIdKlasseNull() {
		final DTOAbteilungsKlassen expectedDTO = createDTOAbteilungsKlassen();

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "idKlasse", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut idKlasse: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Mapping Klasse für Id nicht gefunden")
	void testMapAttributeWithIdKlasseNotFound() {
		final DTOAbteilungsKlassen expectedDTO = createDTOAbteilungsKlassen();

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "idKlasse", 1L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Für die 1 wurde keine Klasse gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Mapping Abteilung für Id nicht gefunden")
	void testMapAttributeWithIdAbteilungNotFound() {
		final DTOAbteilungsKlassen expectedDTO = createDTOAbteilungsKlassen();

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "idAbteilung", 1L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Für die 1 wurde keine Abteilung gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 1L),
				arguments("idAbteilung", idAbteilung),
				arguments("idKlasse", 3L),
				arguments("lehrer", "wndnw")
		);
	}

	@Test
	@DisplayName("mapAttribute | Mapping von Id der Klasse, Klasse wird nicht gefunden")
	void testMapAttributeWithKlasseNotFoundById() {
		final DTOAbteilungsKlassen expectedDTO = createDTOAbteilungsKlassen();
		when(conn.queryByKey(DTOKlassen.class, 2L)).thenReturn(null);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "idKlasse", 2L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Für die 2 wurde keine Klasse gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Mapping von Id der Abteilung, Abteilung wird nicht gefunden")
	void testMapAttributeWithAbteilungNotFoundById() {
		final DTOAbteilungsKlassen expectedDTO = createDTOAbteilungsKlassen();
		when(conn.queryByKey(DTOAbteilungen.class, 2L)).thenReturn(null);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "idAbteilung", 2L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Für die 2 wurde keine Abteilung gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}


	@Test
	@DisplayName("getListByIdAbteilung | Rückgabe von 2 AbteilungKlassenzuordnung")
	void testGetListByIdAbteilung() throws ApiOperationException {
		when(conn.queryList(DTOAbteilungsKlassen.QUERY_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, idAbteilung)).thenReturn(createDTOAbteilungsKlassenList());

		assertThat(this.dut.getListByIdAbteilung(idAbteilung))
				.filteredOn("id", in(1L, 3L))
				.filteredOn("idAbteilung", in(idAbteilung))
				.filteredOn("idKlasse", in(3L, 4L))
				.hasSize(2);
	}

	@Test
	@DisplayName("getListByIdAbteilung | Rückgabe von einer leeren Liste")
	void testGetListByIdAbteilungEmptyList() throws ApiOperationException {
		when(conn.queryList(DTOAbteilungsKlassen.QUERY_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, idAbteilung)).thenReturn(Collections.emptyList());

		assertThat(this.dut.getListByIdAbteilung(idAbteilung)).isEmpty();
	}

	@Test
	@DisplayName("getListByIdAbteilung | ID der Abteilung ist Null")
	void testGetListByIdAbteilungIdNull() {

		final Throwable throwable = catchThrowable(() -> this.dut.getListByIdAbteilung(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Für das Löschen muss eine ID angegeben werden. Null ist nicht zulässig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getListsByListOfIdAbteilung | Erfolgreich")
	void testGetListsByListOfIdAbteilung() {
		final List<Long> idsAbteilungen = List.of(1L, 2L, 3L);
		final List<DTOAbteilungsKlassen> expectedDTOAbteilungsKlassenList = List.of(new DTOAbteilungsKlassen(1L, 1L, 1L), new DTOAbteilungsKlassen(2L,
				2L, 2L), new DTOAbteilungsKlassen(3L, 1L, 3L));
		when(conn.queryList(DTOAbteilungsKlassen.QUERY_LIST_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, idsAbteilungen)).thenReturn(expectedDTOAbteilungsKlassenList);

		final Map<Long, List<AbteilungKlassenzuordnung>> actualListAbteilungKlassenzuordnung =
				DataAbteilungenKlassenzuordnungen.getListsByListOfIdAbteilung(conn, idsAbteilungen);

		assertThat(actualListAbteilungKlassenzuordnung.get(1L))
				.isInstanceOf(List.class)
				.hasSize(2)
				.isNotEmpty()
				.isNotNull()
				.filteredOn("id", in(1L, 3L))
				.filteredOn("idAbteilung", 1L)
				.filteredOn("idKlasse", in(1L, 3L));
	}

	private DTOAbteilungsKlassen createDTOAbteilungsKlassen() {
		return new DTOAbteilungsKlassen(1L, idAbteilung, 3L);
	}

	private List<DTOAbteilungsKlassen> createDTOAbteilungsKlassenList() {
		return List.of(createDTOAbteilungsKlassen(), new DTOAbteilungsKlassen(3L, idAbteilung, 4L));
	}

}
