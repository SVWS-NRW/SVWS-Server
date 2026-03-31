package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Stundentafeln_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Stundentafeln_Faecher")
@JsonPropertyOrder({"ID", "Stundentafel_ID", "Abschnitt", "Fach_ID", "Wochenstunden", "DavonErgaenzungsstunden"})
public final class DTOUvStundentafelFach {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvStundentafelFach e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvStundentafelFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvStundentafelFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvStundentafelFach e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvStundentafelFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvStundentafelFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundentafel_ID */
	public static final String QUERY_BY_STUNDENTAFEL_ID = "SELECT e FROM DTOUvStundentafelFach e WHERE e.Stundentafel_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundentafel_ID */
	public static final String QUERY_LIST_BY_STUNDENTAFEL_ID = "SELECT e FROM DTOUvStundentafelFach e WHERE e.Stundentafel_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM DTOUvStundentafelFach e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM DTOUvStundentafelFach e WHERE e.Abschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOUvStundentafelFach e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOUvStundentafelFach e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochenstunden */
	public static final String QUERY_BY_WOCHENSTUNDEN = "SELECT e FROM DTOUvStundentafelFach e WHERE e.Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochenstunden */
	public static final String QUERY_LIST_BY_WOCHENSTUNDEN = "SELECT e FROM DTOUvStundentafelFach e WHERE e.Wochenstunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DavonErgaenzungsstunden */
	public static final String QUERY_BY_DAVONERGAENZUNGSSTUNDEN = "SELECT e FROM DTOUvStundentafelFach e WHERE e.DavonErgaenzungsstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DavonErgaenzungsstunden */
	public static final String QUERY_LIST_BY_DAVONERGAENZUNGSSTUNDEN = "SELECT e FROM DTOUvStundentafelFach e WHERE e.DavonErgaenzungsstunden IN ?1";

	/** ID des Stundentafel-Fachs (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Fremdschlüssel auf die Stundentafel (Tabelle UV_Stundentafeln) */
	@Column(name = "Stundentafel_ID")
	@JsonProperty
	public long Stundentafel_ID;

	/** Abschnitt des Schuljahres */
	@Column(name = "Abschnitt")
	@JsonProperty
	public int Abschnitt;

	/** Fremdschlüssel auf das Fach der UV (Tabelle UV_Faecher) */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Die Anzahl der Wochenstunden für das Fach */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public double Wochenstunden;

	/** Die Anzahl der Ergänzungsstunden für das Fach (in Wochenstunden enthalten) */
	@Column(name = "DavonErgaenzungsstunden")
	@JsonProperty
	public double DavonErgaenzungsstunden;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvStundentafelFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvStundentafelFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvStundentafelFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundentafel_ID   der Wert für das Attribut Stundentafel_ID
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 * @param DavonErgaenzungsstunden   der Wert für das Attribut DavonErgaenzungsstunden
	 */
	public DTOUvStundentafelFach(final long ID, final long Stundentafel_ID, final int Abschnitt, final long Fach_ID, final double Wochenstunden, final double DavonErgaenzungsstunden) {
		this.ID = ID;
		this.Stundentafel_ID = Stundentafel_ID;
		this.Abschnitt = Abschnitt;
		this.Fach_ID = Fach_ID;
		this.Wochenstunden = Wochenstunden;
		this.DavonErgaenzungsstunden = DavonErgaenzungsstunden;
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
		DTOUvStundentafelFach other = (DTOUvStundentafelFach) obj;
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
		return "DTOUvStundentafelFach(ID=" + this.ID + ", Stundentafel_ID=" + this.Stundentafel_ID + ", Abschnitt=" + this.Abschnitt + ", Fach_ID=" + this.Fach_ID + ", Wochenstunden=" + this.Wochenstunden + ", DavonErgaenzungsstunden=" + this.DavonErgaenzungsstunden + ")";
	}

}
