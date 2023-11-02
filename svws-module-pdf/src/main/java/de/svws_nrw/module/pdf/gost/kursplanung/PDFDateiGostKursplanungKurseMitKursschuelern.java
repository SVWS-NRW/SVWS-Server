package de.svws_nrw.module.pdf.gost.kursplanung;

import de.svws_nrw.base.ResourceUtils;
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
 * Diese Klasse beinhaltet den Code zur Erstellung einer Liste, die zu einem Kurs die enthaltenen Schüler
 * in einem Blockungsergebnis ausgibt.
 */
public final class PDFDateiGostKursplanungKurseMitKursschuelern extends PDFBuilder {
	/** Der Inhalt der html-Dokumentvorlage mit Schleifen und Variablen, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlTemplate = ResourceUtils.text("de/svws_nrw/module/pdf/gost/kursplanung/KursMitKursschuelern.html");

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/pdf/gost/kursplanung/KursMitKursschuelern.css";

	/** Der Dateiname für die PDF-Datei. */
	private final String pdfDateiname;


	/**
	 * Erstellt Liste der Schüler mit deren Kursen auf Basis der HTML-Vorlage
	 *
	 * @param html				Der finale html-Inhalt, aus dem die PDF-Datei erzeugt werden soll.
	 * @param dateiname        	Dateiname der finalen PDF-Datei.
	 */
	private PDFDateiGostKursplanungKurseMitKursschuelern(final String html, final String dateiname) {
		super(html, cssDateipfad);
		this.pdfDateiname = dateiname;
	}


	/**
	 * Erstellt das PDF-Dokument für eine Liste an Kursen mit deren enthaltenen Schülern im gewählten Ergebnis der gewählten Blockung.
	 * und gibt es als Response zum Download zurück.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param blockungsergebnisID	ID des Blockungsergebnisses, aus dem die Liste der Schüler mit ihren Kursen erstellt werden soll.
	 * @param kursIDs           	Liste der IDs der Kurse, deren Schüler aufgelistet werden sollen. Ist die Liste leer, werden alle
	 *                              Kurse der Blockung herangezogen.
	 *
	 * @return die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response query(final DBEntityManager conn, final Long blockungsergebnisID, final List<Long> kursIDs) {

		try {
			final Context contextKurse = PDFContextGostKursplanungKurse.setContext(conn, blockungsergebnisID, kursIDs);
			final Context contextSchule = PDFContextSchule.setContext(conn);

			final PDFDateiGostKursplanungKurseMitKursschuelern pdf = getPDF(contextKurse, contextSchule);

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
	 * @param contextKurse		context mit Daten zu den Kursen für das html-Template, aus dem das PDF erzeugt wird
	 * @param contextSchule		context mit Daten der Schule für das html-Template, aus dem das PDF erzeugt wird
	 *
	 * @return 					Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFDateiGostKursplanungKurseMitKursschuelern getPDF(final Context contextKurse, final Context contextSchule) {

		// Erzeuge den Dateinamen und den finalen html-Inhalt für die PDF-Datei aus den übergebenen Daten im context
		final String pdfDateiname = getDateiname(contextKurse);
		final String html = getHtml(contextKurse, contextSchule);

		return new PDFDateiGostKursplanungKurseMitKursschuelern(html, pdfDateiname);
	}

	/**
	 * Erzeugt den Dateinamen für die PDF-Datei aus den übergebenen Daten
	 *
	 * @param contextKurse 	context mit Daten zu den Kursen für das html-Template, aus dem das PDF erzeugt wird
	 *
	 * @return				den Dateinamen für die PDF-Datei
	 */
	private static String getDateiname(final Context contextKurse) {
		return "Kursliste_%d-%s_(Erg-ID%d).pdf".formatted((int) contextKurse.getVariable("Abiturjahr"), ((String) contextKurse.getVariable("GostHalbjahr")).replace(".", "-"), (Long) contextKurse.getVariable("BlockungsergebnisId"));
	}


	/**
	 * Erstellt das finale html-Dokument mit den Daten, das dann für die Erzeugung der PDF-Datei genutzt wird.
	 * Hierzu werden die Variablen in der html-Vorlage durch Daten ersetzt.
	 *
	 * @param contextKurse		context mit Daten zu den Kursen für das html-Template, aus dem das PDF erzeugt wird
	 * @param contextSchule		context mit Daten der Schule für das html-Template, aus dem das PDF erzeugt wird
	 *
	 * @return 					html zum Erstellen eines PDFs
	 *
	 */
	private static String getHtml(final Context contextKurse, final Context contextSchule) {

		StringTemplateResolver resolver = new StringTemplateResolver();
		resolver.setTemplateMode(TemplateMode.HTML);

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(resolver);

		// Füge die übergebenen Contexts zu einem Context zusammen.
		Context finalContext = new Context();
		for (final String variable : contextKurse.getVariableNames()) {
			finalContext.setVariable(variable, contextKurse.getVariable(variable));
		}
		for (final String variable : contextSchule.getVariableNames()) {
			finalContext.setVariable(variable, contextSchule.getVariable(variable));
		}

		return htmlTemplate != null ? templateEngine.process(htmlTemplate, finalContext) : "";
	}

}
