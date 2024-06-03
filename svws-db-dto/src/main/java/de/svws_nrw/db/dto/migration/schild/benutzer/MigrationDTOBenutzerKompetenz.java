package de.svws_nrw.db.dto.migration.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzerKompetenzen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOBenutzerKompetenzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzerKompetenzen")
@JsonPropertyOrder({"Benutzer_ID", "Kompetenz_ID"})
public final class MigrationDTOBenutzerKompetenz {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOBenutzerKompetenz e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOBenutzerKompetenz e WHERE e.Benutzer_ID = ?1 AND e.Kompetenz_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOBenutzerKompetenz e WHERE e.Benutzer_ID IS NOT NULL AND e.Kompetenz_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzer_ID */
	public static final String QUERY_BY_BENUTZER_ID = "SELECT e FROM MigrationDTOBenutzerKompetenz e WHERE e.Benutzer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzer_ID */
	public static final String QUERY_LIST_BY_BENUTZER_ID = "SELECT e FROM MigrationDTOBenutzerKompetenz e WHERE e.Benutzer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kompetenz_ID */
	public static final String QUERY_BY_KOMPETENZ_ID = "SELECT e FROM MigrationDTOBenutzerKompetenz e WHERE e.Kompetenz_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kompetenz_ID */
	public static final String QUERY_LIST_BY_KOMPETENZ_ID = "SELECT e FROM MigrationDTOBenutzerKompetenz e WHERE e.Kompetenz_ID IN ?1";

	/** Die ID des Benutzers */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

	/** Die ID der zugeordneten Kompetenz */
	@Id
	@Column(name = "Kompetenz_ID")
	@JsonProperty
	public Long Kompetenz_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzerKompetenz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBenutzerKompetenz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzerKompetenz ohne eine Initialisierung der Attribute.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param Kompetenz_ID   der Wert für das Attribut Kompetenz_ID
	 */
	public MigrationDTOBenutzerKompetenz(final Long Benutzer_ID, final Long Kompetenz_ID) {
		if (Benutzer_ID == null) {
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
		if (Kompetenz_ID == null) {
			throw new NullPointerException("Kompetenz_ID must not be null");
		}
		this.Kompetenz_ID = Kompetenz_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBenutzerKompetenz other = (MigrationDTOBenutzerKompetenz) obj;
		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
			return false;
		if (Kompetenz_ID == null) {
			if (other.Kompetenz_ID != null)
				return false;
		} else if (!Kompetenz_ID.equals(other.Kompetenz_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());

		result = prime * result + ((Kompetenz_ID == null) ? 0 : Kompetenz_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOBenutzerKompetenz(Benutzer_ID=" + this.Benutzer_ID + ", Kompetenz_ID=" + this.Kompetenz_ID + ")";
	}

}
