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
 * Diese Klasse dient als DTO für die Datenbanktabelle OrganisationsformenKatalog_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOOrganisationsformenKatalogSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "OrganisationsformenKatalog_Schulformen")
@NamedQuery(name="MigrationDTOOrganisationsformenKatalogSchulformen.all", query="SELECT e FROM MigrationDTOOrganisationsformenKatalogSchulformen e")
@NamedQuery(name="MigrationDTOOrganisationsformenKatalogSchulformen.organisationsform_id", query="SELECT e FROM MigrationDTOOrganisationsformenKatalogSchulformen e WHERE e.Organisationsform_ID = :value")
@NamedQuery(name="MigrationDTOOrganisationsformenKatalogSchulformen.organisationsform_id.multiple", query="SELECT e FROM MigrationDTOOrganisationsformenKatalogSchulformen e WHERE e.Organisationsform_ID IN :value")
@NamedQuery(name="MigrationDTOOrganisationsformenKatalogSchulformen.schulform_kuerzel", query="SELECT e FROM MigrationDTOOrganisationsformenKatalogSchulformen e WHERE e.Schulform_Kuerzel = :value")
@NamedQuery(name="MigrationDTOOrganisationsformenKatalogSchulformen.schulform_kuerzel.multiple", query="SELECT e FROM MigrationDTOOrganisationsformenKatalogSchulformen e WHERE e.Schulform_Kuerzel IN :value")
@NamedQuery(name="MigrationDTOOrganisationsformenKatalogSchulformen.primaryKeyQuery", query="SELECT e FROM MigrationDTOOrganisationsformenKatalogSchulformen e WHERE e.Organisationsform_ID = ?1 AND e.Schulform_Kuerzel = ?2")
@NamedQuery(name="MigrationDTOOrganisationsformenKatalogSchulformen.all.migration", query="SELECT e FROM MigrationDTOOrganisationsformenKatalogSchulformen e WHERE e.Organisationsform_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Organisationsform_ID","Schulform_Kuerzel"})
public class MigrationDTOOrganisationsformenKatalogSchulformen {

	/** die ID der Organisationsform */
	@Id
	@Column(name = "Organisationsform_ID")
	@JsonProperty
	public Long Organisationsform_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOOrganisationsformenKatalogSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOOrganisationsformenKatalogSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOOrganisationsformenKatalogSchulformen ohne eine Initialisierung der Attribute.
	 * @param Organisationsform_ID   der Wert für das Attribut Organisationsform_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public MigrationDTOOrganisationsformenKatalogSchulformen(final Long Organisationsform_ID, final String Schulform_Kuerzel) {
		if (Organisationsform_ID == null) { 
			throw new NullPointerException("Organisationsform_ID must not be null");
		}
		this.Organisationsform_ID = Organisationsform_ID;
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
		MigrationDTOOrganisationsformenKatalogSchulformen other = (MigrationDTOOrganisationsformenKatalogSchulformen) obj;
		if (Organisationsform_ID == null) {
			if (other.Organisationsform_ID != null)
				return false;
		} else if (!Organisationsform_ID.equals(other.Organisationsform_ID))
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
		result = prime * result + ((Organisationsform_ID == null) ? 0 : Organisationsform_ID.hashCode());

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
		return "MigrationDTOOrganisationsformenKatalogSchulformen(Organisationsform_ID=" + this.Organisationsform_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ")";
	}

}