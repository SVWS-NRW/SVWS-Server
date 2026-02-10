package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataLernplattformen")
@ExtendWith(MockitoExtension.class)
class DataLernplattformenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataLernplattformen data;

	@BeforeAll
	static void setUp() {
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
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(mock(DTOLernplattformen.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}
	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOLernplattformen(1L, "");

		this.data.initDTO(dto, 2L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOLernplattformen(1L, "");

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOLernplattformen(1L, "");

		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Lernplattform.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID der Lernplattform darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOLernplattformen.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Lernplattform mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}


	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOLernplattformen(1L, "bez1");
		final var dto2 = new DTOLernplattformen(2L, "bez2");
		when(this.conn.queryAll(DTOLernplattformen.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Lernplattform.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("bezeichnung", "bez1"),
						f2 -> assertThat(f2)
								.isInstanceOf(Lernplattform.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("bezeichnung", "bez2")
				);
	}

	@Test
	@DisplayName("getAll | referenced in other tabled")
	void getAllReferencedInOtherTables() {
		final var dto1 = new DTOLernplattformen(1L, "bez1");
		final var dto2 = new DTOLernplattformen(2L, "bez2");
		when(this.conn.queryAll(DTOLernplattformen.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Lernplattform.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						f2 -> assertThat(f2)
								.isInstanceOf(Lernplattform.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOLernplattformen.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOLernplattformen(1L, "bez1");

		assertThat(this.data.map(dto))
				.isInstanceOf(Lernplattform.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung);
	}

	@Test
	@DisplayName("map | bezeichnung null")
	void mapBezeichnungIsNull() {
		final var dto = new DTOLernplattformen(1L, "bez1");
		dto.Bezeichnung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Lernplattform.class)
				.hasFieldOrPropertyWithValue("bezeichnung", "");
	}

	@Test
	@DisplayName("addBasic | lehrerEinwilligung")
	void addBasicLehrerEinwilligung() throws ApiOperationException {
		final var dto = new DTOLernplattformen(1L, "bez1");
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.addBasic(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, times(1)).queryList("SELECT e.ID FROM DTOLehrer e", Long.class);
		verify(this.conn, times(2)).transactionPersistAll(any());
		verify(this.conn, times(3)).transactionFlush();
	}

	@Test
	@DisplayName("addBasic | schuelerEinwilligung")
	void addBasicSchuelerEinwilligung() throws ApiOperationException {
		final var dto = new DTOLernplattformen(1L, "bez1");
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.addBasic(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, times(1)).queryList("SELECT e.ID FROM DTOSchueler e", Long.class);
		verify(this.conn, times(2)).transactionPersistAll(any());
		verify(this.conn, times(3)).transactionFlush();
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOLernplattformen.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOLernplattformen(1L, "bezeichnung");

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}


	@Test
	@DisplayName("patch | bezeichnung > 255 Zeichen")
	void patchBezeichnungIsTooLong() {
		final var dto = new DTOLernplattformen(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", RandomStringUtils.insecure().nextAscii(256))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 255 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung Null")
	void patchBezeichnungIsNull() {
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(mock(DTOLernplattformen.class));
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
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(mock(DTOLernplattformen.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung is blank")
	void patchBezeichnungIsBlank() throws ApiOperationException {
		final var dto = new DTOLernplattformen(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "    "));

		verify(this.conn, never()).queryAll(DTOLernplattformen.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung doesn't change")
	void patchBezeichnungDoesNotChange() throws ApiOperationException {
		final var dto = new DTOLernplattformen(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOLernplattformen.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung already used")
	void patchBezeichnungAlreadyUsed() {
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(mock(DTOLernplattformen.class));
		when(this.conn.queryAll(DTOLernplattformen.class)).thenReturn(List.of(new DTOLernplattformen(1L, "test")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already used different case")
	void patchBezeichnungAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(mock(DTOLernplattformen.class));
		when(this.conn.queryAll(DTOLernplattformen.class)).thenReturn(List.of(new DTOLernplattformen(2L, "TEST")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}


	@Test
	@DisplayName("patch | bezeichnung change case in same object")
	void patchBezeichnungChangeCase() throws ApiOperationException {
		final var dto = new DTOLernplattformen(1L, "test");
		when(conn.queryAll(DTOLernplattformen.class)).thenReturn(List.of(dto));
		final var newDto = new DTOLernplattformen(2L, "abc");
		when(this.conn.queryByKey(DTOLernplattformen.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("bezeichnung", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | bezeichnung dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchBezeichnungInDtoISNull() {
		final var dto = new DTOLernplattformen(1L, "123");
		dto.Bezeichnung = null;
		when(conn.queryAll(DTOLernplattformen.class)).thenReturn(List.of(dto));
		final var newDto = new DTOLernplattformen(2L, "abc");
		when(this.conn.queryByKey(DTOLernplattformen.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("bezeichnung", "test")));
	}

	@Test
	@DisplayName("patch | bezeichnung")
	void patchBezeichnung() throws ApiOperationException {
		final var dto = new DTOLernplattformen(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "neu"));

		assertThat(dto.Bezeichnung).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(mock(DTOLernplattformen.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
