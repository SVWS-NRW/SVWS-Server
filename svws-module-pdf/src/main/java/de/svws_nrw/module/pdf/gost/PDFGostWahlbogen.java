package de.svws_nrw.module.pdf.gost;

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
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse beinhaltet den Code zur Erstellung eines Wahlbogens
 * für die Laufbahnplanung der gymnasialen Oberstufe.
 */
public final class PDFGostWahlbogen extends PDFCreator {

	private static final String html = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostWahlbogen.html.txt");
	private static final String css = ResourceUtils.text("de/svws_nrw/module/pdf/gost/PDFGostWahlbogen.css.txt");

	/** Der Dateiname für die PDF-Datei */
	private final String filename;


	/**
	 * Erstellt den PDF-Wahlbogen auf Basis der HTML-Vorlage
	 *
	 * @param dateiname				Dateiname der finalen PDF-Datei. Ist dieser leer, so wird aus den übergebenen Daten des Schülers ein eindeutiger Dateiname erzeugt.
	 * @param schulnummer			Schulnummer der Schule des Schülers, dessen Wahlbogen erstellt werden soll
	 * @param schulbezeichnung 		Array mit den drei Einträgen der Schulbezeichnung
	 * @param lfdNr					Laufende Nummer des Schülers bei der aktuellen Ausgabe
	 * @param schueler				Schülerobjekt des Schülers, dessen Wahlbogen erstellt werden soll.
	 * @param laufbahnGrunddaten	Grunddaten der Laufbahn des Schülers
	 * @param listFachwahlen		Fachwahlen der Laufbahn des Schülers
	 * @param laufbahnSummen		Summen zur Laufbahn des Schülers
	 * @param listFehler			Fehler in der Laufbahn des Schülers
	 * @param listHinweise			Hinweise zur Laufbahn des Schülers
	 */
	private PDFGostWahlbogen(final String dateiname,
							 final String schulnummer,
							 final String[] schulbezeichnung,
							 final int lfdNr,
							 final DTOSchueler schueler,
							 final SchildReportingSchuelerGOStLaufbahnplanungGrunddaten laufbahnGrunddaten,
							 final List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen> listFachwahlen,
							 final SchildReportingSchuelerGOStLaufbahnplanungSummen laufbahnSummen,
							 final List<SchildReportingSchuelerGOStLaufbahnplanungFehler> listFehler,
							 final List<SchildReportingSchuelerGOStLaufbahnplanungHinweise> listHinweise) {
		super("Wahlbogen", html, css);

		this.filename = dateiname;

		// Ersetze die Felder des Templates mit Grunddaten
		bodyData.put("PRUEFUNGSORDNUNG", laufbahnGrunddaten.pruefungsordnung);
		bodyData.put("SCHULBEZEICHNUNG_1", schulbezeichnung[0] == null ? "" : schulbezeichnung[0]);
		bodyData.put("SCHULBEZEICHNUNG_2", schulbezeichnung[1] == null ? "" : schulbezeichnung[1]);
		bodyData.put("SCHULBEZEICHNUNG_3", schulbezeichnung[2] == null ? "" : schulbezeichnung[2]);
		bodyData.put("SCHUELER_NAME", schueler.Nachname + ", " + schueler.Vorname);
		bodyData.put("UNTERSCHRIFT_SCHUELER", schueler.Vorname + " " + schueler.Nachname);
		bodyData.put("UNTERSCHRIFT_LEHRER", laufbahnGrunddaten.beratungslehrkraefte);
		bodyData.put("KLASSE", laufbahnGrunddaten.aktuelleKlasse);
		bodyData.put("ABITURJAHR", String.valueOf(laufbahnGrunddaten.abiturjahr));
		bodyData.put("AKTUELLESHALBJAHR", laufbahnGrunddaten.aktuellesGOStHalbjahr);
		bodyData.put("BERATUNGSHALBJAHR",  laufbahnGrunddaten.beratungsGOStHalbjahr);
		bodyData.put("BERATUNGSBOGENTEXT", laufbahnGrunddaten.beratungsbogentext == null ? "" : "<p>" + laufbahnGrunddaten.beratungsbogentext + "</p>");
		bodyData.put("BERATUNGSLEHRER", laufbahnGrunddaten.beratungslehrkraft == null ? "---" : laufbahnGrunddaten.beratungslehrkraft);
		if (laufbahnGrunddaten.beratungsdatum == null || laufbahnGrunddaten.beratungsdatum.isEmpty())
			bodyData.put("BERATUNGSDATUM", "");
		else {
			LocalDate beratungsdatum = LocalDate.parse(laufbahnGrunddaten.beratungsdatum, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			bodyData.put("BERATUNGSDATUM", laufbahnGrunddaten.beratungsdatum == null ? "---" : beratungsdatum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		}
		bodyData.put("ZEIT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		bodyData.put("SCHULNUMMER", schulnummer);
		bodyData.put("LFDNR", String.format("%03d", lfdNr));

		// Ersetze die Felder des Templates mit Fachwahlen
		bodyData.put("ROWS", getFachwahlzeilen(listFachwahlen));

		// Ersetze die Felder des Templates mit den Kurs- und Stundensummen.
		bodyData.put("KURSE_EF1", String.valueOf(laufbahnSummen.kursanzahlEF1));
		bodyData.put("KURSE_EF2", String.valueOf(laufbahnSummen.kursanzahlEF2));
		bodyData.put("KURSE_Q11", String.valueOf(laufbahnSummen.kursanzahlQ11));
		bodyData.put("KURSE_Q12", String.valueOf(laufbahnSummen.kursanzahlQ12));
		bodyData.put("KURSE_Q21", String.valueOf(laufbahnSummen.kursanzahlQ21));
		bodyData.put("KURSE_Q22", String.valueOf(laufbahnSummen.kursanzahlQ22));
		bodyData.put("KURSE_Q", String.valueOf(laufbahnSummen.kursanzahlQPh));
		bodyData.put("WSTD_EF1", String.valueOf(laufbahnSummen.wochenstundenEF1));
		bodyData.put("WSTD_EF2", String.valueOf(laufbahnSummen.wochenstundenEF2));
		bodyData.put("WSTD_Q11", String.valueOf(laufbahnSummen.wochenstundenQ11));
		bodyData.put("WSTD_Q12", String.valueOf(laufbahnSummen.wochenstundenQ12));
		bodyData.put("WSTD_Q21", String.valueOf(laufbahnSummen.wochenstundenQ21));
		bodyData.put("WSTD_Q22", String.valueOf(laufbahnSummen.wochenstundenQ22));
		bodyData.put("JSTD", String.valueOf(laufbahnSummen.wochenstundenGesamt));
		bodyData.put("WSTD_DURCHSCHNITT_EF", String.valueOf(laufbahnSummen.wochenstundenDurchschnittEF));
		bodyData.put("WSTD_DURCHSCHNITT_Q1", String.valueOf(laufbahnSummen.wochenstundenDurchschnittQ1));
		bodyData.put("WSTD_DURCHSCHNITT_Q2", String.valueOf(laufbahnSummen.wochenstundenDurchschnittQ2));
		bodyData.put("WSTD_DURCHSCHNITT_QPH", String.valueOf(laufbahnSummen.wochenstundenDurchschnittQPh));

		// Ersetze die Felder des Templates mit Belegungsfehlern
		 if (listFehler == null || listFehler.isEmpty()) {
			 bodyData.put("BELEGUNGSFEHLER", "");
			 if (laufbahnGrunddaten.kommentar == null || laufbahnGrunddaten.kommentar.isEmpty()) {
                 bodyData.put("BELEGUNGSFEHLER_KOMMENTAR", "- keine -");
             } else {
				 StringBuilder sb = new StringBuilder();
				 List<String> kommentar = new ArrayList<>();
				 kommentar.add(laufbahnGrunddaten.kommentar);
				 ul(sb, kommentar);
				 bodyData.put("BELEGUNGSFEHLER_KOMMENTAR", sb.toString());
			 }
		 } else {
			 StringBuilder sb = new StringBuilder();
             List<String> fehler_kommentar = new ArrayList<>(listFehler.stream().map(f -> f.belegungsfehler).toList());
			 if (laufbahnGrunddaten.kommentar != null && !laufbahnGrunddaten.kommentar.isEmpty()) {
				 fehler_kommentar.add(laufbahnGrunddaten.kommentar);
			 }
			 ul(sb, fehler_kommentar);
			 bodyData.put("BELEGUNGSFEHLER_KOMMENTAR", sb.toString());
		 }

		// Ersetze die Felder des Templates mit Hinweisen
		if (listHinweise == null || listHinweise.isEmpty())
			bodyData.put("BELEGUNGSHINWEISE", "- keine -");
		else {
			StringBuilder sb = new StringBuilder();
			List<String> hinweise = listHinweise.stream().map(f -> f.belegungshinweis).toList();
			ul(sb, hinweise);
			bodyData.put("BELEGUNGSHINWEISE", sb.toString());
		}
	}

	/**
	 *
	 * @param listFachwahlen	Liste alle Fachwahlen, die im Wahlbogen erscheinen sollen.
	 * @return					String in HTML für die PDF-Vorlage des Wahlbogens.
	 */
	private static String getFachwahlzeilen(final List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen> listFachwahlen) {

		final StringBuilder sbFachwahlen = new StringBuilder();

		for (SchildReportingSchuelerGOStLaufbahnplanungFachwahlen fachwahl : listFachwahlen) {
			sbFachwahlen.append("<tr style=\"background-color: rgb(").append(fachwahl.farbeClientRGB).append(");\">");

			sbFachwahlen.append("<th>").append(fachwahl.kuerzel).append("</th>");
			sbFachwahlen.append("<th>").append(fachwahl.bezeichnung).append("</th>");
			if (Boolean.TRUE.equals(fachwahl.fachIstFortfuehrbareFremdspracheInGOSt)) {
				if (fachwahl.jahrgangFremdsprachenbeginn.equals("HSU"))
					sbFachwahlen.append("<td style=\"border-left: 1px gray solid;\">HSU-Prüfung</td>");
				else if (fachwahl.jahrgangFremdsprachenbeginn.equals("SFP"))
					sbFachwahlen.append("<td style=\"border-left: 1px gray solid;\">Feststel.-Prf.</td>");
				else
					sbFachwahlen.append("<td style=\"border-left: 1px gray solid;\">").append(fachwahl.positionFremdsprachenfolge).append(" (ab Jg. ").append(fachwahl.jahrgangFremdsprachenbeginn).append(")</td>");
			} else {
				sbFachwahlen.append("<td style=\"border-left: 1px gray solid;\"></td>");
			}
			sbFachwahlen.append("<td style=\"border-left: 1px gray solid;\">").append(fachwahl.belegungEF1).append("</td>");
			sbFachwahlen.append("<td>").append(fachwahl.belegungEF2).append("</td>");
			sbFachwahlen.append("<td style=\"border-left: 1px gray solid;\">").append(fachwahl.belegungQ11).append("</td>");
			sbFachwahlen.append("<td>").append(fachwahl.belegungQ12).append("</td>");
			sbFachwahlen.append("<td>").append(fachwahl.belegungQ21).append("</td>");
			sbFachwahlen.append("<td>").append(fachwahl.belegungQ22).append("</td>");
			sbFachwahlen.append("<td style=\"border-left: 1px gray solid;\">").append(fachwahl.abiturfach).append("</td>");

			sbFachwahlen.append("</tr>");
		}

		return sbFachwahlen.toString();
	}


	/**
	 * Erstellt ein neues Objekt des Typs PDFGostWahlbogen für den Wahlbogen zu der Laufbahn
	 * eines Schülers der gymnasialen Oberstufe.
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param schuelerIDs   die IDs der Schüler, deren Wahlbögen in der PDF-Datei enthalten sein sollen.
	 *
	 * @return 				das Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFGostWahlbogen getPDFmitWahlboegen(final DBEntityManager conn, final List<Long> schuelerIDs) throws WebApplicationException {

		// Schuldaten sammeln
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		String schulnummer = schule.SchulNr.toString();
		String[] schulbezeichnung = new String[] {schule.Bezeichnung1, schule.Bezeichnung2, schule.Bezeichnung3};

		// Daten auf Validität prüfen und DTO-Objekte in Maps ablegen
		if (schuelerIDs == null || schuelerIDs.isEmpty())
			throw OperationError.NOT_FOUND.exception();

		final Map<Long, DTOSchueler> mapSchueler = conn
			.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		for (final Long schuelerID : schuelerIDs)
			if (mapSchueler.get(schuelerID) == null)
				throw OperationError.NOT_FOUND.exception();

		final Map<Long, DTOGostSchueler> mapGostSchueler = conn
			.queryNamed("DTOGostSchueler.schueler_id.multiple", schuelerIDs, DTOGostSchueler.class)
			.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));
		for (final Long schuelerID : schuelerIDs)
			if (mapGostSchueler.get(schuelerID) == null)
				throw OperationError.NOT_FOUND.exception();

		// Datenquellen und zugehörige Objekte initialisieren
		DataSchildReportingDatenquelle.initMapDatenquellen();
		DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungGrunddaten objDsGD = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungGrunddaten();
		List<SchildReportingSchuelerGOStLaufbahnplanungGrunddaten> listGrunddaten = objDsGD.getDaten(conn, schuelerIDs);
		DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFachwahlen objDsFW = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFachwahlen();
		List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen> listFachwahlen = objDsFW.getDaten(conn, schuelerIDs);
		DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen objDsSUM = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungSummen();
		List<SchildReportingSchuelerGOStLaufbahnplanungSummen> listSummen = objDsSUM.getDaten(conn, schuelerIDs);
		DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFehler objDsFEH = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungFehler();
		List<SchildReportingSchuelerGOStLaufbahnplanungFehler> listFehler = objDsFEH.getDaten(conn, schuelerIDs);
		DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungHinweise objDsHIN = new DataSchildReportingDatenquelleSchuelerGOStLaufbahnplanungHinweise();
		List<SchildReportingSchuelerGOStLaufbahnplanungHinweise> listHinweise = objDsHIN.getDaten(conn, schuelerIDs);

		// Überführung der Ergebnisse der Datenquellen von Listen in Maps für besseren Zugriff auf die Daten.
		final Map<Long, SchildReportingSchuelerGOStLaufbahnplanungGrunddaten> mapGrunddaten = listGrunddaten.stream().collect(Collectors.toMap(d -> d.schuelerID, d -> d));
		final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen>> mapFachwahlen = listFachwahlen.stream().collect(Collectors.groupingBy(d -> d.schuelerID));
		final Map<Long, SchildReportingSchuelerGOStLaufbahnplanungSummen> mapSummen = listSummen.stream().collect(Collectors.toMap(d -> d.schuelerID, d -> d));
		final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungFehler>> mapFehler = listFehler.stream().collect(Collectors.groupingBy(d -> d.schuelerID));
		final Map<Long, List<SchildReportingSchuelerGOStLaufbahnplanungHinweise>> mapHinweise = listHinweise.stream().collect(Collectors.groupingBy(d -> d.schuelerID));

		// Die Schüler-IDs können in einer beliebigen Reihenfolge sein. Für die Ausgabe mehrerer Wahlbögen sollten sie aber in alphabetischer Reihenfolge der Schüler sein.
		// Erzeuge daher eine Liste mit Schüler-IDs, die in der alphabetischen Reihenfolge der Schüler sortiert ist
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);

		List<Long> sortedSchuelerIDs = mapSchueler.values().stream()
															.sorted(Comparator
																.comparing((DTOSchueler s) -> s.Nachname, colGerman)
																.thenComparing((DTOSchueler s) -> s.Vorname, colGerman)
																.thenComparing((DTOSchueler s) -> s.ID))
															.map(s -> s.ID)
															.toList();

		// Dateinamen erzeugen und übergeben. Dabei gilt:
		// Wird nur ein Schüler übergeben, so wird eine persönliche PDF-Datei erstellt. Werden mehrere Schüler übergeben, so wurde über die API ein ganzer Jahrgang angefordert, für den eine PDF-Gesamtdatei erzeugt wird.
		String dateiname = "";
		if (mapSchueler.size() == 1) {
			dateiname = "Laufbahnplanung_%d_%s_%s_%s_(%d).pdf".formatted(
				mapGrunddaten.get(schuelerIDs.get(0)).abiturjahr,
				mapGrunddaten.get(schuelerIDs.get(0)).aktuellesGOStHalbjahr.replace(".", ""),
				mapSchueler.get(schuelerIDs.get(0)).Nachname.replace(' ', '_').replace('.', '_'),
				mapSchueler.get(schuelerIDs.get(0)).Vorname.replace(' ', '_').replace('.', '_'),
				mapSchueler.get(schuelerIDs.get(0)).ID);
		} else {
			dateiname = "Laufbahnplanung_%d_%s.pdf".formatted(
				mapGrunddaten.get(schuelerIDs.get(0)).abiturjahr,
				mapGrunddaten.get(schuelerIDs.get(0)).aktuellesGOStHalbjahr.replace('.', '_'));
		}

		// Erstelle die PDF-Datei, die entweder den Wahlbogen eines Schülers beinhaltet oder alle Wahlbögen eines Abiturjahrgangs.
		int lfdNr = 0;
		PDFGostWahlbogen pdf = null;
		PDFGostWahlbogen pdfAktuelleSeite = null;
		for (final Long schuelerID : sortedSchuelerIDs) {
			lfdNr++;
			final PDFGostWahlbogen pdfNeueSeite = new PDFGostWahlbogen(dateiname,
																		schulnummer, schulbezeichnung,
																		lfdNr,
																		mapSchueler.get(schuelerID),
																		mapGrunddaten.get(schuelerID),
																		mapFachwahlen.get(schuelerID),
																		mapSummen.get(schuelerID),
																		mapFehler.get(schuelerID),
																		mapHinweise.get(schuelerID));
			if (pdfAktuelleSeite == null) {
				pdf = pdfNeueSeite;
			} else {
				pdfAktuelleSeite.setNext(pdfNeueSeite);
			}
			pdfAktuelleSeite = pdfNeueSeite;
		}

		return pdf;
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
		if (schueler_id == null)
			throw OperationError.NOT_FOUND.exception();

		List<Long> schuelerIDs = new ArrayList<>();
		schuelerIDs.add(schueler_id);

		final PDFGostWahlbogen pdf = getPDFmitWahlboegen(conn, schuelerIDs);

		if (pdf == null)
			return OperationError.NOT_FOUND.getResponse();

		final byte[] data = pdf.toByteArray();
		if (data == null)
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();

		Response queryResponse;

		queryResponse = Response.status(Status.OK)
								.type("application/pdf")
								.header("Content-Disposition", "attachment; filename=\"" + pdf.filename + "\"")
								.entity(data)
								.build();

		return queryResponse;
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
		// Bestimme alle Schüler des Abiturjahrgangs
		final List<DTOViewGostSchuelerAbiturjahrgang> queryResult = (abiturjahr == -1)
				? conn.queryAll(DTOViewGostSchuelerAbiturjahrgang.class)
				: conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abiturjahr, DTOViewGostSchuelerAbiturjahrgang.class);

		final List<Long> schuelerIDs = queryResult.stream().filter(s -> (s.Status == SchuelerStatus.AKTIV || s.Status == SchuelerStatus.EXTERN || s.Status == SchuelerStatus.NEUAUFNAHME)).map(s -> s.ID).toList();

		final PDFGostWahlbogen pdf = getPDFmitWahlboegen(conn, schuelerIDs);

		if (pdf == null)
			return OperationError.NOT_FOUND.getResponse();

		final byte[] data = pdf.toByteArray();
		if (data == null)
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();

		Response queryResponse;

		queryResponse = Response.status(Status.OK)
			.type("application/pdf")
			.header("Content-Disposition", "attachment; filename=\"" + pdf.filename + "\"")
			.entity(data)
			.build();

		return queryResponse;
	}

}
