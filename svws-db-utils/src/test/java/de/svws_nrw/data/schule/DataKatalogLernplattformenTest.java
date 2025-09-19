package de.svws_nrw.data.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
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

@DisplayName("Diese Testklasse testet die Klasse DataKatalogLernplattformen")
@ExtendWith(MockitoExtension.class)
class DataKatalogLernplattformenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKatalogLernplattformen dataKatalogLernplattformen;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt")
	void initDTOTest() {
		dataKatalogLernplattformen = new DataKatalogLernplattformen(conn);
		final DTOLernplattformen dto = getDTOLernplattform();
		final long id = 1L;
		final Map<String, Object> initAttributes = new HashMap<>();

		dataKatalogLernplattformen.initDTO(dto, id, initAttributes);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", id)
				.hasFieldOrPropertyWithValue("Bezeichnung", "")
				.hasFieldOrPropertyWithValue("BenutzernameSuffixLehrer", "")
				.hasFieldOrPropertyWithValue("BenutzernameSuffixErzieher", "")
				.hasFieldOrPropertyWithValue("BenutzernameSuffixSchueler", "")
				.hasFieldOrPropertyWithValue("Konfiguration", "");

	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | check Basic Attributes")
	void mapTest() {
		final DTOLernplattformen dto = getDTOLernplattform();

		assertThat(this.dataKatalogLernplattformen.map(dto))
				.isInstanceOf(Lernplattform.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Testbezeichnung")
				.hasFieldOrPropertyWithValue("benutzernameSuffixLehrer", "")
				.hasFieldOrPropertyWithValue("benutzernameSuffixErzieher", "")
				.hasFieldOrPropertyWithValue("benutzernameSuffixSchueler", "")
				.hasFieldOrPropertyWithValue("konfiguration", "")
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 0);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() throws ApiOperationException {
		final DTOLernplattformen dto1 = getDTOLernplattform();
		final DTOLernplattformen dto2 = getDTOLernplattform();
		dto2.ID = 2L;
		dto2.Bezeichnung = "Testbezeichnung2";

		final List<DTOLernplattformen> dtoList = new ArrayList<>();
		dtoList.add(dto1);
		dtoList.add(dto2);

		when(conn.queryAll(DTOLernplattformen.class)).thenReturn(dtoList);

		final List<Lernplattform> result = dataKatalogLernplattformen.getAll();
		final Lernplattform expectedDto1 = result.stream().filter(lFirst -> lFirst.id == dto1.ID).findFirst().orElse(null);
		final Lernplattform expectedDto2 = result.stream().filter(lSecond -> lSecond.id == dto2.ID).findFirst().orElse(null);

		assertThat(expectedDto1)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", dto1.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto1.Bezeichnung)
				.hasFieldOrPropertyWithValue("benutzernameSuffixLehrer", dto1.BenutzernameSuffixLehrer)
				.hasFieldOrPropertyWithValue("benutzernameSuffixErzieher", dto1.BenutzernameSuffixErzieher)
				.hasFieldOrPropertyWithValue("benutzernameSuffixSchueler", dto1.BenutzernameSuffixSchueler)
				.hasFieldOrPropertyWithValue("konfiguration", dto1.Konfiguration)
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 0);

		assertThat(expectedDto2)
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", dto2.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto2.Bezeichnung)
				.hasFieldOrPropertyWithValue("benutzernameSuffixLehrer", dto2.BenutzernameSuffixLehrer)
				.hasFieldOrPropertyWithValue("benutzernameSuffixErzieher", dto2.BenutzernameSuffixErzieher)
				.hasFieldOrPropertyWithValue("benutzernameSuffixSchueler", dto2.BenutzernameSuffixSchueler)
				.hasFieldOrPropertyWithValue("konfiguration", dto2.Konfiguration)
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 0);
	}

	@Test
	@DisplayName("getAllAnzahlEinwilligungen | Erfolg")
	void getAllTest_anzahlEinwilligungen() throws ApiOperationException {
		final DTOLernplattformen dtoSchueler =  getDTOLernplattform();
		final DTOLernplattformen dtoLehrer = getDTOLernplattform();
		dtoLehrer.ID = 2L;

		when(conn.queryAll(DTOLernplattformen.class)).thenReturn(List.of(dtoSchueler, dtoLehrer));

		final DTOSchuelerLernplattform schueler = new DTOSchuelerLernplattform(1L, 1L, false, false, false, false);
		schueler.LernplattformID = dtoSchueler.ID;
		when(conn.queryList((DTOSchuelerLernplattform.QUERY_ALL.concat(" WHERE e.LernplattformID IS NOT NULL")), (DTOSchuelerLernplattform.class)))
				.thenReturn(List.of(schueler));

		final DTOLehrerLernplattform lehrer = new DTOLehrerLernplattform(1L, 1L, false, false, false, false);
		lehrer.LernplattformID = dtoLehrer.ID;
		when(conn.queryList((DTOLehrerLernplattform.QUERY_ALL.concat(" WHERE e.LernplattformID IS NOT NULL")), (DTOLehrerLernplattform.class)))
				.thenReturn(List.of(lehrer, lehrer));

		final List<Lernplattform> result = dataKatalogLernplattformen.getAll();
		final Lernplattform eaSchueler = result.stream().filter(ea -> ea.id == dtoSchueler.ID).findFirst().orElse(null);
		final Lernplattform eaLehrer = result.stream().filter(ea -> ea.id == dtoLehrer.ID).findFirst().orElse(null);

		assertThat(eaSchueler)
				.isNotNull()
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 1);
		assertThat(eaLehrer)
				.isNotNull()
				.hasFieldOrPropertyWithValue("anzahlEinwilligungen", 2);
	}

	@Test
	@DisplayName("getById | Lernplattform null")
	void getByIdTest_notFound() {
		when(conn.queryByKey(DTOLernplattformen.class, 1L)).thenReturn(null);
		final var throwable = catchThrowable(() -> dataKatalogLernplattformen.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Die Lernplattform mit der ID 1 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("getById")
	void getByIdTest() throws ApiOperationException {
		final DTOLernplattformen dto = getDTOLernplattform();
		when(conn.queryByKey(DTOLernplattformen.class, dto.ID)).thenReturn(dto);

		assertThat(dataKatalogLernplattformen.getById(dto.ID))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "Testbezeichnung")
				.hasFieldOrPropertyWithValue("benutzernameSuffixLehrer", "")
				.hasFieldOrPropertyWithValue("benutzernameSuffixErzieher", "")
				.hasFieldOrPropertyWithValue("benutzernameSuffixSchueler", "")
				.hasFieldOrPropertyWithValue("konfiguration", "");
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOLernplattform();
		final Map<String, Object> map = new HashMap<>();
		final var throwable = Assertions.catchThrowable(() -> this.dataKatalogLernplattformen.mapAttribute(expectedDTO, key, value, map));

		switch (key) {
			case "id" -> assertThat(expectedDTO.ID).isEqualTo(value);
			case "bezeichnung" -> assertThat(expectedDTO.Bezeichnung).isEqualTo(value);
			case "benutzernameSuffixLehrer" -> assertThat(expectedDTO.BenutzernameSuffixLehrer).isEqualTo(value);
			case "benutzernameSuffixErzieher" -> assertThat(expectedDTO.BenutzernameSuffixErzieher).isEqualTo(value);
			case "benutzernameSuffixSchueler" -> assertThat(expectedDTO.BenutzernameSuffixSchueler).isEqualTo(value);
			case "konfiguration" -> assertThat(expectedDTO.Konfiguration).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut beschreibung.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 1L),
				arguments("beschreibung", "Testbeschreibung"),
				arguments("benutzernameSuffixLehrer", ""),
				arguments("benutzernameSuffixErzieher", ""),
				arguments("benutzernameSuffixSchueler", ""),
				arguments("konfiguration", "")
		);
	}

	private DTOLernplattformen getDTOLernplattform() {
		final var dtoKatalogLernplattform = new DTOLernplattformen(1L, "Testbezeichnung");
		dtoKatalogLernplattform.ID = 1L;
		dtoKatalogLernplattform.Bezeichnung = "Testbezeichnung";
		dtoKatalogLernplattform.BenutzernameSuffixLehrer = "";
		dtoKatalogLernplattform.BenutzernameSuffixErzieher = "";
		dtoKatalogLernplattform.BenutzernameSuffixSchueler = "";
		dtoKatalogLernplattform.Konfiguration = "";
		return dtoKatalogLernplattform;
	}
}
