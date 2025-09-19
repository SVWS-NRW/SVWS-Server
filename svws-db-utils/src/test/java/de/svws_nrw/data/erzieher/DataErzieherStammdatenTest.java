package de.svws_nrw.data.erzieher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataErzieherStammdaten")
@ExtendWith(MockitoExtension.class)
class DataErzieherStammdatenTest {

	@Mock
	private DBEntityManager conn;

	private DataErzieherStammdaten dataErzieherStammdaten;

	@BeforeEach
	void setUp() {
		dataErzieherStammdaten = new DataErzieherStammdaten(conn);
	}

	@Test
	@DisplayName("getById | Erzieher 1")
	void getByIdErzieher1Test() throws ApiOperationException {
		final DTOSchuelerErzieherAdresse dto = getDTOSchuelerErzieherAdresse1();
		when(conn.queryByKey(DTOSchuelerErzieherAdresse.class, 1L)).thenReturn(dto);

		assertThat(dataErzieherStammdaten.getById(11L))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 11L)
				.hasFieldOrPropertyWithValue("idSchueler", 1L)
				.hasFieldOrPropertyWithValue("idErzieherArt", 1L)
				.hasFieldOrPropertyWithValue("titel", "Dr.")
				.hasFieldOrPropertyWithValue("anrede", "Frau")
				.hasFieldOrPropertyWithValue("nachname", "Meier")
				.hasFieldOrPropertyWithValue("vorname", "Anna")
				.hasFieldOrPropertyWithValue("strassenname", "Hauptstr")
				.hasFieldOrPropertyWithValue("hausnummer", "10")
				.hasFieldOrPropertyWithValue("hausnummerZusatz", "b")
				.hasFieldOrPropertyWithValue("wohnortID", 3L)
				.hasFieldOrPropertyWithValue("ortsteilID", 4L)
				.hasFieldOrPropertyWithValue("eMail", "anna.meier@xyz.de")
				.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", null)
				.hasFieldOrPropertyWithValue("erhaeltAnschreiben", true)
				.hasFieldOrPropertyWithValue("bemerkungen", "Testbeschreibung");
	}

	@Test
	@DisplayName("getById | Erzieher 2")
	void getByIdErzieher2Test() throws ApiOperationException {
		final DTOSchuelerErzieherAdresse dto = getDTOSchuelerErzieherAdresse2();
		when(conn.queryByKey(DTOSchuelerErzieherAdresse.class, 1L)).thenReturn(dto);

		assertThat(dataErzieherStammdaten.getById(12L))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 12L)
				.hasFieldOrPropertyWithValue("idSchueler", 1L)
				.hasFieldOrPropertyWithValue("idErzieherArt", 1L)
				.hasFieldOrPropertyWithValue("titel", "Dr.")
				.hasFieldOrPropertyWithValue("anrede", "Herr")
				.hasFieldOrPropertyWithValue("nachname", "Meier")
				.hasFieldOrPropertyWithValue("vorname", "Holger")
				.hasFieldOrPropertyWithValue("strassenname", "Hauptstr")
				.hasFieldOrPropertyWithValue("hausnummer", "10")
				.hasFieldOrPropertyWithValue("hausnummerZusatz", "b")
				.hasFieldOrPropertyWithValue("wohnortID", 3L)
				.hasFieldOrPropertyWithValue("ortsteilID", 4L)
				.hasFieldOrPropertyWithValue("eMail", "holger.meier@xyz.de")
				.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", null)
				.hasFieldOrPropertyWithValue("erhaeltAnschreiben", true)
				.hasFieldOrPropertyWithValue("bemerkungen", "Testbeschreibung");
	}

	@Test
	@DisplayName("getById | Not found")
	void getByIdNotFoundTest() {
		when(conn.queryByKey(DTOSchuelerErzieherAdresse.class, 1L)).thenReturn(null);
		final var throwable = catchThrowable(() -> dataErzieherStammdaten.getById(11L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Erzieher mit ID 1 nicht gefunden");
	}

	@Test
	@DisplayName("map | Erzieher 1")
	void mapErzieher1Test() throws ApiOperationException {
		final DTOSchuelerErzieherAdresse dto = getDTOSchuelerErzieherAdresse1();

		assertThat(dataErzieherStammdaten.map((dto)))
				.isInstanceOf(ErzieherStammdaten.class)
				.hasFieldOrPropertyWithValue("id", 11L)
				.hasFieldOrPropertyWithValue("idSchueler", 1L)
				.hasFieldOrPropertyWithValue("idErzieherArt", 1L)
				.hasFieldOrPropertyWithValue("titel", "Dr.")
				.hasFieldOrPropertyWithValue("anrede", "Frau")
				.hasFieldOrPropertyWithValue("nachname", "Meier")
				.hasFieldOrPropertyWithValue("vorname", "Anna")
				.hasFieldOrPropertyWithValue("strassenname", "Hauptstr")
				.hasFieldOrPropertyWithValue("hausnummer", "10")
				.hasFieldOrPropertyWithValue("hausnummerZusatz", "b")
				.hasFieldOrPropertyWithValue("wohnortID", 3L)
				.hasFieldOrPropertyWithValue("ortsteilID", 4L)
				.hasFieldOrPropertyWithValue("eMail", "anna.meier@xyz.de")
				.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", null)
				.hasFieldOrPropertyWithValue("erhaeltAnschreiben", true)
				.hasFieldOrPropertyWithValue("bemerkungen", "Testbeschreibung");
	}

	@Test
	@DisplayName("map | Erzieher 2")
	void mapErzieher2Test() throws ApiOperationException {
		final DTOSchuelerErzieherAdresse dto = getDTOSchuelerErzieherAdresse2();

		assertThat(dataErzieherStammdaten.map((dto)))
				.isInstanceOf(ErzieherStammdaten.class)
				.hasFieldOrPropertyWithValue("id", 12L)
				.hasFieldOrPropertyWithValue("idSchueler", 1L)
				.hasFieldOrPropertyWithValue("idErzieherArt", 1L)
				.hasFieldOrPropertyWithValue("titel", "Dr.")
				.hasFieldOrPropertyWithValue("anrede", "Herr")
				.hasFieldOrPropertyWithValue("nachname", "Meier")
				.hasFieldOrPropertyWithValue("vorname", "Holger")
				.hasFieldOrPropertyWithValue("strassenname", "Hauptstr")
				.hasFieldOrPropertyWithValue("hausnummer", "10")
				.hasFieldOrPropertyWithValue("hausnummerZusatz", "b")
				.hasFieldOrPropertyWithValue("wohnortID", 3L)
				.hasFieldOrPropertyWithValue("ortsteilID", 4L)
				.hasFieldOrPropertyWithValue("eMail", "holger.meier@xyz.de")
				.hasFieldOrPropertyWithValue("staatsangehoerigkeitID", null)
				.hasFieldOrPropertyWithValue("erhaeltAnschreiben", true)
				.hasFieldOrPropertyWithValue("bemerkungen", "Testbeschreibung");
	}

	@Test
	@DisplayName("map | Not found")
	void mapNotFoundTest() {
		final DTOSchuelerErzieherAdresse dto = new DTOSchuelerErzieherAdresse(1L, 2L);
		dto.Name1 = "";
		dto.Name2 = "";

		final var throwable = catchThrowable(() -> dataErzieherStammdaten.map(dto));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Erzieher konnte nicht gemappt werden: weder Name1 noch Name2 sind befüllt.");
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolgreiches mapping Erzieher 1")
	@MethodSource("provideMappingAttributesErz1")
	void mapAttributeErzieher1Test(final String name, final Object value) {
		final var expectedDTO = getDTOSchuelerErzieherAdresse1();
		final Map<String, Object> map = new HashMap<>();
		final var throwable = catchThrowable(() -> this.dataErzieherStammdaten.mapAttribute(expectedDTO, name, value, map));

		switch (name) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "idSchueler" -> assertThat(expectedDTO.Schueler_ID).isEqualTo(value);
			case "idErzieherArt" -> assertThat(expectedDTO.ErzieherArt_ID).isEqualTo(value);
			case "titel" -> assertThat(expectedDTO.Titel1).isEqualTo(value);
			case "anrede" -> assertThat(expectedDTO.Anrede1).isEqualTo(value);
			case "nachname" -> assertThat(expectedDTO.Name1).isEqualTo(value);
			case "vorname" -> assertThat(expectedDTO.Vorname1).isEqualTo(value);
			case "strassenname" -> assertThat(expectedDTO.ErzStrassenname).isEqualTo(value);
			case "hausnummer" -> assertThat(expectedDTO.ErzHausNr).isEqualTo(value);
			case "hausnummerZusatz" -> assertThat(expectedDTO.ErzHausNrZusatz).isEqualTo(value);
			case "wohnortID" -> assertThat(expectedDTO.ErzOrt_ID).isEqualTo(value);
			case "ortsteilID" -> assertThat(expectedDTO.ErzOrtsteil_ID).isEqualTo(value);
			case "eMail" -> assertThat(expectedDTO.ErzEmail).isEqualTo(value);
			case "staatsangehoerigkeitID" -> assertThat(expectedDTO.Erz1StaatKrz).isEqualTo(value);
			case "erhaeltAnschreiben" -> assertThat(expectedDTO.ErzAnschreiben).isEqualTo(value);
			case "bemerkungen" -> assertThat(expectedDTO.Bemerkungen).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Unbekanntes Feld: " + name)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolgreiches mapping Erzieher 2")
	@MethodSource("provideMappingAttributesErz2")
	void mapAttributeErzieher2Test(final String name, final Object value) {
		final var expectedDTO = getDTOSchuelerErzieherAdresse2();
		final Map<String, Object> map = new HashMap<>();
		final var throwable = catchThrowable(() -> this.dataErzieherStammdaten.mapAttribute(expectedDTO, name, value, map));

		switch (name) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "idSchueler" -> assertThat(expectedDTO.Schueler_ID).isEqualTo(value);
			case "idErzieherArt" -> assertThat(expectedDTO.ErzieherArt_ID).isEqualTo(value);
			case "titel" -> assertThat(expectedDTO.Titel2).isEqualTo(value);
			case "anrede" -> assertThat(expectedDTO.Anrede2).isEqualTo(value);
			case "nachname" -> assertThat(expectedDTO.Name2).isEqualTo(value);
			case "vorname" -> assertThat(expectedDTO.Vorname2).isEqualTo(value);
			case "strassenname" -> assertThat(expectedDTO.ErzStrassenname).isEqualTo(value);
			case "hausnummer" -> assertThat(expectedDTO.ErzHausNr).isEqualTo(value);
			case "hausnummerZusatz" -> assertThat(expectedDTO.ErzHausNrZusatz).isEqualTo(value);
			case "wohnortID" -> assertThat(expectedDTO.ErzOrt_ID).isEqualTo(value);
			case "ortsteilID" -> assertThat(expectedDTO.ErzOrtsteil_ID).isEqualTo(value);
			case "eMail" -> assertThat(expectedDTO.ErzEmail2).isEqualTo(value);
			case "staatsangehoerigkeitID" -> assertThat(expectedDTO.Erz1StaatKrz).isEqualTo(value);
			case "erhaeltAnschreiben" -> assertThat(expectedDTO.ErzAnschreiben).isEqualTo(value);
			case "bemerkungen" -> assertThat(expectedDTO.Bemerkungen).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Unbekanntes Feld: " + name)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("deleteMultipleAsResponse | Volles Löschen")
	void deleteMultipleRemoveTest() throws ApiOperationException {
		final DTOSchuelerErzieherAdresse dto = new DTOSchuelerErzieherAdresse(1L, 2L);

		doReturn(List.of(dto)).when(conn).queryByKeyList(DTOSchuelerErzieherAdresse.class, List.of(1L));
		when(conn.transactionRemove(dto)).thenReturn(true);

		final Response resp = dataErzieherStammdaten.deleteMultipleAsResponse(List.of(11L));
		assertThat(resp.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

		verify(conn).transactionRemove(dto);
	}

	@Test
	@DisplayName("deleteMultipleAsResponse | Teilweises Löschen")
	void deleteMultipleFlushTest() throws ApiOperationException {
		final DTOSchuelerErzieherAdresse dto = new DTOSchuelerErzieherAdresse(1L, 2L);
		dto.Name1 = "Meier";
		dto.Name2 = "Meier";

		doReturn(List.of(dto)).when(conn).queryByKeyList(DTOSchuelerErzieherAdresse.class, List.of(1L));

		final Response resp = dataErzieherStammdaten.deleteMultipleAsResponse(List.of(12L));
		assertThat(resp.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

		@SuppressWarnings("unchecked")
		final List<ErzieherStammdaten> deleted = (List<ErzieherStammdaten>) resp.getEntity();
		assertThat(deleted).hasSize(1);
		assertThat(deleted.getFirst().id).isEqualTo(12L);

		verify(conn).transactionFlush();
	}

	@Test
	@DisplayName("deleteMultipleAsResponse | Löschen führt zu InternalServerError")
	void deleteMultipleRemoveFailTest() {
		final DTOSchuelerErzieherAdresse dto = new DTOSchuelerErzieherAdresse(1L, 2L);

		doReturn(List.of(dto)).when(conn).queryByKeyList(DTOSchuelerErzieherAdresse.class, List.of(1L));
		when(conn.transactionRemove(dto)).thenReturn(false);

		final var throwable = catchThrowable(() -> dataErzieherStammdaten.deleteMultipleAsResponse(List.of(11L)));
		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Fehler beim Entfernen der Entität.");
	}

	private static Stream<Arguments> provideMappingAttributesErz1() {
		return Stream.of(
				arguments("id", 1L),
				arguments("idSchueler", 1L),
				arguments("idErzieherArt", 1L),
				arguments("titel", "Dr."),
				arguments("anrede", "Frau"),
				arguments("nachname", "Meier"),
				arguments("vorname", "Anna"),
				arguments("strassenname", "Hauptstr"),
				arguments("hausnummer", "10"),
				arguments("hausnummerZusatz", "b"),
				arguments("wohnortID", 3L),
				arguments("ortsteilID", null),
				arguments("eMail", "anna.meier@xyz.de"),
				arguments("staatsangehoerigkeitID", null),
				arguments("erhaeltAnschreiben", true),
				arguments("bemerkungen", "Testbeschreibung")
		);
	}

	private static Stream<Arguments> provideMappingAttributesErz2() {
		return Stream.of(
				arguments("id", 1L),
				arguments("idSchueler", 1L),
				arguments("idErzieherArt", 1L),
				arguments("titel", "Dr."),
				arguments("anrede", "Herr"),
				arguments("nachname", "Meier"),
				arguments("vorname", "Holger"),
				arguments("strassenname", "Hauptstr"),
				arguments("hausnummer", "10"),
				arguments("hausnummerZusatz", "b"),
				arguments("wohnortID", 3L),
				arguments("ortsteilID", null),
				arguments("eMail", "holger.meier@xyz.de"),
				arguments("staatsangehoerigkeitID", null),
				arguments("erhaeltAnschreiben", true),
				arguments("bemerkungen", "Testbeschreibung")
		);
	}

	private DTOSchuelerErzieherAdresse getDTOSchuelerErzieherAdresse1() {
		final var dtoSchuelerErzieherAdresse = new DTOSchuelerErzieherAdresse(1L, 2L);
		dtoSchuelerErzieherAdresse.ID = 1L;
		dtoSchuelerErzieherAdresse.Schueler_ID = 1L;
		dtoSchuelerErzieherAdresse.ErzieherArt_ID = 1L;
		dtoSchuelerErzieherAdresse.Titel1 = "Dr.";
		dtoSchuelerErzieherAdresse.Anrede1 = "Frau";
		dtoSchuelerErzieherAdresse.Vorname1 = "Anna";
		dtoSchuelerErzieherAdresse.Name1 = "Meier";
		dtoSchuelerErzieherAdresse.ErzEmail = "anna.meier@xyz.de";
		dtoSchuelerErzieherAdresse.Erz1StaatKrz = null;
		dtoSchuelerErzieherAdresse.ErzStrassenname = "Hauptstr";
		dtoSchuelerErzieherAdresse.ErzHausNr = "10";
		dtoSchuelerErzieherAdresse.ErzHausNrZusatz = "b";
		dtoSchuelerErzieherAdresse.ErzOrt_ID = 3L;
		dtoSchuelerErzieherAdresse.ErzOrtsteil_ID = 4L;
		dtoSchuelerErzieherAdresse.ErzAnschreiben = true;
		dtoSchuelerErzieherAdresse.Bemerkungen = "Testbeschreibung";
		return dtoSchuelerErzieherAdresse;
	}

	private DTOSchuelerErzieherAdresse getDTOSchuelerErzieherAdresse2() {
		final var dtoSchuelerErzieherAdresse = new DTOSchuelerErzieherAdresse(1L, 2L);
		dtoSchuelerErzieherAdresse.ID = 1L;
		dtoSchuelerErzieherAdresse.Schueler_ID = 1L;
		dtoSchuelerErzieherAdresse.ErzieherArt_ID = 1L;
		dtoSchuelerErzieherAdresse.Titel2 = "Dr.";
		dtoSchuelerErzieherAdresse.Anrede2 = "Herr";
		dtoSchuelerErzieherAdresse.Vorname2 = "Holger";
		dtoSchuelerErzieherAdresse.Name2 = "Meier";
		dtoSchuelerErzieherAdresse.ErzEmail2 = "holger.meier@xyz.de";
		dtoSchuelerErzieherAdresse.Erz2StaatKrz = null;
		dtoSchuelerErzieherAdresse.ErzStrassenname = "Hauptstr";
		dtoSchuelerErzieherAdresse.ErzHausNr = "10";
		dtoSchuelerErzieherAdresse.ErzHausNrZusatz = "b";
		dtoSchuelerErzieherAdresse.ErzOrt_ID = 3L;
		dtoSchuelerErzieherAdresse.ErzOrtsteil_ID = 4L;
		dtoSchuelerErzieherAdresse.ErzAnschreiben = true;
		dtoSchuelerErzieherAdresse.Bemerkungen = "Testbeschreibung";
		return dtoSchuelerErzieherAdresse;
	}

}
