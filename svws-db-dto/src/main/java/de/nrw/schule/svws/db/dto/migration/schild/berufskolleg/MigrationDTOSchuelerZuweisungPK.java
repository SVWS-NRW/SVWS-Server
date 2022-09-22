package de.nrw.schule.svws.db.dto.migration.schild.berufskolleg;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle SchuelerZuweisungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOSchuelerZuweisungPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** LernabschnittsID  der Zuweisung (E G Kurse GE und PS SK) */
	public Long Abschnitt_ID;

	/** FachID der Zuweisung */
	public Long Fach_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerZuweisungPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerZuweisungPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerZuweisungPK.
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public MigrationDTOSchuelerZuweisungPK(final Long Abschnitt_ID, final Long Fach_ID) {
		if (Abschnitt_ID == null) { 
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerZuweisungPK other = (MigrationDTOSchuelerZuweisungPK) obj;
		if (Abschnitt_ID == null) {
			if (other.Abschnitt_ID != null)
				return false;
		} else if (!Abschnitt_ID.equals(other.Abschnitt_ID))
			return false;

		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Abschnitt_ID == null) ? 0 : Abschnitt_ID.hashCode());

		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());
		return result;
	}
}