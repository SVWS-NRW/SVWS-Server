package de.nrw.schule.svws.db.dto.current.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_SpezialFilterFelder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_SpezialFilterFelder")
@NamedQuery(name="DTOInternSpezialFilterFelder.all", query="SELECT e FROM DTOInternSpezialFilterFelder e")
@NamedQuery(name="DTOInternSpezialFilterFelder.id", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.ID = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.id.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.ID IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.gruppe", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Gruppe = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.gruppe.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Gruppe IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.kurzbez", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.KurzBez = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.kurzbez.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.KurzBez IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.bezeichnung", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.bezeichnung.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.grundschule", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Grundschule = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.grundschule.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Grundschule IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.tabelle", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Tabelle = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.tabelle.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Tabelle IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.dbfeld", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.DBFeld = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.dbfeld.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.DBFeld IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.typ", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Typ = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.typ.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Typ IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.control", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Control = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.control.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Control IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.werteanzeige", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.WerteAnzeige = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.werteanzeige.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.WerteAnzeige IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.wertesql", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.WerteSQL = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.wertesql.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.WerteSQL IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.lookupinfo", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.LookupInfo = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.lookupinfo.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.LookupInfo IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.operatorenanzeige", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.OperatorenAnzeige = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.operatorenanzeige.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.OperatorenAnzeige IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.operatorensql", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.OperatorenSQL = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.operatorensql.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.OperatorenSQL IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.zusatzbedingung", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Zusatzbedingung = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.zusatzbedingung.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.Zusatzbedingung IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.zusatztabellen", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.ZusatzTabellen = :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.zusatztabellen.multiple", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.ZusatzTabellen IN :value")
@NamedQuery(name="DTOInternSpezialFilterFelder.primaryKeyQuery", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.ID = ?1")
@NamedQuery(name="DTOInternSpezialFilterFelder.all.migration", query="SELECT e FROM DTOInternSpezialFilterFelder e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Gruppe","KurzBez","Bezeichnung","Grundschule","Tabelle","DBFeld","Typ","Control","WerteAnzeige","WerteSQL","LookupInfo","OperatorenAnzeige","OperatorenSQL","Zusatzbedingung","ZusatzTabellen"})
public class DTOInternSpezialFilterFelder {

	/** Schildintern Tabelle: ID der Felder die im Filter II zur Verfügung stehen. */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schildintern Tabelle: Gruppe in Filter II */
	@Column(name = "Gruppe")
	@JsonProperty
	public String Gruppe;

	/** Schildintern Tabelle: Kurzbezeichnung in Filter II */
	@Column(name = "KurzBez")
	@JsonProperty
	public String KurzBez;

	/** Schildintern Tabelle: Bezeichnung in Filter II */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "Grundschule")
	@JsonProperty
	public String Grundschule;

	/** Schildintern Tabelle: Tabelle für Filter II */
	@Column(name = "Tabelle")
	@JsonProperty
	public String Tabelle;

	/** Schildintern Tabelle: Datenbankfeld für Filter II */
	@Column(name = "DBFeld")
	@JsonProperty
	public String DBFeld;

	/** Schildintern Tabelle: Feldty für Filter II */
	@Column(name = "Typ")
	@JsonProperty
	public String Typ;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "Control")
	@JsonProperty
	public String Control;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "WerteAnzeige")
	@JsonProperty
	public String WerteAnzeige;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "WerteSQL")
	@JsonProperty
	public String WerteSQL;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "LookupInfo")
	@JsonProperty
	public String LookupInfo;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "OperatorenAnzeige")
	@JsonProperty
	public String OperatorenAnzeige;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "OperatorenSQL")
	@JsonProperty
	public String OperatorenSQL;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "Zusatzbedingung")
	@JsonProperty
	public String Zusatzbedingung;

	/** Schildintern Tabelle: Filter II */
	@Column(name = "ZusatzTabellen")
	@JsonProperty
	public String ZusatzTabellen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternSpezialFilterFelder ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternSpezialFilterFelder() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternSpezialFilterFelder ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOInternSpezialFilterFelder(final Long ID) {
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
		DTOInternSpezialFilterFelder other = (DTOInternSpezialFilterFelder) obj;
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
		return "DTOInternSpezialFilterFelder(ID=" + this.ID + ", Gruppe=" + this.Gruppe + ", KurzBez=" + this.KurzBez + ", Bezeichnung=" + this.Bezeichnung + ", Grundschule=" + this.Grundschule + ", Tabelle=" + this.Tabelle + ", DBFeld=" + this.DBFeld + ", Typ=" + this.Typ + ", Control=" + this.Control + ", WerteAnzeige=" + this.WerteAnzeige + ", WerteSQL=" + this.WerteSQL + ", LookupInfo=" + this.LookupInfo + ", OperatorenAnzeige=" + this.OperatorenAnzeige + ", OperatorenSQL=" + this.OperatorenSQL + ", Zusatzbedingung=" + this.Zusatzbedingung + ", ZusatzTabellen=" + this.ZusatzTabellen + ")";
	}

}