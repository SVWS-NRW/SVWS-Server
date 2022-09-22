package de.nrw.schule.svws.db.dto.current.gost;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Jahrgang_Beratungslehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostJahrgangBeratungslehrerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Jahrgang_Beratungslehrer")
@NamedQuery(name="DTOGostJahrgangBeratungslehrer.all", query="SELECT e FROM DTOGostJahrgangBeratungslehrer e")
@NamedQuery(name="DTOGostJahrgangBeratungslehrer.abi_jahrgang", query="SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name="DTOGostJahrgangBeratungslehrer.abi_jahrgang.multiple", query="SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name="DTOGostJahrgangBeratungslehrer.lehrer_id", query="SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="DTOGostJahrgangBeratungslehrer.lehrer_id.multiple", query="SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="DTOGostJahrgangBeratungslehrer.primaryKeyQuery", query="SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Abi_Jahrgang = ?1 AND e.Lehrer_ID = ?2")
@NamedQuery(name="DTOGostJahrgangBeratungslehrer.all.migration", query="SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Abi_Jahrgang IS NOT NULL AND e.Lehrer_ID IS NOT NULL")
@JsonPropertyOrder({"Abi_Jahrgang","Lehrer_ID"})
public class DTOGostJahrgangBeratungslehrer {

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht  */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public Integer Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: ID des Beratungslehrers in der Lehrertabelle */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangBeratungslehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangBeratungslehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangBeratungslehrer ohne eine Initialisierung der Attribute.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOGostJahrgangBeratungslehrer(final Integer Abi_Jahrgang, final Long Lehrer_ID) {
		if (Abi_Jahrgang == null) { 
			throw new NullPointerException("Abi_Jahrgang must not be null");
		}
		this.Abi_Jahrgang = Abi_Jahrgang;
		if (Lehrer_ID == null) { 
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangBeratungslehrer other = (DTOGostJahrgangBeratungslehrer) obj;
		if (Abi_Jahrgang == null) {
			if (other.Abi_Jahrgang != null)
				return false;
		} else if (!Abi_Jahrgang.equals(other.Abi_Jahrgang))
			return false;

		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Abi_Jahrgang == null) ? 0 : Abi_Jahrgang.hashCode());

		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostJahrgangBeratungslehrer(Abi_Jahrgang=" + this.Abi_Jahrgang + ", Lehrer_ID=" + this.Lehrer_ID + ")";
	}

}