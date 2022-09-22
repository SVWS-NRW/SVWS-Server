package de.nrw.schule.svws.db.dto.current.schild.erzieher;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerTelefone.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerTelefone")
@NamedQuery(name="DTOSchuelerTelefon.all", query="SELECT e FROM DTOSchuelerTelefon e")
@NamedQuery(name="DTOSchuelerTelefon.id", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.ID = :value")
@NamedQuery(name="DTOSchuelerTelefon.id.multiple", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchuelerTelefon.schueler_id", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DTOSchuelerTelefon.schueler_id.multiple", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DTOSchuelerTelefon.telefonart_id", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.TelefonArt_ID = :value")
@NamedQuery(name="DTOSchuelerTelefon.telefonart_id.multiple", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.TelefonArt_ID IN :value")
@NamedQuery(name="DTOSchuelerTelefon.telefonnummer", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Telefonnummer = :value")
@NamedQuery(name="DTOSchuelerTelefon.telefonnummer.multiple", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Telefonnummer IN :value")
@NamedQuery(name="DTOSchuelerTelefon.bemerkung", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Bemerkung = :value")
@NamedQuery(name="DTOSchuelerTelefon.bemerkung.multiple", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Bemerkung IN :value")
@NamedQuery(name="DTOSchuelerTelefon.sortierung", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOSchuelerTelefon.sortierung.multiple", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOSchuelerTelefon.gesperrt", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Gesperrt = :value")
@NamedQuery(name="DTOSchuelerTelefon.gesperrt.multiple", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.Gesperrt IN :value")
@NamedQuery(name="DTOSchuelerTelefon.primaryKeyQuery", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchuelerTelefon.all.migration", query="SELECT e FROM DTOSchuelerTelefon e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","TelefonArt_ID","Telefonnummer","Bemerkung","Sortierung","Gesperrt"})
public class DTOSchuelerTelefon {

	/** ID des Telefonnummerneintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID des Telefonnummerneintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Art des Telefonnummerneintrags */
	@Column(name = "TelefonArt_ID")
	@JsonProperty
	public Long TelefonArt_ID;

	/** Telefonnummer des Telefonnummerneintrags */
	@Column(name = "Telefonnummer")
	@JsonProperty
	public String Telefonnummer;

	/** Bemerkung des Telefonnummerneintrags */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Sortierung des Telefonnummerneintrags */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sperrung des Telefonnummerneintrags */
	@Column(name = "Gesperrt")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gesperrt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerTelefon ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerTelefon() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerTelefon ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerTelefon(final Long ID, final Long Schueler_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerTelefon other = (DTOSchuelerTelefon) obj;
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
		return "DTOSchuelerTelefon(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", TelefonArt_ID=" + this.TelefonArt_ID + ", Telefonnummer=" + this.Telefonnummer + ", Bemerkung=" + this.Bemerkung + ", Sortierung=" + this.Sortierung + ", Gesperrt=" + this.Gesperrt + ")";
	}

}