package de.svws_nrw.db.dto.migration.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOLehrerLernplattformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerLernplattform")
@JsonPropertyOrder({"LehrerID", "LernplattformID", "CredentialID", "EinwilligungenAbgefragt", "EinwilligungNutzung", "EinwilligungAudiokonferenz", "EinwilligungVideokonferenz", "SchulnrEigner"})
public final class MigrationDTOLehrerLernplattform {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrerLernplattform e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LehrerID = ?1 AND e.LernplattformID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LehrerID IS NOT NULL AND e.LernplattformID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrerID */
	public static final String QUERY_BY_LEHRERID = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LehrerID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrerID */
	public static final String QUERY_LIST_BY_LEHRERID = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LehrerID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LernplattformID */
	public static final String QUERY_BY_LERNPLATTFORMID = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LernplattformID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LernplattformID */
	public static final String QUERY_LIST_BY_LERNPLATTFORMID = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LernplattformID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes CredentialID */
	public static final String QUERY_BY_CREDENTIALID = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.CredentialID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes CredentialID */
	public static final String QUERY_LIST_BY_CREDENTIALID = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.CredentialID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinwilligungenAbgefragt */
	public static final String QUERY_BY_EINWILLIGUNGENABGEFRAGT = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungenAbgefragt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinwilligungenAbgefragt */
	public static final String QUERY_LIST_BY_EINWILLIGUNGENABGEFRAGT = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungenAbgefragt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinwilligungNutzung */
	public static final String QUERY_BY_EINWILLIGUNGNUTZUNG = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungNutzung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinwilligungNutzung */
	public static final String QUERY_LIST_BY_EINWILLIGUNGNUTZUNG = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungNutzung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinwilligungAudiokonferenz */
	public static final String QUERY_BY_EINWILLIGUNGAUDIOKONFERENZ = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungAudiokonferenz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinwilligungAudiokonferenz */
	public static final String QUERY_LIST_BY_EINWILLIGUNGAUDIOKONFERENZ = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungAudiokonferenz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinwilligungVideokonferenz */
	public static final String QUERY_BY_EINWILLIGUNGVIDEOKONFERENZ = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungVideokonferenz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinwilligungVideokonferenz */
	public static final String QUERY_LIST_BY_EINWILLIGUNGVIDEOKONFERENZ = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungVideokonferenz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.SchulnrEigner IN ?1";

	/** LehrerID für den Lernplattform-Datensatz */
	@Id
	@Column(name = "LehrerID")
	@JsonProperty
	public Long LehrerID;

	/** ID der Lernplattform */
	@Id
	@Column(name = "LernplattformID")
	@JsonProperty
	public Long LernplattformID;

	/** CredentialD für den Lernplattform-Datensatz */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/** Einwilligung wurde abgefragt */
	@Column(name = "EinwilligungenAbgefragt")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean EinwilligungenAbgefragt;

	/** Einwilligung zur Nutzung liegt vor */
	@Column(name = "EinwilligungNutzung")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean EinwilligungNutzung;

	/** Einwilligung zur Audiokonferenz liegt vor */
	@Column(name = "EinwilligungAudiokonferenz")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean EinwilligungAudiokonferenz;

	/** Einwilligung zur Videokonferenz liegt vor */
	@Column(name = "EinwilligungVideokonferenz")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean EinwilligungVideokonferenz;

	/** DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLernplattform ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerLernplattform() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLernplattform ohne eine Initialisierung der Attribute.
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param EinwilligungenAbgefragt   der Wert für das Attribut EinwilligungenAbgefragt
	 * @param EinwilligungNutzung   der Wert für das Attribut EinwilligungNutzung
	 * @param EinwilligungAudiokonferenz   der Wert für das Attribut EinwilligungAudiokonferenz
	 * @param EinwilligungVideokonferenz   der Wert für das Attribut EinwilligungVideokonferenz
	 */
	public MigrationDTOLehrerLernplattform(final Long LehrerID, final Long LernplattformID, final Boolean EinwilligungenAbgefragt, final Boolean EinwilligungNutzung, final Boolean EinwilligungAudiokonferenz, final Boolean EinwilligungVideokonferenz) {
		if (LehrerID == null) {
			throw new NullPointerException("LehrerID must not be null");
		}
		this.LehrerID = LehrerID;
		if (LernplattformID == null) {
			throw new NullPointerException("LernplattformID must not be null");
		}
		this.LernplattformID = LernplattformID;
		if (EinwilligungenAbgefragt == null) {
			throw new NullPointerException("EinwilligungenAbgefragt must not be null");
		}
		this.EinwilligungenAbgefragt = EinwilligungenAbgefragt;
		if (EinwilligungNutzung == null) {
			throw new NullPointerException("EinwilligungNutzung must not be null");
		}
		this.EinwilligungNutzung = EinwilligungNutzung;
		if (EinwilligungAudiokonferenz == null) {
			throw new NullPointerException("EinwilligungAudiokonferenz must not be null");
		}
		this.EinwilligungAudiokonferenz = EinwilligungAudiokonferenz;
		if (EinwilligungVideokonferenz == null) {
			throw new NullPointerException("EinwilligungVideokonferenz must not be null");
		}
		this.EinwilligungVideokonferenz = EinwilligungVideokonferenz;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOLehrerLernplattform other = (MigrationDTOLehrerLernplattform) obj;
		if (LehrerID == null) {
			if (other.LehrerID != null)
				return false;
		} else if (!LehrerID.equals(other.LehrerID))
			return false;
		if (LernplattformID == null) {
			if (other.LernplattformID != null)
				return false;
		} else if (!LernplattformID.equals(other.LernplattformID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LehrerID == null) ? 0 : LehrerID.hashCode());

		result = prime * result + ((LernplattformID == null) ? 0 : LernplattformID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOLehrerLernplattform(LehrerID=" + this.LehrerID + ", LernplattformID=" + this.LernplattformID + ", CredentialID=" + this.CredentialID + ", EinwilligungenAbgefragt=" + this.EinwilligungenAbgefragt + ", EinwilligungNutzung=" + this.EinwilligungNutzung + ", EinwilligungAudiokonferenz=" + this.EinwilligungAudiokonferenz + ", EinwilligungVideokonferenz=" + this.EinwilligungVideokonferenz + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
