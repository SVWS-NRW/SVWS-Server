package de.nrw.schule.svws.db.dto.migration.schild.faecher;

import de.nrw.schule.svws.db.DBEntityManager;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle FachKatalog_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOFaecherKatalogSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "FachKatalog_Schulformen")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.all", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.fach_id", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e WHERE e.Fach_ID = :value")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.fach_id.multiple", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e WHERE e.Fach_ID IN :value")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.schulform_kuerzel", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e WHERE e.Schulform_Kuerzel = :value")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.schulform_kuerzel.multiple", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e WHERE e.Schulform_Kuerzel IN :value")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.schulgliederung_kuerzel", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e WHERE e.Schulgliederung_Kuerzel = :value")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.schulgliederung_kuerzel.multiple", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e WHERE e.Schulgliederung_Kuerzel IN :value")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.primaryKeyQuery", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e WHERE e.Fach_ID = ?1 AND e.Schulform_Kuerzel = ?2 AND e.Schulgliederung_Kuerzel = ?3")
@NamedQuery(name="MigrationDTOFaecherKatalogSchulformen.all.migration", query="SELECT e FROM MigrationDTOFaecherKatalogSchulformen e WHERE e.Fach_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL AND e.Schulgliederung_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Fach_ID","Schulform_Kuerzel","Schulgliederung_Kuerzel"})
public class MigrationDTOFaecherKatalogSchulformen {

	/** die ID des Faches */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/** das Kürzel der Schulgliederung bzw. des Bildungsganges. Leerer String, falls alle Gliederungen der Schulform gemeint sind */
	@Id
	@Column(name = "Schulgliederung_Kuerzel")
	@JsonProperty
	public String Schulgliederung_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFaecherKatalogSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFaecherKatalogSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFaecherKatalogSchulformen ohne eine Initialisierung der Attribute.
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 * @param Schulgliederung_Kuerzel   der Wert für das Attribut Schulgliederung_Kuerzel
	 */
	public MigrationDTOFaecherKatalogSchulformen(final Long Fach_ID, final String Schulform_Kuerzel, final String Schulgliederung_Kuerzel) {
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
		if (Schulgliederung_Kuerzel == null) { 
			throw new NullPointerException("Schulgliederung_Kuerzel must not be null");
		}
		this.Schulgliederung_Kuerzel = Schulgliederung_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFaecherKatalogSchulformen other = (MigrationDTOFaecherKatalogSchulformen) obj;
		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;

		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;

		if (Schulgliederung_Kuerzel == null) {
			if (other.Schulgliederung_Kuerzel != null)
				return false;
		} else if (!Schulgliederung_Kuerzel.equals(other.Schulgliederung_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());

		result = prime * result + ((Schulgliederung_Kuerzel == null) ? 0 : Schulgliederung_Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOFaecherKatalogSchulformen(Fach_ID=" + this.Fach_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ", Schulgliederung_Kuerzel=" + this.Schulgliederung_Kuerzel + ")";
	}

}