package de.svws_nrw.module.pdf.gost.laufbahnplanung;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchueler;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.pdf.PDFBuilder;
import de.svws_nrw.module.pdf.schule.PDFContextSchule;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer Übersicht zu den Laufbahnplanungsprüfungsergebnissen für eine Liste von Schülern in der GOSt.
 */
public final class PDFDateiGostLaufbahnplanungSchuelerErgebnisuebersicht extends PDFBuilder {
	/** Der Inhalt der html-Dokumentvorlage mit Schleifen und Variablen, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlTemplate = ResourceUtils.text("de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerErgebnisuebersicht.html");

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerErgebnisuebersicht.css";

	/** Der Dateiname für die PDF-Datei. */
	private final String pdfDateiname;


	/**
	 * Erstellt Liste der Schüler mit deren Kursen auf Basis der HTML-Vorlage
	 *
	 * @param html				Der finale html-Inhalt, aus dem die PDF-Datei erzeugt werden soll.
	 * @param dateiname        	Dateiname der finalen PDF-Datei.
	 */
	private PDFDateiGostLaufbahnplanungSchuelerErgebnisuebersicht(final String html, final String dateiname) {
		super(html, cssDateipfad);
		this.pdfDateiname = dateiname;
	}


	/**
	 * Erstellt das PDF-Dokument für eine Liste an Schüler mit deren GOSt-Laufbahnplanungswahlbögen.
	 * und gibt es als Response zum Download zurück.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param schuelerIDs           Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param detaillevel 			gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 *
	 * @return 						die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response query(final DBEntityManager conn, final List<Long> schuelerIDs, final int detaillevel) {

		try {
			final Context contextLaufbahnplanung = PDFContextGostLaufbahnplanung.setContext(conn, schuelerIDs);
			final Context contextSchule = PDFContextSchule.setContext(conn);

			final PDFDateiGostLaufbahnplanungSchuelerErgebnisuebersicht pdf = getPDF(contextLaufbahnplanung, contextSchule, Math.min(Math.max(detaillevel, 0), 2));

			final byte[] data = pdf.getPDFAlsByteArray();
			if (data == null)
				return OperationError.INTERNAL_SERVER_ERROR.getResponse("Fehler bei der Generierung der PDF-Datei.");

			final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(pdf.pdfDateiname, StandardCharsets.UTF_8);

			return Response.ok(data, "application/pdf")
				.header("Content-Disposition", "attachment; " + encodedFilename)
				.build();

		} catch (WebApplicationException ex) {
			return ex.getResponse();
		}
	}


	/**
	 * Erstellt ein neues PDF-Objekt.
	 *
	 * @param contextLaufbahnplanung	context mit Daten zu den Kursen für das html-Template, aus dem das PDF erzeugt wird
	 * @param contextSchule				context mit Daten der Schule für das html-Template, aus dem das PDF erzeugt wird
	 * @param detaillevel 				gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 *
	 * @return 							Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFDateiGostLaufbahnplanungSchuelerErgebnisuebersicht getPDF(final Context contextLaufbahnplanung, final Context contextSchule, final int detaillevel) {

		// Erzeuge den Dateinamen und den finalen html-Inhalt für die PDF-Datei aus den übergebenen Daten im context
		final String pdfDateiname = getDateiname(contextLaufbahnplanung);
		final String html = getHtml(contextLaufbahnplanung, contextSchule, detaillevel);

		return new PDFDateiGostLaufbahnplanungSchuelerErgebnisuebersicht(html, pdfDateiname);
	}


	/**
	 * Erzeugt den Dateinamen für die PDF-Datei aus den übergebenen Daten
	 *
	 * @param contextLaufbahnplanung	context mit Daten zu den Kursen für das html-Template, aus dem das PDF erzeugt wird
	 *
	 * @return							den Dateinamen für die PDF-Datei
	 */
	private static String getDateiname(final Context contextLaufbahnplanung) {
		String dateiname = "Laufbahnplanung_Prüfungsergebnisse.pdf";

		if ((contextLaufbahnplanung.getVariable("LaufbahnplanungSchueler") instanceof List<?> laufbahnplanungSchueler)
			&& !laufbahnplanungSchueler.isEmpty()
			&& laufbahnplanungSchueler.get(0) instanceof DruckGostLaufbahnplanungSchueler ersteLaufbahnplanung) {

			dateiname = "Laufbahnplanung_Prüfungsergebnisse_Abitur-%d_%s.pdf".formatted(
						ersteLaufbahnplanung.Abiturjahr,
						ersteLaufbahnplanung.BeratungsGOStHalbjahr.replace('.', '_'));
		}

		return dateiname;
	}


	/**
	 * Erstellt das finale html-Dokument mit den Daten, das dann für die Erzeugung der PDF-Datei genutzt wird.
	 * Hierzu werden die Variablen in der html-Vorlage durch Daten ersetzt.
	 *
	 * @param contextLaufbahnplanung	context mit Daten zu den Kursen für das html-Template, aus dem das PDF erzeugt wird
	 * @param contextSchule				context mit Daten der Schule für das html-Template, aus dem das PDF erzeugt wird
	 * @param detaillevel 			gibt an, welche Detailinformationen die Liste enthalten soll: 0 = Summen, 1 = Summen und Fehler, 2 = Summen, Fehler und Hinweise
	 *
	 * @return 							html zum Erstellen eines PDFs
	 *
	 */
	private static String getHtml(final Context contextLaufbahnplanung, final Context contextSchule, final int detaillevel) {

		StringTemplateResolver resolver = new StringTemplateResolver();
		resolver.setTemplateMode(TemplateMode.HTML);

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(resolver);

		// Füge die übergebenen Contexts zu einem Context zusammen.
		Context finalContext = new Context();
		for (final String variable : contextLaufbahnplanung.getVariableNames()) {
			finalContext.setVariable(variable, contextLaufbahnplanung.getVariable(variable));
		}
		for (final String variable : contextSchule.getVariableNames()) {
			finalContext.setVariable(variable, contextSchule.getVariable(variable));
		}
		finalContext.setVariable("DruckparameterDetaillevel", detaillevel);

		return htmlTemplate != null ? templateEngine.process(htmlTemplate, finalContext) : "";
	}

}
