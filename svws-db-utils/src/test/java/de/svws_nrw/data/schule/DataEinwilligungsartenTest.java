package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.types.schule.Einwilligungsschluessel;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("Diese Testklasse testet die Klasse DataKatalogEinwilligungsarten")
@ExtendWith(MockitoExtension.class)
class DataEinwilligungsartenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataEinwilligungsarten data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: bezeichnung")
	void setAttributesRequiredOnCreationBezeichnung() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("idPersonTyp", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (bezeichnung) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: personTyp")
	void setAttributesRequiredOnCreationPersonTyp() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (idPersonTyp) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "", true, 1);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 2L)
				.hasFieldOrPropertyWithValue("Sortierung", 32000)
				.hasFieldOrPropertyWithValue("personTyp", null);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "", true, 1);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "", true, 1);
		dto.personTyp = PersonTyp.ERZIEHER;

		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID der Einwilligungsart darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Einwilligungsart mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOKatalogEinwilligungsart(1L, "bez1", true, 1);
		dto1.personTyp = PersonTyp.ERZIEHER;
		final var dto2 = new DTOKatalogEinwilligungsart(2L, "bez2", false, 2);
		dto2.personTyp = PersonTyp.SCHUELER;
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Einwilligungsart.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("bezeichnung", "bez1")
								.hasFieldOrPropertyWithValue("idPersonTyp", 3)
								.hasFieldOrPropertyWithValue("istSichtbar", true)
								.hasFieldOrPropertyWithValue("sortierung", 1),
						f2 -> assertThat(f2)
								.isInstanceOf(Einwilligungsart.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("bezeichnung", "bez2")
								.hasFieldOrPropertyWithValue("idPersonTyp", 2)
								.hasFieldOrPropertyWithValue("istSichtbar", false)
								.hasFieldOrPropertyWithValue("sortierung", 2)
				);
	}

	@Test
	@DisplayName("getAll | referenced in other tabled")
	void getAllReferencedInOtherTables() {
		final var dto1 = new DTOKatalogEinwilligungsart(1L, "bez1", true, 1);
		dto1.personTyp = PersonTyp.ERZIEHER;
		final var dto2 = new DTOKatalogEinwilligungsart(2L, "bez2", false, 2);
		dto2.personTyp = PersonTyp.SCHUELER;
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Einwilligungsart.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						f2 -> assertThat(f2)
								.isInstanceOf(Einwilligungsart.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.personTyp = PersonTyp.ERZIEHER;
		dto.Schluessel = "key";
		dto.Beschreibung = "tolle Beschreibung";

		assertThat(this.data.map(dto))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung)
				.hasFieldOrPropertyWithValue("idPersonTyp", dto.personTyp.id)
				.hasFieldOrPropertyWithValue("schluessel", dto.Schluessel)
				.hasFieldOrPropertyWithValue("beschreibung", dto.Beschreibung);
	}

	@Test
	@DisplayName("map | bezeichnung null")
	void mapBezeichnungIsNull() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.Bezeichnung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("bezeichnung", "");
	}

	@Test
	@DisplayName("map | schluessel null")
	void mapSchluesselIsNull() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.Schluessel = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("schluessel", "");
	}

	@Test
	@DisplayName("map | beschreibung null")
	void mapBeschreibungIsNull() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.Beschreibung = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("beschreibung", "");
	}

	@Test
	@DisplayName("map | personTyp null")
	void mapPersonTypIsNull() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.personTyp = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("idPersonTyp", -1);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarIsNull() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.Sichtbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("addBasic | lehrerEinwilligung")
	void addBasicLehrerEinwilligung() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.personTyp = PersonTyp.LEHRER;
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.addBasic(1L, Map.of("bezeichnung", "bezeichnung", "idPersonTyp", 1));

		verify(this.conn, times(1)).queryList("SELECT e.ID FROM DTOLehrer e", Long.class);
		verify(this.conn, times(1)).transactionPersistAll(any());
		verify(this.conn, times(2)).transactionFlush();
	}

	@Test
	@DisplayName("addBasic | schuelerEinwilligung")
	void addBasicSchuelerEinwilligung() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.personTyp = PersonTyp.SCHUELER;
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.addBasic(1L, Map.of("bezeichnung", "bezeichnung", "idPersonTyp", 1));

		verify(this.conn, times(1)).queryList("SELECT e.ID FROM DTOSchueler e", Long.class);
		verify(this.conn, times(1)).transactionPersistAll(any());
		verify(this.conn, times(2)).transactionFlush();
	}

	@Test
	@DisplayName("addBasic | erzieherEinwilligung | not supported")
	void addBasicErzieherEinwilligung() {
		assertThatException()
				.isThrownBy(() -> this.data.addBasic(1L, Map.of("bezeichnung", "bezeichnung", "idPersonTyp", 3)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der PersonTyp Erzieher wird derzeit nicht unterstützt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOKatalogEinwilligungsart.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | bezeichnung > 250 Zeichen")
	void patchBezeichnungIsTooLong() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", RandomStringUtils.insecure().nextAscii(251))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 250 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung Null")
	void patchBezeichnungIsNull() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));
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
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung is blank")
	void patchBezeichnungIsBlank() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "    "));

		verify(this.conn, never()).queryAll(DTOKatalogEinwilligungsart.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung doesn't change")
	void patchBezeichnungDoesNotChange() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOKatalogEinwilligungsart.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung already used")
	void patchBezeichnungAlreadyUsed() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(new DTOKatalogEinwilligungsart(1L, "test", true, 123)));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already used different case")
	void patchBezeichnungAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(new DTOKatalogEinwilligungsart(2L, "TEST", true, 123)));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung used with different personTyp")
	void patchBezeichnungUsedWithDifferentPersonTyp() throws ApiOperationException {
		final var differentDtoInDataBase = new DTOKatalogEinwilligungsart(2L, "test", true, 123);
		differentDtoInDataBase.personTyp = PersonTyp.LEHRER;
		final var dtoToPatch = new  DTOKatalogEinwilligungsart(1L, "dtoToPatch", true, 123);
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(differentDtoInDataBase));
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dtoToPatch);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "test"));

		assertThat(dtoToPatch.Bezeichnung).isEqualTo("test");
	}

	@Test
	@DisplayName("patch | bezeichnung used with same personTyp")
	void patchBezeichnungUsedWithSamePersonTyp() {
		final var differentDtoInDataBase = new DTOKatalogEinwilligungsart(2L, "test", true, 123);
		differentDtoInDataBase.personTyp = PersonTyp.LEHRER;
		final var dto = new DTOKatalogEinwilligungsart(1L, "beforeChange", true, 123);
		dto.personTyp = PersonTyp.LEHRER;
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(differentDtoInDataBase, dto));
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | bezeichnung is unique | different PersonTyp")
	void createBezeichnungIsUnique_DifferentPersonTyp() {
		final var existingEntity = new DTOKatalogEinwilligungsart(1L, "doppelte Bezeichnung", true, 123);
		existingEntity.personTyp = PersonTyp.SCHUELER;
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(existingEntity));
		when(this.conn.transactionPersist(any())).thenReturn(true);
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 0L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));

		assertThatNoException()
				.isThrownBy(() -> this.data.add(Map.of("bezeichnung", "doppelte Bezeichnung", "idPersonTyp", PersonTyp.LEHRER.id)));
	}

	@Test
	@DisplayName("create | bezeichnung is unique | same PersonTyp")
	void createBezeichnungIsUnique_samePersonTyp() {
		final var existingEntity = new DTOKatalogEinwilligungsart(1L, "doppelte Bezeichnung", true, 123);
		existingEntity.personTyp = PersonTyp.LEHRER;
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(existingEntity));

		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("bezeichnung", "doppelte Bezeichnung", "idPersonTyp", PersonTyp.LEHRER.id)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung doppelte Bezeichnung ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung change case in same object")
	void patchBezeichnungChangeCase() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		when(conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(dto));
		final var newDto = new DTOKatalogEinwilligungsart(2L, "abc", true, 123);
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("bezeichnung", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | bezeichnung dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchBezeichnungInDtoISNull() {
		final var dto = new DTOKatalogEinwilligungsart(1L, "123", true, 123);
		dto.Bezeichnung = null;
		when(conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(dto));
		final var newDto = new DTOKatalogEinwilligungsart(2L, "abc", true, 123);
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("bezeichnung", "test")));
	}

	@Test
	@DisplayName("patch | bezeichnung")
	void patchBezeichnung() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "neu"));

		assertThat(dto.Bezeichnung).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | schluessel > 20 Zeichen")
	void patchSchluesselIsTooLong() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("schluessel", RandomStringUtils.insecure().nextAscii(21))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut schluessel: Die Länge des Strings ist auf 20 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | schluessel is null")
	void patchSchluesselIsNull() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.Schluessel = "toBeChanged";
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("schluessel", null);

		this.data.patch(1L, map);

		assertThat(dto.Schluessel).isNull();
	}

	@Test
	@DisplayName("patch | schluessel is blank")
	void patchSchluesselIsBlank() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.Schluessel = "notToBeChanged";
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("schluessel", "   "));

		assertThat(dto.Schluessel).isEqualTo("notToBeChanged");
	}

	@Test
	@DisplayName("patch | schluessel | value has not changes")
	void patchSchluesselHasNotChanged() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.Schluessel = "oldValue";
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("schluessel", "oldValue"));

		assertThat(dto.Schluessel).isEqualTo("oldValue");
	}

	@Test
	@DisplayName("patch | schluessel | coreType does not match")
	void patchSchluesselCoreTypeDoesNotMatch() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("schluessel", "-35")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Zum angegebenen Schlüssel -35 wurde keine passende Einwilligungsart gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | schluessel already used from same personTyp")
	void patchSchluesselAlreadyUsedFromSamePersonTyp() {
		final String schluessel = Einwilligungsschluessel.data().getWerte().getFirst().historie().getFirst().schluessel;
		final int idPersonTyp = PersonTyp.SCHUELER.id;
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.personTyp = PersonTyp.getByID(idPersonTyp);
		dto.Schluessel = schluessel;
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("schluessel", schluessel, "idPersonTyp", idPersonTyp)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Schlüssel %s wird bereits verwendet.".formatted(schluessel))
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | schluessel used from different personTyp")
	void patchSchluesselAlreadyUsedFromDifferentPersonTyp() throws ApiOperationException {
		final String schluessel = Einwilligungsschluessel.data().getWerte().getFirst().historie().getFirst().schluessel;
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.personTyp = PersonTyp.getByID(1);
		dto.Schluessel = null;
		final var dto2 = new DTOKatalogEinwilligungsart(2L, "test", true, 123);
		dto2.personTyp = PersonTyp.getByID(2);
		dto2.Schluessel = schluessel;
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(dto, dto2));
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("schluessel", schluessel, "idPersonTyp", 1));

		assertThat(dto.Schluessel).isEqualTo(schluessel);
	}

	@Test
	@DisplayName("patch | schluessel")
	void patchSchluessel() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.Schluessel = "oldValue";
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final String schluessel = Einwilligungsschluessel.data().getWerte().getFirst().historie().getFirst().schluessel;

		this.data.patch(1L, Map.of("schluessel", schluessel));

		assertThat(dto.Schluessel).isEqualTo(schluessel);
	}

	@Test
	@DisplayName("patch | beschreibung is null ")
	void patchBeschreibungIsNull() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.Beschreibung = "oldValue";
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("beschreibung", null);

		this.data.patch(1L, map);

		assertThat(dto.Beschreibung).isNull();
	}

	@Test
	@DisplayName("patch | beschreibung")
	void patchBeschreibung() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.Beschreibung = "oldValue";
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("beschreibung", "newValue"));

		assertThat(dto.Beschreibung).isEqualTo("newValue");
	}

	@Test
	@DisplayName("patch | personTyp is null")
	void patchPersonTypIsNull() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));
		final var map = new HashMap<String, Object>();
		map.put("idPersonTyp", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idPersonTyp: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | personTyp wrong if")
	void patchPersonTypWrongId() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idPersonTyp", -35)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein PersonTyp zur ID -35 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | personTyp wrong type")
	void patchPersonTypWrongType() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idPersonTyp", 99)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein PersonTyp zur ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | personTyp")
	void patchPersonTyp() throws ApiOperationException {
		final var dto = new DTOKatalogEinwilligungsart(1L, "test", true, 123);
		dto.personTyp = PersonTyp.LEHRER;
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("idPersonTyp", PersonTyp.SCHUELER.id));

		assertThat(dto.personTyp).isEqualTo(PersonTyp.SCHUELER);
	}

	@Test
	@DisplayName("patch | istSichtbar is null")
	void patchIstSichtbarIsNull() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));
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
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | sortierung is null")
	void patchSortierungIsNull() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));
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
		final var dto = new DTOKatalogEinwilligungsart(1L, "bezeichnung", true, 123);
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.Sortierung).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(mock(DTOKatalogEinwilligungsart.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
