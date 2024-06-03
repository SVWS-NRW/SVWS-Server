package de.svws_nrw.db.dto.current.gost.kursblockung;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Schienen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Schienen")
@JsonPropertyOrder({"ID", "Blockung_ID", "Nummer", "Bezeichnung", "Wochenstunden"})
public final class DTOGostBlockungSchiene {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostBlockungSchiene e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Blockung_ID */
	public static final String QUERY_BY_BLOCKUNG_ID = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Blockung_ID */
	public static final String QUERY_LIST_BY_BLOCKUNG_ID = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nummer */
	public static final String QUERY_BY_NUMMER = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Nummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nummer */
	public static final String QUERY_LIST_BY_NUMMER = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Nummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochenstunden */
	public static final String QUERY_BY_WOCHENSTUNDEN = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochenstunden */
	public static final String QUERY_LIST_BY_WOCHENSTUNDEN = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Wochenstunden IN ?1";

	/** ID der Schiene in der Blockung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Blockung */
	@Column(name = "Blockung_ID")
	@JsonProperty
	public long Blockung_ID;

	/** Die Nummer der Schiene, beginnend bei 1 */
	@Column(name = "Nummer")
	@JsonProperty
	public int Nummer;

	/** Bezeichnung der Schiene (z.B. LK Schiene 1) */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Anzahl der Wochenstunden, welche der Schiene zugeordnet sind */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public int Wochenstunden;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungSchiene ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungSchiene() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungSchiene ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Blockung_ID   der Wert für das Attribut Blockung_ID
	 * @param Nummer   der Wert für das Attribut Nummer
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 */
	public DTOGostBlockungSchiene(final long ID, final long Blockung_ID, final int Nummer, final String Bezeichnung, final int Wochenstunden) {
		this.ID = ID;
		this.Blockung_ID = Blockung_ID;
		this.Nummer = Nummer;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		this.Wochenstunden = Wochenstunden;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungSchiene other = (DTOGostBlockungSchiene) obj;
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
		return "DTOGostBlockungSchiene(ID=" + this.ID + ", Blockung_ID=" + this.Blockung_ID + ", Nummer=" + this.Nummer + ", Bezeichnung=" + this.Bezeichnung + ", Wochenstunden=" + this.Wochenstunden + ")";
	}

}
