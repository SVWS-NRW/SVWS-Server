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
 * Diese Klasse dient als DTO für die Datenbanktabelle ImpExp_EigeneImporte_Felder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DevDTOEigeneImporteFelderPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ImpExp_EigeneImporte_Felder")
@NamedQuery(name="DevDTOEigeneImporteFelder.all", query="SELECT e FROM DevDTOEigeneImporteFelder e")
@NamedQuery(name="DevDTOEigeneImporteFelder.import_id", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.Import_ID = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.import_id.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.Import_ID IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.field_id", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.Field_ID = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.field_id.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.Field_ID IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.tabledescription", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.TableDescription = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.tabledescription.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.TableDescription IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.fielddescription", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.FieldDescription = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.fielddescription.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.FieldDescription IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.srcposition", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.SrcPosition = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.srcposition.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.SrcPosition IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dsttable", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstTable = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dsttable.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstTable IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstfieldname", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstFieldName = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstfieldname.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstFieldName IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstfieldtype", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstFieldType = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstfieldtype.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstFieldType IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstfieldisidentifier", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstFieldIsIdentifier = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstfieldisidentifier.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstFieldIsIdentifier IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstlookupdir", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstLookupDir = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstlookupdir.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstLookupDir IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstlookuptable", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstLookupTable = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstlookuptable.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstLookupTable IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstlookupfieldname", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstLookupFieldName = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstlookupfieldname.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstLookupFieldName IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstlookuptableidfieldname", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstLookupTableIDFieldName = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstlookuptableidfieldname.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstLookupTableIDFieldName IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstresultfieldname", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstResultFieldName = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstresultfieldname.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstResultFieldName IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstkeylookupinsert", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstKeyLookupInsert = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstkeylookupinsert.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstKeyLookupInsert IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstkeylookupnamecreateid", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstKeyLookupNameCreateID = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstkeylookupnamecreateid.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstKeyLookupNameCreateID IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstforcenumeric", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstForceNumeric = :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.dstforcenumeric.multiple", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.DstForceNumeric IN :value")
@NamedQuery(name="DevDTOEigeneImporteFelder.primaryKeyQuery", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.Import_ID = ?1 AND e.Field_ID = ?2")
@NamedQuery(name="DevDTOEigeneImporteFelder.all.migration", query="SELECT e FROM DevDTOEigeneImporteFelder e WHERE e.Import_ID IS NOT NULL AND e.Field_ID IS NOT NULL")
@JsonPropertyOrder({"Import_ID","Field_ID","TableDescription","FieldDescription","SrcPosition","DstTable","DstFieldName","DstFieldType","DstFieldIsIdentifier","DstLookupDir","DstLookupTable","DstLookupFieldName","DstLookupTableIDFieldName","DstResultFieldName","DstKeyLookupInsert","DstKeyLookupNameCreateID","DstForceNumeric"})
public class DevDTOEigeneImporteFelder {

	/** externen Textimport Felder */
	@Id
	@Column(name = "Import_ID")
	@JsonProperty
	public Integer Import_ID;

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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean DstKeyLookupInsert;

	/** externen Textimport Felder */
	@Column(name = "DstKeyLookupNameCreateID")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean DstKeyLookupNameCreateID;

	/** externen Textimport Felder */
	@Column(name = "DstForceNumeric")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean DstForceNumeric;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOEigeneImporteFelder ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOEigeneImporteFelder() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOEigeneImporteFelder ohne eine Initialisierung der Attribute.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 */
	public DevDTOEigeneImporteFelder(final Integer Import_ID) {
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
		DevDTOEigeneImporteFelder other = (DevDTOEigeneImporteFelder) obj;
		if (Import_ID == null) {
			if (other.Import_ID != null)
				return false;
		} else if (!Import_ID.equals(other.Import_ID))
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
		result = prime * result + ((Import_ID == null) ? 0 : Import_ID.hashCode());

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
		return "DevDTOEigeneImporteFelder(Import_ID=" + this.Import_ID + ", Field_ID=" + this.Field_ID + ", TableDescription=" + this.TableDescription + ", FieldDescription=" + this.FieldDescription + ", SrcPosition=" + this.SrcPosition + ", DstTable=" + this.DstTable + ", DstFieldName=" + this.DstFieldName + ", DstFieldType=" + this.DstFieldType + ", DstFieldIsIdentifier=" + this.DstFieldIsIdentifier + ", DstLookupDir=" + this.DstLookupDir + ", DstLookupTable=" + this.DstLookupTable + ", DstLookupFieldName=" + this.DstLookupFieldName + ", DstLookupTableIDFieldName=" + this.DstLookupTableIDFieldName + ", DstResultFieldName=" + this.DstResultFieldName + ", DstKeyLookupInsert=" + this.DstKeyLookupInsert + ", DstKeyLookupNameCreateID=" + this.DstKeyLookupNameCreateID + ", DstForceNumeric=" + this.DstForceNumeric + ")";
	}

}