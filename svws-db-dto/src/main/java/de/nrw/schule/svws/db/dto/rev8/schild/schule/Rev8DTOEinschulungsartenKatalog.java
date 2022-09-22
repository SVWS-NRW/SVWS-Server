package de.nrw.schule.svws.db.dto.rev8.schild.schule;

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
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.all", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.id", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.id.multiple", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.kuerzel", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.kuerzel.multiple", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.bezeichnung", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.bezeichnung.multiple", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.beschreibung", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.beschreibung.multiple", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.gueltigvon", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.gueltigvon.multiple", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.gueltigbis", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.gueltigbis.multiple", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.primaryKeyQuery", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOEinschulungsartenKatalog.all.migration", query="SELECT e FROM Rev8DTOEinschulungsartenKatalog e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Bezeichnung","Beschreibung","gueltigVon","gueltigBis"})
public class Rev8DTOEinschulungsartenKatalog {

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOEinschulungsartenKatalog ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOEinschulungsartenKatalog() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOEinschulungsartenKatalog ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public Rev8DTOEinschulungsartenKatalog(final Long ID, final String Kuerzel, final String Bezeichnung, final String Beschreibung) {
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
		Rev8DTOEinschulungsartenKatalog other = (Rev8DTOEinschulungsartenKatalog) obj;
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
		return "Rev8DTOEinschulungsartenKatalog(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", Beschreibung=" + this.Beschreibung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}