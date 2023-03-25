package de.svws_nrw.db.dto.current.schild.erzieher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle ErzieherLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOErzieherLernplattformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ErzieherLernplattform")
@NamedQuery(name="DTOErzieherLernplattform.all", query="SELECT e FROM DTOErzieherLernplattform e")
@NamedQuery(name="DTOErzieherLernplattform.erzieherid", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.ErzieherID = :value")
@NamedQuery(name="DTOErzieherLernplattform.erzieherid.multiple", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.ErzieherID IN :value")
@NamedQuery(name="DTOErzieherLernplattform.lernplattformid", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.LernplattformID = :value")
@NamedQuery(name="DTOErzieherLernplattform.lernplattformid.multiple", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.LernplattformID IN :value")
@NamedQuery(name="DTOErzieherLernplattform.credentialid", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.CredentialID = :value")
@NamedQuery(name="DTOErzieherLernplattform.credentialid.multiple", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.CredentialID IN :value")
@NamedQuery(name="DTOErzieherLernplattform.einwilligungabgefragt", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.EinwilligungAbgefragt = :value")
@NamedQuery(name="DTOErzieherLernplattform.einwilligungabgefragt.multiple", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.EinwilligungAbgefragt IN :value")
@NamedQuery(name="DTOErzieherLernplattform.einwilligungnutzung", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.EinwilligungNutzung = :value")
@NamedQuery(name="DTOErzieherLernplattform.einwilligungnutzung.multiple", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.EinwilligungNutzung IN :value")
@NamedQuery(name="DTOErzieherLernplattform.einwilligungaudiokonferenz", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.EinwilligungAudiokonferenz = :value")
@NamedQuery(name="DTOErzieherLernplattform.einwilligungaudiokonferenz.multiple", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.EinwilligungAudiokonferenz IN :value")
@NamedQuery(name="DTOErzieherLernplattform.einwilligungvideokonferenz", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.EinwilligungVideokonferenz = :value")
@NamedQuery(name="DTOErzieherLernplattform.einwilligungvideokonferenz.multiple", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.EinwilligungVideokonferenz IN :value")
@NamedQuery(name="DTOErzieherLernplattform.primaryKeyQuery", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.ErzieherID = ?1 AND e.LernplattformID = ?2")
@NamedQuery(name="DTOErzieherLernplattform.all.migration", query="SELECT e FROM DTOErzieherLernplattform e WHERE e.ErzieherID IS NOT NULL AND e.LernplattformID IS NOT NULL")
@JsonPropertyOrder({"ErzieherID","LernplattformID","CredentialID","EinwilligungAbgefragt","EinwilligungNutzung","EinwilligungAudiokonferenz","EinwilligungVideokonferenz"})
public class DTOErzieherLernplattform {

	/** ErzieherID für den Lernplattform-Datensatz */
	@Id
	@Column(name = "ErzieherID")
	@JsonProperty
	public Long ErzieherID;

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
	 * Erstellt ein neues Objekt der Klasse DTOErzieherLernplattform ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOErzieherLernplattform() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherLernplattform ohne eine Initialisierung der Attribute.
	 * @param ErzieherID   der Wert für das Attribut ErzieherID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param EinwilligungAbgefragt   der Wert für das Attribut EinwilligungAbgefragt
	 * @param EinwilligungNutzung   der Wert für das Attribut EinwilligungNutzung
	 * @param EinwilligungAudiokonferenz   der Wert für das Attribut EinwilligungAudiokonferenz
	 * @param EinwilligungVideokonferenz   der Wert für das Attribut EinwilligungVideokonferenz
	 */
	public DTOErzieherLernplattform(final Long ErzieherID, final Long LernplattformID, final Boolean EinwilligungAbgefragt, final Boolean EinwilligungNutzung, final Boolean EinwilligungAudiokonferenz, final Boolean EinwilligungVideokonferenz) {
		if (ErzieherID == null) { 
			throw new NullPointerException("ErzieherID must not be null");
		}
		this.ErzieherID = ErzieherID;
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
		DTOErzieherLernplattform other = (DTOErzieherLernplattform) obj;
		if (ErzieherID == null) {
			if (other.ErzieherID != null)
				return false;
		} else if (!ErzieherID.equals(other.ErzieherID))
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
		result = prime * result + ((ErzieherID == null) ? 0 : ErzieherID.hashCode());

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
		return "DTOErzieherLernplattform(ErzieherID=" + this.ErzieherID + ", LernplattformID=" + this.LernplattformID + ", CredentialID=" + this.CredentialID + ", EinwilligungAbgefragt=" + this.EinwilligungAbgefragt + ", EinwilligungNutzung=" + this.EinwilligungNutzung + ", EinwilligungAudiokonferenz=" + this.EinwilligungAudiokonferenz + ", EinwilligungVideokonferenz=" + this.EinwilligungVideokonferenz + ")";
	}

}