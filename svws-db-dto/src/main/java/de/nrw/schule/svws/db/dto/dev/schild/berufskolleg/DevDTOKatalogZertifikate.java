package de.nrw.schule.svws.db.dto.dev.schild.berufskolleg;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Zertifikate.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Zertifikate")
@NamedQuery(name="DevDTOKatalogZertifikate.all", query="SELECT e FROM DevDTOKatalogZertifikate e")
@NamedQuery(name="DevDTOKatalogZertifikate.kuerzel", query="SELECT e FROM DevDTOKatalogZertifikate e WHERE e.Kuerzel = :value")
@NamedQuery(name="DevDTOKatalogZertifikate.kuerzel.multiple", query="SELECT e FROM DevDTOKatalogZertifikate e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DevDTOKatalogZertifikate.bezeichnung", query="SELECT e FROM DevDTOKatalogZertifikate e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOKatalogZertifikate.bezeichnung.multiple", query="SELECT e FROM DevDTOKatalogZertifikate e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOKatalogZertifikate.primaryKeyQuery", query="SELECT e FROM DevDTOKatalogZertifikate e WHERE e.Kuerzel = ?1")
@NamedQuery(name="DevDTOKatalogZertifikate.all.migration", query="SELECT e FROM DevDTOKatalogZertifikate e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel","Bezeichnung"})
public class DevDTOKatalogZertifikate {

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
	 * Erstellt ein neues Objekt der Klasse DevDTOKatalogZertifikate ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOKatalogZertifikate() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKatalogZertifikate ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOKatalogZertifikate(final String Kuerzel, final String Bezeichnung) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOKatalogZertifikate other = (DevDTOKatalogZertifikate) obj;
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
		return "DevDTOKatalogZertifikate(Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ")";
	}

}