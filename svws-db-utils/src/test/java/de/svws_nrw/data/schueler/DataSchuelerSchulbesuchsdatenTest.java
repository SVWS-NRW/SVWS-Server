package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.schueler.Herkunftsarten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Stream;
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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
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

	/** --- mapAttribute Tests ---*/

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
	void mapAttributeTest_grundschuleEinschulungsjahr_smallerThenMin() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "grundschuleEinschulungsjahr", 1899, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 1899 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | grundschuleEinschulungsjahr | value is bigger then max")
	void mapAttributeTest_grundschuleEinschulungsjahr_biggerThenMax() throws ApiOperationException {
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
	void mapAttributeTest_sekIWechsel_smallerThenMin() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIWechsel", 1899, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 1899 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | sekIWechsel | value is bigger then max")
	void mapAttributeTest_sekIWechsel_biggerThenMax() throws ApiOperationException {
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
	void mapAttributeTest_sekIIWechsel_smallerThenMin() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() ->  this.schulbesuchsdaten.mapAttribute(dtoSchueler, "sekIIWechsel", 1899, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Jahr 1899 ist nicht gültig. Es muss zwischen 1900 und 2100 liegen.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | sekIIWechsel | value is bigger then max")
	void mapAttributeTest_sekIIWechsel_biggerThenMax() throws ApiOperationException {
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
		final var key = PrimarstufeSchuleingangsphaseBesuchsjahre.E2.daten(2025).schluessel;
		schulbesuchsdaten.mapAttribute(dtoSchueler, "grundschuleJahreEingangsphase", Integer.valueOf(key), null);

		assertThat(dtoSchueler.EPJahre).isEqualTo(Integer.valueOf(key));
	}

	@Test
	@DisplayName("mapAttribute | grundschuleJahreEingangsphase | value is null")
	void mapAttributeTest_grundschuleJahreEingangsphase_null() throws ApiOperationException {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);
		dtoSchueler.EPJahre = 10;

		this.schulbesuchsdaten.mapAttribute(dtoSchueler, "grundschuleJahreEingangsphase", null, null);

		assertThat(dtoSchueler.EPJahre).isNull();
	}

	@Test
	@DisplayName("mapAttribute | grundschuleJahreEingangsphase | value nicht im Katalog")
	void mapAttributeTest_grundschuleJahreEingangsphase_exception() {
		final var dtoSchueler = new DTOSchueler(1L, "1", true);

		final var throwable = catchThrowable(() -> this.schulbesuchsdaten.mapAttribute(dtoSchueler, "grundschuleJahreEingangsphase", 15, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Eingangsphase mit dem Schlüssel 15 vorhanden.")
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
					.hasMessageStartingWith("IdPatch 5 ist ungleich idSchueler 1.")
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
