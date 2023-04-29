package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.NoteConverterFromInteger;
import de.svws_nrw.db.converter.current.SprachpruefungniveauConverter;
import de.svws_nrw.db.converter.current.SprachreferenzniveauConverter;

import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.fach.Sprachpruefungniveau;
import de.svws_nrw.core.types.fach.Sprachreferenzniveau;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.current.NoteConverterFromIntegerSerializer;
import de.svws_nrw.csv.converter.current.NoteConverterFromIntegerDeserializer;
import de.svws_nrw.csv.converter.current.SprachpruefungniveauConverterSerializer;
import de.svws_nrw.csv.converter.current.SprachpruefungniveauConverterDeserializer;
import de.svws_nrw.csv.converter.current.SprachreferenzniveauConverterSerializer;
import de.svws_nrw.csv.converter.current.SprachreferenzniveauConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerSprachpruefungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerSprachpruefungen")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.all", query = "SELECT e FROM DTOSchuelerSprachpruefungen e")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.id", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.id.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.schueler_id", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.sprache", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Sprache = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.sprache.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Sprache IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.asdjahrgang", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.ASDJahrgang = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.asdjahrgang.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.anspruchsniveau", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Anspruchsniveau = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.anspruchsniveau.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Anspruchsniveau IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.pruefungsdatum", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Pruefungsdatum = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.pruefungsdatum.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Pruefungsdatum IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.ersetztesprache", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.ErsetzteSprache = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.ersetztesprache.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.ErsetzteSprache IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.isthsupruefung", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.IstHSUPruefung = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.isthsupruefung.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.IstHSUPruefung IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.istfeststellungspruefung", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.IstFeststellungspruefung = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.istfeststellungspruefung.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.IstFeststellungspruefung IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.kannerstepflichtfremdspracheersetzen", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.KannErstePflichtfremdspracheErsetzen = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.kannerstepflichtfremdspracheersetzen.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.KannErstePflichtfremdspracheErsetzen IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.kannzweitepflichtfremdspracheersetzen", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.KannZweitePflichtfremdspracheErsetzen = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.kannzweitepflichtfremdspracheersetzen.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.KannZweitePflichtfremdspracheErsetzen IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.kannwahlpflichtfremdspracheersetzen", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.KannWahlpflichtfremdspracheErsetzen = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.kannwahlpflichtfremdspracheersetzen.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.KannWahlpflichtfremdspracheErsetzen IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.kannbelegungalsfortgefuehrtespracheerlauben", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.KannBelegungAlsFortgefuehrteSpracheErlauben = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.kannbelegungalsfortgefuehrtespracheerlauben.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.KannBelegungAlsFortgefuehrteSpracheErlauben IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.referenzniveau", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Referenzniveau = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.referenzniveau.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Referenzniveau IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.notepruefung", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.NotePruefung = :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.notepruefung.multiple", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.NotePruefung IN :value")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerSprachpruefungen.all.migration", query = "SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Sprache", "ASDJahrgang", "Anspruchsniveau", "Pruefungsdatum", "ErsetzteSprache", "IstHSUPruefung", "IstFeststellungspruefung", "KannErstePflichtfremdspracheErsetzen", "KannZweitePflichtfremdspracheErsetzen", "KannWahlpflichtfremdspracheErsetzen", "KannBelegungAlsFortgefuehrteSpracheErlauben", "Referenzniveau", "NotePruefung"})
public final class DTOSchuelerSprachpruefungen {

	/** ID des Sprachprüfungseintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Schülers des Sprachprüfungseintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher */
	@Column(name = "Sprache")
	@JsonProperty
	public String Sprache;

	/** ASDJahrgangsbezeichnung, in der die Sprachprüfung abgelegt wurde */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** Das Anspruchsniveau der Sprachprüfung (angelehnt an einen entsprechenden Schulabschluss) */
	@Column(name = "Anspruchsniveau_ID")
	@JsonProperty
	@Convert(converter = SprachpruefungniveauConverter.class)
	@JsonSerialize(using = SprachpruefungniveauConverterSerializer.class)
	@JsonDeserialize(using = SprachpruefungniveauConverterDeserializer.class)
	public Sprachpruefungniveau Anspruchsniveau;

	/** Datum der Sprachprüfung */
	@Column(name = "Pruefungsdatum")
	@JsonProperty
	public String Pruefungsdatum;

	/** Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher für die ersetzte Sprache */
	@Column(name = "ErsetzteSprache")
	@JsonProperty
	public String ErsetzteSprache;

	/** Gibt an, dass die Prüfung eine Prüfung in der Herkunftssprache ist (BASS 13-61 Nr. 2). Entspricht dem Eintrag P in Schild 2 */
	@Column(name = "IstHSUPruefung")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstHSUPruefung;

	/** Gibt an, ob die Prüfung eine Sprachfeststellungsprüfung ist (BASS 13-61 Nr. 1). Entspricht N in Schild 2 */
	@Column(name = "IstFeststellungspruefung")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstFeststellungspruefung;

	/** Gibt an, ob die Sprachprüfung an die Stelle der ersten Pflichtfremdsprache treten kann */
	@Column(name = "KannErstePflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean KannErstePflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung an die Stelle der zweiten Pflichtfremdsprache treten kann */
	@Column(name = "KannZweitePflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean KannZweitePflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung an die Stelle einer Wahlpflichtfremdsprache der Klassen 05-07 treten kann */
	@Column(name = "KannWahlpflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean KannWahlpflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung nachweist, dass die Sprache als fortgeführte Fremdsprache in der Oberstufe belegt werden kann (BASS 13-61 Nr. 1 Abs. 11) */
	@Column(name = "KannBelegungAlsFortgefuehrteSpracheErlauben")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean KannBelegungAlsFortgefuehrteSpracheErlauben;

	/** Das Sprachreferenzniveau der Sprachprüfung gemäß GeR */
	@Column(name = "Referenzniveau")
	@JsonProperty
	@Convert(converter = SprachreferenzniveauConverter.class)
	@JsonSerialize(using = SprachreferenzniveauConverterSerializer.class)
	@JsonDeserialize(using = SprachreferenzniveauConverterDeserializer.class)
	public Sprachreferenzniveau Referenzniveau;

	/** Note der Sprachprüfung, die herangezogen werden kann, falls die Note der Sprachprüfung an die Stelle einer Fremdsprachennote tritt */
	@Column(name = "NotePruefung")
	@JsonProperty
	@Convert(converter = NoteConverterFromInteger.class)
	@JsonSerialize(using = NoteConverterFromIntegerSerializer.class)
	@JsonDeserialize(using = NoteConverterFromIntegerDeserializer.class)
	public Note NotePruefung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerSprachpruefungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerSprachpruefungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerSprachpruefungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Sprache   der Wert für das Attribut Sprache
	 */
	public DTOSchuelerSprachpruefungen(final long ID, final long Schueler_ID, final String Sprache) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
		if (Sprache == null) {
			throw new NullPointerException("Sprache must not be null");
		}
		this.Sprache = Sprache;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerSprachpruefungen other = (DTOSchuelerSprachpruefungen) obj;
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
		return "DTOSchuelerSprachpruefungen(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Sprache=" + this.Sprache + ", ASDJahrgang=" + this.ASDJahrgang + ", Anspruchsniveau=" + this.Anspruchsniveau + ", Pruefungsdatum=" + this.Pruefungsdatum + ", ErsetzteSprache=" + this.ErsetzteSprache + ", IstHSUPruefung=" + this.IstHSUPruefung + ", IstFeststellungspruefung=" + this.IstFeststellungspruefung + ", KannErstePflichtfremdspracheErsetzen=" + this.KannErstePflichtfremdspracheErsetzen + ", KannZweitePflichtfremdspracheErsetzen=" + this.KannZweitePflichtfremdspracheErsetzen + ", KannWahlpflichtfremdspracheErsetzen=" + this.KannWahlpflichtfremdspracheErsetzen + ", KannBelegungAlsFortgefuehrteSpracheErlauben=" + this.KannBelegungAlsFortgefuehrteSpracheErlauben + ", Referenzniveau=" + this.Referenzniveau + ", NotePruefung=" + this.NotePruefung + ")";
	}

}
