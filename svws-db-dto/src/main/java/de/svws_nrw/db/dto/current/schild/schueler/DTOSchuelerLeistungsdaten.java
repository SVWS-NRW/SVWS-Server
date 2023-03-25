package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.NoteConverterFromKuerzel;

import de.svws_nrw.core.types.Note;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;
import de.svws_nrw.csv.converter.current.NoteConverterFromKuerzelSerializer;
import de.svws_nrw.csv.converter.current.NoteConverterFromKuerzelDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLeistungsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLeistungsdaten")
@NamedQuery(name="DTOSchuelerLeistungsdaten.all", query="SELECT e FROM DTOSchuelerLeistungsdaten e")
@NamedQuery(name="DTOSchuelerLeistungsdaten.id", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.id.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.abschnitt_id", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.abschnitt_id.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.fach_id", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Fach_ID = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.fach_id.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.hochrechnung", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Hochrechnung = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.hochrechnung.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Hochrechnung IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.fachlehrer_id", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Fachlehrer_ID = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.fachlehrer_id.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Fachlehrer_ID IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.kursart", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kursart = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.kursart.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kursart IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.kursartallg", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.KursartAllg = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.kursartallg.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.KursartAllg IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.kurs_id", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kurs_ID = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.kurs_id.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kurs_ID IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.notenkrz", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NotenKrz = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.notenkrz.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NotenKrz IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.notenkrzquartal", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NotenKrzQuartal = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.notenkrzquartal.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NotenKrzQuartal IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.warnung", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Warnung = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.warnung.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Warnung IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.warndatum", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Warndatum = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.warndatum.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Warndatum IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.abifach", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbiFach = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.abifach.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbiFach IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.wochenstunden", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Wochenstunden = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.wochenstunden.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Wochenstunden IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.abizeugnis", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbiZeugnis = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.abizeugnis.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbiZeugnis IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.prognose", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Prognose = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.prognose.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Prognose IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.fehlstd", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.FehlStd = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.fehlstd.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.FehlStd IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.ufehlstd", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.uFehlStd = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.ufehlstd.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.uFehlStd IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.sortierung", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.sortierung.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.lernentw", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Lernentw = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.lernentw.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Lernentw IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.gekoppelt", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Gekoppelt = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.gekoppelt.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Gekoppelt IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.vorherabgeschl", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.VorherAbgeschl = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.vorherabgeschl.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.VorherAbgeschl IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.abschlussjahrgang", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbschlussJahrgang = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.abschlussjahrgang.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbschlussJahrgang IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.hochrechnungstatus", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.HochrechnungStatus = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.hochrechnungstatus.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.HochrechnungStatus IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.schulnr", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.SchulNr = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.schulnr.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.SchulNr IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.zusatzkraft_id", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft_ID = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.zusatzkraft_id.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft_ID IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.wochenstdzusatzkraft", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.WochenstdZusatzkraft = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.wochenstdzusatzkraft.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.WochenstdZusatzkraft IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.prf10fach", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Prf10Fach = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.prf10fach.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Prf10Fach IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.aufzeugnis", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AufZeugnis = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.aufzeugnis.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AufZeugnis IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.gewichtung", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Gewichtung = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.gewichtung.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Gewichtung IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.noteabschlussba", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NoteAbschlussBA = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.noteabschlussba.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NoteAbschlussBA IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.umfang", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Umfang = :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.umfang.multiple", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Umfang IN :value")
@NamedQuery(name="DTOSchuelerLeistungsdaten.primaryKeyQuery", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchuelerLeistungsdaten.all.migration", query="SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abschnitt_ID","Fach_ID","Hochrechnung","Fachlehrer_ID","Kursart","KursartAllg","Kurs_ID","NotenKrz","NotenKrzQuartal","Warnung","Warndatum","AbiFach","Wochenstunden","AbiZeugnis","Prognose","FehlStd","uFehlStd","Sortierung","Lernentw","Gekoppelt","VorherAbgeschl","AbschlussJahrgang","HochrechnungStatus","SchulNr","Zusatzkraft_ID","WochenstdZusatzkraft","Prf10Fach","AufZeugnis","Gewichtung","NoteAbschlussBA","Umfang"})
public class DTOSchuelerLeistungsdaten {

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

	/** Das Notenkürzel der Quartals-Note */
	@Column(name = "NotenKrzQuartal")
	@JsonProperty
	@Convert(converter=NoteConverterFromKuerzel.class)
	@JsonSerialize(using=NoteConverterFromKuerzelSerializer.class)
	@JsonDeserialize(using=NoteConverterFromKuerzelDeserializer.class)
	public Note NotenKrzQuartal;

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
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerLeistungsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOSchuelerLeistungsdaten(final Long ID, final Long Abschnitt_ID, final Long Fach_ID) {
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
		DTOSchuelerLeistungsdaten other = (DTOSchuelerLeistungsdaten) obj;
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
		return "DTOSchuelerLeistungsdaten(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Fach_ID=" + this.Fach_ID + ", Hochrechnung=" + this.Hochrechnung + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", Kursart=" + this.Kursart + ", KursartAllg=" + this.KursartAllg + ", Kurs_ID=" + this.Kurs_ID + ", NotenKrz=" + this.NotenKrz + ", NotenKrzQuartal=" + this.NotenKrzQuartal + ", Warnung=" + this.Warnung + ", Warndatum=" + this.Warndatum + ", AbiFach=" + this.AbiFach + ", Wochenstunden=" + this.Wochenstunden + ", AbiZeugnis=" + this.AbiZeugnis + ", Prognose=" + this.Prognose + ", FehlStd=" + this.FehlStd + ", uFehlStd=" + this.uFehlStd + ", Sortierung=" + this.Sortierung + ", Lernentw=" + this.Lernentw + ", Gekoppelt=" + this.Gekoppelt + ", VorherAbgeschl=" + this.VorherAbgeschl + ", AbschlussJahrgang=" + this.AbschlussJahrgang + ", HochrechnungStatus=" + this.HochrechnungStatus + ", SchulNr=" + this.SchulNr + ", Zusatzkraft_ID=" + this.Zusatzkraft_ID + ", WochenstdZusatzkraft=" + this.WochenstdZusatzkraft + ", Prf10Fach=" + this.Prf10Fach + ", AufZeugnis=" + this.AufZeugnis + ", Gewichtung=" + this.Gewichtung + ", NoteAbschlussBA=" + this.NoteAbschlussBA + ", Umfang=" + this.Umfang + ")";
	}

}