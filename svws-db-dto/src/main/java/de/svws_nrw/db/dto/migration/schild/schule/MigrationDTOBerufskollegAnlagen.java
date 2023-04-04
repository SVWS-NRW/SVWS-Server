package de.svws_nrw.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Berufskolleg_Anlagen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Berufskolleg_Anlagen")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.all", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.id", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.id.multiple", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.kuerzel", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.Kuerzel = :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.kuerzel.multiple", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.bezeichnung", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.gueltigvon", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.gueltigVon = :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.gueltigvon.multiple", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.gueltigVon IN :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.gueltigbis", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.gueltigBis = :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.gueltigbis.multiple", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.gueltigBis IN :value")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.primaryKeyQuery", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOBerufskollegAnlagen.all.migration", query = "SELECT e FROM MigrationDTOBerufskollegAnlagen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kuerzel", "Bezeichnung", "gueltigVon", "gueltigBis"})
public final class MigrationDTOBerufskollegAnlagen {

	/** ID der Anlage */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Einstelliges Kürzel der Anlage (A,B,C,D,E,X,Z) */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Bezeichnung der Anlage */
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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBerufskollegAnlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBerufskollegAnlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBerufskollegAnlagen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOBerufskollegAnlagen(final Long ID, final String Kuerzel, final String Bezeichnung) {
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
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBerufskollegAnlagen other = (MigrationDTOBerufskollegAnlagen) obj;
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
		return "MigrationDTOBerufskollegAnlagen(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
