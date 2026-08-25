package de.svws_nrw.module.reporting.builders;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Abstrakte Basisklasse für Report-Builder-Kontexte. Bündelt gemeinsame Eigenschaften und Validierungen.
 * <p>Alle Werte eines Kontexts setzt der Server selbst: Reportvorlage, Ressourcenpfad und gerenderter Inhalt stammen nie aus dem Request. Ein
 * fehlender oder leerer Wert ist deshalb kein Client-Fehler, sondern ein interner Fehler und wird mit {@code INTERNAL_SERVER_ERROR} gemeldet.</p>
 *
 * @param <S> der konkrete Context-Typ des ReportBuilderContext, der von dieser Klasse abgeleitet wird.
 */
public abstract class ReportBuilderContext<S extends ReportBuilderContext<S>> {

	/** Der statische Dateiname, der für Dateien verwendet wird, die einen festen Namen haben, unabhängig vom dynamischen Inhalt. */
	protected String statischerDateiname;

	/** Der Basis-Pfad für Ressourcen wie Fonts, CSS oder andere Dateien, die für die Erstellung eines Berichts erforderlich sind. */
	protected String rootPfad;


	/**
	 * Legt den statischen Dateinamen fest, der für Dateien verwendet wird, die immer denselben Namen haben, unabhängig vom dynamischen Inhalt.
	 * Der statische Dateiname darf weder null noch leer oder blank sein.
	 *
	 * @param statischerDateiname der statische Dateiname als String
	 *
	 * @return die Instanz dieses Kontexts für die Weiterverwendung
	 *
	 * @throws ApiOperationException Wirft eine Exception, wenn der übergebene statische Dateiname null, leer oder blank ist
	 */
	@SuppressWarnings("unchecked")
	public S withStatischerDateiname(final String statischerDateiname) throws ApiOperationException {
		if ((statischerDateiname == null) || statischerDateiname.isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die Erzeugung des Reports fehlt der feste Dateiname.");
		}
		this.statischerDateiname = statischerDateiname;
		return (S) this;
	}

	/**
	 * Setzt den Root-Pfad für Ressourcen wie Fonts oder CSS-Dateien. Der Root-Pfad darf nicht
	 * null, leer oder blank sein. Andernfalls wird eine {@link ApiOperationException} ausgelöst.
	 *
	 * @param rootPfad Der Root-Pfad als String, der für Ressourcen gesetzt werden soll.
	 *
	 * @return die Instanz dieses Kontexts für die Weiterverwendung
	 *
	 * @throws ApiOperationException Wirft eine Exception, wenn der angegebene Root-Pfad null, leer oder blank ist
	 */
	@SuppressWarnings("unchecked")
	public S withRootPfad(final String rootPfad) throws ApiOperationException {
		if ((rootPfad == null) || rootPfad.isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die Erzeugung des Reports fehlt der Pfad zu den Ressourcen.");
		}
		this.rootPfad = rootPfad;
		return (S) this;
	}

	/**
	 * Validiert die aktuelle Instanz des ReportBuilderContext und überprüft, ob alle erforderlichen Eigenschaften gesetzt sind.
	 * Überprüft: Der statische Dateiname und der Root-Pfad dürfen weder null noch leer sein.
	 * Bei Verstößen gegen eine dieser Bedingungen wird eine {@link ApiOperationException} ausgelöst.
	 *
	 * @return Die Instanz dieses ReportBuilderContext-Typs nach erfolgreicher Validierung.
	 *
	 * @throws ApiOperationException Es wird eine Exception geworfen, wenn die Validierung fehlschlägt.
	 */
	@SuppressWarnings("unchecked")
	public S validiert() throws ApiOperationException {
		if ((this.getStatischerDateiname() == null) || this.getStatischerDateiname().isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die Erzeugung des Reports fehlt der feste Dateiname.");
		}
		if ((this.getRootPfad() == null) || this.getRootPfad().isBlank()) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "### FEHLER: Für die Erzeugung des Reports fehlt der Pfad zu den Ressourcen.");
		}
		return (S) this;
	}

	/**
	 * Gibt den statischen Dateinamen zurück, der in der Regel für Dateien genutzt wird, deren Name unabhängig vom dynamischen Inhalt ist (z. B. für
	 * ZIP-Dateien).
	 *
	 * @return Der statische Dateiname als String.
	 */
	protected String getStatischerDateiname() {
		return statischerDateiname;
	}

	/**
	 * Gibt den Root-Pfad für Ressourcen (Fonts, CSS) zurück.
	 *
	 * @return Der Root-Pfad als String.
	 */
	protected String getRootPfad() {
		return rootPfad;
	}

}
