package de.svws_nrw.db.dto.current.schild.stundenplan;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan")
@JsonPropertyOrder({"ID", "Schuljahresabschnitts_ID", "Beginn", "Ende", "Beschreibung", "WochentypModell"})
public final class DTOStundenplan {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundenplan e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundenplan e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundenplan e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundenplan e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundenplan e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundenplan e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOStundenplan e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOStundenplan e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beginn */
	public static final String QUERY_BY_BEGINN = "SELECT e FROM DTOStundenplan e WHERE e.Beginn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beginn */
	public static final String QUERY_LIST_BY_BEGINN = "SELECT e FROM DTOStundenplan e WHERE e.Beginn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ende */
	public static final String QUERY_BY_ENDE = "SELECT e FROM DTOStundenplan e WHERE e.Ende = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ende */
	public static final String QUERY_LIST_BY_ENDE = "SELECT e FROM DTOStundenplan e WHERE e.Ende IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM DTOStundenplan e WHERE e.Beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM DTOStundenplan e WHERE e.Beschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochentypModell */
	public static final String QUERY_BY_WOCHENTYPMODELL = "SELECT e FROM DTOStundenplan e WHERE e.WochentypModell = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochentypModell */
	public static final String QUERY_LIST_BY_WOCHENTYPMODELL = "SELECT e FROM DTOStundenplan e WHERE e.WochentypModell IN ?1";

	/** Die ID des Stundenplans */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Schuljahresabschnittes des Stundenplans als Fremdschlüssel auf die Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public long Schuljahresabschnitts_ID;

	/** Das Datum, ab dem der Stundenplan gültig ist */
	@Column(name = "Beginn")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Beginn;

	/** Das Datum, bis wann der Stundenplan gültig ist - null, wenn kein Ende innerhalb des Abschnitts festgelegt wurde (letzter Stundenplan im Abschnitt) */
	@Column(name = "Ende")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Ende;

	/** Eine Beschreibung / Kommentar zu diesem Stundenplan */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Gibt das Modell für die Wochen an, d.h. ob es sich um einen Stundenplan für jede Woche handelt (0) oder ob es sich um einen unterschiedliche Stundenpläne in Abhängigkeit des Wochentyps handelt - z.B. A-/B-Wochen (2) handelt. Hier wird dann die Anzahl der unterschiedlichen Wochentypen festgelegt. */
	@Column(name = "WochentypModell")
	@JsonProperty
	public int WochentypModell;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplan ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplan() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplan ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param Beginn   der Wert für das Attribut Beginn
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 * @param WochentypModell   der Wert für das Attribut WochentypModell
	 */
	public DTOStundenplan(final long ID, final long Schuljahresabschnitts_ID, final String Beginn, final String Beschreibung, final int WochentypModell) {
		this.ID = ID;
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (Beginn == null) {
			throw new NullPointerException("Beginn must not be null");
		}
		this.Beginn = Beginn;
		if (Beschreibung == null) {
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
		this.WochentypModell = WochentypModell;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplan other = (DTOStundenplan) obj;
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
		return "DTOStundenplan(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", Beschreibung=" + this.Beschreibung + ", WochentypModell=" + this.WochentypModell + ")";
	}

}
