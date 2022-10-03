package de.nrw.schule.svws.db.dto.current.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Statkue_Abgangsart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOStatkueAbgangsartPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Statkue Tabelle IT.NRW: zulässige Schulform der Abgangsart */
	public String SF;

	/** Statkue Tabelle IT.NRW: ASD-Kürzel der Abgangsart */
	public String Art;

	/** Statkue Tabelle IT.NRW: ??? */
	public Integer KZ_Bereich;

	/** Statkue Tabelle IT.NRW: ??? */
	public Integer KZ_Bereich_JG;

	/** Statkue Tabelle IT.NRW: zulässige Jahrgäng der Abgangsart */
	public String AbgangsJG;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueAbgangsartPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStatkueAbgangsartPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueAbgangsartPK.
	 * @param SF   der Wert für das Attribut SF
	 * @param Art   der Wert für das Attribut Art
	 * @param KZ_Bereich   der Wert für das Attribut KZ_Bereich
	 * @param KZ_Bereich_JG   der Wert für das Attribut KZ_Bereich_JG
	 * @param AbgangsJG   der Wert für das Attribut AbgangsJG
	 */
	public DTOStatkueAbgangsartPK(final String SF, final String Art, final Integer KZ_Bereich, final Integer KZ_Bereich_JG, final String AbgangsJG) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (Art == null) { 
			throw new NullPointerException("Art must not be null");
		}
		this.Art = Art;
		if (KZ_Bereich == null) { 
			throw new NullPointerException("KZ_Bereich must not be null");
		}
		this.KZ_Bereich = KZ_Bereich;
		if (KZ_Bereich_JG == null) { 
			throw new NullPointerException("KZ_Bereich_JG must not be null");
		}
		this.KZ_Bereich_JG = KZ_Bereich_JG;
		if (AbgangsJG == null) { 
			throw new NullPointerException("AbgangsJG must not be null");
		}
		this.AbgangsJG = AbgangsJG;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueAbgangsartPK other = (DTOStatkueAbgangsartPK) obj;
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

		if (KZ_Bereich == null) {
			if (other.KZ_Bereich != null)
				return false;
		} else if (!KZ_Bereich.equals(other.KZ_Bereich))
			return false;

		if (KZ_Bereich_JG == null) {
			if (other.KZ_Bereich_JG != null)
				return false;
		} else if (!KZ_Bereich_JG.equals(other.KZ_Bereich_JG))
			return false;

		if (AbgangsJG == null) {
			if (other.AbgangsJG != null)
				return false;
		} else if (!AbgangsJG.equals(other.AbgangsJG))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((Art == null) ? 0 : Art.hashCode());

		result = prime * result + ((KZ_Bereich == null) ? 0 : KZ_Bereich.hashCode());

		result = prime * result + ((KZ_Bereich_JG == null) ? 0 : KZ_Bereich_JG.hashCode());

		result = prime * result + ((AbgangsJG == null) ? 0 : AbgangsJG.hashCode());
		return result;
	}
}