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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Raumstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Raumstunden")
@JsonPropertyOrder({"ID", "Klausurraum_ID", "Zeitraster_ID"})
public final class DTOGostKlausurenRaumstunden {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostKlausurenRaumstunden e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klausurraum_ID */
	public static final String QUERY_BY_KLAUSURRAUM_ID = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.Klausurraum_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klausurraum_ID */
	public static final String QUERY_LIST_BY_KLAUSURRAUM_ID = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.Klausurraum_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zeitraster_ID */
	public static final String QUERY_BY_ZEITRASTER_ID = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.Zeitraster_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zeitraster_ID */
	public static final String QUERY_LIST_BY_ZEITRASTER_ID = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.Zeitraster_ID IN ?1";

	/** ID der Raumstunde (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Klausurraums */
	@Column(name = "Klausurraum_ID")
	@JsonProperty
	public long Klausurraum_ID;

	/** ID des Zeitrasters */
	@Column(name = "Zeitraster_ID")
	@JsonProperty
	public long Zeitraster_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaumstunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenRaumstunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaumstunden ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Klausurraum_ID   der Wert für das Attribut Klausurraum_ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 */
	public DTOGostKlausurenRaumstunden(final long ID, final long Klausurraum_ID, final long Zeitraster_ID) {
		this.ID = ID;
		this.Klausurraum_ID = Klausurraum_ID;
		this.Zeitraster_ID = Zeitraster_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenRaumstunden other = (DTOGostKlausurenRaumstunden) obj;
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
		return "DTOGostKlausurenRaumstunden(ID=" + this.ID + ", Klausurraum_ID=" + this.Klausurraum_ID + ", Zeitraster_ID=" + this.Zeitraster_ID + ")";
	}

}
