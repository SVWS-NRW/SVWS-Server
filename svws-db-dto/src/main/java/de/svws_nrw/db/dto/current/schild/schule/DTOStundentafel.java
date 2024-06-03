package de.svws_nrw.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundentafel.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundentafel")
@JsonPropertyOrder({"ID", "Bezeichnung", "Jahrgang_ID", "ASDJahrgang", "SGL", "Fachklasse_ID", "Sichtbar", "Sortierung"})
public final class DTOStundentafel {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundentafel e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundentafel e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundentafel e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundentafel e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundentafel e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundentafel e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOStundentafel e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOStundentafel e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM DTOStundentafel e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM DTOStundentafel e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgang */
	public static final String QUERY_BY_ASDJAHRGANG = "SELECT e FROM DTOStundentafel e WHERE e.ASDJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgang */
	public static final String QUERY_LIST_BY_ASDJAHRGANG = "SELECT e FROM DTOStundentafel e WHERE e.ASDJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SGL */
	public static final String QUERY_BY_SGL = "SELECT e FROM DTOStundentafel e WHERE e.SGL = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SGL */
	public static final String QUERY_LIST_BY_SGL = "SELECT e FROM DTOStundentafel e WHERE e.SGL IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachklasse_ID */
	public static final String QUERY_BY_FACHKLASSE_ID = "SELECT e FROM DTOStundentafel e WHERE e.Fachklasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachklasse_ID */
	public static final String QUERY_LIST_BY_FACHKLASSE_ID = "SELECT e FROM DTOStundentafel e WHERE e.Fachklasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOStundentafel e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOStundentafel e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOStundentafel e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOStundentafel e WHERE e.Sortierung IN ?1";

	/** ID der Stundentafel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung der Stundentafel */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** JahrgangsID der Stundentafel */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** ASD-Jahrgang der Stundentafel */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** SGL der Stundentafel */
	@Column(name = "SGL")
	@JsonProperty
	public String SGL;

	/** FachklassenID der Stundentafel */
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Sichtbarkeit der Stundentafel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierungnummer  der Stundentafel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundentafel ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundentafel() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundentafel ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOStundentafel(final long ID, final String Bezeichnung) {
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundentafel other = (DTOStundentafel) obj;
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
		return "DTOStundentafel(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Jahrgang_ID=" + this.Jahrgang_ID + ", ASDJahrgang=" + this.ASDJahrgang + ", SGL=" + this.SGL + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ")";
	}

}
