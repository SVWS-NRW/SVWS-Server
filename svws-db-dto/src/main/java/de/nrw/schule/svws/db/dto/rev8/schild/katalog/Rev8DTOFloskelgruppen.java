package de.nrw.schule.svws.db.dto.rev8.schild.katalog;

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
@NamedQuery(name="Rev8DTOFloskelgruppen.all", query="SELECT e FROM Rev8DTOFloskelgruppen e")
@NamedQuery(name="Rev8DTOFloskelgruppen.kuerzel", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev8DTOFloskelgruppen.kuerzel.multiple", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev8DTOFloskelgruppen.hauptgruppe", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Hauptgruppe = :value")
@NamedQuery(name="Rev8DTOFloskelgruppen.hauptgruppe.multiple", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Hauptgruppe IN :value")
@NamedQuery(name="Rev8DTOFloskelgruppen.bezeichnung", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOFloskelgruppen.bezeichnung.multiple", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOFloskelgruppen.farbe", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Farbe = :value")
@NamedQuery(name="Rev8DTOFloskelgruppen.farbe.multiple", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Farbe IN :value")
@NamedQuery(name="Rev8DTOFloskelgruppen.primaryKeyQuery", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Kuerzel = ?1")
@NamedQuery(name="Rev8DTOFloskelgruppen.all.migration", query="SELECT e FROM Rev8DTOFloskelgruppen e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel","Hauptgruppe","Bezeichnung","Farbe"})
public class Rev8DTOFloskelgruppen {

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOFloskelgruppen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOFloskelgruppen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOFloskelgruppen ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public Rev8DTOFloskelgruppen(final String Kuerzel, final String Bezeichnung) {
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
		Rev8DTOFloskelgruppen other = (Rev8DTOFloskelgruppen) obj;
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
		return "Rev8DTOFloskelgruppen(Kuerzel=" + this.Kuerzel + ", Hauptgruppe=" + this.Hauptgruppe + ", Bezeichnung=" + this.Bezeichnung + ", Farbe=" + this.Farbe + ")";
	}

}