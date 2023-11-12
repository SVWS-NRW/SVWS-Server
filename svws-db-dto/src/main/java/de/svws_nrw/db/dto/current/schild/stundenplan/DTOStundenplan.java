package de.svws_nrw.db.dto.current.schild.stundenplan;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumConverter;


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
@NamedQuery(name = "DTOStundenplan.all", query = "SELECT e FROM DTOStundenplan e")
@NamedQuery(name = "DTOStundenplan.id", query = "SELECT e FROM DTOStundenplan e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplan.id.multiple", query = "SELECT e FROM DTOStundenplan e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplan.schuljahresabschnitts_id", query = "SELECT e FROM DTOStundenplan e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name = "DTOStundenplan.schuljahresabschnitts_id.multiple", query = "SELECT e FROM DTOStundenplan e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name = "DTOStundenplan.beginn", query = "SELECT e FROM DTOStundenplan e WHERE e.Beginn = :value")
@NamedQuery(name = "DTOStundenplan.beginn.multiple", query = "SELECT e FROM DTOStundenplan e WHERE e.Beginn IN :value")
@NamedQuery(name = "DTOStundenplan.ende", query = "SELECT e FROM DTOStundenplan e WHERE e.Ende = :value")
@NamedQuery(name = "DTOStundenplan.ende.multiple", query = "SELECT e FROM DTOStundenplan e WHERE e.Ende IN :value")
@NamedQuery(name = "DTOStundenplan.beschreibung", query = "SELECT e FROM DTOStundenplan e WHERE e.Beschreibung = :value")
@NamedQuery(name = "DTOStundenplan.beschreibung.multiple", query = "SELECT e FROM DTOStundenplan e WHERE e.Beschreibung IN :value")
@NamedQuery(name = "DTOStundenplan.wochentypmodell", query = "SELECT e FROM DTOStundenplan e WHERE e.WochentypModell = :value")
@NamedQuery(name = "DTOStundenplan.wochentypmodell.multiple", query = "SELECT e FROM DTOStundenplan e WHERE e.WochentypModell IN :value")
@NamedQuery(name = "DTOStundenplan.primaryKeyQuery", query = "SELECT e FROM DTOStundenplan e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplan.primaryKeyQuery.multiple", query = "SELECT e FROM DTOStundenplan e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOStundenplan.all.migration", query = "SELECT e FROM DTOStundenplan e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schuljahresabschnitts_ID", "Beginn", "Ende", "Beschreibung", "WochentypModell"})
public final class DTOStundenplan {

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
