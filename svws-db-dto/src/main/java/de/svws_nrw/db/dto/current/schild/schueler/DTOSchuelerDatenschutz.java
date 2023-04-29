package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOSchuelerDatenschutzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerDatenschutz")
@NamedQuery(name = "DTOSchuelerDatenschutz.all", query = "SELECT e FROM DTOSchuelerDatenschutz e")
@NamedQuery(name = "DTOSchuelerDatenschutz.schueler_id", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerDatenschutz.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerDatenschutz.datenschutz_id", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Datenschutz_ID = :value")
@NamedQuery(name = "DTOSchuelerDatenschutz.datenschutz_id.multiple", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Datenschutz_ID IN :value")
@NamedQuery(name = "DTOSchuelerDatenschutz.status", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Status = :value")
@NamedQuery(name = "DTOSchuelerDatenschutz.status.multiple", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Status IN :value")
@NamedQuery(name = "DTOSchuelerDatenschutz.abgefragt", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Abgefragt = :value")
@NamedQuery(name = "DTOSchuelerDatenschutz.abgefragt.multiple", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Abgefragt IN :value")
@NamedQuery(name = "DTOSchuelerDatenschutz.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Schueler_ID = ?1 AND e.Datenschutz_ID = ?2")
@NamedQuery(name = "DTOSchuelerDatenschutz.all.migration", query = "SELECT e FROM DTOSchuelerDatenschutz e WHERE e.Schueler_ID IS NOT NULL AND e.Datenschutz_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID", "Datenschutz_ID", "Status", "Abgefragt"})
public final class DTOSchuelerDatenschutz {

	/** Fremdschlüssel auf Tabelle Schueler */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Fremdschlüssel auf den Katalog der DSGVO-Merkmale */
	@Id
	@Column(name = "Datenschutz_ID")
	@JsonProperty
	public long Datenschutz_ID;

	/** Gibt an ob eine Zustimmung zum Merkmal vorliegt. */
	@Column(name = "Status")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Status;

	/** Status der Abfrage Datenschutz-Eintrags (true/false) */
	@Column(name = "Abgefragt")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Abgefragt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerDatenschutz ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Datenschutz_ID   der Wert für das Attribut Datenschutz_ID
	 * @param Status   der Wert für das Attribut Status
	 * @param Abgefragt   der Wert für das Attribut Abgefragt
	 */
	public DTOSchuelerDatenschutz(final long Schueler_ID, final long Datenschutz_ID, final Boolean Status, final Boolean Abgefragt) {
		this.Schueler_ID = Schueler_ID;
		this.Datenschutz_ID = Datenschutz_ID;
		if (Status == null) {
			throw new NullPointerException("Status must not be null");
		}
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
		DTOSchuelerDatenschutz other = (DTOSchuelerDatenschutz) obj;
		if (Schueler_ID != other.Schueler_ID)
			return false;

		if (Datenschutz_ID != other.Datenschutz_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);

		result = prime * result + Long.hashCode(Datenschutz_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerDatenschutz(Schueler_ID=" + this.Schueler_ID + ", Datenschutz_ID=" + this.Datenschutz_ID + ", Status=" + this.Status + ", Abgefragt=" + this.Abgefragt + ")";
	}

}
