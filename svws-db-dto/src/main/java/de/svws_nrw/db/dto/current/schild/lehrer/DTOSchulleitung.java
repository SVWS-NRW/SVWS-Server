package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumConverter;


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
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulleitung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulleitung")
@JsonPropertyOrder({"ID", "LeitungsfunktionID", "Funktionstext", "LehrerID", "Von", "Bis"})
public final class DTOSchulleitung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchulleitung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchulleitung e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchulleitung e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchulleitung e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchulleitung e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchulleitung e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LeitungsfunktionID */
	public static final String QUERY_BY_LEITUNGSFUNKTIONID = "SELECT e FROM DTOSchulleitung e WHERE e.LeitungsfunktionID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LeitungsfunktionID */
	public static final String QUERY_LIST_BY_LEITUNGSFUNKTIONID = "SELECT e FROM DTOSchulleitung e WHERE e.LeitungsfunktionID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Funktionstext */
	public static final String QUERY_BY_FUNKTIONSTEXT = "SELECT e FROM DTOSchulleitung e WHERE e.Funktionstext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Funktionstext */
	public static final String QUERY_LIST_BY_FUNKTIONSTEXT = "SELECT e FROM DTOSchulleitung e WHERE e.Funktionstext IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LehrerID */
	public static final String QUERY_BY_LEHRERID = "SELECT e FROM DTOSchulleitung e WHERE e.LehrerID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LehrerID */
	public static final String QUERY_LIST_BY_LEHRERID = "SELECT e FROM DTOSchulleitung e WHERE e.LehrerID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Von */
	public static final String QUERY_BY_VON = "SELECT e FROM DTOSchulleitung e WHERE e.Von = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Von */
	public static final String QUERY_LIST_BY_VON = "SELECT e FROM DTOSchulleitung e WHERE e.Von IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bis */
	public static final String QUERY_BY_BIS = "SELECT e FROM DTOSchulleitung e WHERE e.Bis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bis */
	public static final String QUERY_LIST_BY_BIS = "SELECT e FROM DTOSchulleitung e WHERE e.Bis IN ?1";

	/** ID der Schulleitungsfunktionseintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Schulleitungsfunktion (Fremdschlüssel aus der Tabelle SchulleitungFunktion) */
	@Column(name = "LeitungsfunktionID")
	@JsonProperty
	public long LeitungsfunktionID;

	/** Beschreibung der Funktion (Default Text aus SchulleitungFunktion aber änderbar) */
	@Column(name = "Funktionstext")
	@JsonProperty
	public String Funktionstext;

	/** ID des Lehrerdatensatzes */
	@Column(name = "LehrerID")
	@JsonProperty
	public long LehrerID;

	/** Beginndatum */
	@Column(name = "Von")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Von;

	/** Endedatum */
	@Column(name = "Bis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Bis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulleitung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulleitung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulleitung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param LeitungsfunktionID   der Wert für das Attribut LeitungsfunktionID
	 * @param Funktionstext   der Wert für das Attribut Funktionstext
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 */
	public DTOSchulleitung(final long ID, final long LeitungsfunktionID, final String Funktionstext, final long LehrerID) {
		this.ID = ID;
		this.LeitungsfunktionID = LeitungsfunktionID;
		if (Funktionstext == null) {
			throw new NullPointerException("Funktionstext must not be null");
		}
		this.Funktionstext = Funktionstext;
		this.LehrerID = LehrerID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchulleitung other = (DTOSchulleitung) obj;
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
		return "DTOSchulleitung(ID=" + this.ID + ", LeitungsfunktionID=" + this.LeitungsfunktionID + ", Funktionstext=" + this.Funktionstext + ", LehrerID=" + this.LehrerID + ", Von=" + this.Von + ", Bis=" + this.Bis + ")";
	}

}
