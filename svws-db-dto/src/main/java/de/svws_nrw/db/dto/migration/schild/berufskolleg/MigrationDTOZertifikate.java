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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Zertifikate.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Zertifikate")
@NamedQuery(name = "MigrationDTOZertifikate.all", query = "SELECT e FROM MigrationDTOZertifikate e")
@NamedQuery(name = "MigrationDTOZertifikate.id", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOZertifikate.id.multiple", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOZertifikate.schulnreigner", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOZertifikate.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOZertifikate.kuerzel", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.Kuerzel = :value")
@NamedQuery(name = "MigrationDTOZertifikate.kuerzel.multiple", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "MigrationDTOZertifikate.bezeichnung", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOZertifikate.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOZertifikate.fach", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.Fach = :value")
@NamedQuery(name = "MigrationDTOZertifikate.fach.multiple", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.Fach IN :value")
@NamedQuery(name = "MigrationDTOZertifikate.formatvorlage", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.Formatvorlage = :value")
@NamedQuery(name = "MigrationDTOZertifikate.formatvorlage.multiple", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.Formatvorlage IN :value")
@NamedQuery(name = "MigrationDTOZertifikate.primaryKeyQuery", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOZertifikate.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOZertifikate.all.migration", query = "SELECT e FROM MigrationDTOZertifikate e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "SchulnrEigner", "Kuerzel", "Bezeichnung", "Fach", "Formatvorlage"})
public final class MigrationDTOZertifikate {

	/** ID des Zertifikats */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Kürzel des Zertifikats */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Bezeichnung des Zertifikats */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Fachbezeichnung für das Zertifikat */
	@Column(name = "Fach")
	@JsonProperty
	public String Fach;

	/** Formatforlage für das Zertifikat */
	@Column(name = "Formatvorlage")
	@JsonProperty
	public String Formatvorlage;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOZertifikate ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOZertifikate() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOZertifikate ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public MigrationDTOZertifikate(final Long ID, final Integer SchulnrEigner, final String Kuerzel) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOZertifikate other = (MigrationDTOZertifikate) obj;
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
		return "MigrationDTOZertifikate(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", Fach=" + this.Fach + ", Formatvorlage=" + this.Formatvorlage + ")";
	}

}
