package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


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

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerGSDaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerGSDaten")
@JsonPropertyOrder({"Schueler_ID", "SchulnrEigner", "Note_Sprachgebrauch", "Note_Lesen", "Note_Rechtschreiben", "Note_Sachunterricht", "Note_Mathematik", "Note_Englisch", "Note_KunstTextil", "Note_Musik", "Note_Sport", "Note_Religion", "Durchschnittsnote_Sprache", "Durchschnittsnote_Einfach", "Durchschnittsnote_Gewichtet", "Anrede_Klassenlehrer", "Nachname_Klassenlehrer", "GS_Klasse", "Bemerkungen", "Geschwisterkind"})
public final class MigrationDTOSchuelerGrundschuldaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Sprachgebrauch */
	public static final String QUERY_BY_NOTE_SPRACHGEBRAUCH = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sprachgebrauch = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Sprachgebrauch */
	public static final String QUERY_LIST_BY_NOTE_SPRACHGEBRAUCH = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sprachgebrauch IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Lesen */
	public static final String QUERY_BY_NOTE_LESEN = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Lesen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Lesen */
	public static final String QUERY_LIST_BY_NOTE_LESEN = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Lesen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Rechtschreiben */
	public static final String QUERY_BY_NOTE_RECHTSCHREIBEN = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Rechtschreiben = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Rechtschreiben */
	public static final String QUERY_LIST_BY_NOTE_RECHTSCHREIBEN = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Rechtschreiben IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Sachunterricht */
	public static final String QUERY_BY_NOTE_SACHUNTERRICHT = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sachunterricht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Sachunterricht */
	public static final String QUERY_LIST_BY_NOTE_SACHUNTERRICHT = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sachunterricht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Mathematik */
	public static final String QUERY_BY_NOTE_MATHEMATIK = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Mathematik = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Mathematik */
	public static final String QUERY_LIST_BY_NOTE_MATHEMATIK = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Mathematik IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Englisch */
	public static final String QUERY_BY_NOTE_ENGLISCH = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Englisch = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Englisch */
	public static final String QUERY_LIST_BY_NOTE_ENGLISCH = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Englisch IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_KunstTextil */
	public static final String QUERY_BY_NOTE_KUNSTTEXTIL = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_KunstTextil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_KunstTextil */
	public static final String QUERY_LIST_BY_NOTE_KUNSTTEXTIL = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_KunstTextil IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Musik */
	public static final String QUERY_BY_NOTE_MUSIK = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Musik = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Musik */
	public static final String QUERY_LIST_BY_NOTE_MUSIK = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Musik IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Sport */
	public static final String QUERY_BY_NOTE_SPORT = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sport = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Sport */
	public static final String QUERY_LIST_BY_NOTE_SPORT = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sport IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note_Religion */
	public static final String QUERY_BY_NOTE_RELIGION = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Religion = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note_Religion */
	public static final String QUERY_LIST_BY_NOTE_RELIGION = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Religion IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Durchschnittsnote_Sprache */
	public static final String QUERY_BY_DURCHSCHNITTSNOTE_SPRACHE = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Sprache = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Durchschnittsnote_Sprache */
	public static final String QUERY_LIST_BY_DURCHSCHNITTSNOTE_SPRACHE = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Sprache IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Durchschnittsnote_Einfach */
	public static final String QUERY_BY_DURCHSCHNITTSNOTE_EINFACH = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Einfach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Durchschnittsnote_Einfach */
	public static final String QUERY_LIST_BY_DURCHSCHNITTSNOTE_EINFACH = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Einfach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Durchschnittsnote_Gewichtet */
	public static final String QUERY_BY_DURCHSCHNITTSNOTE_GEWICHTET = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Gewichtet = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Durchschnittsnote_Gewichtet */
	public static final String QUERY_LIST_BY_DURCHSCHNITTSNOTE_GEWICHTET = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Gewichtet IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Anrede_Klassenlehrer */
	public static final String QUERY_BY_ANREDE_KLASSENLEHRER = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Anrede_Klassenlehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Anrede_Klassenlehrer */
	public static final String QUERY_LIST_BY_ANREDE_KLASSENLEHRER = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Anrede_Klassenlehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nachname_Klassenlehrer */
	public static final String QUERY_BY_NACHNAME_KLASSENLEHRER = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Nachname_Klassenlehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nachname_Klassenlehrer */
	public static final String QUERY_LIST_BY_NACHNAME_KLASSENLEHRER = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Nachname_Klassenlehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GS_Klasse */
	public static final String QUERY_BY_GS_KLASSE = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.GS_Klasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GS_Klasse */
	public static final String QUERY_LIST_BY_GS_KLASSE = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.GS_Klasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Bemerkungen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geschwisterkind */
	public static final String QUERY_BY_GESCHWISTERKIND = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Geschwisterkind = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geschwisterkind */
	public static final String QUERY_LIST_BY_GESCHWISTERKIND = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Geschwisterkind IN ?1";

	/** SchülerID zum GS-Daten-Eintrag */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Note Sprachgebrauch zum GS-Daten-Eintrag */
	@Column(name = "Note_Sprachgebrauch")
	@JsonProperty
	public Integer Note_Sprachgebrauch;

	/** Note Lesen zum GS-Daten-Eintrag */
	@Column(name = "Note_Lesen")
	@JsonProperty
	public Integer Note_Lesen;

	/** Note Rechtschreibe zum GS-Daten-Eintrag */
	@Column(name = "Note_Rechtschreiben")
	@JsonProperty
	public Integer Note_Rechtschreiben;

	/** Note Sachunterricht zum GS-Daten-Eintrag */
	@Column(name = "Note_Sachunterricht")
	@JsonProperty
	public Integer Note_Sachunterricht;

	/** Note Mathematik zum GS-Daten-Eintrag */
	@Column(name = "Note_Mathematik")
	@JsonProperty
	public Integer Note_Mathematik;

	/** Note Englisch zum GS-Daten-Eintrag */
	@Column(name = "Note_Englisch")
	@JsonProperty
	public Integer Note_Englisch;

	/** Note Kunst und Textil zum GS-Daten-Eintrag */
	@Column(name = "Note_KunstTextil")
	@JsonProperty
	public Integer Note_KunstTextil;

	/** Note Musik zum GS-Daten-Eintrag */
	@Column(name = "Note_Musik")
	@JsonProperty
	public Integer Note_Musik;

	/** Note Sport zum GS-Daten-Eintrag */
	@Column(name = "Note_Sport")
	@JsonProperty
	public Integer Note_Sport;

	/** Note Religion zum GS-Daten-Eintrag */
	@Column(name = "Note_Religion")
	@JsonProperty
	public Integer Note_Religion;

	/** Durchschnitt Sprache zum GS-Daten-Eintrag */
	@Column(name = "Durchschnittsnote_Sprache")
	@JsonProperty
	public Double Durchschnittsnote_Sprache;

	/** Durschnit einfach zum GS-Daten-Eintrag */
	@Column(name = "Durchschnittsnote_Einfach")
	@JsonProperty
	public Double Durchschnittsnote_Einfach;

	/** Durchschnitt gewichtet zum GS-Daten-Eintrag (ohne Religion) */
	@Column(name = "Durchschnittsnote_Gewichtet")
	@JsonProperty
	public Double Durchschnittsnote_Gewichtet;

	/** Anrede klassenlehrer zum GS-Daten-Eintrag */
	@Column(name = "Anrede_Klassenlehrer")
	@JsonProperty
	public String Anrede_Klassenlehrer;

	/** Nachname Klassenlehrer zum GS-Daten-Eintrag PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Nachname_Klassenlehrer")
	@JsonProperty
	public String Nachname_Klassenlehrer;

	/** Klassenbezeichnung zum GS-Daten-Eintrag (Die Klasse selbst ist nicht in dieser DB definiert!) */
	@Column(name = "GS_Klasse")
	@JsonProperty
	public String GS_Klasse;

	/** Bemerkungen zum GS-Daten-Eintrag */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/** Angabe Geschwisterkind Ja Nein  zum GS-Daten-Eintrag */
	@Column(name = "Geschwisterkind")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Geschwisterkind;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerGrundschuldaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerGrundschuldaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerGrundschuldaten ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOSchuelerGrundschuldaten(final Long Schueler_ID, final Integer SchulnrEigner) {
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerGrundschuldaten other = (MigrationDTOSchuelerGrundschuldaten) obj;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerGrundschuldaten(Schueler_ID=" + this.Schueler_ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Note_Sprachgebrauch=" + this.Note_Sprachgebrauch + ", Note_Lesen=" + this.Note_Lesen + ", Note_Rechtschreiben=" + this.Note_Rechtschreiben + ", Note_Sachunterricht=" + this.Note_Sachunterricht + ", Note_Mathematik=" + this.Note_Mathematik + ", Note_Englisch=" + this.Note_Englisch + ", Note_KunstTextil=" + this.Note_KunstTextil + ", Note_Musik=" + this.Note_Musik + ", Note_Sport=" + this.Note_Sport + ", Note_Religion=" + this.Note_Religion + ", Durchschnittsnote_Sprache=" + this.Durchschnittsnote_Sprache + ", Durchschnittsnote_Einfach=" + this.Durchschnittsnote_Einfach + ", Durchschnittsnote_Gewichtet=" + this.Durchschnittsnote_Gewichtet + ", Anrede_Klassenlehrer=" + this.Anrede_Klassenlehrer + ", Nachname_Klassenlehrer=" + this.Nachname_Klassenlehrer + ", GS_Klasse=" + this.GS_Klasse + ", Bemerkungen=" + this.Bemerkungen + ", Geschwisterkind=" + this.Geschwisterkind + ")";
	}

}
