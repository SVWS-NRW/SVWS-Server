package de.svws_nrw.module.pdf.pdf.gost.laufbahnplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.html.base.HtmlBuilder;
import de.svws_nrw.module.pdf.html.base.HtmlContext;
import de.svws_nrw.module.pdf.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.pdf.html.contexts.gost.laufbahnplanung.HtmlContextGostLaufbahnplanungSchueler;
import de.svws_nrw.module.pdf.pdf.base.PdfBuilder;
import de.svws_nrw.module.pdf.reptypes.gost.laufbahnplanung.RepGostLaufbahnplanungSchueler;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 */
public final class PdfGostLaufbahnplanungSchuelerErgebnisuebersicht {

	/** Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlVorlageDateipfad = "de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerErgebnisuebersicht.html";

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerErgebnisuebersicht.css";

	private PdfGostLaufbahnplanungSchuelerErgebnisuebersicht() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Erstellt das PDF-Dokument und gibt es als Response zum Download zurück.
	 *
	 * @param conn          Datenbank-Verbindung
	 * @param schuelerIDs	Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param detaillevel 	Gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 *
	 * @return 				HTTP-Response mit der PDF-Datei oder bei Fehler eine WebApplicationException-Response
	 */
	public static Response query(final DBEntityManager conn, final List<Long> schuelerIDs, final int detaillevel) {
		final PdfBuilder pdfBuilder = getPdfBuilder(conn, schuelerIDs, detaillevel);
		return pdfBuilder.getPdfResponse();
	}


	/**
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten den PdfBuilder zur Erzeugung der PDF-Datei.
	 *
	 * @param conn          Datenbank-Verbindung
	 * @param schuelerIDs	Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param detaillevel 	Gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 *
	 * @return				Ein PDF-Builder zur Erzeugung der PDF-Datei
	 */
	public static PdfBuilder getPdfBuilder(final DBEntityManager conn, final List<Long> schuelerIDs, final int detaillevel) {

		// html-Daten-Contexts erstellen und in Liste sammeln
		final HtmlContextGostLaufbahnplanungSchueler htmlContextLaufbahnplanung = new HtmlContextGostLaufbahnplanungSchueler(conn, schuelerIDs);
		final HtmlContextSchule htmlContextSchule = new HtmlContextSchule(conn);

		final List<HtmlContext> htmlContexts = new ArrayList<>();
		htmlContexts.add(htmlContextLaufbahnplanung);
		htmlContexts.add(htmlContextSchule);

		// Einzelne Variablen für den finalen html-Daten-Context sammeln
		final Map<String, Object> variables = new HashMap<>();
		variables.put("DruckparameterDetaillevel", detaillevel);

		// Dateiname der PDF-Datei aus den Daten erzeugen.
		String pdfDateiname = "Laufbahnplanung_Prüfungsergebnisse.pdf";
		if (!htmlContextLaufbahnplanung.getGostLaufbahnplanungenSchueler().isEmpty()) {
			final RepGostLaufbahnplanungSchueler ersteLaufbahnplanung = htmlContextLaufbahnplanung.getGostLaufbahnplanungenSchueler().get(0);
			pdfDateiname = "Laufbahnplanung_Prüfungsergebnisse_Abitur-%d_%s.pdf".formatted(
				ersteLaufbahnplanung.abiturjahr,
				ersteLaufbahnplanung.beratungsGOStHalbjahr.replace('.', '_'));
		}

		// html-Builder erstellen und damit das html mit Daten für die PDF-Datei erzeugen
		final HtmlBuilder htmlBuilder = new HtmlBuilder(htmlVorlageDateipfad, htmlContexts, variables);
		final String html = htmlBuilder.getHtml();

		return new PdfBuilder(html, cssDateipfad, pdfDateiname);
	}
}
