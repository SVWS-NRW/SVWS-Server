package de.svws_nrw.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Texte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Texte")
@JsonPropertyOrder({"ID", "SchulnrEigner", "Kuerzel", "Inhalt", "Beschreibung", "Aenderbar"})
public final class MigrationDTOSchultexte {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchultexte e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchultexte e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchultexte e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Inhalt */
	public static final String QUERY_BY_INHALT = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Inhalt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Inhalt */
	public static final String QUERY_LIST_BY_INHALT = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Inhalt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Beschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Aenderbar IN ?1";

	/** ID des Textes unter Schulverwaltung Eigene Schule bearbeiten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

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
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchultexte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchultexte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchultexte ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOSchultexte(final Long ID, final Integer SchulnrEigner) {
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
		MigrationDTOSchultexte other = (MigrationDTOSchultexte) obj;
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
		return "MigrationDTOSchultexte(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Kuerzel=" + this.Kuerzel + ", Inhalt=" + this.Inhalt + ", Beschreibung=" + this.Beschreibung + ", Aenderbar=" + this.Aenderbar + ")";
	}

}
