package de.svws_nrw.db.dto.current.schild.erzieher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle ErzieherDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOErzieherDatenschutzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ErzieherDatenschutz")
@NamedQuery(name = "DTOErzieherDatenschutz.all", query = "SELECT e FROM DTOErzieherDatenschutz e")
@NamedQuery(name = "DTOErzieherDatenschutz.erzieherid", query = "SELECT e FROM DTOErzieherDatenschutz e WHERE e.ErzieherID = :value")
@NamedQuery(name = "DTOErzieherDatenschutz.erzieherid.multiple", query = "SELECT e FROM DTOErzieherDatenschutz e WHERE e.ErzieherID IN :value")
@NamedQuery(name = "DTOErzieherDatenschutz.datenschutzid", query = "SELECT e FROM DTOErzieherDatenschutz e WHERE e.DatenschutzID = :value")
@NamedQuery(name = "DTOErzieherDatenschutz.datenschutzid.multiple", query = "SELECT e FROM DTOErzieherDatenschutz e WHERE e.DatenschutzID IN :value")
@NamedQuery(name = "DTOErzieherDatenschutz.status", query = "SELECT e FROM DTOErzieherDatenschutz e WHERE e.Status = :value")
@NamedQuery(name = "DTOErzieherDatenschutz.status.multiple", query = "SELECT e FROM DTOErzieherDatenschutz e WHERE e.Status IN :value")
@NamedQuery(name = "DTOErzieherDatenschutz.primaryKeyQuery", query = "SELECT e FROM DTOErzieherDatenschutz e WHERE e.ErzieherID = ?1 AND e.DatenschutzID = ?2")
@NamedQuery(name = "DTOErzieherDatenschutz.all.migration", query = "SELECT e FROM DTOErzieherDatenschutz e WHERE e.ErzieherID IS NOT NULL AND e.DatenschutzID IS NOT NULL")
@JsonPropertyOrder({"ErzieherID", "DatenschutzID", "Status"})
public final class DTOErzieherDatenschutz {

	/** ErzieherID des Datenschutzeintrags */
	@Id
	@Column(name = "ErzieherID")
	@JsonProperty
	public long ErzieherID;

	/** DatenschutzID des Eintrags */
	@Id
	@Column(name = "DatenschutzID")
	@JsonProperty
	public long DatenschutzID;

	/** Status des Datenschutz-Eintrags (true/false) */
	@Column(name = "Status")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Status;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOErzieherDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOErzieherDatenschutz ohne eine Initialisierung der Attribute.
	 * @param ErzieherID   der Wert für das Attribut ErzieherID
	 * @param DatenschutzID   der Wert für das Attribut DatenschutzID
	 * @param Status   der Wert für das Attribut Status
	 */
	public DTOErzieherDatenschutz(final long ErzieherID, final long DatenschutzID, final Boolean Status) {
		this.ErzieherID = ErzieherID;
		this.DatenschutzID = DatenschutzID;
		this.Status = Status;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOErzieherDatenschutz other = (DTOErzieherDatenschutz) obj;
		if (ErzieherID != other.ErzieherID)
			return false;
		return DatenschutzID == other.DatenschutzID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ErzieherID);

		result = prime * result + Long.hashCode(DatenschutzID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOErzieherDatenschutz(ErzieherID=" + this.ErzieherID + ", DatenschutzID=" + this.DatenschutzID + ", Status=" + this.Status + ")";
	}

}
