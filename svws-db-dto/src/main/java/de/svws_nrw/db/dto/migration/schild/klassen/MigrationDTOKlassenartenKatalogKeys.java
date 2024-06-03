package de.svws_nrw.db.dto.migration.schild.klassen;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle KlassenartenKatalog_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KlassenartenKatalog_Keys")
@JsonPropertyOrder({"Kuerzel"})
public final class MigrationDTOKlassenartenKatalogKeys {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOKlassenartenKatalogKeys e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOKlassenartenKatalogKeys e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOKlassenartenKatalogKeys e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOKlassenartenKatalogKeys e WHERE e.Kuerzel IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM MigrationDTOKlassenartenKatalogKeys e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM MigrationDTOKlassenartenKatalogKeys e WHERE e.Kuerzel IN ?1";

	/** Das Kürzel der Klassenart */
	@Id
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKlassenartenKatalogKeys ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKlassenartenKatalogKeys() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKlassenartenKatalogKeys ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public MigrationDTOKlassenartenKatalogKeys(final String Kuerzel) {
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOKlassenartenKatalogKeys other = (MigrationDTOKlassenartenKatalogKeys) obj;
		if (Kuerzel == null) {
			if (other.Kuerzel != null)
				return false;
		} else if (!Kuerzel.equals(other.Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Kuerzel == null) ? 0 : Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOKlassenartenKatalogKeys(Kuerzel=" + this.Kuerzel + ")";
	}

}
