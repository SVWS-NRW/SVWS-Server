package de.nrw.schule.svws.db.dto.migration.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationDatumConverter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationDatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerEinzelleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerEinzelleistungen")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.all", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.schulnreigner", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.schulnreigner.multiple", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.id", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.id.multiple", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.datum", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Datum = :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.datum.multiple", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Datum IN :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.lehrer_id", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.lehrer_id.multiple", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.art_id", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Art_ID = :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.art_id.multiple", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Art_ID IN :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.bemerkung", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Bemerkung = :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.bemerkung.multiple", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Bemerkung IN :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.leistung_id", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Leistung_ID = :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.leistung_id.multiple", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.Leistung_ID IN :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.notenkrz", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.NotenKrz = :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.notenkrz.multiple", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.NotenKrz IN :value")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.primaryKeyQuery", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOSchuelerTeilleistung.all.migration", query="SELECT e FROM MigrationDTOSchuelerTeilleistung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"SchulnrEigner","ID","Datum","Lehrer_ID","Art_ID","Bemerkung","Leistung_ID","NotenKrz"})
public class MigrationDTOSchuelerTeilleistung {

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** ID der Teilleistung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Datum der Teilleistung */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter=MigrationDatumConverter.class)
	@JsonSerialize(using=MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using=MigrationDatumConverterDeserializer.class)
	public String Datum;

	/** LehrerID der Teilleistung */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Art der Teilleistung */
	@Column(name = "Art_ID")
	@JsonProperty
	public Long Art_ID;

	/** Bemerkung zur Teilleistung */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** LeistungsdatenID der Teilleistung */
	@Column(name = "Leistung_ID")
	@JsonProperty
	public Long Leistung_ID;

	/** Notenkürzel der Teilleistung */
	@Column(name = "NotenKrz")
	@JsonProperty
	public String NotenKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerTeilleistung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerTeilleistung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerTeilleistung ohne eine Initialisierung der Attribute.
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param ID   der Wert für das Attribut ID
	 * @param Leistung_ID   der Wert für das Attribut Leistung_ID
	 */
	public MigrationDTOSchuelerTeilleistung(final Integer SchulnrEigner, final Long ID, final Long Leistung_ID) {
		if (SchulnrEigner == null) { 
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Leistung_ID == null) { 
			throw new NullPointerException("Leistung_ID must not be null");
		}
		this.Leistung_ID = Leistung_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerTeilleistung other = (MigrationDTOSchuelerTeilleistung) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerTeilleistung(SchulnrEigner=" + this.SchulnrEigner + ", ID=" + this.ID + ", Datum=" + this.Datum + ", Lehrer_ID=" + this.Lehrer_ID + ", Art_ID=" + this.Art_ID + ", Bemerkung=" + this.Bemerkung + ", Leistung_ID=" + this.Leistung_ID + ", NotenKrz=" + this.NotenKrz + ")";
	}

}