package de.nrw.schule.svws.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Jahrgaenge")
@NamedQuery(name="DTOAlleJahrgaenge.all", query="SELECT e FROM DTOAlleJahrgaenge e")
@NamedQuery(name="DTOAlleJahrgaenge.id", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.ID = :value")
@NamedQuery(name="DTOAlleJahrgaenge.id.multiple", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.ID IN :value")
@NamedQuery(name="DTOAlleJahrgaenge.kuerzel", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.Kuerzel = :value")
@NamedQuery(name="DTOAlleJahrgaenge.kuerzel.multiple", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DTOAlleJahrgaenge.gueltigvon", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOAlleJahrgaenge.gueltigvon.multiple", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOAlleJahrgaenge.gueltigbis", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOAlleJahrgaenge.gueltigbis.multiple", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOAlleJahrgaenge.primaryKeyQuery", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.ID = ?1")
@NamedQuery(name="DTOAlleJahrgaenge.all.migration", query="SELECT e FROM DTOAlleJahrgaenge e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","gueltigVon","gueltigBis"})
public class DTOAlleJahrgaenge {

	/** Die ID des Jahrgangs */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das zweistellige Kürzel des Jahrgangs */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleJahrgaenge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAlleJahrgaenge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleJahrgaenge ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public DTOAlleJahrgaenge(final Long ID, final String Kuerzel) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAlleJahrgaenge other = (DTOAlleJahrgaenge) obj;
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
		return "DTOAlleJahrgaenge(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}