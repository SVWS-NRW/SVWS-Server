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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerMehrleistung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerMehrleistung")
@NamedQuery(name="DevDTOLehrerMehrleistung.all", query="SELECT e FROM DevDTOLehrerMehrleistung e")
@NamedQuery(name="DevDTOLehrerMehrleistung.id", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.ID = :value")
@NamedQuery(name="DevDTOLehrerMehrleistung.id.multiple", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOLehrerMehrleistung.abschnitt_id", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name="DevDTOLehrerMehrleistung.abschnitt_id.multiple", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name="DevDTOLehrerMehrleistung.mehrleistungsgrundkrz", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.MehrleistungsgrundKrz = :value")
@NamedQuery(name="DevDTOLehrerMehrleistung.mehrleistungsgrundkrz.multiple", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.MehrleistungsgrundKrz IN :value")
@NamedQuery(name="DevDTOLehrerMehrleistung.mehrleistungstd", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.MehrleistungStd = :value")
@NamedQuery(name="DevDTOLehrerMehrleistung.mehrleistungstd.multiple", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.MehrleistungStd IN :value")
@NamedQuery(name="DevDTOLehrerMehrleistung.primaryKeyQuery", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOLehrerMehrleistung.all.migration", query="SELECT e FROM DevDTOLehrerMehrleistung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abschnitt_ID","MehrleistungsgrundKrz","MehrleistungStd"})
public class DevDTOLehrerMehrleistung {

	/** ID für den Eintrag für die Mehrarbeitsstunden eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Mehrarbeitsstunden Kürzel */
	@Column(name = "MehrleistungsgrundKrz")
	@JsonProperty
	public String MehrleistungsgrundKrz;

	/** Anzahl Mehrarbeitsstunden */
	@Column(name = "MehrleistungStd")
	@JsonProperty
	public Double MehrleistungStd;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOLehrerMehrleistung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOLehrerMehrleistung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOLehrerMehrleistung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param MehrleistungsgrundKrz   der Wert für das Attribut MehrleistungsgrundKrz
	 */
	public DevDTOLehrerMehrleistung(final Long ID, final Long Abschnitt_ID, final String MehrleistungsgrundKrz) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abschnitt_ID == null) { 
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (MehrleistungsgrundKrz == null) { 
			throw new NullPointerException("MehrleistungsgrundKrz must not be null");
		}
		this.MehrleistungsgrundKrz = MehrleistungsgrundKrz;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOLehrerMehrleistung other = (DevDTOLehrerMehrleistung) obj;
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
		return "DevDTOLehrerMehrleistung(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", MehrleistungsgrundKrz=" + this.MehrleistungsgrundKrz + ", MehrleistungStd=" + this.MehrleistungStd + ")";
	}

}