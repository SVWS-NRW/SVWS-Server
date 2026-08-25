package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
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
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderPdf;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.diagnose.ReportingProblem;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response;
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

	/** Der Umfang einer zulässig leeren Ausgabe: Datensätze waren angefordert, keiner blieb übrig. */
	private static final ReportingAusgabeumfang LEER_GEFILTERT = new ReportingAusgabeumfang(5, 0, true);

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
		// Der Header wird derzeit nur im Entwicklungsmodus ausgeliefert; die Tests messen deshalb dort.
		when(reportingContext.serverMode()).thenReturn(ServerMode.DEV);
		// Der Standard-Umfang der Tests: Alles Angeforderte wird ausgegeben, eine leere Ausgabe ist nicht zulässig.
		when(reportingContext.ausgabeumfang()).thenReturn(new ReportingAusgabeumfang(2, 2, false));
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
		assertEquals(List.of("Betroffene Datei: Bescheinigung_Meier"), fehlermeldungenImLog(),
				"Die Sammelausgabe muss genau ihren eigenen Zusammenhang protokollieren.");
	}


	// ##### Ausgabe ohne verbleibende Ausgabeeinheit #####

	/**
	 * Verdrahtet die Reportvorlage, aus der der Dateiname der ZIP-Ausgabe entsteht.
	 *
	 * @param reportvorlage Die Reportvorlage des Aufrufs.
	 */
	private void gebeReportvorlageVor(final ReportingReportvorlage reportvorlage) {
		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.reportVorlage()).thenReturn(reportvorlage);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of());
	}

	@Test
	void testEineBewusstLeereAuswahlErzeugtEinZipOhnePdfDatei() throws Exception {
		// Eine Einzelausgabe ohne verbleibenden Datensatz ist eine gültige Antwort und kein Serverfehler. Das Archiv entsteht, damit die Antwort einen Träger
		// für die Hinweisdatei hat und der Client eine Datei erhält statt einer Fehlermeldung.
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG);
		when(reportingContext.ausgabeumfang()).thenReturn(LEER_GEFILTERT);
		final PdfFactory factory = new PdfFactory(List.of(), reportingContext);

		final Response response = factory.createPdfResponse();

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals("application/zip", response.getMediaType().toString());
		final List<String> eintraege = eintragsnamen((byte[]) response.getEntity());
		assertTrue(eintraege.stream().noneMatch(name -> name.endsWith(".pdf")), "Ohne Datensatz enthält das Archiv keine PDF-Datei: " + eintraege);
	}

	@Test
	void testDerDateinameDerLeerenZipAusgabeStammtAusDerReportvorlage() throws Exception {
		// Der Name darf nicht am ersten Builder hängen: Bei einer leeren Auswahl gibt es keinen. Er ist deshalb derselbe wie bei einer gefüllten Ausgabe.
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG);
		when(reportingContext.ausgabeumfang()).thenReturn(LEER_GEFILTERT);
		final PdfFactory factory = new PdfFactory(List.of(), reportingContext);

		final String dateiname = dateinameAusResponse(factory.createPdfResponse());

		assertEquals(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG.getDateiname() + ".zip", dateiname);
	}

	@Test
	void testOhneZulaessigLeereAusgabeBleibtEineLeereAusgabeEinServerfehler() {
		// Null Builder ohne das Kennzeichen des Ausgabeumfangs sind ein Programmierfehler. Fiele die Unterscheidung, ginge ein Ausfall der Dokumenterzeugung
		// als leere Ausgabe durch - der Anwender erhielte ein leeres Archiv statt einer Fehlermeldung.
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> new PdfFactory(List.of(), reportingContext));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
	}

	@Test
	void testEineFehlendeUndEineLeereBuilderlisteWerdenVerschiedenGemeldet() {
		// Beide Lagen sind Programmierfehler, aber verschiedene: Einmal hat der Aufrufer die Liste gar nicht übergeben, einmal hat die Erzeugung der
		// Dokumente versagt. Mit demselben Text wären sie in der Fehlerantwort nicht auseinanderzuhalten.
		final ApiOperationException ohneListe = assertThrows(ApiOperationException.class, () -> new PdfFactory(null, reportingContext));
		final ApiOperationException leereListe = assertThrows(ApiOperationException.class, () -> new PdfFactory(List.of(), reportingContext));

		assertEquals(Status.INTERNAL_SERVER_ERROR, ohneListe.getStatus());
		assertEquals(Status.INTERNAL_SERVER_ERROR, leereListe.getStatus());
		assertNotEquals(ohneListe.getMessage(), leereListe.getMessage(),
				"Die beiden Lagen brauchen verschiedene Meldungen: " + ohneListe.getMessage());
	}

	@Test
	void testEineBewusstLeereSammelausgabeErzeugtWeiterhinEinDokument() throws Exception {
		// Bei der Sammelausgabe entsteht auch ohne Datensatz genau ein Builder mit leerem fachlichem Inhalt. Maßgeblich ist die Anzahl der Dokumente und nicht
		// das Kennzeichen: Ein leeres Archiv wäre hier falsch, denn das Dokument existiert.
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_LISTE_KONTAKTDATENERZIEHER);
		when(reportingContext.ausgabeumfang()).thenReturn(LEER_GEFILTERT);
		final ReportBuilderHtml htmlBuilder = htmlBuilder("Schueler-Liste-Kontaktdaten-Erzieher", Set.of());
		final PdfFactory factory = new PdfFactory(List.of(htmlBuilder), reportingContext);

		final Response response = factory.createPdfResponse();

		assertEquals("application/pdf", response.getMediaType().toString(), "Eine Sammelausgabe liefert ein Dokument und kein Archiv.");
		assertEquals("Schueler-Liste-Kontaktdaten-Erzieher.pdf", dateinameAusResponse(response));
		// Der Header meldet trotzdem ausgegeben=0: Er folgt der Auswahl und nicht der Zahl der Dokumente. Genau diese Unterscheidung braucht der Client, um
		// ein Dokument mit leerem Inhalt zu erklären - andernfalls stünde der Anwender vor einer leeren Liste ohne Grund.
		assertEquals("v=1, angefordert=5, ausgegeben=0, hinweise=0", response.getHeaderString(ReportingHinweisSerializer.HEADER_NAME));
	}

	@Test
	void testDieZipAusgabeTraegtDenHinweisHeader() throws Exception {
		// Der Nachweis am produktiven Weg: Der Helfer ist eigens getestet, aber nur hier ist belegt, dass die ZIP-Ausgabe ihn auch aufruft.
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG);
		when(reportingContext.ausgabeumfang()).thenReturn(LEER_GEFILTERT);
		final PdfFactory factory = new PdfFactory(List.of(), reportingContext);

		final Response response = factory.createPdfResponse();

		assertEquals("v=1, angefordert=5, ausgegeben=0, hinweise=0", response.getHeaderString(ReportingHinweisSerializer.HEADER_NAME),
				"Eine Ausgabe ohne Dokument muss die Zählwerte melden.");
	}

	@Test
	void testDieEinzelnePdfDateiTraegtDenHinweisHeader() throws Exception {
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_LISTE_KONTAKTDATENERZIEHER);
		final PdfFactory factory = new PdfFactory(List.of(htmlBuilder("Schueler-Liste-Kontaktdaten-Erzieher", Set.of())), reportingContext);

		final Response response = factory.createPdfResponse();

		assertEquals("v=1, angefordert=2, ausgegeben=2, hinweise=0", response.getHeaderString(ReportingHinweisSerializer.HEADER_NAME));
	}

	@Test
	void testDasLeereArchivEnthaeltGenauEineHinweisdatei() throws Exception {
		// Ohne diese Beilage stünde der Anwender vor einem leeren Download ohne Erklärung. Sie ist zugleich der einzige Weg, auf dem die Hinweise ihn ohne
		// Clientanpassung erreichen: Den Response-Header wertet der heutige Client nicht aus.
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG);
		when(reportingContext.ausgabeumfang()).thenReturn(LEER_GEFILTERT);
		final PdfFactory factory = new PdfFactory(List.of(), reportingContext);

		final Response response = factory.createPdfResponse();

		assertEquals(List.of(ReportingHinweisSerializer.DATEINAME_HINWEISE), eintragsnamen((byte[]) response.getEntity()));
	}

	@Test
	void testDieHinweisdateiImArchivIstLesbarUndErklaertDenLeerstand() throws Exception {
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG);
		when(reportingContext.ausgabeumfang()).thenReturn(LEER_GEFILTERT);
		final PdfFactory factory = new PdfFactory(List.of(), reportingContext);

		final String inhalt = eintragsinhalt((byte[]) factory.createPdfResponse().getEntity(), ReportingHinweisSerializer.DATEINAME_HINWEISE);

		assertTrue(inhalt.contains("blieb nach der Auswahl kein Datensatz übrig"), inhalt);
		assertTrue(inhalt.contains("keine PDF-Datei erzeugt"), inhalt);
	}

	@Test
	void testEineVollstaendigeSammelausgabeErhaeltKeineHinweisdatei() throws Exception {
		// Ohne Befund und ohne Leerstand gibt es nichts zu erklären; eine leere Beilage stellte jede vollständige Ausgabe unter einen Vorbehalt.
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG);
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)),
				pdfBuilder("Bescheinigung_Schulz", Set.of(4712L)));

		assertEquals(List.of("Bescheinigung_Meier.pdf", "Bescheinigung_Schulz.pdf"), eintragsnamen(pdfFactory.createZIP(pdfBuilders)));
	}

	@Test
	void testEinArchivMitBefundTraegtDieHinweisdateiNebenDenDokumenten() throws Exception {
		// Der Fall der unvollständigen Sammelausgabe: Ein Datensatz von dreien wurde ausgelassen, zwei Dokumente entstehen. Die Beilage erklärt das Archiv
		// und verdrängt keines der Dokumente.
		gebeReportvorlageVor(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG);
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of(new ReportingProblem(ReportingProblemursache.NICHT_VORHANDEN,
				ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, ReportingProblemSchluessel.fuer(ReportingSchueler.class, 4713L))));
		final List<ReportBuilderPdf> pdfBuilders = List.of(
				pdfBuilder("Bescheinigung_Meier", Set.of(4711L)),
				pdfBuilder("Bescheinigung_Schulz", Set.of(4712L)));

		final byte[] zip = pdfFactory.createZIP(pdfBuilders);

		assertEquals(List.of("Bescheinigung_Meier.pdf", "Bescheinigung_Schulz.pdf", ReportingHinweisSerializer.DATEINAME_HINWEISE), eintragsnamen(zip));
		final String inhalt = eintragsinhalt(zip, ReportingHinweisSerializer.DATEINAME_HINWEISE);
		assertTrue(inhalt.contains("Die Ausgabe wurde erstellt, ist aber unvollständig."), inhalt);
		assertTrue(inhalt.contains("Hinweise insgesamt: 1"), inhalt);
	}

	/**
	 * Liest den Inhalt eines Eintrags aus den übergebenen ZIP-Daten.
	 *
	 * @param zipData      Die ZIP-Datei als Byte-Array.
	 * @param eintragsname Der Name des gesuchten Eintrags.
	 *
	 * @return Der Inhalt des Eintrags als Text.
	 *
	 * @throws IOException Falls die ZIP-Daten nicht gelesen werden können.
	 */
	private static String eintragsinhalt(final byte[] zipData, final String eintragsname) throws IOException {
		try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
			ZipEntry eintrag = zis.getNextEntry();
			while (eintrag != null) {
				if (eintragsname.equals(eintrag.getName())) {
					return new String(zis.readAllBytes(), StandardCharsets.UTF_8);
				}
				eintrag = zis.getNextEntry();
			}
		}
		return "";
	}

	/**
	 * Liest den Dateinamen aus dem {@code Content-Disposition}-Header einer Response.
	 *
	 * @param response Die Response der Ausgabe.
	 *
	 * @return Der dekodierte Dateiname.
	 */
	private static String dateinameAusResponse(final Response response) {
		final String disposition = response.getHeaderString("Content-Disposition");
		return URLDecoder.decode(disposition.substring(disposition.indexOf("UTF-8''") + "UTF-8''".length()), StandardCharsets.UTF_8);
	}

}
