package de.nrw.schule.svws.db.dto.migration.schild.impexp;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBoolean01Converter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle ImpExp_EigeneImporte_Tabellen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOEigeneImporteTabellenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ImpExp_EigeneImporte_Tabellen")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.all", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.import_id", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.Import_ID = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.import_id.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.Import_ID IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.tablename", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.TableName = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.tablename.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.TableName IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.dstcreateid", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.DstCreateID = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.dstcreateid.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.DstCreateID IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.dstidfieldname", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.DstIDFieldName = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.dstidfieldname.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.DstIDFieldName IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.sequence", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.Sequence = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.sequence.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.Sequence IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookuptable", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupTable = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookuptable.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupTable IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupfields", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupFields = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupfields.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupFields IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupfieldtypes", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupFieldTypes = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupfieldtypes.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupFieldTypes IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupfieldpos", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupFieldPos = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupfieldpos.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupFieldPos IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupkeyfield", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupKeyField = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupkeyfield.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupKeyField IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupresultfield", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupResultField = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupresultfield.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupResultField IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupresultfieldtype", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupResultFieldType = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.lookupresultfieldtype.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.LookupResultFieldType IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.dstdefaultfieldname", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.DstDefaultFieldName = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.dstdefaultfieldname.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.DstDefaultFieldName IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.dstdefaultfieldvalue", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.DstDefaultFieldValue = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.dstdefaultfieldvalue.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.DstDefaultFieldValue IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.gu_id_field", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.GU_ID_Field = :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.gu_id_field.multiple", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.GU_ID_Field IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.primaryKeyQuery", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.Import_ID = ?1 AND e.TableName = ?2")
@NamedQuery(name="MigrationDTOEigeneImporteTabellen.all.migration", query="SELECT e FROM MigrationDTOEigeneImporteTabellen e WHERE e.Import_ID IS NOT NULL AND e.TableName IS NOT NULL")
@JsonPropertyOrder({"Import_ID","TableName","DstCreateID","DstIDFieldName","Sequence","LookupTable","LookupFields","LookupFieldTypes","LookupFieldPos","LookupKeyField","LookupResultField","LookupResultFieldType","DstDefaultFieldName","DstDefaultFieldValue","GU_ID_Field"})
public class MigrationDTOEigeneImporteTabellen {

	/** externen Textimport Tabellen */
	@Id
	@Column(name = "Import_ID")
	@JsonProperty
	public Integer Import_ID;

	/** externen Textimport Tabellen */
	@Id
	@Column(name = "TableName")
	@JsonProperty
	public String TableName;

	/** externen Textimport Tabellen */
	@Column(name = "DstCreateID")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneImporteTabellen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOEigeneImporteTabellen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneImporteTabellen ohne eine Initialisierung der Attribute.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 */
	public MigrationDTOEigeneImporteTabellen(final Integer Import_ID) {
		if (Import_ID == null) { 
			throw new NullPointerException("Import_ID must not be null");
		}
		this.Import_ID = Import_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOEigeneImporteTabellen other = (MigrationDTOEigeneImporteTabellen) obj;
		if (Import_ID == null) {
			if (other.Import_ID != null)
				return false;
		} else if (!Import_ID.equals(other.Import_ID))
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
		result = prime * result + ((Import_ID == null) ? 0 : Import_ID.hashCode());

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
		return "MigrationDTOEigeneImporteTabellen(Import_ID=" + this.Import_ID + ", TableName=" + this.TableName + ", DstCreateID=" + this.DstCreateID + ", DstIDFieldName=" + this.DstIDFieldName + ", Sequence=" + this.Sequence + ", LookupTable=" + this.LookupTable + ", LookupFields=" + this.LookupFields + ", LookupFieldTypes=" + this.LookupFieldTypes + ", LookupFieldPos=" + this.LookupFieldPos + ", LookupKeyField=" + this.LookupKeyField + ", LookupResultField=" + this.LookupResultField + ", LookupResultFieldType=" + this.LookupResultFieldType + ", DstDefaultFieldName=" + this.DstDefaultFieldName + ", DstDefaultFieldValue=" + this.DstDefaultFieldValue + ", GU_ID_Field=" + this.GU_ID_Field + ")";
	}

}