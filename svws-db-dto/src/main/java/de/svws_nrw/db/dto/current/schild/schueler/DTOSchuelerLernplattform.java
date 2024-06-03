package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOSchuelerLernplattformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLernplattform")
@JsonPropertyOrder({"SchuelerID", "LernplattformID", "CredentialID", "EinwilligungAbgefragt", "EinwilligungNutzung", "EinwilligungAudiokonferenz", "EinwilligungVideokonferenz"})
public final class DTOSchuelerLernplattform {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerLernplattform e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.SchuelerID = ?1 AND e.LernplattformID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.SchuelerID IS NOT NULL AND e.LernplattformID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchuelerID */
	public static final String QUERY_BY_SCHUELERID = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.SchuelerID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchuelerID */
	public static final String QUERY_LIST_BY_SCHUELERID = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.SchuelerID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LernplattformID */
	public static final String QUERY_BY_LERNPLATTFORMID = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.LernplattformID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LernplattformID */
	public static final String QUERY_LIST_BY_LERNPLATTFORMID = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.LernplattformID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes CredentialID */
	public static final String QUERY_BY_CREDENTIALID = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.CredentialID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes CredentialID */
	public static final String QUERY_LIST_BY_CREDENTIALID = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.CredentialID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinwilligungAbgefragt */
	public static final String QUERY_BY_EINWILLIGUNGABGEFRAGT = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.EinwilligungAbgefragt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinwilligungAbgefragt */
	public static final String QUERY_LIST_BY_EINWILLIGUNGABGEFRAGT = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.EinwilligungAbgefragt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinwilligungNutzung */
	public static final String QUERY_BY_EINWILLIGUNGNUTZUNG = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.EinwilligungNutzung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinwilligungNutzung */
	public static final String QUERY_LIST_BY_EINWILLIGUNGNUTZUNG = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.EinwilligungNutzung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinwilligungAudiokonferenz */
	public static final String QUERY_BY_EINWILLIGUNGAUDIOKONFERENZ = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.EinwilligungAudiokonferenz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinwilligungAudiokonferenz */
	public static final String QUERY_LIST_BY_EINWILLIGUNGAUDIOKONFERENZ = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.EinwilligungAudiokonferenz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinwilligungVideokonferenz */
	public static final String QUERY_BY_EINWILLIGUNGVIDEOKONFERENZ = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.EinwilligungVideokonferenz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinwilligungVideokonferenz */
	public static final String QUERY_LIST_BY_EINWILLIGUNGVIDEOKONFERENZ = "SELECT e FROM DTOSchuelerLernplattform e WHERE e.EinwilligungVideokonferenz IN ?1";

	/** SchülerID für den Lernplattform-Datensatz */
	@Id
	@Column(name = "SchuelerID")
	@JsonProperty
	public long SchuelerID;

	/** ID der Lernplattform */
	@Id
	@Column(name = "LernplattformID")
	@JsonProperty
	public long LernplattformID;

	/** CredentialD für den Lernplattform-Datensatz */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/** Einwilligung wurde abgefragt */
	@Column(name = "EinwilligungAbgefragt")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean EinwilligungAbgefragt;

	/** Einwilligung zur Nutzung liegt vor */
	@Column(name = "EinwilligungNutzung")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean EinwilligungNutzung;

	/** Einwilligung zur Audiokonferenz liegt vor */
	@Column(name = "EinwilligungAudiokonferenz")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean EinwilligungAudiokonferenz;

	/** Einwilligung zur Videokonferenz liegt vor */
	@Column(name = "EinwilligungVideokonferenz")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean EinwilligungVideokonferenz;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLernplattform ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerLernplattform() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLernplattform ohne eine Initialisierung der Attribute.
	 * @param SchuelerID   der Wert für das Attribut SchuelerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param EinwilligungAbgefragt   der Wert für das Attribut EinwilligungAbgefragt
	 * @param EinwilligungNutzung   der Wert für das Attribut EinwilligungNutzung
	 * @param EinwilligungAudiokonferenz   der Wert für das Attribut EinwilligungAudiokonferenz
	 * @param EinwilligungVideokonferenz   der Wert für das Attribut EinwilligungVideokonferenz
	 */
	public DTOSchuelerLernplattform(final long SchuelerID, final long LernplattformID, final Boolean EinwilligungAbgefragt, final Boolean EinwilligungNutzung, final Boolean EinwilligungAudiokonferenz, final Boolean EinwilligungVideokonferenz) {
		this.SchuelerID = SchuelerID;
		this.LernplattformID = LernplattformID;
		this.EinwilligungAbgefragt = EinwilligungAbgefragt;
		this.EinwilligungNutzung = EinwilligungNutzung;
		this.EinwilligungAudiokonferenz = EinwilligungAudiokonferenz;
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
		DTOSchuelerLernplattform other = (DTOSchuelerLernplattform) obj;
		if (SchuelerID != other.SchuelerID)
			return false;
		return LernplattformID == other.LernplattformID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(SchuelerID);

		result = prime * result + Long.hashCode(LernplattformID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerLernplattform(SchuelerID=" + this.SchuelerID + ", LernplattformID=" + this.LernplattformID + ", CredentialID=" + this.CredentialID + ", EinwilligungAbgefragt=" + this.EinwilligungAbgefragt + ", EinwilligungNutzung=" + this.EinwilligungNutzung + ", EinwilligungAudiokonferenz=" + this.EinwilligungAudiokonferenz + ", EinwilligungVideokonferenz=" + this.EinwilligungVideokonferenz + ")";
	}

}
