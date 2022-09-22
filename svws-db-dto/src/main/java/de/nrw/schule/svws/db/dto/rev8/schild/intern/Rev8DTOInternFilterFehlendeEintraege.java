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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_FilterFehlendeEintraege.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_FilterFehlendeEintraege")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.all", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.id", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.id.multiple", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.beschreibung", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.beschreibung.multiple", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.feldname", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Feldname = :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.feldname.multiple", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Feldname IN :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.tabellen", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Tabellen = :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.tabellen.multiple", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Tabellen IN :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.sqltext", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.SQLText = :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.sqltext.multiple", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.SQLText IN :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.schulform", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Schulform = :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.schulform.multiple", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Schulform IN :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.feldtyp", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Feldtyp = :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.feldtyp.multiple", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.Feldtyp IN :value")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.primaryKeyQuery", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOInternFilterFehlendeEintraege.all.migration", query="SELECT e FROM Rev8DTOInternFilterFehlendeEintraege e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Beschreibung","Feldname","Tabellen","SQLText","Schulform","Feldtyp"})
public class Rev8DTOInternFilterFehlendeEintraege {

	/** Schildintern Tabelle: ID des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schildintern Tabelle: Beschreibung des Eintrags die bei der Prüfung auf fehlende Einträge genutzt werden */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Schildintern Tabelle: Feldname des zu prüfenden Feldes */
	@Column(name = "Feldname")
	@JsonProperty
	public String Feldname;

	/** Schildintern Tabelle: Tabellenname des zu prüfenden Feldes */
	@Column(name = "Tabellen")
	@JsonProperty
	public String Tabellen;

	/** Schildintern Tabelle: Abfrage die zur Prüfung des Feldes führt. */
	@Column(name = "SQLText")
	@JsonProperty
	public String SQLText;

	/** Schildintern Tabelle: ggf Schulform für bestimmte Schulformen */
	@Column(name = "Schulform")
	@JsonProperty
	public String Schulform;

	/** Schildintern Tabelle: Feldty des zu prüfenden Feldes */
	@Column(name = "Feldtyp")
	@JsonProperty
	public String Feldtyp;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternFilterFehlendeEintraege ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOInternFilterFehlendeEintraege() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternFilterFehlendeEintraege ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOInternFilterFehlendeEintraege(final Long ID) {
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
		Rev8DTOInternFilterFehlendeEintraege other = (Rev8DTOInternFilterFehlendeEintraege) obj;
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
		return "Rev8DTOInternFilterFehlendeEintraege(ID=" + this.ID + ", Beschreibung=" + this.Beschreibung + ", Feldname=" + this.Feldname + ", Tabellen=" + this.Tabellen + ", SQLText=" + this.SQLText + ", Schulform=" + this.Schulform + ", Feldtyp=" + this.Feldtyp + ")";
	}

}