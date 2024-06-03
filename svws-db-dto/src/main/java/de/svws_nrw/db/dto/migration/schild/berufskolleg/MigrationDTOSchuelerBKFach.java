package de.svws_nrw.db.dto.migration.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerBKFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerBKFaecher")
@JsonPropertyOrder({"ID", "Schueler_ID", "Schuljahresabschnitts_ID", "Fach_ID", "FachKrz", "FachSchriftlich", "FachSchriftlichBA", "Vornote", "NoteSchriftlich", "MdlPruefung", "MdlPruefungFW", "NoteMuendlich", "NoteAbschluss", "NotePrfGesamt", "FSortierung", "SchulnrEigner", "Fachlehrer_ID", "NoteAbschlussBA", "Kursart", "Jahr", "Abschnitt", "Fachlehrer"})
public final class MigrationDTOSchuelerBKFach {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerBKFach e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachKrz */
	public static final String QUERY_BY_FACHKRZ = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachKrz */
	public static final String QUERY_LIST_BY_FACHKRZ = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachSchriftlich */
	public static final String QUERY_BY_FACHSCHRIFTLICH = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachSchriftlich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachSchriftlich */
	public static final String QUERY_LIST_BY_FACHSCHRIFTLICH = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachSchriftlich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachSchriftlichBA */
	public static final String QUERY_BY_FACHSCHRIFTLICHBA = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachSchriftlichBA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachSchriftlichBA */
	public static final String QUERY_LIST_BY_FACHSCHRIFTLICHBA = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachSchriftlichBA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vornote */
	public static final String QUERY_BY_VORNOTE = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Vornote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vornote */
	public static final String QUERY_LIST_BY_VORNOTE = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Vornote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteSchriftlich */
	public static final String QUERY_BY_NOTESCHRIFTLICH = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteSchriftlich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteSchriftlich */
	public static final String QUERY_LIST_BY_NOTESCHRIFTLICH = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteSchriftlich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MdlPruefung */
	public static final String QUERY_BY_MDLPRUEFUNG = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.MdlPruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MdlPruefung */
	public static final String QUERY_LIST_BY_MDLPRUEFUNG = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.MdlPruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MdlPruefungFW */
	public static final String QUERY_BY_MDLPRUEFUNGFW = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.MdlPruefungFW = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MdlPruefungFW */
	public static final String QUERY_LIST_BY_MDLPRUEFUNGFW = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.MdlPruefungFW IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteMuendlich */
	public static final String QUERY_BY_NOTEMUENDLICH = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteMuendlich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteMuendlich */
	public static final String QUERY_LIST_BY_NOTEMUENDLICH = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteMuendlich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteAbschluss */
	public static final String QUERY_BY_NOTEABSCHLUSS = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteAbschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteAbschluss */
	public static final String QUERY_LIST_BY_NOTEABSCHLUSS = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteAbschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NotePrfGesamt */
	public static final String QUERY_BY_NOTEPRFGESAMT = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NotePrfGesamt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NotePrfGesamt */
	public static final String QUERY_LIST_BY_NOTEPRFGESAMT = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NotePrfGesamt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FSortierung */
	public static final String QUERY_BY_FSORTIERUNG = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FSortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FSortierung */
	public static final String QUERY_LIST_BY_FSORTIERUNG = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FSortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachlehrer_ID */
	public static final String QUERY_BY_FACHLEHRER_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fachlehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachlehrer_ID */
	public static final String QUERY_LIST_BY_FACHLEHRER_ID = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fachlehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteAbschlussBA */
	public static final String QUERY_BY_NOTEABSCHLUSSBA = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteAbschlussBA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteAbschlussBA */
	public static final String QUERY_LIST_BY_NOTEABSCHLUSSBA = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteAbschlussBA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursart */
	public static final String QUERY_BY_KURSART = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursart */
	public static final String QUERY_LIST_BY_KURSART = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Abschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachlehrer */
	public static final String QUERY_BY_FACHLEHRER = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fachlehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachlehrer */
	public static final String QUERY_LIST_BY_FACHLEHRER = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fachlehrer IN ?1";

	/** ID des Facheintrags für den BKAbschlussReiter */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerIDdes Facheintrags für den BKAbschlussReiter */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte für den Facheintrag */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** FachID des Facheintrags für den BKAbschlussReiter */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Kachkürzel des Facheintrags für den BKAbschlussReiter */
	@Column(name = "FachKrz")
	@JsonProperty
	public String FachKrz;

	/** Schriftlichkeit Allgemeinbildend  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "FachSchriftlich")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean FachSchriftlich;

	/** Schriftlichkeit Berufsbezogen des Facheintrags für den BKAbschlussReiter */
	@Column(name = "FachSchriftlichBA")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean FachSchriftlichBA;

	/** Vornote zum Fach des Facheintrags für den BKAbschlussReiter */
	@Column(name = "Vornote")
	@JsonProperty
	public String Vornote;

	/** Schriftliche Note  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "NoteSchriftlich")
	@JsonProperty
	public String NoteSchriftlich;

	/** Mündliche Prüfung angesetzt Ja Nein  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "MdlPruefung")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean MdlPruefung;

	/** Freiwilliege mündliche Prüfung angesetzt Ja Nein  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "MdlPruefungFW")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean MdlPruefungFW;

	/** Note mündliche Prüfung  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "NoteMuendlich")
	@JsonProperty
	public String NoteMuendlich;

	/** Abschlussnote  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "NoteAbschluss")
	@JsonProperty
	public String NoteAbschluss;

	/** DEPRECATED: Wird seit Änderung der APO-BK nicht mehr genutzt. Gesamtprüfungsnote des Facheintrags für den BKAbschlussReiter */
	@Column(name = "NotePrfGesamt")
	@JsonProperty
	public String NotePrfGesamt;

	/** Sortierung des Facheintrags für den BKAbschlussReiter */
	@Column(name = "FSortierung")
	@JsonProperty
	public Integer FSortierung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Lehrer-ID des Facheintrags für den BK-Abschluss-Reiter */
	@Column(name = "Fachlehrer_ID")
	@JsonProperty
	public Long Fachlehrer_ID;

	/** Abschlussnote berufsbezogen  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "NoteAbschlussBA")
	@JsonProperty
	public String NoteAbschlussBA;

	/** Kursart des Facheintrags für den BKAbschlussReiter */
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Schuljahr des Facheintrags für den BKAbschlussReiter */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt des Facheintrags für den BKAbschlussReiter */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** DEPRECATED: Lehrer  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "Fachlehrer")
	@JsonProperty
	public String Fachlehrer;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerBKFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerBKFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerBKFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public MigrationDTOSchuelerBKFach(final Long ID, final Long Schueler_ID, final Long Fach_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
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
		MigrationDTOSchuelerBKFach other = (MigrationDTOSchuelerBKFach) obj;
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
		return "MigrationDTOSchuelerBKFach(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Fach_ID=" + this.Fach_ID + ", FachKrz=" + this.FachKrz + ", FachSchriftlich=" + this.FachSchriftlich + ", FachSchriftlichBA=" + this.FachSchriftlichBA + ", Vornote=" + this.Vornote + ", NoteSchriftlich=" + this.NoteSchriftlich + ", MdlPruefung=" + this.MdlPruefung + ", MdlPruefungFW=" + this.MdlPruefungFW + ", NoteMuendlich=" + this.NoteMuendlich + ", NoteAbschluss=" + this.NoteAbschluss + ", NotePrfGesamt=" + this.NotePrfGesamt + ", FSortierung=" + this.FSortierung + ", SchulnrEigner=" + this.SchulnrEigner + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", NoteAbschlussBA=" + this.NoteAbschlussBA + ", Kursart=" + this.Kursart + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", Fachlehrer=" + this.Fachlehrer + ")";
	}

}
