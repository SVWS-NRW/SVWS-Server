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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Unterrichte_Raeume.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOUvUnterrichtRaumPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Unterrichte_Raeume")
@JsonPropertyOrder({"Unterricht_ID", "Raum_ID", "Planungsabschnitt_ID"})
public final class DTOUvUnterrichtRaum {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvUnterrichtRaum e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvUnterrichtRaum e WHERE e.Unterricht_ID = ?1 AND e.Raum_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvUnterrichtRaum e WHERE e.Unterricht_ID IS NOT NULL AND e.Raum_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Unterricht_ID */
	public static final String QUERY_BY_UNTERRICHT_ID = "SELECT e FROM DTOUvUnterrichtRaum e WHERE e.Unterricht_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Unterricht_ID */
	public static final String QUERY_LIST_BY_UNTERRICHT_ID = "SELECT e FROM DTOUvUnterrichtRaum e WHERE e.Unterricht_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Raum_ID */
	public static final String QUERY_BY_RAUM_ID = "SELECT e FROM DTOUvUnterrichtRaum e WHERE e.Raum_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Raum_ID */
	public static final String QUERY_LIST_BY_RAUM_ID = "SELECT e FROM DTOUvUnterrichtRaum e WHERE e.Raum_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvUnterrichtRaum e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvUnterrichtRaum e WHERE e.Planungsabschnitt_ID IN ?1";

	/** ID des UV_Unterrichts */
	@Id
	@Column(name = "Unterricht_ID")
	@JsonProperty
	public long Unterricht_ID;

	/** Fremdschlüssel auf den Raum (Tabelle UV_Raeume) */
	@Id
	@Column(name = "Raum_ID")
	@JsonProperty
	public long Raum_ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvUnterrichtRaum ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvUnterrichtRaum() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvUnterrichtRaum ohne eine Initialisierung der Attribute.
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param Raum_ID   der Wert für das Attribut Raum_ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 */
	public DTOUvUnterrichtRaum(final long Unterricht_ID, final long Raum_ID, final long Planungsabschnitt_ID) {
		this.Unterricht_ID = Unterricht_ID;
		this.Raum_ID = Raum_ID;
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
		DTOUvUnterrichtRaum other = (DTOUvUnterrichtRaum) obj;
		if (Unterricht_ID != other.Unterricht_ID) {
			return false;
		}
		return Raum_ID == other.Raum_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Unterricht_ID);

		result = prime * result + Long.hashCode(Raum_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvUnterrichtRaum(Unterricht_ID=" + this.Unterricht_ID + ", Raum_ID=" + this.Raum_ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ")";
	}

}
