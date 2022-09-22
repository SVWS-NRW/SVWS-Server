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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_UnicodeUmwandlung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_UnicodeUmwandlung")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.all", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.id", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.id.multiple", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.unicodezeichen", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.Unicodezeichen = :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.unicodezeichen.multiple", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.Unicodezeichen IN :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.ersatzzeichen", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.Ersatzzeichen = :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.ersatzzeichen.multiple", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.Ersatzzeichen IN :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.decimalzeichen", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.DecimalZeichen = :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.decimalzeichen.multiple", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.DecimalZeichen IN :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.decimalersatzzeichen", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.DecimalErsatzzeichen = :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.decimalersatzzeichen.multiple", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.DecimalErsatzzeichen IN :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.hexzeichen", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.Hexzeichen = :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.hexzeichen.multiple", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.Hexzeichen IN :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.hexersatzzeichen", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.HexErsatzzeichen = :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.hexersatzzeichen.multiple", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.HexErsatzzeichen IN :value")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOInternUnicodeUmwandllung.all.migration", query="SELECT e FROM MigrationDTOInternUnicodeUmwandllung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Unicodezeichen","Ersatzzeichen","DecimalZeichen","DecimalErsatzzeichen","Hexzeichen","HexErsatzzeichen"})
public class MigrationDTOInternUnicodeUmwandllung {

	/** Schildintern Tabelle: ID für den Primärschlüssel der Tabelle UnicodeUmwandlung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schildintern Tabelle: Unicodezeichen das umgewandelt werden muss */
	@Column(name = "Unicodezeichen")
	@JsonProperty
	public String Unicodezeichen;

	/** Schildintern Tabelle: Erstazzeichen für das Unicodezeichen */
	@Column(name = "Ersatzzeichen")
	@JsonProperty
	public String Ersatzzeichen;

	/** Schildintern Tabelle: Unicodezeichen in Dezimaldarstellung */
	@Column(name = "DecimalZeichen")
	@JsonProperty
	public String DecimalZeichen;

	/** Schildintern Tabelle: Ersatzzeichen in Dezimaldarstellung (bei zwei Zeichen mit + getrennt) */
	@Column(name = "DecimalErsatzzeichen")
	@JsonProperty
	public String DecimalErsatzzeichen;

	/** Schildintern Tabelle: Hexdarstellung des Unicodezeichen das gewandelt werden muss */
	@Column(name = "Hexzeichen")
	@JsonProperty
	public String Hexzeichen;

	/** Schildintern Tabelle: Hexdarstellung des Ersatzzeichens das gewandelt werden muss (bei zwei Zeichen mit + getrennt) */
	@Column(name = "HexErsatzzeichen")
	@JsonProperty
	public String HexErsatzzeichen;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternUnicodeUmwandllung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternUnicodeUmwandllung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternUnicodeUmwandllung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOInternUnicodeUmwandllung(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternUnicodeUmwandllung other = (MigrationDTOInternUnicodeUmwandllung) obj;
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
		return "MigrationDTOInternUnicodeUmwandllung(ID=" + this.ID + ", Unicodezeichen=" + this.Unicodezeichen + ", Ersatzzeichen=" + this.Ersatzzeichen + ", DecimalZeichen=" + this.DecimalZeichen + ", DecimalErsatzzeichen=" + this.DecimalErsatzzeichen + ", Hexzeichen=" + this.Hexzeichen + ", HexErsatzzeichen=" + this.HexErsatzzeichen + ")";
	}

}