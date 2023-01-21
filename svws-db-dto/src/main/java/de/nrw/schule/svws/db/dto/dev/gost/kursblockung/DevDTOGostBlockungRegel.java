package de.nrw.schule.svws.db.dto.dev.gost.kursblockung;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.kursblockung.GostKursblockungRegelTypConverter;

import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;


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
import de.nrw.schule.svws.csv.converter.current.kursblockung.GostKursblockungRegelTypConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.kursblockung.GostKursblockungRegelTypConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Regeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Regeln")
@NamedQuery(name="DevDTOGostBlockungRegel.all", query="SELECT e FROM DevDTOGostBlockungRegel e")
@NamedQuery(name="DevDTOGostBlockungRegel.id", query="SELECT e FROM DevDTOGostBlockungRegel e WHERE e.ID = :value")
@NamedQuery(name="DevDTOGostBlockungRegel.id.multiple", query="SELECT e FROM DevDTOGostBlockungRegel e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOGostBlockungRegel.blockung_id", query="SELECT e FROM DevDTOGostBlockungRegel e WHERE e.Blockung_ID = :value")
@NamedQuery(name="DevDTOGostBlockungRegel.blockung_id.multiple", query="SELECT e FROM DevDTOGostBlockungRegel e WHERE e.Blockung_ID IN :value")
@NamedQuery(name="DevDTOGostBlockungRegel.typ", query="SELECT e FROM DevDTOGostBlockungRegel e WHERE e.Typ = :value")
@NamedQuery(name="DevDTOGostBlockungRegel.typ.multiple", query="SELECT e FROM DevDTOGostBlockungRegel e WHERE e.Typ IN :value")
@NamedQuery(name="DevDTOGostBlockungRegel.primaryKeyQuery", query="SELECT e FROM DevDTOGostBlockungRegel e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOGostBlockungRegel.all.migration", query="SELECT e FROM DevDTOGostBlockungRegel e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Blockung_ID","Typ"})
public class DevDTOGostBlockungRegel {

	/** ID der Regel (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Blockung */
	@Column(name = "Blockung_ID")
	@JsonProperty
	public Long Blockung_ID;

	/** Die ID des Typs der Regeldefinition (siehe Core-Type GostKursblockungRegeltyp) */
	@Column(name = "Typ")
	@JsonProperty
	@Convert(converter=GostKursblockungRegelTypConverter.class)
	@JsonSerialize(using=GostKursblockungRegelTypConverterSerializer.class)
	@JsonDeserialize(using=GostKursblockungRegelTypConverterDeserializer.class)
	public GostKursblockungRegelTyp Typ;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostBlockungRegel ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostBlockungRegel() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostBlockungRegel ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Blockung_ID   der Wert für das Attribut Blockung_ID
	 * @param Typ   der Wert für das Attribut Typ
	 */
	public DevDTOGostBlockungRegel(final Long ID, final Long Blockung_ID, final GostKursblockungRegelTyp Typ) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Blockung_ID == null) { 
			throw new NullPointerException("Blockung_ID must not be null");
		}
		this.Blockung_ID = Blockung_ID;
		if (Typ == null) { 
			throw new NullPointerException("Typ must not be null");
		}
		this.Typ = Typ;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOGostBlockungRegel other = (DevDTOGostBlockungRegel) obj;
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
		return "DevDTOGostBlockungRegel(ID=" + this.ID + ", Blockung_ID=" + this.Blockung_ID + ", Typ=" + this.Typ + ")";
	}

}