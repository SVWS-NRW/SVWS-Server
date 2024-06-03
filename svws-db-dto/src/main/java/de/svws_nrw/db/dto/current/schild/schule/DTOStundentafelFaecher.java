package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundentafel_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundentafel_Faecher")
@JsonPropertyOrder({"ID", "Stundentafel_ID", "Fach_ID", "KursartAllg", "WochenStd", "Lehrer_ID", "EpochenUnterricht", "Sortierung", "Sichtbar", "Gewichtung"})
public final class DTOStundentafelFaecher {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundentafelFaecher e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundentafelFaecher e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundentafelFaecher e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundentafelFaecher e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundentafelFaecher e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundentafelFaecher e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Stundentafel_ID */
	public static final String QUERY_BY_STUNDENTAFEL_ID = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Stundentafel_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Stundentafel_ID */
	public static final String QUERY_LIST_BY_STUNDENTAFEL_ID = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Stundentafel_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KursartAllg */
	public static final String QUERY_BY_KURSARTALLG = "SELECT e FROM DTOStundentafelFaecher e WHERE e.KursartAllg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KursartAllg */
	public static final String QUERY_LIST_BY_KURSARTALLG = "SELECT e FROM DTOStundentafelFaecher e WHERE e.KursartAllg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenStd */
	public static final String QUERY_BY_WOCHENSTD = "SELECT e FROM DTOStundentafelFaecher e WHERE e.WochenStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenStd */
	public static final String QUERY_LIST_BY_WOCHENSTD = "SELECT e FROM DTOStundentafelFaecher e WHERE e.WochenStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EpochenUnterricht */
	public static final String QUERY_BY_EPOCHENUNTERRICHT = "SELECT e FROM DTOStundentafelFaecher e WHERE e.EpochenUnterricht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EpochenUnterricht */
	public static final String QUERY_LIST_BY_EPOCHENUNTERRICHT = "SELECT e FROM DTOStundentafelFaecher e WHERE e.EpochenUnterricht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gewichtung */
	public static final String QUERY_BY_GEWICHTUNG = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Gewichtung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gewichtung */
	public static final String QUERY_LIST_BY_GEWICHTUNG = "SELECT e FROM DTOStundentafelFaecher e WHERE e.Gewichtung IN ?1";

	/** ID des Facheintrags für die Stundentafel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der zugehörigen Stundentafel */
	@Column(name = "Stundentafel_ID")
	@JsonProperty
	public long Stundentafel_ID;

	/** FachID das in der Stundentafel ist */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean EpochenUnterricht;

	/** Sortierung des Faches in der Stundentafel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit des Faches in der Stundentafel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** ??? entweder BB auch oder weg ??? Gewichtung allgemeinbidend BK  des Faches in der Stundentafel */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundentafelFaecher ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundentafelFaecher() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundentafelFaecher ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundentafel_ID   der Wert für das Attribut Stundentafel_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOStundentafelFaecher(final long ID, final long Stundentafel_ID, final long Fach_ID) {
		this.ID = ID;
		this.Stundentafel_ID = Stundentafel_ID;
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
		DTOStundentafelFaecher other = (DTOStundentafelFaecher) obj;
		return ID == other.ID;
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
		return "DTOStundentafelFaecher(ID=" + this.ID + ", Stundentafel_ID=" + this.Stundentafel_ID + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", WochenStd=" + this.WochenStd + ", Lehrer_ID=" + this.Lehrer_ID + ", EpochenUnterricht=" + this.EpochenUnterricht + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Gewichtung=" + this.Gewichtung + ")";
	}

}
