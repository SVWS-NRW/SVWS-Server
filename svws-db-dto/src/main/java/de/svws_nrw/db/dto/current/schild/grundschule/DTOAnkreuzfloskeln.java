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
@JsonPropertyOrder({"ID", "Fach_ID", "IstASV", "Jahrgang", "Gliederung", "FloskelText", "Sortierung", "FachSortierung", "Abschnitt", "Sichtbar", "Aktiv"})
public final class DTOAnkreuzfloskeln {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOAnkreuzfloskeln e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstASV */
	public static final String QUERY_BY_ISTASV = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.IstASV = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstASV */
	public static final String QUERY_LIST_BY_ISTASV = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.IstASV IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang */
	public static final String QUERY_BY_JAHRGANG = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang */
	public static final String QUERY_LIST_BY_JAHRGANG = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gliederung */
	public static final String QUERY_BY_GLIEDERUNG = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Gliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gliederung */
	public static final String QUERY_LIST_BY_GLIEDERUNG = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Gliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FloskelText */
	public static final String QUERY_BY_FLOSKELTEXT = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.FloskelText = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FloskelText */
	public static final String QUERY_LIST_BY_FLOSKELTEXT = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.FloskelText IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachSortierung */
	public static final String QUERY_BY_FACHSORTIERUNG = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.FachSortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachSortierung */
	public static final String QUERY_LIST_BY_FACHSORTIERUNG = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.FachSortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Abschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aktiv */
	public static final String QUERY_BY_AKTIV = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Aktiv = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aktiv */
	public static final String QUERY_LIST_BY_AKTIV = "SELECT e FROM DTOAnkreuzfloskeln e WHERE e.Aktiv IN ?1";

	/** ID der Ankreuzfloskel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** FachID zu der die Ankreuzfloskel gehört, null für individuelle Ankreuzfloskeln bzw. siehe Spalte IstASV */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Gibt an, falls die Fach_ID null ist, ob es sich bei der Ankreuzfloskel um eine Floskel zum Arbeits- und Sozialverhalten handelt (1) oder nicht (0). */
	@Column(name = "IstASV")
	@JsonProperty
	public int IstASV;

	/** Jahrgang zu der die Ankreuzfloskel gehört */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String Jahrgang;

	/** Schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK) */
	@Column(name = "Gliederung")
	@JsonProperty
	public String Gliederung;

	/** Text der Ankreuzfloskel */
	@Column(name = "FloskelText")
	@JsonProperty
	public String FloskelText;

	/** Sortierung der Ankreuzfloskel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sortierung des Faches der Ankreuzfloskel */
	@Column(name = "FachSortierung")
	@JsonProperty
	public Integer FachSortierung;

	/** Wird in welchen Abschnitten verwendet 1Hj 2HJ beide */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** Sichtbarkeit der Ankreuzfloskel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an ob die Ankreuzfloskel aktiv ist */
	@Column(name = "Aktiv")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aktiv;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAnkreuzfloskeln() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param IstASV   der Wert für das Attribut IstASV
	 * @param Jahrgang   der Wert für das Attribut Jahrgang
	 * @param FloskelText   der Wert für das Attribut FloskelText
	 */
	public DTOAnkreuzfloskeln(final long ID, final int IstASV, final String Jahrgang, final String FloskelText) {
		this.ID = ID;
		this.IstASV = IstASV;
		if (Jahrgang == null) {
			throw new NullPointerException("Jahrgang must not be null");
		}
		this.Jahrgang = Jahrgang;
		if (FloskelText == null) {
			throw new NullPointerException("FloskelText must not be null");
		}
		this.FloskelText = FloskelText;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAnkreuzfloskeln other = (DTOAnkreuzfloskeln) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOAnkreuzfloskeln(ID=" + this.ID + ", Fach_ID=" + this.Fach_ID + ", IstASV=" + this.IstASV + ", Jahrgang=" + this.Jahrgang + ", Gliederung=" + this.Gliederung + ", FloskelText=" + this.FloskelText + ", Sortierung=" + this.Sortierung + ", FachSortierung=" + this.FachSortierung + ", Abschnitt=" + this.Abschnitt + ", Sichtbar=" + this.Sichtbar + ", Aktiv=" + this.Aktiv + ")";
	}

}
