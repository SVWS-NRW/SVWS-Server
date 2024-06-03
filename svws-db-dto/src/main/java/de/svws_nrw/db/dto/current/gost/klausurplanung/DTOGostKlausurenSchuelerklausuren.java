package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Schuelerklausuren.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Schuelerklausuren")
@JsonPropertyOrder({"ID", "Kursklausur_ID", "Schueler_ID", "Bemerkungen"})
public final class DTOGostKlausurenSchuelerklausuren {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursklausur_ID */
	public static final String QUERY_BY_KURSKLAUSUR_ID = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Kursklausur_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursklausur_ID */
	public static final String QUERY_LIST_BY_KURSKLAUSUR_ID = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Kursklausur_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Bemerkungen IN ?1";

	/** ID der Klausurvorgaben (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Kursklausur */
	@Column(name = "Kursklausur_ID")
	@JsonProperty
	public long Kursklausur_ID;

	/** ID des Schülers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Text für Bemerkungen zur Schuelerklausur */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausuren ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenSchuelerklausuren() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausuren ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kursklausur_ID   der Wert für das Attribut Kursklausur_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOGostKlausurenSchuelerklausuren(final long ID, final long Kursklausur_ID, final long Schueler_ID) {
		this.ID = ID;
		this.Kursklausur_ID = Kursklausur_ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenSchuelerklausuren other = (DTOGostKlausurenSchuelerklausuren) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostKlausurenSchuelerklausuren(ID=" + this.ID + ", Kursklausur_ID=" + this.Kursklausur_ID + ", Schueler_ID=" + this.Schueler_ID + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
