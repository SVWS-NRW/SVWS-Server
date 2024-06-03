package de.svws_nrw.db.dto.current.schild.stundenplan;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Pausenzeit.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Pausenzeit")
@JsonPropertyOrder({"ID", "Stundenplan_ID", "Tag", "Beginn", "Ende", "Bezeichnung"})
public final class DTOStundenplanPausenzeit {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundenplanPausenzeit e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundenplan_ID */
	public static final String QUERY_BY_STUNDENPLAN_ID = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Stundenplan_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundenplan_ID */
	public static final String QUERY_LIST_BY_STUNDENPLAN_ID = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Stundenplan_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Tag */
	public static final String QUERY_BY_TAG = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Tag = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Tag */
	public static final String QUERY_LIST_BY_TAG = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Tag IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beginn */
	public static final String QUERY_BY_BEGINN = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Beginn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beginn */
	public static final String QUERY_LIST_BY_BEGINN = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Beginn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ende */
	public static final String QUERY_BY_ENDE = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Ende = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ende */
	public static final String QUERY_LIST_BY_ENDE = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Ende IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOStundenplanPausenzeit e WHERE e.Bezeichnung IN ?1";

	/** Eine ID, die einen Pausenzeit-Eintrag eindeutig identifiziert - hat keinen Bezug zur ID der Katalog-Tabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Stundenplans, dem dies Pausenzeit zugeordnet ist */
	@Column(name = "Stundenplan_ID")
	@JsonProperty
	public long Stundenplan_ID;

	/** Der Wochentag laut ISO-8601 Standard: (1 - Montag, 2 - Dienstag, ...) */
	@Column(name = "Tag")
	@JsonProperty
	public int Tag;

	/** Die Uhrzeit, wann die Pausenzeit beginnt */
	@Column(name = "Beginn")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public Integer Beginn;

	/** Die Uhrzeit, wann die Pausenzeit endet */
	@Column(name = "Ende")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public Integer Ende;

	/** Eine kurze Bezeichnung, welche die Art der Pausenzeit genauer beschreibt (z.B. Mittagspause) */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenzeit ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanPausenzeit() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenzeit ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundenplan_ID   der Wert für das Attribut Stundenplan_ID
	 * @param Tag   der Wert für das Attribut Tag
	 * @param Beginn   der Wert für das Attribut Beginn
	 * @param Ende   der Wert für das Attribut Ende
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOStundenplanPausenzeit(final long ID, final long Stundenplan_ID, final int Tag, final Integer Beginn, final Integer Ende, final String Bezeichnung) {
		this.ID = ID;
		this.Stundenplan_ID = Stundenplan_ID;
		this.Tag = Tag;
		if (Beginn == null) {
			throw new NullPointerException("Beginn must not be null");
		}
		this.Beginn = Beginn;
		if (Ende == null) {
			throw new NullPointerException("Ende must not be null");
		}
		this.Ende = Ende;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanPausenzeit other = (DTOStundenplanPausenzeit) obj;
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
		return "DTOStundenplanPausenzeit(ID=" + this.ID + ", Stundenplan_ID=" + this.Stundenplan_ID + ", Tag=" + this.Tag + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", Bezeichnung=" + this.Bezeichnung + ")";
	}

}
