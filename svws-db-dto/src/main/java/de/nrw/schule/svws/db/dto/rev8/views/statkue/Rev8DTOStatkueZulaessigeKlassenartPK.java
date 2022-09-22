package de.nrw.schule.svws.db.dto.rev8.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_ZulKlArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOStatkueZulaessigeKlassenartPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Das Kürzel der Klassenart */
	public String KlArt;

	/** Die Schulform bei der die Kursart zulässig ist. */
	public String Schulform;

	/** Eine Einschränkung der Zulässigkeit der Kusart auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben) */
	public String FSP;

	/** Die Bezeichnung der Kursart */
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueZulaessigeKlassenartPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStatkueZulaessigeKlassenartPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueZulaessigeKlassenartPK.
	 * @param KlArt   der Wert für das Attribut KlArt
	 * @param Schulform   der Wert für das Attribut Schulform
	 * @param FSP   der Wert für das Attribut FSP
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public Rev8DTOStatkueZulaessigeKlassenartPK(final String KlArt, final String Schulform, final String FSP, final String Bezeichnung) {
		if (KlArt == null) { 
			throw new NullPointerException("KlArt must not be null");
		}
		this.KlArt = KlArt;
		if (Schulform == null) { 
			throw new NullPointerException("Schulform must not be null");
		}
		this.Schulform = Schulform;
		if (FSP == null) { 
			throw new NullPointerException("FSP must not be null");
		}
		this.FSP = FSP;
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
		Rev8DTOStatkueZulaessigeKlassenartPK other = (Rev8DTOStatkueZulaessigeKlassenartPK) obj;
		if (KlArt == null) {
			if (other.KlArt != null)
				return false;
		} else if (!KlArt.equals(other.KlArt))
			return false;

		if (Schulform == null) {
			if (other.Schulform != null)
				return false;
		} else if (!Schulform.equals(other.Schulform))
			return false;

		if (FSP == null) {
			if (other.FSP != null)
				return false;
		} else if (!FSP.equals(other.FSP))
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
		result = prime * result + ((KlArt == null) ? 0 : KlArt.hashCode());

		result = prime * result + ((Schulform == null) ? 0 : Schulform.hashCode());

		result = prime * result + ((FSP == null) ? 0 : FSP.hashCode());

		result = prime * result + ((Bezeichnung == null) ? 0 : Bezeichnung.hashCode());
		return result;
	}
}