import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Fachgruppe } from '../../../asd/types/fach/Fachgruppe';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaMath } from '../../../java/lang/JavaMath';
import { RGBFarbe } from '../../../asd/data/RGBFarbe';
import { Jahrgaenge } from '../../../asd/types/jahrgang/Jahrgaenge';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import type { JavaMap } from '../../../java/util/JavaMap';
import { FachKatalogEintrag } from '../../../asd/data/fach/FachKatalogEintrag';

export class Fach extends JavaEnum<Fach> implements CoreType<FachKatalogEintrag, Fach> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Fach> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Fach> = new Map<string, Fach>();

	/**
	 * Fach Arbeits- und Betriebswirtschaftslehre
	 */
	public static readonly AB : Fach = new Fach("AB", 0, );

	/**
	 * Fach Neigungs- und Projektgruppen
	 */
	public static readonly AG : Fach = new Fach("AG", 1, );

	/**
	 * Fach Arbeitslehre - Schwerpunkt Hauswirtschaft
	 */
	public static readonly AH : Fach = new Fach("AH", 2, );

	/**
	 * Fach Arbeitslehre
	 */
	public static readonly AL : Fach = new Fach("AL", 3, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Arabisch
	 */
	public static readonly AM : Fach = new Fach("AM", 4, );

	/**
	 * Fach Alevitische Religionslehre nach den Grundsätzen des AABF
	 */
	public static readonly AR : Fach = new Fach("AR", 5, );

	/**
	 * Fach Arbeitslehre - Schwerpunkt Technik
	 */
	public static readonly AT : Fach = new Fach("AT", 6, );

	/**
	 * Fach Arbeitsvorbereitung
	 */
	public static readonly AV : Fach = new Fach("AV", 7, );

	/**
	 * Fach Arbeitslehre - Schwerpunkt Wirtschaft
	 */
	public static readonly AW : Fach = new Fach("AW", 8, );

	/**
	 * Fach Arbeitslehre - Technik/Wirtschaft (nur Wahlpflichtunterricht)
	 */
	public static readonly AX : Fach = new Fach("AX", 9, );

	/**
	 * Fach Arbeitslehre - Hauswirtschaft/Wirtschaft (nur Wahlpflichtunterricht)
	 */
	public static readonly AY : Fach = new Fach("AY", 10, );

	/**
	 * Fach Berufs- und Arbeitspädagogik
	 */
	public static readonly BA : Fach = new Fach("BA", 11, );

	/**
	 * Fach Bürowirtschaft
	 */
	public static readonly BF : Fach = new Fach("BF", 12, );

	/**
	 * Fach Betrieb und Gesellschaft/Politik
	 */
	public static readonly BG : Fach = new Fach("BG", 13, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bosnisch
	 */
	public static readonly BH : Fach = new Fach("BH", 14, );

	/**
	 * Fach Biologie
	 */
	public static readonly BI : Fach = new Fach("BI", 15, );

	/**
	 * Fach Betriebsinformatik
	 */
	public static readonly BK : Fach = new Fach("BK", 16, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Bosnisch
	 */
	public static readonly BM : Fach = new Fach("BM", 17, );

	/**
	 * Fach Braille'sche Punktschrift
	 */
	public static readonly BN : Fach = new Fach("BN", 18, );

	/**
	 * Fach Betriebspraxis
	 */
	public static readonly BP : Fach = new Fach("BP", 19, );

	/**
	 * Fach Betriebslehre
	 */
	public static readonly BR : Fach = new Fach("BR", 20, );

	/**
	 * Fach Betriebssoziologie / Arbeitsrecht
	 */
	public static readonly BS : Fach = new Fach("BS", 21, );

	/**
	 * Fach Berufsvorbereitung
	 */
	public static readonly BV : Fach = new Fach("BV", 22, );

	/**
	 * Fach Betriebswirtschaftslehre
	 */
	public static readonly BW : Fach = new Fach("BW", 23, );

	/**
	 * Fach Betriebssysteme / Netzwerke
	 */
	public static readonly BY : Fach = new Fach("BY", 24, );

	/**
	 * Fach Chinesisch
	 */
	public static readonly C : Fach = new Fach("C", 25, );

	/**
	 * Fach Chinesisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly C0 : Fach = new Fach("C0", 26, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly C1 : Fach = new Fach("C1", 27, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly C5 : Fach = new Fach("C5", 28, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly C6 : Fach = new Fach("C6", 29, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly C7 : Fach = new Fach("C7", 30, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly C8 : Fach = new Fach("C8", 31, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly C9 : Fach = new Fach("C9", 32, );

	/**
	 * Fach Chemie
	 */
	public static readonly CH : Fach = new Fach("CH", 33, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Kroatisch
	 */
	public static readonly CM : Fach = new Fach("CM", 34, );

	/**
	 * Fach Chinesisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly CQ : Fach = new Fach("CQ", 35, );

	/**
	 * Fach Deutsch
	 */
	public static readonly D : Fach = new Fach("D", 36, );

	/**
	 * Fach Datenbanken
	 */
	public static readonly DB : Fach = new Fach("DB", 37, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Rumänisch
	 */
	public static readonly DH : Fach = new Fach("DH", 38, );

	/**
	 * Fach Didaktik u. Methodik der soz.päd. Praxis mit Übungen
	 */
	public static readonly DM : Fach = new Fach("DM", 39, );

	/**
	 * Fach Darstellen und Gestalten
	 */
	public static readonly DS : Fach = new Fach("DS", 40, );

	/**
	 * Fach Datenverarbeitung
	 */
	public static readonly DV : Fach = new Fach("DV", 41, );

	/**
	 * Fach Englisch
	 */
	public static readonly E : Fach = new Fach("E", 42, );

	/**
	 * Fach Erweitertes Bildungsangebot
	 */
	public static readonly EB : Fach = new Fach("EB", 43, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Serbisch
	 */
	public static readonly EH : Fach = new Fach("EH", 44, );

	/**
	 * Fach Erdkunde/ Geographie
	 */
	public static readonly EK : Fach = new Fach("EK", 45, );

	/**
	 * Fach Ernährungslehre
	 */
	public static readonly EL : Fach = new Fach("EL", 46, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Serbisch
	 */
	public static readonly EM : Fach = new Fach("EM", 47, );

	/**
	 * Fach Evangelische Religionslehre (konfessionell kooperativ)
	 */
	public static readonly EN : Fach = new Fach("EN", 48, );

	/**
	 * Fach Evangelische Religionslehre
	 */
	public static readonly ER : Fach = new Fach("ER", 49, );

	/**
	 * Fach Französisch
	 */
	public static readonly F : Fach = new Fach("F", 50, );

	/**
	 * Fach Französisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly F0 : Fach = new Fach("F0", 51, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly F1 : Fach = new Fach("F1", 52, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly F5 : Fach = new Fach("F5", 53, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly F6 : Fach = new Fach("F6", 54, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly F7 : Fach = new Fach("F7", 55, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly F8 : Fach = new Fach("F8", 56, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly F9 : Fach = new Fach("F9", 57, );

	/**
	 * Fach Fächer des berufsbezogenen Bereichs
	 */
	public static readonly FB : Fach = new Fach("FB", 58, );

	/**
	 * Fach Fremdsprachliche Kommunikation
	 */
	public static readonly FK : Fach = new Fach("FK", 59, );

	/**
	 * Fach Fachpraxis
	 */
	public static readonly FP : Fach = new Fach("FP", 60, );

	/**
	 * Fach Französisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly FQ : Fach = new Fach("FQ", 61, );

	/**
	 * Fach Förderunterricht
	 */
	public static readonly FU : Fach = new Fach("FU", 62, );

	/**
	 * Fach Griechisch
	 */
	public static readonly G : Fach = new Fach("G", 63, );

	/**
	 * Fach Griechisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly G0 : Fach = new Fach("G0", 64, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly G1 : Fach = new Fach("G1", 65, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly G5 : Fach = new Fach("G5", 66, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly G6 : Fach = new Fach("G6", 67, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly G7 : Fach = new Fach("G7", 68, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly G8 : Fach = new Fach("G8", 69, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly G9 : Fach = new Fach("G9", 70, );

	/**
	 * Fach Geräte- und Maschinenlehre
	 */
	public static readonly GA : Fach = new Fach("GA", 71, );

	/**
	 * Fach Grundbildung
	 */
	public static readonly GB : Fach = new Fach("GB", 72, );

	/**
	 * Fach Geschichte
	 */
	public static readonly GE : Fach = new Fach("GE", 73, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Neugriechisch
	 */
	public static readonly GH : Fach = new Fach("GH", 74, );

	/**
	 * Fach Gesellschaftslehre
	 */
	public static readonly GL : Fach = new Fach("GL", 75, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Neugriechisch
	 */
	public static readonly GM : Fach = new Fach("GM", 76, );

	/**
	 * Fach Geologie (Oberstufenkolleg Bielefeld)
	 */
	public static readonly GO : Fach = new Fach("GO", 77, );

	/**
	 * Fach Geschichte / Politik
	 */
	public static readonly GP : Fach = new Fach("GP", 78, );

	/**
	 * Fach Altgriechisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly GQ : Fach = new Fach("GQ", 79, );

	/**
	 * Fach Gestaltungslehre
	 */
	public static readonly GS : Fach = new Fach("GS", 80, );

	/**
	 * Fach Gesamtunterricht (nur für Förderschulkindergarten)
	 */
	public static readonly GU : Fach = new Fach("GU", 81, );

	/**
	 * Fach Geschichte und Sozialwissenschaft
	 */
	public static readonly GW : Fach = new Fach("GW", 82, );

	/**
	 * Fach Hebräisch
	 */
	public static readonly H : Fach = new Fach("H", 83, );

	/**
	 * Fach Hebräisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly H0 : Fach = new Fach("H0", 84, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly H1 : Fach = new Fach("H1", 85, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly H5 : Fach = new Fach("H5", 86, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly H6 : Fach = new Fach("H6", 87, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly H7 : Fach = new Fach("H7", 88, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly H8 : Fach = new Fach("H8", 89, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly H9 : Fach = new Fach("H9", 90, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kroatisch
	 */
	public static readonly HH : Fach = new Fach("HH", 91, );

	/**
	 * Fach Hebräisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly HQ : Fach = new Fach("HQ", 92, );

	/**
	 * Fach Jüdische Religionslehre
	 */
	public static readonly HR : Fach = new Fach("HR", 93, );

	/**
	 * Fach Hausunterricht
	 */
	public static readonly HU : Fach = new Fach("HU", 94, );

	/**
	 * Fach Hauswirtschaft
	 */
	public static readonly HW : Fach = new Fach("HW", 95, );

	/**
	 * Fach Hygiene
	 */
	public static readonly HY : Fach = new Fach("HY", 96, );

	/**
	 * Fach Italienisch
	 */
	public static readonly I : Fach = new Fach("I", 97, );

	/**
	 * Fach Italienisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly I0 : Fach = new Fach("I0", 98, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly I1 : Fach = new Fach("I1", 99, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly I5 : Fach = new Fach("I5", 100, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly I6 : Fach = new Fach("I6", 101, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly I7 : Fach = new Fach("I7", 102, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly I8 : Fach = new Fach("I8", 103, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly I9 : Fach = new Fach("I9", 104, );

	/**
	 * Fach Individuelles Lernen (Ergänzungsstunden, Ganztag- und Betreuungsangebote)
	 */
	public static readonly IE : Fach = new Fach("IE", 105, );

	/**
	 * Fach Informatik
	 */
	public static readonly IF : Fach = new Fach("IF", 106, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Italienisch
	 */
	public static readonly IH : Fach = new Fach("IH", 107, );

	/**
	 * Fach Individuelles Lernen (dem Kernstundenkontingent entnommen)
	 */
	public static readonly IK : Fach = new Fach("IK", 108, );

	/**
	 * Fach Islamische Religionslehre
	 */
	public static readonly IL : Fach = new Fach("IL", 109, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Italienisch
	 */
	public static readonly IM : Fach = new Fach("IM", 110, );

	/**
	 * Fach Instrumentalpraktischer Grundkurs
	 */
	public static readonly IN : Fach = new Fach("IN", 111, );

	/**
	 * Fach Italienisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly IQ : Fach = new Fach("IQ", 112, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Niederländisch
	 */
	public static readonly JH : Fach = new Fach("JH", 113, );

	/**
	 * Fach Japanisch
	 */
	public static readonly K : Fach = new Fach("K", 114, );

	/**
	 * Fach Japanisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly K0 : Fach = new Fach("K0", 115, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly K1 : Fach = new Fach("K1", 116, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly K5 : Fach = new Fach("K5", 117, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly K6 : Fach = new Fach("K6", 118, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly K7 : Fach = new Fach("K7", 119, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly K8 : Fach = new Fach("K8", 120, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly K9 : Fach = new Fach("K9", 121, );

	/**
	 * Fach Katholische Religionslehre (konfessionell kooperativ)
	 */
	public static readonly KN : Fach = new Fach("KN", 122, );

	/**
	 * Fach Kommunikation
	 */
	public static readonly KO : Fach = new Fach("KO", 123, );

	/**
	 * Fach Japanisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly KQ : Fach = new Fach("KQ", 124, );

	/**
	 * Fach Katholische Religionslehre
	 */
	public static readonly KR : Fach = new Fach("KR", 125, );

	/**
	 * Fach Kurzschrift
	 */
	public static readonly KS : Fach = new Fach("KS", 126, );

	/**
	 * Fach Kunst
	 */
	public static readonly KU : Fach = new Fach("KU", 127, );

	/**
	 * Fach Lateinisch
	 */
	public static readonly L : Fach = new Fach("L", 128, );

	/**
	 * Fach Lateinisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly L0 : Fach = new Fach("L0", 129, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly L1 : Fach = new Fach("L1", 130, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly L5 : Fach = new Fach("L5", 131, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly L6 : Fach = new Fach("L6", 132, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly L7 : Fach = new Fach("L7", 133, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly L8 : Fach = new Fach("L8", 134, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly L9 : Fach = new Fach("L9", 135, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Albanisch
	 */
	public static readonly LH : Fach = new Fach("LH", 136, );

	/**
	 * Fach Literatur
	 */
	public static readonly LI : Fach = new Fach("LI", 137, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Albanisch
	 */
	public static readonly LM : Fach = new Fach("LM", 138, );

	/**
	 * Fach Lateinisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly LQ : Fach = new Fach("LQ", 139, );

	/**
	 * Fach Mathematik
	 */
	public static readonly M : Fach = new Fach("M", 140, );

	/**
	 * Fach Religionsunterricht der mennonitischen Brüdergemeinden in NRW als Schulversuch
	 */
	public static readonly MB : Fach = new Fach("MB", 141, );

	/**
	 * Fach Medienerziehung
	 */
	public static readonly MD : Fach = new Fach("MD", 142, );

	/**
	 * Fach Mechanik
	 */
	public static readonly ME : Fach = new Fach("ME", 143, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Mazedonisch
	 */
	public static readonly MH : Fach = new Fach("MH", 144, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Mazedonisch
	 */
	public static readonly MM : Fach = new Fach("MM", 145, );

	/**
	 * Fach Meß- und Prüftechnik
	 */
	public static readonly MP : Fach = new Fach("MP", 146, );

	/**
	 * Fach Musik
	 */
	public static readonly MU : Fach = new Fach("MU", 147, );

	/**
	 * Fach Methodik des wissenschaftl. orientierten Arbeitens
	 */
	public static readonly MW : Fach = new Fach("MW", 148, );

	/**
	 * Fach Spezielle sonderpädagogische Maßnahme
	 */
	public static readonly MX : Fach = new Fach("MX", 149, );

	/**
	 * Fach Fächerübergreifender Unterricht in SI und SII (Berufspraxisstufe)
	 */
	public static readonly MY : Fach = new Fach("MY", 150, );

	/**
	 * Fach Niederländisch
	 */
	public static readonly N : Fach = new Fach("N", 151, );

	/**
	 * Fach Niederländisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly N0 : Fach = new Fach("N0", 152, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly N1 : Fach = new Fach("N1", 153, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly N5 : Fach = new Fach("N5", 154, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly N6 : Fach = new Fach("N6", 155, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly N7 : Fach = new Fach("N7", 156, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly N8 : Fach = new Fach("N8", 157, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly N9 : Fach = new Fach("N9", 158, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Polnisch
	 */
	public static readonly NH : Fach = new Fach("NH", 159, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Niederländisch
	 */
	public static readonly NM : Fach = new Fach("NM", 160, );

	/**
	 * Fach Niederländisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly NQ : Fach = new Fach("NQ", 161, );

	/**
	 * Fach Naturwissenschaften
	 */
	public static readonly NW : Fach = new Fach("NW", 162, );

	/**
	 * Fach Portugiesisch
	 */
	public static readonly O : Fach = new Fach("O", 163, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly O0 : Fach = new Fach("O0", 164, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly O1 : Fach = new Fach("O1", 165, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly O5 : Fach = new Fach("O5", 166, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly O6 : Fach = new Fach("O6", 167, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly O7 : Fach = new Fach("O7", 168, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly O8 : Fach = new Fach("O8", 169, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly O9 : Fach = new Fach("O9", 170, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Portugiesisch
	 */
	public static readonly OH : Fach = new Fach("OH", 171, );

	/**
	 * Fach Organisationslehre/Büroorganisation
	 */
	public static readonly OL : Fach = new Fach("OL", 172, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Portugiesisch
	 */
	public static readonly OM : Fach = new Fach("OM", 173, );

	/**
	 * Fach Portugiesisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly OQ : Fach = new Fach("OQ", 174, );

	/**
	 * Fach Orthodoxe Religionslehre
	 */
	public static readonly OR : Fach = new Fach("OR", 175, );

	/**
	 * Fach Pädagogik/ Erziehungswissenschaft
	 */
	public static readonly PA : Fach = new Fach("PA", 176, );

	/**
	 * Fach Physik
	 */
	public static readonly PH : Fach = new Fach("PH", 177, );

	/**
	 * Fach Politik
	 */
	public static readonly PK : Fach = new Fach("PK", 178, );

	/**
	 * Fach Philosophie
	 */
	public static readonly PL : Fach = new Fach("PL", 179, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Polnisch
	 */
	public static readonly PM : Fach = new Fach("PM", 180, );

	/**
	 * Fach Praktische Philosophie
	 */
	public static readonly PP : Fach = new Fach("PP", 181, );

	/**
	 * Fach Psychologie
	 */
	public static readonly PS : Fach = new Fach("PS", 182, );

	/**
	 * Fach Fachübergreifender Unterricht
	 */
	public static readonly PU : Fach = new Fach("PU", 183, );

	/**
	 * Fach Personalwirtschaft und Soziologie/Politik
	 */
	public static readonly PW : Fach = new Fach("PW", 184, );

	/**
	 * Fach Projektkurs (mit einem oder zwei Leitfächern)
	 */
	public static readonly PX : Fach = new Fach("PX", 185, );

	/**
	 * Fach Politik/Ökonomische Grundbildung
	 */
	public static readonly POE : Fach = new Fach("POE", 186, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Farsi
	 */
	public static readonly QH : Fach = new Fach("QH", 187, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Farsi
	 */
	public static readonly QM : Fach = new Fach("QM", 188, );

	/**
	 * Fach Russisch
	 */
	public static readonly R : Fach = new Fach("R", 189, );

	/**
	 * Fach Russisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly R0 : Fach = new Fach("R0", 190, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly R1 : Fach = new Fach("R1", 191, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly R5 : Fach = new Fach("R5", 192, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly R6 : Fach = new Fach("R6", 193, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly R7 : Fach = new Fach("R7", 194, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly R8 : Fach = new Fach("R8", 195, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly R9 : Fach = new Fach("R9", 196, );

	/**
	 * Fach Rechnungswesen
	 */
	public static readonly RE : Fach = new Fach("RE", 197, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch
	 */
	public static readonly RH : Fach = new Fach("RH", 198, );

	/**
	 * Fach Rechtskunde
	 */
	public static readonly RK : Fach = new Fach("RK", 199, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Russisch
	 */
	public static readonly RM : Fach = new Fach("RM", 200, );

	/**
	 * Fach Russisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly RQ : Fach = new Fach("RQ", 201, );

	/**
	 * Fach Recht und Verwaltung
	 */
	public static readonly RW : Fach = new Fach("RW", 202, );

	/**
	 * Fach Spanisch
	 */
	public static readonly S : Fach = new Fach("S", 203, );

	/**
	 * Fach Spanisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly S0 : Fach = new Fach("S0", 204, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly S1 : Fach = new Fach("S1", 205, );

	/**
	 * Fach Sport für Jungen
	 */
	public static readonly S3 : Fach = new Fach("S3", 206, );

	/**
	 * Fach Sport für Mädchen
	 */
	public static readonly S4 : Fach = new Fach("S4", 207, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly S5 : Fach = new Fach("S5", 208, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly S6 : Fach = new Fach("S6", 209, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly S7 : Fach = new Fach("S7", 210, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly S8 : Fach = new Fach("S8", 211, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly S9 : Fach = new Fach("S9", 212, );

	/**
	 * Fach Softwareentwicklung und -engineering
	 */
	public static readonly SE : Fach = new Fach("SE", 213, );

	/**
	 * Fach sonderpädag. Förderung für Schüler/-innen mit sonderpädag. Förderbedarf
	 */
	public static readonly SG : Fach = new Fach("SG", 214, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Spanisch
	 */
	public static readonly SH : Fach = new Fach("SH", 215, );

	/**
	 * Fach Spiel
	 */
	public static readonly SI : Fach = new Fach("SI", 216, );

	/**
	 * Fach Soziologie
	 */
	public static readonly SL : Fach = new Fach("SL", 217, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Spanisch
	 */
	public static readonly SM : Fach = new Fach("SM", 218, );

	/**
	 * Fach Sport
	 */
	public static readonly SP : Fach = new Fach("SP", 219, );

	/**
	 * Fach Spanisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly SQ : Fach = new Fach("SQ", 220, );

	/**
	 * Fach Sonstige Fremdsprachen
	 */
	public static readonly SR : Fach = new Fach("SR", 221, );

	/**
	 * Fach Sachunterricht
	 */
	public static readonly SU : Fach = new Fach("SU", 222, );

	/**
	 * Fach Sozialwissenschaften
	 */
	public static readonly SW : Fach = new Fach("SW", 223, );

	/**
	 * Fach Sozialwissenschaften/Wirtschaft
	 */
	public static readonly SZ : Fach = new Fach("SZ", 224, );

	/**
	 * Fach Türkisch
	 */
	public static readonly T : Fach = new Fach("T", 225, );

	/**
	 * Fach Türkisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly T0 : Fach = new Fach("T0", 226, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly T1 : Fach = new Fach("T1", 227, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly T5 : Fach = new Fach("T5", 228, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly T6 : Fach = new Fach("T6", 229, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly T7 : Fach = new Fach("T7", 230, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly T8 : Fach = new Fach("T8", 231, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly T9 : Fach = new Fach("T9", 232, );

	/**
	 * Fach Technik
	 */
	public static readonly TC : Fach = new Fach("TC", 233, );

	/**
	 * Fach Technische Grundbildung
	 */
	public static readonly TG : Fach = new Fach("TG", 234, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Türkisch
	 */
	public static readonly TH : Fach = new Fach("TH", 235, );

	/**
	 * Fach Technische Informatik
	 */
	public static readonly TI : Fach = new Fach("TI", 236, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Türkisch
	 */
	public static readonly TM : Fach = new Fach("TM", 237, );

	/**
	 * Fach Türkisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly TQ : Fach = new Fach("TQ", 238, );

	/**
	 * Fach Textverarbeitung
	 */
	public static readonly TV : Fach = new Fach("TV", 239, );

	/**
	 * Fach Textilgestaltung
	 */
	public static readonly TX : Fach = new Fach("TX", 240, );

	/**
	 * Fach Technisches Zeichnen / Fachzeichnen
	 */
	public static readonly TZ : Fach = new Fach("TZ", 241, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch
	 */
	public static readonly UH : Fach = new Fach("UH", 242, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Rumänisch
	 */
	public static readonly UM : Fach = new Fach("UM", 243, );

	/**
	 * Fach fächerübergreifender Unterricht (Sprache/Sachunt./Mathematik/Förderunt.)
	 */
	public static readonly UU : Fach = new Fach("UU", 244, );

	/**
	 * Fach Unterweisung
	 */
	public static readonly UW : Fach = new Fach("UW", 245, );

	/**
	 * Fach Verwaltungskunde
	 */
	public static readonly VE : Fach = new Fach("VE", 246, );

	/**
	 * Fach Fächer im genehmigten Schulversuch und sonstige Fächer
	 */
	public static readonly VF : Fach = new Fach("VF", 247, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bulgarisch
	 */
	public static readonly VH : Fach = new Fach("VH", 248, );

	/**
	 * Fach Verkaufskunde
	 */
	public static readonly VK : Fach = new Fach("VK", 249, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Bulgarisch
	 */
	public static readonly VM : Fach = new Fach("VM", 250, );

	/**
	 * Fach vokalpraktischer Grundkurs
	 */
	public static readonly VO : Fach = new Fach("VO", 251, );

	/**
	 * Fach Volkswirtschaftslehre/Politik
	 */
	public static readonly VP : Fach = new Fach("VP", 252, );

	/**
	 * Fach Verfahrenstechnik
	 */
	public static readonly VT : Fach = new Fach("VT", 253, );

	/**
	 * Fach Volkswirtschaftslehre
	 */
	public static readonly VW : Fach = new Fach("VW", 254, );

	/**
	 * Fach Vertiefungsfach
	 */
	public static readonly VX : Fach = new Fach("VX", 255, );

	/**
	 * Fach Werken
	 */
	public static readonly W : Fach = new Fach("W", 256, );

	/**
	 * Fach Wirtschaftsgeographie
	 */
	public static readonly WG : Fach = new Fach("WG", 257, );

	/**
	 * Fach Wirtschaftsinformatik/Organisationslehre
	 */
	public static readonly WI : Fach = new Fach("WI", 258, );

	/**
	 * Fach Wirtschaft-Politik
	 */
	public static readonly WP : Fach = new Fach("WP", 259, );

	/**
	 * Fach Wirtschaftslehre
	 */
	public static readonly WW : Fach = new Fach("WW", 260, );

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Hauswirtschaft
	 */
	public static readonly WX : Fach = new Fach("WX", 261, );

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Technik
	 */
	public static readonly WY : Fach = new Fach("WY", 262, );

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Wirtschaft
	 */
	public static readonly WZ : Fach = new Fach("WZ", 263, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - sonstige Sprache
	 */
	public static readonly XH : Fach = new Fach("XH", 264, );

	/**
	 * Fach Unterricht in der Herkunftssprache - sonstige Sprache
	 */
	public static readonly XM : Fach = new Fach("XM", 265, );

	/**
	 * Fach Sonstige Fächer ohne Fremdsprachen (kein Import nach ASDPC)
	 */
	public static readonly XX : Fach = new Fach("XX", 266, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Koreanisch
	 */
	public static readonly YH : Fach = new Fach("YH", 267, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Koreanisch
	 */
	public static readonly YM : Fach = new Fach("YM", 268, );

	/**
	 * Fach orthodoxe Religionslehre (Syrisch)
	 */
	public static readonly YR : Fach = new Fach("YR", 269, );

	/**
	 * Fach Neugriechisch
	 */
	public static readonly Z : Fach = new Fach("Z", 270, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly Z0 : Fach = new Fach("Z0", 271, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly Z1 : Fach = new Fach("Z1", 272, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly Z5 : Fach = new Fach("Z5", 273, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly Z6 : Fach = new Fach("Z6", 274, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly Z7 : Fach = new Fach("Z7", 275, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly Z8 : Fach = new Fach("Z8", 276, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly Z9 : Fach = new Fach("Z9", 277, );

	/**
	 * Fach Zusätzliche Förderung
	 */
	public static readonly ZF : Fach = new Fach("ZF", 278, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kurdische Sprachen
	 */
	public static readonly ZH : Fach = new Fach("ZH", 279, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Kurdische Sprachen (Sorani, Komanci, Zaza)
	 */
	public static readonly ZM : Fach = new Fach("ZM", 280, );

	/**
	 * Fach Neugriechisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly ZQ : Fach = new Fach("ZQ", 281, );

	/**
	 * Fach Zeichnen / Werken
	 */
	public static readonly ZW : Fach = new Fach("ZW", 282, );

	/**
	 * Eine Liste mit allen atomaren Kürzeln von Fremdsprachen
	 */
	private static readonly _mapFremdsprachenKuerzelListe : HashMap<number, List<string>> = new HashMap<number, List<string>>();

	/**
	 * Eine HashMap mit den zulässigen Fremdsprachen-Fächern. Der Zugriff erfolgt dabei über das atomare Kürzel des Faches. Sie enthält nur das Fach, wo das atomare Kürzel mit dem Statistik-Kürzel übereinstimmt.
	 */
	private static readonly _mapFremdsprachenKuerzelAtomar : HashMap<number, JavaMap<string, Fach>> = new HashMap<number, JavaMap<string, Fach>>();

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Fach.all_values_by_ordinal.push(this);
		Fach.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<FachKatalogEintrag, Fach>) : void {
		CoreTypeDataManager.putManager(Fach.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<FachKatalogEintrag, Fach> {
		return CoreTypeDataManager.getManager(Fach.class);
	}

	/**
	 * Gibt die Liste aller atomaren Kürzeln von Fremdsprachen-Fächern zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @param schuljahr		das Schuljahr, für das die Liste benötigt wird.
	 * @return die Liste aller atomaren Kürzeln von Fremdsprachen-Fächern
	 */
	public static getListFremdsprachenKuerzelAtomar(schuljahr : number) : List<string> {
		let listFremdsprachen : List<string> | null = Fach._mapFremdsprachenKuerzelListe.get(schuljahr);
		if (listFremdsprachen === null) {
			listFremdsprachen = new ArrayList();
			Fach._mapFremdsprachenKuerzelListe.put(schuljahr, listFremdsprachen);
		}
		if (listFremdsprachen.isEmpty()) {
			for (const f of Fach.values()) {
				const fke : FachKatalogEintrag | null = f.daten(schuljahr);
				if (fke !== null && fke.istFremdsprache && JavaObject.equalsTranspiler(fke.schluessel, (fke.kuerzel)))
					listFremdsprachen.add(fke.kuerzel);
			}
		}
		return listFremdsprachen;
	}

	/**
	 * Gibt eine Map von den atomaren Kürzeln der Fremdsprachen auf eine Liste der zugehörigen
	 * Sprach-Fächer zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @param schuljahr		das Schuljahr, für das die Liste benötigt wird.
	 * @return die Map von den atomaren Kürzeln der Fremdsprachen auf eine Liste der zugehörigen Sprach-Fächer
	 */
	public static getMapFremdsprachenKuerzelAtomar(schuljahr : number) : JavaMap<string, Fach> {
		let mapFremdsprachen : JavaMap<string, Fach> | null = Fach._mapFremdsprachenKuerzelAtomar.get(schuljahr);
		if (mapFremdsprachen === null) {
			mapFremdsprachen = new HashMap();
			Fach._mapFremdsprachenKuerzelAtomar.put(schuljahr, mapFremdsprachen);
		}
		if (mapFremdsprachen.isEmpty()) {
			for (const f of Fach.values()) {
				const fke : FachKatalogEintrag | null = f.daten(schuljahr!);
				if (fke !== null && fke.istFremdsprache && JavaObject.equalsTranspiler(fke.schluessel, (fke.kuerzel)))
					mapFremdsprachen.put(fke.kuerzel, f);
			}
		}
		return mapFremdsprachen;
	}

	/**
	 * Gibt die Fachgruppe zurück, der das Fach in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Fachgruppe oder null, falls keine Zuordnung vorliegt
	 */
	public getFachgruppe(schuljahr : number) : Fachgruppe | null {
		const fke : FachKatalogEintrag | null = Fach.data().getEintragBySchuljahrUndWert(schuljahr, this);
		if ((fke === null) || (fke.fachgruppe === null))
			return null;
		return Fachgruppe.data().getWertByBezeichner(fke.fachgruppe);
	}

	/**
	 * Gibt den Jahrgang zurück, ab wann dieses Faches in dem angegebenen Schuljahr zulässig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Jahrgang
	 */
	public getJahrgangAb(schuljahr : number) : Jahrgaenge | null {
		const fke : FachKatalogEintrag | null = Fach.data().getEintragBySchuljahrUndWert(schuljahr, this);
		if ((fke === null) || (fke.abJahrgang === null))
			return null;
		return Jahrgaenge.data().getWertByBezeichner(fke.abJahrgang);
	}

	/**
	 * Gibt die Farbe des zulässigen Faches zurück.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage bezieht
	 *
	 * @return die Farbe des zulässigen Faches
	 */
	public getFarbe(schuljahr : number) : RGBFarbe {
		const gruppe : Fachgruppe | null = this.getFachgruppe(schuljahr);
		if (gruppe === null)
			return new RGBFarbe();
		return gruppe.getFarbe(schuljahr);
	}

	/**
	 * Gibt die HTML-Farbe des zulässigen Faches als Aufruf der rgb-Funktion
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage bezieht
	 *
	 * @return die RGB-HTML-Farbdefinition als String
	 */
	public getHMTLFarbeRGB(schuljahr : number) : string {
		const farbe : RGBFarbe = this.getFarbe(schuljahr);
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}

	/**
	 * Gibt die HTML-Farbe des zulässigen Faches als Aufruf der rgba-Funktion
	 * mit der übergebenen Transparenz zurück.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage bezieht
	 * @param alpha   gibt die Deckkraft der Farbe an
	 *
	 * @return die RGBA-HTML-Farbdefinition als String
	 */
	public getHMTLFarbeRGBA(schuljahr : number, alpha : number) : string {
		const farbe : RGBFarbe = this.getFarbe(schuljahr);
		const a : number = JavaMath.clamp(alpha, 0.0, 1.0);
		return "rgba(" + farbe.red + "," + farbe.green + "," + farbe.blue + ", " + a + ")";
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Fach> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Fach | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<FachKatalogEintrag, Fach> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : FachKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<FachKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.fach.Fach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.fach.Fach', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Fach>('de.svws_nrw.asd.types.fach.Fach');

}

export function cast_de_svws_nrw_asd_types_fach_Fach(obj : unknown) : Fach {
	return obj as Fach;
}
