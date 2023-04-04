package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFoerderempfehlungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFoerderempfehlungen")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.all", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.gu_id", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.gu_id.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.abschnitt_id", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.abschnitt_id.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.datumangelegt", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAngelegt = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.datumangelegt.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAngelegt IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.klassen_id", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Klassen_ID = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.klassen_id.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Klassen_ID IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.lehrer_id", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.lehrer_id.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.datumaenderungschild", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAenderungSchild = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.datumaenderungschild.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAenderungSchild IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.datumaenderungschildweb", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAenderungSchildWeb = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.datumaenderungschildweb.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAenderungSchildWeb IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.inhaltl_prozessbez_komp", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Inhaltl_Prozessbez_Komp = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.inhaltl_prozessbez_komp.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Inhaltl_Prozessbez_Komp IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.methodische_komp", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Methodische_Komp = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.methodische_komp.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Methodische_Komp IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.lern_arbeitsverhalten", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Lern_Arbeitsverhalten = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.lern_arbeitsverhalten.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Lern_Arbeitsverhalten IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.massn_inhaltl_prozessbez_komp", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Inhaltl_Prozessbez_Komp = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.massn_inhaltl_prozessbez_komp.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Inhaltl_Prozessbez_Komp IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.massn_methodische_komp", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Methodische_Komp = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.massn_methodische_komp.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Methodische_Komp IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.massn_lern_arbeitsverhalten", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Lern_Arbeitsverhalten = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.massn_lern_arbeitsverhalten.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Lern_Arbeitsverhalten IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.verantwortlichkeit_eltern", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Verantwortlichkeit_Eltern = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.verantwortlichkeit_eltern.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Verantwortlichkeit_Eltern IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.verantwortlichkeit_schueler", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Verantwortlichkeit_Schueler = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.verantwortlichkeit_schueler.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Verantwortlichkeit_Schueler IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.zeitrahmen_von_datum", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Zeitrahmen_von_Datum = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.zeitrahmen_von_datum.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Zeitrahmen_von_Datum IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.zeitrahmen_bis_datum", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Zeitrahmen_bis_Datum = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.zeitrahmen_bis_datum.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Zeitrahmen_bis_Datum IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.ueberpruefung_datum", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Ueberpruefung_Datum = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.ueberpruefung_datum.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Ueberpruefung_Datum IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.naechstes_beratungsgespraech", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Naechstes_Beratungsgespraech = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.naechstes_beratungsgespraech.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Naechstes_Beratungsgespraech IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.eingabefertig", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.EingabeFertig = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.eingabefertig.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.EingabeFertig IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.faecher", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Faecher = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.faecher.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Faecher IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.abgeschlossen", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Abgeschlossen = :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.abgeschlossen.multiple", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Abgeschlossen IN :value")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID = ?1")
@NamedQuery(name = "DTOSchuelerFoerderempfehlung.all.migration", query = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID IS NOT NULL")
@JsonPropertyOrder({"GU_ID", "Abschnitt_ID", "DatumAngelegt", "Klassen_ID", "Lehrer_ID", "DatumAenderungSchild", "DatumAenderungSchildWeb", "Inhaltl_Prozessbez_Komp", "Methodische_Komp", "Lern_Arbeitsverhalten", "Massn_Inhaltl_Prozessbez_Komp", "Massn_Methodische_Komp", "Massn_Lern_Arbeitsverhalten", "Verantwortlichkeit_Eltern", "Verantwortlichkeit_Schueler", "Zeitrahmen_von_Datum", "Zeitrahmen_bis_Datum", "Ueberpruefung_Datum", "Naechstes_Beratungsgespraech", "EingabeFertig", "Faecher", "Abgeschlossen"})
public final class DTOSchuelerFoerderempfehlung {

	/** GU_ID der Förderempfehlung (wird genutzt für Import Export) */
	@Id
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Anlegedatum der Förderempfehlung */
	@Column(name = "DatumAngelegt")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumAngelegt;

	/** Klassen-ID der Förderempfehlung */
	@Column(name = "Klassen_ID")
	@JsonProperty
	public Long Klassen_ID;

	/** LehrerID der Förderempfehlung */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Änderungsdatum in Schild-NRW der Förderempfehlung */
	@Column(name = "DatumAenderungSchild")
	@JsonProperty
	public String DatumAenderungSchild;

	/** Änderungsdatum in SchildWeb der Förderempfehlung */
	@Column(name = "DatumAenderungSchildWeb")
	@JsonProperty
	public String DatumAenderungSchildWeb;

	/** Inhalt Prozessbezogene Kompetenzen der Förderempfehlung */
	@Column(name = "Inhaltl_Prozessbez_Komp")
	@JsonProperty
	public String Inhaltl_Prozessbez_Komp;

	/** Inhalte methodische Kompetenzen der Förderempfehlung */
	@Column(name = "Methodische_Komp")
	@JsonProperty
	public String Methodische_Komp;

	/** Inhalt Lern und Arbeitsverhalten der Förderempfehlung */
	@Column(name = "Lern_Arbeitsverhalten")
	@JsonProperty
	public String Lern_Arbeitsverhalten;

	/** Inhalt Maßnahmen Prozessbezogenen Kompetenzen der Förderempfehlung */
	@Column(name = "Massn_Inhaltl_Prozessbez_Komp")
	@JsonProperty
	public String Massn_Inhaltl_Prozessbez_Komp;

	/** Inhalt Maßnahmen methodische Kompetenzen der Förderempfehlung */
	@Column(name = "Massn_Methodische_Komp")
	@JsonProperty
	public String Massn_Methodische_Komp;

	/** Inhalt Maßnahmen Lern und Arbeitsverhalten der Förderempfehlung */
	@Column(name = "Massn_Lern_Arbeitsverhalten")
	@JsonProperty
	public String Massn_Lern_Arbeitsverhalten;

	/** Inhalt Verantwortlichkeit der Eltern der Förderempfehlung */
	@Column(name = "Verantwortlichkeit_Eltern")
	@JsonProperty
	public String Verantwortlichkeit_Eltern;

	/** Inhalt Verantwortlichkeit des Schülers der Förderempfehlung */
	@Column(name = "Verantwortlichkeit_Schueler")
	@JsonProperty
	public String Verantwortlichkeit_Schueler;

	/** Zeitrahmen Datum von der Förderempfehlung */
	@Column(name = "Zeitrahmen_von_Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Zeitrahmen_von_Datum;

	/** Zeitrahmen Datum bis der Förderempfehlung */
	@Column(name = "Zeitrahmen_bis_Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Zeitrahmen_bis_Datum;

	/** Datum der Überprüfung der Förderempfehlung */
	@Column(name = "Ueberpruefung_Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Ueberpruefung_Datum;

	/** Datum nächstes Beratungsgespräch der Förderempfehlung */
	@Column(name = "Naechstes_Beratungsgespraech")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Naechstes_Beratungsgespraech;

	/** Eingabe abgeschlossen Ja Nein  der Förderempfehlung */
	@Column(name = "EingabeFertig")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean EingabeFertig;

	/** Fächer der Förderempfehlung im Kürzel komma getrennt */
	@Column(name = "Faecher")
	@JsonProperty
	public String Faecher;

	/** Datum Abgeschlossen der Förderempfehlung */
	@Column(name = "Abgeschlossen")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Abgeschlossen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFoerderempfehlung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerFoerderempfehlung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFoerderempfehlung ohne eine Initialisierung der Attribute.
	 * @param GU_ID   der Wert für das Attribut GU_ID
	 * @param DatumAngelegt   der Wert für das Attribut DatumAngelegt
	 */
	public DTOSchuelerFoerderempfehlung(final String GU_ID, final String DatumAngelegt) {
		if (GU_ID == null) {
			throw new NullPointerException("GU_ID must not be null");
		}
		this.GU_ID = GU_ID;
		if (DatumAngelegt == null) {
			throw new NullPointerException("DatumAngelegt must not be null");
		}
		this.DatumAngelegt = DatumAngelegt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerFoerderempfehlung other = (DTOSchuelerFoerderempfehlung) obj;
		if (GU_ID == null) {
			if (other.GU_ID != null)
				return false;
		} else if (!GU_ID.equals(other.GU_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((GU_ID == null) ? 0 : GU_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerFoerderempfehlung(GU_ID=" + this.GU_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", DatumAngelegt=" + this.DatumAngelegt + ", Klassen_ID=" + this.Klassen_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", DatumAenderungSchild=" + this.DatumAenderungSchild + ", DatumAenderungSchildWeb=" + this.DatumAenderungSchildWeb + ", Inhaltl_Prozessbez_Komp=" + this.Inhaltl_Prozessbez_Komp + ", Methodische_Komp=" + this.Methodische_Komp + ", Lern_Arbeitsverhalten=" + this.Lern_Arbeitsverhalten + ", Massn_Inhaltl_Prozessbez_Komp=" + this.Massn_Inhaltl_Prozessbez_Komp + ", Massn_Methodische_Komp=" + this.Massn_Methodische_Komp + ", Massn_Lern_Arbeitsverhalten=" + this.Massn_Lern_Arbeitsverhalten + ", Verantwortlichkeit_Eltern=" + this.Verantwortlichkeit_Eltern + ", Verantwortlichkeit_Schueler=" + this.Verantwortlichkeit_Schueler + ", Zeitrahmen_von_Datum=" + this.Zeitrahmen_von_Datum + ", Zeitrahmen_bis_Datum=" + this.Zeitrahmen_bis_Datum + ", Ueberpruefung_Datum=" + this.Ueberpruefung_Datum + ", Naechstes_Beratungsgespraech=" + this.Naechstes_Beratungsgespraech + ", EingabeFertig=" + this.EingabeFertig + ", Faecher=" + this.Faecher + ", Abgeschlossen=" + this.Abgeschlossen + ")";
	}

}
