package de.svws_nrw.db.dto.migration.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ankreuzfloskeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ankreuzfloskeln")
@JsonPropertyOrder({"id", "SchulnrEigner", "idFach", "istASV", "Jahrgang", "schulgliederung", "floskelText", "sortierung", "fachSortierung", "abschnitt", "istSichtbar", "istAktiv"})
public final class MigrationDTOAnkreuzkompetenz {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOAnkreuzkompetenz e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idFach */
	public static final String QUERY_BY_IDFACH = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.idFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idFach */
	public static final String QUERY_LIST_BY_IDFACH = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.idFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istASV */
	public static final String QUERY_BY_ISTASV = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.istASV = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istASV */
	public static final String QUERY_LIST_BY_ISTASV = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.istASV IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang */
	public static final String QUERY_BY_JAHRGANG = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang */
	public static final String QUERY_LIST_BY_JAHRGANG = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schulgliederung */
	public static final String QUERY_BY_SCHULGLIEDERUNG = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.schulgliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schulgliederung */
	public static final String QUERY_LIST_BY_SCHULGLIEDERUNG = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.schulgliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes floskelText */
	public static final String QUERY_BY_FLOSKELTEXT = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.floskelText = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes floskelText */
	public static final String QUERY_LIST_BY_FLOSKELTEXT = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.floskelText IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes fachSortierung */
	public static final String QUERY_BY_FACHSORTIERUNG = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.fachSortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes fachSortierung */
	public static final String QUERY_LIST_BY_FACHSORTIERUNG = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.fachSortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.abschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSichtbar */
	public static final String QUERY_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.istSichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSichtbar */
	public static final String QUERY_LIST_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.istSichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istAktiv */
	public static final String QUERY_BY_ISTAKTIV = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.istAktiv = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istAktiv */
	public static final String QUERY_LIST_BY_ISTAKTIV = "SELECT e FROM MigrationDTOAnkreuzkompetenz e WHERE e.istAktiv IN ?1";

	/** ID der Ankreuzfloskel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** FachID zu der die Ankreuzfloskel gehört, null für individuelle Ankreuzfloskeln bzw. siehe Spalte IstASV */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long idFach;

	/** Gibt an, falls die Fach_ID null ist, ob es sich bei der Ankreuzfloskel um eine Floskel zum Arbeits- und Sozialverhalten handelt (1) oder nicht (0). */
	@Column(name = "IstASV")
	@JsonProperty
	public Integer istASV;

	/** ASD-Jahrgang zu der die Ankreuzfloskel gehört */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String Jahrgang;

	/** Schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK) */
	@Column(name = "Gliederung")
	@JsonProperty
	public String schulgliederung;

	/** Text der Ankreuzfloskel */
	@Column(name = "FloskelText")
	@JsonProperty
	public String floskelText;

	/** Sortierung der Ankreuzfloskel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** Sortierung des Faches der Ankreuzfloskel */
	@Column(name = "FachSortierung")
	@JsonProperty
	public Integer fachSortierung;

	/** Wird in welchen Abschnitten verwendet 1Hj 2HJ beide */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer abschnitt;

	/** Sichtbarkeit der Ankreuzfloskel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSichtbar;

	/** Gibt an ob die Ankreuzfloskel aktiv ist */
	@Column(name = "Aktiv")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istAktiv;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAnkreuzkompetenz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAnkreuzkompetenz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAnkreuzkompetenz ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param istASV   der Wert für das Attribut istASV
	 * @param Jahrgang   der Wert für das Attribut Jahrgang
	 * @param floskelText   der Wert für das Attribut floskelText
	 */
	public MigrationDTOAnkreuzkompetenz(final Long id, final Integer SchulnrEigner, final Integer istASV, final String Jahrgang, final String floskelText) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
		this.id = id;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (istASV == null) {
			throw new NullPointerException("istASV must not be null");
		}
		this.istASV = istASV;
		if (Jahrgang == null) {
			throw new NullPointerException("Jahrgang must not be null");
		}
		this.Jahrgang = Jahrgang;
		if (floskelText == null) {
			throw new NullPointerException("floskelText must not be null");
		}
		this.floskelText = floskelText;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MigrationDTOAnkreuzkompetenz other = (MigrationDTOAnkreuzkompetenz) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOAnkreuzkompetenz(id=" + this.id + ", SchulnrEigner=" + this.SchulnrEigner + ", idFach=" + this.idFach + ", istASV=" + this.istASV + ", Jahrgang=" + this.Jahrgang + ", schulgliederung=" + this.schulgliederung + ", floskelText=" + this.floskelText + ", sortierung=" + this.sortierung + ", fachSortierung=" + this.fachSortierung + ", abschnitt=" + this.abschnitt + ", istSichtbar=" + this.istSichtbar + ", istAktiv=" + this.istAktiv + ")";
	}

}
