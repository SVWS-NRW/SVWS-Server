package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Merkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Merkmale")
@NamedQuery(name="MigrationDTOMerkmale.all", query="SELECT e FROM MigrationDTOMerkmale e")
@NamedQuery(name="MigrationDTOMerkmale.id", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOMerkmale.id.multiple", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOMerkmale.schule", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.Schule = :value")
@NamedQuery(name="MigrationDTOMerkmale.schule.multiple", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.Schule IN :value")
@NamedQuery(name="MigrationDTOMerkmale.schueler", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.Schueler = :value")
@NamedQuery(name="MigrationDTOMerkmale.schueler.multiple", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.Schueler IN :value")
@NamedQuery(name="MigrationDTOMerkmale.kurztext", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.Kurztext = :value")
@NamedQuery(name="MigrationDTOMerkmale.kurztext.multiple", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.Kurztext IN :value")
@NamedQuery(name="MigrationDTOMerkmale.langtext", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.Langtext = :value")
@NamedQuery(name="MigrationDTOMerkmale.langtext.multiple", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.Langtext IN :value")
@NamedQuery(name="MigrationDTOMerkmale.schulnreigner", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOMerkmale.schulnreigner.multiple", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOMerkmale.primaryKeyQuery", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOMerkmale.all.migration", query="SELECT e FROM MigrationDTOMerkmale e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schule","Schueler","Kurztext","Langtext","SchulnrEigner"})
public class MigrationDTOMerkmale {

	/** ID des Merkmals das an der Schule vorhanden ist */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Merkmal kann der Schule zugewiesen werden */
	@Column(name = "Schule")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Schule;

	/** Merkmal kann auch einem einzelnen Schüler auf Individualdaten II zugewiesen werden */
	@Column(name = "Schueler")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Schueler;

	/** Kurztext des Merkmals zB OGS */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Langtext des Merkmal zB offener Ganztag */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOMerkmale ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOMerkmale(final Long ID) {
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
		MigrationDTOMerkmale other = (MigrationDTOMerkmale) obj;
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
		return "MigrationDTOMerkmale(ID=" + this.ID + ", Schule=" + this.Schule + ", Schueler=" + this.Schueler + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}