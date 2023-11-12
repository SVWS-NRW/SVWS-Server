package de.svws_nrw.db.dto.migration.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusConverter;


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
@NamedQuery(name = "MigrationDTOSchuelerBKFach.all", query = "SELECT e FROM MigrationDTOSchuelerBKFach e")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.id", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.schuljahresabschnitts_id", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.schuljahresabschnitts_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fach_id", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fach_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fach_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachkrz", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachKrz = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachkrz.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachKrz IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachschriftlich", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachSchriftlich = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachschriftlich.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachSchriftlich IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachschriftlichba", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachSchriftlichBA = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachschriftlichba.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FachSchriftlichBA IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.vornote", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Vornote = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.vornote.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Vornote IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.noteschriftlich", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteSchriftlich = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.noteschriftlich.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteSchriftlich IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.mdlpruefung", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.MdlPruefung = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.mdlpruefung.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.MdlPruefung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.mdlpruefungfw", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.MdlPruefungFW = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.mdlpruefungfw.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.MdlPruefungFW IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.notemuendlich", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteMuendlich = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.notemuendlich.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteMuendlich IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.noteabschluss", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteAbschluss = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.noteabschluss.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteAbschluss IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.noteprfgesamt", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NotePrfGesamt = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.noteprfgesamt.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NotePrfGesamt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fsortierung", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FSortierung = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fsortierung.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.FSortierung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachlehrer_id", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fachlehrer_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachlehrer_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fachlehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.noteabschlussba", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteAbschlussBA = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.noteabschlussba.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.NoteAbschlussBA IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.kursart", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Kursart = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.kursart.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Kursart IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.jahr", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Jahr = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.jahr.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Jahr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.abschnitt", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Abschnitt = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.abschnitt.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Abschnitt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachlehrer", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fachlehrer = :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.fachlehrer.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.Fachlehrer IN :value")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOSchuelerBKFach.all.migration", query = "SELECT e FROM MigrationDTOSchuelerBKFach e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Schuljahresabschnitts_ID", "Fach_ID", "FachKrz", "FachSchriftlich", "FachSchriftlichBA", "Vornote", "NoteSchriftlich", "MdlPruefung", "MdlPruefungFW", "NoteMuendlich", "NoteAbschluss", "NotePrfGesamt", "FSortierung", "SchulnrEigner", "Fachlehrer_ID", "NoteAbschlussBA", "Kursart", "Jahr", "Abschnitt", "Fachlehrer"})
public final class MigrationDTOSchuelerBKFach {

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
