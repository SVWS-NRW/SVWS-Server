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
 * Diese Klasse dient als DTO für die Datenbanktabelle Katalog_Pausenzeiten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Katalog_Pausenzeiten")
@NamedQuery(name = "DTOKatalogPausenzeit.all", query = "SELECT e FROM DTOKatalogPausenzeit e")
@NamedQuery(name = "DTOKatalogPausenzeit.id", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.ID = :value")
@NamedQuery(name = "DTOKatalogPausenzeit.id.multiple", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKatalogPausenzeit.tag", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.Tag = :value")
@NamedQuery(name = "DTOKatalogPausenzeit.tag.multiple", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.Tag IN :value")
@NamedQuery(name = "DTOKatalogPausenzeit.beginn", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.Beginn = :value")
@NamedQuery(name = "DTOKatalogPausenzeit.beginn.multiple", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.Beginn IN :value")
@NamedQuery(name = "DTOKatalogPausenzeit.ende", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.Ende = :value")
@NamedQuery(name = "DTOKatalogPausenzeit.ende.multiple", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.Ende IN :value")
@NamedQuery(name = "DTOKatalogPausenzeit.primaryKeyQuery", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.ID = ?1")
@NamedQuery(name = "DTOKatalogPausenzeit.all.migration", query = "SELECT e FROM DTOKatalogPausenzeit e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Tag", "Beginn", "Ende"})
public final class DTOKatalogPausenzeit {

	/** Eine ID, die einen Pausenzeit-Eintrag eindeutig identifiziert */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Wochentag laut ISO-8601 Standard: (1 - Montag, 2 - Dienstag, ...) */
	@Column(name = "Tag")
	@JsonProperty
	public int Tag;

	/** Die Uhrzeit, wann die Pausenzeit beginnt */
	@Column(name = "Beginn")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Beginn;

	/** Die Uhrzeit, wann die Pausenzeit endet */
	@Column(name = "Ende")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Ende;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogPausenzeit ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKatalogPausenzeit() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogPausenzeit ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Tag   der Wert für das Attribut Tag
	 * @param Beginn   der Wert für das Attribut Beginn
	 * @param Ende   der Wert für das Attribut Ende
	 */
	public DTOKatalogPausenzeit(final long ID, final int Tag, final String Beginn, final String Ende) {
		this.ID = ID;
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKatalogPausenzeit other = (DTOKatalogPausenzeit) obj;
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
		return "DTOKatalogPausenzeit(ID=" + this.ID + ", Tag=" + this.Tag + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ")";
	}

}
