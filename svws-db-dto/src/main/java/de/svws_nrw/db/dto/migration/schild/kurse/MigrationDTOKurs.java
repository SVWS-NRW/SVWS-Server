package de.svws_nrw.db.dto.migration.schild.kurse;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Kurse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Kurse")
@JsonPropertyOrder({"ID", "Schuljahresabschnitts_ID", "KurzBez", "Jahrgang_ID", "ASDJahrgang", "Fach_ID", "KursartAllg", "WochenStd", "Lehrer_ID", "LehrerKrz", "Sortierung", "Sichtbar", "Schienen", "Fortschreibungsart", "WochenstdKL", "SchulNr", "EpochU", "SchulnrEigner", "ZeugnisBez", "Jahrgaenge", "Jahr", "Abschnitt"})
public final class MigrationDTOKurs {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOKurs e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOKurs e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOKurs e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOKurs e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KurzBez */
	public static final String QUERY_BY_KURZBEZ = "SELECT e FROM MigrationDTOKurs e WHERE e.KurzBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KurzBez */
	public static final String QUERY_LIST_BY_KURZBEZ = "SELECT e FROM MigrationDTOKurs e WHERE e.KurzBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgang */
	public static final String QUERY_BY_ASDJAHRGANG = "SELECT e FROM MigrationDTOKurs e WHERE e.ASDJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgang */
	public static final String QUERY_LIST_BY_ASDJAHRGANG = "SELECT e FROM MigrationDTOKurs e WHERE e.ASDJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KursartAllg */
	public static final String QUERY_BY_KURSARTALLG = "SELECT e FROM MigrationDTOKurs e WHERE e.KursartAllg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KursartAllg */
	public static final String QUERY_LIST_BY_KURSARTALLG = "SELECT e FROM MigrationDTOKurs e WHERE e.KursartAllg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenStd */
	public static final String QUERY_BY_WOCHENSTD = "SELECT e FROM MigrationDTOKurs e WHERE e.WochenStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenStd */
	public static final String QUERY_LIST_BY_WOCHENSTD = "SELECT e FROM MigrationDTOKurs e WHERE e.WochenStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOKurs e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrerKrz */
	public static final String QUERY_BY_LEHRERKRZ = "SELECT e FROM MigrationDTOKurs e WHERE e.LehrerKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrerKrz */
	public static final String QUERY_LIST_BY_LEHRERKRZ = "SELECT e FROM MigrationDTOKurs e WHERE e.LehrerKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOKurs e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOKurs e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM MigrationDTOKurs e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM MigrationDTOKurs e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schienen */
	public static final String QUERY_BY_SCHIENEN = "SELECT e FROM MigrationDTOKurs e WHERE e.Schienen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schienen */
	public static final String QUERY_LIST_BY_SCHIENEN = "SELECT e FROM MigrationDTOKurs e WHERE e.Schienen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fortschreibungsart */
	public static final String QUERY_BY_FORTSCHREIBUNGSART = "SELECT e FROM MigrationDTOKurs e WHERE e.Fortschreibungsart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fortschreibungsart */
	public static final String QUERY_LIST_BY_FORTSCHREIBUNGSART = "SELECT e FROM MigrationDTOKurs e WHERE e.Fortschreibungsart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenstdKL */
	public static final String QUERY_BY_WOCHENSTDKL = "SELECT e FROM MigrationDTOKurs e WHERE e.WochenstdKL = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenstdKL */
	public static final String QUERY_LIST_BY_WOCHENSTDKL = "SELECT e FROM MigrationDTOKurs e WHERE e.WochenstdKL IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulNr */
	public static final String QUERY_BY_SCHULNR = "SELECT e FROM MigrationDTOKurs e WHERE e.SchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulNr */
	public static final String QUERY_LIST_BY_SCHULNR = "SELECT e FROM MigrationDTOKurs e WHERE e.SchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EpochU */
	public static final String QUERY_BY_EPOCHU = "SELECT e FROM MigrationDTOKurs e WHERE e.EpochU = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EpochU */
	public static final String QUERY_LIST_BY_EPOCHU = "SELECT e FROM MigrationDTOKurs e WHERE e.EpochU IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOKurs e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOKurs e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeugnisBez */
	public static final String QUERY_BY_ZEUGNISBEZ = "SELECT e FROM MigrationDTOKurs e WHERE e.ZeugnisBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeugnisBez */
	public static final String QUERY_LIST_BY_ZEUGNISBEZ = "SELECT e FROM MigrationDTOKurs e WHERE e.ZeugnisBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgaenge */
	public static final String QUERY_BY_JAHRGAENGE = "SELECT e FROM MigrationDTOKurs e WHERE e.Jahrgaenge = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgaenge */
	public static final String QUERY_LIST_BY_JAHRGAENGE = "SELECT e FROM MigrationDTOKurs e WHERE e.Jahrgaenge IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM MigrationDTOKurs e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM MigrationDTOKurs e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOKurs e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOKurs e WHERE e.Abschnitt IN ?1";

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
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	public boolean equals(final Object obj) {
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
