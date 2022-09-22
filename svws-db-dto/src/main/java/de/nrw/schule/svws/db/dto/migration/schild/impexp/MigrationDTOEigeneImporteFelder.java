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
 * Diese Klasse dient als DTO für die Datenbanktabelle ImpExp_EigeneImporte_Felder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOEigeneImporteFelderPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ImpExp_EigeneImporte_Felder")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.all", query="SELECT e FROM MigrationDTOEigeneImporteFelder e")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.import_id", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.Import_ID = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.import_id.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.Import_ID IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.field_id", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.Field_ID = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.field_id.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.Field_ID IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.tabledescription", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.TableDescription = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.tabledescription.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.TableDescription IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.fielddescription", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.FieldDescription = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.fielddescription.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.FieldDescription IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.srcposition", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.SrcPosition = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.srcposition.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.SrcPosition IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dsttable", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstTable = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dsttable.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstTable IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstfieldname", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstFieldName = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstfieldname.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstFieldName IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstfieldtype", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstFieldType = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstfieldtype.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstFieldType IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstfieldisidentifier", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstFieldIsIdentifier = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstfieldisidentifier.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstFieldIsIdentifier IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstlookupdir", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstLookupDir = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstlookupdir.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstLookupDir IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstlookuptable", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstLookupTable = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstlookuptable.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstLookupTable IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstlookupfieldname", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstLookupFieldName = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstlookupfieldname.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstLookupFieldName IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstlookuptableidfieldname", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstLookupTableIDFieldName = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstlookuptableidfieldname.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstLookupTableIDFieldName IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstresultfieldname", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstResultFieldName = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstresultfieldname.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstResultFieldName IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstkeylookupinsert", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstKeyLookupInsert = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstkeylookupinsert.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstKeyLookupInsert IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstkeylookupnamecreateid", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstKeyLookupNameCreateID = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstkeylookupnamecreateid.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstKeyLookupNameCreateID IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstforcenumeric", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstForceNumeric = :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.dstforcenumeric.multiple", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.DstForceNumeric IN :value")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.primaryKeyQuery", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.Import_ID = ?1 AND e.Field_ID = ?2")
@NamedQuery(name="MigrationDTOEigeneImporteFelder.all.migration", query="SELECT e FROM MigrationDTOEigeneImporteFelder e WHERE e.Import_ID IS NOT NULL AND e.Field_ID IS NOT NULL")
@JsonPropertyOrder({"Import_ID","Field_ID","TableDescription","FieldDescription","SrcPosition","DstTable","DstFieldName","DstFieldType","DstFieldIsIdentifier","DstLookupDir","DstLookupTable","DstLookupFieldName","DstLookupTableIDFieldName","DstResultFieldName","DstKeyLookupInsert","DstKeyLookupNameCreateID","DstForceNumeric"})
public class MigrationDTOEigeneImporteFelder {

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
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
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
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean DstKeyLookupInsert;

	/** externen Textimport Felder */
	@Column(name = "DstKeyLookupNameCreateID")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean DstKeyLookupNameCreateID;

	/** externen Textimport Felder */
	@Column(name = "DstForceNumeric")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean DstForceNumeric;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneImporteFelder ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOEigeneImporteFelder() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneImporteFelder ohne eine Initialisierung der Attribute.
	 * @param Import_ID   der Wert für das Attribut Import_ID
	 */
	public MigrationDTOEigeneImporteFelder(final Integer Import_ID) {
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
		MigrationDTOEigeneImporteFelder other = (MigrationDTOEigeneImporteFelder) obj;
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
		return "MigrationDTOEigeneImporteFelder(Import_ID=" + this.Import_ID + ", Field_ID=" + this.Field_ID + ", TableDescription=" + this.TableDescription + ", FieldDescription=" + this.FieldDescription + ", SrcPosition=" + this.SrcPosition + ", DstTable=" + this.DstTable + ", DstFieldName=" + this.DstFieldName + ", DstFieldType=" + this.DstFieldType + ", DstFieldIsIdentifier=" + this.DstFieldIsIdentifier + ", DstLookupDir=" + this.DstLookupDir + ", DstLookupTable=" + this.DstLookupTable + ", DstLookupFieldName=" + this.DstLookupFieldName + ", DstLookupTableIDFieldName=" + this.DstLookupTableIDFieldName + ", DstResultFieldName=" + this.DstResultFieldName + ", DstKeyLookupInsert=" + this.DstKeyLookupInsert + ", DstKeyLookupNameCreateID=" + this.DstKeyLookupNameCreateID + ", DstForceNumeric=" + this.DstForceNumeric + ")";
	}

}