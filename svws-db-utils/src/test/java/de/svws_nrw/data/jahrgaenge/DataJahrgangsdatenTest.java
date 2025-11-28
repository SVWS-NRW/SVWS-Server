package de.svws_nrw.data.jahrgaenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schule.BildungsstufeKatalogEintrag;
import de.svws_nrw.asd.types.schule.Bildungsstufe;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
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
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataJahrgangsdaten}
 */
@DisplayName("Diese Klasse testet die Klasse DataJahrgangsdaten")
@ExtendWith(MockitoExtension.class)
class DataJahrgangsdatenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataJahrgangsdaten data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final DTOJahrgang dtoJahrgang = new DTOJahrgang(0L);

		this.data.initDTO(dtoJahrgang, 14L, null);

		assertThat(dtoJahrgang.ID).isEqualTo(14L);
	}

	@Test
	@DisplayName("getAll | 2 DTOJahrgang")
	void getAllTest() {
		when(this.conn.queryAll(DTOJahrgang.class)).thenReturn(List.of(new DTOJahrgang(1L), new DTOJahrgang(3L)));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		final List<JahrgangsDaten> result = this.data.getAll();

		assertThat(result).hasSize(2)
				.hasOnlyElementsOfType(JahrgangsDaten.class);
	}

	@Test
	@DisplayName("getAll | Empty List")
	void getListAllTest() {
		when(this.conn.queryAll(DTOJahrgang.class)).thenReturn(Collections.emptyList());
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		final List<JahrgangsDaten> result = this.data.getAll();

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("GetById | Erfolg")
	void getByIdTestSuccess() throws ApiOperationException {
		final DTOJahrgang dtoJahrgang = getDTOJahrgang();
		when(this.conn.queryByKey(DTOJahrgang.class, dtoJahrgang.ID)).thenReturn(dtoJahrgang);

		assertThat(data.getById(dtoJahrgang.ID))
				.isInstanceOf(JahrgangsDaten.class)
				.hasFieldOrPropertyWithValue("id", dtoJahrgang.ID);
	}

	@Test
	@DisplayName("GetById | Falsche ID")
	void getByIdTestFailed() {
		final Throwable throwable = catchThrowable(() -> data.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Jahrgang zur ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("GetById | ID null")
	void getByIdTestIdNull() {
		final Throwable throwable = catchThrowable(() -> data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine ID für den Jahrgang übergeben.")
				.hasFieldOrPropertyWithValue("Status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("Map | Erfolgreich")
	void testMap() {
		final var dtoJahrgang = getDTOJahrgang();

		assertThat(this.data.map(dtoJahrgang))
				.isInstanceOf(JahrgangsDaten.class)
				.hasFieldOrPropertyWithValue("id", 0L)
				.hasFieldOrPropertyWithValue("kuerzel", "intern")
				.hasFieldOrPropertyWithValue("gueltigBis", 5L)
				.hasFieldOrPropertyWithValue("gueltigVon", 4L)
				.hasFieldOrPropertyWithValue("kuerzelStatistik", "ASD")
				.hasFieldOrPropertyWithValue("bezeichnung", "ASD")
				.hasFieldOrPropertyWithValue("kurzbezeichnung", "12")
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

		assertThat(this.data.map(dtoJahrgang))
				.isInstanceOf(JahrgangsDaten.class)
				.hasFieldOrPropertyWithValue("istSichtbar", false)
				.hasFieldOrPropertyWithValue("sortierung", 32000);
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
				arguments("gueltigBis", 12L),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void testMapAttribute(final String key, final Object value) {
		final var expectedDTO = new DTOJahrgang(2L);
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
			default -> {
				//
			}
		}

		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, key, value, null));

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
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | mapping mit unbekannten Wert")
	void testMapAttributeUnknownValue() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(1L);

		final Throwable throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "wdqwbidqwbid", "wbdbqwbd", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut wdqwbidqwbid.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit KürzelStatistik = null")
	void testMapAttributeKuerzelStatistikIsNull() {
		final var expectedDTO = new DTOJahrgang(1L);

		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "kuerzelStatistik", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein ASD-Jahrgang ausgewählt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit falscher KürzelStatstik")
	void testMapAttributeFalseKuerzelStatistik() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(1L);

		final Throwable throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "kuerzelStatistik", "YK", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Jahrgang mit dem Schlüssel YK gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit falscher ID")
	void testMapAttributeFalseID() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		final Throwable throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "id", 1L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID 1 des Patches ist null oder stimmt nicht mit der ID 2 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit KuerzelSchuldgliederung = null")
	void testMapAttributeKuerzelSchuldgliederungIsNull() throws ApiOperationException {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		this.data.mapAttribute(expectedDTO, "kuerzelSchulgliederung", null, null);

		assertThat(expectedDTO.GliederungKuerzel).isNull();
	}

	@Test
	@DisplayName("mapAttribute | mapping mit falsches KuerzelSchuldgliederung ")
	void testMapAttributeFalseKuerzelSchuldgliederung() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		final Throwable throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "kuerzelSchulgliederung", "A50", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Schulgliederung mit dem Schlüssel A50 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit KuerzelSchuldgliederung Schulform nicht vorhanden")
	void testMapAttributeFalseKuerzelSchuldgliederungSchulformNotFound() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);
		when(conn.getUser()).thenReturn(mock(Benutzer.class));
		when(conn.getUser().schuleGetSchuljahr()).thenReturn(2022);
		when(conn.getUser().schuleGetSchulform()).thenReturn(Schulform.GE);

		final Throwable throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "kuerzelSchulgliederung", "A01", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Schulgliederung ist für diese Schulform nicht gültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit Folgejahrgang nicht gefunden")
	void testMapAttributeIdFolgejahrgangNotFound() {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		when(conn.queryByKey(DTOJahrgang.class, 10L)).thenReturn(null);

		final Throwable throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "idFolgejahrgang", 10, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Ein Folgejahrgang mit der ID 10 wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("mapAttribute | mapping mit IdFolgejahrgang = null")
	void testMapAttributeIdFolgejahrgangIsNull() throws ApiOperationException {
		final DTOJahrgang expectedDTO = new DTOJahrgang(2L);

		this.data.mapAttribute(expectedDTO, "idFolgejahrgang", null, null);

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
		final var klasse = new DTOKlassen(1L, 1L, "5a");
		klasse.Jahrgang_ID = 10L;
		final var jahrgang = new DTOJahrgang(klasse.Jahrgang_ID);
		jahrgang.Folgejahrgang_ID = 11L;
		when(conn.queryByKeyList(DTOJahrgang.class, Set.of(10L))).thenReturn(List.of(jahrgang));

		final var result = DataJahrgangsdaten.getDTOMapByKlassen(conn, List.of(klasse));

		assertThat(result.get(klasse.ID))
				.isNotNull()
				.satisfies(dto -> {
					assertThat(dto.ID).isEqualTo(jahrgang.ID);
					assertThat(dto.Folgejahrgang_ID).isEqualTo(jahrgang.Folgejahrgang_ID);
				});
	}

	private DTOJahrgang getDTOJahrgang() {
		final var dto = new DTOJahrgang(0L);
		dto.InternKrz = "intern";
		dto.GueltigVon = 4L;
		dto.GueltigBis = 5L;
		dto.ASDJahrgang = "ASD";
		dto.ASDBezeichnung = "ASD";
		dto.Kurzbezeichnung = "12";
		dto.Sichtbar = false;
		dto.Sortierung = 10000;
		dto.GliederungKuerzel = "SGL";
		dto.AnzahlRestabschnitte = 3;
		dto.Folgejahrgang_ID = 1L;
		return dto;
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung bereits vorhanden")
	void mapAttributeTest_bezeichnungDoppeltVergeben() {
		final var dto = new  DTOJahrgang(0L);
		dto.ASDBezeichnung = "abc";
		when(this.conn.queryAll(DTOJahrgang.class)).thenReturn(List.of(dto));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOJahrgang(1L), "bezeichnung", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung unverändert")
	void mapAttributeTest_bezeichnungUnchanging() {
		final var dto = new DTOJahrgang(0L);
		dto.ASDBezeichnung = "abc";

		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "bezeichnung", "abc", null));

		verifyNoInteractions(this.conn);
		assertThat(dto.ASDBezeichnung).isEqualTo("abc");
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung null")
	void mapAttributeTest_bezeichnungNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOJahrgang(0L), "bezeichnung", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut bezeichnung: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung blank")
	void mapAttributeTest_bezeichnungBlank() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOJahrgang(0L), "bezeichnung", "", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzel bereits vorhanden")
	void mapAttributeTest_kuerzelDoppeltVergeben() {
		final var dto = new  DTOJahrgang(0L);
		dto.InternKrz = "abc";
		when(this.conn.queryAll(DTOJahrgang.class)).thenReturn(List.of(dto));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOJahrgang(1L), "kuerzel", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Kürzel ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzel unverändert")
	void mapAttributeTest_kuerzelUnchanging() {
		final var dto = new DTOJahrgang(0L);
		dto.InternKrz = "abc";

		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "kuerzel", "abc", null));

		verifyNoInteractions(this.conn);
		assertThat(dto.InternKrz).isEqualTo("abc");
	}

	@Test
	@DisplayName("mapAttribute | kuerzel null")
	void mapAttributeTest_kuerzelNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOJahrgang(0L), "kuerzel", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut kuerzel: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzel blank")
	void mapAttributeTest_kuerzelBlank() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOJahrgang(0L), "kuerzel", "", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut kuerzel: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idBildungsstufe is Null")
	void patchIdBildungsstufeIsNull() throws ApiOperationException {
		final var dto = new DTOJahrgang(1L);
		dto.Sekundarstufe = "beforePatch";
		when(this.conn.queryByKey(DTOJahrgang.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put("idBildungsstufe", null);

		this.data.patch(1L, map);

		assertThat(dto.Sekundarstufe).isNull();
	}

	@Test
	@DisplayName("patch | idBildungsstufe | wrong id")
	void idBildungsstufeWrongId() {
		when(this.conn.queryByKey(DTOJahrgang.class, 1L)).thenReturn(mock(DTOJahrgang.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idBildungsstufe", -1L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Bildungsstufe zur ID -1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idBildungsstufe")
	void patchIdBildungsstufe() throws ApiOperationException {
		final var dto = new DTOJahrgang(1L);
		dto.Sekundarstufe = "beforePatch";
		when(this.conn.queryByKey(DTOJahrgang.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));
		when(this.conn.getUser().schuleGetSchuljahr()).thenReturn(1);
		final BildungsstufeKatalogEintrag bildungsstufe = Bildungsstufe.data().getWerte().getFirst().historie().getFirst();

		this.data.patch(1L, Map.of("idBildungsstufe", bildungsstufe.id));

		assertThat(dto.Sekundarstufe)
				.isEqualTo(bildungsstufe.schluessel);
	}
}
