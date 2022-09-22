package de.nrw.schule.svws.db.dto.migration.schild.intern;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schildintern_PruefungsOrdnung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOInternPruefungsOrdnungPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schildintern Tabelle: erstes Kürzel */
	public String PO_Krz;

	/** Schildintern Tabelle: zulässige Schulformen der Prüfungsordnungen */
	public String PO_Schulform;

	/** Schildintern Tabelle: zulässige Gliederungen */
	public String PO_SGL;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternPruefungsOrdnungPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternPruefungsOrdnungPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternPruefungsOrdnungPK.
	 * @param PO_Krz   der Wert für das Attribut PO_Krz
	 * @param PO_Schulform   der Wert für das Attribut PO_Schulform
	 * @param PO_SGL   der Wert für das Attribut PO_SGL
	 */
	public MigrationDTOInternPruefungsOrdnungPK(final String PO_Krz, final String PO_Schulform, final String PO_SGL) {
		if (PO_Krz == null) { 
			throw new NullPointerException("PO_Krz must not be null");
		}
		this.PO_Krz = PO_Krz;
		if (PO_Schulform == null) { 
			throw new NullPointerException("PO_Schulform must not be null");
		}
		this.PO_Schulform = PO_Schulform;
		if (PO_SGL == null) { 
			throw new NullPointerException("PO_SGL must not be null");
		}
		this.PO_SGL = PO_SGL;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternPruefungsOrdnungPK other = (MigrationDTOInternPruefungsOrdnungPK) obj;
		if (PO_Krz == null) {
			if (other.PO_Krz != null)
				return false;
		} else if (!PO_Krz.equals(other.PO_Krz))
			return false;

		if (PO_Schulform == null) {
			if (other.PO_Schulform != null)
				return false;
		} else if (!PO_Schulform.equals(other.PO_Schulform))
			return false;

		if (PO_SGL == null) {
			if (other.PO_SGL != null)
				return false;
		} else if (!PO_SGL.equals(other.PO_SGL))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((PO_Krz == null) ? 0 : PO_Krz.hashCode());

		result = prime * result + ((PO_Schulform == null) ? 0 : PO_Schulform.hashCode());

		result = prime * result + ((PO_SGL == null) ? 0 : PO_SGL.hashCode());
		return result;
	}
}