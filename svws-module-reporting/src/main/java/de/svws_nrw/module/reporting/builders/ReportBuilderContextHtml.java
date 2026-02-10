package de.svws_nrw.module.reporting.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.TemplateEngine;

/**
 * Context-Klasse zur schrittweisen Konfiguration und Erstellung einer Instanz von {@link ReportBuilderHtml}.
 * Diese Klasse ermöglicht die Konfiguration verschiedener Parameter wie der HTML-Vorlage, der Kontexte, der IDs, des Dateinamens sowie der
 * Template-Engine.
 */
public final class ReportBuilderContextHtml extends ReportBuilderContext<ReportBuilderContextHtml> {

	/** Die TemplateEngine, die für die Verarbeitung von Templates verwendet wird. */
	private TemplateEngine htmlTemplateEngine;

	/** Eine Liste von HtmlContext-Objekten, die für die Kontexte des Reports verwendet werden. */
	private final List<HtmlContext<?>> htmlContexts = new ArrayList<>();

	/** Das HTML-Template, welches mit der Template-Engine verarbeitet wird. */
	private String htmlTemplate;

	/** Der HTML-Renderer, der die HTML-Datei erzeugt. */
	private ReportRendererHtml renderer;

	/** Die IDs, für die der Builder die HTML-Datei erzeugt. */
	private final Set<Long> ids = new LinkedHashSet<>();

	/** Die Dateinamensvorlage, aus der der Dateiname des Reports ermittelt wird. */
	private String dateinamensvorlage;


	/**
	 * Fügt einen HtmlContext zur Liste der Kontexte hinzu.
	 *
	 * @param htmlContext Der Hinzuzufügende HtmlContext.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextHtml addHtmlContext(final HtmlContext<?> htmlContext) {
		this.htmlContexts.add(htmlContext);
		return this;
	}

	/**
	 * Fügt der Liste mehrere HtmlContexts hinzu.
	 *
	 * @param htmlContexts Die Liste von HtmlContexts, die hinzugefügt werden sollen.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextHtml addHtmlContexts(final List<HtmlContext<?>> htmlContexts) {
		this.htmlContexts.addAll(htmlContexts);
		return this;
	}

	/**
	 * Fügt die angegebene ID der Liste der IDs hinzu, sofern sie nicht null ist.
	 *
	 * @param id Die hinzuzufügende ID als Long-Wert.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextHtml addId(final Long id) {
		if (id != null) {
			this.ids.add(id);
		}
		return this;
	}

	/**
	 * Fügt der Builder-Instanz eine Sammlung von IDs hinzu. Null-Werte innerhalb der Sammlung werden ignoriert.
	 *
	 * @param ids Eine Sammlung von IDs vom Typ Long, die hinzugefügt werden sollen.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextHtml addIds(final Collection<Long> ids) {
		if (ids != null) {
			this.ids.addAll(ids.stream().filter(Objects::nonNull).toList());
		}
		return this;
	}

	/**
	 * Setzt die Dateinamensvorlage für den Builder. Die Vorlage wird verwendet, um den Dateinamen dynamisch aus den Kontexten zu generieren.
	 *
	 * @param dateinamensvorlage Die Dateinamensvorlage im Thymeleaf-Format
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextHtml withDateinamensvorlage(final String dateinamensvorlage) {
		this.dateinamensvorlage = dateinamensvorlage;
		return this;
	}

	/**
	 * Setzt das HTML-Template für den Builder.
	 *
	 * @param htmlTemplate Das HTML-Template, das verwendet werden soll.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn die HTML-Vorlage leer ist.
	 */
	public ReportBuilderContextHtml withHtmlTemplate(final String htmlTemplate) throws ApiOperationException {
		if ((htmlTemplate == null) || htmlTemplate.isBlank())
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Bei der HTML-Erzeugung darf die HTML-Vorlage nicht leer sein");
		this.htmlTemplate = htmlTemplate;
		return this;
	}

	/**
	 * Setzt die Template-Engine für den Builder.
	 *
	 * @param htmlTemplateEngine Die Template-Engine, die verwendet werden soll.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextHtml withHtmlTemplateEngine(final TemplateEngine htmlTemplateEngine) {
		this.htmlTemplateEngine = htmlTemplateEngine;
		return this;
	}

	/**
	 * Setzt einen benutzerdefinierten HTML-Renderer.
	 *
	 * @param renderer Der Renderer, der verwendet werden soll.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextHtml withRenderer(final ReportRendererHtml renderer) {
		this.renderer = renderer;
		return this;
	}


	/**
	 * Überprüft die Validität der aktuellen Instanz, indem sichergestellt wird, dass die HTML-Vorlage gesetzt und nicht leer ist.
	 *
	 * @return Die aktuelle Instanz des Builders, falls die Validierung erfolgreich ist.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn die HTML-Vorlage nicht gesetzt oder leer ist.
	 */
	@Override
	public ReportBuilderContextHtml validiert() throws ApiOperationException {
		super.validiert();
		if ((htmlTemplate == null) || htmlTemplate.isBlank())
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die HTML-Vorlage des Report-Builders darf nicht leer sein");
		return this;
	}


	/**
	 * Gibt die Liste der HtmlContexts zurück, die in der aktuellen Instanz enthalten sind.
	 *
	 * @return Eine Liste von HtmlContexts der generischen Typisierung, die für die Erstellung von Report-Daten verwendet werden können.
	 */
	List<HtmlContext<?>> getHtmlContexts() {
		return htmlContexts;
	}

	/**
	 * Gibt die aktuell gesetzte Dateinamensvorlage des Builders zurück.
	 *
	 * @return Die Dateinamensvorlage im Thymeleaf-Format als String.
	 */
	String getDateinamensvorlage() {
		return dateinamensvorlage;
	}

	/**
	 * Gibt die Menge der IDs zurück, die in diesem Kontext definiert sind.
	 *
	 * @return Eine Menge von Long-Werten, die die IDs darstellen.
	 */
	Set<Long> getIds() {
		return ids;
	}

	/**
	 * Gibt das aktuelle HTML-Template zurück, das in diesem Kontext definiert ist.
	 *
	 * @return Das HTML-Template als String.
	 */
	String getHtmlTemplate() {
		return htmlTemplate;
	}

	/**
	 * Gibt die Template-Engine zurück. Falls die Template-Engine nicht gesetzt ist, wird eine Standard-HTML-Template-Engine erstellt und zurückgegeben.
	 *
	 * @return Die Template-Engine für die Verwendung im Reporting.
	 */
	TemplateEngine getHtmlTemplateEngine() {
		if (this.htmlTemplateEngine == null)
			this.htmlTemplateEngine = ReportBuilderUtils.createHtmlTemplateEngine();
		return htmlTemplateEngine;
	}

	/**
	 * Gibt den aktuellen HTML-Renderer zurück. Falls kein Renderer vorhanden ist, wird ein neuer {@link ReportRendererHtml} mit der aktuellen
	 * Template-Engine und dem Logger erstellt.
	 *
	 * @return Der aktuelle oder neu erstellte HTML-Renderer.
	 */
	ReportRendererHtml getRenderer() {
		if (this.renderer == null)
			this.renderer = new ReportRendererHtml(this.getHtmlTemplateEngine(), this.logger);
		return renderer;
	}

}
