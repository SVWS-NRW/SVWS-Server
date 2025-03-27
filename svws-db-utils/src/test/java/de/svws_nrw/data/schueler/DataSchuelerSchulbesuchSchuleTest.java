package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Diese Klasse teste die Klasse {@link DataSchuelerSchulbesuchSchule}*/
@DisplayName("Diese Klasse teste die Klasse DataSchuelerSchulbesuchSchule")
class DataSchuelerSchulbesuchSchuleTest {

	private final DBEntityManager conn = mock(DBEntityManager.class);

	private DataSchuelerSchulbesuchSchule data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(List.of(new DTOEntlassarten(7, "Beurlaubung")));
		data = new DataSchuelerSchulbesuchSchule(conn, 123L);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableTest() {
		final var dto = new DTOSchuelerAbgaenge(1L, 1001);
		when(this.conn.queryByKey(DTOSchuelerAbgaenge.class, 1L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", 99L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTOTest() throws ApiOperationException {
		final var dto = getDtoSchuelerAbgaenge();

		this.data.initDTO(dto, 2L, null);

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("initDTO | missing idSchueler")
	void initDTOTest_idSchuelerMissing() {
		final var dto = getDtoSchuelerAbgaenge();
		data = new DataSchuelerSchulbesuchSchule(conn);

		final var throwable = catchThrowable(() -> this.data.initDTO(dto, 2L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID des Schuelers darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = getDtoSchuelerAbgaenge();

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getByID | Erfolg")
	void getByIDTest() throws ApiOperationException {
		final var dto = getDtoSchuelerAbgaenge();
		when(this.conn.queryByKey(DTOSchuelerAbgaenge.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(SchuelerSchulbesuchSchule.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für den Schulbesuch eines Schülers darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = catchThrowable(() -> this.data.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Schulbesuch eines Schülers mit der Id 99 gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() throws ApiOperationException {
		final var dto = getDtoSchuelerAbgaenge();
		final var schulnummer = "123123";
		final var idSchule = 111L;
		final var c = mock(DBEntityManager.class);
		lenient().when(c.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(new DTOSchuleNRW(idSchule, schulnummer)));
		lenient().when(c.queryAll(DTOEntlassarten.class)).thenReturn(List.of(new DTOEntlassarten(123L, "Beurlaubung")));
		final var data = new DataSchuelerSchulbesuchSchule(c);

		assertThat(data.map(dto))
				.isInstanceOf(SchuelerSchulbesuchSchule.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("idSchule", idSchule)
				.hasFieldOrPropertyWithValue("schulgliederung", dto.LSSGL)
				.hasFieldOrPropertyWithValue("entlassgrundID", 123L)
				.hasFieldOrPropertyWithValue("abschlussartID", dto.LSEntlassArt)
				.hasFieldOrPropertyWithValue("organisationsFormID", dto.OrganisationsformKrz)
				.hasFieldOrPropertyWithValue("datumVon", dto.LSBeginnDatum)
				.hasFieldOrPropertyWithValue("datumBis", dto.LSSchulEntlassDatum)
				.hasFieldOrPropertyWithValue("jahrgangVon", dto.LSBeginnJahrgang)
				.hasFieldOrPropertyWithValue("jahrgangBis", dto.LSJahrgang);
	}

	@Test
	@DisplayName("map | schule null")
	void mapTest_schuleNull() throws ApiOperationException {
		final var dto = getDtoSchuelerAbgaenge();
		dto.AbgangsSchulNr = null;

		final var result = this.data.map(dto);

		assertThat(result.idSchule).isNull();
	}

	@Test
	@DisplayName("mapMultiple | Erfolg")
	void mapMultipleTest() {
		final var dto = getDtoSchuelerAbgaenge();
		final var entlassart = new DTOEntlassarten(7, "Beurlaubung");
		final var schule = new DTOSchuleNRW(1L, "123123");

		assertThat(DataSchuelerSchulbesuchSchule.mapMultiple(List.of(dto), Map.of(entlassart.Bezeichnung, entlassart), Map.of(schule.SchulNr, schule)))
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(SchuelerSchulbesuchSchule.class)
						.hasFieldOrPropertyWithValue("id", dto.ID)
						.hasFieldOrPropertyWithValue("idSchule", schule.ID)
						.hasFieldOrPropertyWithValue("schulgliederung", dto.LSSGL)
						.hasFieldOrPropertyWithValue("entlassgrundID", entlassart.ID)
						.hasFieldOrPropertyWithValue("abschlussartID", dto.LSEntlassArt)
						.hasFieldOrPropertyWithValue("organisationsFormID", dto.OrganisationsformKrz)
						.hasFieldOrPropertyWithValue("datumVon", dto.LSBeginnDatum)
						.hasFieldOrPropertyWithValue("datumBis", dto.LSSchulEntlassDatum)
						.hasFieldOrPropertyWithValue("jahrgangVon", dto.LSBeginnJahrgang)
						.hasFieldOrPropertyWithValue("jahrgangBis", dto.LSJahrgang)
				);
	}

	@Test
	@DisplayName("mapMultiple | List is empty")
	void mapMultipleTest_emptyList() {
		assertThat(DataSchuelerSchulbesuchSchule.mapMultiple(Collections.emptyList(), Collections.emptyMap(), Collections.emptyMap())).isNotNull().isEmpty();
	}


	@Test
	@DisplayName("mapAttribute | id is correct | nothing thrown")
	void mapAttributeTest_idIsCorrect() {
		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(getDtoSchuelerAbgaenge(), "id", 1L, null));
	}

	@Test
	@DisplayName("mapAttribute | id is not correct")
	void mapAttributeTest_idIsNotCorrect() {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "id", 2L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("PatchId 2 ist ungleich dtoID 1.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | idSchule")
	void mapAttributeTest_idSchule() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);
		final var schulnummer = "123123";
		final var idSchule = 111L;
		final var c = mock(DBEntityManager.class);
		lenient().when(c.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(new DTOSchuleNRW(idSchule, schulnummer)));
		final var data = new DataSchuelerSchulbesuchSchule(c);

		data.mapAttribute(dto, "idSchule", idSchule, null);

		assertThat(dto.AbgangsSchulNr).isEqualTo(schulnummer);
	}

	@Test
	@DisplayName("mapAttribute | idSchule | Exception")
	void mapAttributeTest_idSchuleException() {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "idSchule", 37L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Schule mit der ID 37 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | schulgliederung")
	final void mapAttributeTest_schulgliederung() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		this.data.mapAttribute(dto, "schulgliederung", "123", null);

		assertThat(dto.LSSGL).isEqualTo("123");
	}

	@Test
	@DisplayName("mapAttribute | Entlassgrund")
	final void mapAttributeTest_entlassgrund() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);
		final var c = mock(DBEntityManager.class);
		lenient().when(c.queryAll(DTOEntlassarten.class)).thenReturn(List.of(new DTOEntlassarten(123L, "test")));
		final var data = new DataSchuelerSchulbesuchSchule(c);

		data.mapAttribute(dto, "entlassgrundID", 123L, null);

		assertThat(dto.BemerkungIntern).isEqualTo("test");
	}

	@Test
	@DisplayName("mapAttribute | Entlassgrund | Exception")
	final void mapAttributeTest_entlassgrundException() {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "entlassgrundID", 37L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Zur id 37 existiert keine Entlassart")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | Entlassgrund | null")
	final void mapAttributeTest_entlassgrundNull() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);
		dto.BemerkungIntern = "needs to be replaced with null";

		this.data.mapAttribute(dto, "entlassgrundID", null, null);

		assertThat(dto.BemerkungIntern).isNull();



	}

	@Test
	@DisplayName("mapAttribute | AbschlussartID")
	final void mapAttributeTest_AbschlussartID() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		this.data.mapAttribute(dto, "abschlussartID", "12", null);

		assertThat(dto.LSEntlassArt).isEqualTo("12");
	}

	@Test
	@DisplayName("mapAttribute | organisationsFormID")
	final void mapAttributeTest_organisationsFormID() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		this.data.mapAttribute(dto, "organisationsFormID", "A", null);

		assertThat(dto.OrganisationsformKrz).isEqualTo("A");
	}

	@Test
	@DisplayName("mapAttribute | datumVon")
	final void mapAttributeTest_datumVon() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		this.data.mapAttribute(dto, "datumVon", "01-02-2020", null);

		assertThat(dto.LSBeginnDatum).isEqualTo("01-02-2020");
	}

	@Test
	@DisplayName("mapAttribute | datumBis")
	final void mapAttributeTest_datumBis() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		this.data.mapAttribute(dto, "datumBis", "01-02-2020", null);

		assertThat(dto.LSSchulEntlassDatum).isEqualTo("01-02-2020");
	}

	@Test
	@DisplayName("mapAttribute | jahrgangVon")
	final void mapAttributeTest_jahrgangVon() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		this.data.mapAttribute(dto, "jahrgangVon", "02", null);

		assertThat(dto.LSBeginnJahrgang).isEqualTo("02");
	}

	@Test
	@DisplayName("mapAttribute | jahrgangBis")
	final void mapAttributeTest_jahrgangBis() throws ApiOperationException {
		final var dto = new DTOSchuelerAbgaenge(1L, 2L);

		this.data.mapAttribute(dto, "jahrgangBis", "02", null);

		assertThat(dto.LSJahrgang).isEqualTo("02");
	}

	@Test
	@DisplayName("mapAttribute | unknown")
	final void mapAttributeTest_unknown() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(null, "unknown", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private DTOSchuelerAbgaenge getDtoSchuelerAbgaenge() {
		final var dto = new DTOSchuelerAbgaenge(1L, 1001);
		dto.AbgangsSchulNr = "123123";
		dto.LSSGL = "456789";
		dto.BemerkungIntern = "Beurlaubung";
		dto.LSEntlassArt = "OA";
		dto.OrganisationsformKrz = "1";
		dto.LSBeginnDatum = "1907-12-01";
		dto.LSSchulEntlassDatum = "1909-02-03";
		dto.LSBeginnJahrgang = "07";
		dto.LSJahrgang = "08";
		return dto;

	}
}
