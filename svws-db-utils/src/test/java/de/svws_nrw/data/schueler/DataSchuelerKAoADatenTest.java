package de.svws_nrw.data.schueler;


import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataSchuelerKAoADaten}.
 */
@DisplayName("Diese Klasse testet die Klasse DataSchuelerKAoADaten")
@TestMethodOrder(MethodOrderer.MethodName.class)
class DataSchuelerKAoADatenTest {

	public static final String STATUS = "status";

	public static final String ID = "id";
	public static final String SCHULJAHRESABSCHNTT = "idSchuljahresabschnitt";
	public static final String JAHRGANG = "idJahrgang";
	public static final String KATEGORIE = "idKategorie";
	public static final String MERKMAL = "idMerkmal";
	public static final String ZUSATZMERKMAL = "idZusatzmerkmal";
	public static final String ANSCHLUSSOPTION = "idAnschlussoption";
	public static final String BERUFSFELD = "idBerufsfeld";
	public static final String EBENE_4 = "idEbene4";
	public static final String BEMERKUNG = "bemerkung";

	private final DBEntityManager conn = mock(DBEntityManager.class);

	private DataSchuelerKAoADaten dataSchuelerKAoADaten;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() throws ApiOperationException {
		final Long schuelerId = 123L;
		when(conn.queryByKey(DTOSchueler.class, schuelerId)).thenReturn(mock(DTOSchueler.class));
		dataSchuelerKAoADaten = new DataSchuelerKAoADaten(conn, schuelerId);
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final var schuelerKAoADaten = new DTOSchuelerKAoADaten(1L, 1L, 1L);

		this.dataSchuelerKAoADaten.initDTO(schuelerKAoADaten, 2L, null);

		assertThat(schuelerKAoADaten.id).isEqualTo(2L);
	}

	@Test
	@DisplayName("constructor | wrong schuelerID")
	void constructorTest_WrongSchuelerID() {
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(null);
		final var throwable = catchThrowable(() -> dataSchuelerKAoADaten = new DataSchuelerKAoADaten(conn, 1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine SchuelerDaten mit der ID 1 gefunden")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		dataSchuelerKAoADaten = spy(dataSchuelerKAoADaten);
		final var data = new DTOSchuelerKAoADaten(123L, 7L, 10L);
		data.jahrgang = "08";
		data.idMerkmal = 51L;
		data.idZusatzmerkmal = 68L;
		data.idAnschlussoption = 22L;
		data.idBerufsfeld = 12L;
		data.idEbene4 = 11L;
		data.bemerkung = "schule ist toll";
		when(this.conn.queryByKey(DTOSchuelerKAoADaten.class, 123L)).thenReturn(data);
		final var jahrgaengeKatalogEintrag = new JahrgaengeKatalogEintrag();
		jahrgaengeKatalogEintrag.id = 1L;
		doReturn(jahrgaengeKatalogEintrag).when(dataSchuelerKAoADaten).getJahrgaengeKatalogEintrag(any());
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(lernabschnittsdaten);

		assertThat(dataSchuelerKAoADaten.getById(123L))
				.isInstanceOf(SchuelerKAoADaten.class)
				.hasFieldOrPropertyWithValue(ID, 123L);
	}

	@Test
	@DisplayName("getById | wrong Id")
	void getByIdTest_wrongId() {
		when(this.conn.queryByKey(any(), any())).thenReturn(null);

		final var throwable = catchThrowable(() -> dataSchuelerKAoADaten.getById(123L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine SchuelerKAoADaten mit der ID 123 gefunden")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | erfolgreiches mapping")
	void mapTest() throws ApiOperationException {
		dataSchuelerKAoADaten = spy(dataSchuelerKAoADaten);
		final var dtoSchuelerKAoADaten = new DTOSchuelerKAoADaten(123L, 123L, 10L);
		dtoSchuelerKAoADaten.idMerkmal = 51L;
		dtoSchuelerKAoADaten.idZusatzmerkmal = 68L;
		dtoSchuelerKAoADaten.idAnschlussoption = 22L;
		dtoSchuelerKAoADaten.idBerufsfeld = 12L;
		dtoSchuelerKAoADaten.idEbene4 = 11L;
		dtoSchuelerKAoADaten.jahrgang = "08";
		dtoSchuelerKAoADaten.bemerkung = "schule ist toll";
		final var jahrgaengeKatalogEintrag = new JahrgaengeKatalogEintrag();
		jahrgaengeKatalogEintrag.id = 8000000L;
		doReturn(jahrgaengeKatalogEintrag).when(dataSchuelerKAoADaten).getJahrgaengeKatalogEintrag(any());
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(List.of(lernabschnittsdaten));
		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 123L))
				.thenReturn(lernabschnittsdaten);

		assertThat(this.dataSchuelerKAoADaten.map(dtoSchuelerKAoADaten))
				.isInstanceOf(SchuelerKAoADaten.class)
				.hasFieldOrPropertyWithValue(ID, 123L)
				.hasFieldOrPropertyWithValue(SCHULJAHRESABSCHNTT, 7L)
				.hasFieldOrPropertyWithValue(KATEGORIE, 10L)
				.hasFieldOrPropertyWithValue(MERKMAL, 51L)
				.hasFieldOrPropertyWithValue(ZUSATZMERKMAL, 68L)
				.hasFieldOrPropertyWithValue(ANSCHLUSSOPTION, 22L)
				.hasFieldOrPropertyWithValue(BERUFSFELD, 12L)
				.hasFieldOrPropertyWithValue(EBENE_4, 11L)
				.hasFieldOrPropertyWithValue(JAHRGANG, 8000000L)
				.hasFieldOrPropertyWithValue(BEMERKUNG, "schule ist toll");
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = new DTOSchuelerKAoADaten(0, 7L, 0);
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 2L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, 2L))
				.thenReturn(List.of(lernabschnittsdaten));

		final var throwable = catchThrowable(() -> this.dataSchuelerKAoADaten.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case JAHRGANG -> assertThat(expectedDTO.jahrgang).isEqualTo("08");
			case SCHULJAHRESABSCHNTT -> assertThat(expectedDTO.idLernabschnitt).isEqualTo(value);
			case KATEGORIE -> assertThat(expectedDTO.idKategorie).isEqualTo(value);
			case MERKMAL -> assertThat(expectedDTO.idMerkmal).isEqualTo(value);
			case ZUSATZMERKMAL -> assertThat(expectedDTO.idZusatzmerkmal).isEqualTo(value);
			case EBENE_4 -> assertThat(expectedDTO.idEbene4).isEqualTo(value);
			case ANSCHLUSSOPTION -> assertThat(expectedDTO.idAnschlussoption).isEqualTo(value);
			case BERUFSFELD -> assertThat(expectedDTO.idBerufsfeld).isEqualTo(value);
			case BEMERKUNG -> assertThat(expectedDTO.bemerkung).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | Value null -> Exception")
	void mapAttributeTest_ValueNull() {
		final var throwable = catchThrowable(() -> this.dataSchuelerKAoADaten.mapAttribute(mock(DTOSchuelerKAoADaten.class), KATEGORIE, null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut Kategorie: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getAll | erfolg")
	void getAllTest() throws ApiOperationException {
		dataSchuelerKAoADaten = spy(dataSchuelerKAoADaten);
		final var dtoSchuelerKAoADaten = new DTOSchuelerKAoADaten(123L, 7L, 10L);
		dtoSchuelerKAoADaten.idMerkmal = 51L;
		dtoSchuelerKAoADaten.idZusatzmerkmal = 51L;
		final DTOSchuelerLernabschnittsdaten lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(lernabschnittsdaten);
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID, DTOSchuelerLernabschnittsdaten.class, 123L))
				.thenReturn(List.of(lernabschnittsdaten));
		when(this.conn.queryList(any(), eq(DTOSchuelerKAoADaten.class), any())).thenReturn(List.of(dtoSchuelerKAoADaten));
		final var jahrgaengeKatalogEintrag = new JahrgaengeKatalogEintrag();
		jahrgaengeKatalogEintrag.id = 8000000L;
		doReturn(jahrgaengeKatalogEintrag).when(dataSchuelerKAoADaten).getJahrgaengeKatalogEintrag(any());

		assertThat(((List<?>) this.dataSchuelerKAoADaten.getAll()).getFirst())
				.isInstanceOf(SchuelerKAoADaten.class)
				.hasFieldOrPropertyWithValue(ID, 123L)
				.hasFieldOrPropertyWithValue(SCHULJAHRESABSCHNTT, 7L)
				.hasFieldOrPropertyWithValue(KATEGORIE, 10L)
				.hasFieldOrPropertyWithValue(MERKMAL, 51L);
	}

	@ParameterizedTest
	@DisplayName("checkBeforeCreation | Erfolg")
	@MethodSource("provideSchuelerKAoADatenForSuccess")
	void checkBeforeCreation_Success(final Map<String, Object> initAttributes) {
		final var schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2024;
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));
		when(this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(7L)).thenReturn(schuljahresabschnitt);
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(lernabschnittsdaten);
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(List.of(lernabschnittsdaten));
		assertDoesNotThrow(() -> dataSchuelerKAoADaten.checkBeforeCreation(1L, initAttributes));
	}

	@Test
	@DisplayName("checkBeforePatch | Erfolg")
	void checkBeforePatch() {
		final var schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2024;
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));
		when(this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(7L)).thenReturn(schuljahresabschnitt);
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(lernabschnittsdaten);
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(List.of(lernabschnittsdaten));
		final var dto = new DTOSchuelerKAoADaten(1L, 7L, 10L);
		dto.jahrgang = "08";
		dto.idMerkmal = 51L;
		dto.idZusatzmerkmal = 68L;
		dto.bemerkung = "schule ist toll!";

		assertDoesNotThrow(() -> dataSchuelerKAoADaten.checkBeforePatch(dto, Map.of("bemerkung", "Freizeit aber auch!")));
	}

	@Test
	@DisplayName("patchCoreDTo - wrong field")
	void patchCoreDtoTest() {
		final var initAttributes = new HashMap<String, Object>();
		initAttributes.put(SCHULJAHRESABSCHNTT, 7L);
		initAttributes.put(JAHRGANG, 8000000L);
		initAttributes.put("hakuna matata", 10L);
		initAttributes.put(MERKMAL, 51L);
		initAttributes.put(ZUSATZMERKMAL, 68);
		initAttributes.put(BEMERKUNG, "schule ist toll!");

		final var throwable = catchThrowable(() -> dataSchuelerKAoADaten.checkBeforeCreation(1L, initAttributes));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Fehler beim patchen des CoreDto: hakuna matata")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@ParameterizedTest
	@DisplayName("validateOptionalAttributes | Exception")
	@MethodSource("provideSchuelerKAoADatenForValidateOptionalAttributesException")
	void validateOptionalAttributesTest_Exception(final Map<String, Object> initAttributes) {
		final var throwable = catchThrowable(() -> dataSchuelerKAoADaten.checkBeforeCreation(1L, initAttributes));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageStartingWith("Die Anzahl vorhandener optionaler Attribute ist größer 1")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("validateLernabschnittsdaten | Lernabschnittsdaten sind null -> Exception")
	void validateLernabschnittsdatenTest_LernabschnittsdatenNull() {
		final var initAttributes = new HashMap<String, Object>();
		initAttributes.put(SCHULJAHRESABSCHNTT, 7L);
		initAttributes.put(JAHRGANG, 8000000L);
		initAttributes.put(KATEGORIE, 10L);
		initAttributes.put(MERKMAL, 51L);
		initAttributes.put(ZUSATZMERKMAL, 68);
		initAttributes.put(BEMERKUNG, "schule ist toll!");
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(List.of(lernabschnittsdaten));

		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 123L)).thenReturn(null);

		final var throwable = catchThrowable(() -> this.dataSchuelerKAoADaten.checkBeforeCreation(123L, initAttributes));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Lernabschnittsdaten mit der ID 7 vorhanden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}


	@ParameterizedTest
	@DisplayName("validateKategorie | falsche KategorieId -> Exception")
	@ValueSource(longs = { 5, 15, -1 })
	void validateKategorieTest_KategorieIdWrong(final long kategorieId) {
		final var initAttributes = new HashMap<String, Object>();
		initAttributes.put(SCHULJAHRESABSCHNTT, 7L);
		initAttributes.put(JAHRGANG, 8000000L);
		initAttributes.put(KATEGORIE, kategorieId);
		initAttributes.put(MERKMAL, 51L);
		initAttributes.put(ZUSATZMERKMAL, 68);
		initAttributes.put(BEMERKUNG, "schule ist toll!");
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(lernabschnittsdaten);
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(List.of(lernabschnittsdaten));

		final var throwable = catchThrowable(() -> dataSchuelerKAoADaten.checkBeforeCreation(123L, initAttributes));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageStartingWith("Keine KAoAKategorie mit der ID")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("validateGetSchuljahr | kein passender Schuljahresabschnitt gefunden")
	void validateGetSchuljahrTest() {
		final var initAttributes = new HashMap<String, Object>();
		initAttributes.put(SCHULJAHRESABSCHNTT, 7L);
		initAttributes.put(JAHRGANG, 8000000L);
		initAttributes.put(KATEGORIE, 10L);
		initAttributes.put(MERKMAL, 51L);
		initAttributes.put(ZUSATZMERKMAL, 68);
		initAttributes.put(BEMERKUNG, "schule ist toll!");
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));
		when(this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(7L)).thenThrow(DeveloperNotificationException.class);
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(lernabschnittsdaten);
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(List.of(lernabschnittsdaten));

		final var throwable = catchThrowable(() -> dataSchuelerKAoADaten.checkBeforeCreation(123L, initAttributes));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageStartingWith("Kein Schuljahresabschnitt zur ID")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@ParameterizedTest
	@DisplayName("validateTests")
	@MethodSource("provideValidateTestsObjects")
	void validateTests(final Map<String, Object> initAttributes, final String errorMessage, final Response.Status errorCode) {
		final var lernabschnittsdaten = new DTOSchuelerLernabschnittsdaten(7L, 123L, 7L, false, false);
		lernabschnittsdaten.WechselNr = 0;
		when(this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(lernabschnittsdaten);
		when(this.conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchuelerLernabschnittsdaten.class, 7L))
				.thenReturn(List.of(lernabschnittsdaten));
		final var schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2024;
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));
		when(this.conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(7L)).thenReturn(schuljahresabschnitt);

		final var throwable = catchThrowable(() -> dataSchuelerKAoADaten.checkBeforeCreation(123L, initAttributes));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage(errorMessage)
				.hasFieldOrPropertyWithValue(STATUS, errorCode);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments(ID, 1L),
				arguments(JAHRGANG, 8000000),
				arguments(SCHULJAHRESABSCHNTT, 7L),
				arguments(KATEGORIE, 3L),
				arguments(MERKMAL, 4L),
				arguments(ZUSATZMERKMAL, 5L),
				arguments(EBENE_4, 6L),
				arguments(ANSCHLUSSOPTION, 7L),
				arguments(BERUFSFELD, 42L),
				arguments(BEMERKUNG, "schule ist toll"),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	private static Stream<Arguments> provideSchuelerKAoADatenForSuccess() {
		final var mapBemerkung = new HashMap<String, Object>();
		mapBemerkung.put(SCHULJAHRESABSCHNTT, 7L);
		mapBemerkung.put(JAHRGANG, 8000000L);
		mapBemerkung.put(KATEGORIE, 10L);
		mapBemerkung.put(MERKMAL, 51L);
		mapBemerkung.put(ZUSATZMERKMAL, 68);
		mapBemerkung.put(BEMERKUNG, "schule ist toll!");
		final var mapAnschlussoption = new HashMap<String, Object>();
		mapAnschlussoption.put(SCHULJAHRESABSCHNTT, 7L);
		mapAnschlussoption.put(JAHRGANG, 10000000L);
		mapAnschlussoption.put(KATEGORIE, 14L);
		mapAnschlussoption.put(MERKMAL, 69L);
		mapAnschlussoption.put(ZUSATZMERKMAL, 117L);
		mapAnschlussoption.put(ANSCHLUSSOPTION, 24L);
		final var mapBerufsfeld = new HashMap<String, Object>();
		mapBerufsfeld.put(SCHULJAHRESABSCHNTT, 7L);
		mapBerufsfeld.put(JAHRGANG, 8000000L);
		mapBerufsfeld.put(KATEGORIE, 9L);
		mapBerufsfeld.put(MERKMAL, 47L);
		mapBerufsfeld.put(ZUSATZMERKMAL, 44L);
		mapBerufsfeld.put(BERUFSFELD, 2L);
		final var mapEbene4 = new HashMap<String, Object>();
		mapEbene4.put(SCHULJAHRESABSCHNTT, 7L);
		mapEbene4.put(JAHRGANG, 8000000L);
		mapEbene4.put(KATEGORIE, 10L);
		mapEbene4.put(MERKMAL, 55L);
		mapEbene4.put(ZUSATZMERKMAL, 80L);
		mapEbene4.put(EBENE_4, 1L);

		return Stream.of(
				arguments(mapBemerkung),
				arguments(mapAnschlussoption),
				arguments(mapBerufsfeld),
				arguments(mapEbene4)
		);
	}

	private static Stream<Arguments> provideSchuelerKAoADatenForValidateOptionalAttributesException() {
		final var mapTwoOptionals = new HashMap<String, Object>();
		mapTwoOptionals.put(SCHULJAHRESABSCHNTT, 7L);
		mapTwoOptionals.put(JAHRGANG, 8000000L);
		mapTwoOptionals.put(KATEGORIE, 10L);
		mapTwoOptionals.put(MERKMAL, 51L);
		mapTwoOptionals.put(ZUSATZMERKMAL, 68);
		mapTwoOptionals.put(BEMERKUNG, "schule ist toll!");
		mapTwoOptionals.put(ANSCHLUSSOPTION, 24L);
		final var mapThreeOptionals = new HashMap<String, Object>();
		mapThreeOptionals.put(SCHULJAHRESABSCHNTT, 7L);
		mapThreeOptionals.put(JAHRGANG, 8000000L);
		mapThreeOptionals.put(KATEGORIE, 10L);
		mapThreeOptionals.put(MERKMAL, 51L);
		mapThreeOptionals.put(ZUSATZMERKMAL, 68);
		mapThreeOptionals.put(BEMERKUNG, "schule ist toll!");
		mapThreeOptionals.put(ANSCHLUSSOPTION, 24L);
		mapThreeOptionals.put(BERUFSFELD, 2L);
		final var mapFourOptionals = new HashMap<String, Object>();
		mapFourOptionals.put(SCHULJAHRESABSCHNTT, 7L);
		mapFourOptionals.put(JAHRGANG, 8000000L);
		mapFourOptionals.put(KATEGORIE, 10L);
		mapFourOptionals.put(MERKMAL, 51L);
		mapFourOptionals.put(ZUSATZMERKMAL, 68);
		mapFourOptionals.put(BEMERKUNG, "schule ist toll!");
		mapFourOptionals.put(ANSCHLUSSOPTION, 24L);
		mapFourOptionals.put(BERUFSFELD, 2L);
		mapFourOptionals.put(EBENE_4, 1L);

		return Stream.of(
				arguments(mapTwoOptionals),
				arguments(mapThreeOptionals),
				arguments(mapFourOptionals)
		);
	}

	private static Stream<Arguments> provideValidateTestsObjects() {
		final var wrongIdJahrgang = new HashMap<String, Object>();
		wrongIdJahrgang.put(SCHULJAHRESABSCHNTT, 7L);
		wrongIdJahrgang.put(JAHRGANG, 1L);
		wrongIdJahrgang.put(KATEGORIE, 10L);
		wrongIdJahrgang.put(MERKMAL, 51L);
		wrongIdJahrgang.put(ZUSATZMERKMAL, 68);
		wrongIdJahrgang.put(BEMERKUNG, "schule ist toll!");
		final var jahrgangAndKategorieDoesntMatch = new HashMap<String, Object>();
		jahrgangAndKategorieDoesntMatch.put(SCHULJAHRESABSCHNTT, 7L);
		jahrgangAndKategorieDoesntMatch.put(JAHRGANG, 8000000L);
		jahrgangAndKategorieDoesntMatch.put(KATEGORIE, 14L);
		jahrgangAndKategorieDoesntMatch.put(MERKMAL, 51L);
		jahrgangAndKategorieDoesntMatch.put(ZUSATZMERKMAL, 68);
		jahrgangAndKategorieDoesntMatch.put(BEMERKUNG, "schule ist toll!");
		final var noJahrgangWithThisIdExists = new HashMap<String, Object>();
		noJahrgangWithThisIdExists.put(SCHULJAHRESABSCHNTT, 7L);
		noJahrgangWithThisIdExists.put(JAHRGANG, 123456L);
		noJahrgangWithThisIdExists.put(KATEGORIE, 14L);
		noJahrgangWithThisIdExists.put(MERKMAL, 51L);
		noJahrgangWithThisIdExists.put(ZUSATZMERKMAL, 68);
		noJahrgangWithThisIdExists.put(BEMERKUNG, "schule ist toll!");
		final var merkmalDoesntMatchKategorie = new HashMap<String, Object>();
		merkmalDoesntMatchKategorie.put(SCHULJAHRESABSCHNTT, 7L);
		merkmalDoesntMatchKategorie.put(JAHRGANG, 8000000L);
		merkmalDoesntMatchKategorie.put(KATEGORIE, 10L);
		merkmalDoesntMatchKategorie.put(MERKMAL, 55L);
		merkmalDoesntMatchKategorie.put(ZUSATZMERKMAL, 68);
		merkmalDoesntMatchKategorie.put(BEMERKUNG, "schule ist toll!");
		final var wrongIdMerkmal = new HashMap<String, Object>();
		wrongIdMerkmal.put(SCHULJAHRESABSCHNTT, 7L);
		wrongIdMerkmal.put(JAHRGANG, 8000000L);
		wrongIdMerkmal.put(KATEGORIE, 10L);
		wrongIdMerkmal.put(MERKMAL, 1000L);
		wrongIdMerkmal.put(ZUSATZMERKMAL, 68);
		wrongIdMerkmal.put(BEMERKUNG, "schule ist toll!");
		final var zusatzmerkmalDoesntMatch = new HashMap<String, Object>();
		zusatzmerkmalDoesntMatch.put(SCHULJAHRESABSCHNTT, 7L);
		zusatzmerkmalDoesntMatch.put(JAHRGANG, 8000000L);
		zusatzmerkmalDoesntMatch.put(KATEGORIE, 10L);
		zusatzmerkmalDoesntMatch.put(MERKMAL, 51L);
		zusatzmerkmalDoesntMatch.put(ZUSATZMERKMAL, 35L);
		zusatzmerkmalDoesntMatch.put(BEMERKUNG, "schule ist toll!");
		final var wrongIdZusatzmerkmal = new HashMap<String, Object>();
		wrongIdZusatzmerkmal.put(SCHULJAHRESABSCHNTT, 7L);
		wrongIdZusatzmerkmal.put(JAHRGANG, 8000000L);
		wrongIdZusatzmerkmal.put(KATEGORIE, 10L);
		wrongIdZusatzmerkmal.put(MERKMAL, 51L);
		wrongIdZusatzmerkmal.put(ZUSATZMERKMAL, 3500000L);
		wrongIdZusatzmerkmal.put(BEMERKUNG, "schule ist toll!");
		final var bemerkungTooLong = new HashMap<String, Object>();
		bemerkungTooLong.put(SCHULJAHRESABSCHNTT, 7L);
		bemerkungTooLong.put(JAHRGANG, 8000000L);
		bemerkungTooLong.put(KATEGORIE, 10L);
		bemerkungTooLong.put(MERKMAL, 51L);
		bemerkungTooLong.put(ZUSATZMERKMAL, 68);
		bemerkungTooLong.put(BEMERKUNG, "1Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore "
				+ "magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata");
		final var idBerufsfeldNull = new HashMap<String, Object>();
		idBerufsfeldNull.put(SCHULJAHRESABSCHNTT, 7L);
		idBerufsfeldNull.put(JAHRGANG, 8000000L);
		idBerufsfeldNull.put(KATEGORIE, 9L);
		idBerufsfeldNull.put(MERKMAL, 47L);
		idBerufsfeldNull.put(ZUSATZMERKMAL, 44L);
		idBerufsfeldNull.put(BEMERKUNG, "abc");
		idBerufsfeldNull.put(BERUFSFELD, null);
		final var wrongIdBerufsfeld = new HashMap<String, Object>();
		wrongIdBerufsfeld.put(SCHULJAHRESABSCHNTT, 7L);
		wrongIdBerufsfeld.put(JAHRGANG, 8000000L);
		wrongIdBerufsfeld.put(KATEGORIE, 9L);
		wrongIdBerufsfeld.put(MERKMAL, 47L);
		wrongIdBerufsfeld.put(ZUSATZMERKMAL, 44L);
		wrongIdBerufsfeld.put(BERUFSFELD, 370000);
		final var idAnschlussoptionNull = new HashMap<String, Object>();
		idAnschlussoptionNull.put(SCHULJAHRESABSCHNTT, 7L);
		idAnschlussoptionNull.put(JAHRGANG, 10000000L);
		idAnschlussoptionNull.put(KATEGORIE, 14L);
		idAnschlussoptionNull.put(MERKMAL, 69L);
		idAnschlussoptionNull.put(ZUSATZMERKMAL, 117L);
		idAnschlussoptionNull.put(ANSCHLUSSOPTION, null);
		idAnschlussoptionNull.put(BEMERKUNG, "abc");
		final var wrongIdAnschlussOption = new HashMap<String, Object>();
		wrongIdAnschlussOption.put(SCHULJAHRESABSCHNTT, 7L);
		wrongIdAnschlussOption.put(JAHRGANG, 10000000L);
		wrongIdAnschlussOption.put(KATEGORIE, 14L);
		wrongIdAnschlussOption.put(MERKMAL, 69L);
		wrongIdAnschlussOption.put(ZUSATZMERKMAL, 117L);
		wrongIdAnschlussOption.put(ANSCHLUSSOPTION, 3551545L);
		final var idAnschlussoptionDoesntMatch = new HashMap<String, Object>();
		idAnschlussoptionDoesntMatch.put(SCHULJAHRESABSCHNTT, 7L);
		idAnschlussoptionDoesntMatch.put(JAHRGANG, 10000000L);
		idAnschlussoptionDoesntMatch.put(KATEGORIE, 14L);
		idAnschlussoptionDoesntMatch.put(MERKMAL, 69L);
		idAnschlussoptionDoesntMatch.put(ZUSATZMERKMAL, 117L);
		idAnschlussoptionDoesntMatch.put(ANSCHLUSSOPTION, 33L);
		final var idEbene4Null = new HashMap<String, Object>();
		idEbene4Null.put(SCHULJAHRESABSCHNTT, 7L);
		idEbene4Null.put(JAHRGANG, 8000000L);
		idEbene4Null.put(KATEGORIE, 10L);
		idEbene4Null.put(MERKMAL, 55L);
		idEbene4Null.put(ZUSATZMERKMAL, 80L);
		idEbene4Null.put(BEMERKUNG, "abc");
		idEbene4Null.put(EBENE_4, null);
		final var wrongIdEbene4 = new HashMap<String, Object>();
		wrongIdEbene4.put(SCHULJAHRESABSCHNTT, 7L);
		wrongIdEbene4.put(JAHRGANG, 8000000L);
		wrongIdEbene4.put(KATEGORIE, 10L);
		wrongIdEbene4.put(MERKMAL, 55L);
		wrongIdEbene4.put(ZUSATZMERKMAL, 80L);
		wrongIdEbene4.put(EBENE_4, 987654321L);
		final var idEbene4DoesntMatch = new HashMap<String, Object>();
		idEbene4DoesntMatch.put(SCHULJAHRESABSCHNTT, 7L);
		idEbene4DoesntMatch.put(JAHRGANG, 8000000L);
		idEbene4DoesntMatch.put(KATEGORIE, 10L);
		idEbene4DoesntMatch.put(MERKMAL, 55L);
		idEbene4DoesntMatch.put(ZUSATZMERKMAL, 80L);
		idEbene4DoesntMatch.put(EBENE_4, 12L);

		return Stream.of(
				arguments(wrongIdJahrgang, "Kein Jahrgangseintrag mit der ID 1 vorhanden.", Response.Status.NOT_FOUND),
				arguments(jahrgangAndKategorieDoesntMatch, "Der Jahrgang mit der ID 8000000 ist nicht in der Kategorie SBO_10 für das Schuljahr 2024 "
						+ "enthalten.", Response.Status.BAD_REQUEST),
				arguments(noJahrgangWithThisIdExists, "Kein Jahrgangseintrag mit der ID 123456 vorhanden.", Response.Status.NOT_FOUND),
				arguments(merkmalDoesntMatchKategorie, "Das KAoAZusatzmerkmal mit der ID 68 passt nicht zum KAoAMerkmal mit der ID 55.",
						Response.Status.BAD_REQUEST),
				arguments(wrongIdMerkmal, "Kein KAoAMerkmal mit der ID 1000 vorhanden.", Response.Status.NOT_FOUND),
				arguments(zusatzmerkmalDoesntMatch, "Das KAoAZusatzmerkmal mit der ID 35 passt nicht zum KAoAMerkmal mit der ID 51.", Response.Status.BAD_REQUEST),
				arguments(wrongIdZusatzmerkmal, "Kein KAoAZusatzmerkmal mit der ID 3500000 vorhanden.", Response.Status.NOT_FOUND),
				arguments(bemerkungTooLong, "Bemerkung darf nicht mehr als 255 Zeichen beinhalten.", Response.Status.BAD_REQUEST),
				arguments(idBerufsfeldNull, "idBerufsfeld darf nicht null sein.", Response.Status.BAD_REQUEST),
				arguments(wrongIdBerufsfeld, "Kein Berufsfeld mit der ID 370000 vorhanden.", Response.Status.NOT_FOUND),
				arguments(idAnschlussoptionNull, "idAnschlussoption darf nicht null sein.", Response.Status.BAD_REQUEST),
				arguments(wrongIdAnschlussOption, "Keine Anschlussoption mit der ID 3551545 vorhanden.", Response.Status.NOT_FOUND),
				arguments(idAnschlussoptionDoesntMatch, "Die Anschlussoption mit der ID 33 passt nicht zum KAoAZusatzmerkmal mit der ID 117.",
						Response.Status.BAD_REQUEST),
				arguments(idEbene4Null, "idEbene4 darf nicht null sein.", Response.Status.BAD_REQUEST),
				arguments(wrongIdEbene4, "Keine Ebene4 mit der ID 987654321 vorhanden.", Response.Status.NOT_FOUND),
				arguments(idEbene4DoesntMatch, "Die Ebene4 mit der ID 12 passt nicht zum KAoAZusatzmerkmal mit der ID 80.", Response.Status.BAD_REQUEST)
		);
	}
}
