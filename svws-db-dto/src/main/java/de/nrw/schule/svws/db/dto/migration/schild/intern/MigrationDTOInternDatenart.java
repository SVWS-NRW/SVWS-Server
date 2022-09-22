package de.nrw.schule.svws.db.dto.migration.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_Datenart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_Datenart")
@NamedQuery(name="MigrationDTOInternDatenart.all", query="SELECT e FROM MigrationDTOInternDatenart e")
@NamedQuery(name="MigrationDTOInternDatenart.datenartkrz", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.DatenartKrz = :value")
@NamedQuery(name="MigrationDTOInternDatenart.datenartkrz.multiple", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.DatenartKrz IN :value")
@NamedQuery(name="MigrationDTOInternDatenart.datenart", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.Datenart = :value")
@NamedQuery(name="MigrationDTOInternDatenart.datenart.multiple", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.Datenart IN :value")
@NamedQuery(name="MigrationDTOInternDatenart.tabellenname", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.Tabellenname = :value")
@NamedQuery(name="MigrationDTOInternDatenart.tabellenname.multiple", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.Tabellenname IN :value")
@NamedQuery(name="MigrationDTOInternDatenart.reihenfolge", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.Reihenfolge = :value")
@NamedQuery(name="MigrationDTOInternDatenart.reihenfolge.multiple", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.Reihenfolge IN :value")
@NamedQuery(name="MigrationDTOInternDatenart.gueltigvon", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOInternDatenart.gueltigvon.multiple", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOInternDatenart.gueltigbis", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOInternDatenart.gueltigbis.multiple", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOInternDatenart.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.DatenartKrz = ?1")
@NamedQuery(name="MigrationDTOInternDatenart.all.migration", query="SELECT e FROM MigrationDTOInternDatenart e WHERE e.DatenartKrz IS NOT NULL")
@JsonPropertyOrder({"DatenartKrz","Datenart","Tabellenname","Reihenfolge","gueltigVon","gueltigBis"})
public class MigrationDTOInternDatenart {

	/** Schildintern Tabelle:  */
	@Id
	@Column(name = "DatenartKrz")
	@JsonProperty
	public String DatenartKrz;

	/** Schildintern Tabelle:  */
	@Column(name = "Datenart")
	@JsonProperty
	public String Datenart;

	/** Schildintern Tabelle:  */
	@Column(name = "Tabellenname")
	@JsonProperty
	public String Tabellenname;

	/** Schildintern Tabelle:  */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public Integer Reihenfolge;

	/** Schildintern Tabelle: Gültig ab Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Gültig bis Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternDatenart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternDatenart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternDatenart ohne eine Initialisierung der Attribute.
	 * @param DatenartKrz   der Wert für das Attribut DatenartKrz
	 */
	public MigrationDTOInternDatenart(final String DatenartKrz) {
		if (DatenartKrz == null) { 
			throw new NullPointerException("DatenartKrz must not be null");
		}
		this.DatenartKrz = DatenartKrz;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternDatenart other = (MigrationDTOInternDatenart) obj;
		if (DatenartKrz == null) {
			if (other.DatenartKrz != null)
				return false;
		} else if (!DatenartKrz.equals(other.DatenartKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DatenartKrz == null) ? 0 : DatenartKrz.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOInternDatenart(DatenartKrz=" + this.DatenartKrz + ", Datenart=" + this.Datenart + ", Tabellenname=" + this.Tabellenname + ", Reihenfolge=" + this.Reihenfolge + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}