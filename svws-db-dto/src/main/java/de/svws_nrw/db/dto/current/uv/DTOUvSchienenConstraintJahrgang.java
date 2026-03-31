package de.svws_nrw.db.dto.current.uv;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Schienen_Constraint_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOUvSchienenConstraintJahrgangPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Schienen_Constraint_Jahrgaenge")
@JsonPropertyOrder({"Schiene_ID", "Jahrgang_ID", "Planungsabschnitt_ID"})
public final class DTOUvSchienenConstraintJahrgang {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvSchienenConstraintJahrgang e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvSchienenConstraintJahrgang e WHERE e.Schiene_ID = ?1 AND e.Jahrgang_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvSchienenConstraintJahrgang e WHERE e.Schiene_ID IS NOT NULL AND e.Jahrgang_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schiene_ID */
	public static final String QUERY_BY_SCHIENE_ID = "SELECT e FROM DTOUvSchienenConstraintJahrgang e WHERE e.Schiene_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schiene_ID */
	public static final String QUERY_LIST_BY_SCHIENE_ID = "SELECT e FROM DTOUvSchienenConstraintJahrgang e WHERE e.Schiene_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM DTOUvSchienenConstraintJahrgang e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM DTOUvSchienenConstraintJahrgang e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvSchienenConstraintJahrgang e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvSchienenConstraintJahrgang e WHERE e.Planungsabschnitt_ID IN ?1";

	/** ID der Schiene */
	@Id
	@Column(name = "Schiene_ID")
	@JsonProperty
	public long Schiene_ID;

	/** ID des zugeordneten Jahrgangs */
	@Id
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public long Jahrgang_ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchienenConstraintJahrgang ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvSchienenConstraintJahrgang() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchienenConstraintJahrgang ohne eine Initialisierung der Attribute.
	 * @param Schiene_ID   der Wert für das Attribut Schiene_ID
	 * @param Jahrgang_ID   der Wert für das Attribut Jahrgang_ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 */
	public DTOUvSchienenConstraintJahrgang(final long Schiene_ID, final long Jahrgang_ID, final long Planungsabschnitt_ID) {
		this.Schiene_ID = Schiene_ID;
		this.Jahrgang_ID = Jahrgang_ID;
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
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
		DTOUvSchienenConstraintJahrgang other = (DTOUvSchienenConstraintJahrgang) obj;
		if (Schiene_ID != other.Schiene_ID) {
			return false;
		}
		return Jahrgang_ID == other.Jahrgang_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schiene_ID);

		result = prime * result + Long.hashCode(Jahrgang_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvSchienenConstraintJahrgang(Schiene_ID=" + this.Schiene_ID + ", Jahrgang_ID=" + this.Jahrgang_ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ")";
	}

}
