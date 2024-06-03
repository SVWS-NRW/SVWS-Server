package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerSprachenfolge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerSprachenfolge")
@JsonPropertyOrder({"ID", "Schueler_ID", "AbschnittVon", "AbschnittBis", "Referenzniveau", "SchulnrEigner", "Fach_ID", "Reihenfolge", "JahrgangVon", "JahrgangBis"})
public final class MigrationDTOSchuelerSprachenfolge {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschnittVon */
	public static final String QUERY_BY_ABSCHNITTVON = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.AbschnittVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschnittVon */
	public static final String QUERY_LIST_BY_ABSCHNITTVON = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.AbschnittVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschnittBis */
	public static final String QUERY_BY_ABSCHNITTBIS = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.AbschnittBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschnittBis */
	public static final String QUERY_LIST_BY_ABSCHNITTBIS = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.AbschnittBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Referenzniveau */
	public static final String QUERY_BY_REFERENZNIVEAU = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Referenzniveau = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Referenzniveau */
	public static final String QUERY_LIST_BY_REFERENZNIVEAU = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Referenzniveau IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Reihenfolge */
	public static final String QUERY_BY_REIHENFOLGE = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Reihenfolge = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Reihenfolge */
	public static final String QUERY_LIST_BY_REIHENFOLGE = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Reihenfolge IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrgangVon */
	public static final String QUERY_BY_JAHRGANGVON = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.JahrgangVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrgangVon */
	public static final String QUERY_LIST_BY_JAHRGANGVON = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.JahrgangVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrgangBis */
	public static final String QUERY_BY_JAHRGANGBIS = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.JahrgangBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrgangBis */
	public static final String QUERY_LIST_BY_JAHRGANGBIS = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.JahrgangBis IN ?1";

	/** ID des Sprachenfolgeeintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID des Sprachenfolgeeintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Abschnitt Beginn des Sprachenfolgeeintrags */
	@Column(name = "AbschnittVon")
	@JsonProperty
	public Integer AbschnittVon;

	/** Abschnitt Ende des Sprachenfolgeeintrags */
	@Column(name = "AbschnittBis")
	@JsonProperty
	public Integer AbschnittBis;

	/** Referenzniveau GeR des Sprachenfolgeeintrags */
	@Column(name = "Referenzniveau")
	@JsonProperty
	public String Referenzniveau;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** DEPRECATED: FachID des Sprachenfolgeeintrags, Ersetzt durch das Sprachenkürzel */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** DEPRECATED: Reihenfolge Nummer des Sprachenfolgeeintrags bzw. N oder P, ersetzt durch ReihenfolgeNr und Sprachprüfung-Tabelle */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public String Reihenfolge;

	/** DEPRECATED: Jahrgang Beginn des Sprachenfolgeeintrags, durch ASDJahrgang ersetzt */
	@Column(name = "JahrgangVon")
	@JsonProperty
	public Integer JahrgangVon;

	/** DEPRECATED: Jahrgang Ende des Sprachenfolgeeintrags, durch ASDJahrgang ersetzt */
	@Column(name = "JahrgangBis")
	@JsonProperty
	public Integer JahrgangBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerSprachenfolge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerSprachenfolge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerSprachenfolge ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerSprachenfolge(final Long ID, final Long Schueler_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
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
		MigrationDTOSchuelerSprachenfolge other = (MigrationDTOSchuelerSprachenfolge) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerSprachenfolge(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", AbschnittVon=" + this.AbschnittVon + ", AbschnittBis=" + this.AbschnittBis + ", Referenzniveau=" + this.Referenzniveau + ", SchulnrEigner=" + this.SchulnrEigner + ", Fach_ID=" + this.Fach_ID + ", Reihenfolge=" + this.Reihenfolge + ", JahrgangVon=" + this.JahrgangVon + ", JahrgangBis=" + this.JahrgangBis + ")";
	}

}
