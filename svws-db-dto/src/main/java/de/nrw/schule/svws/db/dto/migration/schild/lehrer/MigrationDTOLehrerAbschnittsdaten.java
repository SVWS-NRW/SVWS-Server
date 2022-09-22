package de.nrw.schule.svws.db.dto.migration.schild.lehrer;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.all", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.id", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.id.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.lehrer_id", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.lehrer_id.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.schuljahresabschnitts_id", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.schuljahresabschnitts_id.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.rechtsverhaeltnis", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Rechtsverhaeltnis = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.rechtsverhaeltnis.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Rechtsverhaeltnis IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.beschaeftigungsart", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Beschaeftigungsart = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.beschaeftigungsart.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Beschaeftigungsart IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.einsatzstatus", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Einsatzstatus = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.einsatzstatus.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Einsatzstatus IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.pflichtstdsoll", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.PflichtstdSoll = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.pflichtstdsoll.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.PflichtstdSoll IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.unterrichtsstd", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.UnterrichtsStd = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.unterrichtsstd.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.UnterrichtsStd IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.mehrleistungstd", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.MehrleistungStd = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.mehrleistungstd.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.MehrleistungStd IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.entlastungstd", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.EntlastungStd = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.entlastungstd.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.EntlastungStd IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.anrechnungstd", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.AnrechnungStd = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.anrechnungstd.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.AnrechnungStd IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.reststd", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.RestStd = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.reststd.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.RestStd IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.schulnreigner", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.schulnreigner.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.jahr", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Jahr = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.jahr.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Jahr IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.abschnitt", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Abschnitt = :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.abschnitt.multiple", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.Abschnitt IN :value")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.primaryKeyQuery", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOLehrerAbschnittsdaten.all.migration", query="SELECT e FROM MigrationDTOLehrerAbschnittsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Lehrer_ID","Schuljahresabschnitts_ID","Rechtsverhaeltnis","Beschaeftigungsart","Einsatzstatus","PflichtstdSoll","UnterrichtsStd","MehrleistungStd","EntlastungStd","AnrechnungStd","RestStd","SchulnrEigner","Jahr","Abschnitt"})
public class MigrationDTOLehrerAbschnittsdaten {

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
	public boolean equals(Object obj) {
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
		return "MigrationDTOLehrerAbschnittsdaten(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Rechtsverhaeltnis=" + this.Rechtsverhaeltnis + ", Beschaeftigungsart=" + this.Beschaeftigungsart + ", Einsatzstatus=" + this.Einsatzstatus + ", PflichtstdSoll=" + this.PflichtstdSoll + ", UnterrichtsStd=" + this.UnterrichtsStd + ", MehrleistungStd=" + this.MehrleistungStd + ", EntlastungStd=" + this.EntlastungStd + ", AnrechnungStd=" + this.AnrechnungStd + ", RestStd=" + this.RestStd + ", SchulnrEigner=" + this.SchulnrEigner + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ")";
	}

}