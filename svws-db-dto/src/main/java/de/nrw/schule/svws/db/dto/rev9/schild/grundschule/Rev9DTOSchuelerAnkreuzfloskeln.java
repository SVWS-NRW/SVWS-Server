package de.nrw.schule.svws.db.dto.rev9.schild.grundschule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAnkreuzfloskeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAnkreuzfloskeln")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.all", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.id", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.id.multiple", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.abschnitt_id", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.abschnitt_id.multiple", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.floskel_id", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Floskel_ID = :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.floskel_id.multiple", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Floskel_ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe1", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe1 = :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe1.multiple", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe1 IN :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe2", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe2 = :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe2.multiple", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe2 IN :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe3", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe3 = :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe3.multiple", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe3 IN :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe4", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe4 = :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe4.multiple", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe4 IN :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe5", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe5 = :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.stufe5.multiple", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.Stufe5 IN :value")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.primaryKeyQuery", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOSchuelerAnkreuzfloskeln.all.migration", query="SELECT e FROM Rev9DTOSchuelerAnkreuzfloskeln e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abschnitt_ID","Floskel_ID","Stufe1","Stufe2","Stufe3","Stufe4","Stufe5"})
public class Rev9DTOSchuelerAnkreuzfloskeln {

	/** ID des Ankreuzfloskeleintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** ID der Ankreuzfloskel aus dem Katalog */
	@Column(name = "Floskel_ID")
	@JsonProperty
	public Long Floskel_ID;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe1")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe1;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe2")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe2;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe3")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe3;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe4")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe4;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe5")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe5;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOSchuelerAnkreuzfloskeln() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Floskel_ID   der Wert für das Attribut Floskel_ID
	 */
	public Rev9DTOSchuelerAnkreuzfloskeln(final Long ID, final Long Abschnitt_ID, final Long Floskel_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abschnitt_ID == null) { 
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Floskel_ID == null) { 
			throw new NullPointerException("Floskel_ID must not be null");
		}
		this.Floskel_ID = Floskel_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOSchuelerAnkreuzfloskeln other = (Rev9DTOSchuelerAnkreuzfloskeln) obj;
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
		return "Rev9DTOSchuelerAnkreuzfloskeln(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Floskel_ID=" + this.Floskel_ID + ", Stufe1=" + this.Stufe1 + ", Stufe2=" + this.Stufe2 + ", Stufe3=" + this.Stufe3 + ", Stufe4=" + this.Stufe4 + ", Stufe5=" + this.Stufe5 + ")";
	}

}