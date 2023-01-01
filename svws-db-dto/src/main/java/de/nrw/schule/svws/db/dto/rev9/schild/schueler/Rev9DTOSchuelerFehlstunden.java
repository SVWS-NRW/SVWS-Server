package de.nrw.schule.svws.db.dto.rev9.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFehlstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFehlstunden")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.all", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.id", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.id.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.abschnitt_id", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.abschnitt_id.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.datum", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Datum = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.datum.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Datum IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.fach_id", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Fach_ID = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.fach_id.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Fach_ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.fehlstd", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.FehlStd = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.fehlstd.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.FehlStd IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.vonstd", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.VonStd = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.vonstd.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.VonStd IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.bisstd", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.BisStd = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.bisstd.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.BisStd IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.entschuldigt", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Entschuldigt = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.entschuldigt.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Entschuldigt IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.lehrer_id", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.lehrer_id.multiple", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.primaryKeyQuery", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOSchuelerFehlstunden.all.migration", query="SELECT e FROM Rev9DTOSchuelerFehlstunden e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abschnitt_ID","Datum","Fach_ID","FehlStd","VonStd","BisStd","Entschuldigt","Lehrer_ID"})
public class Rev9DTOSchuelerFehlstunden {

	/** ID des Fehlstundeneintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** AbschnittsID des zugehörigen Lernabschnitts */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Datum der Fehlzeit */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Datum;

	/** FachID der Fehlzeit */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Anzahl der Fehlstunden */
	@Column(name = "FehlStd")
	@JsonProperty
	public Double FehlStd;

	/** Beginn Stunde Fehlzeit */
	@Column(name = "VonStd")
	@JsonProperty
	public Integer VonStd;

	/** Ende Stunde Fehlzeit */
	@Column(name = "BisStd")
	@JsonProperty
	public Integer BisStd;

	/** Entschuldigt Ja Nein */
	@Column(name = "Entschuldigt")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Entschuldigt;

	/** LehrerID der Fehlzeit */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerFehlstunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOSchuelerFehlstunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerFehlstunden ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Datum   der Wert für das Attribut Datum
	 * @param FehlStd   der Wert für das Attribut FehlStd
	 */
	public Rev9DTOSchuelerFehlstunden(final Long ID, final Long Abschnitt_ID, final String Datum, final Double FehlStd) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abschnitt_ID == null) { 
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Datum == null) { 
			throw new NullPointerException("Datum must not be null");
		}
		this.Datum = Datum;
		if (FehlStd == null) { 
			throw new NullPointerException("FehlStd must not be null");
		}
		this.FehlStd = FehlStd;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOSchuelerFehlstunden other = (Rev9DTOSchuelerFehlstunden) obj;
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
		return "Rev9DTOSchuelerFehlstunden(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Datum=" + this.Datum + ", Fach_ID=" + this.Fach_ID + ", FehlStd=" + this.FehlStd + ", VonStd=" + this.VonStd + ", BisStd=" + this.BisStd + ", Entschuldigt=" + this.Entschuldigt + ", Lehrer_ID=" + this.Lehrer_ID + ")";
	}

}