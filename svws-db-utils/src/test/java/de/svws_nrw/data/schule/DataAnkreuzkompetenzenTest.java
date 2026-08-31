package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangService;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
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

/**
 * Diese Klasse testet die Klasse {@link DataAnkreuzkompetenzen}
 */
@DisplayName("Diese Klasse testet die Klasse DataAnkreuzkompetenzen")
@ExtendWith(MockitoExtension.class)
class DataAnkreuzkompetenzenTest {

	@Mock
	private DBEntityManager conn;

	@Mock
	private AnkreuzkompetenzJahrgangService ankreuzkompetenzJahrgangService;

	@InjectMocks
	private DataAnkreuzkompetenzen data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableIdTest() {
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(mock(DTOAnkreuzfloskeln.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", 1L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: referenziertInAnderenTabellen")
	void setAttributesNotPatchableReferenziertInAnderenTabellen() {
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(mock(DTOAnkreuzfloskeln.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("referenziertInAnderenTabellen", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: referenziertInAnderenTabellen.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: istASV")
	void setAttributesRequiredOnCreationASV() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("floskelText", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (istASV) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: floskelText")
	void setAttributesRequiredOnCreationFloskelText() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("istASV", false)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (floskelText) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dataAnkreuzkompetenzen = new DataAnkreuzkompetenzen(this.conn, this.ankreuzkompetenzJahrgangService);
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "");

		dataAnkreuzkompetenzen.initDTO(dto, 2L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "");

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "");

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Ankreuzkompetenz.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID der Ankreuzkompetenz darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Ankreuzkompetenz mit der ID 99 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map")
	void map() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "");
		dto.Fach_ID = 1L;
		dto.Gliederung = "A02";
		dto.Abschnitt = 1;
		dto.Aktiv = true;
		dto.Sortierung = 42;
		dto.FachSortierung = 42;
		dto.Sichtbar = true;

		assertThat(this.data.map(dto))
				.isInstanceOf(Ankreuzkompetenz.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("idFach", dto.Fach_ID)
				.hasFieldOrPropertyWithValue("istASV", dto.IstASV == 1)
				.hasFieldOrPropertyWithValue("schulgliederung", dto.Gliederung)
				.hasFieldOrPropertyWithValue("floskelText", dto.FloskelText)
				.hasFieldOrPropertyWithValue("abschnitt", dto.Abschnitt)
				.hasFieldOrPropertyWithValue("istAktiv", dto.Aktiv)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung)
				.hasFieldOrPropertyWithValue("fachSortierung", dto.FachSortierung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar);
	}

	@Test
	@DisplayName("getList | Erfolg")
	void getList() {
		final var dataAnkreuzkompetenzen = new DataAnkreuzkompetenzen(this.conn, this.ankreuzkompetenzJahrgangService);
		final var dto1 = new DTOAnkreuzfloskeln(1L, 0, "Test 1");
		final var dto2 = new DTOAnkreuzfloskeln(2L, 0, "Test 2");

		when(this.conn.queryAll(DTOAnkreuzfloskeln.class)).thenReturn(List.of(dto1, dto2));

		final var zuordnung = new AnkreuzkompetenzJahrgangszuordnung();
		zuordnung.id = 42L;
		zuordnung.idAnkreuzkompetenz = 1L;
		zuordnung.idJahrgang = 1L;
		when(this.ankreuzkompetenzJahrgangService.getAllByIdAnkreuzkompetenz())
				.thenReturn(Map.of(1L, List.of(zuordnung)));

		@SuppressWarnings("unchecked") final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(dataAnkreuzkompetenzen.getList())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Ankreuzkompetenz.class)
								.hasFieldOrPropertyWithValue("floskelText", dto1.FloskelText)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true)
								.extracting(e -> e.jahrgaengezuordnung)
								.asInstanceOf(InstanceOfAssertFactories.LIST)
								.hasSize(1)
								.satisfiesExactly(
										a -> assertThat(a)
												.isInstanceOf(AnkreuzkompetenzJahrgangszuordnung.class)
												.hasFieldOrPropertyWithValue("id", zuordnung.id)
												.hasFieldOrPropertyWithValue("idAnkreuzkompetenz", zuordnung.idAnkreuzkompetenz)
												.hasFieldOrPropertyWithValue("idJahrgang", zuordnung.idJahrgang)
								),
						f2 -> assertThat(f2)
								.isInstanceOf(Ankreuzkompetenz.class)
								.hasFieldOrPropertyWithValue("floskelText", dto2.FloskelText)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOAnkreuzfloskeln.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto = new DTOAnkreuzfloskeln(1L, 1, "");

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | istASV")
	void patchIstAsvSetsFachNull() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		dto.Fach_ID = 10L;
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		final Map<String, Object> patchData = new HashMap<>();
		patchData.put("istASV", true);
		patchData.put("idFach", null);

		this.data.patch(1L, patchData);

		assertThat(dto.IstASV).isEqualTo(1);
		assertThat(dto.Fach_ID).isNull();
	}

	@Test
	@DisplayName("patch | istASV | Aktivierung schlägt fehl, wenn noch ein Fach zugeordnet ist")
	void patchIstASVActivationWithFach() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		dto.Fach_ID = 10L;
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("istASV", true)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Umstellung auf ASV nicht möglich: Es ist noch ein Fach zugeordnet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | istASV | Deaktivierung schlägt fehl, wenn idFach im Patch fehlt")
	void patchIstASVDeactivationWithoutFach() {
		final var dto = new DTOAnkreuzfloskeln(1L, 1, "test");
		dto.Fach_ID = null;
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("istASV", false)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Bei Deaktivierung von ASV muss gleichzeitig ein Fach im Patch übergeben werden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | istASV | Deaktivierung schlägt fehl, wenn idFach im Patch null ist")
	void patchIstASVDeactivationWithFachNull() {
		final var dto = new DTOAnkreuzfloskeln(1L, 1, "test");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		final Map<String, Object> patchData = new HashMap<>();
		patchData.put("istASV", false);
		patchData.put("idFach", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, patchData))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Wenn ASV deaktiviert wird, muss ein gültiges Fach ausgewählt werden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Erfolg | ASV deaktivieren und Fach gleichzeitig setzen")
	void patchIstASVDeactivationAndSetFach() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 1, "test");
		final var fachDto = new DTOFach(20L, false);

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOFach.class, 20L)).thenReturn(fachDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		final Map<String, Object> patchData = new HashMap<>();
		patchData.put("istASV", false);
		patchData.put("idFach", 20L);

		assertDoesNotThrow(() -> this.data.patch(1L, patchData));

		assertThat(dto.IstASV).isZero();
		assertThat(dto.Fach_ID).isEqualTo(20L);
	}

	@Test
	@DisplayName("patch | idFach | Erfolg")
	void patchIdFach() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		final var fachDto = new DTOFach(1L, false);

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(de.svws_nrw.db.dto.current.schild.faecher.DTOFach.class, 10L)).thenReturn(fachDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("idFach", 10L));

		assertThat(dto.Fach_ID).isEqualTo(10L);
	}

	@Test
	@DisplayName("patch | idFach | ASV ist aktiv")
	void patchIdFachFehlerASVAktiv() {
		final var dto = new DTOAnkreuzfloskeln(1L, 1, "test");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idFach", 10L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Einer ASV-Ankreuzkompetenz kann kein Fach zugeordnet werden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idFach | ASV ist aktiv und Fach_ID wird auf null gesetzt")
	void patchIdFachSetFachIdToNull() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 1, "test");
		dto.Fach_ID = 123L;

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		final Map<String, Object> patchData = new HashMap<>();
		patchData.put("idFach", null);

		assertDoesNotThrow(() -> this.data.patch(1L, patchData));

		assertThat(dto.IstASV).isEqualTo(1);
		assertThat(dto.Fach_ID).isNull();
	}

	@Test
	@DisplayName("patch | idFach | Fach ID ist null bei Nicht-ASV")
	void patchIdFachIsNull() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		final Map<String, Object> patchData = new HashMap<>();
		patchData.put("idFach", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, patchData))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Für diese Ankreuzkompetenz muss ein Fach ausgewählt werden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idFach | Fach nicht gefunden")
	void patchIdFachNotFound() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(de.svws_nrw.db.dto.current.schild.faecher.DTOFach.class, 999L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idFach", 999L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Fach mit der ID 999 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | schulgliederung | Erfolg")
	void patchSchulgliederung() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");

		when(conn.getUser()).thenReturn(mock(Benutzer.class));
		when(conn.getUser().schuleGetSchuljahr()).thenReturn(2015);
		when(conn.getUser().schuleGetSchulform()).thenReturn(Schulform.BK);
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("schulgliederung", "A06"));

		assertThat(dto.Gliederung).isEqualTo("A06");
	}

	@Test
	@DisplayName("patch | schulgliederung | null setzen")
	void patchSchulgliederungNull() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		dto.Gliederung = "A01";

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		final Map<String, Object> patchData = new HashMap<>();
		patchData.put("schulgliederung", null);

		this.data.patch(1L, patchData);

		assertThat(dto.Gliederung).isNull();
	}

	@Test
	@DisplayName("patch | schulgliederung | Kürzel nicht gefunden")
	void patchSchulgliederungNotFound() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("schulgliederung", "A50")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Schulgliederung mit dem Schlüssel A50 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | schulgliederung ungültig für Schulform")
	void patchSchulgliederungConflict() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");

		when(conn.getUser()).thenReturn(mock(Benutzer.class));
		when(conn.getUser().schuleGetSchuljahr()).thenReturn(2015);
		when(conn.getUser().schuleGetSchulform()).thenReturn(Schulform.BK);
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("schulgliederung", "A13")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Schulgliederung ist für diese Schulform nicht gültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("patch | floskelText > 255 Zeichen")
	void patchFloskeltextIsTooLong() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("floskelText", RandomStringUtils.insecure().nextAscii(256))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut floskelText: Die Länge des Strings ist auf 255 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | floskelText empty")
	void patchFloskelTextIsEmpty() {
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(mock(DTOAnkreuzfloskeln.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("floskelText", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut floskelText: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | floskelText is blank")
	void patchFloskelTextIsBlank() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "floskelText");

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("floskelText", "    "));

		verify(this.conn, never()).queryAll(DTOAnkreuzfloskeln.class);
		assertThat(dto.FloskelText).isEqualTo("floskelText");
	}

	@Test
	@DisplayName("patch | floskelText doesn't change")
	void patchFloskelTextDoesNotChange() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "floskelText");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("floskelText", "floskelText"));

		verify(this.conn, never()).queryAll(DTOAnkreuzfloskeln.class);
		assertThat(dto.FloskelText).isEqualTo("floskelText");
	}

	@Test
	@DisplayName("patch | floskelText already used")
	void patchFloskelTextAlreadyUsed() {
		final DTOAnkreuzfloskeln patchedAnkreuzkompetenz = new DTOAnkreuzfloskeln(1L, 0, "TEST");
		patchedAnkreuzkompetenz.Fach_ID = 10L;
		final DTOAnkreuzfloskeln existingAnkreuzkompetenz = new DTOAnkreuzfloskeln(2L, 0, "test");
		existingAnkreuzkompetenz.Fach_ID = 10L;

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(patchedAnkreuzkompetenz);
		when(this.conn.queryAll(DTOAnkreuzfloskeln.class)).thenReturn(List.of(existingAnkreuzkompetenz));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("floskelText", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Floskeltext test wird für die FachID 10 bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | floskelText already used different case")
	void patchFloskelTextAlreadyUsedWithDifferentCase() {
		final DTOAnkreuzfloskeln patchedAnkreuzkompetenz = new DTOAnkreuzfloskeln(1L, 0, "TEST");
		patchedAnkreuzkompetenz.Fach_ID = 10L;
		final DTOAnkreuzfloskeln existingAnkreuzkompetenz = new DTOAnkreuzfloskeln(2L, 0, "TEST");
		existingAnkreuzkompetenz.Fach_ID = 10L;

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(patchedAnkreuzkompetenz);
		when(this.conn.queryAll(DTOAnkreuzfloskeln.class)).thenReturn(List.of(existingAnkreuzkompetenz));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("floskelText", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Floskeltext test wird für die FachID 10 bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | floskelText change case in same object")
	void patchFloskelTextChangeCase() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		when(conn.queryAll(DTOAnkreuzfloskeln.class)).thenReturn(List.of(dto));
		final var newDto = new DTOAnkreuzfloskeln(2L, 0, "abc");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("floskelText", "ABC"));

		assertThat(newDto.FloskelText).isEqualTo("ABC");
	}

	@Test
	@DisplayName("patch | floskelText dto is null | make sure no Nullpointer is thrown in equalsIgnoreCase check")
	void patchFloskelTextInDtoISNull() {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "123");
		dto.FloskelText = null;
		when(conn.queryAll(DTOAnkreuzfloskeln.class)).thenReturn(List.of(dto));
		final var newDto = new DTOAnkreuzfloskeln(2L, 0, "abc");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		assertThatNoException()
				.isThrownBy(() -> this.data.patch(2L, Map.of("floskelText", "test")));
	}

	@Test
	@DisplayName("patch | floskelText")
	void patchFloskelText() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("floskelText", "neu"));

		assertThat(dto.FloskelText).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | same floskelText with different Fach_ID")
	void patchFloskelTextWithDifferentFachID() throws ApiOperationException {
		final DTOAnkreuzfloskeln patchedAnkreuzkompetenz = new DTOAnkreuzfloskeln(1L, 0, "abc");
		patchedAnkreuzkompetenz.Fach_ID = 10L;
		final DTOAnkreuzfloskeln existingAnkreuzkompetenz = new DTOAnkreuzfloskeln(2L, 0, "test");
		existingAnkreuzkompetenz.Fach_ID = 20L;

		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(patchedAnkreuzkompetenz);
		when(this.conn.queryAll(DTOAnkreuzfloskeln.class)).thenReturn(List.of(existingAnkreuzkompetenz));
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("floskelText", "test"));

		assertThat(patchedAnkreuzkompetenz.FloskelText).isEqualTo("test");
	}

	@Test
	@DisplayName("patch | abschnitt | Erfolg")
	void patchAbschnitt() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("abschnitt", 2));

		assertThat(dto.Abschnitt).isEqualTo(2);
	}

	@Test
	@DisplayName("patch | istAktiv")
	void patchIstAktiv() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istAktiv", true));

		assertThat(dto.Aktiv).isTrue();
	}

	@Test
	@DisplayName("patch | istSichtbar")
	void patchIstSichtbar() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | fachSortierung")
	void patchFachSortierung() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("fachSortierung", 345));

		assertThat(dto.FachSortierung).isEqualTo(345);
	}

	@Test
	@DisplayName("patch | Sortierung")
	void patchSortierung() throws ApiOperationException {
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");
		dto.Sortierung = 123;
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.Sortierung).isEqualTo(345);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("floskelText", "Attribut floskelText: Der Wert null ist nicht erlaubt."),
				arguments("istSichtbar", "Attribut istSichtbar: Der Wert null ist nicht erlaubt"),
				arguments("sortierung", "Attribut sortierung: Der Wert null ist nicht erlaubt")
		);
	}

	@ParameterizedTest
	@DisplayName("patch | null attributes")
	@MethodSource("provideMappingAttributes")
	void patchAttributeIsNull(final String attribute, final String expectedMessage) {
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(mock(DTOAnkreuzfloskeln.class));

		final Map<String, Object> patchData = new HashMap<>();
		patchData.put(attribute, null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, patchData))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | unknown argument")
	void patchUnknownArgument() {
		when(this.conn.queryByKey(DTOAnkreuzfloskeln.class, 1L)).thenReturn(mock(DTOAnkreuzfloskeln.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("unknown", "unknown")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse | referenziert")
	void checkBeforeDeletionWithSimpleOperationResponseReferenziert() {
		@SuppressWarnings("unchecked") final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Die Ankreuzkompetenz mit der Kompetenzbeschreibung test ist in der Datenbank referenziert und kann daher nicht gelöscht werden");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse | nicht referenziert")
	void checkBeforeDeletionWithSimpleOperationResponseNichtReferenziert() {
		@SuppressWarnings("unchecked") final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(Collections.emptyList());
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOAnkreuzfloskeln(1L, 0, "test");

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", true);
		assertThat(responses.get(1L).log).isEmpty();
	}

}
