package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Merkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Merkmale")
@NamedQuery(name="DTOMerkmale.all", query="SELECT e FROM DTOMerkmale e")
@NamedQuery(name="DTOMerkmale.id", query="SELECT e FROM DTOMerkmale e WHERE e.ID = :value")
@NamedQuery(name="DTOMerkmale.id.multiple", query="SELECT e FROM DTOMerkmale e WHERE e.ID IN :value")
@NamedQuery(name="DTOMerkmale.schule", query="SELECT e FROM DTOMerkmale e WHERE e.Schule = :value")
@NamedQuery(name="DTOMerkmale.schule.multiple", query="SELECT e FROM DTOMerkmale e WHERE e.Schule IN :value")
@NamedQuery(name="DTOMerkmale.schueler", query="SELECT e FROM DTOMerkmale e WHERE e.Schueler = :value")
@NamedQuery(name="DTOMerkmale.schueler.multiple", query="SELECT e FROM DTOMerkmale e WHERE e.Schueler IN :value")
@NamedQuery(name="DTOMerkmale.kurztext", query="SELECT e FROM DTOMerkmale e WHERE e.Kurztext = :value")
@NamedQuery(name="DTOMerkmale.kurztext.multiple", query="SELECT e FROM DTOMerkmale e WHERE e.Kurztext IN :value")
@NamedQuery(name="DTOMerkmale.langtext", query="SELECT e FROM DTOMerkmale e WHERE e.Langtext = :value")
@NamedQuery(name="DTOMerkmale.langtext.multiple", query="SELECT e FROM DTOMerkmale e WHERE e.Langtext IN :value")
@NamedQuery(name="DTOMerkmale.primaryKeyQuery", query="SELECT e FROM DTOMerkmale e WHERE e.ID = ?1")
@NamedQuery(name="DTOMerkmale.all.migration", query="SELECT e FROM DTOMerkmale e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schule","Schueler","Kurztext","Langtext"})
public class DTOMerkmale {

	/** ID des Merkmals das an der Schule vorhanden ist */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Merkmal kann der Schule zugewiesen werden */
	@Column(name = "Schule")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Schule;

	/** Merkmal kann auch einem einzelnen Schüler auf Individualdaten II zugewiesen werden */
	@Column(name = "Schueler")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Schueler;

	/** Kurztext des Merkmals zB OGS */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Langtext des Merkmal zB offener Ganztag */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOMerkmale ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOMerkmale(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOMerkmale other = (DTOMerkmale) obj;
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
		return "DTOMerkmale(ID=" + this.ID + ", Schule=" + this.Schule + ", Schueler=" + this.Schueler + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ")";
	}

}