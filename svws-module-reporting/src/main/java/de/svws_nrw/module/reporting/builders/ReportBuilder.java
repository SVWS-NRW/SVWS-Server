package de.svws_nrw.module.reporting.builders;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Abstrakte Klasse für den Aufbau von Reports.
 * Diese Klasse dient als Basis für spezielle Implementierungen von Report-Builder. Sie bietet grundlegende Funktionalitäten wie das Festlegen des Dateinamens,
 * die Generierung des Reports als Byte-Array und die Erstellung einer HTTP-Response mit dem Reportinhalt.
 * <p>Der Content-Type legt die Implementierung selbst fest; wie die Werte des {@link ReportBuilderContext} ist er interner Zustand und ein fehlender Wert
 * damit {@code INTERNAL_SERVER_ERROR}.</p>
 *
 * @param <T> Der Rückgabetyp des generierten Outputs (String, byte[])
 */
public abstract class ReportBuilder<T> {

	/** Der Kontext des Report-Builders, der die gemeinsamen Eigenschaften und Validierungen bündelt. */
	protected final ReportBuilderContext<?> reportBuilderContext;

	/** Der Content-Typ (MIME-Type) des Builder-Ergebnisses als String, der für die HTTP-Response gesetzt werden soll. */
	protected final String contentType;

	/** Der Dateiname des Reports. Dieser ergibt sich in der Regel aus einer Dateinamensvorlage und den zugehörigen Daten. */
	protected final String dateiname;


	/**
	 * Erstellt eine neue Instanz von ReportBuilder.
	 *
	 * @param reportBuilderContext der Kontext des Report-Builders, der die gemeinsamen Eigenschaften und Validierungen bündelt.
	 * @param contentType der MIME-Typ (Content-Type) des Reports, der nicht null, leer oder blank sein darf
	 * @param dateiname der Dateiname des Reports
	 *
	 * @throws ApiOperationException Die Exception wird ausgelöst, wenn ein Fehler bei der Validierung auftritt.
	 */
	protected ReportBuilder(final ReportBuilderContext<?> reportBuilderContext, final String contentType, final String dateiname)
			throws ApiOperationException {
		this.reportBuilderContext = reportBuilderContext.validiert();
		if ((contentType == null) || contentType.isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die Erzeugung des Reports fehlt die Angabe des Dateityps.");
		}
		this.contentType = contentType;
		this.dateiname = (dateiname != null) ? dateiname : "";
	}

	/**
	 * Gibt den MIME-Typ des Reports zurück.
	 * Diese Methode liefert den Content-Typ als String, der für die HTTP-Response gesetzt werden soll. Der konkrete Wert hängt von der Implementierung ab.
	 *
	 * @return der MIME-Type des Reports als String
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Gibt den Dateinamen des Reports zurück.
	 *
	 * @return der Dateiname des Reports als String
	 */
	public String getDateiname() {
		return dateiname;
	}

	/**
	 * Gibt den Dateinamen des Reports zusammen mit der entsprechenden Dateiendung zurück.
	 *
	 * @return der vollständige Dateiname des Reports inklusive der Dateiendung als String
	 */
	public abstract String getDateinameMitEndung();

	/**
	 * Der statische Dateiname, der unabhängig vom dynamischen Inhalt bleibt. Z. B. für ZIP-Dateien genutzt.
	 *
	 * @return der statische Dateiname, z. B. für ZIP-Dateien.
	 */
	public String getStatischerDateiname() {
		return this.reportBuilderContext.getStatischerDateiname();
	}

	/**
	 * Gibt den Root-Pfad für Ressourcen zurück.
	 *
	 * @return der Root-Pfad als String
	 */
	public String getRootPfad() {
		return this.reportBuilderContext.getRootPfad();
	}

	/**
	 * Generiert das gewünschte Objekt basierend auf der Implementierung im konkreten Report-Builder.
	 *
	 * @return eine Instanz des generierten Objekts vom generischen Typ <T>
	 */
	public abstract T generate() throws ApiOperationException;

	/**
	 * Gibt die Byte-Darstellung des Reports zurück.
	 * Diese Methode ruft eine interne Methode auf, um das Byte-Array des Reports zu generieren. Bei Fehlern während der Generierung wird eine
	 * ApiOperationException ausgelöst.
	 *
	 * @return Ein Byte-Array, das den generierten Report repräsentiert
	 */
	public byte[] getByteArray() throws ApiOperationException {
		return generateInternalByteArray();
	}

	/**
	 * Generiert ein internes Byte-Array, das den Inhalt des Reports darstellt.
	 * Diese Methode ist abstrakt und muss in abgeleiteten Klassen implementiert werden. Während der Generierung können IO-Probleme auftreten, die durch eine
	 * Exception signalisiert werden.
	 *
	 * @return Ein Byte-Array, das den generierten Report repräsentiert
	 */
	protected abstract byte[] generateInternalByteArray() throws ApiOperationException;

}
