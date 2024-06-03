package de.svws_nrw.db.dto.current.schild.katalog;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@JsonPropertyOrder({"Kuerzel", "Hauptgruppe", "Bezeichnung", "Farbe"})
public final class DTOFloskelgruppen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOFloskelgruppen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOFloskelgruppen e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOFloskelgruppen e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOFloskelgruppen e WHERE e.Kuerzel IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOFloskelgruppen e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOFloskelgruppen e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Hauptgruppe */
	public static final String QUERY_BY_HAUPTGRUPPE = "SELECT e FROM DTOFloskelgruppen e WHERE e.Hauptgruppe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Hauptgruppe */
	public static final String QUERY_LIST_BY_HAUPTGRUPPE = "SELECT e FROM DTOFloskelgruppen e WHERE e.Hauptgruppe IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOFloskelgruppen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOFloskelgruppen e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Farbe */
	public static final String QUERY_BY_FARBE = "SELECT e FROM DTOFloskelgruppen e WHERE e.Farbe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Farbe */
	public static final String QUERY_LIST_BY_FARBE = "SELECT e FROM DTOFloskelgruppen e WHERE e.Farbe IN ?1";

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
	 * Erstellt ein neues Objekt der Klasse DTOFloskelgruppen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFloskelgruppen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFloskelgruppen ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOFloskelgruppen(final String Kuerzel, final String Bezeichnung) {
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
		DTOFloskelgruppen other = (DTOFloskelgruppen) obj;
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
		return "DTOFloskelgruppen(Kuerzel=" + this.Kuerzel + ", Hauptgruppe=" + this.Hauptgruppe + ", Bezeichnung=" + this.Bezeichnung + ", Farbe=" + this.Farbe + ")";
	}

}
