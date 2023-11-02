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
 * Diese Klasse beinhaltet den Code zur Erstellung von Wahlbögen für eine Liste von Schülern für die Laufbahnplanung in der GOSt.
 */
public final class PDFDateiGostLaufbahnplanungSchuelerWahlbogen extends PDFBuilder {
	/** Der Inhalt der html-Dokumentvorlage mit Schleifen und Variablen, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlTemplate = ResourceUtils.text("de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerWahlbogen.html");
	private static final String htmlTemplateNurBelegung = ResourceUtils.text("de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerWahlbogenNurBelegung.html");

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/pdf/gost/laufbahnplanung/SchuelerWahlbogen.css";

	/** Der Dateiname für die PDF-Datei. */
	private final String pdfDateiname;


	/**
	 * Erstellt Liste der Schüler mit deren Kursen auf Basis der HTML-Vorlage
	 *
	 * @param html				Der finale html-Inhalt, aus dem die PDF-Datei erzeugt werden soll.
	 * @param dateiname        	Dateiname der finalen PDF-Datei.
	 */
	private PDFDateiGostLaufbahnplanungSchuelerWahlbogen(final String html, final String dateiname) {
		super(html, cssDateipfad);
		this.pdfDateiname = dateiname;
	}


	/**
	 * Erstellt das PDF-Dokument für eine Liste an Schüler mit deren GOSt-Laufbahnplanungswahlbögen.
	 * und gibt es als Response zum Download zurück.
	 *
	 * @param conn          		die Datenbank-Verbindung
	 * @param schuelerIDs           Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param nurBelegteFaecher 	auf dem Wahlbogen werden nur die vom Schüler belegten Fächer ausgegeben.
	 *
	 * @return 						die HTTP-Response mit dem PDF-Dokument
	 */
	public static Response query(final DBEntityManager conn, final List<Long> schuelerIDs, final Boolean nurBelegteFaecher) {

		try {
			final Context contextLaufbahnplanung = PDFContextGostLaufbahnplanung.setContext(conn, schuelerIDs);
			final Context contextSchule = PDFContextSchule.setContext(conn);

			final PDFDateiGostLaufbahnplanungSchuelerWahlbogen pdf = getPDF(contextLaufbahnplanung, contextSchule, nurBelegteFaecher);

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
	 * @param nurBelegteFaecher 		auf dem Wahlbogen werden nur die vom Schüler belegten Fächer ausgegeben.
	 *
	 * @return 							Objekt zum Erstellen eines PDFs
	 *
	 */
	private static PDFDateiGostLaufbahnplanungSchuelerWahlbogen getPDF(final Context contextLaufbahnplanung, final Context contextSchule, final Boolean nurBelegteFaecher) {

		// Erzeuge den Dateinamen und den finalen html-Inhalt für die PDF-Datei aus den übergebenen Daten im context
		final String pdfDateiname = getDateiname(contextLaufbahnplanung);
		final String html = getHtml(contextLaufbahnplanung, contextSchule, nurBelegteFaecher);

		return new PDFDateiGostLaufbahnplanungSchuelerWahlbogen(html, pdfDateiname);
	}


	/**
	 * Erzeugt den Dateinamen für die PDF-Datei aus den übergebenen Daten
	 *
	 * @param contextLaufbahnplanung	context mit Daten zu den Kursen für das html-Template, aus dem das PDF erzeugt wird
	 *
	 * @return							den Dateinamen für die PDF-Datei
	 */
	private static String getDateiname(final Context contextLaufbahnplanung) {
		String dateiname = "Laufbahnplanung.pdf";

		if ((contextLaufbahnplanung.getVariable("LaufbahnplanungSchueler") instanceof List<?> laufbahnplanungSchueler)
			&& !laufbahnplanungSchueler.isEmpty()
			&& laufbahnplanungSchueler.get(0) instanceof DruckGostLaufbahnplanungSchueler ersteLaufbahnplanung) {

			if (laufbahnplanungSchueler.size() == 1) {
				dateiname = "Laufbahnplanung_%d_%s_%s_%s_(%d).pdf".formatted(
							ersteLaufbahnplanung.Abiturjahr,
							ersteLaufbahnplanung.BeratungsGOStHalbjahr.replace(".", ""),
							ersteLaufbahnplanung.Nachname.replace(' ', '_').replace('.', '_'),
							ersteLaufbahnplanung.Vorname.replace(' ', '_').replace('.', '_'),
							ersteLaufbahnplanung.SchuelerID);
			} else {
				dateiname = "Laufbahnplanung_%d_%s.pdf".formatted(ersteLaufbahnplanung.Abiturjahr, ersteLaufbahnplanung.BeratungsGOStHalbjahr.replace('.', '_'));
			}
		}

		return dateiname;
	}


	/**
	 * Erstellt das finale html-Dokument mit den Daten, das dann für die Erzeugung der PDF-Datei genutzt wird.
	 * Hierzu werden die Variablen in der html-Vorlage durch Daten ersetzt.
	 *
	 * @param contextLaufbahnplanung	context mit Daten zu den Kursen für das html-Template, aus dem das PDF erzeugt wird
	 * @param contextSchule				context mit Daten der Schule für das html-Template, aus dem das PDF erzeugt wird
	 * @param nurBelegteFaecher 		auf dem Wahlbogen werden nur die vom Schüler belegten Fächer ausgegeben.
	 *
	 * @return 							html zum Erstellen eines PDFs
	 *
	 */
	private static String getHtml(final Context contextLaufbahnplanung, final Context contextSchule, final Boolean nurBelegteFaecher) {

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

		if (Boolean.TRUE.equals(nurBelegteFaecher))
			return htmlTemplateNurBelegung != null ? templateEngine.process(htmlTemplateNurBelegung, finalContext) : "";
		else
			return htmlTemplate != null ? templateEngine.process(htmlTemplate, finalContext) : "";
	}

}
