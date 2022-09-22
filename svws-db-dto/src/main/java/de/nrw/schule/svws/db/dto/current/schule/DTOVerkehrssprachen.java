package de.nrw.schule.svws.db.dto.current.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Verkehrssprachen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Verkehrssprachen")
@NamedQuery(name="DTOVerkehrssprachen.all", query="SELECT e FROM DTOVerkehrssprachen e")
@NamedQuery(name="DTOVerkehrssprachen.id", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.ID = :value")
@NamedQuery(name="DTOVerkehrssprachen.id.multiple", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.ID IN :value")
@NamedQuery(name="DTOVerkehrssprachen.iso3", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.Iso3 = :value")
@NamedQuery(name="DTOVerkehrssprachen.iso3.multiple", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.Iso3 IN :value")
@NamedQuery(name="DTOVerkehrssprachen.iso2", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.Iso2 = :value")
@NamedQuery(name="DTOVerkehrssprachen.iso2.multiple", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.Iso2 IN :value")
@NamedQuery(name="DTOVerkehrssprachen.bezeichnung", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOVerkehrssprachen.bezeichnung.multiple", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOVerkehrssprachen.primaryKeyQuery", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.ID = ?1")
@NamedQuery(name="DTOVerkehrssprachen.all.migration", query="SELECT e FROM DTOVerkehrssprachen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Iso3","Iso2","Bezeichnung"})
public class DTOVerkehrssprachen {

	/** Die ID der Verkehrssprache */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der dreistellige ISO 639-Code der Verkehrssprache */
	@Column(name = "Iso3")
	@JsonProperty
	public String Iso3;

	/** Der zweistellige ISO 639-1 Code der Verkehrssprache */
	@Column(name = "Iso2")
	@JsonProperty
	public String Iso2;

	/** Die Bezeichnung der Verkehrssprache */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOVerkehrssprachen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOVerkehrssprachen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOVerkehrssprachen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Iso3   der Wert für das Attribut Iso3
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOVerkehrssprachen(final Long ID, final String Iso3, final String Bezeichnung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Iso3 == null) { 
			throw new NullPointerException("Iso3 must not be null");
		}
		this.Iso3 = Iso3;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOVerkehrssprachen other = (DTOVerkehrssprachen) obj;
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
		return "DTOVerkehrssprachen(ID=" + this.ID + ", Iso3=" + this.Iso3 + ", Iso2=" + this.Iso2 + ", Bezeichnung=" + this.Bezeichnung + ")";
	}

}