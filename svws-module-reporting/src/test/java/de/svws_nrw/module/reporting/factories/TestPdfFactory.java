package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderPdf;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der {@link PdfFactory}: die Erzeugung der PDF-Builder aus den HTML-Buildern und die Sammelausgabe mehrerer PDF-Dateien als ZIP.
 * <p>Anlass für die Namensvergabe im ZIP ist, dass {@code putNextEntry} bei einem bereits vergebenen Eintragsnamen eine {@code ZipException} wirft. Zwei
 * Personen mit gleichem namensbasiertem Dateinamen genügen, um die gesamte Sammelausgabe abzubrechen.</p>
 * <p>Geprüft wird über die tatsächlich erzeugten ZIP-Bytes, nicht über eine interne Hilfsmethode: Nur so ist belegt, dass der Eintrag auch wirklich
 * geschrieben werden kann. Die PDF-Builder sind dabei gemockt, weil das Rendern echter PDF-Dateien Schriftarten und Renderer-Ressourcen erfordern würde und
 * für die Namensvergabe ohne Belang ist. Dass die IDs auf dem produktiven Weg überhaupt bis zum PDF-Builder gelangen, sichert dafür der eigene Abschnitt
 * zur Builder-Erzeugung ab.</p>
 */
class TestPdfFactory {

	/** Der Root-Pfad, den die Builder für ihre Ressourcen erhalten. */
	private static final String ROOT_PFAD = "de/svws_nrw/module/reporting/";

	/** Der Context, den die Factory in den Tests erhält. */
	private ReportingContext reportingContext;

	/** Die Factory, deren Sammelausgabe geprüft wird. */
	private PdfFactory pdfFactory;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() throws ApiOperationException {
		reportingContext = mock(ReportingContext.class);
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		when(reportingContext.logger()).thenReturn(logger);
		// Die Factory verlangt mindestens einen HTML-Builder. Für die Sammelausgabe werden die PDF-Builder direkt übergeben.
		pdfFactory = new PdfFactory(List.of(mock(ReportBuilderHtml.class)), reportingContext);
	}

	/**
	 * Gibt die Texte aller Logeinträge mit dem Level ERROR zurück. Die Einrückung, die der Logger dem Text voranstellt, wird dabei entfernt: Geprüft wird
	 * die Meldung, nicht ihre Formatierung.
	 *
	 * @return Die Texte der ERROR-Logeinträge in der Reihenfolge ihres Auftretens.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}


	/**
	 * Erzeugt einen PDF-Builder, der den angegebenen Dateinamen und die angegebenen IDs meldet.
	 *
	 * @param dateiname Der Dateiname ohne Endung.
	 * @param ids       Die IDs der Hauptdaten des Builders.
	 *
	 * @return Der gemockte PDF-Builder.
	 *
	 * @throws ApiOperationException Wird von der gemockten Generierung deklariert, tritt hier aber nicht auf.
	 */
	private static ReportBuilderPdf pdfBuilder(final String dateiname, final Set<Long> ids) throws ApiOperationException {
		final ReportBuilderPdf pdfBuilder = mock(ReportBuilderPdf.class);
		when(pdfBuilder.getDateiname()).thenReturn(dateiname);
		when(pdfBuilder.getDateinameMitEndung()).thenReturn(dateiname + ".pdf");
		when(pdfBuilder.getIds()).thenReturn(ids);
		when(pdfBuilder.generate()).thenReturn(("Inhalt " + dateiname).getBytes(StandardCharsets.UTF_8));
		return pdfBuilder;
	}

	/**
	 * Erzeugt das ZIP zu den übergebenen Buildern und ordnet jeder fachlichen ID den Namen zu, unter dem ihr Dokument abgelegt wurde.
	 *
	 * @param pdfBuilders Die PDF-Builder, die jeweils genau eine ID tragen müssen.
	 *
	 * @return Die Zuordnung von fachlicher ID zu Eintragsname.
	 *
	 * @throws Exception Falls die Sammelausgabe oder das Lesen der ZIP-Daten fehlschlägt.
	 */
	private Map<Long, String> zuordnungIdZuEintragsname(final List<ReportBuilderPdf> pdfBuilders) throws Exception {
		final List<String> namen = eintragsnamen(pdfFactory.createZIP(pdfBuilders));
		final Map<Long, String> zuordnung = new HashMap<>();
		for (int i = 0; i < pdfBuilders.size(); i++) {
			zuordnung.put(pdfBuilders.get(i).getIds().iterator().next(), namen.get(i));
		}
		return zuordnung;
	}

	/**
	 * Liest die Namen der Einträge aus den übergebenen ZIP-Daten in der Reihenfolge ihres Auftretens.
	 *
	 * @param zipData Die ZIP-Datei als Byte-Array.
	 *
	 * @return Die Namen der enthaltenen Einträge.
	 *
	 * @throws IOException Falls die ZIP-Daten nicht gelesen werden können.
	 */
	private static List<String> eintragsnamen(final byte[] zipData) throws IOException {
		final List<String> namen = new ArrayList<>();
		try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
			ZipEntry eintrag = zis.getNextEntry();
			while (eintrag != null) {
				namen.add(eintrag.getName());
				eintrag = zis.getNextEntry();
			}
		}
		return namen;
	}


	/**
	 * Erzeugt einen HTML-Builder, wie ihn die HtmlFactory an die PdfFactory übergibt.
	 *
	 * @param dateiname Der aus der Dateinamensvorlage erzeugte Dateiname ohne Endung.
	 * @param ids       Die IDs der Hauptdaten, für die der Builder seine Datei erzeugt.
	 *
	 * @return Der gemockte HTML-Builder.
	 *
	 * @throws ApiOperationException Wird von der gemockten Generierung deklariert, tritt hier aber nicht auf.
	 */
	private static ReportBuilderHtml htmlBuilder(final String dateiname, final Set<Long> ids) throws ApiOperationException {
		final ReportBuilderHtml htmlBuilder = mock(ReportBuilderHtml.class);
		when(htmlBuilder.generate()).thenReturn("<html><body>" + dateiname + "</body></html>");
		when(htmlBuilder.getDateiname()).thenReturn(dateiname);
		when(htmlBuilder.getStatischerDateiname()).thenReturn("Sammelausgabe");
		when(htmlBuilder.getRootPfad()).thenReturn(ROOT_PFAD);
		when(htmlBuilder.getIds()).thenReturn(ids);
		return htmlBuilder;
	}


	// ##### Erzeugung der PDF-Builder aus den HTML-Buildern #####

	@Test
	void testDieIdsDerHtmlBuilderErreichenDiePdfBuilder() throws Exception {
		// Dieser Test geht bewusst den produktiven Weg ReportBuilderHtml -> ReportBuilderContextPdf -> ReportBuilderPdf. Die ZIP-Tests mocken die
		// PDF-Builder und würden ein versehentliches Entfernen der Durchreichung nicht bemerken.
		final PdfFactory factory = new PdfFactory(
				List.of(htmlBuilder("Bescheinigung_Meier", Set.of(4711L)), htmlBuilder("Bescheinigung_Schulz", Set.of(4712L, 4713L))),
				reportingContext);

		final List<ReportBuilderPdf> pdfBuilders = factory.getPdfBuilders();

		assertEquals(List.of(Set.of(4711L), Set.of(4712L, 4713L)), pdfBuilders.stream().map(ReportBuilderPdf::getIds).toList());
		assertEquals(List.of("Bescheinigung_Meier", "Bescheinigung_Schulz"), pdfBuilders.stream().map(ReportBuilderPdf::getDateiname).toList());
	}

	@Test
	void testEinHtmlBuilderOhneIdsErgibtEinenPdfBuilderOhneIds() throws Exception {
		final PdfFactory factory = new PdfFactory(List.of(htmlBuilder("Sammelliste", Set.of())), reportingContext);

		assertEquals(Set.of(), factory.getPdfBuilders().getFirst().getIds());
	}


	// ##### Namensvergabe in der ZIP-Sammelausgabe #####

	@Test
	void testEindeutigeDateinamenBleibenUnveraendert() throws Exception {
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)),
				pdfBuilder("Bescheinigung_Schulz", Set.of(4712L)));

		assertEquals(List.of("Bescheinigung_Meier.pdf", "Bescheinigung_Schulz.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testGleicheDateinamenErhaltenIhreFachlicheIdAlsSuffix() throws Exception {
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)),
				pdfBuilder("Bescheinigung_Meier", Set.of(4712L)));

		// Auch der erste Eintrag erhält ein Suffix: Ein gemischtes Schema ließe nicht erkennen, welcher Datensatz gemeint ist.
		assertEquals(List.of("Bescheinigung_Meier-4711.pdf", "Bescheinigung_Meier-4712.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testNurDieKollidierendenDateinamenWerdenErgaenzt() throws Exception {
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)),
				pdfBuilder("Bescheinigung_Schulz", Set.of(4713L)),
				pdfBuilder("Bescheinigung_Meier", Set.of(4712L)));

		assertEquals(List.of("Bescheinigung_Meier-4711.pdf", "Bescheinigung_Schulz.pdf", "Bescheinigung_Meier-4712.pdf"),
				eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testOhneIdGreiftDieLaufendeNummerAbEins() throws Exception {
		// Ohne ID bekommen alle Beteiligten eine Nummer - auch der erste, damit keine unnummerierte Datei neben nummerierten steht.
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Sammelliste", Set.of()),
				pdfBuilder("Sammelliste", Set.of()),
				pdfBuilder("Sammelliste", Set.of()));

		assertEquals(List.of("Sammelliste-1.pdf", "Sammelliste-2.pdf", "Sammelliste-3.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testMehrdeutigeIdsFuehrenZurLaufendenNummer() throws Exception {
		// Aus einem Set mit mehreren IDs ließe sich keine ID deterministisch auswählen.
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Kursliste", Set.of(1L, 2L)),
				pdfBuilder("Kursliste", Set.of(3L, 4L)));

		assertEquals(List.of("Kursliste-1.pdf", "Kursliste-2.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testEineGruppeMitUndOhneIdWirdEinheitlichNummeriert() throws Exception {
		// Innerhalb einer Kollisionsgruppe darf es kein gemischtes Schema geben, sonst stünde eine ID neben einer laufenden Nummer.
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Schulz", Set.of(4711L)),
				pdfBuilder("Bescheinigung_Schulz", Set.of()));

		assertEquals(List.of("Bescheinigung_Schulz-1.pdf", "Bescheinigung_Schulz-2.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testGleicheIdInEinerGruppeFuehrtZurLaufendenNummer() throws Exception {
		// Zwei Dokumente zum selben Datensatz: Das ID-Suffix würde die Kollision nicht auflösen.
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)),
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)));

		assertEquals(List.of("Bescheinigung_Meier-1.pdf", "Bescheinigung_Meier-2.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testEinAufgeloesterNameWeichtEinemTatsaechlichSoBenanntenEintragAus() throws Exception {
		// Die gruppenweise Auflösung allein genügt nicht: Der für die Kollisionsgruppe erzeugte Name Meier-4711.pdf existiert bereits als eigenständiger
		// Eintrag. Ohne die abschließende globale Prüfung entstünde er zweimal und putNextEntry bräche ab.
		// Den Namen behält der Datensatz, der ihn tatsächlich trägt (ID 4714), nicht der, für den er erzeugt wurde (ID 4711).
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Meier", Set.of(4711L)),
				pdfBuilder("Meier", Set.of(4712L)),
				pdfBuilder("Meier-4711", Set.of(4714L)));

		assertEquals(List.of("Meier-4711-2.pdf", "Meier-4712.pdf", "Meier-4711.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testDieZuordnungVonDatensatzZuDateinameHaengtNichtVonDerEingabereihenfolgeAb() throws Exception {
		// Entscheidend ist nicht, dass beide Reihenfolgen dieselbe Namensliste ergeben - das wäre bereits durch die Positionsgleichheit erfüllt. Geprüft
		// wird, dass jeder Datensatz in beiden Reihenfolgen denselben Dateinamen erhält.
		final Map<Long, String> beiVorwaertsgelesenerListe = zuordnungIdZuEintragsname(List.of(
				pdfBuilder("Meier", Set.of(4711L)),
				pdfBuilder("Meier", Set.of(4712L)),
				pdfBuilder("Meier-4711", Set.of(4714L))));

		final Map<Long, String> beiUmgekehrterListe = zuordnungIdZuEintragsname(List.of(
				pdfBuilder("Meier-4711", Set.of(4714L)),
				pdfBuilder("Meier", Set.of(4712L)),
				pdfBuilder("Meier", Set.of(4711L))));

		assertEquals(Map.of(4711L, "Meier-4711-2.pdf", 4712L, "Meier-4712.pdf", 4714L, "Meier-4711.pdf"), beiVorwaertsgelesenerListe);
		assertEquals(beiVorwaertsgelesenerListe, beiUmgekehrterListe);
	}

	@Test
	void testNamenDieSichNurInDerSchreibweiseUnterscheidenKollidieren() throws Exception {
		// "Meier" und "meier" benennen dieselbe Person. Zwei so benannte Dateien wären zudem beim Entpacken unter Windows oder macOS nicht nebeneinander
		// ablegbar - eine der beiden ginge verloren.
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)),
				pdfBuilder("bescheinigung_meier", Set.of(4712L)));

		// Die Schreibweise des jeweiligen Dateinamens bleibt dabei erhalten; ergänzt wird nur das unterscheidende Suffix.
		assertEquals(List.of("Bescheinigung_Meier-4711.pdf", "bescheinigung_meier-4712.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testAuchDieGlobalePruefungIgnoriertDieSchreibweise() throws Exception {
		// Der erzeugte Name Meier-4711.pdf trifft auf einen vorhandenen Eintrag MEIER-4711, der sich nur in der Schreibweise unterscheidet. Auch hier
		// behält der tatsächlich so benannte Eintrag seinen Namen samt eigener Schreibweise.
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Meier", Set.of(4711L)),
				pdfBuilder("Meier", Set.of(4712L)),
				pdfBuilder("MEIER-4711", Set.of(4714L)));

		assertEquals(List.of("Meier-4711-2.pdf", "Meier-4712.pdf", "MEIER-4711.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testAlleEintragsnamenSindPaarweiseVerschieden() throws Exception {
		// Sammelprüfung über einen bewusst verschachtelten Bestand: Entscheidend ist allein, dass kein Name zweimal vorkommt.
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Meier", Set.of(4711L)),
				pdfBuilder("Meier", Set.of(4712L)),
				pdfBuilder("Meier-4711", Set.of()),
				pdfBuilder("Meier-4711", Set.of()),
				pdfBuilder("Meier-4711-1", Set.of(4715L)));

		final List<String> namen = eintragsnamen(pdfFactory.createZIP(pdfBuilders));

		// Geprüft wird die Vergleichsform und nicht der Name selbst: Zwei Einträge, die sich allein in der Schreibweise unterscheiden, gelten als gleich.
		final Set<String> vergleichsnamen = namen.stream().map(name -> name.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());

		assertEquals(pdfBuilders.size(), namen.size());
		assertEquals(namen.size(), vergleichsnamen.size(), "Die Eintragsnamen im ZIP müssen paarweise verschieden sein: " + namen);
	}

	@Test
	void testDreiGleicheDateinamenErgebenDreiEintraege() throws Exception {
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)),
				pdfBuilder("Bescheinigung_Meier", Set.of(4712L)),
				pdfBuilder("Bescheinigung_Meier", Set.of(4713L)));

		assertEquals(List.of("Bescheinigung_Meier-4711.pdf", "Bescheinigung_Meier-4712.pdf", "Bescheinigung_Meier-4713.pdf"),
				eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}


	// ##### Fehlerweitergabe der Sammelausgabe #####

	@Test
	void testDerStatusEinesBuildersUeberlebtDieSammelausgabe() throws Exception {
		// Die Sammelausgabe darf den Status des Builders nicht verschärfen - sonst erreicht die Klassifikation der Renderer den API-Rand auf dem Weg
		// über das ZIP nicht.
		final ApiOperationException ursprung = new ApiOperationException(Status.NOT_FOUND, "FEHLER: Der Stundenplan zur ID 4711 wurde nicht gefunden.");
		final ReportBuilderPdf pdfBuilder = pdfBuilder("Bescheinigung_Meier", Set.of(4711L));
		when(pdfBuilder.generate()).thenThrow(ursprung);

		final List<ReportBuilderPdf> pdfBuilders = List.of(pdfBuilder);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> pdfFactory.createZIP(pdfBuilders));
		assertSame(ursprung, aoe, "Die ursprüngliche Exception muss unverändert durchgereicht werden.");
		assertEquals(Status.NOT_FOUND, aoe.getStatus());
		// Der Dateiname ist bei einer Sammelausgabe die einzige Angabe, welches Dokument betroffen ist - und damit das Einzige, was diese Ebene beiträgt.
		// Die Meldung der Fehlerquelle gehört nicht hinein: Sie steht bereits in deren eigenem Eintrag und reist mit der Exception weiter.
		assertEquals(List.of("### FEHLER: Die PDF-Datei 'Bescheinigung_Meier' konnte nicht erzeugt werden."), fehlermeldungenImLog(),
				"Die Sammelausgabe muss genau ihren eigenen Zusammenhang protokollieren.");
	}

}
