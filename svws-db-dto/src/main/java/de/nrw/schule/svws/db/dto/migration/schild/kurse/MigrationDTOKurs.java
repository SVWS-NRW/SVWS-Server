package de.nrw.schule.svws.db.dto.migration.schild.kurse;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Kurse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Kurse")
@NamedQuery(name="MigrationDTOKurs.all", query="SELECT e FROM MigrationDTOKurs e")
@NamedQuery(name="MigrationDTOKurs.id", query="SELECT e FROM MigrationDTOKurs e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOKurs.id.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOKurs.schuljahresabschnitts_id", query="SELECT e FROM MigrationDTOKurs e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="MigrationDTOKurs.schuljahresabschnitts_id.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="MigrationDTOKurs.kurzbez", query="SELECT e FROM MigrationDTOKurs e WHERE e.KurzBez = :value")
@NamedQuery(name="MigrationDTOKurs.kurzbez.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.KurzBez IN :value")
@NamedQuery(name="MigrationDTOKurs.jahrgang_id", query="SELECT e FROM MigrationDTOKurs e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name="MigrationDTOKurs.jahrgang_id.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name="MigrationDTOKurs.asdjahrgang", query="SELECT e FROM MigrationDTOKurs e WHERE e.ASDJahrgang = :value")
@NamedQuery(name="MigrationDTOKurs.asdjahrgang.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name="MigrationDTOKurs.fach_id", query="SELECT e FROM MigrationDTOKurs e WHERE e.Fach_ID = :value")
@NamedQuery(name="MigrationDTOKurs.fach_id.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Fach_ID IN :value")
@NamedQuery(name="MigrationDTOKurs.kursartallg", query="SELECT e FROM MigrationDTOKurs e WHERE e.KursartAllg = :value")
@NamedQuery(name="MigrationDTOKurs.kursartallg.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.KursartAllg IN :value")
@NamedQuery(name="MigrationDTOKurs.wochenstd", query="SELECT e FROM MigrationDTOKurs e WHERE e.WochenStd = :value")
@NamedQuery(name="MigrationDTOKurs.wochenstd.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.WochenStd IN :value")
@NamedQuery(name="MigrationDTOKurs.lehrer_id", query="SELECT e FROM MigrationDTOKurs e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="MigrationDTOKurs.lehrer_id.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="MigrationDTOKurs.lehrerkrz", query="SELECT e FROM MigrationDTOKurs e WHERE e.LehrerKrz = :value")
@NamedQuery(name="MigrationDTOKurs.lehrerkrz.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.LehrerKrz IN :value")
@NamedQuery(name="MigrationDTOKurs.sortierung", query="SELECT e FROM MigrationDTOKurs e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOKurs.sortierung.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOKurs.sichtbar", query="SELECT e FROM MigrationDTOKurs e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOKurs.sichtbar.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOKurs.schienen", query="SELECT e FROM MigrationDTOKurs e WHERE e.Schienen = :value")
@NamedQuery(name="MigrationDTOKurs.schienen.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Schienen IN :value")
@NamedQuery(name="MigrationDTOKurs.fortschreibungsart", query="SELECT e FROM MigrationDTOKurs e WHERE e.Fortschreibungsart = :value")
@NamedQuery(name="MigrationDTOKurs.fortschreibungsart.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Fortschreibungsart IN :value")
@NamedQuery(name="MigrationDTOKurs.wochenstdkl", query="SELECT e FROM MigrationDTOKurs e WHERE e.WochenstdKL = :value")
@NamedQuery(name="MigrationDTOKurs.wochenstdkl.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.WochenstdKL IN :value")
@NamedQuery(name="MigrationDTOKurs.schulnr", query="SELECT e FROM MigrationDTOKurs e WHERE e.SchulNr = :value")
@NamedQuery(name="MigrationDTOKurs.schulnr.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.SchulNr IN :value")
@NamedQuery(name="MigrationDTOKurs.epochu", query="SELECT e FROM MigrationDTOKurs e WHERE e.EpochU = :value")
@NamedQuery(name="MigrationDTOKurs.epochu.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.EpochU IN :value")
@NamedQuery(name="MigrationDTOKurs.schulnreigner", query="SELECT e FROM MigrationDTOKurs e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOKurs.schulnreigner.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOKurs.zeugnisbez", query="SELECT e FROM MigrationDTOKurs e WHERE e.ZeugnisBez = :value")
@NamedQuery(name="MigrationDTOKurs.zeugnisbez.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.ZeugnisBez IN :value")
@NamedQuery(name="MigrationDTOKurs.jahrgaenge", query="SELECT e FROM MigrationDTOKurs e WHERE e.Jahrgaenge = :value")
@NamedQuery(name="MigrationDTOKurs.jahrgaenge.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Jahrgaenge IN :value")
@NamedQuery(name="MigrationDTOKurs.jahr", query="SELECT e FROM MigrationDTOKurs e WHERE e.Jahr = :value")
@NamedQuery(name="MigrationDTOKurs.jahr.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Jahr IN :value")
@NamedQuery(name="MigrationDTOKurs.abschnitt", query="SELECT e FROM MigrationDTOKurs e WHERE e.Abschnitt = :value")
@NamedQuery(name="MigrationDTOKurs.abschnitt.multiple", query="SELECT e FROM MigrationDTOKurs e WHERE e.Abschnitt IN :value")
@NamedQuery(name="MigrationDTOKurs.primaryKeyQuery", query="SELECT e FROM MigrationDTOKurs e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOKurs.all.migration", query="SELECT e FROM MigrationDTOKurs e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schuljahresabschnitts_ID","KurzBez","Jahrgang_ID","ASDJahrgang","Fach_ID","KursartAllg","WochenStd","Lehrer_ID","LehrerKrz","Sortierung","Sichtbar","Schienen","Fortschreibungsart","WochenstdKL","SchulNr","EpochU","SchulnrEigner","ZeugnisBez","Jahrgaenge","Jahr","Abschnitt"})
public class MigrationDTOKurs {

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

	/** DEPRECATED: Lehrerkürzel des unterrichtenden Lehrers des Kurses */
	@Column(name = "LehrerKrz")
	@JsonProperty
	public String LehrerKrz;

	/** Sortierung des Kurses */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit des Kurses */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Auflistung der Schienen in denen der Kurs ist */
	@Column(name = "Schienen")
	@JsonProperty
	public String Schienen;

	/** Fortschreibungsart des Kurses für die Hochschreibung in den nächsten Abschnitt */
	@Column(name = "Fortschreibungsart")
	@JsonProperty
	public String Fortschreibungsart;

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
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean EpochU;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Zeugnisbezeichnung des Kurses */
	@Column(name = "ZeugnisBez")
	@JsonProperty
	public String ZeugnisBez;

	/** Auflistung der Jahrgänge wenn Kurs übergreifen */
	@Column(name = "Jahrgaenge")
	@JsonProperty
	public String Jahrgaenge;

	/** Schuljahr des Kurses */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt des Kurses */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKurs ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKurs() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKurs ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param KurzBez   der Wert für das Attribut KurzBez
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOKurs(final Long ID, final Long Schuljahresabschnitts_ID, final String KurzBez, final Long Fach_ID, final Integer Jahr, final Integer Abschnitt) {
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
		if (Jahr == null) { 
			throw new NullPointerException("Jahr must not be null");
		}
		this.Jahr = Jahr;
		if (Abschnitt == null) { 
			throw new NullPointerException("Abschnitt must not be null");
		}
		this.Abschnitt = Abschnitt;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOKurs other = (MigrationDTOKurs) obj;
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
		return "MigrationDTOKurs(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", KurzBez=" + this.KurzBez + ", Jahrgang_ID=" + this.Jahrgang_ID + ", ASDJahrgang=" + this.ASDJahrgang + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", WochenStd=" + this.WochenStd + ", Lehrer_ID=" + this.Lehrer_ID + ", LehrerKrz=" + this.LehrerKrz + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Schienen=" + this.Schienen + ", Fortschreibungsart=" + this.Fortschreibungsart + ", WochenstdKL=" + this.WochenstdKL + ", SchulNr=" + this.SchulNr + ", EpochU=" + this.EpochU + ", SchulnrEigner=" + this.SchulnrEigner + ", ZeugnisBez=" + this.ZeugnisBez + ", Jahrgaenge=" + this.Jahrgaenge + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ")";
	}

}