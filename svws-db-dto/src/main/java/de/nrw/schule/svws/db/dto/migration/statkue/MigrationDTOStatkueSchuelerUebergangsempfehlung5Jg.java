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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_SchuelerUebergangsempfehlung5Jg.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_SchuelerUebergangsempfehlung5Jg")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.all", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.id", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.id.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.sf", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.SF = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.sf.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.SF IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.kurztext", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Kurztext = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.kurztext.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Kurztext IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.langtext", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Langtext = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.langtext.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Langtext IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.beginn", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Beginn = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.beginn.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Beginn IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.ende", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Ende = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.ende.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Ende IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.sort", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Sort = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.sort.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.Sort IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.hgsem", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.HGSEM = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.hgsem.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.HGSEM IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.gueltigvon", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.gueltigvon.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.gueltigbis", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.gueltigbis.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.primaryKeyQuery", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg.all.migration", query="SELECT e FROM MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","SF","Kurztext","Langtext","Beginn","Ende","Sort","HGSEM","gueltigVon","gueltigBis"})
public class MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg {

	/** Statkue Tabelle IT.NRW: ID der Übergangsempfehlung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Statkue Tabelle IT.NRW: leer */
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Statkue Tabelle IT.NRW: Kurztext der Übergangsempfehlung */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Statkue Tabelle IT.NRW: Langtext der Übergangsempfehlung */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/** Statkue Tabelle IT.NRW: Beginn der Gültigkeit der Übergangsempfehlung */
	@Column(name = "Beginn")
	@JsonProperty
	public String Beginn;

	/** Statkue Tabelle IT.NRW: Ende der Gültigkeit der Übergangsempfehlung */
	@Column(name = "Ende")
	@JsonProperty
	public String Ende;

	/** Statkue Tabelle IT.NRW: Sortierung der Übergangsempfehlung */
	@Column(name = "Sort")
	@JsonProperty
	public Integer Sort;

	/** Statkue Tabelle IT.NRW: Statistikschlüssel der Übergangsempfehlung */
	@Column(name = "HGSEM")
	@JsonProperty
	public String HGSEM;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kurztext   der Wert für das Attribut Kurztext
	 * @param Langtext   der Wert für das Attribut Langtext
	 * @param HGSEM   der Wert für das Attribut HGSEM
	 */
	public MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg(final Long ID, final String Kurztext, final String Langtext, final String HGSEM) {
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
		if (HGSEM == null) { 
			throw new NullPointerException("HGSEM must not be null");
		}
		this.HGSEM = HGSEM;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg other = (MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg) obj;
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
		return "MigrationDTOStatkueSchuelerUebergangsempfehlung5Jg(ID=" + this.ID + ", SF=" + this.SF + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", Sort=" + this.Sort + ", HGSEM=" + this.HGSEM + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}