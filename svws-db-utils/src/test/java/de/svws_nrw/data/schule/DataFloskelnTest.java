package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Floskel;
import de.svws_nrw.core.data.schule.FloskelJahrgangZuordnung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Diese Klasse testet die Klasse DataFloskeln")
class DataFloskelnTest {

	@Mock
	private DBEntityManager conn;

	@Mock
	private DataFloskelJahrgangZuordnung dataZuordnung;

	@InjectMocks
	private DataFloskeln data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: kuerzel")
	void setAttributesRequiredOnCreationTest() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (kuerzel) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableTest() {
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(mock(DTOFloskeln.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesDelayedOnCreation: idJahrgaenge")
	void setAttributesDelayedOnCreation() throws NoSuchFieldException, IllegalAccessException {
		final var field = this.data.getClass().getSuperclass().getDeclaredField("attributesDelayedOnCreation");
		field.setAccessible(true);
		@SuppressWarnings("unchecked")
		final var attributes = (Set<String>) field.get(this.data);

		assertThat(attributes).contains("idsJahrgaenge");
	}

	@Test
	@DisplayName("initDTO | id is assigned")
	void initDTO_idIsAssigned() {
		final var dto = new DTOFloskeln(-1L, "kuerzel", "text");

		this.data.initDTO(dto, 2L, Collections.emptyMap());

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getLongId")
	void getLongId() throws ApiOperationException {
		final var dto = new DTOFloskeln(2L, "kuerzel", "bez");

		assertThat(this.data.getLongId(dto)).isEqualTo(2);
	}

	@Test
	@DisplayName("getById | Id is null")
	void getByIdTest_idIsNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID der Floskel darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getById | no entry found")
	void getByIdTest_noEntryFound() {
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(1L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde keine Floskel mit der ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getById")
	void getByIdTest() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "kuerzel", "text");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Floskel.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "kuerzel")
				.hasFieldOrPropertyWithValue("text", "text");
	}

	@Test
	@DisplayName("getAll | no entries found")
	void getAllTest_noEntriesFound() {
		when(this.conn.queryAll(DTOFloskeln.class)).thenReturn(Collections.emptyList());

		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("getAll")
	void getAllTest() {
		final var dto = new DTOFloskeln(1L, "k1", "t1");
		final var dto2 = new DTOFloskeln(2L, "k2", "t2");
		when(this.conn.queryAll(DTOFloskeln.class)).thenReturn(List.of(dto, dto2));

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Floskel.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("kuerzel", "k1")
								.hasFieldOrPropertyWithValue("text", "t1"),
						f2 -> assertThat(f2)
								.isInstanceOf(Floskel.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("kuerzel", "k2")
								.hasFieldOrPropertyWithValue("text", "t2")
				);
	}

	@Test
	@DisplayName("map")
	void mapTest() {
		final var zuordnung1 = new FloskelJahrgangZuordnung();
		final var zuordnung2 = new FloskelJahrgangZuordnung();
		final var zuordnung3 = new FloskelJahrgangZuordnung();
		zuordnung1.idJahrgang = 11L;
		zuordnung2.idJahrgang = 22;
		zuordnung3.idJahrgang = 33;
		when(this.dataZuordnung.getListByIdFloskel(1L))
				.thenReturn(List.of(zuordnung1, zuordnung2, zuordnung3));
		final var dto = new DTOFloskeln(1L, "kue", "t");
		dto.Gruppe_ID = 1L;
		dto.Fach_ID = 2L;
		dto.Niveau = 3;
		dto.Sichtbar = true;
		dto.Sortierung = 123;

		final var result = this.data.map(dto);

		assertThat(result)
				.isInstanceOf(Floskel.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "kue")
				.hasFieldOrPropertyWithValue("text", "t")
				.hasFieldOrPropertyWithValue("idFloskelgruppe", 1L)
				.hasFieldOrPropertyWithValue("idFach", 2L)
				.hasFieldOrPropertyWithValue("niveau", 3)
				.hasFieldOrPropertyWithValue("idsJahrgaenge", List.of(11L, 22L, 33L))
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 123);
	}

	@Test
	@DisplayName("map | jahrgaenge empty")
	void mapTest_jahrgaengeEmpty() {
		when(this.dataZuordnung.getListByIdFloskel(1L))
				.thenReturn(Collections.emptyList());

		final var result = this.data.map(new DTOFloskeln(1L, "kue", "t"));

		assertThat(result.idsJahrgaenge)
				.isNotNull()
				.isEmpty();
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapTest_sichtbarNull() {
		final var dto = new DTOFloskeln(1L, "kue", "t");
		dto.Sichtbar = null;

		final var result = this.data.map(dto);

		assertThat(result.istSichtbar).isFalse();
	}

	@Test
	@DisplayName("map | sortierung null")
	void mapTest_sortierungNull() {
		final var dto = new DTOFloskeln(1L, "kue", "t");
		dto.Sortierung = null;

		final var result = this.data.map(dto);

		assertThat(result.sortierung).isEqualTo(32000);
	}

	@Test
	@DisplayName("patch | kuerzel | no changes")
	void patch_kuerzelNoChanges() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("kuerzel", "same"));

		verify(this.conn, never()).queryAll(DTOFloskeln.class);
		assertThat(dto.Kuerzel).isEqualTo("same");
	}

	@Test
	@DisplayName("patch | kuerzel | isBlank")
	void patch_kuerzelIsBlank() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("kuerzel", "  "));

		verify(this.conn, never()).queryAll(DTOFloskeln.class);
		assertThat(dto.Kuerzel).isEqualTo("same");
	}

	@Test
	@DisplayName("patch | kuerzel already Used")
	void patch_kuerzelAlreadyUsed() {
		final var dto = new DTOFloskeln(1L, "first", "bez");
		final var dto2 = new DTOFloskeln(2L, "second", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 2L)).thenReturn(dto2);
		when(this.conn.queryAll(DTOFloskeln.class)).thenReturn(List.of(dto, dto2));

		assertThatException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("kuerzel", "first")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Das Kürzel first wird bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzel already Used | upper case")
	void patch_kuerzelAlreadyUsed_upperCase() {
		final var dto = new DTOFloskeln(1L, "first", "bez");
		final var dto2 = new DTOFloskeln(2L, "second", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 2L)).thenReturn(dto2);
		when(this.conn.queryAll(DTOFloskeln.class)).thenReturn(List.of(dto, dto2));

		assertThatException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("kuerzel", "FIRST")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Das Kürzel FIRST wird bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | kuerzel")
	void patch_kuerzel() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "first", "bez");
		final var dto2 = new DTOFloskeln(2L, "second", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 2L)).thenReturn(dto2);
		when(this.conn.queryAll(DTOFloskeln.class)).thenReturn(List.of(dto, dto2));
		when(this.conn.transactionPersist(dto2)).thenReturn(true);

		this.data.patch(2L, Map.of("kuerzel", "third"));

		assertThat(dto2.Kuerzel).isEqualTo("third");
	}

	@Test
	@DisplayName("patch | text | null")
	void patch_text_null() {
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(mock(DTOFloskeln.class));
		final var map = new HashMap<String, Object>();
		map.put("text", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut text: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | text | empty")
	void patch_text_empty() {
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(mock(DTOFloskeln.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("text", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut text: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | text")
	void patch_text() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "kuerz", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("text", "neuerText"));

		assertThat(dto.Text).isEqualTo("neuerText");
	}

	@Test
	@DisplayName("patch | Floskelgruppe | null")
	void patch_floskelgruppeIsNull() {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		final var map = new HashMap<String, Object>();
		map.put("idFloskelgruppe", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idFloskelgruppe: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Floskelgruppe | no Changes")
	void patch_floskelgruppeNoChanges() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Gruppe_ID = 2L;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("idFloskelgruppe", 2L));

		verify(this.conn, never()).queryAll(DTOFloskeln.class);
		assertThat(dto.Gruppe_ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("patch | Floskelgruppe | no match")
	void patch_floskelgruppeNoMatch() {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOFloskelgruppen.class, 2L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idFloskelgruppe", 2L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Floskelgruppe mit der id 2 vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | Floskelgruppe")
	void patch_floskelgruppe() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOFloskelgruppen.class, 35L)).thenReturn(new DTOFloskelgruppen(35L, "kur", "bez"));
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("idFloskelgruppe", 35L));

		assertThat(dto.Gruppe_ID).isEqualTo(35L);
	}

	@Test
	@DisplayName("patch | fach is null")
	void patch_fachIsNull() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Fach_ID = 35L;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("idFach", null);

		this.data.patch(1L, map);

		assertThat(dto.Fach_ID).isNull();
	}

	@Test
	@DisplayName("patch | fach | no match")
	void patch_fachNoMatch() {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOFach.class, 35L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idFach", 35L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Fach mit der ID 35 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | Fach")
	void patch_fach() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOFach.class, 35L)).thenReturn(new DTOFach(35L, true));
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("idFach", 35L));

		assertThat(dto.Fach_ID).isEqualTo(35L);
	}

	@Test
	@DisplayName("patch | niveau | null")
	void patch_niveauNull() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Niveau = 99;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("niveau", null);

		this.data.patch(1L, map);

		assertThat(dto.Niveau).isNull();
	}

	@Test
	@DisplayName("patch | niveau")
	void patch_niveau() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Niveau = 99;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("niveau", 66));

		assertThat(dto.Niveau).isEqualTo(66);
	}

	@Test
	@DisplayName("patch | jahrgaenge | add")
	void patch_jahrgaenge_add() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);
		final var zuordnung = new FloskelJahrgangZuordnung();
		zuordnung.id = 0L;
		zuordnung.idFloskel = 1L;
		zuordnung.idJahrgang = 1L;
		when(this.dataZuordnung.getListByIdFloskel(1L)).thenReturn(List.of(zuordnung));

		this.data.patch(1L, Map.of("idsJahrgaenge", List.of(1, 2, 3)));

		verify(this.dataZuordnung, times(1)).add(Map.of("idFloskel", 1L, "idJahrgang", 2L));
		verify(this.dataZuordnung, times(1)).add(Map.of("idFloskel", 1L, "idJahrgang", 3L));
		verify(this.dataZuordnung, never()).delete(any());
	}

	@Test
	@DisplayName("patch | jahrgaenge | delete")
	void patch_jahrgaenge_delete() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);
		final var zuordnung1 = new FloskelJahrgangZuordnung();
		zuordnung1.id = 0L;
		zuordnung1.idFloskel = 1L;
		zuordnung1.idJahrgang = 1L;
		final var zuordnung2 = new FloskelJahrgangZuordnung();
		zuordnung2.id = 1L;
		zuordnung2.idFloskel = 1L;
		zuordnung2.idJahrgang = 2L;
		when(this.dataZuordnung.getListByIdFloskel(1L)).thenReturn(List.of(zuordnung1, zuordnung2));

		this.data.patch(1L, Map.of("idsJahrgaenge", List.of(1)));

		verify(this.dataZuordnung, times(1)).delete(1L);
		verify(this.dataZuordnung, never()).add(any());
	}

	@Test
	@DisplayName("patch | jahrgaenge | duplikate")
	void patch_jahrgaenge_duplikates() {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idsJahrgaenge", List.of(1, 1))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Liste der neuen JahrgangIDs darf keine Duplikate enthalten.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		verify(this.conn, never()).queryList(any(), any(), any());
	}

	@Test
	@DisplayName("patch | jahrgaenge | delete and add")
	void patch_jahrgaenge_deleteAndAdd() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);
		final var zuordnung1 = new FloskelJahrgangZuordnung();
		zuordnung1.id = 0L;
		zuordnung1.idFloskel = 1L;
		zuordnung1.idJahrgang = 1L;
		final var zuordnung2 = new FloskelJahrgangZuordnung();
		zuordnung2.id = 1L;
		zuordnung2.idFloskel = 1L;
		zuordnung2.idJahrgang = 2L;
		when(this.dataZuordnung.getListByIdFloskel(1L))
				.thenReturn(List.of(zuordnung1, zuordnung2));

		this.data.patch(1L, Map.of("idsJahrgaenge", List.of(1, 3)));

		verify(this.dataZuordnung, times(1)).delete(1L);
		verify(this.dataZuordnung, times(1)).add(Map.of("idFloskel", 1L, "idJahrgang", 3L));
	}

	@Test
	@DisplayName("patch | jahrgaenge | noChanges")
	void patch_jahrgaenge_noChanges() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);
		final var zuordnung1 = new FloskelJahrgangZuordnung();
		final var zuordnung2 = new FloskelJahrgangZuordnung();
		zuordnung1.idJahrgang = 1L;
		zuordnung2.idJahrgang = 2L;
		when(this.dataZuordnung.getListByIdFloskel(1L))
				.thenReturn(List.of(zuordnung1, zuordnung2));

		this.data.patch(1L, Map.of("idsJahrgaenge", List.of(1, 2)));

		verify(this.dataZuordnung, never()).delete(any());
		verify(this.dataZuordnung, never()).add(any());
	}

	@Test
	@DisplayName("patch | sortierung | null")
	void patch_sortierungNull() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Sortierung = 99;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("sortierung", null);

		this.data.patch(1L, map);

		assertThat(dto.Sortierung).isNull();
	}

	@Test
	@DisplayName("patch | sortierung | below min value")
	void patch_sortierungBelowMin() {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("sortierung", -1)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut sortierung: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | sortierung at max")
	void patch_sortierungAtMax() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Sortierung = 99;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 32000));

		assertThat(dto.Sortierung).isEqualTo(32000);
	}

	@Test
	@DisplayName("patch | sortierung")
	void patch_sortierung() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Sortierung = 99;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 66));

		assertThat(dto.Sortierung).isEqualTo(66);
	}

	@Test
	@DisplayName("patch | sichtbar | null")
	void patch_sichtbarNull() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Sichtbar = true;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("istSichtbar", null);

		this.data.patch(1L, map);

		assertThat(dto.Sichtbar).isNull();
	}

	@Test
	@DisplayName("patch | sichtbar")
	void patch_sichtbar() throws ApiOperationException {
		final var dto = new DTOFloskeln(1L, "same", "bez");
		dto.Sichtbar = null;
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(dto)).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patch_unknownArgument() {
		when(this.conn.queryByKey(DTOFloskeln.class, 1L)).thenReturn(mock(DTOFloskeln.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", -1)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
