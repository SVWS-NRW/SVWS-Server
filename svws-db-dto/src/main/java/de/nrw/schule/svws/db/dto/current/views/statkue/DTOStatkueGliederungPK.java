package de.nrw.schule.svws.db.dto.current.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_Gliederung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOStatkueGliederungPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Zulässige Schulform der Gliederung */
	public String SF;

	/** Ein Flag (hier zur Kompatibilität vorhanden) */
	public String Flag;

	/** Die Anlage bei einem Bildungsgang des Berufskollegs */
	public String BKAnlage;

	/** Der Typ der Anlage bei einem Bildungsgang des Berufskollegs */
	public String BKTyp;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueGliederungPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStatkueGliederungPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueGliederungPK.
	 * @param SF   der Wert für das Attribut SF
	 * @param Flag   der Wert für das Attribut Flag
	 * @param BKAnlage   der Wert für das Attribut BKAnlage
	 * @param BKTyp   der Wert für das Attribut BKTyp
	 */
	public DTOStatkueGliederungPK(final String SF, final String Flag, final String BKAnlage, final String BKTyp) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (Flag == null) { 
			throw new NullPointerException("Flag must not be null");
		}
		this.Flag = Flag;
		if (BKAnlage == null) { 
			throw new NullPointerException("BKAnlage must not be null");
		}
		this.BKAnlage = BKAnlage;
		if (BKTyp == null) { 
			throw new NullPointerException("BKTyp must not be null");
		}
		this.BKTyp = BKTyp;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueGliederungPK other = (DTOStatkueGliederungPK) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (Flag == null) {
			if (other.Flag != null)
				return false;
		} else if (!Flag.equals(other.Flag))
			return false;

		if (BKAnlage == null) {
			if (other.BKAnlage != null)
				return false;
		} else if (!BKAnlage.equals(other.BKAnlage))
			return false;

		if (BKTyp == null) {
			if (other.BKTyp != null)
				return false;
		} else if (!BKTyp.equals(other.BKTyp))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((Flag == null) ? 0 : Flag.hashCode());

		result = prime * result + ((BKAnlage == null) ? 0 : BKAnlage.hashCode());

		result = prime * result + ((BKTyp == null) ? 0 : BKTyp.hashCode());
		return result;
	}
}