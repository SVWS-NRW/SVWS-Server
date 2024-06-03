package de.svws_nrw.db.dto.current.schild;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schild_Verwaltung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schild_Verwaltung")
@JsonPropertyOrder({"BackupDatum", "AutoBerechnung", "DatumStatkue", "DatumSchildIntern", "Bescheinigung", "Stammblatt", "DatenGeprueft", "Version", "GU_ID", "DatumLoeschfristHinweisDeaktiviert", "DatumLoeschfristHinweisDeaktiviertUserID", "DatumDatenGeloescht"})
public final class DTOSchildVerwaltung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchildVerwaltung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BackupDatum */
	public static final String QUERY_BY_BACKUPDATUM = "SELECT e FROM DTOSchildVerwaltung e WHERE e.BackupDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BackupDatum */
	public static final String QUERY_LIST_BY_BACKUPDATUM = "SELECT e FROM DTOSchildVerwaltung e WHERE e.BackupDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AutoBerechnung */
	public static final String QUERY_BY_AUTOBERECHNUNG = "SELECT e FROM DTOSchildVerwaltung e WHERE e.AutoBerechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AutoBerechnung */
	public static final String QUERY_LIST_BY_AUTOBERECHNUNG = "SELECT e FROM DTOSchildVerwaltung e WHERE e.AutoBerechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumStatkue */
	public static final String QUERY_BY_DATUMSTATKUE = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumStatkue = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumStatkue */
	public static final String QUERY_LIST_BY_DATUMSTATKUE = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumStatkue IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumSchildIntern */
	public static final String QUERY_BY_DATUMSCHILDINTERN = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumSchildIntern = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumSchildIntern */
	public static final String QUERY_LIST_BY_DATUMSCHILDINTERN = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumSchildIntern IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bescheinigung */
	public static final String QUERY_BY_BESCHEINIGUNG = "SELECT e FROM DTOSchildVerwaltung e WHERE e.Bescheinigung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bescheinigung */
	public static final String QUERY_LIST_BY_BESCHEINIGUNG = "SELECT e FROM DTOSchildVerwaltung e WHERE e.Bescheinigung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stammblatt */
	public static final String QUERY_BY_STAMMBLATT = "SELECT e FROM DTOSchildVerwaltung e WHERE e.Stammblatt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stammblatt */
	public static final String QUERY_LIST_BY_STAMMBLATT = "SELECT e FROM DTOSchildVerwaltung e WHERE e.Stammblatt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatenGeprueft */
	public static final String QUERY_BY_DATENGEPRUEFT = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatenGeprueft = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatenGeprueft */
	public static final String QUERY_LIST_BY_DATENGEPRUEFT = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatenGeprueft IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Version */
	public static final String QUERY_BY_VERSION = "SELECT e FROM DTOSchildVerwaltung e WHERE e.Version = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Version */
	public static final String QUERY_LIST_BY_VERSION = "SELECT e FROM DTOSchildVerwaltung e WHERE e.Version IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID */
	public static final String QUERY_BY_GU_ID = "SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID */
	public static final String QUERY_LIST_BY_GU_ID = "SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumLoeschfristHinweisDeaktiviert */
	public static final String QUERY_BY_DATUMLOESCHFRISTHINWEISDEAKTIVIERT = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumLoeschfristHinweisDeaktiviert */
	public static final String QUERY_LIST_BY_DATUMLOESCHFRISTHINWEISDEAKTIVIERT = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumLoeschfristHinweisDeaktiviertUserID */
	public static final String QUERY_BY_DATUMLOESCHFRISTHINWEISDEAKTIVIERTUSERID = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviertUserID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumLoeschfristHinweisDeaktiviertUserID */
	public static final String QUERY_LIST_BY_DATUMLOESCHFRISTHINWEISDEAKTIVIERTUSERID = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviertUserID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumDatenGeloescht */
	public static final String QUERY_BY_DATUMDATENGELOESCHT = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumDatenGeloescht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumDatenGeloescht */
	public static final String QUERY_LIST_BY_DATUMDATENGELOESCHT = "SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumDatenGeloescht IN ?1";

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse DTOSchildVerwaltung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchildVerwaltung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchildVerwaltung ohne eine Initialisierung der Attribute.
	 * @param GU_ID   der Wert für das Attribut GU_ID
	 */
	public DTOSchildVerwaltung(final String GU_ID) {
		if (GU_ID == null) {
			throw new NullPointerException("GU_ID must not be null");
		}
		this.GU_ID = GU_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchildVerwaltung other = (DTOSchildVerwaltung) obj;
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
		return "DTOSchildVerwaltung(BackupDatum=" + this.BackupDatum + ", AutoBerechnung=" + this.AutoBerechnung + ", DatumStatkue=" + this.DatumStatkue + ", DatumSchildIntern=" + this.DatumSchildIntern + ", Bescheinigung=" + this.Bescheinigung + ", Stammblatt=" + this.Stammblatt + ", DatenGeprueft=" + this.DatenGeprueft + ", Version=" + this.Version + ", GU_ID=" + this.GU_ID + ", DatumLoeschfristHinweisDeaktiviert=" + this.DatumLoeschfristHinweisDeaktiviert + ", DatumLoeschfristHinweisDeaktiviertUserID=" + this.DatumLoeschfristHinweisDeaktiviertUserID + ", DatumDatenGeloescht=" + this.DatumDatenGeloescht + ")";
	}

}
