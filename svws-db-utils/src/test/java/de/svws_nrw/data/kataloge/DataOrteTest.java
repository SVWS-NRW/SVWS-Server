package de.svws_nrw.data.kataloge;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
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

/** Diese Klasse testet die Klasse {@link DataOrte} */
@DisplayName("Diese Klasse testet die Klasse DataOrte")
@ExtendWith(MockitoExtension.class)
class DataOrteTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataOrte data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: ortsname")
	void setAttributesRequiredOnCreationOrtsname() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("plz", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (ortsname) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: plz")
	void setAttributesRequiredOnCreationPlz() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("ortsname", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (plz) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(mock(DTOOrt.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOOrt(1L, "12345", "ort");

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "13245", "ort");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Orts darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOOrt.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde kein Ort mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOOrt(1L, "plz1", "bez1");
		final var dto2 = new DTOOrt(2L, "plz2", "bez2");
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(OrtKatalogEintrag.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("plz", "plz1")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true)
								.hasFieldOrPropertyWithValue("ortsname", "bez1"),
						f2 -> assertThat(f2)
								.isInstanceOf(OrtKatalogEintrag.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("plz", "plz2")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
								.hasFieldOrPropertyWithValue("ortsname", "bez2")
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOOrt(1L, "plz1", "bez1");
		dto.Sichtbar = true;
		dto.Aenderbar = true;
		dto.Kreis = "kreis";
		dto.Land = "land";
		dto.Sortierung = 42;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("plz", dto.PLZ)
				.hasFieldOrPropertyWithValue("ortsname", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("kreis", dto.Kreis)
				.hasFieldOrPropertyWithValue("kuerzelBundesland", dto.Land)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar)
				.hasFieldOrPropertyWithValue("istAenderbar", dto.Aenderbar);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarIsNull() {
		final var dto = new DTOOrt(1L, "plz1", "bez1");
		dto.Sichtbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("map | sortierung null")
	void mapSortierungIsNull() {
		final var dto = new DTOOrt(1L, "plz1", "bez1");
		dto.Sortierung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("map | aenderbar null")
	void mapAenderbarIsNull() {
		final var dto = new DTOOrt(1L, "plz1", "bez1");
		dto.Aenderbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(OrtKatalogEintrag.class)
				.hasFieldOrPropertyWithValue("istAenderbar", false);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOOrt.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOOrt(1L, "12345", "ort");

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | Ortsname > 50 Zeichen")
	void patchOrtsnameIsTooLong() {
		final var dto = new DTOOrt(1L, "12345", "ort");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsname", RandomStringUtils.insecure().nextAscii(51))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut ortsname: Die Länge des Strings ist auf 50 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Ortsname Null")
	void patchOrtsnameIsNull() {
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(mock(DTOOrt.class));
		final var map = new HashMap<String, Object>();
		map.put("ortsname", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut ortsname: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Ortsname empty")
	void patchOrtsnameIsEmpty() {
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(mock(DTOOrt.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsname", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut ortsname: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Ortsname is blank")
	void patchOrtsnameIsBlank() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "12345", "ort");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ortsname", "    "));

		verify(this.conn, never()).queryAll(DTOOrt.class);
		assertThat(dto.Bezeichnung).isEqualTo("ort");
	}

	@Test
	@DisplayName("patch | Ortsname doesn't change")
	void patchOrtsnameDoesNotChange() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "12345", "ort");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ortsname", "ort"));

		verify(this.conn, never()).queryAll(DTOOrt.class);
		assertThat(dto.Bezeichnung).isEqualTo("ort");
	}

	@Test
	@DisplayName("patch | Ortsname already used")
	void patchOrtsnameAlreadyUsed() {
		final var dto = new DTOOrt(1L, "12345", "abc");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(List.of(new DTOOrt(2L, "12345", "test")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsname", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Ortsname test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Ortsname already used different case")
	void patchOrtsnameAlreadyUsedWithDifferentCase() {
		final var dto = new DTOOrt(1L, "12345", "abc");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(List.of(new DTOOrt(2L, "12345", "TEST")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsname", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Ortsname test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Ortsname used with different Plz")
	void patchOrtsnameUsedWithDifferentPlz() throws ApiOperationException {
		final var differentDtoInDataBase = new DTOOrt(2L, "12345", "test");
		final var dtoToPatch = new DTOOrt(1L, "54321", "lalala");
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(List.of(differentDtoInDataBase));
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dtoToPatch);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ortsname", "test"));

		assertThat(dtoToPatch.Bezeichnung).isEqualTo("test");
	}

	@Test
	@DisplayName("patch | Ortsname used with same Plz")
	void patchOrtsnameUsedWithSamePlz() {
		final var differentDtoInDataBase = new DTOOrt(2L, "12345", "test");
		final var dto = new DTOOrt(1L, "12345", "beforeChange");
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(List.of(differentDtoInDataBase, dto));
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("ortsname", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Ortsname test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Ortsname is unique | different Plz")
	void createOrtsnameIsUnique_DifferentPlz() {
		final var existingEntity = new DTOOrt(1L, "12345", "doppelte Bezeichnung");
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(List.of(existingEntity));
		when(this.conn.transactionPersist(any())).thenReturn(true);
		when(this.conn.queryByKey(DTOOrt.class, 0L)).thenReturn(mock(DTOOrt.class));

		assertThatNoException()
				.isThrownBy(() -> this.data.add(Map.of("ortsname", "doppelte Bezeichnung", "plz", "54321")));
	}

	@Test
	@DisplayName("create | Ortsname is unique | same Plz")
	void createOrtsnameIsUnique_samePlz() {
		final var existingEntity = new DTOOrt(1L, "12345", "doppelte Bezeichnung");
		when(this.conn.queryAll(DTOOrt.class)).thenReturn(List.of(existingEntity));

		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("ortsname", "doppelte Bezeichnung", "plz", "12345")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Ortsname doppelte Bezeichnung ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Ortsname change case in same object")
	void patchOrtsnameChangeCase() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "12345", "test");
		when(conn.queryAll(DTOOrt.class)).thenReturn(List.of(dto));
		final var newDto = new DTOOrt(2L, "12345", "abc");
		when(this.conn.queryByKey(DTOOrt.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("ortsname", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | Ortsname dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchOrtsnameInDtoISNull() {
		final var dto = new DTOOrt(1L, "12345", "123");
		dto.Bezeichnung = null;
		when(conn.queryAll(DTOOrt.class)).thenReturn(List.of(dto));
		final var newDto = new DTOOrt(2L, "12345", "abc");
		when(this.conn.queryByKey(DTOOrt.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("ortsname", "test")));
	}

	@Test
	@DisplayName("patch | Ortsname")
	void patchOrtsname() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "12345", "bezeichnung");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ortsname", "neu"));

		assertThat(dto.Bezeichnung).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | Kreis > 50 Zeichen")
	void patchKreisIsTooLong() {
		final var dto = new DTOOrt(1L, "12345", "ort");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kreis", RandomStringUtils.insecure().nextAscii(4))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kreis: Die Länge des Strings ist auf 3 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Kreis")
	void patchKreis() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "12345", "ort");
		dto.Kreis = "123";
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("kreis", "321"));

		assertThat(dto.Kreis).isEqualTo("321");
	}

	@Test
	@DisplayName("patch | kuerzelBundesland > 50 Zeichen")
	void patchKuerzelBundeslandIsTooLong() {
		final var dto = new DTOOrt(1L, "12345", "ort");
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kuerzelBundesland", RandomStringUtils.insecure().nextAscii(3))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzelBundesland: Die Länge des Strings ist auf 2 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzelBundesland")
	void patchKuerzelBundesland() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "12345", "ort");
		dto.Land = "12";
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("kuerzelBundesland", "45"));

		assertThat(dto.Land).isEqualTo("45");
	}

	@Test
	@DisplayName("patch | istSichtbar is null")
	void patchIstSichtbarIsNull() {
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(mock(DTOOrt.class));
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
		final var dto = new DTOOrt(1L, "12345", "ort");
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | istAenderbar")
	void patchIstAenderbar() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "12345", "ort");
		dto.Aenderbar = false;
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istAenderbar", true));

		assertThat(dto.Aenderbar).isTrue();
	}

	@Test
	@DisplayName("patch | Sortierung")
	void patchSortierung() throws ApiOperationException {
		final var dto = new DTOOrt(1L, "12345", "ort");
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.Sortierung).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOOrt.class, 1L)).thenReturn(mock(DTOOrt.class));

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
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOOrt(1L, "12345", "ort");

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Der Ort mit dem Name ort ist in der Datenbank referenziert und kann daher nicht gelöscht werden.");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponseTest | foerderschwerpunkt not referenced")
	void checkBeforeDeletionWithSimpleOperationResponseFoerderschwerpunktNotReferenced() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(2L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOOrt(1L, "12345", "ort");

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(response)
				.hasFieldOrPropertyWithValue("success", true)
				.satisfies(r -> assertThat(r.log).isEmpty());
	}

}
