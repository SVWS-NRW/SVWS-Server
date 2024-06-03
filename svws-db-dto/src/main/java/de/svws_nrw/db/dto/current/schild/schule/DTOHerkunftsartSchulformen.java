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
 * Diese Klasse dient als DTO für die Datenbanktabelle Herkunftsart_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOHerkunftsartSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Herkunftsart_Schulformen")
@JsonPropertyOrder({"Herkunftsart_ID", "Schulform_Kuerzel", "KurzBezeichnung", "Bezeichnung"})
public final class DTOHerkunftsartSchulformen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOHerkunftsartSchulformen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.Herkunftsart_ID = ?1 AND e.Schulform_Kuerzel = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.Herkunftsart_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Herkunftsart_ID */
	public static final String QUERY_BY_HERKUNFTSART_ID = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.Herkunftsart_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Herkunftsart_ID */
	public static final String QUERY_LIST_BY_HERKUNFTSART_ID = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.Herkunftsart_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulform_Kuerzel */
	public static final String QUERY_BY_SCHULFORM_KUERZEL = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.Schulform_Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulform_Kuerzel */
	public static final String QUERY_LIST_BY_SCHULFORM_KUERZEL = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.Schulform_Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KurzBezeichnung */
	public static final String QUERY_BY_KURZBEZEICHNUNG = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.KurzBezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KurzBezeichnung */
	public static final String QUERY_LIST_BY_KURZBEZEICHNUNG = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.KurzBezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOHerkunftsartSchulformen e WHERE e.Bezeichnung IN ?1";

	/** die ID der Herkunftsart */
	@Id
	@Column(name = "Herkunftsart_ID")
	@JsonProperty
	public long Herkunftsart_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/** Eine kurze Bezeichnung der Herkunftsart */
	@Column(name = "KurzBezeichnung")
	@JsonProperty
	public String KurzBezeichnung;

	/** Die Bezeichnung der Herkunftsart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftsartSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOHerkunftsartSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOHerkunftsartSchulformen ohne eine Initialisierung der Attribute.
	 * @param Herkunftsart_ID   der Wert für das Attribut Herkunftsart_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 * @param KurzBezeichnung   der Wert für das Attribut KurzBezeichnung
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOHerkunftsartSchulformen(final long Herkunftsart_ID, final String Schulform_Kuerzel, final String KurzBezeichnung, final String Bezeichnung) {
		this.Herkunftsart_ID = Herkunftsart_ID;
		if (Schulform_Kuerzel == null) {
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
		if (KurzBezeichnung == null) {
			throw new NullPointerException("KurzBezeichnung must not be null");
		}
		this.KurzBezeichnung = KurzBezeichnung;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOHerkunftsartSchulformen other = (DTOHerkunftsartSchulformen) obj;
		if (Herkunftsart_ID != other.Herkunftsart_ID)
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
		result = prime * result + Long.hashCode(Herkunftsart_ID);

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
		return "DTOHerkunftsartSchulformen(Herkunftsart_ID=" + this.Herkunftsart_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ", KurzBezeichnung=" + this.KurzBezeichnung + ", Bezeichnung=" + this.Bezeichnung + ")";
	}

}
