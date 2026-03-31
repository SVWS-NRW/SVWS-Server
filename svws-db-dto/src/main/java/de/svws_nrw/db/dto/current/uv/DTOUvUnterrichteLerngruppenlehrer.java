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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Unterrichte_Lerngruppenlehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOUvUnterrichteLerngruppenlehrerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Unterrichte_Lerngruppenlehrer")
@JsonPropertyOrder({"Unterricht_ID", "LerngruppenLehrer_ID", "Planungsabschnitt_ID"})
public final class DTOUvUnterrichteLerngruppenlehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e WHERE e.Unterricht_ID = ?1 AND e.LerngruppenLehrer_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e WHERE e.Unterricht_ID IS NOT NULL AND e.LerngruppenLehrer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Unterricht_ID */
	public static final String QUERY_BY_UNTERRICHT_ID = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e WHERE e.Unterricht_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Unterricht_ID */
	public static final String QUERY_LIST_BY_UNTERRICHT_ID = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e WHERE e.Unterricht_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LerngruppenLehrer_ID */
	public static final String QUERY_BY_LERNGRUPPENLEHRER_ID = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e WHERE e.LerngruppenLehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LerngruppenLehrer_ID */
	public static final String QUERY_LIST_BY_LERNGRUPPENLEHRER_ID = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e WHERE e.LerngruppenLehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvUnterrichteLerngruppenlehrer e WHERE e.Planungsabschnitt_ID IN ?1";

	/** ID des UV_Unterrichts */
	@Id
	@Column(name = "Unterricht_ID")
	@JsonProperty
	public long Unterricht_ID;

	/** ID des Lehrers, welcher der Lerngruppe zugeordnet ist */
	@Id
	@Column(name = "LerngruppenLehrer_ID")
	@JsonProperty
	public long LerngruppenLehrer_ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvUnterrichteLerngruppenlehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvUnterrichteLerngruppenlehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvUnterrichteLerngruppenlehrer ohne eine Initialisierung der Attribute.
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param LerngruppenLehrer_ID   der Wert für das Attribut LerngruppenLehrer_ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 */
	public DTOUvUnterrichteLerngruppenlehrer(final long Unterricht_ID, final long LerngruppenLehrer_ID, final long Planungsabschnitt_ID) {
		this.Unterricht_ID = Unterricht_ID;
		this.LerngruppenLehrer_ID = LerngruppenLehrer_ID;
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
		DTOUvUnterrichteLerngruppenlehrer other = (DTOUvUnterrichteLerngruppenlehrer) obj;
		if (Unterricht_ID != other.Unterricht_ID) {
			return false;
		}
		return LerngruppenLehrer_ID == other.LerngruppenLehrer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Unterricht_ID);

		result = prime * result + Long.hashCode(LerngruppenLehrer_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvUnterrichteLerngruppenlehrer(Unterricht_ID=" + this.Unterricht_ID + ", LerngruppenLehrer_ID=" + this.LerngruppenLehrer_ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ")";
	}

}
