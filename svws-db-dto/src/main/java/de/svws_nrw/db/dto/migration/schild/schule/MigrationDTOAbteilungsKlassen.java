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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Abt_Kl.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Abt_Kl")
@JsonPropertyOrder({"ID", "Abteilung_ID", "Klasse", "Sichtbar", "SchulnrEigner"})
public final class MigrationDTOAbteilungsKlassen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOAbteilungsKlassen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abteilung_ID */
	public static final String QUERY_BY_ABTEILUNG_ID = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Abteilung_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abteilung_ID */
	public static final String QUERY_LIST_BY_ABTEILUNG_ID = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Abteilung_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klasse */
	public static final String QUERY_BY_KLASSE = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Klasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klasse */
	public static final String QUERY_LIST_BY_KLASSE = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Klasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.SchulnrEigner IN ?1";

	/** ID der Klassenzugehörigkeit zu einer Abteilung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Abteilung in der übergeordneten Tabelle */
	@Column(name = "Abteilung_ID")
	@JsonProperty
	public Long Abteilung_ID;

	/** DEPRECATED: Klassenbezeichnung die zur Abteilung gehört */
	@Column(name = "Klasse")
	@JsonProperty
	public String Klasse;

	/** steuert die Sichtbarkeit der Klasse zur Abteilung */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAbteilungsKlassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abteilung_ID   der Wert für das Attribut Abteilung_ID
	 * @param Klasse   der Wert für das Attribut Klasse
	 */
	public MigrationDTOAbteilungsKlassen(final Long ID, final Long Abteilung_ID, final String Klasse) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abteilung_ID == null) {
			throw new NullPointerException("Abteilung_ID must not be null");
		}
		this.Abteilung_ID = Abteilung_ID;
		if (Klasse == null) {
			throw new NullPointerException("Klasse must not be null");
		}
		this.Klasse = Klasse;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAbteilungsKlassen other = (MigrationDTOAbteilungsKlassen) obj;
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
		return "MigrationDTOAbteilungsKlassen(ID=" + this.ID + ", Abteilung_ID=" + this.Abteilung_ID + ", Klasse=" + this.Klasse + ", Sichtbar=" + this.Sichtbar + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
