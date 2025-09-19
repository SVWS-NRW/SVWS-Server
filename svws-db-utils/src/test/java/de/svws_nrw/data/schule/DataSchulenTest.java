package de.svws_nrw.data.schule;

import java.util.List;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


/**
 * Diese Klasse testet die Klasse {@link DataSchulen}.
 */
@DisplayName("Diese Klasse testet die Klasse DataSchulen")
@ExtendWith(MockitoExtension.class)
class DataSchulenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchulen data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final var dtoSchuleNRW = new DTOSchuleNRW(1L, "");

		this.data.initDTO(dtoSchuleNRW, 2L, null);

		assertThat(dtoSchuleNRW.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getAll")
	void getAllTest() {
		when(this.conn.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(new DTOSchuleNRW(1L, "123"), new DTOSchuleNRW(3L, "123")));

		final var result = this.data.getAll();

		assertThat(result).hasSize(2)
				.isInstanceOf(List.class)
				.hasOnlyElementsOfType(SchulEintrag.class);
	}

	@Test
	@DisplayName("getList")
	void getListTest() {
		final var dtoWithKuerzel = new DTOSchuleNRW(1L, "123");
		dtoWithKuerzel.Kuerzel = "abc";
		when(this.conn.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(dtoWithKuerzel, new DTOSchuleNRW(3L, "123")));

		final var result = this.data.getList();

		assertThat(result).hasSize(1)
				.isInstanceOf(List.class)
				.hasOnlyElementsOfType(SchulEintrag.class);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dtoSchuleNRW = getDtoSchuleNRW();
		when(this.conn.queryByKey(DTOSchuleNRW.class, dtoSchuleNRW.ID)).thenReturn(dtoSchuleNRW);
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));

		assertThat(data.getById(dtoSchuleNRW.ID))
				.isInstanceOf(SchulEintrag.class)
				.hasFieldOrPropertyWithValue("id", dtoSchuleNRW.ID);
	}

	@Test
	@DisplayName("getById | wrong Id")
	void getByIdTest_wrongId() {
		when(this.conn.queryByKey(any(), any())).thenReturn(null);

		final var throwable = catchThrowable(() -> data.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Eintrag im Katalog der Schulen mit der ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | erfolgreiches mapping")
	void mapTest() {
		final var dtoSchuleNRW = getDtoSchuleNRW();
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));

		assertThat(this.data.map(dtoSchuleNRW))
				.isInstanceOf(SchulEintrag.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("schulnummerStatistik", "456")
				.hasFieldOrPropertyWithValue("idSchulform", 10000L)
				.hasFieldOrPropertyWithValue("strassenname", "RollercoasterRoad")
				.hasFieldOrPropertyWithValue("hausnummer", "42")
				.hasFieldOrPropertyWithValue("zusatzHausnummer", "0")
				.hasFieldOrPropertyWithValue("plz", "10317")
				.hasFieldOrPropertyWithValue("ort", "ein kleiner feiner nicht gemeiner Ort in NRW")
				.hasFieldOrPropertyWithValue("telefon", "01248163264")
				.hasFieldOrPropertyWithValue("fax", "fax??? :D")
				.hasFieldOrPropertyWithValue("email", "wieFaxNurBesser@mail.com")
				.hasFieldOrPropertyWithValue("schulleiter", "Herr Vorragend")
				.hasFieldOrPropertyWithValue("sortierung", 37)
				.hasFieldOrPropertyWithValue("istSichtbar", true);
	}

	@Test
	@DisplayName("map | erfolgreiches mapping, einige Werte bewusst null")
	void mapTest_someValuesNull() {
		final var dtoSchuleNRW = new DTOSchuleNRW(1L, "");
		dtoSchuleNRW.SchulformNr = null;
		dtoSchuleNRW.Name = null;
		dtoSchuleNRW.Sortierung = null;
		dtoSchuleNRW.Sichtbar = null;

		assertThat(this.data.map(dtoSchuleNRW))
				.isInstanceOf(SchulEintrag.class)
				.hasFieldOrPropertyWithValue("idSchulform", null)
				.hasFieldOrPropertyWithValue("name", "")
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("istSichtbar", true);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35),
				arguments("schulnummer", "123456"),
				arguments("kuerzel", "1234567890"),
				arguments("kurzbezeichnung", "eine ganz kurze be"),
				arguments("name", "Telefonmann"),
				arguments("idSchulform", 10000L),
				arguments("strassenname", "CoasterRollerStr"),
				arguments("hausnummer", "101"),
				arguments("zusatzHausnummer", "a"),
				arguments("plz", "31514"),
				arguments("ort", "Dorf"),
				arguments("telefon", "12345678"),
				arguments("fax", "hmm"),
				arguments("email", "mail@mail.com"),
				arguments("schulleiter", "Herr Lehrer"),
				arguments("sortierung", 101),
				arguments("istSichtbar", true),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}


	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = new DTOSchuleNRW(1L, "123456");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "schulnummer" -> assertThat(expectedDTO.SchulNr).isEqualTo(value);
			case "kuerzel" -> assertThat(expectedDTO.Kuerzel).isEqualTo(value);
			case "kurzbezeichnung" -> assertThat(expectedDTO.KurzBez).isEqualTo(value);
			case "name" -> assertThat(expectedDTO.Name).isEqualTo(value);
			case "idSchulform" -> assertThat(expectedDTO)
					.hasFieldOrPropertyWithValue("SchulformBez", "Realschule")
					.hasFieldOrPropertyWithValue("SchulformKrz", "R")
					.hasFieldOrPropertyWithValue("SchulformNr", "10");
			case "strassenname" -> assertThat(expectedDTO.Strassenname).isEqualTo(value);
			case "hausnummer" -> assertThat(expectedDTO.HausNr).isEqualTo(value);
			case "zusatzHausnummer" -> assertThat(expectedDTO.HausNrZusatz).isEqualTo(value);
			case "plz" -> assertThat(expectedDTO.PLZ).isEqualTo(value);
			case "ort" -> assertThat(expectedDTO.Ort).isEqualTo(value);
			case "telefon" -> assertThat(expectedDTO.Telefon).isEqualTo(value);
			case "fax" -> assertThat(expectedDTO.Fax).isEqualTo(value);
			case "email" -> assertThat(expectedDTO.Email).isEqualTo(value);
			case "schulleiter" -> assertThat(expectedDTO.Schulleiter).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.Sortierung).isEqualTo(value);
			case "istSichtbar" -> assertThat(expectedDTO.Sichtbar).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | wrong idSchulform")
	void mapAttributeTest_WrongIdSchulform() {
		final var expectedDTO = new DTOSchuleNRW(1L, "123456");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "idSchulform", 1L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("SchulformKatalogEintrag mit der id 1 nicht gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | idSchulform null")
	void mapAttributeTest_idSchulformNull() throws ApiOperationException {
		final var expectedDTO = new DTOSchuleNRW(1L, "123456");

		this.data.mapAttribute(expectedDTO, "idSchulform", null, null);

		assertThat(expectedDTO)
				.hasFieldOrPropertyWithValue("SchulformBez", null)
				.hasFieldOrPropertyWithValue("SchulformKrz", null)
				.hasFieldOrPropertyWithValue("SchulformNr", null);
	}

	@Test
	@DisplayName("mapAttribute | updateSchulnummer | interne Schule | Erfolg")
	void mapAttributeTest_interneSchulnummer() throws ApiOperationException {
		final var expectedDTO = new DTOSchuleNRW(1L, "1");

		this.data.mapAttribute(expectedDTO, "schulnummerStatistik", "123456", null);

		assertThat(expectedDTO)
				.hasFieldOrPropertyWithValue("SchulNr", "123456")
				.hasFieldOrPropertyWithValue("SchulNr_SIM", "123456");
	}

	@Test
	@DisplayName("mapAttribute | updateSchulnummer | externe Schule | Erfolg")
	void mapAttributeTest_externeSchulnummer() throws ApiOperationException {
		final var expectedDTO = new DTOSchuleNRW(123L, "1");

		this.data.mapAttribute(expectedDTO, "schulnummerStatistik", "987654", null);

		assertThat(expectedDTO)
				.hasFieldOrPropertyWithValue("SchulNr", "200123")
				.hasFieldOrPropertyWithValue("SchulNr_SIM", "987654");
	}

	@Test
	@DisplayName("mapAttribute | updateSchulnummer | falsche Schulnummer")
	void mapAttributeTest_wrongSchulnummer() {
		final var expectedDTO = new DTOSchuleNRW(1L, "1");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "schulnummerStatistik", "222333", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Schulnummer 222333 ist ungültig. Gültige Schulnummern starten mit der Ziffer 1 (intern) oder 9 (extern).")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | updateSchulnummer | unverändert")
	void mapAttributeTest_schulnummerHasntChanged() throws ApiOperationException {
		final var expectedDTO = new DTOSchuleNRW(1L, "123456");

		this.data.mapAttribute(expectedDTO, "schulnummerStatistik", "123456", null);

		assertThat(expectedDTO).hasFieldOrPropertyWithValue("SchulNr", "123456");
		verifyNoInteractions(this.conn);
	}


	@Test
	@DisplayName("mapAttribute | kuerzel doppelt vergeben")
	void mapAttributeTest_duplicatedKuerzel() {
		final var schuleABC = new DTOSchuleNRW(1L, "2");
		schuleABC.Kuerzel = "abc";
		when(this.conn.queryAll(DTOSchuleNRW.class))
				.thenReturn(List.of(schuleABC));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOSchuleNRW(2L, "123"), "kuerzel", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Kuerzel ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzel ist null")
	void mapAttributeTest_kuerzelNull() throws ApiOperationException {
		final var schule = new DTOSchuleNRW(1L, "123");
		schule.Kuerzel = "kuerzel";

		this.data.mapAttribute(schule, "kuerzel", null, null);

		assertThat(schule.Kuerzel).isNull();
	}

	@Test
	@DisplayName("mapAttribute | kuerzel ist empty")
	void mapAttributeTest_kuerzelEmpty() throws ApiOperationException {
		final var schule = new DTOSchuleNRW(1L, "123");
		schule.Kuerzel = "kuerzel";

		this.data.mapAttribute(schule, "kuerzel", "", null);

		assertThat(schule.Kuerzel).isNull();
	}

	private static DTOSchuleNRW getDtoSchuleNRW() {
		final var dtoSchuleNRW = new DTOSchuleNRW(1L, "123");
		dtoSchuleNRW.SchulNr_SIM = "456";
		dtoSchuleNRW.Name = "Schöne Schule";
		dtoSchuleNRW.SchulformNr = "10";
		dtoSchuleNRW.Strassenname = "RollercoasterRoad";
		dtoSchuleNRW.HausNr = "42";
		dtoSchuleNRW.HausNrZusatz = "0";
		dtoSchuleNRW.PLZ = "10317";
		dtoSchuleNRW.Ort = "ein kleiner feiner nicht gemeiner Ort in NRW";
		dtoSchuleNRW.Telefon = "01248163264";
		dtoSchuleNRW.Fax = "fax??? :D";
		dtoSchuleNRW.Email = "wieFaxNurBesser@mail.com";
		dtoSchuleNRW.Schulleiter = "Herr Vorragend";
		dtoSchuleNRW.Sortierung = 37;
		dtoSchuleNRW.Sichtbar = true;
		return dtoSchuleNRW;
	}
}
