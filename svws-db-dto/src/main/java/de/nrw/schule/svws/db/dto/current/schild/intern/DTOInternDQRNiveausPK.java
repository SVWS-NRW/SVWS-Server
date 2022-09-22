package de.nrw.schule.svws.db.dto.current.schild.intern;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schildintern_DQR_Niveaus.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOInternDQRNiveausPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schildintern Tabelle: DQR-Niveau für Gliederung */
	public String Gliederung;

	/** Schildintern Tabelle: DQR-Niveau für die FAchklasse */
	public String FKS;

	/** Schildintern Tabelle: DQR-Niveau als Nummer */
	public Integer DQR_Niveau;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternDQRNiveausPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternDQRNiveausPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternDQRNiveausPK.
	 * @param Gliederung   der Wert für das Attribut Gliederung
	 * @param FKS   der Wert für das Attribut FKS
	 * @param DQR_Niveau   der Wert für das Attribut DQR_Niveau
	 */
	public DTOInternDQRNiveausPK(final String Gliederung, final String FKS, final Integer DQR_Niveau) {
		if (Gliederung == null) { 
			throw new NullPointerException("Gliederung must not be null");
		}
		this.Gliederung = Gliederung;
		if (FKS == null) { 
			throw new NullPointerException("FKS must not be null");
		}
		this.FKS = FKS;
		if (DQR_Niveau == null) { 
			throw new NullPointerException("DQR_Niveau must not be null");
		}
		this.DQR_Niveau = DQR_Niveau;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInternDQRNiveausPK other = (DTOInternDQRNiveausPK) obj;
		if (Gliederung == null) {
			if (other.Gliederung != null)
				return false;
		} else if (!Gliederung.equals(other.Gliederung))
			return false;

		if (FKS == null) {
			if (other.FKS != null)
				return false;
		} else if (!FKS.equals(other.FKS))
			return false;

		if (DQR_Niveau == null) {
			if (other.DQR_Niveau != null)
				return false;
		} else if (!DQR_Niveau.equals(other.DQR_Niveau))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Gliederung == null) ? 0 : Gliederung.hashCode());

		result = prime * result + ((FKS == null) ? 0 : FKS.hashCode());

		result = prime * result + ((DQR_Niveau == null) ? 0 : DQR_Niveau.hashCode());
		return result;
	}
}