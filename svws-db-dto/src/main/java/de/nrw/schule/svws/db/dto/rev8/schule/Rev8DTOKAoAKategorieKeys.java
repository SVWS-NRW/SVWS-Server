package de.nrw.schule.svws.db.dto.rev8.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle KAoA_Kategorie_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KAoA_Kategorie_Keys")
@NamedQuery(name="Rev8DTOKAoAKategorieKeys.all", query="SELECT e FROM Rev8DTOKAoAKategorieKeys e")
@NamedQuery(name="Rev8DTOKAoAKategorieKeys.id", query="SELECT e FROM Rev8DTOKAoAKategorieKeys e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKAoAKategorieKeys.id.multiple", query="SELECT e FROM Rev8DTOKAoAKategorieKeys e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKAoAKategorieKeys.primaryKeyQuery", query="SELECT e FROM Rev8DTOKAoAKategorieKeys e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKAoAKategorieKeys.all.migration", query="SELECT e FROM Rev8DTOKAoAKategorieKeys e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID"})
public class Rev8DTOKAoAKategorieKeys {

	/** Die eindeutige ID der Kategorie */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKAoAKategorieKeys ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKAoAKategorieKeys() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKAoAKategorieKeys ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOKAoAKategorieKeys(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKAoAKategorieKeys other = (Rev8DTOKAoAKategorieKeys) obj;
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
		return "Rev8DTOKAoAKategorieKeys(ID=" + this.ID + ")";
	}

}