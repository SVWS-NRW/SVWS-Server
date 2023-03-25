package de.svws_nrw.db.dto.migration.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Floskelgruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Floskelgruppen")
@NamedQuery(name="MigrationDTOFloskelgruppen.all", query="SELECT e FROM MigrationDTOFloskelgruppen e")
@NamedQuery(name="MigrationDTOFloskelgruppen.kuerzel", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Kuerzel = :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.kuerzel.multiple", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Kuerzel IN :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.hauptgruppe", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Hauptgruppe = :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.hauptgruppe.multiple", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Hauptgruppe IN :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.bezeichnung", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.bezeichnung.multiple", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.farbe", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Farbe = :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.farbe.multiple", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Farbe IN :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.schulnreigner", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.schulnreigner.multiple", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOFloskelgruppen.primaryKeyQuery", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Kuerzel = ?1")
@NamedQuery(name="MigrationDTOFloskelgruppen.all.migration", query="SELECT e FROM MigrationDTOFloskelgruppen e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel","Hauptgruppe","Bezeichnung","Farbe","SchulnrEigner"})
public class MigrationDTOFloskelgruppen {

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

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFloskelgruppen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFloskelgruppen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFloskelgruppen ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOFloskelgruppen(final String Kuerzel, final String Bezeichnung, final Integer SchulnrEigner) {
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (SchulnrEigner == null) { 
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFloskelgruppen other = (MigrationDTOFloskelgruppen) obj;
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
		return "MigrationDTOFloskelgruppen(Kuerzel=" + this.Kuerzel + ", Hauptgruppe=" + this.Hauptgruppe + ", Bezeichnung=" + this.Bezeichnung + ", Farbe=" + this.Farbe + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}