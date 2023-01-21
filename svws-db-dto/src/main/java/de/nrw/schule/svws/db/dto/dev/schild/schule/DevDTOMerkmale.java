package de.nrw.schule.svws.db.dto.dev.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Merkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Merkmale")
@NamedQuery(name="DevDTOMerkmale.all", query="SELECT e FROM DevDTOMerkmale e")
@NamedQuery(name="DevDTOMerkmale.id", query="SELECT e FROM DevDTOMerkmale e WHERE e.ID = :value")
@NamedQuery(name="DevDTOMerkmale.id.multiple", query="SELECT e FROM DevDTOMerkmale e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOMerkmale.schule", query="SELECT e FROM DevDTOMerkmale e WHERE e.Schule = :value")
@NamedQuery(name="DevDTOMerkmale.schule.multiple", query="SELECT e FROM DevDTOMerkmale e WHERE e.Schule IN :value")
@NamedQuery(name="DevDTOMerkmale.schueler", query="SELECT e FROM DevDTOMerkmale e WHERE e.Schueler = :value")
@NamedQuery(name="DevDTOMerkmale.schueler.multiple", query="SELECT e FROM DevDTOMerkmale e WHERE e.Schueler IN :value")
@NamedQuery(name="DevDTOMerkmale.kurztext", query="SELECT e FROM DevDTOMerkmale e WHERE e.Kurztext = :value")
@NamedQuery(name="DevDTOMerkmale.kurztext.multiple", query="SELECT e FROM DevDTOMerkmale e WHERE e.Kurztext IN :value")
@NamedQuery(name="DevDTOMerkmale.langtext", query="SELECT e FROM DevDTOMerkmale e WHERE e.Langtext = :value")
@NamedQuery(name="DevDTOMerkmale.langtext.multiple", query="SELECT e FROM DevDTOMerkmale e WHERE e.Langtext IN :value")
@NamedQuery(name="DevDTOMerkmale.primaryKeyQuery", query="SELECT e FROM DevDTOMerkmale e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOMerkmale.all.migration", query="SELECT e FROM DevDTOMerkmale e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schule","Schueler","Kurztext","Langtext"})
public class DevDTOMerkmale {

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
	 * Erstellt ein neues Objekt der Klasse DevDTOMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOMerkmale ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DevDTOMerkmale(final Long ID) {
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
		DevDTOMerkmale other = (DevDTOMerkmale) obj;
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
		return "DevDTOMerkmale(ID=" + this.ID + ", Schule=" + this.Schule + ", Schueler=" + this.Schueler + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ")";
	}

}