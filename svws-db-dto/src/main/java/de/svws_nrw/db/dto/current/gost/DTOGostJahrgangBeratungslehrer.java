package de.svws_nrw.db.dto.current.gost;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Jahrgang_Beratungslehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostJahrgangBeratungslehrerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Jahrgang_Beratungslehrer")
@JsonPropertyOrder({"Abi_Jahrgang", "Lehrer_ID"})
public final class DTOGostJahrgangBeratungslehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostJahrgangBeratungslehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Abi_Jahrgang = ?1 AND e.Lehrer_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Abi_Jahrgang IS NOT NULL AND e.Lehrer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abi_Jahrgang */
	public static final String QUERY_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Abi_Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abi_Jahrgang */
	public static final String QUERY_LIST_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Abi_Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOGostJahrgangBeratungslehrer e WHERE e.Lehrer_ID IN ?1";

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht  */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: ID des Beratungslehrers in der Lehrertabelle */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangBeratungslehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangBeratungslehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangBeratungslehrer ohne eine Initialisierung der Attribute.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOGostJahrgangBeratungslehrer(final int Abi_Jahrgang, final long Lehrer_ID) {
		this.Abi_Jahrgang = Abi_Jahrgang;
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
		DTOGostJahrgangBeratungslehrer other = (DTOGostJahrgangBeratungslehrer) obj;
		if (Abi_Jahrgang != other.Abi_Jahrgang)
			return false;
		return Lehrer_ID == other.Lehrer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Abi_Jahrgang);

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
		return "DTOGostJahrgangBeratungslehrer(Abi_Jahrgang=" + this.Abi_Jahrgang + ", Lehrer_ID=" + this.Lehrer_ID + ")";
	}

}
