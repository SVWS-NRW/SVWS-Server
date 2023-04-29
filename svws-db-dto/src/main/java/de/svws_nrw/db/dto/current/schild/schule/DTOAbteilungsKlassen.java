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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Abt_Kl.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Abt_Kl")
@NamedQuery(name = "DTOAbteilungsKlassen.all", query = "SELECT e FROM DTOAbteilungsKlassen e")
@NamedQuery(name = "DTOAbteilungsKlassen.id", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID = :value")
@NamedQuery(name = "DTOAbteilungsKlassen.id.multiple", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOAbteilungsKlassen.abteilung_id", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Abteilung_ID = :value")
@NamedQuery(name = "DTOAbteilungsKlassen.abteilung_id.multiple", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Abteilung_ID IN :value")
@NamedQuery(name = "DTOAbteilungsKlassen.klassen_id", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Klassen_ID = :value")
@NamedQuery(name = "DTOAbteilungsKlassen.klassen_id.multiple", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Klassen_ID IN :value")
@NamedQuery(name = "DTOAbteilungsKlassen.sichtbar", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOAbteilungsKlassen.sichtbar.multiple", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOAbteilungsKlassen.primaryKeyQuery", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID = ?1")
@NamedQuery(name = "DTOAbteilungsKlassen.all.migration", query = "SELECT e FROM DTOAbteilungsKlassen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abteilung_ID", "Klassen_ID", "Sichtbar"})
public final class DTOAbteilungsKlassen {

	/** ID der Klassenzugehörigkeit zu einer Abteilung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Abteilung in der übergeordneten Tabelle */
	@Column(name = "Abteilung_ID")
	@JsonProperty
	public long Abteilung_ID;

	/** ID der Klasse die zur Abteilung gehört */
	@Column(name = "Klassen_ID")
	@JsonProperty
	public long Klassen_ID;

	/** steuert die Sichtbarkeit der Klasse zur Abteilung */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAbteilungsKlassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAbteilungsKlassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abteilung_ID   der Wert für das Attribut Abteilung_ID
	 * @param Klassen_ID   der Wert für das Attribut Klassen_ID
	 */
	public DTOAbteilungsKlassen(final long ID, final long Abteilung_ID, final long Klassen_ID) {
		this.ID = ID;
		this.Abteilung_ID = Abteilung_ID;
		this.Klassen_ID = Klassen_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAbteilungsKlassen other = (DTOAbteilungsKlassen) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOAbteilungsKlassen(ID=" + this.ID + ", Abteilung_ID=" + this.Abteilung_ID + ", Klassen_ID=" + this.Klassen_ID + ", Sichtbar=" + this.Sichtbar + ")";
	}

}
