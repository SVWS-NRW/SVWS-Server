package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.SchulinterneFunktion;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchulinterneFunktion;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Diese Klasse testet die Klasse DataSchulinterneFunktionen")
class DataSchulinterneFunktionenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchulinterneFunktionen data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: bezeichnung")
	void setAttributesRequiredOnCreationTest() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("id", "2")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (bezeichnung) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableTest() {
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(mock(DTOSchulinterneFunktion.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "1")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | id is assigned")
	void initDTO_idIsAssignedTest() {
		final var dto = new DTOSchulinterneFunktion(-1L);

		this.data.initDTO(dto, 2L, Collections.emptyMap());

		assertThat(dto.id).isEqualTo(2L);
	}

	@Test
	@DisplayName("getLongId")
	void getLongId() throws ApiOperationException {
		final var dto = new DTOSchulinterneFunktion(1L);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Id is null")
	void getById_IdIsNull() throws ApiOperationException {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Eine Anfrage zu einer schulinternen Funktion mit der ID null ist unzulässig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getById | no entry found")
	void getById_noEntryFound() throws ApiOperationException {
		when(conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(1L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die schulinterne Funktion mit der ID 1 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.bezeichnung = "test";
		dto.istSichtbar = true;
		dto.sortierung = 5;
		when(conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(SchulinterneFunktion.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "test")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 5);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final var dto1 = new DTOSchulinterneFunktion(1L);
		final var dto2 = new DTOSchulinterneFunktion(2L);
		when(this.conn.queryAll(DTOSchulinterneFunktion.class)).thenReturn(List.of(dto1, dto2));

		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						lf1 -> assertThat(lf1)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						lf2 -> assertThat(lf2)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Datenbank leer")
	void getAllTest_noEntriesFound() {
		when(this.conn.queryAll(DTOSchulinterneFunktion.class)).thenReturn(Collections.emptyList());

		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.bezeichnung = "Bezeichnung";
		dto.istSichtbar = false;
		dto.sortierung = 32000;

		assertThat(this.data.map(dto))
				.isInstanceOf(SchulinterneFunktion.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Bezeichnung")
				.hasFieldOrPropertyWithValue("istSichtbar", false)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapTest_sichtbarNull() {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.istSichtbar = null;
		assertThat(this.data.map(dto))
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("map | sortierung null")
	void mapTest_sortierungNull() {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.sortierung = null;
		assertThat(this.data.map(dto))
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("patch | bezeichnung | no changes")
	void patch_bezeichnungNoChanges() throws ApiOperationException {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.bezeichnung = "gleich";
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "gleich"));

		verify(this.conn, never()).queryAll(DTOSchulinterneFunktion.class);
		assertThat(dto.bezeichnung).isEqualTo("gleich");
	}

	@Test
	@DisplayName("patch | bezeichnung | isBlank")
	void patch_bezeichnungIsBlank() throws ApiOperationException {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.bezeichnung = "gleich";
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", " "));

		verify(this.conn, never()).queryAll(DTOSchulinterneFunktion.class);
		assertThat(dto.bezeichnung).isEqualTo("gleich");
	}

	@Test
	@DisplayName("patch | bezeichnung | null")
	void patch_bezeichnung_null() {
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(mock(DTOSchulinterneFunktion.class));
		final var input = new HashMap<String, Object>();
		input.put("bezeichnung", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, input))
				.withMessage("Attribut bezeichnung: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung | empty")
	void patch_bezeichnung_empty() {
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(mock(DTOSchulinterneFunktion.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "")))
				.withMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already Used")
	void patch_bezeichnungAlreadyUsed() {
		final var dto1 = new DTOSchulinterneFunktion(1L);
		final var dto2 = new DTOSchulinterneFunktion(2L);
		dto1.bezeichnung = "first";
		dto2.bezeichnung = "second";
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 2L)).thenReturn(dto2);
		when(this.conn.queryAll(DTOSchulinterneFunktion.class)).thenReturn(List.of(dto1, dto2));

		assertThatException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("bezeichnung", "first")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung first ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already Used | upper case")
	void patch_bezeichnungAlreadyUsed_upperCase() {
		final var dto1 = new DTOSchulinterneFunktion(1L);
		final var dto2 = new DTOSchulinterneFunktion(2L);
		dto1.bezeichnung = "first";
		dto2.bezeichnung = "second";
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 2L)).thenReturn(dto2);
		when(this.conn.queryAll(DTOSchulinterneFunktion.class)).thenReturn(List.of(dto1, dto2));

		assertThatException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("bezeichnung", "FIRST")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung FIRST ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung | tooLong")
	void patch_bezeichnung_tooLong() {
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(mock(DTOSchulinterneFunktion.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "Lorem ipsum dolor sit amet, consetetur sadipscing e")))
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 50 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung | Erfolg")
	void patch_bezeichnung() throws ApiOperationException {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.bezeichnung = "first";
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "second"));

		assertThat(dto.bezeichnung).isEqualTo("second");
	}

	@Test
	@DisplayName("patch | istSichtbar | null")
	void patch_istSichtbarNull() {
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(mock(DTOSchulinterneFunktion.class));
		final var input = new HashMap<String, Object>();
		input.put("istSichtbar", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, input))
				.withMessage("Attribut istSichtbar: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | istSichtbar | Erfolg")
	void patch_istSichtbar() throws ApiOperationException {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.istSichtbar = true;
		when(conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", false));

		assertThat(dto.istSichtbar).isFalse();
	}

	@Test
	@DisplayName("patch | sortierung | null")
	void patch_sortierungNull() {
		when(conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(mock(DTOSchulinterneFunktion.class));
		final var input = new HashMap<String, Object>();
		input.put("sortierung", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, input))
				.withMessage("Attribut sortierung: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | sortierung | Erfolg")
	void patch_sortierung() throws ApiOperationException {
		final var dto = new DTOSchulinterneFunktion(1L);
		dto.sortierung = 32000;
		when(conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 31000));

		assertThat(dto.sortierung).isEqualTo(31000);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patch_unknownArgument() {
		when(this.conn.queryByKey(DTOSchulinterneFunktion.class, 1L)).thenReturn(mock(DTOSchulinterneFunktion.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse | referenziert")
	void checkBeforeDeletionWithSimpleOperationResponse_referenced() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);

		final var dto = new DTOSchulinterneFunktion(1L);
		dto.bezeichnung = "Schulleitung";

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Die schulinterne Funktion mit der Bezeichnung Schulleitung ist in der Datenbank referenziert und kann daher nicht gelöscht werden.");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse | nicht referenziert")
	void checkBeforeDeletionWithSimpleOperationResponse_notReferenced() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(2L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);

		final var dto = new DTOSchulinterneFunktion(1L);

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(response)
				.hasFieldOrPropertyWithValue("success", true)
				.satisfies(r -> assertThat(r.log).isEmpty());
	}

}
