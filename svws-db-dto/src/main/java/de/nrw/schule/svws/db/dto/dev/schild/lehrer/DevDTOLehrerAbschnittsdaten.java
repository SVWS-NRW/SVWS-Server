package de.nrw.schule.svws.db.dto.dev.schild.lehrer;

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
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.all", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.id", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.ID = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.id.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.lehrer_id", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.lehrer_id.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.schuljahresabschnitts_id", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.schuljahresabschnitts_id.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.rechtsverhaeltnis", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Rechtsverhaeltnis = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.rechtsverhaeltnis.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Rechtsverhaeltnis IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.beschaeftigungsart", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Beschaeftigungsart = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.beschaeftigungsart.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Beschaeftigungsart IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.einsatzstatus", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Einsatzstatus = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.einsatzstatus.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.Einsatzstatus IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.pflichtstdsoll", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.PflichtstdSoll = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.pflichtstdsoll.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.PflichtstdSoll IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.unterrichtsstd", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.UnterrichtsStd = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.unterrichtsstd.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.UnterrichtsStd IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.mehrleistungstd", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.MehrleistungStd = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.mehrleistungstd.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.MehrleistungStd IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.entlastungstd", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.EntlastungStd = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.entlastungstd.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.EntlastungStd IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.anrechnungstd", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.AnrechnungStd = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.anrechnungstd.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.AnrechnungStd IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.reststd", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.RestStd = :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.reststd.multiple", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.RestStd IN :value")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.primaryKeyQuery", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOLehrerAbschnittsdaten.all.migration", query="SELECT e FROM DevDTOLehrerAbschnittsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Lehrer_ID","Schuljahresabschnitts_ID","Rechtsverhaeltnis","Beschaeftigungsart","Einsatzstatus","PflichtstdSoll","UnterrichtsStd","MehrleistungStd","EntlastungStd","AnrechnungStd","RestStd"})
public class DevDTOLehrerAbschnittsdaten {

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

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOLehrerAbschnittsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOLehrerAbschnittsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOLehrerAbschnittsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 */
	public DevDTOLehrerAbschnittsdaten(final Long ID, final Long Lehrer_ID, final Long Schuljahresabschnitts_ID) {
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
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOLehrerAbschnittsdaten other = (DevDTOLehrerAbschnittsdaten) obj;
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
		return "DevDTOLehrerAbschnittsdaten(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Rechtsverhaeltnis=" + this.Rechtsverhaeltnis + ", Beschaeftigungsart=" + this.Beschaeftigungsart + ", Einsatzstatus=" + this.Einsatzstatus + ", PflichtstdSoll=" + this.PflichtstdSoll + ", UnterrichtsStd=" + this.UnterrichtsStd + ", MehrleistungStd=" + this.MehrleistungStd + ", EntlastungStd=" + this.EntlastungStd + ", AnrechnungStd=" + this.AnrechnungStd + ", RestStd=" + this.RestStd + ")";
	}

}