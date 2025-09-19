package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataKatalogSchuelerFoerderschwerpunkte")
@ExtendWith(MockitoExtension.class)
class DataKatalogSchuelerFoerderschwerpunkteTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKatalogSchuelerFoerderschwerpunkte data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesNotPatchable : id")
	void setAttributesNotPatchableTest_id() {
		final var dto = new DTOFoerderschwerpunkt(1L, "");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", 99L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.");
		}
	}

	@Test
	@DisplayName("setAttributesNotPatchable : text")
	void setAttributesNotPatchableTest_text() {
		final var dto = new DTOFoerderschwerpunkt(1L, "");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("text", 99L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: text.");
		}
	}

	@Test
	@DisplayName("setAttributesNotPatchable : kuerzelStatistik")
	void setAttributesNotPatchableTest_kuerzelStatistik() {
		final var dto = new DTOFoerderschwerpunkt(1L, "");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("kuerzelStatistik", 99L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: kuerzelStatistik.");
		}
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTOTest() {
		final var dto = new DTOFoerderschwerpunkt(1L, "");

		this.data.initDTO(dto, 2L, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("ID", 2L)
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = new DTOFoerderschwerpunkt(1L, "");

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "");
		when(this.conn.queryByKey(DTOFoerderschwerpunkt.class, 1L)).thenReturn(dto);
		mockSchuljahr();
		assertThat(this.data.getById(1L))
				.isInstanceOf(FoerderschwerpunktEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID des Förderschwerpunktes darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = catchThrowable(() -> this.data.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Förderschwerpunkt mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final var dto1 = new DTOFoerderschwerpunkt(1L, "");
		final var dto2 = new DTOFoerderschwerpunkt(2L, "");
		when(this.conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(List.of(dto1, dto2));
		mockSchuljahr();

		assertThat(this.data.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(FoerderschwerpunktEintrag.class)
						.hasFieldOrProperty("id"));
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllTest_Empty() {
		assertThat(this.data.getAll()).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Sichtbar = true;
		dto.Sortierung = 123;
		final var fs = Foerderschwerpunkt.BL.historie().getLast();
		dto.StatistikKrz = fs.schluessel;
		mockSchuljahr();

		assertThat(this.data.map(dto))
				.isInstanceOf(FoerderschwerpunktEintrag.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("kuerzel", "bezeichnung")
				.hasFieldOrPropertyWithValue("kuerzelStatistik", fs.schluessel)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung)
				.hasFieldOrPropertyWithValue("text", fs.text);
	}

	private void mockSchuljahr() {
		final var userMock = mock(Benutzer.class);
		when(userMock.schuleGetSchuljahr()).thenReturn(2015);
		when(this.conn.getUser()).thenReturn(userMock);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeTest_idWrong() {
		final var dto  = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "id", 2L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeTest_id() {
		final var dto  = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("mapAttribute | kuerzel")
	void mapAttributeTest_kuerzel() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		this.data.mapAttribute(dto, "kuerzel", "test", null);

		assertThat(dto.Bezeichnung).isEqualTo("test");
	}

	@Test
	@DisplayName("mapAttribute | kuerzel > 50 Zeichen")
	void mapAttributeTest_kuerzelTooLong() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(
				dto, "kuerzel", "Dieser Text ist viel zu lang, Dieser Text ist viel zu lang, Dieser Text ist viel zu lang", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut kuerzel: Die Länge des Strings ist auf 50 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzel Null")
	void mapAttributeTest_kuerzelNull() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Bezeichnung = "abc";

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "kuerzel", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut kuerzel: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzel doppelt vergeben")
	void mapAttribute_kuerzelDoppelt() {
		final var expectedDTO = new DTOFoerderschwerpunkt(1L, "abc");
		when(this.conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(List.of(new DTOFoerderschwerpunkt(2L, "test")));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(expectedDTO, "kuerzel", "TEST", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Kürzel TEST darf nicht doppelt vergeben werden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung case aendern im gleichen Objekt")
	void mapAttributeTest_changeCaseOfBezeichnung() throws ApiOperationException {
		final var expectedDTO = new DTOFoerderschwerpunkt(1L, "test");
		when(this.conn.queryAll(DTOFoerderschwerpunkt.class)).thenReturn(List.of(expectedDTO));

		this.data.mapAttribute(expectedDTO, "kuerzel", "TEST", null);

		assertThat(expectedDTO.Bezeichnung).isEqualTo("TEST");
	}

	@Test
	@DisplayName("mapAttribute | istSichtbar")
	void mapAttributeTest_istSichtbar() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Sichtbar = false;

		this.data.mapAttribute(dto, "istSichtbar", true, null);

		assertThat(dto.Sichtbar).isTrue();
	}

	@Test
	@DisplayName("mapAttribute | istSichtbarNull")
	void mapAttributeTest_istSichtbarNull() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "istSichtbar", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut istSichtbar: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | Sortierung")
	void mapAttributeTest_Sortierung() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.Sortierung = 123;

		this.data.mapAttribute(dto, "sortierung", 345, null);

		assertThat(dto.Sortierung).isEqualTo(345);

	}

	@Test
	@DisplayName("mapAttribute | SortierungNull")
	void mapAttributeTest_SortierungNull() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "sortierung", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut sortierung: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik")
	void mapAttributeTest_kuerzelStatistik() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.StatistikKrz = "ab";

		this.data.mapAttribute(dto, "kuerzelStatistik", "ab", null);

		assertThat(dto.StatistikKrz).isEqualTo("ab");
	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik Zu lang")
	void mapAttributeTest_kuerzelStatistikTooLong() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "kuerzelStatistik", "Zu viele Zeichen", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut kuerzelStatistik: Die Länge des Strings ist auf 2 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik | falsches Kürzel")
	void mapAttribute_kuerzelStatistik_wrong() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "kuerzelStatistik", "ßß", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Zum angegebenen Kürzel ßß wurde kein passender Förderschwerpunkt gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelStatistik")
	void mapAttribute_kuerzelStatistik() throws ApiOperationException {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");
		dto.StatistikKrz = "ab";
		final var key = Foerderschwerpunkt.BL.historie().getLast().schluessel;

		this.data.mapAttribute(dto, "kuerzelStatistik", key, null);

		assertThat(dto.StatistikKrz).isEqualTo(key);
	}

	@Test
	@DisplayName("mapAttribute | unknown argument")
	void mapAttribute_unknownArgument() {
		final var dto = new DTOFoerderschwerpunkt(1L, "bezeichnung");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "unknown", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
