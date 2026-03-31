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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Schuelergruppen_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOUvSchuelergruppeSchuelerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Schuelergruppen_Schueler")
@JsonPropertyOrder({"Schuelergruppe_ID", "Schueler_ID", "Planungsabschnitt_ID"})
public final class DTOUvSchuelergruppeSchueler {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvSchuelergruppeSchueler e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvSchuelergruppeSchueler e WHERE e.Schuelergruppe_ID = ?1 AND e.Schueler_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvSchuelergruppeSchueler e WHERE e.Schuelergruppe_ID IS NOT NULL AND e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuelergruppe_ID */
	public static final String QUERY_BY_SCHUELERGRUPPE_ID = "SELECT e FROM DTOUvSchuelergruppeSchueler e WHERE e.Schuelergruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuelergruppe_ID */
	public static final String QUERY_LIST_BY_SCHUELERGRUPPE_ID = "SELECT e FROM DTOUvSchuelergruppeSchueler e WHERE e.Schuelergruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOUvSchuelergruppeSchueler e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOUvSchuelergruppeSchueler e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvSchuelergruppeSchueler e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvSchuelergruppeSchueler e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die ID der Schülergruppe als Fremdschlüssel auf die Tabelle UV_Schuelergruppen */
	@Id
	@Column(name = "Schuelergruppe_ID")
	@JsonProperty
	public long Schuelergruppe_ID;

	/** Die ID des Schülers als Fremdschlüssel auf die Tabelle Schueler */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchuelergruppeSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvSchuelergruppeSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvSchuelergruppeSchueler ohne eine Initialisierung der Attribute.
	 * @param Schuelergruppe_ID   der Wert für das Attribut Schuelergruppe_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 */
	public DTOUvSchuelergruppeSchueler(final long Schuelergruppe_ID, final long Schueler_ID, final long Planungsabschnitt_ID) {
		this.Schuelergruppe_ID = Schuelergruppe_ID;
		this.Schueler_ID = Schueler_ID;
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
		DTOUvSchuelergruppeSchueler other = (DTOUvSchuelergruppeSchueler) obj;
		if (Schuelergruppe_ID != other.Schuelergruppe_ID) {
			return false;
		}
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schuelergruppe_ID);

		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvSchuelergruppeSchueler(Schuelergruppe_ID=" + this.Schuelergruppe_ID + ", Schueler_ID=" + this.Schueler_ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ")";
	}

}
