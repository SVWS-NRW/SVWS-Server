package de.svws_nrw.db.dto.current.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Zertifikate.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Zertifikate")
@JsonPropertyOrder({"ID", "Kuerzel", "Bezeichnung", "Fach", "Formatvorlage"})
public final class DTOZertifikate {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOZertifikate e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOZertifikate e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOZertifikate e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOZertifikate e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOZertifikate e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOZertifikate e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOZertifikate e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOZertifikate e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOZertifikate e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOZertifikate e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach */
	public static final String QUERY_BY_FACH = "SELECT e FROM DTOZertifikate e WHERE e.Fach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach */
	public static final String QUERY_LIST_BY_FACH = "SELECT e FROM DTOZertifikate e WHERE e.Fach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Formatvorlage */
	public static final String QUERY_BY_FORMATVORLAGE = "SELECT e FROM DTOZertifikate e WHERE e.Formatvorlage = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Formatvorlage */
	public static final String QUERY_LIST_BY_FORMATVORLAGE = "SELECT e FROM DTOZertifikate e WHERE e.Formatvorlage IN ?1";

	/** ID des Zertifikats */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Kürzel des Zertifikats */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Bezeichnung des Zertifikats */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Fachbezeichnung für das Zertifikat */
	@Column(name = "Fach")
	@JsonProperty
	public String Fach;

	/** Formatforlage für das Zertifikat */
	@Column(name = "Formatvorlage")
	@JsonProperty
	public String Formatvorlage;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOZertifikate ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOZertifikate() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOZertifikate ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public DTOZertifikate(final long ID, final String Kuerzel) {
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOZertifikate other = (DTOZertifikate) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOZertifikate(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", Fach=" + this.Fach + ", Formatvorlage=" + this.Formatvorlage + ")";
	}

}
