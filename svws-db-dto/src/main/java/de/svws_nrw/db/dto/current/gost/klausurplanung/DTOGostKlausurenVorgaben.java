package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;
import de.svws_nrw.db.converter.current.gost.GOStKursartConverter;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.current.gost.GOStHalbjahrConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStHalbjahrConverterDeserializer;
import de.svws_nrw.csv.converter.current.gost.GOStKursartConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStKursartConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Vorgaben.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Vorgaben")
@NamedQuery(name = "DTOGostKlausurenVorgaben.all", query = "SELECT e FROM DTOGostKlausurenVorgaben e")
@NamedQuery(name = "DTOGostKlausurenVorgaben.id", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.id.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.abi_jahrgang", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.halbjahr", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Halbjahr = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.halbjahr.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Halbjahr IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.quartal", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Quartal = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.quartal.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Quartal IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.fach_id", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Fach_ID = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.fach_id.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.kursart", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Kursart = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.kursart.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Kursart IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.dauer", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Dauer = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.dauer.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Dauer IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.auswahlzeit", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Auswahlzeit = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.auswahlzeit.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Auswahlzeit IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.istmdlpruefung", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.IstMdlPruefung = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.istmdlpruefung.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.IstMdlPruefung IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.istaudionotwendig", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.IstAudioNotwendig = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.istaudionotwendig.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.IstAudioNotwendig IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.istvideonotwendig", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.IstVideoNotwendig = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.istvideonotwendig.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.IstVideoNotwendig IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.bemerkungen", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenVorgaben.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenVorgaben.all.migration", query = "SELECT e FROM DTOGostKlausurenVorgaben e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abi_Jahrgang", "Halbjahr", "Quartal", "Fach_ID", "Kursart", "Dauer", "Auswahlzeit", "IstMdlPruefung", "IstAudioNotwendig", "IstVideoNotwendig", "Bemerkungen"})
public final class DTOGostKlausurenVorgaben {

	/** ID der Klausurvorgaben (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Abiturjahrgang, dem die Klausurvorgabe zugeordnet ist */
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Das Halbjahr, welchem die Klausurvorgabe zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Column(name = "Halbjahr")
	@JsonProperty
	@Convert(converter = GOStHalbjahrConverter.class)
	@JsonSerialize(using = GOStHalbjahrConverterSerializer.class)
	@JsonDeserialize(using = GOStHalbjahrConverterDeserializer.class)
	public GostHalbjahr Halbjahr;

	/** Das Quartal, in dem die Klausur geschrieben wird. */
	@Column(name = "Quartal")
	@JsonProperty
	public int Quartal;

	/** Fach_ID der Klausurvorgaben */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** ID der Kursart (siehe ID des Core-Types GostKursart) */
	@Column(name = "Kursart")
	@JsonProperty
	@Convert(converter = GOStKursartConverter.class)
	@JsonSerialize(using = GOStKursartConverterSerializer.class)
	@JsonDeserialize(using = GOStKursartConverterDeserializer.class)
	public GostKursart Kursart;

	/** Das Dauer der Klausur/Prüfung in Minuten */
	@Column(name = "Dauer")
	@JsonProperty
	public int Dauer;

	/** Das Dauer der Auswahlzeit in Minuten */
	@Column(name = "Auswahlzeit")
	@JsonProperty
	public int Auswahlzeit;

	/** Gibt an, ob es sich um eine mündliche Prüfunge handelt oder nicht: 1 - true, 0 - false. */
	@Column(name = "IstMdlPruefung")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstMdlPruefung;

	/** Gibt an, ob es sich um eine Klausur mit Hörverstehen handelt oder nicht: 1 - true, 0 - false. */
	@Column(name = "IstAudioNotwendig")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstAudioNotwendig;

	/** Gibt an, ob es sich um eine Klausur handelt, in der ein Video gezeigt werden muss oder nicht: 1 - true, 0 - false. */
	@Column(name = "IstVideoNotwendig")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstVideoNotwendig;

	/** Text für Bemerkungen zur Klausurvorlage */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenVorgaben ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenVorgaben() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenVorgaben ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Halbjahr   der Wert für das Attribut Halbjahr
	 * @param Quartal   der Wert für das Attribut Quartal
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Kursart   der Wert für das Attribut Kursart
	 * @param Dauer   der Wert für das Attribut Dauer
	 * @param Auswahlzeit   der Wert für das Attribut Auswahlzeit
	 * @param IstMdlPruefung   der Wert für das Attribut IstMdlPruefung
	 * @param IstAudioNotwendig   der Wert für das Attribut IstAudioNotwendig
	 * @param IstVideoNotwendig   der Wert für das Attribut IstVideoNotwendig
	 */
	public DTOGostKlausurenVorgaben(final long ID, final int Abi_Jahrgang, final GostHalbjahr Halbjahr, final int Quartal, final long Fach_ID, final GostKursart Kursart, final int Dauer, final int Auswahlzeit, final Boolean IstMdlPruefung, final Boolean IstAudioNotwendig, final Boolean IstVideoNotwendig) {
		this.ID = ID;
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Halbjahr = Halbjahr;
		this.Quartal = Quartal;
		this.Fach_ID = Fach_ID;
		if (Kursart == null) {
			throw new NullPointerException("Kursart must not be null");
		}
		this.Kursart = Kursart;
		this.Dauer = Dauer;
		this.Auswahlzeit = Auswahlzeit;
		this.IstMdlPruefung = IstMdlPruefung;
		this.IstAudioNotwendig = IstAudioNotwendig;
		this.IstVideoNotwendig = IstVideoNotwendig;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenVorgaben other = (DTOGostKlausurenVorgaben) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostKlausurenVorgaben(ID=" + this.ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Halbjahr=" + this.Halbjahr + ", Quartal=" + this.Quartal + ", Fach_ID=" + this.Fach_ID + ", Kursart=" + this.Kursart + ", Dauer=" + this.Dauer + ", Auswahlzeit=" + this.Auswahlzeit + ", IstMdlPruefung=" + this.IstMdlPruefung + ", IstAudioNotwendig=" + this.IstAudioNotwendig + ", IstVideoNotwendig=" + this.IstVideoNotwendig + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
