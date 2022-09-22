package de.nrw.schule.svws.db.dto.current.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerLehramtLehrbef.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOLehrerLehramtBefaehigungPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerLehramtLehrbef")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.all", query="SELECT e FROM DTOLehrerLehramtBefaehigung e")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.lehrer_id", query="SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.lehrer_id.multiple", query="SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.lehrbefkrz", query="SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehrbefKrz = :value")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.lehrbefkrz.multiple", query="SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehrbefKrz IN :value")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.lehrbefanerkennungkrz", query="SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehrbefAnerkennungKrz = :value")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.lehrbefanerkennungkrz.multiple", query="SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehrbefAnerkennungKrz IN :value")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.primaryKeyQuery", query="SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID = ?1 AND e.LehrbefKrz = ?2")
@NamedQuery(name="DTOLehrerLehramtBefaehigung.all.migration", query="SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID IS NOT NULL AND e.LehrbefKrz IS NOT NULL")
@JsonPropertyOrder({"Lehrer_ID","LehrbefKrz","LehrbefAnerkennungKrz"})
public class DTOLehrerLehramtBefaehigung {

	/** LehrerID zu der die Lehrbefähigung gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Kürzel der Lehrbefähigung */
	@Id
	@Column(name = "LehrbefKrz")
	@JsonProperty
	public String LehrbefKrz;

	/** Kürzel der LehrbefähigungsAnerkennung */
	@Column(name = "LehrbefAnerkennungKrz")
	@JsonProperty
	public String LehrbefAnerkennungKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLehramtBefaehigung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerLehramtBefaehigung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLehramtBefaehigung ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOLehrerLehramtBefaehigung(final Long Lehrer_ID) {
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
		DTOLehrerLehramtBefaehigung other = (DTOLehrerLehramtBefaehigung) obj;
		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;

		if (LehrbefKrz == null) {
			if (other.LehrbefKrz != null)
				return false;
		} else if (!LehrbefKrz.equals(other.LehrbefKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());

		result = prime * result + ((LehrbefKrz == null) ? 0 : LehrbefKrz.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLehrerLehramtBefaehigung(Lehrer_ID=" + this.Lehrer_ID + ", LehrbefKrz=" + this.LehrbefKrz + ", LehrbefAnerkennungKrz=" + this.LehrbefAnerkennungKrz + ")";
	}

}