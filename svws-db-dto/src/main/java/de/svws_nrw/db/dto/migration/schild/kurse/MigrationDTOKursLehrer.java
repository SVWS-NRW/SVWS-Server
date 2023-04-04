package de.svws_nrw.db.dto.migration.schild.kurse;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle KursLehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOKursLehrerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KursLehrer")
@NamedQuery(name = "MigrationDTOKursLehrer.all", query = "SELECT e FROM MigrationDTOKursLehrer e")
@NamedQuery(name = "MigrationDTOKursLehrer.kurs_id", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.Kurs_ID = :value")
@NamedQuery(name = "MigrationDTOKursLehrer.kurs_id.multiple", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.Kurs_ID IN :value")
@NamedQuery(name = "MigrationDTOKursLehrer.lehrer_id", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "MigrationDTOKursLehrer.lehrer_id.multiple", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOKursLehrer.anteil", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.Anteil = :value")
@NamedQuery(name = "MigrationDTOKursLehrer.anteil.multiple", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.Anteil IN :value")
@NamedQuery(name = "MigrationDTOKursLehrer.schulnreigner", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOKursLehrer.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOKursLehrer.primaryKeyQuery", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.Kurs_ID = ?1 AND e.Lehrer_ID = ?2")
@NamedQuery(name = "MigrationDTOKursLehrer.all.migration", query = "SELECT e FROM MigrationDTOKursLehrer e WHERE e.Kurs_ID IS NOT NULL AND e.Lehrer_ID IS NOT NULL")
@JsonPropertyOrder({"Kurs_ID", "Lehrer_ID", "Anteil", "SchulnrEigner"})
public final class MigrationDTOKursLehrer {

	/** ID des Kurses zu denen der Lehrer gehört */
	@Id
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** ID des Lehrers */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Wochenstunden für die Zusatzkraft */
	@Column(name = "Anteil")
	@JsonProperty
	public Double Anteil;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKursLehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKursLehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKursLehrer ohne eine Initialisierung der Attribute.
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public MigrationDTOKursLehrer(final Long Kurs_ID, final Long Lehrer_ID) {
		if (Kurs_ID == null) {
			throw new NullPointerException("Kurs_ID must not be null");
		}
		this.Kurs_ID = Kurs_ID;
		if (Lehrer_ID == null) {
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOKursLehrer other = (MigrationDTOKursLehrer) obj;
		if (Kurs_ID == null) {
			if (other.Kurs_ID != null)
				return false;
		} else if (!Kurs_ID.equals(other.Kurs_ID))
			return false;

		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Kurs_ID == null) ? 0 : Kurs_ID.hashCode());

		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOKursLehrer(Kurs_ID=" + this.Kurs_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Anteil=" + this.Anteil + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
