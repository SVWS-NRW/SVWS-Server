package de.svws_nrw.db.dto.migration.schild;

import de.svws_nrw.db.DBEntityManager;

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
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.all", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.id", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.id.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.schulnreigner", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.schulnreigner.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.jahrgang_id", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.jahrgang_id.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.abschluss", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Abschluss = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.abschluss.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Abschluss IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.abschlussbb", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.AbschlussBB = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.abschlussbb.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.AbschlussBB IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.abschlussart", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.AbschlussArt = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.abschlussart.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.AbschlussArt IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.versetzungkrz", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.VersetzungKrz = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.versetzungkrz.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.VersetzungKrz IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.fachklasse_id", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.fachklasse_id.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.reportvorlage", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Reportvorlage = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.reportvorlage.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Reportvorlage IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.beschreibung", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Beschreibung = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.beschreibung.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Beschreibung IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.gruppe", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Gruppe = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.gruppe.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Gruppe IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.zeugnisart", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Zeugnisart = :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.zeugnisart.multiple", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.Zeugnisart IN :value")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.primaryKeyQuery", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOZuordnungReportvorlagen.all.migration", query="SELECT e FROM MigrationDTOZuordnungReportvorlagen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","SchulnrEigner","Jahrgang_ID","Abschluss","AbschlussBB","AbschlussArt","VersetzungKrz","Fachklasse_ID","Reportvorlage","Beschreibung","Gruppe","Zeugnisart"})
public class MigrationDTOZuordnungReportvorlagen {

	/** ID des Datensatzes der einen Zeugnisreport einer Gruppe oder Klasse zuordnet */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOZuordnungReportvorlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOZuordnungReportvorlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOZuordnungReportvorlagen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOZuordnungReportvorlagen(final Long ID, final Integer SchulnrEigner) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) { 
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOZuordnungReportvorlagen other = (MigrationDTOZuordnungReportvorlagen) obj;
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
		return "MigrationDTOZuordnungReportvorlagen(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Jahrgang_ID=" + this.Jahrgang_ID + ", Abschluss=" + this.Abschluss + ", AbschlussBB=" + this.AbschlussBB + ", AbschlussArt=" + this.AbschlussArt + ", VersetzungKrz=" + this.VersetzungKrz + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Reportvorlage=" + this.Reportvorlage + ", Beschreibung=" + this.Beschreibung + ", Gruppe=" + this.Gruppe + ", Zeugnisart=" + this.Zeugnisart + ")";
	}

}