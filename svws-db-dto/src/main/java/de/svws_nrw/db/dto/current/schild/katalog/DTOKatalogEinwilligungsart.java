package de.svws_nrw.db.dto.current.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.PersonTypConverter;

import de.svws_nrw.core.types.schule.PersonTyp;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.PersonTypConverterSerializer;
import de.svws_nrw.csv.converter.current.PersonTypConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Datenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Datenschutz")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sichtbar", "Schluessel", "Sortierung", "Beschreibung", "personTyp"})
public final class DTOKatalogEinwilligungsart {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKatalogEinwilligungsart e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schluessel */
	public static final String QUERY_BY_SCHLUESSEL = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Schluessel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schluessel */
	public static final String QUERY_LIST_BY_SCHLUESSEL = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Schluessel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Beschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes personTyp */
	public static final String QUERY_BY_PERSONTYP = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.personTyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes personTyp */
	public static final String QUERY_LIST_BY_PERSONTYP = "SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.personTyp IN ?1";

	/** Eindeutige ID für den Datensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Eine kurze Bezeichnung der Einwilligungsart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Regelt die Sichtbarkeit der Einwilligungsart bei der Ansicht der Schülertabelle  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Fest vorgegebene Werte, die es in Schild-NRW später ermöglichen, die Einwilligungsart zu erkennen */
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Gibt die Reihenfolge der Einwilligungsarten bei der Darstellung an. */
	@Column(name = "Sortierung")
	@JsonProperty
	public int Sortierung;

	/** Eine ausführliche Beschreibung der Einwilligungsart */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Personentyp der Einwilligungsart (S=Schueler L=Lehrer E=Erzieher) */
	@Column(name = "PersonArt")
	@JsonProperty
	@Convert(converter = PersonTypConverter.class)
	@JsonSerialize(using = PersonTypConverterSerializer.class)
	@JsonDeserialize(using = PersonTypConverterDeserializer.class)
	public PersonTyp personTyp;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogEinwilligungsart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKatalogEinwilligungsart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogEinwilligungsart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Sichtbar   der Wert für das Attribut Sichtbar
	 * @param Sortierung   der Wert für das Attribut Sortierung
	 */
	public DTOKatalogEinwilligungsart(final long ID, final String Bezeichnung, final Boolean Sichtbar, final int Sortierung) {
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Sichtbar == null) {
			throw new NullPointerException("Sichtbar must not be null");
		}
		this.Sichtbar = Sichtbar;
		this.Sortierung = Sortierung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKatalogEinwilligungsart other = (DTOKatalogEinwilligungsart) obj;
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
		return "DTOKatalogEinwilligungsart(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sichtbar=" + this.Sichtbar + ", Schluessel=" + this.Schluessel + ", Sortierung=" + this.Sortierung + ", Beschreibung=" + this.Beschreibung + ", personTyp=" + this.personTyp + ")";
	}

}
