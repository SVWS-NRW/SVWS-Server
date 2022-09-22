package de.nrw.schule.svws.db.dto.rev8.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Abt_Kl.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Abt_Kl")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.all", query="SELECT e FROM Rev8DTOAbteilungsKlassen e")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.id", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.id.multiple", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.abteilung_id", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.Abteilung_ID = :value")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.abteilung_id.multiple", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.Abteilung_ID IN :value")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.klassen_id", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.Klassen_ID = :value")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.klassen_id.multiple", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.Klassen_ID IN :value")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.sichtbar", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.sichtbar.multiple", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.primaryKeyQuery", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOAbteilungsKlassen.all.migration", query="SELECT e FROM Rev8DTOAbteilungsKlassen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abteilung_ID","Klassen_ID","Sichtbar"})
public class Rev8DTOAbteilungsKlassen {

	/** ID der Klassenzugehörigkeit zu einer Abteilung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Abteilung in der übergeordneten Tabelle */
	@Column(name = "Abteilung_ID")
	@JsonProperty
	public Long Abteilung_ID;

	/** ID der Klasse die zur Abteilung gehört */
	@Column(name = "Klassen_ID")
	@JsonProperty
	public Long Klassen_ID;

	/** steuert die Sichtbarkeit der Klasse zur Abteilung */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOAbteilungsKlassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abteilung_ID   der Wert für das Attribut Abteilung_ID
	 * @param Klassen_ID   der Wert für das Attribut Klassen_ID
	 */
	public Rev8DTOAbteilungsKlassen(final Long ID, final Long Abteilung_ID, final Long Klassen_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abteilung_ID == null) { 
			throw new NullPointerException("Abteilung_ID must not be null");
		}
		this.Abteilung_ID = Abteilung_ID;
		if (Klassen_ID == null) { 
			throw new NullPointerException("Klassen_ID must not be null");
		}
		this.Klassen_ID = Klassen_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOAbteilungsKlassen other = (Rev8DTOAbteilungsKlassen) obj;
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
		return "Rev8DTOAbteilungsKlassen(ID=" + this.ID + ", Abteilung_ID=" + this.Abteilung_ID + ", Klassen_ID=" + this.Klassen_ID + ", Sichtbar=" + this.Sichtbar + ")";
	}

}