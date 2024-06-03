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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Merkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Merkmale")
@JsonPropertyOrder({"ID", "Schule", "Schueler", "Kurztext", "Langtext"})
public final class DTOMerkmale {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOMerkmale e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOMerkmale e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOMerkmale e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOMerkmale e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOMerkmale e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOMerkmale e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schule */
	public static final String QUERY_BY_SCHULE = "SELECT e FROM DTOMerkmale e WHERE e.Schule = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schule */
	public static final String QUERY_LIST_BY_SCHULE = "SELECT e FROM DTOMerkmale e WHERE e.Schule IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler */
	public static final String QUERY_BY_SCHUELER = "SELECT e FROM DTOMerkmale e WHERE e.Schueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler */
	public static final String QUERY_LIST_BY_SCHUELER = "SELECT e FROM DTOMerkmale e WHERE e.Schueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurztext */
	public static final String QUERY_BY_KURZTEXT = "SELECT e FROM DTOMerkmale e WHERE e.Kurztext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurztext */
	public static final String QUERY_LIST_BY_KURZTEXT = "SELECT e FROM DTOMerkmale e WHERE e.Kurztext IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Langtext */
	public static final String QUERY_BY_LANGTEXT = "SELECT e FROM DTOMerkmale e WHERE e.Langtext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Langtext */
	public static final String QUERY_LIST_BY_LANGTEXT = "SELECT e FROM DTOMerkmale e WHERE e.Langtext IN ?1";

	/** ID des Merkmals das an der Schule vorhanden ist */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Merkmal kann der Schule zugewiesen werden */
	@Column(name = "Schule")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Schule;

	/** Merkmal kann auch einem einzelnen Schüler auf Individualdaten II zugewiesen werden */
	@Column(name = "Schueler")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Schueler;

	/** Kurztext des Merkmals zB OGS */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Langtext des Merkmal zB offener Ganztag */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOMerkmale ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOMerkmale(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOMerkmale other = (DTOMerkmale) obj;
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
		return "DTOMerkmale(ID=" + this.ID + ", Schule=" + this.Schule + ", Schueler=" + this.Schueler + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ")";
	}

}
