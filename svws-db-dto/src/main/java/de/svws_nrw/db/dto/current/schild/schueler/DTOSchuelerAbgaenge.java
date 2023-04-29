package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAbgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAbgaenge")
@NamedQuery(name = "DTOSchuelerAbgaenge.all", query = "SELECT e FROM DTOSchuelerAbgaenge e")
@NamedQuery(name = "DTOSchuelerAbgaenge.id", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.id.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.schueler_id", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.bemerkungintern", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.BemerkungIntern = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.bemerkungintern.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.BemerkungIntern IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsschulform", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulform = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsschulform.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulform IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsbeschreibung", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsbeschreibung.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.organisationsformkrz", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.OrganisationsformKrz = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.organisationsformkrz.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.OrganisationsformKrz IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsschule", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchule = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsschule.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchule IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsschuleanschr", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsschuleanschr.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsschulnr", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulNr = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.abgangsschulnr.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulNr IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsjahrgang", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSJahrgang = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsjahrgang.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSJahrgang IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsentlassart", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSEntlassArt = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsentlassart.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSEntlassArt IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsschulformsim", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulformSIM = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsschulformsim.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulformSIM IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsschulentlassdatum", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulEntlassDatum = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsschulentlassdatum.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulEntlassDatum IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsversetzung", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSVersetzung = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsversetzung.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSVersetzung IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lssgl", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSGL = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lssgl.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSGL IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsfachklkennung", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSFachklKennung = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsfachklkennung.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSFachklKennung IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsfachklsim", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSFachklSIM = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsfachklsim.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSFachklSIM IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.fuersimexport", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.FuerSIMExport = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.fuersimexport.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.FuerSIMExport IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsbeginndatum", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSBeginnDatum = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsbeginndatum.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSBeginnDatum IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsbeginnjahrgang", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSBeginnJahrgang = :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.lsbeginnjahrgang.multiple", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSBeginnJahrgang IN :value")
@NamedQuery(name = "DTOSchuelerAbgaenge.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerAbgaenge.all.migration", query = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "BemerkungIntern", "AbgangsSchulform", "AbgangsBeschreibung", "OrganisationsformKrz", "AbgangsSchule", "AbgangsSchuleAnschr", "AbgangsSchulNr", "LSJahrgang", "LSEntlassArt", "LSSchulformSIM", "LSSchulEntlassDatum", "LSVersetzung", "LSSGL", "LSFachklKennung", "LSFachklSIM", "FuerSIMExport", "LSBeginnDatum", "LSBeginnJahrgang"})
public final class DTOSchuelerAbgaenge {

	/** ID der abgebenden Schule in der Liste */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** SchülerID zur abgebenden Schule */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

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
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FuerSIMExport;

	/** Aufnahmedatum zur abgebenden Schule */
	@Column(name = "LSBeginnDatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String LSBeginnDatum;

	/** Aufnahmejahrgang zur abgebenden Schule */
	@Column(name = "LSBeginnJahrgang")
	@JsonProperty
	public String LSBeginnJahrgang;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbgaenge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerAbgaenge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbgaenge ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerAbgaenge(final long ID, final long Schueler_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerAbgaenge other = (DTOSchuelerAbgaenge) obj;
		return ID == other.ID;
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
		return "DTOSchuelerAbgaenge(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", BemerkungIntern=" + this.BemerkungIntern + ", AbgangsSchulform=" + this.AbgangsSchulform + ", AbgangsBeschreibung=" + this.AbgangsBeschreibung + ", OrganisationsformKrz=" + this.OrganisationsformKrz + ", AbgangsSchule=" + this.AbgangsSchule + ", AbgangsSchuleAnschr=" + this.AbgangsSchuleAnschr + ", AbgangsSchulNr=" + this.AbgangsSchulNr + ", LSJahrgang=" + this.LSJahrgang + ", LSEntlassArt=" + this.LSEntlassArt + ", LSSchulformSIM=" + this.LSSchulformSIM + ", LSSchulEntlassDatum=" + this.LSSchulEntlassDatum + ", LSVersetzung=" + this.LSVersetzung + ", LSSGL=" + this.LSSGL + ", LSFachklKennung=" + this.LSFachklKennung + ", LSFachklSIM=" + this.LSFachklSIM + ", FuerSIMExport=" + this.FuerSIMExport + ", LSBeginnDatum=" + this.LSBeginnDatum + ", LSBeginnJahrgang=" + this.LSBeginnJahrgang + ")";
	}

}
