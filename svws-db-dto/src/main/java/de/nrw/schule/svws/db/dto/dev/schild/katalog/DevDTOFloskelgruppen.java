package de.nrw.schule.svws.db.dto.dev.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Floskelgruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Floskelgruppen")
@NamedQuery(name="DevDTOFloskelgruppen.all", query="SELECT e FROM DevDTOFloskelgruppen e")
@NamedQuery(name="DevDTOFloskelgruppen.kuerzel", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Kuerzel = :value")
@NamedQuery(name="DevDTOFloskelgruppen.kuerzel.multiple", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DevDTOFloskelgruppen.hauptgruppe", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Hauptgruppe = :value")
@NamedQuery(name="DevDTOFloskelgruppen.hauptgruppe.multiple", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Hauptgruppe IN :value")
@NamedQuery(name="DevDTOFloskelgruppen.bezeichnung", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOFloskelgruppen.bezeichnung.multiple", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOFloskelgruppen.farbe", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Farbe = :value")
@NamedQuery(name="DevDTOFloskelgruppen.farbe.multiple", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Farbe IN :value")
@NamedQuery(name="DevDTOFloskelgruppen.primaryKeyQuery", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Kuerzel = ?1")
@NamedQuery(name="DevDTOFloskelgruppen.all.migration", query="SELECT e FROM DevDTOFloskelgruppen e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel","Hauptgruppe","Bezeichnung","Farbe"})
public class DevDTOFloskelgruppen {

	/** Kürzel der Floskelgruppe */
	@Id
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Hauptgruppe der Floskelgruppe ermöglich es Floskel Unterkategorien zuzuordnen */
	@Column(name = "Hauptgruppe")
	@JsonProperty
	public String Hauptgruppe;

	/** Bezeichnung der Floskelgruppe */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Farbe für die Flsokelgruppe */
	@Column(name = "Farbe")
	@JsonProperty
	public Integer Farbe;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOFloskelgruppen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOFloskelgruppen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOFloskelgruppen ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOFloskelgruppen(final String Kuerzel, final String Bezeichnung) {
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
		DevDTOFloskelgruppen other = (DevDTOFloskelgruppen) obj;
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
		return "DevDTOFloskelgruppen(Kuerzel=" + this.Kuerzel + ", Hauptgruppe=" + this.Hauptgruppe + ", Bezeichnung=" + this.Bezeichnung + ", Farbe=" + this.Farbe + ")";
	}

}