package de.nrw.schule.svws.db.dto.current.schild.intern;

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
@IdClass(DTOInternTextExportPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_TextExport")
@NamedQuery(name="DTOInternTextExport.all", query="SELECT e FROM DTOInternTextExport e")
@NamedQuery(name="DTOInternTextExport.datenartkrz", query="SELECT e FROM DTOInternTextExport e WHERE e.DatenartKrz = :value")
@NamedQuery(name="DTOInternTextExport.datenartkrz.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.DatenartKrz IN :value")
@NamedQuery(name="DTOInternTextExport.feldname", query="SELECT e FROM DTOInternTextExport e WHERE e.Feldname = :value")
@NamedQuery(name="DTOInternTextExport.feldname.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.Feldname IN :value")
@NamedQuery(name="DTOInternTextExport.anzeigetext", query="SELECT e FROM DTOInternTextExport e WHERE e.AnzeigeText = :value")
@NamedQuery(name="DTOInternTextExport.anzeigetext.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.AnzeigeText IN :value")
@NamedQuery(name="DTOInternTextExport.feldtyp", query="SELECT e FROM DTOInternTextExport e WHERE e.Feldtyp = :value")
@NamedQuery(name="DTOInternTextExport.feldtyp.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.Feldtyp IN :value")
@NamedQuery(name="DTOInternTextExport.feldwerte", query="SELECT e FROM DTOInternTextExport e WHERE e.Feldwerte = :value")
@NamedQuery(name="DTOInternTextExport.feldwerte.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.Feldwerte IN :value")
@NamedQuery(name="DTOInternTextExport.ergebniswerte", query="SELECT e FROM DTOInternTextExport e WHERE e.ErgebnisWerte = :value")
@NamedQuery(name="DTOInternTextExport.ergebniswerte.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.ErgebnisWerte IN :value")
@NamedQuery(name="DTOInternTextExport.lookupfeldname", query="SELECT e FROM DTOInternTextExport e WHERE e.LookupFeldname = :value")
@NamedQuery(name="DTOInternTextExport.lookupfeldname.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.LookupFeldname IN :value")
@NamedQuery(name="DTOInternTextExport.lookupsqltext", query="SELECT e FROM DTOInternTextExport e WHERE e.LookupSQLText = :value")
@NamedQuery(name="DTOInternTextExport.lookupsqltext.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.LookupSQLText IN :value")
@NamedQuery(name="DTOInternTextExport.dbformat", query="SELECT e FROM DTOInternTextExport e WHERE e.DBFormat = :value")
@NamedQuery(name="DTOInternTextExport.dbformat.multiple", query="SELECT e FROM DTOInternTextExport e WHERE e.DBFormat IN :value")
@NamedQuery(name="DTOInternTextExport.primaryKeyQuery", query="SELECT e FROM DTOInternTextExport e WHERE e.DatenartKrz = ?1 AND e.Feldname = ?2 AND e.AnzeigeText = ?3 AND e.DBFormat = ?4")
@NamedQuery(name="DTOInternTextExport.all.migration", query="SELECT e FROM DTOInternTextExport e WHERE e.DatenartKrz IS NOT NULL AND e.Feldname IS NOT NULL AND e.AnzeigeText IS NOT NULL AND e.DBFormat IS NOT NULL")
@JsonPropertyOrder({"DatenartKrz","Feldname","AnzeigeText","Feldtyp","Feldwerte","ErgebnisWerte","LookupFeldname","LookupSQLText","DBFormat"})
public class DTOInternTextExport {

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
	 * Erstellt ein neues Objekt der Klasse DTOInternTextExport ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternTextExport() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternTextExport ohne eine Initialisierung der Attribute.
	 * @param DatenartKrz   der Wert für das Attribut DatenartKrz
	 * @param Feldname   der Wert für das Attribut Feldname
	 * @param AnzeigeText   der Wert für das Attribut AnzeigeText
	 * @param DBFormat   der Wert für das Attribut DBFormat
	 */
	public DTOInternTextExport(final String DatenartKrz, final String Feldname, final String AnzeigeText, final String DBFormat) {
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
		DTOInternTextExport other = (DTOInternTextExport) obj;
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
		return "DTOInternTextExport(DatenartKrz=" + this.DatenartKrz + ", Feldname=" + this.Feldname + ", AnzeigeText=" + this.AnzeigeText + ", Feldtyp=" + this.Feldtyp + ", Feldwerte=" + this.Feldwerte + ", ErgebnisWerte=" + this.ErgebnisWerte + ", LookupFeldname=" + this.LookupFeldname + ", LookupSQLText=" + this.LookupSQLText + ", DBFormat=" + this.DBFormat + ")";
	}

}