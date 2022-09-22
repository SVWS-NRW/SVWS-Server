package de.nrw.schule.svws.db.dto.rev8.schild;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle ZuordnungReportvorlagen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ZuordnungReportvorlagen")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.all", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.id", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.id.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.jahrgang_id", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.jahrgang_id.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.abschluss", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Abschluss = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.abschluss.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Abschluss IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.abschlussbb", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.AbschlussBB = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.abschlussbb.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.AbschlussBB IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.abschlussart", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.AbschlussArt = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.abschlussart.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.AbschlussArt IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.versetzungkrz", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.VersetzungKrz = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.versetzungkrz.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.VersetzungKrz IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.fachklasse_id", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.fachklasse_id.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.reportvorlage", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Reportvorlage = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.reportvorlage.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Reportvorlage IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.beschreibung", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.beschreibung.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.gruppe", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Gruppe = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.gruppe.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Gruppe IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.zeugnisart", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Zeugnisart = :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.zeugnisart.multiple", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.Zeugnisart IN :value")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.primaryKeyQuery", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOZuordnungReportvorlagen.all.migration", query="SELECT e FROM Rev8DTOZuordnungReportvorlagen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Jahrgang_ID","Abschluss","AbschlussBB","AbschlussArt","VersetzungKrz","Fachklasse_ID","Reportvorlage","Beschreibung","Gruppe","Zeugnisart"})
public class Rev8DTOZuordnungReportvorlagen {

	/** ID des Datensatzes der einen Zeugnisreport einer Gruppe oder Klasse zuordnet */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Jahrgangs der zum Report zugeordnet wird */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** Bezeichnung des Abschluss der für den Report zugeordnet wird */
	@Column(name = "Abschluss")
	@JsonProperty
	public String Abschluss;

	/** Bezeichnung des berufsbezogenen Abschluss der für den Report zugeordnet wird */
	@Column(name = "AbschlussBB")
	@JsonProperty
	public String AbschlussBB;

	/** Art des Abschluss der für den Report zugeordnet wird */
	@Column(name = "AbschlussArt")
	@JsonProperty
	public Integer AbschlussArt;

	/** Kürzel des Versetzungsvermerk das für den Report zugeordnet wird */
	@Column(name = "VersetzungKrz")
	@JsonProperty
	public String VersetzungKrz;

	/** ID der Fachklasse die für den Report zugeordnet wird */
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Pfad zur Reportvorlage die für das Zeugnis zugeordnet wird */
	@Column(name = "Reportvorlage")
	@JsonProperty
	public String Reportvorlage;

	/** Beschreibung für die Reportzuordnung zum Zeugnisdruck */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Name der Gruppe die für den Report zugeordnet wird */
	@Column(name = "Gruppe")
	@JsonProperty
	public String Gruppe;

	/** Zeugnisart (Laufbahndaten) die für den Report zugeordnet wird */
	@Column(name = "Zeugnisart")
	@JsonProperty
	public String Zeugnisart;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOZuordnungReportvorlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOZuordnungReportvorlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOZuordnungReportvorlagen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOZuordnungReportvorlagen(final Long ID) {
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
		Rev8DTOZuordnungReportvorlagen other = (Rev8DTOZuordnungReportvorlagen) obj;
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
		return "Rev8DTOZuordnungReportvorlagen(ID=" + this.ID + ", Jahrgang_ID=" + this.Jahrgang_ID + ", Abschluss=" + this.Abschluss + ", AbschlussBB=" + this.AbschlussBB + ", AbschlussArt=" + this.AbschlussArt + ", VersetzungKrz=" + this.VersetzungKrz + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Reportvorlage=" + this.Reportvorlage + ", Beschreibung=" + this.Beschreibung + ", Gruppe=" + this.Gruppe + ", Zeugnisart=" + this.Zeugnisart + ")";
	}

}