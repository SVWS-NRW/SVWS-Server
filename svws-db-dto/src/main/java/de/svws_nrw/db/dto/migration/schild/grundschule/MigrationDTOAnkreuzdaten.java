package de.svws_nrw.db.dto.migration.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ankreuzdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ankreuzdaten")
@JsonPropertyOrder({"ID", "SchulnrEigner", "TextStufe1", "TextStufe2", "TextStufe3", "TextStufe4", "TextStufe5", "BezeichnungSONST"})
public final class MigrationDTOAnkreuzdaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOAnkreuzdaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextStufe1 */
	public static final String QUERY_BY_TEXTSTUFE1 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextStufe1 */
	public static final String QUERY_LIST_BY_TEXTSTUFE1 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextStufe2 */
	public static final String QUERY_BY_TEXTSTUFE2 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextStufe2 */
	public static final String QUERY_LIST_BY_TEXTSTUFE2 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextStufe3 */
	public static final String QUERY_BY_TEXTSTUFE3 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe3 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextStufe3 */
	public static final String QUERY_LIST_BY_TEXTSTUFE3 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe3 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextStufe4 */
	public static final String QUERY_BY_TEXTSTUFE4 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe4 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextStufe4 */
	public static final String QUERY_LIST_BY_TEXTSTUFE4 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe4 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextStufe5 */
	public static final String QUERY_BY_TEXTSTUFE5 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe5 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextStufe5 */
	public static final String QUERY_LIST_BY_TEXTSTUFE5 = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.TextStufe5 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BezeichnungSONST */
	public static final String QUERY_BY_BEZEICHNUNGSONST = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.BezeichnungSONST = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BezeichnungSONST */
	public static final String QUERY_LIST_BY_BEZEICHNUNGSONST = "SELECT e FROM MigrationDTOAnkreuzdaten e WHERE e.BezeichnungSONST IN ?1";

	/** ID des Datensatzes für die Angaben zu den Ankreuzkompetenzen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Bezeichnung Stufe 1 */
	@Column(name = "TextStufe1")
	@JsonProperty
	public String TextStufe1;

	/** Bezeichnung Stufe 2 */
	@Column(name = "TextStufe2")
	@JsonProperty
	public String TextStufe2;

	/** Bezeichnung Stufe 3 */
	@Column(name = "TextStufe3")
	@JsonProperty
	public String TextStufe3;

	/** Bezeichnung Stufe 4 */
	@Column(name = "TextStufe4")
	@JsonProperty
	public String TextStufe4;

	/** Bezeichnung Stufe 5 */
	@Column(name = "TextStufe5")
	@JsonProperty
	public String TextStufe5;

	/** Bezeichnung Zusatzstufe */
	@Column(name = "BezeichnungSONST")
	@JsonProperty
	public String BezeichnungSONST;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAnkreuzdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAnkreuzdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAnkreuzdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOAnkreuzdaten(final Long ID, final Integer SchulnrEigner) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAnkreuzdaten other = (MigrationDTOAnkreuzdaten) obj;
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
		return "MigrationDTOAnkreuzdaten(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", TextStufe1=" + this.TextStufe1 + ", TextStufe2=" + this.TextStufe2 + ", TextStufe3=" + this.TextStufe3 + ", TextStufe4=" + this.TextStufe4 + ", TextStufe5=" + this.TextStufe5 + ", BezeichnungSONST=" + this.BezeichnungSONST + ")";
	}

}
