package de.svws_nrw.data.faecher;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
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
import jakarta.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataFachdaten}.
 */
@DisplayName("Diese Klasse testet die Klasse DataFachdaten")
@ExtendWith(MockitoExtension.class)
class DataFachdatenTest {


	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataFachdaten dataFachdaten;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final var fachdaten = new DTOFach(1L, true);

		this.dataFachdaten.initDTO(fachdaten, 2L, null);

		assertThat(fachdaten.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dtoFach = getDtoFach();
		when(this.conn.queryByKey(DTOFach.class, dtoFach.ID)).thenReturn(dtoFach);

		assertThat(dataFachdaten.getById(dtoFach.ID))
				.isInstanceOf(FachDaten.class)
				.hasFieldOrPropertyWithValue("id", dtoFach.ID);
	}

	@Test
	@DisplayName("getById | wrong Id")
	void getByIdTest_wrongId() {
		when(this.conn.queryByKey(any(), any())).thenReturn(null);

		final var throwable = catchThrowable(() -> dataFachdaten.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine FachDaten mit der ID 1 gefunden")
				.hasFieldOrPropertyWithValue("Status", Response.Status.NOT_FOUND);
	}


	@Test
	@DisplayName("map | erfolgreiches mapping")
	void mapTest() throws ApiOperationException {
		final var dtoFach = getDtoFach();

		assertThat(this.dataFachdaten.map(dtoFach))
				.isInstanceOf(FachDaten.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "kurz")
				.hasFieldOrPropertyWithValue("kuerzelStatistik", "B")
				.hasFieldOrPropertyWithValue("bezeichnung", "lang")
				.hasFieldOrPropertyWithValue("sortierung", 1)
				.hasFieldOrPropertyWithValue("istOberstufenFach", true)
				.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", true)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("aufgabenfeld", "wertvolle Aufgaben")
				.hasFieldOrPropertyWithValue("bilingualeSprache", "sanskrit")
				.hasFieldOrPropertyWithValue("istNachpruefungErlaubt", true)
				.hasFieldOrPropertyWithValue("aufZeugnis", true)
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "super")
				.hasFieldOrPropertyWithValue("bezeichnungUeberweisungszeugnis", "nicht ganz so super")
				.hasFieldOrPropertyWithValue("maxZeichenInFachbemerkungen", 42)
				.hasFieldOrPropertyWithValue("istSchriftlichZK", true)
				.hasFieldOrPropertyWithValue("istSchriftlichBA", true)
				.hasFieldOrPropertyWithValue("istFHRFach", true)
				.hasFieldOrPropertyWithValue("holeAusAltenLernabschnitten", true);
	}

	@Test
	@DisplayName("map | erfolgreiches mapping, einige Werte bewusst null")
	void mapTest_someValuesNull() throws ApiOperationException {
		final var dtoFach = getDtoFach();
		dtoFach.Kuerzel = null;
		dtoFach.Bezeichnung = null;
		dtoFach.BezeichnungZeugnis = null;
		dtoFach.BezeichnungUeberweisungsZeugnis = null;
		dtoFach.MaxBemZeichen = null;
		dtoFach.GewichtungFHR = null;

		assertThat(this.dataFachdaten.map(dtoFach))
				.isInstanceOf(FachDaten.class)
				.hasFieldOrPropertyWithValue("kuerzel", "")
				.hasFieldOrPropertyWithValue("bezeichnung", "")
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "")
				.hasFieldOrPropertyWithValue("bezeichnungUeberweisungszeugnis", "")
				.hasFieldOrPropertyWithValue("maxZeichenInFachbemerkungen", Integer.MAX_VALUE)
				.hasFieldOrPropertyWithValue("istFHRFach", false);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = new DTOFach(1L, true);

		final var throwable = catchThrowable(() -> this.dataFachdaten.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "kuerzel" -> assertThat(expectedDTO.Kuerzel).isEqualTo(value);
			case "kuerzelStatistik" -> assertThat(expectedDTO.StatistikKuerzel).isEqualTo(value);
			case "bezeichnung" -> assertThat(expectedDTO.Bezeichnung).isEqualTo(value);
			case "istPruefungsordnungsRelevant" -> assertThat(expectedDTO.IstPruefungsordnungsRelevant).isEqualTo(value);
			case "istOberstufenFach" -> assertThat(expectedDTO.IstOberstufenFach).isEqualTo(value);
			case "sortierung" -> assertThat(expectedDTO.SortierungAllg).isEqualTo(value);
			case "istSichtbar" -> assertThat(expectedDTO.Sichtbar).isEqualTo(value);
			case "aufgabenfeld" -> assertThat(expectedDTO.Aufgabenfeld).isEqualTo(value);
			case "bilingualeSprache" -> assertThat(expectedDTO.Unterrichtssprache).isEqualTo(value);
			case "istNachpruefungErlaubt" -> assertThat(expectedDTO.IstNachpruefungErlaubt).isEqualTo(value);
			case "aufZeugnis" -> assertThat(expectedDTO.AufZeugnis).isEqualTo(value);
			case "bezeichnungZeugnis" -> assertThat(expectedDTO.BezeichnungZeugnis).isEqualTo(value);
			case "bezeichnungUeberweisungszeugnis" -> assertThat(expectedDTO.BezeichnungUeberweisungsZeugnis).isEqualTo(value);
			case "maxZeichenInFachbemerkungen" -> assertThat(expectedDTO.MaxBemZeichen).isEqualTo(value);
			case "istSchriftlichZK" -> assertThat(expectedDTO.IstSchriftlichZK).isEqualTo(value);
			case "istSchriftlichBA" -> assertThat(expectedDTO.IstSchriftlichBA).isEqualTo(value);
			case "holeAusAltenLernabschnitten" -> assertThat(expectedDTO.AbgeschlFaecherHolen).isEqualTo(value);
			case "istFHRFach" -> assertThat(expectedDTO.GewichtungFHR).isEqualTo(1);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | wrong kuerzelStatistik")
	void mapAttributeTest_WrongKuerzelStatistik() {
		final var expectedDTO = new DTOFach(1L, true);

		final var throwable = catchThrowable(() -> this.dataFachdaten.mapAttribute(expectedDTO, "kuerzelStatistik", "ZZ", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Ein Fach mit dem Kuerzel ZZ wurde nicht gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getMapFachdatenFromDTOFachList | success")
	void getMapFachdatenFromDTOFachListTest() throws ApiOperationException {
		final var dtoFach1 = getDtoFach();
		final var dtoFach2 = getDtoFach();
		dtoFach2.ID = 2L;
		final var dtoFach3 = getDtoFach();
		dtoFach3.ID = 3L;
		final var dtoFacher = List.of(dtoFach1, dtoFach2, dtoFach3);

		final var result = this.dataFachdaten.getMapFachdatenFromDTOFachList(dtoFacher);

		assertThat(result).hasSize(3);
		assertThat(result.get(1L)).isInstanceOf(FachDaten.class);
	}

	@Test
	@DisplayName("getMapFachdatenFromDTOFachList | input is Null")
	void getMapFachdatenFromDTOFachListTest_inputNull() throws ApiOperationException {
		final var result = this.dataFachdaten.getMapFachdatenFromDTOFachList(null);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("getMapFachdatenFromDTOFachList | emptyList")
	void getMapFachdatenFromDTOFachListTest_emptyList() throws ApiOperationException {
		final var result = this.dataFachdaten.getMapFachdatenFromDTOFachList(Collections.emptyList());

		assertThat(result).isEmpty();
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("kuerzel", "abc"),
				arguments("kuerzelStatistik", "AB"),
				arguments("bezeichnung", "wunderbar alles klar"),
				arguments("istPruefungsordnungsRelevant", true),
				arguments("istOberstufenFach", true),
				arguments("sortierung", 1337),
				arguments("istSichtbar", true),
				arguments("aufgabenfeld", "XO"),
				arguments("bilingualeSprache", "A"),
				arguments("istNachpruefungErlaubt", true),
				arguments("aufZeugnis", true),
				arguments("bezeichnungZeugnis", "gutes Zeugnis"),
				arguments("bezeichnungUeberweisungszeugnis", "sehr gutes Zeugnis"),
				arguments("maxZeichenInFachbemerkungen", 101010),
				arguments("istSchriftlichZK", true),
				arguments("istSchriftlichBA", true),
				arguments("holeAusAltenLernabschnitten", true),
				arguments("istFHRFach", true),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	private static DTOFach getDtoFach() {
		final var dtoFach = new DTOFach(1L, true);
		dtoFach.Kuerzel = "kurz";
		dtoFach.StatistikKuerzel = "B";
		dtoFach.Bezeichnung = "lang";
		dtoFach.SortierungAllg = 1;
		dtoFach.IstOberstufenFach = true;
		dtoFach.Sichtbar = true;
		dtoFach.Aufgabenfeld = "wertvolle Aufgaben";
		dtoFach.Unterrichtssprache = "sanskrit";
		dtoFach.IstNachpruefungErlaubt = true;
		dtoFach.AufZeugnis = true;
		dtoFach.BezeichnungZeugnis = "super";
		dtoFach.BezeichnungUeberweisungsZeugnis = "nicht ganz so super";
		dtoFach.MaxBemZeichen = 42;
		dtoFach.IstSchriftlichZK = true;
		dtoFach.IstSchriftlichBA = true;
		dtoFach.GewichtungFHR = 37;
		dtoFach.AbgeschlFaecherHolen = true;
		return dtoFach;
	}
}
