package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


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

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerGSDaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerGSDaten")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.all", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_sprachgebrauch", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sprachgebrauch = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_sprachgebrauch.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sprachgebrauch IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_lesen", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Lesen = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_lesen.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Lesen IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_rechtschreiben", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Rechtschreiben = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_rechtschreiben.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Rechtschreiben IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_sachunterricht", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sachunterricht = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_sachunterricht.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sachunterricht IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_mathematik", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Mathematik = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_mathematik.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Mathematik IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_englisch", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Englisch = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_englisch.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Englisch IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_kunsttextil", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_KunstTextil = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_kunsttextil.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_KunstTextil IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_musik", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Musik = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_musik.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Musik IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_sport", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sport = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_sport.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Sport IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_religion", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Religion = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.note_religion.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Note_Religion IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.durchschnittsnote_sprache", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Sprache = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.durchschnittsnote_sprache.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Sprache IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.durchschnittsnote_einfach", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Einfach = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.durchschnittsnote_einfach.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Einfach IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.durchschnittsnote_gewichtet", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Gewichtet = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.durchschnittsnote_gewichtet.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Durchschnittsnote_Gewichtet IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.anrede_klassenlehrer", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Anrede_Klassenlehrer = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.anrede_klassenlehrer.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Anrede_Klassenlehrer IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.nachname_klassenlehrer", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Nachname_Klassenlehrer = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.nachname_klassenlehrer.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Nachname_Klassenlehrer IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.gs_klasse", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.GS_Klasse = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.gs_klasse.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.GS_Klasse IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.bemerkungen", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.bemerkungen.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.geschwisterkind", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Geschwisterkind = :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.geschwisterkind.multiple", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Geschwisterkind IN :value")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerGrundschuldaten.all.migration", query = "SELECT e FROM MigrationDTOSchuelerGrundschuldaten e WHERE e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID", "SchulnrEigner", "Note_Sprachgebrauch", "Note_Lesen", "Note_Rechtschreiben", "Note_Sachunterricht", "Note_Mathematik", "Note_Englisch", "Note_KunstTextil", "Note_Musik", "Note_Sport", "Note_Religion", "Durchschnittsnote_Sprache", "Durchschnittsnote_Einfach", "Durchschnittsnote_Gewichtet", "Anrede_Klassenlehrer", "Nachname_Klassenlehrer", "GS_Klasse", "Bemerkungen", "Geschwisterkind"})
public final class MigrationDTOSchuelerGrundschuldaten {

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
