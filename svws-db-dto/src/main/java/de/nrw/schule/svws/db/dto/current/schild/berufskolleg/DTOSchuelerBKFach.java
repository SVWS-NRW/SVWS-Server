package de.nrw.schule.svws.db.dto.current.schild.berufskolleg;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerBKFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerBKFaecher")
@NamedQuery(name="DTOSchuelerBKFach.all", query="SELECT e FROM DTOSchuelerBKFach e")
@NamedQuery(name="DTOSchuelerBKFach.id", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.ID = :value")
@NamedQuery(name="DTOSchuelerBKFach.id.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchuelerBKFach.schueler_id", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DTOSchuelerBKFach.schueler_id.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DTOSchuelerBKFach.schuljahresabschnitts_id", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="DTOSchuelerBKFach.schuljahresabschnitts_id.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="DTOSchuelerBKFach.fach_id", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Fach_ID = :value")
@NamedQuery(name="DTOSchuelerBKFach.fach_id.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DTOSchuelerBKFach.fachkrz", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.FachKrz = :value")
@NamedQuery(name="DTOSchuelerBKFach.fachkrz.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.FachKrz IN :value")
@NamedQuery(name="DTOSchuelerBKFach.fachschriftlich", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.FachSchriftlich = :value")
@NamedQuery(name="DTOSchuelerBKFach.fachschriftlich.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.FachSchriftlich IN :value")
@NamedQuery(name="DTOSchuelerBKFach.fachschriftlichba", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.FachSchriftlichBA = :value")
@NamedQuery(name="DTOSchuelerBKFach.fachschriftlichba.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.FachSchriftlichBA IN :value")
@NamedQuery(name="DTOSchuelerBKFach.vornote", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Vornote = :value")
@NamedQuery(name="DTOSchuelerBKFach.vornote.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Vornote IN :value")
@NamedQuery(name="DTOSchuelerBKFach.noteschriftlich", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NoteSchriftlich = :value")
@NamedQuery(name="DTOSchuelerBKFach.noteschriftlich.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NoteSchriftlich IN :value")
@NamedQuery(name="DTOSchuelerBKFach.mdlpruefung", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.MdlPruefung = :value")
@NamedQuery(name="DTOSchuelerBKFach.mdlpruefung.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.MdlPruefung IN :value")
@NamedQuery(name="DTOSchuelerBKFach.mdlpruefungfw", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.MdlPruefungFW = :value")
@NamedQuery(name="DTOSchuelerBKFach.mdlpruefungfw.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.MdlPruefungFW IN :value")
@NamedQuery(name="DTOSchuelerBKFach.notemuendlich", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NoteMuendlich = :value")
@NamedQuery(name="DTOSchuelerBKFach.notemuendlich.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NoteMuendlich IN :value")
@NamedQuery(name="DTOSchuelerBKFach.noteabschluss", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NoteAbschluss = :value")
@NamedQuery(name="DTOSchuelerBKFach.noteabschluss.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NoteAbschluss IN :value")
@NamedQuery(name="DTOSchuelerBKFach.noteprfgesamt", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NotePrfGesamt = :value")
@NamedQuery(name="DTOSchuelerBKFach.noteprfgesamt.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NotePrfGesamt IN :value")
@NamedQuery(name="DTOSchuelerBKFach.fsortierung", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.FSortierung = :value")
@NamedQuery(name="DTOSchuelerBKFach.fsortierung.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.FSortierung IN :value")
@NamedQuery(name="DTOSchuelerBKFach.fachlehrer_id", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Fachlehrer_ID = :value")
@NamedQuery(name="DTOSchuelerBKFach.fachlehrer_id.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Fachlehrer_ID IN :value")
@NamedQuery(name="DTOSchuelerBKFach.noteabschlussba", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NoteAbschlussBA = :value")
@NamedQuery(name="DTOSchuelerBKFach.noteabschlussba.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.NoteAbschlussBA IN :value")
@NamedQuery(name="DTOSchuelerBKFach.kursart", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Kursart = :value")
@NamedQuery(name="DTOSchuelerBKFach.kursart.multiple", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.Kursart IN :value")
@NamedQuery(name="DTOSchuelerBKFach.primaryKeyQuery", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchuelerBKFach.all.migration", query="SELECT e FROM DTOSchuelerBKFach e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","Schuljahresabschnitts_ID","Fach_ID","FachKrz","FachSchriftlich","FachSchriftlichBA","Vornote","NoteSchriftlich","MdlPruefung","MdlPruefungFW","NoteMuendlich","NoteAbschluss","NotePrfGesamt","FSortierung","Fachlehrer_ID","NoteAbschlussBA","Kursart"})
public class DTOSchuelerBKFach {

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
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean FachSchriftlich;

	/** Schriftlichkeit Berufsbezogen des Facheintrags für den BKAbschlussReiter */
	@Column(name = "FachSchriftlichBA")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean MdlPruefung;

	/** Freiwilliege mündliche Prüfung angesetzt Ja Nein  des Facheintrags für den BKAbschlussReiter */
	@Column(name = "MdlPruefungFW")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
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

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerBKFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerBKFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerBKFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOSchuelerBKFach(final Long ID, final Long Schueler_ID, final Long Fach_ID) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerBKFach other = (DTOSchuelerBKFach) obj;
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
		return "DTOSchuelerBKFach(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Fach_ID=" + this.Fach_ID + ", FachKrz=" + this.FachKrz + ", FachSchriftlich=" + this.FachSchriftlich + ", FachSchriftlichBA=" + this.FachSchriftlichBA + ", Vornote=" + this.Vornote + ", NoteSchriftlich=" + this.NoteSchriftlich + ", MdlPruefung=" + this.MdlPruefung + ", MdlPruefungFW=" + this.MdlPruefungFW + ", NoteMuendlich=" + this.NoteMuendlich + ", NoteAbschluss=" + this.NoteAbschluss + ", NotePrfGesamt=" + this.NotePrfGesamt + ", FSortierung=" + this.FSortierung + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", NoteAbschlussBA=" + this.NoteAbschlussBA + ", Kursart=" + this.Kursart + ")";
	}

}