package de.svws_nrw.db.dto.current.gost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Schueler_Fachwahlen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostSchuelerFachbelegungenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Schueler_Fachwahlen")
@JsonPropertyOrder({"Schueler_ID", "Fach_ID", "EF1_Kursart", "EF1_Punkte", "EF2_Kursart", "EF2_Punkte", "Q11_Kursart", "Q11_Punkte", "Q12_Kursart", "Q12_Punkte", "Q21_Kursart", "Q21_Punkte", "Q22_Kursart", "Q22_Punkte", "AbiturFach", "Bemerkungen", "Markiert_Q1", "Markiert_Q2", "Markiert_Q3", "Markiert_Q4", "ergebnisAbiturpruefung", "hatMuendlichePflichtpruefung", "ergebnisMuendlichePruefung"})
public final class DTOGostSchuelerFachbelegungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostSchuelerFachbelegungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID = ?1 AND e.Fach_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID IS NOT NULL AND e.Fach_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF1_Kursart */
	public static final String QUERY_BY_EF1_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.EF1_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF1_Kursart */
	public static final String QUERY_LIST_BY_EF1_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.EF1_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF1_Punkte */
	public static final String QUERY_BY_EF1_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.EF1_Punkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF1_Punkte */
	public static final String QUERY_LIST_BY_EF1_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.EF1_Punkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF2_Kursart */
	public static final String QUERY_BY_EF2_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.EF2_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF2_Kursart */
	public static final String QUERY_LIST_BY_EF2_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.EF2_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF2_Punkte */
	public static final String QUERY_BY_EF2_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.EF2_Punkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF2_Punkte */
	public static final String QUERY_LIST_BY_EF2_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.EF2_Punkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q11_Kursart */
	public static final String QUERY_BY_Q11_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q11_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q11_Kursart */
	public static final String QUERY_LIST_BY_Q11_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q11_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q11_Punkte */
	public static final String QUERY_BY_Q11_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q11_Punkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q11_Punkte */
	public static final String QUERY_LIST_BY_Q11_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q11_Punkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q12_Kursart */
	public static final String QUERY_BY_Q12_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q12_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q12_Kursart */
	public static final String QUERY_LIST_BY_Q12_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q12_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q12_Punkte */
	public static final String QUERY_BY_Q12_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q12_Punkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q12_Punkte */
	public static final String QUERY_LIST_BY_Q12_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q12_Punkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q21_Kursart */
	public static final String QUERY_BY_Q21_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q21_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q21_Kursart */
	public static final String QUERY_LIST_BY_Q21_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q21_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q21_Punkte */
	public static final String QUERY_BY_Q21_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q21_Punkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q21_Punkte */
	public static final String QUERY_LIST_BY_Q21_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q21_Punkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q22_Kursart */
	public static final String QUERY_BY_Q22_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q22_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q22_Kursart */
	public static final String QUERY_LIST_BY_Q22_KURSART = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q22_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q22_Punkte */
	public static final String QUERY_BY_Q22_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q22_Punkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q22_Punkte */
	public static final String QUERY_LIST_BY_Q22_PUNKTE = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Q22_Punkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbiturFach */
	public static final String QUERY_BY_ABITURFACH = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.AbiturFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbiturFach */
	public static final String QUERY_LIST_BY_ABITURFACH = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.AbiturFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Bemerkungen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Markiert_Q1 */
	public static final String QUERY_BY_MARKIERT_Q1 = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Markiert_Q1 */
	public static final String QUERY_LIST_BY_MARKIERT_Q1 = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Markiert_Q2 */
	public static final String QUERY_BY_MARKIERT_Q2 = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Markiert_Q2 */
	public static final String QUERY_LIST_BY_MARKIERT_Q2 = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Markiert_Q3 */
	public static final String QUERY_BY_MARKIERT_Q3 = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q3 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Markiert_Q3 */
	public static final String QUERY_LIST_BY_MARKIERT_Q3 = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q3 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Markiert_Q4 */
	public static final String QUERY_BY_MARKIERT_Q4 = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q4 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Markiert_Q4 */
	public static final String QUERY_LIST_BY_MARKIERT_Q4 = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q4 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ergebnisAbiturpruefung */
	public static final String QUERY_BY_ERGEBNISABITURPRUEFUNG = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.ergebnisAbiturpruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ergebnisAbiturpruefung */
	public static final String QUERY_LIST_BY_ERGEBNISABITURPRUEFUNG = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.ergebnisAbiturpruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes hatMuendlichePflichtpruefung */
	public static final String QUERY_BY_HATMUENDLICHEPFLICHTPRUEFUNG = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.hatMuendlichePflichtpruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes hatMuendlichePflichtpruefung */
	public static final String QUERY_LIST_BY_HATMUENDLICHEPFLICHTPRUEFUNG = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.hatMuendlichePflichtpruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ergebnisMuendlichePruefung */
	public static final String QUERY_BY_ERGEBNISMUENDLICHEPRUEFUNG = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.ergebnisMuendlichePruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ergebnisMuendlichePruefung */
	public static final String QUERY_LIST_BY_ERGEBNISMUENDLICHEPRUEFUNG = "SELECT e FROM DTOGostSchuelerFachbelegungen e WHERE e.ergebnisMuendlichePruefung IN ?1";

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Schülers in der Schuelertabelle */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Faches in der Fächertabelle */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in EF.1 */
	@Column(name = "EF1_Kursart")
	@JsonProperty
	public String EF1_Kursart;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in EF.1 */
	@Column(name = "EF1_Punkte")
	@JsonProperty
	public String EF1_Punkte;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in EF.2 */
	@Column(name = "EF2_Kursart")
	@JsonProperty
	public String EF2_Kursart;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in EF.2 */
	@Column(name = "EF2_Punkte")
	@JsonProperty
	public String EF2_Punkte;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in Q1.1 */
	@Column(name = "Q11_Kursart")
	@JsonProperty
	public String Q11_Kursart;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in Q1.1 */
	@Column(name = "Q11_Punkte")
	@JsonProperty
	public String Q11_Punkte;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in Q1.2 */
	@Column(name = "Q12_Kursart")
	@JsonProperty
	public String Q12_Kursart;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in Q1.2 */
	@Column(name = "Q12_Punkte")
	@JsonProperty
	public String Q12_Punkte;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in Q2.1 */
	@Column(name = "Q21_Kursart")
	@JsonProperty
	public String Q21_Kursart;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in Q2.1 */
	@Column(name = "Q21_Punkte")
	@JsonProperty
	public String Q21_Punkte;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Kursart des belegten Faches in Q2.2 */
	@Column(name = "Q22_Kursart")
	@JsonProperty
	public String Q22_Kursart;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Notenpunkte des belegten faches in Q2.2 */
	@Column(name = "Q22_Punkte")
	@JsonProperty
	public String Q22_Punkte;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Abiturfach 1 bis 4 oder null */
	@Column(name = "AbiturFach")
	@JsonProperty
	public Integer AbiturFach;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: Bemerkungen zum belegten Fach */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q1.1 für die Einbringung in das Abitur markiert wurde */
	@Column(name = "Markiert_Q1")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Markiert_Q1;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q1.2 für die Einbringung in das Abitur markiert wurde */
	@Column(name = "Markiert_Q2")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Markiert_Q2;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q2.1 für die Einbringung in das Abitur markiert wurde */
	@Column(name = "Markiert_Q3")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Markiert_Q3;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q2.2 für die Einbringung in das Abitur markiert wurde */
	@Column(name = "Markiert_Q4")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Markiert_Q4;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Das Ergebnis der Abiturprüfung in einem Abiturfach (1. - 4. Fach) */
	@Column(name = "ergebnisAbiturpruefung")
	@JsonProperty
	public Integer ergebnisAbiturpruefung;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob eine mündliche Pflichtprüfung im 1. - 3. Fach angesetzt werden muss (null - unbekannt, 0 - Nein, 1 - Ja)   */
	@Column(name = "hatMuendlichePflichtpruefung")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean hatMuendlichePflichtpruefung;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Ergebnis der mündlichen Prüfung im 1. - 3. Fach */
	@Column(name = "ergebnisMuendlichePruefung")
	@JsonProperty
	public Integer ergebnisMuendlichePruefung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchuelerFachbelegungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostSchuelerFachbelegungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchuelerFachbelegungen ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOGostSchuelerFachbelegungen(final long Schueler_ID, final long Fach_ID) {
		this.Schueler_ID = Schueler_ID;
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
		DTOGostSchuelerFachbelegungen other = (DTOGostSchuelerFachbelegungen) obj;
		if (Schueler_ID != other.Schueler_ID)
			return false;
		return Fach_ID == other.Fach_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);

		result = prime * result + Long.hashCode(Fach_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostSchuelerFachbelegungen(Schueler_ID=" + this.Schueler_ID + ", Fach_ID=" + this.Fach_ID + ", EF1_Kursart=" + this.EF1_Kursart + ", EF1_Punkte=" + this.EF1_Punkte + ", EF2_Kursart=" + this.EF2_Kursart + ", EF2_Punkte=" + this.EF2_Punkte + ", Q11_Kursart=" + this.Q11_Kursart + ", Q11_Punkte=" + this.Q11_Punkte + ", Q12_Kursart=" + this.Q12_Kursart + ", Q12_Punkte=" + this.Q12_Punkte + ", Q21_Kursart=" + this.Q21_Kursart + ", Q21_Punkte=" + this.Q21_Punkte + ", Q22_Kursart=" + this.Q22_Kursart + ", Q22_Punkte=" + this.Q22_Punkte + ", AbiturFach=" + this.AbiturFach + ", Bemerkungen=" + this.Bemerkungen + ", Markiert_Q1=" + this.Markiert_Q1 + ", Markiert_Q2=" + this.Markiert_Q2 + ", Markiert_Q3=" + this.Markiert_Q3 + ", Markiert_Q4=" + this.Markiert_Q4 + ", ergebnisAbiturpruefung=" + this.ergebnisAbiturpruefung + ", hatMuendlichePflichtpruefung=" + this.hatMuendlichePflichtpruefung + ", ergebnisMuendlichePruefung=" + this.ergebnisMuendlichePruefung + ")";
	}

}
