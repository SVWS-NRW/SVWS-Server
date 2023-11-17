package de.svws_nrw.module.pdf.dateien.gost.laufbahnplanung;

import de.svws_nrw.module.pdf.drucktypes.DruckGostLaufbahnplanungSchueler;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.HtmlBuilder;
import de.svws_nrw.module.pdf.HtmlContext;
import de.svws_nrw.module.pdf.PdfBuilder;
import de.svws_nrw.module.pdf.htmlcontexts.HtmlContextGostLaufbahnplanungSchueler;
import de.svws_nrw.module.pdf.htmlcontexts.HtmlContextSchule;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 */
public final class PdfDateiGostLaufbahnplanungSchuelerWahlbogen {

	/** Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlVorlageDateipfad = "de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerWahlbogen.html";

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerWahlbogen.css";

	private PdfDateiGostLaufbahnplanungSchuelerWahlbogen() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Erstellt das PDF-Dokument und gibt es als Response zum Download zurück.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param schuelerIDs           Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param nurBelegteFaecher 	auf dem Wahlbogen werden nur die vom Schüler belegten Fächer ausgegeben.
	 *
	 * @return 						HTTP-Response mit dem PDF-Dokument oder bei Fehler eine WebApplicationException-Response
	 */
	public static Response query(final DBEntityManager conn, final List<Long> schuelerIDs, final Boolean nurBelegteFaecher) {

		try {
			final PdfBuilder pdfBuilder = getPdfBuilder(conn, schuelerIDs, nurBelegteFaecher);
			return pdfBuilder.getPdfResponse();
		} catch (WebApplicationException ex) {
			return ex.getResponse();
		}
	}


	/**
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten den PdfBuilder zur Erzeugung der PDF-Datei.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param schuelerIDs           Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param nurBelegteFaecher 	Legt fest, ob auf dem Wahlbogen werden nur die vom Schüler belegten Fächer ausgegeben werden.
	 *
	 * @return						Ein PDF-Builder zur Erzeugung der PDF-Datei
	 */
	public static PdfBuilder getPdfBuilder(final DBEntityManager conn, final List<Long> schuelerIDs, final boolean nurBelegteFaecher) {

		// html-Daten-Contexts erstellen und in Liste sammeln
		final HtmlContextGostLaufbahnplanungSchueler htmlContextLaufbahnplanung = new HtmlContextGostLaufbahnplanungSchueler(conn, schuelerIDs);
		final HtmlContextSchule htmlContextSchule = new HtmlContextSchule(conn);

		final List<HtmlContext> htmlContexts = new ArrayList<>();
		htmlContexts.add(htmlContextLaufbahnplanung);
		htmlContexts.add(htmlContextSchule);

		// Einzelne Variablen für den finalen html-Daten-Context sammeln
		final Map<String, Object> variables = new HashMap<>();
		variables.put("DruckparameterNurBelegteFaecher", nurBelegteFaecher);

		// Dateiname der PDF-Datei aus den Daten erzeugen.
		String pdfDateiname = "Laufbahnplanung.pdf";
		if ((htmlContextLaufbahnplanung.getContext().getVariable("LaufbahnplanungSchueler") instanceof List<?> laufbahnplanungSchueler)
				&& !laufbahnplanungSchueler.isEmpty()
				&& laufbahnplanungSchueler.get(0) instanceof DruckGostLaufbahnplanungSchueler ersteLaufbahnplanung) {

			if (laufbahnplanungSchueler.size() == 1) {
				pdfDateiname = "Laufbahnplanung_%d_%s_%s_%s_(%d).pdf".formatted(
						ersteLaufbahnplanung.abiturjahr,
						ersteLaufbahnplanung.beratungsGOStHalbjahr.replace(".", ""),
						ersteLaufbahnplanung.nachname.replace(' ', '_').replace('.', '_'),
						ersteLaufbahnplanung.vorname.replace(' ', '_').replace('.', '_'),
						ersteLaufbahnplanung.id);
			} else {
				pdfDateiname = "Laufbahnplanung_%d_%s.pdf".formatted(ersteLaufbahnplanung.abiturjahr, ersteLaufbahnplanung.beratungsGOStHalbjahr.replace('.', '_'));
			}
		}

		// html-Builder stellen und damit das html mit Daten für die PDF-Datei erzeugen
		final HtmlBuilder htmlBuilder = new HtmlBuilder(htmlVorlageDateipfad, htmlContexts, variables);
		final String html = htmlBuilder.getHtml();

		return new PdfBuilder(html, cssDateipfad, pdfDateiname);
	}
}
