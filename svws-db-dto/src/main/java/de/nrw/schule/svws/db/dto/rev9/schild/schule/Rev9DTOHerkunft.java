package de.nrw.schule.svws.db.dto.rev9.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunft.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunft")
@NamedQuery(name="Rev9DTOHerkunft.all", query="SELECT e FROM Rev9DTOHerkunft e")
@NamedQuery(name="Rev9DTOHerkunft.id", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOHerkunft.id.multiple", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOHerkunft.kuerzel", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev9DTOHerkunft.kuerzel.multiple", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev9DTOHerkunft.beschreibung", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev9DTOHerkunft.beschreibung.multiple", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev9DTOHerkunft.gueltigvon", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev9DTOHerkunft.gueltigvon.multiple", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev9DTOHerkunft.gueltigbis", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev9DTOHerkunft.gueltigbis.multiple", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev9DTOHerkunft.primaryKeyQuery", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOHerkunft.all.migration", query="SELECT e FROM Rev9DTOHerkunft e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Beschreibung","gueltigVon","gueltigBis"})
public class Rev9DTOHerkunft {

	/** Die ID der Herkunft */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Statistikkürzel der Herkunft */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Die textuelle Beschreibung der Herkunft */
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
	 * Erstellt ein neues Objekt der Klasse Rev9DTOHerkunft ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOHerkunft() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOHerkunft ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public Rev9DTOHerkunft(final Long ID, final String Kuerzel, final String Beschreibung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
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
		Rev9DTOHerkunft other = (Rev9DTOHerkunft) obj;
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
		return "Rev9DTOHerkunft(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}