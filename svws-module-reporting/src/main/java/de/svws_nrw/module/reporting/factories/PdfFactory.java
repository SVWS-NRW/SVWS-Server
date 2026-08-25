package de.svws_nrw.module.reporting.factories;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderContextPdf;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderPdf;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.diagnose.ReportingProblem;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * <p>Diese Klasse erstellt PDF-Dateien auf Basis von HTML.</p>
 * <p>Dabei werden bei der Initialisierung HTML-Builder übergeben, die die HTML-Inhalte erzeugen. Diese Inhalte werden genutzt, um daraus pdf-Builder zu
 * erzeugen.</p>
 */
public class PdfFactory {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingContext reportingContext;

	/** Map mit den Dateinamen und HTML-Dateiinhalten, die in PDF-Dateien gewandelt werden sollen. */
	private final List<ReportBuilderHtml> htmlBuilders;

	/**
	 * Gibt an, ob eine Ausgabe ohne PDF-Datei zulässig ist. Nur dieses Kennzeichen aus dem gemeldeten Ausgabeumfang macht eine leere Builder-Liste gültig;
	 * ohne es bleibt sie ein Programmierfehler, und ein Ausfall der Dokumenterzeugung ginge als leere Ausgabe durch.
	 */
	private final boolean leereAusgabeZulaessig;


	/**
	 * Erzeugt eine neue PdfFactory, um eine Pdf-Datei aus den übergebenen HTML-Inhalten zu erzeugen. Ob eine leere Ausgabe zulässig ist, stammt aus dem
	 * am Context gemeldeten Ausgabeumfang.
	 *
	 * @param htmlBuilders 				Eine Map mit den Dateinamen und HTML-Dateiinhalten.
	 * @param reportingContext		Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @throws ApiOperationException	Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	protected PdfFactory(final List<ReportBuilderHtml> htmlBuilders, final ReportingContext reportingContext)
			throws ApiOperationException {

		this.reportingContext = reportingContext;
		this.leereAusgabeZulaessig = (reportingContext.ausgabeumfang() != null) && reportingContext.ausgabeumfang().leereAusgabeZulaessig();

		this.reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Initialisierung der PDF-Factory und der Validierung der übergebenen Daten.");

		// Validiere die HTML-Builder. Ohne Liste liegt immer ein Programmierfehler vor; eine leere Liste ist allein bei einer zulässig leeren Ausgabe gültig.
		if (htmlBuilders == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die PDF-Erzeugung wurden keine HTML-Inhalte übergeben.");
		}
		if (htmlBuilders.isEmpty() && !leereAusgabeZulaessig) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Für die PDF-Erzeugung liegen keine HTML-Inhalte vor, obwohl die Ausgabe Daten enthalten sollte.");
		}
		if (htmlBuilders.isEmpty()) {
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 4,
					"Die Auswahl enthält keinen Datensatz. Die Ausgabe entsteht als ZIP-Datei ohne PDF-Dateien.");
		}
		this.htmlBuilders = htmlBuilders;

		this.reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Initialisierung der PDF-Factory und der Validierung der übergebenen Daten.");
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen PDF-Datei oder ZIP-Datei mit den mehreren generierten PDF-Dateien.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das PDF-Dokument oder die ZIP-Datei.
	 *
	 * @throws ApiOperationException	Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	protected Response createPdfResponse() throws ApiOperationException {

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
		final List<ReportBuilderPdf> pdfBuilders = getPdfBuilders();
		// Die Reihenfolge ist verbindlich: Zuerst zählt, wie viele Dokumente entstanden sind, erst danach, ob eine leere Menge zulässig ist. Eine bewusst
		// leere Auswahl in der Sammelausgabe ergibt einen Builder mit leerem fachlichem Inhalt - dort entsteht ein Dokument und kein leeres Archiv.
		if (pdfBuilders.isEmpty()) {
			if (!leereAusgabeZulaessig) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"### FEHLER: Es sind keine PDF-Dokumente entstanden, obwohl die Ausgabe Daten enthalten sollte.");
			}
			return erzeugeZipResponse(pdfBuilders);
		}
		if (pdfBuilders.size() == 1) {
			final ReportBuilderPdf einzigerPdfBuilder = pdfBuilders.getFirst();
			final byte[] data = einzigerPdfBuilder.generate();
			final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(einzigerPdfBuilder.getDateinameMitEndung(), StandardCharsets.UTF_8);
			reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
			return ReportingHinweiseHeader.ergaenze(
					Response.ok(data, einzigerPdfBuilder.getContentType()).header("Content-Disposition", "attachment; " + encodedFilename),
					reportingContext).build();
		}
		return erzeugeZipResponse(pdfBuilders);
	}


	/**
	 * Gibt an, ob eine Ausgabe ohne Dokument zulässig ist, weil keine Einheit des gemeldeten Ausgabeumfangs ausgegeben wird. Die PDF-Ausgabe antwortet dann
	 * mit einem Archiv ohne PDF-Dateien; der E-Mail-Versand reiht keinen Job ein, weil ein Job ohne Anhänge erfolgreich aussähe, ohne etwas zu versenden.
	 *
	 * @return true, wenn eine Ausgabe ohne Dokument zulässig ist, sonst false.
	 */
	protected boolean leereAusgabeZulaessig() {
		return this.leereAusgabeZulaessig;
	}

	/**
	 * Erzeugt die Response einer ZIP-Ausgabe aus den übergebenen PDF-Buildern. Die Liste darf leer sein; dann entsteht ein Archiv ohne PDF-Dateien. Der
	 * Dateiname stammt deshalb aus der Reportvorlage und nicht aus dem ersten Builder: Beide liefern denselben Wert, aber ohne Dokument gibt es keinen ersten
	 * Builder.
	 *
	 * @param pdfBuilders Die PDF-Builder, deren Dokumente in das Archiv gelangen; darf leer sein.
	 *
	 * @return Die Response mit der ZIP-Datei.
	 *
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	private Response erzeugeZipResponse(final List<ReportBuilderPdf> pdfBuilders) throws ApiOperationException {
		final byte[] data = createZIP(pdfBuilders);
		final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(zipDateiname(), StandardCharsets.UTF_8);
		reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
		return ReportingHinweiseHeader.ergaenze(
				Response.ok(data, "application/zip").header("Content-Disposition", "attachment; " + encodedFilename),
				reportingContext).build();
	}

	/**
	 * Der Dateiname der ZIP-Ausgabe, abgeleitet aus der Reportvorlage. Die Vorlage liegt hier zwingend vor, weil die {@code HtmlFactory} sie beim Aufbau prüft
	 * und diese Factory ohne deren HTML-Builder nicht entsteht.
	 *
	 * @return Der Dateiname des Archivs mit Endung.
	 */
	private String zipDateiname() {
		return reportingContext.reportingParameter().reportVorlage().getDateiname() + ".zip";
	}

	/**
	 * Erzeugt auf Basis der übergebenen HTML-Builder die PDF-Builder zur Erzeugung der PDF-Dateien.
	 *
	 * @return Ein oder mehrere ReportBuilderPdf-Instanzen zur Erzeugung der PDF-Dateien.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler bei der Generierung der PDF-Dateien auftritt.
	 */
	public List<ReportBuilderPdf> getPdfBuilders() throws ApiOperationException {

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der PDF-Builder.");
		final List<ReportBuilderPdf> pdfBuilders = new ArrayList<>();

		for (final ReportBuilderHtml htmlBuilder : this.htmlBuilders) {
			pdfBuilders.add(getReportBuilderPdf(htmlBuilder));
		}

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der PDF-Builder.");
		return pdfBuilders;
	}

	/**
	 * Erzeugt eine Zuordnung von IDs zu einer Liste von {@code ReportBuilderPdf}-Objekten, basierend
	 * auf den verfügbaren {@code ReportBuilderHtml}-Objekten. Jedem {@code ReportBuilderHtml} können eine oder
	 * mehrere IDs zugeordnet sein. Falls keine IDs vorhanden sind, wird der zugehörige
	 * {@code ReportBuilderPdf} optional der neutralen Gruppe (-1) hinzugefügt.
	 *
	 * @return Eine Map, die IDs (Schlüssel vom Typ {@code Long}) den jeweiligen Listen
	 *         von {@code ReportBuilderPdf}-Objekten (Werte vom Typ {@code List<ReportBuilderPdf>})
	 *         zuordnet. Die Schlüssel repräsentieren die IDs der zugehörigen {@code ReportBuilderHtml}.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler bei der Generierung der PDF-Dateien auftritt.
	 */
	public Map<Long, List<ReportBuilderPdf>> getPdfBuildersById() throws ApiOperationException {
		final Map<Long, List<ReportBuilderPdf>> result = new LinkedHashMap<>();

		for (final ReportBuilderHtml htmlBuilder : this.htmlBuilders) {
			final ReportBuilderPdf pdf = getReportBuilderPdf(htmlBuilder);

			final Set<Long> ids = htmlBuilder.getIds();

			// Falls kein Bezug hinterlegt ist, werden die Inhalte optional in neutraler Gruppe (-1) gesammelt.
			if (ids.isEmpty()) {
				result.computeIfAbsent(-1L, k -> new ArrayList<>()).add(pdf);
				continue;
			}

			for (final Long id : ids) {
				if (id == null) {
					continue;
				}
				result.computeIfAbsent(id, k -> new ArrayList<>()).add(pdf);
			}
		}
		return result;

	}


	/**
	 * Konvertiert einen HTML-ReportBuilder in einen PDF-ReportBuilder.
	 *
	 * @param reportBuilderHtml Der HTML-ReportBuilder, der die Eingabedaten und Konfigurationen für die PDF-Erstellung enthält.
	 *
	 * @return Eine Instanz von ReportBuilderPdf, die zur Generierung eines PDF-Dokuments verwendet werden kann.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler während der Erstellung des ReportBuilderPdf auftritt.
	 */
	private ReportBuilderPdf getReportBuilderPdf(final ReportBuilderHtml reportBuilderHtml) throws ApiOperationException {
		final ReportBuilderContextPdf reportBuilderContext =
				new ReportBuilderContextPdf()
						.withHtmlInput(reportBuilderHtml.generate())
						.withDateiname(reportBuilderHtml.getDateiname())
						.addIds(reportBuilderHtml.getIds())
						.withStatischerDateiname(reportBuilderHtml.getStatischerDateiname())
						.withRootPfad(reportBuilderHtml.getRootPfad());
		return new ReportBuilderPdf(reportBuilderContext);
	}


	/**
	 * Erstellt eine ZIP-Datei, die alle PDF-Dateien der übergebenen PDF-Builder enthält.
	 * <p>Die Einträge erhalten ihre Namen zuvor über {@link #ermittleZipEintragsnamen(List)}, weil {@code putNextEntry} bei einem bereits vergebenen
	 * Namen abbrechen würde.</p>
	 *
	 * @param pdfBuilders Eine Liste mit ReportBuilderPdf, die die einzelnen PDF-Dateien erzeugen.
	 *
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 *
	 * @throws ApiOperationException	Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	byte[] createZIP(final List<ReportBuilderPdf> pdfBuilders) throws ApiOperationException {
		final List<String> eintragsnamen = ermittleZipEintragsnamen(pdfBuilders);
		final byte[] zipData;
		try {
			try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
					for (int i = 0; i < pdfBuilders.size(); i++) {
						addPdfToZip(pdfBuilders.get(i), eintragsnamen.get(i), zos);
					}
					ergaenzeHinweisdatei(zos);
					byteArrayOutputStream.flush();
				}
				zipData = byteArrayOutputStream.toByteArray();
			}
		} catch (final IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Die PDF-Dateien konnten nicht zu einer ZIP-Datei zusammengestellt werden.");
		}
		return zipData;
	}

	/**
	 * Ein Namensvorschlag für einen einzelnen PDF-Builder, angereichert um die Angaben, die für die Auflösung sekundärer Kollisionen nötig sind.
	 *
	 * @param position            Die Position des Builders in der Ausgabeliste. Über sie wird das Ergebnis wieder in die Ausgabereihenfolge einsortiert.
	 * @param originalname        Der Dateiname, den der Builder selbst meldet.
	 * @param vorgeschlagenerName Der Name aus der ersten Phase. Er entspricht dem Originalnamen oder trägt ein Suffix.
	 * @param fachlicheId         Die fachliche ID des Builders, sofern er genau eine trägt, sonst {@code null}.
	 * @param nameErzeugt         Gibt an, ob der Vorschlag von der ersten Phase erzeugt wurde und damit vom Originalnamen abweicht.
	 */
	private record Namensvorschlag(int position, String originalname, String vorgeschlagenerName, Long fachlicheId, boolean nameErzeugt) {
		// Reiner Datenträger zwischen den beiden Phasen der Namensvergabe.
	}

	/**
	 * Die Reihenfolge, in der die Vorschläge um einen Namen konkurrieren. Sie ist bewusst unabhängig von der Position in der Ausgabeliste, damit eine
	 * andere Sortierung der Hauptdaten nicht die Zuordnung Datei zu Datensatz verschiebt: Ein tatsächlich so benannter Eintrag geht einem erzeugten Namen
	 * vor, danach entscheidet die fachliche ID und zuletzt der Originalname. Die Position dient nur noch als letzter Stichentscheid für Einträge, die sich
	 * in keinem dieser Merkmale unterscheiden — für sie gibt es keinen stabilen fachlichen Schlüssel.
	 */
	private static final Comparator<Namensvorschlag> PRIORITAET_BEI_KOLLISION =
			Comparator.comparing(Namensvorschlag::nameErzeugt)
					.thenComparing(Namensvorschlag::fachlicheId, Comparator.nullsLast(Long::compareTo))
					.thenComparing(Namensvorschlag::originalname)
					.thenComparingInt(Namensvorschlag::position);


	/**
	 * Ermittelt zu jedem PDF-Builder den Namen, unter dem seine Datei im ZIP abgelegt wird. Die Rückgabe ist positionsgleich zur übergebenen Liste und
	 * garantiert paarweise verschiedene Namen — {@code putNextEntry} bricht bei einem bereits vergebenen Namen ab.
	 * <p>Die Vergabe erfolgt in zwei Phasen. Zuerst löst {@link #ermittleNamensvorschlaege(List)} die Gruppen gleich benannter Einträge auf, bevorzugt über
	 * deren fachliche ID. Anschließend werden die Vorschläge <b>global</b> gegeneinander geprüft, denn ein erzeugter Name kann mit dem Namen eines
	 * unbeteiligten Eintrags zusammenfallen: Treffen etwa zwei Einträge {@code Meier.pdf} mit den IDs 4711 und 4712 auf einen bereits vorhandenen
	 * {@code Meier-4711.pdf}, entstünde dieser Name zweimal.</p>
	 * <p>Wer in einer solchen sekundären Kollision den Namen behält, entscheidet nicht die Eingabereihenfolge, sondern
	 * {@link #PRIORITAET_BEI_KOLLISION}. Die unterlegenen Einträge erhalten eine laufende Nummer ab 2. Damit bleibt die Zuordnung Datei zu Datensatz auch
	 * dann erhalten, wenn sich Sortierung oder Auswahl der Hauptdaten ändern.</p>
	 * <p><b>Die beiden Phasen nummerieren unterschiedlich, und das ist beabsichtigt:</b> In der ersten Phase erhalten <b>alle</b> Beteiligten einer Gruppe
	 * ein Suffix, weshalb dort ab 1 gezählt wird und keine unnummerierte Datei neben nummerierten steht. In der zweiten Phase behält der bevorrechtigte
	 * Eintrag seinen Namen — er ist damit faktisch die Nummer 1, und die Zählung der Ausweichenden beginnt bei 2.</p>
	 * <p>Beide Phasen vergleichen über {@link #vergleichsname(String)}, also ohne Beachtung der Groß- und Kleinschreibung.</p>
	 *
	 * @param pdfBuilders Die PDF-Builder in der Reihenfolge, in der sie in das ZIP geschrieben werden.
	 *
	 * @return Die paarweise verschiedenen Eintragsnamen, positionsgleich zur übergebenen Liste.
	 */
	private List<String> ermittleZipEintragsnamen(final List<ReportBuilderPdf> pdfBuilders) {
		final List<Namensvorschlag> nachPrioritaet = new ArrayList<>(ermittleNamensvorschlaege(pdfBuilders));
		nachPrioritaet.sort(PRIORITAET_BEI_KOLLISION);

		final Set<String> vergebeneNamen = new HashSet<>();
		final String[] eintragsnamen = new String[nachPrioritaet.size()];
		for (final Namensvorschlag vorschlag : nachPrioritaet) {
			String eintragsname = vorschlag.vorgeschlagenerName();
			int lfd = 1;
			while (!vergebeneNamen.add(vergleichsname(eintragsname))) {
				lfd++;
				eintragsname = ergaenzeSuffixVorDateiendung(vorschlag.vorgeschlagenerName(), String.valueOf(lfd));
			}
			if (!eintragsname.equals(vorschlag.vorgeschlagenerName())) {
				reportingContext.logger().logLn(LogLevel.INFO, 4,
						"Der Eintragsname '%s' war im ZIP bereits vergeben. Der Eintrag wird als '%s' abgelegt."
								.formatted(vorschlag.vorgeschlagenerName(), eintragsname));
			}
			eintragsnamen[vorschlag.position()] = eintragsname;
		}
		return List.of(eintragsnamen);
	}

	/**
	 * Bildet einen Dateinamen auf die Form ab, in der Namen miteinander verglichen werden: ohne Beachtung der Groß- und Kleinschreibung.
	 * <p>Zwei Dateinamen, die sich allein darin unterscheiden, benennen denselben Sachverhalt — eine Person namens Meier heißt Meier, unabhängig von der
	 * Schreibweise. Sie als verschieden zu behandeln, führte zu zwei Dateien, die dieselbe Datei zu sein scheinen. Hinzu kommt, dass die Dateisysteme von
	 * Windows und macOS sie beim Entpacken ohnehin nicht nebeneinander ablegen können und eine der beiden Dateien verloren ginge.</p>
	 * <p>Der Vergleich betrifft ausschließlich das Erkennen von Kollisionen. Die Schreibweise der Namen selbst bleibt unverändert.</p>
	 *
	 * @param dateiname Der zu vergleichende Dateiname.
	 *
	 * @return Die Vergleichsform des Dateinamens.
	 */
	private static String vergleichsname(final String dateiname) {
		// Locale.ROOT statt der Standard-Locale: Bei türkischer Locale würde aus 'I' das punktlose 'ı' und der Vergleich hinge von der Serverkonfiguration ab.
		return dateiname.toLowerCase(Locale.ROOT);
	}

	/**
	 * Löst die Gruppen gleich benannter Einträge auf — bevorzugt über deren fachliche ID, sonst über eine laufende Nummer ab 1. Die Rückgabe ist
	 * positionsgleich zur übergebenen Liste und kann noch Dubletten enthalten; für die abschließende Eindeutigkeit sorgt
	 * {@link #ermittleZipEintragsnamen(List)}, das dabei ab 2 zählt.
	 * <p>Kollisionsfreie Dateinamen bleiben unverändert. Tragen mehrere Builder denselben Dateinamen, erhalten <b>alle</b> beteiligten Einträge die
	 * fachliche ID als Suffix vor der Dateiendung — auch der erste, weil ein gemischtes Schema aus ID und ursprünglichem Namen nicht erkennen ließe, welcher
	 * Datensatz gemeint ist. Die ID wird bevorzugt, weil damit die Zuordnung Datei zu Datensatz über mehrere Läufe stabil bleibt, auch wenn sich Sortierung
	 * oder Auswahl der Hauptdaten ändern.</p>
	 * <p>Taugen die IDs der Gruppe nicht als Suffix, erhalten die Einträge stattdessen eine laufende Nummer ab 1. Die Entscheidung fällt einheitlich für die
	 * gesamte Kollisionsgruppe. Für diese Nummer gibt es keinen fachlichen Anker: Ohne eindeutige ID besitzen die Dokumente keinen stabilen Schlüssel, an
	 * dem sich eine Nummer dauerhaft festmachen ließe.</p>
	 * <p>Ob zwei Dateinamen zur selben Gruppe gehören, entscheidet {@link #vergleichsname(String)} und damit nicht die Groß- und Kleinschreibung. Der
	 * Vorschlag selbst wird stets aus dem Dateinamen des jeweiligen Builders gebildet, sodass dessen Schreibweise erhalten bleibt.</p>
	 *
	 * @param pdfBuilders Die PDF-Builder in der Reihenfolge, in der sie in das ZIP geschrieben werden.
	 *
	 * @return Die Namensvorschläge, positionsgleich zur übergebenen Liste.
	 */
	private List<Namensvorschlag> ermittleNamensvorschlaege(final List<ReportBuilderPdf> pdfBuilders) {
		final Namensvorschlag[] vorschlaege = new Namensvorschlag[pdfBuilders.size()];
		for (final List<Integer> positionen : gruppierePositionenNachDateiname(pdfBuilders).values()) {
			for (final Namensvorschlag vorschlag : vorschlaegeDerGruppe(pdfBuilders, positionen)) {
				vorschlaege[vorschlag.position()] = vorschlag;
			}
		}
		return List.of(vorschlaege);
	}

	/**
	 * Gruppiert die Positionen der Builder nach ihrem Dateinamen. Maßgeblich ist die Vergleichsform, sodass Namen, die sich allein in der Groß- und
	 * Kleinschreibung unterscheiden, in dieselbe Gruppe fallen.
	 *
	 * @param pdfBuilders Die PDF-Builder in der Reihenfolge, in der sie in das ZIP geschrieben werden.
	 *
	 * @return Die Positionen je Dateiname, in der Reihenfolge des ersten Auftretens.
	 */
	private static Map<String, List<Integer>> gruppierePositionenNachDateiname(final List<ReportBuilderPdf> pdfBuilders) {
		final Map<String, List<Integer>> positionenJeDateiname = new LinkedHashMap<>();
		for (int i = 0; i < pdfBuilders.size(); i++) {
			positionenJeDateiname.computeIfAbsent(vergleichsname(pdfBuilders.get(i).getDateinameMitEndung()), k -> new ArrayList<>()).add(i);
		}
		return positionenJeDateiname;
	}

	/**
	 * Bildet die Namensvorschläge einer einzelnen Gruppe gleich benannter Builder.
	 *
	 * @param pdfBuilders Alle PDF-Builder.
	 * @param positionen  Die Positionen der Builder, die denselben Dateinamen tragen.
	 *
	 * @return Die Vorschläge in der Reihenfolge der übergebenen Positionen.
	 */
	private List<Namensvorschlag> vorschlaegeDerGruppe(final List<ReportBuilderPdf> pdfBuilders, final List<Integer> positionen) {
		final boolean kollision = (positionen.size() > 1);
		final List<Long> ids = kollision ? eindeutigeIdsDerGruppe(pdfBuilders, positionen) : List.of();
		if (kollision) {
			protokolliereKollision(pdfBuilders, positionen, ids);
		}

		final List<Namensvorschlag> vorschlaege = new ArrayList<>(positionen.size());
		for (int lfd = 0; lfd < positionen.size(); lfd++) {
			final int position = positionen.get(lfd);
			final ReportBuilderPdf pdfBuilder = pdfBuilders.get(position);
			final String originalname = pdfBuilder.getDateinameMitEndung();
			final String vorgeschlagenerName = kollision ? ergaenzeSuffixVorDateiendung(originalname, gruppensuffix(ids, lfd)) : originalname;
			vorschlaege.add(new Namensvorschlag(position, originalname, vorgeschlagenerName, fachlicheId(pdfBuilder), kollision));
		}
		return vorschlaege;
	}

	/**
	 * Bestimmt das Suffix für einen Eintrag einer Kollisionsgruppe: die fachliche ID, sofern die Gruppe welche beisteuern konnte, sonst die laufende Nummer
	 * ab 1. Die Nummer ist einsbasiert, weil in dieser Phase auch der erste Eintrag ein Suffix erhält.
	 *
	 * @param ids Die IDs der Gruppe oder eine leere Liste, wenn sie nicht als Suffix taugen.
	 * @param lfd Die nullbasierte Position des Eintrags innerhalb der Gruppe.
	 *
	 * @return Das Suffix ohne Trennzeichen.
	 */
	private static String gruppensuffix(final List<Long> ids, final int lfd) {
		return ids.isEmpty() ? String.valueOf(lfd + 1) : String.valueOf(ids.get(lfd));
	}

	/**
	 * Protokolliert eine Kollisionsgruppe und die Art ihrer Auflösung.
	 *
	 * @param pdfBuilders Alle PDF-Builder.
	 * @param positionen  Die Positionen der Builder, die denselben Dateinamen tragen.
	 * @param ids         Die IDs der Gruppe oder eine leere Liste, wenn sie nicht als Suffix taugen.
	 */
	private void protokolliereKollision(final List<ReportBuilderPdf> pdfBuilders, final List<Integer> positionen, final List<Long> ids) {
		reportingContext.logger().logLn(LogLevel.INFO, 4,
				"Der Dateiname '%s' wurde %d mal vergeben. Die Einträge werden im ZIP um %s ergänzt."
						.formatted(pdfBuilders.get(positionen.getFirst()).getDateinameMitEndung(), positionen.size(),
								ids.isEmpty() ? "eine laufende Nummer" : "ihre fachliche ID"));
	}

	/**
	 * Gibt die fachliche ID eines Builders zurück, sofern er genau eine trägt. Bei keiner oder mehreren IDs ließe sich keine ID deterministisch auswählen.
	 *
	 * @param pdfBuilder Der Builder, dessen ID ermittelt werden soll.
	 *
	 * @return Die eine fachliche ID des Builders oder {@code null}.
	 */
	private static Long fachlicheId(final ReportBuilderPdf pdfBuilder) {
		final Set<Long> ids = pdfBuilder.getIds();
		return (ids.size() == 1) ? ids.iterator().next() : null;
	}

	/**
	 * Ermittelt die fachlichen IDs einer Kollisionsgruppe, sofern sie als Suffix taugen: Jeder beteiligte Builder muss genau eine ID tragen, und die IDs
	 * müssen untereinander verschieden sein. Andernfalls bliebe die Zuordnung Datei zu Datensatz mehrdeutig.
	 *
	 * @param pdfBuilders Alle PDF-Builder.
	 * @param positionen  Die Positionen der Builder, die denselben Dateinamen tragen.
	 *
	 * @return Die IDs in der Reihenfolge der übergebenen Positionen, oder eine leere Liste, wenn die IDs nicht als Suffix taugen. Eine Kollisionsgruppe
	 *         umfasst stets mindestens zwei Einträge, sodass eine leere Liste eindeutig den Rückfall auf die laufende Nummer bedeutet.
	 */
	private static List<Long> eindeutigeIdsDerGruppe(final List<ReportBuilderPdf> pdfBuilders, final List<Integer> positionen) {
		final List<Long> ids = new ArrayList<>();
		for (final int position : positionen) {
			final Long id = fachlicheId(pdfBuilders.get(position));
			if (id == null) {
				return List.of();
			}
			ids.add(id);
		}
		if (new HashSet<>(ids).size() != ids.size()) {
			return List.of();
		}
		return ids;
	}

	/**
	 * Ergänzt einen Dateinamen um ein Suffix vor der Dateiendung.
	 *
	 * @param dateinameMitEndung Der vollständige Dateiname einschließlich Endung.
	 * @param suffix             Das anzuhängende Suffix ohne Trennzeichen.
	 *
	 * @return Der Dateiname mit eingefügtem Suffix.
	 */
	private static String ergaenzeSuffixVorDateiendung(final String dateinameMitEndung, final String suffix) {
		final int trennstelle = dateinameMitEndung.lastIndexOf('.');
		if (trennstelle < 0) {
			return dateinameMitEndung + "-" + suffix;
		}
		return dateinameMitEndung.substring(0, trennstelle) + "-" + suffix + dateinameMitEndung.substring(trennstelle);
	}

	/**
	 * Ergänzt das Archiv um die Hinweisdatei, sofern es etwas zu erklären gibt. Sie ist die einzige Stelle, an der die Hinweise den Anwender ohne
	 * Clientanpassung erreichen; besonders ein Archiv ohne PDF-Datei braucht sie, sonst stünde der Anwender vor einem leeren Download. Sie erklärt, was
	 * bekannt ist, und behauptet keine Vollständigkeit.
	 *
	 * @param zos Der {@code ZipOutputStream}, zu dem die Hinweisdatei hinzugefügt wird.
	 *
	 * @throws ApiOperationException Falls der Eintrag nicht geschrieben werden kann.
	 */
	private void ergaenzeHinweisdatei(final ZipOutputStream zos) throws ApiOperationException {
		final List<ReportingProblem> probleme = reportingContext.ausgabeprobleme();
		final ReportingAusgabeumfang umfang = reportingContext.ausgabeumfang();
		if ((umfang == null) || !ReportingHinweisSerializer.brauchtHinweisdatei(umfang, probleme)) {
			return;
		}
		try {
			zos.putNextEntry(new ZipEntry(ReportingHinweisSerializer.DATEINAME_HINWEISE));
			zos.write(ReportingHinweisSerializer.hinweisdatei(umfang, probleme).getBytes(StandardCharsets.UTF_8));
			zos.closeEntry();
		} catch (final Exception e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Die Hinweisdatei der ZIP-Ausgabe konnte nicht geschrieben werden.");
		}
	}

	/**
	 * Fügt eine generierte PDF-Datei zum ZIP-Output-Stream hinzu.
	 *
	 * @param pdfBuilder Der {@code ReportBuilderPdf}, der die PDF-Datei erzeugt
	 * @param eintragsname Der Name, unter dem der Eintrag im ZIP abgelegt wird. Er kann sich durch die Kollisionsauflösung vom Dateinamen des Builders
	 *                     unterscheiden.
	 * @param zos Der {@code ZipOutputStream}, zu dem die PDF-Datei hinzugefügt wird
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler bei der Generierung der PDF-Datei auftritt. Ein bereits klassifizierter Fehler des
	 *                               Builders behält seinen Status.
	 */
	private void addPdfToZip(final ReportBuilderPdf pdfBuilder, final String eintragsname, final ZipOutputStream zos) throws ApiOperationException {
		try {
			zos.putNextEntry(new ZipEntry(eintragsname));
			zos.write(pdfBuilder.generate());
			zos.closeEntry();
		} catch (final ApiOperationException e) {
			// Der Builder klassifiziert den Fehler bereits nach seiner Quelle; ohne diesen Zweig macht die Sammelausgabe daraus einen Serverfehler.
			// Protokolliert wird allein der Dateiname: Er ist bei einer Sammelausgabe die einzige Angabe, welches Dokument betroffen ist. Die Meldung
			// der Fehlerquelle reist mit der Exception und erscheint im Fehlerblock der Abschlussgrenze.
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "Betroffene Datei: " + pdfBuilder.getDateiname());
			throw e;
		} catch (final Exception e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Die PDF-Datei '%s' konnte nicht erzeugt werden.".formatted(pdfBuilder.getDateiname()));
		}
	}
}
