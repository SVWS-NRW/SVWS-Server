package de.svws_nrw.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Berufskolleg_Berufsebenen2.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Berufskolleg_Berufsebenen2")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.all", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.id", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.ID = :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.id.multiple", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.ID IN :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.kuerzel", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.kuerzel.multiple", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.bezeichnung", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.bezeichnung.multiple", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.gueltigvon", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.gueltigVon = :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.gueltigvon.multiple", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.gueltigVon IN :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.gueltigbis", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.gueltigBis = :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.gueltigbis.multiple", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.gueltigBis IN :value")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.primaryKeyQuery", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.ID = ?1")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.primaryKeyQuery.multiple", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOBerufskollegBerufsebenen2.all.migration", query = "SELECT e FROM DTOBerufskollegBerufsebenen2 e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kuerzel", "Bezeichnung", "gueltigVon", "gueltigBis"})
public final class DTOBerufskollegBerufsebenen2 {

	/** ID der Berufsebene  */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Kürzel der Berufsebene */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Bezeichnung der Berufsebene */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBerufskollegBerufsebenen2 ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBerufskollegBerufsebenen2() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBerufskollegBerufsebenen2 ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOBerufskollegBerufsebenen2(final long ID, final String Kuerzel, final String Bezeichnung) {
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBerufskollegBerufsebenen2 other = (DTOBerufskollegBerufsebenen2) obj;
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
		return "DTOBerufskollegBerufsebenen2(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
