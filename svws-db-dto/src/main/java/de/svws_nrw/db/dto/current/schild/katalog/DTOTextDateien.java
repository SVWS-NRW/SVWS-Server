package de.svws_nrw.db.dto.current.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Textdateien.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Textdateien")
@JsonPropertyOrder({"ID", "Bezeichnung", "Text_ID", "Text_Body", "Sichtbar", "Sortierung"})
public final class DTOTextDateien {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTextDateien e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTextDateien e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTextDateien e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTextDateien e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOTextDateien e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOTextDateien e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOTextDateien e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOTextDateien e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Text_ID */
	public static final String QUERY_BY_TEXT_ID = "SELECT e FROM DTOTextDateien e WHERE e.Text_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Text_ID */
	public static final String QUERY_LIST_BY_TEXT_ID = "SELECT e FROM DTOTextDateien e WHERE e.Text_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Text_Body */
	public static final String QUERY_BY_TEXT_BODY = "SELECT e FROM DTOTextDateien e WHERE e.Text_Body = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Text_Body */
	public static final String QUERY_LIST_BY_TEXT_BODY = "SELECT e FROM DTOTextDateien e WHERE e.Text_Body IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOTextDateien e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOTextDateien e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOTextDateien e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOTextDateien e WHERE e.Sortierung IN ?1";

	/** ID des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnungdes Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** TextID des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Text_ID")
	@JsonProperty
	public long Text_ID;

	/** Text-Body des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Text_Body")
	@JsonProperty
	public String Text_Body;

	/** Sichbarkeit des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierung des Textes der zum Ersatz für txt-Dateien genutzt wird */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTextDateien ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTextDateien() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTextDateien ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Text_ID   der Wert für das Attribut Text_ID
	 */
	public DTOTextDateien(final long ID, final long Text_ID) {
		this.ID = ID;
		this.Text_ID = Text_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOTextDateien other = (DTOTextDateien) obj;
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
		return "DTOTextDateien(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Text_ID=" + this.Text_ID + ", Text_Body=" + this.Text_Body + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ")";
	}

}
