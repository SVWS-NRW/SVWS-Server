package de.svws_nrw.db.dto.migration.schild.erzieher;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerTelefone.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerTelefone")
@JsonPropertyOrder({"ID", "Schueler_ID", "TelefonArt_ID", "Telefonnummer", "Bemerkung", "Sortierung", "SchulnrEigner", "Gesperrt"})
public final class MigrationDTOSchuelerTelefon {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerTelefon e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TelefonArt_ID */
	public static final String QUERY_BY_TELEFONART_ID = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.TelefonArt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TelefonArt_ID */
	public static final String QUERY_LIST_BY_TELEFONART_ID = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.TelefonArt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Telefonnummer */
	public static final String QUERY_BY_TELEFONNUMMER = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Telefonnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Telefonnummer */
	public static final String QUERY_LIST_BY_TELEFONNUMMER = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Telefonnummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gesperrt */
	public static final String QUERY_BY_GESPERRT = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Gesperrt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gesperrt */
	public static final String QUERY_LIST_BY_GESPERRT = "SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Gesperrt IN ?1";

	/** ID des Telefonnummerneintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID des Telefonnummerneintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Art des Telefonnummerneintrags */
	@Column(name = "TelefonArt_ID")
	@JsonProperty
	public Long TelefonArt_ID;

	/** Telefonnummer des Telefonnummerneintrags */
	@Column(name = "Telefonnummer")
	@JsonProperty
	public String Telefonnummer;

	/** Bemerkung des Telefonnummerneintrags */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Sortierung des Telefonnummerneintrags */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Sperrung des Telefonnummerneintrags */
	@Column(name = "Gesperrt")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gesperrt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerTelefon ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerTelefon() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerTelefon ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerTelefon(final Long ID, final Long Schueler_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerTelefon other = (MigrationDTOSchuelerTelefon) obj;
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
		return "MigrationDTOSchuelerTelefon(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", TelefonArt_ID=" + this.TelefonArt_ID + ", Telefonnummer=" + this.Telefonnummer + ", Bemerkung=" + this.Bemerkung + ", Sortierung=" + this.Sortierung + ", SchulnrEigner=" + this.SchulnrEigner + ", Gesperrt=" + this.Gesperrt + ")";
	}

}
