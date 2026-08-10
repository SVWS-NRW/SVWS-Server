package de.svws_nrw.module.reporting.builders;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Context-Klasse zur schrittweisen Konfiguration und Erstellung einer Instanz von {@link ReportBuilderPdf}.
 * Diese Klasse ermöglicht die Konfiguration verschiedener Parameter.
 */
public final class ReportBuilderContextPdf extends ReportBuilderContext<ReportBuilderContextPdf> {

	/** Der HTML-Inhalt, der als PDF-Datei gerendert werden soll. */
	private String htmlInput;

	/** Der Dateiname, der für den Report festgelegt werden soll. */
	private String dateiname;

	/** Der Renderer, der zur Generierung des PDF-Dokumentes verwendet wird. */
	private ReportRendererPdf renderer;

	/** Die IDs der Hauptdaten, für die der Builder die PDF-Datei erzeugt. Sie dienen der Auflösung gleicher Dateinamen in der ZIP-Sammelausgabe. */
	private final Set<Long> ids = new LinkedHashSet<>();


	/**
	 * Setzt den Dateinamen für den Report.
	 *
	 * @param dateiname Der Dateiname, der für den Report festgelegt werden soll
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn der Dateiname leer ist
	 */
	public ReportBuilderContextPdf withDateiname(final String dateiname) throws ApiOperationException {
		if ((dateiname == null) || dateiname.isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Der Dateiname des Report-Builders darf nicht leer sein");
		}
		this.dateiname = dateiname;
		return this;
	}

	/**
	 * Setzt den HTML-Inhalt, der als PDF-Datei gerendert werden soll.
	 *
	 * @param htmlInput Der HTML-Inhalt, der als PDF-Datei gerendert werden soll.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn der HTML-Inhalt leer ist
	 */
	public ReportBuilderContextPdf withHtmlInput(final String htmlInput) throws ApiOperationException {
		if ((htmlInput == null) || htmlInput.isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Der HTML-Input des Report-Builders darf nicht leer sein");
		}
		this.htmlInput = htmlInput;
		return this;
	}

	/**
	 * Fügt der Builder-Instanz eine Sammlung von IDs hinzu. Null-Werte innerhalb der Sammlung werden ignoriert.
	 *
	 * @param ids Eine Sammlung von IDs vom Typ Long, die hinzugefügt werden sollen.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextPdf addIds(final Collection<Long> ids) {
		if (ids != null) {
			this.ids.addAll(ids.stream().filter(Objects::nonNull).toList());
		}
		return this;
	}

	/**
	 * Setzt einen benutzerdefinierten Renderer.
	 *
	 * @param renderer Der zu verwendende Renderer
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 */
	public ReportBuilderContextPdf withRenderer(final ReportRendererPdf renderer) {
		this.renderer = renderer;
		return this;
	}


	/**
	 * Führt eine Validierung der aktuellen Instanz von {@code ReportBuilderContextPdf} durch.
	 * Überprüft, ob die Eigenschaften {@code dateiname} und {@code htmlInput}
	 * nicht null oder leer sind.
	 *
	 * @return Die Instanz dieses Kontexts für die Weiterverwendung.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn der {@code dateiname} oder {@code htmlInput}
	 * null oder leer ist.
	 */
	@Override
	public ReportBuilderContextPdf validiert() throws ApiOperationException {
		super.validiert();
		if ((dateiname == null) || dateiname.isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Der Dateiname des Report-Builders darf nicht leer sein");
		}
		if ((htmlInput == null) || htmlInput.isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Der HTML-Input des Report-Builders darf nicht leer sein");
		}
		return this;
	}


	/**
	 * Gibt den Dateinamen des Reports zurück.
	 *
	 * @return Der Dateiname des Reports als String.
	 */
	String getDateiname() {
		return dateiname;
	}

	/**
	 * Gibt den HTML-Inhalt zurück, der für die Erstellung des Reports verwendet wird.
	 *
	 * @return Der HTML-Inhalt als String.
	 */
	String getHtmlInput() {
		return htmlInput;
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
	 * Gibt den Renderer zurück, der zur Generierung des Reports verwendet wird. Falls kein Renderer gesetzt wurde, wird ein neuer {@link ReportRendererPdf}
	 * mit dem aktuellen Logger erstellt.
	 *
	 * @return Der Renderer vom Typ {@link ReportRendererPdf}, der für die Reportgenerierung verwendet wird.
	 */
	ReportRendererPdf getRenderer() {
		if (this.renderer == null) {
			this.renderer = new ReportRendererPdf(this.logger);
		}
		return renderer;
	}

}
