package de.svws_nrw.db.dto.current.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOLehrerDatenschutzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerDatenschutz")
@NamedQuery(name = "DTOLehrerDatenschutz.all", query = "SELECT e FROM DTOLehrerDatenschutz e")
@NamedQuery(name = "DTOLehrerDatenschutz.lehrerid", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.LehrerID = :value")
@NamedQuery(name = "DTOLehrerDatenschutz.lehrerid.multiple", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.LehrerID IN :value")
@NamedQuery(name = "DTOLehrerDatenschutz.datenschutzid", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.DatenschutzID = :value")
@NamedQuery(name = "DTOLehrerDatenschutz.datenschutzid.multiple", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.DatenschutzID IN :value")
@NamedQuery(name = "DTOLehrerDatenschutz.status", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.Status = :value")
@NamedQuery(name = "DTOLehrerDatenschutz.status.multiple", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.Status IN :value")
@NamedQuery(name = "DTOLehrerDatenschutz.abgefragt", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.Abgefragt = :value")
@NamedQuery(name = "DTOLehrerDatenschutz.abgefragt.multiple", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.Abgefragt IN :value")
@NamedQuery(name = "DTOLehrerDatenschutz.primaryKeyQuery", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.LehrerID = ?1 AND e.DatenschutzID = ?2")
@NamedQuery(name = "DTOLehrerDatenschutz.all.migration", query = "SELECT e FROM DTOLehrerDatenschutz e WHERE e.LehrerID IS NOT NULL AND e.DatenschutzID IS NOT NULL")
@JsonPropertyOrder({"LehrerID", "DatenschutzID", "Status", "Abgefragt"})
public final class DTOLehrerDatenschutz {

	/** LehrerID des Datenschutzeintrags */
	@Id
	@Column(name = "LehrerID")
	@JsonProperty
	public long LehrerID;

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

	/** Status der Abfrage Datenschutz-Eintrags (true/false) */
	@Column(name = "Abgefragt")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Abgefragt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerDatenschutz ohne eine Initialisierung der Attribute.
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 * @param DatenschutzID   der Wert für das Attribut DatenschutzID
	 * @param Status   der Wert für das Attribut Status
	 * @param Abgefragt   der Wert für das Attribut Abgefragt
	 */
	public DTOLehrerDatenschutz(final long LehrerID, final long DatenschutzID, final Boolean Status, final Boolean Abgefragt) {
		this.LehrerID = LehrerID;
		this.DatenschutzID = DatenschutzID;
		this.Status = Status;
		this.Abgefragt = Abgefragt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerDatenschutz other = (DTOLehrerDatenschutz) obj;
		if (LehrerID != other.LehrerID)
			return false;

		if (DatenschutzID != other.DatenschutzID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(LehrerID);

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
		return "DTOLehrerDatenschutz(LehrerID=" + this.LehrerID + ", DatenschutzID=" + this.DatenschutzID + ", Status=" + this.Status + ", Abgefragt=" + this.Abgefragt + ")";
	}

}
