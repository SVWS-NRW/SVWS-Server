package de.svws_nrw.data.kataloge;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
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
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataOrtsteile} */
@DisplayName("Diese Klasse testet die Klasse DataOrtsteile")
@ExtendWith(MockitoExtension.class)
class DataOrtsteileTest {

	@Mock
	private DBEntityManager conn;

	@Mock
	private DataOrte dataOrte;

	@InjectMocks
	private DataOrtsteile data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: ortsteil")
	void setAttributesRequiredOnCreationOrtsteil() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("ort_id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (ortsteil) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: ort_id")
	void setAttributesRequiredOnCreationOrtId() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("ortsteil", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (ort_id) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: referenziertInAnderenTabellen")
	void setAttributesNotPatchableReferenziertInAnderenTabellen() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("referenziertInAnderenTabellen", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: referenziertInAnderenTabellen.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOOrtsteil(1L, "");

		this.data.initDTO(dto, 2L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 2L)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOOrtsteil(1L, "");

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOOrtsteil(1L, "");

		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(OrtsteilKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Ortsteils darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Ortsteil mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var ort = new OrtKatalogEintrag();
		ort.id = 1L;
		ort.ortsname = "ortsname";
		ort.plz = "12345";
		when(this.dataOrte.getAll()).thenReturn(List.of(ort));
		final var dto1 = new DTOOrtsteil(1L, "bez1");
		dto1.Ort_ID = 1L;
		final var dto2 = new DTOOrtsteil(2L, "bez2");
		dto2.Ort_ID = null;
		when(this.conn.queryAll(DTOOrtsteil.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(OrtsteilKatalogEintrag.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("bezeichnungOrt", ort.ortsname)
								.hasFieldOrPropertyWithValue("plzOrt", ort.plz)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("ortsteil", "bez1"),
						f2 -> assertThat(f2)
								.isInstanceOf(OrtsteilKatalogEintrag.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("bezeichnungOrt", null)
								.hasFieldOrPropertyWithValue("plzOrt", null)
								.hasFieldOrPropertyWithValue("ortsteil", "bez2")
				);
	}

	@Test
	@DisplayName("getAll | referenced in other tabled")
	void getAllReferencedInOtherTables() {
		when(this.dataOrte.getAll()).thenReturn(Collections.emptyList());
		final var dto1 = new DTOOrtsteil(1L, "bez1");
		final var dto2 = new DTOOrtsteil(2L, "bez2");
		when(this.conn.queryAll(DTOOrtsteil.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(OrtsteilKatalogEintrag.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						f2 -> assertThat(f2)
								.isInstanceOf(OrtsteilKatalogEintrag.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.dataOrte.getAll()).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOOrtsteil.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOOrtsteil(1L, "bezeichnung");

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtsteilKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("ortsteil", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", false)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("map | bezeichnung null")
	void mapOrtsteilIsNull() {
		final var dto = new DTOOrtsteil(1L, "ortsteil");
		dto.Bezeichnung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtsteilKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("ortsteil", null);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarIsNull() {
		final var dto = new DTOOrtsteil(1L, "bezeichnung");
		dto.Sichtbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtsteilKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("map | sortierung null")
	void mapSortierungIsNull() {
		final var dto = new DTOOrtsteil(1L, "bezeichnung");
		dto.Sortierung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtsteilKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOOrtsteil.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOOrtsteil(1L, "bezeichnung");

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | ort_id | null")
	void patchOrtIdIsNull() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));
		final var map = new HashMap<String, Object>();
		map.put("ort_id", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut ort_id: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ort_id | no changes")
	void patchOrtIdNoChanges() throws ApiOperationException {
		final var dto = new DTOOrtsteil(1L, "abc");
		dto.Ort_ID = 42L;
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ort_id", 42L));

		assertThat(dto.Ort_ID).isEqualTo(42L);
		verify(this.conn, never()).queryByKey(DTOOrt.class, 42);
	}

	@Test
	@DisplayName("patch | ort_id | wrong id")
	void patchOrtIdNoMatch() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ort_id", 42L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde kein Ort mit der ID 42 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ort_id")
	void patchOrtId() throws ApiOperationException {
		final var dto = new DTOOrtsteil(1L, "abc");
		dto.Ort_ID = 11L;
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		when(this.conn.queryByKey(DTOOrt.class, 42L)).thenReturn(mock(DTOOrt.class));

		this.data.patch(1L, Map.of("ort_id", 42L));

		assertThat(dto.Ort_ID).isEqualTo(42L);
	}


	@Test
	@DisplayName("patch | ortsteil > 30 Zeichen")
	void patchOrtsteilIsTooLong() {
		final var dto = new DTOOrtsteil(1L, "bezeichnung");

		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsteil", RandomStringUtils.insecure().nextAscii(31))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut ortsteil: Die Länge des Strings ist auf 30 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ortsteil Null")
	void patchOrtsteilIsNull() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));
		final var map = new HashMap<String, Object>();
		map.put("ortsteil", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut ortsteil: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ortsteil empty")
	void patchOrtsteilIsEmpty() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsteil", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut ortsteil: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ortsteil is blank")
	void patchOrtsteilIsBlank() throws ApiOperationException {
		final var dto = new DTOOrtsteil(1L, "ortsteil");
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ortsteil", "    "));

		verify(this.conn, never()).queryAll(DTOOrtsteil.class);
		assertThat(dto.Bezeichnung).isEqualTo("ortsteil");
	}

	@Test
	@DisplayName("patch | ortsteil doesn't change")
	void patchOrtsteilDoesNotChange() throws ApiOperationException {
		final var dto = new DTOOrtsteil(1L, "ortsteil");
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ortsteil", "ortsteil"));

		verify(this.conn, never()).queryAll(DTOOrtsteil.class);
		assertThat(dto.Bezeichnung).isEqualTo("ortsteil");
	}

	@Test
	@DisplayName("patch | ortsteil already used")
	void patchOrtsteilAlreadyUsed() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));
		when(this.conn.queryAll(DTOOrtsteil.class)).thenReturn(List.of(new DTOOrtsteil(1L, "test")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsteil", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung des Ortsteil test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ortsteil already used different case")
	void patchOrtsteilAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));
		when(this.conn.queryAll(DTOOrtsteil.class)).thenReturn(List.of(new DTOOrtsteil(2L, "TEST")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsteil", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung des Ortsteil test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ortsteil change case in same object")
	void patchOrtsteilChangeCase() throws ApiOperationException {
		final var dto = new DTOOrtsteil(1L, "test");
		when(conn.queryAll(DTOOrtsteil.class)).thenReturn(List.of(dto));
		final var newDto = new DTOOrtsteil(2L, "abc");
		when(this.conn.queryByKey(DTOOrtsteil.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("ortsteil", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | ortsteil dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchOrtsteilInDtoISNull() {
		final var dto = new DTOOrtsteil(1L, "123");
		dto.Bezeichnung = null;
		when(conn.queryAll(DTOOrtsteil.class)).thenReturn(List.of(dto));
		final var newDto = new DTOOrtsteil(2L, "abc");
		when(this.conn.queryByKey(DTOOrtsteil.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("ortsteil", "test")));
	}

	@Test
	@DisplayName("patch | ortsteil")
	void patchOrtsteil() throws ApiOperationException {
		final var dto = new DTOOrtsteil(1L, "ortsteil");
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ortsteil", "neu"));

		assertThat(dto.Bezeichnung).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | istSichtbar is null")
	void patchIstSichtbarIsNull() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));
		final var map = new HashMap<String, Object>();
		map.put("istSichtbar", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut istSichtbar: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | istSichtbar")
	void patchIstSichtbar() throws ApiOperationException {
		final var dto = new DTOOrtsteil(1L, "bezeichnung");
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | sortierung is null")
	void patchSortierungIsNull() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));
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
		final var dto = new DTOOrtsteil(1L, "bezeichnung");
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.Sortierung).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOOrtsteil.class, 1L)).thenReturn(mock(DTOOrtsteil.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
