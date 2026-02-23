package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataAnkreuzkompetenzJahrgangszuordnungen}
 */
@DisplayName("Diese Klasse testet die Klasse DataAnkreuzkompetenzJahrgangszuordnungen")
@ExtendWith(MockitoExtension.class)
class DataAnkreuzkompetenzJahrgangszuordnungenTest {


	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataAnkreuzkompetenzJahrgangszuordnungen data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idAnkreuzkompetenz")
	void setAttributesRequiredOnCreationIdAnkreuzkompetenz() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("idJahrgang", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (idAnkreuzkompetenz) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idJahrgang")
	void setAttributesRequiredOnCreationIdJahrgang() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("idAnkreuzkompetenz", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (idJahrgang) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(mock(DTOAnkreuzkompetenzJahrgang.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOAnkreuzkompetenzJahrgang(1L, 1L, 1L);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto).hasFieldOrPropertyWithValue("id", 2L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOAnkreuzkompetenzJahrgang(1L, 1L, 1L);

		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(AnkreuzkompetenzJahrgangszuordnung.class)
				.hasFieldOrPropertyWithValue("id", dto.id);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID der Zuordnung darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Zuordnung mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOAnkreuzkompetenzJahrgang(1L, 1L, 1L);

		assertThat(this.data.map(dto))
				.isInstanceOf(AnkreuzkompetenzJahrgangszuordnung.class)
				.hasFieldOrPropertyWithValue("id", dto.id)
				.hasFieldOrPropertyWithValue("idAnkreuzkompetenz", dto.idAnkreuzkompetenz)
				.hasFieldOrPropertyWithValue("idJahrgang", dto.idJahrgang);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() throws ApiOperationException {
		final var dto1 = new DTOAnkreuzkompetenzJahrgang(1L, 1L, 1L);
		final var dto2 = new DTOAnkreuzkompetenzJahrgang(2L, 2L, 2L);
		when(this.conn.queryAll(DTOAnkreuzkompetenzJahrgang.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(AnkreuzkompetenzJahrgangszuordnung.class)
								.hasFieldOrPropertyWithValue("id", dto1.id)
								.hasFieldOrPropertyWithValue("idAnkreuzkompetenz", dto1.idAnkreuzkompetenz)
								.hasFieldOrPropertyWithValue("idJahrgang", dto1.idJahrgang),
						f2 -> assertThat(f2)
								.isInstanceOf(AnkreuzkompetenzJahrgangszuordnung.class)
								.hasFieldOrPropertyWithValue("id", dto2.id)
								.hasFieldOrPropertyWithValue("idAnkreuzkompetenz", dto2.idAnkreuzkompetenz)
								.hasFieldOrPropertyWithValue("idJahrgang", dto2.idJahrgang)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOAnkreuzkompetenzJahrgang.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOAnkreuzkompetenzJahrgang.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOAnkreuzkompetenzJahrgang(1L, 1L, 1L);

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | idAnkreuzkompetenz | null")
	void patchIdAnkreuzkompetenzIsNull() {
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(mock(DTOAnkreuzkompetenzJahrgang.class));
		final var map = new HashMap<String, Object>();
		map.put("idAnkreuzkompetenz", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idAnkreuzkompetenz: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idAnkreuzkompetenz | wrong Id")
	void patchIdAnkreuzkompetenzWrongId() {
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(mock(DTOAnkreuzkompetenzJahrgang.class));
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 42L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idAnkreuzkompetenz", 42L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Für die ID 42 wurde keine Ankreuzkompetenz gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idAnkreuzkompetenz")
	void patchIdAnkreuzkompetenz() throws ApiOperationException {
		final var dto = new DTOAnkreuzkompetenzJahrgang(1L, 1L, 1L);
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 42L)).thenReturn(mock(DTOAnkreuzfloskeln.class));
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("idAnkreuzkompetenz", 42L));

		assertThat(dto.idAnkreuzkompetenz).isEqualTo(42L);
	}

	@Test
	@DisplayName("patch | idJahrgang | null")
	void patchIdJahrgangIsNull() {
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(mock(DTOAnkreuzkompetenzJahrgang.class));
		final var map = new HashMap<String, Object>();
		map.put("idJahrgang", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idJahrgang: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idJahrgang | wrong Id")
	void patchIdJahrgangWrongId() {
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(mock(DTOAnkreuzkompetenzJahrgang.class));
		when(this.conn.queryByKey(DTOJahrgang.class, 42L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idJahrgang", 42L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Für die ID 42 wurde kein Jahrgang gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idJahrgang")
	void patchIdJahrgang() throws ApiOperationException {
		final var dto = new DTOAnkreuzkompetenzJahrgang(1L, 1L, 1L);
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOJahrgang.class, 42L)).thenReturn(mock(DTOJahrgang.class));
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("idJahrgang", 42L));

		assertThat(dto.idJahrgang).isEqualTo(42L);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOAnkreuzkompetenzJahrgang.class, 1L)).thenReturn(mock(DTOAnkreuzkompetenzJahrgang.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
