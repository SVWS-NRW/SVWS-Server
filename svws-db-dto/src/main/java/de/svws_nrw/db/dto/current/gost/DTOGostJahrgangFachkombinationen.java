package de.svws_nrw.db.dto.current.gost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.gost.GostLaufbahnplanungFachkombinationTypConverter;

import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.current.gost.GostLaufbahnplanungFachkombinationTypConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GostLaufbahnplanungFachkombinationTypConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Jahrgang_Fachkombinationen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Jahrgang_Fachkombinationen")
@JsonPropertyOrder({"ID", "Abi_Jahrgang", "Fach1_ID", "Fach2_ID", "Kursart1", "Kursart2", "EF1", "EF2", "Q11", "Q12", "Q21", "Q22", "Typ", "Hinweistext"})
public final class DTOGostJahrgangFachkombinationen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostJahrgangFachkombinationen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abi_Jahrgang */
	public static final String QUERY_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Abi_Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abi_Jahrgang */
	public static final String QUERY_LIST_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Abi_Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach1_ID */
	public static final String QUERY_BY_FACH1_ID = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Fach1_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach1_ID */
	public static final String QUERY_LIST_BY_FACH1_ID = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Fach1_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach2_ID */
	public static final String QUERY_BY_FACH2_ID = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Fach2_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach2_ID */
	public static final String QUERY_LIST_BY_FACH2_ID = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Fach2_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursart1 */
	public static final String QUERY_BY_KURSART1 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Kursart1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursart1 */
	public static final String QUERY_LIST_BY_KURSART1 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Kursart1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursart2 */
	public static final String QUERY_BY_KURSART2 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Kursart2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursart2 */
	public static final String QUERY_LIST_BY_KURSART2 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Kursart2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF1 */
	public static final String QUERY_BY_EF1 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.EF1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF1 */
	public static final String QUERY_LIST_BY_EF1 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.EF1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF2 */
	public static final String QUERY_BY_EF2 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.EF2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF2 */
	public static final String QUERY_LIST_BY_EF2 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.EF2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q11 */
	public static final String QUERY_BY_Q11 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Q11 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q11 */
	public static final String QUERY_LIST_BY_Q11 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Q11 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q12 */
	public static final String QUERY_BY_Q12 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Q12 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q12 */
	public static final String QUERY_LIST_BY_Q12 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Q12 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q21 */
	public static final String QUERY_BY_Q21 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Q21 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q21 */
	public static final String QUERY_LIST_BY_Q21 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Q21 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q22 */
	public static final String QUERY_BY_Q22 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Q22 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q22 */
	public static final String QUERY_LIST_BY_Q22 = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Q22 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Typ */
	public static final String QUERY_BY_TYP = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Typ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Typ */
	public static final String QUERY_LIST_BY_TYP = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Typ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Hinweistext */
	public static final String QUERY_BY_HINWEISTEXT = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Hinweistext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Hinweistext */
	public static final String QUERY_LIST_BY_HINWEISTEXT = "SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Hinweistext IN ?1";

	/** ID des Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Schuljahr, in welchem der Jahrgang das Abitur macht */
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Die ID des ersten Faches der nicht möglichen Fächerkombination / eines Fächerprofils */
	@Column(name = "Fach1_ID")
	@JsonProperty
	public long Fach1_ID;

	/** Die ID des zweiten Faches der nicht möglichen Fächerkombination / eines Fächerprofils */
	@Column(name = "Fach2_ID")
	@JsonProperty
	public long Fach2_ID;

	/** Die Kursart des ersten Faches der nicht möglichen Fächerkombination / eines Fächerprofils */
	@Column(name = "Kursart1")
	@JsonProperty
	public String Kursart1;

	/** Die Kursart des zweiten Faches der nicht möglichen Fächerkombination / eines Fächerprofils */
	@Column(name = "Kursart2")
	@JsonProperty
	public String Kursart2;

	/** Gibt an, ob die Regel in EF.1 angewendet werden soll. */
	@Column(name = "EF1")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean EF1;

	/** Gibt an, ob die Regel in EF.2 angewendet werden soll. */
	@Column(name = "EF2")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean EF2;

	/** Gibt an, ob die Regel in Q1.1 angewendet werden soll. */
	@Column(name = "Q11")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Q11;

	/** Gibt an, ob die Regel in Q1.2 angewendet werden soll. */
	@Column(name = "Q12")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Q12;

	/** Gibt an, ob die Regel in Q2.1 angewendet werden soll. */
	@Column(name = "Q21")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Q21;

	/** Gibt an, ob die Regel in Q2.2 angewendet werden soll. */
	@Column(name = "Q22")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean Q22;

	/** Gibt an, ob es sich um eine nicht mögliche Fächerkombination (0) oder ein Fächerprofil handelt (1) */
	@Column(name = "Typ")
	@JsonProperty
	@Convert(converter = GostLaufbahnplanungFachkombinationTypConverter.class)
	@JsonSerialize(using = GostLaufbahnplanungFachkombinationTypConverterSerializer.class)
	@JsonDeserialize(using = GostLaufbahnplanungFachkombinationTypConverterDeserializer.class)
	public GostLaufbahnplanungFachkombinationTyp Typ;

	/** Hinweistext, der ausgegeben wird, wenn das Fachprofil / die nicht mögliche Fächerkombination nicht erfüllt wird. */
	@Column(name = "Hinweistext")
	@JsonProperty
	public String Hinweistext;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFachkombinationen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangFachkombinationen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFachkombinationen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Fach1_ID   der Wert für das Attribut Fach1_ID
	 * @param Fach2_ID   der Wert für das Attribut Fach2_ID
	 * @param EF1   der Wert für das Attribut EF1
	 * @param EF2   der Wert für das Attribut EF2
	 * @param Q11   der Wert für das Attribut Q11
	 * @param Q12   der Wert für das Attribut Q12
	 * @param Q21   der Wert für das Attribut Q21
	 * @param Q22   der Wert für das Attribut Q22
	 * @param Typ   der Wert für das Attribut Typ
	 * @param Hinweistext   der Wert für das Attribut Hinweistext
	 */
	public DTOGostJahrgangFachkombinationen(final long ID, final int Abi_Jahrgang, final long Fach1_ID, final long Fach2_ID, final Boolean EF1, final Boolean EF2, final Boolean Q11, final Boolean Q12, final Boolean Q21, final Boolean Q22, final GostLaufbahnplanungFachkombinationTyp Typ, final String Hinweistext) {
		this.ID = ID;
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Fach1_ID = Fach1_ID;
		this.Fach2_ID = Fach2_ID;
		this.EF1 = EF1;
		this.EF2 = EF2;
		this.Q11 = Q11;
		this.Q12 = Q12;
		this.Q21 = Q21;
		this.Q22 = Q22;
		this.Typ = Typ;
		if (Hinweistext == null) {
			throw new NullPointerException("Hinweistext must not be null");
		}
		this.Hinweistext = Hinweistext;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangFachkombinationen other = (DTOGostJahrgangFachkombinationen) obj;
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
		return "DTOGostJahrgangFachkombinationen(ID=" + this.ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Fach1_ID=" + this.Fach1_ID + ", Fach2_ID=" + this.Fach2_ID + ", Kursart1=" + this.Kursart1 + ", Kursart2=" + this.Kursart2 + ", EF1=" + this.EF1 + ", EF2=" + this.EF2 + ", Q11=" + this.Q11 + ", Q12=" + this.Q12 + ", Q21=" + this.Q21 + ", Q22=" + this.Q22 + ", Typ=" + this.Typ + ", Hinweistext=" + this.Hinweistext + ")";
	}

}
