package de.svws_nrw.db.dto.current.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Kindergarten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Kindergarten")
@JsonPropertyOrder({"ID", "Bezeichnung", "PLZ", "Ort", "Strassenname", "HausNr", "HausNrZusatz", "Tel", "Email", "Bemerkung", "Sichtbar", "Sortierung"})
public final class DTOKindergarten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKindergarten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKindergarten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOKindergarten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKindergarten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOKindergarten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOKindergarten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOKindergarten e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOKindergarten e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PLZ */
	public static final String QUERY_BY_PLZ = "SELECT e FROM DTOKindergarten e WHERE e.PLZ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PLZ */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM DTOKindergarten e WHERE e.PLZ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ort */
	public static final String QUERY_BY_ORT = "SELECT e FROM DTOKindergarten e WHERE e.Ort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ort */
	public static final String QUERY_LIST_BY_ORT = "SELECT e FROM DTOKindergarten e WHERE e.Ort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strassenname */
	public static final String QUERY_BY_STRASSENNAME = "SELECT e FROM DTOKindergarten e WHERE e.Strassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strassenname */
	public static final String QUERY_LIST_BY_STRASSENNAME = "SELECT e FROM DTOKindergarten e WHERE e.Strassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNr */
	public static final String QUERY_BY_HAUSNR = "SELECT e FROM DTOKindergarten e WHERE e.HausNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNr */
	public static final String QUERY_LIST_BY_HAUSNR = "SELECT e FROM DTOKindergarten e WHERE e.HausNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNrZusatz */
	public static final String QUERY_BY_HAUSNRZUSATZ = "SELECT e FROM DTOKindergarten e WHERE e.HausNrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNrZusatz */
	public static final String QUERY_LIST_BY_HAUSNRZUSATZ = "SELECT e FROM DTOKindergarten e WHERE e.HausNrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Tel */
	public static final String QUERY_BY_TEL = "SELECT e FROM DTOKindergarten e WHERE e.Tel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Tel */
	public static final String QUERY_LIST_BY_TEL = "SELECT e FROM DTOKindergarten e WHERE e.Tel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM DTOKindergarten e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM DTOKindergarten e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOKindergarten e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOKindergarten e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOKindergarten e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOKindergarten e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOKindergarten e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOKindergarten e WHERE e.Sortierung IN ?1";

	/** ID des Kindergartens */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung des Kindergartens */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** PLZ  des Kindergartens */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Ort des Kindergartens */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Strassenname des Kindergartens */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer des Kindergarten */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Hausnumemrzusatz des Kindergartens */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** Telefonnummer des Kindergartens */
	@Column(name = "Tel")
	@JsonProperty
	public String Tel;

	/** E-Mail-Adresse des Kindergartens */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Bemerkung zum Kindergarten */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Sichbarkeit des Kindergartens */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierung des Kindergartens */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKindergarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKindergarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKindergarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOKindergarten(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKindergarten other = (DTOKindergarten) obj;
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
		return "DTOKindergarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Tel=" + this.Tel + ", Email=" + this.Email + ", Bemerkung=" + this.Bemerkung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ")";
	}

}
