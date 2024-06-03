package de.svws_nrw.db.dto.current.schild.impexp;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle ImpExp_EigeneImporte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ImpExp_EigeneImporte")
@JsonPropertyOrder({"ID", "Title", "DelimiterChar", "TextQuote", "SkipLines", "DateFormat", "BooleanTrue", "AbkWeiblich", "AbkMaennlich", "MainTable", "InsertMode", "LookupTableDir", "SchuelerIDMode"})
public final class DTOEigeneImporte {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOEigeneImporte e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOEigeneImporte e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOEigeneImporte e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOEigeneImporte e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOEigeneImporte e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOEigeneImporte e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Title */
	public static final String QUERY_BY_TITLE = "SELECT e FROM DTOEigeneImporte e WHERE e.Title = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Title */
	public static final String QUERY_LIST_BY_TITLE = "SELECT e FROM DTOEigeneImporte e WHERE e.Title IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DelimiterChar */
	public static final String QUERY_BY_DELIMITERCHAR = "SELECT e FROM DTOEigeneImporte e WHERE e.DelimiterChar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DelimiterChar */
	public static final String QUERY_LIST_BY_DELIMITERCHAR = "SELECT e FROM DTOEigeneImporte e WHERE e.DelimiterChar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextQuote */
	public static final String QUERY_BY_TEXTQUOTE = "SELECT e FROM DTOEigeneImporte e WHERE e.TextQuote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextQuote */
	public static final String QUERY_LIST_BY_TEXTQUOTE = "SELECT e FROM DTOEigeneImporte e WHERE e.TextQuote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SkipLines */
	public static final String QUERY_BY_SKIPLINES = "SELECT e FROM DTOEigeneImporte e WHERE e.SkipLines = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SkipLines */
	public static final String QUERY_LIST_BY_SKIPLINES = "SELECT e FROM DTOEigeneImporte e WHERE e.SkipLines IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DateFormat */
	public static final String QUERY_BY_DATEFORMAT = "SELECT e FROM DTOEigeneImporte e WHERE e.DateFormat = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DateFormat */
	public static final String QUERY_LIST_BY_DATEFORMAT = "SELECT e FROM DTOEigeneImporte e WHERE e.DateFormat IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BooleanTrue */
	public static final String QUERY_BY_BOOLEANTRUE = "SELECT e FROM DTOEigeneImporte e WHERE e.BooleanTrue = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BooleanTrue */
	public static final String QUERY_LIST_BY_BOOLEANTRUE = "SELECT e FROM DTOEigeneImporte e WHERE e.BooleanTrue IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbkWeiblich */
	public static final String QUERY_BY_ABKWEIBLICH = "SELECT e FROM DTOEigeneImporte e WHERE e.AbkWeiblich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbkWeiblich */
	public static final String QUERY_LIST_BY_ABKWEIBLICH = "SELECT e FROM DTOEigeneImporte e WHERE e.AbkWeiblich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbkMaennlich */
	public static final String QUERY_BY_ABKMAENNLICH = "SELECT e FROM DTOEigeneImporte e WHERE e.AbkMaennlich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbkMaennlich */
	public static final String QUERY_LIST_BY_ABKMAENNLICH = "SELECT e FROM DTOEigeneImporte e WHERE e.AbkMaennlich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MainTable */
	public static final String QUERY_BY_MAINTABLE = "SELECT e FROM DTOEigeneImporte e WHERE e.MainTable = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MainTable */
	public static final String QUERY_LIST_BY_MAINTABLE = "SELECT e FROM DTOEigeneImporte e WHERE e.MainTable IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes InsertMode */
	public static final String QUERY_BY_INSERTMODE = "SELECT e FROM DTOEigeneImporte e WHERE e.InsertMode = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes InsertMode */
	public static final String QUERY_LIST_BY_INSERTMODE = "SELECT e FROM DTOEigeneImporte e WHERE e.InsertMode IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LookupTableDir */
	public static final String QUERY_BY_LOOKUPTABLEDIR = "SELECT e FROM DTOEigeneImporte e WHERE e.LookupTableDir = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LookupTableDir */
	public static final String QUERY_LIST_BY_LOOKUPTABLEDIR = "SELECT e FROM DTOEigeneImporte e WHERE e.LookupTableDir IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchuelerIDMode */
	public static final String QUERY_BY_SCHUELERIDMODE = "SELECT e FROM DTOEigeneImporte e WHERE e.SchuelerIDMode = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchuelerIDMode */
	public static final String QUERY_LIST_BY_SCHUELERIDMODE = "SELECT e FROM DTOEigeneImporte e WHERE e.SchuelerIDMode IN ?1";

	/** ID des Importschemas für den externen Textimport */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public int ID;

	/** Bezeichnung des Schemas */
	@Column(name = "Title")
	@JsonProperty
	public String Title;

	/** Enthält das Trennzeichen für den Import */
	@Column(name = "DelimiterChar")
	@JsonProperty
	public String DelimiterChar;

	/** Texttrennzeichen */
	@Column(name = "TextQuote")
	@JsonProperty
	public String TextQuote;

	/** externen Textimport */
	@Column(name = "SkipLines")
	@JsonProperty
	public Integer SkipLines;

	/** Format der Datumswerte */
	@Column(name = "DateFormat")
	@JsonProperty
	public String DateFormat;

	/** externen Textimport */
	@Column(name = "BooleanTrue")
	@JsonProperty
	public String BooleanTrue;

	/** Abkürzung für weiblich */
	@Column(name = "AbkWeiblich")
	@JsonProperty
	public String AbkWeiblich;

	/** Abkürzung für männlich */
	@Column(name = "AbkMaennlich")
	@JsonProperty
	public String AbkMaennlich;

	/** externen Textimport */
	@Column(name = "MainTable")
	@JsonProperty
	public String MainTable;

	/** externen Textimport */
	@Column(name = "InsertMode")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean InsertMode;

	/** externen Textimport */
	@Column(name = "LookupTableDir")
	@JsonProperty
	public String LookupTableDir;

	/** externen Textimport */
	@Column(name = "SchuelerIDMode")
	@JsonProperty
	public String SchuelerIDMode;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEigeneImporte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporte ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOEigeneImporte(final int ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEigeneImporte other = (DTOEigeneImporte) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOEigeneImporte(ID=" + this.ID + ", Title=" + this.Title + ", DelimiterChar=" + this.DelimiterChar + ", TextQuote=" + this.TextQuote + ", SkipLines=" + this.SkipLines + ", DateFormat=" + this.DateFormat + ", BooleanTrue=" + this.BooleanTrue + ", AbkWeiblich=" + this.AbkWeiblich + ", AbkMaennlich=" + this.AbkMaennlich + ", MainTable=" + this.MainTable + ", InsertMode=" + this.InsertMode + ", LookupTableDir=" + this.LookupTableDir + ", SchuelerIDMode=" + this.SchuelerIDMode + ")";
	}

}
