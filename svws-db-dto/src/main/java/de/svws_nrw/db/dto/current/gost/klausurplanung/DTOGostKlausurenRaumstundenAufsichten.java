package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.UhrzeitConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@JsonPropertyOrder({"ID", "Raumstunde_ID", "Lehrer_ID", "Startzeit", "Endzeit", "Bemerkungen"})
public final class DTOGostKlausurenRaumstundenAufsichten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Raumstunde_ID */
	public static final String QUERY_BY_RAUMSTUNDE_ID = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Raumstunde_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Raumstunde_ID */
	public static final String QUERY_LIST_BY_RAUMSTUNDE_ID = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Raumstunde_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Startzeit */
	public static final String QUERY_BY_STARTZEIT = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Startzeit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Startzeit */
	public static final String QUERY_LIST_BY_STARTZEIT = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Startzeit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Endzeit */
	public static final String QUERY_BY_ENDZEIT = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Endzeit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Endzeit */
	public static final String QUERY_LIST_BY_ENDZEIT = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Endzeit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenRaumstundenAufsichten e WHERE e.Bemerkungen IN ?1";

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
