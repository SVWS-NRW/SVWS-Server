package de.svws_nrw.data.schule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Abteilung;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataAbteilungen}
 */
@DisplayName("Diese Klasse testet die Klasse DataAbteilungen")
@ExtendWith(MockitoExtension.class)
class DataAbteilungenTest {

	@Mock
	private DBEntityManager conn;

	@Mock
	private DataAbteilungenKlassenzuordnungen dataAbteilungenKlassenzuordnungen;

	@InjectMocks
	private DataAbteilungen data;

	@BeforeAll
	static void setup() {
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
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var data = new DataAbteilungen(this.conn, 22L, this.dataAbteilungenKlassenzuordnungen);
		final var dto = new DTOAbteilungen(1L, "", 1L);

		data.initDTO(dto, 2L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 2L)
				.hasFieldOrPropertyWithValue("Schuljahresabschnitts_ID", 22L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOAbteilungen(1L, "", 1L);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);

		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Abteilung.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID der Abteilung darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOAbteilungen.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Abteilung mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map")
	void map() {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Raum = "raum";
		dto.Email = "email";
		dto.Durchwahl = "durchwahl";
		dto.Sortierung = 42;
		dto.Sichtbar = true;

		assertThat(this.data.map(dto))
				.isInstanceOf(Abteilung.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("raum", dto.Raum)
				.hasFieldOrPropertyWithValue("email", dto.Email)
				.hasFieldOrPropertyWithValue("durchwahl", dto.Durchwahl)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar);
	}

	@Test
	@DisplayName("map | sortierung null")
	void mapSortierungNull() {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Sortierung = null;

		assertThat(this.data.map(dto)).hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarNull() {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Sichtbar = null;

		assertThat(this.data.map(dto)).hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("getList | Erfolg")
	void getList() {
		final var data = new DataAbteilungen(this.conn, 22L, this.dataAbteilungenKlassenzuordnungen);
		final var dto1 = new DTOAbteilungen(1L, "1", 22L);
		final var dto2 = new DTOAbteilungen(2L, "2", 22L);

		when(this.conn.queryList(DTOAbteilungen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOAbteilungen.class, 22L)).thenReturn(List.of(dto1, dto2));
		final var zuordnung = new AbteilungKlassenzuordnung();
		zuordnung.id = 42L;
		zuordnung.idKlasse = 99L;
		zuordnung.idAbteilung = 1L;
		when(this.dataAbteilungenKlassenzuordnungen.getAll()).thenReturn(List.of(zuordnung));

		assertThat(data.getList())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Abteilung.class)
								.hasFieldOrPropertyWithValue("bezeichnung", dto1.Bezeichnung)
								.extracting(e -> e.klassenzuordnungen)
								.asInstanceOf(InstanceOfAssertFactories.LIST)
								.hasSize(1)
								.satisfiesExactly(
										a -> assertThat(a)
												.isInstanceOf(AbteilungKlassenzuordnung.class)
												.hasFieldOrPropertyWithValue("id", zuordnung.id)
												.hasFieldOrPropertyWithValue("idKlasse", zuordnung.idKlasse)
												.hasFieldOrPropertyWithValue("idAbteilung", zuordnung.idAbteilung)
								),
						f2 -> assertThat(f2)
								.isInstanceOf(Abteilung.class)
								.hasFieldOrPropertyWithValue("bezeichnung", dto2.Bezeichnung)
				);
	}

	@Test
	@DisplayName("getList | idSchuljahresabschnitt null")
	void getListIdSchuljahresabschnittNull() {
		assertThat(this.data.getList()).isEmpty();
		verify(this.conn, never()).queryList(any(), any(), any());
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOAbteilungen.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOAbteilungen(1L, "1", 1L);

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | bezeichnung > 50 Zeichen")
	void patchBezeichnungIsTooLong() {
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", RandomStringUtils.insecure().nextAscii(51))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 50 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung Null")
	void patchBezeichnungIsNull() {
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(mock(DTOAbteilungen.class));
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
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(mock(DTOAbteilungen.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung is blank")
	void patchBezeichnungIsBlank() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);

		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "    "));

		verify(this.conn, never()).queryAll(DTOAbteilungen.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung doesn't change")
	void patchBezeichnungDoesNotChange() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOAbteilungen.class);
		assertThat(dto.Bezeichnung).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | bezeichnung already used")
	void patchBezeichnungAlreadyUsed() {
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(mock(DTOAbteilungen.class));
		when(this.conn.queryAll(DTOAbteilungen.class)).thenReturn(List.of(new DTOAbteilungen(1L, "test", 1L)));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung already used different case")
	void patchBezeichnungAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(mock(DTOAbteilungen.class));
		when(this.conn.queryAll(DTOAbteilungen.class)).thenReturn(List.of(new DTOAbteilungen(2L, "TEST", 1L)));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("bezeichnung", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Bezeichnung test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | bezeichnung change case in same object")
	void patchBezeichnungChangeCase() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		when(conn.queryAll(DTOAbteilungen.class)).thenReturn(List.of(dto));
		final var newDto = new DTOAbteilungen(2L, "abc", 1L);
		when(this.conn.queryByKey(DTOAbteilungen.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("bezeichnung", "ABC"));

		assertThat(newDto.Bezeichnung).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | bezeichnung dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchBezeichnungInDtoISNull() {
		final var dto = new DTOAbteilungen(1L, "123", 1L);
		dto.Bezeichnung = null;
		when(conn.queryAll(DTOAbteilungen.class)).thenReturn(List.of(dto));
		final var newDto = new DTOAbteilungen(2L, "abc", 1L);
		when(this.conn.queryByKey(DTOAbteilungen.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("bezeichnung", "test")));
	}

	@Test
	@DisplayName("patch | bezeichnung")
	void patchBezeichnung() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("bezeichnung", "neu"));

		assertThat(dto.Bezeichnung).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | idAbteilungsleiter | null")
	void patchIdAbteilungsleiterIsNull() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.AbteilungsLeiter_ID = 42L;
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("idAbteilungsleiter", null);

		this.data.patch(1L, map);

		assertThat(dto.AbteilungsLeiter_ID).isNull();
	}

	@Test
	@DisplayName("patch | idAbteilungsleiter")
	void patchIdAbteilungsleiter() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.AbteilungsLeiter_ID = 42L;
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("idAbteilungsleiter", 99L));

		assertThat(dto.AbteilungsLeiter_ID).isEqualTo(99L);
	}

	@Test
	@DisplayName("patch | Raum | null")
	void patchRaumIsNull() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Raum = "raum";
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("raum", null);

		this.data.patch(1L, map);

		assertThat(dto.Raum).isNull();
	}

	@Test
	@DisplayName("patch | Raum")
	void patchRaum() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Raum = "raum";
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("raum", "neuerRaum"));

		assertThat(dto.Raum).isEqualTo("neuerRaum");
	}

	@Test
	@DisplayName("patch | Raum > 20 Zeichen")
	void patchRaumIsTooLong() {
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("raum", RandomStringUtils.insecure().nextAscii(21))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut raum: Die Länge des Strings ist auf 20 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Email | null")
	void patchEmailIsNull() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Email = "email";
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("email", null);

		this.data.patch(1L, map);

		assertThat(dto.Email).isNull();
	}

	@Test
	@DisplayName("patch | Email")
	void patchEmail() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Email = "email";
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("email", "neueEmail"));

		assertThat(dto.Email).isEqualTo("neueEmail");
	}

	@Test
	@DisplayName("patch | Email > 100 Zeichen")
	void patchEmailIsTooLong() {
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("email", RandomStringUtils.insecure().nextAscii(101))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut email: Die Länge des Strings ist auf 100 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Durchwahl | null")
	void patchDurchwahlIsNull() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Durchwahl = "durchwahl";
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("durchwahl", null);

		this.data.patch(1L, map);

		assertThat(dto.Durchwahl).isNull();
	}

	@Test
	@DisplayName("patch | Durchwahl")
	void patchDurchwahl() throws ApiOperationException {
		final var dto = new DTOAbteilungen(1L, "", 1L);
		dto.Durchwahl = "durchwahl";
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("durchwahl", "neueDurchwahl"));

		assertThat(dto.Durchwahl).isEqualTo("neueDurchwahl");
	}

	@Test
	@DisplayName("patch | Durchwahl > 20 Zeichen")
	void patchDurchwahlIsTooLong() {
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("durchwahl", RandomStringUtils.insecure().nextAscii(21))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut durchwahl: Die Länge des Strings ist auf 20 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | istSichtbar is null")
	void patchIstSichtbarIsNull() {
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(mock(DTOAbteilungen.class));
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
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | sortierung is null")
	void patchSortierungIsNull() {
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(mock(DTOAbteilungen.class));
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
		final var dto = new DTOAbteilungen(1L, "bezeichnung", 1L);
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.Sortierung).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOAbteilungen.class, 1L)).thenReturn(mock(DTOAbteilungen.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
