package de.svws_nrw.db.dto.current.schild.stundenplan;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Raeume.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Raeume")
@JsonPropertyOrder({"ID", "Stundenplan_ID", "Kuerzel", "Beschreibung", "Groesse"})
public final class DTOStundenplanRaum {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundenplanRaum e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundenplanRaum e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundenplanRaum e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundenplanRaum e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundenplanRaum e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundenplanRaum e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundenplan_ID */
	public static final String QUERY_BY_STUNDENPLAN_ID = "SELECT e FROM DTOStundenplanRaum e WHERE e.Stundenplan_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundenplan_ID */
	public static final String QUERY_LIST_BY_STUNDENPLAN_ID = "SELECT e FROM DTOStundenplanRaum e WHERE e.Stundenplan_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOStundenplanRaum e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOStundenplanRaum e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM DTOStundenplanRaum e WHERE e.Beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM DTOStundenplanRaum e WHERE e.Beschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Groesse */
	public static final String QUERY_BY_GROESSE = "SELECT e FROM DTOStundenplanRaum e WHERE e.Groesse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Groesse */
	public static final String QUERY_LIST_BY_GROESSE = "SELECT e FROM DTOStundenplanRaum e WHERE e.Groesse IN ?1";

	/** Die ID identifiziert einen Raumeintrag für einen Stundenplan eindeutig - hat keinen Bezug zur ID der Katalog-Tabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Stundenplans, dem dieser Raumeintrag zugeordnet wird */
	@Column(name = "Stundenplan_ID")
	@JsonProperty
	public long Stundenplan_ID;

	/** Das Kürzel des Raums */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gegebenenfalls eine ausführlichere Beschreibung des Raumes */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Die Größe des Raumes, d.h. wie viele Schüler hier max. Platz haben */
	@Column(name = "Groesse")
	@JsonProperty
	public int Groesse;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanRaum ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanRaum() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanRaum ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundenplan_ID   der Wert für das Attribut Stundenplan_ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 * @param Groesse   der Wert für das Attribut Groesse
	 */
	public DTOStundenplanRaum(final long ID, final long Stundenplan_ID, final String Kuerzel, final String Beschreibung, final int Groesse) {
		this.ID = ID;
		this.Stundenplan_ID = Stundenplan_ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Beschreibung == null) {
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
		this.Groesse = Groesse;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanRaum other = (DTOStundenplanRaum) obj;
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
		return "DTOStundenplanRaum(ID=" + this.ID + ", Stundenplan_ID=" + this.Stundenplan_ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ", Groesse=" + this.Groesse + ")";
	}

}
