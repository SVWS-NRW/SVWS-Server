package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunft_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOHerkunftSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunft_Schulformen")
@NamedQuery(name = "MigrationDTOHerkunftSchulformen.all", query = "SELECT e FROM MigrationDTOHerkunftSchulformen e")
@NamedQuery(name = "MigrationDTOHerkunftSchulformen.herkunft_id", query = "SELECT e FROM MigrationDTOHerkunftSchulformen e WHERE e.Herkunft_ID = :value")
@NamedQuery(name = "MigrationDTOHerkunftSchulformen.herkunft_id.multiple", query = "SELECT e FROM MigrationDTOHerkunftSchulformen e WHERE e.Herkunft_ID IN :value")
@NamedQuery(name = "MigrationDTOHerkunftSchulformen.schulform_kuerzel", query = "SELECT e FROM MigrationDTOHerkunftSchulformen e WHERE e.Schulform_Kuerzel = :value")
@NamedQuery(name = "MigrationDTOHerkunftSchulformen.schulform_kuerzel.multiple", query = "SELECT e FROM MigrationDTOHerkunftSchulformen e WHERE e.Schulform_Kuerzel IN :value")
@NamedQuery(name = "MigrationDTOHerkunftSchulformen.primaryKeyQuery", query = "SELECT e FROM MigrationDTOHerkunftSchulformen e WHERE e.Herkunft_ID = ?1 AND e.Schulform_Kuerzel = ?2")
@NamedQuery(name = "MigrationDTOHerkunftSchulformen.all.migration", query = "SELECT e FROM MigrationDTOHerkunftSchulformen e WHERE e.Herkunft_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Herkunft_ID", "Schulform_Kuerzel"})
public final class MigrationDTOHerkunftSchulformen {

	/** die ID der Herkunft */
	@Id
	@Column(name = "Herkunft_ID")
	@JsonProperty
	public Long Herkunft_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOHerkunftSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOHerkunftSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOHerkunftSchulformen ohne eine Initialisierung der Attribute.
	 * @param Herkunft_ID   der Wert für das Attribut Herkunft_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public MigrationDTOHerkunftSchulformen(final Long Herkunft_ID, final String Schulform_Kuerzel) {
		if (Herkunft_ID == null) {
			throw new NullPointerException("Herkunft_ID must not be null");
		}
		this.Herkunft_ID = Herkunft_ID;
		if (Schulform_Kuerzel == null) {
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOHerkunftSchulformen other = (MigrationDTOHerkunftSchulformen) obj;
		if (Herkunft_ID == null) {
			if (other.Herkunft_ID != null)
				return false;
		} else if (!Herkunft_ID.equals(other.Herkunft_ID))
			return false;

		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Herkunft_ID == null) ? 0 : Herkunft_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOHerkunftSchulformen(Herkunft_ID=" + this.Herkunft_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ")";
	}

}
