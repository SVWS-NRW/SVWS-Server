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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_FilterFeldListe.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_FilterFeldListe")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.all", query="SELECT e FROM Rev8DTOInternFilterFeldListe e")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.id", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.id.multiple", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.bezeichnung", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.bezeichnung.multiple", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.dbfeld", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.DBFeld = :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.dbfeld.multiple", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.DBFeld IN :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.typ", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Typ = :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.typ.multiple", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Typ IN :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.werte", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Werte = :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.werte.multiple", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Werte IN :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.stdwert", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.StdWert = :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.stdwert.multiple", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.StdWert IN :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.operator", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Operator = :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.operator.multiple", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Operator IN :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.zusatzbedingung", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Zusatzbedingung = :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.zusatzbedingung.multiple", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.Zusatzbedingung IN :value")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.primaryKeyQuery", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOInternFilterFeldListe.all.migration", query="SELECT e FROM Rev8DTOInternFilterFeldListe e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","DBFeld","Typ","Werte","StdWert","Operator","Zusatzbedingung"})
public class Rev8DTOInternFilterFeldListe {

	/** Schildintern Tabelle: ID für den Eintrag welche Felder iim Attributsfilter zur Verfügung stehen. */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schildintern Tabelle: Bezeichnung im Attributsfilter */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Schildintern Tabelle: Datenbankfeld im Attributsfilter */
	@Column(name = "DBFeld")
	@JsonProperty
	public String DBFeld;

	/** Schildintern Tabelle: Typ des Feldes im Attributsfilter */
	@Column(name = "Typ")
	@JsonProperty
	public String Typ;

	/** Schildintern Tabelle: mögliche Werte des Feldes im Attributsfilter */
	@Column(name = "Werte")
	@JsonProperty
	public String Werte;

	/** Schildintern Tabelle: Standardwert im Attributsfilter */
	@Column(name = "StdWert")
	@JsonProperty
	public String StdWert;

	/** Schildintern Tabelle: Operator  im Attributsfilter (größer-kleiner) */
	@Column(name = "Operator")
	@JsonProperty
	public String Operator;

	/** Schildintern Tabelle: Zusatzbedingung im Attributsfilter */
	@Column(name = "Zusatzbedingung")
	@JsonProperty
	public String Zusatzbedingung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternFilterFeldListe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOInternFilterFeldListe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternFilterFeldListe ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOInternFilterFeldListe(final Long ID) {
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
		Rev8DTOInternFilterFeldListe other = (Rev8DTOInternFilterFeldListe) obj;
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
		return "Rev8DTOInternFilterFeldListe(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", DBFeld=" + this.DBFeld + ", Typ=" + this.Typ + ", Werte=" + this.Werte + ", StdWert=" + this.StdWert + ", Operator=" + this.Operator + ", Zusatzbedingung=" + this.Zusatzbedingung + ")";
	}

}