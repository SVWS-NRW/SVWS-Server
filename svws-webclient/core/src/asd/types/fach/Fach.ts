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
	 * Fach Unterricht in der Herkunftssprache - Aramäisch
	 */
	public static readonly AN : Fach = new Fach("AN", 5, );

	/**
	 * Fach Alevitische Religionslehre nach den Grundsätzen des AABF
	 */
	public static readonly AR : Fach = new Fach("AR", 6, );

	/**
	 * Fach Arbeitslehre - Schwerpunkt Technik
	 */
	public static readonly AT : Fach = new Fach("AT", 7, );

	/**
	 * Fach Arbeitsvorbereitung
	 */
	public static readonly AV : Fach = new Fach("AV", 8, );

	/**
	 * Fach Arbeitslehre - Schwerpunkt Wirtschaft
	 */
	public static readonly AW : Fach = new Fach("AW", 9, );

	/**
	 * Fach Arbeitslehre - Technik/Wirtschaft (nur Wahlpflichtunterricht)
	 */
	public static readonly AX : Fach = new Fach("AX", 10, );

	/**
	 * Fach Arbeitslehre - Hauswirtschaft/Wirtschaft (nur Wahlpflichtunterricht)
	 */
	public static readonly AY : Fach = new Fach("AY", 11, );

	/**
	 * Fach Berufs- und Arbeitspädagogik
	 */
	public static readonly BA : Fach = new Fach("BA", 12, );

	/**
	 * Fach Bürowirtschaft
	 */
	public static readonly BF : Fach = new Fach("BF", 13, );

	/**
	 * Fach Betrieb und Gesellschaft/Politik
	 */
	public static readonly BG : Fach = new Fach("BG", 14, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bosnisch
	 */
	public static readonly BH : Fach = new Fach("BH", 15, );

	/**
	 * Fach Biologie
	 */
	public static readonly BI : Fach = new Fach("BI", 16, );

	/**
	 * Fach Betriebsinformatik
	 */
	public static readonly BK : Fach = new Fach("BK", 17, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Bosnisch
	 */
	public static readonly BM : Fach = new Fach("BM", 18, );

	/**
	 * Fach Braille'sche Punktschrift
	 */
	public static readonly BN : Fach = new Fach("BN", 19, );

	/**
	 * Fach Betriebspraxis
	 */
	public static readonly BP : Fach = new Fach("BP", 20, );

	/**
	 * Fach Betriebslehre
	 */
	public static readonly BR : Fach = new Fach("BR", 21, );

	/**
	 * Fach Betriebssoziologie / Arbeitsrecht
	 */
	public static readonly BS : Fach = new Fach("BS", 22, );

	/**
	 * Fach Berufsvorbereitung
	 */
	public static readonly BV : Fach = new Fach("BV", 23, );

	/**
	 * Fach Betriebswirtschaftslehre
	 */
	public static readonly BW : Fach = new Fach("BW", 24, );

	/**
	 * Fach Betriebssysteme / Netzwerke
	 */
	public static readonly BY : Fach = new Fach("BY", 25, );

	/**
	 * Fach Chinesisch
	 */
	public static readonly C : Fach = new Fach("C", 26, );

	/**
	 * Fach Chinesisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly C0 : Fach = new Fach("C0", 27, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly C1 : Fach = new Fach("C1", 28, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly C5 : Fach = new Fach("C5", 29, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly C6 : Fach = new Fach("C6", 30, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly C7 : Fach = new Fach("C7", 31, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly C8 : Fach = new Fach("C8", 32, );

	/**
	 * Fach Chinesisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly C9 : Fach = new Fach("C9", 33, );

	/**
	 * Fach Chemie
	 */
	public static readonly CH : Fach = new Fach("CH", 34, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Kroatisch
	 */
	public static readonly CM : Fach = new Fach("CM", 35, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Chinesisch
	 */
	public static readonly CN : Fach = new Fach("CN", 36, );

	/**
	 * Fach Chinesisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly CQ : Fach = new Fach("CQ", 37, );

	/**
	 * Fach Deutsch
	 */
	public static readonly D : Fach = new Fach("D", 38, );

	/**
	 * Fach Datenbanken
	 */
	public static readonly DB : Fach = new Fach("DB", 39, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Rumänisch
	 */
	public static readonly DH : Fach = new Fach("DH", 40, );

	/**
	 * Fach Didaktik u. Methodik der soz.päd. Praxis mit Übungen
	 */
	public static readonly DM : Fach = new Fach("DM", 41, );

	/**
	 * Fach Darstellen und Gestalten
	 */
	public static readonly DS : Fach = new Fach("DS", 42, );

	/**
	 * Fach Datenverarbeitung
	 */
	public static readonly DV : Fach = new Fach("DV", 43, );

	/**
	 * Fach Englisch
	 */
	public static readonly E : Fach = new Fach("E", 44, );

	/**
	 * Fach Erweitertes Bildungsangebot
	 */
	public static readonly EB : Fach = new Fach("EB", 45, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Serbisch
	 */
	public static readonly EH : Fach = new Fach("EH", 46, );

	/**
	 * Fach Erdkunde/ Geographie
	 */
	public static readonly EK : Fach = new Fach("EK", 47, );

	/**
	 * Fach Ernährungslehre
	 */
	public static readonly EL : Fach = new Fach("EL", 48, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Serbisch
	 */
	public static readonly EM : Fach = new Fach("EM", 49, );

	/**
	 * Fach Evangelische Religionslehre (konfessionell kooperativ)
	 */
	public static readonly EN : Fach = new Fach("EN", 50, );

	/**
	 * Fach Evangelische Religionslehre
	 */
	public static readonly ER : Fach = new Fach("ER", 51, );

	/**
	 * Fach Französisch
	 */
	public static readonly F : Fach = new Fach("F", 52, );

	/**
	 * Fach Französisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly F0 : Fach = new Fach("F0", 53, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly F1 : Fach = new Fach("F1", 54, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly F5 : Fach = new Fach("F5", 55, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly F6 : Fach = new Fach("F6", 56, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly F7 : Fach = new Fach("F7", 57, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly F8 : Fach = new Fach("F8", 58, );

	/**
	 * Fach Französisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly F9 : Fach = new Fach("F9", 59, );

	/**
	 * Fach Fächer des berufsbezogenen Bereichs
	 */
	public static readonly FB : Fach = new Fach("FB", 60, );

	/**
	 * Fach Fremdsprachliche Kommunikation
	 */
	public static readonly FK : Fach = new Fach("FK", 61, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Französisch
	 */
	public static readonly FM : Fach = new Fach("FM", 62, );

	/**
	 * Fach Fachpraxis
	 */
	public static readonly FP : Fach = new Fach("FP", 63, );

	/**
	 * Fach Französisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly FQ : Fach = new Fach("FQ", 64, );

	/**
	 * Fach Förderunterricht
	 */
	public static readonly FU : Fach = new Fach("FU", 65, );

	/**
	 * Fach Griechisch
	 */
	public static readonly G : Fach = new Fach("G", 66, );

	/**
	 * Fach Griechisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly G0 : Fach = new Fach("G0", 67, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly G1 : Fach = new Fach("G1", 68, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly G5 : Fach = new Fach("G5", 69, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly G6 : Fach = new Fach("G6", 70, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly G7 : Fach = new Fach("G7", 71, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly G8 : Fach = new Fach("G8", 72, );

	/**
	 * Fach Griechisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly G9 : Fach = new Fach("G9", 73, );

	/**
	 * Fach Geräte- und Maschinenlehre
	 */
	public static readonly GA : Fach = new Fach("GA", 74, );

	/**
	 * Fach Grundbildung
	 */
	public static readonly GB : Fach = new Fach("GB", 75, );

	/**
	 * Fach Geschichte
	 */
	public static readonly GE : Fach = new Fach("GE", 76, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Neugriechisch
	 */
	public static readonly GH : Fach = new Fach("GH", 77, );

	/**
	 * Fach Gesellschaftslehre
	 */
	public static readonly GL : Fach = new Fach("GL", 78, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Neugriechisch
	 */
	public static readonly GM : Fach = new Fach("GM", 79, );

	/**
	 * Fach Geologie (Oberstufenkolleg Bielefeld)
	 */
	public static readonly GO : Fach = new Fach("GO", 80, );

	/**
	 * Fach Geschichte / Politik
	 */
	public static readonly GP : Fach = new Fach("GP", 81, );

	/**
	 * Fach Altgriechisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly GQ : Fach = new Fach("GQ", 82, );

	/**
	 * Fach Gestaltungslehre
	 */
	public static readonly GS : Fach = new Fach("GS", 83, );

	/**
	 * Fach Gesamtunterricht (nur für Förderschulkindergarten)
	 */
	public static readonly GU : Fach = new Fach("GU", 84, );

	/**
	 * Fach Geschichte und Sozialwissenschaft
	 */
	public static readonly GW : Fach = new Fach("GW", 85, );

	/**
	 * Fach Hebräisch
	 */
	public static readonly H : Fach = new Fach("H", 86, );

	/**
	 * Fach Hebräisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly H0 : Fach = new Fach("H0", 87, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly H1 : Fach = new Fach("H1", 88, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly H5 : Fach = new Fach("H5", 89, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly H6 : Fach = new Fach("H6", 90, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly H7 : Fach = new Fach("H7", 91, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly H8 : Fach = new Fach("H8", 92, );

	/**
	 * Fach Hebräisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly H9 : Fach = new Fach("H9", 93, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kroatisch
	 */
	public static readonly HH : Fach = new Fach("HH", 94, );

	/**
	 * Fach Hebräisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly HQ : Fach = new Fach("HQ", 95, );

	/**
	 * Fach Jüdische Religionslehre
	 */
	public static readonly HR : Fach = new Fach("HR", 96, );

	/**
	 * Fach Hausunterricht
	 */
	public static readonly HU : Fach = new Fach("HU", 97, );

	/**
	 * Fach Hauswirtschaft
	 */
	public static readonly HW : Fach = new Fach("HW", 98, );

	/**
	 * Fach Hygiene
	 */
	public static readonly HY : Fach = new Fach("HY", 99, );

	/**
	 * Fach Italienisch
	 */
	public static readonly I : Fach = new Fach("I", 100, );

	/**
	 * Fach Italienisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly I0 : Fach = new Fach("I0", 101, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly I1 : Fach = new Fach("I1", 102, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly I5 : Fach = new Fach("I5", 103, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly I6 : Fach = new Fach("I6", 104, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly I7 : Fach = new Fach("I7", 105, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly I8 : Fach = new Fach("I8", 106, );

	/**
	 * Fach Italienisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly I9 : Fach = new Fach("I9", 107, );

	/**
	 * Fach Individuelles Lernen (Ergänzungsstunden, Ganztag- und Betreuungsangebote)
	 */
	public static readonly IE : Fach = new Fach("IE", 108, );

	/**
	 * Fach Informatik
	 */
	public static readonly IF : Fach = new Fach("IF", 109, );

	/**
	 * Fach Ingenieurwissenschaften / Ingenieurtechnik
	 */
	public static readonly IG : Fach = new Fach("IG", 110, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Italienisch
	 */
	public static readonly IH : Fach = new Fach("IH", 111, );

	/**
	 * Fach Individuelles Lernen (dem Kernstundenkontingent entnommen)
	 */
	public static readonly IK : Fach = new Fach("IK", 112, );

	/**
	 * Fach Islamische Religionslehre
	 */
	public static readonly IL : Fach = new Fach("IL", 113, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Italienisch
	 */
	public static readonly IM : Fach = new Fach("IM", 114, );

	/**
	 * Fach Instrumentalpraktischer Grundkurs
	 */
	public static readonly IN : Fach = new Fach("IN", 115, );

	/**
	 * Fach Italienisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly IQ : Fach = new Fach("IQ", 116, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Niederländisch
	 */
	public static readonly JH : Fach = new Fach("JH", 117, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Japanisch
	 */
	public static readonly JM : Fach = new Fach("JM", 118, );

	/**
	 * Fach Japanisch
	 */
	public static readonly K : Fach = new Fach("K", 119, );

	/**
	 * Fach Japanisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly K0 : Fach = new Fach("K0", 120, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly K1 : Fach = new Fach("K1", 121, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly K5 : Fach = new Fach("K5", 122, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly K6 : Fach = new Fach("K6", 123, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly K7 : Fach = new Fach("K7", 124, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly K8 : Fach = new Fach("K8", 125, );

	/**
	 * Fach Japanisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly K9 : Fach = new Fach("K9", 126, );

	/**
	 * Fach Katholische Religionslehre (konfessionell kooperativ)
	 */
	public static readonly KN : Fach = new Fach("KN", 127, );

	/**
	 * Fach Kommunikation
	 */
	public static readonly KO : Fach = new Fach("KO", 128, );

	/**
	 * Fach Japanisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly KQ : Fach = new Fach("KQ", 129, );

	/**
	 * Fach Katholische Religionslehre
	 */
	public static readonly KR : Fach = new Fach("KR", 130, );

	/**
	 * Fach Kurzschrift
	 */
	public static readonly KS : Fach = new Fach("KS", 131, );

	/**
	 * Fach Kunst
	 */
	public static readonly KU : Fach = new Fach("KU", 132, );

	/**
	 * Fach Lateinisch
	 */
	public static readonly L : Fach = new Fach("L", 133, );

	/**
	 * Fach Lateinisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly L0 : Fach = new Fach("L0", 134, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly L1 : Fach = new Fach("L1", 135, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly L5 : Fach = new Fach("L5", 136, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly L6 : Fach = new Fach("L6", 137, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly L7 : Fach = new Fach("L7", 138, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly L8 : Fach = new Fach("L8", 139, );

	/**
	 * Fach Lateinisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly L9 : Fach = new Fach("L9", 140, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Albanisch
	 */
	public static readonly LH : Fach = new Fach("LH", 141, );

	/**
	 * Fach Literatur
	 */
	public static readonly LI : Fach = new Fach("LI", 142, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Albanisch
	 */
	public static readonly LM : Fach = new Fach("LM", 143, );

	/**
	 * Fach Lateinisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly LQ : Fach = new Fach("LQ", 144, );

	/**
	 * Fach Mathematik
	 */
	public static readonly M : Fach = new Fach("M", 145, );

	/**
	 * Fach Religionsunterricht der mennonitischen Brüdergemeinden in NRW als Schulversuch
	 */
	public static readonly MB : Fach = new Fach("MB", 146, );

	/**
	 * Fach Medienerziehung
	 */
	public static readonly MD : Fach = new Fach("MD", 147, );

	/**
	 * Fach Mechanik
	 */
	public static readonly ME : Fach = new Fach("ME", 148, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Mazedonisch
	 */
	public static readonly MH : Fach = new Fach("MH", 149, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Mazedonisch
	 */
	public static readonly MM : Fach = new Fach("MM", 150, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Ungarisch
	 */
	public static readonly MN : Fach = new Fach("MN", 151, );

	/**
	 * Fach Meß- und Prüftechnik
	 */
	public static readonly MP : Fach = new Fach("MP", 152, );

	/**
	 * Fach Musik
	 */
	public static readonly MU : Fach = new Fach("MU", 153, );

	/**
	 * Fach Methodik des wissenschaftl. orientierten Arbeitens
	 */
	public static readonly MW : Fach = new Fach("MW", 154, );

	/**
	 * Fach Spezielle sonderpädagogische Maßnahme
	 */
	public static readonly MX : Fach = new Fach("MX", 155, );

	/**
	 * Fach Fächerübergreifender Unterricht in SI und SII (Berufspraxisstufe)
	 */
	public static readonly MY : Fach = new Fach("MY", 156, );

	/**
	 * Fach Niederländisch
	 */
	public static readonly N : Fach = new Fach("N", 157, );

	/**
	 * Fach Niederländisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly N0 : Fach = new Fach("N0", 158, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly N1 : Fach = new Fach("N1", 159, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly N5 : Fach = new Fach("N5", 160, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly N6 : Fach = new Fach("N6", 161, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly N7 : Fach = new Fach("N7", 162, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly N8 : Fach = new Fach("N8", 163, );

	/**
	 * Fach Niederländisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly N9 : Fach = new Fach("N9", 164, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Polnisch
	 */
	public static readonly NH : Fach = new Fach("NH", 165, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Niederländisch
	 */
	public static readonly NM : Fach = new Fach("NM", 166, );

	/**
	 * Fach Niederländisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly NQ : Fach = new Fach("NQ", 167, );

	/**
	 * Fach Naturwissenschaften
	 */
	public static readonly NW : Fach = new Fach("NW", 168, );

	/**
	 * Fach Portugiesisch
	 */
	public static readonly O : Fach = new Fach("O", 169, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly O0 : Fach = new Fach("O0", 170, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly O1 : Fach = new Fach("O1", 171, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly O5 : Fach = new Fach("O5", 172, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly O6 : Fach = new Fach("O6", 173, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly O7 : Fach = new Fach("O7", 174, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly O8 : Fach = new Fach("O8", 175, );

	/**
	 * Fach Portugiesisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly O9 : Fach = new Fach("O9", 176, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Portugiesisch
	 */
	public static readonly OH : Fach = new Fach("OH", 177, );

	/**
	 * Fach Organisationslehre/Büroorganisation
	 */
	public static readonly OL : Fach = new Fach("OL", 178, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Portugiesisch
	 */
	public static readonly OM : Fach = new Fach("OM", 179, );

	/**
	 * Fach Portugiesisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly OQ : Fach = new Fach("OQ", 180, );

	/**
	 * Fach Orthodoxe Religionslehre
	 */
	public static readonly OR : Fach = new Fach("OR", 181, );

	/**
	 * Fach Pädagogik/ Erziehungswissenschaft
	 */
	public static readonly PA : Fach = new Fach("PA", 182, );

	/**
	 * Fach Physik
	 */
	public static readonly PH : Fach = new Fach("PH", 183, );

	/**
	 * Fach Politik
	 */
	public static readonly PK : Fach = new Fach("PK", 184, );

	/**
	 * Fach Philosophie
	 */
	public static readonly PL : Fach = new Fach("PL", 185, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Polnisch
	 */
	public static readonly PM : Fach = new Fach("PM", 186, );

	/**
	 * Fach Praktische Philosophie
	 */
	public static readonly PP : Fach = new Fach("PP", 187, );

	/**
	 * Fach Psychologie
	 */
	public static readonly PS : Fach = new Fach("PS", 188, );

	/**
	 * Fach Fachübergreifender Unterricht
	 */
	public static readonly PU : Fach = new Fach("PU", 189, );

	/**
	 * Fach Personalwirtschaft und Soziologie/Politik
	 */
	public static readonly PW : Fach = new Fach("PW", 190, );

	/**
	 * Fach Projektkurs (mit einem oder zwei Leitfächern)
	 */
	public static readonly PX : Fach = new Fach("PX", 191, );

	/**
	 * Fach Politik/Ökonomische Grundbildung
	 */
	public static readonly POE : Fach = new Fach("POE", 192, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Farsi
	 */
	public static readonly QH : Fach = new Fach("QH", 193, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Farsi
	 */
	public static readonly QM : Fach = new Fach("QM", 194, );

	/**
	 * Fach Russisch
	 */
	public static readonly R : Fach = new Fach("R", 195, );

	/**
	 * Fach Russisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly R0 : Fach = new Fach("R0", 196, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly R1 : Fach = new Fach("R1", 197, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly R5 : Fach = new Fach("R5", 198, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly R6 : Fach = new Fach("R6", 199, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly R7 : Fach = new Fach("R7", 200, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly R8 : Fach = new Fach("R8", 201, );

	/**
	 * Fach Russisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly R9 : Fach = new Fach("R9", 202, );

	/**
	 * Fach Rechnungswesen
	 */
	public static readonly RE : Fach = new Fach("RE", 203, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch
	 */
	public static readonly RH : Fach = new Fach("RH", 204, );

	/**
	 * Fach Rechtskunde
	 */
	public static readonly RK : Fach = new Fach("RK", 205, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Russisch
	 */
	public static readonly RM : Fach = new Fach("RM", 206, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Romanes
	 */
	public static readonly RN : Fach = new Fach("RN", 207, );

	/**
	 * Fach Russisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly RQ : Fach = new Fach("RQ", 208, );

	/**
	 * Fach Recht und Verwaltung
	 */
	public static readonly RW : Fach = new Fach("RW", 209, );

	/**
	 * Fach Spanisch
	 */
	public static readonly S : Fach = new Fach("S", 210, );

	/**
	 * Fach Spanisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly S0 : Fach = new Fach("S0", 211, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly S1 : Fach = new Fach("S1", 212, );

	/**
	 * Fach Sport für Jungen
	 */
	public static readonly S3 : Fach = new Fach("S3", 213, );

	/**
	 * Fach Sport für Mädchen
	 */
	public static readonly S4 : Fach = new Fach("S4", 214, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly S5 : Fach = new Fach("S5", 215, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly S6 : Fach = new Fach("S6", 216, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly S7 : Fach = new Fach("S7", 217, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly S8 : Fach = new Fach("S8", 218, );

	/**
	 * Fach Spanisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly S9 : Fach = new Fach("S9", 219, );

	/**
	 * Fach Softwareentwicklung und -engineering
	 */
	public static readonly SE : Fach = new Fach("SE", 220, );

	/**
	 * Fach sonderpädag. Förderung für Schüler/-innen mit sonderpädag. Förderbedarf
	 */
	public static readonly SG : Fach = new Fach("SG", 221, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Spanisch
	 */
	public static readonly SH : Fach = new Fach("SH", 222, );

	/**
	 * Fach Spiel
	 */
	public static readonly SI : Fach = new Fach("SI", 223, );

	/**
	 * Fach Soziologie
	 */
	public static readonly SL : Fach = new Fach("SL", 224, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Spanisch
	 */
	public static readonly SM : Fach = new Fach("SM", 225, );

	/**
	 * Fach Sport
	 */
	public static readonly SP : Fach = new Fach("SP", 226, );

	/**
	 * Fach Spanisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly SQ : Fach = new Fach("SQ", 227, );

	/**
	 * Fach Sonstige Fremdsprachen
	 */
	public static readonly SR : Fach = new Fach("SR", 228, );

	/**
	 * Fach Sachunterricht
	 */
	public static readonly SU : Fach = new Fach("SU", 229, );

	/**
	 * Fach Sozialwissenschaften
	 */
	public static readonly SW : Fach = new Fach("SW", 230, );

	/**
	 * Fach Sozialwissenschaften/Wirtschaft
	 */
	public static readonly SZ : Fach = new Fach("SZ", 231, );

	/**
	 * Fach Türkisch
	 */
	public static readonly T : Fach = new Fach("T", 232, );

	/**
	 * Fach Türkisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly T0 : Fach = new Fach("T0", 233, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly T1 : Fach = new Fach("T1", 234, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly T5 : Fach = new Fach("T5", 235, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly T6 : Fach = new Fach("T6", 236, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly T7 : Fach = new Fach("T7", 237, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly T8 : Fach = new Fach("T8", 238, );

	/**
	 * Fach Türkisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly T9 : Fach = new Fach("T9", 239, );

	/**
	 * Fach Technik
	 */
	public static readonly TC : Fach = new Fach("TC", 240, );

	/**
	 * Fach Technische Grundbildung
	 */
	public static readonly TG : Fach = new Fach("TG", 241, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Türkisch
	 */
	public static readonly TH : Fach = new Fach("TH", 242, );

	/**
	 * Fach Technische Informatik
	 */
	public static readonly TI : Fach = new Fach("TI", 243, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Türkisch
	 */
	public static readonly TM : Fach = new Fach("TM", 244, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Thai
	 */
	public static readonly TN : Fach = new Fach("TN", 245, );

	/**
	 * Fach Türkisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly TQ : Fach = new Fach("TQ", 246, );

	/**
	 * Fach Textverarbeitung
	 */
	public static readonly TV : Fach = new Fach("TV", 247, );

	/**
	 * Fach Textilgestaltung
	 */
	public static readonly TX : Fach = new Fach("TX", 248, );

	/**
	 * Fach Technisches Zeichnen / Fachzeichnen
	 */
	public static readonly TZ : Fach = new Fach("TZ", 249, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch
	 */
	public static readonly UH : Fach = new Fach("UH", 250, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Rumänisch
	 */
	public static readonly UM : Fach = new Fach("UM", 251, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Ukrainisch
	 */
	public static readonly UN : Fach = new Fach("UN", 252, );

	/**
	 * Fach fächerübergreifender Unterricht (Sprache/Sachunt./Mathematik/Förderunt.)
	 */
	public static readonly UU : Fach = new Fach("UU", 253, );

	/**
	 * Fach Unterweisung
	 */
	public static readonly UW : Fach = new Fach("UW", 254, );

	/**
	 * Fach Verwaltungskunde
	 */
	public static readonly VE : Fach = new Fach("VE", 255, );

	/**
	 * Fach Fächer im genehmigten Schulversuch und sonstige Fächer
	 */
	public static readonly VF : Fach = new Fach("VF", 256, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bulgarisch
	 */
	public static readonly VH : Fach = new Fach("VH", 257, );

	/**
	 * Fach Verkaufskunde
	 */
	public static readonly VK : Fach = new Fach("VK", 258, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Bulgarisch
	 */
	public static readonly VM : Fach = new Fach("VM", 259, );

	/**
	 * Fach vokalpraktischer Grundkurs
	 */
	public static readonly VO : Fach = new Fach("VO", 260, );

	/**
	 * Fach Volkswirtschaftslehre/Politik
	 */
	public static readonly VP : Fach = new Fach("VP", 261, );

	/**
	 * Fach Verfahrenstechnik
	 */
	public static readonly VT : Fach = new Fach("VT", 262, );

	/**
	 * Fach Volkswirtschaftslehre
	 */
	public static readonly VW : Fach = new Fach("VW", 263, );

	/**
	 * Fach Vertiefungsfach
	 */
	public static readonly VX : Fach = new Fach("VX", 264, );

	/**
	 * Fach Werken
	 */
	public static readonly W : Fach = new Fach("W", 265, );

	/**
	 * Fach Wirtschaftsgeographie
	 */
	public static readonly WG : Fach = new Fach("WG", 266, );

	/**
	 * Fach Wirtschaftsinformatik/Organisationslehre
	 */
	public static readonly WI : Fach = new Fach("WI", 267, );

	/**
	 * Fach Wirtschaft-Politik
	 */
	public static readonly WP : Fach = new Fach("WP", 268, );

	/**
	 * Fach Wirtschaftslehre
	 */
	public static readonly WW : Fach = new Fach("WW", 269, );

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Hauswirtschaft
	 */
	public static readonly WX : Fach = new Fach("WX", 270, );

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Technik
	 */
	public static readonly WY : Fach = new Fach("WY", 271, );

	/**
	 * Fach Wirtschaft und Arbeitswelt - Schwerpunkt Wirtschaft
	 */
	public static readonly WZ : Fach = new Fach("WZ", 272, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - sonstige Sprache
	 */
	public static readonly XH : Fach = new Fach("XH", 273, );

	/**
	 * Fach Unterricht in der Herkunftssprache - sonstige Sprache
	 */
	public static readonly XM : Fach = new Fach("XM", 274, );

	/**
	 * Fach Sonstige Fächer ohne Fremdsprachen (kein Import nach ASDPC)
	 */
	public static readonly XX : Fach = new Fach("XX", 275, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Koreanisch
	 */
	public static readonly YH : Fach = new Fach("YH", 276, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Koreanisch
	 */
	public static readonly YM : Fach = new Fach("YM", 277, );

	/**
	 * Fach orthodoxe Religionslehre (Syrisch)
	 */
	public static readonly YR : Fach = new Fach("YR", 278, );

	/**
	 * Fach Neugriechisch
	 */
	public static readonly Z : Fach = new Fach("Z", 279, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in der Einführungsphase
	 */
	public static readonly Z0 : Fach = new Fach("Z0", 280, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 11
	 */
	public static readonly Z1 : Fach = new Fach("Z1", 281, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 5
	 */
	public static readonly Z5 : Fach = new Fach("Z5", 282, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 6
	 */
	public static readonly Z6 : Fach = new Fach("Z6", 283, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 7
	 */
	public static readonly Z7 : Fach = new Fach("Z7", 284, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 8
	 */
	public static readonly Z8 : Fach = new Fach("Z8", 285, );

	/**
	 * Fach Neugriechisch, regulärer Beginn in Jahrgang 9
	 */
	public static readonly Z9 : Fach = new Fach("Z9", 286, );

	/**
	 * Fach Zusätzliche Förderung
	 */
	public static readonly ZF : Fach = new Fach("ZF", 287, );

	/**
	 * Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kurdische Sprachen
	 */
	public static readonly ZH : Fach = new Fach("ZH", 288, );

	/**
	 * Fach Unterricht in der Herkunftssprache - Kurdische Sprachen (Sorani, Komanci, Zaza)
	 */
	public static readonly ZM : Fach = new Fach("ZM", 289, );

	/**
	 * Fach Neugriechisch, außerhalb des regulären Fachunterrichts
	 */
	public static readonly ZQ : Fach = new Fach("ZQ", 290, );

	/**
	 * Fach Zeichnen / Werken
	 */
	public static readonly ZW : Fach = new Fach("ZW", 291, );

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
				const fke : FachKatalogEintrag | null = f.daten(schuljahr);
				if (fke !== null && fke.istFremdsprache && JavaObject.equalsTranspiler(fke.schluessel, (fke.kuerzel)))
					mapFremdsprachen.put(fke.kuerzel, f);
			}
		}
		return mapFremdsprachen;
	}

	/**
	 * Gibt da Fach für den angegebene Schlüssel (bei Sprachen zweistelliges Kürzel, z.B. S8) zurück.
	 * Ist der Schlüssel ungültig, so wird als Default VF zurückgegeben.
	 *
	 * @param schluessel   der Schlüssel
	 *
	 * @return das Fach oder bei einem ungültigen Schlüssel das Fach VF
	 */
	public static getBySchluesselOrDefault(schluessel : string) : Fach {
		const result : Fach | null = Fach.data().getWertBySchluessel(schluessel);
		if (result !== null)
			return result;
		return Fach.VF;
	}

	/**
	 * Bestimmt den Fach-Katalogeintrag für das angegebene Schuljahr. Ist der Wert
	 * in diesem Schuljahr nicht mehr gültig, so wird ersatzweise der letzte gültige Wert
	 * zurückgegeben.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Fach-Katalogeintrag
	 */
	public getEintragOrLast(schuljahr : number) : FachKatalogEintrag | null {
		const result : FachKatalogEintrag | null = Fach.data().getEintragBySchuljahrUndWert(schuljahr, this);
		if (result !== null)
			return result;
		return this.historie().getLast();
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
