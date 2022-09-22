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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_FaecherSortierung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_FaecherSortierung")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.all", query="SELECT e FROM MigrationDTOInternFaecherSortierung e")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.fach", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Fach = :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.fach.multiple", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Fach IN :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.bezeichnung", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.bezeichnung.multiple", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.sortierung1", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Sortierung1 = :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.sortierung1.multiple", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Sortierung1 IN :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.sortierung2", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Sortierung2 = :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.sortierung2.multiple", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Sortierung2 IN :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.fachgruppe_id", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Fachgruppe_ID = :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.fachgruppe_id.multiple", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Fachgruppe_ID IN :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.fachgruppekrz", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.FachgruppeKrz = :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.fachgruppekrz.multiple", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.FachgruppeKrz IN :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.aufgabenbereichabitur", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.AufgabenbereichAbitur = :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.aufgabenbereichabitur.multiple", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.AufgabenbereichAbitur IN :value")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Fach = ?1")
@NamedQuery(name="MigrationDTOInternFaecherSortierung.all.migration", query="SELECT e FROM MigrationDTOInternFaecherSortierung e WHERE e.Fach IS NOT NULL")
@JsonPropertyOrder({"Fach","Bezeichnung","Sortierung1","Sortierung2","Fachgruppe_ID","FachgruppeKrz","AufgabenbereichAbitur"})
public class MigrationDTOInternFaecherSortierung {

	/** Schildintern Tabelle: Fächerkürzel der Sortierung */
	@Id
	@Column(name = "Fach")
	@JsonProperty
	public String Fach;

	/** Schildintern Tabelle: Bezeichnung der Sortierung */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Schildintern Tabelle: DEPRECATED:  */
	@Column(name = "Sortierung1")
	@JsonProperty
	public Integer Sortierung1;

	/** Schildintern Tabelle: DEPRECATED:  */
	@Column(name = "Sortierung2")
	@JsonProperty
	public Integer Sortierung2;

	/** Schildintern Tabelle: Verweis auf die Schildintern Fachgruppe */
	@Column(name = "Fachgruppe_ID")
	@JsonProperty
	public Long Fachgruppe_ID;

	/** Schildintern Tabelle: Kürzel der Fachgruppe */
	@Column(name = "FachgruppeKrz")
	@JsonProperty
	public String FachgruppeKrz;

	/** Schildintern Tabelle: Angabe des Aufgabenbreich für das Abitur */
	@Column(name = "AufgabenbereichAbitur")
	@JsonProperty
	public String AufgabenbereichAbitur;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternFaecherSortierung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternFaecherSortierung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternFaecherSortierung ohne eine Initialisierung der Attribute.
	 * @param Fach   der Wert für das Attribut Fach
	 */
	public MigrationDTOInternFaecherSortierung(final String Fach) {
		if (Fach == null) { 
			throw new NullPointerException("Fach must not be null");
		}
		this.Fach = Fach;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternFaecherSortierung other = (MigrationDTOInternFaecherSortierung) obj;
		if (Fach == null) {
			if (other.Fach != null)
				return false;
		} else if (!Fach.equals(other.Fach))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Fach == null) ? 0 : Fach.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOInternFaecherSortierung(Fach=" + this.Fach + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung1=" + this.Sortierung1 + ", Sortierung2=" + this.Sortierung2 + ", Fachgruppe_ID=" + this.Fachgruppe_ID + ", FachgruppeKrz=" + this.FachgruppeKrz + ", AufgabenbereichAbitur=" + this.AufgabenbereichAbitur + ")";
	}

}