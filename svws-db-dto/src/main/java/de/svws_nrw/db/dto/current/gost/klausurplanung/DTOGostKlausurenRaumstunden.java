package de.svws_nrw.db.dto.current.gost.klausurplanung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Raumstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Raumstunden")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.all", query = "SELECT e FROM DTOGostKlausurenRaumstunden e")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.id", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.id.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.klausurraum_id", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.Klausurraum_ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.klausurraum_id.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.Klausurraum_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.zeitraster_id", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.Zeitraster_ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.zeitraster_id.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.Zeitraster_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstunden.all.migration", query = "SELECT e FROM DTOGostKlausurenRaumstunden e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Klausurraum_ID", "Zeitraster_ID"})
public final class DTOGostKlausurenRaumstunden {

	/** ID der Raumstunde (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Klausurraums */
	@Column(name = "Klausurraum_ID")
	@JsonProperty
	public long Klausurraum_ID;

	/** ID des Zeitrasters */
	@Column(name = "Zeitraster_ID")
	@JsonProperty
	public long Zeitraster_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaumstunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenRaumstunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaumstunden ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Klausurraum_ID   der Wert für das Attribut Klausurraum_ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 */
	public DTOGostKlausurenRaumstunden(final long ID, final long Klausurraum_ID, final long Zeitraster_ID) {
		this.ID = ID;
		this.Klausurraum_ID = Klausurraum_ID;
		this.Zeitraster_ID = Zeitraster_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenRaumstunden other = (DTOGostKlausurenRaumstunden) obj;
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
		return "DTOGostKlausurenRaumstunden(ID=" + this.ID + ", Klausurraum_ID=" + this.Klausurraum_ID + ", Zeitraster_ID=" + this.Zeitraster_ID + ")";
	}

}
