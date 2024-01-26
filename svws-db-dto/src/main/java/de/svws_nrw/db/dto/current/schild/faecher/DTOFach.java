package de.svws_nrw.db.dto.current.schild.faecher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.statkue.ZulaessigesFachKuerzelASDConverter;

import de.svws_nrw.core.types.fach.ZulaessigesFach;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.statkue.ZulaessigesFachKuerzelASDConverterSerializer;
import de.svws_nrw.csv.converter.current.statkue.ZulaessigesFachKuerzelASDConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Faecher")
@NamedQuery(name = "DTOFach.all", query = "SELECT e FROM DTOFach e")
@NamedQuery(name = "DTOFach.id", query = "SELECT e FROM DTOFach e WHERE e.ID = :value")
@NamedQuery(name = "DTOFach.id.multiple", query = "SELECT e FROM DTOFach e WHERE e.ID IN :value")
@NamedQuery(name = "DTOFach.kuerzel", query = "SELECT e FROM DTOFach e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOFach.kuerzel.multiple", query = "SELECT e FROM DTOFach e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOFach.bezeichnung", query = "SELECT e FROM DTOFach e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOFach.bezeichnung.multiple", query = "SELECT e FROM DTOFach e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOFach.bezeichnungzeugnis", query = "SELECT e FROM DTOFach e WHERE e.BezeichnungZeugnis = :value")
@NamedQuery(name = "DTOFach.bezeichnungzeugnis.multiple", query = "SELECT e FROM DTOFach e WHERE e.BezeichnungZeugnis IN :value")
@NamedQuery(name = "DTOFach.bezeichnungueberweisungszeugnis", query = "SELECT e FROM DTOFach e WHERE e.BezeichnungUeberweisungsZeugnis = :value")
@NamedQuery(name = "DTOFach.bezeichnungueberweisungszeugnis.multiple", query = "SELECT e FROM DTOFach e WHERE e.BezeichnungUeberweisungsZeugnis IN :value")
@NamedQuery(name = "DTOFach.zeugnisdatenquelle_id", query = "SELECT e FROM DTOFach e WHERE e.Zeugnisdatenquelle_ID = :value")
@NamedQuery(name = "DTOFach.zeugnisdatenquelle_id.multiple", query = "SELECT e FROM DTOFach e WHERE e.Zeugnisdatenquelle_ID IN :value")
@NamedQuery(name = "DTOFach.statistikfach", query = "SELECT e FROM DTOFach e WHERE e.StatistikFach = :value")
@NamedQuery(name = "DTOFach.statistikfach.multiple", query = "SELECT e FROM DTOFach e WHERE e.StatistikFach IN :value")
@NamedQuery(name = "DTOFach.istoberstufenfach", query = "SELECT e FROM DTOFach e WHERE e.IstOberstufenFach = :value")
@NamedQuery(name = "DTOFach.istoberstufenfach.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstOberstufenFach IN :value")
@NamedQuery(name = "DTOFach.istfremdsprache", query = "SELECT e FROM DTOFach e WHERE e.IstFremdsprache = :value")
@NamedQuery(name = "DTOFach.istfremdsprache.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstFremdsprache IN :value")
@NamedQuery(name = "DTOFach.sortierungallg", query = "SELECT e FROM DTOFach e WHERE e.SortierungAllg = :value")
@NamedQuery(name = "DTOFach.sortierungallg.multiple", query = "SELECT e FROM DTOFach e WHERE e.SortierungAllg IN :value")
@NamedQuery(name = "DTOFach.sortierungsekii", query = "SELECT e FROM DTOFach e WHERE e.SortierungSekII = :value")
@NamedQuery(name = "DTOFach.sortierungsekii.multiple", query = "SELECT e FROM DTOFach e WHERE e.SortierungSekII IN :value")
@NamedQuery(name = "DTOFach.istnachpruefungerlaubt", query = "SELECT e FROM DTOFach e WHERE e.IstNachpruefungErlaubt = :value")
@NamedQuery(name = "DTOFach.istnachpruefungerlaubt.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstNachpruefungErlaubt IN :value")
@NamedQuery(name = "DTOFach.sichtbar", query = "SELECT e FROM DTOFach e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOFach.sichtbar.multiple", query = "SELECT e FROM DTOFach e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOFach.aenderbar", query = "SELECT e FROM DTOFach e WHERE e.Aenderbar = :value")
@NamedQuery(name = "DTOFach.aenderbar.multiple", query = "SELECT e FROM DTOFach e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "DTOFach.gewichtung", query = "SELECT e FROM DTOFach e WHERE e.Gewichtung = :value")
@NamedQuery(name = "DTOFach.gewichtung.multiple", query = "SELECT e FROM DTOFach e WHERE e.Gewichtung IN :value")
@NamedQuery(name = "DTOFach.unterichtssprache", query = "SELECT e FROM DTOFach e WHERE e.Unterichtssprache = :value")
@NamedQuery(name = "DTOFach.unterichtssprache.multiple", query = "SELECT e FROM DTOFach e WHERE e.Unterichtssprache IN :value")
@NamedQuery(name = "DTOFach.istschriftlichzk", query = "SELECT e FROM DTOFach e WHERE e.IstSchriftlichZK = :value")
@NamedQuery(name = "DTOFach.istschriftlichzk.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstSchriftlichZK IN :value")
@NamedQuery(name = "DTOFach.istschriftlichba", query = "SELECT e FROM DTOFach e WHERE e.IstSchriftlichBA = :value")
@NamedQuery(name = "DTOFach.istschriftlichba.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstSchriftlichBA IN :value")
@NamedQuery(name = "DTOFach.aufzeugnis", query = "SELECT e FROM DTOFach e WHERE e.AufZeugnis = :value")
@NamedQuery(name = "DTOFach.aufzeugnis.multiple", query = "SELECT e FROM DTOFach e WHERE e.AufZeugnis IN :value")
@NamedQuery(name = "DTOFach.istpruefungsordnungsrelevant", query = "SELECT e FROM DTOFach e WHERE e.IstPruefungsordnungsRelevant = :value")
@NamedQuery(name = "DTOFach.istpruefungsordnungsrelevant.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstPruefungsordnungsRelevant IN :value")
@NamedQuery(name = "DTOFach.lernfelder", query = "SELECT e FROM DTOFach e WHERE e.Lernfelder = :value")
@NamedQuery(name = "DTOFach.lernfelder.multiple", query = "SELECT e FROM DTOFach e WHERE e.Lernfelder IN :value")
@NamedQuery(name = "DTOFach.istmoeglichabilk", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichAbiLK = :value")
@NamedQuery(name = "DTOFach.istmoeglichabilk.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichAbiLK IN :value")
@NamedQuery(name = "DTOFach.istmoeglichabigk", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichAbiGK = :value")
@NamedQuery(name = "DTOFach.istmoeglichabigk.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichAbiGK IN :value")
@NamedQuery(name = "DTOFach.istmoeglichef1", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichEF1 = :value")
@NamedQuery(name = "DTOFach.istmoeglichef1.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichEF1 IN :value")
@NamedQuery(name = "DTOFach.istmoeglichef2", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichEF2 = :value")
@NamedQuery(name = "DTOFach.istmoeglichef2.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichEF2 IN :value")
@NamedQuery(name = "DTOFach.istmoeglichq11", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichQ11 = :value")
@NamedQuery(name = "DTOFach.istmoeglichq11.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichQ11 IN :value")
@NamedQuery(name = "DTOFach.istmoeglichq12", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichQ12 = :value")
@NamedQuery(name = "DTOFach.istmoeglichq12.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichQ12 IN :value")
@NamedQuery(name = "DTOFach.istmoeglichq21", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichQ21 = :value")
@NamedQuery(name = "DTOFach.istmoeglichq21.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichQ21 IN :value")
@NamedQuery(name = "DTOFach.istmoeglichq22", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichQ22 = :value")
@NamedQuery(name = "DTOFach.istmoeglichq22.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichQ22 IN :value")
@NamedQuery(name = "DTOFach.istmoeglichalsneuefremdspracheinsekii", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichAlsNeueFremdspracheInSekII = :value")
@NamedQuery(name = "DTOFach.istmoeglichalsneuefremdspracheinsekii.multiple", query = "SELECT e FROM DTOFach e WHERE e.IstMoeglichAlsNeueFremdspracheInSekII IN :value")
@NamedQuery(name = "DTOFach.projektkursleitfach1_id", query = "SELECT e FROM DTOFach e WHERE e.ProjektKursLeitfach1_ID = :value")
@NamedQuery(name = "DTOFach.projektkursleitfach1_id.multiple", query = "SELECT e FROM DTOFach e WHERE e.ProjektKursLeitfach1_ID IN :value")
@NamedQuery(name = "DTOFach.projektkursleitfach2_id", query = "SELECT e FROM DTOFach e WHERE e.ProjektKursLeitfach2_ID = :value")
@NamedQuery(name = "DTOFach.projektkursleitfach2_id.multiple", query = "SELECT e FROM DTOFach e WHERE e.ProjektKursLeitfach2_ID IN :value")
@NamedQuery(name = "DTOFach.wochenstundenef1", query = "SELECT e FROM DTOFach e WHERE e.WochenstundenEF1 = :value")
@NamedQuery(name = "DTOFach.wochenstundenef1.multiple", query = "SELECT e FROM DTOFach e WHERE e.WochenstundenEF1 IN :value")
@NamedQuery(name = "DTOFach.wochenstundenef2", query = "SELECT e FROM DTOFach e WHERE e.WochenstundenEF2 = :value")
@NamedQuery(name = "DTOFach.wochenstundenef2.multiple", query = "SELECT e FROM DTOFach e WHERE e.WochenstundenEF2 IN :value")
@NamedQuery(name = "DTOFach.wochenstundenqualifikationsphase", query = "SELECT e FROM DTOFach e WHERE e.WochenstundenQualifikationsphase = :value")
@NamedQuery(name = "DTOFach.wochenstundenqualifikationsphase.multiple", query = "SELECT e FROM DTOFach e WHERE e.WochenstundenQualifikationsphase IN :value")
@NamedQuery(name = "DTOFach.mussschriftlichef1", query = "SELECT e FROM DTOFach e WHERE e.MussSchriftlichEF1 = :value")
@NamedQuery(name = "DTOFach.mussschriftlichef1.multiple", query = "SELECT e FROM DTOFach e WHERE e.MussSchriftlichEF1 IN :value")
@NamedQuery(name = "DTOFach.mussschriftlichef2", query = "SELECT e FROM DTOFach e WHERE e.MussSchriftlichEF2 = :value")
@NamedQuery(name = "DTOFach.mussschriftlichef2.multiple", query = "SELECT e FROM DTOFach e WHERE e.MussSchriftlichEF2 IN :value")
@NamedQuery(name = "DTOFach.mussmuendlich", query = "SELECT e FROM DTOFach e WHERE e.MussMuendlich = :value")
@NamedQuery(name = "DTOFach.mussmuendlich.multiple", query = "SELECT e FROM DTOFach e WHERE e.MussMuendlich IN :value")
@NamedQuery(name = "DTOFach.aufgabenfeld", query = "SELECT e FROM DTOFach e WHERE e.Aufgabenfeld = :value")
@NamedQuery(name = "DTOFach.aufgabenfeld.multiple", query = "SELECT e FROM DTOFach e WHERE e.Aufgabenfeld IN :value")
@NamedQuery(name = "DTOFach.abgeschlfaecherholen", query = "SELECT e FROM DTOFach e WHERE e.AbgeschlFaecherHolen = :value")
@NamedQuery(name = "DTOFach.abgeschlfaecherholen.multiple", query = "SELECT e FROM DTOFach e WHERE e.AbgeschlFaecherHolen IN :value")
@NamedQuery(name = "DTOFach.gewichtungfhr", query = "SELECT e FROM DTOFach e WHERE e.GewichtungFHR = :value")
@NamedQuery(name = "DTOFach.gewichtungfhr.multiple", query = "SELECT e FROM DTOFach e WHERE e.GewichtungFHR IN :value")
@NamedQuery(name = "DTOFach.maxbemzeichen", query = "SELECT e FROM DTOFach e WHERE e.MaxBemZeichen = :value")
@NamedQuery(name = "DTOFach.maxbemzeichen.multiple", query = "SELECT e FROM DTOFach e WHERE e.MaxBemZeichen IN :value")
@NamedQuery(name = "DTOFach.primaryKeyQuery", query = "SELECT e FROM DTOFach e WHERE e.ID = ?1")
@NamedQuery(name = "DTOFach.primaryKeyQuery.multiple", query = "SELECT e FROM DTOFach e WHERE e.ID IN :value")
@NamedQuery(name = "DTOFach.all.migration", query = "SELECT e FROM DTOFach e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kuerzel", "Bezeichnung", "BezeichnungZeugnis", "BezeichnungUeberweisungsZeugnis", "Zeugnisdatenquelle_ID", "StatistikFach", "IstOberstufenFach", "IstFremdsprache", "SortierungAllg", "SortierungSekII", "IstNachpruefungErlaubt", "Sichtbar", "Aenderbar", "Gewichtung", "Unterichtssprache", "IstSchriftlichZK", "IstSchriftlichBA", "AufZeugnis", "IstPruefungsordnungsRelevant", "Lernfelder", "IstMoeglichAbiLK", "IstMoeglichAbiGK", "IstMoeglichEF1", "IstMoeglichEF2", "IstMoeglichQ11", "IstMoeglichQ12", "IstMoeglichQ21", "IstMoeglichQ22", "IstMoeglichAlsNeueFremdspracheInSekII", "ProjektKursLeitfach1_ID", "ProjektKursLeitfach2_ID", "WochenstundenEF1", "WochenstundenEF2", "WochenstundenQualifikationsphase", "MussSchriftlichEF1", "MussSchriftlichEF2", "MussMuendlich", "Aufgabenfeld", "AbgeschlFaecherHolen", "GewichtungFHR", "MaxBemZeichen"})
public final class DTOFach {

	/** Eindeutige ID zur Kennzeichnung des Fächer-Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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

	/** Das Statistik-Kürzel des Faches */
	@Column(name = "StatistikKrz")
	@JsonProperty
	@Convert(converter = ZulaessigesFachKuerzelASDConverter.class)
	@JsonSerialize(using = ZulaessigesFachKuerzelASDConverterSerializer.class)
	@JsonDeserialize(using = ZulaessigesFachKuerzelASDConverterDeserializer.class)
	public ZulaessigesFach StatistikFach;

	/** Gibt an, ob das Fach in der Oberstufe unterrichtet wird */
	@Column(name = "BasisFach")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstOberstufenFach;

	/** Gibt an, ob das Fach eine Fremdsprache ist */
	@Column(name = "IstSprache")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstNachpruefungErlaubt;

	/** Gibt an, ob der Datensatz in der Oberfläche sichtbar sein soll und bei einer Auswahl zur Verfügung steht.  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an, ob Änderungen am Datensatz erlaubt sind. */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Gibt die Gewichtung des Faches im allgemeinbildenden Bereich am BK an, wenn keine gliederungsbezogenen Gewichtungen vergeben sind */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/** Einstelliges Kürzel der Unterrichtssprache (E, F, …) */
	@Column(name = "Unterichtssprache")
	@JsonProperty
	public String Unterichtssprache;

	/** gibt an, ob das Fach ein schritliches Fach für ZK ist oder nicht */
	@Column(name = "IstSchriftlich")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstSchriftlichZK;

	/** gibt an, ob ein Fach am BK für den beruflichen Abschluss schriftlich gewertet wird */
	@Column(name = "IstSchriftlichBA")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstSchriftlichBA;

	/** Gibt an, ob das Fach auf dem Zeugnis erscheinen soll */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean AufZeugnis;

	/** Gibt an, ob das Fach für die Prüfungsordnung relevant ist und bei Belegprüfungen genutzt werden soll */
	@Column(name = "IstPruefungsordnungsRelevant")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstPruefungsordnungsRelevant;

	/** speichert die Lernfeld-Texte zu den Fachklassen beim Unterrichtsfach am BK, ist von den Textfelder im Menü Fachklassen abgelöst worden, sollte nicht mehr genutzt werden */
	@Column(name = "Lernfelder")
	@JsonProperty
	public String Lernfelder;

	/** gibt an, ob das Fach als Leistungskurs belegt werden kann. */
	@Column(name = "LK_Moegl")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichAbiLK;

	/** gibt an, ob das Fach als drittes oder viertes Abiturfach belegt werden kann. */
	@Column(name = "Abi_Moegl")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichAbiGK;

	/** gibt an, ob das Fach im 1. Halbjahr der Einführungsphase belegt werden kann, */
	@Column(name = "E1")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichEF1;

	/** gibt an, ob das Fach im 2. Halbjahr der Einführungsphase belegt werden kann, */
	@Column(name = "E2")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichEF2;

	/** gibt an, ob das Fach im 1. Halbjahr des 1. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q1")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ11;

	/** gibt an, ob das Fach im 2. Halbjahr des 1. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q2")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ12;

	/** gibt an, ob das Fach im 1. Halbjahr des 2. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q3")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ21;

	/** gibt an, ob das Fach im 2. Halbjahr des 2. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q4")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ22;

	/** gibt an, ob das Fach als neue Fremdsprache in der Sekundarstufe zählt. */
	@Column(name = "AlsNeueFSInSII")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussSchriftlichEF1;

	/** Beim WBK: Gibt an, ob das Fach in dem 2. Halbjahr der Einführungsphase schriftlich belegt werden muss */
	@Column(name = "E2_S")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussSchriftlichEF2;

	/** Beim WBK: Gibt an, ob das Fach nur muendlich belegt werden darf */
	@Column(name = "NurMuendlich")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussMuendlich;

	/** Das Aufgabenfeld, zu welchem das Fach gehört */
	@Column(name = "Aufgabenfeld")
	@JsonProperty
	public String Aufgabenfeld;

	/** Gibt an, ob das Fach bei der Operation „Abgeschlossene Fächer holen“ berücksichtigt werden soll. */
	@Column(name = "AbgeschlFaecherHolen")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse DTOFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param IstPruefungsordnungsRelevant   der Wert für das Attribut IstPruefungsordnungsRelevant
	 */
	public DTOFach(final long ID, final Boolean IstPruefungsordnungsRelevant) {
		this.ID = ID;
		this.IstPruefungsordnungsRelevant = IstPruefungsordnungsRelevant;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOFach other = (DTOFach) obj;
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
		return "DTOFach(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", BezeichnungZeugnis=" + this.BezeichnungZeugnis + ", BezeichnungUeberweisungsZeugnis=" + this.BezeichnungUeberweisungsZeugnis + ", Zeugnisdatenquelle_ID=" + this.Zeugnisdatenquelle_ID + ", StatistikFach=" + this.StatistikFach + ", IstOberstufenFach=" + this.IstOberstufenFach + ", IstFremdsprache=" + this.IstFremdsprache + ", SortierungAllg=" + this.SortierungAllg + ", SortierungSekII=" + this.SortierungSekII + ", IstNachpruefungErlaubt=" + this.IstNachpruefungErlaubt + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", Gewichtung=" + this.Gewichtung + ", Unterichtssprache=" + this.Unterichtssprache + ", IstSchriftlichZK=" + this.IstSchriftlichZK + ", IstSchriftlichBA=" + this.IstSchriftlichBA + ", AufZeugnis=" + this.AufZeugnis + ", IstPruefungsordnungsRelevant=" + this.IstPruefungsordnungsRelevant + ", Lernfelder=" + this.Lernfelder + ", IstMoeglichAbiLK=" + this.IstMoeglichAbiLK + ", IstMoeglichAbiGK=" + this.IstMoeglichAbiGK + ", IstMoeglichEF1=" + this.IstMoeglichEF1 + ", IstMoeglichEF2=" + this.IstMoeglichEF2 + ", IstMoeglichQ11=" + this.IstMoeglichQ11 + ", IstMoeglichQ12=" + this.IstMoeglichQ12 + ", IstMoeglichQ21=" + this.IstMoeglichQ21 + ", IstMoeglichQ22=" + this.IstMoeglichQ22 + ", IstMoeglichAlsNeueFremdspracheInSekII=" + this.IstMoeglichAlsNeueFremdspracheInSekII + ", ProjektKursLeitfach1_ID=" + this.ProjektKursLeitfach1_ID + ", ProjektKursLeitfach2_ID=" + this.ProjektKursLeitfach2_ID + ", WochenstundenEF1=" + this.WochenstundenEF1 + ", WochenstundenEF2=" + this.WochenstundenEF2 + ", WochenstundenQualifikationsphase=" + this.WochenstundenQualifikationsphase + ", MussSchriftlichEF1=" + this.MussSchriftlichEF1 + ", MussSchriftlichEF2=" + this.MussSchriftlichEF2 + ", MussMuendlich=" + this.MussMuendlich + ", Aufgabenfeld=" + this.Aufgabenfeld + ", AbgeschlFaecherHolen=" + this.AbgeschlFaecherHolen + ", GewichtungFHR=" + this.GewichtungFHR + ", MaxBemZeichen=" + this.MaxBemZeichen + ")";
	}

}
