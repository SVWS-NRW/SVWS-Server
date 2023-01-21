package de.nrw.schule.svws.db.dto.dev.gost;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Schueler_Fachwahlen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DevDTOGostSchuelerFachbelegungenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Schueler_Fachwahlen")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.all", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.schueler_id", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.schueler_id.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.fach_id", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Fach_ID = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.fach_id.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ef1_kursart", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.EF1_Kursart = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ef1_kursart.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.EF1_Kursart IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ef1_punkte", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.EF1_Punkte = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ef1_punkte.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.EF1_Punkte IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ef2_kursart", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.EF2_Kursart = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ef2_kursart.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.EF2_Kursart IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ef2_punkte", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.EF2_Punkte = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ef2_punkte.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.EF2_Punkte IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q11_kursart", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q11_Kursart = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q11_kursart.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q11_Kursart IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q11_punkte", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q11_Punkte = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q11_punkte.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q11_Punkte IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q12_kursart", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q12_Kursart = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q12_kursart.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q12_Kursart IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q12_punkte", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q12_Punkte = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q12_punkte.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q12_Punkte IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q21_kursart", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q21_Kursart = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q21_kursart.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q21_Kursart IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q21_punkte", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q21_Punkte = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q21_punkte.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q21_Punkte IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q22_kursart", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q22_Kursart = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q22_kursart.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q22_Kursart IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q22_punkte", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q22_Punkte = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.q22_punkte.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Q22_Punkte IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.abiturfach", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.AbiturFach = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.abiturfach.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.AbiturFach IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.bemerkungen", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.bemerkungen.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.markiert_q1", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q1 = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.markiert_q1.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q1 IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.markiert_q2", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q2 = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.markiert_q2.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q2 IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.markiert_q3", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q3 = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.markiert_q3.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q3 IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.markiert_q4", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q4 = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.markiert_q4.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Markiert_Q4 IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ergebnisabiturpruefung", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.ergebnisAbiturpruefung = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ergebnisabiturpruefung.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.ergebnisAbiturpruefung IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.hatmuendlichepflichtpruefung", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.hatMuendlichePflichtpruefung = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.hatmuendlichepflichtpruefung.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.hatMuendlichePflichtpruefung IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ergebnismuendlichepruefung", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.ergebnisMuendlichePruefung = :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.ergebnismuendlichepruefung.multiple", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.ergebnisMuendlichePruefung IN :value")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.primaryKeyQuery", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID = ?1 AND e.Fach_ID = ?2")
@NamedQuery(name="DevDTOGostSchuelerFachbelegungen.all.migration", query="SELECT e FROM DevDTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID IS NOT NULL AND e.Fach_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID","Fach_ID","EF1_Kursart","EF1_Punkte","EF2_Kursart","EF2_Punkte","Q11_Kursart","Q11_Punkte","Q12_Kursart","Q12_Punkte","Q21_Kursart","Q21_Punkte","Q22_Kursart","Q22_Punkte","AbiturFach","Bemerkungen","Markiert_Q1","Markiert_Q2","Markiert_Q3","Markiert_Q4","ergebnisAbiturpruefung","hatMuendlichePflichtpruefung","ergebnisMuendlichePruefung"})
public class DevDTOGostSchuelerFachbelegungen {

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Schülers in der Schuelertabelle */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen: ID des Faches in der Fächertabelle */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean Markiert_Q1;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q1.2 für die Einbringung in das Abitur markiert wurde */
	@Column(name = "Markiert_Q2")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean Markiert_Q2;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q2.1 für die Einbringung in das Abitur markiert wurde */
	@Column(name = "Markiert_Q3")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean Markiert_Q3;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob das belegte Fach in der Q2.2 für die Einbringung in das Abitur markiert wurde */
	@Column(name = "Markiert_Q4")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean Markiert_Q4;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Das Ergebnis der Abiturprüfung in einem Abiturfach (1. - 4. Fach) */
	@Column(name = "ergebnisAbiturpruefung")
	@JsonProperty
	public Integer ergebnisAbiturpruefung;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Gibt an, ob eine mündliche Pflichtprüfung im 1. - 3. Fach angesetzt werden muss (null - unbekannt, 0 - Nein, 1 - Ja)   */
	@Column(name = "hatMuendlichePflichtpruefung")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean hatMuendlichePflichtpruefung;

	/** Gymnasiale Oberstufe - Schülerdaten - Fachwahlen - Abiturberechnung: Ergebnis der mündlichen Prüfung im 1. - 3. Fach */
	@Column(name = "ergebnisMuendlichePruefung")
	@JsonProperty
	public Integer ergebnisMuendlichePruefung;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostSchuelerFachbelegungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostSchuelerFachbelegungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostSchuelerFachbelegungen ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DevDTOGostSchuelerFachbelegungen(final Long Schueler_ID, final Long Fach_ID) {
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
		DevDTOGostSchuelerFachbelegungen other = (DevDTOGostSchuelerFachbelegungen) obj;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;

		if (Fach_ID == null) {
			if (other.Fach_ID != null)
				return false;
		} else if (!Fach_ID.equals(other.Fach_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());

		result = prime * result + ((Fach_ID == null) ? 0 : Fach_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DevDTOGostSchuelerFachbelegungen(Schueler_ID=" + this.Schueler_ID + ", Fach_ID=" + this.Fach_ID + ", EF1_Kursart=" + this.EF1_Kursart + ", EF1_Punkte=" + this.EF1_Punkte + ", EF2_Kursart=" + this.EF2_Kursart + ", EF2_Punkte=" + this.EF2_Punkte + ", Q11_Kursart=" + this.Q11_Kursart + ", Q11_Punkte=" + this.Q11_Punkte + ", Q12_Kursart=" + this.Q12_Kursart + ", Q12_Punkte=" + this.Q12_Punkte + ", Q21_Kursart=" + this.Q21_Kursart + ", Q21_Punkte=" + this.Q21_Punkte + ", Q22_Kursart=" + this.Q22_Kursart + ", Q22_Punkte=" + this.Q22_Punkte + ", AbiturFach=" + this.AbiturFach + ", Bemerkungen=" + this.Bemerkungen + ", Markiert_Q1=" + this.Markiert_Q1 + ", Markiert_Q2=" + this.Markiert_Q2 + ", Markiert_Q3=" + this.Markiert_Q3 + ", Markiert_Q4=" + this.Markiert_Q4 + ", ergebnisAbiturpruefung=" + this.ergebnisAbiturpruefung + ", hatMuendlichePflichtpruefung=" + this.hatMuendlichePflichtpruefung + ", ergebnisMuendlichePruefung=" + this.ergebnisMuendlichePruefung + ")";
	}

}