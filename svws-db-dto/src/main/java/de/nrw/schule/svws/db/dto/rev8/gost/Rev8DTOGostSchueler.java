package de.nrw.schule.svws.db.dto.rev8.gost;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.DatumConverter;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Schueler")
@NamedQuery(name="Rev8DTOGostSchueler.all", query="SELECT e FROM Rev8DTOGostSchueler e")
@NamedQuery(name="Rev8DTOGostSchueler.schueler_id", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.Schueler_ID = :value")
@NamedQuery(name="Rev8DTOGostSchueler.schueler_id.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.datumberatung", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.DatumBeratung = :value")
@NamedQuery(name="Rev8DTOGostSchueler.datumberatung.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.DatumBeratung IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.datumruecklauf", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.DatumRuecklauf = :value")
@NamedQuery(name="Rev8DTOGostSchueler.datumruecklauf.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.DatumRuecklauf IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.hatsprachpraktischepruefung", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.HatSprachPraktischePruefung = :value")
@NamedQuery(name="Rev8DTOGostSchueler.hatsprachpraktischepruefung.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.HatSprachPraktischePruefung IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.hatsportattest", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.HatSportattest = :value")
@NamedQuery(name="Rev8DTOGostSchueler.hatsportattest.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.HatSportattest IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.kommentar", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.Kommentar = :value")
@NamedQuery(name="Rev8DTOGostSchueler.kommentar.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.Kommentar IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.beratungslehrer_id", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.Beratungslehrer_ID = :value")
@NamedQuery(name="Rev8DTOGostSchueler.beratungslehrer_id.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.Beratungslehrer_ID IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.pruefphase", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.PruefPhase = :value")
@NamedQuery(name="Rev8DTOGostSchueler.pruefphase.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.PruefPhase IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.besonderelernleistung_art", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.BesondereLernleistung_Art = :value")
@NamedQuery(name="Rev8DTOGostSchueler.besonderelernleistung_art.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.BesondereLernleistung_Art IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.besonderelernleistung_punkte", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.BesondereLernleistung_Punkte = :value")
@NamedQuery(name="Rev8DTOGostSchueler.besonderelernleistung_punkte.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.BesondereLernleistung_Punkte IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.zweitefremdpracheinsekivorhanden", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.ZweiteFremdpracheInSekIVorhanden = :value")
@NamedQuery(name="Rev8DTOGostSchueler.zweitefremdpracheinsekivorhanden.multiple", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.ZweiteFremdpracheInSekIVorhanden IN :value")
@NamedQuery(name="Rev8DTOGostSchueler.primaryKeyQuery", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.Schueler_ID = ?1")
@NamedQuery(name="Rev8DTOGostSchueler.all.migration", query="SELECT e FROM Rev8DTOGostSchueler e WHERE e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID","DatumBeratung","DatumRuecklauf","HatSprachPraktischePruefung","HatSportattest","Kommentar","Beratungslehrer_ID","PruefPhase","BesondereLernleistung_Art","BesondereLernleistung_Punkte","ZweiteFremdpracheInSekIVorhanden"})
public class Rev8DTOGostSchueler {

	/** Gymnasiale Oberstufe - Schülerdaten: Die ID des Schülers in der Schülertabelle */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Gymnasiale Oberstufe - Schülerdaten: Das Datum der letzten Beratung des Schülers */
	@Column(name = "DatumBeratung")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String DatumBeratung;

	/** Gymnasiale Oberstufe - Schülerdaten: Das Datum an dem der letzte Beratungsbogen des Schülersmit seiner Fächerwahl in der Schule eingereicht wurde */
	@Column(name = "DatumRuecklauf")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String DatumRuecklauf;

	/** Gymnasiale Oberstufe - Schülerdaten: Gibt an, ob eine Teilnahme an einer sprachpraktischen Prüfung erfolgt ist: 1 - true, 0 - false */
	@Column(name = "HatSprachPraktischePruefung")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean HatSprachPraktischePruefung;

	/** Gymnasiale Oberstufe - Schülerdaten: Gibt an, ob ein Sportattest bei dem Schüler vorliegt oder nicht und die Wahl eines Ersatzfaches zulässig ist: 1 - true, 0 - false */
	@Column(name = "HatSportattest")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
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

	/** Gymnasiale Oberstufe - Schülerdaten: Gibt an, ob die Belegung der zweiten Fremdsprache in der Sek I manuell geprüft wurde und eine Prüfung bei der Laufbahnplanung entfallen kann: 1 - true, 0 - false */
	@Column(name = "ZweiteFremdpracheInSekIVorhanden")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean ZweiteFremdpracheInSekIVorhanden;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOGostSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostSchueler ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param HatSprachPraktischePruefung   der Wert für das Attribut HatSprachPraktischePruefung
	 * @param HatSportattest   der Wert für das Attribut HatSportattest
	 * @param ZweiteFremdpracheInSekIVorhanden   der Wert für das Attribut ZweiteFremdpracheInSekIVorhanden
	 */
	public Rev8DTOGostSchueler(final Long Schueler_ID, final Boolean HatSprachPraktischePruefung, final Boolean HatSportattest, final Boolean ZweiteFremdpracheInSekIVorhanden) {
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (HatSprachPraktischePruefung == null) { 
			throw new NullPointerException("HatSprachPraktischePruefung must not be null");
		}
		this.HatSprachPraktischePruefung = HatSprachPraktischePruefung;
		if (HatSportattest == null) { 
			throw new NullPointerException("HatSportattest must not be null");
		}
		this.HatSportattest = HatSportattest;
		if (ZweiteFremdpracheInSekIVorhanden == null) { 
			throw new NullPointerException("ZweiteFremdpracheInSekIVorhanden must not be null");
		}
		this.ZweiteFremdpracheInSekIVorhanden = ZweiteFremdpracheInSekIVorhanden;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOGostSchueler other = (Rev8DTOGostSchueler) obj;
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
		return "Rev8DTOGostSchueler(Schueler_ID=" + this.Schueler_ID + ", DatumBeratung=" + this.DatumBeratung + ", DatumRuecklauf=" + this.DatumRuecklauf + ", HatSprachPraktischePruefung=" + this.HatSprachPraktischePruefung + ", HatSportattest=" + this.HatSportattest + ", Kommentar=" + this.Kommentar + ", Beratungslehrer_ID=" + this.Beratungslehrer_ID + ", PruefPhase=" + this.PruefPhase + ", BesondereLernleistung_Art=" + this.BesondereLernleistung_Art + ", BesondereLernleistung_Punkte=" + this.BesondereLernleistung_Punkte + ", ZweiteFremdpracheInSekIVorhanden=" + this.ZweiteFremdpracheInSekIVorhanden + ")";
	}

}