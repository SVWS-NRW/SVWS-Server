package de.svws_nrw.db.dto.migration.schild.faecher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Faecher")
@JsonPropertyOrder({"ID", "Kuerzel", "Bezeichnung", "BezeichnungZeugnis", "BezeichnungUeberweisungsZeugnis", "Zeugnisdatenquelle_ID", "Fachgruppe_ID", "StatistikKuerzel", "IstOberstufenFach", "IstFremdsprache", "SortierungAllg", "SortierungSekII", "IstNachpruefungErlaubt", "Sichtbar", "Aenderbar", "Gewichtung", "Unterrichtssprache", "IstSchriftlichZK", "IstSchriftlichBA", "AufZeugnis", "Lernfelder", "SchulnrEigner", "IstMoeglichAbiLK", "IstMoeglichAbiGK", "IstMoeglichEF1", "IstMoeglichEF2", "IstMoeglichQ11", "IstMoeglichQ12", "IstMoeglichQ21", "IstMoeglichQ22", "IstMoeglichAlsNeueFremdspracheInSekII", "ProjektKursLeitfach1_ID", "ProjektKursLeitfach2_ID", "WochenstundenEF1", "WochenstundenEF2", "WochenstundenQualifikationsphase", "MussSchriftlichEF1", "MussSchriftlichEF2", "MussMuendlich", "Aufgabenfeld", "AbgeschlFaecherHolen", "GewichtungFHR", "MaxBemZeichen"})
public final class MigrationDTOFach {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOFach e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOFach e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM MigrationDTOFach e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM MigrationDTOFach e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOFach e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOFach e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BezeichnungZeugnis */
	public static final String QUERY_BY_BEZEICHNUNGZEUGNIS = "SELECT e FROM MigrationDTOFach e WHERE e.BezeichnungZeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BezeichnungZeugnis */
	public static final String QUERY_LIST_BY_BEZEICHNUNGZEUGNIS = "SELECT e FROM MigrationDTOFach e WHERE e.BezeichnungZeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BezeichnungUeberweisungsZeugnis */
	public static final String QUERY_BY_BEZEICHNUNGUEBERWEISUNGSZEUGNIS = "SELECT e FROM MigrationDTOFach e WHERE e.BezeichnungUeberweisungsZeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BezeichnungUeberweisungsZeugnis */
	public static final String QUERY_LIST_BY_BEZEICHNUNGUEBERWEISUNGSZEUGNIS = "SELECT e FROM MigrationDTOFach e WHERE e.BezeichnungUeberweisungsZeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zeugnisdatenquelle_ID */
	public static final String QUERY_BY_ZEUGNISDATENQUELLE_ID = "SELECT e FROM MigrationDTOFach e WHERE e.Zeugnisdatenquelle_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zeugnisdatenquelle_ID */
	public static final String QUERY_LIST_BY_ZEUGNISDATENQUELLE_ID = "SELECT e FROM MigrationDTOFach e WHERE e.Zeugnisdatenquelle_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachgruppe_ID */
	public static final String QUERY_BY_FACHGRUPPE_ID = "SELECT e FROM MigrationDTOFach e WHERE e.Fachgruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachgruppe_ID */
	public static final String QUERY_LIST_BY_FACHGRUPPE_ID = "SELECT e FROM MigrationDTOFach e WHERE e.Fachgruppe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StatistikKuerzel */
	public static final String QUERY_BY_STATISTIKKUERZEL = "SELECT e FROM MigrationDTOFach e WHERE e.StatistikKuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StatistikKuerzel */
	public static final String QUERY_LIST_BY_STATISTIKKUERZEL = "SELECT e FROM MigrationDTOFach e WHERE e.StatistikKuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstOberstufenFach */
	public static final String QUERY_BY_ISTOBERSTUFENFACH = "SELECT e FROM MigrationDTOFach e WHERE e.IstOberstufenFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstOberstufenFach */
	public static final String QUERY_LIST_BY_ISTOBERSTUFENFACH = "SELECT e FROM MigrationDTOFach e WHERE e.IstOberstufenFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstFremdsprache */
	public static final String QUERY_BY_ISTFREMDSPRACHE = "SELECT e FROM MigrationDTOFach e WHERE e.IstFremdsprache = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstFremdsprache */
	public static final String QUERY_LIST_BY_ISTFREMDSPRACHE = "SELECT e FROM MigrationDTOFach e WHERE e.IstFremdsprache IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SortierungAllg */
	public static final String QUERY_BY_SORTIERUNGALLG = "SELECT e FROM MigrationDTOFach e WHERE e.SortierungAllg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SortierungAllg */
	public static final String QUERY_LIST_BY_SORTIERUNGALLG = "SELECT e FROM MigrationDTOFach e WHERE e.SortierungAllg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SortierungSekII */
	public static final String QUERY_BY_SORTIERUNGSEKII = "SELECT e FROM MigrationDTOFach e WHERE e.SortierungSekII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SortierungSekII */
	public static final String QUERY_LIST_BY_SORTIERUNGSEKII = "SELECT e FROM MigrationDTOFach e WHERE e.SortierungSekII IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstNachpruefungErlaubt */
	public static final String QUERY_BY_ISTNACHPRUEFUNGERLAUBT = "SELECT e FROM MigrationDTOFach e WHERE e.IstNachpruefungErlaubt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstNachpruefungErlaubt */
	public static final String QUERY_LIST_BY_ISTNACHPRUEFUNGERLAUBT = "SELECT e FROM MigrationDTOFach e WHERE e.IstNachpruefungErlaubt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM MigrationDTOFach e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM MigrationDTOFach e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM MigrationDTOFach e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM MigrationDTOFach e WHERE e.Aenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gewichtung */
	public static final String QUERY_BY_GEWICHTUNG = "SELECT e FROM MigrationDTOFach e WHERE e.Gewichtung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gewichtung */
	public static final String QUERY_LIST_BY_GEWICHTUNG = "SELECT e FROM MigrationDTOFach e WHERE e.Gewichtung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Unterrichtssprache */
	public static final String QUERY_BY_UNTERRICHTSSPRACHE = "SELECT e FROM MigrationDTOFach e WHERE e.Unterrichtssprache = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Unterrichtssprache */
	public static final String QUERY_LIST_BY_UNTERRICHTSSPRACHE = "SELECT e FROM MigrationDTOFach e WHERE e.Unterrichtssprache IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstSchriftlichZK */
	public static final String QUERY_BY_ISTSCHRIFTLICHZK = "SELECT e FROM MigrationDTOFach e WHERE e.IstSchriftlichZK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstSchriftlichZK */
	public static final String QUERY_LIST_BY_ISTSCHRIFTLICHZK = "SELECT e FROM MigrationDTOFach e WHERE e.IstSchriftlichZK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstSchriftlichBA */
	public static final String QUERY_BY_ISTSCHRIFTLICHBA = "SELECT e FROM MigrationDTOFach e WHERE e.IstSchriftlichBA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstSchriftlichBA */
	public static final String QUERY_LIST_BY_ISTSCHRIFTLICHBA = "SELECT e FROM MigrationDTOFach e WHERE e.IstSchriftlichBA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AufZeugnis */
	public static final String QUERY_BY_AUFZEUGNIS = "SELECT e FROM MigrationDTOFach e WHERE e.AufZeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AufZeugnis */
	public static final String QUERY_LIST_BY_AUFZEUGNIS = "SELECT e FROM MigrationDTOFach e WHERE e.AufZeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lernfelder */
	public static final String QUERY_BY_LERNFELDER = "SELECT e FROM MigrationDTOFach e WHERE e.Lernfelder = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lernfelder */
	public static final String QUERY_LIST_BY_LERNFELDER = "SELECT e FROM MigrationDTOFach e WHERE e.Lernfelder IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOFach e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOFach e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichAbiLK */
	public static final String QUERY_BY_ISTMOEGLICHABILK = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAbiLK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichAbiLK */
	public static final String QUERY_LIST_BY_ISTMOEGLICHABILK = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAbiLK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichAbiGK */
	public static final String QUERY_BY_ISTMOEGLICHABIGK = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAbiGK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichAbiGK */
	public static final String QUERY_LIST_BY_ISTMOEGLICHABIGK = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAbiGK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichEF1 */
	public static final String QUERY_BY_ISTMOEGLICHEF1 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichEF1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichEF1 */
	public static final String QUERY_LIST_BY_ISTMOEGLICHEF1 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichEF1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichEF2 */
	public static final String QUERY_BY_ISTMOEGLICHEF2 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichEF2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichEF2 */
	public static final String QUERY_LIST_BY_ISTMOEGLICHEF2 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichEF2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichQ11 */
	public static final String QUERY_BY_ISTMOEGLICHQ11 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ11 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichQ11 */
	public static final String QUERY_LIST_BY_ISTMOEGLICHQ11 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ11 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichQ12 */
	public static final String QUERY_BY_ISTMOEGLICHQ12 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ12 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichQ12 */
	public static final String QUERY_LIST_BY_ISTMOEGLICHQ12 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ12 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichQ21 */
	public static final String QUERY_BY_ISTMOEGLICHQ21 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ21 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichQ21 */
	public static final String QUERY_LIST_BY_ISTMOEGLICHQ21 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ21 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichQ22 */
	public static final String QUERY_BY_ISTMOEGLICHQ22 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ22 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichQ22 */
	public static final String QUERY_LIST_BY_ISTMOEGLICHQ22 = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ22 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstMoeglichAlsNeueFremdspracheInSekII */
	public static final String QUERY_BY_ISTMOEGLICHALSNEUEFREMDSPRACHEINSEKII = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAlsNeueFremdspracheInSekII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstMoeglichAlsNeueFremdspracheInSekII */
	public static final String QUERY_LIST_BY_ISTMOEGLICHALSNEUEFREMDSPRACHEINSEKII = "SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAlsNeueFremdspracheInSekII IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ProjektKursLeitfach1_ID */
	public static final String QUERY_BY_PROJEKTKURSLEITFACH1_ID = "SELECT e FROM MigrationDTOFach e WHERE e.ProjektKursLeitfach1_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ProjektKursLeitfach1_ID */
	public static final String QUERY_LIST_BY_PROJEKTKURSLEITFACH1_ID = "SELECT e FROM MigrationDTOFach e WHERE e.ProjektKursLeitfach1_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ProjektKursLeitfach2_ID */
	public static final String QUERY_BY_PROJEKTKURSLEITFACH2_ID = "SELECT e FROM MigrationDTOFach e WHERE e.ProjektKursLeitfach2_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ProjektKursLeitfach2_ID */
	public static final String QUERY_LIST_BY_PROJEKTKURSLEITFACH2_ID = "SELECT e FROM MigrationDTOFach e WHERE e.ProjektKursLeitfach2_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenstundenEF1 */
	public static final String QUERY_BY_WOCHENSTUNDENEF1 = "SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenEF1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenstundenEF1 */
	public static final String QUERY_LIST_BY_WOCHENSTUNDENEF1 = "SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenEF1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenstundenEF2 */
	public static final String QUERY_BY_WOCHENSTUNDENEF2 = "SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenEF2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenstundenEF2 */
	public static final String QUERY_LIST_BY_WOCHENSTUNDENEF2 = "SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenEF2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenstundenQualifikationsphase */
	public static final String QUERY_BY_WOCHENSTUNDENQUALIFIKATIONSPHASE = "SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenQualifikationsphase = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenstundenQualifikationsphase */
	public static final String QUERY_LIST_BY_WOCHENSTUNDENQUALIFIKATIONSPHASE = "SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenQualifikationsphase IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MussSchriftlichEF1 */
	public static final String QUERY_BY_MUSSSCHRIFTLICHEF1 = "SELECT e FROM MigrationDTOFach e WHERE e.MussSchriftlichEF1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MussSchriftlichEF1 */
	public static final String QUERY_LIST_BY_MUSSSCHRIFTLICHEF1 = "SELECT e FROM MigrationDTOFach e WHERE e.MussSchriftlichEF1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MussSchriftlichEF2 */
	public static final String QUERY_BY_MUSSSCHRIFTLICHEF2 = "SELECT e FROM MigrationDTOFach e WHERE e.MussSchriftlichEF2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MussSchriftlichEF2 */
	public static final String QUERY_LIST_BY_MUSSSCHRIFTLICHEF2 = "SELECT e FROM MigrationDTOFach e WHERE e.MussSchriftlichEF2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MussMuendlich */
	public static final String QUERY_BY_MUSSMUENDLICH = "SELECT e FROM MigrationDTOFach e WHERE e.MussMuendlich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MussMuendlich */
	public static final String QUERY_LIST_BY_MUSSMUENDLICH = "SELECT e FROM MigrationDTOFach e WHERE e.MussMuendlich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aufgabenfeld */
	public static final String QUERY_BY_AUFGABENFELD = "SELECT e FROM MigrationDTOFach e WHERE e.Aufgabenfeld = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aufgabenfeld */
	public static final String QUERY_LIST_BY_AUFGABENFELD = "SELECT e FROM MigrationDTOFach e WHERE e.Aufgabenfeld IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbgeschlFaecherHolen */
	public static final String QUERY_BY_ABGESCHLFAECHERHOLEN = "SELECT e FROM MigrationDTOFach e WHERE e.AbgeschlFaecherHolen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbgeschlFaecherHolen */
	public static final String QUERY_LIST_BY_ABGESCHLFAECHERHOLEN = "SELECT e FROM MigrationDTOFach e WHERE e.AbgeschlFaecherHolen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GewichtungFHR */
	public static final String QUERY_BY_GEWICHTUNGFHR = "SELECT e FROM MigrationDTOFach e WHERE e.GewichtungFHR = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GewichtungFHR */
	public static final String QUERY_LIST_BY_GEWICHTUNGFHR = "SELECT e FROM MigrationDTOFach e WHERE e.GewichtungFHR IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MaxBemZeichen */
	public static final String QUERY_BY_MAXBEMZEICHEN = "SELECT e FROM MigrationDTOFach e WHERE e.MaxBemZeichen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MaxBemZeichen */
	public static final String QUERY_LIST_BY_MAXBEMZEICHEN = "SELECT e FROM MigrationDTOFach e WHERE e.MaxBemZeichen IN ?1";

	/** Eindeutige ID zur Kennzeichnung des Fächer-Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Ein eindeutiges Kürzel zur Identifikation des Fach */
	@Column(name = "FachKrz")
	@JsonProperty
	public String Kuerzel;

	/** Die Bezeichnung des Faches */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Bezeichnung des Faches auf einem Zeugnis */
	@Column(name = "ZeugnisBez")
	@JsonProperty
	public String BezeichnungZeugnis;

	/** Die Bezeichnung des Faches auf einem Überweisungszeugnis */
	@Column(name = "UeZeugnisBez")
	@JsonProperty
	public String BezeichnungUeberweisungsZeugnis;

	/** Feld zum Festlegen der ID für die Zeugnisdatenquelle in Schild */
	@Column(name = "Zeugnisdatenquelle_ID")
	@JsonProperty
	public Long Zeugnisdatenquelle_ID;

	/** DEPRECATED: Feld zum Festlegen der ID für die Zeugnisdatenquelle in Schild, wird während der Migration umbenannt. */
	@Column(name = "Fachgruppe_ID")
	@JsonProperty
	public Long Fachgruppe_ID;

	/** Das Statistik-Kürzel des Faches */
	@Column(name = "StatistikKrz")
	@JsonProperty
	public String StatistikKuerzel;

	/** Gibt an, ob das Fach in der Oberstufe unterrichtet wird */
	@Column(name = "BasisFach")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstOberstufenFach;

	/** Gibt an, ob das Fach eine Fremdsprache ist */
	@Column(name = "IstSprache")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstFremdsprache;

	/** Gibt eine Sortierung für die Fächer vor */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer SortierungAllg;

	/** Gibt die spezielle Sortierung der Fächer für die Sekundarstufe II vor */
	@Column(name = "SortierungS2")
	@JsonProperty
	public Integer SortierungSekII;

	/** Gibt an, ob in dem Fach eine Nachprüfung erlaubt ist. */
	@Column(name = "NachprErlaubt")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstNachpruefungErlaubt;

	/** Gibt an, ob der Datensatz in der Oberfläche sichtbar sein soll und bei einer Auswahl zur Verfügung steht.  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an, ob Änderungen am Datensatz erlaubt sind. */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Gibt die Gewichtung des Faches im allgemeinbildenden Bereich am BK an, wenn keine gliederungsbezogenen Gewichtungen vergeben sind */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/** Einstelliges Kürzel der Unterrichtssprache (E, F, …) */
	@Column(name = "Unterichtssprache")
	@JsonProperty
	public String Unterrichtssprache;

	/** gibt an, ob das Fach ein schritliches Fach für ZK ist oder nicht */
	@Column(name = "IstSchriftlich")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstSchriftlichZK;

	/** gibt an, ob ein Fach am BK für den beruflichen Abschluss schriftlich gewertet wird */
	@Column(name = "IstSchriftlichBA")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstSchriftlichBA;

	/** Gibt an, ob das Fach auf dem Zeugnis erscheinen soll */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean AufZeugnis;

	/** speichert die Lernfeld-Texte zu den Fachklassen beim Unterrichtsfach am BK, ist von den Textfelder im Menü Fachklassen abgelöst worden, sollte nicht mehr genutzt werden */
	@Column(name = "Lernfelder")
	@JsonProperty
	public String Lernfelder;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** gibt an, ob das Fach als Leistungskurs belegt werden kann. */
	@Column(name = "LK_Moegl")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichAbiLK;

	/** gibt an, ob das Fach als drittes oder viertes Abiturfach belegt werden kann. */
	@Column(name = "Abi_Moegl")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichAbiGK;

	/** gibt an, ob das Fach im 1. Halbjahr der Einführungsphase belegt werden kann, */
	@Column(name = "E1")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichEF1;

	/** gibt an, ob das Fach im 2. Halbjahr der Einführungsphase belegt werden kann, */
	@Column(name = "E2")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichEF2;

	/** gibt an, ob das Fach im 1. Halbjahr des 1. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q1")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ11;

	/** gibt an, ob das Fach im 2. Halbjahr des 1. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q2")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ12;

	/** gibt an, ob das Fach im 1. Halbjahr des 2. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q3")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ21;

	/** gibt an, ob das Fach im 2. Halbjahr des 2. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q4")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ22;

	/** gibt an, ob das Fach als neue Fremdsprache in der Sekundarstufe zählt. */
	@Column(name = "AlsNeueFSInSII")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstMoeglichAlsNeueFremdspracheInSekII;

	/** bei Projektkursfächern: Die ID des Leitfaches */
	@Column(name = "Leitfach_ID")
	@JsonProperty
	public Long ProjektKursLeitfach1_ID;

	/** bei Projektkursfächern: Ggf. die ID des zweiten Leitfaches */
	@Column(name = "Leitfach2_ID")
	@JsonProperty
	public Long ProjektKursLeitfach2_ID;

	/** Beim WBK: Gibt die Anzahl der Wochenstunden für das 1. Halbjahr in der Einführungsphase an. */
	@Column(name = "E1_WZE")
	@JsonProperty
	public Integer WochenstundenEF1;

	/** Beim WBK: Gibt die Anzahl der Wochenstunden für das 2. Halbjahr in der Einführungsphase an. */
	@Column(name = "E2_WZE")
	@JsonProperty
	public Integer WochenstundenEF2;

	/** Beim WBK: Gibt die Anzahl der Wochenstunden für die Qualifikationsphase an. */
	@Column(name = "Q_WZE")
	@JsonProperty
	public Integer WochenstundenQualifikationsphase;

	/** Beim WBK: Gibt an, ob das Fach in dem 1. Halbjahr der Einführungsphase schriftlich belegt werden muss */
	@Column(name = "E1_S")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussSchriftlichEF1;

	/** Beim WBK: Gibt an, ob das Fach in dem 2. Halbjahr der Einführungsphase schriftlich belegt werden muss */
	@Column(name = "E2_S")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussSchriftlichEF2;

	/** Beim WBK: Gibt an, ob das Fach nur muendlich belegt werden darf */
	@Column(name = "NurMuendlich")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussMuendlich;

	/** Das Aufgabenfeld, zu welchem das Fach gehört */
	@Column(name = "Aufgabenfeld")
	@JsonProperty
	public String Aufgabenfeld;

	/** Gibt an, ob das Fach bei der Operation „Abgeschlossene Fächer holen“ berücksichtigt werden soll. */
	@Column(name = "AbgeschlFaecherHolen")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean AbgeschlFaecherHolen;

	/** Gibt eine Gewichtung des Faches bei der Berechnung der FHR an. */
	@Column(name = "GewichtungFHR")
	@JsonProperty
	public Integer GewichtungFHR;

	/** steuert die max. Anzahl von Zeichen in der Memo-Feldern der Fachbezogenen Bemerkungen (insbes. Grundschulzeugnisse) */
	@Column(name = "MaxBemZeichen")
	@JsonProperty
	public Integer MaxBemZeichen;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOFach(final Long ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFach other = (MigrationDTOFach) obj;
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
		return "MigrationDTOFach(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", BezeichnungZeugnis=" + this.BezeichnungZeugnis + ", BezeichnungUeberweisungsZeugnis=" + this.BezeichnungUeberweisungsZeugnis + ", Zeugnisdatenquelle_ID=" + this.Zeugnisdatenquelle_ID + ", Fachgruppe_ID=" + this.Fachgruppe_ID + ", StatistikKuerzel=" + this.StatistikKuerzel + ", IstOberstufenFach=" + this.IstOberstufenFach + ", IstFremdsprache=" + this.IstFremdsprache + ", SortierungAllg=" + this.SortierungAllg + ", SortierungSekII=" + this.SortierungSekII + ", IstNachpruefungErlaubt=" + this.IstNachpruefungErlaubt + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", Gewichtung=" + this.Gewichtung + ", Unterrichtssprache=" + this.Unterrichtssprache + ", IstSchriftlichZK=" + this.IstSchriftlichZK + ", IstSchriftlichBA=" + this.IstSchriftlichBA + ", AufZeugnis=" + this.AufZeugnis + ", Lernfelder=" + this.Lernfelder + ", SchulnrEigner=" + this.SchulnrEigner + ", IstMoeglichAbiLK=" + this.IstMoeglichAbiLK + ", IstMoeglichAbiGK=" + this.IstMoeglichAbiGK + ", IstMoeglichEF1=" + this.IstMoeglichEF1 + ", IstMoeglichEF2=" + this.IstMoeglichEF2 + ", IstMoeglichQ11=" + this.IstMoeglichQ11 + ", IstMoeglichQ12=" + this.IstMoeglichQ12 + ", IstMoeglichQ21=" + this.IstMoeglichQ21 + ", IstMoeglichQ22=" + this.IstMoeglichQ22 + ", IstMoeglichAlsNeueFremdspracheInSekII=" + this.IstMoeglichAlsNeueFremdspracheInSekII + ", ProjektKursLeitfach1_ID=" + this.ProjektKursLeitfach1_ID + ", ProjektKursLeitfach2_ID=" + this.ProjektKursLeitfach2_ID + ", WochenstundenEF1=" + this.WochenstundenEF1 + ", WochenstundenEF2=" + this.WochenstundenEF2 + ", WochenstundenQualifikationsphase=" + this.WochenstundenQualifikationsphase + ", MussSchriftlichEF1=" + this.MussSchriftlichEF1 + ", MussSchriftlichEF2=" + this.MussSchriftlichEF2 + ", MussMuendlich=" + this.MussMuendlich + ", Aufgabenfeld=" + this.Aufgabenfeld + ", AbgeschlFaecherHolen=" + this.AbgeschlFaecherHolen + ", GewichtungFHR=" + this.GewichtungFHR + ", MaxBemZeichen=" + this.MaxBemZeichen + ")";
	}

}
