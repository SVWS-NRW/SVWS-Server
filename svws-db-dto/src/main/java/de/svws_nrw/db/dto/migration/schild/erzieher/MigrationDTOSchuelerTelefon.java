package de.svws_nrw.db.dto.migration.schild.erzieher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerTelefone.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerTelefone")
@NamedQuery(name="MigrationDTOSchuelerTelefon.all", query="SELECT e FROM MigrationDTOSchuelerTelefon e")
@NamedQuery(name="MigrationDTOSchuelerTelefon.id", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.id.multiple", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.schueler_id", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Schueler_ID = :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.schueler_id.multiple", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.telefonart_id", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.TelefonArt_ID = :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.telefonart_id.multiple", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.TelefonArt_ID IN :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.telefonnummer", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Telefonnummer = :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.telefonnummer.multiple", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Telefonnummer IN :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.bemerkung", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Bemerkung = :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.bemerkung.multiple", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Bemerkung IN :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.sortierung", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.sortierung.multiple", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.schulnreigner", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.schulnreigner.multiple", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.gesperrt", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Gesperrt = :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.gesperrt.multiple", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.Gesperrt IN :value")
@NamedQuery(name="MigrationDTOSchuelerTelefon.primaryKeyQuery", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOSchuelerTelefon.all.migration", query="SELECT e FROM MigrationDTOSchuelerTelefon e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","TelefonArt_ID","Telefonnummer","Bemerkung","Sortierung","SchulnrEigner","Gesperrt"})
public class MigrationDTOSchuelerTelefon {

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

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Sperrung des Telefonnummerneintrags */
	@Column(name = "Gesperrt")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gesperrt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerTelefon ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerTelefon() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerTelefon ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerTelefon(final Long ID, final Long Schueler_ID) {
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
		MigrationDTOSchuelerTelefon other = (MigrationDTOSchuelerTelefon) obj;
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
		return "MigrationDTOSchuelerTelefon(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", TelefonArt_ID=" + this.TelefonArt_ID + ", Telefonnummer=" + this.Telefonnummer + ", Bemerkung=" + this.Bemerkung + ", Sortierung=" + this.Sortierung + ", SchulnrEigner=" + this.SchulnrEigner + ", Gesperrt=" + this.Gesperrt + ")";
	}

}