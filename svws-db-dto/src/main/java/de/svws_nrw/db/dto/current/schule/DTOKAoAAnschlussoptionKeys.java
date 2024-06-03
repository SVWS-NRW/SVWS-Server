package de.svws_nrw.db.dto.current.schule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle KAoA_Anschlussoption_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KAoA_Anschlussoption_Keys")
@JsonPropertyOrder({"ID"})
public final class DTOKAoAAnschlussoptionKeys {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKAoAAnschlussoptionKeys e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKAoAAnschlussoptionKeys e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOKAoAAnschlussoptionKeys e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKAoAAnschlussoptionKeys e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOKAoAAnschlussoptionKeys e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOKAoAAnschlussoptionKeys e WHERE e.ID IN ?1";

	/** Die eindeutige ID der Anschlussoption */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKAoAAnschlussoptionKeys ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKAoAAnschlussoptionKeys() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKAoAAnschlussoptionKeys ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOKAoAAnschlussoptionKeys(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKAoAAnschlussoptionKeys other = (DTOKAoAAnschlussoptionKeys) obj;
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
		return "DTOKAoAAnschlussoptionKeys(ID=" + this.ID + ")";
	}

}
