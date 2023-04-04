package de.svws_nrw.db.dto.current.schild.berufskolleg;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Zertifikate.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Zertifikate")
@NamedQuery(name = "DTOKatalogZertifikate.all", query = "SELECT e FROM DTOKatalogZertifikate e")
@NamedQuery(name = "DTOKatalogZertifikate.kuerzel", query = "SELECT e FROM DTOKatalogZertifikate e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOKatalogZertifikate.kuerzel.multiple", query = "SELECT e FROM DTOKatalogZertifikate e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOKatalogZertifikate.bezeichnung", query = "SELECT e FROM DTOKatalogZertifikate e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOKatalogZertifikate.bezeichnung.multiple", query = "SELECT e FROM DTOKatalogZertifikate e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOKatalogZertifikate.primaryKeyQuery", query = "SELECT e FROM DTOKatalogZertifikate e WHERE e.Kuerzel = ?1")
@NamedQuery(name = "DTOKatalogZertifikate.all.migration", query = "SELECT e FROM DTOKatalogZertifikate e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel", "Bezeichnung"})
public final class DTOKatalogZertifikate {

	/** Kürzel des Zertifikats */
	@Id
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Bezeichnung des Zertifikats */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogZertifikate ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKatalogZertifikate() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogZertifikate ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOKatalogZertifikate(final String Kuerzel, final String Bezeichnung) {
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKatalogZertifikate other = (DTOKatalogZertifikate) obj;
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
		return "DTOKatalogZertifikate(Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ")";
	}

}
