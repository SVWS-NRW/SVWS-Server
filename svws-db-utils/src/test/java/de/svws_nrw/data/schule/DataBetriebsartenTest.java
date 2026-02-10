package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Betriebsart;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebsart;
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
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataBetriebsarten} */
@DisplayName("Diese Klasse testet die Klasse DataBetriebsarten")
@ExtendWith(MockitoExtension.class)
class DataBetriebsartenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataBetriebsarten data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: bezeichnung")
	void setAttributesRequiredOnCreationBezeichnung() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("test", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (bezeichnung) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("add | Erfolg")
	void add() throws ApiOperationException {
		final var dto = new DTOBetriebsart(1L, "TestBetriebsart");
		dto.Sortierung = 32000;
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionGetNextID(DTOBetriebsart.class)).thenReturn(1L);
		when(this.conn.transactionPersist(any(DTOBetriebsart.class))).thenReturn(true);

		final var result = this.data.add(Map.of("bezeichnung", "TestBetriebsart"));

		assertThat(result)
				.isInstanceOf(Betriebsart.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "TestBetriebsart")
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(mock(DTOBetriebsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: referenziertInAnderenTabellen")
	void setAttributesNotPatchableReferenziertInAnderenTabellen() {
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(mock(DTOBetriebsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("referenziertInAnderenTabellen", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: referenziertInAnderenTabellen.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOBetriebsart(1L, "");

		this.data.initDTO(dto, 2L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 2L)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOBetriebsart(1L, "");

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOBetriebsart(1L, "");

		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Betriebsart.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID der Betriebsart darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOBetriebsart.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Betriebsart mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOBetriebsart(1L, "bez1");
		final var dto2 = new DTOBetriebsart(2L, "bez2");
		when(this.conn.queryAll(DTOBetriebsart.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Betriebsart.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true)
								.hasFieldOrPropertyWithValue("bezeichnung", "bez1"),
						f2 -> assertThat(f2)
								.isInstanceOf(Betriebsart.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
								.hasFieldOrPropertyWithValue("bezeichnung", "bez2")
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOBetriebsart.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");
		dto.Sortierung = 42;

		assertThat(this.data.map(dto))
				.isInstanceOf(Betriebsart.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", false)
				.hasFieldOrPropertyWithValue("sortierung", 42);
	}

	@Test
	@DisplayName("map | bezeichnung null")
	void mapBezeichnungIsNull() {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");
		dto.Bezeichnung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Betriebsart.class)
				.hasFieldOrPropertyWithValue("bezeichnung", null);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarIsNull() {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");
		dto.Sichtbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Betriebsart.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOBetriebsart.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | bezeichnung > 30 Zeichen")
	void patchBezeichnungIsTooLong() {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");

		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", RandomStringUtils.insecure().nextAscii(31))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 30 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	static Stream<Arguments> patchAttributeIsNullArguments() {
		return Stream.of(
				Arguments.of("bezeichnung", "Attribut bezeichnung: Der Wert null ist nicht erlaubt."),
				Arguments.of("istSichtbar", "Attribut istSichtbar: Der Wert null ist nicht erlaubt"),
				Arguments.of("sortierung", "Attribut sortierung: Der Wert null ist nicht erlaubt")
		);
	}

	@ParameterizedTest(name = "patch | {0} is null")
	@MethodSource("patchAttributeIsNullArguments")
	@DisplayName("patch | attribute is null")
	void patchAttributeIsNull(final String attribute, final String expectedMessage) {
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(mock(DTOBetriebsart.class));
		final var map = new HashMap<String, Object>();
		map.put(attribute, null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung empty")
	void patchBezeichnungIsEmpty() {
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(mock(DTOBetriebsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung is blank")
	void patchBezeichnungIsBlank() throws ApiOperationException {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "    "));

		verify(this.conn, never()).queryAll(DTOBetriebsart.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung doesn't change")
	void patchBezeichnungDoesNotChange() throws ApiOperationException {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOBetriebsart.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung already used")
	void patchBezeichnungAlreadyUsed() {
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(mock(DTOBetriebsart.class));
		when(this.conn.queryAll(DTOBetriebsart.class)).thenReturn(List.of(new DTOBetriebsart(1L, "test")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already used different case")
	void patchBezeichnungAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(mock(DTOBetriebsart.class));
		when(this.conn.queryAll(DTOBetriebsart.class)).thenReturn(List.of(new DTOBetriebsart(2L, "TEST")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung change case in same object")
	void patchBezeichnungChangeCase() throws ApiOperationException {
		final var dto = new DTOBetriebsart(1L, "test");
		when(conn.queryAll(DTOBetriebsart.class)).thenReturn(List.of(dto));
		final var newDto = new DTOBetriebsart(2L, "abc");
		when(this.conn.queryByKey(DTOBetriebsart.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("bezeichnung", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | bezeichnung dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchBezeichnungInDtoISNull() {
		final var dto = new DTOBetriebsart(1L, "123");
		dto.Bezeichnung = null;
		when(conn.queryAll(DTOBetriebsart.class)).thenReturn(List.of(dto));
		final var newDto = new DTOBetriebsart(2L, "abc");
		when(this.conn.queryByKey(DTOBetriebsart.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("bezeichnung", "test")));
	}

	@Test
	@DisplayName("patch | bezeichnung")
	void patchBezeichnung() throws ApiOperationException {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "neu"));

		assertThat(dto.Bezeichnung).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | istSichtbar")
	void patchIstSichtbar() throws ApiOperationException {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | sortierung")
	void patchSortierung() throws ApiOperationException {
		final var dto = new DTOBetriebsart(1L, "bezeichnung");
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.Sortierung).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOBetriebsart.class, 1L)).thenReturn(mock(DTOBetriebsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse | referenziert")
	void checkBeforeDeletionWithSimpleOperationResponseReferenziert() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOBetriebsart(1L, "abc");

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Die Betriebsart mit dem Name abc ist in der Datenbank referenziert und kann daher nicht gelöscht werden.");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse | nicht referenziert")
	void checkBeforeDeletionWithSimpleOperationResponseNichtReferenziert() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(Collections.emptyList());
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOBetriebsart(1L, "abc");

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", true);
		assertThat(responses.get(1L).log).isEmpty();
	}

}
