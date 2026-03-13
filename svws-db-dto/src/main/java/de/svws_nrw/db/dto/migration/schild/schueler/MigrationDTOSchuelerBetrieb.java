package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationDatumConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schueler_AllgAdr.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schueler_AllgAdr")
@JsonPropertyOrder({"id", "idSchueler", "idBetrieb", "idBeschaeftigungsart", "vertragsbeginn", "vertragende", "nameAusbilder", "erhaeltAnschreiben", "istPraktikum", "sortierung", "idAnsprechpartner", "idBetreuungslehrer", "SchulnrEigner"})
public final class MigrationDTOSchuelerBetrieb {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerBetrieb e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBetrieb */
	public static final String QUERY_BY_IDBETRIEB = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idBetrieb = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBetrieb */
	public static final String QUERY_LIST_BY_IDBETRIEB = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idBetrieb IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBeschaeftigungsart */
	public static final String QUERY_BY_IDBESCHAEFTIGUNGSART = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idBeschaeftigungsart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBeschaeftigungsart */
	public static final String QUERY_LIST_BY_IDBESCHAEFTIGUNGSART = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idBeschaeftigungsart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes vertragsbeginn */
	public static final String QUERY_BY_VERTRAGSBEGINN = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.vertragsbeginn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes vertragsbeginn */
	public static final String QUERY_LIST_BY_VERTRAGSBEGINN = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.vertragsbeginn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes vertragende */
	public static final String QUERY_BY_VERTRAGENDE = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.vertragende = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes vertragende */
	public static final String QUERY_LIST_BY_VERTRAGENDE = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.vertragende IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes nameAusbilder */
	public static final String QUERY_BY_NAMEAUSBILDER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.nameAusbilder = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes nameAusbilder */
	public static final String QUERY_LIST_BY_NAMEAUSBILDER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.nameAusbilder IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes erhaeltAnschreiben */
	public static final String QUERY_BY_ERHAELTANSCHREIBEN = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.erhaeltAnschreiben = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes erhaeltAnschreiben */
	public static final String QUERY_LIST_BY_ERHAELTANSCHREIBEN = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.erhaeltAnschreiben IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istPraktikum */
	public static final String QUERY_BY_ISTPRAKTIKUM = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.istPraktikum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istPraktikum */
	public static final String QUERY_LIST_BY_ISTPRAKTIKUM = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.istPraktikum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAnsprechpartner */
	public static final String QUERY_BY_IDANSPRECHPARTNER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idAnsprechpartner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAnsprechpartner */
	public static final String QUERY_LIST_BY_IDANSPRECHPARTNER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idAnsprechpartner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBetreuungslehrer */
	public static final String QUERY_BY_IDBETREUUNGSLEHRER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idBetreuungslehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBetreuungslehrer */
	public static final String QUERY_LIST_BY_IDBETREUUNGSLEHRER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.idBetreuungslehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerBetrieb e WHERE e.SchulnrEigner IN ?1";

	/** Die ID des Betriebseintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** Die ID des Schülers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long idSchueler;

	/** Die ID des Betriebs */
	@Column(name = "Adresse_ID")
	@JsonProperty
	public Long idBetrieb;

	/** Die ID der Beschäftigungsart */
	@Column(name = "Vertragsart_ID")
	@JsonProperty
	public Long idBeschaeftigungsart;

	/** Das Datum des Vertragsbeginns */
	@Column(name = "Vertragsbeginn")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String vertragsbeginn;

	/** Das Datum des Vertragsendes */
	@Column(name = "Vertragsende")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String vertragende;

	/** Der Name des Ausbilders */
	@Column(name = "Ausbilder")
	@JsonProperty
	public String nameAusbilder;

	/** Betrieb erhält Anschreiben */
	@Column(name = "AllgAdrAnschreiben")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean erhaeltAnschreiben;

	/** Gibt an ob es ein Praktikum ist */
	@Column(name = "Praktikum")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean istPraktikum;

	/** Die Sortierung des Betriebseintrags */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** Die ID des Ansprechpartners */
	@Column(name = "Ansprechpartner_ID")
	@JsonProperty
	public Long idAnsprechpartner;

	/** Die ID des Betreuungslehrers */
	@Column(name = "Betreuungslehrer_ID")
	@JsonProperty
	public Long idBetreuungslehrer;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerBetrieb ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerBetrieb() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerBetrieb ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idSchueler   der Wert für das Attribut idSchueler
	 * @param idBetrieb   der Wert für das Attribut idBetrieb
	 */
	public MigrationDTOSchuelerBetrieb(final Long id, final Long idSchueler, final Long idBetrieb) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
		this.id = id;
		if (idSchueler == null) {
			throw new NullPointerException("idSchueler must not be null");
		}
		this.idSchueler = idSchueler;
		if (idBetrieb == null) {
			throw new NullPointerException("idBetrieb must not be null");
		}
		this.idBetrieb = idBetrieb;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerBetrieb other = (MigrationDTOSchuelerBetrieb) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
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
		return "MigrationDTOSchuelerBetrieb(id=" + this.id + ", idSchueler=" + this.idSchueler + ", idBetrieb=" + this.idBetrieb + ", idBeschaeftigungsart=" + this.idBeschaeftigungsart + ", vertragsbeginn=" + this.vertragsbeginn + ", vertragende=" + this.vertragende + ", nameAusbilder=" + this.nameAusbilder + ", erhaeltAnschreiben=" + this.erhaeltAnschreiben + ", istPraktikum=" + this.istPraktikum + ", sortierung=" + this.sortierung + ", idAnsprechpartner=" + this.idAnsprechpartner + ", idBetreuungslehrer=" + this.idBetreuungslehrer + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
