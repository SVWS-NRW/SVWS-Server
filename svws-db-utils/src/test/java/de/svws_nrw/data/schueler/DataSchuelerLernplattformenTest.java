package de.svws_nrw.data.schueler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerLernplattform;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
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

@DisplayName("Diese Testklasse testet die Klasse DataSchuelerLernplattformen")
@ExtendWith(MockitoExtension.class)
class DataSchuelerLernplattformenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchuelerLernplattformen dataSchuelerLernplattformen;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		dataSchuelerLernplattformen = new DataSchuelerLernplattformen(conn, 1L);
		final DTOSchuelerLernplattform dto = getDTOSchuelerLernplattform();
		final Long[] idArray = new Long[]{1L, 2L};
		final Map<String, Object> initAttributes = new HashMap<>();

		dataSchuelerLernplattformen.initDTO(dto, idArray, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("SchuelerID", 1L)
				.hasFieldOrPropertyWithValue("LernplattformID", 2L)
				.hasFieldOrPropertyWithValue("EinwilligungAbgefragt", false)
				.hasFieldOrPropertyWithValue("EinwilligungNutzung", false)
				.hasFieldOrPropertyWithValue("EinwilligungAudiokonferenz", false)
				.hasFieldOrPropertyWithValue("EinwilligungVideokonferenz", false);
	}


	@Test
	@DisplayName("getByID | Erfolg")
	void getByIDTest() throws ApiOperationException {
		final var dtoSchuelerLernplattform = getDTOSchuelerLernplattform();
		when(this.conn.queryByKey(DTOSchuelerLernplattform.class, dtoSchuelerLernplattform.SchuelerID, dtoSchuelerLernplattform.LernplattformID)).thenReturn(dtoSchuelerLernplattform);

		assertThat(dataSchuelerLernplattformen.getById(new Long[] {dtoSchuelerLernplattform.SchuelerID, dtoSchuelerLernplattform.LernplattformID}))
				.isInstanceOf(SchuelerLernplattform.class)
				.hasFieldOrPropertyWithValue("idSchueler", dtoSchuelerLernplattform.SchuelerID);
	}

	@Test
	@DisplayName("getByID | Lernplattform nicht gefunden")
	void getByIDTest_LernplattformNotFound() {
		when(this.conn.queryByKey(DTOSchuelerLernplattform.class, null, null)).thenReturn(null);

		final var throwable = catchThrowable(() -> dataSchuelerLernplattformen.getById(new Long[] {null, null}));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Lernplattform mit SchuelerID null und der LernplattformID null wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getID | Erfolg")
	void getIDTest() throws ApiOperationException {
		final Long[] idArray = dataSchuelerLernplattformen.getID(attributesMap);

		assertThat(idArray).containsExactly(1L, 1L);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() throws ApiOperationException {
		final DTOSchuelerLernplattform dto = getDTOSchuelerLernplattform();
		when(conn.queryAll(DTOSchuelerLernplattform.class)).thenReturn(List.of(dto));

		assertThat(dataSchuelerLernplattformen.getAll())
				.isNotNull()
				.hasSize(1)
				.first()
				.satisfies(lernplattform -> assertThat(lernplattform)
						.hasFieldOrPropertyWithValue("idSchueler", 1L)
						.hasFieldOrPropertyWithValue("idLernplattform", 1L)
						.hasFieldOrPropertyWithValue("einwilligungAbgefragt", false)
						.hasFieldOrPropertyWithValue("einwilligungNutzung", false)
						.hasFieldOrPropertyWithValue("einwilligungAudiokonferenz", false)
						.hasFieldOrPropertyWithValue("einwilligungVideokonferenz", false));
	}

	@Test
	@DisplayName("getDatabaseDTOById | Erfolg")
	void getDatabaseDTOByIdTest() {
		final DTOSchuelerLernplattform dto = getDTOSchuelerLernplattform();
		when(conn.queryByKey(DTOSchuelerLernplattform.class, dto.SchuelerID, dto.LernplattformID)).thenReturn(dto);

		assertThat(dataSchuelerLernplattformen.getDatabaseDTOByID(new Long[]{dto.SchuelerID, dto.LernplattformID}))
				.isNotNull()
				.hasFieldOrPropertyWithValue("SchuelerID", dto.SchuelerID)
				.hasFieldOrPropertyWithValue("LernplattformID", dto.LernplattformID)
				.hasFieldOrPropertyWithValue("EinwilligungAbgefragt", dto.EinwilligungAbgefragt)
				.hasFieldOrPropertyWithValue("EinwilligungNutzung", dto.EinwilligungNutzung)
				.hasFieldOrPropertyWithValue("EinwilligungAudiokonferenz", dto.EinwilligungAudiokonferenz)
				.hasFieldOrPropertyWithValue("EinwilligungVideokonferenz", dto.EinwilligungVideokonferenz);
	}

	@Test
	@DisplayName("checkBeforeCreation | Erfolg")
	void checkBeforeCreationTest() throws ApiOperationException {
		when(conn.queryByKey(DTOSchuelerLernplattform.class, 1L, 1L)).thenReturn(null);

		dataSchuelerLernplattformen.checkBeforeCreation(new Long[]{1L, 1L}, attributesMap);
	}

	@Test
	@DisplayName("checkBeforeCreation | Fehlende Attribute")
	void checkBeforeCreationTest_MissingAttributes() {
		final Map<String, Object> initAttributes = new HashMap<>();
		initAttributes.put("idSchueler", null);
		initAttributes.put("idLernplattform", 1L);

		final var throwable = catchThrowable(() ->
				dataSchuelerLernplattformen.checkBeforeCreation(new Long[]{null, 1L}, initAttributes)
		);

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut idSchueler: Der Wert null ist nicht erlaubt");
	}

	@Test
	@DisplayName("checkBeforeCreation | Doppelte Einwilligung")
	void checkBeforeCreationTest_ExistingEntry() {
		final DTOSchuelerLernplattform existingEntry = getDTOSchuelerLernplattform();
		when(conn.queryByKey(DTOSchuelerLernplattform.class, 1L, 1L)).thenReturn(existingEntry);

		final var throwable = catchThrowable(() ->
				dataSchuelerLernplattformen.checkBeforeCreation(new Long[]{1L, 1L}, attributesMap)
		);

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es existiert bereits eine Einwilligung für die Kombination aus Schueler-ID 1 und Lernplattform-ID 1.");
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final var dtoSchuelerLernplattform = getDTOSchuelerLernplattform();

		assertThat(this.dataSchuelerLernplattformen.map(dtoSchuelerLernplattform))
				.isInstanceOf(SchuelerLernplattform.class)
				.hasFieldOrPropertyWithValue("idSchueler", 1L)
				.hasFieldOrPropertyWithValue("idLernplattform", 1L)
				.hasFieldOrPropertyWithValue("einwilligungAbgefragt", false)
				.hasFieldOrPropertyWithValue("einwilligungNutzung", false)
				.hasFieldOrPropertyWithValue("einwilligungAudiokonferenz", false)
				.hasFieldOrPropertyWithValue("einwilligungVideokonferenz", false);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOSchuelerLernplattform();

		final var throwable = catchThrowable(() -> this.dataSchuelerLernplattformen.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "idSchueler" -> assertThat(expectedDTO.SchuelerID).isEqualTo(value);
			case "idLernplattform" -> assertThat(expectedDTO.LernplattformID).isEqualTo(value);
			case "einwilligungAbgefragt" -> assertThat(expectedDTO.EinwilligungAbgefragt).isEqualTo(value);
			case "einwilligungNutzung" -> assertThat(expectedDTO.EinwilligungNutzung).isEqualTo(value);
			case "einwilligungAudiokonferenz" -> assertThat(expectedDTO.EinwilligungAudiokonferenz).isEqualTo(value);
			case "einwilligungVideokonferenz" -> assertThat(expectedDTO.EinwilligungVideokonferenz).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(key))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("idSchueler", 1L),
				arguments("idLernplattform", 1L),
				arguments("einwilligungAbgefragt", false),
				arguments("einwilligungNutzung", false),
				arguments("einwilligungAudiokonferenz", false),
				arguments("einwilligungVideokonferenz", false)
		);
	}

	private static final Map<String, Object> attributesMap = Map.of(
			"idSchueler", 1L,
			"idLernplattform", 1L
	);

	private DTOSchuelerLernplattform getDTOSchuelerLernplattform() {
		final var dtoLehrerDatenschutz = new DTOSchuelerLernplattform(1L, 1L, false, false, false, false);
		dtoLehrerDatenschutz.SchuelerID = 1L;
		dtoLehrerDatenschutz.LernplattformID = 1L;
		dtoLehrerDatenschutz.EinwilligungAbgefragt = false;
		dtoLehrerDatenschutz.EinwilligungNutzung = false;
		dtoLehrerDatenschutz.EinwilligungAudiokonferenz = false;
		dtoLehrerDatenschutz.EinwilligungVideokonferenz = false;
		return dtoLehrerDatenschutz;
	}
}
