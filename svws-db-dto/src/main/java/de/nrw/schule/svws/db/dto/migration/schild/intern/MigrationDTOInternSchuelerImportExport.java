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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_SchuelerImpExp.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_SchuelerImpExp")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.all", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.tabelle", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.Tabelle = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.tabelle.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.Tabelle IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.tabellenanzeige", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.TabellenAnzeige = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.tabellenanzeige.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.TabellenAnzeige IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.mastertable", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.MasterTable = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.mastertable.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.MasterTable IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.expcmd", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.ExpCmd = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.expcmd.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.ExpCmd IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.srcgetfieldssql", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.SrcGetFieldsSQL = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.srcgetfieldssql.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.SrcGetFieldsSQL IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.deletesql", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.DeleteSQL = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.deletesql.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.DeleteSQL IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.dstgetidsql", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.DstGetIDSQL = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.dstgetidsql.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.DstGetIDSQL IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.hauptfeld", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.HauptFeld = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.hauptfeld.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.HauptFeld IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.detailfeld", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.DetailFeld = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.detailfeld.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.DetailFeld IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.reihenfolge", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.Reihenfolge = :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.reihenfolge.multiple", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.Reihenfolge IN :value")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.Tabelle = ?1")
@NamedQuery(name="MigrationDTOInternSchuelerImportExport.all.migration", query="SELECT e FROM MigrationDTOInternSchuelerImportExport e WHERE e.Tabelle IS NOT NULL")
@JsonPropertyOrder({"Tabelle","TabellenAnzeige","MasterTable","ExpCmd","SrcGetFieldsSQL","DeleteSQL","DstGetIDSQL","HauptFeld","DetailFeld","Reihenfolge"})
public class MigrationDTOInternSchuelerImportExport {

	/** Schildintern Tabelle: Tabelle für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Id
	@Column(name = "Tabelle")
	@JsonProperty
	public String Tabelle;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "TabellenAnzeige")
	@JsonProperty
	public String TabellenAnzeige;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "MasterTable")
	@JsonProperty
	public String MasterTable;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "ExpCmd")
	@JsonProperty
	public String ExpCmd;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "SrcGetFieldsSQL")
	@JsonProperty
	public String SrcGetFieldsSQL;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "DeleteSQL")
	@JsonProperty
	public String DeleteSQL;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "DstGetIDSQL")
	@JsonProperty
	public String DstGetIDSQL;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "HauptFeld")
	@JsonProperty
	public String HauptFeld;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "DetailFeld")
	@JsonProperty
	public String DetailFeld;

	/** Schildintern Tabelle: Feld für den Datenaustausch Kataloge/Schülerdaten exportieren */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public Integer Reihenfolge;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternSchuelerImportExport ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternSchuelerImportExport() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternSchuelerImportExport ohne eine Initialisierung der Attribute.
	 * @param Tabelle   der Wert für das Attribut Tabelle
	 */
	public MigrationDTOInternSchuelerImportExport(final String Tabelle) {
		if (Tabelle == null) { 
			throw new NullPointerException("Tabelle must not be null");
		}
		this.Tabelle = Tabelle;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternSchuelerImportExport other = (MigrationDTOInternSchuelerImportExport) obj;
		if (Tabelle == null) {
			if (other.Tabelle != null)
				return false;
		} else if (!Tabelle.equals(other.Tabelle))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Tabelle == null) ? 0 : Tabelle.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOInternSchuelerImportExport(Tabelle=" + this.Tabelle + ", TabellenAnzeige=" + this.TabellenAnzeige + ", MasterTable=" + this.MasterTable + ", ExpCmd=" + this.ExpCmd + ", SrcGetFieldsSQL=" + this.SrcGetFieldsSQL + ", DeleteSQL=" + this.DeleteSQL + ", DstGetIDSQL=" + this.DstGetIDSQL + ", HauptFeld=" + this.HauptFeld + ", DetailFeld=" + this.DetailFeld + ", Reihenfolge=" + this.Reihenfolge + ")";
	}

}