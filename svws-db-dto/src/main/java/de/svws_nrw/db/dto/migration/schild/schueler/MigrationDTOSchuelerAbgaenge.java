package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationDatumConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAbgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAbgaenge")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.all", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.id", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.bemerkungintern", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.BemerkungIntern = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.bemerkungintern.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.BemerkungIntern IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsschulform", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsSchulform = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsschulform.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsSchulform IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsbeschreibung", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsbeschreibung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.organisationsformkrz", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.OrganisationsformKrz = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.organisationsformkrz.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.OrganisationsformKrz IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsschule", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsSchule = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsschule.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsSchule IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsschuleanschr", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsschuleanschr.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsschulnr", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsSchulNr = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.abgangsschulnr.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.AbgangsSchulNr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsjahrgang", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSJahrgang = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsjahrgang.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSJahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsentlassart", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSEntlassArt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsentlassart.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSEntlassArt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsschulformsim", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSSchulformSIM = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsschulformsim.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSSchulformSIM IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsschulentlassdatum", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSSchulEntlassDatum = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsschulentlassdatum.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSSchulEntlassDatum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsversetzung", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSVersetzung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsversetzung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSVersetzung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lssgl", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSSGL = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lssgl.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSSGL IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsfachklkennung", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSFachklKennung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsfachklkennung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSFachklKennung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsfachklsim", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSFachklSIM = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsfachklsim.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSFachklSIM IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.fuersimexport", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.FuerSIMExport = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.fuersimexport.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.FuerSIMExport IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsbeginndatum", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSBeginnDatum = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsbeginndatum.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSBeginnDatum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsbeginnjahrgang", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSBeginnJahrgang = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.lsbeginnjahrgang.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.LSBeginnJahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOSchuelerAbgaenge.all.migration", query = "SELECT e FROM MigrationDTOSchuelerAbgaenge e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "BemerkungIntern", "AbgangsSchulform", "AbgangsBeschreibung", "OrganisationsformKrz", "AbgangsSchule", "AbgangsSchuleAnschr", "AbgangsSchulNr", "LSJahrgang", "LSEntlassArt", "LSSchulformSIM", "LSSchulEntlassDatum", "LSVersetzung", "LSSGL", "LSFachklKennung", "LSFachklSIM", "FuerSIMExport", "LSBeginnDatum", "LSBeginnJahrgang", "SchulnrEigner"})
public final class MigrationDTOSchuelerAbgaenge {

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
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FuerSIMExport;

	/** Aufnahmedatum zur abgebenden Schule */
	@Column(name = "LSBeginnDatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String LSBeginnDatum;

	/** Aufnahmejahrgang zur abgebenden Schule */
	@Column(name = "LSBeginnJahrgang")
	@JsonProperty
	public String LSBeginnJahrgang;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerAbgaenge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerAbgaenge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerAbgaenge ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerAbgaenge(final Long ID, final Long Schueler_ID) {
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerAbgaenge other = (MigrationDTOSchuelerAbgaenge) obj;
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
		return "MigrationDTOSchuelerAbgaenge(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", BemerkungIntern=" + this.BemerkungIntern + ", AbgangsSchulform=" + this.AbgangsSchulform + ", AbgangsBeschreibung=" + this.AbgangsBeschreibung + ", OrganisationsformKrz=" + this.OrganisationsformKrz + ", AbgangsSchule=" + this.AbgangsSchule + ", AbgangsSchuleAnschr=" + this.AbgangsSchuleAnschr + ", AbgangsSchulNr=" + this.AbgangsSchulNr + ", LSJahrgang=" + this.LSJahrgang + ", LSEntlassArt=" + this.LSEntlassArt + ", LSSchulformSIM=" + this.LSSchulformSIM + ", LSSchulEntlassDatum=" + this.LSSchulEntlassDatum + ", LSVersetzung=" + this.LSVersetzung + ", LSSGL=" + this.LSSGL + ", LSFachklKennung=" + this.LSFachklKennung + ", LSFachklSIM=" + this.LSFachklSIM + ", FuerSIMExport=" + this.FuerSIMExport + ", LSBeginnDatum=" + this.LSBeginnDatum + ", LSBeginnJahrgang=" + this.LSBeginnJahrgang + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
