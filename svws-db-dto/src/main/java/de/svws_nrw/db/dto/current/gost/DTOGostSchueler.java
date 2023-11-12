package de.svws_nrw.db.dto.current.gost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.DatumConverter;


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
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Schueler")
@NamedQuery(name = "DTOGostSchueler.all", query = "SELECT e FROM DTOGostSchueler e")
@NamedQuery(name = "DTOGostSchueler.schueler_id", query = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOGostSchueler.schueler_id.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOGostSchueler.datumberatung", query = "SELECT e FROM DTOGostSchueler e WHERE e.DatumBeratung = :value")
@NamedQuery(name = "DTOGostSchueler.datumberatung.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.DatumBeratung IN :value")
@NamedQuery(name = "DTOGostSchueler.datumruecklauf", query = "SELECT e FROM DTOGostSchueler e WHERE e.DatumRuecklauf = :value")
@NamedQuery(name = "DTOGostSchueler.datumruecklauf.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.DatumRuecklauf IN :value")
@NamedQuery(name = "DTOGostSchueler.hatsportattest", query = "SELECT e FROM DTOGostSchueler e WHERE e.HatSportattest = :value")
@NamedQuery(name = "DTOGostSchueler.hatsportattest.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.HatSportattest IN :value")
@NamedQuery(name = "DTOGostSchueler.kommentar", query = "SELECT e FROM DTOGostSchueler e WHERE e.Kommentar = :value")
@NamedQuery(name = "DTOGostSchueler.kommentar.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.Kommentar IN :value")
@NamedQuery(name = "DTOGostSchueler.beratungslehrer_id", query = "SELECT e FROM DTOGostSchueler e WHERE e.Beratungslehrer_ID = :value")
@NamedQuery(name = "DTOGostSchueler.beratungslehrer_id.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.Beratungslehrer_ID IN :value")
@NamedQuery(name = "DTOGostSchueler.pruefphase", query = "SELECT e FROM DTOGostSchueler e WHERE e.PruefPhase = :value")
@NamedQuery(name = "DTOGostSchueler.pruefphase.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.PruefPhase IN :value")
@NamedQuery(name = "DTOGostSchueler.besonderelernleistung_art", query = "SELECT e FROM DTOGostSchueler e WHERE e.BesondereLernleistung_Art = :value")
@NamedQuery(name = "DTOGostSchueler.besonderelernleistung_art.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.BesondereLernleistung_Art IN :value")
@NamedQuery(name = "DTOGostSchueler.besonderelernleistung_punkte", query = "SELECT e FROM DTOGostSchueler e WHERE e.BesondereLernleistung_Punkte = :value")
@NamedQuery(name = "DTOGostSchueler.besonderelernleistung_punkte.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.BesondereLernleistung_Punkte IN :value")
@NamedQuery(name = "DTOGostSchueler.primaryKeyQuery", query = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID = ?1")
@NamedQuery(name = "DTOGostSchueler.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOGostSchueler.all.migration", query = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID", "DatumBeratung", "DatumRuecklauf", "HatSportattest", "Kommentar", "Beratungslehrer_ID", "PruefPhase", "BesondereLernleistung_Art", "BesondereLernleistung_Punkte"})
public final class DTOGostSchueler {

	/** Gymnasiale Oberstufe - Schülerdaten: Die ID des Schülers in der Schülertabelle */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Gymnasiale Oberstufe - Schülerdaten: Das Datum der letzten Beratung des Schülers */
	@Column(name = "DatumBeratung")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumBeratung;

	/** Gymnasiale Oberstufe - Schülerdaten: Das Datum an dem der letzte Beratungsbogen des Schülersmit seiner Fächerwahl in der Schule eingereicht wurde */
	@Column(name = "DatumRuecklauf")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumRuecklauf;

	/** Gymnasiale Oberstufe - Schülerdaten: Gibt an, ob ein Sportattest bei dem Schüler vorliegt oder nicht und die Wahl eines Ersatzfaches zulässig ist: 1 - true, 0 - false */
	@Column(name = "HatSportattest")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean HatSportattest;

	/** Gymnasiale Oberstufe - Schülerdaten: Kommentar des Beratungslehrers zur der Wahl des Schülers */
	@Column(name = "Kommentar")
	@JsonProperty
	public String Kommentar;

	/** Gymnasiale Oberstufe - Schülerdaten: ID des Beratungslehrers, der die letzte Beratung vorgenommen hat */
	@Column(name = "Beratungslehrer_ID")
	@JsonProperty
	public Long Beratungslehrer_ID;

	/** Gymnasiale Oberstufe - Schülerdaten: Gibt an welche Halbjahre bei der Belegprüfung geprüft werden sollen (E - nur EF.1, G - Gesamtprüfung bis einschließlich Q2.2) */
	@Column(name = "PruefPhase")
	@JsonProperty
	public String PruefPhase;

	/** Gymnasiale Oberstufe - Schülerdaten: Die Art einer besonderen Lernleistung */
	@Column(name = "BesondereLernleistung_Art")
	@JsonProperty
	public String BesondereLernleistung_Art;

	/** Gymnasiale Oberstufe - Schülerdaten: Die Notenpunkte der besonderen Lernleistung  */
	@Column(name = "BesondereLernleistung_Punkte")
	@JsonProperty
	public Integer BesondereLernleistung_Punkte;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchueler ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param HatSportattest   der Wert für das Attribut HatSportattest
	 */
	public DTOGostSchueler(final long Schueler_ID, final Boolean HatSportattest) {
		this.Schueler_ID = Schueler_ID;
		this.HatSportattest = HatSportattest;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostSchueler other = (DTOGostSchueler) obj;
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostSchueler(Schueler_ID=" + this.Schueler_ID + ", DatumBeratung=" + this.DatumBeratung + ", DatumRuecklauf=" + this.DatumRuecklauf + ", HatSportattest=" + this.HatSportattest + ", Kommentar=" + this.Kommentar + ", Beratungslehrer_ID=" + this.Beratungslehrer_ID + ", PruefPhase=" + this.PruefPhase + ", BesondereLernleistung_Art=" + this.BesondereLernleistung_Art + ", BesondereLernleistung_Punkte=" + this.BesondereLernleistung_Punkte + ")";
	}

}
