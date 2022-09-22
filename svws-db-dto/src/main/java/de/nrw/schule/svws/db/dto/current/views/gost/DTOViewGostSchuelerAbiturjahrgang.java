package de.nrw.schule.svws.db.dto.current.views.gost;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.SchuelerStatusConverter;
import de.nrw.schule.svws.db.converter.current.statkue.SchulformKuerzelConverter;
import de.nrw.schule.svws.db.converter.current.statkue.SchulgliederungKuerzelConverter;

import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.types.statkue.Schulgliederung;


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
import de.nrw.schule.svws.csv.converter.current.SchuelerStatusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.SchuelerStatusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.statkue.SchulformKuerzelConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.statkue.SchulformKuerzelConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.statkue.SchulgliederungKuerzelConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.statkue.SchulgliederungKuerzelConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbank-View V_Gost_Schueler_Abiturjahrgang.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "V_Gost_Schueler_Abiturjahrgang")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.all", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.id", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.ID = :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.id.multiple", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.ID IN :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.status", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Status = :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.status.multiple", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Status IN :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.schulform", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Schulform = :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.schulform.multiple", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Schulform IN :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.schulgliederung", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Schulgliederung = :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.schulgliederung.multiple", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Schulgliederung IN :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.abiturjahr", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Abiturjahr = :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.abiturjahr.multiple", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.Abiturjahr IN :value")
@NamedQuery(name="DTOViewGostSchuelerAbiturjahrgang.primaryKeyQuery", query="SELECT e FROM DTOViewGostSchuelerAbiturjahrgang e WHERE e.ID = ?1")
@JsonPropertyOrder({"ID","Status","Schulform","Schulgliederung","Abiturjahr"})
public class DTOViewGostSchuelerAbiturjahrgang {

	/** Die ID des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Status des Schülers */
	@Column(name = "Status")
	@JsonProperty
	@Convert(converter=SchuelerStatusConverter.class)
	@JsonSerialize(using=SchuelerStatusConverterSerializer.class)
	@JsonDeserialize(using=SchuelerStatusConverterDeserializer.class)
	public SchuelerStatus Status;

	/** Die Schulform des Schülers */
	@Column(name = "Schulform")
	@JsonProperty
	@Convert(converter=SchulformKuerzelConverter.class)
	@JsonSerialize(using=SchulformKuerzelConverterSerializer.class)
	@JsonDeserialize(using=SchulformKuerzelConverterDeserializer.class)
	public Schulform Schulform;

	/** Die Schulgliederung des Schülers */
	@Column(name = "Schulgliederung")
	@JsonProperty
	@Convert(converter=SchulgliederungKuerzelConverter.class)
	@JsonSerialize(using=SchulgliederungKuerzelConverterSerializer.class)
	@JsonDeserialize(using=SchulgliederungKuerzelConverterDeserializer.class)
	public Schulgliederung Schulgliederung;

	/** Das Abiturjahr des Schülers */
	@Column(name = "Abiturjahr")
	@JsonProperty
	public Long Abiturjahr;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOViewGostSchuelerAbiturjahrgang ohne eine Initialisierung der Attribute.
	 */
	private DTOViewGostSchuelerAbiturjahrgang() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOViewGostSchuelerAbiturjahrgang other = (DTOViewGostSchuelerAbiturjahrgang) obj;
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
		return "DTOViewGostSchuelerAbiturjahrgang(ID=" + this.ID + ", Status=" + this.Status + ", Schulform=" + this.Schulform + ", Schulgliederung=" + this.Schulgliederung + ", Abiturjahr=" + this.Abiturjahr + ")";
	}

}