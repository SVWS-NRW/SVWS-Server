package de.svws_nrw.db.dto.current.schild.stundenplan;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Schienen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Schienen")
@JsonPropertyOrder({"ID", "Stundenplan_ID", "Jahrgang_ID", "Nummer", "Bezeichnung"})
public final class DTOStundenplanSchienen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundenplanSchienen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundenplanSchienen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundenplanSchienen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundenplanSchienen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundenplanSchienen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundenplanSchienen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundenplan_ID */
	public static final String QUERY_BY_STUNDENPLAN_ID = "SELECT e FROM DTOStundenplanSchienen e WHERE e.Stundenplan_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundenplan_ID */
	public static final String QUERY_LIST_BY_STUNDENPLAN_ID = "SELECT e FROM DTOStundenplanSchienen e WHERE e.Stundenplan_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM DTOStundenplanSchienen e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM DTOStundenplanSchienen e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nummer */
	public static final String QUERY_BY_NUMMER = "SELECT e FROM DTOStundenplanSchienen e WHERE e.Nummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nummer */
	public static final String QUERY_LIST_BY_NUMMER = "SELECT e FROM DTOStundenplanSchienen e WHERE e.Nummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOStundenplanSchienen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOStundenplanSchienen e WHERE e.Bezeichnung IN ?1";

	/** Die ID identifiziert einen Schieneneintrag für einen Stundenplan eindeutig */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Stundenplans, dem dieser Schieneneintrag zugeordnet wird */
	@Column(name = "Stundenplan_ID")
	@JsonProperty
	public long Stundenplan_ID;

	/** ID des Jahrgangs dem die Schiene zugeordnet wird */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** Die Nummer der Schiene */
	@Column(name = "Nummer")
	@JsonProperty
	public int Nummer;

	/** Die Bezeichnung der Schiene */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanSchienen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanSchienen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanSchienen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundenplan_ID   der Wert für das Attribut Stundenplan_ID
	 * @param Nummer   der Wert für das Attribut Nummer
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOStundenplanSchienen(final long ID, final long Stundenplan_ID, final int Nummer, final String Bezeichnung) {
		this.ID = ID;
		this.Stundenplan_ID = Stundenplan_ID;
		this.Nummer = Nummer;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanSchienen other = (DTOStundenplanSchienen) obj;
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
		return "DTOStundenplanSchienen(ID=" + this.ID + ", Stundenplan_ID=" + this.Stundenplan_ID + ", Jahrgang_ID=" + this.Jahrgang_ID + ", Nummer=" + this.Nummer + ", Bezeichnung=" + this.Bezeichnung + ")";
	}

}
