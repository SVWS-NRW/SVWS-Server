package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundentafel_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundentafel_Faecher")
@JsonPropertyOrder({"ID", "Stundentafel_ID", "Fach_ID", "KursartAllg", "WochenStd", "Lehrer_ID", "EpochenUnterricht", "Sortierung", "Sichtbar", "Gewichtung", "SchulnrEigner", "LehrerKrz"})
public final class MigrationDTOStundentafelFaecher {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOStundentafelFaecher e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundentafel_ID */
	public static final String QUERY_BY_STUNDENTAFEL_ID = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Stundentafel_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundentafel_ID */
	public static final String QUERY_LIST_BY_STUNDENTAFEL_ID = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Stundentafel_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KursartAllg */
	public static final String QUERY_BY_KURSARTALLG = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.KursartAllg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KursartAllg */
	public static final String QUERY_LIST_BY_KURSARTALLG = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.KursartAllg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenStd */
	public static final String QUERY_BY_WOCHENSTD = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.WochenStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenStd */
	public static final String QUERY_LIST_BY_WOCHENSTD = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.WochenStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EpochenUnterricht */
	public static final String QUERY_BY_EPOCHENUNTERRICHT = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.EpochenUnterricht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EpochenUnterricht */
	public static final String QUERY_LIST_BY_EPOCHENUNTERRICHT = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.EpochenUnterricht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gewichtung */
	public static final String QUERY_BY_GEWICHTUNG = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Gewichtung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gewichtung */
	public static final String QUERY_LIST_BY_GEWICHTUNG = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Gewichtung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrerKrz */
	public static final String QUERY_BY_LEHRERKRZ = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.LehrerKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrerKrz */
	public static final String QUERY_LIST_BY_LEHRERKRZ = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.LehrerKrz IN ?1";

	/** ID des Facheintrags für die Stundentafel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der zugehörigen Stundentafel */
	@Column(name = "Stundentafel_ID")
	@JsonProperty
	public Long Stundentafel_ID;

	/** FachID das in der Stundentafel ist */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Kursart des Faches in der Stundentafel */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Wochenstunde des Faches in der Stundentafel */
	@Column(name = "WochenStd")
	@JsonProperty
	public Integer WochenStd;

	/** Lehrer-ID des unterrichtenden Lehrers für das Fach der Stundentafel */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Merkmal Epochenunterricht des Faches in der Stundentafel */
	@Column(name = "EpochenUnterricht")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean EpochenUnterricht;

	/** Sortierung des Faches in der Stundentafel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit des Faches in der Stundentafel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** ??? entweder BB auch oder weg ??? Gewichtung allgemeinbidend BK  des Faches in der Stundentafel */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/** DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** DEPRECATED: LehrerKRZ des Faches in der Stundetafel */
	@Column(name = "LehrerKrz")
	@JsonProperty
	public String LehrerKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStundentafelFaecher ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOStundentafelFaecher() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStundentafelFaecher ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundentafel_ID   der Wert für das Attribut Stundentafel_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public MigrationDTOStundentafelFaecher(final Long ID, final Long Stundentafel_ID, final Long Fach_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Stundentafel_ID == null) {
			throw new NullPointerException("Stundentafel_ID must not be null");
		}
		this.Stundentafel_ID = Stundentafel_ID;
		if (Fach_ID == null) {
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOStundentafelFaecher other = (MigrationDTOStundentafelFaecher) obj;
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
		return "MigrationDTOStundentafelFaecher(ID=" + this.ID + ", Stundentafel_ID=" + this.Stundentafel_ID + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", WochenStd=" + this.WochenStd + ", Lehrer_ID=" + this.Lehrer_ID + ", EpochenUnterricht=" + this.EpochenUnterricht + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Gewichtung=" + this.Gewichtung + ", SchulnrEigner=" + this.SchulnrEigner + ", LehrerKrz=" + this.LehrerKrz + ")";
	}

}
