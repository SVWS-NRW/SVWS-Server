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
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 * Prüft, dass das Lehrer-Repository die Initialkennwörter des Notenmoduls getrennt von den Stammdaten lädt. Der Vertrag: Ein Kennwort wird höchstens einmal
 * aus der Datenbank geholt, und eine Lehrkraft ohne Credentials liefert einen leeren String, ohne weitere Abfragen auszulösen.
 */
class TestReportingRepositoryLehrerNotenmodulKennwoerter {

	/** Die ID der Lehrkraft, zu der ein Initialkennwort hinterlegt ist. */
	private static final long ID_LEHRER = 7L;

	/** Die ID einer zweiten bekannten Lehrkraft, deren Kennwort in derselben Abfrage mitkommt. */
	private static final long ID_LEHRER_ZWEI = 8L;

	/** Die ID der Lehrkraft ohne Credentials für das Notenmodul. */
	private static final long ID_LEHRER_OHNE_CREDENTIALS = 9L;

	/** Das Initialkennwort, wie es die Datenbank führt. */
	private static final String KENNWORT = "aB3dE5fG7hJ9kL2m";

	/** Der Kennwort-Hash im Credential-Datensatz. Für die Ausgabe ist er ohne Belang, der Konstruktor verlangt ihn aber. */
	private static final String HASH = "$2a$10$abcdefghijklmnopqrstuv";

	/** Die Datenbankverbindung, über die das Repository die Kennwörter lädt. */
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
	 * Erzeugt den Datenbankeintrag der Notenmodul-Credentials einer Lehrkraft.
	 *
	 * @param idLehrer        Die ID der Lehrkraft.
	 * @param initialkennwort Das Initialkennwort.
	 *
	 * @return Der Eintrag, wie ihn die Abfrage liefert.
	 */
	private static DTONotenmodulCredentials dtoCredentials(final long idLehrer, final String initialkennwort) {
		return new DTONotenmodulCredentials(idLehrer, initialkennwort, true, HASH, 0, true);
	}

	/**
	 * Legt fest, was die Credential-Abfrage der Datenbank zurückgibt.
	 *
	 * @param eintraege Die Einträge, die die Abfrage liefert.
	 */
	private void gebeCredentialsVor(final List<DTONotenmodulCredentials> eintraege) {
		when(conn.queryByKeyList(eq(DTONotenmodulCredentials.class), anyCollection())).thenReturn(eintraege);
	}

	/**
	 * Erzeugt die Stammdaten einer Lehrkraft.
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


	/** Zu einer Lehrkraft mit Credentials liefert das Repository ihr Initialkennwort. */
	@Test
	void kennwortWirdGeliefert() {
		gebeCredentialsVor(List.of(dtoCredentials(ID_LEHRER, KENNWORT)));

		assertEquals(KENNWORT, repository.notenmodulInitialkennwort(ID_LEHRER));
	}

	/** Fehlen die Credentials, ist das kein Fehler: Das Repository liefert einen leeren String. */
	@Test
	void lehrkraftOhneCredentialsLiefertLeerenString() {
		gebeCredentialsVor(List.of());

		assertEquals("", repository.notenmodulInitialkennwort(ID_LEHRER_OHNE_CREDENTIALS));
	}

	/** Ein einmal geladenes Kennwort kommt aus dem Cache. */
	@Test
	void zweiterZugriffFragtDieDatenbankNichtErneut() {
		gebeCredentialsVor(List.of(dtoCredentials(ID_LEHRER, KENNWORT)));

		repository.notenmodulInitialkennwort(ID_LEHRER);
		repository.notenmodulInitialkennwort(ID_LEHRER);

		verify(conn, times(1)).queryByKeyList(eq(DTONotenmodulCredentials.class), anyCollection());
	}

	/** Auch das Fehlen der Credentials wird gemerkt, sonst fragte jeder weitere Zugriff die Datenbank erneut. */
	@Test
	void zweiterZugriffOhneCredentialsFragtDieDatenbankNichtErneut() {
		gebeCredentialsVor(List.of());

		repository.notenmodulInitialkennwort(ID_LEHRER_OHNE_CREDENTIALS);
		repository.notenmodulInitialkennwort(ID_LEHRER_OHNE_CREDENTIALS);

		verify(conn, times(1)).queryByKeyList(eq(DTONotenmodulCredentials.class), anyCollection());
	}

	/** Der erste Zugriff holt die Kennwörter aller bekannten Lehrkräfte in einer Abfrage; die zweite Lehrkraft löst danach keine weitere aus. */
	@Test
	void bekannteLehrkraefteWerdenGemeinsamGeladen() {
		gebeCredentialsVor(List.of(dtoCredentials(ID_LEHRER, KENNWORT), dtoCredentials(ID_LEHRER_ZWEI, KENNWORT)));
		gebeVollbestandVor(List.of(stammdaten(ID_LEHRER), stammdaten(ID_LEHRER_ZWEI)));

		repository.notenmodulInitialkennwort(ID_LEHRER);

		final ArgumentCaptor<Collection<Long>> ids = ArgumentCaptor.captor();
		verify(conn, times(1)).queryByKeyList(eq(DTONotenmodulCredentials.class), ids.capture());
		assertTrue(ids.getValue().containsAll(List.of(ID_LEHRER, ID_LEHRER_ZWEI)), "Die Abfrage umfasst beide bekannten Lehrkräfte.");

		assertEquals(KENNWORT, repository.notenmodulInitialkennwort(ID_LEHRER_ZWEI));
		verify(conn, times(1)).queryByKeyList(eq(DTONotenmodulCredentials.class), anyCollection());
	}

	/** Ein Ladefehler beendet die Ausgabe nicht; die Lehrkraft erscheint dann ohne Kennwort. */
	@Test
	void ladefehlerLiefertLeerenString() {
		when(conn.queryByKeyList(eq(DTONotenmodulCredentials.class), anyCollection())).thenThrow(new IllegalStateException("Verbindung verloren"));

		assertEquals("", repository.notenmodulInitialkennwort(ID_LEHRER));
	}

	/** Ein erfolgreicher Zugriff meldet kein Problem, auch wenn die Lehrkraft gar keine Credentials besitzt. */
	@Test
	void erfolgreicherZugriffMeldetKeinProblem() {
		gebeCredentialsVor(List.of());

		repository.notenmodulInitialkennwort(ID_LEHRER_OHNE_CREDENTIALS);

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	/** Ein Ladefehler wird als Ausgabeproblem gemeldet, sonst verschwände er ohne jede Spur. */
	@Test
	void ladefehlerWirdAlsAusgabeproblemGemeldet() {
		when(conn.queryByKeyList(eq(DTONotenmodulCredentials.class), anyCollection())).thenThrow(new IllegalStateException("Verbindung verloren"));

		repository.notenmodulInitialkennwort(ID_LEHRER);

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingLehrer.class, ID_LEHRER)), anyString(), any());
	}

}
