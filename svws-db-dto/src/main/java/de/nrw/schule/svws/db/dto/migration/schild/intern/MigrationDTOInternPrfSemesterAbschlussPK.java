package de.nrw.schule.svws.db.dto.migration.schild.intern;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schildintern_PrfSemAbschl.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOInternPrfSemesterAbschlussPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schildintern Tabelle: Nummer des Versetzungsvermerk */
	public String Nr;

	/** Schildintern Tabelle: Schulform des Versetzungsvermerk */
	public String Schulform;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternPrfSemesterAbschlussPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternPrfSemesterAbschlussPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternPrfSemesterAbschlussPK.
	 * @param Nr   der Wert für das Attribut Nr
	 * @param Schulform   der Wert für das Attribut Schulform
	 */
	public MigrationDTOInternPrfSemesterAbschlussPK(final String Nr, final String Schulform) {
		if (Nr == null) { 
			throw new NullPointerException("Nr must not be null");
		}
		this.Nr = Nr;
		if (Schulform == null) { 
			throw new NullPointerException("Schulform must not be null");
		}
		this.Schulform = Schulform;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternPrfSemesterAbschlussPK other = (MigrationDTOInternPrfSemesterAbschlussPK) obj;
		if (Nr == null) {
			if (other.Nr != null)
				return false;
		} else if (!Nr.equals(other.Nr))
			return false;

		if (Schulform == null) {
			if (other.Schulform != null)
				return false;
		} else if (!Schulform.equals(other.Schulform))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Nr == null) ? 0 : Nr.hashCode());

		result = prime * result + ((Schulform == null) ? 0 : Schulform.hashCode());
		return result;
	}
}