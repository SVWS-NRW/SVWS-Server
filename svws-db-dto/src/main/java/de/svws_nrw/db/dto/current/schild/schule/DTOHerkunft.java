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
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunft.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunft")
@NamedQuery(name = "DTOHerkunft.all", query = "SELECT e FROM DTOHerkunft e")
@NamedQuery(name = "DTOHerkunft.id", query = "SELECT e FROM DTOHerkunft e WHERE e.ID = :value")
@NamedQuery(name = "DTOHerkunft.id.multiple", query = "SELECT e FROM DTOHerkunft e WHERE e.ID IN :value")
@NamedQuery(name = "DTOHerkunft.kuerzel", query = "SELECT e FROM DTOHerkunft e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOHerkunft.kuerzel.multiple", query = "SELECT e FROM DTOHerkunft e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOHerkunft.beschreibung", query = "SELECT e FROM DTOHerkunft e WHERE e.Beschreibung = :value")
@NamedQuery(name = "DTOHerkunft.beschreibung.multiple", query = "SELECT e FROM DTOHerkunft e WHERE e.Beschreibung IN :value")
@NamedQuery(name = "DTOHerkunft.gueltigvon", query = "SELECT e FROM DTOHerkunft e WHERE e.gueltigVon = :value")
@NamedQuery(name = "DTOHerkunft.gueltigvon.multiple", query = "SELECT e FROM DTOHerkunft e WHERE e.gueltigVon IN :value")
@NamedQuery(name = "DTOHerkunft.gueltigbis", query = "SELECT e FROM DTOHerkunft e WHERE e.gueltigBis = :value")
@NamedQuery(name = "DTOHerkunft.gueltigbis.multiple", query = "SELECT e FROM DTOHerkunft e WHERE e.gueltigBis IN :value")
@NamedQuery(name = "DTOHerkunft.primaryKeyQuery", query = "SELECT e FROM DTOHerkunft e WHERE e.ID = ?1")
@NamedQuery(name = "DTOHerkunft.primaryKeyQuery.multiple", query = "SELECT e FROM DTOHerkunft e WHERE e.ID IN :value")
@NamedQuery(name = "DTOHerkunft.all.migration", query = "SELECT e FROM DTOHerkunft e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kuerzel", "Beschreibung", "gueltigVon", "gueltigBis"})
public final class DTOHerkunft {

	/** Die ID der Herkunft */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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
	 * Erstellt ein neues Objekt der Klasse DTOHerkunft ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOHerkunft() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunft ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public DTOHerkunft(final long ID, final String Kuerzel, final String Beschreibung) {
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOHerkunft other = (DTOHerkunft) obj;
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
		return "DTOHerkunft(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
