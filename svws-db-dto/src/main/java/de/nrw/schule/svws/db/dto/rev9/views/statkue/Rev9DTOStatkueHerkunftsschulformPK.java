package de.nrw.schule.svws.db.dto.rev9.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_Herkunftsschulform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev9DTOStatkueHerkunftsschulformPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Zulässige Schulform für die Herkunft */
	public String SF;

	/** Das Kürzel für die Herkunft/Herkunftsschulform */
	public String HSF;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOStatkueHerkunftsschulformPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOStatkueHerkunftsschulformPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOStatkueHerkunftsschulformPK.
	 * @param SF   der Wert für das Attribut SF
	 * @param HSF   der Wert für das Attribut HSF
	 */
	public Rev9DTOStatkueHerkunftsschulformPK(final String SF, final String HSF) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (HSF == null) { 
			throw new NullPointerException("HSF must not be null");
		}
		this.HSF = HSF;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOStatkueHerkunftsschulformPK other = (Rev9DTOStatkueHerkunftsschulformPK) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (HSF == null) {
			if (other.HSF != null)
				return false;
		} else if (!HSF.equals(other.HSF))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((HSF == null) ? 0 : HSF.hashCode());
		return result;
	}
}