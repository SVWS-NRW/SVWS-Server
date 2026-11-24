package de.svws_nrw.module.reporting.repositories;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Prüft, dass der Fächer-Manager eines Abiturjahrgangs immer mit dem Schuljahr des Abiturs entsteht. Davon hängt die Prüfungsordnung
 * ab. Der ausgewählte Abschnitt liegt beim Druck eines Wahlbogens oft in der EF oder Q1 und zählt deshalb nicht.
 */
@DisplayName("Der Fächer-Manager des GOSt-Repositorys")
class TestReportingRepositoryGostFaecherManager {

	/** Ein Abiturjahrgang, für den die Prüfungsordnung ab 2030 gilt. */
	private static final int ABITURJAHR = 2030;

	/** Die gemockte Datenbankverbindung. */
	private DBEntityManager conn;

	/** Der statische Mock für das Laden der Fächer. */
	private MockedStatic<DBUtilsFaecherGost> faecher;

	/** Der statische Mock für das Laden der Fachkombinationen. */
	private MockedStatic<DataGostJahrgangFachkombinationen> fachkombinationen;

	/** Das geprüfte Repository. */
	private ReportingRepositoryGost repository;


	@BeforeEach
	void setUp() {
		conn = mock(DBEntityManager.class);
		final ReportingContext reportingContext = mock(ReportingContext.class);
		when(reportingContext.conn()).thenReturn(conn);

		// Der ausgewählte Abschnitt liegt in der EF des Jahrgangs.
		final ReportingSchuljahresabschnitt auswahl = mock(ReportingSchuljahresabschnitt.class);
		when(auswahl.schuljahr()).thenReturn(ABITURJAHR - 3);
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.auswahlSchuljahresabschnitt()).thenReturn(auswahl);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		faecher = mockStatic(DBUtilsFaecherGost.class);
		faecher.when(() -> DBUtilsFaecherGost.getFaecherManager(anyInt(), eq(conn), eq(ABITURJAHR)))
				.thenAnswer(inv -> new GostFaecherManager(inv.getArgument(0, Integer.class)));
		fachkombinationen = mockStatic(DataGostJahrgangFachkombinationen.class);
		fachkombinationen.when(() -> DataGostJahrgangFachkombinationen.getFachkombinationen(conn, ABITURJAHR)).thenReturn(List.of());

		repository = new ReportingRepositoryGost(reportingContext);
	}

	@AfterEach
	void tearDown() {
		faecher.close();
		fachkombinationen.close();
	}


	@Test
	@DisplayName("faecherManager: Das Schuljahr ist das Schuljahr des Abiturs, auch wenn ein Abschnitt der EF ausgewählt ist")
	void testSchuljahrDesAbiturs() throws Exception {
		repository.faecherManager(ABITURJAHR);

		faecher.verify(() -> DBUtilsFaecherGost.getFaecherManager(ABITURJAHR - 1, conn, ABITURJAHR));
	}

}
