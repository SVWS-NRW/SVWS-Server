package de.svws_nrw.db.dto.current.schulbewerbung;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulbewerbung_Importe.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulbewerbung_Importe")
@JsonPropertyOrder({"Schueler_ID", "LastSync", "LastXML"})
public final class DTOSchulbewerbungImporte {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchulbewerbungImporte e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LastSync */
	public static final String QUERY_BY_LASTSYNC = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.LastSync = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LastSync */
	public static final String QUERY_LIST_BY_LASTSYNC = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.LastSync IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LastXML */
	public static final String QUERY_BY_LASTXML = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.LastXML = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LastXML */
	public static final String QUERY_LIST_BY_LASTXML = "SELECT e FROM DTOSchulbewerbungImporte e WHERE e.LastXML IN ?1";

	/** Schüler-ID des Schülers, für welchen die Import-Daten speichert werden */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Der Zeitstempel der letzten Synchronisation der Daten mit schulbewerbung.de */
	@Column(name = "LastSync")
	@JsonProperty
	public String LastSync;

	/** Das XML der letzten Synchronisation der Daten mit schulbewerbung.de */
	@Column(name = "LastXML")
	@JsonProperty
	public String LastXML;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulbewerbungImporte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulbewerbungImporte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulbewerbungImporte ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param LastSync   der Wert für das Attribut LastSync
	 * @param LastXML   der Wert für das Attribut LastXML
	 */
	public DTOSchulbewerbungImporte(final long Schueler_ID, final String LastSync, final String LastXML) {
		this.Schueler_ID = Schueler_ID;
		if (LastSync == null) {
			throw new NullPointerException("LastSync must not be null");
		}
		this.LastSync = LastSync;
		if (LastXML == null) {
			throw new NullPointerException("LastXML must not be null");
		}
		this.LastXML = LastXML;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchulbewerbungImporte other = (DTOSchulbewerbungImporte) obj;
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchulbewerbungImporte(Schueler_ID=" + this.Schueler_ID + ", LastSync=" + this.LastSync + ", LastXML=" + this.LastXML + ")";
	}

}
