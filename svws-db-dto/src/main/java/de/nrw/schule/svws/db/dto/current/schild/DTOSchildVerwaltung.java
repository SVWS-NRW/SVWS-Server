package de.nrw.schule.svws.db.dto.current.schild;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schild_Verwaltung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schild_Verwaltung")
@NamedQuery(name="DTOSchildVerwaltung.all", query="SELECT e FROM DTOSchildVerwaltung e")
@NamedQuery(name="DTOSchildVerwaltung.backupdatum", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.BackupDatum = :value")
@NamedQuery(name="DTOSchildVerwaltung.backupdatum.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.BackupDatum IN :value")
@NamedQuery(name="DTOSchildVerwaltung.autoberechnung", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.AutoBerechnung = :value")
@NamedQuery(name="DTOSchildVerwaltung.autoberechnung.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.AutoBerechnung IN :value")
@NamedQuery(name="DTOSchildVerwaltung.datumstatkue", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumStatkue = :value")
@NamedQuery(name="DTOSchildVerwaltung.datumstatkue.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumStatkue IN :value")
@NamedQuery(name="DTOSchildVerwaltung.datumschildintern", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumSchildIntern = :value")
@NamedQuery(name="DTOSchildVerwaltung.datumschildintern.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumSchildIntern IN :value")
@NamedQuery(name="DTOSchildVerwaltung.bescheinigung", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.Bescheinigung = :value")
@NamedQuery(name="DTOSchildVerwaltung.bescheinigung.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.Bescheinigung IN :value")
@NamedQuery(name="DTOSchildVerwaltung.stammblatt", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.Stammblatt = :value")
@NamedQuery(name="DTOSchildVerwaltung.stammblatt.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.Stammblatt IN :value")
@NamedQuery(name="DTOSchildVerwaltung.datengeprueft", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatenGeprueft = :value")
@NamedQuery(name="DTOSchildVerwaltung.datengeprueft.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatenGeprueft IN :value")
@NamedQuery(name="DTOSchildVerwaltung.version", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.Version = :value")
@NamedQuery(name="DTOSchildVerwaltung.version.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.Version IN :value")
@NamedQuery(name="DTOSchildVerwaltung.gu_id", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID = :value")
@NamedQuery(name="DTOSchildVerwaltung.gu_id.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID IN :value")
@NamedQuery(name="DTOSchildVerwaltung.datumloeschfristhinweisdeaktiviert", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviert = :value")
@NamedQuery(name="DTOSchildVerwaltung.datumloeschfristhinweisdeaktiviert.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviert IN :value")
@NamedQuery(name="DTOSchildVerwaltung.datumloeschfristhinweisdeaktiviertuserid", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviertUserID = :value")
@NamedQuery(name="DTOSchildVerwaltung.datumloeschfristhinweisdeaktiviertuserid.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumLoeschfristHinweisDeaktiviertUserID IN :value")
@NamedQuery(name="DTOSchildVerwaltung.datumdatengeloescht", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumDatenGeloescht = :value")
@NamedQuery(name="DTOSchildVerwaltung.datumdatengeloescht.multiple", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.DatumDatenGeloescht IN :value")
@NamedQuery(name="DTOSchildVerwaltung.primaryKeyQuery", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID = ?1")
@NamedQuery(name="DTOSchildVerwaltung.all.migration", query="SELECT e FROM DTOSchildVerwaltung e WHERE e.GU_ID IS NOT NULL")
@JsonPropertyOrder({"BackupDatum","AutoBerechnung","DatumStatkue","DatumSchildIntern","Bescheinigung","Stammblatt","DatenGeprueft","Version","GU_ID","DatumLoeschfristHinweisDeaktiviert","DatumLoeschfristHinweisDeaktiviertUserID","DatumDatenGeloescht"})
public class DTOSchildVerwaltung {

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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	public boolean equals(Object obj) {
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