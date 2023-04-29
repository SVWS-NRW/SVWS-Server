package de.svws_nrw.db.dto.migration.schild.impexp;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle ImpExp_EigeneImporte_Tabellen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class MigrationDTOEigeneImporteTabellenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** externen Textimport Tabellen */
	public Integer Import_ID;

	/** externen Textimport Tabellen */
	public String TableName;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneImporteTabellenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOEigeneImporteTabellenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneImporteTabellenPK.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 * @param TableName   der Wert für das Attribut TableName
	 */
	public MigrationDTOEigeneImporteTabellenPK(final Integer Import_ID, final String TableName) {
		if (Import_ID == null) {
			throw new NullPointerException("Import_ID must not be null");
		}
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
		MigrationDTOEigeneImporteTabellenPK other = (MigrationDTOEigeneImporteTabellenPK) obj;
		if (Import_ID == null) {
			if (other.Import_ID != null)
				return false;
		} else if (!Import_ID.equals(other.Import_ID))
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
		result = prime * result + ((Import_ID == null) ? 0 : Import_ID.hashCode());

		result = prime * result + ((TableName == null) ? 0 : TableName.hashCode());
		return result;
	}
}
