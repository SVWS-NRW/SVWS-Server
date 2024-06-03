package de.svws_nrw.db.dto.migration.schema;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schema_AutoInkremente.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schema_AutoInkremente")
@JsonPropertyOrder({"NameTabelle", "MaxID"})
public final class MigrationDTOSchemaAutoInkremente {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchemaAutoInkremente e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchemaAutoInkremente e WHERE e.NameTabelle = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchemaAutoInkremente e WHERE e.NameTabelle IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchemaAutoInkremente e WHERE e.NameTabelle IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NameTabelle */
	public static final String QUERY_BY_NAMETABELLE = "SELECT e FROM MigrationDTOSchemaAutoInkremente e WHERE e.NameTabelle = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NameTabelle */
	public static final String QUERY_LIST_BY_NAMETABELLE = "SELECT e FROM MigrationDTOSchemaAutoInkremente e WHERE e.NameTabelle IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MaxID */
	public static final String QUERY_BY_MAXID = "SELECT e FROM MigrationDTOSchemaAutoInkremente e WHERE e.MaxID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MaxID */
	public static final String QUERY_LIST_BY_MAXID = "SELECT e FROM MigrationDTOSchemaAutoInkremente e WHERE e.MaxID IN ?1";

	/** Gibt den Tabellennamen an, für dessen Auto-Inkrement der ID-Wert verwendet werden soll. */
	@Id
	@Column(name = "NameTabelle")
	@JsonProperty
	public String NameTabelle;

	/** Die ID des höchsten jemals in die DB geschriebenen ID-Wertes bei der zugehörigen Tabelle */
	@Column(name = "MaxID")
	@JsonProperty
	public Long MaxID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchemaAutoInkremente ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchemaAutoInkremente() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchemaAutoInkremente ohne eine Initialisierung der Attribute.
	 * @param NameTabelle   der Wert für das Attribut NameTabelle
	 * @param MaxID   der Wert für das Attribut MaxID
	 */
	public MigrationDTOSchemaAutoInkremente(final String NameTabelle, final Long MaxID) {
		if (NameTabelle == null) {
			throw new NullPointerException("NameTabelle must not be null");
		}
		this.NameTabelle = NameTabelle;
		if (MaxID == null) {
			throw new NullPointerException("MaxID must not be null");
		}
		this.MaxID = MaxID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchemaAutoInkremente other = (MigrationDTOSchemaAutoInkremente) obj;
		if (NameTabelle == null) {
			if (other.NameTabelle != null)
				return false;
		} else if (!NameTabelle.equals(other.NameTabelle))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((NameTabelle == null) ? 0 : NameTabelle.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchemaAutoInkremente(NameTabelle=" + this.NameTabelle + ", MaxID=" + this.MaxID + ")";
	}

}
