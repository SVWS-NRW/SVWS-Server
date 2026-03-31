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
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_StundenplanErgebnisse_Unterricht_Zeitraster.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOUvStundenplanErgebnisUnterrichtZeitrasterPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_StundenplanErgebnisse_Unterricht_Zeitraster")
@JsonPropertyOrder({"Ergebnis_ID", "Unterricht_ID", "ZeitrasterEintrag_ID"})
public final class DTOUvStundenplanErgebnisUnterrichtZeitraster {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e WHERE e.Ergebnis_ID = ?1 AND e.Unterricht_ID = ?2 AND e.ZeitrasterEintrag_ID = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e WHERE e.Ergebnis_ID IS NOT NULL AND e.Unterricht_ID IS NOT NULL AND e.ZeitrasterEintrag_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ergebnis_ID */
	public static final String QUERY_BY_ERGEBNIS_ID = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e WHERE e.Ergebnis_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ergebnis_ID */
	public static final String QUERY_LIST_BY_ERGEBNIS_ID = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e WHERE e.Ergebnis_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Unterricht_ID */
	public static final String QUERY_BY_UNTERRICHT_ID = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e WHERE e.Unterricht_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Unterricht_ID */
	public static final String QUERY_LIST_BY_UNTERRICHT_ID = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e WHERE e.Unterricht_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeitrasterEintrag_ID */
	public static final String QUERY_BY_ZEITRASTEREINTRAG_ID = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e WHERE e.ZeitrasterEintrag_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeitrasterEintrag_ID */
	public static final String QUERY_LIST_BY_ZEITRASTEREINTRAG_ID = "SELECT e FROM DTOUvStundenplanErgebnisUnterrichtZeitraster e WHERE e.ZeitrasterEintrag_ID IN ?1";

	/** Fremdschlüssel auf das Stundenplanergebnis */
	@Id
	@Column(name = "Ergebnis_ID")
	@JsonProperty
	public long Ergebnis_ID;

	/** Fremdschlüssel auf den UV_Unterricht */
	@Id
	@Column(name = "Unterricht_ID")
	@JsonProperty
	public long Unterricht_ID;

	/** Fremdschlüssel auf den UV_ZeitrasterEintrag */
	@Id
	@Column(name = "ZeitrasterEintrag_ID")
	@JsonProperty
	public long ZeitrasterEintrag_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvStundenplanErgebnisUnterrichtZeitraster ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvStundenplanErgebnisUnterrichtZeitraster() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvStundenplanErgebnisUnterrichtZeitraster ohne eine Initialisierung der Attribute.
	 * @param Ergebnis_ID   der Wert für das Attribut Ergebnis_ID
	 * @param Unterricht_ID   der Wert für das Attribut Unterricht_ID
	 * @param ZeitrasterEintrag_ID   der Wert für das Attribut ZeitrasterEintrag_ID
	 */
	public DTOUvStundenplanErgebnisUnterrichtZeitraster(final long Ergebnis_ID, final long Unterricht_ID, final long ZeitrasterEintrag_ID) {
		this.Ergebnis_ID = Ergebnis_ID;
		this.Unterricht_ID = Unterricht_ID;
		this.ZeitrasterEintrag_ID = ZeitrasterEintrag_ID;
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
		DTOUvStundenplanErgebnisUnterrichtZeitraster other = (DTOUvStundenplanErgebnisUnterrichtZeitraster) obj;
		if (Ergebnis_ID != other.Ergebnis_ID) {
			return false;
		}
		if (Unterricht_ID != other.Unterricht_ID) {
			return false;
		}
		return ZeitrasterEintrag_ID == other.ZeitrasterEintrag_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Ergebnis_ID);

		result = prime * result + Long.hashCode(Unterricht_ID);

		result = prime * result + Long.hashCode(ZeitrasterEintrag_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvStundenplanErgebnisUnterrichtZeitraster(Ergebnis_ID=" + this.Ergebnis_ID + ", Unterricht_ID=" + this.Unterricht_ID + ", ZeitrasterEintrag_ID=" + this.ZeitrasterEintrag_ID + ")";
	}

}
