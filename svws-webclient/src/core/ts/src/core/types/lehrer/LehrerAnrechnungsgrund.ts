import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerKatalogAnrechnungsgrundEintrag } from '../../../core/data/lehrer/LehrerKatalogAnrechnungsgrundEintrag';

export class LehrerAnrechnungsgrund extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerAnrechnungsgrund> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LehrerAnrechnungsgrund> = new Map<string, LehrerAnrechnungsgrund>();

	/**
	 * Anrechnungsgrund 'Seiteneinsteiger/-in: Entlastung für berufsbegleitende Ausbildung für Universitätsabsolventen OBAS' für Stunden bei einem Lehrer
	 */
	public static readonly ID_310 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_310", 0, [new LehrerKatalogAnrechnungsgrundEintrag(310000, "310", "Seiteneinsteiger/-in: Entlastung für berufsbegleitende Ausbildung für Universitätsabsolventen OBAS", 2010, null)]);

	/**
	 * Anrechnungsgrund 'Seiteneinsteiger/-in: Entlastung für berufsbegleitende Ausbildung für FH - Absolventen' für Stunden bei einem Lehrer
	 */
	public static readonly ID_315 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_315", 1, [new LehrerKatalogAnrechnungsgrundEintrag(315000, "315", "Seiteneinsteiger/-in: Entlastung für berufsbegleitende Ausbildung für FH - Absolventen", 2010, null)]);

	/**
	 * Anrechnungsgrund 'Seiteneinsteiger/-in: Entlastung für Pädagogische Einführung in den Schuldienst' für Stunden bei einem Lehrer
	 */
	public static readonly ID_320 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_320", 2, [new LehrerKatalogAnrechnungsgrundEintrag(320000, "320", "Seiteneinsteiger/-in: Entlastung für Pädagogische Einführung in den Schuldienst", 2010, null)]);

	/**
	 * Anrechnungsgrund 'Teilnahme an Qualifizierungsmaßnahme: Einführung in die Grundschuldidaktik' für Stunden bei einem Lehrer
	 */
	public static readonly ID_325 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_325", 3, [new LehrerKatalogAnrechnungsgrundEintrag(325000, "325", "Teilnahme an Qualifizierungsmaßnahme: Einführung in die Grundschuldidaktik", 2019, null)]);

	/**
	 * Anrechnungsgrund 'Tätigkeit als schulische/-r Ausbilder/-in OBAS (§ 11 Abs. 5 i. V. mit § 9 Abs. 2)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_330 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_330", 4, [new LehrerKatalogAnrechnungsgrundEintrag(330000, "330", "Tätigkeit als schulische/-r Ausbilder/-in OBAS (§ 11 Abs. 5 i. V. mit § 9 Abs. 2)", 2012, null)]);

	/**
	 * Anrechnungsgrund 'Erfahrene Lehrkraft: Entlastung für Pädagogische Einführung in den Schuldienst' für Stunden bei einem Lehrer
	 */
	public static readonly ID_340 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_340", 5, [new LehrerKatalogAnrechnungsgrundEintrag(340000, "340", "Erfahrene Lehrkraft: Entlastung für Pädagogische Einführung in den Schuldienst", 2012, null)]);

	/**
	 * Anrechnungsgrund 'Wahrnehmung besonderer schulischer Aufgaben / Ausgleich besonderer unterrichtlicher Belastungen' für Stunden bei einem Lehrer
	 */
	public static readonly ID_500 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_500", 6, [new LehrerKatalogAnrechnungsgrundEintrag(500000, "500", "Wahrnehmung besonderer schulischer Aufgaben / Ausgleich besonderer unterrichtlicher Belastungen", null, null)]);

	/**
	 * Anrechnungsgrund 'Schulleitungspauschale' für Stunden bei einem Lehrer
	 */
	public static readonly ID_510 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_510", 7, [new LehrerKatalogAnrechnungsgrundEintrag(510000, "510", "Schulleitungspauschale", null, null)]);

	/**
	 * Anrechnungsgrund 'Schulübergreifende Aufgaben kleineren Umfangs' für Stunden bei einem Lehrer
	 */
	public static readonly ID_520 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_520", 8, [new LehrerKatalogAnrechnungsgrundEintrag(520000, "520", "Schulübergreifende Aufgaben kleineren Umfangs", null, null)]);

	/**
	 * Anrechnungsgrund 'SV-Verbindungslehrer, Beratungslehrer' für Stunden bei einem Lehrer
	 */
	public static readonly ID_530 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_530", 9, [new LehrerKatalogAnrechnungsgrundEintrag(530000, "530", "SV-Verbindungslehrer, Beratungslehrer", null, null)]);

	/**
	 * Anrechnungsgrund 'Beratungsaufgaben in der Sek. I' für Stunden bei einem Lehrer
	 */
	public static readonly ID_540 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_540", 10, [new LehrerKatalogAnrechnungsgrundEintrag(540000, "540", "Beratungsaufgaben in der Sek. I", null, null)]);

	/**
	 * Anrechnungsgrund 'Laufbahnberatung und -kontrolle in der gymnasialen Oberstufe' für Stunden bei einem Lehrer
	 */
	public static readonly ID_550 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_550", 11, [new LehrerKatalogAnrechnungsgrundEintrag(550000, "550", "Laufbahnberatung und -kontrolle in der gymnasialen Oberstufe", null, null)]);

	/**
	 * Anrechnungsgrund 'Koordinations- und Beratungsaufgaben im Landesvorhaben KAoA' für Stunden bei einem Lehrer
	 */
	public static readonly ID_590 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_590", 12, [new LehrerKatalogAnrechnungsgrundEintrag(590000, "590", "Koordinations- und Beratungsaufgaben im Landesvorhaben KAoA", 2013, null)]);

	/**
	 * Anrechnungsgrund 'Teamabsprachen, Unterrichtsvorbereitung für Gemeinsames Lernen' für Stunden bei einem Lehrer
	 */
	public static readonly ID_600 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_600", 13, [new LehrerKatalogAnrechnungsgrundEintrag(600000, "600", "Teamabsprachen, Unterrichtsvorbereitung für Gemeinsames Lernen", null, 2021), new LehrerKatalogAnrechnungsgrundEintrag(600001, "600", "Beratung, Teamabsprachen, Unterrichtsvorbereitung für Gemeinsames Lernen", 2022, null)]);

	/**
	 * Anrechnungsgrund 'Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Vorbereitungsdienst, OBAS, VOBASOF, Pädagogische Einführung in den Schuldienst)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_605 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_605", 14, [new LehrerKatalogAnrechnungsgrundEintrag(605000, "605", "Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Vorbereitungsdienst, OBAS, VOBASOF, Pädagogische Einführung in den Schuldienst)", null, null)]);

	/**
	 * Anrechnungsgrund 'Personalratstätigkeit' für Stunden bei einem Lehrer
	 */
	public static readonly ID_610 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_610", 15, [new LehrerKatalogAnrechnungsgrundEintrag(610000, "610", "Personalratstätigkeit", null, null)]);

	/**
	 * Anrechnungsgrund 'Schwerbehindertenvertretung' für Stunden bei einem Lehrer
	 */
	public static readonly ID_615 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_615", 16, [new LehrerKatalogAnrechnungsgrundEintrag(615000, "615", "Schwerbehindertenvertretung", null, null)]);

	/**
	 * Anrechnungsgrund 'Tätigkeit als schulische/-r Ausbilder/-in VOBASOF (qualifizierte Fachkraft, § 11 VOBASOF)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_620 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_620", 17, [new LehrerKatalogAnrechnungsgrundEintrag(620000, "620", "Tätigkeit als schulische/-r Ausbilder/-in VOBASOF (qualifizierte Fachkraft, § 11 VOBASOF)", 2013, null)]);

	/**
	 * Anrechnungsgründe
	 *  'Fortbildungslehrgänge für technische Lehrer aus Entwicklungsländern (LIB)' bzw.
	 *  'Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Eignungs- und Orientierungspraktikum)'
	 *  für Stunden bei einem Lehrer
	 */
	public static readonly ID_625 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_625", 18, [new LehrerKatalogAnrechnungsgrundEintrag(625000, "625", "Fortbildungslehrgänge für technische Lehrer aus Entwicklungsländern (LIB)", null, 2007), new LehrerKatalogAnrechnungsgrundEintrag(625001, "625", "Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Eignungs- und Orientierungspraktikum)", 2013, null)]);

	/**
	 * Anrechnungsgrund 'Lehrkraft in Ausbildung: Schulpraktische Ausbildung am Zentrum für schulpraktische Ausbildung (§ 7 VOBASOF)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_630 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_630", 19, [new LehrerKatalogAnrechnungsgrundEintrag(630000, "630", "Lehrkraft in Ausbildung: Schulpraktische Ausbildung am Zentrum für schulpraktische Ausbildung (§ 7 VOBASOF)", 2013, null)]);

	/**
	 * Anrechnungsgrund 'Fortbildung und Qualifikation, Medien und Datenschutz' für Stunden bei einem Lehrer
	 */
	public static readonly ID_635 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_635", 20, [new LehrerKatalogAnrechnungsgrundEintrag(635000, "635", "Fortbildung und Qualifikation, Medien und Datenschutz", null, null)]);

	/**
	 * Anrechnungsgrund 'Fachberater Schulaufsicht' für Stunden bei einem Lehrer
	 */
	public static readonly ID_640 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_640", 21, [new LehrerKatalogAnrechnungsgrundEintrag(640000, "640", "Fachberater Schulaufsicht", null, null)]);

	/**
	 * Anrechnungsgrund 'Sport sowie für Beratung und Koordination im Verbundsystem Schule und Leistungssport' für Stunden bei einem Lehrer
	 */
	public static readonly ID_645 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_645", 22, [new LehrerKatalogAnrechnungsgrundEintrag(645000, "645", "Sport sowie für Beratung und Koordination im Verbundsystem Schule und Leistungssport", null, null)]);

	/**
	 * Anrechnungsgrund 'Mitarbeit in Kommunalen Integrationszentren' für Stunden bei einem Lehrer
	 */
	public static readonly ID_650 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_650", 23, [new LehrerKatalogAnrechnungsgrundEintrag(650000, "650", "Mitarbeit in Kommunalen Integrationszentren", null, null)]);

	/**
	 * Anrechnungsgrund 'Auslandstätigkeit (Landeslehrerentsendeprogramm)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_655 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_655", 24, [new LehrerKatalogAnrechnungsgrundEintrag(655000, "655", "Auslandstätigkeit (Landeslehrerentsendeprogramm)", null, null)]);

	/**
	 * Anrechnungsgründe
	 *  'Laborschule Bielefeld / Bildungspol. Sonderaufgaben' bzw.
	 *  'Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Praxissemester)'
	 *  für Stunden bei einem Lehrer
	 */
	public static readonly ID_665 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_665", 25, [new LehrerKatalogAnrechnungsgrundEintrag(665000, "665", "Laborschule Bielefeld / Bildungspol. Sonderaufgaben", null, 2011), new LehrerKatalogAnrechnungsgrundEintrag(665001, "665", "Tätigkeit als Fachleiter/-in am Zentrum für schulpraktische Lehrerausbildung (Praxissemester)", 2013, null)]);

	/**
	 * Anrechnungsgrund 'Richtlinien und Lehrpläne / learn:line' für Stunden bei einem Lehrer
	 */
	public static readonly ID_670 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_670", 26, [new LehrerKatalogAnrechnungsgrundEintrag(670000, "670", "Richtlinien und Lehrpläne / learn:line", null, 2011)]);

	/**
	 * Anrechnungsgrund 'GÖS (GanzTag und Öffnung von Schule)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_675 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_675", 27, [new LehrerKatalogAnrechnungsgrundEintrag(675000, "675", "GÖS (GanzTag und Öffnung von Schule)", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Archivpädagogen Schülertheater Pusteblume / Koordination GU' für Stunden bei einem Lehrer
	 */
	public static readonly ID_680 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_680", 28, [new LehrerKatalogAnrechnungsgrundEintrag(680000, "680", "Archivpädagogen Schülertheater Pusteblume / Koordination GU", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Modellversuche / BLK' für Stunden bei einem Lehrer
	 */
	public static readonly ID_685 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_685", 29, [new LehrerKatalogAnrechnungsgrundEintrag(685000, "685", "Modellversuche / BLK", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Musik / Tanz / Theater' für Stunden bei einem Lehrer
	 */
	public static readonly ID_690 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_690", 30, [new LehrerKatalogAnrechnungsgrundEintrag(690000, "690", "Musik / Tanz / Theater", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Andere Lernorte' für Stunden bei einem Lehrer
	 */
	public static readonly ID_695 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_695", 31, [new LehrerKatalogAnrechnungsgrundEintrag(695000, "695", "Andere Lernorte", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Schaustellerkinder' für Stunden bei einem Lehrer
	 */
	public static readonly ID_700 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_700", 32, [new LehrerKatalogAnrechnungsgrundEintrag(700000, "700", "Schaustellerkinder", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Gesundheitserziehung' für Stunden bei einem Lehrer
	 */
	public static readonly ID_705 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_705", 33, [new LehrerKatalogAnrechnungsgrundEintrag(705000, "705", "Gesundheitserziehung", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Landerschülervertretungsverbindungslehrer / Go to School' für Stunden bei einem Lehrer
	 */
	public static readonly ID_710 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_710", 34, [new LehrerKatalogAnrechnungsgrundEintrag(710000, "710", "Landerschülervertretungsverbindungslehrer / Go to School", null, 2011)]);

	/**
	 * Anrechnungsgründe
	 *  'Stiftung (Wirtschaft und Schule / Pra WiS, Selbstständige Schule)' bzw.
	 *  'Eignungspraktikum Mentor Schule'
	 *  für Stunden bei einem Lehrer
	 */
	public static readonly ID_715 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_715", 35, [new LehrerKatalogAnrechnungsgrundEintrag(715000, "715", "Stiftung (Wirtschaft und Schule / Pra WiS, Selbstständige Schule)", null, 2011), new LehrerKatalogAnrechnungsgrundEintrag(715001, "715", "Eignungspraktikum Mentor Schule", 2013, 2017)]);

	/**
	 * Anrechnungsgrund 'Pädaudiologische Zentren und Frühförderzentren für Sehgeschädigte' für Stunden bei einem Lehrer
	 */
	public static readonly ID_720 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_720", 36, [new LehrerKatalogAnrechnungsgrundEintrag(720000, "720", "Pädaudiologische Zentren und Frühförderzentren für Sehgeschädigte", null, null)]);

	/**
	 * Anrechnungsgrund 'Tätigkeit bei EU-Geschäftsstellen' für Stunden bei einem Lehrer
	 */
	public static readonly ID_725 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_725", 37, [new LehrerKatalogAnrechnungsgrundEintrag(725000, "725", "Tätigkeit bei EU-Geschäftsstellen", null, null)]);

	/**
	 * Anrechnungsgrund 'Curriculumentwicklung / Zentrale Prüfungen' für Stunden bei einem Lehrer
	 */
	public static readonly ID_730 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_730", 38, [new LehrerKatalogAnrechnungsgrundEintrag(730000, "730", "Curriculumentwicklung / Zentrale Prüfungen", null, null)]);

	/**
	 * Anrechnungsgrund 'Förderung des Theatertreffens für behinderte Kinder und Jugendliche' für Stunden bei einem Lehrer
	 */
	public static readonly ID_735 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_735", 39, [new LehrerKatalogAnrechnungsgrundEintrag(735000, "735", "Förderung des Theatertreffens für behinderte Kinder und Jugendliche", null, null)]);

	/**
	 * Anrechnungsgrund 'Archivpädagogik' für Stunden bei einem Lehrer
	 */
	public static readonly ID_740 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_740", 40, [new LehrerKatalogAnrechnungsgrundEintrag(740000, "740", "Archivpädagogik", null, null)]);

	/**
	 * Anrechnungsgrund 'Bildungspolitische Sonderaufgaben' für Stunden bei einem Lehrer
	 */
	public static readonly ID_745 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_745", 41, [new LehrerKatalogAnrechnungsgrundEintrag(745000, "745", "Bildungspolitische Sonderaufgaben", null, null)]);

	/**
	 * Anrechnungsgrund 'Sonstiger Ausgleichsbedarf, der aus wechselnden Ausgleichs- und Mehrbedarfen finanziert ist.' für Stunden bei einem Lehrer
	 */
	public static readonly ID_750 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_750", 42, [new LehrerKatalogAnrechnungsgrundEintrag(750000, "750", "Sonstiger Ausgleichsbedarf, der aus wechselnden Ausgleichs- und Mehrbedarfen finanziert ist.", null, null)]);

	/**
	 * Anrechnungsgrund 'Tätigkeit als schulische/-r Mentor/-in: Praxissemester' für Stunden bei einem Lehrer
	 */
	public static readonly ID_765 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_765", 43, [new LehrerKatalogAnrechnungsgrundEintrag(765000, "765", "Tätigkeit als schulische/-r Mentor/-in: Praxissemester", 2013, null)]);

	/**
	 * Anrechnungsgrund 'Individuelle Beratung der Schülerinnen und Schüler im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_770 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_770", 44, [new LehrerKatalogAnrechnungsgrundEintrag(770000, "770", "Individuelle Beratung der Schülerinnen und Schüler im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Innerschulischer Transfer der Maßnahmen im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_771 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_771", 45, [new LehrerKatalogAnrechnungsgrundEintrag(771000, "771", "Innerschulischer Transfer der Maßnahmen im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Durchführung soziales Training im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_772 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_772", 46, [new LehrerKatalogAnrechnungsgrundEintrag(772000, "772", "Durchführung soziales Training im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Berufsfeld- oder fachbereichsübergreifende Berufsorientierung im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_773 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_773", 47, [new LehrerKatalogAnrechnungsgrundEintrag(773000, "773", "Berufsfeld- oder fachbereichsübergreifende Berufsorientierung im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Erweiterte Blockpraktika/Praxismodul „Berufliche Orientierung“ im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_774 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_774", 48, [new LehrerKatalogAnrechnungsgrundEintrag(774000, "774", "Erweiterte Blockpraktika/Praxismodul „Berufliche Orientierung“ im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Sprachförderung im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_775 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_775", 49, [new LehrerKatalogAnrechnungsgrundEintrag(775000, "775", "Sprachförderung im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Übergangssicherung/Datenmonitoring im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_776 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_776", 50, [new LehrerKatalogAnrechnungsgrundEintrag(776000, "776", "Übergangssicherung/Datenmonitoring im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Kooperation mit externen Partnern im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_777 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_777", 51, [new LehrerKatalogAnrechnungsgrundEintrag(777000, "777", "Kooperation mit externen Partnern im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Verwaltungstätigkeiten zur Unterstützung der Schulleitung bei den Landesaufgaben im Schulversuch Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_778 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_778", 52, [new LehrerKatalogAnrechnungsgrundEintrag(778000, "778", "Verwaltungstätigkeiten zur Unterstützung der Schulleitung bei den Landesaufgaben im Schulversuch Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Sonstige Tätigkeiten im Rahmen des Schulversuchs Talentschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_779 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_779", 53, [new LehrerKatalogAnrechnungsgrundEintrag(779000, "779", "Sonstige Tätigkeiten im Rahmen des Schulversuchs Talentschule", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Zeitbudget, Steigerung der Berufsfähigkeit (Förderschulen)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_800 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_800", 54, [new LehrerKatalogAnrechnungsgrundEintrag(800000, "800", "Zeitbudget, Steigerung der Berufsfähigkeit (Förderschulen)", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Zeitbudget, Modellvorhaben "Selbstständige Schule"' für Stunden bei einem Lehrer
	 */
	public static readonly ID_805 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_805", 55, [new LehrerKatalogAnrechnungsgrundEintrag(805000, "805", "Zeitbudget, Modellvorhaben \"Selbstständige Schule\"", null, 2007)]);

	/**
	 * Anrechnungsgrund 'Projekt: Abitur-Online' für Stunden bei einem Lehrer
	 */
	public static readonly ID_815 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_815", 56, [new LehrerKatalogAnrechnungsgrundEintrag(815000, "815", "Projekt: Abitur-Online", null, null)]);

	/**
	 * Anrechnungsgrund 'Sonstiger Ausgleichsbedarf, der aus Zeitbudget finanziert ist.' für Stunden bei einem Lehrer
	 */
	public static readonly ID_820 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_820", 57, [new LehrerKatalogAnrechnungsgrundEintrag(820000, "820", "Sonstiger Ausgleichsbedarf, der aus Zeitbudget finanziert ist.", null, null)]);

	/**
	 * Anrechnungsgrund 'Förderung lernschwacher und begabter Schülerinnen und Schüler' für Stunden bei einem Lehrer
	 */
	public static readonly ID_850 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_850", 58, [new LehrerKatalogAnrechnungsgrundEintrag(850000, "850", "Förderung lernschwacher und begabter Schülerinnen und Schüler", null, null)]);

	/**
	 * Anrechnungsgrund 'sonderpädagogischer Förderbedarf, z. B. Lese- und Rechtschreibschwächen, Lernbehinderungen' für Stunden bei einem Lehrer
	 */
	public static readonly ID_855 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_855", 59, [new LehrerKatalogAnrechnungsgrundEintrag(855000, "855", "sonderpädagogischer Förderbedarf, z. B. Lese- und Rechtschreibschwächen, Lernbehinderungen", null, null)]);

	/**
	 * Anrechnungsgrund 'Einstiegshilfen in Beruf/Ausbildung' für Stunden bei einem Lehrer
	 */
	public static readonly ID_860 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_860", 60, [new LehrerKatalogAnrechnungsgrundEintrag(860000, "860", "Einstiegshilfen in Beruf/Ausbildung", null, null)]);

	/**
	 * Anrechnungsgrund 'Landes- und Bundeswettbewerbe, Landessschülertheater ' für Stunden bei einem Lehrer
	 */
	public static readonly ID_875 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_875", 61, [new LehrerKatalogAnrechnungsgrundEintrag(875000, "875", "Landes- und Bundeswettbewerbe, Landessschülertheater", null, null)]);

	/**
	 * Anrechnungsgrund 'Nichtschüler-, Änderungs- und Feststellungsprüfungen' für Stunden bei einem Lehrer
	 */
	public static readonly ID_880 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_880", 62, [new LehrerKatalogAnrechnungsgrundEintrag(880000, "880", "Nichtschüler-, Änderungs- und Feststellungsprüfungen", null, null)]);

	/**
	 * Anrechnungsgrund 'Sonstiger Ausgleichsbedarf, der aus Rundungsgewinnen finanziert ist.' für Stunden bei einem Lehrer
	 */
	public static readonly ID_885 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_885", 63, [new LehrerKatalogAnrechnungsgrundEintrag(885000, "885", "Sonstiger Ausgleichsbedarf, der aus Rundungsgewinnen finanziert ist.", null, null)]);

	/**
	 * Anrechnungsgrund 'Tätigkeit als schulische/-r Ausbildungsbeauftragte/-r OVP' für Stunden bei einem Lehrer
	 */
	public static readonly ID_900 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_900", 64, [new LehrerKatalogAnrechnungsgrundEintrag(900000, "900", "Tätigkeit als schulische/-r Ausbildungsbeauftragte/-r OVP", null, null)]);

	/**
	 * Anrechnungsgrund 'Außerunterrichtliche Tätigkeit in der offenen Ganztagsschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_910 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_910", 65, [new LehrerKatalogAnrechnungsgrundEintrag(910000, "910", "Außerunterrichtliche Tätigkeit in der offenen Ganztagsschule", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Unterrichtsausfallstatistik' für Stunden bei einem Lehrer
	 */
	public static readonly ID_915 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_915", 66, [new LehrerKatalogAnrechnungsgrundEintrag(915000, "915", "Unterrichtsausfallstatistik", null, null)]);

	/**
	 * Anrechnungsgrund 'Einsatz als sozialpädagogische Fachkraft in der Schuleingangsphase' für Stunden bei einem Lehrer
	 */
	public static readonly ID_920 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_920", 67, [new LehrerKatalogAnrechnungsgrundEintrag(920000, "920", "Einsatz als sozialpädagogische Fachkraft in der Schuleingangsphase", null, 2019)]);

	/**
	 * Anrechnungsgrund 'Einsatz als sozialpädagogische Fachkraft' für Stunden bei einem Lehrer
	 */
	public static readonly ID_930 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_930", 68, [new LehrerKatalogAnrechnungsgrundEintrag(930000, "930", "Einsatz als sozialpädagogische Fachkraft", null, null)]);

	/**
	 * Anrechnungsgrund 'Tätigkeit als Verwaltungsassistent/ Verwaltungsassistentin' für Stunden bei einem Lehrer
	 */
	public static readonly ID_935 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_935", 69, [new LehrerKatalogAnrechnungsgrundEintrag(935000, "935", "Tätigkeit als Verwaltungsassistent/ Verwaltungsassistentin", 2008, null)]);

	/**
	 * Anrechnungsgrund 'LOGINEO: Anrechnungsstunde zur Betreuung und Pflege' für Stunden bei einem Lehrer
	 */
	public static readonly ID_936 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_936", 70, [new LehrerKatalogAnrechnungsgrundEintrag(936000, "936", "LOGINEO: Anrechnungsstunde zur Betreuung und Pflege", 2020, null)]);

	/**
	 * Anrechnungsgrund 'Betreung und Aufsicht im Ganztagsbereich der Ganztagsschulen' für Stunden bei einem Lehrer
	 */
	public static readonly ID_940 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_940", 71, [new LehrerKatalogAnrechnungsgrundEintrag(940000, "940", "Betreung und Aufsicht im Ganztagsbereich der Ganztagsschulen", null, 2011)]);

	/**
	 * Anrechnungsgrund 'Lehrerwochenstunden, die nicht verplant sind' für Stunden bei einem Lehrer
	 */
	public static readonly ID_945 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_945", 72, [new LehrerKatalogAnrechnungsgrundEintrag(945000, "945", "Lehrerwochenstunden, die nicht verplant sind", 2008, null)]);

	/**
	 * Anrechnungsgrund 'Ausgleichsbedarf, der aus Flexiblen Mitteln finanziert wird' für Stunden bei einem Lehrer
	 */
	public static readonly ID_950 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_950", 73, [new LehrerKatalogAnrechnungsgrundEintrag(950000, "950", "Ausgleichsbedarf, der aus Flexiblen Mitteln finanziert wird", null, null)]);

	/**
	 * Anrechnungsgrund 'Schulpsychologischer Dienst' für Stunden bei einem Lehrer
	 */
	public static readonly ID_955 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_955", 74, [new LehrerKatalogAnrechnungsgrundEintrag(955000, "955", "Schulpsychologischer Dienst", 2010, null)]);

	/**
	 * Anrechnungsgrund 'Pädagogische Tätigkeiten im Bereich Ganztag und Übermittagsbetreuung' für Stunden bei einem Lehrer
	 */
	public static readonly ID_960 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_960", 75, [new LehrerKatalogAnrechnungsgrundEintrag(960000, "960", "Pädagogische Tätigkeiten im Bereich Ganztag und Übermittagsbetreuung", 2011, null)]);

	/**
	 * Anrechnungsgrund 'Aufsicht im Bereich Ganztag und Übermittagsbetreuung (halbe Anrechnung)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_965 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_965", 76, [new LehrerKatalogAnrechnungsgrundEintrag(965000, "965", "Aufsicht im Bereich Ganztag und Übermittagsbetreuung (halbe Anrechnung)", 2011, null)]);

	/**
	 * Anrechnungsgrund 'sonstige nichtunterrichtliche Tätigkeiten (Nur mit Genehmigung durch die Schulaufsicht.; auch Beratungstätigkeiten für sonderpäd. Förderung)' für Stunden bei einem Lehrer
	 */
	public static readonly ID_970 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_970", 77, [new LehrerKatalogAnrechnungsgrundEintrag(970000, "970", "sonstige nichtunterrichtliche Tätigkeiten (Nur mit Genehmigung durch die Schulaufsicht.; auch Beratungstätigkeiten für sonderpäd. Förderung)", null, null)]);

	/**
	 * Anrechnungsgrund 'Begleitung Schulversuch Gemeinschaftsschule' für Stunden bei einem Lehrer
	 */
	public static readonly ID_975 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_975", 78, [new LehrerKatalogAnrechnungsgrundEintrag(975000, "975", "Begleitung Schulversuch Gemeinschaftsschule", 2011, null)]);

	/**
	 * Anrechnungsgrund 'Übergangsbegleitung im Rahmen des Langzeitpraktikums im Ausbildungskonsens KAoA' für Stunden bei einem Lehrer
	 */
	public static readonly ID_985 : LehrerAnrechnungsgrund = new LehrerAnrechnungsgrund("ID_985", 79, [new LehrerKatalogAnrechnungsgrundEintrag(985000, "985", "Übergangsbegleitung im Rahmen des Langzeitpraktikums im Ausbildungskonsens KAoA", 2015, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 2;

	/**
	 * Der aktuellen Daten des Abgangsgrundes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogAnrechnungsgrundEintrag;

	/**
	 * Die Historie mit den Einträgen des Abgangsgrundes
	 */
	public readonly historie : Array<LehrerKatalogAnrechnungsgrundEintrag>;

	/**
	 * Eine Hashmap mit allen Anrechnungsgründen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _gruendeByID : HashMap<number, LehrerAnrechnungsgrund | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Anrechnungsgründen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _gruendeByKuerzel : HashMap<string, LehrerAnrechnungsgrund | null> = new HashMap();

	/**
	 * Erzeugt einen neuen Grund in der Aufzählung.
	 *
	 * @param historie   die Historie des Anrechnungsgrundes, welches ein Array von {@link LehrerKatalogAnrechnungsgrundEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogAnrechnungsgrundEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerAnrechnungsgrund.all_values_by_ordinal.push(this);
		LehrerAnrechnungsgrund.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Anrechnungsgründe auf die zugehörigen Anrechnungsgründe
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den IDs der Anrechnungsgründe auf die zugehörigen Anrechnungsgründe
	 */
	private static getMapGruendeByID() : HashMap<number, LehrerAnrechnungsgrund | null> {
		if (LehrerAnrechnungsgrund._gruendeByID.size() === 0)
			for (let g of LehrerAnrechnungsgrund.values())
				LehrerAnrechnungsgrund._gruendeByID.put(g.daten.id, g);
		return LehrerAnrechnungsgrund._gruendeByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Anrechnungsgründe auf die zugehörigen Anrechnungsgründe
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den Kürzeln der Anrechnungsgründe auf die zugehörigen Anrechnungsgründe
	 */
	private static getMapGruendeByKuerzel() : HashMap<string, LehrerAnrechnungsgrund | null> {
		if (LehrerAnrechnungsgrund._gruendeByKuerzel.size() === 0)
			for (let g of LehrerAnrechnungsgrund.values())
				LehrerAnrechnungsgrund._gruendeByKuerzel.put(g.daten.kuerzel, g);
		return LehrerAnrechnungsgrund._gruendeByKuerzel;
	}

	/**
	 * Gibt den Anrechnungsgrund anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Anrechnungsgrundes
	 *
	 * @return der Anrechnungsgrund oder null, falls die ID ungültig ist.
	 */
	public static getByID(id : number) : LehrerAnrechnungsgrund | null {
		return LehrerAnrechnungsgrund.getMapGruendeByID().get(id);
	}

	/**
	 * Gibt den Anrechnungsgrund anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Anrechnungsgrundes
	 *
	 * @return der Anrechnungsgrund oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerAnrechnungsgrund | null {
		return LehrerAnrechnungsgrund.getMapGruendeByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof LehrerAnrechnungsgrund))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : LehrerAnrechnungsgrund) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerAnrechnungsgrund> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerAnrechnungsgrund | null {
		const tmp : LehrerAnrechnungsgrund | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.lehrer.LehrerAnrechnungsgrund'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_lehrer_LehrerAnrechnungsgrund(obj : unknown) : LehrerAnrechnungsgrund {
	return obj as LehrerAnrechnungsgrund;
}
