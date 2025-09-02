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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerPersonaldatenLehramtLehrbefaehigung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerPersonaldatenLehramtLehrbefaehigung")
@JsonPropertyOrder({"ID", "Lehreramt_ID", "Lehrbefaehigung_Katalog_ID", "LehrbefaehigungAnerkennung_Katalog_ID"})
public final class DTOLehrerPersonaldatenLehramtBefaehigung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehreramt_ID */
	public static final String QUERY_BY_LEHRERAMT_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.Lehreramt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehreramt_ID */
	public static final String QUERY_LIST_BY_LEHRERAMT_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.Lehreramt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrbefaehigung_Katalog_ID */
	public static final String QUERY_BY_LEHRBEFAEHIGUNG_KATALOG_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.Lehrbefaehigung_Katalog_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrbefaehigung_Katalog_ID */
	public static final String QUERY_LIST_BY_LEHRBEFAEHIGUNG_KATALOG_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.Lehrbefaehigung_Katalog_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrbefaehigungAnerkennung_Katalog_ID */
	public static final String QUERY_BY_LEHRBEFAEHIGUNGANERKENNUNG_KATALOG_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.LehrbefaehigungAnerkennung_Katalog_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrbefaehigungAnerkennung_Katalog_ID */
	public static final String QUERY_LIST_BY_LEHRBEFAEHIGUNGANERKENNUNG_KATALOG_ID = "SELECT e FROM DTOLehrerPersonaldatenLehramtBefaehigung e WHERE e.LehrbefaehigungAnerkennung_Katalog_ID IN ?1";

	/** Eine eindeutige ID für den Eintrag zu der Lehrbefähigung zu einem Lehramt eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Lehramtseintrags des Lehrers zu der die Lehrbefähigung gehört */
	@Column(name = "Lehreramt_ID")
	@JsonProperty
	public long Lehreramt_ID;

	/** Die ID der Lehrbefähigung aus dem zugehörigen Statistik-Katalog */
	@Column(name = "Lehrbefaehigung_Katalog_ID")
	@JsonProperty
	public long Lehrbefaehigung_Katalog_ID;

	/** Die ID des Anerkennungsgrundes für die Lehrbefähigung des Lehrers aus dem zugehörigen Statistik-Katalog */
	@Column(name = "LehrbefaehigungAnerkennung_Katalog_ID")
	@JsonProperty
	public Long LehrbefaehigungAnerkennung_Katalog_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerPersonaldatenLehramtBefaehigung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerPersonaldatenLehramtBefaehigung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerPersonaldatenLehramtBefaehigung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehreramt_ID   der Wert für das Attribut Lehreramt_ID
	 * @param Lehrbefaehigung_Katalog_ID   der Wert für das Attribut Lehrbefaehigung_Katalog_ID
	 */
	public DTOLehrerPersonaldatenLehramtBefaehigung(final long ID, final long Lehreramt_ID, final long Lehrbefaehigung_Katalog_ID) {
		this.ID = ID;
		this.Lehreramt_ID = Lehreramt_ID;
		this.Lehrbefaehigung_Katalog_ID = Lehrbefaehigung_Katalog_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerPersonaldatenLehramtBefaehigung other = (DTOLehrerPersonaldatenLehramtBefaehigung) obj;
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
		return "DTOLehrerPersonaldatenLehramtBefaehigung(ID=" + this.ID + ", Lehreramt_ID=" + this.Lehreramt_ID + ", Lehrbefaehigung_Katalog_ID=" + this.Lehrbefaehigung_Katalog_ID + ", LehrbefaehigungAnerkennung_Katalog_ID=" + this.LehrbefaehigungAnerkennung_Katalog_ID + ")";
	}

}
