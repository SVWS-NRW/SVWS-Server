package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunftsart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunftsart")
@JsonPropertyOrder({"ID", "Kuerzel", "gueltigVon", "gueltigBis"})
public final class DTOHerkunftsart {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOHerkunftsart e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOHerkunftsart e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOHerkunftsart e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOHerkunftsart e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOHerkunftsart e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOHerkunftsart e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOHerkunftsart e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOHerkunftsart e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM DTOHerkunftsart e WHERE e.gueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM DTOHerkunftsart e WHERE e.gueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM DTOHerkunftsart e WHERE e.gueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM DTOHerkunftsart e WHERE e.gueltigBis IN ?1";

	/** Die ID der Herkunftsart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Das Kürzel der Herkunftsart */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftsart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOHerkunftsart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftsart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public DTOHerkunftsart(final long ID, final String Kuerzel) {
		this.ID = ID;
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
		DTOHerkunftsart other = (DTOHerkunftsart) obj;
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
		return "DTOHerkunftsart(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
