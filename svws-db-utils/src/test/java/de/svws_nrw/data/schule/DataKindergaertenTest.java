package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Kindergarten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOKindergarten;
import de.svws_nrw.db.schema.Schema;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataKatalogKindergaerten")
@ExtendWith(MockitoExtension.class)
class DataKindergaertenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKindergaerten data;

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
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: referenziertInAnderenTabellen")
	void setAttributesNotPatchableReferenziertInAnderenTabellen() {
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("referenziertInAnderenTabellen", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: referenziertInAnderenTabellen.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOKindergarten(1L);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto).hasFieldOrPropertyWithValue("ID", 2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOKindergarten(1L);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);

		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Kindergarten.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID für den Kindergarten darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOKindergarten.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde kein Kindergarten mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOKindergarten(1L);
		dto.Bezeichnung = "bezeichnung";
		dto.Bemerkung = "bemerkung";
		dto.Tel = "tel";
		dto.Email = "email";
		dto.Strassenname = "strassenname";
		dto.HausNr = "hausnr";
		dto.HausNrZusatz = "hausNrZusatz";
		dto.PLZ = "plz";
		dto.Ort = "ort";
		dto.Sichtbar = true;
		dto.Sortierung = 123;

		assertThat(this.data.map(dto))
				.isInstanceOf(Kindergarten.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("bemerkung", dto.Bemerkung)
				.hasFieldOrPropertyWithValue("tel", dto.Tel)
				.hasFieldOrPropertyWithValue("email", dto.Email)
				.hasFieldOrPropertyWithValue("strassenname", dto.Strassenname)
				.hasFieldOrPropertyWithValue("hausNr", dto.HausNr)
				.hasFieldOrPropertyWithValue("hausNrZusatz", dto.HausNrZusatz)
				.hasFieldOrPropertyWithValue("plz", dto.PLZ)
				.hasFieldOrPropertyWithValue("ort", dto.Ort)
				 .hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 123);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarIsNull() {
		final var dto = new DTOKindergarten(1L);

		dto.Sichtbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Kindergarten.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("map | sortierung null")
	void mapSortierungIsNull() {
		final var dto = new DTOKindergarten(1L);

		dto.Sortierung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Kindergarten.class)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("getAll")
	void getAll() {
		final var dto1 = new DTOKindergarten(1L);
		final var dto2 = new DTOKindergarten(2L);
		when(this.conn.queryAll(DTOKindergarten.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Kindergarten.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						f2 -> assertThat(f2)
								.isInstanceOf(Kindergarten.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOKindergarten.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOKindergarten.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOKindergarten(1L);

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | bezeichnung Null")
	void patchBezeichnungIsNull() {
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));
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
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung is blank")
	void patchBezeichnungIsBlank() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Bezeichnung = "bezeichnung";

		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "    "));

		verify(this.conn, never()).queryAll(DTOKindergarten.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung doesn't change")
	void patchBezeichnungDoesNotChange() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Bezeichnung = "bezeichnung";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOKindergarten.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung already used")
	void patchBezeichnungAlreadyUsed() {
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));
		final var dto = new DTOKindergarten(1L);
		dto.Bezeichnung = "test";
		when(this.conn.queryAll(DTOKindergarten.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already used different case")
	void patchBezeichnungAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));
		final var dto = new DTOKindergarten(1L);
		dto.Bezeichnung = "TEST";
		when(this.conn.queryAll(DTOKindergarten.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung change case in same object")
	void patchBezeichnungChangeCase() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Bezeichnung = "bezeichnung";
		when(conn.queryAll(DTOKindergarten.class)).thenReturn(List.of(dto));
		final var newDto = new DTOKindergarten(1L);
		newDto.Bezeichnung = "abc";
		when(this.conn.queryByKey(DTOKindergarten.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("bezeichnung", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | bezeichnung dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchBezeichnungInDtoISNull() {
		final var dto = new DTOKindergarten(1L);
		dto.Bezeichnung = null;
		when(conn.queryAll(DTOKindergarten.class)).thenReturn(List.of(dto));
		final var newDto = new DTOKindergarten(1L);
		newDto.Bezeichnung = "abc";
		when(this.conn.queryByKey(DTOKindergarten.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("bezeichnung", "test")));
	}

	@Test
	@DisplayName("patch | bezeichnung")
	void patchBezeichnung() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Bezeichnung = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "new"));

		assertThat(dto.Bezeichnung).isEqualTo("new");
	}

	@ParameterizedTest
	@DisplayName("patch | max Length exceeded")
	@MethodSource("getArgumentsMaxLength")
	void patchBezeichnungIsTooLong(final String name, final int maxLength) {
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of(name, RandomStringUtils.insecure().nextAscii(maxLength + 1))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut %s: Die Länge des Strings ist auf %d Zeichen limitiert.".formatted(name, maxLength))
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private static Stream<Arguments> getArgumentsMaxLength() {
		return Stream.of(
				arguments("bezeichnung", Schema.tab_K_Kindergarten.col_Bezeichnung.datenlaenge()),
				arguments("bemerkung", Schema.tab_K_Kindergarten.col_Bemerkung.datenlaenge()),
				arguments("tel", Schema.tab_K_Kindergarten.col_Tel.datenlaenge()),
				arguments("email", Schema.tab_K_Kindergarten.col_Email.datenlaenge()),
				arguments("strassenname", Schema.tab_K_Kindergarten.col_Strassenname.datenlaenge()),
				arguments("hausNr", Schema.tab_K_Kindergarten.col_HausNr.datenlaenge()),
				arguments("hausNrZusatz", Schema.tab_K_Kindergarten.col_HausNrZusatz.datenlaenge()),
				arguments("plz", Schema.tab_K_Kindergarten.col_PLZ.datenlaenge()),
				arguments("ort", Schema.tab_K_Kindergarten.col_Ort.datenlaenge())
		);
	}

	@Test
	@DisplayName("patch | bemerkung")
	void patchBemerkung() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Bemerkung = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bemerkung", "new"));

		assertThat(dto.Bemerkung).isEqualTo("new");
	}

	@Test
	@DisplayName("patch | tel")
	void patchTel() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Tel = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("tel", "new"));

		assertThat(dto.Tel).isEqualTo("new");
	}

	@Test
	@DisplayName("patch | email")
	void patchEmail() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Email = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("email", "new"));

		assertThat(dto.Email).isEqualTo("new");
	}

	@Test
	@DisplayName("patch | strassenname")
	void patchStrassenname() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Strassenname = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("strassenname", "new"));

		assertThat(dto.Strassenname).isEqualTo("new");
	}

	@Test
	@DisplayName("patch | hausNr")
	void patchHausNr() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.HausNr = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("hausNr", "new"));

		assertThat(dto.HausNr).isEqualTo("new");
	}

	@Test
	@DisplayName("patch | hausNrZusatz")
	void patchHausNrZusatz() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.HausNrZusatz = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("hausNrZusatz", "new"));

		assertThat(dto.HausNrZusatz).isEqualTo("new");
	}

	@Test
	@DisplayName("patch | plz")
	void patchPlz() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.PLZ = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("plz", "new"));

		assertThat(dto.PLZ).isEqualTo("new");
	}

	@Test
	@DisplayName("patch | ort")
	void patchOrt() throws ApiOperationException {
		final var dto = new DTOKindergarten(1L);
		dto.Ort = "old";
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("ort", "new"));

		assertThat(dto.Ort).isEqualTo("new");
	}

	@Test
	@DisplayName("patch | istSichtbar is null")
	void patchIstSichtbarIsNull() {
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));
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
		final var dto = new DTOKindergarten(1L);
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | sortierung is null")
	void patchSortierungIsNull() {
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));
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
		final var dto = new DTOKindergarten(1L);
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.Sortierung).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOKindergarten.class, 1L)).thenReturn(mock(DTOKindergarten.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
