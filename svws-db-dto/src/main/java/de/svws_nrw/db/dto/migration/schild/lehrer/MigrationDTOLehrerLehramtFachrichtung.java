package de.svws_nrw.db.dto.migration.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerLehramtFachr.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOLehrerLehramtFachrichtungPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerLehramtFachr")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.all", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.lehrer_id", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.lehrer_id.multiple", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.fachrkrz", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.FachrKrz = :value")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.fachrkrz.multiple", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.FachrKrz IN :value")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.fachranerkennungkrz", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.FachrAnerkennungKrz = :value")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.fachranerkennungkrz.multiple", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.FachrAnerkennungKrz IN :value")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.schulnreigner", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.primaryKeyQuery", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.Lehrer_ID = ?1 AND e.FachrKrz = ?2")
@NamedQuery(name = "MigrationDTOLehrerLehramtFachrichtung.all.migration", query = "SELECT e FROM MigrationDTOLehrerLehramtFachrichtung e WHERE e.Lehrer_ID IS NOT NULL AND e.FachrKrz IS NOT NULL")
@JsonPropertyOrder({"Lehrer_ID", "FachrKrz", "FachrAnerkennungKrz", "SchulnrEigner"})
public final class MigrationDTOLehrerLehramtFachrichtung {

	/** LehrerID zu der die Fachrichtung gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Fachrichtungskürzel */
	@Id
	@Column(name = "FachrKrz")
	@JsonProperty
	public String FachrKrz;

	/** FachrichtungAnerkennungskürzel */
	@Column(name = "FachrAnerkennungKrz")
	@JsonProperty
	public String FachrAnerkennungKrz;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLehramtFachrichtung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerLehramtFachrichtung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerLehramtFachrichtung ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public MigrationDTOLehrerLehramtFachrichtung(final Long Lehrer_ID) {
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
		MigrationDTOLehrerLehramtFachrichtung other = (MigrationDTOLehrerLehramtFachrichtung) obj;
		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;
		if (FachrKrz == null) {
			if (other.FachrKrz != null)
				return false;
		} else if (!FachrKrz.equals(other.FachrKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());

		result = prime * result + ((FachrKrz == null) ? 0 : FachrKrz.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOLehrerLehramtFachrichtung(Lehrer_ID=" + this.Lehrer_ID + ", FachrKrz=" + this.FachrKrz + ", FachrAnerkennungKrz=" + this.FachrAnerkennungKrz + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
