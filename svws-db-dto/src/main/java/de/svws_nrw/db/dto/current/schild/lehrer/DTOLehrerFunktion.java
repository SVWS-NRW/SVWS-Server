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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerFunktionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerFunktionen")
@JsonPropertyOrder({"id", "idAbschnitt", "idFunktion"})
public final class DTOLehrerFunktion {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerFunktion e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerFunktion e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerFunktion e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerFunktion e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerFunktion e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerFunktion e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAbschnitt */
	public static final String QUERY_BY_IDABSCHNITT = "SELECT e FROM DTOLehrerFunktion e WHERE e.idAbschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAbschnitt */
	public static final String QUERY_LIST_BY_IDABSCHNITT = "SELECT e FROM DTOLehrerFunktion e WHERE e.idAbschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idFunktion */
	public static final String QUERY_BY_IDFUNKTION = "SELECT e FROM DTOLehrerFunktion e WHERE e.idFunktion = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idFunktion */
	public static final String QUERY_LIST_BY_IDFUNKTION = "SELECT e FROM DTOLehrerFunktion e WHERE e.idFunktion IN ?1";

	/** ID für den Eintrag für die schulinterne Funktion eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long idAbschnitt;

	/** ID der schulinternen Funktion */
	@Column(name = "Funktion_ID")
	@JsonProperty
	public long idFunktion;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerFunktion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerFunktion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerFunktion ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idAbschnitt   der Wert für das Attribut idAbschnitt
	 * @param idFunktion   der Wert für das Attribut idFunktion
	 */
	public DTOLehrerFunktion(final long id, final long idAbschnitt, final long idFunktion) {
		this.id = id;
		this.idAbschnitt = idAbschnitt;
		this.idFunktion = idFunktion;
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
		DTOLehrerFunktion other = (DTOLehrerFunktion) obj;
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
		return "DTOLehrerFunktion(id=" + this.id + ", idAbschnitt=" + this.idAbschnitt + ", idFunktion=" + this.idFunktion + ")";
	}

}
