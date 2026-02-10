package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
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
 * Diese Klasse testet die Klasse {@link DataAbteilungenKlassenzuordnungen}
 */
@DisplayName("Diese Klasse testet die Klasse DataAbteilungenKlassenzuordnungen")
@ExtendWith(MockitoExtension.class)
class DataAbteilungenKlassenzuordnungenTest {


	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataAbteilungenKlassenzuordnungen data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idAbteilung")
	void setAttributesRequiredOnCreationIdAbteilung() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("idKlasse", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (idAbteilung) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idKlasse")
	void setAttributesRequiredOnCreationIdKlasse() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("idAbteilung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (idKlasse) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(mock(DTOAbteilungsKlassen.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOAbteilungsKlassen(1L, 1L, 1L);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto).hasFieldOrPropertyWithValue("ID", 2L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOAbteilungsKlassen(1L, 1L, 1L);

		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(AbteilungKlassenzuordnung.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
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
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Zuordnung mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOAbteilungsKlassen(1L, 1L, 1L);

		assertThat(this.data.map(dto))
				.isInstanceOf(AbteilungKlassenzuordnung.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("idKlasse", dto.Klassen_ID)
				.hasFieldOrPropertyWithValue("idAbteilung", dto.Abteilung_ID);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() throws ApiOperationException {
		final var dto1 = new DTOAbteilungsKlassen(1L, 1L, 1L);
		final var dto2 = new DTOAbteilungsKlassen(2L, 2L, 2L);
		when(this.conn.queryAll(DTOAbteilungsKlassen.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(AbteilungKlassenzuordnung.class)
								.hasFieldOrPropertyWithValue("id", dto1.ID)
								.hasFieldOrPropertyWithValue("idKlasse", dto1.Klassen_ID)
								.hasFieldOrPropertyWithValue("idAbteilung", dto1.Abteilung_ID),
						f2 -> assertThat(f2)
								.isInstanceOf(AbteilungKlassenzuordnung.class)
								.hasFieldOrPropertyWithValue("id", dto2.ID)
								.hasFieldOrPropertyWithValue("idKlasse", dto2.Klassen_ID)
								.hasFieldOrPropertyWithValue("idAbteilung", dto2.Abteilung_ID)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOAbteilungsKlassen.class)).thenReturn(Collections.emptyList());

		verify(this.conn, never()).query(anyString(), eq(Long.class));
		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOAbteilungsKlassen.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOAbteilungsKlassen(1L, 1L, 1L);

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | idAbteilung | null")
	void patchIdAbteilungIsNull() {
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(mock(DTOAbteilungsKlassen.class));
		final var map = new HashMap<String, Object>();
		map.put("idAbteilung", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idAbteilung: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idAbteilung | wrong Id")
	void patchIdAbteilungWrongId() {
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(mock(DTOAbteilungsKlassen.class));
		when(this.conn.queryByKey(DTOAbteilungen.class, 42L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idAbteilung", 42L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Für die ID 42 wurde keine Abteilung gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idAbteilung")
	void patchIdAbteilung() throws ApiOperationException {
		final var dto = new DTOAbteilungsKlassen(1L, 1L, 1L);
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOAbteilungen.class, 42L)).thenReturn(mock(DTOAbteilungen.class));
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("idAbteilung", 42L));

		assertThat(dto.Abteilung_ID).isEqualTo(42L);
	}

	@Test
	@DisplayName("patch | idKlasse | null")
	void patchIdKlasseIsNull() {
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(mock(DTOAbteilungsKlassen.class));
		final var map = new HashMap<String, Object>();
		map.put("idKlasse", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idKlasse: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idKlasse | wrong Id")
	void patchIdKlasseWrongId() {
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(mock(DTOAbteilungsKlassen.class));
		when(this.conn.queryByKey(DTOKlassen.class, 42L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idKlasse", 42L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Für die ID 42 wurde keine Klasse gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idKlasse")
	void patchIdKlasse() throws ApiOperationException {
		final var dto = new DTOAbteilungsKlassen(1L, 1L, 1L);
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOKlassen.class, 42L)).thenReturn(mock(DTOKlassen.class));
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("idKlasse", 42L));

		assertThat(dto.Klassen_ID).isEqualTo(42L);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOAbteilungsKlassen.class, 1L)).thenReturn(mock(DTOAbteilungsKlassen.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
