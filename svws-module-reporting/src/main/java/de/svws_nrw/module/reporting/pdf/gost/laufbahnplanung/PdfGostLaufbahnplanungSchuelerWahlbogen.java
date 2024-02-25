package de.svws_nrw.module.reporting.pdf.gost.laufbahnplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.reporting.html.base.HtmlBuilder;
import de.svws_nrw.module.reporting.html.base.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.reporting.pdf.base.PdfBuilder;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 */
public final class PdfGostLaufbahnplanungSchuelerWahlbogen {

	/** Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlVorlageDateipfad = "de/svws_nrw/module/reporting/gost/laufbahnplanung/GostLaufbahnplanungWahlbogen.html";

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/reporting/gost/laufbahnplanung/GostLaufbahnplanungWahlbogen.css";

	private PdfGostLaufbahnplanungSchuelerWahlbogen() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Erstellt das PDF-Dokument und gibt es als Response zum Download zurück.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param idsSchueler           Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param nurBelegteFaecher 	auf dem Wahlbogen werden nur die vom Schüler belegten Fächer ausgegeben.
	 *
	 * @return 						HTTP-Response mit dem PDF-Dokument oder bei Fehler eine WebApplicationException-Response
	 */
	public static Response query(final DBEntityManager conn, final List<Long> idsSchueler, final Boolean nurBelegteFaecher) {
		final PdfBuilder pdfBuilder = getPdfBuilder(conn, idsSchueler, nurBelegteFaecher);
		return pdfBuilder.getPdfResponse();
	}


	/**
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten den PdfBuilder zur Erzeugung der PDF-Datei.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param idsSchueler           Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param nurBelegteFaecher 	Legt fest, ob auf dem Wahlbogen werden nur die vom Schüler belegten Fächer ausgegeben werden.
	 *
	 * @return						Ein PDF-Builder zur Erzeugung der PDF-Datei
	 */
	public static PdfBuilder getPdfBuilder(final DBEntityManager conn, final List<Long> idsSchueler, final boolean nurBelegteFaecher) {

		// html-Daten-Contexts erstellen und in Liste sammeln
		final ReportingRepository reportingRepository = new ReportingRepository(conn);
		final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository, idsSchueler, true, false);
		final HtmlContextSchule htmlContextSchule = new HtmlContextSchule(reportingRepository);

		final List<HtmlContext> htmlContexts = new ArrayList<>();
		htmlContexts.add(htmlContextSchueler);
		htmlContexts.add(htmlContextSchule);

		// Einzelne Variablen für den finalen html-Daten-Context sammeln
		final Map<String, Object> variables = new HashMap<>();
		variables.put("DruckparameterNurBelegteFaecher", nurBelegteFaecher);

		// Dateiname der PDF-Datei aus den Daten erzeugen.
		String pdfDateiname = "Laufbahnplanung.pdf";
		if (!htmlContextSchueler.getSchueler().isEmpty()) {
			final ProxyReportingSchueler ersteLaufbahnplanung = htmlContextSchueler.getSchueler().getFirst();
			if (htmlContextSchueler.getSchueler().size() == 1) {
				pdfDateiname = "Laufbahnplanung_%d_%s_%s_%s_(%d).pdf".formatted(
						ersteLaufbahnplanung.gostLaufbahnplanung().abiturjahr(),
						ersteLaufbahnplanung.gostLaufbahnplanung().folgeGOStHalbjahr().replace(".", ""),
						ersteLaufbahnplanung.nachname().replace(' ', '_').replace('.', '_'),
						ersteLaufbahnplanung.vorname().replace(' ', '_').replace('.', '_'),
						ersteLaufbahnplanung.id());
			} else {
				pdfDateiname = "Laufbahnplanung_%d_%s.pdf".formatted(ersteLaufbahnplanung.gostLaufbahnplanung().abiturjahr(), ersteLaufbahnplanung.gostLaufbahnplanung().folgeGOStHalbjahr().replace('.', '_'));
			}
		}

		// html-Builder erstellen und damit das html mit Daten für die PDF-Datei erzeugen
		final HtmlBuilder htmlBuilder = new HtmlBuilder(htmlVorlageDateipfad, htmlContexts, variables);
		final String html = htmlBuilder.getHtml();

		return new PdfBuilder(html, cssDateipfad, pdfDateiname);
	}
}
