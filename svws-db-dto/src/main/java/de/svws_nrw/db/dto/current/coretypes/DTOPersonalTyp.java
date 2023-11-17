package de.svws_nrw.db.dto.current.coretypes;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle PersonalTypen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "PersonalTypen")
@NamedQuery(name = "DTOPersonalTyp.all", query = "SELECT e FROM DTOPersonalTyp e")
@NamedQuery(name = "DTOPersonalTyp.id", query = "SELECT e FROM DTOPersonalTyp e WHERE e.ID = :value")
@NamedQuery(name = "DTOPersonalTyp.id.multiple", query = "SELECT e FROM DTOPersonalTyp e WHERE e.ID IN :value")
@NamedQuery(name = "DTOPersonalTyp.kuerzel", query = "SELECT e FROM DTOPersonalTyp e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOPersonalTyp.kuerzel.multiple", query = "SELECT e FROM DTOPersonalTyp e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOPersonalTyp.bezeichnung", query = "SELECT e FROM DTOPersonalTyp e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOPersonalTyp.bezeichnung.multiple", query = "SELECT e FROM DTOPersonalTyp e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOPersonalTyp.gueltigvon", query = "SELECT e FROM DTOPersonalTyp e WHERE e.gueltigVon = :value")
@NamedQuery(name = "DTOPersonalTyp.gueltigvon.multiple", query = "SELECT e FROM DTOPersonalTyp e WHERE e.gueltigVon IN :value")
@NamedQuery(name = "DTOPersonalTyp.gueltigbis", query = "SELECT e FROM DTOPersonalTyp e WHERE e.gueltigBis = :value")
@NamedQuery(name = "DTOPersonalTyp.gueltigbis.multiple", query = "SELECT e FROM DTOPersonalTyp e WHERE e.gueltigBis IN :value")
@NamedQuery(name = "DTOPersonalTyp.primaryKeyQuery", query = "SELECT e FROM DTOPersonalTyp e WHERE e.ID = ?1")
@NamedQuery(name = "DTOPersonalTyp.primaryKeyQuery.multiple", query = "SELECT e FROM DTOPersonalTyp e WHERE e.ID IN :value")
@NamedQuery(name = "DTOPersonalTyp.all.migration", query = "SELECT e FROM DTOPersonalTyp e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kuerzel", "Bezeichnung", "gueltigVon", "gueltigBis"})
public final class DTOPersonalTyp {

	/** ID des Personal-Typs */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Das Kürzel des Personal-Typs */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Die (Lang-)Bezeichnung des Personal-Typs */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Der Datensatz ist gültig ab dem Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Der Datensatz ist gültig bis zu dem Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOPersonalTyp ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOPersonalTyp() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOPersonalTyp ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOPersonalTyp(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOPersonalTyp other = (DTOPersonalTyp) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOPersonalTyp(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
