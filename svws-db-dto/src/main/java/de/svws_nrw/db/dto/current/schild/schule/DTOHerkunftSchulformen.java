package de.svws_nrw.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunft_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOHerkunftSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunft_Schulformen")
@JsonPropertyOrder({"Herkunft_ID", "Schulform_Kuerzel"})
public final class DTOHerkunftSchulformen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOHerkunftSchulformen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOHerkunftSchulformen e WHERE e.Herkunft_ID = ?1 AND e.Schulform_Kuerzel = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOHerkunftSchulformen e WHERE e.Herkunft_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Herkunft_ID */
	public static final String QUERY_BY_HERKUNFT_ID = "SELECT e FROM DTOHerkunftSchulformen e WHERE e.Herkunft_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Herkunft_ID */
	public static final String QUERY_LIST_BY_HERKUNFT_ID = "SELECT e FROM DTOHerkunftSchulformen e WHERE e.Herkunft_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulform_Kuerzel */
	public static final String QUERY_BY_SCHULFORM_KUERZEL = "SELECT e FROM DTOHerkunftSchulformen e WHERE e.Schulform_Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulform_Kuerzel */
	public static final String QUERY_LIST_BY_SCHULFORM_KUERZEL = "SELECT e FROM DTOHerkunftSchulformen e WHERE e.Schulform_Kuerzel IN ?1";

	/** die ID der Herkunft */
	@Id
	@Column(name = "Herkunft_ID")
	@JsonProperty
	public long Herkunft_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOHerkunftSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftSchulformen ohne eine Initialisierung der Attribute.
	 * @param Herkunft_ID   der Wert für das Attribut Herkunft_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public DTOHerkunftSchulformen(final long Herkunft_ID, final String Schulform_Kuerzel) {
		this.Herkunft_ID = Herkunft_ID;
		if (Schulform_Kuerzel == null) {
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOHerkunftSchulformen other = (DTOHerkunftSchulformen) obj;
		if (Herkunft_ID != other.Herkunft_ID)
			return false;
		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Herkunft_ID);

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOHerkunftSchulformen(Herkunft_ID=" + this.Herkunft_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ")";
	}

}
