package de.svws_nrw.db.dto.current.schild.faecher;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle FachKatalog_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOFaecherKatalogSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "FachKatalog_Schulformen")
@JsonPropertyOrder({"Fach_ID", "Schulform_Kuerzel", "Schulgliederung_Kuerzel"})
public final class DTOFaecherKatalogSchulformen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOFaecherKatalogSchulformen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOFaecherKatalogSchulformen e WHERE e.Fach_ID = ?1 AND e.Schulform_Kuerzel = ?2 AND e.Schulgliederung_Kuerzel = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOFaecherKatalogSchulformen e WHERE e.Fach_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL AND e.Schulgliederung_Kuerzel IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOFaecherKatalogSchulformen e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOFaecherKatalogSchulformen e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulform_Kuerzel */
	public static final String QUERY_BY_SCHULFORM_KUERZEL = "SELECT e FROM DTOFaecherKatalogSchulformen e WHERE e.Schulform_Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulform_Kuerzel */
	public static final String QUERY_LIST_BY_SCHULFORM_KUERZEL = "SELECT e FROM DTOFaecherKatalogSchulformen e WHERE e.Schulform_Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulgliederung_Kuerzel */
	public static final String QUERY_BY_SCHULGLIEDERUNG_KUERZEL = "SELECT e FROM DTOFaecherKatalogSchulformen e WHERE e.Schulgliederung_Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulgliederung_Kuerzel */
	public static final String QUERY_LIST_BY_SCHULGLIEDERUNG_KUERZEL = "SELECT e FROM DTOFaecherKatalogSchulformen e WHERE e.Schulgliederung_Kuerzel IN ?1";

	/** die ID des Faches */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/** das Kürzel der Schulgliederung bzw. des Bildungsganges. Leerer String, falls alle Gliederungen der Schulform gemeint sind */
	@Id
	@Column(name = "Schulgliederung_Kuerzel")
	@JsonProperty
	public String Schulgliederung_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFaecherKatalogSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFaecherKatalogSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFaecherKatalogSchulformen ohne eine Initialisierung der Attribute.
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 * @param Schulgliederung_Kuerzel   der Wert für das Attribut Schulgliederung_Kuerzel
	 */
	public DTOFaecherKatalogSchulformen(final long Fach_ID, final String Schulform_Kuerzel, final String Schulgliederung_Kuerzel) {
		this.Fach_ID = Fach_ID;
		if (Schulform_Kuerzel == null) {
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
		if (Schulgliederung_Kuerzel == null) {
			throw new NullPointerException("Schulgliederung_Kuerzel must not be null");
		}
		this.Schulgliederung_Kuerzel = Schulgliederung_Kuerzel;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOFaecherKatalogSchulformen other = (DTOFaecherKatalogSchulformen) obj;
		if (Fach_ID != other.Fach_ID)
			return false;
		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;
		if (Schulgliederung_Kuerzel == null) {
			if (other.Schulgliederung_Kuerzel != null)
				return false;
		} else if (!Schulgliederung_Kuerzel.equals(other.Schulgliederung_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Fach_ID);

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());

		result = prime * result + ((Schulgliederung_Kuerzel == null) ? 0 : Schulgliederung_Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOFaecherKatalogSchulformen(Fach_ID=" + this.Fach_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ", Schulgliederung_Kuerzel=" + this.Schulgliederung_Kuerzel + ")";
	}

}
