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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Texte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Texte")
@JsonPropertyOrder({"ID", "Kuerzel", "Inhalt", "Beschreibung", "Aenderbar"})
public final class DTOSchultexte {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchultexte e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchultexte e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchultexte e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchultexte e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchultexte e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchultexte e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOSchultexte e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOSchultexte e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Inhalt */
	public static final String QUERY_BY_INHALT = "SELECT e FROM DTOSchultexte e WHERE e.Inhalt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Inhalt */
	public static final String QUERY_LIST_BY_INHALT = "SELECT e FROM DTOSchultexte e WHERE e.Inhalt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM DTOSchultexte e WHERE e.Beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM DTOSchultexte e WHERE e.Beschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM DTOSchultexte e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM DTOSchultexte e WHERE e.Aenderbar IN ?1";

	/** ID des Textes unter Schulverwaltung Eigene Schule bearbeiten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Kürzel des Schultextes */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Inhalt des Schultextes */
	@Column(name = "Inhalt")
	@JsonProperty
	public String Inhalt;

	/** Beschreibung des Schultextes */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Gibt an ob der Text änderbar ist */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchultexte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchultexte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchultexte ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOSchultexte(final long ID) {
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
		DTOSchultexte other = (DTOSchultexte) obj;
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
		return "DTOSchultexte(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Inhalt=" + this.Inhalt + ", Beschreibung=" + this.Beschreibung + ", Aenderbar=" + this.Aenderbar + ")";
	}

}
