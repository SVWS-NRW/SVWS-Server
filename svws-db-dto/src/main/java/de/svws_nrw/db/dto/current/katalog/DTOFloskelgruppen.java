package de.svws_nrw.db.dto.current.katalog;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Katalog_Floskeln_Gruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Katalog_Floskeln_Gruppen")
@JsonPropertyOrder({"ID", "Kuerzel", "Hauptgruppe_ID", "Bezeichnung", "Farbe"})
public final class DTOFloskelgruppen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOFloskelgruppen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOFloskelgruppen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOFloskelgruppen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOFloskelgruppen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOFloskelgruppen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOFloskelgruppen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOFloskelgruppen e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOFloskelgruppen e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Hauptgruppe_ID */
	public static final String QUERY_BY_HAUPTGRUPPE_ID = "SELECT e FROM DTOFloskelgruppen e WHERE e.Hauptgruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Hauptgruppe_ID */
	public static final String QUERY_LIST_BY_HAUPTGRUPPE_ID = "SELECT e FROM DTOFloskelgruppen e WHERE e.Hauptgruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOFloskelgruppen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOFloskelgruppen e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Farbe */
	public static final String QUERY_BY_FARBE = "SELECT e FROM DTOFloskelgruppen e WHERE e.Farbe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Farbe */
	public static final String QUERY_LIST_BY_FARBE = "SELECT e FROM DTOFloskelgruppen e WHERE e.Farbe IN ?1";

	/** ID der Floskelgruppe (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Das Kürzel der Floskelgruppe */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Die übergeordnete Hauptgruppe, welche spezifiziert zu welchem Bemerkungskatalog die Floskeln der Floskelgruppe gehören. (siehe Core-Type) */
	@Column(name = "Hauptgruppe_ID")
	@JsonProperty
	public Long Hauptgruppe_ID;

	/** Die Bezeichnung der Floskelgruppe */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Farbe für die Floskelgruppe */
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
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOFloskelgruppen(final long ID, final String Kuerzel, final String Bezeichnung) {
		this.ID = ID;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOFloskelgruppen other = (DTOFloskelgruppen) obj;
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
		return "DTOFloskelgruppen(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Hauptgruppe_ID=" + this.Hauptgruppe_ID + ", Bezeichnung=" + this.Bezeichnung + ", Farbe=" + this.Farbe + ")";
	}

}
