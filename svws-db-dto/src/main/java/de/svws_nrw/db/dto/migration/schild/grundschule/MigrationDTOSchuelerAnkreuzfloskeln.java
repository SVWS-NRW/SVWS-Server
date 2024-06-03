package de.svws_nrw.db.dto.migration.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAnkreuzfloskeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAnkreuzfloskeln")
@JsonPropertyOrder({"ID", "Schueler_ID", "Abschnitt_ID", "Floskel_ID", "Stufe1", "Stufe2", "Stufe3", "Stufe4", "Stufe5", "SchulnrEigner", "Jahr", "Abschnitt"})
public final class MigrationDTOSchuelerAnkreuzfloskeln {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Floskel_ID */
	public static final String QUERY_BY_FLOSKEL_ID = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Floskel_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Floskel_ID */
	public static final String QUERY_LIST_BY_FLOSKEL_ID = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Floskel_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe1 */
	public static final String QUERY_BY_STUFE1 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe1 */
	public static final String QUERY_LIST_BY_STUFE1 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe2 */
	public static final String QUERY_BY_STUFE2 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe2 */
	public static final String QUERY_LIST_BY_STUFE2 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe3 */
	public static final String QUERY_BY_STUFE3 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe3 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe3 */
	public static final String QUERY_LIST_BY_STUFE3 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe3 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe4 */
	public static final String QUERY_BY_STUFE4 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe4 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe4 */
	public static final String QUERY_LIST_BY_STUFE4 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe4 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe5 */
	public static final String QUERY_BY_STUFE5 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe5 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe5 */
	public static final String QUERY_LIST_BY_STUFE5 = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe5 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt IN ?1";

	/** ID des Ankreuzfloskeleintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED: Schüler-ID zum Ankreuzfloskeleintrag, in Abschnitt_ID enthalten */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** ID der Ankreuzfloskel aus dem Katalog */
	@Column(name = "Floskel_ID")
	@JsonProperty
	public Long Floskel_ID;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe1")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe1;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe2")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe2;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe3")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe3;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe4")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe4;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe5")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe5;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Schuljahr zum Ankreusfloskeleintrag */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt zum Ankreusfloskeleintrag */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerAnkreuzfloskeln() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Floskel_ID   der Wert für das Attribut Floskel_ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOSchuelerAnkreuzfloskeln(final Long ID, final Long Schueler_ID, final Long Abschnitt_ID, final Long Floskel_ID, final Integer SchulnrEigner, final Integer Jahr, final Integer Abschnitt) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Floskel_ID == null) {
			throw new NullPointerException("Floskel_ID must not be null");
		}
		this.Floskel_ID = Floskel_ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Jahr == null) {
			throw new NullPointerException("Jahr must not be null");
		}
		this.Jahr = Jahr;
		if (Abschnitt == null) {
			throw new NullPointerException("Abschnitt must not be null");
		}
		this.Abschnitt = Abschnitt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerAnkreuzfloskeln other = (MigrationDTOSchuelerAnkreuzfloskeln) obj;
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
		return "MigrationDTOSchuelerAnkreuzfloskeln(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Floskel_ID=" + this.Floskel_ID + ", Stufe1=" + this.Stufe1 + ", Stufe2=" + this.Stufe2 + ", Stufe3=" + this.Stufe3 + ", Stufe4=" + this.Stufe4 + ", Stufe5=" + this.Stufe5 + ", SchulnrEigner=" + this.SchulnrEigner + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ")";
	}

}
