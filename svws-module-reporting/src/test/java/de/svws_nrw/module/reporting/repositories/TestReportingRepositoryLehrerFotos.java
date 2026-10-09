package de.svws_nrw.module.reporting.repositories;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFoto;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.service.lehrer.foto.LehrerFoto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Prüft, dass das Lehrer-Repository die Fotos getrennt von den Stammdaten lädt. Der Vertrag: Ein Foto wird höchstens einmal aus der Datenbank geholt, und
 * eine Lehrkraft ohne hinterlegtes Foto liefert einen leeren String, ohne weitere Abfragen auszulösen.
 */
class TestReportingRepositoryLehrerFotos {

	private static final long ID_LEHRER = 7L;
	private static final long ID_LEHRER_ZWEI = 8L;
	private static final long ID_LEHRER_OHNE_FOTO = 9L;
	private static final String FOTO_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

	private DBEntityManager conn;
	private ReportingContext reportingContext;
	private ReportingRepositoryLehrer repository;

	private MockedStatic<DbConnectionProvider> dbConnectionProvider;

	@BeforeEach
	void setUp() {
		conn = mock(DBEntityManager.class);

		dbConnectionProvider = mockStatic(DbConnectionProvider.class);
		dbConnectionProvider.when(DbConnectionProvider::getConnection).thenReturn(conn);

		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerList());

		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);
		when(reportingContext.conn()).thenReturn(conn);

		repository = new ReportingRepositoryLehrer(reportingContext);
	}

	@AfterEach
	void tearDown() {
		dbConnectionProvider.close();
	}

	private static LehrerFoto lehrerFoto(final long idLehrer, final String base64) {
		return new LehrerFoto(idLehrer, base64);
	}

	private void gebeFotosVor(final List<LehrerFoto> fotos) {
		when(conn.queryByKeyList(eq(DTOLehrerFoto.class), anyCollection()))
				.thenAnswer(invocation -> {
					final Collection<Long> ids = invocation.getArgument(1);
					return fotos.stream()
							.filter(f -> ids.contains(f.idLehrer()))
							.map(f -> {
								final DTOLehrerFoto dto = new DTOLehrerFoto(f.idLehrer());
								dto.fotoBase64 = f.fotoBase64();
								return dto;
							})
							.toList();
				});
	}


	/**
	 * Erzeugt die Stammdaten einer Lehrkraft ohne Foto, wie sie das Laden ohne Bilddaten liefert.
	 *
	 * @param idLehrer Die ID der Lehrkraft.
	 *
	 * @return Die Stammdaten.
	 */
	private static LehrerStammdaten stammdaten(final long idLehrer) {
		final LehrerStammdaten stammdaten = new LehrerStammdaten();
		stammdaten.id = idLehrer;
		return stammdaten;
	}


	/**
	 * Legt den Vollbestand der Lehrerstammdaten in den Cache. Das Repository holt ihn beim Registrieren der ersten Lehrkraft, deshalb wird für diesen einen
	 * Aufruf die Datenklasse ersetzt.
	 *
	 * @param alle Die Stammdaten aller Lehrkräfte.
	 */
	private void gebeVollbestandVor(final List<LehrerStammdaten> alle) {
		try (MockedConstruction<DataLehrerStammdaten> datenklasse = mockConstruction(DataLehrerStammdaten.class,
				(dataLehrerStammdaten, kontext) -> when(dataLehrerStammdaten.getAllOhneFotos()).thenReturn(alle))) {
			repository.registriereStammdaten(alle.getFirst().id, alle.getFirst());
		}
	}

	@Test
	@DisplayName("Zu einer Lehrkraft mit hinterlegtem Foto liefert das Repository dessen Base64-Daten.")
	void lehrerFotoLiefertDasHinterlegteFoto() {
		gebeFotosVor(List.of(lehrerFoto(ID_LEHRER, FOTO_BASE64)));

		assertEquals(FOTO_BASE64, repository.lehrerFoto(ID_LEHRER));
	}

	@Test
	@DisplayName("Fehlt das Foto, ist das kein Fehler: Das Repository liefert einen leeren String.")
	void lehrerOhneFotoLiefertLeerenString() {
		gebeFotosVor(List.of());

		assertEquals("", repository.lehrerFoto(ID_LEHRER_OHNE_FOTO));
	}

	@Test
	@DisplayName("Ein einmal geladenes Foto kommt aus dem Cache.")
	void zweiterZugriffFragtDieDatenbankNichtErneut() {
		gebeFotosVor(List.of(lehrerFoto(ID_LEHRER, FOTO_BASE64)));

		repository.lehrerFoto(ID_LEHRER);
		repository.lehrerFoto(ID_LEHRER);

		verify(conn, times(1)).queryByKeyList(eq(DTOLehrerFoto.class), anyCollection());
	}

	@Test
	@DisplayName("Auch das Fehlen eines Fotos wird gemerkt, sonst fragte jeder weitere Zugriff die Datenbank erneut.")
	void zweiterZugriffOhneFotoFragtDieDatenbankNichtErneut() {
		gebeFotosVor(List.of());

		repository.lehrerFoto(ID_LEHRER_OHNE_FOTO);
		repository.lehrerFoto(ID_LEHRER_OHNE_FOTO);

		verify(conn, times(1)).queryByKeyList(eq(DTOLehrerFoto.class), anyCollection());
	}

	@Test
	@DisplayName("Der erste Zugriff holt die Fotos aller bekannten Lehrkräfte in einer Abfrage; die zweite Lehrkraft löst danach keine weitere aus.")
	void bekannteLehrkraefteWerdenGemeinsamGeladen() {
		gebeFotosVor(List.of(lehrerFoto(ID_LEHRER, FOTO_BASE64), lehrerFoto(ID_LEHRER_ZWEI, FOTO_BASE64)));
		gebeVollbestandVor(List.of(stammdaten(ID_LEHRER), stammdaten(ID_LEHRER_ZWEI)));

		repository.lehrerFoto(ID_LEHRER);

		final ArgumentCaptor<Collection<Long>> ids = ArgumentCaptor.captor();
		verify(conn, times(1)).queryByKeyList(eq(DTOLehrerFoto.class), ids.capture());
		assertTrue(ids.getValue().containsAll(List.of(ID_LEHRER, ID_LEHRER_ZWEI)), "Die Abfrage umfasst beide bekannten Lehrkräfte.");

		assertEquals(FOTO_BASE64, repository.lehrerFoto(ID_LEHRER_ZWEI));
		verify(conn, times(1)).queryByKeyList(eq(DTOLehrerFoto.class), anyCollection());
	}

	@Test
	@DisplayName("Ein Ladefehler beendet die Ausgabe nicht; die Lehrkraft erscheint dann ohne Foto.")
	void ladefehlerLiefertLeerenString() {
		when(conn.queryByKeyList(eq(DTOLehrerFoto.class), anyCollection()))
				.thenThrow(new IllegalStateException("Verbindung verloren"));

		assertEquals("", repository.lehrerFoto(ID_LEHRER));
	}

	@Test
	@DisplayName("Ein erfolgreicher Zugriff meldet kein Problem, auch wenn zu der Lehrkraft gar kein Foto hinterlegt ist.")
	void erfolgreicherZugriffMeldetKeinProblem() {
		gebeFotosVor(List.of());

		repository.lehrerFoto(ID_LEHRER_OHNE_FOTO);

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	@DisplayName("Ein Ladefehler wird als Ausgabeproblem gemeldet, sonst verschwände er ohne jede Spur.")
	void ladefehlerWirdAlsAusgabeproblemGemeldet() {
		when(conn.queryByKeyList(eq(DTOLehrerFoto.class), anyCollection()))
				.thenThrow(new IllegalStateException("Verbindung verloren"));

		repository.lehrerFoto(ID_LEHRER);

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingLehrer.class, ID_LEHRER)), anyString(), any());
	}
}
