package de.nrw.schule.svws.db.dto.dev.schild.stundenplan;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.DatumConverter;


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
import de.nrw.schule.svws.csv.converter.current.DatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Katalog_Zeitraster.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Katalog_Zeitraster")
@NamedQuery(name="DevDTOKatalogZeitraster.all", query="SELECT e FROM DevDTOKatalogZeitraster e")
@NamedQuery(name="DevDTOKatalogZeitraster.id", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.ID = :value")
@NamedQuery(name="DevDTOKatalogZeitraster.id.multiple", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOKatalogZeitraster.tag", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.Tag = :value")
@NamedQuery(name="DevDTOKatalogZeitraster.tag.multiple", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.Tag IN :value")
@NamedQuery(name="DevDTOKatalogZeitraster.stunde", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.Stunde = :value")
@NamedQuery(name="DevDTOKatalogZeitraster.stunde.multiple", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.Stunde IN :value")
@NamedQuery(name="DevDTOKatalogZeitraster.beginn", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.Beginn = :value")
@NamedQuery(name="DevDTOKatalogZeitraster.beginn.multiple", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.Beginn IN :value")
@NamedQuery(name="DevDTOKatalogZeitraster.ende", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.Ende = :value")
@NamedQuery(name="DevDTOKatalogZeitraster.ende.multiple", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.Ende IN :value")
@NamedQuery(name="DevDTOKatalogZeitraster.primaryKeyQuery", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOKatalogZeitraster.all.migration", query="SELECT e FROM DevDTOKatalogZeitraster e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Tag","Stunde","Beginn","Ende"})
public class DevDTOKatalogZeitraster {

	/** Eine ID, die einen Zeitraster-Eintrag eindeutig identifiziert */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

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
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Beginn;

	/** Die Uhrzeit, wann die Stunde endet */
	@Column(name = "Ende")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Ende;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKatalogZeitraster ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOKatalogZeitraster() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKatalogZeitraster ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Tag   der Wert für das Attribut Tag
	 * @param Stunde   der Wert für das Attribut Stunde
	 * @param Beginn   der Wert für das Attribut Beginn
	 * @param Ende   der Wert für das Attribut Ende
	 */
	public DevDTOKatalogZeitraster(final Long ID, final Integer Tag, final Integer Stunde, final String Beginn, final String Ende) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
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
		DevDTOKatalogZeitraster other = (DevDTOKatalogZeitraster) obj;
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
		return "DevDTOKatalogZeitraster(ID=" + this.ID + ", Tag=" + this.Tag + ", Stunde=" + this.Stunde + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ")";
	}

}