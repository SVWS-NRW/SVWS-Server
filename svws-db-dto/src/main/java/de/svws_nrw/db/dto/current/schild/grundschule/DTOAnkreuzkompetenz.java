package de.svws_nrw.db.dto.current.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ankreuzfloskeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ankreuzfloskeln")
@JsonPropertyOrder({"id", "idFach", "istASV", "schulgliederung", "floskelText", "sortierung", "fachSortierung", "abschnitt", "istSichtbar", "istAktiv"})
public final class DTOAnkreuzkompetenz {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOAnkreuzkompetenz e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idFach */
	public static final String QUERY_BY_IDFACH = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.idFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idFach */
	public static final String QUERY_LIST_BY_IDFACH = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.idFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istASV */
	public static final String QUERY_BY_ISTASV = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.istASV = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istASV */
	public static final String QUERY_LIST_BY_ISTASV = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.istASV IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schulgliederung */
	public static final String QUERY_BY_SCHULGLIEDERUNG = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.schulgliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schulgliederung */
	public static final String QUERY_LIST_BY_SCHULGLIEDERUNG = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.schulgliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes floskelText */
	public static final String QUERY_BY_FLOSKELTEXT = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.floskelText = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes floskelText */
	public static final String QUERY_LIST_BY_FLOSKELTEXT = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.floskelText IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes fachSortierung */
	public static final String QUERY_BY_FACHSORTIERUNG = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.fachSortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes fachSortierung */
	public static final String QUERY_LIST_BY_FACHSORTIERUNG = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.fachSortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.abschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSichtbar */
	public static final String QUERY_BY_ISTSICHTBAR = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.istSichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSichtbar */
	public static final String QUERY_LIST_BY_ISTSICHTBAR = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.istSichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istAktiv */
	public static final String QUERY_BY_ISTAKTIV = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.istAktiv = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istAktiv */
	public static final String QUERY_LIST_BY_ISTAKTIV = "SELECT e FROM DTOAnkreuzkompetenz e WHERE e.istAktiv IN ?1";

	/** ID der Ankreuzfloskel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** FachID zu der die Ankreuzfloskel gehört, null für individuelle Ankreuzfloskeln bzw. siehe Spalte IstASV */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long idFach;

	/** Gibt an, falls die Fach_ID null ist, ob es sich bei der Ankreuzfloskel um eine Floskel zum Arbeits- und Sozialverhalten handelt (1) oder nicht (0). */
	@Column(name = "IstASV")
	@JsonProperty
	public int istASV;

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
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSichtbar;

	/** Gibt an ob die Ankreuzfloskel aktiv ist */
	@Column(name = "Aktiv")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istAktiv;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAnkreuzkompetenz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAnkreuzkompetenz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAnkreuzkompetenz ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param istASV   der Wert für das Attribut istASV
	 * @param floskelText   der Wert für das Attribut floskelText
	 */
	public DTOAnkreuzkompetenz(final long id, final int istASV, final String floskelText) {
		this.id = id;
		this.istASV = istASV;
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
		DTOAnkreuzkompetenz other = (DTOAnkreuzkompetenz) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOAnkreuzkompetenz(id=" + this.id + ", idFach=" + this.idFach + ", istASV=" + this.istASV + ", schulgliederung=" + this.schulgliederung + ", floskelText=" + this.floskelText + ", sortierung=" + this.sortierung + ", fachSortierung=" + this.fachSortierung + ", abschnitt=" + this.abschnitt + ", istSichtbar=" + this.istSichtbar + ", istAktiv=" + this.istAktiv + ")";
	}

}
