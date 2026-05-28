package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Klassen_Lehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Klassen_Lehrer")
@JsonPropertyOrder({"ID", "Planungsabschnitt_ID", "Klasse_ID", "Lehrer_ID", "Reihenfolge"})
public final class DTOUvKlassenLehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvKlassenLehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klasse_ID */
	public static final String QUERY_BY_KLASSE_ID = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.Klasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klasse_ID */
	public static final String QUERY_LIST_BY_KLASSE_ID = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.Klasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Reihenfolge */
	public static final String QUERY_BY_REIHENFOLGE = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.Reihenfolge = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Reihenfolge */
	public static final String QUERY_LIST_BY_REIHENFOLGE = "SELECT e FROM DTOUvKlassenLehrer e WHERE e.Reihenfolge IN ?1";

	/** ID des Klassen-Lehrers (generiert, planungsspezifisch) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/** ID der UV_Klasse, welcher der Klassenlehrer zugeordnet ist */
	@Column(name = "Klasse_ID")
	@JsonProperty
	public long Klasse_ID;

	/** ID des Lehrers, welcher der Klasse als Klassenlehrer zugeordnet ist */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Eine Reihenfolge für die Klassenlehrer, z.B. zur Unterscheidung von Klassenleitung (1) und stellvertretender Klassenleitung (2) */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public int Reihenfolge;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvKlassenLehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvKlassenLehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvKlassenLehrer ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Klasse_ID   der Wert für das Attribut Klasse_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Reihenfolge   der Wert für das Attribut Reihenfolge
	 */
	public DTOUvKlassenLehrer(final long ID, final long Planungsabschnitt_ID, final long Klasse_ID, final long Lehrer_ID, final int Reihenfolge) {
		this.ID = ID;
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Klasse_ID = Klasse_ID;
		this.Lehrer_ID = Lehrer_ID;
		this.Reihenfolge = Reihenfolge;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOUvKlassenLehrer other = (DTOUvKlassenLehrer) obj;
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
		return "DTOUvKlassenLehrer(ID=" + this.ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ", Klasse_ID=" + this.Klasse_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Reihenfolge=" + this.Reihenfolge + ")";
	}

}
