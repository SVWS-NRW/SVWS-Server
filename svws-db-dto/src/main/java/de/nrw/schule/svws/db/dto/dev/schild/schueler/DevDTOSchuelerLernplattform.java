package de.nrw.schule.svws.db.dto.dev.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DevDTOSchuelerLernplattformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLernplattform")
@NamedQuery(name="DevDTOSchuelerLernplattform.all", query="SELECT e FROM DevDTOSchuelerLernplattform e")
@NamedQuery(name="DevDTOSchuelerLernplattform.schuelerid", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.SchuelerID = :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.schuelerid.multiple", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.SchuelerID IN :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.lernplattformid", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.LernplattformID = :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.lernplattformid.multiple", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.LernplattformID IN :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.credentialid", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.CredentialID = :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.credentialid.multiple", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.CredentialID IN :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.einwilligungabgefragt", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.EinwilligungAbgefragt = :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.einwilligungabgefragt.multiple", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.EinwilligungAbgefragt IN :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.einwilligungnutzung", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.EinwilligungNutzung = :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.einwilligungnutzung.multiple", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.EinwilligungNutzung IN :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.einwilligungaudiokonferenz", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.EinwilligungAudiokonferenz = :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.einwilligungaudiokonferenz.multiple", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.EinwilligungAudiokonferenz IN :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.einwilligungvideokonferenz", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.EinwilligungVideokonferenz = :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.einwilligungvideokonferenz.multiple", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.EinwilligungVideokonferenz IN :value")
@NamedQuery(name="DevDTOSchuelerLernplattform.primaryKeyQuery", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.SchuelerID = ?1 AND e.LernplattformID = ?2")
@NamedQuery(name="DevDTOSchuelerLernplattform.all.migration", query="SELECT e FROM DevDTOSchuelerLernplattform e WHERE e.SchuelerID IS NOT NULL AND e.LernplattformID IS NOT NULL")
@JsonPropertyOrder({"SchuelerID","LernplattformID","CredentialID","EinwilligungAbgefragt","EinwilligungNutzung","EinwilligungAudiokonferenz","EinwilligungVideokonferenz"})
public class DevDTOSchuelerLernplattform {

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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean EinwilligungAbgefragt;

	/** Einwilligung zur Nutzung liegt vor */
	@Column(name = "EinwilligungNutzung")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean EinwilligungNutzung;

	/** Einwilligung zur Audiokonferenz liegt vor */
	@Column(name = "EinwilligungAudiokonferenz")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean EinwilligungAudiokonferenz;

	/** Einwilligung zur Videokonferenz liegt vor */
	@Column(name = "EinwilligungVideokonferenz")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean EinwilligungVideokonferenz;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerLernplattform ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchuelerLernplattform() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerLernplattform ohne eine Initialisierung der Attribute.
	 * @param SchuelerID   der Wert für das Attribut SchuelerID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param EinwilligungAbgefragt   der Wert für das Attribut EinwilligungAbgefragt
	 * @param EinwilligungNutzung   der Wert für das Attribut EinwilligungNutzung
	 * @param EinwilligungAudiokonferenz   der Wert für das Attribut EinwilligungAudiokonferenz
	 * @param EinwilligungVideokonferenz   der Wert für das Attribut EinwilligungVideokonferenz
	 */
	public DevDTOSchuelerLernplattform(final Long SchuelerID, final Long LernplattformID, final Boolean EinwilligungAbgefragt, final Boolean EinwilligungNutzung, final Boolean EinwilligungAudiokonferenz, final Boolean EinwilligungVideokonferenz) {
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
		DevDTOSchuelerLernplattform other = (DevDTOSchuelerLernplattform) obj;
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
		return "DevDTOSchuelerLernplattform(SchuelerID=" + this.SchuelerID + ", LernplattformID=" + this.LernplattformID + ", CredentialID=" + this.CredentialID + ", EinwilligungAbgefragt=" + this.EinwilligungAbgefragt + ", EinwilligungNutzung=" + this.EinwilligungNutzung + ", EinwilligungAudiokonferenz=" + this.EinwilligungAudiokonferenz + ", EinwilligungVideokonferenz=" + this.EinwilligungVideokonferenz + ")";
	}

}