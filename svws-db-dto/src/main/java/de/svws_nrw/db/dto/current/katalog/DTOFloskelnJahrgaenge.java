package de.svws_nrw.db.dto.current.katalog;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Katalog_Floskeln_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Katalog_Floskeln_Jahrgaenge")
@JsonPropertyOrder({"ID", "Floskel_ID", "Jahrgang_ID"})
public final class DTOFloskelnJahrgaenge {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOFloskelnJahrgaenge e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Floskel_ID */
	public static final String QUERY_BY_FLOSKEL_ID = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.Floskel_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Floskel_ID */
	public static final String QUERY_LIST_BY_FLOSKEL_ID = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.Floskel_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM DTOFloskelnJahrgaenge e WHERE e.Jahrgang_ID IN ?1";

	/** ID der Floskel-Jahrgangszuordnung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID der Floskel */
	@Column(name = "Floskel_ID")
	@JsonProperty
	public long Floskel_ID;

	/** Die ID des Jahrgangs */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public long Jahrgang_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFloskelnJahrgaenge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFloskelnJahrgaenge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFloskelnJahrgaenge ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Floskel_ID   der Wert für das Attribut Floskel_ID
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 */
	public DTOFloskelnJahrgaenge(final long ID, final long Floskel_ID, final long Jahrgang_ID) {
		this.ID = ID;
		this.Floskel_ID = Floskel_ID;
		this.Jahrgang_ID = Jahrgang_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOFloskelnJahrgaenge other = (DTOFloskelnJahrgaenge) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOFloskelnJahrgaenge(ID=" + this.ID + ", Floskel_ID=" + this.Floskel_ID + ", Jahrgang_ID=" + this.Jahrgang_ID + ")";
	}

}
