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
 * Diese Klasse dient als DTO für die Datenbanktabelle KursFortschreibungsarten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KursFortschreibungsarten")
@NamedQuery(name = "DTOKursFortschreibungsart.all", query = "SELECT e FROM DTOKursFortschreibungsart e")
@NamedQuery(name = "DTOKursFortschreibungsart.id", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.ID = :value")
@NamedQuery(name = "DTOKursFortschreibungsart.id.multiple", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKursFortschreibungsart.kuerzel", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOKursFortschreibungsart.kuerzel.multiple", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOKursFortschreibungsart.bezeichnung", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOKursFortschreibungsart.bezeichnung.multiple", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOKursFortschreibungsart.gueltigvon", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.gueltigVon = :value")
@NamedQuery(name = "DTOKursFortschreibungsart.gueltigvon.multiple", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.gueltigVon IN :value")
@NamedQuery(name = "DTOKursFortschreibungsart.gueltigbis", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.gueltigBis = :value")
@NamedQuery(name = "DTOKursFortschreibungsart.gueltigbis.multiple", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.gueltigBis IN :value")
@NamedQuery(name = "DTOKursFortschreibungsart.primaryKeyQuery", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.ID = ?1")
@NamedQuery(name = "DTOKursFortschreibungsart.all.migration", query = "SELECT e FROM DTOKursFortschreibungsart e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kuerzel", "Bezeichnung", "gueltigVon", "gueltigBis"})
public final class DTOKursFortschreibungsart {

	/** ID der Kurs-Fortschreibungsart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Kürzel der Kurs-Fortschreibungsart */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Die Bezeichnung der Kurs-Fortschreibungsart */
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
	 * Erstellt ein neues Objekt der Klasse DTOKursFortschreibungsart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKursFortschreibungsart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursFortschreibungsart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public DTOKursFortschreibungsart(final Long ID, final String Kuerzel) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKursFortschreibungsart other = (DTOKursFortschreibungsart) obj;
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
		return "DTOKursFortschreibungsart(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
