package de.svws_nrw.data.lehrer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.Schulleitung;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataLehrerStammdaten")
@ExtendWith(MockitoExtension.class)
class DataLehrerStammdatenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataLehrerStammdaten dataLehrerStammdaten;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDto | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final var dtoLehrer = new DTOLehrer(1L, "abc", "abc");

		this.dataLehrerStammdaten.initDTO(dtoLehrer, 2L, null);

		assertThat(dtoLehrer.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getByID | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dtoLehrer = new DTOLehrer(1L, "abc", "abc");
		when(this.conn.queryByKey(DTOLehrer.class, dtoLehrer.ID)).thenReturn(dtoLehrer);

		assertThat(dataLehrerStammdaten.getById(dtoLehrer.ID))
				.isInstanceOf(LehrerStammdaten.class)
				.hasFieldOrPropertyWithValue("id", dtoLehrer.ID);
	}

	@Test
	@DisplayName("getById | wrong ID")
	void getByIdTest_wrongID() {
		when(this.conn.queryByKey(any(), any())).thenReturn(null);

		final var throwable = catchThrowable(() -> dataLehrerStammdaten.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Lehrkraft mit der ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getListByID | Erfolg")
	void getListByIDTest() throws ApiOperationException {
		final var lehrer = List.of(new DTOLehrer(1L, "abc", "abc"));
		when(this.conn.queryByKeyList(DTOLehrer.class, List.of(1L))).thenReturn(lehrer);

		assertThat(this.dataLehrerStammdaten.getListByIDs(List.of(1L)))
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerStammdaten.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
				);
	}

	@Test
	@DisplayName("getListByID | list of ids null")
	void getListByIDTest_idsNull() throws ApiOperationException {

		assertThat(this.dataLehrerStammdaten.getListByIDs(null))
				.isInstanceOf(List.class)
				.isNotNull()
				.isEmpty();
	}

	@Test
	@DisplayName("getListByID | empty list of ids")
	void getListByIDTest_idsEmpty() throws ApiOperationException {

		assertThat(this.dataLehrerStammdaten.getListByIDs(Collections.emptyList()))
				.isInstanceOf(List.class)
				.isNotNull()
				.isEmpty();
	}

	@Test
	@DisplayName("getSichtbareLehrerStammdaten | Erfolg")
	void getSichtbareLehrerStammdatenTest() throws ApiOperationException {
		final var lehrer = List.of(new DTOLehrer(1L, "abc", "abc"));
		when(this.conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true)).thenReturn(lehrer);

		assertThat(this.dataLehrerStammdaten.getSichtbareLehrerStammdaten())
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerStammdaten.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
				);
	}

	@Test
	@DisplayName("getSichtbareLehrerStammdaten | result empty")
	void getSichtbareLehrerStammdatenTest_emptyList() throws ApiOperationException {
		when(this.conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerStammdaten.getSichtbareLehrerStammdaten())
				.isInstanceOf(List.class)
				.isNotNull()
				.isEmpty();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() throws ApiOperationException {
		final var lehrer = List.of(new DTOLehrer(1L, "abc", "abc"));
		when(this.conn.queryAll(DTOLehrer.class)).thenReturn(lehrer);

		assertThat(this.dataLehrerStammdaten.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerStammdaten.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
				);
	}

	@Test
	@DisplayName("getAll | result empty")
	void getAllTest_emptyList() throws ApiOperationException {
		when(this.conn.queryAll(DTOLehrer.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerStammdaten.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isEmpty();
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() throws ApiOperationException {
		final var dtoLehrer = getDtoLehrer();
		final var foto = new DTOLehrerFoto(1L);
		foto.FotoBase64 = "abc";
		when(conn.queryByKey(DTOLehrerFoto.class, 1L)).thenReturn(foto);

		assertThat(this.dataLehrerStammdaten.map(dtoLehrer))
				.isInstanceOf(LehrerStammdaten.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "abc")
				.hasFieldOrPropertyWithValue("personalTyp", PersonalTyp.LEHRKRAFT.kuerzel)
				.hasFieldOrPropertyWithValue("anrede", "abc")
				.hasFieldOrPropertyWithValue("titel", "abc")
				.hasFieldOrPropertyWithValue("amtsbezeichnung", "abc")
				.hasFieldOrPropertyWithValue("nachname", "abc")
				.hasFieldOrPropertyWithValue("vorname", "abc")
				.hasFieldOrPropertyWithValue("geschlecht", Geschlecht.M.id)
				.hasFieldOrPropertyWithValue("geburtsdatum", "abc")
				.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", Nationalitaeten.DEU.daten.iso3)
				.hasFieldOrPropertyWithValue("strassenname", "abc")
				.hasFieldOrPropertyWithValue("hausnummer", "abc")
				.hasFieldOrPropertyWithValue("hausnummerZusatz", "abc")
				.hasFieldOrPropertyWithValue("wohnortID", 1L)
				.hasFieldOrPropertyWithValue("ortsteilID", 1L)
				.hasFieldOrPropertyWithValue("telefon", "abc")
				.hasFieldOrPropertyWithValue("telefonMobil", "abc")
				.hasFieldOrPropertyWithValue("emailDienstlich", "abc")
				.hasFieldOrPropertyWithValue("emailPrivat", "abc")
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true)
				.hasFieldOrPropertyWithValue("foto", "abc");
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Leitungsfunktionen")
	void mapTest_checkLeitungsfunktionen() throws ApiOperationException {
		final var dtoLehrer = getDtoLehrer();
		final var dtoSchulleitung = new DTOSchulleitung(1L, 1L, "abc", 1L);
		dtoSchulleitung.Von = "abc";
		dtoSchulleitung.Bis = "abc";
		when(conn.queryList(DTOSchulleitung.QUERY_BY_LEHRERID, DTOSchulleitung.class, 1L))
				.thenReturn(List.of(dtoSchulleitung));

		final var lehrerStammdaten = this.dataLehrerStammdaten.map(dtoLehrer);

		assertThat(lehrerStammdaten.leitungsfunktionen.getFirst())
				.isInstanceOf(Schulleitung.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idLehrer", 1L)
				.hasFieldOrPropertyWithValue("idLeitungsfunktion", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "abc")
				.hasFieldOrPropertyWithValue("beginn", "abc")
				.hasFieldOrPropertyWithValue("ende", "abc");
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | some Values null")
	void mapTest_someValuesNull() throws ApiOperationException {
		final var dtoLehrer = getDtoLehrer();
		dtoLehrer.PersonTyp = null;
		dtoLehrer.Anrede = null;
		dtoLehrer.Titel = null;
		dtoLehrer.Amtsbezeichnung = null;
		dtoLehrer.Nachname = null;
		dtoLehrer.Vorname = null;
		dtoLehrer.Geschlecht = null;
		dtoLehrer.staatsangehoerigkeit = null;
		dtoLehrer.Sichtbar = null;
		dtoLehrer.statistikRelevant = null;

		assertThat(this.dataLehrerStammdaten.map(dtoLehrer))
				.isInstanceOf(LehrerStammdaten.class)
				.hasFieldOrPropertyWithValue("personalTyp", "")
				.hasFieldOrPropertyWithValue("anrede", "")
				.hasFieldOrPropertyWithValue("titel", "")
				.hasFieldOrPropertyWithValue("amtsbezeichnung", "")
				.hasFieldOrPropertyWithValue("nachname", "")
				.hasFieldOrPropertyWithValue("vorname", "")
				.hasFieldOrPropertyWithValue("geschlecht", -1)
				.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", null)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true)
				.hasFieldOrPropertyWithValue("foto", null);
	}

	@Test
	@DisplayName("mapList | Erfolg")
	void mapListTest() throws ApiOperationException {
		final var dtoLehrer = getDtoLehrer();
		final var foto = new DTOLehrerFoto(1L);
		foto.FotoBase64 = "abc";
		when(conn.queryByKeyList(DTOLehrerFoto.class, List.of(1L))).thenReturn(List.of(foto));

		assertThat(this.dataLehrerStammdaten.mapList(List.of(dtoLehrer)))
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("personalTyp", PersonalTyp.LEHRKRAFT.kuerzel)
						.hasFieldOrPropertyWithValue("anrede", "abc")
						.hasFieldOrPropertyWithValue("titel", "abc")
						.hasFieldOrPropertyWithValue("amtsbezeichnung", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
						.hasFieldOrPropertyWithValue("vorname", "abc")
						.hasFieldOrPropertyWithValue("geschlecht", Geschlecht.M.id)
						.hasFieldOrPropertyWithValue("geburtsdatum", "abc")
						.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", Nationalitaeten.DEU.daten.iso3)
						.hasFieldOrPropertyWithValue("strassenname", "abc")
						.hasFieldOrPropertyWithValue("hausnummer", "abc")
						.hasFieldOrPropertyWithValue("hausnummerZusatz", "abc")
						.hasFieldOrPropertyWithValue("wohnortID", 1L)
						.hasFieldOrPropertyWithValue("ortsteilID", 1L)
						.hasFieldOrPropertyWithValue("telefon", "abc")
						.hasFieldOrPropertyWithValue("telefonMobil", "abc")
						.hasFieldOrPropertyWithValue("emailDienstlich", "abc")
						.hasFieldOrPropertyWithValue("emailPrivat", "abc")
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true)
						.hasFieldOrPropertyWithValue("foto", "abc")
				);
	}

	@Test
	@DisplayName("mapList | Erfolg | list is empty")
	void mapListTest_listEmpty() throws ApiOperationException {
		assertThat(this.dataLehrerStammdaten.mapList(Collections.emptyList()))
				.isInstanceOf(List.class)
				.isNotNull()
				.isEmpty();
	}

	@Test
	@DisplayName("mapList | Erfolg | list is null")
	void mapListTest_listNull() throws ApiOperationException {
		assertThat(this.dataLehrerStammdaten.mapList(null))
				.isInstanceOf(List.class)
				.isNotNull()
				.isEmpty();
	}

	@Test
	@DisplayName("mapList | erfolgreiches Mapping | some Values null")
	void mapListTest_someValuesNull() throws ApiOperationException {
		final var dtoLehrer = getDtoLehrer();
		dtoLehrer.PersonTyp = null;
		dtoLehrer.Anrede = null;
		dtoLehrer.Titel = null;
		dtoLehrer.Amtsbezeichnung = null;
		dtoLehrer.Nachname = null;
		dtoLehrer.Vorname = null;
		dtoLehrer.Geschlecht = null;
		dtoLehrer.staatsangehoerigkeit = null;
		dtoLehrer.Sichtbar = null;
		dtoLehrer.statistikRelevant = null;

		assertThat(this.dataLehrerStammdaten.mapList(List.of(dtoLehrer)))
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerStammdaten.class)
						.hasFieldOrPropertyWithValue("personalTyp", "")
						.hasFieldOrPropertyWithValue("anrede", "")
						.hasFieldOrPropertyWithValue("titel", "")
						.hasFieldOrPropertyWithValue("amtsbezeichnung", "")
						.hasFieldOrPropertyWithValue("nachname", "")
						.hasFieldOrPropertyWithValue("vorname", "")
						.hasFieldOrPropertyWithValue("geschlecht", -1)
						.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", null)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true)
						.hasFieldOrPropertyWithValue("foto", null));
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDtoLehrer();

		final var throwable = catchThrowable(() -> this.dataLehrerStammdaten.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "kuerzel" -> assertThat(expectedDTO.Kuerzel).isEqualTo(value);
			case "personalTyp" -> assertThat(expectedDTO.PersonTyp).isEqualTo(value);
			case "anrede" -> assertThat(expectedDTO.Anrede).isEqualTo(value);
			case "titel" -> assertThat(expectedDTO.Titel).isEqualTo(value);
			case "amtsbezeichnung" -> assertThat(expectedDTO.Amtsbezeichnung).isEqualTo(value);
			case "nachname" -> assertThat(expectedDTO.Nachname).isEqualTo(value);
			case "vorname" -> assertThat(expectedDTO.Vorname).isEqualTo(value);
			case "geschlecht" -> assertThat(expectedDTO.Geschlecht).isEqualTo(value);
			case "geburtsdatum" -> assertThat(expectedDTO.Geburtsdatum).isEqualTo(value);
			case "staatsangehoerigkeitID" -> assertThat(expectedDTO.staatsangehoerigkeit).isEqualTo(value);
			case "strassenname" -> assertThat(expectedDTO.Strassenname).isEqualTo(value);
			case "hausnummer" -> assertThat(expectedDTO.HausNr).isEqualTo(value);
			case "hausnummerZusatz" -> assertThat(expectedDTO.HausNrZusatz).isEqualTo(value);
			case "wohnortID" -> assertThat(expectedDTO.Ort_ID).isEqualTo(value);
			case "ortsteilID" -> assertThat(expectedDTO.Ortsteil_ID).isEqualTo(value);
			case "telefon" -> assertThat(expectedDTO.telefon).isEqualTo(value);
			case "telefonMobil" -> assertThat(expectedDTO.telefonMobil).isEqualTo(value);
			case "emailDienstlich" -> assertThat(expectedDTO.eMailDienstlich).isEqualTo(value);
			case "emailPrivat" -> assertThat(expectedDTO.eMailPrivat).isEqualTo(value);
			case "istSichtbar" -> assertThat(expectedDTO.Sichtbar).isEqualTo(value);
			case "istRelevantFuerStatistik" -> assertThat(expectedDTO.statistikRelevant).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 1L),
				arguments("kuerzel", "abc"),
				arguments("personalTyp", PersonalTyp.LEHRKRAFT),
				arguments("anrede", "abc"),
				arguments("titel", "abc"),
				arguments("amtsbezeichnung", "abc"),
				arguments("nachname", "abc"),
				arguments("vorname", "abc"),
				arguments("geschlecht", Geschlecht.M),
				arguments("geburtsdatum", "abc"),
				arguments("staatsangehoerigkeitID", Nationalitaeten.DEU),
				arguments("strassenname", "abc"),
				arguments("hausnummer", "abc"),
				arguments("hausnummerZusatz", "abc"),
				arguments("wohnortID", 1L),
				arguments("ortsteilID", 1L),
				arguments("telefon", "abc"),
				arguments("telefonMobil", "abc"),
				arguments("emailDienstlich", "abc"),
				arguments("emailPrivat", "abc"),
				arguments("istSichtbar", true),
				arguments("istRelevantFuerStatistik", true),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@Test
	@DisplayName("mapAttribute | personalTyp is Not Valid")
	void mapAttributeTest_personalTypNotValid() {
		final var throwable = catchThrowable(() -> this.dataLehrerStammdaten.mapAttribute(getDtoLehrer(), "personalTyp", "abc", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Ein PersonalTyp mit dem Kuerzel abc wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | geschlecht is Not Valid")
	void mapAttributeTest_geschlechtNotValid() {
		final var throwable = catchThrowable(() -> this.dataLehrerStammdaten.mapAttribute(getDtoLehrer(), "geschlecht", -1, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Geschlecht mit dem dem Wert -1 vorhanden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | staatsangehoerigkeitID is Null")
	void mapAttributeTest_staatsangehoerigkeitIDNull() throws ApiOperationException {
		final var expectedDTO = getDtoLehrer();

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "staatsangehoerigkeitID", null, null);

		assertThat(expectedDTO).hasFieldOrPropertyWithValue("staatsangehoerigkeit", null);
	}

	@Test
	@DisplayName("mapAttribute | staatsangehoerigkeitID is Blank")
	void mapAttributeTest_staatsangehoerigkeitIDBlank() throws ApiOperationException {
		final var expectedDTO = getDtoLehrer();

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "staatsangehoerigkeitID", "", null);

		assertThat(expectedDTO).hasFieldOrPropertyWithValue("staatsangehoerigkeit", null);
	}

	@Test
	@DisplayName("mapAttribute | staatsangehoerigkeitID is Not Valid")
	void mapAttributeTest_staatsangehoerigkeitIDNotValid() {
		final var throwable = catchThrowable(() -> this.dataLehrerStammdaten.mapAttribute(null, "staatsangehoerigkeitID", "-1", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("keine Nationalität zur ID -1 vorhanden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | wohnortID Valid")
	void mapAttributeTest_wohnortID() throws ApiOperationException {
		final var expectedDTO = getDtoLehrer();

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "wohnortID", 11L, Map.of("ortsteilID", 22L));

		assertThat(expectedDTO)
				.hasFieldOrPropertyWithValue("Ort_ID", 11L);
	}

	@Test
	@DisplayName("mapAttribute | wohnortID is Not Valid")
	void mapAttributeTest_wohnortIDNotValid() {
		final var throwable = catchThrowable(() -> this.dataLehrerStammdaten.mapAttribute(getDtoLehrer(), "wohnortID", -1L, Map.of("ortsteilID", 22L)));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("WohnortID -1 ungültig.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | ortsteilID Valid")
	void mapAttributeTest_ortsteilID() throws ApiOperationException {
		final var expectedDTO = getDtoLehrer();
		final var dtoOrtsteil = new DTOOrtsteil(11L, "abc");
		dtoOrtsteil.Ort_ID = 22L;
		when(conn.queryByKey(DTOOrtsteil.class, 11L)).thenReturn(dtoOrtsteil);

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "ortsteilID", 11L, Map.of("wohnortID", 22L));

		assertThat(expectedDTO).hasFieldOrPropertyWithValue("Ortsteil_ID", 11L);
	}

	@Test
	@DisplayName("mapAttribute | ortsteil.Ort_ID is null")
	void mapAttributeTest_ortIdNull() throws ApiOperationException {
		final var expectedDTO = getDtoLehrer();
		when(conn.queryByKey(DTOOrtsteil.class, 11L)).thenReturn(new DTOOrtsteil(11L, "abc"));

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "ortsteilID", 11L, Map.of("wohnortID", 22L));

		assertThat(expectedDTO).hasFieldOrPropertyWithValue("Ortsteil_ID", null);
	}

	@Test
	@DisplayName("mapAttribute | ortsteil not found")
	void mapAttributeTest_ortsteilNotFound() throws ApiOperationException {
		final var expectedDTO = getDtoLehrer();
		when(conn.queryByKey(DTOOrtsteil.class, 11L)).thenReturn(null);

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "ortsteilID", 11L, Map.of("wohnortID", 22L));

		assertThat(expectedDTO).hasFieldOrPropertyWithValue("Ortsteil_ID", null);
	}

	@Test
	@DisplayName("mapAttribute | ort and ortsteil dont match")
	void mapAttributeTest_OrtAndOrtsteilDontMatch() throws ApiOperationException {
		final var expectedDTO = getDtoLehrer();
		final var dtoOrtsteil = new DTOOrtsteil(11L, "abc");
		dtoOrtsteil.Ort_ID = 15L;
		when(conn.queryByKey(DTOOrtsteil.class, 11L)).thenReturn(dtoOrtsteil);

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "ortsteilID", 11L, Map.of("wohnortID", 22L));

		assertThat(expectedDTO).hasFieldOrPropertyWithValue("Ortsteil_ID", null);
	}

	@Test
	@DisplayName("mapAttribute | ortsteilID is Not Valid")
	void mapAttributeTest_ortsteilIDNotValid() {
		final var throwable = catchThrowable(() -> this.dataLehrerStammdaten.mapAttribute(getDtoLehrer(), "ortsteilID", -1L, Map.of("wohnortID", 22L)));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("OrtsteilID -1 ungültig.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | update foto")
	void mapAttributeTest_updateFoto() throws ApiOperationException {
		final var expectedDTO = new DTOLehrer(1L, "1", "1");
		final var foto = new DTOLehrerFoto(1L);
		foto.FotoBase64 = "abc";
		when(conn.queryByKey(DTOLehrerFoto.class, 1L)).thenReturn(foto);
		final var expectedFoto = new DTOLehrerFoto(1L);
		expectedFoto.FotoBase64 = "cde";

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "foto", "cde", null);

		verify(conn, times(1)).transactionPersist(expectedFoto);
	}

	@Test
	@DisplayName("mapAttribute | fotos identisch | kein update")
	void mapAttributeTest_fotosIdentical() throws ApiOperationException {
		final var expectedDTO = new DTOLehrer(1L, "1", "1");
		final var foto = new DTOLehrerFoto(1L);
		foto.FotoBase64 = "abc";
		when(conn.queryByKey(DTOLehrerFoto.class, 1L)).thenReturn(foto);

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "foto", "abc", null);

		verify(conn, never()).transactionPersist(any());
	}

	@Test
	@DisplayName("mapAttribute | altes und neues foto null | kein update")
	void mapAttributeTest_oldAndNewFotoNull() throws ApiOperationException {
		final var expectedDTO = new DTOLehrer(1L, "1", "1");
		when(conn.queryByKey(DTOLehrerFoto.class, 1L)).thenReturn(null);

		this.dataLehrerStammdaten.mapAttribute(expectedDTO, "foto", null, null);

		verify(conn, never()).transactionPersist(any());
	}

	private DTOLehrer getDtoLehrer() {
		final var dtoLehrer = new DTOLehrer(1L, "abc", "abc");
		dtoLehrer.PersonTyp = PersonalTyp.LEHRKRAFT;
		dtoLehrer.Anrede = "abc";
		dtoLehrer.Titel = "abc";
		dtoLehrer.Amtsbezeichnung = "abc";
		dtoLehrer.Vorname = "abc";
		dtoLehrer.Geschlecht = Geschlecht.M;
		dtoLehrer.Geburtsdatum = "abc";
		dtoLehrer.staatsangehoerigkeit = Nationalitaeten.DEU;
		dtoLehrer.Strassenname = "abc";
		dtoLehrer.HausNr = "abc";
		dtoLehrer.HausNrZusatz = "abc";
		dtoLehrer.Ort_ID = 1L;
		dtoLehrer.Ortsteil_ID = 1L;
		dtoLehrer.telefon = "abc";
		dtoLehrer.telefonMobil = "abc";
		dtoLehrer.eMailDienstlich = "abc";
		dtoLehrer.eMailPrivat = "abc";
		dtoLehrer.Sichtbar = true;
		dtoLehrer.statistikRelevant = true;
		return dtoLehrer;
	}
}
