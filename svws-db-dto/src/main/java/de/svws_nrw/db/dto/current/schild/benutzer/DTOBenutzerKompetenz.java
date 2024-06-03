package de.svws_nrw.db.dto.current.schild.benutzer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzerKompetenzen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOBenutzerKompetenzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzerKompetenzen")
@JsonPropertyOrder({"Benutzer_ID", "Kompetenz_ID"})
public final class DTOBenutzerKompetenz {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOBenutzerKompetenz e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOBenutzerKompetenz e WHERE e.Benutzer_ID = ?1 AND e.Kompetenz_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOBenutzerKompetenz e WHERE e.Benutzer_ID IS NOT NULL AND e.Kompetenz_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzer_ID */
	public static final String QUERY_BY_BENUTZER_ID = "SELECT e FROM DTOBenutzerKompetenz e WHERE e.Benutzer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzer_ID */
	public static final String QUERY_LIST_BY_BENUTZER_ID = "SELECT e FROM DTOBenutzerKompetenz e WHERE e.Benutzer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kompetenz_ID */
	public static final String QUERY_BY_KOMPETENZ_ID = "SELECT e FROM DTOBenutzerKompetenz e WHERE e.Kompetenz_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kompetenz_ID */
	public static final String QUERY_LIST_BY_KOMPETENZ_ID = "SELECT e FROM DTOBenutzerKompetenz e WHERE e.Kompetenz_ID IN ?1";

	/** Die ID des Benutzers */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public long Benutzer_ID;

	/** Die ID der zugeordneten Kompetenz */
	@Id
	@Column(name = "Kompetenz_ID")
	@JsonProperty
	public long Kompetenz_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerKompetenz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzerKompetenz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerKompetenz ohne eine Initialisierung der Attribute.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param Kompetenz_ID   der Wert für das Attribut Kompetenz_ID
	 */
	public DTOBenutzerKompetenz(final long Benutzer_ID, final long Kompetenz_ID) {
		this.Benutzer_ID = Benutzer_ID;
		this.Kompetenz_ID = Kompetenz_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBenutzerKompetenz other = (DTOBenutzerKompetenz) obj;
		if (Benutzer_ID != other.Benutzer_ID)
			return false;
		return Kompetenz_ID == other.Kompetenz_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Benutzer_ID);

		result = prime * result + Long.hashCode(Kompetenz_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOBenutzerKompetenz(Benutzer_ID=" + this.Benutzer_ID + ", Kompetenz_ID=" + this.Kompetenz_ID + ")";
	}

}
