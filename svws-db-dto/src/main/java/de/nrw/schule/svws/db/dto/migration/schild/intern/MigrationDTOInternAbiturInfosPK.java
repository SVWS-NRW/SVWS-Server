package de.nrw.schule.svws.db.dto.migration.schild.intern;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schildintern_AbiturInfos.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOInternAbiturInfosPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	public String AbiFach;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	public String AbiInfoKrz;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	public String Bedingung;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	public String PrfOrdnung;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternAbiturInfosPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternAbiturInfosPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternAbiturInfosPK.
	 * @param AbiFach   der Wert für das Attribut AbiFach
	 * @param AbiInfoKrz   der Wert für das Attribut AbiInfoKrz
	 * @param Bedingung   der Wert für das Attribut Bedingung
	 * @param PrfOrdnung   der Wert für das Attribut PrfOrdnung
	 */
	public MigrationDTOInternAbiturInfosPK(final String AbiFach, final String AbiInfoKrz, final String Bedingung, final String PrfOrdnung) {
		if (AbiFach == null) { 
			throw new NullPointerException("AbiFach must not be null");
		}
		this.AbiFach = AbiFach;
		if (AbiInfoKrz == null) { 
			throw new NullPointerException("AbiInfoKrz must not be null");
		}
		this.AbiInfoKrz = AbiInfoKrz;
		if (Bedingung == null) { 
			throw new NullPointerException("Bedingung must not be null");
		}
		this.Bedingung = Bedingung;
		if (PrfOrdnung == null) { 
			throw new NullPointerException("PrfOrdnung must not be null");
		}
		this.PrfOrdnung = PrfOrdnung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternAbiturInfosPK other = (MigrationDTOInternAbiturInfosPK) obj;
		if (AbiFach == null) {
			if (other.AbiFach != null)
				return false;
		} else if (!AbiFach.equals(other.AbiFach))
			return false;

		if (AbiInfoKrz == null) {
			if (other.AbiInfoKrz != null)
				return false;
		} else if (!AbiInfoKrz.equals(other.AbiInfoKrz))
			return false;

		if (Bedingung == null) {
			if (other.Bedingung != null)
				return false;
		} else if (!Bedingung.equals(other.Bedingung))
			return false;

		if (PrfOrdnung == null) {
			if (other.PrfOrdnung != null)
				return false;
		} else if (!PrfOrdnung.equals(other.PrfOrdnung))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AbiFach == null) ? 0 : AbiFach.hashCode());

		result = prime * result + ((AbiInfoKrz == null) ? 0 : AbiInfoKrz.hashCode());

		result = prime * result + ((Bedingung == null) ? 0 : Bedingung.hashCode());

		result = prime * result + ((PrfOrdnung == null) ? 0 : PrfOrdnung.hashCode());
		return result;
	}
}