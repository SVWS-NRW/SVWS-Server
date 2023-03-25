package de.svws_nrw.db.dto.current.svws.client;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SVWS_Client_Konfiguration_Benutzer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOClientKonfigurationBenutzerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die ID des Datenbankbenutzers, für den der Client-Konfigurationsdatensatz gespeichert ist */
	public Long Benutzer_ID;

	/** Der Name der Client-Anwendung, für die der Konfigurationsdatensatz gespeichert ist */
	public String AppName;

	/** Der Schlüsselname des Konfigurationsdatensatzes */
	public String Schluessel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOClientKonfigurationBenutzerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOClientKonfigurationBenutzerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOClientKonfigurationBenutzerPK.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param AppName   der Wert für das Attribut AppName
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 */
	public DTOClientKonfigurationBenutzerPK(final Long Benutzer_ID, final String AppName, final String Schluessel) {
		if (Benutzer_ID == null) { 
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
		if (AppName == null) { 
			throw new NullPointerException("AppName must not be null");
		}
		this.AppName = AppName;
		if (Schluessel == null) { 
			throw new NullPointerException("Schluessel must not be null");
		}
		this.Schluessel = Schluessel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOClientKonfigurationBenutzerPK other = (DTOClientKonfigurationBenutzerPK) obj;
		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
			return false;

		if (AppName == null) {
			if (other.AppName != null)
				return false;
		} else if (!AppName.equals(other.AppName))
			return false;

		if (Schluessel == null) {
			if (other.Schluessel != null)
				return false;
		} else if (!Schluessel.equals(other.Schluessel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());

		result = prime * result + ((AppName == null) ? 0 : AppName.hashCode());

		result = prime * result + ((Schluessel == null) ? 0 : Schluessel.hashCode());
		return result;
	}
}