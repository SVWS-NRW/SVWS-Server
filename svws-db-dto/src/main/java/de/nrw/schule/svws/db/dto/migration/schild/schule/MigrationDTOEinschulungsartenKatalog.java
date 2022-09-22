package de.nrw.schule.svws.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EinschulungsartKatalog.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EinschulungsartKatalog")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.all", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.id", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.id.multiple", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.kuerzel", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.Kuerzel = :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.kuerzel.multiple", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.Kuerzel IN :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.bezeichnung", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.bezeichnung.multiple", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.beschreibung", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.Beschreibung = :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.beschreibung.multiple", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.Beschreibung IN :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.gueltigvon", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.gueltigvon.multiple", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.gueltigbis", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.gueltigbis.multiple", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.primaryKeyQuery", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOEinschulungsartenKatalog.all.migration", query="SELECT e FROM MigrationDTOEinschulungsartenKatalog e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Bezeichnung","Beschreibung","gueltigVon","gueltigBis"})
public class MigrationDTOEinschulungsartenKatalog {

	/** ID der Einschulungsart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Kürzel der Einschulungsart */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Eine kurze Bezeichnung für die Einschulungsart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die textuelle Beschreibung der Einschulungsart */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEinschulungsartenKatalog ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOEinschulungsartenKatalog() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEinschulungsartenKatalog ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public MigrationDTOEinschulungsartenKatalog(final Long ID, final String Kuerzel, final String Bezeichnung, final String Beschreibung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Beschreibung == null) { 
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOEinschulungsartenKatalog other = (MigrationDTOEinschulungsartenKatalog) obj;
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
		return "MigrationDTOEinschulungsartenKatalog(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", Beschreibung=" + this.Beschreibung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}