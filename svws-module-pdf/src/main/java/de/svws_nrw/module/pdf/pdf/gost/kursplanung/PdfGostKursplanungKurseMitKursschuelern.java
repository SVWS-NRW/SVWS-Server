package de.svws_nrw.module.pdf.pdf.gost.kursplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.html.base.HtmlBuilder;
import de.svws_nrw.module.pdf.html.base.HtmlContext;
import de.svws_nrw.module.pdf.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.pdf.html.contexts.gost.kursplanung.HtmlContextGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.pdf.pdf.base.PdfBuilder;
import de.svws_nrw.module.pdf.proxytypes.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.pdf.repositories.ReportingRepository;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 */
public final class PdfGostKursplanungKurseMitKursschuelern {

	/** Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlVorlageDateipfad = "de/svws_nrw/module/pdf/gost/kursplanung/KursMitKursschuelern.html";

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/pdf/gost/kursplanung/KursMitKursschuelern.css";

	private PdfGostKursplanungKurseMitKursschuelern() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Erstellt das PDF-Dokument und gibt es als Response zum Download zurück.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem die Liste der Schüler mit ihren Kursen erstellt werden soll.
	 * @param kursIDs           	Liste der IDs der Kurse, deren Schüler aufgelistet werden sollen. Ist die Liste leer, werden alle
	 *                              Kurse der Blockung herangezogen.
	 *
	 * @return 						HTTP-Response mit der PDF-Datei oder bei Fehler eine WebApplicationException-Response
	 */
	public static Response query(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> kursIDs) {
		final PdfBuilder pdfBuilder = getPdfBuilder(conn, blockungsergebnisID, kursIDs);
		return pdfBuilder.getPdfResponse();
	}


	/**
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten den PdfBuilder zur Erzeugung der PDF-Datei.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param idBlockungsergebnis	ID des Blockungsergebnisses, aus dem die Liste der Schüler mit ihren Kursen erstellt werden soll.
	 * @param idsKurse           	Liste der IDs der Kurse, deren Schüler aufgelistet werden sollen. Ist die Liste leer, werden alle
	 *                              Kurse der Blockung herangezogen.
	 *
	 * @return						Ein PDF-Builder zur Erzeugung der PDF-Datei
	 */
	public static PdfBuilder getPdfBuilder(final DBEntityManager conn, final Long idBlockungsergebnis, final List<Long> idsKurse) {

		// html-Daten-Contexts erstellen und in Liste sammeln
		final ReportingRepository reportingRepository = new ReportingRepository(conn);
		final HtmlContextGostKursplanungBlockungsergebnis htmlContextBlockung = new HtmlContextGostKursplanungBlockungsergebnis(conn, idBlockungsergebnis, false, new ArrayList<>(), !(idsKurse == null || idsKurse.isEmpty()), idsKurse);
		final HtmlContext htmlContextSchule = new HtmlContextSchule(reportingRepository);

		final List<HtmlContext> htmlContexts = new ArrayList<>();
		htmlContexts.add(htmlContextBlockung);
		htmlContexts.add(htmlContextSchule);

		final ProxyReportingGostKursplanungBlockungsergebnis blockungsergebnis = (ProxyReportingGostKursplanungBlockungsergebnis) htmlContextBlockung.getContext().getVariable("Blockungsergebnis");

		// Dateiname der PDF-Datei aus den Daten erzeugen.
		final String pdfDateiname =
				"Kursliste_%d-%s_(Erg-ID%d).pdf".formatted(
						blockungsergebnis.abiturjahr(),
						blockungsergebnis.gostHalbjahr().replace(".", "-"),
						blockungsergebnis.id());

		// html-Builder erstellen und damit das html mit Daten für die PDF-Datei erzeugen
		final HtmlBuilder htmlBuilder = new HtmlBuilder(htmlVorlageDateipfad, htmlContexts, null);
		final String html = htmlBuilder.getHtml();

		return new PdfBuilder(html, cssDateipfad, pdfDateiname);
	}
}
