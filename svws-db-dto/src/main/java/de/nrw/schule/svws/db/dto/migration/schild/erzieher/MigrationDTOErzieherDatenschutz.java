package de.nrw.schule.svws.db.dto.migration.schild.erzieher;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBoolean01Converter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle ErzieherDatenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOErzieherDatenschutzPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ErzieherDatenschutz")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.all", query="SELECT e FROM MigrationDTOErzieherDatenschutz e")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.erzieherid", query="SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.ErzieherID = :value")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.erzieherid.multiple", query="SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.ErzieherID IN :value")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.datenschutzid", query="SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.DatenschutzID = :value")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.datenschutzid.multiple", query="SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.DatenschutzID IN :value")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.status", query="SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.Status = :value")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.status.multiple", query="SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.Status IN :value")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.primaryKeyQuery", query="SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.ErzieherID = ?1 AND e.DatenschutzID = ?2")
@NamedQuery(name="MigrationDTOErzieherDatenschutz.all.migration", query="SELECT e FROM MigrationDTOErzieherDatenschutz e WHERE e.ErzieherID IS NOT NULL AND e.DatenschutzID IS NOT NULL")
@JsonPropertyOrder({"ErzieherID","DatenschutzID","Status"})
public class MigrationDTOErzieherDatenschutz {

	/** ErzieherID des Datenschutzeintrags */
	@Id
	@Column(name = "ErzieherID")
	@JsonProperty
	public Long ErzieherID;

	/** DatenschutzID des Eintrags */
	@Id
	@Column(name = "DatenschutzID")
	@JsonProperty
	public Long DatenschutzID;

	/** Status des Datenschutz-Eintrags (true/false) */
	@Column(name = "Status")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean Status;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOErzieherDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOErzieherDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOErzieherDatenschutz ohne eine Initialisierung der Attribute.
	 * @param ErzieherID   der Wert für das Attribut ErzieherID
	 * @param DatenschutzID   der Wert für das Attribut DatenschutzID
	 * @param Status   der Wert für das Attribut Status
	 */
	public MigrationDTOErzieherDatenschutz(final Long ErzieherID, final Long DatenschutzID, final Boolean Status) {
		if (ErzieherID == null) { 
			throw new NullPointerException("ErzieherID must not be null");
		}
		this.ErzieherID = ErzieherID;
		if (DatenschutzID == null) { 
			throw new NullPointerException("DatenschutzID must not be null");
		}
		this.DatenschutzID = DatenschutzID;
		if (Status == null) { 
			throw new NullPointerException("Status must not be null");
		}
		this.Status = Status;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOErzieherDatenschutz other = (MigrationDTOErzieherDatenschutz) obj;
		if (ErzieherID == null) {
			if (other.ErzieherID != null)
				return false;
		} else if (!ErzieherID.equals(other.ErzieherID))
			return false;

		if (DatenschutzID == null) {
			if (other.DatenschutzID != null)
				return false;
		} else if (!DatenschutzID.equals(other.DatenschutzID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ErzieherID == null) ? 0 : ErzieherID.hashCode());

		result = prime * result + ((DatenschutzID == null) ? 0 : DatenschutzID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOErzieherDatenschutz(ErzieherID=" + this.ErzieherID + ", DatenschutzID=" + this.DatenschutzID + ", Status=" + this.Status + ")";
	}

}