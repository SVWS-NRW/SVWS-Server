package de.svws_nrw.module.reporting.builders;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

import de.svws_nrw.db.utils.ApiOperationException;


/**
 * Eine Klasse zur Erstellung von HTML-Reports. Diese Klasse erweitert die abstrakte Klasse ReportBuilder
 * und übernimmt die spezifischen Anforderungen für die Generierung von HTML-Inhalten mithilfe einer
 * Template-Engine.
 */
public final class ReportBuilderHtml extends ReportBuilder<String> {

	/** Der Kontext, der alle notwendigen Informationen für die Erstellung des ReportBuilderHtml enthält. */
	private final ReportBuilderContextHtml builderContext;

	/** Speichert den generierten HTML-Output des Reports. Der Wert wird durch Verarbeitung eines Templates und der zugehörigen Datenkontexte erstellt. */
	private String htmlOutput = "";


	/**
	 * Erstellt eine neue Instanz von ReportBuilderHtml basierend auf dem angegebenen Kontext. Überprüft, ob alle notwendigen Parameter im Kontext gesetzt
	 * sind, und löst eine Exception aus, wenn ein erforderliches Feld fehlt.
	 *
	 * @param builderContext Der Kontext, der alle notwendigen Informationen für die Erstellung des ReportBuilderHtml enthält.
	 *
	 * @throws ApiOperationException Die Exception wird ausgelöst, wenn ein Fehler bei der Validierung auftritt.
	 */
	public ReportBuilderHtml(final ReportBuilderContextHtml builderContext) throws ApiOperationException {
		super(builderContext.validiert(), "text/html", generiereDateiname(builderContext));
		this.builderContext = builderContext;
	}

	/**
	 * Gibt die Menge der IDs zurück, die im internen Builder gespeichert sind. Die zurückgegebene Menge ist unveränderlich und enthält alle aktuell gespeicherten IDs.
	 *
	 * @return eine unveränderliche Menge von IDs (Long-Werten)
	 */
	public Set<Long> getIds() {
		return Collections.unmodifiableSet(builderContext.getIds());
	}

	/**
	 * Gibt den vollständigen Dateinamen inklusive der Dateiendung zurück. Diese Methode erzeugt den Dateinamen basierend auf dem Basismodul-Attribut
	 * und fügt die Standardendung ".html" hinzu.
	 *
	 * @return der vollständige Dateiname des Reports als String mit der Endung ".html"
	 */
	@Override
	public String getDateinameMitEndung() {
		return dateiname + ".html";
	}

	/**
	 * Generiert den finalen HTML-Output des Reports.
	 * Diese Methode ruft intern die Methode {@code generateHtml()} auf, um den HTML-Report durch Verarbeitung der definierten Vorlagen und Datenkontexte zu erstellen.
	 * Bestehende Caches werden berücksichtigt, um unnötige Neuberechnungen zu vermeiden.
	 *
	 * @return der generierte HTML-Report als String. Gibt einen leeren String zurück, wenn keine Vorlage oder Kontexte verfügbar sind.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn beim Rendern des HTML-Templates ein Fehler auftritt.
	 */
	@Override
	public String generate() throws ApiOperationException {
		return generateHtml();
	}

	/**
	 * Generiert ein internes Byte-Array, das den HTML-Output des Reports repräsentiert.
	 * Diese Methode verarbeitet den generierten HTML-Code mittels UTF-8-Codierung zu einem Byte-Array.
	 * Der HTML-Code wird durch die Methode {@code generateHtml()} erzeugt, die die notwendigen Daten und Kontexte zusammenführt.
	 *
	 * @return Ein Byte-Array, das den HTML-Output des Reports in UTF-8-Codierung darstellt
	 *
	 * @throws ApiOperationException Wird geworfen, wenn beim Rendern des HTML-Templates ein Fehler auftritt.
	 */
	@Override
	protected byte[] generateInternalByteArray() throws ApiOperationException {
		return generateHtml().getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Generiert den finalen HTML-Output durch Verarbeitung eines HTML-Templates mit den zusammengeführten Datenkontexten.
	 * Diese Methode verwendet einen Cache-Mechanismus, um unnötige Neuberechnungen zu vermeiden. Wenn der HTML-Output bereits generiert wurde, wird der
	 * zwischengespeicherte Wert zurückgegeben.
	 *
	 * @return der generierte HTML-Code als String. Gibt einen leeren String zurück, wenn entweder kein Template vorhanden ist oder keine Variablen verfügbar sind.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn beim Rendern des HTML-Templates ein Fehler auftritt.
	 */
	private String generateHtml() throws ApiOperationException {
		// Wenn das HTML bereits einmal erzeugt wurde, so wird das HTML direkt zurückgegeben.
		if (!htmlOutput.isEmpty())
			return htmlOutput;

		// HTML mittels Renderer erzeugen.
		htmlOutput = builderContext.getRenderer().renderHtml(builderContext.getHtmlTemplate(), builderContext.getHtmlContexts());

		return htmlOutput;
	}

	/**
	 * Erzeuge den Dateinamen für den Builder. Versuche zunächst, den Namen aus der Dateinamensvorlage
	 * zu generieren. Falls dies fehlschlägt oder keine Vorlage vorhanden ist, wird der statische Dateiname verwendet.
	 *
	 * @param builderContext Der Builder mit den notwendigen Informationen
	 *
	 * @return Der erzeugte Dateiname
	 */
	private static String generiereDateiname(final ReportBuilderContextHtml builderContext) {
		// Wenn keine Vorlage vorhanden ist, wird der statische Name verwendet.
		if ((builderContext.getDateinamensvorlage() == null) || builderContext.getDateinamensvorlage().isBlank()) {
			return builderContext.getStatischerDateiname();
		}

		// Versuche, den Dateinamen aus der Vorlage zu generieren. Wenn dies misslingt, wird ein leerer String zurückgegeben und der statische Name verwendet.
		final String generierterDateiname =
				ReportBuilderUtils.generiereDateinameAusVorlage(builderContext.getDateinamensvorlage(),	builderContext.getHtmlContexts());
		if (!generierterDateiname.isBlank())
			return generierterDateiname;

		// Fallback: Verwende den statischen Dateinamen
		return builderContext.getStatischerDateiname();
	}

}
