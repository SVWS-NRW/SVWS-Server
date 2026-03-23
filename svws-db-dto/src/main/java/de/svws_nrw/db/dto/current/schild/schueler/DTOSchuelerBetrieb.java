package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;


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
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schueler_AllgAdr.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schueler_AllgAdr")
@JsonPropertyOrder({"id", "idSchueler", "idBetrieb", "idBeschaeftigungsart", "vertragsbeginn", "vertragsende", "nameAusbilder", "erhaeltAnschreiben", "istPraktikum", "sortierung", "idAnsprechpartner", "idBetreuungslehrer"})
public final class DTOSchuelerBetrieb {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerBetrieb e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBetrieb */
	public static final String QUERY_BY_IDBETRIEB = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idBetrieb = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBetrieb */
	public static final String QUERY_LIST_BY_IDBETRIEB = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idBetrieb IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBeschaeftigungsart */
	public static final String QUERY_BY_IDBESCHAEFTIGUNGSART = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idBeschaeftigungsart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBeschaeftigungsart */
	public static final String QUERY_LIST_BY_IDBESCHAEFTIGUNGSART = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idBeschaeftigungsart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes vertragsbeginn */
	public static final String QUERY_BY_VERTRAGSBEGINN = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.vertragsbeginn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes vertragsbeginn */
	public static final String QUERY_LIST_BY_VERTRAGSBEGINN = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.vertragsbeginn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes vertragsende */
	public static final String QUERY_BY_VERTRAGSENDE = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.vertragsende = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes vertragsende */
	public static final String QUERY_LIST_BY_VERTRAGSENDE = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.vertragsende IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes nameAusbilder */
	public static final String QUERY_BY_NAMEAUSBILDER = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.nameAusbilder = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes nameAusbilder */
	public static final String QUERY_LIST_BY_NAMEAUSBILDER = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.nameAusbilder IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes erhaeltAnschreiben */
	public static final String QUERY_BY_ERHAELTANSCHREIBEN = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.erhaeltAnschreiben = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes erhaeltAnschreiben */
	public static final String QUERY_LIST_BY_ERHAELTANSCHREIBEN = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.erhaeltAnschreiben IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istPraktikum */
	public static final String QUERY_BY_ISTPRAKTIKUM = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.istPraktikum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istPraktikum */
	public static final String QUERY_LIST_BY_ISTPRAKTIKUM = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.istPraktikum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAnsprechpartner */
	public static final String QUERY_BY_IDANSPRECHPARTNER = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idAnsprechpartner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAnsprechpartner */
	public static final String QUERY_LIST_BY_IDANSPRECHPARTNER = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idAnsprechpartner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBetreuungslehrer */
	public static final String QUERY_BY_IDBETREUUNGSLEHRER = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idBetreuungslehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBetreuungslehrer */
	public static final String QUERY_LIST_BY_IDBETREUUNGSLEHRER = "SELECT e FROM DTOSchuelerBetrieb e WHERE e.idBetreuungslehrer IN ?1";

	/** Die ID des Betriebseintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** Die ID des Schülers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long idSchueler;

	/** Die ID des Betriebs */
	@Column(name = "Adresse_ID")
	@JsonProperty
	public long idBetrieb;

	/** Die ID der Beschäftigungsart */
	@Column(name = "Vertragsart_ID")
	@JsonProperty
	public Long idBeschaeftigungsart;

	/** Das Datum des Vertragsbeginns */
	@Column(name = "Vertragsbeginn")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String vertragsbeginn;

	/** Das Datum des Vertragsendes */
	@Column(name = "Vertragsende")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String vertragsende;

	/** Der Name des Ausbilders */
	@Column(name = "Ausbilder")
	@JsonProperty
	public String nameAusbilder;

	/** Betrieb erhält Anschreiben */
	@Column(name = "AllgAdrAnschreiben")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean erhaeltAnschreiben;

	/** Gibt an ob es ein Praktikum ist */
	@Column(name = "Praktikum")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerBetrieb ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerBetrieb() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerBetrieb ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idSchueler   der Wert für das Attribut idSchueler
	 * @param idBetrieb   der Wert für das Attribut idBetrieb
	 */
	public DTOSchuelerBetrieb(final long id, final long idSchueler, final long idBetrieb) {
		this.id = id;
		this.idSchueler = idSchueler;
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
		DTOSchuelerBetrieb other = (DTOSchuelerBetrieb) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerBetrieb(id=" + this.id + ", idSchueler=" + this.idSchueler + ", idBetrieb=" + this.idBetrieb + ", idBeschaeftigungsart=" + this.idBeschaeftigungsart + ", vertragsbeginn=" + this.vertragsbeginn + ", vertragsende=" + this.vertragsende + ", nameAusbilder=" + this.nameAusbilder + ", erhaeltAnschreiben=" + this.erhaeltAnschreiben + ", istPraktikum=" + this.istPraktikum + ", sortierung=" + this.sortierung + ", idAnsprechpartner=" + this.idAnsprechpartner + ", idBetreuungslehrer=" + this.idBetreuungslehrer + ")";
	}

}
