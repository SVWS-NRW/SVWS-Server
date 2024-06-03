package de.svws_nrw.db.dto.migration.schild;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerListe_Inhalt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOSchuelerIndividuelleGruppeSchuelerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerListe_Inhalt")
@JsonPropertyOrder({"Liste_ID", "Schueler_ID", "SchulnrEigner"})
public final class MigrationDTOSchuelerIndividuelleGruppeSchueler {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e WHERE e.Liste_ID = ?1 AND e.Schueler_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e WHERE e.Liste_ID IS NOT NULL AND e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Liste_ID */
	public static final String QUERY_BY_LISTE_ID = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e WHERE e.Liste_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Liste_ID */
	public static final String QUERY_LIST_BY_LISTE_ID = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e WHERE e.Liste_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerIndividuelleGruppeSchueler e WHERE e.SchulnrEigner IN ?1";

	/** ID der individuellen Schülerliste */
	@Id
	@Column(name = "Liste_ID")
	@JsonProperty
	public Long Liste_ID;

	/** SchülerID des Schülers der zur individuellen Schülerliste gehört */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerIndividuelleGruppeSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerIndividuelleGruppeSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerIndividuelleGruppeSchueler ohne eine Initialisierung der Attribute.
	 * @param Liste_ID   der Wert für das Attribut Liste_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerIndividuelleGruppeSchueler(final Long Liste_ID, final Long Schueler_ID) {
		if (Liste_ID == null) {
			throw new NullPointerException("Liste_ID must not be null");
		}
		this.Liste_ID = Liste_ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerIndividuelleGruppeSchueler other = (MigrationDTOSchuelerIndividuelleGruppeSchueler) obj;
		if (Liste_ID == null) {
			if (other.Liste_ID != null)
				return false;
		} else if (!Liste_ID.equals(other.Liste_ID))
			return false;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Liste_ID == null) ? 0 : Liste_ID.hashCode());

		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerIndividuelleGruppeSchueler(Liste_ID=" + this.Liste_ID + ", Schueler_ID=" + this.Schueler_ID + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
