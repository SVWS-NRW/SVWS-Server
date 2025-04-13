package de.svws_nrw.data.lehrer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.lehrer.LehrerEinwilligung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
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
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataLehrerEinwilligungen")
@ExtendWith(MockitoExtension.class)
class DataLehrerEinwilligungenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataLehrerEinwilligungen dataLehrerEinwilligungen;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		dataLehrerEinwilligungen = new DataLehrerEinwilligungen(conn, 1L);
		final DTOLehrerDatenschutz dto = getDTOLehrerDatenschutz();
		final Long[] idArray = new Long[]{1L, 2L};
		final Map<String, Object> initAttributes = new HashMap<>();

		dataLehrerEinwilligungen.initDTO(dto, idArray, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("LehrerID", 1L)
				.hasFieldOrPropertyWithValue("DatenschutzID", 2L)
				.hasFieldOrPropertyWithValue("Status", false)
				.hasFieldOrPropertyWithValue("Abgefragt", false);
	}


	@Test
	@DisplayName("getByID | Erfolg")
	void getByIDTest() throws ApiOperationException {
		final var dtoLehrerDatenschutz = getDTOLehrerDatenschutz();
		when(this.conn.queryByKey(DTOLehrerDatenschutz.class, dtoLehrerDatenschutz.LehrerID, dtoLehrerDatenschutz.DatenschutzID)).thenReturn(dtoLehrerDatenschutz);

		assertThat(dataLehrerEinwilligungen.getById(new Long[] {dtoLehrerDatenschutz.LehrerID, dtoLehrerDatenschutz.DatenschutzID}))
				.isInstanceOf(LehrerEinwilligung.class)
				.hasFieldOrPropertyWithValue("idLehrer", dtoLehrerDatenschutz.LehrerID);
	}

	@Test
	@DisplayName("getByID | Einwilligung nicht gefunden")
	void getByIDTest_EinwilligungNotFound() {
		when(this.conn.queryByKey(DTOLehrerDatenschutz.class, null, null)).thenReturn(null);

		final var throwable = catchThrowable(() -> dataLehrerEinwilligungen.getById(new Long[] {null, null}));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Einwilligung mit LehrerID null und der EinwilligungsartID null wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getID | Erfolg")
	void getIDTest() throws ApiOperationException {
		final Long[] idArray = dataLehrerEinwilligungen.getID(attributesMap);

		assertThat(idArray).containsExactly(1L, 1L);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() throws ApiOperationException {
		final DTOLehrerDatenschutz dto = new DTOLehrerDatenschutz(1L, 1L, false, false);
		when(conn.queryAll(DTOLehrerDatenschutz.class)).thenReturn(List.of(dto));

		assertThat(dataLehrerEinwilligungen.getAll())
				.isNotNull()
				.hasSize(1)
				.first()
				.satisfies(einwilligung -> assertThat(einwilligung)
						.hasFieldOrPropertyWithValue("idLehrer", 1L)
						.hasFieldOrPropertyWithValue("idEinwilligungsart", 1L)
						.hasFieldOrPropertyWithValue("istZugestimmt", false)
						.hasFieldOrPropertyWithValue("istAbgefragt", false));
	}

	@Test
	@DisplayName("getDatabaseDTOById | Erfolg")
	void getDatabaseDTOByIdTest() {
		final DTOLehrerDatenschutz dto = getDTOLehrerDatenschutz();
		when(conn.queryByKey(DTOLehrerDatenschutz.class, dto.LehrerID, dto.DatenschutzID)).thenReturn(dto);

		assertThat(dataLehrerEinwilligungen.getDatabaseDTOByID(new Long[]{dto.LehrerID, dto.DatenschutzID}))
				.isNotNull()
				.hasFieldOrPropertyWithValue("LehrerID", dto.LehrerID)
				.hasFieldOrPropertyWithValue("DatenschutzID", dto.DatenschutzID)
				.hasFieldOrPropertyWithValue("Status", dto.Status)
				.hasFieldOrPropertyWithValue("Abgefragt", dto.Abgefragt);
	}

	@Test
	@DisplayName("checkBeforeCreation | Erfolg")
	void checkBeforeCreationTest() throws ApiOperationException {
		when(conn.queryByKey(DTOLehrerDatenschutz.class, 1L, 1L)).thenReturn(null);

		dataLehrerEinwilligungen.checkBeforeCreation(new Long[]{1L, 1L}, attributesMap);
	}

	@Test
	@DisplayName("checkBeforeCreation | Fehlende Attribute")
	void checkBeforeCreationTest_MissingAttributes() {
		final Map<String, Object> initAttributes = new HashMap<>();
		initAttributes.put("idLehrer", null);
		initAttributes.put("idEinwilligungsart", 1L);

		final var throwable = catchThrowable(() ->
				dataLehrerEinwilligungen.checkBeforeCreation(new Long[]{null, 1L}, initAttributes)
		);

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut idLehrer: Der Wert null ist nicht erlaubt");
	}

	@Test
	@DisplayName("checkBeforeCreation | Doppelte Einwilligung")
	void checkBeforeCreationTest_ExistingEntry() {
		final DTOLehrerDatenschutz existingEntry = getDTOLehrerDatenschutz();
		when(conn.queryByKey(DTOLehrerDatenschutz.class, 1L, 1L)).thenReturn(existingEntry);

		final var throwable = catchThrowable(() ->
				dataLehrerEinwilligungen.checkBeforeCreation(new Long[]{1L, 1L}, attributesMap)
		);

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es existiert bereits eine Einwilligung für die Kombination aus Lehrer-ID 1 und Einwilligungsart-ID 1.");
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final var dtoLehrerDatenschutz = getDTOLehrerDatenschutz();

		assertThat(this.dataLehrerEinwilligungen.map(dtoLehrerDatenschutz))
				.isInstanceOf(LehrerEinwilligung.class)
				.hasFieldOrPropertyWithValue("idLehrer", 1L)
				.hasFieldOrPropertyWithValue("idEinwilligungsart", 1L)
				.hasFieldOrPropertyWithValue("istZugestimmt", false)
				.hasFieldOrPropertyWithValue("istAbgefragt", false);

	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOLehrerDatenschutz();

		final var throwable = catchThrowable(() -> this.dataLehrerEinwilligungen.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "idLehrer" -> assertThat(expectedDTO.LehrerID).isEqualTo(value);
			case "idEinwilligungsart" -> assertThat(expectedDTO.DatenschutzID).isEqualTo(value);
			case "status" -> assertThat(expectedDTO.Status).isEqualTo(value);
			case "abgefragt" -> assertThat(expectedDTO.Abgefragt).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(key))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("idLehrer", 1L),
				arguments("idEinwilligungsart", 1L),
				arguments("status", false),
				arguments("abgefragt", false)
		);
	}

	private static final Map<String, Object> attributesMap = Map.of(
			"idLehrer", 1L,
			"idEinwilligungsart", 1L
	);

	private DTOLehrerDatenschutz getDTOLehrerDatenschutz() {
		final var dtoLehrerDatenschutz = new DTOLehrerDatenschutz(1L, 1L, false, false);
		dtoLehrerDatenschutz.LehrerID = 1L;
		dtoLehrerDatenschutz.DatenschutzID = 1L;
		dtoLehrerDatenschutz.Status = false;
		dtoLehrerDatenschutz.Abgefragt = false;
		return dtoLehrerDatenschutz;
	}
}
