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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_UnterrichtLehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_UnterrichtLehrer")
@JsonPropertyOrder({"ID", "Unterricht_ID", "Lehrer_ID"})
public final class DTOStundenplanUnterrichtLehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundenplanUnterrichtLehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Unterricht_ID */
	public static final String QUERY_BY_UNTERRICHT_ID = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.Unterricht_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Unterricht_ID */
	public static final String QUERY_LIST_BY_UNTERRICHT_ID = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.Unterricht_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOStundenplanUnterrichtLehrer e WHERE e.Lehrer_ID IN ?1";

	/** Die eindeutige ID für die Zuordnung des Lehrer zum Unterricht */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Unterricht-Eintrages im Stundenplan */
	@Column(name = "Unterricht_ID")
	@JsonProperty
	public long Unterricht_ID;

	/** Die ID der unterrichtenden Lehrers. Im Falle von Team-Teaching werden für eine Unterricht-ID einfach mehrere Datensätze erzeugt */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterrichtLehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanUnterrichtLehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanUnterrichtLehrer ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOStundenplanUnterrichtLehrer(final long ID, final long Unterricht_ID, final long Lehrer_ID) {
		this.ID = ID;
		this.Unterricht_ID = Unterricht_ID;
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanUnterrichtLehrer other = (DTOStundenplanUnterrichtLehrer) obj;
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
		return "DTOStundenplanUnterrichtLehrer(ID=" + this.ID + ", Unterricht_ID=" + this.Unterricht_ID + ", Lehrer_ID=" + this.Lehrer_ID + ")";
	}

}
