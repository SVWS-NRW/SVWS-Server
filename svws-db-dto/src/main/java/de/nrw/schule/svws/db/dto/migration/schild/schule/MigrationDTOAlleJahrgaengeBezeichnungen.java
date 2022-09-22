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
 * Diese Klasse dient als DTO für die Datenbanktabelle Jahrgaenge_Bezeichnungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOAlleJahrgaengeBezeichnungenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Jahrgaenge_Bezeichnungen")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.all", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.jahrgang_id", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.jahrgang_id.multiple", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.schulform_kuerzel", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e WHERE e.Schulform_Kuerzel = :value")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.schulform_kuerzel.multiple", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e WHERE e.Schulform_Kuerzel IN :value")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.bezeichnung", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.bezeichnung.multiple", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.primaryKeyQuery", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e WHERE e.Jahrgang_ID = ?1 AND e.Schulform_Kuerzel = ?2")
@NamedQuery(name="MigrationDTOAlleJahrgaengeBezeichnungen.all.migration", query="SELECT e FROM MigrationDTOAlleJahrgaengeBezeichnungen e WHERE e.Jahrgang_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Jahrgang_ID","Schulform_Kuerzel","Bezeichnung"})
public class MigrationDTOAlleJahrgaengeBezeichnungen {

	/** Die ID des Jahrgangs */
	@Id
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** das Kürzel der Schulform, für welche die Bezeichnung gültig ist */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/** die Bezeichnung des Jahrgangs */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleJahrgaengeBezeichnungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAlleJahrgaengeBezeichnungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleJahrgaengeBezeichnungen ohne eine Initialisierung der Attribute.
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOAlleJahrgaengeBezeichnungen(final Long Jahrgang_ID, final String Schulform_Kuerzel, final String Bezeichnung) {
		if (Jahrgang_ID == null) { 
			throw new NullPointerException("Jahrgang_ID must not be null");
		}
		this.Jahrgang_ID = Jahrgang_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAlleJahrgaengeBezeichnungen other = (MigrationDTOAlleJahrgaengeBezeichnungen) obj;
		if (Jahrgang_ID == null) {
			if (other.Jahrgang_ID != null)
				return false;
		} else if (!Jahrgang_ID.equals(other.Jahrgang_ID))
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
		result = prime * result + ((Jahrgang_ID == null) ? 0 : Jahrgang_ID.hashCode());

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
		return "MigrationDTOAlleJahrgaengeBezeichnungen(Jahrgang_ID=" + this.Jahrgang_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ")";
	}

}