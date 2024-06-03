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
 * Diese Klasse dient als DTO für die Datenbanktabelle ImpExp_EigeneImporte_Tabellen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOEigeneImporteTabellenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ImpExp_EigeneImporte_Tabellen")
@JsonPropertyOrder({"Import_ID", "TableName", "DstCreateID", "DstIDFieldName", "Sequence", "LookupTable", "LookupFields", "LookupFieldTypes", "LookupFieldPos", "LookupKeyField", "LookupResultField", "LookupResultFieldType", "DstDefaultFieldName", "DstDefaultFieldValue", "GU_ID_Field"})
public final class DTOEigeneImporteTabellen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOEigeneImporteTabellen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Import_ID = ?1 AND e.TableName = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Import_ID IS NOT NULL AND e.TableName IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Import_ID */
	public static final String QUERY_BY_IMPORT_ID = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Import_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Import_ID */
	public static final String QUERY_LIST_BY_IMPORT_ID = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Import_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TableName */
	public static final String QUERY_BY_TABLENAME = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.TableName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TableName */
	public static final String QUERY_LIST_BY_TABLENAME = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.TableName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstCreateID */
	public static final String QUERY_BY_DSTCREATEID = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstCreateID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstCreateID */
	public static final String QUERY_LIST_BY_DSTCREATEID = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstCreateID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstIDFieldName */
	public static final String QUERY_BY_DSTIDFIELDNAME = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstIDFieldName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstIDFieldName */
	public static final String QUERY_LIST_BY_DSTIDFIELDNAME = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstIDFieldName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sequence */
	public static final String QUERY_BY_SEQUENCE = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Sequence = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sequence */
	public static final String QUERY_LIST_BY_SEQUENCE = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Sequence IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LookupTable */
	public static final String QUERY_BY_LOOKUPTABLE = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupTable = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LookupTable */
	public static final String QUERY_LIST_BY_LOOKUPTABLE = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupTable IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LookupFields */
	public static final String QUERY_BY_LOOKUPFIELDS = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFields = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LookupFields */
	public static final String QUERY_LIST_BY_LOOKUPFIELDS = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFields IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LookupFieldTypes */
	public static final String QUERY_BY_LOOKUPFIELDTYPES = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFieldTypes = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LookupFieldTypes */
	public static final String QUERY_LIST_BY_LOOKUPFIELDTYPES = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFieldTypes IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LookupFieldPos */
	public static final String QUERY_BY_LOOKUPFIELDPOS = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFieldPos = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LookupFieldPos */
	public static final String QUERY_LIST_BY_LOOKUPFIELDPOS = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFieldPos IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LookupKeyField */
	public static final String QUERY_BY_LOOKUPKEYFIELD = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupKeyField = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LookupKeyField */
	public static final String QUERY_LIST_BY_LOOKUPKEYFIELD = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupKeyField IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LookupResultField */
	public static final String QUERY_BY_LOOKUPRESULTFIELD = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupResultField = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LookupResultField */
	public static final String QUERY_LIST_BY_LOOKUPRESULTFIELD = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupResultField IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LookupResultFieldType */
	public static final String QUERY_BY_LOOKUPRESULTFIELDTYPE = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupResultFieldType = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LookupResultFieldType */
	public static final String QUERY_LIST_BY_LOOKUPRESULTFIELDTYPE = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupResultFieldType IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstDefaultFieldName */
	public static final String QUERY_BY_DSTDEFAULTFIELDNAME = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstDefaultFieldName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstDefaultFieldName */
	public static final String QUERY_LIST_BY_DSTDEFAULTFIELDNAME = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstDefaultFieldName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DstDefaultFieldValue */
	public static final String QUERY_BY_DSTDEFAULTFIELDVALUE = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstDefaultFieldValue = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DstDefaultFieldValue */
	public static final String QUERY_LIST_BY_DSTDEFAULTFIELDVALUE = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstDefaultFieldValue IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID_Field */
	public static final String QUERY_BY_GU_ID_FIELD = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.GU_ID_Field = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID_Field */
	public static final String QUERY_LIST_BY_GU_ID_FIELD = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.GU_ID_Field IN ?1";

	/** externen Textimport Tabellen */
	@Id
	@Column(name = "Import_ID")
	@JsonProperty
	public int Import_ID;

	/** externen Textimport Tabellen */
	@Id
	@Column(name = "TableName")
	@JsonProperty
	public String TableName;

	/** externen Textimport Tabellen */
	@Column(name = "DstCreateID")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean DstCreateID;

	/** externen Textimport Tabellen */
	@Column(name = "DstIDFieldName")
	@JsonProperty
	public String DstIDFieldName;

	/** externen Textimport Tabellen */
	@Column(name = "Sequence")
	@JsonProperty
	public Integer Sequence;

	/** externen Textimport Tabellen */
	@Column(name = "LookupTable")
	@JsonProperty
	public String LookupTable;

	/** externen Textimport Tabellen */
	@Column(name = "LookupFields")
	@JsonProperty
	public String LookupFields;

	/** externen Textimport Tabellen */
	@Column(name = "LookupFieldTypes")
	@JsonProperty
	public String LookupFieldTypes;

	/** externen Textimport Tabellen */
	@Column(name = "LookupFieldPos")
	@JsonProperty
	public String LookupFieldPos;

	/** externen Textimport Tabellen */
	@Column(name = "LookupKeyField")
	@JsonProperty
	public String LookupKeyField;

	/** externen Textimport Tabellen */
	@Column(name = "LookupResultField")
	@JsonProperty
	public String LookupResultField;

	/** externen Textimport Tabellen */
	@Column(name = "LookupResultFieldType")
	@JsonProperty
	public String LookupResultFieldType;

	/** externen Textimport Tabellen */
	@Column(name = "DstDefaultFieldName")
	@JsonProperty
	public String DstDefaultFieldName;

	/** externen Textimport Tabellen */
	@Column(name = "DstDefaultFieldValue")
	@JsonProperty
	public String DstDefaultFieldValue;

	/** externen Textimport Tabellen */
	@Column(name = "GU_ID_Field")
	@JsonProperty
	public String GU_ID_Field;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporteTabellen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEigeneImporteTabellen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneImporteTabellen ohne eine Initialisierung der Attribute.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 */
	public DTOEigeneImporteTabellen(final int Import_ID) {
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
		DTOEigeneImporteTabellen other = (DTOEigeneImporteTabellen) obj;
		if (Import_ID != other.Import_ID)
			return false;
		if (TableName == null) {
			if (other.TableName != null)
				return false;
		} else if (!TableName.equals(other.TableName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Import_ID);

		result = prime * result + ((TableName == null) ? 0 : TableName.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOEigeneImporteTabellen(Import_ID=" + this.Import_ID + ", TableName=" + this.TableName + ", DstCreateID=" + this.DstCreateID + ", DstIDFieldName=" + this.DstIDFieldName + ", Sequence=" + this.Sequence + ", LookupTable=" + this.LookupTable + ", LookupFields=" + this.LookupFields + ", LookupFieldTypes=" + this.LookupFieldTypes + ", LookupFieldPos=" + this.LookupFieldPos + ", LookupKeyField=" + this.LookupKeyField + ", LookupResultField=" + this.LookupResultField + ", LookupResultFieldType=" + this.LookupResultFieldType + ", DstDefaultFieldName=" + this.DstDefaultFieldName + ", DstDefaultFieldValue=" + this.DstDefaultFieldValue + ", GU_ID_Field=" + this.GU_ID_Field + ")";
	}

}
