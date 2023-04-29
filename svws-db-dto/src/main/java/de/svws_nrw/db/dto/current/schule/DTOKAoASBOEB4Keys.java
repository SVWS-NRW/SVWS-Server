package de.svws_nrw.db.dto.current.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle KAoA_SBO_Ebene4_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KAoA_SBO_Ebene4_Keys")
@NamedQuery(name = "DTOKAoASBOEB4Keys.all", query = "SELECT e FROM DTOKAoASBOEB4Keys e")
@NamedQuery(name = "DTOKAoASBOEB4Keys.id", query = "SELECT e FROM DTOKAoASBOEB4Keys e WHERE e.ID = :value")
@NamedQuery(name = "DTOKAoASBOEB4Keys.id.multiple", query = "SELECT e FROM DTOKAoASBOEB4Keys e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKAoASBOEB4Keys.primaryKeyQuery", query = "SELECT e FROM DTOKAoASBOEB4Keys e WHERE e.ID = ?1")
@NamedQuery(name = "DTOKAoASBOEB4Keys.all.migration", query = "SELECT e FROM DTOKAoASBOEB4Keys e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID"})
public final class DTOKAoASBOEB4Keys {

	/** Die eindeutige ID des Merkmals der SBO-Ebene 4 */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKAoASBOEB4Keys ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKAoASBOEB4Keys() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKAoASBOEB4Keys ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOKAoASBOEB4Keys(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKAoASBOEB4Keys other = (DTOKAoASBOEB4Keys) obj;
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
		return "DTOKAoASBOEB4Keys(ID=" + this.ID + ")";
	}

}
