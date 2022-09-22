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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_SchuelerErsteSchulformSekI.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_SchuelerErsteSchulformSekI")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.all", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.id", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.id.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.sf", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.SF = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.sf.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.SF IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.kurztext", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Kurztext = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.kurztext.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Kurztext IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.langtext", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Langtext = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.langtext.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Langtext IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.beginn", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Beginn = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.beginn.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Beginn IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.ende", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Ende = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.ende.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Ende IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.sort", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Sort = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.sort.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.Sort IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.geaendert", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.geaendert = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.geaendert.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.geaendert IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.gueltigvon", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.gueltigvon.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.gueltigbis", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.gueltigbis.multiple", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.primaryKeyQuery", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOStatkueSchuelerErsteSchulformSekI.all.migration", query="SELECT e FROM MigrationDTOStatkueSchuelerErsteSchulformSekI e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","SF","Kurztext","Langtext","Beginn","Ende","Sort","geaendert","gueltigVon","gueltigBis"})
public class MigrationDTOStatkueSchuelerErsteSchulformSekI {

	/** Statkue Tabelle IT.NRW: ID der ersten Schulform Sek1 */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Statkue Tabelle IT.NRW: zulässige Schulform der ersten Schulform Sek1 */
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Statkue Tabelle IT.NRW: Statsistikschlüssel der ersten Schulform Sek1 */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Statkue Tabelle IT.NRW: Langtext der ersten Schulform Sek1 */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/** Statkue Tabelle IT.NRW: Beginn der Gültigkeit der ersten Schulform Sek1 */
	@Column(name = "Beginn")
	@JsonProperty
	public String Beginn;

	/** Statkue Tabelle IT.NRW: Ende der Gültigkeit der ersten Schulform Sek1 */
	@Column(name = "Ende")
	@JsonProperty
	public String Ende;

	/** Statkue Tabelle IT.NRW: Sortierung der ersten Schulform Sek1 */
	@Column(name = "Sort")
	@JsonProperty
	public Integer Sort;

	/** Statkue Tabelle IT.NRW: Datum der letzten Änderung der ersten Schulform Sek1 */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueSchuelerErsteSchulformSekI ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOStatkueSchuelerErsteSchulformSekI() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueSchuelerErsteSchulformSekI ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kurztext   der Wert für das Attribut Kurztext
	 * @param Langtext   der Wert für das Attribut Langtext
	 * @param Sort   der Wert für das Attribut Sort
	 */
	public MigrationDTOStatkueSchuelerErsteSchulformSekI(final Long ID, final String Kurztext, final String Langtext, final Integer Sort) {
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
		MigrationDTOStatkueSchuelerErsteSchulformSekI other = (MigrationDTOStatkueSchuelerErsteSchulformSekI) obj;
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
		return "MigrationDTOStatkueSchuelerErsteSchulformSekI(ID=" + this.ID + ", SF=" + this.SF + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", Sort=" + this.Sort + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}