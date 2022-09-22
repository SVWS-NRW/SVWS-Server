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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_FilterFeldListe.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_FilterFeldListe")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.all", query="SELECT e FROM MigrationDTOInternFilterFeldListe e")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.id", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.id.multiple", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.bezeichnung", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.bezeichnung.multiple", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.dbfeld", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.DBFeld = :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.dbfeld.multiple", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.DBFeld IN :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.typ", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Typ = :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.typ.multiple", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Typ IN :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.werte", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Werte = :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.werte.multiple", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Werte IN :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.stdwert", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.StdWert = :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.stdwert.multiple", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.StdWert IN :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.operator", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Operator = :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.operator.multiple", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Operator IN :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.zusatzbedingung", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Zusatzbedingung = :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.zusatzbedingung.multiple", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.Zusatzbedingung IN :value")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOInternFilterFeldListe.all.migration", query="SELECT e FROM MigrationDTOInternFilterFeldListe e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","DBFeld","Typ","Werte","StdWert","Operator","Zusatzbedingung"})
public class MigrationDTOInternFilterFeldListe {

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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternFilterFeldListe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternFilterFeldListe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternFilterFeldListe ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOInternFilterFeldListe(final Long ID) {
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
		MigrationDTOInternFilterFeldListe other = (MigrationDTOInternFilterFeldListe) obj;
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
		return "MigrationDTOInternFilterFeldListe(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", DBFeld=" + this.DBFeld + ", Typ=" + this.Typ + ", Werte=" + this.Werte + ", StdWert=" + this.StdWert + ", Operator=" + this.Operator + ", Zusatzbedingung=" + this.Zusatzbedingung + ")";
	}

}