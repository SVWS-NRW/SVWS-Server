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
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunft_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunft_Keys")
@NamedQuery(name = "DTOHerkunftKeys.all", query = "SELECT e FROM DTOHerkunftKeys e")
@NamedQuery(name = "DTOHerkunftKeys.kuerzel", query = "SELECT e FROM DTOHerkunftKeys e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOHerkunftKeys.kuerzel.multiple", query = "SELECT e FROM DTOHerkunftKeys e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOHerkunftKeys.primaryKeyQuery", query = "SELECT e FROM DTOHerkunftKeys e WHERE e.Kuerzel = ?1")
@NamedQuery(name = "DTOHerkunftKeys.primaryKeyQuery.multiple", query = "SELECT e FROM DTOHerkunftKeys e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOHerkunftKeys.all.migration", query = "SELECT e FROM DTOHerkunftKeys e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel"})
public final class DTOHerkunftKeys {

	/** Das Kürzel der Herkunft */
	@Id
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftKeys ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOHerkunftKeys() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftKeys ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public DTOHerkunftKeys(final String Kuerzel) {
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOHerkunftKeys other = (DTOHerkunftKeys) obj;
		if (Kuerzel == null) {
			if (other.Kuerzel != null)
				return false;
		} else if (!Kuerzel.equals(other.Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Kuerzel == null) ? 0 : Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOHerkunftKeys(Kuerzel=" + this.Kuerzel + ")";
	}

}
