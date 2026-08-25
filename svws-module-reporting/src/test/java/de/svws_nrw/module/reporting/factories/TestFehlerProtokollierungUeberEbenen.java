package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderContextPdf;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderPdf;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Fehlerprotokollierung über die <b>echte</b> Kette Renderer → {@link ReportBuilderPdf} → {@link PdfFactory} → Fehlerausgabe der obersten Ebene.
 * <p>Die übrigen Tests des Moduls prüfen je Ebene isoliert und mocken die darunterliegende. Damit lässt sich nicht zeigen, wie das Log am Ende aussieht,
 * das der Client als Fehlerantwort erhält. Genau das ist hier Gegenstand.</p>
 * <p>Ein Abbruch hat eine Meldungsquelle: die Meldung der Exception. Sie muss den API-Rand unverändert erreichen und als Meldungszeile im Fehlerblock am
 * Ende des Logs stehen - nicht ersetzt, nicht erweitert, nicht wiederholt.</p>
 * <p>Davor trägt jede Ebene genau ihren eigenen Beitrag bei: die Sammelausgabe den Dateinamen des betroffenen Dokuments, die oberste Ebene den Vorgang samt
 * Statuscode und Stacktrace. Renderer und Builder tragen nichts bei und protokollieren deshalb nicht. Kein Eintrag hängt die Meldung einer anderen Ebene an
 * — sonst wüchse derselbe Satz mit jeder Ebene mit.</p>
 * <p>Geprüft wird das Log als vollständige, geordnete Liste in drei Abschnitten: die Beiträge vor dem Fehlerblock, der Fehlerblock selbst und das, was nach
 * dem Stacktrace noch folgt. Ein Vergleich einzelner Vorkommen ließe eine doppelte, eine fremde oder eine vertauschte Zeile durch. Verankert wird am Ende
 * der Liste, weil der Fehlerblock das Log abschließt.</p>
 * <p>Gezählt wird dagegen nicht, wie oft die Meldung im gesamten Text steht: Der Stacktrace führt sie in seiner Kopfzeile technisch erneut, und das gibt
 * Java vor.</p>
 * <p>Die oberste Ebene wird durch den direkten Aufruf von {@link ReportingExceptionUtils#logException} nachgebildet — so verfährt die
 * {@code ReportingFactory} mit dem bei ihr ankommenden Fehler. Ein Aufbau der Factory selbst käme ohne Datenbankverbindung nicht aus; die Kopfzeile der
 * Fehlerantwort prüft {@code TestReportingFactoryEingangspruefungen} an der echten Abschlussgrenze.</p>
 */
class TestFehlerProtokollierungUeberEbenen {

	/** Ein Ressourcenpfad, den es nicht gibt - er lässt den Renderer als unterste Ebene scheitern. */
	private static final String ROOT_PFAD_UNBEKANNT = "de/svws_nrw/module/reporting/gibt-es-nicht/";

	/**
	 * Die Meldung, die der Renderer als Fehlerquelle erzeugt. Sie steht hier bewusst als ganzer Text: Nur so zeigt der Test, dass keine Ebene der Kette sie
	 * ersetzt oder erweitert. Ihre Form sichert der Architekturtest, ihren Wortlaut der Renderer-Test.
	 */
	private static final String MELDUNG_QUELLE = "### FEHLER: Das Ressourcenverzeichnis für die PDF-Erzeugung fehlt auf dem Server.";

	/** Die Beschreibung, mit der die oberste Ebene den Vorgang einordnet. Sie ist die Ersatzmeldung der Abschlussgrenze für die Ausgabe eines Reports. */
	private static final String BESCHREIBUNG = "### FEHLER: Die Ausgabe des Reports konnte nicht erstellt werden.";

	/** Der Eintrag, mit dem die Sammelausgabe das betroffene Dokument benennt. */
	private static final String BEITRAG_SAMMELAUSGABE = "Betroffene Datei: Bescheinigung_Meier";

	/** Die Zeile, mit der der Fehlerblock den Fehlertyp und den Statuscode nennt. */
	private static final String ZEILE_FEHLERTYP = "### FEHLER: Fehler vom Typ ApiOperationException - Code: 500";

	/** Die Überschrift des Abschnitts mit dem Stacktrace. */
	private static final String UEBERSCHRIFT_STACKTRACE = "### STACKTRACE:";

	/** Das Zeichen, mit dem jeder Meldungseintrag des Reportings beginnt. Die Zeilen eines Stacktrace tragen es nicht am Anfang. */
	private static final String MARKER = "###";

	/** Der Fehlerblock, mit dem die Abschlussgrenze das Log beendet: der Vorgang, der Fehlertyp mit Statuscode und die Meldung der Fehlerquelle. */
	private static final List<String> FEHLERBLOCK = List.of(BESCHREIBUNG, ZEILE_FEHLERTYP, MELDUNG_QUELLE);

	/**
	 * Die Einträge vor dem Fehlerblock: allein der Beitrag der Sammelausgabe. Er nennt den Dateinamen des betroffenen Dokuments und damit einen
	 * Zusammenhang, den die Fehlerquelle nicht kennt.
	 */
	private static final List<String> EINTRAEGE_VOR_DEM_FEHLERBLOCK = List.of(BEITRAG_SAMMELAUSGABE);

	/** Der Logger, den alle Ebenen gemeinsam verwenden. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;

	/** Der Context, den die Factory erhält. */
	private ReportingContext reportingContext;


	@BeforeEach
	void setUp() {
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);
	}


	/**
	 * Gibt die Texte aller ERROR-Einträge zurück, ohne die Einrückung des Loggers. Geprüft wird die Meldung, nicht ihre Formatierung.
	 *
	 * @return Die Texte in der Reihenfolge ihres Auftretens.
	 */
	private List<String> fehlermeldungen() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}

	/**
	 * Gibt die Position der Stacktrace-Überschrift zurück und verlangt damit zugleich, dass es sie gibt. Ohne diese Prüfung fiele ein unvollständiger
	 * Fehlerblock nicht auf: Die Abschnitte davor und danach wären dann leer oder vertauscht, ohne dass eine Zusicherung greift.
	 *
	 * @param eintraege Die ERROR-Einträge des Laufs.
	 *
	 * @return Der Index der Überschrift.
	 */
	private static int beginnDesStacktrace(final List<String> eintraege) {
		final int beginn = eintraege.indexOf(UEBERSCHRIFT_STACKTRACE);
		assertTrue(beginn >= 0, "Der Fehlerblock muss mit dem Stacktrace enden: " + eintraege);
		return beginn;
	}

	/**
	 * Gibt die Texte aller ERROR-Einträge bis zum Beginn des Stacktrace zurück. Der Stacktrace selbst bleibt außen vor: Seine Zeilen hängen von der
	 * Aufrufumgebung ab, und dass er die Meldung in seiner Kopfzeile wiederholt, gibt Java vor.
	 *
	 * @return Die Texte der ERROR-Einträge vor dem Stacktrace.
	 */
	private List<String> fehlermeldungenVorStacktrace() {
		final List<String> eintraege = fehlermeldungen();
		return eintraege.subList(0, beginnDesStacktrace(eintraege));
	}

	/**
	 * Gibt die Texte aller ERROR-Einträge nach dem Beginn des Stacktrace zurück.
	 *
	 * @return Die Texte der ERROR-Einträge hinter der Überschrift des Stacktrace.
	 */
	private List<String> fehlermeldungenNachStacktrace() {
		final List<String> eintraege = fehlermeldungen();
		return eintraege.subList(beginnDesStacktrace(eintraege) + 1, eintraege.size());
	}

	/**
	 * Lässt einen Fehler durch die echte Kette laufen: Der Renderer findet seine Ressourcen nicht, der Builder gibt den Fehler weiter, die Sammelausgabe
	 * ergänzt den Dateinamen, und die oberste Ebene erzeugt ihre Fehlerausgabe.
	 *
	 * @return Die Exception, die am Ende der Kette ankommt.
	 */
	private ApiOperationException fehlerDurchDieKette() {
		final PdfFactory pdfFactory = new PdfFactory(List.of(mock(ReportBuilderHtml.class)), reportingContext);
		final ReportBuilderPdf pdfBuilder = new ReportBuilderPdf(new ReportBuilderContextPdf()
				.withHtmlInput("<html><body><p>Testinhalt</p></body></html>")
				.withDateiname("Bescheinigung_Meier")
				.withStatischerDateiname("Bescheinigungen")
				.withRootPfad(ROOT_PFAD_UNBEKANNT));

		final List<ReportBuilderPdf> pdfBuilders = List.of(pdfBuilder);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> pdfFactory.createZIP(pdfBuilders));
		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);
		return aoe;
	}


	@Test
	void testDerFehlerblockBeendetDasLogMitDerMeldungDerFehlerquelle() {
		fehlerDurchDieKette();

		// Der Fehlerblock der Abschlussgrenze steht am Ende. Er muss auch für Fehler funktionieren, die zuvor nirgends protokolliert wurden, und kann
		// deshalb nicht entfallen. Weil der Vergleich am Ende der Liste hängt, darf zwischen der Meldung und dem Stacktrace nichts mehr stehen.
		final List<String> block = fehlermeldungenVorStacktrace();
		assertTrue(block.size() >= FEHLERBLOCK.size(), "Der Fehlerblock der Abschlussgrenze fehlt: " + block);
		assertEquals(FEHLERBLOCK, block.subList(block.size() - FEHLERBLOCK.size(), block.size()),
				"Der Fehlerblock muss aus Vorgang, Fehlertyp und der Meldung der Fehlerquelle bestehen: " + block);
	}

	@Test
	void testJedeEbeneTraegtGenauIhrenEigenenBeitragBei() {
		fehlerDurchDieKette();

		// Vollständig und in dieser Reihenfolge: Ein doppelter, ein fremder oder ein vertauschter Eintrag fällt damit auf. Ein Eintrag, der die Meldung
		// einer anderen Ebene mitführt, ebenso - sonst wächst derselbe Satz mit jeder Ebene mit.
		final List<String> block = fehlermeldungenVorStacktrace();
		assertEquals(EINTRAEGE_VOR_DEM_FEHLERBLOCK, block.subList(0, Math.max(block.size() - FEHLERBLOCK.size(), 0)),
				"Vor dem Fehlerblock steht je Ebene genau ihr eigener Beitrag: " + block);
	}

	@Test
	void testNachDemStacktraceFolgtKeinWeitererMeldungseintrag() {
		fehlerDurchDieKette();

		// Der Stacktrace schließt die Ausgabe ab. Ein weiterer Eintrag mit dem Marker wäre eine dritte Darstellung derselben Meldung oder ein zweiter
		// Fehlerblock; beides sähe der Anwender in der Fehlerantwort. Die Zeilen des Stacktrace beginnen mit dem Klassennamen oder mit "at" und tragen
		// den Marker deshalb nicht am Anfang.
		final List<String> danach = fehlermeldungenNachStacktrace();
		assertTrue(danach.stream().noneMatch(eintrag -> eintrag.startsWith(MARKER)),
				"Nach dem Stacktrace darf kein weiterer Meldungseintrag folgen: " + danach);
	}

	@Test
	void testDerStatusUeberlebtDieGesamteKette() {
		final ApiOperationException aoe = fehlerDurchDieKette();

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals(MELDUNG_QUELLE, aoe.getBody(), "Die Meldung der Fehlerquelle muss den API-Rand unverändert erreichen.");
	}

}
