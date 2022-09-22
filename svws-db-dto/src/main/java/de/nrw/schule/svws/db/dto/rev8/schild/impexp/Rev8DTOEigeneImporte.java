package de.nrw.schule.svws.db.dto.rev8.schild.impexp;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle ImpExp_EigeneImporte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "ImpExp_EigeneImporte")
@NamedQuery(name="Rev8DTOEigeneImporte.all", query="SELECT e FROM Rev8DTOEigeneImporte e")
@NamedQuery(name="Rev8DTOEigeneImporte.id", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.id.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.title", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.Title = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.title.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.Title IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.delimiterchar", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.DelimiterChar = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.delimiterchar.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.DelimiterChar IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.textquote", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.TextQuote = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.textquote.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.TextQuote IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.skiplines", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.SkipLines = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.skiplines.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.SkipLines IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.dateformat", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.DateFormat = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.dateformat.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.DateFormat IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.booleantrue", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.BooleanTrue = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.booleantrue.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.BooleanTrue IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.abkweiblich", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.AbkWeiblich = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.abkweiblich.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.AbkWeiblich IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.abkmaennlich", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.AbkMaennlich = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.abkmaennlich.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.AbkMaennlich IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.maintable", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.MainTable = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.maintable.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.MainTable IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.insertmode", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.InsertMode = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.insertmode.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.InsertMode IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.lookuptabledir", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.LookupTableDir = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.lookuptabledir.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.LookupTableDir IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.schueleridmode", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.SchuelerIDMode = :value")
@NamedQuery(name="Rev8DTOEigeneImporte.schueleridmode.multiple", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.SchuelerIDMode IN :value")
@NamedQuery(name="Rev8DTOEigeneImporte.primaryKeyQuery", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOEigeneImporte.all.migration", query="SELECT e FROM Rev8DTOEigeneImporte e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Title","DelimiterChar","TextQuote","SkipLines","DateFormat","BooleanTrue","AbkWeiblich","AbkMaennlich","MainTable","InsertMode","LookupTableDir","SchuelerIDMode"})
public class Rev8DTOEigeneImporte {

	/** ID des Importschemas für den externen Textimport */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Integer ID;

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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOEigeneImporte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOEigeneImporte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOEigeneImporte ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOEigeneImporte(final Integer ID) {
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
		Rev8DTOEigeneImporte other = (Rev8DTOEigeneImporte) obj;
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
		return "Rev8DTOEigeneImporte(ID=" + this.ID + ", Title=" + this.Title + ", DelimiterChar=" + this.DelimiterChar + ", TextQuote=" + this.TextQuote + ", SkipLines=" + this.SkipLines + ", DateFormat=" + this.DateFormat + ", BooleanTrue=" + this.BooleanTrue + ", AbkWeiblich=" + this.AbkWeiblich + ", AbkMaennlich=" + this.AbkMaennlich + ", MainTable=" + this.MainTable + ", InsertMode=" + this.InsertMode + ", LookupTableDir=" + this.LookupTableDir + ", SchuelerIDMode=" + this.SchuelerIDMode + ")";
	}

}