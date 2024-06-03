package de.svws_nrw.db.dto.current.schild.kurse;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.KursFortschreibungsartConverter;

import de.svws_nrw.core.types.KursFortschreibungsart;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.KursFortschreibungsartConverterSerializer;
import de.svws_nrw.csv.converter.current.KursFortschreibungsartConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Kurse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Kurse")
@JsonPropertyOrder({"ID", "Schuljahresabschnitts_ID", "KurzBez", "Jahrgang_ID", "ASDJahrgang", "Fach_ID", "KursartAllg", "WochenStd", "Lehrer_ID", "Sortierung", "Sichtbar", "Schienen", "Fortschreibungsart", "WochenstdKL", "SchulNr", "EpochU", "ZeugnisBez", "Jahrgaenge"})
public final class DTOKurs {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKurs e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKurs e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOKurs e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKurs e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOKurs e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOKurs e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOKurs e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOKurs e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KurzBez */
	public static final String QUERY_BY_KURZBEZ = "SELECT e FROM DTOKurs e WHERE e.KurzBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KurzBez */
	public static final String QUERY_LIST_BY_KURZBEZ = "SELECT e FROM DTOKurs e WHERE e.KurzBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM DTOKurs e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM DTOKurs e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgang */
	public static final String QUERY_BY_ASDJAHRGANG = "SELECT e FROM DTOKurs e WHERE e.ASDJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgang */
	public static final String QUERY_LIST_BY_ASDJAHRGANG = "SELECT e FROM DTOKurs e WHERE e.ASDJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOKurs e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOKurs e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KursartAllg */
	public static final String QUERY_BY_KURSARTALLG = "SELECT e FROM DTOKurs e WHERE e.KursartAllg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KursartAllg */
	public static final String QUERY_LIST_BY_KURSARTALLG = "SELECT e FROM DTOKurs e WHERE e.KursartAllg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenStd */
	public static final String QUERY_BY_WOCHENSTD = "SELECT e FROM DTOKurs e WHERE e.WochenStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenStd */
	public static final String QUERY_LIST_BY_WOCHENSTD = "SELECT e FROM DTOKurs e WHERE e.WochenStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOKurs e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOKurs e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOKurs e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOKurs e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOKurs e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOKurs e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schienen */
	public static final String QUERY_BY_SCHIENEN = "SELECT e FROM DTOKurs e WHERE e.Schienen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schienen */
	public static final String QUERY_LIST_BY_SCHIENEN = "SELECT e FROM DTOKurs e WHERE e.Schienen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fortschreibungsart */
	public static final String QUERY_BY_FORTSCHREIBUNGSART = "SELECT e FROM DTOKurs e WHERE e.Fortschreibungsart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fortschreibungsart */
	public static final String QUERY_LIST_BY_FORTSCHREIBUNGSART = "SELECT e FROM DTOKurs e WHERE e.Fortschreibungsart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenstdKL */
	public static final String QUERY_BY_WOCHENSTDKL = "SELECT e FROM DTOKurs e WHERE e.WochenstdKL = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenstdKL */
	public static final String QUERY_LIST_BY_WOCHENSTDKL = "SELECT e FROM DTOKurs e WHERE e.WochenstdKL IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulNr */
	public static final String QUERY_BY_SCHULNR = "SELECT e FROM DTOKurs e WHERE e.SchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulNr */
	public static final String QUERY_LIST_BY_SCHULNR = "SELECT e FROM DTOKurs e WHERE e.SchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EpochU */
	public static final String QUERY_BY_EPOCHU = "SELECT e FROM DTOKurs e WHERE e.EpochU = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EpochU */
	public static final String QUERY_LIST_BY_EPOCHU = "SELECT e FROM DTOKurs e WHERE e.EpochU IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeugnisBez */
	public static final String QUERY_BY_ZEUGNISBEZ = "SELECT e FROM DTOKurs e WHERE e.ZeugnisBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeugnisBez */
	public static final String QUERY_LIST_BY_ZEUGNISBEZ = "SELECT e FROM DTOKurs e WHERE e.ZeugnisBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgaenge */
	public static final String QUERY_BY_JAHRGAENGE = "SELECT e FROM DTOKurs e WHERE e.Jahrgaenge = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgaenge */
	public static final String QUERY_LIST_BY_JAHRGAENGE = "SELECT e FROM DTOKurs e WHERE e.Jahrgaenge IN ?1";

	/** ID des Kurses */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public long Schuljahresabschnitts_ID;

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
	public long Fach_ID;

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
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Auflistung der Schienen in denen der Kurs ist */
	@Column(name = "Schienen")
	@JsonProperty
	public String Schienen;

	/** Fortschreibungsart des Kurses für die Hochschreibung in den nächsten Abschnitt */
	@Column(name = "Fortschreibungsart")
	@JsonProperty
	@Convert(converter = KursFortschreibungsartConverter.class)
	@JsonSerialize(using = KursFortschreibungsartConverterSerializer.class)
	@JsonDeserialize(using = KursFortschreibungsartConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse DTOKurs ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKurs() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKurs ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param KurzBez   der Wert für das Attribut KurzBez
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOKurs(final long ID, final long Schuljahresabschnitts_ID, final String KurzBez, final long Fach_ID) {
		this.ID = ID;
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (KurzBez == null) {
			throw new NullPointerException("KurzBez must not be null");
		}
		this.KurzBez = KurzBez;
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKurs other = (DTOKurs) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOKurs(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", KurzBez=" + this.KurzBez + ", Jahrgang_ID=" + this.Jahrgang_ID + ", ASDJahrgang=" + this.ASDJahrgang + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", WochenStd=" + this.WochenStd + ", Lehrer_ID=" + this.Lehrer_ID + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Schienen=" + this.Schienen + ", Fortschreibungsart=" + this.Fortschreibungsart + ", WochenstdKL=" + this.WochenstdKL + ", SchulNr=" + this.SchulNr + ", EpochU=" + this.EpochU + ", ZeugnisBez=" + this.ZeugnisBez + ", Jahrgaenge=" + this.Jahrgaenge + ")";
	}

}
