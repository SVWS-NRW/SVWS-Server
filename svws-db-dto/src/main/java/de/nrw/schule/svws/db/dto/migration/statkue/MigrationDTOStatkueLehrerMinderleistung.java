package de.nrw.schule.svws.db.dto.migration.statkue;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_LehrerMinderleistung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_LehrerMinderleistung")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.all", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.id", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.id.multiple", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.kurztext", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Kurztext = :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.kurztext.multiple", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Kurztext IN :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.langtext", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Langtext = :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.langtext.multiple", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Langtext IN :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.beginn", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Beginn = :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.beginn.multiple", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Beginn IN :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.ende", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Ende = :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.ende.multiple", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Ende IN :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.sort", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Sort = :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.sort.multiple", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.Sort IN :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.gueltigvon", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.gueltigvon.multiple", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.gueltigbis", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.gueltigbis.multiple", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.primaryKeyQuery", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOStatkueLehrerMinderleistung.all.migration", query="SELECT e FROM MigrationDTOStatkueLehrerMinderleistung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kurztext","Langtext","Beginn","Ende","Sort","gueltigVon","gueltigBis"})
public class MigrationDTOStatkueLehrerMinderleistung {

	/** Statkue Tabelle IT.NRW: ID der Lehrerminderleistung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Statkue Tabelle IT.NRW: Statistikkürzel der Lehrerminderleistung */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Statkue Tabelle IT.NRW: Langtext der Lehrerminderleistung */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/** Statkue Tabelle IT.NRW: Begin der Gültigkeit der Lehrerminderleistung */
	@Column(name = "Beginn")
	@JsonProperty
	public String Beginn;

	/** Statkue Tabelle IT.NRW: Ende der Gültigkeit der Lehrerminderleistung */
	@Column(name = "Ende")
	@JsonProperty
	public String Ende;

	/** Statkue Tabelle IT.NRW: Sortierung der Lehrerminderleistung */
	@Column(name = "Sort")
	@JsonProperty
	public Integer Sort;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueLehrerMinderleistung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOStatkueLehrerMinderleistung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueLehrerMinderleistung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kurztext   der Wert für das Attribut Kurztext
	 * @param Langtext   der Wert für das Attribut Langtext
	 * @param Sort   der Wert für das Attribut Sort
	 */
	public MigrationDTOStatkueLehrerMinderleistung(final Long ID, final String Kurztext, final String Langtext, final Integer Sort) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kurztext == null) { 
			throw new NullPointerException("Kurztext must not be null");
		}
		this.Kurztext = Kurztext;
		if (Langtext == null) { 
			throw new NullPointerException("Langtext must not be null");
		}
		this.Langtext = Langtext;
		if (Sort == null) { 
			throw new NullPointerException("Sort must not be null");
		}
		this.Sort = Sort;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOStatkueLehrerMinderleistung other = (MigrationDTOStatkueLehrerMinderleistung) obj;
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
		return "MigrationDTOStatkueLehrerMinderleistung(ID=" + this.ID + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", Sort=" + this.Sort + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}