package de.nrw.schule.svws.db.dto.dev.gost.klausurplanung;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.gost.GOStHalbjahrConverter;

import de.nrw.schule.svws.core.types.gost.GostHalbjahr;


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
import de.nrw.schule.svws.csv.converter.current.gost.GOStHalbjahrConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStHalbjahrConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Vorgaben.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Vorgaben")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.all", query="SELECT e FROM DevDTOGostKlausurenVorgaben e")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.id", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.ID = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.id.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.abi_jahrgang", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.abi_jahrgang.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.halbjahr", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Halbjahr = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.halbjahr.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Halbjahr IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.quartal", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Quartal = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.quartal.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Quartal IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.fach_id", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Fach_ID = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.fach_id.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.kursartallg", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.KursartAllg = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.kursartallg.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.KursartAllg IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.dauer", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Dauer = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.dauer.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Dauer IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.auswahlzeit", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Auswahlzeit = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.auswahlzeit.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Auswahlzeit IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.istmdlpruefung", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.IstMdlPruefung = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.istmdlpruefung.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.IstMdlPruefung IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.istaudionotwendig", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.IstAudioNotwendig = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.istaudionotwendig.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.IstAudioNotwendig IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.istvideonotwendig", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.IstVideoNotwendig = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.istvideonotwendig.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.IstVideoNotwendig IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.bemerkungen", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.bemerkungen.multiple", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.primaryKeyQuery", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOGostKlausurenVorgaben.all.migration", query="SELECT e FROM DevDTOGostKlausurenVorgaben e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abi_Jahrgang","Halbjahr","Quartal","Fach_ID","KursartAllg","Dauer","Auswahlzeit","IstMdlPruefung","IstAudioNotwendig","IstVideoNotwendig","Bemerkungen"})
public class DevDTOGostKlausurenVorgaben {

	/** ID der Klausurvorgaben (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Abiturjahrgang, dem die Klausurvorgabe zugeordnet ist */
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public Integer Abi_Jahrgang;

	/** Das Halbjahr, welchem die Klausurvorgabe zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Column(name = "Halbjahr")
	@JsonProperty
	@Convert(converter=GOStHalbjahrConverter.class)
	@JsonSerialize(using=GOStHalbjahrConverterSerializer.class)
	@JsonDeserialize(using=GOStHalbjahrConverterDeserializer.class)
	public GostHalbjahr Halbjahr;

	/** Das Quartal, in dem die Klausur geschrieben wird. */
	@Column(name = "Quartal")
	@JsonProperty
	public Integer Quartal;

	/** Fach_ID der Klausurvorgaben */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Allgemeine Kursart des Klausur-Kurses */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Das Dauer der Klausur/Prüfung in Minuten */
	@Column(name = "Dauer")
	@JsonProperty
	public Integer Dauer;

	/** Das Dauer der Auswahlzeit in Minuten */
	@Column(name = "Auswahlzeit")
	@JsonProperty
	public Integer Auswahlzeit;

	/** Gibt an, ob es sich um eine mündliche Prüfunge handelt oder nicht: 1 - true, 0 - false. */
	@Column(name = "IstMdlPruefung")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstMdlPruefung;

	/** Gibt an, ob es sich um eine Klausur mit Hörverstehen handelt oder nicht: 1 - true, 0 - false. */
	@Column(name = "IstAudioNotwendig")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstAudioNotwendig;

	/** Gibt an, ob es sich um eine Klausur handelt, in der ein Video gezeigt werden muss oder nicht: 1 - true, 0 - false. */
	@Column(name = "IstVideoNotwendig")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstVideoNotwendig;

	/** Text für Bemerkungen zur Klausurvorlage */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostKlausurenVorgaben ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostKlausurenVorgaben() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostKlausurenVorgaben ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Halbjahr   der Wert für das Attribut Halbjahr
	 * @param Quartal   der Wert für das Attribut Quartal
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param KursartAllg   der Wert für das Attribut KursartAllg
	 * @param Dauer   der Wert für das Attribut Dauer
	 * @param Auswahlzeit   der Wert für das Attribut Auswahlzeit
	 * @param IstMdlPruefung   der Wert für das Attribut IstMdlPruefung
	 * @param IstAudioNotwendig   der Wert für das Attribut IstAudioNotwendig
	 * @param IstVideoNotwendig   der Wert für das Attribut IstVideoNotwendig
	 */
	public DevDTOGostKlausurenVorgaben(final Long ID, final Integer Abi_Jahrgang, final GostHalbjahr Halbjahr, final Integer Quartal, final Long Fach_ID, final String KursartAllg, final Integer Dauer, final Integer Auswahlzeit, final Boolean IstMdlPruefung, final Boolean IstAudioNotwendig, final Boolean IstVideoNotwendig) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abi_Jahrgang == null) { 
			throw new NullPointerException("Abi_Jahrgang must not be null");
		}
		this.Abi_Jahrgang = Abi_Jahrgang;
		if (Halbjahr == null) { 
			throw new NullPointerException("Halbjahr must not be null");
		}
		this.Halbjahr = Halbjahr;
		if (Quartal == null) { 
			throw new NullPointerException("Quartal must not be null");
		}
		this.Quartal = Quartal;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
		if (KursartAllg == null) { 
			throw new NullPointerException("KursartAllg must not be null");
		}
		this.KursartAllg = KursartAllg;
		if (Dauer == null) { 
			throw new NullPointerException("Dauer must not be null");
		}
		this.Dauer = Dauer;
		if (Auswahlzeit == null) { 
			throw new NullPointerException("Auswahlzeit must not be null");
		}
		this.Auswahlzeit = Auswahlzeit;
		if (IstMdlPruefung == null) { 
			throw new NullPointerException("IstMdlPruefung must not be null");
		}
		this.IstMdlPruefung = IstMdlPruefung;
		if (IstAudioNotwendig == null) { 
			throw new NullPointerException("IstAudioNotwendig must not be null");
		}
		this.IstAudioNotwendig = IstAudioNotwendig;
		if (IstVideoNotwendig == null) { 
			throw new NullPointerException("IstVideoNotwendig must not be null");
		}
		this.IstVideoNotwendig = IstVideoNotwendig;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOGostKlausurenVorgaben other = (DevDTOGostKlausurenVorgaben) obj;
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
		return "DevDTOGostKlausurenVorgaben(ID=" + this.ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Halbjahr=" + this.Halbjahr + ", Quartal=" + this.Quartal + ", Fach_ID=" + this.Fach_ID + ", KursartAllg=" + this.KursartAllg + ", Dauer=" + this.Dauer + ", Auswahlzeit=" + this.Auswahlzeit + ", IstMdlPruefung=" + this.IstMdlPruefung + ", IstAudioNotwendig=" + this.IstAudioNotwendig + ", IstVideoNotwendig=" + this.IstVideoNotwendig + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}