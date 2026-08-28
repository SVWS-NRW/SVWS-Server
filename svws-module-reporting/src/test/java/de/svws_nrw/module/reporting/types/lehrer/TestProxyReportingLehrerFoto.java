package de.svws_nrw.module.reporting.types.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
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
 * Prüft, wann der Lehrer-Proxy sein Foto nachlädt. Bringen die Stammdaten das Foto bereits mit, unterbleibt das Nachladen; kommen sie ohne Foto, holt der
 * Proxy es beim ersten Zugriff und danach nicht erneut.
 */
class TestProxyReportingLehrerFoto {

	/** Die ID der Lehrkraft, deren Proxy erzeugt wird. */
	private static final long ID_LEHRER = 7L;

	/** Das Foto im Base64-Format. */
	private static final String FOTO_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

	/** Der gemockte Context, den der Proxy erhält. */
	private ReportingContext reportingContext;

	/** Das gemockte Repository, über das der Proxy das Foto nachlädt. */
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
	 * @param foto Das Foto im Base64-Format oder {@code null}, wenn die Stammdaten ohne Foto geladen wurden.
	 *
	 * @return Die Stammdaten für den Proxy.
	 */
	private static LehrerStammdaten stammdaten(final String foto) {
		final LehrerStammdaten stammdaten = new LehrerStammdaten();
		stammdaten.id = ID_LEHRER;
		stammdaten.foto = foto;
		return stammdaten;
	}


	/** Bringen die Stammdaten das Foto mit, wird es unverändert geliefert und nicht erneut geholt. */
	@Test
	void stammdatenMitFotoLoesenKeinNachladenAus() {
		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten(FOTO_BASE64));

		assertEquals(FOTO_BASE64, lehrer.foto());
		verify(repositoryLehrer, never()).lehrerFoto(anyLong());
	}

	/** Fehlt das Foto in den Stammdaten, holt der Proxy es beim ersten Zugriff nach. */
	@Test
	void stammdatenOhneFotoLoesenNachladenAus() {
		when(repositoryLehrer.lehrerFoto(ID_LEHRER)).thenReturn(FOTO_BASE64);

		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten(null));

		assertEquals(FOTO_BASE64, lehrer.foto());
		verify(repositoryLehrer, times(1)).lehrerFoto(ID_LEHRER);
	}

	/** Der Proxy lädt höchstens einmal nach. */
	@Test
	void zweiterZugriffLaedtNichtErneut() {
		when(repositoryLehrer.lehrerFoto(ID_LEHRER)).thenReturn(FOTO_BASE64);

		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten(null));
		lehrer.foto();
		lehrer.foto();

		verify(repositoryLehrer, times(1)).lehrerFoto(ID_LEHRER);
	}

	/** Auch ohne hinterlegtes Foto bleibt es bei einem Ladeversuch; der leere String ist ein Ergebnis, kein fehlender Wert. */
	@Test
	void lehrkraftOhneFotoLaedtNurEinmalNach() {
		when(repositoryLehrer.lehrerFoto(ID_LEHRER)).thenReturn("");

		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten(null));

		assertEquals("", lehrer.foto());
		assertEquals("", lehrer.foto());
		verify(repositoryLehrer, times(1)).lehrerFoto(ID_LEHRER);
	}

	/** Die Bildquelle wird aus dem nachgeladenen Foto abgeleitet und nicht aus dem noch leeren Feld. */
	@Test
	void fotoHtmlSourceNutztDasNachgeladeneFoto() {
		when(repositoryLehrer.lehrerFoto(ID_LEHRER)).thenReturn(FOTO_BASE64);

		final ProxyReportingLehrer lehrer = new ProxyReportingLehrer(reportingContext, stammdaten(null));

		assertEquals("data:image/png;base64," + FOTO_BASE64, lehrer.fotoHtmlSource());
	}

}
