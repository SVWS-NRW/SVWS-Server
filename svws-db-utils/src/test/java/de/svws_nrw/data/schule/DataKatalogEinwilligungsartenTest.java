package de.svws_nrw.data.schule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
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
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataKatalogEinwilligungsarten")
@ExtendWith(MockitoExtension.class)
class DataKatalogEinwilligungsartenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKatalogEinwilligungsarten dataKatalogEinwilligungsarten;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		dataKatalogEinwilligungsarten = new DataKatalogEinwilligungsarten(conn);
		final DTOKatalogEinwilligungsart dto = getDTOKatalogEinwilligungsart();
		final long id = 1L;
		final Map<String, Object> initAttributes = new HashMap<>();

		dataKatalogEinwilligungsarten.initDTO(dto, id, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", id)
				.hasFieldOrPropertyWithValue("Bezeichnung", "")
				.hasFieldOrPropertyWithValue("Beschreibung", "")
				.hasFieldOrPropertyWithValue("Schluessel", "")
				.hasFieldOrPropertyWithValue("personTyp", PersonTyp.SCHUELER)
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);

	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final DTOKatalogEinwilligungsart dto = getDTOKatalogEinwilligungsart();

		assertThat(this.dataKatalogEinwilligungsarten.map(dto))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Testbezeichnung")
				.hasFieldOrPropertyWithValue("beschreibung", "Testbeschreibung")
				.hasFieldOrPropertyWithValue("schluessel", "Testschluessel")
				.hasFieldOrPropertyWithValue("personTyp", PersonTyp.SCHUELER.id)
				.hasFieldOrPropertyWithValue("sichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 0);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | some Values null")
	void mapTest_someValuesNull() {
		final DTOKatalogEinwilligungsart dto = getDTOKatalogEinwilligungsart();
		dto.Bezeichnung = null;
		dto.Beschreibung = null;
		dto.Schluessel = null;

		assertThat(this.dataKatalogEinwilligungsarten.map(dto))
				.isInstanceOf(Einwilligungsart.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "")
				.hasFieldOrPropertyWithValue("beschreibung", "")
				.hasFieldOrPropertyWithValue("schluessel", "")
				.hasFieldOrPropertyWithValue("personTyp", PersonTyp.SCHUELER.id)
				.hasFieldOrPropertyWithValue("sichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 0);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() throws ApiOperationException {
		final DTOKatalogEinwilligungsart dtoSchueler = getDTOKatalogEinwilligungsart();
		final DTOKatalogEinwilligungsart dtoLehrer = getDTOKatalogEinwilligungsart();
		dtoLehrer.ID = 2L;
		dtoLehrer.personTyp = PersonTyp.LEHRER;

		when(conn.queryAll(DTOKatalogEinwilligungsart.class)).thenReturn(List.of(dtoSchueler, dtoLehrer));

		final DTOSchuelerDatenschutz schueler = getDTOSchuelerDatenschutz();
		schueler.Datenschutz_ID = dtoSchueler.ID;
		when(conn.queryList((DTOSchuelerDatenschutz.QUERY_ALL.concat(" WHERE e.Datenschutz_ID IS NOT NULL")), (DTOSchuelerDatenschutz.class)))
				.thenReturn(List.of(schueler));

		final DTOLehrerDatenschutz lehrer = getDTOLehrerDatenschutz();
		lehrer.DatenschutzID = dtoLehrer.ID;
		when(conn.queryList((DTOLehrerDatenschutz.QUERY_ALL.concat(" WHERE e.DatenschutzID IS NOT NULL")), (DTOLehrerDatenschutz.class)))
				.thenReturn(List.of(lehrer, lehrer));

		final List<Einwilligungsart> result = dataKatalogEinwilligungsarten.getAll();
		final Einwilligungsart eaSchueler = result.stream().filter(ea -> ea.id == dtoSchueler.ID).findFirst().orElse(null);
		final Einwilligungsart eaLehrer = result.stream().filter(ea -> ea.id == dtoLehrer.ID).findFirst().orElse(null);

		assertThat(eaSchueler)
				.isNotNull()
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 1);
		assertThat(eaLehrer)
				.isNotNull()
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 2);
	}

	@Test
	@DisplayName("getById | Einwilligungsart null")
	void getByIdTest_notFound() {
		when(conn.queryByKey(DTOKatalogEinwilligungsart.class, 1L)).thenReturn(null);
		final var throwable = catchThrowable(() -> dataKatalogEinwilligungsarten.getById(1L));
		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Die Einwilligungsart mit der ID 1 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("getById | Schüler")
	void getByIdTest_Schueler() throws ApiOperationException {
		final DTOKatalogEinwilligungsart dto = getDTOKatalogEinwilligungsart();
		when(conn.queryByKey(DTOKatalogEinwilligungsart.class, dto.ID)).thenReturn(dto);
		final DTOSchuelerDatenschutz schueler1 = getDTOSchuelerDatenschutz();
		final DTOSchuelerDatenschutz schueler2 = getDTOSchuelerDatenschutz();
		when(conn.queryList((DTOSchuelerDatenschutz.QUERY_BY_DATENSCHUTZ_ID.replace("SELECT e ", "SELECT COUNT(e) ")),
				(DTOSchuelerDatenschutz.class), (dto.ID)))
				.thenReturn(List.of(schueler1, schueler2));

		assertThat(dataKatalogEinwilligungsarten.getById(dto.ID))
				.isNotNull()
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 2);
	}

	@Test
	@DisplayName("getById | Lehrer")
	void getByIdTest_Lehrer() throws ApiOperationException {
		final DTOKatalogEinwilligungsart dto = getDTOKatalogEinwilligungsart();
		dto.ID = 2L;
		dto.personTyp = PersonTyp.LEHRER;
		when(conn.queryByKey(DTOKatalogEinwilligungsart.class, dto.ID)).thenReturn(dto);
		final DTOLehrerDatenschutz lehrer1 = getDTOLehrerDatenschutz();
		final DTOLehrerDatenschutz lehrer2 = getDTOLehrerDatenschutz();
		when(conn.queryList((DTOLehrerDatenschutz.QUERY_BY_DATENSCHUTZID.replace("SELECT e ", "SELECT COUNT(e) ")),
				(DTOLehrerDatenschutz.class), (dto.ID)))
				.thenReturn(List.of(lehrer1, lehrer2));

		assertThat(dataKatalogEinwilligungsarten.getById(dto.ID))
				.isNotNull()
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 2);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOKatalogEinwilligungsart();
		final Map<String, Object> map = new HashMap<>();
		final var throwable = Assertions.catchThrowable(() -> this.dataKatalogEinwilligungsarten.mapAttribute(expectedDTO, key, value, map));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "bezeichnung" -> assertThat(expectedDTO.Bezeichnung).isEqualTo(value);
			case "beschreibung" -> assertThat(expectedDTO.Beschreibung).isEqualTo(value);
			case "schluessel" -> assertThat(expectedDTO.Schluessel).isEqualTo(value);
			case "personTyp" -> assertThat(expectedDTO.personTyp).isEqualTo(value);
			case "sichtbar" -> assertThat(expectedDTO.Sichtbar).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.Sortierung).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten ein unbekanntes Attribut.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 1L),
				arguments("bezeichnung", "Testbezeichnung"),
				arguments("beschreibung", "Testbeschreibung"),
				arguments("schluessel", "Testschluessel"),
				arguments("personTyp", PersonTyp.SCHUELER),
				arguments("sichtbar", true),
				arguments("sortierung", 32000)
		);
	}

	private DTOKatalogEinwilligungsart getDTOKatalogEinwilligungsart() {
		final var dtoKatalogEinwilligungsart = new DTOKatalogEinwilligungsart(1L, "Testbezeichnung", true, 32000);
		dtoKatalogEinwilligungsart.ID = 1L;
		dtoKatalogEinwilligungsart.Bezeichnung = "Testbezeichnung";
		dtoKatalogEinwilligungsart.Beschreibung = "Testbeschreibung";
		dtoKatalogEinwilligungsart.Schluessel = "Testschluessel";
		dtoKatalogEinwilligungsart.personTyp = PersonTyp.SCHUELER;
		dtoKatalogEinwilligungsart.Sichtbar = true;
		dtoKatalogEinwilligungsart.Sortierung = 32000;
		return dtoKatalogEinwilligungsart;
	}

	private DTOSchuelerDatenschutz getDTOSchuelerDatenschutz() {
		final var dtoSchuelerDatenschutz = new DTOSchuelerDatenschutz(1L, 1L, false, false);
		dtoSchuelerDatenschutz.Schueler_ID = 1L;
		dtoSchuelerDatenschutz.Datenschutz_ID = 1L;
		dtoSchuelerDatenschutz.Status = false;
		dtoSchuelerDatenschutz.Abgefragt = false;
		return dtoSchuelerDatenschutz;
	}

	private DTOLehrerDatenschutz getDTOLehrerDatenschutz() {
		final var dtoLehrerDatenschutz = new DTOLehrerDatenschutz(1L, 1L, false, false);
		dtoLehrerDatenschutz.LehrerID = 1L;
		dtoLehrerDatenschutz.DatenschutzID = 1L;
		dtoLehrerDatenschutz.Status = false;
		dtoLehrerDatenschutz.Abgefragt = false;
		return dtoLehrerDatenschutz;
	}
}
