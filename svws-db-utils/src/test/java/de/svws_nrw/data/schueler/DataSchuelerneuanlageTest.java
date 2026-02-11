package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.util.TestUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSchuelerneuanlageTest {

	@Mock
	private DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten;

	@Mock
	private DBEntityManager conn;

	private DataSchuelerneuanlage cut;

	@Captor
	private ArgumentCaptor<List<DTOSchuelerDatenschutz>> einwilligungenCaptor;

	@Captor
	private ArgumentCaptor<List<DTOSchuelerLernplattform>> lernplattformenCaptor;

	@Captor
	private ArgumentCaptor<Map<String, Object>> lernabschnittsdatenCaptor;

	@BeforeEach
	void init() {
		cut = new DataSchuelerneuanlage(conn, 1L, dataSchuelerLernabschnittsdaten);
	}

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: nachname")
	void setAttributesRequiredOnCreationWithoutNachname() {
		assertThatException()
				.isThrownBy(() -> cut.add(Map.of("nachname", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (geburtsdatum,vorname,geschlecht) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: vorname")
	void setAttributesRequiredOnCreationWithoutVorname() {
		assertThatException()
				.isThrownBy(() -> cut.add(Map.of("vorname", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (geburtsdatum,nachname,geschlecht) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: geschlecht")
	void setAttributesRequiredOnCreationWithoutGeschlecht() {
		assertThatException()
				.isThrownBy(() -> cut.add(Map.of("geschlecht", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (geburtsdatum,vorname,nachname) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: geburtsdatum")
	void setAttributesRequiredOnCreationWithoutGeburtsdatum() {
		assertThatException()
				.isThrownBy(() -> cut.add(Map.of("geburtsdatum", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (vorname,nachname,geschlecht) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(mock(DTOSchueler.class));

		assertThatException()
				.isThrownBy(() -> cut.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getById | success")
	void getById() throws ApiOperationException {
		final DTOSchueler schuelerDto = createDTOSchueler();
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(schuelerDto);

		final SchuelerStammdaten result = cut.getById(1L);

		assertThat(result).isNotNull()
				.hasFieldOrPropertyWithValue("id", schuelerDto.ID)
				.hasFieldOrPropertyWithValue("vorname", schuelerDto.Vorname)
				.hasFieldOrPropertyWithValue("nachname", schuelerDto.Nachname)
				.hasFieldOrPropertyWithValue("alleVornamen", schuelerDto.AlleVornamen)
				.hasFieldOrPropertyWithValue("geschlecht", schuelerDto.Geschlecht.id)
				.hasFieldOrPropertyWithValue("geburtsdatum", schuelerDto.Geburtsdatum)
				.hasFieldOrPropertyWithValue("status", schuelerDto.idStatus)
				.hasFieldOrPropertyWithValue("anmeldedatum", schuelerDto.AnmeldeDatum)
				.hasFieldOrPropertyWithValue("aufnahmedatum", schuelerDto.Aufnahmedatum)
				.hasFieldOrPropertyWithValue("beginnBildungsgang", schuelerDto.BeginnBildungsgang)
				.hasFieldOrPropertyWithValue("dauerBildungsgang", schuelerDto.DauerBildungsgang);
	}

	@Test
	@DisplayName("getById | failed")
	void getByIdNotFound() {
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> cut.getById(1L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Schüler zur ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getDTO | success")
	void getDTO() throws ApiOperationException {
		final DTOSchueler dtoSchueler = createDTOSchueler();
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(dtoSchueler);

		final DTOSchueler result = cut.getDTO(1L);

		assertThat(result).isEqualTo(dtoSchueler);
	}

	@Test
	@DisplayName("getDTO | ID is null")
	void getDTOWithIdIsNull() {
		assertThatException()
				.isThrownBy(() -> cut.getDTO(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID für den Schüler darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getDTO | ID not found")
	void getDTOWithIdNotFound() {
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> cut.getDTO(1L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Schüler zur ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | success")
	void mapNewSchueler() throws ApiOperationException {
		final DTOSchueler schuelerDto = createDTOSchueler();

		assertThat(cut.map((schuelerDto)))
				.isInstanceOf(SchuelerStammdaten.class)
				.hasFieldOrPropertyWithValue("id", schuelerDto.ID)
				.hasFieldOrPropertyWithValue("vorname", schuelerDto.Vorname)
				.hasFieldOrPropertyWithValue("nachname", schuelerDto.Nachname)
				.hasFieldOrPropertyWithValue("alleVornamen", schuelerDto.AlleVornamen)
				.hasFieldOrPropertyWithValue("geschlecht", schuelerDto.Geschlecht.id)
				.hasFieldOrPropertyWithValue("geburtsdatum", schuelerDto.Geburtsdatum)
				.hasFieldOrPropertyWithValue("status", schuelerDto.idStatus)
				.hasFieldOrPropertyWithValue("anmeldedatum", schuelerDto.AnmeldeDatum)
				.hasFieldOrPropertyWithValue("aufnahmedatum", schuelerDto.Aufnahmedatum)
				.hasFieldOrPropertyWithValue("beginnBildungsgang", schuelerDto.BeginnBildungsgang)
				.hasFieldOrPropertyWithValue("dauerBildungsgang", schuelerDto.DauerBildungsgang);
	}

	@Test
	@DisplayName("map | null attributes")
	void mapNewSchuelerWithNullAttributes() throws ApiOperationException {
		final DTOSchueler schuelerDto = createDTOSchueler();
		schuelerDto.Nachname = null;
		schuelerDto.Vorname = null;
		schuelerDto.AlleVornamen = null;
		schuelerDto.Geschlecht = null;
		schuelerDto.idStatus = null;

		assertThat(cut.map((schuelerDto)))
				.isInstanceOf(SchuelerStammdaten.class)
				.hasFieldOrPropertyWithValue("vorname", "")
				.hasFieldOrPropertyWithValue("nachname", "")
				.hasFieldOrPropertyWithValue("alleVornamen", "")
				.hasFieldOrPropertyWithValue("geschlecht", -1)
				.hasFieldOrPropertyWithValue("status", -1);
	}

	@Test
	@DisplayName("patch | ID is not allowed")
	void patchIdIsNotAllowed() {
		final DTOSchueler dtoSchueler = createDTOSchueler();
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(dtoSchueler);
		final Map<String, Object> map = new HashMap<>();
		map.put("id", 1L);

		assertThatException()
				.isThrownBy(() -> cut.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | ID is wrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> cut.mapAttribute(mock(DTOSchueler.class), "id", 1L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 1 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@ParameterizedTest
	@MethodSource("patchAttributes")
	@DisplayName("patch attributes")
	void patchAttributes(final String attributeName, final Object value, final Object expectedValue) throws ApiOperationException {
		final DTOSchueler dtoSchueler = createDTOSchueler();
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(dtoSchueler);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		cut.patch(1L, Map.of(attributeName, value));

		switch (attributeName) {
			case "nachname" -> assertThat(dtoSchueler.Nachname).isEqualTo(expectedValue);
			case "vorname" -> assertThat(dtoSchueler.Vorname).isEqualTo(expectedValue);
			case "alleVornamen" -> assertThat(dtoSchueler.AlleVornamen).isEqualTo(expectedValue);
			case "geburtsdatum" -> assertThat(dtoSchueler.Geburtsdatum).isEqualTo(expectedValue);
			case "anmeldedatum" -> assertThat(dtoSchueler.AnmeldeDatum).isEqualTo(expectedValue);
			case "aufnahmedatum" -> assertThat(dtoSchueler.Aufnahmedatum).isEqualTo(expectedValue);
			case "beginnBildungsgang" -> assertThat(dtoSchueler.BeginnBildungsgang).isEqualTo(expectedValue);
			case "dauerBildungsgang" -> assertThat(dtoSchueler.DauerBildungsgang).isEqualTo(expectedValue);
			default -> assertThatException()
					.isThrownBy(() -> cut.mapAttribute(dtoSchueler, attributeName, value, Collections.emptyMap()))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Das Patchen des Attributes %s ist nicht implementiert.".formatted(attributeName))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@ParameterizedTest
	@MethodSource("patchNullAttributes")
	@DisplayName("patch | attribute is null")
	void patchAttributeIsNull(final String attributeName, final String expectedMessage) {
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(mock(DTOSchueler.class));
		final Map<String, Object> map = new HashMap<>();
		map.put(attributeName, null);

		assertThatException()
				.isThrownBy(() -> cut.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | geschlecht is null")
	void patchGeschlechtIsNull() {
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(mock(DTOSchueler.class));

		assertThatException()
				.isThrownBy(() -> cut.patch(1L, Map.of("geschlecht", 999)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Geschlecht darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@ParameterizedTest
	@MethodSource("patchEmptyAttributes")
	@DisplayName("patch | attribute is empty")
	void patchAttributeIsEmpty(final String attributeName, final String expectedMessage) {
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(mock(DTOSchueler.class));

		assertThatException()
				.isThrownBy(() -> cut.patch(1L, Map.of(attributeName, "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@ParameterizedTest
	@MethodSource("patchTooLongAttributes")
	@DisplayName("patch | attribute is too long")
	void patchAttributeIsTooLong(final String attributeName, final String tooLongValue, final String expectedMessage) {
		final DTOSchueler dtoSchueler = createDTOSchueler();
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(dtoSchueler);

		assertThatException()
				.isThrownBy(() -> cut.patch(1L, Map.of(attributeName, tooLongValue)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@ParameterizedTest
	@MethodSource("invalidNumericAttributes")
	@DisplayName("patch | convert to Integer")
	void patch_InvalidNumericAttributes_Throws(final String attributeName, final Object givenValue, final String expectedMessage) {
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(mock(DTOSchueler.class));

		assertThatException()
				.isThrownBy(() -> cut.patch(1L, Map.of(attributeName, givenValue)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Schuljahresabschnitts_ID is null")
	void patchSchuljahresabschnittsIdIsNull() throws ApiOperationException {
		final DTOSchueler dtoSchueler = createDTOSchueler();
		dtoSchueler.Schuljahresabschnitts_ID = null;
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(dtoSchueler);
		final Benutzer benutzer = mock(Benutzer.class);
		final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2000;
		when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(1L)).thenReturn(schuljahresabschnitt);
		when(conn.getUser()).thenReturn(benutzer);
		when(conn.transactionPersist(any())).thenReturn(true);

		cut.patch(1L, Map.of("status", 0));

		assertThat(dtoSchueler.idStatus).isZero();
	}

	@Test
	@DisplayName("addNewSchuelerWithLernabschnitt | without Einwilligungen | without Lernplattformen | without Lernabschnitt")
	void addNewSchuelerWithoutLernabschnitt() throws ApiOperationException {
		final Map<String, Object> mapSchueler = schuelerMapWithoutLernabschnitt();
		final InputStream inputStream = TestUtils.fromObject(mapSchueler);
		final Benutzer benutzer = mock(Benutzer.class);
		final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2000;
		when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(1L)).thenReturn(schuljahresabschnitt);
		when(conn.getUser()).thenReturn(benutzer);
		when(conn.transactionGetNextID(DTOSchueler.class)).thenReturn(1L);
		when(conn.transactionPersist(any())).thenReturn(true);
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn((createDTOSchueler()));

		cut.addNewSchuelerWithLernabschnitt(inputStream);

		final ArgumentCaptor<DTOSchueler> captor = ArgumentCaptor.forClass(DTOSchueler.class);
		verify(conn, times(1)).transactionPersist(captor.capture());
		assertThat(captor.getValue())
				.hasFieldOrPropertyWithValue("Vorname", mapSchueler.get("vorname"))
				.hasFieldOrPropertyWithValue("Nachname", mapSchueler.get("nachname"))
				.hasFieldOrPropertyWithValue("AlleVornamen", mapSchueler.get("alleVornamen"))
				.hasFieldOrPropertyWithValue("Geburtsdatum", mapSchueler.get("geburtsdatum"))
				.hasFieldOrPropertyWithValue("idStatus", mapSchueler.get("status"))
				.hasFieldOrPropertyWithValue("AnmeldeDatum", mapSchueler.get("anmeldedatum"))
				.hasFieldOrPropertyWithValue("Aufnahmedatum", mapSchueler.get("aufnahmedatum"))
				.hasFieldOrPropertyWithValue("BeginnBildungsgang", mapSchueler.get("beginnBildungsgang"))
				.hasFieldOrPropertyWithValue("DauerBildungsgang", mapSchueler.get("dauerBildungsgang"));
		assertThat(captor.getValue().Geschlecht.id).isEqualTo(mapSchueler.get("geschlecht"));
	}

	@Test
	@DisplayName("addNewSchuelerWithLernabschnitt | with Einwilligungen")
	void addNewSchuelerWithEinwilligungenWithoutLernabschnitt() throws ApiOperationException {
		final InputStream inputStream = TestUtils.fromObject(schuelerMapWithoutLernabschnitt());
		final Benutzer benutzer = mock(Benutzer.class);
		final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2000;
		when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(1L)).thenReturn(schuljahresabschnitt);
		when(conn.getUser()).thenReturn(benutzer);
		when(conn.transactionGetNextID(DTOSchueler.class)).thenReturn(1L);
		when(conn.transactionPersist(any())).thenReturn(true);
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn((createDTOSchueler()));
		final DTOKatalogEinwilligungsart katalogEinwilligung = new DTOKatalogEinwilligungsart(1L, "Testeinwilligung", false, 1);
		katalogEinwilligung.personTyp = PersonTyp.SCHUELER;
		when(conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(katalogEinwilligung));

		cut.addNewSchuelerWithLernabschnitt(inputStream);

		verify(conn, times(1)).transactionPersistAll(this.einwilligungenCaptor.capture());
		assertThat(einwilligungenCaptor.getValue().getFirst())
				.hasFieldOrPropertyWithValue("Schueler_ID", 1L)
				.hasFieldOrPropertyWithValue("Datenschutz_ID", 1L)
				.hasFieldOrPropertyWithValue("Status", false)
				.hasFieldOrPropertyWithValue("Abgefragt", false);
	}

	@Test
	@DisplayName("addNewSchuelerWithLernabschnitt | Einwilligungen with other PersonTyp")
	void addNewSchuelerWithWrongPersonTyp() throws ApiOperationException {
		final InputStream inputStream = TestUtils.fromObject(schuelerMapWithoutLernabschnitt());
		final Benutzer benutzer = mock(Benutzer.class);
		final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2000;
		when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(1L)).thenReturn(schuljahresabschnitt);
		when(conn.getUser()).thenReturn(benutzer);
		when(conn.transactionGetNextID(DTOSchueler.class)).thenReturn(1L);
		when(conn.transactionPersist(any())).thenReturn(true);
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(createDTOSchueler());
		final DTOKatalogEinwilligungsart katalogEinwilligung = new DTOKatalogEinwilligungsart(1L, "Testeinwilligung", true, 1);
		katalogEinwilligung.personTyp = PersonTyp.LEHRER;
		when(conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(katalogEinwilligung));

		cut.addNewSchuelerWithLernabschnitt(inputStream);

		verify(conn, times(0)).transactionPersistAll(this.einwilligungenCaptor.capture());
	}

	@Test
	@DisplayName("addNewSchuelerWithLernabschnitt | with Lernplattformen")
	void addNewSchuelerWithLerplattformenWithoutLernabschnitt() throws ApiOperationException {
		final InputStream inputStream = TestUtils.fromObject(schuelerMapWithoutLernabschnitt());
		final Benutzer benutzer = mock(Benutzer.class);
		final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2000;
		when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(1L)).thenReturn(schuljahresabschnitt);
		when(conn.getUser()).thenReturn(benutzer);
		when(conn.transactionGetNextID(DTOSchueler.class)).thenReturn(1L);
		when(conn.transactionPersist(any())).thenReturn(true);
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn((createDTOSchueler()));
		when(conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of());
		final DTOLernplattformen katalogLernplattform = new DTOLernplattformen(1L, "Testlernplattform");
		when(conn.queryAll(DTOLernplattformen.class)).thenReturn(List.of(katalogLernplattform));

		cut.addNewSchuelerWithLernabschnitt(inputStream);

		verify(conn, times(1)).transactionPersistAll(this.lernplattformenCaptor.capture());
		assertThat(lernplattformenCaptor.getValue().getFirst())
				.hasFieldOrPropertyWithValue("SchuelerID", 1L)
				.hasFieldOrPropertyWithValue("LernplattformID", 1L)
				.hasFieldOrPropertyWithValue("EinwilligungAbgefragt", false)
				.hasFieldOrPropertyWithValue("EinwilligungNutzung", false)
				.hasFieldOrPropertyWithValue("EinwilligungAudiokonferenz", false)
				.hasFieldOrPropertyWithValue("EinwilligungVideokonferenz", false);
	}

	@Test
	@DisplayName("addNewSchuelerWithLernabschnitt | with Lernabschnitt")
	void addNewSchuelerWithLernabschnitt() throws ApiOperationException {
		final InputStream inputStream = TestUtils.fromObject(schuelerMapWithLernabschnitt());
		final Benutzer benutzer = mock(Benutzer.class);
		final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2000;
		when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(1L)).thenReturn(schuljahresabschnitt);
		when(conn.getUser()).thenReturn(benutzer);
		when(conn.transactionGetNextID(DTOSchueler.class)).thenReturn(1L);
		when(conn.transactionPersist(any())).thenReturn(true);
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn((createDTOSchueler()));

		cut.addNewSchuelerWithLernabschnitt(inputStream);

		verify(this.dataSchuelerLernabschnittsdaten, times(1)).add(lernabschnittsdatenCaptor.capture());
		assertThat(lernabschnittsdatenCaptor.getValue())
				.hasFieldOrPropertyWithValue("schuljahresabschnitt", 1L)
				.hasFieldOrPropertyWithValue("jahrgangID", 1)
				.hasFieldOrPropertyWithValue("klassenID", 1);
	}

	@Test
	@DisplayName("addNewSchuelerWithLernabschnitt | schuljahresabschnitt not in the Map")
	void addNewSchuelerWithoutIdSchuljahresabschnitt() throws ApiOperationException {
		final InputStream inputStream = TestUtils.fromObject(Map.of(
				"vorname", "Max",
				"nachname", "Mustermann",
				"alleVornamen", "Moritz",
				"geburtsdatum", "12-04-1990",
				"geschlecht", 3,
				"status", 0,
				"jahrgangID", 1L,
				"klassenID", 1L));
		final Benutzer benutzer = mock(Benutzer.class);
		final Schuljahresabschnitt schuljahresabschnitt = new Schuljahresabschnitt();
		schuljahresabschnitt.schuljahr = 2000;
		when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(1L)).thenReturn(schuljahresabschnitt);
		when(conn.getUser()).thenReturn(benutzer);
		when(conn.transactionGetNextID(DTOSchueler.class)).thenReturn(1L);
		when(conn.transactionPersist(any())).thenReturn(true);
		when(conn.queryByKey(DTOSchuelerFoto.class, 1L)).thenReturn(null);
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn((createDTOSchueler()));

		cut.addNewSchuelerWithLernabschnitt(inputStream);

		verify(this.dataSchuelerLernabschnittsdaten, times(1)).add(lernabschnittsdatenCaptor.capture());
		assertThat(lernabschnittsdatenCaptor.getValue())
				.hasFieldOrPropertyWithValue("jahrgangID", 1)
				.hasFieldOrPropertyWithValue("klassenID", 1)
				.doesNotContainKey("schuljahresabschnitt");

	}

	private static Stream<Arguments> patchAttributes() {
		return Stream.of(
				arguments("nachname", "Mustermann", "Mustermann"),
				arguments("vorname", "Max", "Max"),
				arguments("alleVornamen", "Moritz", "Moritz"),
				arguments("geburtsdatum", "12-04-1990", "12-04-1990"),
				arguments("anmeldedatum", "01-01-2015", "01-01-2015"),
				arguments("aufnahmedatum", "20-01-2015", "20-01-2015"),
				arguments("beginnBildungsgang", "01-02-2015", "01-02-2015"),
				arguments("dauerBildungsgang", 1, 1)
		);
	}

	private static Stream<Arguments> patchNullAttributes() {
		return Stream.of(
				arguments("nachname", "Attribut nachname: Der Wert null ist nicht erlaubt."),
				arguments("vorname", "Attribut vorname: Der Wert null ist nicht erlaubt."),
				arguments("alleVornamen", "Attribut alleVornamen: Der Wert null ist nicht erlaubt."),
				arguments("geschlecht", "Attribut geschlecht: Der Wert null ist nicht erlaubt"),
				arguments("geburtsdatum", "Attribut geburtsdatum: Der Wert null ist nicht erlaubt."),
				arguments("status", "Attribut status: Der Wert null ist nicht erlaubt")
		);
	}

	private static Stream<Arguments> patchEmptyAttributes() {
		return Stream.of(
				arguments("nachname", "Attribut nachname: Ein leerer String ist hier nicht erlaubt."),
				arguments("vorname", "Attribut vorname: Ein leerer String ist hier nicht erlaubt."),
				arguments("geburtsdatum", "Attribut geburtsdatum: Ein leerer String ist hier nicht erlaubt."),
				arguments("anmeldedatum", "Attribut anmeldedatum: Ein leerer String ist hier nicht erlaubt."),
				arguments("aufnahmedatum", "Attribut aufnahmedatum: Ein leerer String ist hier nicht erlaubt."),
				arguments("beginnBildungsgang", "Attribut beginnBildungsgang: Ein leerer String ist hier nicht erlaubt.")
		);
	}

	private static Stream<Arguments> patchTooLongAttributes() {
		return Stream.of(
				arguments("nachname", RandomStringUtils.insecure().nextAscii(121),
						"Attribut nachname: Die Länge des Strings ist auf 120 Zeichen limitiert."),
				arguments("vorname", RandomStringUtils.insecure().nextAscii(81),
						"Attribut vorname: Die Länge des Strings ist auf 80 Zeichen limitiert."),
				arguments("alleVornamen", RandomStringUtils.insecure().nextAscii(256),
						"Attribut alleVornamen: Die Länge des Strings ist auf 255 Zeichen limitiert.")
		);
	}

	private static Stream<Arguments> invalidNumericAttributes() {
		return Stream.of(
				arguments("status", "abc", "Attribut status: Fehler beim Konvertieren zu Integer"),
				arguments("geschlecht", "abc", "Attribut geschlecht: Fehler beim Konvertieren zu Integer"),
				arguments("dauerBildungsgang", "abc", "Attribut dauerBildungsgang: Fehler beim Konvertieren zu Integer")
		);
	}

	private static Map<String, Object> schuelerMapWithoutLernabschnitt() {
		return Map.ofEntries(
				entry("vorname", "Max"),
				entry("nachname", "Mustermann"),
				entry("alleVornamen", "Moritz"),
				entry("geburtsdatum", "12-04-1990"),
				entry("geschlecht", 3),
				entry("status", 0),
				entry("anmeldedatum", "01-01-2015"),
				entry("aufnahmedatum", "20-01-2015"),
				entry("beginnBildungsgang", "01-02-2015"),
				entry("dauerBildungsgang", 2)
		);
	}

	private static Map<String, Object> schuelerMapWithLernabschnitt() {
		return Map.ofEntries(
				entry("vorname", "Max"),
				entry("nachname", "Mustermann"),
				entry("alleVornamen", "Moritz"),
				entry("geburtsdatum", "12-04-1990"),
				entry("geschlecht", 3),
				entry("status", 0),
				entry("anmeldedatum", "01-01-2015"),
				entry("aufnahmedatum", "20-01-2015"),
				entry("beginnBildungsgang", "01-02-2015"),
				entry("dauerBildungsgang", 2),
				entry("schuljahresabschnitt", 1L),
				entry("jahrgangID", 1),
				entry("klassenID", 1)
		);
	}

	DTOSchueler createDTOSchueler() {
		final DTOSchueler dto = new DTOSchueler(1L, "TestGUID", false);
		// Persönliche Daten
		dto.Schuljahresabschnitts_ID = 99L;
		dto.Nachname = "Mustermann";
		dto.Vorname = "Max";
		dto.AlleVornamen = "Moritz";
		dto.Geschlecht = Geschlecht.M;
		dto.Geburtsdatum = "12-04-1990";
		// Statusdaten
		dto.idStatus = 0;
		dto.AnmeldeDatum = "01-01-2015";
		dto.Aufnahmedatum = "20-01-2015";
		dto.BeginnBildungsgang = "01-02-2015";
		dto.DauerBildungsgang = 1;
		return dto;
	}

}
