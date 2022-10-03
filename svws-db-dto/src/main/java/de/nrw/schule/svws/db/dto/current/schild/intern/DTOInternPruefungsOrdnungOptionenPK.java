package de.nrw.schule.svws.db.dto.current.schild.intern;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schildintern_PruefOrd_Optionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOInternPruefungsOrdnungOptionenPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schildintern Tabelle: Schulformen für die die Optionen gelten */
	public String OP_Schulformen;

	/** Schildintern Tabelle: Kürzel der Prüfungsordung */
	public String OP_POKrz;

	/** Schildintern Tabelle: Angezeigter Kurztext in Schild-NRW */
	public String OP_Krz;

	/** Schildintern Tabelle: A=Allgemein B=Berufsbildend */
	public String OP_Art;

	/** Schildintern Tabelle: Bildungsgang A oder B */
	public String OP_Bildungsgang;

	/** Schildintern Tabelle: Text des Abschlusses */
	public String OP_Name;

	/** Schildintern Tabelle: zulässig für diese Jahrgänge */
	public String OP_Jahrgaenge;

	/** Schildintern Tabelle: Reihenfolge */
	public Integer OP_Reihenfolge;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternPruefungsOrdnungOptionenPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternPruefungsOrdnungOptionenPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternPruefungsOrdnungOptionenPK.
	 * @param OP_Schulformen   der Wert für das Attribut OP_Schulformen
	 * @param OP_POKrz   der Wert für das Attribut OP_POKrz
	 * @param OP_Krz   der Wert für das Attribut OP_Krz
	 * @param OP_Art   der Wert für das Attribut OP_Art
	 * @param OP_Bildungsgang   der Wert für das Attribut OP_Bildungsgang
	 * @param OP_Name   der Wert für das Attribut OP_Name
	 * @param OP_Jahrgaenge   der Wert für das Attribut OP_Jahrgaenge
	 * @param OP_Reihenfolge   der Wert für das Attribut OP_Reihenfolge
	 */
	public DTOInternPruefungsOrdnungOptionenPK(final String OP_Schulformen, final String OP_POKrz, final String OP_Krz, final String OP_Art, final String OP_Bildungsgang, final String OP_Name, final String OP_Jahrgaenge, final Integer OP_Reihenfolge) {
		if (OP_Schulformen == null) { 
			throw new NullPointerException("OP_Schulformen must not be null");
		}
		this.OP_Schulformen = OP_Schulformen;
		if (OP_POKrz == null) { 
			throw new NullPointerException("OP_POKrz must not be null");
		}
		this.OP_POKrz = OP_POKrz;
		if (OP_Krz == null) { 
			throw new NullPointerException("OP_Krz must not be null");
		}
		this.OP_Krz = OP_Krz;
		if (OP_Art == null) { 
			throw new NullPointerException("OP_Art must not be null");
		}
		this.OP_Art = OP_Art;
		if (OP_Bildungsgang == null) { 
			throw new NullPointerException("OP_Bildungsgang must not be null");
		}
		this.OP_Bildungsgang = OP_Bildungsgang;
		if (OP_Name == null) { 
			throw new NullPointerException("OP_Name must not be null");
		}
		this.OP_Name = OP_Name;
		if (OP_Jahrgaenge == null) { 
			throw new NullPointerException("OP_Jahrgaenge must not be null");
		}
		this.OP_Jahrgaenge = OP_Jahrgaenge;
		if (OP_Reihenfolge == null) { 
			throw new NullPointerException("OP_Reihenfolge must not be null");
		}
		this.OP_Reihenfolge = OP_Reihenfolge;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInternPruefungsOrdnungOptionenPK other = (DTOInternPruefungsOrdnungOptionenPK) obj;
		if (OP_Schulformen == null) {
			if (other.OP_Schulformen != null)
				return false;
		} else if (!OP_Schulformen.equals(other.OP_Schulformen))
			return false;

		if (OP_POKrz == null) {
			if (other.OP_POKrz != null)
				return false;
		} else if (!OP_POKrz.equals(other.OP_POKrz))
			return false;

		if (OP_Krz == null) {
			if (other.OP_Krz != null)
				return false;
		} else if (!OP_Krz.equals(other.OP_Krz))
			return false;

		if (OP_Art == null) {
			if (other.OP_Art != null)
				return false;
		} else if (!OP_Art.equals(other.OP_Art))
			return false;

		if (OP_Bildungsgang == null) {
			if (other.OP_Bildungsgang != null)
				return false;
		} else if (!OP_Bildungsgang.equals(other.OP_Bildungsgang))
			return false;

		if (OP_Name == null) {
			if (other.OP_Name != null)
				return false;
		} else if (!OP_Name.equals(other.OP_Name))
			return false;

		if (OP_Jahrgaenge == null) {
			if (other.OP_Jahrgaenge != null)
				return false;
		} else if (!OP_Jahrgaenge.equals(other.OP_Jahrgaenge))
			return false;

		if (OP_Reihenfolge == null) {
			if (other.OP_Reihenfolge != null)
				return false;
		} else if (!OP_Reihenfolge.equals(other.OP_Reihenfolge))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((OP_Schulformen == null) ? 0 : OP_Schulformen.hashCode());

		result = prime * result + ((OP_POKrz == null) ? 0 : OP_POKrz.hashCode());

		result = prime * result + ((OP_Krz == null) ? 0 : OP_Krz.hashCode());

		result = prime * result + ((OP_Art == null) ? 0 : OP_Art.hashCode());

		result = prime * result + ((OP_Bildungsgang == null) ? 0 : OP_Bildungsgang.hashCode());

		result = prime * result + ((OP_Name == null) ? 0 : OP_Name.hashCode());

		result = prime * result + ((OP_Jahrgaenge == null) ? 0 : OP_Jahrgaenge.hashCode());

		result = prime * result + ((OP_Reihenfolge == null) ? 0 : OP_Reihenfolge.hashCode());
		return result;
	}
}