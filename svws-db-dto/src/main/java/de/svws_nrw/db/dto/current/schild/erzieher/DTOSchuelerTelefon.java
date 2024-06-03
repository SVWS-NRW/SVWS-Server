package de.svws_nrw.db.dto.current.schild.erzieher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerTelefone.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerTelefone")
@JsonPropertyOrder({"ID", "Schueler_ID", "TelefonArt_ID", "Telefonnummer", "Bemerkung", "Sortierung", "Gesperrt"})
public final class DTOSchuelerTelefon {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerTelefon e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerTelefon e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerTelefon e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerTelefon e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerTelefon e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerTelefon e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TelefonArt_ID */
	public static final String QUERY_BY_TELEFONART_ID = "SELECT e FROM DTOSchuelerTelefon e WHERE e.TelefonArt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TelefonArt_ID */
	public static final String QUERY_LIST_BY_TELEFONART_ID = "SELECT e FROM DTOSchuelerTelefon e WHERE e.TelefonArt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Telefonnummer */
	public static final String QUERY_BY_TELEFONNUMMER = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Telefonnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Telefonnummer */
	public static final String QUERY_LIST_BY_TELEFONNUMMER = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Telefonnummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gesperrt */
	public static final String QUERY_BY_GESPERRT = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Gesperrt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gesperrt */
	public static final String QUERY_LIST_BY_GESPERRT = "SELECT e FROM DTOSchuelerTelefon e WHERE e.Gesperrt IN ?1";

	/** ID des Telefonnummerneintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** SchülerID des Telefonnummerneintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Art des Telefonnummerneintrags */
	@Column(name = "TelefonArt_ID")
	@JsonProperty
	public Long TelefonArt_ID;

	/** Telefonnummer des Telefonnummerneintrags */
	@Column(name = "Telefonnummer")
	@JsonProperty
	public String Telefonnummer;

	/** Bemerkung des Telefonnummerneintrags */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Sortierung des Telefonnummerneintrags */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sperrung des Telefonnummerneintrags */
	@Column(name = "Gesperrt")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gesperrt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerTelefon ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerTelefon() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerTelefon ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerTelefon(final long ID, final long Schueler_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerTelefon other = (DTOSchuelerTelefon) obj;
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
		return "DTOSchuelerTelefon(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", TelefonArt_ID=" + this.TelefonArt_ID + ", Telefonnummer=" + this.Telefonnummer + ", Bemerkung=" + this.Bemerkung + ", Sortierung=" + this.Sortierung + ", Gesperrt=" + this.Gesperrt + ")";
	}

}
