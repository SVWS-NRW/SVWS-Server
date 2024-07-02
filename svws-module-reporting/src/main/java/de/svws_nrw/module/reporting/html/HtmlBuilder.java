package de.svws_nrw.module.reporting.html;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.dialects.ConvertExpressionDialect;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Erzeugt aus einem Thymeleaf-html-Template (html-Vorlage) und den zugehörigen Daten in den Contexts das finale html inklusive der Daten.
 */
public class HtmlBuilder {

	/** Html-Inhalt der Vorlagendatei. */
	private final String htmlVorlage;

	/** Html-Inhalt der finalen Html-Datei. */
	private String htmlFinal;

	/** Liste mit Daten-Contexts, die zu einem finalen Context zusammengefügt werden, um damit das html-Template zu füllen. */
	private final List<HtmlContext> contexts;

	/** Dateiname der html-Datei. */
	private final String dateiname;



	/**
	 * Erstellt einen neunen html-Builder und initialisiert die Variablen
	 * @param htmlVorlage   Der Inhalt einer Html-Vorlagendatei, die mit Daten gefüllt werden soll.
	 * @param contexts 	    Liste mit Daten-Contexts, die zu einem finalen Context zusammengefügt werden, um damit das html-Template zu füllen.
	 * @param dateiname 	Dateiname der html-Datei ohne Dateiendung.
	 */
	public HtmlBuilder(final String htmlVorlage, final List<HtmlContext> contexts, final String dateiname) {
		this.htmlVorlage = htmlVorlage;
		this.contexts = contexts;
		this.dateiname = dateiname;
		this.htmlFinal = "";
	}


	/**
	 * Gibt den Dateinamen der Html-Datei zurück.
	 * @return Dateiname der Html-Datei.
	 */
	public String getDateiname() {
		return dateiname;
	}

	/**
	 * Gibt den Dateinamen der Html-Datei mit Dateiendung zurück.
	 * @return Dateiname der Html-Datei mit Endung.
	 */
	public String getDateinameMitEndung() {
		return dateiname + ".html";
	}


	/**
	 * Gibt den finalen Html-Inhalt als String zurück.
	 * @return String des finalen Html-Inhaltes.
	 */
	public String getHtml() {
		return erzeugeHtml();
	}


	/**
	 * Gibt den finalen Html-Inhalt in Form eines Byte-Arrays.
	 * @return 	das Byte-Array des finalen Html-Inhaltes im UTF-8-Format.
	 */
	public byte[] getHtmlByteArray() {
		return erzeugeHtml().getBytes(StandardCharsets.UTF_8);
	}


	/**
	 * Erzeugt eine Response mit einer Html-Datei als Content
	 * @return Response mit der Html-Datei als Content
	 */
	public Response getHtmlResponse() {
		final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(dateiname, StandardCharsets.UTF_8);
		final String html = erzeugeHtml();
		return Response.ok(html, "text/html")
				.header("Content-Disposition", "attachment; " + encodedFilename)
				.build();
	}


	/**
	 * Erstellt das finale Html-Dokument mit den Daten.
	 * Hierzu werden die Variablen in der Html-Vorlage durch Daten ersetzt.
	 * @return 	Das finale Html mit den Daten
	 */
	private String erzeugeHtml() {

		// Wurde das finale Html bereits erzeugt, gebe dieses zurück.
		if (!htmlFinal.isEmpty())
			return htmlFinal;

		final StringTemplateResolver resolver = new StringTemplateResolver();
		resolver.setTemplateMode(TemplateMode.HTML);

		final TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(resolver);

		// Dialects des SVWS-Servers für weitere Funktionen hinzufügen.
		templateEngine.addDialect(new ConvertExpressionDialect());

		// Füge die übergebenen Contexts zu einem Context zusammen.
		final Context finalContext = new Context();
		if ((contexts != null) && !contexts.isEmpty()) {
			for (final HtmlContext htmlCtx : contexts) {
				for (final String variable : htmlCtx.getContext().getVariableNames()) {
					finalContext.setVariable(variable, htmlCtx.getContext().getVariable(variable));
				}
			}
		}

		htmlFinal = (!(finalContext.getVariableNames().isEmpty() || (htmlVorlage == null))) ? templateEngine.process(htmlVorlage, finalContext) : "";
		return htmlFinal;
	}
}
