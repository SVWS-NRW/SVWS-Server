package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerEntlastung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerEntlastung")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "EntlastungsgrundKrz", "EntlastungStd"})
public final class DTOLehrerEntlastungsstunde {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerEntlastungsstunde e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EntlastungsgrundKrz */
	public static final String QUERY_BY_ENTLASTUNGSGRUNDKRZ = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.EntlastungsgrundKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EntlastungsgrundKrz */
	public static final String QUERY_LIST_BY_ENTLASTUNGSGRUNDKRZ = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.EntlastungsgrundKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EntlastungStd */
	public static final String QUERY_BY_ENTLASTUNGSTD = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.EntlastungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EntlastungStd */
	public static final String QUERY_LIST_BY_ENTLASTUNGSTD = "SELECT e FROM DTOLehrerEntlastungsstunde e WHERE e.EntlastungStd IN ?1";

	/** ID für den Eintrag für die Entlastungsstunden (Mehr-Minderleistung) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** Kürzel für die Entlastungsstunden (Minderleistung) */
	@Column(name = "EntlastungsgrundKrz")
	@JsonProperty
	public String EntlastungsgrundKrz;

	/** Anzahl für die Entlastungsstunden (Minderleistung) */
	@Column(name = "EntlastungStd")
	@JsonProperty
	public Double EntlastungStd;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerEntlastungsstunde ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerEntlastungsstunde() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerEntlastungsstunde ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public DTOLehrerEntlastungsstunde(final long ID, final long Abschnitt_ID) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerEntlastungsstunde other = (DTOLehrerEntlastungsstunde) obj;
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
		return "DTOLehrerEntlastungsstunde(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", EntlastungsgrundKrz=" + this.EntlastungsgrundKrz + ", EntlastungStd=" + this.EntlastungStd + ")";
	}

}
