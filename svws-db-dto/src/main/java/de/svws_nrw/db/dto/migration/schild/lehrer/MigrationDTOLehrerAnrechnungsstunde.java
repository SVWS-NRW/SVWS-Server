package de.svws_nrw.db.dto.migration.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerAnrechnung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerAnrechnung")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.all", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.id", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.id.multiple", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.lehrer_id", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.lehrer_id.multiple", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.abschnitt_id", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.abschnitt_id.multiple", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.anrechnungsgrundkrz", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.AnrechnungsgrundKrz = :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.anrechnungsgrundkrz.multiple", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.AnrechnungsgrundKrz IN :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.anrechnungstd", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.AnrechnungStd = :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.anrechnungstd.multiple", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.AnrechnungStd IN :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.jahr", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Jahr = :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.jahr.multiple", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Jahr IN :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.abschnitt", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Abschnitt = :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.abschnitt.multiple", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.Abschnitt IN :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.schulnreigner", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.primaryKeyQuery", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOLehrerAnrechnungsstunde.all.migration", query = "SELECT e FROM MigrationDTOLehrerAnrechnungsstunde e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Lehrer_ID", "Abschnitt_ID", "AnrechnungsgrundKrz", "AnrechnungStd", "Jahr", "Abschnitt", "SchulnrEigner"})
public final class MigrationDTOLehrerAnrechnungsstunde {

	/** ID für den Eintrag für die Anrechnungsstunden */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED: Lehrer-ID für die Anrechnungsstunden, in LehrerAbchnittsdaten enthalten */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Anrechnungsstundentext  für die Anrechnungsstunden */
	@Column(name = "AnrechnungsgrundKrz")
	@JsonProperty
	public String AnrechnungsgrundKrz;

	/** Zahl der Anrechnungsstunden für die Anrechnungsstunden */
	@Column(name = "AnrechnungStd")
	@JsonProperty
	public Double AnrechnungStd;

	/** Schuljahr für die Anrechnungsstunden */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnit für die Anrechnungsstunden */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerAnrechnungsstunde ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerAnrechnungsstunde() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerAnrechnungsstunde ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public MigrationDTOLehrerAnrechnungsstunde(final Long ID, final Long Lehrer_ID, final Long Abschnitt_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Lehrer_ID == null) {
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
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
		MigrationDTOLehrerAnrechnungsstunde other = (MigrationDTOLehrerAnrechnungsstunde) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOLehrerAnrechnungsstunde(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", AnrechnungsgrundKrz=" + this.AnrechnungsgrundKrz + ", AnrechnungStd=" + this.AnrechnungStd + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
