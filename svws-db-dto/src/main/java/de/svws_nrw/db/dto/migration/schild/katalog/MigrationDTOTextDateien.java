package de.svws_nrw.db.dto.migration.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Textdateien.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Textdateien")
@JsonPropertyOrder({"ID", "Bezeichnung", "Text_ID", "Text_Body", "Sichtbar", "Sortierung", "SchulnrEigner"})
public final class MigrationDTOTextDateien {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOTextDateien e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOTextDateien e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOTextDateien e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOTextDateien e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOTextDateien e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOTextDateien e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Text_ID */
	public static final String QUERY_BY_TEXT_ID = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Text_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Text_ID */
	public static final String QUERY_LIST_BY_TEXT_ID = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Text_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Text_Body */
	public static final String QUERY_BY_TEXT_BODY = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Text_Body = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Text_Body */
	public static final String QUERY_LIST_BY_TEXT_BODY = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Text_Body IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOTextDateien e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOTextDateien e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOTextDateien e WHERE e.SchulnrEigner IN ?1";

	/** ID des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnungdes Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** TextID des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Text_ID")
	@JsonProperty
	public Long Text_ID;

	/** Text-Body des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Text_Body")
	@JsonProperty
	public String Text_Body;

	/** Sichbarkeit des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierung des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTextDateien ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOTextDateien() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTextDateien ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Text_ID   der Wert für das Attribut Text_ID
	 */
	public MigrationDTOTextDateien(final Long ID, final Long Text_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Text_ID == null) {
			throw new NullPointerException("Text_ID must not be null");
		}
		this.Text_ID = Text_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOTextDateien other = (MigrationDTOTextDateien) obj;
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
		return "MigrationDTOTextDateien(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Text_ID=" + this.Text_ID + ", Text_Body=" + this.Text_Body + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
