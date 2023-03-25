package de.svws_nrw.db.dto.current.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Fach_Gliederungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOFachgliederungenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Fach_Gliederungen")
@NamedQuery(name="DTOFachgliederungen.all", query="SELECT e FROM DTOFachgliederungen e")
@NamedQuery(name="DTOFachgliederungen.fach_id", query="SELECT e FROM DTOFachgliederungen e WHERE e.Fach_ID = :value")
@NamedQuery(name="DTOFachgliederungen.fach_id.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DTOFachgliederungen.gliederung", query="SELECT e FROM DTOFachgliederungen e WHERE e.Gliederung = :value")
@NamedQuery(name="DTOFachgliederungen.gliederung.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.Gliederung IN :value")
@NamedQuery(name="DTOFachgliederungen.faechergruppe", query="SELECT e FROM DTOFachgliederungen e WHERE e.Faechergruppe = :value")
@NamedQuery(name="DTOFachgliederungen.faechergruppe.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.Faechergruppe IN :value")
@NamedQuery(name="DTOFachgliederungen.gewichtungab", query="SELECT e FROM DTOFachgliederungen e WHERE e.GewichtungAB = :value")
@NamedQuery(name="DTOFachgliederungen.gewichtungab.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.GewichtungAB IN :value")
@NamedQuery(name="DTOFachgliederungen.gewichtungbb", query="SELECT e FROM DTOFachgliederungen e WHERE e.GewichtungBB = :value")
@NamedQuery(name="DTOFachgliederungen.gewichtungbb.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.GewichtungBB IN :value")
@NamedQuery(name="DTOFachgliederungen.schriftlichab", query="SELECT e FROM DTOFachgliederungen e WHERE e.SchriftlichAB = :value")
@NamedQuery(name="DTOFachgliederungen.schriftlichab.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.SchriftlichAB IN :value")
@NamedQuery(name="DTOFachgliederungen.schriftlichbb", query="SELECT e FROM DTOFachgliederungen e WHERE e.SchriftlichBB = :value")
@NamedQuery(name="DTOFachgliederungen.schriftlichbb.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.SchriftlichBB IN :value")
@NamedQuery(name="DTOFachgliederungen.gymosfach", query="SELECT e FROM DTOFachgliederungen e WHERE e.GymOSFach = :value")
@NamedQuery(name="DTOFachgliederungen.gymosfach.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.GymOSFach IN :value")
@NamedQuery(name="DTOFachgliederungen.zeugnisbez", query="SELECT e FROM DTOFachgliederungen e WHERE e.ZeugnisBez = :value")
@NamedQuery(name="DTOFachgliederungen.zeugnisbez.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.ZeugnisBez IN :value")
@NamedQuery(name="DTOFachgliederungen.lernfelder", query="SELECT e FROM DTOFachgliederungen e WHERE e.Lernfelder = :value")
@NamedQuery(name="DTOFachgliederungen.lernfelder.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.Lernfelder IN :value")
@NamedQuery(name="DTOFachgliederungen.fachklasse_id", query="SELECT e FROM DTOFachgliederungen e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name="DTOFachgliederungen.fachklasse_id.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name="DTOFachgliederungen.sortierung", query="SELECT e FROM DTOFachgliederungen e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOFachgliederungen.sortierung.multiple", query="SELECT e FROM DTOFachgliederungen e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOFachgliederungen.primaryKeyQuery", query="SELECT e FROM DTOFachgliederungen e WHERE e.Fach_ID = ?1 AND e.Fachklasse_ID = ?2")
@NamedQuery(name="DTOFachgliederungen.all.migration", query="SELECT e FROM DTOFachgliederungen e WHERE e.Fach_ID IS NOT NULL AND e.Fachklasse_ID IS NOT NULL")
@JsonPropertyOrder({"Fach_ID","Gliederung","Faechergruppe","GewichtungAB","GewichtungBB","SchriftlichAB","SchriftlichBB","GymOSFach","ZeugnisBez","Lernfelder","Fachklasse_ID","Sortierung"})
public class DTOFachgliederungen {

	/** ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** SGL für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Gliederung")
	@JsonProperty
	public String Gliederung;

	/** Fächergruppe für gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Faechergruppe")
	@JsonProperty
	public Integer Faechergruppe;

	/** Gewichtung Allgemeinbildend für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "GewichtungAB")
	@JsonProperty
	public Integer GewichtungAB;

	/** Gewichtung Berufsbezogen für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "GewichtungBB")
	@JsonProperty
	public Integer GewichtungBB;

	/** Ist schriftliches Fach Allgemeinbildend für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "SchriftlichAB")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchriftlichAB;

	/** Ist schriftliches Fach Berufsbildend für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "SchriftlichBB")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchriftlichBB;

	/** Ist Fach der GymOB für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "GymOSFach")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean GymOSFach;

	/** Zeugnisbezeihnung für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "ZeugnisBez")
	@JsonProperty
	public String ZeugnisBez;

	/** Lernfelder für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Lernfelder")
	@JsonProperty
	public String Lernfelder;

	/** Fachklassen ID für die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Id
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Sortierung dfür die gliederungsbezogenen Einstellungen zum Fach (BK) */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachgliederungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFachgliederungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachgliederungen ohne eine Initialisierung der Attribute.
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Gliederung   der Wert für das Attribut Gliederung
	 * @param Fachklasse_ID   der Wert für das Attribut Fachklasse_ID
	 */
	public DTOFachgliederungen(final Long Fach_ID, final String Gliederung, final Long Fachklasse_ID) {
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
		if (Gliederung == null) { 
			throw new NullPointerException("Gliederung must not be null");
		}
		this.Gliederung = Gliederung;
		if (Fachklasse_ID == null) { 
			throw new NullPointerException("Fachklasse_ID must not be null");
		}
		this.Fachklasse_ID = Fachklasse_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOFachgliederungen other = (DTOFachgliederungen) obj;
		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;

		if (Fachklasse_ID == null) {
			if (other.Fachklasse_ID != null)
				return false;
		} else if (!Fachklasse_ID.equals(other.Fachklasse_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());

		result = prime * result + ((Fachklasse_ID == null) ? 0 : Fachklasse_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOFachgliederungen(Fach_ID=" + this.Fach_ID + ", Gliederung=" + this.Gliederung + ", Faechergruppe=" + this.Faechergruppe + ", GewichtungAB=" + this.GewichtungAB + ", GewichtungBB=" + this.GewichtungBB + ", SchriftlichAB=" + this.SchriftlichAB + ", SchriftlichBB=" + this.SchriftlichBB + ", GymOSFach=" + this.GymOSFach + ", ZeugnisBez=" + this.ZeugnisBez + ", Lernfelder=" + this.Lernfelder + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Sortierung=" + this.Sortierung + ")";
	}

}