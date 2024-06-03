package de.svws_nrw.db.dto.migration.schild.faecher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;


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
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Fachgruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Fachgruppen")
@JsonPropertyOrder({"ID", "Fachbereich", "SchildFgID", "FG_Bezeichnung", "FG_Kuerzel", "Schulformen", "FarbeR", "FarbeG", "FarbeB", "Sortierung", "FuerZeugnis", "gueltigVon", "gueltigBis"})
public final class MigrationDTOFachgruppen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOFachgruppen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachbereich */
	public static final String QUERY_BY_FACHBEREICH = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.Fachbereich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachbereich */
	public static final String QUERY_LIST_BY_FACHBEREICH = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.Fachbereich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchildFgID */
	public static final String QUERY_BY_SCHILDFGID = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.SchildFgID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchildFgID */
	public static final String QUERY_LIST_BY_SCHILDFGID = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.SchildFgID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FG_Bezeichnung */
	public static final String QUERY_BY_FG_BEZEICHNUNG = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FG_Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FG_Bezeichnung */
	public static final String QUERY_LIST_BY_FG_BEZEICHNUNG = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FG_Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FG_Kuerzel */
	public static final String QUERY_BY_FG_KUERZEL = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FG_Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FG_Kuerzel */
	public static final String QUERY_LIST_BY_FG_KUERZEL = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FG_Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulformen */
	public static final String QUERY_BY_SCHULFORMEN = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.Schulformen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulformen */
	public static final String QUERY_LIST_BY_SCHULFORMEN = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.Schulformen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FarbeR */
	public static final String QUERY_BY_FARBER = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FarbeR = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FarbeR */
	public static final String QUERY_LIST_BY_FARBER = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FarbeR IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FarbeG */
	public static final String QUERY_BY_FARBEG = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FarbeG = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FarbeG */
	public static final String QUERY_LIST_BY_FARBEG = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FarbeG IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FarbeB */
	public static final String QUERY_BY_FARBEB = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FarbeB = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FarbeB */
	public static final String QUERY_LIST_BY_FARBEB = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FarbeB IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FuerZeugnis */
	public static final String QUERY_BY_FUERZEUGNIS = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FuerZeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FuerZeugnis */
	public static final String QUERY_LIST_BY_FUERZEUGNIS = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.FuerZeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.gueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.gueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.gueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM MigrationDTOFachgruppen e WHERE e.gueltigBis IN ?1";

	/** ID des Fachgruppen-Core-Type, welcher auch ein Mapping zu den Fachgruppen von SchildNRW und Lupo bereitstellt */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Fachbereich (Nr) der Fachgruppe aus Lupo */
	@Column(name = "Fachbereich")
	@JsonProperty
	public Integer Fachbereich;

	/** Fachgruppen ID aus SchildNRW */
	@Column(name = "SchildFgID")
	@JsonProperty
	public Long SchildFgID;

	/** Bezeichnung der Fachgruppe */
	@Column(name = "FG_Bezeichnung")
	@JsonProperty
	public String FG_Bezeichnung;

	/** Kürzel der Fachgruppe */
	@Column(name = "FG_Kuerzel")
	@JsonProperty
	public String FG_Kuerzel;

	/** Gibt an in welchen Schulformen die Fachgruppe zulässig ist */
	@Column(name = "Schulformen")
	@JsonProperty
	public String Schulformen;

	/** Default-Fachgruppenfarbe (Rot-Wert) */
	@Column(name = "FarbeR")
	@JsonProperty
	public Integer FarbeR;

	/** Default-Fachgruppenfarbe (Grün-Wert) */
	@Column(name = "FarbeG")
	@JsonProperty
	public Integer FarbeG;

	/** Default-Fachgruppenfarbe (Blau-Wert) */
	@Column(name = "FarbeB")
	@JsonProperty
	public Integer FarbeB;

	/** Eine Standard-Sortierreihenfolge für die Fachgruppen */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Gibt an, ob die Fachgruppe für Unterteilungen auf Zeugnissen verwendet wird */
	@Column(name = "FuerZeugnis")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean FuerZeugnis;

	/** Das Schuljahr, ab dem die Fachgruppe eingeführt wurde */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Das Schuljahr, bis wann die Fachgruppe gültig ist */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachgruppen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFachgruppen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachgruppen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param FG_Bezeichnung   der Wert für das Attribut FG_Bezeichnung
	 * @param FuerZeugnis   der Wert für das Attribut FuerZeugnis
	 */
	public MigrationDTOFachgruppen(final Long ID, final String FG_Bezeichnung, final Boolean FuerZeugnis) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (FG_Bezeichnung == null) {
			throw new NullPointerException("FG_Bezeichnung must not be null");
		}
		this.FG_Bezeichnung = FG_Bezeichnung;
		if (FuerZeugnis == null) {
			throw new NullPointerException("FuerZeugnis must not be null");
		}
		this.FuerZeugnis = FuerZeugnis;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFachgruppen other = (MigrationDTOFachgruppen) obj;
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
		return "MigrationDTOFachgruppen(ID=" + this.ID + ", Fachbereich=" + this.Fachbereich + ", SchildFgID=" + this.SchildFgID + ", FG_Bezeichnung=" + this.FG_Bezeichnung + ", FG_Kuerzel=" + this.FG_Kuerzel + ", Schulformen=" + this.Schulformen + ", FarbeR=" + this.FarbeR + ", FarbeG=" + this.FarbeG + ", FarbeB=" + this.FarbeB + ", Sortierung=" + this.Sortierung + ", FuerZeugnis=" + this.FuerZeugnis + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
