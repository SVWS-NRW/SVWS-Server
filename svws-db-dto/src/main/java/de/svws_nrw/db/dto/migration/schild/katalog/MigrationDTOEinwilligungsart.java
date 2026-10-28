package de.svws_nrw.db.dto.migration.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Datenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Datenschutz")
@JsonPropertyOrder({"id", "bezeichnung", "istSichtbar", "schluessel", "SchulnrEigner", "sortierung", "beschreibung", "personTyp"})
public final class MigrationDTOEinwilligungsart {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOEinwilligungsart e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSichtbar */
	public static final String QUERY_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.istSichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSichtbar */
	public static final String QUERY_LIST_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.istSichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schluessel */
	public static final String QUERY_BY_SCHLUESSEL = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.schluessel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schluessel */
	public static final String QUERY_LIST_BY_SCHLUESSEL = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.schluessel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.beschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes personTyp */
	public static final String QUERY_BY_PERSONTYP = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.personTyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes personTyp */
	public static final String QUERY_LIST_BY_PERSONTYP = "SELECT e FROM MigrationDTOEinwilligungsart e WHERE e.personTyp IN ?1";

	/** Eindeutige ID für den Datensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** Eine kurze Bezeichnung der Einwilligungsart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String bezeichnung;

	/** Regelt die Sichtbarkeit der Einwilligungsart bei der Ansicht der Schülertabelle  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSichtbar;

	/** Fest vorgegebene Werte, die es in Schild-NRW später ermöglichen, die Einwilligungsart zu erkennen */
	@Column(name = "Schluessel")
	@JsonProperty
	public String schluessel;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Gibt die Reihenfolge der Einwilligungsarten bei der Darstellung an. */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** Eine ausführliche Beschreibung der Einwilligungsart */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String beschreibung;

	/** Personentyp der Einwilligungsart (S=Schueler L=Lehrer E=Erzieher) */
	@Column(name = "PersonArt")
	@JsonProperty
	public String personTyp;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEinwilligungsart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOEinwilligungsart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEinwilligungsart ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param bezeichnung   der Wert für das Attribut bezeichnung
	 * @param istSichtbar   der Wert für das Attribut istSichtbar
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param sortierung   der Wert für das Attribut sortierung
	 */
	public MigrationDTOEinwilligungsart(final Long id, final String bezeichnung, final Boolean istSichtbar, final Integer SchulnrEigner, final Integer sortierung) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
		this.id = id;
		if (bezeichnung == null) {
			throw new NullPointerException("bezeichnung must not be null");
		}
		this.bezeichnung = bezeichnung;
		if (istSichtbar == null) {
			throw new NullPointerException("istSichtbar must not be null");
		}
		this.istSichtbar = istSichtbar;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (sortierung == null) {
			throw new NullPointerException("sortierung must not be null");
		}
		this.sortierung = sortierung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MigrationDTOEinwilligungsart other = (MigrationDTOEinwilligungsart) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOEinwilligungsart(id=" + this.id + ", bezeichnung=" + this.bezeichnung + ", istSichtbar=" + this.istSichtbar + ", schluessel=" + this.schluessel + ", SchulnrEigner=" + this.SchulnrEigner + ", sortierung=" + this.sortierung + ", beschreibung=" + this.beschreibung + ", personTyp=" + this.personTyp + ")";
	}

}
