package de.nrw.schule.svws.db.dto.dev.schild.impexp;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle ImpExp_EigeneImporte_Tabellen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DevDTOEigeneImporteTabellenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ImpExp_EigeneImporte_Tabellen")
@NamedQuery(name="DevDTOEigeneImporteTabellen.all", query="SELECT e FROM DevDTOEigeneImporteTabellen e")
@NamedQuery(name="DevDTOEigeneImporteTabellen.import_id", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.Import_ID = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.import_id.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.Import_ID IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.tablename", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.TableName = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.tablename.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.TableName IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.dstcreateid", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.DstCreateID = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.dstcreateid.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.DstCreateID IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.dstidfieldname", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.DstIDFieldName = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.dstidfieldname.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.DstIDFieldName IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.sequence", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.Sequence = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.sequence.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.Sequence IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookuptable", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupTable = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookuptable.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupTable IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupfields", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupFields = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupfields.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupFields IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupfieldtypes", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupFieldTypes = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupfieldtypes.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupFieldTypes IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupfieldpos", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupFieldPos = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupfieldpos.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupFieldPos IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupkeyfield", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupKeyField = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupkeyfield.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupKeyField IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupresultfield", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupResultField = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupresultfield.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupResultField IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupresultfieldtype", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupResultFieldType = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.lookupresultfieldtype.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.LookupResultFieldType IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.dstdefaultfieldname", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.DstDefaultFieldName = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.dstdefaultfieldname.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.DstDefaultFieldName IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.dstdefaultfieldvalue", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.DstDefaultFieldValue = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.dstdefaultfieldvalue.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.DstDefaultFieldValue IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.gu_id_field", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.GU_ID_Field = :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.gu_id_field.multiple", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.GU_ID_Field IN :value")
@NamedQuery(name="DevDTOEigeneImporteTabellen.primaryKeyQuery", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.Import_ID = ?1 AND e.TableName = ?2")
@NamedQuery(name="DevDTOEigeneImporteTabellen.all.migration", query="SELECT e FROM DevDTOEigeneImporteTabellen e WHERE e.Import_ID IS NOT NULL AND e.TableName IS NOT NULL")
@JsonPropertyOrder({"Import_ID","TableName","DstCreateID","DstIDFieldName","Sequence","LookupTable","LookupFields","LookupFieldTypes","LookupFieldPos","LookupKeyField","LookupResultField","LookupResultFieldType","DstDefaultFieldName","DstDefaultFieldValue","GU_ID_Field"})
public class DevDTOEigeneImporteTabellen {

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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse DevDTOEigeneImporteTabellen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOEigeneImporteTabellen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOEigeneImporteTabellen ohne eine Initialisierung der Attribute.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 */
	public DevDTOEigeneImporteTabellen(final Integer Import_ID) {
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
		DevDTOEigeneImporteTabellen other = (DevDTOEigeneImporteTabellen) obj;
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
		return "DevDTOEigeneImporteTabellen(Import_ID=" + this.Import_ID + ", TableName=" + this.TableName + ", DstCreateID=" + this.DstCreateID + ", DstIDFieldName=" + this.DstIDFieldName + ", Sequence=" + this.Sequence + ", LookupTable=" + this.LookupTable + ", LookupFields=" + this.LookupFields + ", LookupFieldTypes=" + this.LookupFieldTypes + ", LookupFieldPos=" + this.LookupFieldPos + ", LookupKeyField=" + this.LookupKeyField + ", LookupResultField=" + this.LookupResultField + ", LookupResultFieldType=" + this.LookupResultFieldType + ", DstDefaultFieldName=" + this.DstDefaultFieldName + ", DstDefaultFieldValue=" + this.DstDefaultFieldValue + ", GU_ID_Field=" + this.GU_ID_Field + ")";
	}

}