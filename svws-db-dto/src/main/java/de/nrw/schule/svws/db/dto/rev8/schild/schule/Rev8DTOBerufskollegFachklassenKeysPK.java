package de.nrw.schule.svws.db.dto.rev8.schild.schule;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Berufskolleg_Fachklassen_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOBerufskollegFachklassenKeysPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Der Index, welcher der Fachklasse für die Zuordnung zum Bildungsgang zugeordnet ist */
	public Integer FachklassenIndex;

	/** Der erste Teil des Fachklassenschlüssels (FKS, dreistellig)  */
	public String Schluessel;

	/** Der zweite Teil des Fachklassenschlüssels (AP, zweistellig) */
	public String Schluessel2;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOBerufskollegFachklassenKeysPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOBerufskollegFachklassenKeysPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOBerufskollegFachklassenKeysPK.
	 * @param FachklassenIndex   der Wert für das Attribut FachklassenIndex
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 * @param Schluessel2   der Wert für das Attribut Schluessel2
	 */
	public Rev8DTOBerufskollegFachklassenKeysPK(final Integer FachklassenIndex, final String Schluessel, final String Schluessel2) {
		if (FachklassenIndex == null) { 
			throw new NullPointerException("FachklassenIndex must not be null");
		}
		this.FachklassenIndex = FachklassenIndex;
		if (Schluessel == null) { 
			throw new NullPointerException("Schluessel must not be null");
		}
		this.Schluessel = Schluessel;
		if (Schluessel2 == null) { 
			throw new NullPointerException("Schluessel2 must not be null");
		}
		this.Schluessel2 = Schluessel2;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOBerufskollegFachklassenKeysPK other = (Rev8DTOBerufskollegFachklassenKeysPK) obj;
		if (FachklassenIndex == null) {
			if (other.FachklassenIndex != null)
				return false;
		} else if (!FachklassenIndex.equals(other.FachklassenIndex))
			return false;

		if (Schluessel == null) {
			if (other.Schluessel != null)
				return false;
		} else if (!Schluessel.equals(other.Schluessel))
			return false;

		if (Schluessel2 == null) {
			if (other.Schluessel2 != null)
				return false;
		} else if (!Schluessel2.equals(other.Schluessel2))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FachklassenIndex == null) ? 0 : FachklassenIndex.hashCode());

		result = prime * result + ((Schluessel == null) ? 0 : Schluessel.hashCode());

		result = prime * result + ((Schluessel2 == null) ? 0 : Schluessel2.hashCode());
		return result;
	}
}