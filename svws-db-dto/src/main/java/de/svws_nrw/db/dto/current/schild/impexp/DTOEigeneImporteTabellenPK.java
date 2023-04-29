package de.svws_nrw.db.dto.current.schild.impexp;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle ImpExp_EigeneImporte_Tabellen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOEigeneImporteTabellenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** externen Textimport Tabellen */
	public int Import_ID;

	/** externen Textimport Tabellen */
	public String TableName;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporteTabellenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEigeneImporteTabellenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporteTabellenPK.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 * @param TableName   der Wert für das Attribut TableName
	 */
	public DTOEigeneImporteTabellenPK(final int Import_ID, final String TableName) {
		this.Import_ID = Import_ID;
		if (TableName == null) {
			throw new NullPointerException("TableName must not be null");
		}
		this.TableName = TableName;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEigeneImporteTabellenPK other = (DTOEigeneImporteTabellenPK) obj;
		if (Import_ID != other.Import_ID)
			return false;

		if (TableName == null) {
			if (other.TableName != null)
				return false;
		} else if (!TableName.equals(other.TableName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Import_ID);

		result = prime * result + ((TableName == null) ? 0 : TableName.hashCode());
		return result;
	}
}
