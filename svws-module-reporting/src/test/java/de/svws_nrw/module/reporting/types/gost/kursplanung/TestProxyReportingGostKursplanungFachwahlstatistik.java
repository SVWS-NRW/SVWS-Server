package de.svws_nrw.module.reporting.types.gost.kursplanung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ReportingGostFachwahlstatistikHalbjahr;

/**
 * Tests der Kursgrößendifferenzen in {@link ProxyReportingGostKursplanungFachwahlstatistik}.
 * <p>Der Ergebnis-Manager meldet eine nicht vorhandene Fach-Kursart-Kombination mit einer {@link DeveloperNotificationException}; allein sie wird in den
 * Rückfallwert -1 übersetzt, den die Vorlage als Platzhalter zeigt. Jeder andere Fehler propagiert - er wäre sonst still als fehlende Kombination
 * gedeutet.</p>
 */
class TestProxyReportingGostKursplanungFachwahlstatistik {

	/** Die ID des Fachs, dessen Kursgrößendifferenzen ermittelt werden. */
	private static final long ID_FACH = 42L;


	/**
	 * Erzeugt eine Fachwahlstatistik, die auf ein Fach mit der Test-ID verweist.
	 *
	 * @return Die Fachwahlstatistik als Mock.
	 */
	private static ReportingGostFachwahlstatistikHalbjahr statistikMitFach() {
		final ReportingGostFachwahlstatistikHalbjahr statistik = mock(ReportingGostFachwahlstatistikHalbjahr.class);
		final ReportingFach fach = mock(ReportingFach.class);
		when(fach.id()).thenReturn(ID_FACH);
		when(statistik.fach()).thenReturn(fach);
		return statistik;
	}


	@Test
	void testEineNichtVorhandeneFachKursartKombinationErgibtDenRueckfallwert() {
		final GostBlockungsergebnisManager ergebnisManager = mock(GostBlockungsergebnisManager.class);
		when(ergebnisManager.getOfFachOfKursartKursdifferenz(ID_FACH, GostKursart.LK.id)).thenThrow(new DeveloperNotificationException("Fehlerinjektion"));
		when(ergebnisManager.getOfFachOfKursartKursdifferenz(ID_FACH, GostKursart.GK.id)).thenReturn(3);

		final ProxyReportingGostKursplanungFachwahlstatistik statistik =
				new ProxyReportingGostKursplanungFachwahlstatistik(statistikMitFach(), ergebnisManager);

		assertEquals(-1, statistik.differenzKursgroessenLK(), "Die nicht vorhandene Kombination erhält den Platzhalter-Wert der Vorlage.");
		assertEquals(3, statistik.differenzKursgroessenGK(), "Die vorhandene Kombination behält ihren Wert.");
	}

	@Test
	void testEinAndererFehlerDerKursdifferenzPropagiert() {
		final GostBlockungsergebnisManager ergebnisManager = mock(GostBlockungsergebnisManager.class);
		final ReportingGostFachwahlstatistikHalbjahr statistik = statistikMitFach();
		when(ergebnisManager.getOfFachOfKursartKursdifferenz(ID_FACH, GostKursart.LK.id)).thenThrow(new IllegalStateException("Fehlerinjektion"));

		assertThrows(IllegalStateException.class, () -> new ProxyReportingGostKursplanungFachwahlstatistik(statistik, ergebnisManager));
	}

}
