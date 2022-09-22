package de.nrw.schule.svws.db.dto.current.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_HSchStatus.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_HSchStatus")
@NamedQuery(name="DTOInternSchuelerStatus.all", query="SELECT e FROM DTOInternSchuelerStatus e")
@NamedQuery(name="DTOInternSchuelerStatus.statusnr", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.StatusNr = :value")
@NamedQuery(name="DTOInternSchuelerStatus.statusnr.multiple", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.StatusNr IN :value")
@NamedQuery(name="DTOInternSchuelerStatus.bezeichnung", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOInternSchuelerStatus.bezeichnung.multiple", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOInternSchuelerStatus.sortierung", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOInternSchuelerStatus.sortierung.multiple", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOInternSchuelerStatus.insimexp", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.InSimExp = :value")
@NamedQuery(name="DTOInternSchuelerStatus.insimexp.multiple", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.InSimExp IN :value")
@NamedQuery(name="DTOInternSchuelerStatus.simabschnitt", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.SIMAbschnitt = :value")
@NamedQuery(name="DTOInternSchuelerStatus.simabschnitt.multiple", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.SIMAbschnitt IN :value")
@NamedQuery(name="DTOInternSchuelerStatus.primaryKeyQuery", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.StatusNr = ?1")
@NamedQuery(name="DTOInternSchuelerStatus.all.migration", query="SELECT e FROM DTOInternSchuelerStatus e WHERE e.StatusNr IS NOT NULL")
@JsonPropertyOrder({"StatusNr","Bezeichnung","Sortierung","InSimExp","SIMAbschnitt"})
public class DTOInternSchuelerStatus {

	/** Schildintern Tabelle: Nummer des Schüler-Status */
	@Id
	@Column(name = "StatusNr")
	@JsonProperty
	public Integer StatusNr;

	/** Schildintern Tabelle: Klartext des Schülerstatus */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Schildintern Tabelle: Sortierung des Schülerstatus */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Schildintern Tabelle: DEPRECATED:  */
	@Column(name = "InSimExp")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean InSimExp;

	/** Schildintern Tabelle: DEPRECATED:  */
	@Column(name = "SIMAbschnitt")
	@JsonProperty
	public String SIMAbschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternSchuelerStatus ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternSchuelerStatus() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternSchuelerStatus ohne eine Initialisierung der Attribute.
	 * @param StatusNr   der Wert für das Attribut StatusNr
	 */
	public DTOInternSchuelerStatus(final Integer StatusNr) {
		if (StatusNr == null) { 
			throw new NullPointerException("StatusNr must not be null");
		}
		this.StatusNr = StatusNr;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInternSchuelerStatus other = (DTOInternSchuelerStatus) obj;
		if (StatusNr == null) {
			if (other.StatusNr != null)
				return false;
		} else if (!StatusNr.equals(other.StatusNr))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((StatusNr == null) ? 0 : StatusNr.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOInternSchuelerStatus(StatusNr=" + this.StatusNr + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", InSimExp=" + this.InSimExp + ", SIMAbschnitt=" + this.SIMAbschnitt + ")";
	}

}