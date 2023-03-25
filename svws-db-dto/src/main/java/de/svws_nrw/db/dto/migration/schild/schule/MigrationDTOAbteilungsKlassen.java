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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Abt_Kl.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Abt_Kl")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.all", query="SELECT e FROM MigrationDTOAbteilungsKlassen e")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.id", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.id.multiple", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.abteilung_id", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Abteilung_ID = :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.abteilung_id.multiple", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Abteilung_ID IN :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.klasse", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Klasse = :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.klasse.multiple", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Klasse IN :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.sichtbar", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.sichtbar.multiple", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.schulnreigner", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.schulnreigner.multiple", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.primaryKeyQuery", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOAbteilungsKlassen.all.migration", query="SELECT e FROM MigrationDTOAbteilungsKlassen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abteilung_ID","Klasse","Sichtbar","SchulnrEigner"})
public class MigrationDTOAbteilungsKlassen {

	/** ID der Klassenzugehörigkeit zu einer Abteilung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Abteilung in der übergeordneten Tabelle */
	@Column(name = "Abteilung_ID")
	@JsonProperty
	public Long Abteilung_ID;

	/** DEPRECATED: Klassenbezeichnung die zur Abteilung gehört */
	@Column(name = "Klasse")
	@JsonProperty
	public String Klasse;

	/** steuert die Sichtbarkeit der Klasse zur Abteilung */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAbteilungsKlassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abteilung_ID   der Wert für das Attribut Abteilung_ID
	 * @param Klasse   der Wert für das Attribut Klasse
	 */
	public MigrationDTOAbteilungsKlassen(final Long ID, final Long Abteilung_ID, final String Klasse) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abteilung_ID == null) { 
			throw new NullPointerException("Abteilung_ID must not be null");
		}
		this.Abteilung_ID = Abteilung_ID;
		if (Klasse == null) { 
			throw new NullPointerException("Klasse must not be null");
		}
		this.Klasse = Klasse;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAbteilungsKlassen other = (MigrationDTOAbteilungsKlassen) obj;
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
		return "MigrationDTOAbteilungsKlassen(ID=" + this.ID + ", Abteilung_ID=" + this.Abteilung_ID + ", Klasse=" + this.Klasse + ", Sichtbar=" + this.Sichtbar + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}