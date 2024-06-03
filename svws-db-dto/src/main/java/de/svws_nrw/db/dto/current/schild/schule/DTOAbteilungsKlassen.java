package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Abt_Kl.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Abt_Kl")
@JsonPropertyOrder({"ID", "Abteilung_ID", "Klassen_ID", "Sichtbar"})
public final class DTOAbteilungsKlassen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOAbteilungsKlassen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abteilung_ID */
	public static final String QUERY_BY_ABTEILUNG_ID = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Abteilung_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abteilung_ID */
	public static final String QUERY_LIST_BY_ABTEILUNG_ID = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Abteilung_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klassen_ID */
	public static final String QUERY_BY_KLASSEN_ID = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Klassen_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klassen_ID */
	public static final String QUERY_LIST_BY_KLASSEN_ID = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Klassen_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Sichtbar IN ?1";

	/** ID der Klassenzugehörigkeit zu einer Abteilung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Abteilung in der übergeordneten Tabelle */
	@Column(name = "Abteilung_ID")
	@JsonProperty
	public long Abteilung_ID;

	/** ID der Klasse die zur Abteilung gehört */
	@Column(name = "Klassen_ID")
	@JsonProperty
	public long Klassen_ID;

	/** steuert die Sichtbarkeit der Klasse zur Abteilung */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAbteilungsKlassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abteilung_ID   der Wert für das Attribut Abteilung_ID
	 * @param Klassen_ID   der Wert für das Attribut Klassen_ID
	 */
	public DTOAbteilungsKlassen(final long ID, final long Abteilung_ID, final long Klassen_ID) {
		this.ID = ID;
		this.Abteilung_ID = Abteilung_ID;
		this.Klassen_ID = Klassen_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAbteilungsKlassen other = (DTOAbteilungsKlassen) obj;
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
		return "DTOAbteilungsKlassen(ID=" + this.ID + ", Abteilung_ID=" + this.Abteilung_ID + ", Klassen_ID=" + this.Klassen_ID + ", Sichtbar=" + this.Sichtbar + ")";
	}

}
