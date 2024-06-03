package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Teilstandorte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Teilstandorte")
@JsonPropertyOrder({"SchulnrEigner", "AdrMerkmal", "PLZ", "Ort", "Strasse", "Strassenname", "HausNr", "HausNrZusatz", "Bemerkung", "Kuerzel"})
public final class MigrationDTOTeilstandorte {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOTeilstandorte e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AdrMerkmal */
	public static final String QUERY_BY_ADRMERKMAL = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AdrMerkmal */
	public static final String QUERY_LIST_BY_ADRMERKMAL = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PLZ */
	public static final String QUERY_BY_PLZ = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.PLZ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PLZ */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.PLZ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ort */
	public static final String QUERY_BY_ORT = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Ort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ort */
	public static final String QUERY_LIST_BY_ORT = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Ort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strasse */
	public static final String QUERY_BY_STRASSE = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Strasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strasse */
	public static final String QUERY_LIST_BY_STRASSE = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Strasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strassenname */
	public static final String QUERY_BY_STRASSENNAME = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Strassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strassenname */
	public static final String QUERY_LIST_BY_STRASSENNAME = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Strassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNr */
	public static final String QUERY_BY_HAUSNR = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.HausNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNr */
	public static final String QUERY_LIST_BY_HAUSNR = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.HausNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNrZusatz */
	public static final String QUERY_BY_HAUSNRZUSATZ = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.HausNrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNrZusatz */
	public static final String QUERY_LIST_BY_HAUSNRZUSATZ = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.HausNrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Kuerzel IN ?1";

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Adressmerkmal des Teilstandortes (A...Z) */
	@Id
	@Column(name = "AdrMerkmal")
	@JsonProperty
	public String AdrMerkmal;

	/** Postleitzahl des Teilstandortes */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Ort des Teilstandortes */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Straße des Teilstandortes */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

	/** Straßenname des Teilstandortes */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer des Teilstandortes */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Hausnummerzusatz des Teilstandortes */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** Bemerkung zum Teilstandort (Text) */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Kürzel des Teilstandortes */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTeilstandorte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOTeilstandorte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTeilstandorte ohne eine Initialisierung der Attribute.
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param AdrMerkmal   der Wert für das Attribut AdrMerkmal
	 */
	public MigrationDTOTeilstandorte(final Integer SchulnrEigner, final String AdrMerkmal) {
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (AdrMerkmal == null) {
			throw new NullPointerException("AdrMerkmal must not be null");
		}
		this.AdrMerkmal = AdrMerkmal;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOTeilstandorte other = (MigrationDTOTeilstandorte) obj;
		if (AdrMerkmal == null) {
			if (other.AdrMerkmal != null)
				return false;
		} else if (!AdrMerkmal.equals(other.AdrMerkmal))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AdrMerkmal == null) ? 0 : AdrMerkmal.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOTeilstandorte(SchulnrEigner=" + this.SchulnrEigner + ", AdrMerkmal=" + this.AdrMerkmal + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strasse=" + this.Strasse + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Bemerkung=" + this.Bemerkung + ", Kuerzel=" + this.Kuerzel + ")";
	}

}
