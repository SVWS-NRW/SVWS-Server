package de.nrw.schule.svws.db.dto.current.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.DatumConverter;


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
import de.nrw.schule.svws.csv.converter.current.DatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerEinzelleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerEinzelleistungen")
@NamedQuery(name="DTOSchuelerTeilleistung.all", query="SELECT e FROM DTOSchuelerTeilleistung e")
@NamedQuery(name="DTOSchuelerTeilleistung.id", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.ID = :value")
@NamedQuery(name="DTOSchuelerTeilleistung.id.multiple", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchuelerTeilleistung.datum", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Datum = :value")
@NamedQuery(name="DTOSchuelerTeilleistung.datum.multiple", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Datum IN :value")
@NamedQuery(name="DTOSchuelerTeilleistung.lehrer_id", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="DTOSchuelerTeilleistung.lehrer_id.multiple", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="DTOSchuelerTeilleistung.art_id", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Art_ID = :value")
@NamedQuery(name="DTOSchuelerTeilleistung.art_id.multiple", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Art_ID IN :value")
@NamedQuery(name="DTOSchuelerTeilleistung.bemerkung", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Bemerkung = :value")
@NamedQuery(name="DTOSchuelerTeilleistung.bemerkung.multiple", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Bemerkung IN :value")
@NamedQuery(name="DTOSchuelerTeilleistung.leistung_id", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Leistung_ID = :value")
@NamedQuery(name="DTOSchuelerTeilleistung.leistung_id.multiple", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.Leistung_ID IN :value")
@NamedQuery(name="DTOSchuelerTeilleistung.notenkrz", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.NotenKrz = :value")
@NamedQuery(name="DTOSchuelerTeilleistung.notenkrz.multiple", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.NotenKrz IN :value")
@NamedQuery(name="DTOSchuelerTeilleistung.primaryKeyQuery", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchuelerTeilleistung.all.migration", query="SELECT e FROM DTOSchuelerTeilleistung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Datum","Lehrer_ID","Art_ID","Bemerkung","Leistung_ID","NotenKrz"})
public class DTOSchuelerTeilleistung {

	/** ID der Teilleistung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Datum der Teilleistung */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerTeilleistung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerTeilleistung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerTeilleistung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Leistung_ID   der Wert für das Attribut Leistung_ID
	 */
	public DTOSchuelerTeilleistung(final Long ID, final Long Leistung_ID) {
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
		DTOSchuelerTeilleistung other = (DTOSchuelerTeilleistung) obj;
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
		return "DTOSchuelerTeilleistung(ID=" + this.ID + ", Datum=" + this.Datum + ", Lehrer_ID=" + this.Lehrer_ID + ", Art_ID=" + this.Art_ID + ", Bemerkung=" + this.Bemerkung + ", Leistung_ID=" + this.Leistung_ID + ", NotenKrz=" + this.NotenKrz + ")";
	}

}