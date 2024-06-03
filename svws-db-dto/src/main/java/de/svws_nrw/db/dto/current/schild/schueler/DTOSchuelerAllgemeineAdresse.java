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
@JsonPropertyOrder({"ID", "Schueler_ID", "Adresse_ID", "Vertragsart_ID", "Vertragsbeginn", "Vertragsende", "Ausbilder", "AllgAdrAnschreiben", "Praktikum", "Sortierung", "Ansprechpartner_ID", "Betreuungslehrer_ID"})
public final class DTOSchuelerAllgemeineAdresse {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerAllgemeineAdresse e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Adresse_ID */
	public static final String QUERY_BY_ADRESSE_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Adresse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Adresse_ID */
	public static final String QUERY_LIST_BY_ADRESSE_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Adresse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vertragsart_ID */
	public static final String QUERY_BY_VERTRAGSART_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Vertragsart_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vertragsart_ID */
	public static final String QUERY_LIST_BY_VERTRAGSART_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Vertragsart_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vertragsbeginn */
	public static final String QUERY_BY_VERTRAGSBEGINN = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Vertragsbeginn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vertragsbeginn */
	public static final String QUERY_LIST_BY_VERTRAGSBEGINN = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Vertragsbeginn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vertragsende */
	public static final String QUERY_BY_VERTRAGSENDE = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Vertragsende = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vertragsende */
	public static final String QUERY_LIST_BY_VERTRAGSENDE = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Vertragsende IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ausbilder */
	public static final String QUERY_BY_AUSBILDER = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Ausbilder = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ausbilder */
	public static final String QUERY_LIST_BY_AUSBILDER = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Ausbilder IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AllgAdrAnschreiben */
	public static final String QUERY_BY_ALLGADRANSCHREIBEN = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.AllgAdrAnschreiben = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AllgAdrAnschreiben */
	public static final String QUERY_LIST_BY_ALLGADRANSCHREIBEN = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.AllgAdrAnschreiben IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Praktikum */
	public static final String QUERY_BY_PRAKTIKUM = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Praktikum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Praktikum */
	public static final String QUERY_LIST_BY_PRAKTIKUM = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Praktikum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ansprechpartner_ID */
	public static final String QUERY_BY_ANSPRECHPARTNER_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Ansprechpartner_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ansprechpartner_ID */
	public static final String QUERY_LIST_BY_ANSPRECHPARTNER_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Ansprechpartner_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Betreuungslehrer_ID */
	public static final String QUERY_BY_BETREUUNGSLEHRER_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Betreuungslehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Betreuungslehrer_ID */
	public static final String QUERY_LIST_BY_BETREUUNGSLEHRER_ID = "SELECT e FROM DTOSchuelerAllgemeineAdresse e WHERE e.Betreuungslehrer_ID IN ?1";

	/** ID des Betriebeeintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** SchülerID des Betriebeeintrags beim Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** AdressID des Betriebeeintrags beim Schüler */
	@Column(name = "Adresse_ID")
	@JsonProperty
	public long Adresse_ID;

	/** VertragsArtID des Betriebeeintrags beim Schüler */
	@Column(name = "Vertragsart_ID")
	@JsonProperty
	public Long Vertragsart_ID;

	/** Datum Vertragsbeginn des Betriebeeintrags beim Schüler */
	@Column(name = "Vertragsbeginn")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Vertragsbeginn;

	/** Datum des Vertragsende des Betriebeeintrags beim Schüler */
	@Column(name = "Vertragsende")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Vertragsende;

	/** Ausbildername des Betriebeeintrags beim Schüler */
	@Column(name = "Ausbilder")
	@JsonProperty
	public String Ausbilder;

	/** Betrieb erhält Anschreiben Ja/Nein */
	@Column(name = "AllgAdrAnschreiben")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean AllgAdrAnschreiben;

	/** Gibt an ob es ein Praktikum ist beim Betriebeeintrags beim Schüler */
	@Column(name = "Praktikum")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Praktikum;

	/** Sortierung des Betriebeeintrags beim Schüler */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** AnsprechpartnerID des Betriebeeintrags beim Schüler */
	@Column(name = "Ansprechpartner_ID")
	@JsonProperty
	public Long Ansprechpartner_ID;

	/** BetreuungslehrerID des Betriebeeintrags beim Schüler */
	@Column(name = "Betreuungslehrer_ID")
	@JsonProperty
	public Long Betreuungslehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerAllgemeineAdresse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Adresse_ID   der Wert für das Attribut Adresse_ID
	 */
	public DTOSchuelerAllgemeineAdresse(final long ID, final long Schueler_ID, final long Adresse_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
		this.Adresse_ID = Adresse_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerAllgemeineAdresse other = (DTOSchuelerAllgemeineAdresse) obj;
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
		return "DTOSchuelerAllgemeineAdresse(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Adresse_ID=" + this.Adresse_ID + ", Vertragsart_ID=" + this.Vertragsart_ID + ", Vertragsbeginn=" + this.Vertragsbeginn + ", Vertragsende=" + this.Vertragsende + ", Ausbilder=" + this.Ausbilder + ", AllgAdrAnschreiben=" + this.AllgAdrAnschreiben + ", Praktikum=" + this.Praktikum + ", Sortierung=" + this.Sortierung + ", Ansprechpartner_ID=" + this.Ansprechpartner_ID + ", Betreuungslehrer_ID=" + this.Betreuungslehrer_ID + ")";
	}

}
