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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_VerfImportFelder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_VerfImportFelder")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.all", query="SELECT e FROM Rev8DTOInternVerfImportFelder e")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.id", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.id.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.tabledescription", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.TableDescription = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.tabledescription.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.TableDescription IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.fielddescription", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.FieldDescription = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.fielddescription.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.FieldDescription IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dsttable", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstTable = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dsttable.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstTable IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstfieldname", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstFieldName = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstfieldname.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstFieldName IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstfieldtype", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstFieldType = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstfieldtype.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstFieldType IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstrequiredstate", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstRequiredState = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstrequiredstate.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstRequiredState IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstlookuptable", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstLookupTable = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstlookuptable.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstLookupTable IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstlookuptableidfieldname", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstLookupTableIDFieldName = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstlookuptableidfieldname.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstLookupTableIDFieldName IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstlookupfieldname", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstLookupFieldName = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstlookupfieldname.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstLookupFieldName IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstresultfieldname", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstResultFieldName = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstresultfieldname.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstResultFieldName IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstkeylookupinsert", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstKeyLookupInsert = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstkeylookupinsert.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstKeyLookupInsert IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstkeylookupnamecreateid", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstKeyLookupNameCreateID = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstkeylookupnamecreateid.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstKeyLookupNameCreateID IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstforcenumeric", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstForceNumeric = :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.dstforcenumeric.multiple", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.DstForceNumeric IN :value")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.primaryKeyQuery", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOInternVerfImportFelder.all.migration", query="SELECT e FROM Rev8DTOInternVerfImportFelder e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","TableDescription","FieldDescription","DstTable","DstFieldName","DstFieldType","DstRequiredState","DstLookupTable","DstLookupTableIDFieldName","DstLookupFieldName","DstResultFieldName","DstKeyLookupInsert","DstKeyLookupNameCreateID","DstForceNumeric"})
public class Rev8DTOInternVerfImportFelder {

	/** Schildintern Tabelle: Notwendiges Tabelle für den WinSchildImport ??? DEPRECATED */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "TableDescription")
	@JsonProperty
	public String TableDescription;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "FieldDescription")
	@JsonProperty
	public String FieldDescription;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstTable")
	@JsonProperty
	public String DstTable;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstFieldName")
	@JsonProperty
	public String DstFieldName;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstFieldType")
	@JsonProperty
	public String DstFieldType;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstRequiredState")
	@JsonProperty
	public String DstRequiredState;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstLookupTable")
	@JsonProperty
	public String DstLookupTable;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstLookupTableIDFieldName")
	@JsonProperty
	public String DstLookupTableIDFieldName;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstLookupFieldName")
	@JsonProperty
	public String DstLookupFieldName;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstResultFieldName")
	@JsonProperty
	public String DstResultFieldName;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstKeyLookupInsert")
	@JsonProperty
	public String DstKeyLookupInsert;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstKeyLookupNameCreateID")
	@JsonProperty
	public String DstKeyLookupNameCreateID;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	@Column(name = "DstForceNumeric")
	@JsonProperty
	public String DstForceNumeric;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternVerfImportFelder ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOInternVerfImportFelder() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternVerfImportFelder ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOInternVerfImportFelder(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOInternVerfImportFelder other = (Rev8DTOInternVerfImportFelder) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOInternVerfImportFelder(ID=" + this.ID + ", TableDescription=" + this.TableDescription + ", FieldDescription=" + this.FieldDescription + ", DstTable=" + this.DstTable + ", DstFieldName=" + this.DstFieldName + ", DstFieldType=" + this.DstFieldType + ", DstRequiredState=" + this.DstRequiredState + ", DstLookupTable=" + this.DstLookupTable + ", DstLookupTableIDFieldName=" + this.DstLookupTableIDFieldName + ", DstLookupFieldName=" + this.DstLookupFieldName + ", DstResultFieldName=" + this.DstResultFieldName + ", DstKeyLookupInsert=" + this.DstKeyLookupInsert + ", DstKeyLookupNameCreateID=" + this.DstKeyLookupNameCreateID + ", DstForceNumeric=" + this.DstForceNumeric + ")";
	}

}