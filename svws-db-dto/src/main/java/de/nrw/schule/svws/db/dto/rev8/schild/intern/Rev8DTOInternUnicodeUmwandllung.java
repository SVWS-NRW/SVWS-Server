package de.nrw.schule.svws.db.dto.rev8.schild.intern;

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
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.all", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.id", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.id.multiple", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.unicodezeichen", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.Unicodezeichen = :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.unicodezeichen.multiple", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.Unicodezeichen IN :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.ersatzzeichen", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.Ersatzzeichen = :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.ersatzzeichen.multiple", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.Ersatzzeichen IN :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.decimalzeichen", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.DecimalZeichen = :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.decimalzeichen.multiple", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.DecimalZeichen IN :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.decimalersatzzeichen", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.DecimalErsatzzeichen = :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.decimalersatzzeichen.multiple", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.DecimalErsatzzeichen IN :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.hexzeichen", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.Hexzeichen = :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.hexzeichen.multiple", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.Hexzeichen IN :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.hexersatzzeichen", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.HexErsatzzeichen = :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.hexersatzzeichen.multiple", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.HexErsatzzeichen IN :value")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.primaryKeyQuery", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOInternUnicodeUmwandllung.all.migration", query="SELECT e FROM Rev8DTOInternUnicodeUmwandllung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Unicodezeichen","Ersatzzeichen","DecimalZeichen","DecimalErsatzzeichen","Hexzeichen","HexErsatzzeichen"})
public class Rev8DTOInternUnicodeUmwandllung {

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternUnicodeUmwandllung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOInternUnicodeUmwandllung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternUnicodeUmwandllung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOInternUnicodeUmwandllung(final Long ID) {
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
		Rev8DTOInternUnicodeUmwandllung other = (Rev8DTOInternUnicodeUmwandllung) obj;
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
		return "Rev8DTOInternUnicodeUmwandllung(ID=" + this.ID + ", Unicodezeichen=" + this.Unicodezeichen + ", Ersatzzeichen=" + this.Ersatzzeichen + ", DecimalZeichen=" + this.DecimalZeichen + ", DecimalErsatzzeichen=" + this.DecimalErsatzzeichen + ", Hexzeichen=" + this.Hexzeichen + ", HexErsatzzeichen=" + this.HexErsatzzeichen + ")";
	}

}