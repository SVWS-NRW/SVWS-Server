package de.svws_nrw.data.jahrgaenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataJahrgangsdaten}
 */
@DisplayName("Diese Klasse testet die Klasse DataJahrgangsDaten")
@ExtendWith(MockitoExtension.class)
class DataJahrgangsdatenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataJahrgangsdaten dut;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final DTOJahrgang dtoJahrgang = new DTOJahrgang(0L);

		this.dut.initDTO(dtoJahrgang, 14L, null);

		assertThat(dtoJahrgang.ID).isEqualTo(14L);
	}

	@Test
	@DisplayName("getAll | 2 DTOJahrgang")
	void getAllTest() {
		when(this.conn.queryAll(DTOJahrgang.class)).thenReturn(List.of(new DTOJahrgang(1L), new DTOJahrgang(3L)));

		final List<JahrgangsDaten> result = this.dut.getAll();

		assertThat(result).hasSize(2)
				.hasOnlyElementsOfType(JahrgangsDaten.class);
	}

	@Test
	@DisplayName("getAll | Empty List")
	void getListAllTest() {
		when(this.conn.queryAll(DTOJahrgang.class)).thenReturn(Collections.emptyList());

		final List<JahrgangsDaten> result = this.dut.getAll();

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("GetById | Erfolg")
	void getByIdTestSuccess() throws ApiOperationException {
		final DTOJahrgang dtoJahrgang = getDTOJahrgang();
		when(this.conn.queryByKey(DTOJahrgang.class, dtoJahrgang.ID)).thenReturn(dtoJahrgang);

		assertThat(dut.getById(dtoJahrgang.ID))
				.isInstanceOf(JahrgangsDaten.class)
				.hasFieldOrPropertyWithValue("id", dtoJahrgang.ID);
	}

	@Test
	@DisplayName("GetById | Falsche ID")
	void getByIdTestFailed() {
		final Throwable throwable = catchThrowable(() -> dut.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Jahrgang zur ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("GetById | ID null")
	void getByIdTestIdNull() {
		final Throwable throwable = catchThrowable(() -> dut.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine ID für den Jahrgang übergeben.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("Map | Erfolgreich")
	void testMap() {
		final DTOJahrgang dtoJahrgang = getDTOJahrgang();

		assertThat(this.dut.map(dtoJahrgang))
				.isInstanceOf(JahrgangsDaten.class)
				.hasFieldOrPropertyWithValue("id", 0L)
				.hasFieldOrPropertyWithValue("kuerzel", "intern")
				.hasFieldOrPropertyWithValue("gueltigBis", 5L)
				.hasFieldOrPropertyWithValue("gueltigVon", 4L)
				.hasFieldOrPropertyWithValue("kuerzelStatistik", "ASD")
				.hasFieldOrPropertyWithValue("bezeichnung", "ASD")
				.hasFieldOrPropertyWithValue("istSichtbar", false)
				.hasFieldOrPropertyWithValue("sortierung", 10000)
				.hasFieldOrPropertyWithValue("kuerzelSchulgliederung", "SGL")
				.hasFieldOrPropertyWithValue("anzahlRestabschnitte", 3)
				.hasFieldOrPropertyWithValue("idFolgejahrgang", 1L);
	}

	@Test
	@DisplayName("Map | Sortierung = null, Sichtbar = null und nutzt Default-Werte")
	void testMapSortierungNullAndSichtbarNull() {
		final DTOJahrgang dtoJahrgang = getDTOJahrgang();
		dtoJahrgang.Sichtbar = null;
		dtoJahrgang.Sortierung = null;

		assertThat(this.dut.map(dtoJahrgang))
				.isInstanceOf(JahrgangsDaten.class)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void testMapAttribute(final String key, final Object value) throws ApiOperationException {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);
		switch (key) {
			case "kuerzelStatistik" -> {
				when(conn.getUser()).thenReturn(mock(Benutzer.class));
				when(conn.getUser().schuleGetSchuljahr()).thenReturn(2024);
			}
			case "kuerzelSchulgliederung" -> {
				when(conn.getUser()).thenReturn(mock(Benutzer.class));
				when(conn.getUser().schuleGetSchuljahr()).thenReturn(2022);
				when(conn.getUser().schuleGetSchulform()).thenReturn(Schulform.BK);
			}
			case "idFolgejahrgang" -> when(conn.queryByKey(DTOJahrgang.class, value)).thenReturn(new DTOJahrgang(10L));
		}

		this.dut.mapAttribute(expectedDTO, key, value, null);

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "kuerzel" -> assertThat(expectedDTO.InternKrz).isEqualTo(value);
			case "kuerzelStatistik" -> assertThat(expectedDTO.ASDJahrgang).isEqualTo(value);
			case "bezeichnung" -> assertThat(expectedDTO.ASDBezeichnung).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.Sortierung).isEqualTo(value);
			case "kuerzelSchulgliederung" -> assertThat(expectedDTO.GliederungKuerzel).isEqualTo(value);
			case "idFolgejahrgang" -> assertThat(expectedDTO.Folgejahrgang_ID).isEqualTo(value);
			case "anzahlRestabschnitte" -> assertThat(expectedDTO.AnzahlRestabschnitte).isEqualTo(value);
			case "istSichtbar" -> assertThat(expectedDTO.Sichtbar).isEqualTo(value);
			case "gueltigVon" -> assertThat(expectedDTO.GueltigVon).isEqualTo(value);
			case "gueltigBis" -> assertThat(expectedDTO.GueltigBis).isEqualTo(value);
		}
	}

	@Test
	@DisplayName("mapAttribute | mapping mit unbekannten Wert")
	void testMapAttributeUnknownValue() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(1L);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "wdqwbidqwbid", "wbdbqwbd", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut wdqwbidqwbid.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit KürzelStatistik = null")
	void testMapAttributeKuerzelStatistikIsNull() throws ApiOperationException {
		final DTOJahrgang expectedDTO = new DTOJahrgang(1L);

		this.dut.mapAttribute(expectedDTO, "kuerzelStatistik", null, null);

		assertThat(expectedDTO.ASDJahrgang).isNull();
		assertThat(expectedDTO.ASDBezeichnung).isNull();
	}

	@Test
	@DisplayName("mapAttribute | mapping mit falscher KürzelStatstik")
	void testMapAttributeFalseKuerzelStatistik() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(1L);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "kuerzelStatistik", "YK", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Jahrgang mit zum Küerzel YK gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit falscher ID")
	void testMapAttributeFalseID() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "id", 1L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Id 1 der PatchMap ist ungleich der id 2 vom Dto")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit KuerzelSchuldgliederung = null")
	void testMapAttributeKuerzelSchuldgliederungIsNull() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "kuerzelSchulgliederung", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Schulgliederung ist für die Schulform nicht gültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit falsches KuerzelSchuldgliederung ")
	void testMapAttributeFalseKuerzelSchuldgliederung() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "kuerzelSchulgliederung", "A50", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Kürzel für die Schulgliederung ist ungültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit KuerzelSchuldgliederung Schulform nicht vorhanden")
	void testMapAttributeFalseKuerzelSchuldgliederungSchulformNotFound() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);
		when(conn.getUser()).thenReturn(mock(Benutzer.class));
		when(conn.getUser().schuleGetSchuljahr()).thenReturn(2022);
		when(conn.getUser().schuleGetSchulform()).thenReturn(Schulform.GE);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "kuerzelSchulgliederung", "A01", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Schulgliederung ist für die Schulform nicht gültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit Folgejahrgang nicht gefunden")
	void testMapAttributeIdFolgejahrgangNotFound() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		when(conn.queryByKey(DTOJahrgang.class, 10L)).thenReturn(null);

		final Throwable throwable = catchThrowable(() -> this.dut.mapAttribute(expectedDTO, "idFolgejahrgang", 10, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Ein Folgejahrgang mit der ID 10 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit IdFolgejahrgang = null")
	void testMapAttributeIdFolgejahrgangIsNull() throws ApiOperationException {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		this.dut.mapAttribute(expectedDTO, "idFolgejahrgang", null, null);

		assertThat(expectedDTO.Folgejahrgang_ID).isNull();
	}

	@Test
	@DisplayName("DTOMapByKlassen | leere Liste von DTOKlassen")
	void testDTOMapByKlassenEmptyListDTOKlassen() {
		final List<DTOKlassen> dtoJahrgangList = new ArrayList<>();

		final Map<Long, DTOJahrgang> actual = DataJahrgangsdaten.getDTOMapByKlassen(conn, dtoJahrgangList);

		assertThat(actual).isEmpty();
	}

	@Test
	@DisplayName("DTOMapByKlassen | Liste von DTOKlassen")
	void testDTOMapByKlassenListDTOKlassen() {
		final List<DTOKlassen> dtoKlassen = new ArrayList<>();
		final DTOKlassen dtoKlasse = new DTOKlassen(1L, 1L, "5a");
		dtoKlasse.Jahrgang_ID = 10L;
		dtoKlassen.add(dtoKlasse);

		final List<DTOJahrgang> dtoJahrgangs = new ArrayList<>();
		final DTOJahrgang dtoJahrgang = new DTOJahrgang(dtoKlasse.Jahrgang_ID);
		dtoJahrgang.Folgejahrgang_ID = 11L;
		dtoJahrgangs.add(dtoJahrgang);

		final List<Long> idsJahrgaenge = new ArrayList<>();
		idsJahrgaenge.add(10L);

		when(conn.queryByKeyList(DTOJahrgang.class, idsJahrgaenge)).thenReturn(dtoJahrgangs);

		final Map<Long, DTOJahrgang> actual = DataJahrgangsdaten.getDTOMapByKlassen(conn, dtoKlassen);

		assertThat(actual.get(dtoKlasse.ID))
				.isNotNull()
				.satisfies(dto -> {
					assertThat(dto.ID).isEqualTo(dtoJahrgang.ID);
					assertThat(dto.Folgejahrgang_ID).isEqualTo(dtoJahrgang.Folgejahrgang_ID);
				});
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 2L),
				arguments("kuerzel", "kl"),
				arguments("kuerzelStatistik", "00"),
				arguments("bezeichnung", "bezeichnung"),
				arguments("sortierung", 10000),
				arguments("kuerzelSchulgliederung", "A01"),
				arguments("idFolgejahrgang", 10L),
				arguments("anzahlRestabschnitte", 3),
				arguments("istSichtbar", true),
				arguments("gueltigVon", 10L),
				arguments("gueltigBis", 12L)
		);
	}

	private DTOJahrgang getDTOJahrgang() {
		final DTOJahrgang dto = new DTOJahrgang(0L);
		dto.InternKrz = "intern";
		dto.GueltigVon = 4L;
		dto.GueltigBis = 5L;
		dto.ASDJahrgang = "ASD";
		dto.ASDBezeichnung = "ASD";
		dto.Sichtbar = false;
		dto.Sortierung = 10000;
		dto.GliederungKuerzel = "SGL";
		dto.AnzahlRestabschnitte = 3;
		dto.Folgejahrgang_ID = 1L;
		return dto;
	}
}
