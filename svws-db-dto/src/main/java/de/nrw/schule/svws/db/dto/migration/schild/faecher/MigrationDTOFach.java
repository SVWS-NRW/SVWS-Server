package de.nrw.schule.svws.db.dto.migration.schild.faecher;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Faecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Faecher")
@NamedQuery(name="MigrationDTOFach.all", query="SELECT e FROM MigrationDTOFach e")
@NamedQuery(name="MigrationDTOFach.id", query="SELECT e FROM MigrationDTOFach e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOFach.id.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOFach.kuerzel", query="SELECT e FROM MigrationDTOFach e WHERE e.Kuerzel = :value")
@NamedQuery(name="MigrationDTOFach.kuerzel.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Kuerzel IN :value")
@NamedQuery(name="MigrationDTOFach.bezeichnung", query="SELECT e FROM MigrationDTOFach e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOFach.bezeichnung.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOFach.bezeichnungzeugnis", query="SELECT e FROM MigrationDTOFach e WHERE e.BezeichnungZeugnis = :value")
@NamedQuery(name="MigrationDTOFach.bezeichnungzeugnis.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.BezeichnungZeugnis IN :value")
@NamedQuery(name="MigrationDTOFach.bezeichnungueberweisungszeugnis", query="SELECT e FROM MigrationDTOFach e WHERE e.BezeichnungUeberweisungsZeugnis = :value")
@NamedQuery(name="MigrationDTOFach.bezeichnungueberweisungszeugnis.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.BezeichnungUeberweisungsZeugnis IN :value")
@NamedQuery(name="MigrationDTOFach.zeugnisdatenquelle_id", query="SELECT e FROM MigrationDTOFach e WHERE e.Zeugnisdatenquelle_ID = :value")
@NamedQuery(name="MigrationDTOFach.zeugnisdatenquelle_id.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Zeugnisdatenquelle_ID IN :value")
@NamedQuery(name="MigrationDTOFach.fachgruppe_id", query="SELECT e FROM MigrationDTOFach e WHERE e.Fachgruppe_ID = :value")
@NamedQuery(name="MigrationDTOFach.fachgruppe_id.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Fachgruppe_ID IN :value")
@NamedQuery(name="MigrationDTOFach.statistikfach", query="SELECT e FROM MigrationDTOFach e WHERE e.StatistikFach = :value")
@NamedQuery(name="MigrationDTOFach.statistikfach.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.StatistikFach IN :value")
@NamedQuery(name="MigrationDTOFach.istoberstufenfach", query="SELECT e FROM MigrationDTOFach e WHERE e.IstOberstufenFach = :value")
@NamedQuery(name="MigrationDTOFach.istoberstufenfach.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstOberstufenFach IN :value")
@NamedQuery(name="MigrationDTOFach.istfremdsprache", query="SELECT e FROM MigrationDTOFach e WHERE e.IstFremdsprache = :value")
@NamedQuery(name="MigrationDTOFach.istfremdsprache.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstFremdsprache IN :value")
@NamedQuery(name="MigrationDTOFach.sortierungallg", query="SELECT e FROM MigrationDTOFach e WHERE e.SortierungAllg = :value")
@NamedQuery(name="MigrationDTOFach.sortierungallg.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.SortierungAllg IN :value")
@NamedQuery(name="MigrationDTOFach.sortierungsekii", query="SELECT e FROM MigrationDTOFach e WHERE e.SortierungSekII = :value")
@NamedQuery(name="MigrationDTOFach.sortierungsekii.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.SortierungSekII IN :value")
@NamedQuery(name="MigrationDTOFach.istnachpruefungerlaubt", query="SELECT e FROM MigrationDTOFach e WHERE e.IstNachpruefungErlaubt = :value")
@NamedQuery(name="MigrationDTOFach.istnachpruefungerlaubt.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstNachpruefungErlaubt IN :value")
@NamedQuery(name="MigrationDTOFach.sichtbar", query="SELECT e FROM MigrationDTOFach e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOFach.sichtbar.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOFach.aenderbar", query="SELECT e FROM MigrationDTOFach e WHERE e.Aenderbar = :value")
@NamedQuery(name="MigrationDTOFach.aenderbar.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Aenderbar IN :value")
@NamedQuery(name="MigrationDTOFach.gewichtung", query="SELECT e FROM MigrationDTOFach e WHERE e.Gewichtung = :value")
@NamedQuery(name="MigrationDTOFach.gewichtung.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Gewichtung IN :value")
@NamedQuery(name="MigrationDTOFach.unterichtssprache", query="SELECT e FROM MigrationDTOFach e WHERE e.Unterichtssprache = :value")
@NamedQuery(name="MigrationDTOFach.unterichtssprache.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Unterichtssprache IN :value")
@NamedQuery(name="MigrationDTOFach.istschriftlichzk", query="SELECT e FROM MigrationDTOFach e WHERE e.IstSchriftlichZK = :value")
@NamedQuery(name="MigrationDTOFach.istschriftlichzk.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstSchriftlichZK IN :value")
@NamedQuery(name="MigrationDTOFach.istschriftlichba", query="SELECT e FROM MigrationDTOFach e WHERE e.IstSchriftlichBA = :value")
@NamedQuery(name="MigrationDTOFach.istschriftlichba.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstSchriftlichBA IN :value")
@NamedQuery(name="MigrationDTOFach.aufzeugnis", query="SELECT e FROM MigrationDTOFach e WHERE e.AufZeugnis = :value")
@NamedQuery(name="MigrationDTOFach.aufzeugnis.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.AufZeugnis IN :value")
@NamedQuery(name="MigrationDTOFach.lernfelder", query="SELECT e FROM MigrationDTOFach e WHERE e.Lernfelder = :value")
@NamedQuery(name="MigrationDTOFach.lernfelder.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Lernfelder IN :value")
@NamedQuery(name="MigrationDTOFach.schulnreigner", query="SELECT e FROM MigrationDTOFach e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOFach.schulnreigner.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichabilk", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAbiLK = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichabilk.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAbiLK IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichabigk", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAbiGK = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichabigk.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAbiGK IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichef1", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichEF1 = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichef1.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichEF1 IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichef2", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichEF2 = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichef2.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichEF2 IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichq11", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ11 = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichq11.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ11 IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichq12", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ12 = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichq12.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ12 IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichq21", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ21 = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichq21.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ21 IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichq22", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ22 = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichq22.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichQ22 IN :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichalsneuefremdspracheinsekii", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAlsNeueFremdspracheInSekII = :value")
@NamedQuery(name="MigrationDTOFach.istmoeglichalsneuefremdspracheinsekii.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.IstMoeglichAlsNeueFremdspracheInSekII IN :value")
@NamedQuery(name="MigrationDTOFach.projektkursleitfach1_id", query="SELECT e FROM MigrationDTOFach e WHERE e.ProjektKursLeitfach1_ID = :value")
@NamedQuery(name="MigrationDTOFach.projektkursleitfach1_id.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.ProjektKursLeitfach1_ID IN :value")
@NamedQuery(name="MigrationDTOFach.projektkursleitfach2_id", query="SELECT e FROM MigrationDTOFach e WHERE e.ProjektKursLeitfach2_ID = :value")
@NamedQuery(name="MigrationDTOFach.projektkursleitfach2_id.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.ProjektKursLeitfach2_ID IN :value")
@NamedQuery(name="MigrationDTOFach.wochenstundenef1", query="SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenEF1 = :value")
@NamedQuery(name="MigrationDTOFach.wochenstundenef1.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenEF1 IN :value")
@NamedQuery(name="MigrationDTOFach.wochenstundenef2", query="SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenEF2 = :value")
@NamedQuery(name="MigrationDTOFach.wochenstundenef2.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenEF2 IN :value")
@NamedQuery(name="MigrationDTOFach.wochenstundenqualifikationsphase", query="SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenQualifikationsphase = :value")
@NamedQuery(name="MigrationDTOFach.wochenstundenqualifikationsphase.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.WochenstundenQualifikationsphase IN :value")
@NamedQuery(name="MigrationDTOFach.mussschriftlichef1", query="SELECT e FROM MigrationDTOFach e WHERE e.MussSchriftlichEF1 = :value")
@NamedQuery(name="MigrationDTOFach.mussschriftlichef1.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.MussSchriftlichEF1 IN :value")
@NamedQuery(name="MigrationDTOFach.mussschriftlichef2", query="SELECT e FROM MigrationDTOFach e WHERE e.MussSchriftlichEF2 = :value")
@NamedQuery(name="MigrationDTOFach.mussschriftlichef2.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.MussSchriftlichEF2 IN :value")
@NamedQuery(name="MigrationDTOFach.mussmuendlich", query="SELECT e FROM MigrationDTOFach e WHERE e.MussMuendlich = :value")
@NamedQuery(name="MigrationDTOFach.mussmuendlich.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.MussMuendlich IN :value")
@NamedQuery(name="MigrationDTOFach.aufgabenfeld", query="SELECT e FROM MigrationDTOFach e WHERE e.Aufgabenfeld = :value")
@NamedQuery(name="MigrationDTOFach.aufgabenfeld.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.Aufgabenfeld IN :value")
@NamedQuery(name="MigrationDTOFach.abgeschlfaecherholen", query="SELECT e FROM MigrationDTOFach e WHERE e.AbgeschlFaecherHolen = :value")
@NamedQuery(name="MigrationDTOFach.abgeschlfaecherholen.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.AbgeschlFaecherHolen IN :value")
@NamedQuery(name="MigrationDTOFach.gewichtungfhr", query="SELECT e FROM MigrationDTOFach e WHERE e.GewichtungFHR = :value")
@NamedQuery(name="MigrationDTOFach.gewichtungfhr.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.GewichtungFHR IN :value")
@NamedQuery(name="MigrationDTOFach.maxbemzeichen", query="SELECT e FROM MigrationDTOFach e WHERE e.MaxBemZeichen = :value")
@NamedQuery(name="MigrationDTOFach.maxbemzeichen.multiple", query="SELECT e FROM MigrationDTOFach e WHERE e.MaxBemZeichen IN :value")
@NamedQuery(name="MigrationDTOFach.primaryKeyQuery", query="SELECT e FROM MigrationDTOFach e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOFach.all.migration", query="SELECT e FROM MigrationDTOFach e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Bezeichnung","BezeichnungZeugnis","BezeichnungUeberweisungsZeugnis","Zeugnisdatenquelle_ID","Fachgruppe_ID","StatistikFach","IstOberstufenFach","IstFremdsprache","SortierungAllg","SortierungSekII","IstNachpruefungErlaubt","Sichtbar","Aenderbar","Gewichtung","Unterichtssprache","IstSchriftlichZK","IstSchriftlichBA","AufZeugnis","Lernfelder","SchulnrEigner","IstMoeglichAbiLK","IstMoeglichAbiGK","IstMoeglichEF1","IstMoeglichEF2","IstMoeglichQ11","IstMoeglichQ12","IstMoeglichQ21","IstMoeglichQ22","IstMoeglichAlsNeueFremdspracheInSekII","ProjektKursLeitfach1_ID","ProjektKursLeitfach2_ID","WochenstundenEF1","WochenstundenEF2","WochenstundenQualifikationsphase","MussSchriftlichEF1","MussSchriftlichEF2","MussMuendlich","Aufgabenfeld","AbgeschlFaecherHolen","GewichtungFHR","MaxBemZeichen"})
public class MigrationDTOFach {

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
	public String StatistikFach;

	/** Gibt an, ob das Fach in der Oberstufe unterrichtet wird */
	@Column(name = "BasisFach")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstOberstufenFach;

	/** Gibt an, ob das Fach eine Fremdsprache ist */
	@Column(name = "IstSprache")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstNachpruefungErlaubt;

	/** Gibt an, ob der Datensatz in der Oberfläche sichtbar sein soll und bei einer Auswahl zur Verfügung steht.  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an, ob Änderungen am Datensatz erlaubt sind. */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstSchriftlichZK;

	/** gibt an, ob ein Fach am BK für den beruflichen Abschluss schriftlich gewertet wird */
	@Column(name = "IstSchriftlichBA")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean IstSchriftlichBA;

	/** Gibt an, ob das Fach auf dem Zeugnis erscheinen soll */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichAbiLK;

	/** gibt an, ob das Fach als drittes oder viertes Abiturfach belegt werden kann. */
	@Column(name = "Abi_Moegl")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichAbiGK;

	/** gibt an, ob das Fach im 1. Halbjahr der Einführungsphase belegt werden kann, */
	@Column(name = "E1")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichEF1;

	/** gibt an, ob das Fach im 2. Halbjahr der Einführungsphase belegt werden kann, */
	@Column(name = "E2")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichEF2;

	/** gibt an, ob das Fach im 1. Halbjahr des 1. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q1")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ11;

	/** gibt an, ob das Fach im 2. Halbjahr des 1. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q2")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ12;

	/** gibt an, ob das Fach im 1. Halbjahr des 2. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q3")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ21;

	/** gibt an, ob das Fach im 2. Halbjahr des 2. Jahres der Qualifikationsphase belegt werden kann, */
	@Column(name = "Q4")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstMoeglichQ22;

	/** gibt an, ob das Fach als neue Fremdsprache in der Sekundarstufe zählt. */
	@Column(name = "AlsNeueFSInSII")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussSchriftlichEF1;

	/** Beim WBK: Gibt an, ob das Fach in dem 2. Halbjahr der Einführungsphase schriftlich belegt werden muss */
	@Column(name = "E2_S")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussSchriftlichEF2;

	/** Beim WBK: Gibt an, ob das Fach nur muendlich belegt werden darf */
	@Column(name = "NurMuendlich")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MussMuendlich;

	/** Das Aufgabenfeld, zu welchem das Fach gehört */
	@Column(name = "Aufgabenfeld")
	@JsonProperty
	public String Aufgabenfeld;

	/** Gibt an, ob das Fach bei der Operation „Abgeschlossene Fächer holen“ berücksichtigt werden soll. */
	@Column(name = "AbgeschlFaecherHolen")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	public boolean equals(Object obj) {
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
		return "MigrationDTOFach(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", BezeichnungZeugnis=" + this.BezeichnungZeugnis + ", BezeichnungUeberweisungsZeugnis=" + this.BezeichnungUeberweisungsZeugnis + ", Zeugnisdatenquelle_ID=" + this.Zeugnisdatenquelle_ID + ", Fachgruppe_ID=" + this.Fachgruppe_ID + ", StatistikFach=" + this.StatistikFach + ", IstOberstufenFach=" + this.IstOberstufenFach + ", IstFremdsprache=" + this.IstFremdsprache + ", SortierungAllg=" + this.SortierungAllg + ", SortierungSekII=" + this.SortierungSekII + ", IstNachpruefungErlaubt=" + this.IstNachpruefungErlaubt + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", Gewichtung=" + this.Gewichtung + ", Unterichtssprache=" + this.Unterichtssprache + ", IstSchriftlichZK=" + this.IstSchriftlichZK + ", IstSchriftlichBA=" + this.IstSchriftlichBA + ", AufZeugnis=" + this.AufZeugnis + ", Lernfelder=" + this.Lernfelder + ", SchulnrEigner=" + this.SchulnrEigner + ", IstMoeglichAbiLK=" + this.IstMoeglichAbiLK + ", IstMoeglichAbiGK=" + this.IstMoeglichAbiGK + ", IstMoeglichEF1=" + this.IstMoeglichEF1 + ", IstMoeglichEF2=" + this.IstMoeglichEF2 + ", IstMoeglichQ11=" + this.IstMoeglichQ11 + ", IstMoeglichQ12=" + this.IstMoeglichQ12 + ", IstMoeglichQ21=" + this.IstMoeglichQ21 + ", IstMoeglichQ22=" + this.IstMoeglichQ22 + ", IstMoeglichAlsNeueFremdspracheInSekII=" + this.IstMoeglichAlsNeueFremdspracheInSekII + ", ProjektKursLeitfach1_ID=" + this.ProjektKursLeitfach1_ID + ", ProjektKursLeitfach2_ID=" + this.ProjektKursLeitfach2_ID + ", WochenstundenEF1=" + this.WochenstundenEF1 + ", WochenstundenEF2=" + this.WochenstundenEF2 + ", WochenstundenQualifikationsphase=" + this.WochenstundenQualifikationsphase + ", MussSchriftlichEF1=" + this.MussSchriftlichEF1 + ", MussSchriftlichEF2=" + this.MussSchriftlichEF2 + ", MussMuendlich=" + this.MussMuendlich + ", Aufgabenfeld=" + this.Aufgabenfeld + ", AbgeschlFaecherHolen=" + this.AbgeschlFaecherHolen + ", GewichtungFHR=" + this.GewichtungFHR + ", MaxBemZeichen=" + this.MaxBemZeichen + ")";
	}

}