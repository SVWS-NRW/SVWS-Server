package de.svws_nrw.db.dto.current.schild.kurse;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle KursLehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOKursLehrerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KursLehrer")
@JsonPropertyOrder({"Kurs_ID", "Lehrer_ID", "Anteil"})
public final class DTOKursLehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKursLehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKursLehrer e WHERE e.Kurs_ID = ?1 AND e.Lehrer_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKursLehrer e WHERE e.Kurs_ID IS NOT NULL AND e.Lehrer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurs_ID */
	public static final String QUERY_BY_KURS_ID = "SELECT e FROM DTOKursLehrer e WHERE e.Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurs_ID */
	public static final String QUERY_LIST_BY_KURS_ID = "SELECT e FROM DTOKursLehrer e WHERE e.Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOKursLehrer e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOKursLehrer e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Anteil */
	public static final String QUERY_BY_ANTEIL = "SELECT e FROM DTOKursLehrer e WHERE e.Anteil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Anteil */
	public static final String QUERY_LIST_BY_ANTEIL = "SELECT e FROM DTOKursLehrer e WHERE e.Anteil IN ?1";

	/** ID des Kurses zu denen der Lehrer gehört */
	@Id
	@Column(name = "Kurs_ID")
	@JsonProperty
	public long Kurs_ID;

	/** ID des Lehrers */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Wochenstunden für die Zusatzkraft */
	@Column(name = "Anteil")
	@JsonProperty
	public Double Anteil;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursLehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKursLehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursLehrer ohne eine Initialisierung der Attribute.
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOKursLehrer(final long Kurs_ID, final long Lehrer_ID) {
		this.Kurs_ID = Kurs_ID;
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
		DTOKursLehrer other = (DTOKursLehrer) obj;
		if (Kurs_ID != other.Kurs_ID)
			return false;
		return Lehrer_ID == other.Lehrer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Kurs_ID);

		result = prime * result + Long.hashCode(Lehrer_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOKursLehrer(Kurs_ID=" + this.Kurs_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Anteil=" + this.Anteil + ")";
	}

}
