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
 * Diese Klasse dient als DTO für die Datenbanktabelle Katalog_Pausenzeiten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Katalog_Pausenzeiten")
@NamedQuery(name="DevDTOKatalogPausenzeit.all", query="SELECT e FROM DevDTOKatalogPausenzeit e")
@NamedQuery(name="DevDTOKatalogPausenzeit.id", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.ID = :value")
@NamedQuery(name="DevDTOKatalogPausenzeit.id.multiple", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOKatalogPausenzeit.tag", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.Tag = :value")
@NamedQuery(name="DevDTOKatalogPausenzeit.tag.multiple", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.Tag IN :value")
@NamedQuery(name="DevDTOKatalogPausenzeit.beginn", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.Beginn = :value")
@NamedQuery(name="DevDTOKatalogPausenzeit.beginn.multiple", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.Beginn IN :value")
@NamedQuery(name="DevDTOKatalogPausenzeit.ende", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.Ende = :value")
@NamedQuery(name="DevDTOKatalogPausenzeit.ende.multiple", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.Ende IN :value")
@NamedQuery(name="DevDTOKatalogPausenzeit.primaryKeyQuery", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOKatalogPausenzeit.all.migration", query="SELECT e FROM DevDTOKatalogPausenzeit e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Tag","Beginn","Ende"})
public class DevDTOKatalogPausenzeit {

	/** Eine ID, die einen Pausenzeit-Eintrag eindeutig identifiziert */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Wochentag laut ISO-8601 Standard: (1 - Montag, 2 - Dienstag, ...) */
	@Column(name = "Tag")
	@JsonProperty
	public Integer Tag;

	/** Die Uhrzeit, wann die Pausenzeit beginnt */
	@Column(name = "Beginn")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Beginn;

	/** Die Uhrzeit, wann die Pausenzeit endet */
	@Column(name = "Ende")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Ende;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKatalogPausenzeit ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOKatalogPausenzeit() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKatalogPausenzeit ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Tag   der Wert für das Attribut Tag
	 * @param Beginn   der Wert für das Attribut Beginn
	 * @param Ende   der Wert für das Attribut Ende
	 */
	public DevDTOKatalogPausenzeit(final Long ID, final Integer Tag, final String Beginn, final String Ende) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Tag == null) { 
			throw new NullPointerException("Tag must not be null");
		}
		this.Tag = Tag;
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
		DevDTOKatalogPausenzeit other = (DevDTOKatalogPausenzeit) obj;
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
		return "DevDTOKatalogPausenzeit(ID=" + this.ID + ", Tag=" + this.Tag + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ")";
	}

}