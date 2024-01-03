package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.UhrzeitConverter;


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
import de.svws_nrw.csv.converter.current.UhrzeitConverterSerializer;
import de.svws_nrw.csv.converter.current.UhrzeitConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Raumstunden_Aufsichten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Raumstunden_Aufsichten")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.all", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.id", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.id.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.raumstunde_id", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Raumstunde_ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.raumstunde_id.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Raumstunde_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.lehrer_id", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.lehrer_id.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.startzeit", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Startzeit = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.startzeit.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Startzeit IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.endzeit", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Endzeit = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.endzeit.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Endzeit IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.bemerkungen", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenRaumstundenAufsichten.all.migration", query = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Raumstunde_ID", "Lehrer_ID", "Startzeit", "Endzeit", "Bemerkungen"})
public final class DTOGostKlausurenRaumstundenAufsichten {

	/** ID der Klausuraufsicht (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Klausur-Raumstunde */
	@Column(name = "Raumstunde_ID")
	@JsonProperty
	public long Raumstunde_ID;

	/** ID des Lehrers */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Die Startzeit der Aufsicht */
	@Column(name = "Startzeit")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public Integer Startzeit;

	/** Die Endzeit der Aufsicht */
	@Column(name = "Endzeit")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public Integer Endzeit;

	/** Text für Bemerkungen zur Aufsicht */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaumstundenAufsichten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenRaumstundenAufsichten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaumstundenAufsichten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Raumstunde_ID   der Wert für das Attribut Raumstunde_ID
	 */
	public DTOGostKlausurenRaumstundenAufsichten(final long ID, final long Raumstunde_ID) {
		this.ID = ID;
		this.Raumstunde_ID = Raumstunde_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenRaumstundenAufsichten other = (DTOGostKlausurenRaumstundenAufsichten) obj;
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
		return "DTOGostKlausurenRaumstundenAufsichten(ID=" + this.ID + ", Raumstunde_ID=" + this.Raumstunde_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Startzeit=" + this.Startzeit + ", Endzeit=" + this.Endzeit + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
