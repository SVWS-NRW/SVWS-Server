package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@JsonPropertyOrder({"ID", "Schueler_ID", "BemerkungIntern", "AbgangsSchulform", "AbgangsBeschreibung", "OrganisationsformKrz", "AbgangsSchule", "AbgangsSchuleAnschr", "AbgangsSchulNr", "LSJahrgang", "LSEntlassArt", "LSSchulformSIM", "LSSchulEntlassDatum", "LSVersetzung", "LSSGL", "LSFachklKennung", "LSFachklSIM", "FuerSIMExport", "LSBeginnDatum", "LSBeginnJahrgang"})
public final class DTOSchuelerAbgaenge {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerAbgaenge e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BemerkungIntern */
	public static final String QUERY_BY_BEMERKUNGINTERN = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.BemerkungIntern = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BemerkungIntern */
	public static final String QUERY_LIST_BY_BEMERKUNGINTERN = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.BemerkungIntern IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsSchulform */
	public static final String QUERY_BY_ABGANGSSCHULFORM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulform = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsSchulform */
	public static final String QUERY_LIST_BY_ABGANGSSCHULFORM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulform IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsBeschreibung */
	public static final String QUERY_BY_ABGANGSBESCHREIBUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsBeschreibung */
	public static final String QUERY_LIST_BY_ABGANGSBESCHREIBUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes OrganisationsformKrz */
	public static final String QUERY_BY_ORGANISATIONSFORMKRZ = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.OrganisationsformKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes OrganisationsformKrz */
	public static final String QUERY_LIST_BY_ORGANISATIONSFORMKRZ = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.OrganisationsformKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsSchule */
	public static final String QUERY_BY_ABGANGSSCHULE = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchule = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsSchule */
	public static final String QUERY_LIST_BY_ABGANGSSCHULE = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchule IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsSchuleAnschr */
	public static final String QUERY_BY_ABGANGSSCHULEANSCHR = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsSchuleAnschr */
	public static final String QUERY_LIST_BY_ABGANGSSCHULEANSCHR = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsSchulNr */
	public static final String QUERY_BY_ABGANGSSCHULNR = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsSchulNr */
	public static final String QUERY_LIST_BY_ABGANGSSCHULNR = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSJahrgang */
	public static final String QUERY_BY_LSJAHRGANG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSJahrgang */
	public static final String QUERY_LIST_BY_LSJAHRGANG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSEntlassArt */
	public static final String QUERY_BY_LSENTLASSART = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSEntlassArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSEntlassArt */
	public static final String QUERY_LIST_BY_LSENTLASSART = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSEntlassArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulformSIM */
	public static final String QUERY_BY_LSSCHULFORMSIM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulformSIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulformSIM */
	public static final String QUERY_LIST_BY_LSSCHULFORMSIM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulformSIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulEntlassDatum */
	public static final String QUERY_BY_LSSCHULENTLASSDATUM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulEntlassDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulEntlassDatum */
	public static final String QUERY_LIST_BY_LSSCHULENTLASSDATUM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulEntlassDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSVersetzung */
	public static final String QUERY_BY_LSVERSETZUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSVersetzung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSVersetzung */
	public static final String QUERY_LIST_BY_LSVERSETZUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSVersetzung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSGL */
	public static final String QUERY_BY_LSSGL = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSGL = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSGL */
	public static final String QUERY_LIST_BY_LSSGL = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSGL IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSFachklKennung */
	public static final String QUERY_BY_LSFACHKLKENNUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSFachklKennung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSFachklKennung */
	public static final String QUERY_LIST_BY_LSFACHKLKENNUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSFachklKennung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSFachklSIM */
	public static final String QUERY_BY_LSFACHKLSIM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSFachklSIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSFachklSIM */
	public static final String QUERY_LIST_BY_LSFACHKLSIM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSFachklSIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FuerSIMExport */
	public static final String QUERY_BY_FUERSIMEXPORT = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.FuerSIMExport = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FuerSIMExport */
	public static final String QUERY_LIST_BY_FUERSIMEXPORT = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.FuerSIMExport IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSBeginnDatum */
	public static final String QUERY_BY_LSBEGINNDATUM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSBeginnDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSBeginnDatum */
	public static final String QUERY_LIST_BY_LSBEGINNDATUM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSBeginnDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSBeginnJahrgang */
	public static final String QUERY_BY_LSBEGINNJAHRGANG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSBeginnJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSBeginnJahrgang */
	public static final String QUERY_LIST_BY_LSBEGINNJAHRGANG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSBeginnJahrgang IN ?1";

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

	/** Statistikkürzel Schulform zur abgebenden Schule */
	@Column(name = "LSSchulformSIM")
	@JsonProperty
	public String LSSchulformSIM;

	/** Entlassdtaum zur abgebenden Schule */
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
