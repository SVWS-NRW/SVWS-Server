package de.nrw.schule.svws.db.dto.rev8.svws.enm;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EnmLeistungsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EnmLeistungsdaten")
@NamedQuery(name="Rev8DTOEnmLeistungsdaten.all", query="SELECT e FROM Rev8DTOEnmLeistungsdaten e")
@NamedQuery(name="Rev8DTOEnmLeistungsdaten.id", query="SELECT e FROM Rev8DTOEnmLeistungsdaten e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOEnmLeistungsdaten.id.multiple", query="SELECT e FROM Rev8DTOEnmLeistungsdaten e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOEnmLeistungsdaten.tsnotenkrz", query="SELECT e FROM Rev8DTOEnmLeistungsdaten e WHERE e.tsNotenKrz = :value")
@NamedQuery(name="Rev8DTOEnmLeistungsdaten.tsnotenkrz.multiple", query="SELECT e FROM Rev8DTOEnmLeistungsdaten e WHERE e.tsNotenKrz IN :value")
@NamedQuery(name="Rev8DTOEnmLeistungsdaten.primaryKeyQuery", query="SELECT e FROM Rev8DTOEnmLeistungsdaten e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOEnmLeistungsdaten.all.migration", query="SELECT e FROM Rev8DTOEnmLeistungsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","tsNotenKrz"})
public class Rev8DTOEnmLeistungsdaten {

	/** ID der Leistungsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Zeitstempel der letzten Änderung an der Note. */
	@Column(name = "tsNotenKrz")
	@JsonProperty
	public String tsNotenKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOEnmLeistungsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOEnmLeistungsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOEnmLeistungsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsNotenKrz   der Wert für das Attribut tsNotenKrz
	 */
	public Rev8DTOEnmLeistungsdaten(final Long ID, final String tsNotenKrz) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (tsNotenKrz == null) { 
			throw new NullPointerException("tsNotenKrz must not be null");
		}
		this.tsNotenKrz = tsNotenKrz;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOEnmLeistungsdaten other = (Rev8DTOEnmLeistungsdaten) obj;
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
		return "Rev8DTOEnmLeistungsdaten(ID=" + this.ID + ", tsNotenKrz=" + this.tsNotenKrz + ")";
	}

}