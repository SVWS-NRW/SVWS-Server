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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Raeume.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Raeume")
@JsonPropertyOrder({"ID", "Termin_ID", "Stundenplan_Raum_ID", "Stundenplan_Raum_Kuerzel", "Bemerkungen"})
public final class DTOGostKlausurenRaeume {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostKlausurenRaeume e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Termin_ID */
	public static final String QUERY_BY_TERMIN_ID = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Termin_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Termin_ID */
	public static final String QUERY_LIST_BY_TERMIN_ID = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Termin_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundenplan_Raum_ID */
	public static final String QUERY_BY_STUNDENPLAN_RAUM_ID = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Stundenplan_Raum_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundenplan_Raum_ID */
	public static final String QUERY_LIST_BY_STUNDENPLAN_RAUM_ID = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Stundenplan_Raum_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundenplan_Raum_Kuerzel */
	public static final String QUERY_BY_STUNDENPLAN_RAUM_KUERZEL = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Stundenplan_Raum_Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundenplan_Raum_Kuerzel */
	public static final String QUERY_LIST_BY_STUNDENPLAN_RAUM_KUERZEL = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Stundenplan_Raum_Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenRaeume e WHERE e.Bemerkungen IN ?1";

	/** ID des Klausurraums (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Termins */
	@Column(name = "Termin_ID")
	@JsonProperty
	public long Termin_ID;

	/** ID des Raums aus der Tabelle Stundenplan_Raeume */
	@Column(name = "Stundenplan_Raum_ID")
	@JsonProperty
	public Long Stundenplan_Raum_ID;

	/** Das Kürzel des Stundenplan_Raums, falls keine Stundenplan_Raum_ID gesetzt ist */
	@Column(name = "Stundenplan_Raum_Kuerzel")
	@JsonProperty
	public String Stundenplan_Raum_Kuerzel;

	/** Text für Bemerkungen zum Klausurraum */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeume ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenRaeume() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeume ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Termin_ID   der Wert für das Attribut Termin_ID
	 */
	public DTOGostKlausurenRaeume(final long ID, final long Termin_ID) {
		this.ID = ID;
		this.Termin_ID = Termin_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenRaeume other = (DTOGostKlausurenRaeume) obj;
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
		return "DTOGostKlausurenRaeume(ID=" + this.ID + ", Termin_ID=" + this.Termin_ID + ", Stundenplan_Raum_ID=" + this.Stundenplan_Raum_ID + ", Stundenplan_Raum_Kuerzel=" + this.Stundenplan_Raum_Kuerzel + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
