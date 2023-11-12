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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerEntlastung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerEntlastung")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.all", query = "SELECT e FROM DTOLehrerEntlastungsstunde e")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.id", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID = :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.id.multiple", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID IN :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.abschnitt_id", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.abschnitt_id.multiple", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.entlastungsgrundkrz", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.EntlastungsgrundKrz = :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.entlastungsgrundkrz.multiple", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.EntlastungsgrundKrz IN :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.entlastungstd", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.EntlastungStd = :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.entlastungstd.multiple", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.EntlastungStd IN :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.primaryKeyQuery", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID = ?1")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.primaryKeyQuery.multiple", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID IN :value")
@NamedQuery(name = "DTOLehrerEntlastungsstunde.all.migration", query = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "EntlastungsgrundKrz", "EntlastungStd"})
public final class DTOLehrerEntlastungsstunde {

	/** ID für den Eintrag für die Entlastungsstunden (Mehr-Minderleistung) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** Kürzel für die Entlastungsstunden (Minderleistung) */
	@Column(name = "EntlastungsgrundKrz")
	@JsonProperty
	public String EntlastungsgrundKrz;

	/** Anzahl für die Entlastungsstunden (Minderleistung) */
	@Column(name = "EntlastungStd")
	@JsonProperty
	public Double EntlastungStd;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerEntlastungsstunde ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerEntlastungsstunde() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerEntlastungsstunde ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public DTOLehrerEntlastungsstunde(final long ID, final long Abschnitt_ID) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerEntlastungsstunde other = (DTOLehrerEntlastungsstunde) obj;
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
		return "DTOLehrerEntlastungsstunde(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", EntlastungsgrundKrz=" + this.EntlastungsgrundKrz + ", EntlastungStd=" + this.EntlastungStd + ")";
	}

}
