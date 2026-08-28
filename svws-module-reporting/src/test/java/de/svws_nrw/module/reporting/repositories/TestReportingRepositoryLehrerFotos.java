package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;

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

/**
 * Prüft, dass das Lehrer-Repository die Fotos getrennt von den Stammdaten lädt. Der Vertrag: Ein Foto wird höchstens einmal aus der Datenbank geholt, und
 * eine Lehrkraft ohne hinterlegtes Foto liefert einen leeren String, ohne weitere Abfragen auszulösen.
 */
class TestReportingRepositoryLehrerFotos {

	/** Die ID der Lehrkraft, zu der ein Foto hinterlegt ist. */
	private static final long ID_LEHRER = 7L;

	/** Die ID einer zweiten bekannten Lehrkraft, deren Foto in derselben Abfrage mitkommt. */
	private static final long ID_LEHRER_ZWEI = 8L;

	/** Die ID der Lehrkraft ohne hinterlegtes Foto. */
	private static final long ID_LEHRER_OHNE_FOTO = 9L;

	/** Das Foto im Base64-Format, wie es die Datenbank führt. */
	private static final String FOTO_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

	/** Die Datenbankverbindung, über die das Repository die Fotos lädt. */
	private DBEntityManager conn;

	/** Der gemockte Context, über den das Repository Ausgabeprobleme meldet. */
	private ReportingContext reportingContext;

	/** Das Repository unter Test. */
	private ReportingRepositoryLehrer repository;


	@BeforeEach
	void setUp() {
		conn = mock(DBEntityManager.class);

		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerList());

		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);
		when(reportingContext.conn()).thenReturn(conn);

		repository = new ReportingRepositoryLehrer(reportingContext);
	}

	/**
	 * Erzeugt den Datenbankeintrag eines Lehrerfotos.
	 *
	 * @param idLehrer Die ID der Lehrkraft.
	 * @param base64   Das Foto im Base64-Format.
	 *
	 * @return Der Eintrag, wie ihn die Abfrage liefert.
	 */
	private static DTOLehrerFoto dtoFoto(final long idLehrer, final String base64) {
		final DTOLehrerFoto dto = new DTOLehrerFoto(idLehrer);
		dto.FotoBase64 = base64;
		return dto;
	}

	/**
	 * Legt fest, was die Foto-Abfrage der Datenbank zurückgibt.
	 *
	 * @param eintraege Die Einträge, die die Abfrage liefert.
	 */
	private void gebeFotosVor(final List<DTOLehrerFoto> eintraege) {
		when(conn.queryByKeyList(eq(DTOLehrerFoto.class), anyCollection())).thenReturn(eintraege);
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
				(dataLehrerStammdaten, kontext) -> when(dataLehrerStammdaten.getAll()).thenReturn(alle))) {
			repository.registriereStammdaten(alle.getFirst().id, alle.getFirst());
		}
	}


	/** Zu einer Lehrkraft mit hinterlegtem Foto liefert das Repository dessen Base64-Daten. */
	@Test
	void lehrerFotoLiefertDasHinterlegteFoto() {
		gebeFotosVor(List.of(dtoFoto(ID_LEHRER, FOTO_BASE64)));

		assertEquals(FOTO_BASE64, repository.lehrerFoto(ID_LEHRER));
	}

	/** Fehlt das Foto, ist das kein Fehler: Das Repository liefert einen leeren String. */
	@Test
	void lehrerOhneFotoLiefertLeerenString() {
		gebeFotosVor(List.of());

		assertEquals("", repository.lehrerFoto(ID_LEHRER_OHNE_FOTO));
	}

	/** Ein einmal geladenes Foto kommt aus dem Cache. */
	@Test
	void zweiterZugriffFragtDieDatenbankNichtErneut() {
		gebeFotosVor(List.of(dtoFoto(ID_LEHRER, FOTO_BASE64)));

		repository.lehrerFoto(ID_LEHRER);
		repository.lehrerFoto(ID_LEHRER);

		verify(conn, times(1)).queryByKeyList(eq(DTOLehrerFoto.class), anyCollection());
	}

	/** Auch das Fehlen eines Fotos wird gemerkt, sonst fragte jeder weitere Zugriff die Datenbank erneut. */
	@Test
	void zweiterZugriffOhneFotoFragtDieDatenbankNichtErneut() {
		gebeFotosVor(List.of());

		repository.lehrerFoto(ID_LEHRER_OHNE_FOTO);
		repository.lehrerFoto(ID_LEHRER_OHNE_FOTO);

		verify(conn, times(1)).queryByKeyList(eq(DTOLehrerFoto.class), anyCollection());
	}

	/** Der erste Zugriff holt die Fotos aller bekannten Lehrkräfte in einer Abfrage; die zweite Lehrkraft löst danach keine weitere aus. */
	@Test
	void bekannteLehrkraefteWerdenGemeinsamGeladen() {
		gebeFotosVor(List.of(dtoFoto(ID_LEHRER, FOTO_BASE64), dtoFoto(ID_LEHRER_ZWEI, FOTO_BASE64)));
		gebeVollbestandVor(List.of(stammdaten(ID_LEHRER), stammdaten(ID_LEHRER_ZWEI)));

		repository.lehrerFoto(ID_LEHRER);

		final ArgumentCaptor<Collection<Long>> ids = ArgumentCaptor.captor();
		verify(conn, times(1)).queryByKeyList(eq(DTOLehrerFoto.class), ids.capture());
		assertTrue(ids.getValue().containsAll(List.of(ID_LEHRER, ID_LEHRER_ZWEI)), "Die Abfrage umfasst beide bekannten Lehrkräfte.");

		assertEquals(FOTO_BASE64, repository.lehrerFoto(ID_LEHRER_ZWEI));
		verify(conn, times(1)).queryByKeyList(eq(DTOLehrerFoto.class), anyCollection());
	}

	/** Ein Ladefehler beendet die Ausgabe nicht; die Lehrkraft erscheint dann ohne Foto. */
	@Test
	void ladefehlerLiefertLeerenString() {
		when(conn.queryByKeyList(eq(DTOLehrerFoto.class), anyCollection())).thenThrow(new IllegalStateException("Verbindung verloren"));

		assertEquals("", repository.lehrerFoto(ID_LEHRER));
	}

	/** Ein erfolgreicher Zugriff meldet kein Problem, auch wenn zu der Lehrkraft gar kein Foto hinterlegt ist. */
	@Test
	void erfolgreicherZugriffMeldetKeinProblem() {
		gebeFotosVor(List.of());

		repository.lehrerFoto(ID_LEHRER_OHNE_FOTO);

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	/** Ein Ladefehler wird als Ausgabeproblem gemeldet, sonst verschwände er ohne jede Spur. */
	@Test
	void ladefehlerWirdAlsAusgabeproblemGemeldet() {
		when(conn.queryByKeyList(eq(DTOLehrerFoto.class), anyCollection())).thenThrow(new IllegalStateException("Verbindung verloren"));

		repository.lehrerFoto(ID_LEHRER);

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingLehrer.class, ID_LEHRER)), anyString(), any());
	}

}
