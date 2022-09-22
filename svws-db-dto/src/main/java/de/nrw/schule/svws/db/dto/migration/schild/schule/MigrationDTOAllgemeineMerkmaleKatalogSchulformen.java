package de.nrw.schule.svws.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle AllgemeineMerkmaleKatalog_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOAllgemeineMerkmaleKatalogSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "AllgemeineMerkmaleKatalog_Schulformen")
@NamedQuery(name="MigrationDTOAllgemeineMerkmaleKatalogSchulformen.all", query="SELECT e FROM MigrationDTOAllgemeineMerkmaleKatalogSchulformen e")
@NamedQuery(name="MigrationDTOAllgemeineMerkmaleKatalogSchulformen.merkmal_id", query="SELECT e FROM MigrationDTOAllgemeineMerkmaleKatalogSchulformen e WHERE e.Merkmal_ID = :value")
@NamedQuery(name="MigrationDTOAllgemeineMerkmaleKatalogSchulformen.merkmal_id.multiple", query="SELECT e FROM MigrationDTOAllgemeineMerkmaleKatalogSchulformen e WHERE e.Merkmal_ID IN :value")
@NamedQuery(name="MigrationDTOAllgemeineMerkmaleKatalogSchulformen.schulform_kuerzel", query="SELECT e FROM MigrationDTOAllgemeineMerkmaleKatalogSchulformen e WHERE e.Schulform_Kuerzel = :value")
@NamedQuery(name="MigrationDTOAllgemeineMerkmaleKatalogSchulformen.schulform_kuerzel.multiple", query="SELECT e FROM MigrationDTOAllgemeineMerkmaleKatalogSchulformen e WHERE e.Schulform_Kuerzel IN :value")
@NamedQuery(name="MigrationDTOAllgemeineMerkmaleKatalogSchulformen.primaryKeyQuery", query="SELECT e FROM MigrationDTOAllgemeineMerkmaleKatalogSchulformen e WHERE e.Merkmal_ID = ?1 AND e.Schulform_Kuerzel = ?2")
@NamedQuery(name="MigrationDTOAllgemeineMerkmaleKatalogSchulformen.all.migration", query="SELECT e FROM MigrationDTOAllgemeineMerkmaleKatalogSchulformen e WHERE e.Merkmal_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Merkmal_ID","Schulform_Kuerzel"})
public class MigrationDTOAllgemeineMerkmaleKatalogSchulformen {

	/** die ID des allgemeinen Merkmals bei Schulen und/oder Schülern */
	@Id
	@Column(name = "Merkmal_ID")
	@JsonProperty
	public Long Merkmal_ID;

	/** das Kürzel des allgemeinen Merkmals bei Schulen und/oder Schülern */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAllgemeineMerkmaleKatalogSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAllgemeineMerkmaleKatalogSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAllgemeineMerkmaleKatalogSchulformen ohne eine Initialisierung der Attribute.
	 * @param Merkmal_ID   der Wert für das Attribut Merkmal_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public MigrationDTOAllgemeineMerkmaleKatalogSchulformen(final Long Merkmal_ID, final String Schulform_Kuerzel) {
		if (Merkmal_ID == null) { 
			throw new NullPointerException("Merkmal_ID must not be null");
		}
		this.Merkmal_ID = Merkmal_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAllgemeineMerkmaleKatalogSchulformen other = (MigrationDTOAllgemeineMerkmaleKatalogSchulformen) obj;
		if (Merkmal_ID == null) {
			if (other.Merkmal_ID != null)
				return false;
		} else if (!Merkmal_ID.equals(other.Merkmal_ID))
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
		result = prime * result + ((Merkmal_ID == null) ? 0 : Merkmal_ID.hashCode());

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
		return "MigrationDTOAllgemeineMerkmaleKatalogSchulformen(Merkmal_ID=" + this.Merkmal_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ")";
	}

}