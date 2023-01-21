package de.nrw.schule.svws.db.dto.dev.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.DatumConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAbgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAbgaenge")
@NamedQuery(name="DevDTOSchuelerAbgaenge.all", query="SELECT e FROM DevDTOSchuelerAbgaenge e")
@NamedQuery(name="DevDTOSchuelerAbgaenge.id", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.ID = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.id.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.schueler_id", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.schueler_id.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.bemerkungintern", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.BemerkungIntern = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.bemerkungintern.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.BemerkungIntern IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsschulform", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsSchulform = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsschulform.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsSchulform IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsbeschreibung", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsbeschreibung.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.organisationsformkrz", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.OrganisationsformKrz = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.organisationsformkrz.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.OrganisationsformKrz IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsschule", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsSchule = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsschule.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsSchule IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsschuleanschr", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsschuleanschr.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsschulnr", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsSchulNr = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.abgangsschulnr.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.AbgangsSchulNr IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsjahrgang", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSJahrgang = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsjahrgang.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSJahrgang IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsentlassart", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSEntlassArt = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsentlassart.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSEntlassArt IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsschulformsim", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSSchulformSIM = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsschulformsim.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSSchulformSIM IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsschulentlassdatum", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSSchulEntlassDatum = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsschulentlassdatum.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSSchulEntlassDatum IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsversetzung", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSVersetzung = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsversetzung.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSVersetzung IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lssgl", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSSGL = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lssgl.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSSGL IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsfachklkennung", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSFachklKennung = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsfachklkennung.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSFachklKennung IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsfachklsim", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSFachklSIM = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsfachklsim.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSFachklSIM IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.fuersimexport", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.FuerSIMExport = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.fuersimexport.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.FuerSIMExport IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsbeginndatum", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSBeginnDatum = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsbeginndatum.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSBeginnDatum IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsbeginnjahrgang", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSBeginnJahrgang = :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.lsbeginnjahrgang.multiple", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.LSBeginnJahrgang IN :value")
@NamedQuery(name="DevDTOSchuelerAbgaenge.primaryKeyQuery", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOSchuelerAbgaenge.all.migration", query="SELECT e FROM DevDTOSchuelerAbgaenge e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","BemerkungIntern","AbgangsSchulform","AbgangsBeschreibung","OrganisationsformKrz","AbgangsSchule","AbgangsSchuleAnschr","AbgangsSchulNr","LSJahrgang","LSEntlassArt","LSSchulformSIM","LSSchulEntlassDatum","LSVersetzung","LSSGL","LSFachklKennung","LSFachklSIM","FuerSIMExport","LSBeginnDatum","LSBeginnJahrgang"})
public class DevDTOSchuelerAbgaenge {

	/** ID der abgebenden Schule in der Liste */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID zur abgebenden Schule */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** interne Bemerkung zur abgebenden Schule */
	@Column(name = "BemerkungIntern")
	@JsonProperty
	public String BemerkungIntern;

	/** FSchulform zur abgebenden Schule */
	@Column(name = "AbgangsSchulform")
	@JsonProperty
	public String AbgangsSchulform;

	/** Abgangsbeschreibung zur abgebenden Schule */
	@Column(name = "AbgangsBeschreibung")
	@JsonProperty
	public String AbgangsBeschreibung;

	/** Organisationform zur abgebenden Schule */
	@Column(name = "OrganisationsformKrz")
	@JsonProperty
	public String OrganisationsformKrz;

	/** Bezeichnung  zur abgebenden Schule */
	@Column(name = "AbgangsSchule")
	@JsonProperty
	public String AbgangsSchule;

	/** Anschrift zur abgebenden Schule */
	@Column(name = "AbgangsSchuleAnschr")
	@JsonProperty
	public String AbgangsSchuleAnschr;

	/** Schulnummer zur abgebenden Schule */
	@Column(name = "AbgangsSchulNr")
	@JsonProperty
	public String AbgangsSchulNr;

	/** Abgangsjahrgang zur abgebenden Schule */
	@Column(name = "LSJahrgang")
	@JsonProperty
	public String LSJahrgang;

	/** Entlassart zur abgebenden Schule */
	@Column(name = "LSEntlassArt")
	@JsonProperty
	public String LSEntlassArt;

	/** Statiatikkürzel Schulform zur abgebenden Schule */
	@Column(name = "LSSchulformSIM")
	@JsonProperty
	public String LSSchulformSIM;

	/** Entalssdtaum zur abgebenden Schule */
	@Column(name = "LSSchulEntlassDatum")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String LSSchulEntlassDatum;

	/** Versetzungsvermerk zur abgebenden Schule */
	@Column(name = "LSVersetzung")
	@JsonProperty
	public String LSVersetzung;

	/** SGL zur abgebenden Schule */
	@Column(name = "LSSGL")
	@JsonProperty
	public String LSSGL;

	/** Fachklassenkennung zur abgebenden Schule BK */
	@Column(name = "LSFachklKennung")
	@JsonProperty
	public String LSFachklKennung;

	/** Statiatikkürzel Fachklasse zur abgebenden Schule */
	@Column(name = "LSFachklSIM")
	@JsonProperty
	public String LSFachklSIM;

	/** SIM-Export zur abgebenden Schule */
	@Column(name = "FuerSIMExport")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FuerSIMExport;

	/** Aufnahmedatum zur abgebenden Schule */
	@Column(name = "LSBeginnDatum")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String LSBeginnDatum;

	/** Aufnahmejahrgang zur abgebenden Schule */
	@Column(name = "LSBeginnJahrgang")
	@JsonProperty
	public String LSBeginnJahrgang;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerAbgaenge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchuelerAbgaenge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerAbgaenge ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DevDTOSchuelerAbgaenge(final Long ID, final Long Schueler_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOSchuelerAbgaenge other = (DevDTOSchuelerAbgaenge) obj;
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
		return "DevDTOSchuelerAbgaenge(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", BemerkungIntern=" + this.BemerkungIntern + ", AbgangsSchulform=" + this.AbgangsSchulform + ", AbgangsBeschreibung=" + this.AbgangsBeschreibung + ", OrganisationsformKrz=" + this.OrganisationsformKrz + ", AbgangsSchule=" + this.AbgangsSchule + ", AbgangsSchuleAnschr=" + this.AbgangsSchuleAnschr + ", AbgangsSchulNr=" + this.AbgangsSchulNr + ", LSJahrgang=" + this.LSJahrgang + ", LSEntlassArt=" + this.LSEntlassArt + ", LSSchulformSIM=" + this.LSSchulformSIM + ", LSSchulEntlassDatum=" + this.LSSchulEntlassDatum + ", LSVersetzung=" + this.LSVersetzung + ", LSSGL=" + this.LSSGL + ", LSFachklKennung=" + this.LSFachklKennung + ", LSFachklSIM=" + this.LSFachklSIM + ", FuerSIMExport=" + this.FuerSIMExport + ", LSBeginnDatum=" + this.LSBeginnDatum + ", LSBeginnJahrgang=" + this.LSBeginnJahrgang + ")";
	}

}