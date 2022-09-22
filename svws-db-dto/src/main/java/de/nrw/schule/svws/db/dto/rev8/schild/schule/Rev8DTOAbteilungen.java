package de.nrw.schule.svws.db.dto.rev8.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Abteilungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Abteilungen")
@NamedQuery(name="Rev8DTOAbteilungen.all", query="SELECT e FROM Rev8DTOAbteilungen e")
@NamedQuery(name="Rev8DTOAbteilungen.id", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOAbteilungen.id.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.bezeichnung", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOAbteilungen.bezeichnung.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.schuljahresabschnitts_id", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="Rev8DTOAbteilungen.schuljahresabschnitts_id.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.abteilungsleiter_id", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.AbteilungsLeiter_ID = :value")
@NamedQuery(name="Rev8DTOAbteilungen.abteilungsleiter_id.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.AbteilungsLeiter_ID IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.sichtbar", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev8DTOAbteilungen.sichtbar.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.raum", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Raum = :value")
@NamedQuery(name="Rev8DTOAbteilungen.raum.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Raum IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.email", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Email = :value")
@NamedQuery(name="Rev8DTOAbteilungen.email.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Email IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.durchwahl", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Durchwahl = :value")
@NamedQuery(name="Rev8DTOAbteilungen.durchwahl.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Durchwahl IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.sortierung", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOAbteilungen.sortierung.multiple", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOAbteilungen.primaryKeyQuery", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOAbteilungen.all.migration", query="SELECT e FROM Rev8DTOAbteilungen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Schuljahresabschnitts_ID","AbteilungsLeiter_ID","Sichtbar","Raum","Email","Durchwahl","Sortierung"})
public class Rev8DTOAbteilungen {

	/** ID der Abteilung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Text für die Bezeichnung der Abteilung */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Lehrer-ID für den Abteilungsleiter */
	@Column(name = "AbteilungsLeiter_ID")
	@JsonProperty
	public Long AbteilungsLeiter_ID;

	/** steuert die Sichtbarkeit der Abteilung */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Bezeichnung für den Raum des Abteilungsleiter z.B. für Briefköpfe */
	@Column(name = "Raum")
	@JsonProperty
	public String Raum;

	/** Email des Abteilungsleiters */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Telefonische Durchwahl des Abteilungsleiters */
	@Column(name = "Durchwahl")
	@JsonProperty
	public String Durchwahl;

	/** Sortierung des Datensatzes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOAbteilungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOAbteilungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOAbteilungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 */
	public Rev8DTOAbteilungen(final Long ID, final String Bezeichnung, final Long Schuljahresabschnitts_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Schuljahresabschnitts_ID == null) { 
			throw new NullPointerException("Schuljahresabschnitts_ID must not be null");
		}
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOAbteilungen other = (Rev8DTOAbteilungen) obj;
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
		return "Rev8DTOAbteilungen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", AbteilungsLeiter_ID=" + this.AbteilungsLeiter_ID + ", Sichtbar=" + this.Sichtbar + ", Raum=" + this.Raum + ", Email=" + this.Email + ", Durchwahl=" + this.Durchwahl + ", Sortierung=" + this.Sortierung + ")";
	}

}