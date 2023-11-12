package de.svws_nrw.db.dto.current.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerStatus_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerStatus_Keys")
@NamedQuery(name = "DTOSchuelerStatus.all", query = "SELECT e FROM DTOSchuelerStatus e")
@NamedQuery(name = "DTOSchuelerStatus.id", query = "SELECT e FROM DTOSchuelerStatus e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerStatus.id.multiple", query = "SELECT e FROM DTOSchuelerStatus e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerStatus.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerStatus e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerStatus.primaryKeyQuery.multiple", query = "SELECT e FROM DTOSchuelerStatus e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOSchuelerStatus.all.migration", query = "SELECT e FROM DTOSchuelerStatus e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID"})
public final class DTOSchuelerStatus {

	/** ID des Schüler-Status */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public int ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerStatus ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerStatus() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerStatus ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOSchuelerStatus(final int ID) {
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
		DTOSchuelerStatus other = (DTOSchuelerStatus) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerStatus(ID=" + this.ID + ")";
	}

}
