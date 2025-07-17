package de.svws_nrw.data.kurse;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataKurse} */
@DisplayName("Diese Klasse teste die Klasse DataKurse")
@ExtendWith(MockitoExtension.class)
class DataKurseTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKurse data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idSchuljahresabschnitt")
	void setAttributesRequiredOnCreationTest_idSchuljahresabschnitt() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("kuerzel", "a", "idFach", "b", "kursartAllg", "c"));

			final var throwable = catchThrowable(() -> this.data.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (idSchuljahresabschnitt) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: kuerzel")
	void setAttributesRequiredOnCreationTest_kuerzel() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("idSchuljahresabschnitt", "a", "idFach", "b", "kursartAllg", "c"));

			final var throwable = catchThrowable(() -> this.data.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (kuerzel) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idFach")
	void setAttributesRequiredOnCreationTest_idFach() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("idSchuljahresabschnitt", "a", "kuerzel", "b", "kursartAllg", "c"));

			final var throwable = catchThrowable(() -> this.data.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (idFach) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: kursartAllg")
	void setAttributesRequiredOnCreationTest_kursartAllg() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("idSchuljahresabschnitt", "a", "idFach", "b", "kuerzel", "c"));

			final var throwable = catchThrowable(() -> this.data.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (kursartAllg) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("setAttributesNotPatchable : id")
	void setAttributesNotPatchableTest_id() {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOKurs.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", 99L));

			final var throwable = Assertions.catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.");
		}
	}

	@Test
	@DisplayName("setAttributesNotPatchable : idSchuljahresabschnitt")
	void setAttributesNotPatchableTest_idSchuljahresabschnitt() {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOKurs.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("idSchuljahresabschnitt", 99L));

			final var throwable = Assertions.catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: idSchuljahresabschnitt.");
		}
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOKurs.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(KursDaten.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = Assertions.catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID des Kurses darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getLongId")
	void getLongIdTest() {
		final var dto = getDto();

		assertThat(this.data.getLongId(dto)).isEqualTo(dto.ID);
	}

	@Test
	@DisplayName("initDTO")
	void initDTOTest() {
		final var dto = getDto();

		this.data.initDTO(dto, 4L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 4L)
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000)
				.hasFieldOrPropertyWithValue("WochenStd", 3)
				.hasFieldOrPropertyWithValue("Fortschreibungsart", KursFortschreibungsart.KEINE)
				.hasFieldOrPropertyWithValue("EpochU", false);
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() throws ApiOperationException {
		final var dto = new DTOKurs(1L, 2L, "kurzBez", 3L);
		dto.Jahrgang_ID = 4L;
		dto.Jahrgaenge = "5,6,7";
		dto.Lehrer_ID = 5L;
		dto.KursartAllg = "kursartAllg";
		dto.Sortierung = 6;
		dto.Sichtbar = true;
		dto.Schienen = "8,9,10";
		dto.WochenStd = 7;
		dto.WochenstdKL = 8D;
		dto.Fortschreibungsart = KursFortschreibungsart.KOMPLETT;
		dto.SchulNr = 9;
		dto.EpochU = true;
		dto.ZeugnisBez = "hij";


		assertThat(this.data.map(dto))
				.isInstanceOf(KursDaten.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("idSchuljahresabschnitt", dto.Schuljahresabschnitts_ID)
				.hasFieldOrPropertyWithValue("kuerzel", dto.KurzBez)
				.hasFieldOrPropertyWithValue("idJahrgaenge", List.of(4L, 5L, 6L, 7L))
				.hasFieldOrPropertyWithValue("idFach", dto.Fach_ID)
				.hasFieldOrPropertyWithValue("lehrer", dto.Lehrer_ID)
				.hasFieldOrPropertyWithValue("kursartAllg", dto.KursartAllg)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar)
				.hasFieldOrPropertyWithValue("schienen", List.of(8, 9, 10))
				.hasFieldOrPropertyWithValue("wochenstunden", dto.WochenStd)
				.hasFieldOrPropertyWithValue("wochenstundenLehrer", dto.WochenstdKL)
				.hasFieldOrPropertyWithValue("idKursFortschreibungsart", KursFortschreibungsart.KOMPLETT.id)
				.hasFieldOrPropertyWithValue("schulnummer", dto.SchulNr)
				.hasFieldOrPropertyWithValue("istEpochalunterricht", dto.EpochU)
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", dto.ZeugnisBez);
	}

	@Test
	@DisplayName("map | bezeichnungZeugnis is Blank")
	void mapTest_ZeugnisIsBlank() throws ApiOperationException {
		final var dto = getDto();
		dto.ZeugnisBez = "";

		assertThat(this.data.map(dto).bezeichnungZeugnis).isNull();
	}

	@Test
	@DisplayName("mapAttribute | idSchuljahresabschnitt | null")
	void mapAttributeTest_idSchuljahresabschnittNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "idSchuljahresabschnitt", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut idSchuljahresabschnitt: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | idFach")
	void mapAttributeTest_idFach() throws ApiOperationException {
		final var dto = getDto();
		dto.Fach_ID = -1;
		when(this.conn.queryByKey(DTOFach.class, 3L)).thenReturn(mock(DTOFach.class));

		this.data.mapAttribute(dto, "idFach", 3L, null);

		assertThat(dto.Fach_ID).isEqualTo(3);
	}

	@Test
	@DisplayName("mapAttribute | idFach null")
	void mapAttributeTest_idFachNull() {
		final var dto = getDto();

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "idFach", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID des Faches darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | idFach | no matching Fach in DB")
	void mapAttributeTest_idFachNotMatching() {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOFach.class, 2L)).thenReturn(null);

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "idFach", 2L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es konnte kein Fach mit der angegebenen ID gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | lehrer")
	void mapAttributeTest_lehrer() throws ApiOperationException {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOLehrer.class, 3L)).thenReturn(mock(DTOLehrer.class));

		this.data.mapAttribute(dto, "lehrer", 3L, null);

		assertThat(dto.Lehrer_ID).isEqualTo(3);
	}

	@Test
	@DisplayName("mapAttribute | lehrer | id null")
	void mapAttributeTest_idNull() throws ApiOperationException {
		final var dto = getDto();
		dto.Lehrer_ID = 45L;

		this.data.mapAttribute(dto, "lehrer", null, null);

		assertThat(dto.Lehrer_ID).isNull();
	}

	@Test
	@DisplayName("mapAttribute | lehrer | not matching")
	void mapAttributeTest_idNotMatching() {
		when(this.conn.queryByKey(DTOLehrer.class, 3L)).thenReturn(null);

		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "lehrer", 3L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es konnte kein Lehrer mit der angegebenen ID 3 gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | kuerzel null")
	void mapAttributeTest_kuerzelNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "kuerzel", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut kuerzel: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kursartAllg")
	void mapAttributeTest_kursartAllg() throws ApiOperationException {
		final var dto = getDto();
		dto.KursartAllg = "123";

		this.data.mapAttribute(dto, "kursartAllg", "abc", null);

		assertThat(dto.KursartAllg).isEqualTo("abc");
	}

	@Test
	@DisplayName("mapAttribute | kursartAllg null")
	void mapAttributeTest_kursartAllgNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "kursartAllg", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut kursartAllg: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | sortierung null")
	void mapAttributeTest_sortierungNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "sortierung", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut sortierung: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | istSichtbar null")
	void mapAttributeTest_istSichtbarNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "istSichtbar", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut istSichtbar: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | wochenstunden null")
	void mapAttributeTest_wochenstundenNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "wochenstunden", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut wochenstunden: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | wochenstundenLehrerNull")
	void mapAttributeTest_wochenstundenLehrerNull() throws ApiOperationException {
		final var dto = getDto();
		dto.WochenstdKL = 5.5;

		this.data.mapAttribute(dto, "wochenstundenLehrer", null, null);

		assertThat(dto.WochenstdKL).isEqualTo(0.0);
	}

	@Test
	@DisplayName("mapAttribute | idKursFortschreibungsart null")
	void mapAttributeTest_idKursFortschreibungsartNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "idKursFortschreibungsart", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut idKursFortschreibungsart: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | schulnummer")
	void mapAttributeTest_schulnummer() throws ApiOperationException {
		final var dto = getDto();
		dto.SchulNr = null;
		when(this.conn.queryList(DTOSchuleNRW.QUERY_BY_SCHULNR, DTOSchuleNRW.class, 100123)).thenReturn(List.of(mock(DTOSchuleNRW.class)));

		this.data.mapAttribute(dto, "schulnummer", 100123, null);

		assertThat(dto.SchulNr).isEqualTo(100123);
	}

	@Test
	@DisplayName("mapAttribute | schulnummer null")
	void mapAttributeTest_schulnummerNull() throws ApiOperationException {
		final var dto = getDto();
		dto.SchulNr = 123;

		this.data.mapAttribute(dto, "schulnummer", null, null);

		assertThat(dto.SchulNr).isNull();
	}

	@Test
	@DisplayName("mapAttribute | schulnummer is wrong")
	void mapAttributeTest_schulnummerWrong() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "schulnummer", 100999, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Für die Schulnummer 100999 konnte keine passende Schule gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);

	}

	@Test
	@DisplayName("mapAttribute | istEpochalunterricht null")
	void mapAttributeTest_istEpochalunterricht() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "istEpochalunterricht", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut istEpochalunterricht: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnungZeugnis null")
	void mapAttribute_bezeichnungZeugnisNull() throws ApiOperationException {
		final var dto = getDto();
		dto.ZeugnisBez = "test";

		this.data.mapAttribute(dto, "bezeichnungZeugnis", null, null);

		assertThat(dto.ZeugnisBez).isNull();
	}

	@Test
	@DisplayName("mapAttribute | schienen")
	void mapAttribute_mapSchienen() throws ApiOperationException {
		final var dto = getDto();
		dto.Schienen = "3,4";

		this.data.mapAttribute(dto, "schienen", List.of(1, 2, 3), null);

		assertThat(dto.Schienen).isEqualTo("1,2,3");
	}

	@Test
	@DisplayName("mapAttribute | schienen null")
	void mapAttribute_schienenNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "schienen", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut schienen: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | negative Schienen-Nummer")
	void mapAttribute_negativSchiene() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "schienen", List.of(-1), null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Schienen-Nummer kleiner als 0 ist nicht zulässig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | idJahrgaengeList")
	void mapAttribute_idJahrgaenge() throws ApiOperationException {
		final var dto = getDto();
		dto.ASDJahrgang = "abc";
		dto.Jahrgang_ID = 9L;
		dto.Jahrgaenge = "abc";
		final var newValues = List.of(1L, 2L);
		when(this.conn.queryByKeyList(DTOJahrgang.class, newValues)).thenReturn(List.of(mock(DTOJahrgang.class), mock(DTOJahrgang.class)));

		this.data.mapAttribute(dto, "idJahrgaenge", newValues, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ASDJahrgang", null)
				.hasFieldOrPropertyWithValue("Jahrgang_ID", null)
				.hasFieldOrPropertyWithValue("Jahrgaenge", "1,2");
	}

	@Test
	@DisplayName("mapAttribute | idJahrgaengeSingle")
	void mapAttribute_idJahrgaengeSingle() throws ApiOperationException {
		final var dto = getDto();
		dto.ASDJahrgang = "abc";
		dto.Jahrgang_ID = 9L;
		dto.Jahrgaenge = "abc";
		final var newValues = List.of(1L);
		final var dtoJahrgang = new DTOJahrgang(1L);
		dtoJahrgang.ASDJahrgang = "TEST";
		when(this.conn.queryByKeyList(DTOJahrgang.class, newValues)).thenReturn(List.of(dtoJahrgang));

		this.data.mapAttribute(dto, "idJahrgaenge", newValues, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ASDJahrgang", dtoJahrgang.ASDJahrgang)
				.hasFieldOrPropertyWithValue("Jahrgang_ID", 1L)
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("mapAttribute | idJahrgaenge | wrong id")
	void mapAttribute_idJahrgaengeWrongID() {
		final var newValues = List.of(1L, 2L);
		when(this.conn.queryByKeyList(DTOJahrgang.class, newValues)).thenReturn(List.of(mock(DTOJahrgang.class)));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(getDto(), "idJahrgaenge", newValues, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Mindestens einer der angegebenen Jahrgang-IDs existiert nicht in der SVWS-Datenbank")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | idJahrgaenge | emptyList")
	void mapAttribute_idJahrgaengeEmptyList() throws ApiOperationException {
		final var dto = getDto();
		dto.ASDJahrgang = "abc";
		dto.Jahrgang_ID = 9L;
		dto.Jahrgaenge = "abc";

		this.data.mapAttribute(dto, "idJahrgaenge", emptyList(), null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ASDJahrgang", null)
				.hasFieldOrPropertyWithValue("Jahrgang_ID", null)
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("mapAttribute | idJahrgaenge | noChanges")
	void mapAttribute_idJahrgaengeNoChanges() throws ApiOperationException {
		final var dto = getDto();
		dto.ASDJahrgang = "abc";
		dto.Jahrgang_ID = 9L;
		dto.Jahrgaenge = "1,2,3";

		this.data.mapAttribute(dto, "idJahrgaenge", List.of(1L, 2L, 3L, 9L), null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ASDJahrgang", "abc")
				.hasFieldOrPropertyWithValue("Jahrgang_ID", 9L)
				.hasFieldOrPropertyWithValue("Jahrgaenge", "1,2,3");
	}


	private static DTOKurs getDto() {
		return new DTOKurs(1L, 1L, "1", 1L);
	}



}
