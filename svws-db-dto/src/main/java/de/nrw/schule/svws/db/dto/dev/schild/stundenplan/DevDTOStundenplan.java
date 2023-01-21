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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan")
@NamedQuery(name="DevDTOStundenplan.all", query="SELECT e FROM DevDTOStundenplan e")
@NamedQuery(name="DevDTOStundenplan.id", query="SELECT e FROM DevDTOStundenplan e WHERE e.ID = :value")
@NamedQuery(name="DevDTOStundenplan.id.multiple", query="SELECT e FROM DevDTOStundenplan e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOStundenplan.schuljahresabschnitts_id", query="SELECT e FROM DevDTOStundenplan e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="DevDTOStundenplan.schuljahresabschnitts_id.multiple", query="SELECT e FROM DevDTOStundenplan e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="DevDTOStundenplan.beginn", query="SELECT e FROM DevDTOStundenplan e WHERE e.Beginn = :value")
@NamedQuery(name="DevDTOStundenplan.beginn.multiple", query="SELECT e FROM DevDTOStundenplan e WHERE e.Beginn IN :value")
@NamedQuery(name="DevDTOStundenplan.ende", query="SELECT e FROM DevDTOStundenplan e WHERE e.Ende = :value")
@NamedQuery(name="DevDTOStundenplan.ende.multiple", query="SELECT e FROM DevDTOStundenplan e WHERE e.Ende IN :value")
@NamedQuery(name="DevDTOStundenplan.beschreibung", query="SELECT e FROM DevDTOStundenplan e WHERE e.Beschreibung = :value")
@NamedQuery(name="DevDTOStundenplan.beschreibung.multiple", query="SELECT e FROM DevDTOStundenplan e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DevDTOStundenplan.primaryKeyQuery", query="SELECT e FROM DevDTOStundenplan e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOStundenplan.all.migration", query="SELECT e FROM DevDTOStundenplan e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schuljahresabschnitts_ID","Beginn","Ende","Beschreibung"})
public class DevDTOStundenplan {

	/** Die ID des Stundenplans */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die ID des Schuljahresabschnittes des Stundenplans als Fremdschlüssel auf die Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Das Datum, ab dem der Stundenplan gültig ist */
	@Column(name = "Beginn")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Beginn;

	/** Das Datum, bis wann der Stundenplan gültig ist - null, wenn kein Ende innerhalb des Abschnitts festgelegt wurde (letzter Stundenplan im Abschnitt) */
	@Column(name = "Ende")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Ende;

	/** Eine Beschreibung / Kommentar zu diesem Stundenplan */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOStundenplan ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOStundenplan() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOStundenplan ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param Beginn   der Wert für das Attribut Beginn
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public DevDTOStundenplan(final Long ID, final Long Schuljahresabschnitts_ID, final String Beginn, final String Beschreibung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schuljahresabschnitts_ID == null) { 
			throw new NullPointerException("Schuljahresabschnitts_ID must not be null");
		}
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (Beginn == null) { 
			throw new NullPointerException("Beginn must not be null");
		}
		this.Beginn = Beginn;
		if (Beschreibung == null) { 
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOStundenplan other = (DevDTOStundenplan) obj;
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
		return "DevDTOStundenplan(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", Beschreibung=" + this.Beschreibung + ")";
	}

}