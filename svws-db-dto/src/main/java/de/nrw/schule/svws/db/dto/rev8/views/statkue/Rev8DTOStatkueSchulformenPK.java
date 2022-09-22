package de.nrw.schule.svws.db.dto.rev8.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOStatkueSchulformenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Das Kürzel der Schulform */
	public String SF;

	/** Die Bezeichnung der Schulform */
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueSchulformenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStatkueSchulformenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueSchulformenPK.
	 * @param SF   der Wert für das Attribut SF
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public Rev8DTOStatkueSchulformenPK(final String SF, final String Bezeichnung) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueSchulformenPK other = (Rev8DTOStatkueSchulformenPK) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (Bezeichnung == null) {
			if (other.Bezeichnung != null)
				return false;
		} else if (!Bezeichnung.equals(other.Bezeichnung))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((Bezeichnung == null) ? 0 : Bezeichnung.hashCode());
		return result;
	}
}