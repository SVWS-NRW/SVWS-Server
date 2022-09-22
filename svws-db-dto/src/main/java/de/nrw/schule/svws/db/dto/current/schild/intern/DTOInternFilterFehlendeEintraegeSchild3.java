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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_FilterFehlendeEintraegeSchild3.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_FilterFehlendeEintraegeSchild3")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.all", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.id", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.ID = :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.id.multiple", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.ID IN :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.beschreibung", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.beschreibung.multiple", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.feldname", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Feldname = :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.feldname.multiple", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Feldname IN :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.tabellen", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Tabellen = :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.tabellen.multiple", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Tabellen IN :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.sqltext", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.SQLText = :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.sqltext.multiple", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.SQLText IN :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.schulform", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Schulform = :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.schulform.multiple", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Schulform IN :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.feldtyp", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Feldtyp = :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.feldtyp.multiple", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.Feldtyp IN :value")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.primaryKeyQuery", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.ID = ?1")
@NamedQuery(name="DTOInternFilterFehlendeEintraegeSchild3.all.migration", query="SELECT e FROM DTOInternFilterFehlendeEintraegeSchild3 e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Beschreibung","Feldname","Tabellen","SQLText","Schulform","Feldtyp"})
public class DTOInternFilterFehlendeEintraegeSchild3 {

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
	 * Erstellt ein neues Objekt der Klasse DTOInternFilterFehlendeEintraegeSchild3 ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternFilterFehlendeEintraegeSchild3() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternFilterFehlendeEintraegeSchild3 ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOInternFilterFehlendeEintraegeSchild3(final Long ID) {
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
		DTOInternFilterFehlendeEintraegeSchild3 other = (DTOInternFilterFehlendeEintraegeSchild3) obj;
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
		return "DTOInternFilterFehlendeEintraegeSchild3(ID=" + this.ID + ", Beschreibung=" + this.Beschreibung + ", Feldname=" + this.Feldname + ", Tabellen=" + this.Tabellen + ", SQLText=" + this.SQLText + ", Schulform=" + this.Schulform + ", Feldtyp=" + this.Feldtyp + ")";
	}

}