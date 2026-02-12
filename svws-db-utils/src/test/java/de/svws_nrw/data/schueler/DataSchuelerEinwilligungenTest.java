package de.svws_nrw.data.schueler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerEinwilligung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.InstanceOfAssertFactories;
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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataSchuelerEinwilligungen")
@ExtendWith(MockitoExtension.class)
class DataSchuelerEinwilligungenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchuelerEinwilligungen dataSchuelerEinwilligungen;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idSchueler")
	void setAttributesRequiredOnCreationIdSchueler() {
		assertThatException()
				.isThrownBy(() -> this.dataSchuelerEinwilligungen.add(Map.of("test", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idSchueler: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idEinwilligungsart")
	void setAttributesRequiredOnCreationIdEinwilligungsart() {
		assertThatException()
				.isThrownBy(() -> this.dataSchuelerEinwilligungen.add(Map.of("idSchueler", 1L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idEinwilligungsart: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: idSchueler")
	void setAttributesNotPatchableIdSchueler() {
		when(this.conn.queryByKey(DTOSchuelerDatenschutz.class, (Object) new Long[]{1L, 2L})).thenReturn(mock(DTOSchuelerDatenschutz.class));

		assertThatException()
				.isThrownBy(() -> this.dataSchuelerEinwilligungen.patch(new Long[]{1L, 2L}, Map.of("idSchueler", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: idSchueler.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: idEinwilligungsart")
	void setAttributesNotPatchableIdEinwilligungsart() {
		when(this.conn.queryByKey(DTOSchuelerDatenschutz.class, (Object) new Long[]{1L, 2L})).thenReturn(mock(DTOSchuelerDatenschutz.class));

		assertThatException()
				.isThrownBy(() -> this.dataSchuelerEinwilligungen.patch(new Long[]{1L, 2L}, Map.of("idEinwilligungsart", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: idEinwilligungsart.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		final DTOSchuelerDatenschutz dto = getDTOSchuelerDatenschutz();
		final Long[] idArray = new Long[] { 3L, 4L };

		this.dataSchuelerEinwilligungen.initDTO(dto, idArray, Collections.emptyMap());

		assertThat(dto)
				.hasFieldOrPropertyWithValue("Schueler_ID", 3L)
				.hasFieldOrPropertyWithValue("Datenschutz_ID", 4L);
	}


	@Test
	@DisplayName("getByID | Erfolg")
	void getByIDTest() throws ApiOperationException {
		final var dtoSchuelerDatenschutz = getDTOSchuelerDatenschutz();
		when(this.conn.queryByKey(DTOSchuelerDatenschutz.class, dtoSchuelerDatenschutz.Schueler_ID, dtoSchuelerDatenschutz.Datenschutz_ID))
				.thenReturn(dtoSchuelerDatenschutz);

		assertThat(dataSchuelerEinwilligungen.getById(new Long[] { dtoSchuelerDatenschutz.Schueler_ID, dtoSchuelerDatenschutz.Schueler_ID }))
				.isInstanceOf(SchuelerEinwilligung.class)
				.hasFieldOrPropertyWithValue("idSchueler", dtoSchuelerDatenschutz.Schueler_ID);
	}

	@Test
	@DisplayName("getByID | Einwilligung nicht gefunden")
	void getByIDTest_EinwilligungNotFound() {
		when(this.conn.queryByKey(DTOSchuelerDatenschutz.class, null, null)).thenReturn(null);

		final var throwable = catchThrowable(() -> dataSchuelerEinwilligungen.getById(new Long[] { null, null }));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Einwilligung mit SchuelerID null und der EinwilligungsartID null wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.dataSchuelerEinwilligungen.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID der Einwilligungsart darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getID | Erfolg")
	void getIDTest() throws ApiOperationException {
		final Long[] idArray = dataSchuelerEinwilligungen.getID(attributesMap);

		assertThat(idArray).containsExactly(1L, 1L);
	}

	@Test
	@DisplayName("getAllByIdSchueler | Erfolg")
	void getAllByIdSchueler() throws ApiOperationException {
		final DTOSchuelerDatenschutz dto = new DTOSchuelerDatenschutz(1L, 1L, false, false);
		when(conn.queryList(DTOSchuelerDatenschutz.QUERY_BY_SCHUELER_ID, DTOSchuelerDatenschutz.class, 1L)).thenReturn(List.of(dto));

		assertThat(dataSchuelerEinwilligungen.getAllByIdSchueler(1L))
				.isInstanceOf(Response.class)
				.extracting(r -> r.getEntity())
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.isNotNull()
				.hasSize(1)
				.first()
				.satisfies(schuelerEinwilligung -> assertThat(schuelerEinwilligung)
						.hasFieldOrPropertyWithValue("idSchueler", 1L)
						.hasFieldOrPropertyWithValue("idEinwilligungsart", 1L)
						.hasFieldOrPropertyWithValue("status", false)
						.hasFieldOrPropertyWithValue("abgefragt", false));
	}

	@Test
	@DisplayName("getDatabaseDTOById | Erfolg")
	void getDatabaseDTOByIdTest() {
		final DTOSchuelerDatenschutz dto = getDTOSchuelerDatenschutz();
		when(conn.queryByKey(DTOSchuelerDatenschutz.class, dto.Schueler_ID, dto.Datenschutz_ID)).thenReturn(dto);

		assertThat(dataSchuelerEinwilligungen.getDatabaseDTOByID(new Long[] { dto.Schueler_ID, dto.Datenschutz_ID }))
				.isNotNull()
				.hasFieldOrPropertyWithValue("Schueler_ID", dto.Schueler_ID)
				.hasFieldOrPropertyWithValue("Datenschutz_ID", dto.Datenschutz_ID)
				.hasFieldOrPropertyWithValue("Status", dto.Status)
				.hasFieldOrPropertyWithValue("Abgefragt", dto.Abgefragt);
	}

	@Test
	@DisplayName("checkBeforeCreation | Erfolg")
	void checkBeforeCreationTest() throws ApiOperationException {
		when(conn.queryByKey(DTOSchuelerDatenschutz.class, 1L, 1L)).thenReturn(null);

		dataSchuelerEinwilligungen.checkBeforeCreation(new Long[] { 1L, 1L }, attributesMap);
	}

	@Test
	@DisplayName("checkBeforeCreation | Fehlende Attribute")
	void checkBeforeCreationTest_MissingAttributes() {
		final Map<String, Object> initAttributes = new HashMap<>();
		initAttributes.put("idSchueler", null);
		initAttributes.put("idEinwilligungsart", 1L);

		final var throwable = catchThrowable(() -> dataSchuelerEinwilligungen.checkBeforeCreation(new Long[] { null, 1L }, initAttributes)
		);

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut idSchueler: Der Wert null ist nicht erlaubt");
	}

	@Test
	@DisplayName("checkBeforeCreation | Doppelte Einwilligung")
	void checkBeforeCreationTest_ExistingEntry() {
		final DTOSchuelerDatenschutz existingEntry = getDTOSchuelerDatenschutz();
		when(conn.queryByKey(DTOSchuelerDatenschutz.class, 1L, 1L)).thenReturn(existingEntry);

		final var throwable = catchThrowable(() -> dataSchuelerEinwilligungen.checkBeforeCreation(new Long[] { 1L, 1L }, attributesMap)
		);

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es existiert bereits eine Einwilligung für die Kombination aus Schüler-ID 1 und Einwilligungsart-ID 1.");
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final var dtoLehrerDatenschutz = getDTOSchuelerDatenschutz();

		assertThat(this.dataSchuelerEinwilligungen.map(dtoLehrerDatenschutz))
				.isInstanceOf(SchuelerEinwilligung.class)
				.hasFieldOrPropertyWithValue("idSchueler", 1L)
				.hasFieldOrPropertyWithValue("idEinwilligungsart", 1L)
				.hasFieldOrPropertyWithValue("status", false)
				.hasFieldOrPropertyWithValue("abgefragt", false);

	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOSchuelerDatenschutz();

		final var throwable = catchThrowable(() -> this.dataSchuelerEinwilligungen.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "idSchueler" -> assertThat(expectedDTO.Schueler_ID).isEqualTo(value);
			case "idEinwilligungsart" -> assertThat(expectedDTO.Datenschutz_ID).isEqualTo(value);
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
				arguments("idSchueler", 1L),
				arguments("idEinwilligungsart", 1L),
				arguments("status", false),
				arguments("abgefragt", false)
		);
	}

	private static final Map<String, Object> attributesMap = Map.of(
			"idSchueler", 1L,
			"idEinwilligungsart", 1L
	);

	private DTOSchuelerDatenschutz getDTOSchuelerDatenschutz() {
		final var dtoSchuelerDatenschutz = new DTOSchuelerDatenschutz(1L, 1L, false, false);
		dtoSchuelerDatenschutz.Schueler_ID = 1L;
		dtoSchuelerDatenschutz.Datenschutz_ID = 1L;
		dtoSchuelerDatenschutz.Status = false;
		dtoSchuelerDatenschutz.Abgefragt = false;
		return dtoSchuelerDatenschutz;
	}

}
