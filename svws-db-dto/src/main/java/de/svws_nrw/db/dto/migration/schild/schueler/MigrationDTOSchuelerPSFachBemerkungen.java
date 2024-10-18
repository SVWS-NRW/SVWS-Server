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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLD_PSFachBem.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLD_PSFachBem")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "ASV", "LELS", "AUE", "ESF", "BemerkungFSP", "BemerkungVersetzung", "SchulnrEigner"})
public final class MigrationDTOSchuelerPSFachBemerkungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASV */
	public static final String QUERY_BY_ASV = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ASV = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASV */
	public static final String QUERY_LIST_BY_ASV = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ASV IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LELS */
	public static final String QUERY_BY_LELS = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.LELS = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LELS */
	public static final String QUERY_LIST_BY_LELS = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.LELS IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AUE */
	public static final String QUERY_BY_AUE = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.AUE = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AUE */
	public static final String QUERY_LIST_BY_AUE = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.AUE IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ESF */
	public static final String QUERY_BY_ESF = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ESF = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ESF */
	public static final String QUERY_LIST_BY_ESF = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.ESF IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BemerkungFSP */
	public static final String QUERY_BY_BEMERKUNGFSP = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.BemerkungFSP = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BemerkungFSP */
	public static final String QUERY_LIST_BY_BEMERKUNGFSP = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.BemerkungFSP IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BemerkungVersetzung */
	public static final String QUERY_BY_BEMERKUNGVERSETZUNG = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.BemerkungVersetzung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BemerkungVersetzung */
	public static final String QUERY_LIST_BY_BEMERKUNGVERSETZUNG = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.BemerkungVersetzung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerPSFachBemerkungen e WHERE e.SchulnrEigner IN ?1";

	/** ID des Bemerkungseintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Lernabschnittes */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Text zum Arvbeits und Sozialverhalten */
	@Column(name = "ASV")
	@JsonProperty
	public String ASV;

	/** Text zur Lernentwicklung bei Grundschulen */
	@Column(name = "LELS")
	@JsonProperty
	public String LELS;

	/** Text zum Außerunterrichtlichen Engagement (früher in LELS integeriert, falls, die Schulform keine Grundschule ist) */
	@Column(name = "AUE")
	@JsonProperty
	public String AUE;

	/** Text für die "Empfehlung der Schulform" beim Übergang von Primar- nach SekI */
	@Column(name = "ESF")
	@JsonProperty
	public String ESF;

	/** Text für Förderschwerpunktbemerkung */
	@Column(name = "BemerkungFSP")
	@JsonProperty
	public String BemerkungFSP;

	/** Text für Versetzungsentscheidung */
	@Column(name = "BemerkungVersetzung")
	@JsonProperty
	public String BemerkungVersetzung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerPSFachBemerkungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerPSFachBemerkungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerPSFachBemerkungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public MigrationDTOSchuelerPSFachBemerkungen(final Long ID, final Long Abschnitt_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerPSFachBemerkungen other = (MigrationDTOSchuelerPSFachBemerkungen) obj;
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
		return "MigrationDTOSchuelerPSFachBemerkungen(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", ASV=" + this.ASV + ", LELS=" + this.LELS + ", AUE=" + this.AUE + ", ESF=" + this.ESF + ", BemerkungFSP=" + this.BemerkungFSP + ", BemerkungVersetzung=" + this.BemerkungVersetzung + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
