package de.svws_nrw.db.dto.migration.schild.benutzer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Benutzergruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Benutzergruppen")
@JsonPropertyOrder({"ID", "Bezeichnung", "IstAdmin"})
public final class MigrationDTOBenutzergruppe {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOBenutzergruppe e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstAdmin */
	public static final String QUERY_BY_ISTADMIN = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.IstAdmin = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstAdmin */
	public static final String QUERY_LIST_BY_ISTADMIN = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.IstAdmin IN ?1";

	/** Die ID der Benutzergruppe */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Bezeichnung der Benutzergruppe */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Gibt an, ob die Benutzergruppe Administrator-Rechte hat (1) oder nicht (0) */
	@Column(name = "IstAdmin")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstAdmin;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzergruppe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBenutzergruppe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzergruppe ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param IstAdmin   der Wert für das Attribut IstAdmin
	 */
	public MigrationDTOBenutzergruppe(final Long ID, final String Bezeichnung, final Boolean IstAdmin) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (IstAdmin == null) {
			throw new NullPointerException("IstAdmin must not be null");
		}
		this.IstAdmin = IstAdmin;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBenutzergruppe other = (MigrationDTOBenutzergruppe) obj;
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
		return "MigrationDTOBenutzergruppe(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", IstAdmin=" + this.IstAdmin + ")";
	}

}
