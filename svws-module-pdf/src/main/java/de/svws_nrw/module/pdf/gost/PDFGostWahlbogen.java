package de.svws_nrw.module.pdf.gost;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerGOStLaufbahnplanungFachwahlen;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerGOStLaufbahnplanungFehler;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerGOStLaufbahnplanungGrunddaten;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerGOStLaufbahnplanungHinweise;
import de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerGOStLaufbahnplanungSummen;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.schild3.reporting.DataSchildReportingDatenquelle;
import de.svws_nrw.data.schild3.reporting.DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFachwahlen;
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
public final class PDFGostWahlbogen extends PDFCreator {

	/** Der HTML-Code des body für die HTML-Vorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String html = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostWahlbogen.html");

	/** Der Dateiname für die PDF-Datei */
	private final String filename;


	/**
	 * Erstellt die PDF-Wahlbögen auf Basis der HTML-Vorlage
	 *
	 * @param dateiname				Dateiname der finalen PDF-Datei. Ist dieser leer, so wird aus den übergebenen Daten des Schülers ein eindeutiger Dateiname erzeugt.
	 * @param schulbezeichnung 		Array mit den drei Einträgen der Schulbezeichnung
	 * @param schuelerIDs			Sortierte Liste der Schüler-IDs, deren Wahlbögen erstellt werden sollen.
	 * @param mapSchueler			Sortierte Liste der Schüler-IDs, deren Wahlbögen erstellt werden sollen.
	 * @param mapGrunddaten		Map mit den Grunddaten der Laufbahn der Schüler
	 * @param mapFachwahlen			Map mit den Fachwahlen der Laufbahn der Schüler
	 * @param mapSummen				Map mit den Summen zur Laufbahn der Schüler
	 * @param mapFehler				Map mit den Fehlern in der Laufbahn der Schüler
	 * @param mapHinweise			Map mit den Hinweisen zur Laufbahn der Schüler
	 */
	private PDFGostWahlbogen(final String dateiname,
							 final String[] schulbezeichnung,
							 final List<Long> schuelerIDs,
							 final Map<Long, DTOSchueler> mapSchueler,
							 final Map<Long, SchildReportingSchuelerGOStLaufbahnplanungGrunddaten> mapGrunddaten,
							 final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen>> mapFachwahlen,
							 final Map<Long, SchildReportingSchuelerGOStLaufbahnplanungSummen> mapSummen,
							 final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungFehler>> mapFehler,
							 final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungHinweise>> mapHinweise) {
		super(html);

		this.filename = dateiname;

		// Ersetze die Felder des Templates mit Grunddaten
		htmlData.put("SCHULBEZEICHNUNG_1", schulbezeichnung[0] == null ? "" : schulbezeichnung[0]);
		htmlData.put("SCHULBEZEICHNUNG_2", schulbezeichnung[1] == null ? "" : schulbezeichnung[1]);
		htmlData.put("SCHULBEZEICHNUNG_3", schulbezeichnung[2] == null ? "" : schulbezeichnung[2]);
		htmlData.put("ABITURJAHR", String.valueOf(mapGrunddaten.get(schuelerIDs.get(0)).abiturjahr));
		htmlData.put("AKTUELLESHALBJAHR", mapGrunddaten.get(schuelerIDs.get(0)).aktuellesGOStHalbjahr);
		htmlData.put("BERATUNGSHALBJAHR",  mapGrunddaten.get(schuelerIDs.get(0)).beratungsGOStHalbjahr);
		htmlData.put("PRUEFUNGSORDNUNG", mapGrunddaten.get(schuelerIDs.get(0)).pruefungsordnung);
		htmlData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		htmlData.put("WAHLBOEGENMITDATEN", getWahlboegenMitDaten(schuelerIDs, mapSchueler, mapGrunddaten, mapFachwahlen, mapSummen, mapFehler, mapHinweise));
	}

	/**
	 * Erstellt für jeden Schüler den Wahlbogen.
	 *
	 * @param schuelerIDs			Sortierte Liste der Schüler-IDs, deren Wahlbögen erstellt werden sollen.
	 * @param mapSchueler			Sortierte Liste der Schüler-IDs, deren Wahlbögen erstellt werden sollen.
	 * @param mapGrunddaten			Map mit den Grunddaten der Laufbahn der Schüler
	 * @param mapFachwahlen			Map mit den Fachwahlen der Laufbahn der Schüler
	 * @param mapSummen				Map mit den Summen zur Laufbahn der Schüler
	 * @param mapFehler				Map mit den Fehlern in der Laufbahn der Schüler
	 * @param mapHinweise			Map mit den Hinweisen zur Laufbahn der Schüler
	 *
	 * @return 						Gibt die Wahlboegen aller Schüler mit deren Daten zurück
	 */
	private String getWahlboegenMitDaten(final List<Long> schuelerIDs,
										 final Map<Long, DTOSchueler> mapSchueler,
										 final Map<Long, SchildReportingSchuelerGOStLaufbahnplanungGrunddaten> mapGrunddaten,
										 final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen>> mapFachwahlen,
										 final Map<Long, SchildReportingSchuelerGOStLaufbahnplanungSummen> mapSummen,
										 final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungFehler>> mapFehler,
										 final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungHinweise>> mapHinweise) {

		final StringBuilder wahlboegen = new StringBuilder();
		final int maxSchueler = schuelerIDs.size();
		int lfdNr = 0;

		for (final Long schuelerID : schuelerIDs) {

			// Objekte mit Informationen zum aktuellen Schüler und dessen Laufbahn erstellen
            final DTOSchueler schueler = mapSchueler.get(schuelerID);
			final SchildReportingSchuelerGOStLaufbahnplanungGrunddaten grunddaten = mapGrunddaten.get(schuelerID);
			final List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen> fachwahlen = mapFachwahlen.get(schuelerID);
			final SchildReportingSchuelerGOStLaufbahnplanungSummen summen = mapSummen.get(schuelerID);
			final List<SchildReportingSchuelerGOStLaufbahnplanungFehler> fehler = mapFehler.get(schuelerID);
			final List<SchildReportingSchuelerGOStLaufbahnplanungHinweise> hinweise = mapHinweise.get(schuelerID);

			lfdNr++;

			final String betratungsbogenText = (grunddaten.beratungsbogentext != null && !grunddaten.beratungsbogentext.isBlank() && !grunddaten.beratungsbogentext.isEmpty()) ? "<p>" + grunddaten.beratungsbogentext + "</p>" : "";
			final String unterschriftBeratungslehrkraefte = (grunddaten.beratungslehrkraefte != null && !grunddaten.beratungslehrkraefte.isBlank() && !grunddaten.beratungslehrkraefte.isEmpty()) ? grunddaten.beratungslehrkraefte : "Beratungslehrkraft";

			final String wahlbogen =
            """
                <table width="100%%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td style="width: 50%%; text-align: left; font-size: 7pt; vertical-align: top;"></td>
                        <td style="width: 50%%; text-align: right; font-size: 7pt; color: gray; vertical-align: top;">Klasse: %s</td>
                    </tr>
                    <tr>
                        <td style="width: 20%%; border-bottom: 1px gray solid; text-align: left; vertical-align: middle;"><h3>Wahlbogen</h3></td>
                        <td style="width: 80%%; border-bottom: 1px gray solid; text-align: right; vertical-align: middle"><h3>%s, %s</h3></td>
                    </tr>
                </table>
                %s
                <table class="faecher" width="100%%" cellspacing="0" cellpadding="0.5px" style="margin-top: 10px; margin-bottom: 20px; border-collapse: collapse; page-break-inside: avoid;">
                    <thead>
                        <tr style="height: 0em" cellspacing="0" cellpadding="0">
                            <td style="width: 8%%; border-width: 0em;"></td>
                            <td style="width: 35%%; border-width: 0em;"></td>
                            <td style="width: 15%%; border-width: 0em;"></td>
                            <td style="width: 6%%; border-width: 0em;"></td>
                            <td style="width: 6%%; border-width: 0em;"></td>
                            <td style="width: 6%%; border-width: 0em;"></td>
                            <td style="width: 6%%; border-width: 0em;"></td>
                            <td style="width: 6%%; border-width: 0em;"></td>
                            <td style="width: 6%%; border-width: 0em;"></td>
                            <td style="width: 6%%; border-width: 0em;"></td>
                        </tr>
                        <tr>
                            <th colspan="2" style="border-bottom: 1px black solid;">Fach</th>
                            <th style="border-bottom: 1px black solid; border-left: 1px gray solid;">Fremdsprache</th>
                            <th style="border-bottom: 1px black solid; border-left: 1px gray solid;">EF.1</th>
                            <th style="border-bottom: 1px black solid;">EF.2</th>
                            <th style="border-bottom: 1px black solid; border-left: 1px gray solid;">Q1.1</th>
                            <th style="border-bottom: 1px black solid;">Q1.2</th>
                            <th style="border-bottom: 1px black solid;">Q2.1</th>
                            <th style="border-bottom: 1px black solid;">Q2.2</th>
                            <th style="border-bottom: 1px black solid; border-left: 1px gray solid; border-right: 1px gray solid;">Abitur</th>
                        </tr>
                    </thead>
                    <tbody>
                        %s
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3" style="padding-right: 6px; border-top: 1px black solid; text-align: right;"><b>Anzahl anrechenbare Kurse:</b></td>
                            <td style="border-top: 1px black solid; border-left: 1px gray solid; border-bottom: 0.5px gray solid">%d</td>
                            <td style="border-top: 1px black solid; border-left: 0.5px gray solid; border-bottom: 0.5px gray solid">%d</td>
                            <td style="background-color: rgb(211, 211, 211); border-top: 1px black solid; border-left: 1px gray solid; border-bottom: 0.5px gray solid">%d</td>
                            <td style="background-color: rgb(211, 211, 211); border-top: 1px black solid; border-left: 0.5px gray solid; border-bottom: 0.5px gray solid">%d</td>
                            <td style="background-color: rgb(211, 211, 211); border-top: 1px black solid; border-left: 0.5px gray solid; border-bottom: 0.5px gray solid">%d</td>
                            <td style="background-color: rgb(211, 211, 211); border-top: 1px black solid; border-left: 0.5px gray solid; border-bottom: 0.5px gray solid">%d</td>
                            <td style="background-color: rgb(211, 211, 211); border-top: 1px black solid; border-left: 1px gray solid; border-right: 0.5px gray solid; border-bottom: 0.5px gray solid"><b>%d</b></td>
                        </tr>
                        <tr>
                            <td colspan="3" style="padding-right: 6px; text-align: right;"><b>Wochenstundensumme:</b></td>
                            <td style="border-left: 1px gray solid;">%d</td>
                            <td style="border: 0.5px gray solid;">%d</td>
                            <td style="border-left: 1px gray solid;">%d</td>
                            <td style="border: 0.5px gray solid;">%d</td>
                            <td style="border: 0.5px gray solid;">%d</td>
                            <td style="border: 0.5px gray solid;">%d</td>
                            <td rowspan="2" style="vertical-align: middle; border: 0.5px gray solid; border-left: 1px gray solid;"><b>%.1f</b></td>
                        </tr>
                        <tr>
                            <td colspan="3" class="tiny" style="padding-right: 6px; text-align: right;">(pro Schuljahr)</td>
                            <td colspan="2" class="tiny" style="border: 0.5px gray solid; border-left: 1px gray solid;">%.1f</td>
                            <td colspan="2" class="tiny" style="border: 0.5px gray solid; border-left: 1px gray solid;">%.1f</td>
                            <td colspan="2" class="tiny" style="border: 0.5px gray solid;">%.1f</td>
                        </tr>
                        <tr>
                            <td colspan="3" style="padding-right: 6px; text-align: right;"><b>Wochenstundendurchschnitt:</b></td>
                            <td colspan="2" style="border: 0.5px gray solid; border-left: 1px gray solid;"><b>%.1f</b></td>
                            <td colspan="4" style="border: 0.5px gray solid; border-left: 1px gray solid;"><b>%.1f</b></td>
                            <td style="border-left: 1px gray solid;"></td>
                        </tr>
                    </tfoot>
                </table>
                <p><u>Bemerkungen der Schule zur Gesamtlaufbahn:</u></p>
                %s
                <p><u>Sonstige Hinweise zur Gesamtlaufbahn:</u></p>
                %s
                <p>Die oben aufgeführten Fachwahlen gelten für die weitere Schullaufbahn als verbindlich gewählt, solange keine Korrekturwünsche den Beratungslehrkräften mitgeteilt werden.
                Korrekturwünsche können vor jedem Halbjahreswechsel nach Rücksprache mit den Beratungslehrkräften durchgeführt werden.
                Schulorganisatorische Gründe können zu einer Änderung der Fachwahlen und der Laufbahn führen.</p>
                %s
                <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="page-break-inside: avoid;%s">
                    <tr style="height: 3em;">
                        <td style="width: 30%%;"> </td>
                        <td style="width: 5%%;"> </td>
                        <td style="width: 30%%;"> </td>
                        <td style="width: 5%%;"> </td>
                        <td style="width: 30%%;"> </td>
                    </tr>
                    <tr>
                        <td class="tiny" style="text-align: center; border: 0.5px solid black; border-style: solid none none none">%s</td>
                        <td> </td>
                        <td class="tiny" style="text-align: center; border: 0.5px solid black; border-style: solid none none none">%s %s</td>
                        <td> </td>
                        <td class="tiny" style="text-align: center; border: 0.5px solid black; border-style: solid none none none">Erziehungsberechtigte</td>
                    </tr>
                </table>
                """.formatted(
					grunddaten.aktuelleKlasse,
					schueler.Nachname,
					schueler.Vorname,
					betratungsbogenText,
					getFachwahlzeilen(fachwahlen),
					summen.kursanzahlEF1,
					summen.kursanzahlEF2,
					summen.kursanzahlQ11,
					summen.kursanzahlQ12,
					summen.kursanzahlQ21,
					summen.kursanzahlQ22,
					summen.kursanzahlQPh,
					summen.wochenstundenEF1,
					summen.wochenstundenEF2,
					summen.wochenstundenQ11,
					summen.wochenstundenQ12,
					summen.wochenstundenQ21,
					summen.wochenstundenQ22,
					summen.wochenstundenGesamt,
					summen.wochenstundenDurchschnittEF,
					summen.wochenstundenDurchschnittQ1,
					summen.wochenstundenDurchschnittQ2,
					summen.wochenstundenDurchschnittEF,
					summen.wochenstundenDurchschnittQPh,
					getBelegungFehlerKommentar(fehler, grunddaten),
					getBelegungHinweise(hinweise),
					getLetzteBeratung(grunddaten),
					(lfdNr < maxSchueler) ? " page-break-after: always;" : "",  // <<< hier wird für alle Schüler, bis auf den letzten, ein Seitenumbruch eingefügt.
					unterschriftBeratungslehrkraefte,
					schueler.Vorname,
					schueler.Nachname
					);

			wahlboegen.append(wahlbogen);
		}

		return wahlboegen.toString();
	}


	/**
	 *
	 *
	 * @param listFachwahlen	Liste alle Fachwahlen, die im Wahlbogen erscheinen sollen.
	 *
	 * @return					Tabellenzeile in HTML für die Fachwahlen.
	 */
	private static String getFachwahlzeilen(final List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen> listFachwahlen) {

		final StringBuilder sbFachwahlen = new StringBuilder();
		String sprachbelegung;

		for (final SchildReportingSchuelerGOStLaufbahnplanungFachwahlen fachwahl : listFachwahlen) {
			sprachbelegung = "";
			if (Boolean.TRUE.equals(fachwahl.fachIstFortfuehrbareFremdspracheInGOSt)) {
				if (fachwahl.jahrgangFremdsprachenbeginn.equals("HSU"))
					sprachbelegung = "HSU-Prüfung";
				else if (fachwahl.jahrgangFremdsprachenbeginn.equals("SFP"))
					sprachbelegung = "Festst.prüf.";
				else
					sprachbelegung = fachwahl.positionFremdsprachenfolge + " (ab Jg. " + fachwahl.jahrgangFremdsprachenbeginn + ")";
			}
			final String zeileFachwahl =
				"""
                <tr style="background-color: rgb(%s);">
                    <th>%s</th>
                    <th>%s</th>
                    <td style="border-left: 1px gray solid;">%s</td>
                    <td style="border-left: 1px gray solid;">%s</td>
                    <td>%s</td>
                    <td style="border-left: 1px gray solid;">%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td style="border-left: 1px gray solid; border-right: 1px gray solid;">%s</td>
                </tr>
                """.formatted(
					fachwahl.farbeClientRGB,
					fachwahl.kuerzel,
					fachwahl.bezeichnung,
					sprachbelegung,
					fachwahl.belegungEF1,
					fachwahl.belegungEF2,
					fachwahl.belegungQ11,
					fachwahl.belegungQ12,
					fachwahl.belegungQ21,
					fachwahl.belegungQ22,
					fachwahl.abiturfach
				);
			sbFachwahlen.append(zeileFachwahl);
		}

		return sbFachwahlen.toString();
	}


	/**
	 *
	 * @param grunddaten	Die Grunddaten der Laufbahn des Schülers
	 *
	 * @return 				Ein HTML-Absatz <p> mit den Informationen zur letzten Beratung.
	 */
	private static String getLetzteBeratung(final SchildReportingSchuelerGOStLaufbahnplanungGrunddaten grunddaten) {
		// Ausgabe der Informationen (Zeit und Person) der letzten Beratung zusammenstellen, je nachdem, welche Informationen hinterlegt sind.
		String letzteBeratung = "<p>Die letzte Beratung wurde durchgeführt";
		boolean hatLetzteBeratung = false;
		if (grunddaten.beratungsdatum != null && !grunddaten.beratungsdatum.isBlank() && !grunddaten.beratungsdatum.isEmpty()) {
			final LocalDate beratungsdatum = LocalDate.parse(grunddaten.beratungsdatum, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			letzteBeratung = letzteBeratung + " am " + beratungsdatum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			hatLetzteBeratung = true;
		}
		if (grunddaten.beratungslehrkraft != null && !grunddaten.beratungslehrkraft.isBlank() && !grunddaten.beratungslehrkraft.isEmpty()) {
			letzteBeratung = letzteBeratung + " von " + grunddaten.beratungslehrkraft;
			hatLetzteBeratung = true;
		}
		letzteBeratung = Boolean.TRUE.equals(hatLetzteBeratung) ? letzteBeratung + ".</p>" : "";
		return letzteBeratung;
	}

	/**
	 *
	 * @param fehler		Die Fehler der Laufbahn des Schülers
	 * @param grunddaten	Die Grunddaten der Laufbahn des Schülers
	 *
	 * @return				Ein HTML-Absatz p oder eine Liste ul mit Laufbahnfehlern und dem Kommentar.
	 */
	private static String getBelegungFehlerKommentar(final List<SchildReportingSchuelerGOStLaufbahnplanungFehler> fehler, final SchildReportingSchuelerGOStLaufbahnplanungGrunddaten grunddaten) {
		String belegungFehlerKommentar;
		if (fehler == null || fehler.isEmpty()) {
			if (grunddaten.kommentar == null || grunddaten.kommentar.isEmpty()) {
				belegungFehlerKommentar = "<p><i>- keine -</i></p>";
			} else {
				final StringBuilder sb = new StringBuilder();
				final List<String> kommentar = new ArrayList<>();
				kommentar.add(grunddaten.kommentar);
				ul(sb, kommentar);
				belegungFehlerKommentar = sb.toString();
			}
		} else {
			final StringBuilder sb = new StringBuilder();
			final List<String> fehler_kommentar = new ArrayList<>(fehler.stream().map(f -> f.belegungsfehler).toList());
			if (grunddaten.kommentar != null && !grunddaten.kommentar.isEmpty()) {
				fehler_kommentar.add(grunddaten.kommentar);
			}
			ul(sb, fehler_kommentar);
			belegungFehlerKommentar = sb.toString();
		}
		return belegungFehlerKommentar;
	}

	/**
	 *
	 * @param hinweise	Die Hinweise der Laufbahn des Schülers
	 *
	 * @return			Ein HTML-Absatz p oder eine Liste ul mit Laufbahnhinweisen.
	 */
	private static String getBelegungHinweise(final List<SchildReportingSchuelerGOStLaufbahnplanungHinweise> hinweise) {
		String belegungHinweise;
		if (hinweise == null || hinweise.isEmpty())
			belegungHinweise = "<p><i>- keine -</i></p>";
		else {
			final StringBuilder sb = new StringBuilder();
			ul(sb, hinweise.stream().map(f -> f.belegungshinweis).toList());
			belegungHinweise =  sb.toString();
		}
		return belegungHinweise;
	}


	/**
	 * Erstellt ein neues Objekt des Typs PDFGostWahlbogen für den Wahlbogen zu der Laufbahn
	 * eines Schülers oder eines Abiturjahrgangs der gymnasialen Oberstufe.
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param schuelerID   	die ID eines Schülers, wenn ein einzelner Wahlbogen gedruckt werden soll. Sofern ein Abiturjahrgang angegeben wird, wird die schuelerID ignoriert.
	 * @param abiturjahr   	ein Abiturjahrgang, wenn alle dessen Wahlbögen gedruckt werden soll.
	 *
	 * @return 				das Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFGostWahlbogen getPDFmitWahlboegen(final DBEntityManager conn, final Long schuelerID, final Integer abiturjahr) throws WebApplicationException {

		// Schuldaten sammeln, pruefeSchuleMitGOSt wirft eine NOT_FOUND-Exception, wenn die Schule keine GOSt hat.
		DTOEigeneSchule schule;
		try {
			schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		} catch (WebApplicationException ex) {
			throw OperationError.NOT_FOUND.exception("Keine Schule oder Schule ohne GOSt gefunden.");
		}
		final String[] schulbezeichnung = new String[] {schule.Bezeichnung1, schule.Bezeichnung2, schule.Bezeichnung3};

		// Die Schüler-IDs für den Druck sammeln: Entweder die eine übergebene ID verwenden, oder die IDs des Abiturjahrgangs ermitteln.
		if (schuelerID == null && abiturjahr == null)
			throw OperationError.NOT_FOUND.exception("Keine Schüler-ID oder Abiturjahrgang angegeben.");

		final List<Long> schuelerIDs = new ArrayList<>();
		if (schuelerID != null && abiturjahr == null)
			// Übernehme die einzelne, übergebene Schüler-ID
			schuelerIDs.add(schuelerID);
		else {
			// Bestimme alle Schüler des Abiturjahrgangs
			final List<DTOViewGostSchuelerAbiturjahrgang> queryResult = (abiturjahr == -1)
				? conn.queryAll(DTOViewGostSchuelerAbiturjahrgang.class)
				: conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abiturjahr, DTOViewGostSchuelerAbiturjahrgang.class);

			if ((queryResult == null) || queryResult.isEmpty())
				throw OperationError.NOT_FOUND.exception("Keine Schüler zum angegebenen Abiturjahrgang gefunden.");

			schuelerIDs.addAll(queryResult.stream().filter(s -> (s.Status == SchuelerStatus.AKTIV || s.Status == SchuelerStatus.EXTERN || s.Status == SchuelerStatus.NEUAUFNAHME)).map(s -> s.ID).toList());
		}

		// Daten auf Validität prüfen und DTO-Objekte in Maps ablegen
		if (schuelerIDs.isEmpty())
			throw OperationError.NOT_FOUND.exception("Keine Schüler zum angegebenen Abiturjahrgang gefunden.");

		final Map<Long, DTOSchueler> mapSchueler = conn
			.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final Long sID : schuelerIDs)
			if (mapSchueler.get(sID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden ungültige Schüler-IDs übergeben.");

		final Map<Long, DTOGostSchueler> mapGostSchueler = conn
			.queryNamed("DTOGostSchueler.schueler_id.multiple", schuelerIDs, DTOGostSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));
		for (final Long sID : schuelerIDs)
			if (mapGostSchueler.get(sID) == null)
				throw OperationError.NOT_FOUND.exception("Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");

		// Datenquellen und zugehörige Objekte initialisieren
		DataSchildReportingDatenquelle.initMapDatenquellen();
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungGrunddaten objDsGD = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungGrunddaten();
		final List<SchildReportingSchuelerGOStLaufbahnplanungGrunddaten> listGrunddaten = objDsGD.getDaten(conn, schuelerIDs);
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFachwahlen objDsFW = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFachwahlen();
		final List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen> listFachwahlen = objDsFW.getDaten(conn, schuelerIDs);
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen objDsSUM = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen();
		final List<SchildReportingSchuelerGOStLaufbahnplanungSummen> listSummen = objDsSUM.getDaten(conn, schuelerIDs);
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFehler objDsFEH = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFehler();
		final List<SchildReportingSchuelerGOStLaufbahnplanungFehler> listFehler = objDsFEH.getDaten(conn, schuelerIDs);
		final DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungHinweise objDsHIN = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungHinweise();
		final List<SchildReportingSchuelerGOStLaufbahnplanungHinweise> listHinweise = objDsHIN.getDaten(conn, schuelerIDs);

		// Überführung der Ergebnisse der Datenquellen von Listen in Maps für besseren Zugriff auf die Daten.
		final Map<Long, SchildReportingSchuelerGOStLaufbahnplanungGrunddaten> mapGrunddaten = listGrunddaten.stream().collect(Collectors.toMap(d -> d.schuelerID, d -> d));
		final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen>> mapFachwahlen = listFachwahlen.stream().collect(Collectors.groupingBy(d -> d.schuelerID));
		final Map<Long, SchildReportingSchuelerGOStLaufbahnplanungSummen> mapSummen = listSummen.stream().collect(Collectors.toMap(d -> d.schuelerID, d -> d));
		final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungFehler>> mapFehler = listFehler.stream().collect(Collectors.groupingBy(d -> d.schuelerID));
		final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungHinweise>> mapHinweise = listHinweise.stream().collect(Collectors.groupingBy(d -> d.schuelerID));

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
		if (mapSchueler.size() == 1) {
			dateiname = "Laufbahnplanung_%d_%s_%s_%s_(%d).pdf".formatted(mapGrunddaten.get(sortedSchuelerIDs.get(0)).abiturjahr,
								   										 mapGrunddaten.get(sortedSchuelerIDs.get(0)).beratungsGOStHalbjahr.replace(".", ""),
																		 mapSchueler.get(sortedSchuelerIDs.get(0)).Nachname.replace(' ', '_').replace('.', '_'),
																		 mapSchueler.get(sortedSchuelerIDs.get(0)).Vorname.replace(' ', '_').replace('.', '_'),
																		 mapSchueler.get(sortedSchuelerIDs.get(0)).ID);
		} else {
			dateiname = "Laufbahnplanung_%d_%s.pdf".formatted(mapGrunddaten.get(sortedSchuelerIDs.get(0)).abiturjahr, mapGrunddaten.get(sortedSchuelerIDs.get(0)).beratungsGOStHalbjahr.replace('.', '_'));
		}

		// Erstelle die PDF-Datei, die entweder den Wahlbogen eines Schülers beinhaltet oder alle Wahlbögen eines Abiturjahrgangs.
		return new PDFGostWahlbogen(dateiname, schulbezeichnung, sortedSchuelerIDs, mapSchueler, mapGrunddaten, mapFachwahlen, mapSummen, mapFehler, mapHinweise);
	}


	/**
	 * Erstellt das PDF-Dokument für den Wahlbogen zu der Laufbahn
	 * eines Schülers der gymnasialen Oberstufe.
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param schueler_id   die ID des Schülers
	 *
	 * @return die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response query(final DBEntityManager conn, final Long schueler_id) {

		try {
			final PDFGostWahlbogen pdf = getPDFmitWahlboegen(conn, schueler_id, null);

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


	/**
	 * Erstellt das PDF-Dokument für den Wahlbogen zu den Schüler-Laufbahnen
	 * eines Abiturjahrgangs der gymnasialen Oberstufe.
	 *
	 * @param conn         die Datenbank-Verbindung
	 * @param abiturjahr   das Abiturjahr
	 *
	 * @return die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response queryJahrgang(final DBEntityManager conn, final int abiturjahr) {

		try {
			final PDFGostWahlbogen pdf = getPDFmitWahlboegen(conn, null, abiturjahr);

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
