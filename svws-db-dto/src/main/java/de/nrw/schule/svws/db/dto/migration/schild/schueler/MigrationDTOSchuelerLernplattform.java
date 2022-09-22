package de.nrw.schule.svws.db.dto.migration.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBoolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nrw.schule.svws.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOSchuelerLernplattformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLernplattform")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.all", query="SELECT e FROM MigrationDTOSchuelerLernplattform e")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.schuelerid", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.SchuelerID = :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.schuelerid.multiple", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.SchuelerID IN :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.lernplattformid", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.LernplattformID = :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.lernplattformid.multiple", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.LernplattformID IN :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.credentialid", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.CredentialID = :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.credentialid.multiple", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.CredentialID IN :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.einwilligungabgefragt", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.EinwilligungAbgefragt = :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.einwilligungabgefragt.multiple", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.EinwilligungAbgefragt IN :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.einwilligungnutzung", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.EinwilligungNutzung = :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.einwilligungnutzung.multiple", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.EinwilligungNutzung IN :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.einwilligungaudiokonferenz", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.EinwilligungAudiokonferenz = :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.einwilligungaudiokonferenz.multiple", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.EinwilligungAudiokonferenz IN :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.einwilligungvideokonferenz", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.EinwilligungVideokonferenz = :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.einwilligungvideokonferenz.multiple", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.EinwilligungVideokonferenz IN :value")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.primaryKeyQuery", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.SchuelerID = ?1 AND e.LernplattformID = ?2")
@NamedQuery(name="MigrationDTOSchuelerLernplattform.all.migration", query="SELECT e FROM MigrationDTOSchuelerLernplattform e WHERE e.SchuelerID IS NOT NULL AND e.LernplattformID IS NOT NULL")
@JsonPropertyOrder({"SchuelerID","LernplattformID","CredentialID","EinwilligungAbgefragt","EinwilligungNutzung","EinwilligungAudiokonferenz","EinwilligungVideokonferenz"})
public class MigrationDTOSchuelerLernplattform {

	/** SchülerID für den Lernplattform-Datensatz */
	@Id
	@Column(name = "SchuelerID")
	@JsonProperty
	public Long SchuelerID;

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
	@Column(name = "EinwilligungAbgefragt")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean EinwilligungAbgefragt;

	/** Einwilligung zur Nutzung liegt vor */
	@Column(name = "EinwilligungNutzung")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean EinwilligungNutzung;

	/** Einwilligung zur Audiokonferenz liegt vor */
	@Column(name = "EinwilligungAudiokonferenz")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean EinwilligungAudiokonferenz;

	/** Einwilligung zur Videokonferenz liegt vor */
	@Column(name = "EinwilligungVideokonferenz")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean EinwilligungVideokonferenz;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerLernplattform ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerLernplattform() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerLernplattform ohne eine Initialisierung der Attribute.
	 * @param SchuelerID   der Wert für das Attribut SchuelerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param EinwilligungAbgefragt   der Wert für das Attribut EinwilligungAbgefragt
	 * @param EinwilligungNutzung   der Wert für das Attribut EinwilligungNutzung
	 * @param EinwilligungAudiokonferenz   der Wert für das Attribut EinwilligungAudiokonferenz
	 * @param EinwilligungVideokonferenz   der Wert für das Attribut EinwilligungVideokonferenz
	 */
	public MigrationDTOSchuelerLernplattform(final Long SchuelerID, final Long LernplattformID, final Boolean EinwilligungAbgefragt, final Boolean EinwilligungNutzung, final Boolean EinwilligungAudiokonferenz, final Boolean EinwilligungVideokonferenz) {
		if (SchuelerID == null) { 
			throw new NullPointerException("SchuelerID must not be null");
		}
		this.SchuelerID = SchuelerID;
		if (LernplattformID == null) { 
			throw new NullPointerException("LernplattformID must not be null");
		}
		this.LernplattformID = LernplattformID;
		if (EinwilligungAbgefragt == null) { 
			throw new NullPointerException("EinwilligungAbgefragt must not be null");
		}
		this.EinwilligungAbgefragt = EinwilligungAbgefragt;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerLernplattform other = (MigrationDTOSchuelerLernplattform) obj;
		if (SchuelerID == null) {
			if (other.SchuelerID != null)
				return false;
		} else if (!SchuelerID.equals(other.SchuelerID))
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
		result = prime * result + ((SchuelerID == null) ? 0 : SchuelerID.hashCode());

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
		return "MigrationDTOSchuelerLernplattform(SchuelerID=" + this.SchuelerID + ", LernplattformID=" + this.LernplattformID + ", CredentialID=" + this.CredentialID + ", EinwilligungAbgefragt=" + this.EinwilligungAbgefragt + ", EinwilligungNutzung=" + this.EinwilligungNutzung + ", EinwilligungAudiokonferenz=" + this.EinwilligungAudiokonferenz + ", EinwilligungVideokonferenz=" + this.EinwilligungVideokonferenz + ")";
	}

}