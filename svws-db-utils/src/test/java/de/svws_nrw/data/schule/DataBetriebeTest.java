package de.svws_nrw.data.schule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Betrieb;
import de.svws_nrw.core.data.schule.BetriebeAnsprechpartner;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebsart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetrieb;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataBetriebe")
@ExtendWith(MockitoExtension.class)
class DataBetriebeTest {

	@Mock
	private DBEntityManager conn;

	@Mock
	private DataBetriebeAnsprechpartner dataBetriebeAnsprechpartner;

	@InjectMocks
	private DataBetriebe data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUpEach() {
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: name")
	void setAttributesRequiredOnCreationTest() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("test", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (name) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOBetrieb(1L);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto).hasFieldOrPropertyWithValue("ID", 2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOBetrieb(1L);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(Betrieb.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Betriebs darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOBetrieb.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde kein Betrieb mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOBetrieb(1L);
		final var dto2 = new DTOBetrieb(2L);
		when(this.conn.queryAll(DTOBetrieb.class)).thenReturn(List.of(dto1, dto2));
		final var ansprechpartner = new BetriebeAnsprechpartner();
		ansprechpartner.id = 42L;
		ansprechpartner.idBetrieb = 1L;
		ansprechpartner.name = "test";
		when(this.dataBetriebeAnsprechpartner.getAll()).thenReturn(List.of(ansprechpartner));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Betrieb.class)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true)
								.extracting(e -> e.ansprechpartner)
								.asInstanceOf(InstanceOfAssertFactories.LIST)
								.hasSize(1)
								.satisfiesExactly(
										a -> assertThat(a)
												.isInstanceOf(BetriebeAnsprechpartner.class)
												.hasFieldOrPropertyWithValue("id", ansprechpartner.id)
												.hasFieldOrPropertyWithValue("name", ansprechpartner.name)
								),
						f2 -> assertThat(f2)
								.isInstanceOf(Betrieb.class)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOBetrieb.class)).thenReturn(emptyList());

		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOBetrieb(1L);
		dto.name1 = "name1";
		dto.name2 = "name2";
		dto.bemerkungen = "bemerkungen";
		dto.branche = "branche";
		dto.adressArt = 42L;
		dto.ausbildungsbetrieb = true;
		dto.Massnahmentraeger = true;
		dto.BelehrungISG = true;
		dto.ErwFuehrungszeugnis = true;
		dto.bietetPraktika = true;
		dto.strassenname = "strassenname";
		dto.hausnr = "42";
		dto.hausnrzusatz = "42";
		dto.ort_id = 12L;
		dto.telefon1 = "telefon1";
		dto.telefon2 = "telefon2";
		dto.fax = "fax";
		dto.email = "email";
		dto.Sichtbar = true;
		dto.sortierung = 123;

		assertThat(this.data.map(dto))
				.isInstanceOf(Betrieb.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("name", dto.name1)
				.hasFieldOrPropertyWithValue("nameZusatz", dto.name2)
				.hasFieldOrPropertyWithValue("bemerkungen", dto.bemerkungen)
				.hasFieldOrPropertyWithValue("branche", dto.branche)
				.hasFieldOrPropertyWithValue("idBetriebsart", dto.adressArt)
				.hasFieldOrPropertyWithValue("istAusbildungsbetrieb", dto.ausbildungsbetrieb)
				.hasFieldOrPropertyWithValue("istMassnahmentraeger", dto.Massnahmentraeger)
				.hasFieldOrPropertyWithValue("belehrungNachISGErforderlich", dto.BelehrungISG)
				.hasFieldOrPropertyWithValue("erweitertesFuehrungszeugnisErforderlich", dto.ErwFuehrungszeugnis)
				.hasFieldOrPropertyWithValue("bietetPraktikumsplaetzeAn", dto.bietetPraktika)
				.hasFieldOrPropertyWithValue("strasse", dto.strassenname)
				.hasFieldOrPropertyWithValue("hausnummer", dto.hausnr)
				.hasFieldOrPropertyWithValue("hausnummerZusatz", dto.hausnrzusatz)
				.hasFieldOrPropertyWithValue("idOrt", dto.ort_id)
				.hasFieldOrPropertyWithValue("telefon1", dto.telefon1)
				.hasFieldOrPropertyWithValue("telefon2", dto.telefon2)
				.hasFieldOrPropertyWithValue("fax", dto.fax)
				.hasFieldOrPropertyWithValue("eMail", dto.email)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto.sortierung = 123);
	}

	@Test
	@DisplayName("map | sichtbar null")
	void mapSichtbarIsNull() {
		final var dto = new DTOBetrieb(1L);
		dto.Sichtbar = null;

		assertThat(this.data.map(dto))
				.isInstanceOf(Betrieb.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOBetrieb.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto  = new DTOBetrieb(1L);

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("patch | name")
	void patchName() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("name", "neu"));

		assertThat(dto.name1).isEqualTo("neu");
	}

	@Test
	@DisplayName("patch | name > 50 Zeichen")
	void patchNameIsTooLong() {
		final var dto = new DTOBetrieb(1L);
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("name", RandomStringUtils.insecure().nextAscii(51))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut name: Die Länge des Strings ist auf 50 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | name Null")
	void patchNameIsNull() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));
		final var map = new HashMap<String, Object>();
		map.put("name", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut name: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | name empty")
	void patchNameIsEmpty() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("name", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut name: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | wrong type")
	void patchNameWrongType() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("name", 42)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut name: Es wurde ein String erwartet, aber keiner übergeben.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | name is blank")
	void patchNameIsBlank() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		dto.name1 = "bezeichnung";
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("name", "    "));

		verify(this.conn, never()).queryAll(DTOBetrieb.class);
		assertThat(dto.name1).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | name doesn't change")
	void patchNameDoesNotChange() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("name", "bezeichnung"));

		verify(this.conn, never()).queryAll(DTOFoerderschwerpunkt.class);
		assertThat(dto.name1).isEqualTo("bezeichnung");
	}

	@Test
	@DisplayName("patch | name already used")
	void patchNameAlreadyUsed() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));
		final var dto = new DTOBetrieb(2L);
		dto.name1 = "test";
		when(this.conn.queryAll(DTOBetrieb.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("name", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Name test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | name already used different case")
	void patchNameAlreadyUsedWithDifferentCase() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));
		final var dto = new DTOBetrieb(2L);
		dto.name1 = "TEST";
		when(this.conn.queryAll(DTOBetrieb.class)).thenReturn(List.of(dto));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("name", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Name test ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | name change case in same object")
	void patchNameChangeCase() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		when(conn.queryAll(DTOBetrieb.class)).thenReturn(List.of(dto));
		final var newDto = new DTOBetrieb(2L);
		newDto.name1 = "abc";
		when(this.conn.queryByKey(DTOBetrieb.class, 2L)).thenReturn(newDto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(2L, Map.of("name", "ABC"));

		assertThat(newDto.name1).isEqualTo("ABC");
	}

	private static Stream<Arguments> patchStringAttributesTooManyCharacters() {
		return Stream.of(
				arguments("nameZusatz", "Attribut nameZusatz: Die Länge des Strings ist auf 50 Zeichen limitiert."),
				arguments("bemerkungen", "Attribut bemerkungen: Die Länge des Strings ist auf 255 Zeichen limitiert."),
				arguments("branche", "Attribut branche: Die Länge des Strings ist auf 50 Zeichen limitiert."),
				arguments("strasse", "Attribut strasse: Die Länge des Strings ist auf 55 Zeichen limitiert."),
				arguments("hausnummerZusatz", "Attribut hausnummerZusatz: Die Länge des Strings ist auf 30 Zeichen limitiert."),
				arguments("hausnummer", "Attribut hausnummer: Die Länge des Strings ist auf 10 Zeichen limitiert."),
				arguments("telefon1", "Attribut telefon1: Die Länge des Strings ist auf 20 Zeichen limitiert."),
				arguments("telefon2", "Attribut telefon2: Die Länge des Strings ist auf 20 Zeichen limitiert."),
				arguments("fax", "Attribut fax: Die Länge des Strings ist auf 20 Zeichen limitiert."),
				arguments("eMail", "Attribut eMail: Die Länge des Strings ist auf 100 Zeichen limitiert.")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributesTooManyCharacters")
	@DisplayName("patch | String Attributes Too Long")
	void patchAttributeTooLong(final String attributeName, final String expectedMessage) {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));

		assertThatException()
				.isThrownBy(() -> data.patch(1L, Map.of(attributeName, RandomStringUtils.insecure().nextAscii(256))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private static Stream<Arguments> patchStringAttributes() {
		return Stream.of(
				arguments("nameZusatz"),
				arguments("bemerkungen"),
				arguments("branche"),
				arguments("strasse"),
				arguments("hausnummerZusatz"),
				arguments("hausnummer"),
				arguments("telefon1"),
				arguments("telefon2"),
				arguments("fax"),
				arguments("eMail"),
				arguments("unknown")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributes")
	@DisplayName("patch String attributes")
	void patchStringAttributes(final String attributeName) {
		final var dto = new DTOBetrieb(1L);
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		final var newValue = "newValue";

		final var throwable = catchThrowable(() -> this.data.patch(1L, Map.of(attributeName, newValue)));

		switch (attributeName) {
			case "nameZusatz" -> assertThat(dto.name2).isEqualTo(newValue);
			case "bemerkungen" -> assertThat(dto.bemerkungen).isEqualTo(newValue);
			case "branche" -> assertThat(dto.branche).isEqualTo(newValue);
			case "strasse" -> assertThat(dto.strassenname).isEqualTo(newValue);
			case "hausnummerZusatz" -> assertThat(dto.hausnrzusatz).isEqualTo(newValue);
			case "hausnummer" -> assertThat(dto.hausnr).isEqualTo(newValue);
			case "telefon1" -> assertThat(dto.telefon1).isEqualTo(newValue);
			case "telefon2" -> assertThat(dto.telefon2).isEqualTo(newValue);
			case "fax" -> assertThat(dto.fax).isEqualTo(newValue);
			case "eMail" -> assertThat(dto.email).isEqualTo(newValue);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> patchBooleanAttributes() {
		return Stream.of(
				arguments("istAusbildungsbetrieb"),
				arguments("istMassnahmentraeger"),
				arguments("belehrungNachISGErforderlich"),
				arguments("erweitertesFuehrungszeugnisErforderlich"),
				arguments("bietetPraktikumsplaetzeAn"),
				arguments("istSichtbar")
		);
	}

	@ParameterizedTest
	@MethodSource("patchBooleanAttributes")
	@DisplayName("patch Boolean attributes")
	void patchBooleanAttributes(final String attributeName) throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		final var newValue = true;

		this.data.patch(1L, Map.of(attributeName, newValue));

		switch (attributeName) {
			case "istAusbildungsbetrieb" -> assertThat(dto.ausbildungsbetrieb).isEqualTo(newValue);
			case "istMassnahmentraeger" -> assertThat(dto.Massnahmentraeger).isEqualTo(newValue);
			case "belehrungNachISGErforderlich" -> assertThat(dto.BelehrungISG).isEqualTo(newValue);
			case "erweitertesFuehrungszeugnisErforderlich" -> assertThat(dto.ErwFuehrungszeugnis).isEqualTo(newValue);
			case "bietetPraktikumsplaetzeAn" -> assertThat(dto.bietetPraktika).isEqualTo(newValue);
			case "istSichtbar" -> assertThat(dto.Sichtbar).isEqualTo(newValue);
			default -> {
				//
			}
		}
	}

	@Test
	@DisplayName("patch | istSichtbar")
	void patchIstSichtbar() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		dto.Sichtbar = false;
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("istSichtbar", true));

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("patch | istSichtbar is null")
	void patchIstSichtbarIsNull() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));
		final var map = new HashMap<String, Object>();
		map.put("istSichtbar", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut istSichtbar: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idOrt | null")
	void patchIdOrtIsNull() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		dto.ort_id = 42L;
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new  HashMap<String, Object>();
		map.put("idOrt", null);

		this.data.patch(1L, map);

		assertThat(dto.ort_id).isNull();
	}

	@Test
	@DisplayName("patch | idOrt | noChanges")
	void patchIdOrtNoChanges() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		dto.ort_id = 42L;
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("idOrt", 42L));

		assertThat(dto.ort_id).isEqualTo(42L);
		verify(this.conn, never()).queryByKey(DTOOrt.class, 1L);
	}

	@Test
	@DisplayName("patch | idOrt | wrong id")
	void patchIdOrtWrongId() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idOrt", -48L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Ort zur id -48 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idOrt")
	void patchIdOrt() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		dto.ort_id = 42L;
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOOrt.class, 99L)).thenReturn(mock(DTOOrt.class));

		this.data.patch(1L, Map.of("idOrt", 99L));

		assertThat(dto.ort_id).isEqualTo(99L);
	}

	@Test
	@DisplayName("patch | idBetriebsart | null")
	void patchIdBetriebsartIsNull() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		dto.adressArt = 42L;
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new  HashMap<String, Object>();
		map.put("idBetriebsart", null);

		this.data.patch(1L, map);

		assertThat(dto.adressArt).isNull();
	}

	@Test
	@DisplayName("patch | idBetriebsart | noChanges")
	void patchIdBetriebsartNoChanges() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		dto.adressArt = 42L;
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("idBetriebsart", 42L));

		assertThat(dto.adressArt).isEqualTo(42L);
		verify(this.conn, never()).queryByKey(DTOOrt.class, 1L);
	}

	@Test
	@DisplayName("patch | idBetriebsart | wrong id")
	void patchIdBetriebsartWrongId() {
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(mock(DTOBetrieb.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idBetriebsart", -48L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Betriebsart zur id -48 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idBetriebsart")
	void patchIdBetriebsart() throws ApiOperationException {
		final var dto = new DTOBetrieb(1L);
		dto.adressArt = 42L;
		when(this.conn.queryByKey(DTOBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOBetriebsart.class, 99L)).thenReturn(mock(DTOBetriebsart.class));

		this.data.patch(1L, Map.of("idBetriebsart", 99L));

		assertThat(dto.adressArt).isEqualTo(99L);
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
		final var dto = new DTOBetrieb(1L);
		dto.name1 = "abc";

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Der Betrieb mit dem Name abc ist in der Datenbank referenziert und kann daher nicht gelöscht werden.");
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
		final var dto = new DTOBetrieb(1L);

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(response)
				.hasFieldOrPropertyWithValue("success", true)
				.satisfies(r -> assertThat(r.log).isEmpty());
	}

}
