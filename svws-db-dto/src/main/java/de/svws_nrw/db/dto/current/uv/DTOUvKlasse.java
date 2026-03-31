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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Klassen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Klassen")
@JsonPropertyOrder({"ID", "Planungsabschnitt_ID", "Schuljahresabschnitts_ID", "Bezeichnung", "Kuerzel", "Parallelitaet", "Stundentafel_ID", "Schuelergruppe_ID", "OrgFormKrz", "Fachklasse_ID", "ASDSchulformNr"})
public final class DTOUvKlasse {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvKlasse e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvKlasse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvKlasse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvKlasse e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvKlasse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvKlasse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOUvKlasse e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOUvKlasse e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOUvKlasse e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOUvKlasse e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Parallelitaet */
	public static final String QUERY_BY_PARALLELITAET = "SELECT e FROM DTOUvKlasse e WHERE e.Parallelitaet = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Parallelitaet */
	public static final String QUERY_LIST_BY_PARALLELITAET = "SELECT e FROM DTOUvKlasse e WHERE e.Parallelitaet IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundentafel_ID */
	public static final String QUERY_BY_STUNDENTAFEL_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Stundentafel_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundentafel_ID */
	public static final String QUERY_LIST_BY_STUNDENTAFEL_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Stundentafel_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuelergruppe_ID */
	public static final String QUERY_BY_SCHUELERGRUPPE_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Schuelergruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuelergruppe_ID */
	public static final String QUERY_LIST_BY_SCHUELERGRUPPE_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Schuelergruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes OrgFormKrz */
	public static final String QUERY_BY_ORGFORMKRZ = "SELECT e FROM DTOUvKlasse e WHERE e.OrgFormKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes OrgFormKrz */
	public static final String QUERY_LIST_BY_ORGFORMKRZ = "SELECT e FROM DTOUvKlasse e WHERE e.OrgFormKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachklasse_ID */
	public static final String QUERY_BY_FACHKLASSE_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Fachklasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachklasse_ID */
	public static final String QUERY_LIST_BY_FACHKLASSE_ID = "SELECT e FROM DTOUvKlasse e WHERE e.Fachklasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDSchulformNr */
	public static final String QUERY_BY_ASDSCHULFORMNR = "SELECT e FROM DTOUvKlasse e WHERE e.ASDSchulformNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDSchulformNr */
	public static final String QUERY_LIST_BY_ASDSCHULFORMNR = "SELECT e FROM DTOUvKlasse e WHERE e.ASDSchulformNr IN ?1";

	/** ID der Klasse (generiert, planungsspezifisch) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public long Schuljahresabschnitts_ID;

	/** Bezeichnender Text für die Klasse */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Kürzel der Klasse */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Parallelitaet der Klasse (a/b/c/...) */
	@Column(name = "Parallelitaet")
	@JsonProperty
	public String Parallelitaet;

	/** Fremdschlüssel auf die Stundentafel (Tabelle UV_Stundentafeln) */
	@Column(name = "Stundentafel_ID")
	@JsonProperty
	public Long Stundentafel_ID;

	/** Fremdschlüssel auf die Schülergruppe (Tabelle UV_Schuelergruppen) */
	@Column(name = "Schuelergruppe_ID")
	@JsonProperty
	public long Schuelergruppe_ID;

	/** Organisationsform der Klasse Kürzel IT.NRW */
	@Column(name = "OrgFormKrz")
	@JsonProperty
	public String OrgFormKrz;

	/** FID des Fachklasse nur BK SBK */
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Schulgliederung in der Klasse */
	@Column(name = "ASDSchulformNr")
	@JsonProperty
	public String ASDSchulformNr;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvKlasse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvKlasse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvKlasse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Parallelitaet   der Wert für das Attribut Parallelitaet
	 * @param Schuelergruppe_ID   der Wert für das Attribut Schuelergruppe_ID
	 */
	public DTOUvKlasse(final long ID, final long Planungsabschnitt_ID, final long Schuljahresabschnitts_ID, final String Kuerzel, final String Parallelitaet, final long Schuelergruppe_ID) {
		this.ID = ID;
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Parallelitaet == null) {
			throw new NullPointerException("Parallelitaet must not be null");
		}
		this.Parallelitaet = Parallelitaet;
		this.Schuelergruppe_ID = Schuelergruppe_ID;
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
		DTOUvKlasse other = (DTOUvKlasse) obj;
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
		return "DTOUvKlasse(ID=" + this.ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Bezeichnung=" + this.Bezeichnung + ", Kuerzel=" + this.Kuerzel + ", Parallelitaet=" + this.Parallelitaet + ", Stundentafel_ID=" + this.Stundentafel_ID + ", Schuelergruppe_ID=" + this.Schuelergruppe_ID + ", OrgFormKrz=" + this.OrgFormKrz + ", Fachklasse_ID=" + this.Fachklasse_ID + ", ASDSchulformNr=" + this.ASDSchulformNr + ")";
	}

}
