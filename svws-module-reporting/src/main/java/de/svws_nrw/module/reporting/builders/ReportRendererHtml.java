package de.svws_nrw.module.reporting.builders;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

/**
 * Diese Klasse rendert HTML-Output aus einem Template und einer Menge von HtmlContext-Daten.
 */
public final class ReportRendererHtml {

	private final TemplateEngine templateEngine;

	/**
	 * Erzeugt eine Instanz mit der gegebenen TemplateEngine.
	 * <p>Der Renderer protokolliert nicht: Ein Abbruch trägt seinen Grund als Meldung der Exception, und ausgegeben wird er an der Abschlussgrenze.</p>
	 *
	 * @param templateEngine Die zu verwendende TemplateEngine
	 */
	public ReportRendererHtml(final TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	/**
	 * Rendert HTML aus Template und Contexts.
	 * <p>Template-Engine und Template werden ausschließlich intern aufgebaut; der API-Client übergibt kein HTML-Template. Ein fehlendes oder leeres Template
	 * ist deshalb ein serverseitiges Problem und wird mit {@code INTERNAL_SERVER_ERROR} gemeldet.</p>
	 *
	 * @param htmlTemplate Das HTML-Template (Thymeleaf)
	 * @param contexts     Liste der HtmlContexts, die zu einem finalen Context zusammengeführt werden
	 *
	 * @return Der gerenderte HTML-String (nie null); leer, wenn die Contexts keine fachlichen Variablen tragen.
	 *
	 * @throws ApiOperationException Bei fehlender Template-Engine oder fehlendem Template sowie bei einem Fehler während des Renderns jeweils mit
	 *                               {@code INTERNAL_SERVER_ERROR}. Eine aus den Daten-Zugriffen der Vorlage stammende {@link ApiOperationException} wird
	 *                               dagegen mit ihrem ursprünglichen Status durchgereicht, sofern Thymeleaf sie nicht seinerseits verpackt hat.
	 */
	public String renderHtml(final String htmlTemplate, final List<HtmlContext<?>> contexts) throws ApiOperationException {
		try {
			if ((templateEngine == null) || (htmlTemplate == null) || htmlTemplate.isBlank()) {
				throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
						"### FEHLER: Für die HTML-Erzeugung fehlt die Vorlage oder ihre Verarbeitung.");
			}
			final Context finalContext = ReportBuilderUtils.mergeHtmlContexts(contexts);
			// Der Melder ist eine interne Variable und zählt nicht als Inhalt: Ohne fachliche Variablen gibt es nichts zu rendern.
			if (finalContext.getVariableNames().stream().allMatch(ReportBuilderUtils.VARIABLE_PROBLEMMELDER::equals)) {
				return "";
			}
			return templateEngine.process(htmlTemplate, finalContext);
		} catch (final ApiOperationException e) {
			// Bereits klassifizierte Fehler behalten ihren Status - sonst würde der allgemeine Catch daraus einen Serverfehler machen.
			throw e;
		} catch (final Exception e) {
			// Thymeleaf wickelt Fehler aus den Datenzugriffen der Vorlage in eigene Exceptions. Eine statustragende Ursache in der Kette - etwa der
			// Abbruch des Signier-Batches mit einem Anmeldefehler - behält Status und Meldung; sonst würde ein klassifizierter Client-Fehler zum
			// undifferenzierten Serverfehler.
			final ApiOperationException klassifiziert = ReportingExceptionUtils.apiOperationExceptionInUrsachenkette(e);
			if (klassifiziert != null) {
				throw new ApiOperationException(klassifiziert.getStatus(), e, (klassifiziert.getMessage() != null) ? klassifiziert.getMessage()
						: "### FEHLER: Die HTML-Vorlage des Reports konnte nicht verarbeitet werden.");
			}
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Die HTML-Vorlage des Reports konnte nicht verarbeitet werden.");
		}
	}

}
