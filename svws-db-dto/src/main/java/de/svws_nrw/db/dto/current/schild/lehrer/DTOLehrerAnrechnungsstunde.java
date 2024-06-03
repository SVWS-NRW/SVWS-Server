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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerAnrechnung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerAnrechnung")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "AnrechnungsgrundKrz", "AnrechnungStd"})
public final class DTOLehrerAnrechnungsstunde {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerAnrechnungsstunde e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnrechnungsgrundKrz */
	public static final String QUERY_BY_ANRECHNUNGSGRUNDKRZ = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.AnrechnungsgrundKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnrechnungsgrundKrz */
	public static final String QUERY_LIST_BY_ANRECHNUNGSGRUNDKRZ = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.AnrechnungsgrundKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnrechnungStd */
	public static final String QUERY_BY_ANRECHNUNGSTD = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.AnrechnungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnrechnungStd */
	public static final String QUERY_LIST_BY_ANRECHNUNGSTD = "SELECT e FROM DTOLehrerAnrechnungsstunde e WHERE e.AnrechnungStd IN ?1";

	/** ID für den Eintrag für die Anrechnungsstunden */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** Anrechnungsstundentext  für die Anrechnungsstunden */
	@Column(name = "AnrechnungsgrundKrz")
	@JsonProperty
	public String AnrechnungsgrundKrz;

	/** Zahl der Anrechnungsstunden für die Anrechnungsstunden */
	@Column(name = "AnrechnungStd")
	@JsonProperty
	public Double AnrechnungStd;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerAnrechnungsstunde ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerAnrechnungsstunde() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerAnrechnungsstunde ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public DTOLehrerAnrechnungsstunde(final long ID, final long Abschnitt_ID) {
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
		DTOLehrerAnrechnungsstunde other = (DTOLehrerAnrechnungsstunde) obj;
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
		return "DTOLehrerAnrechnungsstunde(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", AnrechnungsgrundKrz=" + this.AnrechnungsgrundKrz + ", AnrechnungStd=" + this.AnrechnungStd + ")";
	}

}
