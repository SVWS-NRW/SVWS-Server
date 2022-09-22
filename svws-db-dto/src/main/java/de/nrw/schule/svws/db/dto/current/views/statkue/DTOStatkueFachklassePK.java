package de.nrw.schule.svws.db.dto.current.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_Fachklasse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOStatkueFachklassePK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schild-ID der Fachgruppe */
	public Integer BKIndex;

	/** Fachklassenschlüssel Teil 1 */
	public String FKS;

	/** Fachklassenschlüssel Teil 2 */
	public String AP;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueFachklassePK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStatkueFachklassePK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueFachklassePK.
	 * @param BKIndex   der Wert für das Attribut BKIndex
	 * @param FKS   der Wert für das Attribut FKS
	 * @param AP   der Wert für das Attribut AP
	 */
	public DTOStatkueFachklassePK(final Integer BKIndex, final String FKS, final String AP) {
		if (BKIndex == null) { 
			throw new NullPointerException("BKIndex must not be null");
		}
		this.BKIndex = BKIndex;
		if (FKS == null) { 
			throw new NullPointerException("FKS must not be null");
		}
		this.FKS = FKS;
		if (AP == null) { 
			throw new NullPointerException("AP must not be null");
		}
		this.AP = AP;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueFachklassePK other = (DTOStatkueFachklassePK) obj;
		if (BKIndex == null) {
			if (other.BKIndex != null)
				return false;
		} else if (!BKIndex.equals(other.BKIndex))
			return false;

		if (FKS == null) {
			if (other.FKS != null)
				return false;
		} else if (!FKS.equals(other.FKS))
			return false;

		if (AP == null) {
			if (other.AP != null)
				return false;
		} else if (!AP.equals(other.AP))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BKIndex == null) ? 0 : BKIndex.hashCode());

		result = prime * result + ((FKS == null) ? 0 : FKS.hashCode());

		result = prime * result + ((AP == null) ? 0 : AP.hashCode());
		return result;
	}
}