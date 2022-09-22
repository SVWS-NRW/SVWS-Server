package de.nrw.schule.svws.db.dto.migration.schild.lehrer;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.all", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.id", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.id.multiple", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.lehrer_id", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.lehrer_id.multiple", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.abschnitt_id", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.abschnitt_id.multiple", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.entlastungsgrundkrz", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.EntlastungsgrundKrz = :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.entlastungsgrundkrz.multiple", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.EntlastungsgrundKrz IN :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.entlastungstd", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.EntlastungStd = :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.entlastungstd.multiple", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.EntlastungStd IN :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.jahr", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.Jahr = :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.jahr.multiple", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.Jahr IN :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.abschnitt", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.Abschnitt = :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.abschnitt.multiple", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.Abschnitt IN :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.schulnreigner", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.schulnreigner.multiple", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.primaryKeyQuery", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOLehrerEntlastungsstunde.all.migration", query="SELECT e FROM MigrationDTOLehrerEntlastungsstunde e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Lehrer_ID","Abschnitt_ID","EntlastungsgrundKrz","EntlastungStd","Jahr","Abschnitt","SchulnrEigner"})
public class MigrationDTOLehrerEntlastungsstunde {

	/** ID für den Eintrag für die Entlastungsstunden (Mehr-Minderleistung) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED: Lehrer-ID für die Entlastungsstunden (Mehr-Minderleistung), in LehrerAbchnittsdaten enthalten */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Kürzel für die Entlastungsstunden (Minderleistung) */
	@Column(name = "EntlastungsgrundKrz")
	@JsonProperty
	public String EntlastungsgrundKrz;

	/** Anzahl für die Entlastungsstunden (Minderleistung) */
	@Column(name = "EntlastungStd")
	@JsonProperty
	public Double EntlastungStd;

	/** Schuljahr für die Entlastungsstunden (Minderleistung) */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt für die Entlastungsstunden (Minderleistung) */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerEntlastungsstunde ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerEntlastungsstunde() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerEntlastungsstunde ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public MigrationDTOLehrerEntlastungsstunde(final Long ID, final Long Lehrer_ID, final Long Abschnitt_ID) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOLehrerEntlastungsstunde other = (MigrationDTOLehrerEntlastungsstunde) obj;
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
		return "MigrationDTOLehrerEntlastungsstunde(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", EntlastungsgrundKrz=" + this.EntlastungsgrundKrz + ", EntlastungStd=" + this.EntlastungStd + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}