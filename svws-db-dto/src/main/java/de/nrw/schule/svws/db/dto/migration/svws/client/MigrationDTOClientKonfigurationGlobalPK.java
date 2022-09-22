package de.nrw.schule.svws.db.dto.migration.svws.client;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SVWS_Client_Konfiguration_Global.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOClientKonfigurationGlobalPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Der Name der Client-Anwendung, für die der Konfigurationsdatensatz gespeichert ist */
	public String AppName;

	/** Der Schlüsselname des Konfigurationsdatensatzes */
	public String Schluessel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOClientKonfigurationGlobalPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOClientKonfigurationGlobalPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOClientKonfigurationGlobalPK.
	 * @param AppName   der Wert für das Attribut AppName
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 */
	public MigrationDTOClientKonfigurationGlobalPK(final String AppName, final String Schluessel) {
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
		MigrationDTOClientKonfigurationGlobalPK other = (MigrationDTOClientKonfigurationGlobalPK) obj;
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
		result = prime * result + ((AppName == null) ? 0 : AppName.hashCode());

		result = prime * result + ((Schluessel == null) ? 0 : Schluessel.hashCode());
		return result;
	}
}