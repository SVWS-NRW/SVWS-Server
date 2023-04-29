package de.svws_nrw.db.dto.current.schild.impexp;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
@NamedQuery(name = "DTOEigeneImporteTabellen.all", query = "SELECT e FROM DTOEigeneImporteTabellen e")
@NamedQuery(name = "DTOEigeneImporteTabellen.import_id", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Import_ID = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.import_id.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Import_ID IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.tablename", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.TableName = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.tablename.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.TableName IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.dstcreateid", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstCreateID = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.dstcreateid.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstCreateID IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.dstidfieldname", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstIDFieldName = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.dstidfieldname.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstIDFieldName IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.sequence", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Sequence = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.sequence.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Sequence IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookuptable", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupTable = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookuptable.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupTable IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupfields", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFields = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupfields.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFields IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupfieldtypes", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFieldTypes = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupfieldtypes.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFieldTypes IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupfieldpos", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFieldPos = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupfieldpos.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupFieldPos IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupkeyfield", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupKeyField = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupkeyfield.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupKeyField IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupresultfield", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupResultField = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupresultfield.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupResultField IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupresultfieldtype", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupResultFieldType = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.lookupresultfieldtype.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.LookupResultFieldType IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.dstdefaultfieldname", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstDefaultFieldName = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.dstdefaultfieldname.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstDefaultFieldName IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.dstdefaultfieldvalue", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstDefaultFieldValue = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.dstdefaultfieldvalue.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.DstDefaultFieldValue IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.gu_id_field", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.GU_ID_Field = :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.gu_id_field.multiple", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.GU_ID_Field IN :value")
@NamedQuery(name = "DTOEigeneImporteTabellen.primaryKeyQuery", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Import_ID = ?1 AND e.TableName = ?2")
@NamedQuery(name = "DTOEigeneImporteTabellen.all.migration", query = "SELECT e FROM DTOEigeneImporteTabellen e WHERE e.Import_ID IS NOT NULL AND e.TableName IS NOT NULL")
@JsonPropertyOrder({"Import_ID", "TableName", "DstCreateID", "DstIDFieldName", "Sequence", "LookupTable", "LookupFields", "LookupFieldTypes", "LookupFieldPos", "LookupKeyField", "LookupResultField", "LookupResultFieldType", "DstDefaultFieldName", "DstDefaultFieldValue", "GU_ID_Field"})
public final class DTOEigeneImporteTabellen {

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
