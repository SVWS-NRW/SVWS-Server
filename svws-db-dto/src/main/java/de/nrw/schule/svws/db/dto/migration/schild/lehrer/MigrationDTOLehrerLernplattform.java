package de.nrw.schule.svws.db.dto.migration.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOLehrerLernplattformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerLernplattform")
@NamedQuery(name="MigrationDTOLehrerLernplattform.all", query="SELECT e FROM MigrationDTOLehrerLernplattform e")
@NamedQuery(name="MigrationDTOLehrerLernplattform.lehrerid", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LehrerID = :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.lehrerid.multiple", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LehrerID IN :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.lernplattformid", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LernplattformID = :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.lernplattformid.multiple", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LernplattformID IN :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.credentialid", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.CredentialID = :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.credentialid.multiple", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.CredentialID IN :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.einwilligungabgefragt", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungAbgefragt = :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.einwilligungabgefragt.multiple", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungAbgefragt IN :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.einwilligungnutzung", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungNutzung = :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.einwilligungnutzung.multiple", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungNutzung IN :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.einwilligungaudiokonferenz", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungAudiokonferenz = :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.einwilligungaudiokonferenz.multiple", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungAudiokonferenz IN :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.einwilligungvideokonferenz", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungVideokonferenz = :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.einwilligungvideokonferenz.multiple", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.EinwilligungVideokonferenz IN :value")
@NamedQuery(name="MigrationDTOLehrerLernplattform.primaryKeyQuery", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LehrerID = ?1 AND e.LernplattformID = ?2")
@NamedQuery(name="MigrationDTOLehrerLernplattform.all.migration", query="SELECT e FROM MigrationDTOLehrerLernplattform e WHERE e.LehrerID IS NOT NULL AND e.LernplattformID IS NOT NULL")
@JsonPropertyOrder({"LehrerID","LernplattformID","CredentialID","EinwilligungAbgefragt","EinwilligungNutzung","EinwilligungAudiokonferenz","EinwilligungVideokonferenz"})
public class MigrationDTOLehrerLernplattform {

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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLernplattform ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerLernplattform() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLernplattform ohne eine Initialisierung der Attribute.
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param EinwilligungAbgefragt   der Wert für das Attribut EinwilligungAbgefragt
	 * @param EinwilligungNutzung   der Wert für das Attribut EinwilligungNutzung
	 * @param EinwilligungAudiokonferenz   der Wert für das Attribut EinwilligungAudiokonferenz
	 * @param EinwilligungVideokonferenz   der Wert für das Attribut EinwilligungVideokonferenz
	 */
	public MigrationDTOLehrerLernplattform(final Long LehrerID, final Long LernplattformID, final Boolean EinwilligungAbgefragt, final Boolean EinwilligungNutzung, final Boolean EinwilligungAudiokonferenz, final Boolean EinwilligungVideokonferenz) {
		if (LehrerID == null) { 
			throw new NullPointerException("LehrerID must not be null");
		}
		this.LehrerID = LehrerID;
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
		return "MigrationDTOLehrerLernplattform(LehrerID=" + this.LehrerID + ", LernplattformID=" + this.LernplattformID + ", CredentialID=" + this.CredentialID + ", EinwilligungAbgefragt=" + this.EinwilligungAbgefragt + ", EinwilligungNutzung=" + this.EinwilligungNutzung + ", EinwilligungAudiokonferenz=" + this.EinwilligungAudiokonferenz + ", EinwilligungVideokonferenz=" + this.EinwilligungVideokonferenz + ")";
	}

}