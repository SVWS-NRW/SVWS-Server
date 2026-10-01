package de.svws_nrw.module.reporting.types.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryLehrer;

/**
 * Prüft, wann der Lehrer-Proxy das Initialkennwort des Notenmoduls nachlädt. Die Stammdaten führen es nicht mit, deshalb holt der Proxy es beim ersten
 * Zugriff und danach nicht erneut. Eine Ausgabe, die das Kennwort nicht zeigt, löst gar keinen Ladevorgang aus.
 */
class TestProxyReportingLehrerNotenmodulKennwort {

	/** Die ID der Lehrkraft, deren Proxy erzeugt wird. */
	private static final long ID_LEHRER = 7L;

	/** Das Initialkennwort für das Notenmodul. */
	private static final String KENNWORT = "aB3dE5fG7hJ9kL2m";

	/** Der gemockte Context, den der Proxy erhält. */
	private ReportingContext reportingContext;

	/** Das gemockte Repository, über das der Proxy das Kennwort nachlädt. */
	private ReportingRepositoryLehrer repositoryLehrer;


	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class, RETURNS_DEEP_STUBS);
		repositoryLehrer = mock(ReportingRepositoryLehrer.class);
		when(reportingContext.repositoryLehrer()).thenReturn(repositoryLehrer);
	}

	/**
	 * Erzeugt die Stammdaten der Lehrkraft.
	 *
	 * @return Die Stammdaten für den Proxy.
	 */
	private static LehrerStammdaten stammdaten() {
		final LehrerStammdaten stammdaten = new LehrerStammdaten();
		stammdaten.id = ID_LEHRER;
		return stammdaten;
	}


	/** Der Proxy holt das Kennwort beim ersten Zugriff aus dem Repository. */
	@Test
	void ersterZugriffLaedtDasKennwort() {
		when(repositoryLehrer.notenmodulInitialkennwort(ID_LEHRER)).thenReturn(KENNWORT);

		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten());

		assertEquals(KENNWORT, lehrer.notenmodulInitialkennwort());
		verify(repositoryLehrer, times(1)).notenmodulInitialkennwort(ID_LEHRER);
	}

	/** Der Proxy lädt höchstens einmal nach. */
	@Test
	void zweiterZugriffLaedtNichtErneut() {
		when(repositoryLehrer.notenmodulInitialkennwort(ID_LEHRER)).thenReturn(KENNWORT);

		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten());
		lehrer.notenmodulInitialkennwort();
		lehrer.notenmodulInitialkennwort();

		verify(repositoryLehrer, times(1)).notenmodulInitialkennwort(ID_LEHRER);
	}

	/** Auch ohne hinterlegtes Kennwort bleibt es bei einem Ladeversuch; der leere String ist ein Ergebnis, kein fehlender Wert. */
	@Test
	void lehrkraftOhneKennwortLaedtNurEinmalNach() {
		when(repositoryLehrer.notenmodulInitialkennwort(ID_LEHRER)).thenReturn("");

		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten());

		assertEquals("", lehrer.notenmodulInitialkennwort());
		assertEquals("", lehrer.notenmodulInitialkennwort());
		verify(repositoryLehrer, times(1)).notenmodulInitialkennwort(ID_LEHRER);
	}

	/** Eine Ausgabe ohne Kennwort fragt es gar nicht erst an; das Erzeugen des Proxys allein löst keinen Ladevorgang aus. */
	@Test
	void ohneZugriffWirdNichtGeladen() {
		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten());

		assertEquals(ID_LEHRER, lehrer.id());
		verify(repositoryLehrer, never()).notenmodulInitialkennwort(ID_LEHRER);
	}

}
