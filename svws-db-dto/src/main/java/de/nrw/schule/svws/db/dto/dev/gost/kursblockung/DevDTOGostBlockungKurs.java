package de.nrw.schule.svws.db.dto.dev.gost.kursblockung;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.gost.GOStKursartConverter;

import de.nrw.schule.svws.core.types.gost.GostKursart;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStKursartConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStKursartConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Kurse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Kurse")
@NamedQuery(name="DevDTOGostBlockungKurs.all", query="SELECT e FROM DevDTOGostBlockungKurs e")
@NamedQuery(name="DevDTOGostBlockungKurs.id", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.ID = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.id.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.blockung_id", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Blockung_ID = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.blockung_id.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Blockung_ID IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.fach_id", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Fach_ID = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.fach_id.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.kursart", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Kursart = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.kursart.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Kursart IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.kursnummer", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Kursnummer = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.kursnummer.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Kursnummer IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.istkoopkurs", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.IstKoopKurs = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.istkoopkurs.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.IstKoopKurs IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.bezeichnungsuffix", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.BezeichnungSuffix = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.bezeichnungsuffix.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.BezeichnungSuffix IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.schienenanzahl", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Schienenanzahl = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.schienenanzahl.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Schienenanzahl IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.wochenstunden", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Wochenstunden = :value")
@NamedQuery(name="DevDTOGostBlockungKurs.wochenstunden.multiple", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.Wochenstunden IN :value")
@NamedQuery(name="DevDTOGostBlockungKurs.primaryKeyQuery", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOGostBlockungKurs.all.migration", query="SELECT e FROM DevDTOGostBlockungKurs e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Blockung_ID","Fach_ID","Kursart","Kursnummer","IstKoopKurs","BezeichnungSuffix","Schienenanzahl","Wochenstunden"})
public class DevDTOGostBlockungKurs {

	/** ID des Kurses in der Blockung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Blockung */
	@Column(name = "Blockung_ID")
	@JsonProperty
	public Long Blockung_ID;

	/** ID des Faches */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** ID der Kursart (siehe ID des Core-Types GostKursart) */
	@Column(name = "Kursart")
	@JsonProperty
	@Convert(converter=GOStKursartConverter.class)
	@JsonSerialize(using=GOStKursartConverterSerializer.class)
	@JsonDeserialize(using=GOStKursartConverterDeserializer.class)
	public GostKursart Kursart;

	/** Die Nummer des Kurses in Bezug auf das Fach (Kurse eines Faches sind in einer Blockung üblicherweise von 1 ab durchnummeriert) */
	@Column(name = "Kursnummer")
	@JsonProperty
	public Integer Kursnummer;

	/** Gibt an, ob es sich um einen Kooperations-Kurs mit einer anderen Schule handelt oder nicht: 1 - true, 0 - false  */
	@Column(name = "IstKoopKurs")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstKoopKurs;

	/** Ein Suffix, welches der Kursbezeichnung ggf. angehangen wird (kann z.B. zum Kennzeichnen von Kooperationskursen verwendet werden) */
	@Column(name = "BezeichnungSuffix")
	@JsonProperty
	public String BezeichnungSuffix;

	/** Gibt die Anzahl der Schienen an, die für den Kurs in der Blockung verwendet werden soll (normalerweise 1) */
	@Column(name = "Schienenanzahl")
	@JsonProperty
	public Integer Schienenanzahl;

	/** Die Anzahl der Wochenstunden für den Kurs */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public Integer Wochenstunden;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostBlockungKurs ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostBlockungKurs() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostBlockungKurs ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Blockung_ID   der Wert für das Attribut Blockung_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Kursart   der Wert für das Attribut Kursart
	 * @param Kursnummer   der Wert für das Attribut Kursnummer
	 * @param IstKoopKurs   der Wert für das Attribut IstKoopKurs
	 * @param Schienenanzahl   der Wert für das Attribut Schienenanzahl
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 */
	public DevDTOGostBlockungKurs(final Long ID, final Long Blockung_ID, final Long Fach_ID, final GostKursart Kursart, final Integer Kursnummer, final Boolean IstKoopKurs, final Integer Schienenanzahl, final Integer Wochenstunden) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Blockung_ID == null) { 
			throw new NullPointerException("Blockung_ID must not be null");
		}
		this.Blockung_ID = Blockung_ID;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
		if (Kursart == null) { 
			throw new NullPointerException("Kursart must not be null");
		}
		this.Kursart = Kursart;
		if (Kursnummer == null) { 
			throw new NullPointerException("Kursnummer must not be null");
		}
		this.Kursnummer = Kursnummer;
		if (IstKoopKurs == null) { 
			throw new NullPointerException("IstKoopKurs must not be null");
		}
		this.IstKoopKurs = IstKoopKurs;
		if (Schienenanzahl == null) { 
			throw new NullPointerException("Schienenanzahl must not be null");
		}
		this.Schienenanzahl = Schienenanzahl;
		if (Wochenstunden == null) { 
			throw new NullPointerException("Wochenstunden must not be null");
		}
		this.Wochenstunden = Wochenstunden;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOGostBlockungKurs other = (DevDTOGostBlockungKurs) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DevDTOGostBlockungKurs(ID=" + this.ID + ", Blockung_ID=" + this.Blockung_ID + ", Fach_ID=" + this.Fach_ID + ", Kursart=" + this.Kursart + ", Kursnummer=" + this.Kursnummer + ", IstKoopKurs=" + this.IstKoopKurs + ", BezeichnungSuffix=" + this.BezeichnungSuffix + ", Schienenanzahl=" + this.Schienenanzahl + ", Wochenstunden=" + this.Wochenstunden + ")";
	}

}