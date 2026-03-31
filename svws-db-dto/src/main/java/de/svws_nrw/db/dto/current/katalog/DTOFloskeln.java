package de.svws_nrw.db.dto.current.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Katalog_Floskeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Katalog_Floskeln")
@JsonPropertyOrder({"ID", "Kuerzel", "Text", "Gruppe_ID", "Fach_ID", "Niveau", "Sortierung", "Sichtbar"})
public final class DTOFloskeln {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOFloskeln e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOFloskeln e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOFloskeln e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOFloskeln e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOFloskeln e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOFloskeln e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOFloskeln e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOFloskeln e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Text */
	public static final String QUERY_BY_TEXT = "SELECT e FROM DTOFloskeln e WHERE e.Text = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Text */
	public static final String QUERY_LIST_BY_TEXT = "SELECT e FROM DTOFloskeln e WHERE e.Text IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gruppe_ID */
	public static final String QUERY_BY_GRUPPE_ID = "SELECT e FROM DTOFloskeln e WHERE e.Gruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gruppe_ID */
	public static final String QUERY_LIST_BY_GRUPPE_ID = "SELECT e FROM DTOFloskeln e WHERE e.Gruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOFloskeln e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOFloskeln e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Niveau */
	public static final String QUERY_BY_NIVEAU = "SELECT e FROM DTOFloskeln e WHERE e.Niveau = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Niveau */
	public static final String QUERY_LIST_BY_NIVEAU = "SELECT e FROM DTOFloskeln e WHERE e.Niveau IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOFloskeln e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOFloskeln e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOFloskeln e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOFloskeln e WHERE e.Sichtbar IN ?1";

	/** ID der Floskel (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Kürzel für die Floskel */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Text der Floskel */
	@Column(name = "Text")
	@JsonProperty
	public String Text;

	/** Die ID der Floskelgruppe, welcher die Floskel zugeordnet ist */
	@Column(name = "Gruppe_ID")
	@JsonProperty
	public Long Gruppe_ID;

	/** Die ID des Faches bei Floskeln für fachbezogene Bemerkungen, sonst null */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Die ID des Niveaus bei Floskeln für fachbezogene Bemerkungen, sonst null */
	@Column(name = "Niveau")
	@JsonProperty
	public Integer Niveau;

	/** Sortierung der Floskel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Floskel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFloskeln ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFloskeln() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFloskeln ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Text   der Wert für das Attribut Text
	 */
	public DTOFloskeln(final long ID, final String Kuerzel, final String Text) {
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Text == null) {
			throw new NullPointerException("Text must not be null");
		}
		this.Text = Text;
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
		DTOFloskeln other = (DTOFloskeln) obj;
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
		return "DTOFloskeln(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Text=" + this.Text + ", Gruppe_ID=" + this.Gruppe_ID + ", Fach_ID=" + this.Fach_ID + ", Niveau=" + this.Niveau + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ")";
	}

}
