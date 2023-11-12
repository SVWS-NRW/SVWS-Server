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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerMehrleistung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerMehrleistung")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.all", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.id", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.id.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.lehrer_id", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.lehrer_id.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.abschnitt_id", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.abschnitt_id.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.mehrleistungsgrundkrz", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.MehrleistungsgrundKrz = :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.mehrleistungsgrundkrz.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.MehrleistungsgrundKrz IN :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.mehrleistungstd", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.MehrleistungStd = :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.mehrleistungstd.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.MehrleistungStd IN :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.jahr", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.Jahr = :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.jahr.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.Jahr IN :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.abschnitt", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.Abschnitt = :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.abschnitt.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.Abschnitt IN :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.schulnreigner", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.primaryKeyQuery", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOLehrerMehrleistung.all.migration", query = "SELECT e FROM MigrationDTOLehrerMehrleistung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Lehrer_ID", "Abschnitt_ID", "MehrleistungsgrundKrz", "MehrleistungStd", "Jahr", "Abschnitt", "SchulnrEigner"})
public final class MigrationDTOLehrerMehrleistung {

	/** ID für den Eintrag für die Mehrarbeitsstunden eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED: Lehrer-ID die zu den Mehrarbeitsstunden gehört, in LehrerAbchnittsdaten enthalten */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Mehrarbeitsstunden Kürzel */
	@Column(name = "MehrleistungsgrundKrz")
	@JsonProperty
	public String MehrleistungsgrundKrz;

	/** Anzahl Mehrarbeitsstunden */
	@Column(name = "MehrleistungStd")
	@JsonProperty
	public Double MehrleistungStd;

	/** Schuljahr der Mehrarbeitsstunden */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt der Mehrarbeitsstunden */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerMehrleistung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerMehrleistung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerMehrleistung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param MehrleistungsgrundKrz   der Wert für das Attribut MehrleistungsgrundKrz
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOLehrerMehrleistung(final Long ID, final Long Lehrer_ID, final Long Abschnitt_ID, final String MehrleistungsgrundKrz, final Integer Jahr, final Integer Abschnitt) {
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
		if (MehrleistungsgrundKrz == null) {
			throw new NullPointerException("MehrleistungsgrundKrz must not be null");
		}
		this.MehrleistungsgrundKrz = MehrleistungsgrundKrz;
		if (Jahr == null) {
			throw new NullPointerException("Jahr must not be null");
		}
		this.Jahr = Jahr;
		if (Abschnitt == null) {
			throw new NullPointerException("Abschnitt must not be null");
		}
		this.Abschnitt = Abschnitt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOLehrerMehrleistung other = (MigrationDTOLehrerMehrleistung) obj;
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
		return "MigrationDTOLehrerMehrleistung(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", MehrleistungsgrundKrz=" + this.MehrleistungsgrundKrz + ", MehrleistungStd=" + this.MehrleistungStd + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
