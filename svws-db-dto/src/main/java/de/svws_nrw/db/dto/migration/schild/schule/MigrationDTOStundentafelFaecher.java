package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundentafel_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundentafel_Faecher")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.all", query = "SELECT e FROM MigrationDTOStundentafelFaecher e")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.id", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.id.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.stundentafel_id", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Stundentafel_ID = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.stundentafel_id.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Stundentafel_ID IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.fach_id", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Fach_ID = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.fach_id.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.kursartallg", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.KursartAllg = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.kursartallg.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.KursartAllg IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.wochenstd", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.WochenStd = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.wochenstd.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.WochenStd IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.lehrer_id", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.lehrer_id.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.epochenunterricht", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.EpochenUnterricht = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.epochenunterricht.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.EpochenUnterricht IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.sortierung", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.sortierung.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.sichtbar", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.sichtbar.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.gewichtung", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Gewichtung = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.gewichtung.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.Gewichtung IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.schulnreigner", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.lehrerkrz", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.LehrerKrz = :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.lehrerkrz.multiple", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.LehrerKrz IN :value")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.primaryKeyQuery", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOStundentafelFaecher.all.migration", query = "SELECT e FROM MigrationDTOStundentafelFaecher e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Stundentafel_ID", "Fach_ID", "KursartAllg", "WochenStd", "Lehrer_ID", "EpochenUnterricht", "Sortierung", "Sichtbar", "Gewichtung", "SchulnrEigner", "LehrerKrz"})
public final class MigrationDTOStundentafelFaecher {

	/** ID des Facheintrags für die Stundentafel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der zugehörigen Stundentafel */
	@Column(name = "Stundentafel_ID")
	@JsonProperty
	public Long Stundentafel_ID;

	/** FachID das in der Stundentafel ist */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Kursart des Faches in der Stundentafel */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Wochenstunde des Faches in der Stundentafel */
	@Column(name = "WochenStd")
	@JsonProperty
	public Integer WochenStd;

	/** Lehrer-ID des unterrichtenden Lehrers für das Fach der Stundentafel */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Merkmal Epochenunterricht des Faches in der Stundentafel */
	@Column(name = "EpochenUnterricht")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean EpochenUnterricht;

	/** Sortierung des Faches in der Stundentafel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit des Faches in der Stundentafel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** ??? entweder BB auch oder weg ??? Gewichtung allgemeinbidend BK  des Faches in der Stundentafel */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/** DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** DEPRECATED: LehrerKRZ des Faches in der Stundetafel */
	@Column(name = "LehrerKrz")
	@JsonProperty
	public String LehrerKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStundentafelFaecher ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOStundentafelFaecher() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStundentafelFaecher ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundentafel_ID   der Wert für das Attribut Stundentafel_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public MigrationDTOStundentafelFaecher(final Long ID, final Long Stundentafel_ID, final Long Fach_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Stundentafel_ID == null) {
			throw new NullPointerException("Stundentafel_ID must not be null");
		}
		this.Stundentafel_ID = Stundentafel_ID;
		if (Fach_ID == null) {
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOStundentafelFaecher other = (MigrationDTOStundentafelFaecher) obj;
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
		return "MigrationDTOStundentafelFaecher(ID=" + this.ID + ", Stundentafel_ID=" + this.Stundentafel_ID + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", WochenStd=" + this.WochenStd + ", Lehrer_ID=" + this.Lehrer_ID + ", EpochenUnterricht=" + this.EpochenUnterricht + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Gewichtung=" + this.Gewichtung + ", SchulnrEigner=" + this.SchulnrEigner + ", LehrerKrz=" + this.LehrerKrz + ")";
	}

}
