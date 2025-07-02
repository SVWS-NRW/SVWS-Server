package de.svws_nrw.db.dto.current.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerLehramtLehrbef.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOLehrerLehramtBefaehigungPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerLehramtLehrbef")
@JsonPropertyOrder({"Lehrer_ID", "LehramtKrz", "LehrbefKrz", "LehrbefAnerkennungKrz"})
public final class DTOLehrerLehramtBefaehigung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerLehramtBefaehigung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID = ?1 AND e.LehramtKrz = ?2 AND e.LehrbefKrz = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID IS NOT NULL AND e.LehramtKrz IS NOT NULL AND e.LehrbefKrz IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehramtKrz */
	public static final String QUERY_BY_LEHRAMTKRZ = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehramtKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehramtKrz */
	public static final String QUERY_LIST_BY_LEHRAMTKRZ = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehramtKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrbefKrz */
	public static final String QUERY_BY_LEHRBEFKRZ = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehrbefKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrbefKrz */
	public static final String QUERY_LIST_BY_LEHRBEFKRZ = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehrbefKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrbefAnerkennungKrz */
	public static final String QUERY_BY_LEHRBEFANERKENNUNGKRZ = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehrbefAnerkennungKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrbefAnerkennungKrz */
	public static final String QUERY_LIST_BY_LEHRBEFANERKENNUNGKRZ = "SELECT e FROM DTOLehrerLehramtBefaehigung e WHERE e.LehrbefAnerkennungKrz IN ?1";

	/** LehrerID zu der die Lehrbefähigung gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Lehramtskürzel */
	@Id
	@Column(name = "LehramtKrz")
	@JsonProperty
	public String LehramtKrz;

	/** Kürzel der Lehrbefähigung */
	@Id
	@Column(name = "LehrbefKrz")
	@JsonProperty
	public String LehrbefKrz;

	/** Kürzel der LehrbefähigungsAnerkennung */
	@Column(name = "LehrbefAnerkennungKrz")
	@JsonProperty
	public String LehrbefAnerkennungKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLehramtBefaehigung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerLehramtBefaehigung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerLehramtBefaehigung ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOLehrerLehramtBefaehigung(final long Lehrer_ID) {
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
		DTOLehrerLehramtBefaehigung other = (DTOLehrerLehramtBefaehigung) obj;
		if (Lehrer_ID != other.Lehrer_ID)
			return false;
		if (LehramtKrz == null) {
			if (other.LehramtKrz != null)
				return false;
		} else if (!LehramtKrz.equals(other.LehramtKrz))
			return false;
		if (LehrbefKrz == null) {
			if (other.LehrbefKrz != null)
				return false;
		} else if (!LehrbefKrz.equals(other.LehrbefKrz))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Lehrer_ID);

		result = prime * result + ((LehramtKrz == null) ? 0 : LehramtKrz.hashCode());

		result = prime * result + ((LehrbefKrz == null) ? 0 : LehrbefKrz.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLehrerLehramtBefaehigung(Lehrer_ID=" + this.Lehrer_ID + ", LehramtKrz=" + this.LehramtKrz + ", LehrbefKrz=" + this.LehrbefKrz + ", LehrbefAnerkennungKrz=" + this.LehrbefAnerkennungKrz + ")";
	}

}
