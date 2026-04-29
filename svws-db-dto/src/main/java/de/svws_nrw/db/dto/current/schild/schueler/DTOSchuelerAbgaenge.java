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
@JsonPropertyOrder({"id", "idSchueler", "schulnummer", "schluesselSchulgliederung", "bezeichnungEntlassgrund", "idAbschlussart", "idOrganisationsform", "datumVon", "datumBis", "jahrgangVon", "jahrgangBis", "AbgangsSchulform", "AbgangsBeschreibung", "AbgangsSchule", "AbgangsSchuleAnschr", "LSSchulformSIM", "LSVersetzung", "LSFachklKennung", "LSFachklSIM", "FuerSIMExport"})
public final class DTOSchuelerAbgaenge {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerAbgaenge e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schulnummer */
	public static final String QUERY_BY_SCHULNUMMER = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.schulnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schulnummer */
	public static final String QUERY_LIST_BY_SCHULNUMMER = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.schulnummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schluesselSchulgliederung */
	public static final String QUERY_BY_SCHLUESSELSCHULGLIEDERUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.schluesselSchulgliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schluesselSchulgliederung */
	public static final String QUERY_LIST_BY_SCHLUESSELSCHULGLIEDERUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.schluesselSchulgliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnungEntlassgrund */
	public static final String QUERY_BY_BEZEICHNUNGENTLASSGRUND = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.bezeichnungEntlassgrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnungEntlassgrund */
	public static final String QUERY_LIST_BY_BEZEICHNUNGENTLASSGRUND = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.bezeichnungEntlassgrund IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAbschlussart */
	public static final String QUERY_BY_IDABSCHLUSSART = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.idAbschlussart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAbschlussart */
	public static final String QUERY_LIST_BY_IDABSCHLUSSART = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.idAbschlussart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idOrganisationsform */
	public static final String QUERY_BY_IDORGANISATIONSFORM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.idOrganisationsform = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idOrganisationsform */
	public static final String QUERY_LIST_BY_IDORGANISATIONSFORM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.idOrganisationsform IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes datumVon */
	public static final String QUERY_BY_DATUMVON = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.datumVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes datumVon */
	public static final String QUERY_LIST_BY_DATUMVON = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.datumVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes datumBis */
	public static final String QUERY_BY_DATUMBIS = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.datumBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes datumBis */
	public static final String QUERY_LIST_BY_DATUMBIS = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.datumBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes jahrgangVon */
	public static final String QUERY_BY_JAHRGANGVON = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.jahrgangVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes jahrgangVon */
	public static final String QUERY_LIST_BY_JAHRGANGVON = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.jahrgangVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes jahrgangBis */
	public static final String QUERY_BY_JAHRGANGBIS = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.jahrgangBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes jahrgangBis */
	public static final String QUERY_LIST_BY_JAHRGANGBIS = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.jahrgangBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsSchulform */
	public static final String QUERY_BY_ABGANGSSCHULFORM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulform = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsSchulform */
	public static final String QUERY_LIST_BY_ABGANGSSCHULFORM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchulform IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsBeschreibung */
	public static final String QUERY_BY_ABGANGSBESCHREIBUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsBeschreibung */
	public static final String QUERY_LIST_BY_ABGANGSBESCHREIBUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsBeschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsSchule */
	public static final String QUERY_BY_ABGANGSSCHULE = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchule = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsSchule */
	public static final String QUERY_LIST_BY_ABGANGSSCHULE = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchule IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgangsSchuleAnschr */
	public static final String QUERY_BY_ABGANGSSCHULEANSCHR = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgangsSchuleAnschr */
	public static final String QUERY_LIST_BY_ABGANGSSCHULEANSCHR = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.AbgangsSchuleAnschr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulformSIM */
	public static final String QUERY_BY_LSSCHULFORMSIM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulformSIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulformSIM */
	public static final String QUERY_LIST_BY_LSSCHULFORMSIM = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSSchulformSIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSVersetzung */
	public static final String QUERY_BY_LSVERSETZUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSVersetzung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSVersetzung */
	public static final String QUERY_LIST_BY_LSVERSETZUNG = "SELECT e FROM DTOSchuelerAbgaenge e WHERE e.LSVersetzung IN ?1";

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

	/** ID der abgebenden Schule in der Liste */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** SchülerID zur abgebenden Schule */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long idSchueler;

	/** Schulnummer zur abgebenden Schule */
	@Column(name = "AbgangsSchulNr")
	@JsonProperty
	public String schulnummer;

	/** SGL zur abgebenden Schule */
	@Column(name = "LSSGL")
	@JsonProperty
	public String schluesselSchulgliederung;

	/** interne Bemerkung zur abgebenden Schule */
	@Column(name = "BemerkungIntern")
	@JsonProperty
	public String bezeichnungEntlassgrund;

	/** Entlassart zur abgebenden Schule */
	@Column(name = "LSEntlassArt")
	@JsonProperty
	public String idAbschlussart;

	/** Organisationform zur abgebenden Schule */
	@Column(name = "OrganisationsformKrz")
	@JsonProperty
	public String idOrganisationsform;

	/** Aufnahmedatum zur abgebenden Schule */
	@Column(name = "LSBeginnDatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String datumVon;

	/** Entlassdtaum zur abgebenden Schule */
	@Column(name = "LSSchulEntlassDatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String datumBis;

	/** Aufnahmejahrgang zur abgebenden Schule */
	@Column(name = "LSBeginnJahrgang")
	@JsonProperty
	public String jahrgangVon;

	/** Abgangsjahrgang zur abgebenden Schule */
	@Column(name = "LSJahrgang")
	@JsonProperty
	public String jahrgangBis;

	/** FSchulform zur abgebenden Schule */
	@Column(name = "AbgangsSchulform")
	@JsonProperty
	public String AbgangsSchulform;

	/** Abgangsbeschreibung zur abgebenden Schule */
	@Column(name = "AbgangsBeschreibung")
	@JsonProperty
	public String AbgangsBeschreibung;

	/** Bezeichnung  zur abgebenden Schule */
	@Column(name = "AbgangsSchule")
	@JsonProperty
	public String AbgangsSchule;

	/** Anschrift zur abgebenden Schule */
	@Column(name = "AbgangsSchuleAnschr")
	@JsonProperty
	public String AbgangsSchuleAnschr;

	/** Statistikkürzel Schulform zur abgebenden Schule */
	@Column(name = "LSSchulformSIM")
	@JsonProperty
	public String LSSchulformSIM;

	/** Versetzungsvermerk zur abgebenden Schule */
	@Column(name = "LSVersetzung")
	@JsonProperty
	public String LSVersetzung;

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

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbgaenge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerAbgaenge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbgaenge ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idSchueler   der Wert für das Attribut idSchueler
	 */
	public DTOSchuelerAbgaenge(final long id, final long idSchueler) {
		this.id = id;
		this.idSchueler = idSchueler;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOSchuelerAbgaenge other = (DTOSchuelerAbgaenge) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerAbgaenge(id=" + this.id + ", idSchueler=" + this.idSchueler + ", schulnummer=" + this.schulnummer + ", schluesselSchulgliederung=" + this.schluesselSchulgliederung + ", bezeichnungEntlassgrund=" + this.bezeichnungEntlassgrund + ", idAbschlussart=" + this.idAbschlussart + ", idOrganisationsform=" + this.idOrganisationsform + ", datumVon=" + this.datumVon + ", datumBis=" + this.datumBis + ", jahrgangVon=" + this.jahrgangVon + ", jahrgangBis=" + this.jahrgangBis + ", AbgangsSchulform=" + this.AbgangsSchulform + ", AbgangsBeschreibung=" + this.AbgangsBeschreibung + ", AbgangsSchule=" + this.AbgangsSchule + ", AbgangsSchuleAnschr=" + this.AbgangsSchuleAnschr + ", LSSchulformSIM=" + this.LSSchulformSIM + ", LSVersetzung=" + this.LSVersetzung + ", LSFachklKennung=" + this.LSFachklKennung + ", LSFachklSIM=" + this.LSFachklSIM + ", FuerSIMExport=" + this.FuerSIMExport + ")";
	}

}
