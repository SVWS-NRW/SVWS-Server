package de.svws_nrw.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Abteilungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Abteilungen")
@JsonPropertyOrder({"ID", "Bezeichnung", "Schuljahresabschnitts_ID", "AbteilungsLeiter_ID", "Sichtbar", "Raum", "Email", "Durchwahl", "Sortierung"})
public final class DTOAbteilungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOAbteilungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOAbteilungen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOAbteilungen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOAbteilungen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOAbteilungen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOAbteilungen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOAbteilungen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOAbteilungen e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOAbteilungen e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOAbteilungen e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbteilungsLeiter_ID */
	public static final String QUERY_BY_ABTEILUNGSLEITER_ID = "SELECT e FROM DTOAbteilungen e WHERE e.AbteilungsLeiter_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbteilungsLeiter_ID */
	public static final String QUERY_LIST_BY_ABTEILUNGSLEITER_ID = "SELECT e FROM DTOAbteilungen e WHERE e.AbteilungsLeiter_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOAbteilungen e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOAbteilungen e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Raum */
	public static final String QUERY_BY_RAUM = "SELECT e FROM DTOAbteilungen e WHERE e.Raum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Raum */
	public static final String QUERY_LIST_BY_RAUM = "SELECT e FROM DTOAbteilungen e WHERE e.Raum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM DTOAbteilungen e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM DTOAbteilungen e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Durchwahl */
	public static final String QUERY_BY_DURCHWAHL = "SELECT e FROM DTOAbteilungen e WHERE e.Durchwahl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Durchwahl */
	public static final String QUERY_LIST_BY_DURCHWAHL = "SELECT e FROM DTOAbteilungen e WHERE e.Durchwahl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOAbteilungen e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOAbteilungen e WHERE e.Sortierung IN ?1";

	/** ID der Abteilung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Text für die Bezeichnung der Abteilung */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public long Schuljahresabschnitts_ID;

	/** Lehrer-ID für den Abteilungsleiter */
	@Column(name = "AbteilungsLeiter_ID")
	@JsonProperty
	public Long AbteilungsLeiter_ID;

	/** steuert die Sichtbarkeit der Abteilung */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Bezeichnung für den Raum des Abteilungsleiter z.B. für Briefköpfe */
	@Column(name = "Raum")
	@JsonProperty
	public String Raum;

	/** Email des Abteilungsleiters */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Telefonische Durchwahl des Abteilungsleiters */
	@Column(name = "Durchwahl")
	@JsonProperty
	public String Durchwahl;

	/** Sortierung des Datensatzes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAbteilungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAbteilungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAbteilungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 */
	public DTOAbteilungen(final long ID, final String Bezeichnung, final long Schuljahresabschnitts_ID) {
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAbteilungen other = (DTOAbteilungen) obj;
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
		return "DTOAbteilungen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", AbteilungsLeiter_ID=" + this.AbteilungsLeiter_ID + ", Sichtbar=" + this.Sichtbar + ", Raum=" + this.Raum + ", Email=" + this.Email + ", Durchwahl=" + this.Durchwahl + ", Sortierung=" + this.Sortierung + ")";
	}

}
