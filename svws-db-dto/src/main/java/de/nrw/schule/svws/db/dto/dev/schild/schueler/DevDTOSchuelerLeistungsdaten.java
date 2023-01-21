package de.nrw.schule.svws.db.dto.dev.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.current.DatumConverter;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromKuerzel;

import de.nrw.schule.svws.core.types.Note;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.NoteConverterFromKuerzelSerializer;
import de.nrw.schule.svws.csv.converter.current.NoteConverterFromKuerzelDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLeistungsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLeistungsdaten")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.all", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.id", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.ID = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.id.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.abschnitt_id", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.abschnitt_id.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.fach_id", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Fach_ID = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.fach_id.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.hochrechnung", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Hochrechnung = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.hochrechnung.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Hochrechnung IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.fachlehrer_id", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Fachlehrer_ID = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.fachlehrer_id.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Fachlehrer_ID IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.kursart", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Kursart = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.kursart.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Kursart IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.kursartallg", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.KursartAllg = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.kursartallg.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.KursartAllg IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.kurs_id", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Kurs_ID = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.kurs_id.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Kurs_ID IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.notenkrz", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.NotenKrz = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.notenkrz.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.NotenKrz IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.warnung", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Warnung = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.warnung.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Warnung IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.warndatum", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Warndatum = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.warndatum.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Warndatum IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.abifach", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.AbiFach = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.abifach.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.AbiFach IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.wochenstunden", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Wochenstunden = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.wochenstunden.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Wochenstunden IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.abizeugnis", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.AbiZeugnis = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.abizeugnis.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.AbiZeugnis IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.prognose", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Prognose = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.prognose.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Prognose IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.fehlstd", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.FehlStd = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.fehlstd.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.FehlStd IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.ufehlstd", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.uFehlStd = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.ufehlstd.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.uFehlStd IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.sortierung", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.sortierung.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.lernentw", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Lernentw = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.lernentw.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Lernentw IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.gekoppelt", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Gekoppelt = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.gekoppelt.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Gekoppelt IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.vorherabgeschl", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.VorherAbgeschl = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.vorherabgeschl.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.VorherAbgeschl IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.abschlussjahrgang", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.AbschlussJahrgang = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.abschlussjahrgang.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.AbschlussJahrgang IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.hochrechnungstatus", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.HochrechnungStatus = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.hochrechnungstatus.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.HochrechnungStatus IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.schulnr", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.SchulNr = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.schulnr.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.SchulNr IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.zusatzkraft_id", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft_ID = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.zusatzkraft_id.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft_ID IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.wochenstdzusatzkraft", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.WochenstdZusatzkraft = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.wochenstdzusatzkraft.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.WochenstdZusatzkraft IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.prf10fach", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Prf10Fach = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.prf10fach.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Prf10Fach IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.aufzeugnis", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.AufZeugnis = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.aufzeugnis.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.AufZeugnis IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.gewichtung", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Gewichtung = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.gewichtung.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Gewichtung IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.noteabschlussba", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.NoteAbschlussBA = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.noteabschlussba.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.NoteAbschlussBA IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.umfang", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Umfang = :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.umfang.multiple", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.Umfang IN :value")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.primaryKeyQuery", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOSchuelerLeistungsdaten.all.migration", query="SELECT e FROM DevDTOSchuelerLeistungsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abschnitt_ID","Fach_ID","Hochrechnung","Fachlehrer_ID","Kursart","KursartAllg","Kurs_ID","NotenKrz","Warnung","Warndatum","AbiFach","Wochenstunden","AbiZeugnis","Prognose","FehlStd","uFehlStd","Sortierung","Lernentw","Gekoppelt","VorherAbgeschl","AbschlussJahrgang","HochrechnungStatus","SchulNr","Zusatzkraft_ID","WochenstdZusatzkraft","Prf10Fach","AufZeugnis","Gewichtung","NoteAbschlussBA","Umfang"})
public class DevDTOSchuelerLeistungsdaten {

	/** Eine eindeutige ID für die Leistungsdaten des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die eindeutige ID des zugehörigen Lernabschnittes – verweist auf den Lernabschnitt des Schülers */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Die eindeutige ID des zugehörigen Faches – verweist auf das Fach */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Gibt an ob der Datensatz aus einem vorherigen Abschnitt geholt wurde die Minuszahl gibt die Schritte zurück an */
	@Column(name = "Hochrechnung")
	@JsonProperty
	public Integer Hochrechnung;

	/** Die ID des zugehörigen Fach-Lehrers */
	@Column(name = "Fachlehrer_ID")
	@JsonProperty
	public Long Fachlehrer_ID;

	/** Die Kursart */
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Die allgemeine Kursart des Faches (z.B. GK, LK) */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Die eindeutige ID des zugehörigen Kurses, sofern kein Klassenunterricht vorliegt – verweist auf den Kurs */
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** Das Notenkürzel der erteilten Note */
	@Column(name = "NotenKrz")
	@JsonProperty
	@Convert(converter=NoteConverterFromKuerzel.class)
	@JsonSerialize(using=NoteConverterFromKuerzelSerializer.class)
	@JsonDeserialize(using=NoteConverterFromKuerzelDeserializer.class)
	public Note NotenKrz;

	/** gibt an, ob die Leistung gemahnt wurde bzw. gemahnt werden soll – sie Mahndatum */
	@Column(name = "Warnung")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Warnung;

	/** gibt das Datum an, wann die Leistung gemahnt wurde */
	@Column(name = "Warndatum")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Warndatum;

	/** gibt an, ob das Fach als Abiturfach belegt wurde (NULL, 1, 2, 3, 4) */
	@Column(name = "AbiFach")
	@JsonProperty
	public String AbiFach;

	/** gibt die Wochenstunden */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public Integer Wochenstunden;

	/** DEPRECATED: Relikt aus Winschild nicht mehr benötigt */
	@Column(name = "AbiZeugnis")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean AbiZeugnis;

	/** DEPRECATED: Relikt aus Winschild nicht mehr benötigt */
	@Column(name = "Prognose")
	@JsonProperty
	public String Prognose;

	/** Fachbezogene Fehlstunden */
	@Column(name = "FehlStd")
	@JsonProperty
	public Integer FehlStd;

	/** Fachbezogene Fehlstunden unentschuldigt */
	@Column(name = "uFehlStd")
	@JsonProperty
	public Integer uFehlStd;

	/** Sortierungnummer des Leistungsdatensatzes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Text für Fachbezogene Lernentwicklung Bemerkung */
	@Column(name = "Lernentw")
	@JsonProperty
	public String Lernentw;

	/** DEPRECATED: Relikt aus Winschild nicht mehr benötigt */
	@Column(name = "Gekoppelt")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gekoppelt;

	/** Gibt an ob das Fach Epochal war oder ist */
	@Column(name = "VorherAbgeschl")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean VorherAbgeschl;

	/** Hier wird beim Holen von abgeschlossenen Fächern eingetragen, wann das Fach zuletzt unterrichtet wurde */
	@Column(name = "AbschlussJahrgang")
	@JsonProperty
	public String AbschlussJahrgang;

	/** DEPRECATED: Hier wurde früher gespeichert, ob es sich um eine temporäre oder dauerhafte Hochrechnung handelt. Eine Hochrechnung ist nur noch am BK möglich und immer temporär. */
	@Column(name = "HochrechnungStatus")
	@JsonProperty
	public String HochrechnungStatus;

	/** Schulnummer bei externem Unterricht */
	@Column(name = "SchulNr")
	@JsonProperty
	public Integer SchulNr;

	/** Die Lehrer-ID der Zusatzkraft */
	@Column(name = "Zusatzkraft_ID")
	@JsonProperty
	public Long Zusatzkraft_ID;

	/** Wochenstunden Zusatzkraft */
	@Column(name = "WochenstdZusatzkraft")
	@JsonProperty
	public Integer WochenstdZusatzkraft;

	/** Ist Fach für ZP10 / ZK10 */
	@Column(name = "Prf10Fach")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Prf10Fach;

	/** Fach kommt aufs Zeugnnis Ja Nein */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean AufZeugnis;

	/** Gewichtung allgemeinbidend BK */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/** Beim BK wird hier die Note Berufsabschluss eingetragen. Ist vermutl. nicht mehr notwendig, wenn alle Zeugnisse sich aus dem BKAbschluss-Fächern bedienen */
	@Column(name = "NoteAbschlussBA")
	@JsonProperty
	public String NoteAbschlussBA;

	/** Facheigenschaft für Lernstandsberichte (V voller Umfang) (R reduzierter Umfang) */
	@Column(name = "Umfang")
	@JsonProperty
	public String Umfang;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchuelerLeistungsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DevDTOSchuelerLeistungsdaten(final Long ID, final Long Abschnitt_ID, final Long Fach_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abschnitt_ID == null) { 
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
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
		DevDTOSchuelerLeistungsdaten other = (DevDTOSchuelerLeistungsdaten) obj;
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
		return "DevDTOSchuelerLeistungsdaten(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Fach_ID=" + this.Fach_ID + ", Hochrechnung=" + this.Hochrechnung + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", Kursart=" + this.Kursart + ", KursartAllg=" + this.KursartAllg + ", Kurs_ID=" + this.Kurs_ID + ", NotenKrz=" + this.NotenKrz + ", Warnung=" + this.Warnung + ", Warndatum=" + this.Warndatum + ", AbiFach=" + this.AbiFach + ", Wochenstunden=" + this.Wochenstunden + ", AbiZeugnis=" + this.AbiZeugnis + ", Prognose=" + this.Prognose + ", FehlStd=" + this.FehlStd + ", uFehlStd=" + this.uFehlStd + ", Sortierung=" + this.Sortierung + ", Lernentw=" + this.Lernentw + ", Gekoppelt=" + this.Gekoppelt + ", VorherAbgeschl=" + this.VorherAbgeschl + ", AbschlussJahrgang=" + this.AbschlussJahrgang + ", HochrechnungStatus=" + this.HochrechnungStatus + ", SchulNr=" + this.SchulNr + ", Zusatzkraft_ID=" + this.Zusatzkraft_ID + ", WochenstdZusatzkraft=" + this.WochenstdZusatzkraft + ", Prf10Fach=" + this.Prf10Fach + ", AufZeugnis=" + this.AufZeugnis + ", Gewichtung=" + this.Gewichtung + ", NoteAbschlussBA=" + this.NoteAbschlussBA + ", Umfang=" + this.Umfang + ")";
	}

}