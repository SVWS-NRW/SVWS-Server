package de.nrw.schule.svws.db.dto.rev8.statkue;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_LehrerFachrAnerkennung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_LehrerFachrAnerkennung")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.all", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.id", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.id.multiple", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.kurztext", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Kurztext = :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.kurztext.multiple", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Kurztext IN :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.langtext", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Langtext = :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.langtext.multiple", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Langtext IN :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.beginn", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Beginn = :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.beginn.multiple", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Beginn IN :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.ende", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Ende = :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.ende.multiple", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Ende IN :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.sort", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Sort = :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.sort.multiple", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.Sort IN :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.gueltigvon", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.gueltigbis", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOStatkueLehrerFachrichtungAnerkennung.all.migration", query="SELECT e FROM Rev8DTOStatkueLehrerFachrichtungAnerkennung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kurztext","Langtext","Beginn","Ende","Sort","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueLehrerFachrichtungAnerkennung {

	/** Statkue Tabelle IT.NRW: ID der Fachanerkennung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Statkue Tabelle IT.NRW: Statistikschlüssel der Fachanerkennung */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Statkue Tabelle IT.NRW: Langtext der Fachanerkennung */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/** Statkue Tabelle IT.NRW: Beginn der Gültigkeit der Fachanerkennung */
	@Column(name = "Beginn")
	@JsonProperty
	public String Beginn;

	/** Statkue Tabelle IT.NRW: Ende der Gültigkeit der Fachanerkennung */
	@Column(name = "Ende")
	@JsonProperty
	public String Ende;

	/** Statkue Tabelle IT.NRW: Sortierung der Fachanerkennung */
	@Column(name = "Sort")
	@JsonProperty
	public Integer Sort;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueLehrerFachrichtungAnerkennung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStatkueLehrerFachrichtungAnerkennung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueLehrerFachrichtungAnerkennung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kurztext   der Wert für das Attribut Kurztext
	 * @param Langtext   der Wert für das Attribut Langtext
	 * @param Sort   der Wert für das Attribut Sort
	 */
	public Rev8DTOStatkueLehrerFachrichtungAnerkennung(final Long ID, final String Kurztext, final String Langtext, final Integer Sort) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kurztext == null) { 
			throw new NullPointerException("Kurztext must not be null");
		}
		this.Kurztext = Kurztext;
		if (Langtext == null) { 
			throw new NullPointerException("Langtext must not be null");
		}
		this.Langtext = Langtext;
		if (Sort == null) { 
			throw new NullPointerException("Sort must not be null");
		}
		this.Sort = Sort;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueLehrerFachrichtungAnerkennung other = (Rev8DTOStatkueLehrerFachrichtungAnerkennung) obj;
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
		return "Rev8DTOStatkueLehrerFachrichtungAnerkennung(ID=" + this.ID + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", Sort=" + this.Sort + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}