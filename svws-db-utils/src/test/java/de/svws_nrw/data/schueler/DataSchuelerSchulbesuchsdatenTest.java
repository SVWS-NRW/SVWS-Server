package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Kindergartenbesuch;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.schueler.Herkunftsarten;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOKindergarten;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.ThrowableAssert;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataSchuelerSchulbesuchsdaten")
@ExtendWith(MockitoExtension.class)
class DataSchuelerSchulbesuchsdatenTest {

	/** Definition STATUS */
	static final String STATUS = "status";

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchuelerSchulbesuchsdaten schulbesuchsdaten;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableTest_id() {
		final var dto = new DTOSchueler(1L, "123", false);
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(dto);

		try (var mapperMock = mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", 99L));

			final var throwable = ThrowableAssert.catchThrowable(() -> this.schulbesuchsdaten.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = new DTOSchueler(1L, "123", false);

		assertThat(this.schulbesuchsdaten.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(new DTOSchueler(1L, "1", true));

		assertThat(this.schulbesuchsdaten.getById(1L))
				.isInstanceOf(SchuelerSchulbesuchsdaten.class)
				.hasFieldOrPropertyWithValue("id", 1L);
	}

	@Test
	@DisplayName("getById | idSchueler ist null")
	void getByIdTest_idSchuelerNull() {
		final var throwable = catchThrowable(() -> schulbesuchsdaten.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für den Schüler darf nicht null sein.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getById | falsche idSchueler")
	void getByIdTest_wrongIdSchueler() {
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(null);

		final var throwable = catchThrowable(() -> schulbesuchsdaten.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Schüler mit der Id 1 gefunden")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	/** --- map Tests ---*/

	@Test
	@DisplayName("map | idVorherigeSchule | Erfolg")
	void mapTest_idVorherigeSchule_Erfolg() throws ApiOperationException {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(new DTOSchuleNRW(3L, "123")));
		final var data = new DataSchuelerSchulbesuchsdaten(this.conn);
		final var dto = new DTOSchueler(1L, "2", false);
		dto.LSSchulNr = "123";

		final var result = data.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idVorherigeSchule", 3L);
	}

	@Test
	@DisplayName("map | idVorherigeSchule | Erfolg")
	void mapTest_idVorherigeSchule_Null() throws ApiOperationException {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOSchuleNRW.class)).thenReturn(Collections.emptyList());
		final var data = new DataSchuelerSchulbesuchsdaten(this.conn);
		final var dto = new DTOSchueler(1L, "2", false);
		dto.LSSchulNr = "123";

		final var result = data.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idVorherigeSchule", null);
	}

	@Test
	@DisplayName("map | vorigeEntlassgrundID | Erfolg")
	void mapTest_vorigeEntlassgrundID_Erfolg() throws ApiOperationException {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(List.of(new DTOEntlassarten(3L, "entlassgrund")));
		final var data = new DataSchuelerSchulbesuchsdaten(this.conn);
		final var dto = new DTOSchueler(1L, "2", false);
		dto.LSEntlassgrund = "entlassgrund";

		final var result = data.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("vorigeEntlassgrundID", 3L);
	}

	@Test
	@DisplayName("map | vorigeEntlassgrundID | Null")
	void mapTest_vorigeEntlassgrundID_Null() throws ApiOperationException {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(Collections.emptyList());
		final var data = new DataSchuelerSchulbesuchsdaten(this.conn);
		final var dto = new DTOSchueler(1L, "2", false);
		dto.LSEntlassgrund = "entlassgrund";

		final var result = data.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("vorigeEntlassgrundID", null);
	}

	@Test
	@DisplayName("map | entlassungGrundID | Erfolg")
	void mapTest_entlassungGrundID_Erfolg() throws ApiOperationException {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(List.of(new DTOEntlassarten(3L, "entlassgrund")));
		final var data = new DataSchuelerSchulbesuchsdaten(this.conn);
		final var dto = new DTOSchueler(1L, "2", false);
		dto.Entlassgrund = "entlassgrund";

		final var result = data.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("entlassungGrundID", 3L);
	}

	@Test
	@DisplayName("map | entlassungGrundID | Null")
	void mapTest_entlassungGrundID_Null() throws ApiOperationException {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(Collections.emptyList());
		final var data = new DataSchuelerSchulbesuchsdaten(this.conn);
		final var dto = new DTOSchueler(1L, "2", false);
		dto.Entlassgrund = "entlassgrund";

		final var result = data.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("entlassungGrundID", null);
	}

	@Test
	@DisplayName("map | idAufnehmendeSchule | Erfolg")
	void mapTest_idAufnehmendeSchule_Erfolg() throws ApiOperationException {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(new DTOSchuleNRW(3L, "123")));
		final var data = new DataSchuelerSchulbesuchsdaten(this.conn);
		final var dto = new DTOSchueler(1L, "2", false);
		dto.SchulwechselNr = "123";

		final var result = data.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idAufnehmendeSchule", 3L);
	}


	@Test
	@DisplayName("map | idAufnehmendeSchule | null")
	void mapTest_idAufnehmendeSchule_Null() throws ApiOperationException {
		when(this.conn.queryAll(DTOEntlassarten.class)).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOMerkmale.class)).thenReturn(Collections.emptyList());
		when(this.conn.queryAll(DTOSchuleNRW.class)).thenReturn(Collections.emptyList());
		final var data = new DataSchuelerSchulbesuchsdaten(this.conn);
		final var dto = new DTOSchueler(1L, "2", false);
		dto.SchulwechselNr = "123";

		final var result = data.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idAufnehmendeSchule", null);
	}

	@Test
	@DisplayName("map | grundschuleEinschulungsartId | Erfolg")
	void mapTest_grundschuleEinschulungsartId_Erfolg() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		final var einschulungsart = Einschulungsart.values()[0].getLetzterEintrag();
		dto.EinschulungsartASD = einschulungsart.schluessel;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("grundschuleEinschulungsartID", einschulungsart.id);
	}

	@Test
	@DisplayName("map | verpflichtungSprachfoerderkurs | True")
	void mapTest_verpflichtungSprachfoerderkurs_True() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.VerpflichtungSprachfoerderkurs = true;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("verpflichtungSprachfoerderkurs", true);
	}

	@Test
	@DisplayName("map | verpflichtungSprachfoerderkurs | False")
	void mapTest_verpflichtungSprachfoerderkurs_False() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.VerpflichtungSprachfoerderkurs = false;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("verpflichtungSprachfoerderkurs", false);
	}


	@Test
	@DisplayName("map | verpflichtungSprachfoerderkurs | Null")
	void mapTest_verpflichtungSprachfoerderkurs_Null() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.VerpflichtungSprachfoerderkurs = null;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("verpflichtungSprachfoerderkurs", false);
	}

	@Test
	@DisplayName("map | teilnahmeSprachfoerderkurs | True")
	void mapTest_teilnahmeSprachfoerderkurs_True() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.TeilnahmeSprachfoerderkurs = true;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("teilnahmeSprachfoerderkurs", true);
	}

	@Test
	@DisplayName("map | teilnahmeSprachfoerderkurs | False")
	void mapTest_teilnahmeSprachfoerderkurs_False() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.TeilnahmeSprachfoerderkurs = false;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("teilnahmeSprachfoerderkurs", false);
	}


	@Test
	@DisplayName("map | teilnahmeSprachfoerderkurs | Null")
	void mapTest_teilnahmeSprachfoerderkurs_Null() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.TeilnahmeSprachfoerderkurs = null;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("teilnahmeSprachfoerderkurs", false);
	}

	@Test
	@DisplayName("map | idDauerKindergartenbesuch | Null")
	void mapTest_idDauerKindergartenbesuch_Null() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.DauerKindergartenbesuch = null;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idDauerKindergartenbesuch", null);
	}

	@Test
	@DisplayName("map | idDauerKindergartenbesuch | wrongKey")
	void mapTest_idDauerKindergartenbesuch_wrongKey() {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.DauerKindergartenbesuch = "wrong";

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.map(dto));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Kindergartenbesuch mit dem Schlüssel wrong gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map | idDauerKindergartenbesuch | Erfolg")
	void mapTest_idDauerKindergartenbesuch_Erfolg() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		final var kindergartenbesuch = Kindergartenbesuch.values()[0].historie().getFirst();
		dto.DauerKindergartenbesuch = kindergartenbesuch.schluessel;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idDauerKindergartenbesuch", kindergartenbesuch.id);
	}

	@Test
	@DisplayName("map | idGrundschuleJahreEingangsphase | Erfolg")
	void mapTest_idGrundschuleJahreEingangsphase_Erfolg() throws ApiOperationException {
		final var primarstufe = PrimarstufeSchuleingangsphaseBesuchsjahre.values()[0].historie().getLast();
		final var dto = new DTOSchueler(1L, "2", false);
		dto.EPJahre = Integer.valueOf(primarstufe.schluessel);
		dto.Einschulungsjahr = 2100;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idGrundschuleJahreEingangsphase", primarstufe.id);
	}

	@Test
	@DisplayName("map | idGrundschuleJahreEingangsphase | EPJahre is null")
	void mapTest_idGrundschuleJahreEingangsphase_EPJahreNull() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.EPJahre = null;
		dto.Einschulungsjahr = 2100;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idGrundschuleJahreEingangsphase", null);
	}

	@Test
	@DisplayName("map | idGrundschuleJahreEingangsphase | Einschlungsjahr is null")
	void mapTest_idGrundschuleJahreEingangsphase_EinschlungsjahrNull() throws ApiOperationException {
		final var primarstufe = PrimarstufeSchuleingangsphaseBesuchsjahre.values()[0].historie().getLast();
		final var dto = new DTOSchueler(1L, "2", false);
		dto.EPJahre = Integer.valueOf(primarstufe.schluessel);
		dto.Einschulungsjahr = null;

		final var result = this.schulbesuchsdaten.map(dto);

		assertThat(result).hasFieldOrPropertyWithValue("idGrundschuleJahreEingangsphase", null);
	}

	/** --- mapAttribute Tests ---*/

	@Test
	@DisplayName("mapAttribute | match id")
	void mapAttributeTest_matchId() {
		final var dto = new DTOSchueler(1L, "2", false);

		assertDoesNotThrow(() -> this.schulbesuchsdaten.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("mapAttribute | idDauerKindergartenbesuch | null")
	void mapAttributeTest_idDauerKindergartenbesuch_null() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.DauerKindergartenbesuch = "123";

		this.schulbesuchsdaten.mapAttribute(dto, "idDauerKindergartenbesuch", null, null);

		assertThat(dto.DauerKindergartenbesuch).isNull();
	}

	@Test
	@DisplayName("mapAttribute | idDauerKindergartenbesuch | eintrag null")
	void mapAttributeTest_idDauerKindergartenbesuch_eintragNull() {
		final var dto = new DTOSchueler(1L, "2", false);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dto, "idDauerKindergartenbesuch", -1, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Kindergartenbesuch mit der ID -1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | idDauerKindergartenbesuch | Erfolg")
	void mapAttributeTest_idDauerKindergartenbesuch_Erfolg() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		final var entry = Kindergartenbesuch.values()[0].historie().getLast();

		this.schulbesuchsdaten.mapAttribute(dto, "idDauerKindergartenbesuch", entry.id, null);

		assertThat(dto.DauerKindergartenbesuch).isEqualTo(entry.schluessel);
	}

	@Test
	@DisplayName("mapAttribute | KindergartenID | null")
	void mapAttributeTest_KindergartenID_Null() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);

		this.schulbesuchsdaten.mapAttribute(dto, "idKindergarten", null, null);

		assertThat(dto.Kindergarten_ID).isNull();
	}

	@Test
	@DisplayName("mapAttribute | KindergartenID | wrongKey")
	void mapAttributeTest_KindergartenID_wrongKey() {
		final var dto = new DTOSchueler(1L, "2", false);
		when(this.conn.queryByKey(DTOKindergarten.class, 2L)).thenReturn(null);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dto, "idKindergarten", 2L, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Kindergarten mit der ID 2 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | KindergartenID | Erfolg")
	void mapAttributeTest_KindergartenId_Erfolg() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		final var kindergarten = new DTOKindergarten(5L);
		when(this.conn.queryByKey(DTOKindergarten.class, 5L)).thenReturn(kindergarten);

		this.schulbesuchsdaten.mapAttribute(dto, "idKindergarten", kindergarten.ID, null);

		assertThat(dto.Kindergarten_ID).isEqualTo(kindergarten.ID);
	}

	@Test
	@DisplayName("mapAttribute | Einschulungsart | idNull")
	void mapAttributeTest_Einschulungsart_idNull() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		dto.EinschulungsartASD = "abc";

		this.schulbesuchsdaten.mapAttribute(dto, "grundschuleEinschulungsartID", null, null);

		assertThat(dto.EinschulungsartASD).isNull();
	}

	@Test
	@DisplayName("mapAttribute | Einschulungsart | wrongID")
	void mapAttributeTest_Einschulungsart_wrongID() {
		final var dto = new DTOSchueler(1L, "2", false);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dto, "grundschuleEinschulungsartID", -1, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Einschulungsart mit der ID -1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | Einschulungsart | Erfolg")
	void mapAttributeTest_Einschulungsart_Erfolg() throws ApiOperationException {
		final var dto = new DTOSchueler(1L, "2", false);
		final var entry = Einschulungsart.values()[0].historie().getLast();

		this.schulbesuchsdaten.mapAttribute(dto, "grundschuleEinschulungsartID", entry.id, null);

		assertThat(dto.EinschulungsartASD).isEqualTo(entry.schluessel);
	}


	@Test
	@DisplayName("mapAttribute | idVorherigeSchule | Erfolg")
	void mapAttributeTest_idVorherigeSchule() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var schulnummer = "123123";
		final var idSchule = 111L;
		final var c = mock(DBEntityManager.class);
		lenient().when(c.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(new DTOSchuleNRW(idSchule, schulnummer)));
		final var data = new DataSchuelerSchulbesuchsdaten(c);

		data.mapAttribute(dtoSchueler, "idVorherigeSchule", idSchule, null);

		assertThat(dtoSchueler.LSSchulNr).isEqualTo(schulnummer);
	}

	@Test
	@DisplayName("mapAttribute | idVorherigeSchule | value is null")
	void mapAttributeTest_idVorherigeSchule_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.LSSchulNr = "soll mit null ueberschrieben werden";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "idVorherigeSchule", null, null);

		assertThat(dtoSchueler.LSSchulNr).isNull();
	}

	@Test
	@DisplayName("mapAttribute | idVorherigeSchule | value nicht im Katalog")
	void mapAttributeTest_idVorherigeSchule_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "idVorherigeSchule", "123", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Schule mit der ID 123 gefunden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | idAufnehmendeSchule | Erfolg")
	void mapAttributeTest_idAufnehmendeSchule() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var schulnummer = "123123";
		final var idSchule = 111L;
		final var c = mock(DBEntityManager.class);
		lenient().when(c.queryAll(DTOSchuleNRW.class)).thenReturn(List.of(new DTOSchuleNRW(idSchule, schulnummer)));
		final var data = new DataSchuelerSchulbesuchsdaten(c);

		data.mapAttribute(dtoSchueler, "idAufnehmendeSchule", idSchule, null);

		assertThat(dtoSchueler.SchulwechselNr).isEqualTo(schulnummer);
	}

	@Test
	@DisplayName("mapAttribute | idAufnehmendeSchule | value is null")
	void mapAttributeTest_idAufnehmendeSchule_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.SchulwechselNr = "soll mit null ueberschrieben werden";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "idAufnehmendeSchule", null, null);

		assertThat(dtoSchueler.SchulwechselNr).isNull();
	}

	@Test
	@DisplayName("mapAttribute | idAufnehmendeSchule | value nicht im Katalog")
	void mapAttributeTest_idAufnehmendeSchule_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "idAufnehmendeSchule", 123, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Schule mit der ID 123 gefunden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | vorigeEntlassjahrgang | Erfolg")
	void mapAttributeTest_vorigeEntlassjahrgang() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var kuerzel = Jahrgaenge.EF.daten(2025).kuerzel;
		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "vorigeEntlassjahrgang", kuerzel, null);

		assertThat(dtoSchueler.LSJahrgang).isEqualTo(kuerzel);
	}

	@Test
	@DisplayName("mapAttribute | vorigeEntlassjahrgang | value is null")
	void mapAttributeTest_vorigeEntlassjahrgang_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.LSJahrgang = "soll mit null ueberschrieben werden";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "vorigeEntlassjahrgang", null, null);

		assertThat(dtoSchueler.LSJahrgang).isNull();
	}

	@Test
	@DisplayName("mapAttribute | vorigeEntlassjahrgang | value nicht im Katalog")
	void mapAttributeTest_vorigeEntlassjahrgang_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "vorigeEntlassjahrgang", "XX", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Jahrgang für das Kürzel XX gefunden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | entlassungJahrgang | Erfolg")
	void mapAttributeTest_entlassungJahrgang() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var kuerzel = Jahrgaenge.EF.daten(2025).kuerzel;
		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "entlassungJahrgang", kuerzel, null);

		assertThat(dtoSchueler.Entlassjahrgang).isEqualTo(kuerzel);
	}

	@Test
	@DisplayName("mapAttribute | entlassungJahrgang | value is null")
	void mapAttributeTest_entlassungJahrgang_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.Entlassjahrgang = "soll mit null ueberschrieben werden";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "entlassungJahrgang", null, null);

		assertThat(dtoSchueler.Entlassjahrgang).isNull();
	}

	@Test
	@DisplayName("mapAttribute | entlassungJahrgang | value nicht im Katalog")
	void mapAttributeTest_entlassungJahrgang_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "entlassungJahrgang", "XX", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Kein Jahrgang für das Kürzel XX gefunden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | vorigeArtLetzteVersetzung | Erfolg")
	void mapAttributeTest_vorigeArtLetzteVersetzung() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var id = String.valueOf(Herkunftsarten.AHR.daten.id);

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "vorigeArtLetzteVersetzung", id, null);

		assertThat(dtoSchueler.LSVersetzung).isEqualTo(id);
	}

	@Test
	@DisplayName("mapAttribute | vorigeArtLetzteVersetzung | value is null")
	void mapAttributeTest_vorigeArtLetzteVersetzung_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.LSVersetzung = "soll mit null ueberschrieben werden";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "vorigeArtLetzteVersetzung", null, null);

		assertThat(dtoSchueler.LSVersetzung).isNull();
	}

	@Test
	@DisplayName("mapAttribute | vorigeArtLetzteVersetzung | value nicht im Katalog")
	void mapAttributeTest_vorigeArtLetzteVersetzung_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "vorigeArtLetzteVersetzung", "123", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Herkunftsart für die ID: 123 gefunden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | vorigeEntlassgrundID | Erfolg")
	void mapAttributeTest_vorigeEntlassgrundID() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var c = mock(DBEntityManager.class);
		lenient().when(c.queryAll(DTOEntlassarten.class)).thenReturn(List.of(new DTOEntlassarten(123L, "test")));
		final var data = new DataSchuelerSchulbesuchsdaten(c);

		data.mapAttribute(dtoSchueler, "vorigeEntlassgrundID", 123L, null);

		assertThat(dtoSchueler.LSEntlassgrund).isEqualTo("test");
	}

	@Test
	@DisplayName("mapAttribute | vorigeEntlassgrundID | value is null")
	void mapAttributeTest_vorigeEntlassgrundID_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.LSEntlassgrund = "soll mit null ueberschrieben werden";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "vorigeEntlassgrundID", null, null);

		assertThat(dtoSchueler.LSEntlassgrund).isNull();
	}

	@Test
	@DisplayName("mapAttribute | vorigeEntlassgrundID | value nicht im Katalog")
	void mapAttributeTest_vorigeEntlassgrundID_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "vorigeEntlassgrundID", "123", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Entlassart mit der ID 123 gefunden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | entlassungGrundID | Erfolg")
	void mapAttributeTest_entlassungGrundID() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var c = mock(DBEntityManager.class);
		lenient().when(c.queryAll(DTOEntlassarten.class)).thenReturn(List.of(new DTOEntlassarten(123L, "test")));
		final var data = new DataSchuelerSchulbesuchsdaten(c);

		data.mapAttribute(dtoSchueler, "entlassungGrundID", 123L, null);

		assertThat(dtoSchueler.Entlassgrund).isEqualTo("test");
	}

	@Test
	@DisplayName("mapAttribute | entlassungGrundID | value is null")
	void mapAttributeTest_entlassungGrundID_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.Entlassgrund = "soll mit null ueberschrieben werden";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "entlassungGrundID", null, null);

		assertThat(dtoSchueler.Entlassgrund).isNull();
	}

	@Test
	@DisplayName("mapAttribute | entlassungGrundID | value nicht im Katalog")
	void mapAttributeTest_entlassungGrundID_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "entlassungGrundID", "123", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Entlassart mit der ID 123 gefunden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | grundschuleEinschulungsjahr | Erfolg")
	void mapAttributeTest_grundschuleEinschulungsjahr() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		schulbesuchsdaten.mapAttribute(dtoSchueler, "grundschuleEinschulungsjahr", 2011, null);

		assertThat(dtoSchueler.Einschulungsjahr).isEqualTo(2011);
	}

	@Test
	@DisplayName("mapAttribute | grundschuleEinschulungsjahr | value null")
	void mapAttributeTest_grundschuleEinschulungsjahr_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.Einschulungsjahr = 1999;

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "grundschuleEinschulungsjahr", null, null);

		assertThat(dtoSchueler.Einschulungsjahr).isNull();
	}

	@Test
	@DisplayName("mapAttribute | grundschuleEinschulungsjahr | value is smaller then min")
	void mapAttributeTest_grundschuleEinschulungsjahr_smallerThenMin() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "grundschuleEinschulungsjahr", 1899, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 1899 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | grundschuleEinschulungsjahr | value is bigger then max")
	void mapAttributeTest_grundschuleEinschulungsjahr_biggerThenMax() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "grundschuleEinschulungsjahr", 2101, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 2101 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | sekIWechsel | Erfolg")
	void mapAttributeTest_sekIWechsel() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIWechsel", 2011, null);

		assertThat(dtoSchueler.JahrWechsel_SI).isEqualTo(2011);
	}

	@Test
	@DisplayName("mapAttribute | sekIWechsel | value null")
	void mapAttributeTest_sekIWechsel_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.JahrWechsel_SI = 1999;

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIWechsel", null, null);

		assertThat(dtoSchueler.JahrWechsel_SI).isNull();
	}

	@Test
	@DisplayName("mapAttribute | sekIWechsel | value is smaller then min")
	void mapAttributeTest_sekIWechsel_smallerThenMin() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIWechsel", 1899, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 1899 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | sekIWechsel | value is bigger then max")
	void mapAttributeTest_sekIWechsel_biggerThenMax() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIWechsel", 2101, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 2101 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | sekIIWechsel | Erfolg")
	void mapAttributeTest_sekIIWechsel() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIIWechsel", 2011, null);

		assertThat(dtoSchueler.JahrWechsel_SII).isEqualTo(2011);
	}

	@Test
	@DisplayName("mapAttribute | sekIIWechsel | value null")
	void mapAttributeTest_sekIIWechsel_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.JahrWechsel_SII = 1999;

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIIWechsel", null, null);

		assertThat(dtoSchueler.JahrWechsel_SII).isNull();
	}

	@Test
	@DisplayName("mapAttribute | sekIIWechsel | value is smaller then min")
	void mapAttributeTest_sekIIWechsel_smallerThenMin() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIIWechsel", 1899, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 1899 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | sekIIWechsel | value is bigger then max")
	void mapAttributeTest_sekIIWechsel_biggerThenMax() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIIWechsel", 2101, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 2101 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | grundschuleJahreEingangsphase | Erfolg")
	void mapAttributeTest_grundschuleJahreEingangsphase() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var key = PrimarstufeSchuleingangsphaseBesuchsjahre.E2.daten(2025);
		schulbesuchsdaten.mapAttribute(dtoSchueler, "idGrundschuleJahreEingangsphase", key.id, null);

		assertThat(dtoSchueler.EPJahre).isEqualTo(Integer.valueOf(key.schluessel));
	}

	@Test
	@DisplayName("mapAttribute | grundschuleJahreEingangsphase | value is null")
	void mapAttributeTest_grundschuleJahreEingangsphase_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.EPJahre = 10;

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "idGrundschuleJahreEingangsphase", null, null);

		assertThat(dtoSchueler.EPJahre).isNull();
	}

	@Test
	@DisplayName("mapAttribute | grundschuleJahreEingangsphase | value nicht im Katalog")
	void mapAttributeTest_grundschuleJahreEingangsphase_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "idGrundschuleJahreEingangsphase", 15, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Eingangsphase mit der ID 15 vorhanden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelGrundschuleUebergangsempfehlung | Erfolg")
	void mapAttributeTest_kuerzelGrundschuleUebergangsempfehlung() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var kuerzel = Uebergangsempfehlung.GYMNASIUM.daten(2025).kuerzel;

		schulbesuchsdaten.mapAttribute(dtoSchueler, "kuerzelGrundschuleUebergangsempfehlung", kuerzel, null);

		assertThat(dtoSchueler.Uebergangsempfehlung_JG5).isEqualTo(kuerzel);
	}

	@Test
	@DisplayName("mapAttribute | kuerzelGrundschuleUebergangsempfehlung | value is null")
	void mapAttributeTest_kuerzelGrundschuleUebergangsempfehlung_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.Uebergangsempfehlung_JG5 = "test";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "kuerzelGrundschuleUebergangsempfehlung", null, null);

		assertThat(dtoSchueler.Uebergangsempfehlung_JG5).isNull();
	}

	@Test
	@DisplayName("mapAttribute | kuerzelGrundschuleUebergangsempfehlung | value nicht im Katalog")
	void mapAttributeTest_kuerzelGrundschuleUebergangsempfehlung_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "kuerzelGrundschuleUebergangsempfehlung", "test", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Übergangsempfehlung für das Kürzel test gefunden.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("mapAttribute | sekIErsteSchulform | Erfolg")
	void mapAttributeTest_sekIErsteSchulform() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		final var kuerzel = Schulform.BK.daten(2025).kuerzel;

		schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIErsteSchulform", kuerzel, null);

		assertThat(dtoSchueler.ErsteSchulform_SI).isEqualTo(kuerzel);
	}

	@Test
	@DisplayName("mapAttribute | sekIErsteSchulform | value is null")
	void mapAttributeTest_sekIErsteSchulform_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.ErsteSchulform_SI = "test";

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIErsteSchulform", null, null);

		assertThat(dtoSchueler.ErsteSchulform_SI).isNull();
	}

	@Test
	@DisplayName("mapAttribute | sekIErsteSchulform | value nicht im Katalog")
	void mapAttributeTest_sekIErsteSchulform_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIErsteSchulform", "test", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Für das Kürzel test wurde keine Schulform gefunden")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 5),
				arguments("vorigeEntlassdatum", "2025-14-03"),
				arguments("vorigeBemerkung", "bemerkung"),
				arguments("entlassungDatum", "2025-14-03"),
				arguments("aufnehmendWechseldatum", "2025-14-03"),
				arguments("aufnehmendBestaetigt", true),
				arguments("unknown argument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var expectedDTO = new DTOSchueler(1, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(expectedDTO, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die ID 5 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "vorigeEntlassdatum" -> assertThat(expectedDTO.LSSchulEntlassDatum).isEqualTo(value);
			case "vorigeBemerkung" -> assertThat(expectedDTO.LSBemerkung).isEqualTo(value);
			case "entlassungDatum" -> assertThat(expectedDTO.Entlassdatum).isEqualTo(value);
			case "aufnehmendWechseldatum" -> assertThat(expectedDTO.Schulwechseldatum).isEqualTo(value);
			case "aufnehmendBestaetigt" -> assertThat(expectedDTO.WechselBestaetigt).isTrue();
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}
}
