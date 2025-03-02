package de.svws_nrw.db.dto.current.schild.kurse;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Kurs_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public final class DTOKursSchuelerPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die eindeutige ID des Kurses – verweist auf den Kurs */
	public long Kurs_ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schüler */
	public long Schueler_ID;

	/** Wird für Wiederholungen im Laufenden Schuljahresabschnitt genutzt 0=aktueller/neuester Lernabschnitt 1=vor dem ersten Wechsel 2=vor dem zweiten Wechsel usw */
	public Integer LernabschnittWechselNr;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursSchuelerPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKursSchuelerPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursSchuelerPK.
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param LernabschnittWechselNr   der Wert für das Attribut LernabschnittWechselNr
	 */
	public DTOKursSchuelerPK(final long Kurs_ID, final long Schueler_ID, final Integer LernabschnittWechselNr) {
		this.Kurs_ID = Kurs_ID;
		this.Schueler_ID = Schueler_ID;
		if (LernabschnittWechselNr == null) {
			throw new NullPointerException("LernabschnittWechselNr must not be null");
		}
		this.LernabschnittWechselNr = LernabschnittWechselNr;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKursSchuelerPK other = (DTOKursSchuelerPK) obj;
		if (Kurs_ID != other.Kurs_ID)
			return false;
		if (Schueler_ID != other.Schueler_ID)
			return false;
		if (LernabschnittWechselNr == null) {
			if (other.LernabschnittWechselNr != null)
				return false;
		} else if (!LernabschnittWechselNr.equals(other.LernabschnittWechselNr))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Kurs_ID);

		result = prime * result + Long.hashCode(Schueler_ID);

		result = prime * result + ((LernabschnittWechselNr == null) ? 0 : LernabschnittWechselNr.hashCode());
		return result;
	}
}
