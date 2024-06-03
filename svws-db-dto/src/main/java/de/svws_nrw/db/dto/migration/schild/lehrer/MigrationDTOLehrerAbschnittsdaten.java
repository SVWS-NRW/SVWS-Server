package de.svws_nrw.db.dto.migration.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerAbschnittsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerAbschnittsdaten")
@JsonPropertyOrder({"ID", "Lehrer_ID", "Schuljahresabschnitts_ID", "Rechtsverhaeltnis", "Beschaeftigungsart", "Einsatzstatus", "StammschulNr", "PflichtstdSoll", "UnterrichtsStd", "MehrleistungStd", "EntlastungStd", "AnrechnungStd", "RestStd", "SchulnrEigner", "Jahr", "Abschnitt"})
public final class MigrationDTOLehrerAbschnittsdaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Rechtsverhaeltnis */
	public static final String QUERY_BY_RECHTSVERHAELTNIS = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Rechtsverhaeltnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Rechtsverhaeltnis */
	public static final String QUERY_LIST_BY_RECHTSVERHAELTNIS = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Rechtsverhaeltnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschaeftigungsart */
	public static final String QUERY_BY_BESCHAEFTIGUNGSART = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Beschaeftigungsart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschaeftigungsart */
	public static final String QUERY_LIST_BY_BESCHAEFTIGUNGSART = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Beschaeftigungsart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Einsatzstatus */
	public static final String QUERY_BY_EINSATZSTATUS = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Einsatzstatus = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Einsatzstatus */
	public static final String QUERY_LIST_BY_EINSATZSTATUS = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Einsatzstatus IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StammschulNr */
	public static final String QUERY_BY_STAMMSCHULNR = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.StammschulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StammschulNr */
	public static final String QUERY_LIST_BY_STAMMSCHULNR = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.StammschulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PflichtstdSoll */
	public static final String QUERY_BY_PFLICHTSTDSOLL = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.PflichtstdSoll = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PflichtstdSoll */
	public static final String QUERY_LIST_BY_PFLICHTSTDSOLL = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.PflichtstdSoll IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes UnterrichtsStd */
	public static final String QUERY_BY_UNTERRICHTSSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.UnterrichtsStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes UnterrichtsStd */
	public static final String QUERY_LIST_BY_UNTERRICHTSSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.UnterrichtsStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MehrleistungStd */
	public static final String QUERY_BY_MEHRLEISTUNGSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.MehrleistungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MehrleistungStd */
	public static final String QUERY_LIST_BY_MEHRLEISTUNGSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.MehrleistungStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EntlastungStd */
	public static final String QUERY_BY_ENTLASTUNGSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.EntlastungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EntlastungStd */
	public static final String QUERY_LIST_BY_ENTLASTUNGSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.EntlastungStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnrechnungStd */
	public static final String QUERY_BY_ANRECHNUNGSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.AnrechnungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnrechnungStd */
	public static final String QUERY_LIST_BY_ANRECHNUNGSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.AnrechnungStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RestStd */
	public static final String QUERY_BY_RESTSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.RestStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RestStd */
	public static final String QUERY_LIST_BY_RESTSTD = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.RestStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Abschnitt IN ?1";

	/** ID des Eintrags für die LehrerAbschnittsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** LehrerID für die LehrerAbschnittsdaten */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Rechtsverhältnis für die LehrerAbschnittsdaten */
	@Column(name = "Rechtsverhaeltnis")
	@JsonProperty
	public String Rechtsverhaeltnis;

	/** Beschäftigungsart für die LehrerAbschnittsdaten */
	@Column(name = "Beschaeftigungsart")
	@JsonProperty
	public String Beschaeftigungsart;

	/** Einsatzstatus für die LehrerAbschnittsdaten */
	@Column(name = "Einsatzstatus")
	@JsonProperty
	public String Einsatzstatus;

	/** Die Schulnummer der Stammschule, sofern diese abweicht */
	@Column(name = "StammschulNr")
	@JsonProperty
	public String StammschulNr;

	/** Pflichtstundensoll für die LehrerAbschnittsdaten */
	@Column(name = "PflichtstdSoll")
	@JsonProperty
	public Double PflichtstdSoll;

	/** Unterichsstunden für die LehrerAbschnittsdaten */
	@Column(name = "UnterrichtsStd")
	@JsonProperty
	public Double UnterrichtsStd;

	/** Mehrleistungsstunden für die LehrerAbschnittsdaten */
	@Column(name = "MehrleistungStd")
	@JsonProperty
	public Double MehrleistungStd;

	/** Entlastungsstunden für die LehrerAbschnittsdaten */
	@Column(name = "EntlastungStd")
	@JsonProperty
	public Double EntlastungStd;

	/** Anrechnungstunden für die LehrerAbschnittsdaten */
	@Column(name = "AnrechnungStd")
	@JsonProperty
	public Double AnrechnungStd;

	/** Reststunden die nicht vergeben wurden  für die LehrerAbschnittsdaten */
	@Column(name = "RestStd")
	@JsonProperty
	public Double RestStd;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Schuljahr für die LehrerAbschnittsdaten */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnit für die LehrerAbschnittsdaten */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerAbschnittsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerAbschnittsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerAbschnittsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOLehrerAbschnittsdaten(final Long ID, final Long Lehrer_ID, final Long Schuljahresabschnitts_ID, final Integer Jahr, final Integer Abschnitt) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Lehrer_ID == null) {
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
		if (Schuljahresabschnitts_ID == null) {
			throw new NullPointerException("Schuljahresabschnitts_ID must not be null");
		}
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
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
		MigrationDTOLehrerAbschnittsdaten other = (MigrationDTOLehrerAbschnittsdaten) obj;
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
		return "MigrationDTOLehrerAbschnittsdaten(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Rechtsverhaeltnis=" + this.Rechtsverhaeltnis + ", Beschaeftigungsart=" + this.Beschaeftigungsart + ", Einsatzstatus=" + this.Einsatzstatus + ", StammschulNr=" + this.StammschulNr + ", PflichtstdSoll=" + this.PflichtstdSoll + ", UnterrichtsStd=" + this.UnterrichtsStd + ", MehrleistungStd=" + this.MehrleistungStd + ", EntlastungStd=" + this.EntlastungStd + ", AnrechnungStd=" + this.AnrechnungStd + ", RestStd=" + this.RestStd + ", SchulnrEigner=" + this.SchulnrEigner + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ")";
	}

}
