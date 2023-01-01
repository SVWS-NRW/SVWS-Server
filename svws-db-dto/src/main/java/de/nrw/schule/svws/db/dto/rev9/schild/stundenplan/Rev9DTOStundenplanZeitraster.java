package de.nrw.schule.svws.db.dto.rev9.schild.stundenplan;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.UhrzeitConverter;


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
import de.nrw.schule.svws.csv.converter.current.UhrzeitConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.UhrzeitConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Zeitraster.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Zeitraster")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.all", query="SELECT e FROM Rev9DTOStundenplanZeitraster e")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.id", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.id.multiple", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.stundenplan_id", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Stundenplan_ID = :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.stundenplan_id.multiple", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Stundenplan_ID IN :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.tag", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Tag = :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.tag.multiple", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Tag IN :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.stunde", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Stunde = :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.stunde.multiple", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Stunde IN :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.beginn", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Beginn = :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.beginn.multiple", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Beginn IN :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.ende", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Ende = :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.ende.multiple", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.Ende IN :value")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.primaryKeyQuery", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOStundenplanZeitraster.all.migration", query="SELECT e FROM Rev9DTOStundenplanZeitraster e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Stundenplan_ID","Tag","Stunde","Beginn","Ende"})
public class Rev9DTOStundenplanZeitraster {

	/** Eine ID, die einen Zeitraster-Eintrag eindeutig identifiziert - hat keinen Bezug zur ID der Katalog-Tabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die ID des Stundenplans, dem dieses Zeitraster zugeordnet ist */
	@Column(name = "Stundenplan_ID")
	@JsonProperty
	public Long Stundenplan_ID;

	/** Der Wochentag laut ISO-8601 Standard: (1 - Montag, 2 - Dienstag, ...) */
	@Column(name = "Tag")
	@JsonProperty
	public Integer Tag;

	/** Die Stunde laut Stundenplan (1, 2, ...) */
	@Column(name = "Stunde")
	@JsonProperty
	public Integer Stunde;

	/** Die Uhrzeit, wann die Stunde beginnt */
	@Column(name = "Beginn")
	@JsonProperty
	@Convert(converter=UhrzeitConverter.class)
	@JsonSerialize(using=UhrzeitConverterSerializer.class)
	@JsonDeserialize(using=UhrzeitConverterDeserializer.class)
	public String Beginn;

	/** Die Uhrzeit, wann die Stunde endet */
	@Column(name = "Ende")
	@JsonProperty
	@Convert(converter=UhrzeitConverter.class)
	@JsonSerialize(using=UhrzeitConverterSerializer.class)
	@JsonDeserialize(using=UhrzeitConverterDeserializer.class)
	public String Ende;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOStundenplanZeitraster ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOStundenplanZeitraster() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOStundenplanZeitraster ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundenplan_ID   der Wert für das Attribut Stundenplan_ID
	 * @param Tag   der Wert für das Attribut Tag
	 * @param Stunde   der Wert für das Attribut Stunde
	 * @param Beginn   der Wert für das Attribut Beginn
	 * @param Ende   der Wert für das Attribut Ende
	 */
	public Rev9DTOStundenplanZeitraster(final Long ID, final Long Stundenplan_ID, final Integer Tag, final Integer Stunde, final String Beginn, final String Ende) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Stundenplan_ID == null) { 
			throw new NullPointerException("Stundenplan_ID must not be null");
		}
		this.Stundenplan_ID = Stundenplan_ID;
		if (Tag == null) { 
			throw new NullPointerException("Tag must not be null");
		}
		this.Tag = Tag;
		if (Stunde == null) { 
			throw new NullPointerException("Stunde must not be null");
		}
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOStundenplanZeitraster other = (Rev9DTOStundenplanZeitraster) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOStundenplanZeitraster(ID=" + this.ID + ", Stundenplan_ID=" + this.Stundenplan_ID + ", Tag=" + this.Tag + ", Stunde=" + this.Stunde + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ")";
	}

}