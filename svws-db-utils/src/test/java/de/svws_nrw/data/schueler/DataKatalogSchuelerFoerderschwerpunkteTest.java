package de.svws_nrw.data.schueler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
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

import static java.util.Collections.emptyList;
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

@DisplayName("Diese Klasse testet die Klasse DataKatalogSchuelerFoerderschwerpunkte")
@ExtendWith(MockitoExtension.class)
class DataKatalogSchuelerFoerderschwerpunkteTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKatalogSchuelerFoerderschwerpunkte data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: bezeichnung")
	void setAttributesRequiredOnCreationTest() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (kuerzelStatistik) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(mock(DTOFoerderschwerpunkt.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: text")
	void setAttributesNotPatchableText() {
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(mock(DTOFoerderschwerpunkt.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("text", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: text.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}


	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOFoerderschwerpunkt(1L, "");
		dto.Sortierung = null;
		dto.Sichtbar = null;

		this.data.initDTO(dto, 2L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 2L)
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOFoerderschwerpunkt(1L, "");

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(FoerderschwerpunktEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Förderschwerpunktes darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde kein Förderschwerpunkt mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOFoerderschwerpunkt(1L, "bezeichnung1");
		final var dto2 = new DTOFoerderschwerpunkt(2L, "bezeichnung2");
		when(this.conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(FoerderschwerpunktEintrag.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung1")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						f2 -> assertThat(f2)
								.isInstanceOf(FoerderschwerpunktEintrag.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung2")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(emptyList());

		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		final var fs = Foerderschwerpunkt.BL.historie().getLast();
		dto.StatistikKrz = fs.schluessel;
		dto.Sichtbar = true;
		dto.Sortierung = 123;

		assertThat(this.data.map(dto))
				.isInstanceOf(FoerderschwerpunktEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung")
				.hasFieldOrPropertyWithValue("kuerzelStatistik", fs.schluessel)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung);
	}

	@Test
	@DisplayName("map | bezeichnung null")
	void mapBezeichnungISNull() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Bezeichnung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(FoerderschwerpunktEintrag.class)
				.hasFieldOrPropertyWithValue("bezeichnung", "");
	}

	@Test
	@DisplayName("map | kuerzelStatistik null")
	void mapKuerzelStatistikIsNull() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.StatistikKrz = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(FoerderschwerpunktEintrag.class)
				.hasFieldOrPropertyWithValue("kuerzelStatistik", "");
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarIsNull() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Sichtbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(FoerderschwerpunktEintrag.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("map | sortierung null")
	void mapSortierungIsNull() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Sortierung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(FoerderschwerpunktEintrag.class)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOFoerderschwerpunkt.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto  = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | bezeichnung")
	void patchBezeichnung() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "neu"));

		assertThat(dto.Bezeichnung).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | bezeichnung > 50 Zeichen")
	void patchBezeichnungIsTooLong() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", RandomStringUtils.insecure().nextAscii(51))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 50 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung Null")
	void patchBezeichnungIsNull() {
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(mock(DTOFoerderschwerpunkt.class));
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
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(mock(DTOFoerderschwerpunkt.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung is blank")
	void patchBezeichnungIsBlank() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "    "));

		verify(this.conn, never()).queryAll(DTOFoerderschwerpunkt.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung doesn't change")
	void patchBezeichnungDoesNotChange() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOFoerderschwerpunkt.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung already used")
	void patchBezeichnungAlreadyUsed() {
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(mock(DTOFoerderschwerpunkt.class));
		when(this.conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(List.of(new DTOFoerderschwerpunkt(2L, "test")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Das Kürzel test darf nicht doppelt vergeben werden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already used different case")
	void patchBezeichnungAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(mock(DTOFoerderschwerpunkt.class));
		when(this.conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(List.of(new DTOFoerderschwerpunkt(2L, "TEST")));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Das Kürzel test darf nicht doppelt vergeben werden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung change case in same object")
	void patchBezeichnungChangeCase() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "test");
		when(conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(List.of(dto));
		final var newDto = new DTOFoerderschwerpunkt(2L, "abc");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("bezeichnung", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | bezeichnung dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchBezeichnungInDtoISNull() {
		final var dto = new DTOFoerderschwerpunkt(1L, "123");
		dto.Bezeichnung = null;
		when(conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(List.of(dto));
		final var newDto = new DTOFoerderschwerpunkt(2L, "abc");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("bezeichnung", "test")));
	}

	@Test
	@DisplayName("patch | istSichtbar")
	void patchIstSichtbar() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | istSichtbar is null")
	void patchIstSichtbarIsNull() {
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(mock(DTOFoerderschwerpunkt.class));
		final var map = new HashMap<String, Object>();
		map.put("istSichtbar", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut istSichtbar: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Sortierung")
	void patchSortierung() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.Sortierung).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | sortierung is null")
	void patchSortierungIsNull() {
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(mock(DTOFoerderschwerpunkt.class));
		final var map = new HashMap<String, Object>();
		map.put("sortierung", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut sortierung: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik")
	void mapAttributeKuerzelStatistik() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.StatistikKrz = "xy";
		final var fs = Foerderschwerpunkt.BL.historie().getLast();

		this.data.mapAttribute(dto, "kuerzelStatistik", fs.kuerzel, null);

		assertThat(dto.StatistikKrz).isEqualTo(fs.kuerzel);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik Zu lang")
	void mapAttributeKuerzelStatistikIsTooLong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOFoerderschwerpunkt.class), "kuerzelStatistik", "Zu viele Zeichen", null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzelStatistik: Die Länge des Strings ist auf 2 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik is wrong")
	void mapAttributeKuerzelStatistikIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOFoerderschwerpunkt.class), "kuerzelStatistik", "-1", null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Zum angegebenen Kürzel -1 wurde kein passender Förderschwerpunkt gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik is null")
	void mapAttributeKuerzelStatistikIsNull() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOFoerderschwerpunkt.class), "kuerzelStatistik", null, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzelStatistik: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik is empty")
	void mapAttributeKuerzelStatistikIsEmpty() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOFoerderschwerpunkt.class), "kuerzelStatistik", "", null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut kuerzelStatistik: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzelStatistik is blank")
	void patchKuerzelStatistikIsBlank() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.StatistikKrz = "ab";

		this.data.mapAttribute(dto, "kuerzelStatistik", "  ", null);

		verify(this.conn, never()).queryAll(DTOFoerderschwerpunkt.class);
		assertThat(dto.StatistikKrz).isEqualTo("ab");
	}

	@Test
	@DisplayName("patch | kuerzelStatistik doesn't change")
	void patchKuerzelStatistikDoesNotChange() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.StatistikKrz = "ab";

		this.data.mapAttribute(dto, "kuerzelStatistik", "ab", null);

		verify(this.conn, never()).queryAll(DTOFoerderschwerpunkt.class);
		assertThat(dto.StatistikKrz).isEqualTo("ab");
	}

	@Test
	@DisplayName("mapAttribute | unknown argument")
	void mapAttribute_unknownArgument() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOFoerderschwerpunkt.class), "unknown", null, null))
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
		final var dto = new DTOFoerderschwerpunkt(1L, "abc");

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Der Förderschwerpunkt mit dem Kürzel abc ist in der Datenbank referenziert und kann daher nicht gelöscht werden.");
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
		final var dto = new DTOFoerderschwerpunkt(1L, "abc");

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(response)
				.hasFieldOrPropertyWithValue("success", true)
				.satisfies(r -> assertThat(r.log).isEmpty());
	}

	@Test
	@DisplayName("getIdsOfReferencedFoerderschwerpunkte | null")
	void getIdsOfReferencedFoerderschwerpunkteIdsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		final Method method = DataKatalogSchuelerFoerderschwerpunkte.class.getDeclaredMethod("getIdsOfReferencedFoerderschwerpunkte", Set.class);
		method.setAccessible(true);
		final Object result = method.invoke(this.data, (Object) null);

		assertThat((Set<Long>) result).isEmpty();

		verify(this.conn, never()).query(anyString(), any());
	}

	@Test
	@DisplayName("getIdsOfReferencedFoerderschwerpunkte | emptyList")
	void getIdsOfReferencedFoerderschwerpunkteIdsEmpty() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		final Method method = DataKatalogSchuelerFoerderschwerpunkte.class.getDeclaredMethod("getIdsOfReferencedFoerderschwerpunkte", Set.class);
		method.setAccessible(true);
		final Set<Long> emptySet = Collections.emptySet();
		final Object result = method.invoke(this.data, emptySet);

		assertThat((Set<Long>) result).isEmpty();

		verify(this.conn, never()).query(anyString(), any());
	}

}
