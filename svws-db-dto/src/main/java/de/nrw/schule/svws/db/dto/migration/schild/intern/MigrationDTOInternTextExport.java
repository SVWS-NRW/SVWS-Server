package de.nrw.schule.svws.db.dto.migration.schild.intern;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_TextExport.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOInternTextExportPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_TextExport")
@NamedQuery(name="MigrationDTOInternTextExport.all", query="SELECT e FROM MigrationDTOInternTextExport e")
@NamedQuery(name="MigrationDTOInternTextExport.datenartkrz", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.DatenartKrz = :value")
@NamedQuery(name="MigrationDTOInternTextExport.datenartkrz.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.DatenartKrz IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.feldname", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.Feldname = :value")
@NamedQuery(name="MigrationDTOInternTextExport.feldname.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.Feldname IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.anzeigetext", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.AnzeigeText = :value")
@NamedQuery(name="MigrationDTOInternTextExport.anzeigetext.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.AnzeigeText IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.feldtyp", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.Feldtyp = :value")
@NamedQuery(name="MigrationDTOInternTextExport.feldtyp.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.Feldtyp IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.feldwerte", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.Feldwerte = :value")
@NamedQuery(name="MigrationDTOInternTextExport.feldwerte.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.Feldwerte IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.ergebniswerte", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.ErgebnisWerte = :value")
@NamedQuery(name="MigrationDTOInternTextExport.ergebniswerte.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.ErgebnisWerte IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.lookupfeldname", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.LookupFeldname = :value")
@NamedQuery(name="MigrationDTOInternTextExport.lookupfeldname.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.LookupFeldname IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.lookupsqltext", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.LookupSQLText = :value")
@NamedQuery(name="MigrationDTOInternTextExport.lookupsqltext.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.LookupSQLText IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.dbformat", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.DBFormat = :value")
@NamedQuery(name="MigrationDTOInternTextExport.dbformat.multiple", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.DBFormat IN :value")
@NamedQuery(name="MigrationDTOInternTextExport.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.DatenartKrz = ?1 AND e.Feldname = ?2 AND e.AnzeigeText = ?3 AND e.DBFormat = ?4")
@NamedQuery(name="MigrationDTOInternTextExport.all.migration", query="SELECT e FROM MigrationDTOInternTextExport e WHERE e.DatenartKrz IS NOT NULL AND e.Feldname IS NOT NULL AND e.AnzeigeText IS NOT NULL AND e.DBFormat IS NOT NULL")
@JsonPropertyOrder({"DatenartKrz","Feldname","AnzeigeText","Feldtyp","Feldwerte","ErgebnisWerte","LookupFeldname","LookupSQLText","DBFormat"})
public class MigrationDTOInternTextExport {

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Id
	@Column(name = "DatenartKrz")
	@JsonProperty
	public String DatenartKrz;

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Id
	@Column(name = "Feldname")
	@JsonProperty
	public String Feldname;

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Id
	@Column(name = "AnzeigeText")
	@JsonProperty
	public String AnzeigeText;

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Column(name = "Feldtyp")
	@JsonProperty
	public String Feldtyp;

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Column(name = "Feldwerte")
	@JsonProperty
	public String Feldwerte;

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Column(name = "ErgebnisWerte")
	@JsonProperty
	public String ErgebnisWerte;

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Column(name = "LookupFeldname")
	@JsonProperty
	public String LookupFeldname;

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Column(name = "LookupSQLText")
	@JsonProperty
	public String LookupSQLText;

	/** Schildintern Tabelle: Excel_CSV_Export */
	@Id
	@Column(name = "DBFormat")
	@JsonProperty
	public String DBFormat;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternTextExport ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternTextExport() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternTextExport ohne eine Initialisierung der Attribute.
	 * @param DatenartKrz   der Wert für das Attribut DatenartKrz
	 * @param Feldname   der Wert für das Attribut Feldname
	 * @param AnzeigeText   der Wert für das Attribut AnzeigeText
	 * @param DBFormat   der Wert für das Attribut DBFormat
	 */
	public MigrationDTOInternTextExport(final String DatenartKrz, final String Feldname, final String AnzeigeText, final String DBFormat) {
		if (DatenartKrz == null) { 
			throw new NullPointerException("DatenartKrz must not be null");
		}
		this.DatenartKrz = DatenartKrz;
		if (Feldname == null) { 
			throw new NullPointerException("Feldname must not be null");
		}
		this.Feldname = Feldname;
		if (AnzeigeText == null) { 
			throw new NullPointerException("AnzeigeText must not be null");
		}
		this.AnzeigeText = AnzeigeText;
		if (DBFormat == null) { 
			throw new NullPointerException("DBFormat must not be null");
		}
		this.DBFormat = DBFormat;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternTextExport other = (MigrationDTOInternTextExport) obj;
		if (DatenartKrz == null) {
			if (other.DatenartKrz != null)
				return false;
		} else if (!DatenartKrz.equals(other.DatenartKrz))
			return false;

		if (Feldname == null) {
			if (other.Feldname != null)
				return false;
		} else if (!Feldname.equals(other.Feldname))
			return false;

		if (AnzeigeText == null) {
			if (other.AnzeigeText != null)
				return false;
		} else if (!AnzeigeText.equals(other.AnzeigeText))
			return false;

		if (DBFormat == null) {
			if (other.DBFormat != null)
				return false;
		} else if (!DBFormat.equals(other.DBFormat))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DatenartKrz == null) ? 0 : DatenartKrz.hashCode());

		result = prime * result + ((Feldname == null) ? 0 : Feldname.hashCode());

		result = prime * result + ((AnzeigeText == null) ? 0 : AnzeigeText.hashCode());

		result = prime * result + ((DBFormat == null) ? 0 : DBFormat.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOInternTextExport(DatenartKrz=" + this.DatenartKrz + ", Feldname=" + this.Feldname + ", AnzeigeText=" + this.AnzeigeText + ", Feldtyp=" + this.Feldtyp + ", Feldwerte=" + this.Feldwerte + ", ErgebnisWerte=" + this.ErgebnisWerte + ", LookupFeldname=" + this.LookupFeldname + ", LookupSQLText=" + this.LookupSQLText + ", DBFormat=" + this.DBFormat + ")";
	}

}