package de.svws_nrw.db.dto.migration.schild;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schild_Verwaltung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schild_Verwaltung")
@JsonPropertyOrder({"BackupDatum", "AutoBerechnung", "DatumStatkue", "DatumSchildIntern", "Bescheinigung", "Stammblatt", "DatenGeprueft", "Version", "GU_ID", "SchulnrEigner", "DatumLoeschfristHinweisDeaktiviert", "DatumLoeschfristHinweisDeaktiviertUserID", "DatumDatenGeloescht"})
public final class MigrationDTOSchildVerwaltung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchildVerwaltung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.GU_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BackupDatum */
	public static final String QUERY_BY_BACKUPDATUM = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.BackupDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BackupDatum */
	public static final String QUERY_LIST_BY_BACKUPDATUM = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.BackupDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AutoBerechnung */
	public static final String QUERY_BY_AUTOBERECHNUNG = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.AutoBerechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AutoBerechnung */
	public static final String QUERY_LIST_BY_AUTOBERECHNUNG = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.AutoBerechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumStatkue */
	public static final String QUERY_BY_DATUMSTATKUE = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumStatkue = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumStatkue */
	public static final String QUERY_LIST_BY_DATUMSTATKUE = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumStatkue IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumSchildIntern */
	public static final String QUERY_BY_DATUMSCHILDINTERN = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumSchildIntern = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumSchildIntern */
	public static final String QUERY_LIST_BY_DATUMSCHILDINTERN = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumSchildIntern IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bescheinigung */
	public static final String QUERY_BY_BESCHEINIGUNG = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.Bescheinigung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bescheinigung */
	public static final String QUERY_LIST_BY_BESCHEINIGUNG = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.Bescheinigung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stammblatt */
	public static final String QUERY_BY_STAMMBLATT = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.Stammblatt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stammblatt */
	public static final String QUERY_LIST_BY_STAMMBLATT = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.Stammblatt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatenGeprueft */
	public static final String QUERY_BY_DATENGEPRUEFT = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatenGeprueft = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatenGeprueft */
	public static final String QUERY_LIST_BY_DATENGEPRUEFT = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatenGeprueft IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Version */
	public static final String QUERY_BY_VERSION = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.Version = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Version */
	public static final String QUERY_LIST_BY_VERSION = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.Version IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID */
	public static final String QUERY_BY_GU_ID = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID */
	public static final String QUERY_LIST_BY_GU_ID = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumLoeschfristHinweisDeaktiviert */
	public static final String QUERY_BY_DATUMLOESCHFRISTHINWEISDEAKTIVIERT = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumLoeschfristHinweisDeaktiviert */
	public static final String QUERY_LIST_BY_DATUMLOESCHFRISTHINWEISDEAKTIVIERT = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumLoeschfristHinweisDeaktiviertUserID */
	public static final String QUERY_BY_DATUMLOESCHFRISTHINWEISDEAKTIVIERTUSERID = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviertUserID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumLoeschfristHinweisDeaktiviertUserID */
	public static final String QUERY_LIST_BY_DATUMLOESCHFRISTHINWEISDEAKTIVIERTUSERID = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviertUserID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumDatenGeloescht */
	public static final String QUERY_BY_DATUMDATENGELOESCHT = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumDatenGeloescht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumDatenGeloescht */
	public static final String QUERY_LIST_BY_DATUMDATENGELOESCHT = "SELECT e FROM MigrationDTOSchildVerwaltung e WHERE e.DatumDatenGeloescht IN ?1";

	/** Wird das Dateum des letzten Backupo eingetragen. */
	@Column(name = "BackupDatum")
	@JsonProperty
	public String BackupDatum;

	/** Wurden die täglichen automatischen Prozesse schon durchgeführt? */
	@Column(name = "AutoBerechnung")
	@JsonProperty
	public String AutoBerechnung;

	/** DEPRECATED Datum der Statkue wird nicht benutzt. */
	@Column(name = "DatumStatkue")
	@JsonProperty
	public String DatumStatkue;

	/** DEPRECATED Datum der Schildintern wird nicht benutzt. */
	@Column(name = "DatumSchildIntern")
	@JsonProperty
	public String DatumSchildIntern;

	/** Pfad zu der ausgewählten Reportvorlage */
	@Column(name = "Bescheinigung")
	@JsonProperty
	public String Bescheinigung;

	/** Pfad zu der ausgewählten Reportvorlage */
	@Column(name = "Stammblatt")
	@JsonProperty
	public String Stammblatt;

	/** Stößt eine Datenprüfung nach großen Importen an */
	@Column(name = "DatenGeprueft")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean DatenGeprueft;

	/** Versionsdatum (wird zur Erkennung für Updates genutzt) */
	@Column(name = "Version")
	@JsonProperty
	public String Version;

	/** Stellt eine GU_ID für die Datenbank zur Verfügung damit bei Kurs42 erkannt werden kann ob verschiedene DBs verwendet wurden. */
	@Id
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Gibt an ob der User den Hiweis zu den Löschfristen deaktiviert hat. */
	@Column(name = "DatumLoeschfristHinweisDeaktiviert")
	@JsonProperty
	public String DatumLoeschfristHinweisDeaktiviert;

	/** Gibt an welcher User den Hiweis deaktiviert hat. */
	@Column(name = "DatumLoeschfristHinweisDeaktiviertUserID")
	@JsonProperty
	public Long DatumLoeschfristHinweisDeaktiviertUserID;

	/** Gibt an wann der Löschvorgang zuletzt gelaufen ist. */
	@Column(name = "DatumDatenGeloescht")
	@JsonProperty
	public String DatumDatenGeloescht;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchildVerwaltung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchildVerwaltung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchildVerwaltung ohne eine Initialisierung der Attribute.
	 * @param GU_ID   der Wert für das Attribut GU_ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOSchildVerwaltung(final String GU_ID, final Integer SchulnrEigner) {
		if (GU_ID == null) {
			throw new NullPointerException("GU_ID must not be null");
		}
		this.GU_ID = GU_ID;
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
		MigrationDTOSchildVerwaltung other = (MigrationDTOSchildVerwaltung) obj;
		if (GU_ID == null) {
			if (other.GU_ID != null)
				return false;
		} else if (!GU_ID.equals(other.GU_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((GU_ID == null) ? 0 : GU_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchildVerwaltung(BackupDatum=" + this.BackupDatum + ", AutoBerechnung=" + this.AutoBerechnung + ", DatumStatkue=" + this.DatumStatkue + ", DatumSchildIntern=" + this.DatumSchildIntern + ", Bescheinigung=" + this.Bescheinigung + ", Stammblatt=" + this.Stammblatt + ", DatenGeprueft=" + this.DatenGeprueft + ", Version=" + this.Version + ", GU_ID=" + this.GU_ID + ", SchulnrEigner=" + this.SchulnrEigner + ", DatumLoeschfristHinweisDeaktiviert=" + this.DatumLoeschfristHinweisDeaktiviert + ", DatumLoeschfristHinweisDeaktiviertUserID=" + this.DatumLoeschfristHinweisDeaktiviertUserID + ", DatumDatenGeloescht=" + this.DatumDatenGeloescht + ")";
	}

}
