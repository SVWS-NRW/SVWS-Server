package de.svws_nrw.module.pdf.pdf.gost.kursplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.module.pdf.html.base.HtmlBuilder;
import de.svws_nrw.module.pdf.html.base.HtmlContext;
import de.svws_nrw.module.pdf.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.pdf.html.contexts.gost.kursplanung.HtmlContextGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.pdf.html.contexts.gost.kursplanung.HtmlContextGostKursplanungSchienen;
import de.svws_nrw.module.pdf.html.contexts.gost.kursplanung.HtmlContextGostKursplanungSchueler;
import de.svws_nrw.module.pdf.pdf.base.PdfBuilder;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 */
public final class PdfGostKursplanungSchuelerMitSchienenKursen {

	/** Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlVorlageDateipfad = "de/svws_nrw/module/pdf/gost/kursplanung/SchuelerMitSchienenKursen.html";

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/pdf/gost/kursplanung/SchuelerMitSchienenKursen.css";

	private PdfGostKursplanungSchuelerMitSchienenKursen() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Erstellt das PDF-Dokument und gibt es als Response zum Download zurück.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem die Liste der Schüler mit ihren Kursen erstellt werden soll.
	 * @param schuelerIDs          	Liste der IDs der Schüler, deren Schienen-Kurse-Zuordnung aufgelistet werden sollen. Ist die Liste leer,
	 *                              so wird die allgemeine Schienen-Kurse-Zuordnung der Blockung ausgegeben.
	 *
	 * @return 						HTTP-Response mit der PDF-Datei oder bei Fehler eine WebApplicationException-Response
	 */
	public static Response query(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) {

		try {
			final PdfBuilder pdfBuilder = getPdfBuilder(conn, blockungsergebnisID, schuelerIDs);
			return pdfBuilder.getPdfResponse();
		} catch (WebApplicationException ex) {
			return ex.getResponse();
		}
	}


	/**
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten den PdfBuilder zur Erzeugung der PDF-Datei.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem die Liste der Schüler mit ihren Kursen erstellt werden soll.
	 * @param schuelerIDs          	Liste der IDs der Schüler, deren Schienen-Kurse-Zuordnung aufgelistet werden sollen. Ist die Liste leer,
	 *                              so wird die allgemeine Schienen-Kurse-Zuordnung der Blockung ausgegeben.
	 *
	 * @return						Ein PDF-Builder zur Erzeugung der PDF-Datei
	 */
	public static PdfBuilder getPdfBuilder(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> schuelerIDs) {

		// html-Daten-Contexts erstellen und in Liste sammeln
		final HtmlContextGostKursplanungBlockungsergebnis htmlContextBlockung = new HtmlContextGostKursplanungBlockungsergebnis(conn, blockungsergebnisID);
		final HtmlContextGostKursplanungSchienen htmlContextSchienen = new HtmlContextGostKursplanungSchienen(conn, blockungsergebnisID);
		final HtmlContextGostKursplanungSchueler htmlContextSchueler = new HtmlContextGostKursplanungSchueler(conn, blockungsergebnisID, schuelerIDs);
		final HtmlContext htmlContextSchule = new HtmlContextSchule(conn);

		final List<HtmlContext> htmlContexts = new ArrayList<>();
		htmlContexts.add(htmlContextBlockung);
		htmlContexts.add(htmlContextSchienen);
		htmlContexts.add(htmlContextSchueler);
		htmlContexts.add(htmlContextSchule);

		// Dateiname der PDF-Datei aus den Daten erzeugen.
		final String pdfDateiname =
				"Schueler-Schienen-Kurse-Zuordnung_%d-%s_(Erg-ID%d).pdf".formatted(
					(int) htmlContextBlockung.getContext().getVariable("Abiturjahr"),
					((String) htmlContextBlockung.getContext().getVariable("GostHalbjahr")).replace(".", "-"),
					(Long) htmlContextBlockung.getContext().getVariable("BlockungsergebnisId"));

		// html-Builder erstellen und damit das html mit Daten für die PDF-Datei erzeugen
		final HtmlBuilder htmlBuilder = new HtmlBuilder(htmlVorlageDateipfad, htmlContexts, null);
		final String html = htmlBuilder.getHtml();

		return new PdfBuilder(html, cssDateipfad, pdfDateiname);
	}
}
