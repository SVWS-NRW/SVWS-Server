package de.svws_nrw.data.klassen;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataKlassendatenTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKlassendaten cut;

	@Test
	@DisplayName("getDTOTeilstandort | Es existiert kein Teilstandort => ApiOperationException mit Status NOT_FOUND")
	void getDTOTeilstandort_Found() throws ApiOperationException {
		final DTOTeilstandorte teilstandortMock = mock(DTOTeilstandorte.class);
		when(conn.querySingle(DTOTeilstandorte.class)).thenReturn(teilstandortMock);

		final DTOTeilstandorte result = cut.getDTOTeilstandort();

		assertThat(result).isEqualTo(teilstandortMock);
	}

	@Test
	@DisplayName("getDTOTeilstandort | Es existiert kein Teilstandort => ApiOperationException mit Status NOT_FOUND")
	void getDTOTeilstandort_NotFound() {
		when(conn.querySingle(DTOTeilstandorte.class)).thenReturn(null);

		final Throwable result = catchThrowable(() -> cut.getDTOTeilstandort());

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Es ist kein Teilstandort definiert, es muss mindestens ein Teilstandort hinterlegt sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getDTOEigeneSchule | Es existiert eine Schule => DTOEigeneSchule zur Schule")
	void getDTOEigeneSchule_Found() throws ApiOperationException {
		final DTOEigeneSchule schuleMock = mock(DTOEigeneSchule.class);
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schuleMock);

		final DTOEigeneSchule result = cut.getDTOEigeneSchule();

		assertThat(result).isEqualTo(schuleMock);
	}

	@Test
	@DisplayName("getDTOEigeneSchule | Es existiert keine Schule => ApiOperationException mit dem Status NOT_FOUND")
	void getDTOEigeneSchule_NotFound() {
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(null);

		final Throwable result = catchThrowable(() -> cut.getDTOEigeneSchule());

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Es konnten keine Informationen zur Schule gefunden werden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("checkDeletePreConditions | Es wird eine löschbare Klasse übergeben => SimpleOperationResponse mit leerem Log")
	void checkDeletePreConditions_Valid() {
		cut = spy(cut);
		doReturn(Collections.EMPTY_LIST).when(cut).getSchuelerIDsByKlassenID(1L);

		final DTOKlassen klasse = new DTOKlassen(1L, 1, "5a");

		final SimpleOperationResponse result = cut.checkDeletePreConditions(klasse);
		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(e -> e.log, as(InstanceOfAssertFactories.LIST)).isEmpty();
	}

	@Test
	@DisplayName("checkDeletePreConditions | Es wird eine Klasse mit einem verknüpften Schüler übergeben => SimpleOperationResponse mit einem Log-Eintrag zu "
			+ "verknüpftem Schüler")
	void checkDeletePreConditions_KlasseExists() {
		cut = spy(cut);
		doReturn(List.of(99L)).when(cut).getSchuelerIDsByKlassenID(1L);

		final DTOKlassen klasse = new DTOKlassen(1L, 1, "5a");

		final SimpleOperationResponse result = cut.checkDeletePreConditions(klasse);
		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(e -> e.log, as(InstanceOfAssertFactories.LIST)).containsExactly("Klasse 5a (ID: 1) hat noch 1 verknüpfte(n) Schüler.");
	}

	@Test
	@DisplayName("checkDeletePreConditions | Es wird eine Klasse mit zwei verknüpften Schülern übergeben => SimpleOperationResponse mit einem Log-Eintrag zu "
			+ "verknüpften Schülern")
	void checkDeletePreConditions_KlassenExist() {
		cut = spy(cut);
		doReturn(List.of(99L, 44L)).when(cut).getSchuelerIDsByKlassenID(1L);

		final DTOKlassen klasse = new DTOKlassen(1L, 1, "5a");

		final SimpleOperationResponse result = cut.checkDeletePreConditions(klasse);
		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(e -> e.log, as(InstanceOfAssertFactories.LIST)).containsExactly("Klasse 5a (ID: 1) hat noch 2 verknüpfte(n) Schüler.");
	}

	@Test
	@DisplayName("getSchulgliederungIdByKlasseAndSchulform | Es wird eine Klasse und die Schulform 'GY' übergeben => Die Schulgliederung zu 'GY' mit "
			+ "der ID 15005000")
	void getSchulgliederungIdByKlasseAndSchulform_Found() {
		final DTOKlassen dtoKlasse = new DTOKlassen(1L, 1L, "5a");
		dtoKlasse.ASDSchulformNr = "20";
		final Schulform schulform = Schulform.GY;

		try (MockedStatic<Schulgliederung> mocked = mockStatic(Schulgliederung.class)) {
			mocked.when(() -> Schulgliederung.getBySchulformAndKuerzel(schulform, "20")).thenReturn(Schulgliederung.GY);

			final Long result = cut.getSchulgliederungIdByKlasseAndSchulform(dtoKlasse, schulform);

			assertThat(result).isEqualTo(15005000);
		}
	}

	@Test
	@DisplayName("getSchulgliederungIdByKlasseAndSchulform | Es wird eine Klasse und eine Schulform übergeben, zu der keine Schulgliederung existiert => "
			+ "Default Schulgliederung mit der ID 0")
	void getSchulgliederungIdByKlasseAndSchulform_NotFound() {
		final DTOKlassen dtoKlasse = new DTOKlassen(1L, 1L, "5a");
		dtoKlasse.ASDSchulformNr = "20";
		final Schulform schulform = Schulform.GY;

		try (MockedStatic<Schulgliederung> mocked = mockStatic(Schulgliederung.class)) {
			mocked.when(() -> Schulgliederung.getBySchulformAndKuerzel(schulform, "20")).thenReturn(null);
			mocked.when(() -> Schulgliederung.getDefault(schulform)).thenReturn(Schulgliederung.DEFAULT);

			final Long result = cut.getSchulgliederungIdByKlasseAndSchulform(dtoKlasse, schulform);

			assertThat(result).isZero();
		}
	}

	@Test
	@DisplayName("getSchulgliederungIdByKlasseAndSchulform | Es wird eine Klasse und 'null' als Schulform übergeben => Keine Schulgliederung daher -1")
	void getSchulgliederungIdByKlasseAndSchulform_Fallback() {
		final DTOKlassen dtoKlasse = new DTOKlassen(1L, 1L, "5a");
		dtoKlasse.ASDSchulformNr = "20";

		try (MockedStatic<Schulgliederung> mocked = mockStatic(Schulgliederung.class)) {
			mocked.when(() -> Schulgliederung.getBySchulformAndKuerzel(null, "20")).thenReturn(null);
			mocked.when(() -> Schulgliederung.getDefault(null)).thenReturn(null);

			final Long result = cut.getSchulgliederungIdByKlasseAndSchulform(dtoKlasse, null);

			assertThat(result).isEqualTo(-1);
		}
	}

	@Test
	@DisplayName("getKlassenBySchuljahresabschnittId | Es wird 'null' als ID übergeben => leere Map")
	void getKlassenBySchuljahresabschnittId_NullEmpty() {
		final Map<String, DTOKlassen> result = cut.getKlassenBySchuljahresabschnittId(null);

		assertThat(result).isEmpty();
		verify(conn, times(0)).queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L);
	}

	@Test
	@DisplayName("getKlassenBySchuljahresabschnittId | Es wird eine ID übergeben, mit einer verknüpften Klasse => Map mit der verknüpften Klasse")
	void getKlassenBySchuljahresabschnittId_Found() {
		final DTOKlassen klasse = new DTOKlassen(10L, 1L, "5a");
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L)).thenReturn(List.of(klasse));

		final Map<String, DTOKlassen> result = cut.getKlassenBySchuljahresabschnittId(1L);

		assertThat(result).isEqualTo(Map.of("5a", klasse));
		verify(conn, times(1)).queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L);
	}

	@Test
	@DisplayName("getKlassenBySchuljahresabschnittId | Es wird eine ID übergeben, ohne verknüpfte Klassen => leere Map")
	void getKlassenBySchuljahresabschnittId_NotFound() {
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L)).thenReturn(Collections.emptyList());

		final Map<String, DTOKlassen> result = cut.getKlassenBySchuljahresabschnittId(1L);

		assertThat(result).isEmpty();
		verify(conn, times(1)).queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L);
	}
}
