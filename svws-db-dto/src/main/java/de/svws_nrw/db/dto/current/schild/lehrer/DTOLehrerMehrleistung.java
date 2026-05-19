package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerMehrleistung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerMehrleistung")
@JsonPropertyOrder({"id", "idAbschnittsdaten", "idGrund", "anzahl"})
public final class DTOLehrerMehrleistung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerMehrleistung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAbschnittsdaten */
	public static final String QUERY_BY_IDABSCHNITTSDATEN = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.idAbschnittsdaten = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAbschnittsdaten */
	public static final String QUERY_LIST_BY_IDABSCHNITTSDATEN = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.idAbschnittsdaten IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idGrund */
	public static final String QUERY_BY_IDGRUND = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.idGrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idGrund */
	public static final String QUERY_LIST_BY_IDGRUND = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.idGrund IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes anzahl */
	public static final String QUERY_BY_ANZAHL = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.anzahl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes anzahl */
	public static final String QUERY_LIST_BY_ANZAHL = "SELECT e FROM DTOLehrerMehrleistung e WHERE e.anzahl IN ?1";

	/** ID für den Eintrag für die Mehrarbeitsstunden eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long idAbschnittsdaten;

	/** Mehrarbeitsstunden Kürzel */
	@Column(name = "MehrleistungsgrundKrz")
	@JsonProperty
	public String idGrund;

	/** Anzahl Mehrarbeitsstunden */
	@Column(name = "MehrleistungStd")
	@JsonProperty
	public Double anzahl;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerMehrleistung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerMehrleistung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerMehrleistung ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idAbschnittsdaten   der Wert für das Attribut idAbschnittsdaten
	 * @param idGrund   der Wert für das Attribut idGrund
	 */
	public DTOLehrerMehrleistung(final long id, final long idAbschnittsdaten, final String idGrund) {
		this.id = id;
		this.idAbschnittsdaten = idAbschnittsdaten;
		if (idGrund == null) {
			throw new NullPointerException("idGrund must not be null");
		}
		this.idGrund = idGrund;
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
		DTOLehrerMehrleistung other = (DTOLehrerMehrleistung) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLehrerMehrleistung(id=" + this.id + ", idAbschnittsdaten=" + this.idAbschnittsdaten + ", idGrund=" + this.idGrund + ", anzahl=" + this.anzahl + ")";
	}

}
