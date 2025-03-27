package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Abteilung;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
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
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataAbteilungen}
 */
@DisplayName("Diese Klasse testet die Klasse DataAbteilungen")
@ExtendWith(MockitoExtension.class)
class DataAbteilungenTest {

	private final Long idSchuljahresabschnitts = 2L;

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataAbteilungen dut;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final DTOAbteilungen dtoAbteilungen = createDTOAbteilungen();
		final Map<String, Object> initAttributes = new HashMap<>();
		initAttributes.put("idSchuljahresabschnitts", idSchuljahresabschnitts);

		this.dut.initDTO(dtoAbteilungen, 2L, initAttributes);

		assertThat(dtoAbteilungen)
				.isInstanceOf(DTOAbteilungen.class)
				.hasFieldOrPropertyWithValue("ID", 2L)
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Schuljahresabschnitts_ID", idSchuljahresabschnitts);
	}

	@Test
	@DisplayName("GetById | Erfolg")
	void getByIdTestSuccess() throws ApiOperationException {
		final DTOAbteilungen dtoAbteilungen = createDTOAbteilungen();
		when(this.conn.queryByKey(DTOAbteilungen.class, dtoAbteilungen.ID)).thenReturn(dtoAbteilungen);

		assertThat(dut.getById(dtoAbteilungen.ID))
				.isInstanceOf(Abteilung.class)
				.hasFieldOrPropertyWithValue("id", dtoAbteilungen.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dtoAbteilungen.Bezeichnung)
				.hasFieldOrPropertyWithValue("idAbteilungsleiter", dtoAbteilungen.AbteilungsLeiter_ID)
				.hasFieldOrPropertyWithValue("sortierung", dtoAbteilungen.Sortierung)
				.hasFieldOrPropertyWithValue("durchwahl", dtoAbteilungen.Durchwahl)
				.hasFieldOrPropertyWithValue("durchwahl", dtoAbteilungen.Durchwahl)
				.hasFieldOrPropertyWithValue("raum", dtoAbteilungen.Raum);
	}

	@Test
	@DisplayName("GetById | Erfolg, überprüfe Klassenzuordnungen")
	void getByIdTestSuccessCheckKlassenzuordnungen() throws ApiOperationException {
		final DTOAbteilungen dtoAbteilungen = createDTOAbteilungen();
		when(this.conn.queryByKey(DTOAbteilungen.class, dtoAbteilungen.ID)).thenReturn(dtoAbteilungen);
		when(this.conn.queryList(DTOAbteilungsKlassen.QUERY_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, dtoAbteilungen.ID))
				.thenReturn(List.of(new DTOAbteilungsKlassen(1L, dtoAbteilungen.ID, 2L)));

		assertThat(dut.getById(dtoAbteilungen.ID).klassenzuordnungen)
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.allSatisfy(abteilungKlassenzuordnung -> {
					assertThat(abteilungKlassenzuordnung)
					.isInstanceOf(AbteilungKlassenzuordnung.class)
					.hasFieldOrPropertyWithValue("id", 1L)
					.hasFieldOrPropertyWithValue("idAbteilung", dtoAbteilungen.ID)
					.hasFieldOrPropertyWithValue("idKlasse", 2L);
		});
	}

	@Test
	@DisplayName("GetById | Falsche ID")
	void getByIdTestFailed() {
		final Throwable throwable = Assertions.catchThrowable(() -> dut.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Abteilung mit der ID 1 konnte nicht gefunden werden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolgreich mit JahresabschnittsId, überprüfe Abteilungen")
	void getAllTest() throws ApiOperationException {
		dut = new DataAbteilungen(conn, idSchuljahresabschnitts);
		final DTOAbteilungen dtoAbteilungen = createDTOAbteilungen();
		when(conn.queryList(DTOAbteilungen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOAbteilungen.class, idSchuljahresabschnitts))
				.thenReturn(List.of(dtoAbteilungen));
		final List<DTOAbteilungsKlassen> dtoAbteilungsKlassens = List.of(new DTOAbteilungsKlassen(1L, 2L, 3L));
		when(conn.queryList(DTOAbteilungsKlassen.QUERY_LIST_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, List.of(dtoAbteilungen.ID))).thenReturn(dtoAbteilungsKlassens);

		final List<Abteilung> result = this.dut.getAll();

		assertThat(result)
				.isInstanceOf(List.class)
				.hasSize(1)
				.isNotNull()
				.isNotEmpty()
				.allSatisfy(abteilung -> {
					assertThat(abteilung)
							.isInstanceOf(Abteilung.class)
							.hasFieldOrPropertyWithValue("id", dtoAbteilungen.ID)
							.hasFieldOrPropertyWithValue("bezeichnung", dtoAbteilungen.Bezeichnung)
							.hasFieldOrPropertyWithValue("idSchuljahresabschnitts", dtoAbteilungen.Schuljahresabschnitts_ID)
							.hasFieldOrPropertyWithValue("raum", dtoAbteilungen.Raum)
							.hasFieldOrPropertyWithValue("email", dtoAbteilungen.Email)
							.hasFieldOrPropertyWithValue("durchwahl", dtoAbteilungen.Durchwahl)
							.hasFieldOrPropertyWithValue("sortierung", dtoAbteilungen.Sortierung);
				});
	}

	@Test
	@DisplayName("getAll | Leere Liste")
	void getAllTestEmptyList() throws ApiOperationException {
		dut = new DataAbteilungen(conn, idSchuljahresabschnitts);
		when(conn.queryList(DTOAbteilungen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOAbteilungen.class, idSchuljahresabschnitts))
				.thenReturn(Collections.emptyList());

		final List<Abteilung> result = this.dut.getAll();

		assertThat(result).isEmpty();

	}


	@Test
	@DisplayName("getAll | Erfolgreich mit JahresabschnittsId, überprüfe AbteilungKlassenzurodnungen")
	void getAllTestAbteilungKlassenzuordnungen() throws ApiOperationException {
		dut = new DataAbteilungen(conn, idSchuljahresabschnitts);
		final DTOAbteilungen dtoAbteilungen = createDTOAbteilungen();
		dtoAbteilungen.ID = 2L;
		when(conn.queryList(DTOAbteilungen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOAbteilungen.class, idSchuljahresabschnitts))
				.thenReturn(List.of(dtoAbteilungen));
		when(conn.queryList(DTOAbteilungsKlassen.QUERY_LIST_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, List.of(2L))).thenReturn(List.of(new DTOAbteilungsKlassen(1L, 1L, 1L), new DTOAbteilungsKlassen(2L,
				2L, 2L), new DTOAbteilungsKlassen(3L, 1L, 3L)));

		final List<Abteilung> actual = this.dut.getAll();

		assertThat(actual.getFirst().klassenzuordnungen)
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.allSatisfy(abteilungKlassenzuordnung -> {
					assertThat(abteilungKlassenzuordnung)
							.isInstanceOf(AbteilungKlassenzuordnung.class)
							.hasFieldOrPropertyWithValue("id", 2L)
							.hasFieldOrPropertyWithValue("idAbteilung", 2L)
							.hasFieldOrPropertyWithValue("idKlasse", 2L);
				});
	}

	@Test
	@DisplayName("Map | Erfolgreich")
	void testMap() throws ApiOperationException {
		final DTOAbteilungen dtoAbteilungen = createDTOAbteilungen();

		assertThat(this.dut.map(dtoAbteilungen))
				.isInstanceOf(Abteilung.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Bezeichnung")
				.hasFieldOrPropertyWithValue("idSchuljahresabschnitts", 2L)
				.hasFieldOrPropertyWithValue("idAbteilungsleiter", 2L)
				.hasFieldOrPropertyWithValue("raum", "205")
				.hasFieldOrPropertyWithValue("email", "test@test.com")
				.hasFieldOrPropertyWithValue("durchwahl", "343")
				.hasFieldOrPropertyWithValue("sortierung", 3200);
	}

	@Test
	@DisplayName("Map | Erfolgreich mit null Werten")
	void testMapNullValues() throws ApiOperationException {
		final DTOAbteilungen dtoAbteilungen = createDTOAbteilungen();
		dtoAbteilungen.Raum = null;
		dtoAbteilungen.Email = null;
		dtoAbteilungen.Durchwahl = null;
		dtoAbteilungen.Bezeichnung = null;
		dtoAbteilungen.Sortierung = null;

		assertThat(this.dut.map(dtoAbteilungen))
				.isInstanceOf(Abteilung.class)
				.hasFieldOrPropertyWithValue("bezeichnung", "")
				.hasFieldOrPropertyWithValue("raum", null)
				.hasFieldOrPropertyWithValue("email", null)
				.hasFieldOrPropertyWithValue("durchwahl", null)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@ParameterizedTest
	@DisplayName("MapAttribute | Erfolgreiches Mapping und unbekannte Daten")
	@MethodSource("provideMappingAttributes")
	void testMapAttribute(final String key, final Object value) {
		final DTOAbteilungen expectedDTO = createDTOAbteilungen();

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "bezeichnung" -> assertThat(expectedDTO.Bezeichnung).isEqualTo(value);
			case "idSchuljahresabschnitts" -> assertThat(expectedDTO.Schuljahresabschnitts_ID).isEqualTo(value);
			case "idAbteilungsleiter" -> assertThat(expectedDTO.AbteilungsLeiter_ID).isEqualTo(value);
			case "raum" -> assertThat(expectedDTO.Raum).isEqualTo(value);
			case "email" -> assertThat(expectedDTO.Email).isEqualTo(value);
			case "durchwahl" -> assertThat(expectedDTO.Durchwahl).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.Sortierung).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Das Patchen des Attributes test wird nicht unterstützt.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}

	}

	@ParameterizedTest
	@DisplayName("mapAttribute | fehlerhaftes Mapping der ID")
	@MethodSource("provideMappingAttributes")
	void testMapAttributeFalseID(final String key, final Object value) {
		final DTOAbteilungen expectedDTO = createDTOAbteilungen();
		expectedDTO.ID = 14L;

		final Throwable throwable = ThrowableAssert.catchThrowable(() -> this.dut.mapAttribute(expectedDTO, key, value, null));

		if (key.equals("id"))
			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Id 1 der PatchMap ist ungleich der id 14 vom Dto")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 1L),
				arguments("bezeichnung", "Bezeichnung"),
				arguments("idSchuljahresabschnitts", 2L),
				arguments("idAbteilungsleiter", 2L),
				arguments("raum", "205"),
				arguments("email", "test@test.com"),
				arguments("durchwahl", "343"),
				arguments("sortierung", 3200),
				arguments("test", 3000)
		);
	}

	private DTOAbteilungen createDTOAbteilungen() {
		final DTOAbteilungen dtoAbteilungen = new DTOAbteilungen(1L, "Bezeichnung", idSchuljahresabschnitts);
		dtoAbteilungen.Sichtbar = true;
		dtoAbteilungen.AbteilungsLeiter_ID = 2L;
		dtoAbteilungen.Sortierung = 3200;
		dtoAbteilungen.Durchwahl = "343";
		dtoAbteilungen.Email = "test@test.com";
		dtoAbteilungen.Raum = "205";
		return dtoAbteilungen;
	}

	private List<DTOAbteilungsKlassen> getDTOAbteilungsklassen() {
		return List.of(new DTOAbteilungsKlassen(1L, 1L, 1L), new DTOAbteilungsKlassen(2L, 1L, 2L));
	}
}
