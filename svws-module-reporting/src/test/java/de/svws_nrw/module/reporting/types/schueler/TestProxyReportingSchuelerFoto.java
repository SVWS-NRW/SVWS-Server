package de.svws_nrw.module.reporting.types.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchueler;

/**
 * Prüft, dass der Schüler-Proxy sein Foto erst beim Zugriff nachlädt. Die Stammdaten kommen ohne Fotos; wer keines abruft, löst auch keine Abfrage aus.
 */
class TestProxyReportingSchuelerFoto {

	/** Die ID des Schülers, dessen Proxy erzeugt wird. */
	private static final long ID_SCHUELER = 17L;

	/** Das Foto im Base64-Format. */
	private static final String FOTO_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

	/** Der gemockte Context, den der Proxy erhält. */
	private ReportingContext reportingContext;

	/** Das gemockte Repository, über das der Proxy das Foto nachlädt. */
	private ReportingRepositorySchueler repositorySchueler;


	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class, RETURNS_DEEP_STUBS);
		repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);
	}

	/**
	 * Erzeugt den Proxy zu Stammdaten ohne Foto, wie sie das Repository liefert.
	 *
	 * @return Der Proxy unter Test.
	 */
	private ProxyReportingSchueler proxy() {
		final SchuelerStammdaten stammdaten = new SchuelerStammdaten();
		stammdaten.id = ID_SCHUELER;
		return new ProxyReportingSchueler(reportingContext, stammdaten);
	}


	/** Solange niemand das Foto abruft, bleibt die Datenbank unbehelligt. */
	@Test
	void ohneZugriffWirdNichtsGeladen() {
		proxy();

		verify(repositorySchueler, times(0)).schuelerFoto(ID_SCHUELER);
	}

	/** Der erste Zugriff holt das Foto nach. */
	@Test
	void ersterZugriffLaedtNach() {
		when(repositorySchueler.schuelerFoto(ID_SCHUELER)).thenReturn(FOTO_BASE64);

		assertEquals(FOTO_BASE64, proxy().foto());
	}

	/** Der Proxy lädt höchstens einmal nach. */
	@Test
	void zweiterZugriffLaedtNichtErneut() {
		when(repositorySchueler.schuelerFoto(ID_SCHUELER)).thenReturn(FOTO_BASE64);

		final ProxyReportingSchueler schueler = proxy();
		schueler.foto();
		schueler.foto();

		verify(repositorySchueler, times(1)).schuelerFoto(ID_SCHUELER);
	}

	/** Auch ohne hinterlegtes Foto bleibt es bei einem Ladeversuch; der leere String ist ein Ergebnis, kein fehlender Wert. */
	@Test
	void schuelerOhneFotoLaedtNurEinmalNach() {
		when(repositorySchueler.schuelerFoto(ID_SCHUELER)).thenReturn("");

		final ProxyReportingSchueler schueler = proxy();

		assertEquals("", schueler.foto());
		assertEquals("", schueler.foto());
		verify(repositorySchueler, times(1)).schuelerFoto(ID_SCHUELER);
	}

	/** Die Bildquelle wird aus dem nachgeladenen Foto abgeleitet und nicht aus dem noch leeren Feld. */
	@Test
	void fotoHtmlSourceNutztDasNachgeladeneFoto() {
		when(repositorySchueler.schuelerFoto(ID_SCHUELER)).thenReturn(FOTO_BASE64);

		assertEquals("data:image/png;base64," + FOTO_BASE64, proxy().fotoHtmlSource());
	}

}
