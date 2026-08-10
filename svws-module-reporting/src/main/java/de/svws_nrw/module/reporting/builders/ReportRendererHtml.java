package de.svws_nrw.module.reporting.builders;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
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

	private final Logger logger;
	private final TemplateEngine templateEngine;

	/**
	 * Erzeugt eine Instanz mit gegebener TemplateEngine und Logger.
	 *
	 * @param templateEngine Die zu verwendende TemplateEngine
	 * @param logger         Logger für Fehlermeldungen
	 */
	public ReportRendererHtml(final TemplateEngine templateEngine, final Logger logger) {
		this.templateEngine = templateEngine;
		this.logger = logger;
	}

	/**
	 * Rendert HTML aus Template und Contexts.
	 * <p>Template-Engine und Template werden ausschließlich intern aufgebaut; der API-Client übergibt kein HTML-Template. Ein fehlendes oder leeres Template
	 * ist deshalb ein serverseitiges Problem und wird mit {@code INTERNAL_SERVER_ERROR} gemeldet.</p>
	 *
	 * @param htmlTemplate Das HTML-Template (Thymeleaf)
	 * @param contexts     Liste der HtmlContexts, die zu einem finalen Context zusammengeführt werden
	 *
	 * @return Der gerenderte HTML-String (nie null)
	 *
	 * @throws ApiOperationException Bei fehlender Template-Engine oder fehlendem Template sowie bei einem Fehler während des Renderns jeweils mit
	 *                               {@code INTERNAL_SERVER_ERROR}. Eine aus den Daten-Zugriffen der Vorlage stammende {@link ApiOperationException} wird
	 *                               dagegen mit ihrem ursprünglichen Status durchgereicht, sofern Thymeleaf sie nicht seinerseits verpackt hat.
	 */
	public String renderHtml(final String htmlTemplate, final List<HtmlContext<?>> contexts) throws ApiOperationException {
		try {
			if ((templateEngine == null) || (htmlTemplate == null) || htmlTemplate.isBlank()) {
				if (logger != null) {
					logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Die HTML-Template-Engine oder das HTML-Template wurden nicht übergeben.");
				}
				throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "### FEHLER: Die HTML-Template-Engine oder das HTML-Template wurden "
						+ "nicht übergeben.");
			}
			final Context finalContext = ReportBuilderUtils.mergeHtmlContexts(contexts);
			if (finalContext.getVariableNames().isEmpty()) {
				return "";
			}
			// Der Logger begleitet den Context, damit die Dialekte in den Log des laufenden Reports schreiben können und nicht in einen globalen.
			finalContext.setVariable(ReportBuilderUtils.VARIABLE_LOGGER, logger);
			return templateEngine.process(htmlTemplate, finalContext);
		} catch (final ApiOperationException e) {
			// Bereits klassifizierte Fehler behalten ihren Status - sonst würde der allgemeine Catch daraus einen Serverfehler machen.
			throw e;
		} catch (final Exception e) {
			if (logger != null) {
				ReportingExceptionUtils.logException("### FEHLER: Das HTML konnte nicht gerendert werden.", e, logger, LogLevel.ERROR, 0);
			}
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e, "### FEHLER: Das HTML konnte nicht gerendert werden.");
		}
	}

}
