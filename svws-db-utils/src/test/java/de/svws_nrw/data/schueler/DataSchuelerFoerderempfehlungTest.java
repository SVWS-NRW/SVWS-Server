package de.svws_nrw.data.schueler;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


import de.svws_nrw.asd.data.schueler.SchuelerFoerderempfehlung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoerderempfehlung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
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
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSchuelerFoerderempfehlungTest {
	/**
	 * Class Under Test
	 */
	@InjectMocks
	DataSchuelerFoerderempfehlung cut;
	/**
	 * Mock für den DBEntityManager
	 */
	@Mock
	DBEntityManager conn;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	// 1. Tests für checkBeforeCreation

	@Test
	@DisplayName("checkBeforeCreation | erfolgreiche Validierung")
	void checkBeforeCreationTest_Success() {
		final var initAttributes = Map.<String, Object>of("idLernabschnitt", 100L);

		when(conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 100L))
				.thenReturn(Mockito.mock(DTOSchuelerLernabschnittsdaten.class));

		final var throwable = catchThrowable(() -> cut.checkBeforeCreation("new-id", initAttributes));
		assertThat(throwable).isNull();
	}

	@Test
	@DisplayName("checkBeforeCreation | Exception bei fehlendem abschnittID")
	void checkBeforeCreationTest_MissingAbschnittID() {
		final var initAttributes = Map.<String, Object>of("idKlasse", 200L);

		final var throwable = catchThrowable(() -> cut.checkBeforeCreation("new-id", initAttributes));
		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut idLernabschnitt: Der Wert null ist nicht erlaubt");
	}

	@Test
	@DisplayName("checkBeforeCreation | Exception bei nicht existierendem Abschnitt")
	void checkBeforeCreationTest_AbschnittNotFound() {
		final var initAttributes = Map.<String, Object>of("idLernabschnitt", 100L);

		when(conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 100L))
				.thenReturn(null);

		final var throwable = catchThrowable(() -> cut.checkBeforeCreation("new-id", initAttributes));
		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Schüler-Lernabschnitt mit der ID 100 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("checkBeforeCreation | erfolgreich mit optionalen IDs")
	void checkBeforeCreationTest_SuccessWithOptional() {
		final var initAttributes = Map.<String, Object>of(
				"idLernabschnitt", 100L,
				"idKlasse", 200L,
				"idLehrer", 300L
		);

		when(conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, 100L))
				.thenReturn(Mockito.mock(DTOSchuelerLernabschnittsdaten.class));

		final var throwable = catchThrowable(() -> cut.checkBeforeCreation("new-id", initAttributes));
		assertThat(throwable).isNull();
	}

	@Test
	@DisplayName("mapAttribute | Exception bei nicht existierender Klasse")
	void mapAttributeTest_KlasseNotFound() {
		final var dto = getDTOSchuelerFoerderempfehlung();

		when(conn.queryByKey(DTOKlassen.class, 200L))
				.thenReturn(null);

		final var throwable = catchThrowable(() -> cut.mapAttribute(dto, "idKlasse", 200L, null));
		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Klasse mit der ID 200 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Exception bei nicht existierendem Lehrer")
	void mapAttributeTest_LehrerNotFound() {
		final var dto = getDTOSchuelerFoerderempfehlung();

		when(conn.queryByKey(DTOLehrer.class, 300L))
				.thenReturn(null);

		final var throwable = catchThrowable(() -> cut.mapAttribute(dto, "idLehrer", 300L, null));
		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Lehrer mit der ID 300 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	// 2. Test für initDTO

	@Test
	@DisplayName("initDTO | setzt die Felder korrekt.")
	void initDTOTest() {
		final var dto = new DTOSchuelerFoerderempfehlung("abc", "2025-12-09");

		this.cut.initDTO(dto, "lnp", null);

		assertThat(dto).hasFieldOrPropertyWithValue("GU_ID", "lnp");
	}

	// 3. Tests für checkBeforePersist

	@Test
	@DisplayName("checkBeforePersist | erfolgreiche Validierung")
	void checkBeforePersistTest_Success() {
		final var dto = getDTOSchuelerFoerderempfehlung();
		dto.Abschnitt_ID = 100L; // Positiv

		final var throwable = catchThrowable(() -> cut.checkBeforePersist(dto, Map.of()));
		assertThat(throwable).isNull();
	}

	@Test
	@DisplayName("checkBeforePersist | Exception bei null abschnittID")
	void checkBeforePersistTest_NullAbschnittID() {
		final var dto = getDTOSchuelerFoerderempfehlung();
		dto.Abschnitt_ID = null;

		final var throwable = catchThrowable(() -> cut.checkBeforePersist(dto, Map.of()));
		assertThat(throwable).isNull(); // No validation in checkBeforePersist
	}

	@Test
	@DisplayName("checkBeforePersist | Exception bei negativer abschnittID")
	void checkBeforePersistTest_NegativeAbschnittID() {
		final var dto = getDTOSchuelerFoerderempfehlung();
		dto.Abschnitt_ID = -1L;

		final var throwable = catchThrowable(() -> cut.checkBeforePersist(dto, Map.of()));
		assertThat(throwable).isNull(); // No validation in checkBeforePersist
	}

	// 4. Test für map

	@Test
	@DisplayName("map | erfolgreiches Mapping : DTO -> CTO ")
	void mapTest() throws ApiOperationException {
		final DTOSchuelerFoerderempfehlung dto = getDTOSchuelerFoerderempfehlung();
		assertThat(this.cut.map(dto))
				.isInstanceOf(SchuelerFoerderempfehlung.class)
				.hasFieldOrPropertyWithValue("guid", "abc")
				.hasFieldOrPropertyWithValue("idLernabschnitt", 34L)
				.hasFieldOrPropertyWithValue("idKlasse", 123L)
				.hasFieldOrPropertyWithValue("idLehrer", 456L)
				.hasFieldOrPropertyWithValue("datumAngelegt", "2025-12-11")
				.hasFieldOrPropertyWithValue("datumLetzteAenderung", "2025-12-09")
				.hasFieldOrPropertyWithValue("diagnoseKompetenzenInhaltlichProzessbezogen", "Test Inhalt Prozessbezogene Kompetenzen")
				.hasFieldOrPropertyWithValue("diagnoseKompetenzenMethodisch", "Test Methodische Kompetenzen")
				.hasFieldOrPropertyWithValue("diagnoseLernUndArbeitsverhalten", "Test Lern und Arbeitsverhalten")
				.hasFieldOrPropertyWithValue("massnahmeKompetenzenInhaltlichProzessbezogen", "Test Maßnahmen Inhalt Prozessbezogen")
				.hasFieldOrPropertyWithValue("massnahmeKompetenzenMethodische", "Test Maßnahmen Methodisch")
				.hasFieldOrPropertyWithValue("massnahmeLernArbeitsverhalten", "Test Maßnahmen Lernverhalten")
				.hasFieldOrPropertyWithValue("verantwortlichkeitEltern", "Test Verantwortlichkeit Eltern")
				.hasFieldOrPropertyWithValue("verantwortlichkeitSchueler", "Test Verantwortlichkeit Schüler")
				.hasFieldOrPropertyWithValue("datumUmsetzungVon", "2025-01-01")
				.hasFieldOrPropertyWithValue("datumUmsetzungBis", "2025-12-31")
				.hasFieldOrPropertyWithValue("datumUeberpruefung", "2025-06-15")
				.hasFieldOrPropertyWithValue("datumNaechstesBeratungsgespraech", "2025-11-20")
				.hasFieldOrPropertyWithValue("eingabeFertig", false)
				.hasFieldOrPropertyWithValue("faecher", "DE,MA,EN")
				.hasFieldOrPropertyWithValue("abgeschlossen", true);
	}

	// 5. Tests für mapAttribute

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches Mapping CTO -> DTO")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = getDTOSchuelerFoerderempfehlung();

		// Mock DB queries for idKlasse and idLehrer
		if ("idKlasse".equals(key)) {
			when(conn.queryByKey(DTOKlassen.class, value))
					.thenReturn(Mockito.mock(DTOKlassen.class));
		} else if ("idLehrer".equals(key)) {
			when(conn.queryByKey(DTOLehrer.class, value))
					.thenReturn(Mockito.mock(DTOLehrer.class));
		}

		final var throwable = catchThrowable(() -> this.cut.mapAttribute(expectedDTO, key, value, null));
		assertThat(throwable).isNull(); // No exception expected

		switch (key) {
			case "idLernabschnitt" -> assertThat(expectedDTO.Abschnitt_ID).isEqualTo(value);
			case "idKlasse" -> assertThat(expectedDTO.Klassen_ID).isEqualTo(value);
			case "idLehrer" -> assertThat(expectedDTO.Lehrer_ID).isEqualTo(value);
			case "datumAngelegt" -> assertThat(expectedDTO.DatumAngelegt).isEqualTo(value);
			case "datumLetzteAenderung" -> assertThat(expectedDTO.DatumAenderungSchild).isEqualTo(value);
			case "diagnoseKompetenzenInhaltlichProzessbezogen" -> assertThat(expectedDTO.Inhaltl_Prozessbez_Komp).isEqualTo(value);
			case "diagnoseKompetenzenMethodisch" -> assertThat(expectedDTO.Methodische_Komp).isEqualTo(value);
			case "diagnoseLernUndArbeitsverhalten" -> assertThat(expectedDTO.Lern_Arbeitsverhalten).isEqualTo(value);
			case "massnahmeKompetenzenInhaltlichProzessbezogen" -> assertThat(expectedDTO.Massn_Inhaltl_Prozessbez_Komp).isEqualTo(value);
			case "massnahmeKompetenzenMethodische" -> assertThat(expectedDTO.Massn_Methodische_Komp).isEqualTo(value);
			case "massnahmeLernArbeitsverhalten" -> assertThat(expectedDTO.Massn_Lern_Arbeitsverhalten).isEqualTo(value);
			case "verantwortlichkeitEltern" -> assertThat(expectedDTO.Verantwortlichkeit_Eltern).isEqualTo(value);
			case "verantwortlichkeitSchueler" -> assertThat(expectedDTO.Verantwortlichkeit_Schueler).isEqualTo(value);
			case "datumUmsetzungVon" -> assertThat(expectedDTO.Zeitrahmen_von_Datum).isEqualTo(value);
			case "datumUmsetzungBis" -> assertThat(expectedDTO.Zeitrahmen_bis_Datum).isEqualTo(value);
			case "datumUeberpruefung" -> assertThat(expectedDTO.Ueberpruefung_Datum).isEqualTo(value);
			case "datumNaechstesBeratungsgespraech" -> assertThat(expectedDTO.Naechstes_Beratungsgespraech).isEqualTo(value);
			case "eingabeFertig" -> assertThat(expectedDTO.EingabeFertig).isEqualTo(value);
			case "faecher" -> assertThat(expectedDTO.Faecher).isEqualTo(value);
			case "abgeschlossen" -> assertThat(expectedDTO.Abgeschlossen).isEqualTo(value);
			default -> {
				final var dto = getDTOSchuelerFoerderempfehlung();
				final var throwable1 = catchThrowable(() -> this.cut.mapAttribute(dto, key, value, null));
				assertThat(throwable1)
						.isInstanceOf(ApiOperationException.class)
						.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(key))
						.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			}
		}
	}

	@Test
	@DisplayName("mapAttribute | unbekanntes Attribut wirft Exception")
	void mapAttributeTest_UnknownAttribute() {
		final var dto = getDTOSchuelerFoerderempfehlung();
		final var throwable = catchThrowable(() -> this.cut.mapAttribute(dto, "unknownKey", "value", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknownKey.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	// 6. Tests für getById

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("getById | ID darf nicht leer oder null sein")
	void getByIDTest_InvalidId(final String invalidId) {
		final var throwable = Assertions.catchThrowable(() -> this.cut.getById(invalidId));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die GUID darf nicht null oder leer sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}


	@Test
	@DisplayName("getByID | DTO mit ID in DB vorhanden ")
	void getByIDTest_Found() throws ApiOperationException {
		final DTOSchuelerFoerderempfehlung dto = getDTOSchuelerFoerderempfehlung();

		when(conn.queryByKey(DTOSchuelerFoerderempfehlung.class, "abc")).thenReturn(dto);

		assertThat(cut.getById(dto.GU_ID))
				.isNotNull()
				.isInstanceOf(SchuelerFoerderempfehlung.class)
				.hasFieldOrPropertyWithValue("guid", "abc")
				.hasFieldOrPropertyWithValue("idLernabschnitt", 34L)
				.hasFieldOrPropertyWithValue("idKlasse", 123L)
				.hasFieldOrPropertyWithValue("idLehrer", 456L)
				.hasFieldOrPropertyWithValue("datumAngelegt", "2025-12-11")
				.hasFieldOrPropertyWithValue("datumLetzteAenderung", "2025-12-09")
				.hasFieldOrPropertyWithValue("diagnoseKompetenzenInhaltlichProzessbezogen", "Test Inhalt Prozessbezogene Kompetenzen")
				.hasFieldOrPropertyWithValue("diagnoseKompetenzenMethodisch", "Test Methodische Kompetenzen")
				.hasFieldOrPropertyWithValue("diagnoseLernUndArbeitsverhalten", "Test Lern und Arbeitsverhalten")
				.hasFieldOrPropertyWithValue("massnahmeKompetenzenInhaltlichProzessbezogen", "Test Maßnahmen Inhalt Prozessbezogen")
				.hasFieldOrPropertyWithValue("massnahmeKompetenzenMethodische", "Test Maßnahmen Methodisch")
				.hasFieldOrPropertyWithValue("massnahmeLernArbeitsverhalten", "Test Maßnahmen Lernverhalten")
				.hasFieldOrPropertyWithValue("verantwortlichkeitEltern", "Test Verantwortlichkeit Eltern")
				.hasFieldOrPropertyWithValue("verantwortlichkeitSchueler", "Test Verantwortlichkeit Schüler")
				.hasFieldOrPropertyWithValue("datumUmsetzungVon", "2025-01-01")
				.hasFieldOrPropertyWithValue("datumUmsetzungBis", "2025-12-31")
				.hasFieldOrPropertyWithValue("datumUeberpruefung", "2025-06-15")
				.hasFieldOrPropertyWithValue("datumNaechstesBeratungsgespraech", "2025-11-20")
				.hasFieldOrPropertyWithValue("eingabeFertig", false)
				.hasFieldOrPropertyWithValue("faecher", "DE,MA,EN")
				.hasFieldOrPropertyWithValue("abgeschlossen", true);
	}

	@Test
	@DisplayName("getByID | DTO mit ID nicht in DB vorhanden")
	void getByIDTest_NotFound() {
		when(conn.queryByKey(DTOSchuelerFoerderempfehlung.class, "abc")).thenReturn(null);
		final var throwable = catchThrowable(() -> this.cut.getById("abc"));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Förderempfehlung mit der GUID abc gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	// 7. Tests für getByLernabschnittsdatenIdAsResponse

	@Test
	@DisplayName("getListByLernabschnittsdatenIdAsResponse | erfolgreiche Abfrage mit DTOs")
	void getListByLernabschnittsdatenIdAsResponseTest_Success() throws ApiOperationException {
		final var dto1 = getDTOSchuelerFoerderempfehlung();
		final var dto2 = getDTOSchuelerFoerderempfehlung();
		dto2.GU_ID = "different-id";

		when(conn.queryList(DTOSchuelerFoerderempfehlung.QUERY_BY_ABSCHNITT_ID, DTOSchuelerFoerderempfehlung.class, 123L))
				.thenReturn(List.of(dto1, dto2));

		final var response = cut.getListByLernabschnittsdatenIdAsResponse(conn, 123L);

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getEntity()).isInstanceOf(List.class);
		@SuppressWarnings("unchecked")
		final var list = (List<SchuelerFoerderempfehlung>) response.getEntity();
		assertThat(list).hasSize(2);
		assertThat(list.get(0).guid).isEqualTo("abc");
		assertThat(list.get(1).guid).isEqualTo("different-id");
	}

	@Test
	@DisplayName("getByLernabschnittsdatenIdAsResponse | leere Liste")
	void getByLernabschnittsdatenIdAsResponseTest_Empty() throws ApiOperationException {
		when(conn.queryList(DTOSchuelerFoerderempfehlung.QUERY_BY_ABSCHNITT_ID, DTOSchuelerFoerderempfehlung.class, 456L))
				.thenReturn(List.of());

		final var response = cut.getListByLernabschnittsdatenIdAsResponse(conn, 456L);

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getEntity()).isInstanceOf(List.class);
		@SuppressWarnings("unchecked")
		final var list = (List<SchuelerFoerderempfehlung>) response.getEntity();
		assertThat(list).isEmpty();
	}

	static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("idLernabschnitt", 100L),
				arguments("idKlasse", 200L),
				arguments("idLehrer", 300L),
				arguments("datumAngelegt", "2025-01-01"),
				arguments("datumLetzteAenderung", "2025-01-02"),
				arguments("diagnoseKompetenzenInhaltlichProzessbezogen", "Test Inhalt"),
				arguments("diagnoseKompetenzenMethodisch", "Test Methodisch"),
				arguments("diagnoseLernUndArbeitsverhalten", "Test Lernverhalten"),
				arguments("massnahmeKompetenzenInhaltlichProzessbezogen", "Test Maßnahme Inhalt"),
				arguments("massnahmeKompetenzenMethodische", "Test Maßnahme Methodisch"),
				arguments("massnahmeLernArbeitsverhalten", "Test Maßnahme Lernverhalten"),
				arguments("verantwortlichkeitEltern", "Test Eltern"),
				arguments("verantwortlichkeitSchueler", "Test Schüler"),
				arguments("datumUmsetzungVon", "2025-02-01"),
				arguments("datumUmsetzungBis", "2025-02-28"),
				arguments("datumUeberpruefung", "2025-03-01"),
				arguments("datumNaechstesBeratungsgespraech", "2025-03-15"),
				arguments("eingabeFertig", true),
				arguments("faecher", "DE,EN"),
				arguments("abgeschlossen", false)
		);
	}

	private DTOSchuelerFoerderempfehlung getDTOSchuelerFoerderempfehlung() {
		final var dto = new DTOSchuelerFoerderempfehlung("abc", "2025-12-11");
		dto.GU_ID = "abc";
		dto.DatumAngelegt = "2025-12-11";
		dto.Abschnitt_ID = 34L;
		dto.DatumAenderungSchild = "2025-12-08";
		dto.Klassen_ID = 123L;
		dto.Lehrer_ID = 456L;
		dto.DatumAenderungSchildWeb = "2025-12-09";
		dto.Inhaltl_Prozessbez_Komp = "Test Inhalt Prozessbezogene Kompetenzen";
		dto.Methodische_Komp = "Test Methodische Kompetenzen";
		dto.Lern_Arbeitsverhalten = "Test Lern und Arbeitsverhalten";
		dto.Massn_Inhaltl_Prozessbez_Komp = "Test Maßnahmen Inhalt Prozessbezogen";
		dto.Massn_Methodische_Komp = "Test Maßnahmen Methodisch";
		dto.Massn_Lern_Arbeitsverhalten = "Test Maßnahmen Lernverhalten";
		dto.Verantwortlichkeit_Eltern = "Test Verantwortlichkeit Eltern";
		dto.Verantwortlichkeit_Schueler = "Test Verantwortlichkeit Schüler";
		dto.Zeitrahmen_von_Datum = "2025-01-01";
		dto.Zeitrahmen_bis_Datum = "2025-12-31";
		dto.Ueberpruefung_Datum = "2025-06-15";
		dto.Naechstes_Beratungsgespraech = "2025-11-20";
		dto.EingabeFertig = false;
		dto.Faecher = "DE,MA,EN";
		dto.Abgeschlossen = true;
		return dto;
	}
}
