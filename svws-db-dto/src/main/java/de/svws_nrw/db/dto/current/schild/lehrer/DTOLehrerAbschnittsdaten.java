package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

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
@NamedQuery(name = "DTOLehrerAbschnittsdaten.all", query = "SELECT e FROM DTOLehrerAbschnittsdaten e")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.id", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.ID = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.id.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.lehrer_id", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.lehrer_id.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.schuljahresabschnitts_id", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.schuljahresabschnitts_id.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.rechtsverhaeltnis", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Rechtsverhaeltnis = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.rechtsverhaeltnis.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Rechtsverhaeltnis IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.beschaeftigungsart", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Beschaeftigungsart = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.beschaeftigungsart.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Beschaeftigungsart IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.einsatzstatus", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Einsatzstatus = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.einsatzstatus.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Einsatzstatus IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.stammschulnr", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.StammschulNr = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.stammschulnr.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.StammschulNr IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.pflichtstdsoll", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.PflichtstdSoll = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.pflichtstdsoll.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.PflichtstdSoll IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.unterrichtsstd", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.UnterrichtsStd = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.unterrichtsstd.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.UnterrichtsStd IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.mehrleistungstd", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.MehrleistungStd = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.mehrleistungstd.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.MehrleistungStd IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.entlastungstd", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.EntlastungStd = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.entlastungstd.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.EntlastungStd IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.anrechnungstd", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.AnrechnungStd = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.anrechnungstd.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.AnrechnungStd IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.reststd", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.RestStd = :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.reststd.multiple", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.RestStd IN :value")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.primaryKeyQuery", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOLehrerAbschnittsdaten.all.migration", query = "SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Lehrer_ID", "Schuljahresabschnitts_ID", "Rechtsverhaeltnis", "Beschaeftigungsart", "Einsatzstatus", "StammschulNr", "PflichtstdSoll", "UnterrichtsStd", "MehrleistungStd", "EntlastungStd", "AnrechnungStd", "RestStd"})
public final class DTOLehrerAbschnittsdaten {

	/** ID des Eintrags für die LehrerAbschnittsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** LehrerID für die LehrerAbschnittsdaten */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public long Schuljahresabschnitts_ID;

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

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerAbschnittsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerAbschnittsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerAbschnittsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 */
	public DTOLehrerAbschnittsdaten(final long ID, final long Lehrer_ID, final long Schuljahresabschnitts_ID) {
		this.ID = ID;
		this.Lehrer_ID = Lehrer_ID;
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerAbschnittsdaten other = (DTOLehrerAbschnittsdaten) obj;
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
		return "DTOLehrerAbschnittsdaten(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Rechtsverhaeltnis=" + this.Rechtsverhaeltnis + ", Beschaeftigungsart=" + this.Beschaeftigungsart + ", Einsatzstatus=" + this.Einsatzstatus + ", StammschulNr=" + this.StammschulNr + ", PflichtstdSoll=" + this.PflichtstdSoll + ", UnterrichtsStd=" + this.UnterrichtsStd + ", MehrleistungStd=" + this.MehrleistungStd + ", EntlastungStd=" + this.EntlastungStd + ", AnrechnungStd=" + this.AnrechnungStd + ", RestStd=" + this.RestStd + ")";
	}

}
