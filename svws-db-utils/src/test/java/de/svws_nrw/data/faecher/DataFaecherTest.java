package de.svws_nrw.data.faecher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataFaecher}.
 */
@DisplayName("Diese Klasse testet die Klasse DataFachdaten")
@ExtendWith(MockitoExtension.class)
class DataFaecherTest {


	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataFaecher data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: kuerzel")
	void setAttributesRequiredOnCreationTestKuerzel() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("kuerzelStatistik", "test", "bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (kuerzel) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: kuerzelStatistik")
	void setAttributesRequiredOnCreationTestKuerzelStatistik() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("kuerzel", "test", "bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (kuerzelStatistik) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: bezeichnung")
	void setAttributesRequiredOnCreationTestBezeichnung() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("kuerzel", "test", "kuerzelStatistik", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (bezeichnung) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: referenziertInAnderenTabellen")
	void setAttributesNotPatchableReferenziertInAnderenTabellen() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("referenziertInAnderenTabellen", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: referenziertInAnderenTabellen.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final var fachdaten = new DTOFach(1L, true);

		this.data.initDTO(fachdaten, 2L, null);

		assertThat(fachdaten.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOFach(1L, true);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(FachDaten.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Fachs darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOFach.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde kein Fach mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOFach(1L, true);
		final var dto2 = new DTOFach(2L, false);
		when(this.conn.queryAll(DTOFach.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsFaecher"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(FachDaten.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", true)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						f2 -> assertThat(f2)
								.isInstanceOf(FachDaten.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", false)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOFach.class)).thenReturn(emptyList());

		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map")
	void map() {
		final var dto = new DTOFach(1L, true);
		dto.Kuerzel = "kuerzel";
		dto.Bezeichnung = "bezeichnung";
		dto.StatistikKuerzel = "staKue";
		dto.Aufgabenfeld = "aufgabenfeld";
		dto.Unterrichtssprache = "chinesisch";
		dto.AufZeugnis = true;
		dto.BezeichnungZeugnis = "bezZeugnis";
		dto.BezeichnungUeberweisungsZeugnis = "bezUeberweisung";
		dto.IstOberstufenFach = true;
		dto.IstPruefungsordnungsRelevant = true;
		dto.IstFremdsprache = true;
		dto.IstMoeglichAlsNeueFremdspracheInSekII = true;
		dto.IstNachpruefungErlaubt = true;
		dto.IstSchriftlichZK = true;
		dto.IstSchriftlichBA = true;
		dto.GewichtungFHR = 1;
		dto.AbgeschlFaecherHolen = true;
		dto.MaxBemZeichen = 123;
		dto.Sichtbar = true;
		dto.SortierungAllg = 42;

		final var result = this.data.map(dto);

		assertThat(result)
				.isInstanceOf(FachDaten.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("kuerzel", dto.Kuerzel)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("kuerzelStatistik", dto.StatistikKuerzel)
				.hasFieldOrPropertyWithValue("aufgabenfeld", dto.Aufgabenfeld)
				.hasFieldOrPropertyWithValue("bilingualeSprache", dto.Unterrichtssprache)
				.hasFieldOrPropertyWithValue("aufZeugnis", true)
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", dto.BezeichnungZeugnis)
				.hasFieldOrPropertyWithValue("bezeichnungUeberweisungszeugnis", dto.BezeichnungUeberweisungsZeugnis)
				.hasFieldOrPropertyWithValue("istOberstufenFach", true)
				.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", true)
				.hasFieldOrPropertyWithValue("istFremdsprache", true)
				.hasFieldOrPropertyWithValue("istMoeglichAlsNeueFremdspracheInSekII", true)
				.hasFieldOrPropertyWithValue("istNachpruefungErlaubt", true)
				.hasFieldOrPropertyWithValue("istSchriftlichZK", true)
				.hasFieldOrPropertyWithValue("istSchriftlichBA", true)
				.hasFieldOrPropertyWithValue("istFHRFach", true)
				.hasFieldOrPropertyWithValue("holeAusAltenLernabschnitten", true)
				.hasFieldOrPropertyWithValue("maxZeichenInFachbemerkungen", 123)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 42);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(booleans = false)
	@DisplayName("map – Boolean with null or false should default to false")
	void mapBooleanDefaults(final Boolean value) {
		final var dto = new DTOFach(1L, true);
		dto.AufZeugnis = value;
		dto.IstOberstufenFach = value;
		dto.IstPruefungsordnungsRelevant = value;
		dto.IstFremdsprache = value;
		dto.IstMoeglichAlsNeueFremdspracheInSekII = value;
		dto.IstNachpruefungErlaubt = value;
		dto.IstSchriftlichZK = value;
		dto.IstSchriftlichBA = value;
		dto.AbgeschlFaecherHolen = value;
		dto.Sichtbar = value;

		final var result = this.data.map(dto);

		assertThat(result)
				.hasFieldOrPropertyWithValue("aufZeugnis", false)
				.hasFieldOrPropertyWithValue("istOberstufenFach", false)
				.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", false)
				.hasFieldOrPropertyWithValue("istFremdsprache", false)
				.hasFieldOrPropertyWithValue("istMoeglichAlsNeueFremdspracheInSekII", false)
				.hasFieldOrPropertyWithValue("istNachpruefungErlaubt", false)
				.hasFieldOrPropertyWithValue("istSchriftlichZK", false)
				.hasFieldOrPropertyWithValue("istSchriftlichBA", false)
				.hasFieldOrPropertyWithValue("holeAusAltenLernabschnitten", false)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@ParameterizedTest
	@CsvSource({ ", false", "0, false", "1, true", "5, true" })
	@DisplayName("map – istFHRFach depends on GewichtungFHR")
	void mapIstFHRFach(final Integer gewichtungFHR, final boolean expected) {
		final var dto = new DTOFach(1L, true);
		dto.GewichtungFHR = gewichtungFHR;

		final var result = this.data.map(dto);

		assertThat(result)
				.hasFieldOrPropertyWithValue("istFHRFach", expected);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOFach.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto  = new DTOFach(1L, true);

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	private static Stream<Arguments> patchStringAttributesTooManyCharacters() {
		return Stream.of(
				arguments("kuerzel", "Attribut kuerzel: Die Länge des Strings ist auf 20 Zeichen limitiert."),
				arguments("bezeichnung", "Attribut bezeichnung: Die Länge des Strings ist auf 255 Zeichen limitiert."),
				arguments("kuerzelStatistik", "Attribut kuerzelStatistik: Die Länge des Strings ist auf 2 Zeichen limitiert."),
				arguments("aufgabenfeld", "Attribut aufgabenfeld: Die Länge des Strings ist auf 2 Zeichen limitiert."),
				arguments("bilingualeSprache", "Attribut bilingualeSprache: Die Länge des Strings ist auf 1 Zeichen limitiert."),
				arguments("bezeichnungZeugnis", "Attribut bezeichnungZeugnis: Die Länge des Strings ist auf 255 Zeichen limitiert."),
				arguments("bezeichnungUeberweisungszeugnis", "Attribut bezeichnungUeberweisungszeugnis: Die Länge des Strings ist auf 255 Zeichen limitiert.")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributesTooManyCharacters")
	@DisplayName("patch | String Attributes Too Long")
	void patchAttributeTooLong(final String attributeName, final String expectedMessage) {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> data.patch(1L, Map.of(attributeName, RandomStringUtils.insecure().nextAscii(256))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private static Stream<Arguments> patchStringAttributes() {
		return Stream.of(
				arguments("aufgabenfeld"),
				arguments("bilingualeSprache"),
				arguments("bezeichnungZeugnis"),
				arguments("bezeichnungUeberweisungszeugnis"),
				arguments("unknown")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributes")
	@DisplayName("patch String attributes")
	void patchStringAttributes(final String attributeName) {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		final var newValue = "a";

		final var throwable = catchThrowable(() -> this.data.patch(1L, Map.of(attributeName, newValue)));

		switch (attributeName) {
			case "aufgabenfeld" -> assertThat(dto.Aufgabenfeld).isEqualTo(newValue);
			case "bilingualeSprache" -> assertThat(dto.Unterrichtssprache).isEqualTo(newValue);
			case "bezeichnungZeugnis" -> assertThat(dto.BezeichnungZeugnis).isEqualTo(newValue);
			case "bezeichnungUeberweisungszeugnis" -> assertThat(dto.BezeichnungUeberweisungsZeugnis).isEqualTo(newValue);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> patchBooleanAttributes() {
		return Stream.of(
				arguments("aufZeugnis", true),
				arguments("aufZeugnis", false),
				arguments("istOberstufenFach", true),
				arguments("istOberstufenFach", false),
				arguments("istPruefungsordnungsRelevant", true),
				arguments("istPruefungsordnungsRelevant", false),
				arguments("istFremdsprache", true),
				arguments("istFremdsprache", false),
				arguments("istMoeglichAlsNeueFremdspracheInSekII", true),
				arguments("istMoeglichAlsNeueFremdspracheInSekII", false),
				arguments("istNachpruefungErlaubt", true),
				arguments("istNachpruefungErlaubt", false),
				arguments("istSchriftlichZK", true),
				arguments("istSchriftlichZK", false),
				arguments("istSchriftlichBA", true),
				arguments("istSchriftlichBA", false),
				arguments("holeAusAltenLernabschnitten", true),
				arguments("holeAusAltenLernabschnitten", false),
				arguments("istSichtbar", true),
				arguments("istSichtbar", false)
		);
	}

	@ParameterizedTest
	@MethodSource("patchBooleanAttributes")
	@DisplayName("patch Boolean attributes")
	void patchBooleanAttributes(final String attributeName, final boolean newValue) throws ApiOperationException {

		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of(attributeName, newValue));

		switch (attributeName) {
			case "aufZeugnis" -> assertThat(dto.AufZeugnis).isEqualTo(newValue);
			case "istOberstufenFach" -> assertThat(dto.IstOberstufenFach).isEqualTo(newValue);
			case "istPruefungsordnungsRelevant" -> assertThat(dto.IstPruefungsordnungsRelevant).isEqualTo(newValue);
			case "istFremdsprache" -> assertThat(dto.IstFremdsprache).isEqualTo(newValue);
			case "istMoeglichAlsNeueFremdspracheInSekII" -> assertThat(dto.IstMoeglichAlsNeueFremdspracheInSekII).isEqualTo(newValue);
			case "istNachpruefungErlaubt" -> assertThat(dto.IstNachpruefungErlaubt).isEqualTo(newValue);
			case "istSchriftlichZK" -> assertThat(dto.IstSchriftlichZK).isEqualTo(newValue);
			case "istSchriftlichBA" -> assertThat(dto.IstSchriftlichBA).isEqualTo(newValue);
			case "holeAusAltenLernabschnitten" -> assertThat(dto.AbgeschlFaecherHolen).isEqualTo(newValue);
			case "istSichtbar" -> assertThat(dto.Sichtbar).isEqualTo(newValue);
		}
	}

	@ParameterizedTest
	@CsvSource({ "false, 0", "true, 1" })
	@DisplayName("patch – GewichtungFHR depends on GewichtungFHR")
	void patchGewichtungFHR(final boolean input, final int expectedResult) throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istFHRFach", input));

		assertThat(dto.GewichtungFHR).isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("patch | maxZeichenInFachbemerkungen")
	void patchMaxZeichenInFachbemerkungen() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("maxZeichenInFachbemerkungen", 345));

		assertThat(dto.MaxBemZeichen).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | sortierung is null")
	void patchSortierungIsNull() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));
		final var map = new HashMap<String, Object>();
		map.put("sortierung", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut sortierung: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Sortierung")
	void patchSortierung() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		dto.SortierungAllg = 123;
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.SortierungAllg).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse")
	void checkBeforeDeletionWithSimpleOperationResponse() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsFaecher"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOFach(1L, true);
		dto.Bezeichnung = "abc";

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Das Fach mit dem Name abc ist in der Datenbank referenziert und kann daher nicht gelöscht werden.");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponseTest | not referenced")
	void checkBeforeDeletionWithSimpleOperationResponseNotReferenced() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsFaecher"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(2L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOFach(1L, true);

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(response)
				.hasFieldOrPropertyWithValue("success", true)
				.satisfies(r -> assertThat(r.log).isEmpty());
	}

	@Test
	@DisplayName("patch | kuerzel")
	void patchKuerzel() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("kuerzel", "neu"));

		assertThat(dto.Kuerzel).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | kuerzel > 20 Zeichen")
	void patchKuerzelIsTooLong() {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzel", RandomStringUtils.insecure().nextAscii(21))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzel: Die Länge des Strings ist auf 20 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzel Null")
	void patchKuerzelIsNull() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));
		final var map = new HashMap<String, Object>();
		map.put("kuerzel", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzel: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzel empty")
	void patchKuerzelIsEmpty() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzel", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzel: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | wrong type")
	void patchKuerzelWrongType() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzel", 21)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzel: Es wurde ein String erwartet, aber keiner übergeben.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzel is blank")
	void patchKuerzelIsBlank() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		dto.Kuerzel = "bezeichnung";
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("kuerzel", "    "));

		verify(this.conn, never()).queryAll(DTOFach.class);
		assertThat(dto.Kuerzel).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | kuerzel doesn't change")
	void patchKuerzelDoesNotChange() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("kuerzel", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOFoerderschwerpunkt.class);
		assertThat(dto.Kuerzel).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | kuerzel already used")
	void patchKuerzelAlreadyUsed() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));
		final var dto = new DTOFach(2L, true);
		dto.Kuerzel = "test";
		when(this.conn.queryAll(DTOFach.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzel", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Das Kürzel test wird bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzel already used different case")
	void patchKuerzelAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));
		final var dto = new DTOFach(2L, true);
		dto.Kuerzel = "TEST";
		when(this.conn.queryAll(DTOFach.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzel", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Das Kürzel test wird bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzel change case in same object")
	void patchKuerzelChangeCase() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(conn.queryAll(DTOFach.class)).thenReturn(List.of(dto));
		final var newDto = new DTOFach(2L, true);
		newDto.Kuerzel = "abc";
		when(this.conn.queryByKey(DTOFach.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("kuerzel", "ABC"));

		assertThat(newDto.Kuerzel).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | bezeichnung")
	void patchBezeichnung() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "neu"));

		assertThat(dto.Bezeichnung).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | bezeichnung > 20 Zeichen")
	void patchBezeichnungIsTooLong() {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", RandomStringUtils.insecure().nextAscii(256))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 255 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung Null")
	void patchBezeichnungIsNull() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));
		final var map = new HashMap<String, Object>();
		map.put("bezeichnung", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung empty")
	void patchBezeichnungIsEmpty() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | wrong type")
	void patchBezeichnungWrongType() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", 21)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Es wurde ein String erwartet, aber keiner übergeben.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung is blank")
	void patchBezeichnungIsBlank() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		dto.Bezeichnung = "bezeichnung";
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "    "));

		verify(this.conn, never()).queryAll(DTOFach.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung doesn't change")
	void patchBezeichnungDoesNotChange() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOFoerderschwerpunkt.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung already used")
	void patchBezeichnungAlreadyUsed() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));
		final var dto = new DTOFach(2L, true);
		dto.Bezeichnung = "test";
		when(this.conn.queryAll(DTOFach.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test wird bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already used different case")
	void patchBezeichnungAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));
		final var dto = new DTOFach(2L, true);
		dto.Bezeichnung = "TEST";
		when(this.conn.queryAll(DTOFach.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test wird bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung change case in same object")
	void patchBezeichnungChangeCase() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(conn.queryAll(DTOFach.class)).thenReturn(List.of(dto));
		final var newDto = new DTOFach(2L, true);
		newDto.Bezeichnung = "abc";
		when(this.conn.queryByKey(DTOFach.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("bezeichnung", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | kuerzelStatistik")
	void patchKuerzelStatistik() throws ApiOperationException {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final String schluessel = Fach.AB.historie().getFirst().schluessel;

		this.data.patch(1L, Map.of("kuerzelStatistik", schluessel));

		assertThat(dto.StatistikKuerzel).isEqualTo(schluessel);
	}

	@Test
	@DisplayName("patch | kuerzelStatistik | wrong schluessel")
	void patchKuerzelStatistikWrongSchluessel() {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);
		final String schluessel = "XY";

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzelStatistik", schluessel)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Ein Fach mit dem Kuerzel XY wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzelStatistik > 20 Zeichen")
	void patchKuerzelStatistikIsTooLong() {
		final var dto = new DTOFach(1L, true);
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzelStatistik", RandomStringUtils.insecure().nextAscii(3))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzelStatistik: Die Länge des Strings ist auf 2 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzelStatistik Null")
	void patchKuerzelStatistikIsNull() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));
		final var map = new HashMap<String, Object>();
		map.put("kuerzelStatistik", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzelStatistik: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzelStatistik empty")
	void patchKuerzelStatistikIsEmpty() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzelStatistik", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzelStatistik: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | wrong type")
	void patchKuerzelStatistikWrongType() {
		when(this.conn.queryByKey(DTOFach.class, 1L)).thenReturn(mock(DTOFach.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzelStatistik", 21)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzelStatistik: Es wurde ein String erwartet, aber keiner übergeben.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
