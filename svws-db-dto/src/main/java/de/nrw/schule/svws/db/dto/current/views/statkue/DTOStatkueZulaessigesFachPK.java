package de.nrw.schule.svws.db.dto.current.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_ZulFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOStatkueZulaessigesFachPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die Schulform bei der das Fach zulässig ist. */
	public String Schulform;

	/** Eine Einschränkung der Zulässigkeit des Faches auf einen Bildungsgang */
	public String BG;

	/** Das Kürzel für die amtliche Schulstatistik */
	public String Fach;

	/** Die Bezeichnung des Faches */
	public String Bezeichnung;

	/** Flag - zur Kompatibilität (hier 1) */
	public String Flag;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueZulaessigesFachPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStatkueZulaessigesFachPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueZulaessigesFachPK.
	 * @param Schulform   der Wert für das Attribut Schulform
	 * @param BG   der Wert für das Attribut BG
	 * @param Fach   der Wert für das Attribut Fach
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Flag   der Wert für das Attribut Flag
	 */
	public DTOStatkueZulaessigesFachPK(final String Schulform, final String BG, final String Fach, final String Bezeichnung, final String Flag) {
		if (Schulform == null) { 
			throw new NullPointerException("Schulform must not be null");
		}
		this.Schulform = Schulform;
		if (BG == null) { 
			throw new NullPointerException("BG must not be null");
		}
		this.BG = BG;
		if (Fach == null) { 
			throw new NullPointerException("Fach must not be null");
		}
		this.Fach = Fach;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Flag == null) { 
			throw new NullPointerException("Flag must not be null");
		}
		this.Flag = Flag;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueZulaessigesFachPK other = (DTOStatkueZulaessigesFachPK) obj;
		if (Schulform == null) {
			if (other.Schulform != null)
				return false;
		} else if (!Schulform.equals(other.Schulform))
			return false;

		if (BG == null) {
			if (other.BG != null)
				return false;
		} else if (!BG.equals(other.BG))
			return false;

		if (Fach == null) {
			if (other.Fach != null)
				return false;
		} else if (!Fach.equals(other.Fach))
			return false;

		if (Bezeichnung == null) {
			if (other.Bezeichnung != null)
				return false;
		} else if (!Bezeichnung.equals(other.Bezeichnung))
			return false;

		if (Flag == null) {
			if (other.Flag != null)
				return false;
		} else if (!Flag.equals(other.Flag))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schulform == null) ? 0 : Schulform.hashCode());

		result = prime * result + ((BG == null) ? 0 : BG.hashCode());

		result = prime * result + ((Fach == null) ? 0 : Fach.hashCode());

		result = prime * result + ((Bezeichnung == null) ? 0 : Bezeichnung.hashCode());

		result = prime * result + ((Flag == null) ? 0 : Flag.hashCode());
		return result;
	}
}