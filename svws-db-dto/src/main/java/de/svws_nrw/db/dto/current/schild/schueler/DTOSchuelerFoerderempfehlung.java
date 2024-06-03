package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;


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
@JsonPropertyOrder({"GU_ID", "Abschnitt_ID", "DatumAngelegt", "Klassen_ID", "Lehrer_ID", "DatumAenderungSchild", "DatumAenderungSchildWeb", "Inhaltl_Prozessbez_Komp", "Methodische_Komp", "Lern_Arbeitsverhalten", "Massn_Inhaltl_Prozessbez_Komp", "Massn_Methodische_Komp", "Massn_Lern_Arbeitsverhalten", "Verantwortlichkeit_Eltern", "Verantwortlichkeit_Schueler", "Zeitrahmen_von_Datum", "Zeitrahmen_bis_Datum", "Ueberpruefung_Datum", "Naechstes_Beratungsgespraech", "EingabeFertig", "Faecher", "Abgeschlossen"})
public final class DTOSchuelerFoerderempfehlung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerFoerderempfehlung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID */
	public static final String QUERY_BY_GU_ID = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID */
	public static final String QUERY_LIST_BY_GU_ID = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumAngelegt */
	public static final String QUERY_BY_DATUMANGELEGT = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAngelegt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumAngelegt */
	public static final String QUERY_LIST_BY_DATUMANGELEGT = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAngelegt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klassen_ID */
	public static final String QUERY_BY_KLASSEN_ID = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Klassen_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klassen_ID */
	public static final String QUERY_LIST_BY_KLASSEN_ID = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Klassen_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumAenderungSchild */
	public static final String QUERY_BY_DATUMAENDERUNGSCHILD = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAenderungSchild = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumAenderungSchild */
	public static final String QUERY_LIST_BY_DATUMAENDERUNGSCHILD = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAenderungSchild IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumAenderungSchildWeb */
	public static final String QUERY_BY_DATUMAENDERUNGSCHILDWEB = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAenderungSchildWeb = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumAenderungSchildWeb */
	public static final String QUERY_LIST_BY_DATUMAENDERUNGSCHILDWEB = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.DatumAenderungSchildWeb IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Inhaltl_Prozessbez_Komp */
	public static final String QUERY_BY_INHALTL_PROZESSBEZ_KOMP = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Inhaltl_Prozessbez_Komp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Inhaltl_Prozessbez_Komp */
	public static final String QUERY_LIST_BY_INHALTL_PROZESSBEZ_KOMP = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Inhaltl_Prozessbez_Komp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Methodische_Komp */
	public static final String QUERY_BY_METHODISCHE_KOMP = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Methodische_Komp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Methodische_Komp */
	public static final String QUERY_LIST_BY_METHODISCHE_KOMP = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Methodische_Komp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lern_Arbeitsverhalten */
	public static final String QUERY_BY_LERN_ARBEITSVERHALTEN = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Lern_Arbeitsverhalten = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lern_Arbeitsverhalten */
	public static final String QUERY_LIST_BY_LERN_ARBEITSVERHALTEN = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Lern_Arbeitsverhalten IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Massn_Inhaltl_Prozessbez_Komp */
	public static final String QUERY_BY_MASSN_INHALTL_PROZESSBEZ_KOMP = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Inhaltl_Prozessbez_Komp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Massn_Inhaltl_Prozessbez_Komp */
	public static final String QUERY_LIST_BY_MASSN_INHALTL_PROZESSBEZ_KOMP = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Inhaltl_Prozessbez_Komp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Massn_Methodische_Komp */
	public static final String QUERY_BY_MASSN_METHODISCHE_KOMP = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Methodische_Komp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Massn_Methodische_Komp */
	public static final String QUERY_LIST_BY_MASSN_METHODISCHE_KOMP = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Methodische_Komp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Massn_Lern_Arbeitsverhalten */
	public static final String QUERY_BY_MASSN_LERN_ARBEITSVERHALTEN = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Lern_Arbeitsverhalten = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Massn_Lern_Arbeitsverhalten */
	public static final String QUERY_LIST_BY_MASSN_LERN_ARBEITSVERHALTEN = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Massn_Lern_Arbeitsverhalten IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Verantwortlichkeit_Eltern */
	public static final String QUERY_BY_VERANTWORTLICHKEIT_ELTERN = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Verantwortlichkeit_Eltern = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Verantwortlichkeit_Eltern */
	public static final String QUERY_LIST_BY_VERANTWORTLICHKEIT_ELTERN = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Verantwortlichkeit_Eltern IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Verantwortlichkeit_Schueler */
	public static final String QUERY_BY_VERANTWORTLICHKEIT_SCHUELER = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Verantwortlichkeit_Schueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Verantwortlichkeit_Schueler */
	public static final String QUERY_LIST_BY_VERANTWORTLICHKEIT_SCHUELER = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Verantwortlichkeit_Schueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zeitrahmen_von_Datum */
	public static final String QUERY_BY_ZEITRAHMEN_VON_DATUM = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Zeitrahmen_von_Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zeitrahmen_von_Datum */
	public static final String QUERY_LIST_BY_ZEITRAHMEN_VON_DATUM = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Zeitrahmen_von_Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zeitrahmen_bis_Datum */
	public static final String QUERY_BY_ZEITRAHMEN_BIS_DATUM = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Zeitrahmen_bis_Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zeitrahmen_bis_Datum */
	public static final String QUERY_LIST_BY_ZEITRAHMEN_BIS_DATUM = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Zeitrahmen_bis_Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ueberpruefung_Datum */
	public static final String QUERY_BY_UEBERPRUEFUNG_DATUM = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Ueberpruefung_Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ueberpruefung_Datum */
	public static final String QUERY_LIST_BY_UEBERPRUEFUNG_DATUM = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Ueberpruefung_Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Naechstes_Beratungsgespraech */
	public static final String QUERY_BY_NAECHSTES_BERATUNGSGESPRAECH = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Naechstes_Beratungsgespraech = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Naechstes_Beratungsgespraech */
	public static final String QUERY_LIST_BY_NAECHSTES_BERATUNGSGESPRAECH = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Naechstes_Beratungsgespraech IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EingabeFertig */
	public static final String QUERY_BY_EINGABEFERTIG = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.EingabeFertig = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EingabeFertig */
	public static final String QUERY_LIST_BY_EINGABEFERTIG = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.EingabeFertig IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Faecher */
	public static final String QUERY_BY_FAECHER = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Faecher = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Faecher */
	public static final String QUERY_LIST_BY_FAECHER = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Faecher IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abgeschlossen */
	public static final String QUERY_BY_ABGESCHLOSSEN = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Abgeschlossen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abgeschlossen */
	public static final String QUERY_LIST_BY_ABGESCHLOSSEN = "SELECT e FROM DTOSchuelerFoerderempfehlung e WHERE e.Abgeschlossen IN ?1";

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
