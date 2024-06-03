package de.svws_nrw.db.dto.migration.schild;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TextExportVorlagen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TextExportVorlagen")
@JsonPropertyOrder({"SchulnrEigner", "VorlageName", "Daten"})
public final class MigrationDTOTextExportVorlagen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOTextExportVorlagen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.VorlageName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.VorlageName IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.VorlageName IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VorlageName */
	public static final String QUERY_BY_VORLAGENAME = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.VorlageName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VorlageName */
	public static final String QUERY_LIST_BY_VORLAGENAME = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.VorlageName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Daten */
	public static final String QUERY_BY_DATEN = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.Daten = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Daten */
	public static final String QUERY_LIST_BY_DATEN = "SELECT e FROM MigrationDTOTextExportVorlagen e WHERE e.Daten IN ?1";

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Name der Export-Textvorlage */
	@Id
	@Column(name = "VorlageName")
	@JsonProperty
	public String VorlageName;

	/** Daten die in der Export-Textvorlage enthalten sind */
	@Column(name = "Daten")
	@JsonProperty
	public String Daten;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTextExportVorlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOTextExportVorlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTextExportVorlagen ohne eine Initialisierung der Attribute.
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param VorlageName   der Wert für das Attribut VorlageName
	 */
	public MigrationDTOTextExportVorlagen(final Integer SchulnrEigner, final String VorlageName) {
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (VorlageName == null) {
			throw new NullPointerException("VorlageName must not be null");
		}
		this.VorlageName = VorlageName;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOTextExportVorlagen other = (MigrationDTOTextExportVorlagen) obj;
		if (VorlageName == null) {
			if (other.VorlageName != null)
				return false;
		} else if (!VorlageName.equals(other.VorlageName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((VorlageName == null) ? 0 : VorlageName.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOTextExportVorlagen(SchulnrEigner=" + this.SchulnrEigner + ", VorlageName=" + this.VorlageName + ", Daten=" + this.Daten + ")";
	}

}
