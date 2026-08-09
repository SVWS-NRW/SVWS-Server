package de.svws_nrw.module.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;

/**
 * Diese Tests prüfen, dass zu jeder Reportvorlage der vollständige Satz an Ressourcendateien im Classpath liegt: die HTML-Vorlage, die reportspezifische
 * CSS-Datei und die Dateinamensvorlage.
 * <p>Die Pfade werden dabei <b>genau so abgeleitet, wie es der Produktivcode zur Laufzeit tut</b>: die HTML-Vorlage über
 * {@link ReportingReportvorlage#getRootPfadHtmlTemplate()}, die Dateinamensvorlage über {@link ReportingReportvorlage#getPfadDateinamensvorlage()} und die
 * CSS-Datei durch Ersetzen der Endung {@code .html} durch {@code .css} — Letzteres geschieht im Fragment {@code reportHtmlHead.html} und wird von
 * {@code InlineExpressionHelper.css(...)} aufgelöst. Eine fehlende Datei fällt heute erst bei der Ausgabe auf, und die CSS-Datei fehlt dabei sogar
 * lautlos, weil sie über {@code textOrEmpty} geladen wird.</p>
 * <p>Maßgeblich für den geprüften Bestand ist allein {@link ReportingReportvorlage}. Eine neue Vorlage wird dadurch ohne Anpassung dieser Tests
 * mitgeprüft; eine erwartete Anzahl ist bewusst nicht hinterlegt, da sie nur ein zweiter zu pflegender Ort wäre.</p>
 * <p>Der Test benötigt weder Datenbank noch Schema und ist damit unabhängig von allen weiteren Umbauten des Moduls.</p>
 */
class TestReportvorlagenRessourcen {

	/** Die Endung der HTML-Vorlage. */
	private static final String ENDUNG_HTML = ".html";

	/** Die Endung der reportspezifischen CSS-Datei. */
	private static final String ENDUNG_CSS = ".css";


	/**
	 * Prüft, ob die angegebene Ressource über den Classloader erreichbar ist.
	 *
	 * @param rootPfadRessource Der Pfad der Ressource einschließlich des Root-Pfads der Reportvorlagen.
	 *
	 * @return true, wenn die Ressource vorhanden ist, sonst false.
	 */
	private static boolean existiert(final String rootPfadRessource) {
		return TestReportvorlagenRessourcen.class.getClassLoader().getResource(rootPfadRessource) != null;
	}


	@Test
	void testDieRessourcenpruefungSchlaegtBeiEinemUnbekanntenPfadAn() {
		// Gegenprobe: Ohne sie könnten alle folgenden Prüfungen nur deshalb grün sein, weil die Auflösung grundsätzlich einen Treffer meldet.
		assertTrue(existiert(ReportingReportvorlage.getRootPfad() + "css/reporting-styles.css"));
		assertFalse(existiert(ReportingReportvorlage.getRootPfad() + "css/diese-datei-gibt-es-nicht.css"));
	}

	@Test
	void testJedeReportvorlageHatEineHtmlVorlage() {
		final List<String> fehlend = new ArrayList<>();
		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			final String pfad = reportvorlage.getRootPfadHtmlTemplate();
			if (!existiert(pfad)) {
				fehlend.add("%s -> %s".formatted(reportvorlage.name(), pfad));
			}
		}
		assertEquals(List.of(), fehlend, "Zu diesen Reportvorlagen fehlt die HTML-Vorlage.");
	}

	@Test
	void testJedeReportvorlageHatEineCssDatei() {
		final List<String> fehlend = new ArrayList<>();
		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			final String pfad = reportvorlage.getRootPfadHtmlTemplate().replace(ENDUNG_HTML, ENDUNG_CSS);
			if (!existiert(pfad)) {
				fehlend.add("%s -> %s".formatted(reportvorlage.name(), pfad));
			}
		}
		assertEquals(List.of(), fehlend, "Zu diesen Reportvorlagen fehlt die reportspezifische CSS-Datei.");
	}

	@Test
	void testJedeReportvorlageHatEineDateinamensvorlage() {
		final List<String> fehlend = new ArrayList<>();
		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			final String pfad = ReportingReportvorlage.getRootPfad() + reportvorlage.getPfadDateinamensvorlage();
			if (!existiert(pfad)) {
				fehlend.add("%s -> %s".formatted(reportvorlage.name(), pfad));
			}
		}
		assertEquals(List.of(), fehlend, "Zu diesen Reportvorlagen fehlt die Dateinamensvorlage.");
	}

	@Test
	void testJedeReportvorlageVerweistAufEineHtmlDatei() {
		// Die Pfade der CSS-Datei und der Dateinamensvorlage werden durch Ersetzen der Endung ".html" gebildet. Endet der Vorlagenpfad nicht darauf,
		// zeigen beide Ableitungen unbemerkt auf die HTML-Datei selbst.
		final List<String> abweichend = new ArrayList<>();
		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			final String pfad = reportvorlage.getPfadHtmlTemplate();
			if (!pfad.endsWith(ENDUNG_HTML)) {
				abweichend.add("%s -> %s".formatted(reportvorlage.name(), pfad));
			}
		}
		assertEquals(List.of(), abweichend, "Diese Reportvorlagen verweisen nicht auf eine Datei mit der Endung \".html\".");
	}

}
