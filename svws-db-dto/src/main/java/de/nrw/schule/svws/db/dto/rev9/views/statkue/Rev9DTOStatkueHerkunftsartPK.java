package de.nrw.schule.svws.db.dto.rev9.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_Herkunftsart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev9DTOStatkueHerkunftsartPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Zulässige Schulform für die Herkunft */
	public String SF;

	/** Das Kürzel für die Herkunftsart */
	public String Art;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOStatkueHerkunftsartPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOStatkueHerkunftsartPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOStatkueHerkunftsartPK.
	 * @param SF   der Wert für das Attribut SF
	 * @param Art   der Wert für das Attribut Art
	 */
	public Rev9DTOStatkueHerkunftsartPK(final String SF, final String Art) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (Art == null) { 
			throw new NullPointerException("Art must not be null");
		}
		this.Art = Art;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOStatkueHerkunftsartPK other = (Rev9DTOStatkueHerkunftsartPK) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (Art == null) {
			if (other.Art != null)
				return false;
		} else if (!Art.equals(other.Art))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((Art == null) ? 0 : Art.hashCode());
		return result;
	}
}