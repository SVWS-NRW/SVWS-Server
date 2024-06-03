package de.svws_nrw.db.dto.current.schild.impexp;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle ImpExp_EigeneImporte_Felder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOEigeneImporteFelderPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ImpExp_EigeneImporte_Felder")
@JsonPropertyOrder({"Import_ID", "Field_ID", "TableDescription", "FieldDescription", "SrcPosition", "DstTable", "DstFieldName", "DstFieldType", "DstFieldIsIdentifier", "DstLookupDir", "DstLookupTable", "DstLookupFieldName", "DstLookupTableIDFieldName", "DstResultFieldName", "DstKeyLookupInsert", "DstKeyLookupNameCreateID", "DstForceNumeric"})
public final class DTOEigeneImporteFelder {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOEigeneImporteFelder e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.Import_ID = ?1 AND e.Field_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.Import_ID IS NOT NULL AND e.Field_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Import_ID */
	public static final String QUERY_BY_IMPORT_ID = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.Import_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Import_ID */
	public static final String QUERY_LIST_BY_IMPORT_ID = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.Import_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Field_ID */
	public static final String QUERY_BY_FIELD_ID = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.Field_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Field_ID */
	public static final String QUERY_LIST_BY_FIELD_ID = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.Field_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TableDescription */
	public static final String QUERY_BY_TABLEDESCRIPTION = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.TableDescription = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TableDescription */
	public static final String QUERY_LIST_BY_TABLEDESCRIPTION = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.TableDescription IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FieldDescription */
	public static final String QUERY_BY_FIELDDESCRIPTION = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.FieldDescription = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FieldDescription */
	public static final String QUERY_LIST_BY_FIELDDESCRIPTION = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.FieldDescription IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SrcPosition */
	public static final String QUERY_BY_SRCPOSITION = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.SrcPosition = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SrcPosition */
	public static final String QUERY_LIST_BY_SRCPOSITION = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.SrcPosition IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstTable */
	public static final String QUERY_BY_DSTTABLE = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstTable = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstTable */
	public static final String QUERY_LIST_BY_DSTTABLE = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstTable IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstFieldName */
	public static final String QUERY_BY_DSTFIELDNAME = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstFieldName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstFieldName */
	public static final String QUERY_LIST_BY_DSTFIELDNAME = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstFieldName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstFieldType */
	public static final String QUERY_BY_DSTFIELDTYPE = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstFieldType = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstFieldType */
	public static final String QUERY_LIST_BY_DSTFIELDTYPE = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstFieldType IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstFieldIsIdentifier */
	public static final String QUERY_BY_DSTFIELDISIDENTIFIER = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstFieldIsIdentifier = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstFieldIsIdentifier */
	public static final String QUERY_LIST_BY_DSTFIELDISIDENTIFIER = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstFieldIsIdentifier IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstLookupDir */
	public static final String QUERY_BY_DSTLOOKUPDIR = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstLookupDir = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstLookupDir */
	public static final String QUERY_LIST_BY_DSTLOOKUPDIR = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstLookupDir IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstLookupTable */
	public static final String QUERY_BY_DSTLOOKUPTABLE = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstLookupTable = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstLookupTable */
	public static final String QUERY_LIST_BY_DSTLOOKUPTABLE = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstLookupTable IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstLookupFieldName */
	public static final String QUERY_BY_DSTLOOKUPFIELDNAME = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstLookupFieldName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstLookupFieldName */
	public static final String QUERY_LIST_BY_DSTLOOKUPFIELDNAME = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstLookupFieldName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstLookupTableIDFieldName */
	public static final String QUERY_BY_DSTLOOKUPTABLEIDFIELDNAME = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstLookupTableIDFieldName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstLookupTableIDFieldName */
	public static final String QUERY_LIST_BY_DSTLOOKUPTABLEIDFIELDNAME = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstLookupTableIDFieldName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstResultFieldName */
	public static final String QUERY_BY_DSTRESULTFIELDNAME = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstResultFieldName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstResultFieldName */
	public static final String QUERY_LIST_BY_DSTRESULTFIELDNAME = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstResultFieldName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstKeyLookupInsert */
	public static final String QUERY_BY_DSTKEYLOOKUPINSERT = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstKeyLookupInsert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstKeyLookupInsert */
	public static final String QUERY_LIST_BY_DSTKEYLOOKUPINSERT = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstKeyLookupInsert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstKeyLookupNameCreateID */
	public static final String QUERY_BY_DSTKEYLOOKUPNAMECREATEID = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstKeyLookupNameCreateID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstKeyLookupNameCreateID */
	public static final String QUERY_LIST_BY_DSTKEYLOOKUPNAMECREATEID = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstKeyLookupNameCreateID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstForceNumeric */
	public static final String QUERY_BY_DSTFORCENUMERIC = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstForceNumeric = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstForceNumeric */
	public static final String QUERY_LIST_BY_DSTFORCENUMERIC = "SELECT e FROM DTOEigeneImporteFelder e WHERE e.DstForceNumeric IN ?1";

	/** externen Textimport Felder */
	@Id
	@Column(name = "Import_ID")
	@JsonProperty
	public int Import_ID;

	/** externen Textimport Felder */
	@Id
	@Column(name = "Field_ID")
	@JsonProperty
	public Integer Field_ID;

	/** externen Textimport Felder */
	@Column(name = "TableDescription")
	@JsonProperty
	public String TableDescription;

	/** externen Textimport Felder */
	@Column(name = "FieldDescription")
	@JsonProperty
	public String FieldDescription;

	/** externen Textimport Felder */
	@Column(name = "SrcPosition")
	@JsonProperty
	public Integer SrcPosition;

	/** externen Textimport Felder */
	@Column(name = "DstTable")
	@JsonProperty
	public String DstTable;

	/** externen Textimport Felder */
	@Column(name = "DstFieldName")
	@JsonProperty
	public String DstFieldName;

	/** externen Textimport Felder */
	@Column(name = "DstFieldType")
	@JsonProperty
	public String DstFieldType;

	/** externen Textimport Felder */
	@Column(name = "DstFieldIsIdentifier")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean DstFieldIsIdentifier;

	/** externen Textimport Felder */
	@Column(name = "DstLookupDir")
	@JsonProperty
	public String DstLookupDir;

	/** externen Textimport Felder */
	@Column(name = "DstLookupTable")
	@JsonProperty
	public String DstLookupTable;

	/** externen Textimport Felder */
	@Column(name = "DstLookupFieldName")
	@JsonProperty
	public String DstLookupFieldName;

	/** externen Textimport Felder */
	@Column(name = "DstLookupTableIDFieldName")
	@JsonProperty
	public String DstLookupTableIDFieldName;

	/** externen Textimport Felder */
	@Column(name = "DstResultFieldName")
	@JsonProperty
	public String DstResultFieldName;

	/** externen Textimport Felder */
	@Column(name = "DstKeyLookupInsert")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean DstKeyLookupInsert;

	/** externen Textimport Felder */
	@Column(name = "DstKeyLookupNameCreateID")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean DstKeyLookupNameCreateID;

	/** externen Textimport Felder */
	@Column(name = "DstForceNumeric")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean DstForceNumeric;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporteFelder ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEigeneImporteFelder() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporteFelder ohne eine Initialisierung der Attribute.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 */
	public DTOEigeneImporteFelder(final int Import_ID) {
		this.Import_ID = Import_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEigeneImporteFelder other = (DTOEigeneImporteFelder) obj;
		if (Import_ID != other.Import_ID)
			return false;
		if (Field_ID == null) {
			if (other.Field_ID != null)
				return false;
		} else if (!Field_ID.equals(other.Field_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Import_ID);

		result = prime * result + ((Field_ID == null) ? 0 : Field_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOEigeneImporteFelder(Import_ID=" + this.Import_ID + ", Field_ID=" + this.Field_ID + ", TableDescription=" + this.TableDescription + ", FieldDescription=" + this.FieldDescription + ", SrcPosition=" + this.SrcPosition + ", DstTable=" + this.DstTable + ", DstFieldName=" + this.DstFieldName + ", DstFieldType=" + this.DstFieldType + ", DstFieldIsIdentifier=" + this.DstFieldIsIdentifier + ", DstLookupDir=" + this.DstLookupDir + ", DstLookupTable=" + this.DstLookupTable + ", DstLookupFieldName=" + this.DstLookupFieldName + ", DstLookupTableIDFieldName=" + this.DstLookupTableIDFieldName + ", DstResultFieldName=" + this.DstResultFieldName + ", DstKeyLookupInsert=" + this.DstKeyLookupInsert + ", DstKeyLookupNameCreateID=" + this.DstKeyLookupNameCreateID + ", DstForceNumeric=" + this.DstForceNumeric + ")";
	}

}
