package de.nrw.schule.svws.db.dto.rev8.schild.erzieher;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle ErzieherLernplattform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOErzieherLernplattformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ErzieherLernplattform")
@NamedQuery(name="Rev8DTOErzieherLernplattform.all", query="SELECT e FROM Rev8DTOErzieherLernplattform e")
@NamedQuery(name="Rev8DTOErzieherLernplattform.erzieherid", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.ErzieherID = :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.erzieherid.multiple", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.ErzieherID IN :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.lernplattformid", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.LernplattformID = :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.lernplattformid.multiple", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.LernplattformID IN :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.credentialid", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.CredentialID = :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.credentialid.multiple", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.CredentialID IN :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.einwilligungabgefragt", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.EinwilligungAbgefragt = :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.einwilligungabgefragt.multiple", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.EinwilligungAbgefragt IN :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.einwilligungnutzung", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.EinwilligungNutzung = :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.einwilligungnutzung.multiple", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.EinwilligungNutzung IN :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.einwilligungaudiokonferenz", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.EinwilligungAudiokonferenz = :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.einwilligungaudiokonferenz.multiple", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.EinwilligungAudiokonferenz IN :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.einwilligungvideokonferenz", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.EinwilligungVideokonferenz = :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.einwilligungvideokonferenz.multiple", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.EinwilligungVideokonferenz IN :value")
@NamedQuery(name="Rev8DTOErzieherLernplattform.primaryKeyQuery", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.ErzieherID = ?1 AND e.LernplattformID = ?2")
@NamedQuery(name="Rev8DTOErzieherLernplattform.all.migration", query="SELECT e FROM Rev8DTOErzieherLernplattform e WHERE e.ErzieherID IS NOT NULL AND e.LernplattformID IS NOT NULL")
@JsonPropertyOrder({"ErzieherID","LernplattformID","CredentialID","EinwilligungAbgefragt","EinwilligungNutzung","EinwilligungAudiokonferenz","EinwilligungVideokonferenz"})
public class Rev8DTOErzieherLernplattform {

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOErzieherLernplattform ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOErzieherLernplattform() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOErzieherLernplattform ohne eine Initialisierung der Attribute.
	 * @param ErzieherID   der Wert für das Attribut ErzieherID
	 * @param LernplattformID   der Wert für das Attribut LernplattformID
	 * @param EinwilligungAbgefragt   der Wert für das Attribut EinwilligungAbgefragt
	 * @param EinwilligungNutzung   der Wert für das Attribut EinwilligungNutzung
	 * @param EinwilligungAudiokonferenz   der Wert für das Attribut EinwilligungAudiokonferenz
	 * @param EinwilligungVideokonferenz   der Wert für das Attribut EinwilligungVideokonferenz
	 */
	public Rev8DTOErzieherLernplattform(final Long ErzieherID, final Long LernplattformID, final Boolean EinwilligungAbgefragt, final Boolean EinwilligungNutzung, final Boolean EinwilligungAudiokonferenz, final Boolean EinwilligungVideokonferenz) {
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
		Rev8DTOErzieherLernplattform other = (Rev8DTOErzieherLernplattform) obj;
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
		return "Rev8DTOErzieherLernplattform(ErzieherID=" + this.ErzieherID + ", LernplattformID=" + this.LernplattformID + ", CredentialID=" + this.CredentialID + ", EinwilligungAbgefragt=" + this.EinwilligungAbgefragt + ", EinwilligungNutzung=" + this.EinwilligungNutzung + ", EinwilligungAudiokonferenz=" + this.EinwilligungAudiokonferenz + ", EinwilligungVideokonferenz=" + this.EinwilligungVideokonferenz + ")";
	}

}