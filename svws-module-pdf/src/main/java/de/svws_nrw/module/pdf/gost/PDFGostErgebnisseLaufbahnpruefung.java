package de.svws_nrw.module.pdf.gost;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerFehler;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerGrunddaten;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerHinweise;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerSummen;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.schild3.reporting.DataSchildReportingDatenquelle;
import de.svws_nrw.data.schild3.reporting.DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFehler;
import de.svws_nrw.data.schild3.reporting.DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungGrunddaten;
import de.svws_nrw.data.schild3.reporting.DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungHinweise;
import de.svws_nrw.data.schild3.reporting.DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.PDFCreator;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung eines Wahlbogens
 * für die Laufbahnplanung der gymnasialen Oberstufe.
 */
public final class PDFGostErgebnisseLaufbahnpruefung extends PDFCreator {

	/** Das HTML-Dokument mit Platzhaltern, aus der später die PDF-Datei erzeugt wird. */
	private static final String html = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostErgebnisseLaufbahnpruefung.html");

	/** Der Dateiname für die PDF-Datei */
	private final String filename;


	/**
	 * Erstellt den PDF-Wahlbogen auf Basis der HTML-Vorlage
	 *
	 * @param dateiname        	Dateiname der finalen PDF-Datei. Ist dieser leer, so wird aus den übergebenen Daten des Schülers ein eindeutiger Dateiname erzeugt.
	 * @param detaillevel      	gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 * @param schulnummer      	Schulnummer der Schule des Schülers, dessen Wahlbogen erstellt werden soll
	 * @param schulbezeichnung 	Array mit den drei Einträgen der Schulbezeichnung
	 * @param abiturjahr 		das Abiturjahr
	 * @param schuelerIDs 		Liste mit Schüler-IDs, die nach den Namen der Schüler sortiert sind.
	 * @param mapSchueler      	Schülerobjekt des Schülers, dessen Wahlbogen erstellt werden soll.
	 * @param mapGrunddaten    	Grunddaten der Laufbahn des Schülers
	 * @param mapSummen        	Summen zur Laufbahn des Schülers
	 * @param mapFehler        	Fehler in der Laufbahn des Schülers
	 * @param mapHinweise      	Hinweise zur Laufbahn des Schülers
	 */
	private PDFGostErgebnisseLaufbahnpruefung(final String dateiname,
											  final int detaillevel,
											  final String schulnummer,
											  final String[] schulbezeichnung,
											  final int abiturjahr,
											  final List<Long> schuelerIDs,
											  final Map<Long, DTOSchueler> mapSchueler,
											  final Map<Long, DruckGostLaufbahnplanungSchuelerGrunddaten> mapGrunddaten,
											  final Map<Long, DruckGostLaufbahnplanungSchuelerSummen> mapSummen,
											  final Map<Long, List<DruckGostLaufbahnplanungSchuelerFehler>> mapFehler,
											  final Map<Long, List<DruckGostLaufbahnplanungSchuelerHinweise>> mapHinweise) {
		super(html);

		this.filename = dateiname;

		// Ersetze die Felder des Templates mit Grunddaten im Kopf der ersten Seite.
		// Da ein ganzer Abiturjahrgang gedruckt wird, verwende ich Daten des ersten Schülers
		htmlData.put("SCHULNUMMER", schulnummer);
		htmlData.put("SCHULBEZEICHNUNG_1", schulbezeichnung[0] == null ? "" : schulbezeichnung[0]);
		htmlData.put("SCHULBEZEICHNUNG_2", schulbezeichnung[1] == null ? "" : schulbezeichnung[1]);
		htmlData.put("SCHULBEZEICHNUNG_3", schulbezeichnung[2] == null ? "" : schulbezeichnung[2]);
		htmlData.put("ABITURJAHR", String.valueOf(abiturjahr));
		htmlData.put("AKTUELLESHALBJAHR", mapGrunddaten.get(schuelerIDs.get(0)).aktuellesGOStHalbjahr);
		htmlData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		htmlData.put("SCHUELERTABELLENMITDATEN", getSchuelerTabellendaten(detaillevel, schuelerIDs, mapSchueler, mapGrunddaten, mapSummen, mapFehler, mapHinweise));
	}

	private String getSchuelerTabellendaten(final int detaillevel,
											final List<Long> schuelerIDs,
											final Map<Long, DTOSchueler> mapSchueler,
											final Map<Long, DruckGostLaufbahnplanungSchuelerGrunddaten> mapGrunddaten,
											final Map<Long, DruckGostLaufbahnplanungSchuelerSummen> mapSummen,
											final Map<Long, List<DruckGostLaufbahnplanungSchuelerFehler>> mapFehler,
											final Map<Long, List<DruckGostLaufbahnplanungSchuelerHinweise>> mapHinweise) {

		int lfdNr = 0;

		StringBuilder schuelerDatentabellen = new StringBuilder();

		for (final Long schuelerID : schuelerIDs) {
			// Pro Schüler wird eine Tabelle erzeugt, die folgende Inhalte enthalten.
			// 1. Zeile: Spaltendefinition mit Linie oberhalb und Spacing
			// 2. Zeile: Alle Daten: LfdNr, Name, Kurse, Stunden, Summen, Fehlerzahl
			// 3. Zeile: Beschriftungen zur Zeile 2
			// 4+ Ja nach Detaillevel: Nichts, Fehler, Hinweise.
			// Letzte Zeile: Spacing und Linie unten

			lfdNr++;
            final DTOSchueler schueler = mapSchueler.get(schuelerID);
			final DruckGostLaufbahnplanungSchuelerGrunddaten grunddaten = mapGrunddaten.get(schuelerID);
			final DruckGostLaufbahnplanungSchuelerSummen summen = mapSummen.get(schuelerID);
			final List<DruckGostLaufbahnplanungSchuelerFehler> fehler = mapFehler.get(schuelerID);
			final List<DruckGostLaufbahnplanungSchuelerHinweise> hinweise = mapHinweise.get(schuelerID);
			final int anzahlFehler = fehler == null ? 0 : fehler.size();
			final int anzahlHinweise = hinweise == null ? 0 : hinweise.size();

			String beratungsdatum = "Beratungsdatum: ";
			if (grunddaten.beratungsdatum != null && !grunddaten.beratungsdatum.isBlank() && !grunddaten.beratungsdatum.isEmpty())
				beratungsdatum += LocalDate.parse(grunddaten.beratungsdatum, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			else
				beratungsdatum += "----";

			final String schuelerDatentabelle =
				"""
					<table width="100%%" style="table-layout:fixed; border-collapse: collapse; page-break-inside: avoid; font-size: 9pt;" cellspacing="0" cellpadding="0">
					    <tr style="height: 5px;">
					        <td class="btGray" style="width:4%%;"></td>
					        <td class="btGray" style="width:30%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:4%%;"></td>
					        <td class="btGray" style="width:1%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:3%%;"></td>
					        <td class="btGray" style="width:7%%;"></td>
					        <td class="btGray" style="width:6%%;"></td>
					        <td class="btGray" style="width:6%%;"></td>
					        <td class="btGray" style="width:1%%;"></td>
					        <td class="btGray" style="width:5%%;"></td>
					    </tr>
					    <tr style="text-align: center;">
					        <td style="text-align: left; font-size: 8pt; color: gray">%s</td>
					        <td style="text-align: left;">%s, %s</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style=" font-weight: bold;%s">%d</td>
					        <td></td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="%s">%d</td>
					        <td class="bbGray" style="font-weight: bold;%s">%.1f</td>
					        <td class="bbGray" style="font-weight: bold;%s">%.1f</td>
					        <td class="bbGray" style="font-weight: bold;%s">%.1f</td>
					        <td></td>
					        <td class="bbGray" style="font-weight: bold;%s">%d</td>
					    </tr>
					    <tr style="font-size: 7pt; color: gray;">
					        <td></td>
					        <td style="text-align: left;">%s</td>
					        <td colspan="7" style="text-align: center;">anrechenbare Kurse</td>
					        <td></td>
					        <td colspan="9" style="text-align: center;">Wochenstunden (gesamt und Durchschnitt EF + QPh)</td>
					        <td></td>
					        <td style="text-align: center;">Fehler</td>
					    </tr>
					    %s
					    %s
					    <tr style="height:5px;">
					        <td class="bbGray" colspan="21"></td>
					    </tr>
					</table>
					""".formatted(
						String.format("%03d" + ".", lfdNr),
						schueler.Nachname, schueler.Vorname,
						// RGB-Farbkodierung im Folgenden: 255,0,0 ist rot und 255,255,0 ist gelb.
						getBgColor(summen.kursanzahlEF1, 10, 10), summen.kursanzahlEF1,
						getBgColor(summen.kursanzahlEF2, 10, 10), summen.kursanzahlEF2,
						getBgColor(summen.kursanzahlQ11,  9,  9), summen.kursanzahlQ11,
						getBgColor(summen.kursanzahlQ12,  9,  9), summen.kursanzahlQ12,
						getBgColor(summen.kursanzahlQ21,  9,  9), summen.kursanzahlQ21,
						getBgColor(summen.kursanzahlQ22,  9,  9), summen.kursanzahlQ22,
						getBgColor(summen.kursanzahlQPh, 38, 39), summen.kursanzahlQPh,

						getBgColor(summen.wochenstundenEF1, 32, 32), summen.wochenstundenEF1,
						getBgColor(summen.wochenstundenEF2, 32, 32), summen.wochenstundenEF2,
						getBgColor(summen.wochenstundenQ11, 31, 33), summen.wochenstundenQ11,
						getBgColor(summen.wochenstundenQ12, 31, 33), summen.wochenstundenQ12,
						getBgColor(summen.wochenstundenQ21, 31, 33), summen.wochenstundenQ21,
						getBgColor(summen.wochenstundenQ22, 31, 33), summen.wochenstundenQ22,
						getBgColor(summen.wochenstundenGesamt, 100, 101.75), summen.wochenstundenGesamt,

						summen.wochenstundenDurchschnittEF 	< 34 ? "background-color: red;" : "", summen.wochenstundenDurchschnittEF,
						summen.wochenstundenDurchschnittQPh < 34 ? "background-color: red;" : "", summen.wochenstundenDurchschnittQPh,

						anzahlFehler > 0 ? "background-color: red;" : "", anzahlFehler,

						beratungsdatum,
						(detaillevel > 0 && anzahlFehler > 0) ? getAusgabeBelegungsfehler(fehler) : "",
						(detaillevel > 1 && anzahlHinweise > 0) ? getAusgabeBelegungshinweise(hinweise) : ""
					);

			schuelerDatentabellen.append(schuelerDatentabelle);
		}

		return schuelerDatentabellen.toString();
	}


	/**
	 * Setzt die Hintergrundfarbe für kritische Einträge bei den Kurszahlen und Wochenstunden in der Liste
	 *
	 * @param value				Wert, der ausgegeben wird
	 * @param limitRed			Unterschreitet value diesen Wert, so wird der Hintergrund rot.
	 * @param limitYellow		Unterschreitet value diesen Wert oder ist value gleich diesem Wert, so wird der Hintergrund gelb.
	 *
	 * @return					html-css-style für die Hintergrundfarbe von td. Ist keine Bedingung erfüllt, wird ein leerer String zurückgegeben.
	 */
	private String getBgColor(final double value, final double limitRed, final double limitYellow) {
		if (value < limitRed)
			return "background-color: red;";
		else if (value <= limitYellow)
			return "background-color: yellow;";
		else
			return "";
	}


	/**
	 * Ersetzt den Platzhalter für die Belegungsfehler in der HTML-Vorlage
	 *
	 * @param listFehler		Liste der Fehlermeldungen zur Laufbahn
	 *
	 * @return HTML-Tabellenzeile mit der Liste der Belegungsfehler gemäß Gesamtprüfung
	 */
	private String getAusgabeBelegungsfehler(final List<DruckGostLaufbahnplanungSchuelerFehler> listFehler) {
		if (listFehler == null || listFehler.isEmpty()) {
			return "";
		} else {
			final StringBuilder sb = new StringBuilder();
			final List<String> fehler = new ArrayList<>(listFehler.stream().map(f -> f.belegungsfehler).toList());
			ul(sb, fehler);
			return """
                   <tr>
                       <td></td>
                       <td colspan="19" style="font-size: 8pt; color: gray; text-align: left;"><p><u>Belegungsfehler gemäß Gesamtprüfung:</u></p>
                           %s
                       </td>
                       <td></td>
                   </tr>
                   """.formatted(sb.toString());
		}
	}

	/**
	 * Ersetzt den Platzhalter für die Belegungshinweise in der HTML-Vorlage
	 *
	 * @param listHinweise		Liste der Hinweise zur Laufbahn
	 *
	 * @return HTML-Tabellenzeile mit der Liste der Belegungsfehler gemäß Gesamtprüfung
	 */
	private String getAusgabeBelegungshinweise(final List<DruckGostLaufbahnplanungSchuelerHinweise> listHinweise) {
		if (listHinweise == null || listHinweise.isEmpty())
			return "";
		else {
			final StringBuilder sb = new StringBuilder();
			final List<String> hinweise = listHinweise.stream().map(f -> f.belegungshinweis).toList();
			ul(sb, hinweise);
			return """
                   <tr>
                       <td></td>
                       <td colspan="19" style="font-size: 8pt; color: gray; text-align: left;"><p><u>Hinweise gemäß Gesamtprüfung:</u></p>
                           %s
                       </td>
                       <td></td>
                   </tr>
                   """.formatted(sb.toString());
		}
	}


	/**
	 * Erstellt ein neues Objekt des Typs PDFGostWahlbogen für den Wahlbogen zu der Laufbahn
	 * eines Schülers der gymnasialen Oberstufe.
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param abiturjahr 	das Abiturjahr
	 * @param detaillevel 	gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 *
	 * @return 				das Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFGostErgebnisseLaufbahnpruefung getPDFmitErgebnisseLaufbahnpruefung(final DBEntityManager conn, final int abiturjahr, final int detaillevel) throws WebApplicationException {

		// Schuldaten sammeln, pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
		DTOEigeneSchule schule;
		try {
			schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		} catch (WebApplicationException ex) {
			throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
		}
		final String schulnummer = schule.SchulNr.toString();
		final String[] schulbezeichnung = new String[] {schule.Bezeichnung1, schule.Bezeichnung2, schule.Bezeichnung3};

		// Bestimme alle Schüler des Abiturjahrgangs
		final List<DTOViewGostSchuelerAbiturjahrgang> queryResult = (abiturjahr == -1)
			? conn.queryAll(DTOViewGostSchuelerAbiturjahrgang.class)
			: conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abiturjahr, DTOViewGostSchuelerAbiturjahrgang.class);

		if ((queryResult == null) || queryResult.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Schüler zum angegebenen Abiturjahrgang gefunden.");

		final List<Long> schuelerIDs = queryResult.stream().filter(s -> (s.Status == SchuelerStatus.AKTIV || s.Status == SchuelerStatus.EXTERN || s.Status == SchuelerStatus.NEUAUFNAHME)).map(s -> s.ID).toList();

		// Daten auf Validität prüfen und DTO-Objekte in Maps ablegen
		if (schuelerIDs.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Schüler zum angegebenen Abiturjahrgang gefunden.");

		final Map<Long, DTOSchueler> mapSchueler = conn
			.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final Long schuelerID : schuelerIDs)
			if (mapSchueler.get(schuelerID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden ungültige Schüler-IDs übergeben.");

		final Map<Long, DTOGostSchueler> mapGostSchueler = conn
			.queryNamed("DTOGostSchueler.schueler_id.multiple", schuelerIDs, DTOGostSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));
		for (final Long schuelerID : schuelerIDs)
			if (mapGostSchueler.get(schuelerID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");

		// Datenquellen und zugehörige Objekte initialisieren
		DataSchildReportingDatenquelle.initMapDatenquellen();
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungGrunddaten objDsGD = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungGrunddaten();
		final List<DruckGostLaufbahnplanungSchuelerGrunddaten> listGrunddaten = objDsGD.getDaten(conn, schuelerIDs);
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen objDsSUM = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen();
		final List<DruckGostLaufbahnplanungSchuelerSummen> listSummen = objDsSUM.getDaten(conn, schuelerIDs);
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFehler objDsFEH = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFehler();
		final List<DruckGostLaufbahnplanungSchuelerFehler> listFehler = objDsFEH.getDaten(conn, schuelerIDs);
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungHinweise objDsHIN = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungHinweise();
		final List<DruckGostLaufbahnplanungSchuelerHinweise> listHinweise = objDsHIN.getDaten(conn, schuelerIDs);

		// Überführung der Ergebnisse der Datenquellen von Listen in Maps für besseren Zugriff auf die Daten.
		final Map<Long, DruckGostLaufbahnplanungSchuelerGrunddaten> mapGrunddaten = listGrunddaten.stream().collect(Collectors.toMap(d -> d.schuelerID, d -> d));
		final Map<Long, DruckGostLaufbahnplanungSchuelerSummen> mapSummen = listSummen.stream().collect(Collectors.toMap(d -> d.schuelerID, d -> d));
		final Map<Long, List<DruckGostLaufbahnplanungSchuelerFehler>> mapFehler = listFehler.stream().collect(Collectors.groupingBy(d -> d.schuelerID));
		final Map<Long, List<DruckGostLaufbahnplanungSchuelerHinweise>> mapHinweise = listHinweise.stream().collect(Collectors.groupingBy(d -> d.schuelerID));

		// Die Schüler-IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe mehrerer Wahlbögen sollten sie aber in alphabetischer Reihenfolge der Schüler sein.
		// Erzeuge daher eine Liste mit Schüler-IDs, die in der alphabetischen Reihenfolge der Schüler sortiert ist
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);

		final List<Long> sortedSchuelerIDs = mapSchueler.values().stream()
				.sorted(Comparator.comparing((final DTOSchueler s) -> s.Nachname, colGerman)
						.thenComparing((final DTOSchueler s) -> s.Vorname, colGerman)
						.thenComparing((final DTOSchueler s) -> s.ID))
				.map(s -> s.ID)
				.toList();

		// Dateinamen erzeugen und übergeben. Dabei gilt:
		// Wird nur ein Schüler übergeben, so wird eine persönliche PDF-Datei erstellt. Werden mehrere Schüler übergeben, so wurde über die API ein ganzer Jahrgang angefordert, für den eine PDF-Gesamtdatei erzeugt wird.
		String dateiname;
		dateiname = "Laufbahnprüfungsergebnisse_Abitur-%d_%s.pdf".formatted(
			mapGrunddaten.get(schuelerIDs.get(0)).abiturjahr,
			mapGrunddaten.get(schuelerIDs.get(0)).beratungsGOStHalbjahr.replace('.', '_'));

		// Erstelle die PDF-Datei mit der Liste für den Abiturjahrgang.
		return new PDFGostErgebnisseLaufbahnpruefung(dateiname, detaillevel, schulnummer, schulbezeichnung, abiturjahr, sortedSchuelerIDs, mapSchueler, mapGrunddaten, mapSummen, mapFehler, mapHinweise);
	}


	/**
	 * Erstellt ein PDF-Dokument mit den Ergebnissen der Laufbahnprüfung
	 * eines Abiturjahrgangs der gymnasialen Oberstufe.
	 *
	 * @param conn         	die Datenbank-Verbindung
	 * @param abiturjahr   	das Abiturjahr
	 * @param detaillevel 	gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 *
	 * @return die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response queryJahrgang(final DBEntityManager conn, final int abiturjahr, final int detaillevel) {

		try {
			final PDFGostErgebnisseLaufbahnpruefung pdf = getPDFmitErgebnisseLaufbahnpruefung(conn, abiturjahr, Math.min(Math.max(detaillevel, 0), 2));

			final byte[] data = pdf.toByteArray();
			if (data == null)
				return OperationError.INTERNAL_SERVER_ERROR.getResponse("Fehler bei der Generierung der PDF-Datei.");

			final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(pdf.filename, StandardCharsets.UTF_8);

			return Response.ok(data, "application/pdf")
						   .header("Content-Disposition", "attachment; " + encodedFilename)
						   .build();

		} catch (WebApplicationException ex) {
			return ex.getResponse();
		}
	}
}
