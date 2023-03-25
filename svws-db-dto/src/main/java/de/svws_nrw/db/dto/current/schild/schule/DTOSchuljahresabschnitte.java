package de.svws_nrw.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schuljahresabschnitte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schuljahresabschnitte")
@NamedQuery(name="DTOSchuljahresabschnitte.all", query="SELECT e FROM DTOSchuljahresabschnitte e")
@NamedQuery(name="DTOSchuljahresabschnitte.id", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID = :value")
@NamedQuery(name="DTOSchuljahresabschnitte.id.multiple", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchuljahresabschnitte.jahr", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr = :value")
@NamedQuery(name="DTOSchuljahresabschnitte.jahr.multiple", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Jahr IN :value")
@NamedQuery(name="DTOSchuljahresabschnitte.abschnitt", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Abschnitt = :value")
@NamedQuery(name="DTOSchuljahresabschnitte.abschnitt.multiple", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.Abschnitt IN :value")
@NamedQuery(name="DTOSchuljahresabschnitte.vorigerabschnitt_id", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.VorigerAbschnitt_ID = :value")
@NamedQuery(name="DTOSchuljahresabschnitte.vorigerabschnitt_id.multiple", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.VorigerAbschnitt_ID IN :value")
@NamedQuery(name="DTOSchuljahresabschnitte.folgeabschnitt_id", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.FolgeAbschnitt_ID = :value")
@NamedQuery(name="DTOSchuljahresabschnitte.folgeabschnitt_id.multiple", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.FolgeAbschnitt_ID IN :value")
@NamedQuery(name="DTOSchuljahresabschnitte.primaryKeyQuery", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchuljahresabschnitte.all.migration", query="SELECT e FROM DTOSchuljahresabschnitte e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Jahr","Abschnitt","VorigerAbschnitt_ID","FolgeAbschnitt_ID"})
public class DTOSchuljahresabschnitte {

	/** ID des Schuljahresabschnittes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schuljahr des Schuljahresabschnitts (z.B. 2012 für 2012/13) */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt des Schuljahresabschnitts */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** ID des vorigen Schuljahresabschnitts */
	@Column(name = "VorigerAbschnitt_ID")
	@JsonProperty
	public Long VorigerAbschnitt_ID;

	/** ID des nachfolgenden Schuljahresabschnitts */
	@Column(name = "FolgeAbschnitt_ID")
	@JsonProperty
	public Long FolgeAbschnitt_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuljahresabschnitte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuljahresabschnitte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuljahresabschnitte ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public DTOSchuljahresabschnitte(final Long ID, final Integer Jahr, final Integer Abschnitt) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
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
		DTOSchuljahresabschnitte other = (DTOSchuljahresabschnitte) obj;
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
		return "DTOSchuljahresabschnitte(ID=" + this.ID + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", VorigerAbschnitt_ID=" + this.VorigerAbschnitt_ID + ", FolgeAbschnitt_ID=" + this.FolgeAbschnitt_ID + ")";
	}

}