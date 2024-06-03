package de.svws_nrw.db.dto.current.schema;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schema_Status.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schema_Status")
@JsonPropertyOrder({"Revision", "IsTainted"})
public final class DTOSchemaStatus {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchemaStatus e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchemaStatus e WHERE e.Revision = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchemaStatus e WHERE e.Revision IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchemaStatus e WHERE e.Revision IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Revision */
	public static final String QUERY_BY_REVISION = "SELECT e FROM DTOSchemaStatus e WHERE e.Revision = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Revision */
	public static final String QUERY_LIST_BY_REVISION = "SELECT e FROM DTOSchemaStatus e WHERE e.Revision IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IsTainted */
	public static final String QUERY_BY_ISTAINTED = "SELECT e FROM DTOSchemaStatus e WHERE e.IsTainted = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IsTainted */
	public static final String QUERY_LIST_BY_ISTAINTED = "SELECT e FROM DTOSchemaStatus e WHERE e.IsTainted IN ?1";

	/** Die Revision des Datenbankschemas der SVWS-DB */
	@Id
	@Column(name = "Revision")
	@JsonProperty
	public long Revision;

	/** Gibt an, ob die Datenbank noch für einen Produktivbetrieb zugelassen ist oder durch ein Update auf eine Entwicklerversion eventuell in einem ungültigen Zustand ist */
	@Column(name = "IsTainted")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IsTainted;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchemaStatus ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchemaStatus() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchemaStatus ohne eine Initialisierung der Attribute.
	 * @param Revision   der Wert für das Attribut Revision
	 * @param IsTainted   der Wert für das Attribut IsTainted
	 */
	public DTOSchemaStatus(final long Revision, final Boolean IsTainted) {
		this.Revision = Revision;
		this.IsTainted = IsTainted;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchemaStatus other = (DTOSchemaStatus) obj;
		return Revision == other.Revision;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Revision);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchemaStatus(Revision=" + this.Revision + ", IsTainted=" + this.IsTainted + ")";
	}

}
