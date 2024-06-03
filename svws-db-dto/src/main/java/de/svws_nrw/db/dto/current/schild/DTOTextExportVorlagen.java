package de.svws_nrw.db.dto.current.schild;

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
@JsonPropertyOrder({"VorlageName", "Daten"})
public final class DTOTextExportVorlagen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTextExportVorlagen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VorlageName */
	public static final String QUERY_BY_VORLAGENAME = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VorlageName */
	public static final String QUERY_LIST_BY_VORLAGENAME = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Daten */
	public static final String QUERY_BY_DATEN = "SELECT e FROM DTOTextExportVorlagen e WHERE e.Daten = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Daten */
	public static final String QUERY_LIST_BY_DATEN = "SELECT e FROM DTOTextExportVorlagen e WHERE e.Daten IN ?1";

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
	 * Erstellt ein neues Objekt der Klasse DTOTextExportVorlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTextExportVorlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTextExportVorlagen ohne eine Initialisierung der Attribute.
	 * @param VorlageName   der Wert für das Attribut VorlageName
	 */
	public DTOTextExportVorlagen(final String VorlageName) {
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
		DTOTextExportVorlagen other = (DTOTextExportVorlagen) obj;
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
		return "DTOTextExportVorlagen(VorlageName=" + this.VorlageName + ", Daten=" + this.Daten + ")";
	}

}
