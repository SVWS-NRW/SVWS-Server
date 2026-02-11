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
 * Diese Klasse dient als DTO für die Datenbanktabelle Ankreuzkompetenz_Jahrgang.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Ankreuzkompetenz_Jahrgang")
@JsonPropertyOrder({"id", "idAnkreuzkompetenz", "idJahrgang"})
public final class DTOAnkreuzkompetenzJahrgang {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAnkreuzkompetenz */
	public static final String QUERY_BY_IDANKREUZKOMPETENZ = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.idAnkreuzkompetenz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAnkreuzkompetenz */
	public static final String QUERY_LIST_BY_IDANKREUZKOMPETENZ = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.idAnkreuzkompetenz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idJahrgang */
	public static final String QUERY_BY_IDJAHRGANG = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.idJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idJahrgang */
	public static final String QUERY_LIST_BY_IDJAHRGANG = "SELECT e FROM DTOAnkreuzkompetenzJahrgang e WHERE e.idJahrgang IN ?1";

	/** ID der Ankreuzkompetenz-Jahrgangszuordnung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** Die ID der Ankreuzkompetenz */
	@Column(name = "ID_Ankreuzkompetenz")
	@JsonProperty
	public long idAnkreuzkompetenz;

	/** Die ID des Jahrgangs */
	@Column(name = "ID_Jahrgang")
	@JsonProperty
	public long idJahrgang;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAnkreuzkompetenzJahrgang ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAnkreuzkompetenzJahrgang() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAnkreuzkompetenzJahrgang ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idAnkreuzkompetenz   der Wert für das Attribut idAnkreuzkompetenz
	 * @param idJahrgang   der Wert für das Attribut idJahrgang
	 */
	public DTOAnkreuzkompetenzJahrgang(final long id, final long idAnkreuzkompetenz, final long idJahrgang) {
		this.id = id;
		this.idAnkreuzkompetenz = idAnkreuzkompetenz;
		this.idJahrgang = idJahrgang;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAnkreuzkompetenzJahrgang other = (DTOAnkreuzkompetenzJahrgang) obj;
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
		return "DTOAnkreuzkompetenzJahrgang(id=" + this.id + ", idAnkreuzkompetenz=" + this.idAnkreuzkompetenz + ", idJahrgang=" + this.idJahrgang + ")";
	}

}
