package de.svws_nrw.db.dto.migration.schild.faecher;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_FachTeilleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOFachTeilleistungsartenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_FachTeilleistungen")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.all", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.teilleistungsart_id", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.Teilleistungsart_ID = :value")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.teilleistungsart_id.multiple", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.Teilleistungsart_ID IN :value")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.fach_id", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.Fach_ID = :value")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.fach_id.multiple", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.Fach_ID IN :value")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.kursart", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.Kursart = :value")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.kursart.multiple", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.Kursart IN :value")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.schulnreigner", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.schulnreigner.multiple", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.primaryKeyQuery", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.Teilleistungsart_ID = ?1 AND e.Fach_ID = ?2 AND e.Kursart = ?3")
@NamedQuery(name="MigrationDTOFachTeilleistungsarten.all.migration", query="SELECT e FROM MigrationDTOFachTeilleistungsarten e WHERE e.Teilleistungsart_ID IS NOT NULL AND e.Fach_ID IS NOT NULL AND e.Kursart IS NOT NULL")
@JsonPropertyOrder({"Teilleistungsart_ID","Fach_ID","Kursart","SchulnrEigner"})
public class MigrationDTOFachTeilleistungsarten {

	/** Die eindeutige ID der Teilleistungsart – verweist auf die Teilleistungsart */
	@Id
	@Column(name = "Teilleistung_ID")
	@JsonProperty
	public Long Teilleistungsart_ID;

	/** Die eindeutige ID des Faches – verweist auf das Fach */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Gibt an, bei welcher Kursart die Teilleistungsart zugeordnet werden soll */
	@Id
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachTeilleistungsarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFachTeilleistungsarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachTeilleistungsarten ohne eine Initialisierung der Attribute.
	 * @param Teilleistungsart_ID   der Wert für das Attribut Teilleistungsart_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOFachTeilleistungsarten(final Long Teilleistungsart_ID, final Long Fach_ID, final Integer SchulnrEigner) {
		if (Teilleistungsart_ID == null) { 
			throw new NullPointerException("Teilleistungsart_ID must not be null");
		}
		this.Teilleistungsart_ID = Teilleistungsart_ID;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
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
		MigrationDTOFachTeilleistungsarten other = (MigrationDTOFachTeilleistungsarten) obj;
		if (Teilleistungsart_ID == null) {
			if (other.Teilleistungsart_ID != null)
				return false;
		} else if (!Teilleistungsart_ID.equals(other.Teilleistungsart_ID))
			return false;

		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;

		if (Kursart == null) {
			if (other.Kursart != null)
				return false;
		} else if (!Kursart.equals(other.Kursart))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Teilleistungsart_ID == null) ? 0 : Teilleistungsart_ID.hashCode());

		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());

		result = prime * result + ((Kursart == null) ? 0 : Kursart.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOFachTeilleistungsarten(Teilleistungsart_ID=" + this.Teilleistungsart_ID + ", Fach_ID=" + this.Fach_ID + ", Kursart=" + this.Kursart + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}