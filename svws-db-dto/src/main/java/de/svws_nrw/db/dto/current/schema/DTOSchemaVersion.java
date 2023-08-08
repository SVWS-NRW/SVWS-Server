package de.svws_nrw.db.dto.current.schema;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schema_Version.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schema_Version")
@NamedQuery(name = "DTOSchemaVersion.all", query = "SELECT e FROM DTOSchemaVersion e")
@NamedQuery(name = "DTOSchemaVersion.revision", query = "SELECT e FROM DTOSchemaVersion e WHERE e.Revision = :value")
@NamedQuery(name = "DTOSchemaVersion.revision.multiple", query = "SELECT e FROM DTOSchemaVersion e WHERE e.Revision IN :value")
@NamedQuery(name = "DTOSchemaVersion.istainted", query = "SELECT e FROM DTOSchemaVersion e WHERE e.IsTainted = :value")
@NamedQuery(name = "DTOSchemaVersion.istainted.multiple", query = "SELECT e FROM DTOSchemaVersion e WHERE e.IsTainted IN :value")
@NamedQuery(name = "DTOSchemaVersion.primaryKeyQuery", query = "SELECT e FROM DTOSchemaVersion e WHERE e.Revision = ?1")
@NamedQuery(name = "DTOSchemaVersion.all.migration", query = "SELECT e FROM DTOSchemaVersion e WHERE e.Revision IS NOT NULL")
@JsonPropertyOrder({"Revision", "IsTainted"})
public final class DTOSchemaVersion {

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
	 * Erstellt ein neues Objekt der Klasse DTOSchemaVersion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchemaVersion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchemaVersion ohne eine Initialisierung der Attribute.
	 * @param Revision   der Wert für das Attribut Revision
	 * @param IsTainted   der Wert für das Attribut IsTainted
	 */
	public DTOSchemaVersion(final long Revision, final Boolean IsTainted) {
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
		DTOSchemaVersion other = (DTOSchemaVersion) obj;
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
		return "DTOSchemaVersion(Revision=" + this.Revision + ", IsTainted=" + this.IsTainted + ")";
	}

}
