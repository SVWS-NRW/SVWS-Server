package de.svws_nrw.db.dto.current.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAnkreuzfloskeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAnkreuzfloskeln")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "Floskel_ID", "Stufe1", "Stufe2", "Stufe3", "Stufe4", "Stufe5"})
public final class DTOSchuelerAnkreuzfloskeln {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Floskel_ID */
	public static final String QUERY_BY_FLOSKEL_ID = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Floskel_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Floskel_ID */
	public static final String QUERY_LIST_BY_FLOSKEL_ID = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Floskel_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe1 */
	public static final String QUERY_BY_STUFE1 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe1 */
	public static final String QUERY_LIST_BY_STUFE1 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe2 */
	public static final String QUERY_BY_STUFE2 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe2 */
	public static final String QUERY_LIST_BY_STUFE2 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe3 */
	public static final String QUERY_BY_STUFE3 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe3 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe3 */
	public static final String QUERY_LIST_BY_STUFE3 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe3 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe4 */
	public static final String QUERY_BY_STUFE4 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe4 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe4 */
	public static final String QUERY_LIST_BY_STUFE4 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe4 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stufe5 */
	public static final String QUERY_BY_STUFE5 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe5 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stufe5 */
	public static final String QUERY_LIST_BY_STUFE5 = "SELECT e FROM DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe5 IN ?1";

	/** ID des Ankreuzfloskeleintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** ID der Ankreuzfloskel aus dem Katalog */
	@Column(name = "Floskel_ID")
	@JsonProperty
	public long Floskel_ID;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe1")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe1;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe2")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe2;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe3")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe3;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe4")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe4;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe5")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe5;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerAnkreuzfloskeln() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Floskel_ID   der Wert für das Attribut Floskel_ID
	 */
	public DTOSchuelerAnkreuzfloskeln(final long ID, final long Abschnitt_ID, final long Floskel_ID) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
		this.Floskel_ID = Floskel_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerAnkreuzfloskeln other = (DTOSchuelerAnkreuzfloskeln) obj;
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
		return "DTOSchuelerAnkreuzfloskeln(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Floskel_ID=" + this.Floskel_ID + ", Stufe1=" + this.Stufe1 + ", Stufe2=" + this.Stufe2 + ", Stufe3=" + this.Stufe3 + ", Stufe4=" + this.Stufe4 + ", Stufe5=" + this.Stufe5 + ")";
	}

}
