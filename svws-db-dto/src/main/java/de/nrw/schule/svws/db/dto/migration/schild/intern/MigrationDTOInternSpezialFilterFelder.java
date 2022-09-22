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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_SpezialFilterFelder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_SpezialFilterFelder")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.all", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.id", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.id.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.gruppe", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Gruppe = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.gruppe.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Gruppe IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.kurzbez", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.KurzBez = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.kurzbez.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.KurzBez IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.bezeichnung", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.bezeichnung.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.grundschule", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Grundschule = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.grundschule.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Grundschule IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.tabelle", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Tabelle = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.tabelle.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Tabelle IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.dbfeld", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.DBFeld = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.dbfeld.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.DBFeld IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.typ", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Typ = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.typ.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Typ IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.control", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Control = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.control.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Control IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.werteanzeige", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.WerteAnzeige = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.werteanzeige.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.WerteAnzeige IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.wertesql", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.WerteSQL = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.wertesql.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.WerteSQL IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.lookupinfo", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.LookupInfo = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.lookupinfo.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.LookupInfo IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.operatorenanzeige", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.OperatorenAnzeige = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.operatorenanzeige.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.OperatorenAnzeige IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.operatorensql", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.OperatorenSQL = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.operatorensql.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.OperatorenSQL IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.zusatzbedingung", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Zusatzbedingung = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.zusatzbedingung.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.Zusatzbedingung IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.zusatztabellen", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.ZusatzTabellen = :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.zusatztabellen.multiple", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.ZusatzTabellen IN :value")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOInternSpezialFilterFelder.all.migration", query="SELECT e FROM MigrationDTOInternSpezialFilterFelder e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Gruppe","KurzBez","Bezeichnung","Grundschule","Tabelle","DBFeld","Typ","Control","WerteAnzeige","WerteSQL","LookupInfo","OperatorenAnzeige","OperatorenSQL","Zusatzbedingung","ZusatzTabellen"})
public class MigrationDTOInternSpezialFilterFelder {

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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternSpezialFilterFelder ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternSpezialFilterFelder() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternSpezialFilterFelder ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOInternSpezialFilterFelder(final Long ID) {
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
		MigrationDTOInternSpezialFilterFelder other = (MigrationDTOInternSpezialFilterFelder) obj;
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
		return "MigrationDTOInternSpezialFilterFelder(ID=" + this.ID + ", Gruppe=" + this.Gruppe + ", KurzBez=" + this.KurzBez + ", Bezeichnung=" + this.Bezeichnung + ", Grundschule=" + this.Grundschule + ", Tabelle=" + this.Tabelle + ", DBFeld=" + this.DBFeld + ", Typ=" + this.Typ + ", Control=" + this.Control + ", WerteAnzeige=" + this.WerteAnzeige + ", WerteSQL=" + this.WerteSQL + ", LookupInfo=" + this.LookupInfo + ", OperatorenAnzeige=" + this.OperatorenAnzeige + ", OperatorenSQL=" + this.OperatorenSQL + ", Zusatzbedingung=" + this.Zusatzbedingung + ", ZusatzTabellen=" + this.ZusatzTabellen + ")";
	}

}