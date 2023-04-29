package de.svws_nrw.db.dto.current.gost;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Jahrgang_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostJahrgangFaecherPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Jahrgang_Faecher")
@NamedQuery(name = "DTOGostJahrgangFaecher.all", query = "SELECT e FROM DTOGostJahrgangFaecher e")
@NamedQuery(name = "DTOGostJahrgangFaecher.abi_jahrgang", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.fach_id", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.Fach_ID = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.fach_id.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbaref1", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarEF1 = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbaref1.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarEF1 IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbaref2", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarEF2 = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbaref2.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarEF2 IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarq11", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarQ11 = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarq11.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarQ11 IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarq12", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarQ12 = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarq12.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarQ12 IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarq21", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarQ21 = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarq21.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarQ21 IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarq22", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarQ22 = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarq22.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarQ22 IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarabigk", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarAbiGK = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarabigk.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarAbiGK IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarabilk", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarAbiLK = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.waehlbarabilk.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WaehlbarAbiLK IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.wochenstundenqphase", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WochenstundenQPhase = :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.wochenstundenqphase.multiple", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.WochenstundenQPhase IN :value")
@NamedQuery(name = "DTOGostJahrgangFaecher.primaryKeyQuery", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.Abi_Jahrgang = ?1 AND e.Fach_ID = ?2")
@NamedQuery(name = "DTOGostJahrgangFaecher.all.migration", query = "SELECT e FROM DTOGostJahrgangFaecher e WHERE e.Abi_Jahrgang IS NOT NULL AND e.Fach_ID IS NOT NULL")
@JsonPropertyOrder({"Abi_Jahrgang", "Fach_ID", "WaehlbarEF1", "WaehlbarEF2", "WaehlbarQ11", "WaehlbarQ12", "WaehlbarQ21", "WaehlbarQ22", "WaehlbarAbiGK", "WaehlbarAbiLK", "WochenstundenQPhase"})
public final class DTOGostJahrgangFaecher {

	/** Schuljahr, in welchem der Jahrgang das Abitur macht */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** ID des Faches in der Fächertabelle */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Belegung des Faches in der EF.1 möglich: 1 - true, 0 - false  */
	@Column(name = "WaehlbarEF1")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean WaehlbarEF1;

	/** Belegung des Faches in der EF.2 möglich: 1 - true, 0 - false */
	@Column(name = "WaehlbarEF2")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean WaehlbarEF2;

	/** Belegung des Faches in der Q1.1 möglich: 1 - true, 0 - false */
	@Column(name = "WaehlbarQ11")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean WaehlbarQ11;

	/** Belegung des Faches in der Q1.2 möglich: 1 - true, 0 - false */
	@Column(name = "WaehlbarQ12")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean WaehlbarQ12;

	/** Belegung des Faches in der Q2.1 möglich: 1 - true, 0 - false */
	@Column(name = "WaehlbarQ21")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean WaehlbarQ21;

	/** Belegung des Faches in der Q2.2 möglich: 1 - true, 0 - false */
	@Column(name = "WaehlbarQ22")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean WaehlbarQ22;

	/** Eintrag, ob das Fach als 3. oder 4. Abiturfach gewählt werden kann: 1 - true, 0 - false */
	@Column(name = "WaehlbarAbiGK")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean WaehlbarAbiGK;

	/** Eintrag, ob das Fach als 1. oder 2. Abiturfach (LK) gewählt werden kann: 1 - true, 0 - false */
	@Column(name = "WaehlbarAbiLK")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean WaehlbarAbiLK;

	/** Anzahl der Unterrichtsstunden in 45-minuten-Einheiten des Faches in der Qualifikationsphase */
	@Column(name = "WochenstundenQPhase")
	@JsonProperty
	public Integer WochenstundenQPhase;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFaecher ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangFaecher() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFaecher ohne eine Initialisierung der Attribute.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param WaehlbarEF1   der Wert für das Attribut WaehlbarEF1
	 * @param WaehlbarEF2   der Wert für das Attribut WaehlbarEF2
	 * @param WaehlbarQ11   der Wert für das Attribut WaehlbarQ11
	 * @param WaehlbarQ12   der Wert für das Attribut WaehlbarQ12
	 * @param WaehlbarQ21   der Wert für das Attribut WaehlbarQ21
	 * @param WaehlbarQ22   der Wert für das Attribut WaehlbarQ22
	 * @param WaehlbarAbiGK   der Wert für das Attribut WaehlbarAbiGK
	 * @param WaehlbarAbiLK   der Wert für das Attribut WaehlbarAbiLK
	 */
	public DTOGostJahrgangFaecher(final int Abi_Jahrgang, final long Fach_ID, final Boolean WaehlbarEF1, final Boolean WaehlbarEF2, final Boolean WaehlbarQ11, final Boolean WaehlbarQ12, final Boolean WaehlbarQ21, final Boolean WaehlbarQ22, final Boolean WaehlbarAbiGK, final Boolean WaehlbarAbiLK) {
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Fach_ID = Fach_ID;
		this.WaehlbarEF1 = WaehlbarEF1;
		this.WaehlbarEF2 = WaehlbarEF2;
		this.WaehlbarQ11 = WaehlbarQ11;
		this.WaehlbarQ12 = WaehlbarQ12;
		this.WaehlbarQ21 = WaehlbarQ21;
		this.WaehlbarQ22 = WaehlbarQ22;
		this.WaehlbarAbiGK = WaehlbarAbiGK;
		this.WaehlbarAbiLK = WaehlbarAbiLK;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangFaecher other = (DTOGostJahrgangFaecher) obj;
		if (Abi_Jahrgang != other.Abi_Jahrgang)
			return false;
		return Fach_ID == other.Fach_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Abi_Jahrgang);

		result = prime * result + Long.hashCode(Fach_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostJahrgangFaecher(Abi_Jahrgang=" + this.Abi_Jahrgang + ", Fach_ID=" + this.Fach_ID + ", WaehlbarEF1=" + this.WaehlbarEF1 + ", WaehlbarEF2=" + this.WaehlbarEF2 + ", WaehlbarQ11=" + this.WaehlbarQ11 + ", WaehlbarQ12=" + this.WaehlbarQ12 + ", WaehlbarQ21=" + this.WaehlbarQ21 + ", WaehlbarQ22=" + this.WaehlbarQ22 + ", WaehlbarAbiGK=" + this.WaehlbarAbiGK + ", WaehlbarAbiLK=" + this.WaehlbarAbiLK + ", WochenstundenQPhase=" + this.WochenstundenQPhase + ")";
	}

}
