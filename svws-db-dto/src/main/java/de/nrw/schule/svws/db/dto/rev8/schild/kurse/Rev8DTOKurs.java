package de.nrw.schule.svws.db.dto.rev8.schild.kurse;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.current.KursFortschreibungsartConverter;

import de.nrw.schule.svws.core.types.KursFortschreibungsart;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.KursFortschreibungsartConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.KursFortschreibungsartConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Kurse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Kurse")
@NamedQuery(name="Rev8DTOKurs.all", query="SELECT e FROM Rev8DTOKurs e")
@NamedQuery(name="Rev8DTOKurs.id", query="SELECT e FROM Rev8DTOKurs e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKurs.id.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKurs.schuljahresabschnitts_id", query="SELECT e FROM Rev8DTOKurs e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="Rev8DTOKurs.schuljahresabschnitts_id.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="Rev8DTOKurs.kurzbez", query="SELECT e FROM Rev8DTOKurs e WHERE e.KurzBez = :value")
@NamedQuery(name="Rev8DTOKurs.kurzbez.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.KurzBez IN :value")
@NamedQuery(name="Rev8DTOKurs.jahrgang_id", query="SELECT e FROM Rev8DTOKurs e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name="Rev8DTOKurs.jahrgang_id.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name="Rev8DTOKurs.asdjahrgang", query="SELECT e FROM Rev8DTOKurs e WHERE e.ASDJahrgang = :value")
@NamedQuery(name="Rev8DTOKurs.asdjahrgang.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name="Rev8DTOKurs.fach_id", query="SELECT e FROM Rev8DTOKurs e WHERE e.Fach_ID = :value")
@NamedQuery(name="Rev8DTOKurs.fach_id.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Fach_ID IN :value")
@NamedQuery(name="Rev8DTOKurs.kursartallg", query="SELECT e FROM Rev8DTOKurs e WHERE e.KursartAllg = :value")
@NamedQuery(name="Rev8DTOKurs.kursartallg.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.KursartAllg IN :value")
@NamedQuery(name="Rev8DTOKurs.wochenstd", query="SELECT e FROM Rev8DTOKurs e WHERE e.WochenStd = :value")
@NamedQuery(name="Rev8DTOKurs.wochenstd.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.WochenStd IN :value")
@NamedQuery(name="Rev8DTOKurs.lehrer_id", query="SELECT e FROM Rev8DTOKurs e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="Rev8DTOKurs.lehrer_id.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="Rev8DTOKurs.sortierung", query="SELECT e FROM Rev8DTOKurs e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOKurs.sortierung.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOKurs.sichtbar", query="SELECT e FROM Rev8DTOKurs e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev8DTOKurs.sichtbar.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev8DTOKurs.schienen", query="SELECT e FROM Rev8DTOKurs e WHERE e.Schienen = :value")
@NamedQuery(name="Rev8DTOKurs.schienen.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Schienen IN :value")
@NamedQuery(name="Rev8DTOKurs.fortschreibungsart", query="SELECT e FROM Rev8DTOKurs e WHERE e.Fortschreibungsart = :value")
@NamedQuery(name="Rev8DTOKurs.fortschreibungsart.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Fortschreibungsart IN :value")
@NamedQuery(name="Rev8DTOKurs.wochenstdkl", query="SELECT e FROM Rev8DTOKurs e WHERE e.WochenstdKL = :value")
@NamedQuery(name="Rev8DTOKurs.wochenstdkl.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.WochenstdKL IN :value")
@NamedQuery(name="Rev8DTOKurs.schulnr", query="SELECT e FROM Rev8DTOKurs e WHERE e.SchulNr = :value")
@NamedQuery(name="Rev8DTOKurs.schulnr.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.SchulNr IN :value")
@NamedQuery(name="Rev8DTOKurs.epochu", query="SELECT e FROM Rev8DTOKurs e WHERE e.EpochU = :value")
@NamedQuery(name="Rev8DTOKurs.epochu.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.EpochU IN :value")
@NamedQuery(name="Rev8DTOKurs.zeugnisbez", query="SELECT e FROM Rev8DTOKurs e WHERE e.ZeugnisBez = :value")
@NamedQuery(name="Rev8DTOKurs.zeugnisbez.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.ZeugnisBez IN :value")
@NamedQuery(name="Rev8DTOKurs.jahrgaenge", query="SELECT e FROM Rev8DTOKurs e WHERE e.Jahrgaenge = :value")
@NamedQuery(name="Rev8DTOKurs.jahrgaenge.multiple", query="SELECT e FROM Rev8DTOKurs e WHERE e.Jahrgaenge IN :value")
@NamedQuery(name="Rev8DTOKurs.primaryKeyQuery", query="SELECT e FROM Rev8DTOKurs e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKurs.all.migration", query="SELECT e FROM Rev8DTOKurs e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schuljahresabschnitts_ID","KurzBez","Jahrgang_ID","ASDJahrgang","Fach_ID","KursartAllg","WochenStd","Lehrer_ID","Sortierung","Sichtbar","Schienen","Fortschreibungsart","WochenstdKL","SchulNr","EpochU","ZeugnisBez","Jahrgaenge"})
public class Rev8DTOKurs {

	/** ID des Kurses */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Kursbezeichnung des Kurses */
	@Column(name = "KurzBez")
	@JsonProperty
	public String KurzBez;

	/** Jahrgangs_ID des Kurses */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** ASD-Kürzel des Jahrgangs des Kurses */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** Fach_ID des Kurses */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Allgemeine Kursart des Kurses */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Wochenstunden des Kurses */
	@Column(name = "WochenStd")
	@JsonProperty
	public Integer WochenStd;

	/** Lehrer-ID des unterrichtenden Lehrers des Kurses */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Sortierung des Kurses */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit des Kurses */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Auflistung der Schienen in denen der Kurs ist */
	@Column(name = "Schienen")
	@JsonProperty
	public String Schienen;

	/** Fortschreibungsart des Kurses für die Hochschreibung in den nächsten Abschnitt */
	@Column(name = "Fortschreibungsart")
	@JsonProperty
	@Convert(converter=KursFortschreibungsartConverter.class)
	@JsonSerialize(using=KursFortschreibungsartConverterSerializer.class)
	@JsonDeserialize(using=KursFortschreibungsartConverterDeserializer.class)
	public KursFortschreibungsart Fortschreibungsart;

	/** Wochenstunden des Kurslehrers */
	@Column(name = "WochenstdKL")
	@JsonProperty
	public Double WochenstdKL;

	/** Schulnummer des Kurses bei externen Kursen nötig */
	@Column(name = "SchulNr")
	@JsonProperty
	public Integer SchulNr;

	/** Gibt an ob ein Kurs Epochal unterrichtet wird */
	@Column(name = "EpochU")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean EpochU;

	/** Zeugnisbezeichnung des Kurses */
	@Column(name = "ZeugnisBez")
	@JsonProperty
	public String ZeugnisBez;

	/** Auflistung der Jahrgänge wenn Kurs übergreifen */
	@Column(name = "Jahrgaenge")
	@JsonProperty
	public String Jahrgaenge;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKurs ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKurs() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKurs ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param KurzBez   der Wert für das Attribut KurzBez
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public Rev8DTOKurs(final Long ID, final Long Schuljahresabschnitts_ID, final String KurzBez, final Long Fach_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schuljahresabschnitts_ID == null) { 
			throw new NullPointerException("Schuljahresabschnitts_ID must not be null");
		}
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (KurzBez == null) { 
			throw new NullPointerException("KurzBez must not be null");
		}
		this.KurzBez = KurzBez;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKurs other = (Rev8DTOKurs) obj;
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
		return "Rev8DTOKurs(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", KurzBez=" + this.KurzBez + ", Jahrgang_ID=" + this.Jahrgang_ID + ", ASDJahrgang=" + this.ASDJahrgang + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", WochenStd=" + this.WochenStd + ", Lehrer_ID=" + this.Lehrer_ID + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Schienen=" + this.Schienen + ", Fortschreibungsart=" + this.Fortschreibungsart + ", WochenstdKL=" + this.WochenstdKL + ", SchulNr=" + this.SchulNr + ", EpochU=" + this.EpochU + ", ZeugnisBez=" + this.ZeugnisBez + ", Jahrgaenge=" + this.Jahrgaenge + ")";
	}

}