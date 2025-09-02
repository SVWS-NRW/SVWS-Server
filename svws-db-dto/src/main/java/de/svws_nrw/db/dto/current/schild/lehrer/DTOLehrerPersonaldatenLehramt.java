package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerPersonaldatenLehramt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerPersonaldatenLehramt")
@JsonPropertyOrder({"ID", "Lehrer_ID", "Lehramt_Katalog_ID", "LehramtAnerkennung_Katalog_ID"})
public final class DTOLehrerPersonaldatenLehramt {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerPersonaldatenLehramt e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehramt_Katalog_ID */
	public static final String QUERY_BY_LEHRAMT_KATALOG_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.Lehramt_Katalog_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehramt_Katalog_ID */
	public static final String QUERY_LIST_BY_LEHRAMT_KATALOG_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.Lehramt_Katalog_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehramtAnerkennung_Katalog_ID */
	public static final String QUERY_BY_LEHRAMTANERKENNUNG_KATALOG_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.LehramtAnerkennung_Katalog_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehramtAnerkennung_Katalog_ID */
	public static final String QUERY_LIST_BY_LEHRAMTANERKENNUNG_KATALOG_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramt e WHERE e.LehramtAnerkennung_Katalog_ID IN ?1";

	/** Eine eindeutige ID für den Eintrag zum Lehramt eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Lehrers zu der das Lehramt gehört */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Die ID des Lehramtes aus dem zugehörigen Statistik-Katalog */
	@Column(name = "Lehramt_Katalog_ID")
	@JsonProperty
	public Long Lehramt_Katalog_ID;

	/** Die ID der Lehramts-Anerkennung aus dem zugehörigen Statistik-Katalog */
	@Column(name = "LehramtAnerkennung_Katalog_ID")
	@JsonProperty
	public Long LehramtAnerkennung_Katalog_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerPersonaldatenLehramt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerPersonaldatenLehramt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerPersonaldatenLehramt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOLehrerPersonaldatenLehramt(final long ID, final long Lehrer_ID) {
		this.ID = ID;
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
		DTOLehrerPersonaldatenLehramt other = (DTOLehrerPersonaldatenLehramt) obj;
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
		return "DTOLehrerPersonaldatenLehramt(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Lehramt_Katalog_ID=" + this.Lehramt_Katalog_ID + ", LehramtAnerkennung_Katalog_ID=" + this.LehramtAnerkennung_Katalog_ID + ")";
	}

}
