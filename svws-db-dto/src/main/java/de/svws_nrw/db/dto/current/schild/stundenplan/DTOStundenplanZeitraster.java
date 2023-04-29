package de.svws_nrw.db.dto.current.schild.stundenplan;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Zeitraster.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Zeitraster")
@NamedQuery(name = "DTOStundenplanZeitraster.all", query = "SELECT e FROM DTOStundenplanZeitraster e")
@NamedQuery(name = "DTOStundenplanZeitraster.id", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanZeitraster.id.multiple", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanZeitraster.stundenplan_id", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Stundenplan_ID = :value")
@NamedQuery(name = "DTOStundenplanZeitraster.stundenplan_id.multiple", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Stundenplan_ID IN :value")
@NamedQuery(name = "DTOStundenplanZeitraster.tag", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Tag = :value")
@NamedQuery(name = "DTOStundenplanZeitraster.tag.multiple", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Tag IN :value")
@NamedQuery(name = "DTOStundenplanZeitraster.stunde", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Stunde = :value")
@NamedQuery(name = "DTOStundenplanZeitraster.stunde.multiple", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Stunde IN :value")
@NamedQuery(name = "DTOStundenplanZeitraster.beginn", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Beginn = :value")
@NamedQuery(name = "DTOStundenplanZeitraster.beginn.multiple", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Beginn IN :value")
@NamedQuery(name = "DTOStundenplanZeitraster.ende", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Ende = :value")
@NamedQuery(name = "DTOStundenplanZeitraster.ende.multiple", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.Ende IN :value")
@NamedQuery(name = "DTOStundenplanZeitraster.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanZeitraster.all.migration", query = "SELECT e FROM DTOStundenplanZeitraster e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Stundenplan_ID", "Tag", "Stunde", "Beginn", "Ende"})
public final class DTOStundenplanZeitraster {

	/** Eine ID, die einen Zeitraster-Eintrag eindeutig identifiziert - hat keinen Bezug zur ID der Katalog-Tabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Stundenplans, dem dieses Zeitraster zugeordnet ist */
	@Column(name = "Stundenplan_ID")
	@JsonProperty
	public long Stundenplan_ID;

	/** Der Wochentag laut ISO-8601 Standard: (1 - Montag, 2 - Dienstag, ...) */
	@Column(name = "Tag")
	@JsonProperty
	public int Tag;

	/** Die Stunde laut Stundenplan (1, 2, ...) */
	@Column(name = "Stunde")
	@JsonProperty
	public int Stunde;

	/** Die Uhrzeit, wann die Stunde beginnt */
	@Column(name = "Beginn")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public String Beginn;

	/** Die Uhrzeit, wann die Stunde endet */
	@Column(name = "Ende")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public String Ende;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanZeitraster ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanZeitraster() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanZeitraster ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundenplan_ID   der Wert für das Attribut Stundenplan_ID
	 * @param Tag   der Wert für das Attribut Tag
	 * @param Stunde   der Wert für das Attribut Stunde
	 * @param Beginn   der Wert für das Attribut Beginn
	 * @param Ende   der Wert für das Attribut Ende
	 */
	public DTOStundenplanZeitraster(final long ID, final long Stundenplan_ID, final int Tag, final int Stunde, final String Beginn, final String Ende) {
		this.ID = ID;
		this.Stundenplan_ID = Stundenplan_ID;
		this.Tag = Tag;
		this.Stunde = Stunde;
		if (Beginn == null) {
			throw new NullPointerException("Beginn must not be null");
		}
		this.Beginn = Beginn;
		if (Ende == null) {
			throw new NullPointerException("Ende must not be null");
		}
		this.Ende = Ende;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanZeitraster other = (DTOStundenplanZeitraster) obj;
		if (ID != other.ID)
			return false;
		return true;
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
		return "DTOStundenplanZeitraster(ID=" + this.ID + ", Stundenplan_ID=" + this.Stundenplan_ID + ", Tag=" + this.Tag + ", Stunde=" + this.Stunde + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ")";
	}

}
