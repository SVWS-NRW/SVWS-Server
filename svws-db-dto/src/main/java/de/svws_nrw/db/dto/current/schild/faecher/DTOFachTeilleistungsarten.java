package de.svws_nrw.db.dto.current.schild.faecher;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_FachTeilleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOFachTeilleistungsartenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_FachTeilleistungen")
@JsonPropertyOrder({"Teilleistungsart_ID", "Fach_ID", "Kursart"})
public final class DTOFachTeilleistungsarten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOFachTeilleistungsarten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOFachTeilleistungsarten e WHERE e.Teilleistungsart_ID = ?1 AND e.Fach_ID = ?2 AND e.Kursart = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOFachTeilleistungsarten e WHERE e.Teilleistungsart_ID IS NOT NULL AND e.Fach_ID IS NOT NULL AND e.Kursart IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Teilleistungsart_ID */
	public static final String QUERY_BY_TEILLEISTUNGSART_ID = "SELECT e FROM DTOFachTeilleistungsarten e WHERE e.Teilleistungsart_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Teilleistungsart_ID */
	public static final String QUERY_LIST_BY_TEILLEISTUNGSART_ID = "SELECT e FROM DTOFachTeilleistungsarten e WHERE e.Teilleistungsart_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOFachTeilleistungsarten e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOFachTeilleistungsarten e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursart */
	public static final String QUERY_BY_KURSART = "SELECT e FROM DTOFachTeilleistungsarten e WHERE e.Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursart */
	public static final String QUERY_LIST_BY_KURSART = "SELECT e FROM DTOFachTeilleistungsarten e WHERE e.Kursart IN ?1";

	/** Die eindeutige ID der Teilleistungsart – verweist auf die Teilleistungsart */
	@Id
	@Column(name = "Teilleistung_ID")
	@JsonProperty
	public long Teilleistungsart_ID;

	/** Die eindeutige ID des Faches – verweist auf das Fach */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Gibt an, bei welcher Kursart die Teilleistungsart zugeordnet werden soll */
	@Id
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachTeilleistungsarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFachTeilleistungsarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachTeilleistungsarten ohne eine Initialisierung der Attribute.
	 * @param Teilleistungsart_ID   der Wert für das Attribut Teilleistungsart_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOFachTeilleistungsarten(final long Teilleistungsart_ID, final long Fach_ID) {
		this.Teilleistungsart_ID = Teilleistungsart_ID;
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOFachTeilleistungsarten other = (DTOFachTeilleistungsarten) obj;
		if (Teilleistungsart_ID != other.Teilleistungsart_ID)
			return false;
		if (Fach_ID != other.Fach_ID)
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
		result = prime * result + Long.hashCode(Teilleistungsart_ID);

		result = prime * result + Long.hashCode(Fach_ID);

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
		return "DTOFachTeilleistungsarten(Teilleistungsart_ID=" + this.Teilleistungsart_ID + ", Fach_ID=" + this.Fach_ID + ", Kursart=" + this.Kursart + ")";
	}

}
