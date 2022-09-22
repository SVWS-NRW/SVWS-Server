package de.nrw.schule.svws.db.dto.rev8.schild.intern;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_VerfImportTabellen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_VerfImportTabellen")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.all", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.tablename", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.TableName = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.tablename.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.TableName IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstrequiredfields", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstRequiredFields = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstrequiredfields.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstRequiredFields IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstidfieldname", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstIDFieldName = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstidfieldname.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstIDFieldName IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.sequence", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.Sequence = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.sequence.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.Sequence IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookuptable", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupTable = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookuptable.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupTable IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupfields", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupFields = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupfields.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupFields IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupfieldtypes", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupFieldTypes = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupfieldtypes.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupFieldTypes IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupresultfield", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupResultField = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupresultfield.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupResultField IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupresultfieldtype", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupResultFieldType = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupresultfieldtype.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupResultFieldType IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupkeyfield", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupKeyField = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.lookupkeyfield.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.LookupKeyField IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstdefaultfieldname", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstDefaultFieldName = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstdefaultfieldname.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstDefaultFieldName IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstdefaultfieldvalue", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstDefaultFieldValue = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstdefaultfieldvalue.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstDefaultFieldValue IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstcreateid", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstCreateID = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.dstcreateid.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.DstCreateID IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.gu_id_field", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.GU_ID_Field = :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.gu_id_field.multiple", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.GU_ID_Field IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.primaryKeyQuery", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.TableName = ?1")
@NamedQuery(name="Rev8DTOInternVerfImportTabellen.all.migration", query="SELECT e FROM Rev8DTOInternVerfImportTabellen e WHERE e.TableName IS NOT NULL")
@JsonPropertyOrder({"TableName","DstRequiredFields","DstIDFieldName","Sequence","LookupTable","LookupFields","LookupFieldTypes","LookupResultField","LookupResultFieldType","LookupKeyField","DstDefaultFieldName","DstDefaultFieldValue","DstCreateID","GU_ID_Field"})
public class Rev8DTOInternVerfImportTabellen {

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Id
	@Column(name = "TableName")
	@JsonProperty
	public String TableName;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstRequiredFields")
	@JsonProperty
	public String DstRequiredFields;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstIDFieldName")
	@JsonProperty
	public String DstIDFieldName;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "Sequence")
	@JsonProperty
	public Integer Sequence;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "LookupTable")
	@JsonProperty
	public String LookupTable;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "LookupFields")
	@JsonProperty
	public String LookupFields;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "LookupFieldTypes")
	@JsonProperty
	public String LookupFieldTypes;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "LookupResultField")
	@JsonProperty
	public String LookupResultField;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "LookupResultFieldType")
	@JsonProperty
	public String LookupResultFieldType;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "LookupKeyField")
	@JsonProperty
	public String LookupKeyField;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstDefaultFieldName")
	@JsonProperty
	public String DstDefaultFieldName;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstDefaultFieldValue")
	@JsonProperty
	public String DstDefaultFieldValue;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstCreateID")
	@JsonProperty
	public String DstCreateID;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "GU_ID_Field")
	@JsonProperty
	public String GU_ID_Field;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternVerfImportTabellen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOInternVerfImportTabellen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternVerfImportTabellen ohne eine Initialisierung der Attribute.
	 * @param TableName   der Wert für das Attribut TableName
	 */
	public Rev8DTOInternVerfImportTabellen(final String TableName) {
		if (TableName == null) { 
			throw new NullPointerException("TableName must not be null");
		}
		this.TableName = TableName;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOInternVerfImportTabellen other = (Rev8DTOInternVerfImportTabellen) obj;
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
		return "Rev8DTOInternVerfImportTabellen(TableName=" + this.TableName + ", DstRequiredFields=" + this.DstRequiredFields + ", DstIDFieldName=" + this.DstIDFieldName + ", Sequence=" + this.Sequence + ", LookupTable=" + this.LookupTable + ", LookupFields=" + this.LookupFields + ", LookupFieldTypes=" + this.LookupFieldTypes + ", LookupResultField=" + this.LookupResultField + ", LookupResultFieldType=" + this.LookupResultFieldType + ", LookupKeyField=" + this.LookupKeyField + ", DstDefaultFieldName=" + this.DstDefaultFieldName + ", DstDefaultFieldValue=" + this.DstDefaultFieldValue + ", DstCreateID=" + this.DstCreateID + ", GU_ID_Field=" + this.GU_ID_Field + ")";
	}

}