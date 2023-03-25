package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Abteilungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Abteilungen")
@NamedQuery(name="MigrationDTOAbteilungen.all", query="SELECT e FROM MigrationDTOAbteilungen e")
@NamedQuery(name="MigrationDTOAbteilungen.id", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOAbteilungen.id.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.bezeichnung", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOAbteilungen.bezeichnung.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.schuljahresabschnitts_id", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="MigrationDTOAbteilungen.schuljahresabschnitts_id.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.abteilungsleiter_id", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.AbteilungsLeiter_ID = :value")
@NamedQuery(name="MigrationDTOAbteilungen.abteilungsleiter_id.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.AbteilungsLeiter_ID IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.sichtbar", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOAbteilungen.sichtbar.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.raum", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Raum = :value")
@NamedQuery(name="MigrationDTOAbteilungen.raum.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Raum IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.email", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Email = :value")
@NamedQuery(name="MigrationDTOAbteilungen.email.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Email IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.durchwahl", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Durchwahl = :value")
@NamedQuery(name="MigrationDTOAbteilungen.durchwahl.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Durchwahl IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.sortierung", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOAbteilungen.sortierung.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.abteilungsleiter", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.AbteilungsLeiter = :value")
@NamedQuery(name="MigrationDTOAbteilungen.abteilungsleiter.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.AbteilungsLeiter IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.schulnreigner", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOAbteilungen.schulnreigner.multiple", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOAbteilungen.primaryKeyQuery", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOAbteilungen.all.migration", query="SELECT e FROM MigrationDTOAbteilungen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Schuljahresabschnitts_ID","AbteilungsLeiter_ID","Sichtbar","Raum","Email","Durchwahl","Sortierung","AbteilungsLeiter","SchulnrEigner"})
public class MigrationDTOAbteilungen {

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
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
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

	/** DEPRECATED: Lehrer-Kürzel für den Abteilungsleiter */
	@Column(name = "AbteilungsLeiter")
	@JsonProperty
	public String AbteilungsLeiter;

	/** DEPRECATED: Schulnummer zu der der Datensatz gehört */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAbteilungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAbteilungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAbteilungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 */
	public MigrationDTOAbteilungen(final Long ID, final String Bezeichnung, final Long Schuljahresabschnitts_ID) {
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
		MigrationDTOAbteilungen other = (MigrationDTOAbteilungen) obj;
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
		return "MigrationDTOAbteilungen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", AbteilungsLeiter_ID=" + this.AbteilungsLeiter_ID + ", Sichtbar=" + this.Sichtbar + ", Raum=" + this.Raum + ", Email=" + this.Email + ", Durchwahl=" + this.Durchwahl + ", Sortierung=" + this.Sortierung + ", AbteilungsLeiter=" + this.AbteilungsLeiter + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}