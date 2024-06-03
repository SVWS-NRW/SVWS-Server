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
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzergruppenMitglieder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOBenutzergruppenMitgliedPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzergruppenMitglieder")
@JsonPropertyOrder({"Gruppe_ID", "Benutzer_ID"})
public final class MigrationDTOBenutzergruppenMitglied {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOBenutzergruppenMitglied e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOBenutzergruppenMitglied e WHERE e.Gruppe_ID = ?1 AND e.Benutzer_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOBenutzergruppenMitglied e WHERE e.Gruppe_ID IS NOT NULL AND e.Benutzer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gruppe_ID */
	public static final String QUERY_BY_GRUPPE_ID = "SELECT e FROM MigrationDTOBenutzergruppenMitglied e WHERE e.Gruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gruppe_ID */
	public static final String QUERY_LIST_BY_GRUPPE_ID = "SELECT e FROM MigrationDTOBenutzergruppenMitglied e WHERE e.Gruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzer_ID */
	public static final String QUERY_BY_BENUTZER_ID = "SELECT e FROM MigrationDTOBenutzergruppenMitglied e WHERE e.Benutzer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzer_ID */
	public static final String QUERY_LIST_BY_BENUTZER_ID = "SELECT e FROM MigrationDTOBenutzergruppenMitglied e WHERE e.Benutzer_ID IN ?1";

	/** Die ID der Benutzergruppe */
	@Id
	@Column(name = "Gruppe_ID")
	@JsonProperty
	public Long Gruppe_ID;

	/** Die ID des Benutzers */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzergruppenMitglied ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBenutzergruppenMitglied() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzergruppenMitglied ohne eine Initialisierung der Attribute.
	 * @param Gruppe_ID   der Wert für das Attribut Gruppe_ID
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 */
	public MigrationDTOBenutzergruppenMitglied(final Long Gruppe_ID, final Long Benutzer_ID) {
		if (Gruppe_ID == null) {
			throw new NullPointerException("Gruppe_ID must not be null");
		}
		this.Gruppe_ID = Gruppe_ID;
		if (Benutzer_ID == null) {
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBenutzergruppenMitglied other = (MigrationDTOBenutzergruppenMitglied) obj;
		if (Gruppe_ID == null) {
			if (other.Gruppe_ID != null)
				return false;
		} else if (!Gruppe_ID.equals(other.Gruppe_ID))
			return false;
		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Gruppe_ID == null) ? 0 : Gruppe_ID.hashCode());

		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOBenutzergruppenMitglied(Gruppe_ID=" + this.Gruppe_ID + ", Benutzer_ID=" + this.Benutzer_ID + ")";
	}

}
