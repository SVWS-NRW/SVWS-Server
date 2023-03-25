package de.svws_nrw.db.dto.migration.schild.berufskolleg;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Zertifikate.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Zertifikate")
@NamedQuery(name="MigrationDTOKatalogZertifikate.all", query="SELECT e FROM MigrationDTOKatalogZertifikate e")
@NamedQuery(name="MigrationDTOKatalogZertifikate.kuerzel", query="SELECT e FROM MigrationDTOKatalogZertifikate e WHERE e.Kuerzel = :value")
@NamedQuery(name="MigrationDTOKatalogZertifikate.kuerzel.multiple", query="SELECT e FROM MigrationDTOKatalogZertifikate e WHERE e.Kuerzel IN :value")
@NamedQuery(name="MigrationDTOKatalogZertifikate.bezeichnung", query="SELECT e FROM MigrationDTOKatalogZertifikate e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOKatalogZertifikate.bezeichnung.multiple", query="SELECT e FROM MigrationDTOKatalogZertifikate e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOKatalogZertifikate.schulnreigner", query="SELECT e FROM MigrationDTOKatalogZertifikate e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOKatalogZertifikate.schulnreigner.multiple", query="SELECT e FROM MigrationDTOKatalogZertifikate e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOKatalogZertifikate.primaryKeyQuery", query="SELECT e FROM MigrationDTOKatalogZertifikate e WHERE e.Kuerzel = ?1")
@NamedQuery(name="MigrationDTOKatalogZertifikate.all.migration", query="SELECT e FROM MigrationDTOKatalogZertifikate e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel","Bezeichnung","SchulnrEigner"})
public class MigrationDTOKatalogZertifikate {

	/** Kürzel des Zertifikats */
	@Id
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Bezeichnung des Zertifikats */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKatalogZertifikate ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKatalogZertifikate() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKatalogZertifikate ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOKatalogZertifikate(final String Kuerzel, final String Bezeichnung, final Integer SchulnrEigner) {
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (SchulnrEigner == null) { 
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOKatalogZertifikate other = (MigrationDTOKatalogZertifikate) obj;
		if (Kuerzel == null) {
			if (other.Kuerzel != null)
				return false;
		} else if (!Kuerzel.equals(other.Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Kuerzel == null) ? 0 : Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOKatalogZertifikate(Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}