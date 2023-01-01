package de.nrw.schule.svws.db.dto.rev9.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerLehramt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev9DTOLehrerLehramtPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerLehramt")
@NamedQuery(name="Rev9DTOLehrerLehramt.all", query="SELECT e FROM Rev9DTOLehrerLehramt e")
@NamedQuery(name="Rev9DTOLehrerLehramt.lehrer_id", query="SELECT e FROM Rev9DTOLehrerLehramt e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="Rev9DTOLehrerLehramt.lehrer_id.multiple", query="SELECT e FROM Rev9DTOLehrerLehramt e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="Rev9DTOLehrerLehramt.lehramtkrz", query="SELECT e FROM Rev9DTOLehrerLehramt e WHERE e.LehramtKrz = :value")
@NamedQuery(name="Rev9DTOLehrerLehramt.lehramtkrz.multiple", query="SELECT e FROM Rev9DTOLehrerLehramt e WHERE e.LehramtKrz IN :value")
@NamedQuery(name="Rev9DTOLehrerLehramt.lehramtanerkennungkrz", query="SELECT e FROM Rev9DTOLehrerLehramt e WHERE e.LehramtAnerkennungKrz = :value")
@NamedQuery(name="Rev9DTOLehrerLehramt.lehramtanerkennungkrz.multiple", query="SELECT e FROM Rev9DTOLehrerLehramt e WHERE e.LehramtAnerkennungKrz IN :value")
@NamedQuery(name="Rev9DTOLehrerLehramt.primaryKeyQuery", query="SELECT e FROM Rev9DTOLehrerLehramt e WHERE e.Lehrer_ID = ?1 AND e.LehramtKrz = ?2")
@NamedQuery(name="Rev9DTOLehrerLehramt.all.migration", query="SELECT e FROM Rev9DTOLehrerLehramt e WHERE e.Lehrer_ID IS NOT NULL AND e.LehramtKrz IS NOT NULL")
@JsonPropertyOrder({"Lehrer_ID","LehramtKrz","LehramtAnerkennungKrz"})
public class Rev9DTOLehrerLehramt {

	/** LehrerID zu der das Lehramt gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Lehramtskürzel */
	@Id
	@Column(name = "LehramtKrz")
	@JsonProperty
	public String LehramtKrz;

	/** Lehramts-Anerkennung-Kürzel */
	@Column(name = "LehramtAnerkennungKrz")
	@JsonProperty
	public String LehramtAnerkennungKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOLehrerLehramt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOLehrerLehramt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOLehrerLehramt ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public Rev9DTOLehrerLehramt(final Long Lehrer_ID) {
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
		Rev9DTOLehrerLehramt other = (Rev9DTOLehrerLehramt) obj;
		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;

		if (LehramtKrz == null) {
			if (other.LehramtKrz != null)
				return false;
		} else if (!LehramtKrz.equals(other.LehramtKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());

		result = prime * result + ((LehramtKrz == null) ? 0 : LehramtKrz.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOLehrerLehramt(Lehrer_ID=" + this.Lehrer_ID + ", LehramtKrz=" + this.LehramtKrz + ", LehramtAnerkennungKrz=" + this.LehramtAnerkennungKrz + ")";
	}

}