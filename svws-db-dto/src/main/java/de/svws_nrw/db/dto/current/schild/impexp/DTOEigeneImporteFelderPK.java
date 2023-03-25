package de.svws_nrw.db.dto.current.schild.impexp;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle ImpExp_EigeneImporte_Felder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOEigeneImporteFelderPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** externen Textimport Felder */
	public Integer Import_ID;

	/** externen Textimport Felder */
	public Integer Field_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporteFelderPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEigeneImporteFelderPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporteFelderPK.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 * @param Field_ID   der Wert für das Attribut Field_ID
	 */
	public DTOEigeneImporteFelderPK(final Integer Import_ID, final Integer Field_ID) {
		if (Import_ID == null) { 
			throw new NullPointerException("Import_ID must not be null");
		}
		this.Import_ID = Import_ID;
		if (Field_ID == null) { 
			throw new NullPointerException("Field_ID must not be null");
		}
		this.Field_ID = Field_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEigeneImporteFelderPK other = (DTOEigeneImporteFelderPK) obj;
		if (Import_ID == null) {
			if (other.Import_ID != null)
				return false;
		} else if (!Import_ID.equals(other.Import_ID))
			return false;

		if (Field_ID == null) {
			if (other.Field_ID != null)
				return false;
		} else if (!Field_ID.equals(other.Field_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Import_ID == null) ? 0 : Import_ID.hashCode());

		result = prime * result + ((Field_ID == null) ? 0 : Field_ID.hashCode());
		return result;
	}
}