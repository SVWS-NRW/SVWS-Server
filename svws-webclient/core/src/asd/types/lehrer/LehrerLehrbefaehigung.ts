import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { LehrerLehrbefaehigungKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehrbefaehigungKatalogEintrag';

export class LehrerLehrbefaehigung extends JavaEnum<LehrerLehrbefaehigung> implements CoreType<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerLehrbefaehigung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerLehrbefaehigung> = new Map<string, LehrerLehrbefaehigung>();

	/**
	 * Lehrbefähigung 'Arbeitslehre / Schwerpunkt Haushaltslehre'
	 */
	public static readonly AH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AH", 0, );

	/**
	 * Lehrbefähigung 'Arbeitslehre'
	 */
	public static readonly AL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AL", 1, );

	/**
	 * Lehrbefähigung 'Arabisch (Muttersprachl. Unterricht)'
	 */
	public static readonly AM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AM", 2, );

	/**
	 * Lehrbefähigung 'Aramäisch (Herkunftssprache)'
	 */
	public static readonly AN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AN", 3, );

	/**
	 * Lehrbefähigung 'Arbeitslehre / Schwerpunkt Technik'
	 */
	public static readonly AT : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AT", 4, );

	/**
	 * Lehrbefähigung 'Arbeitslehre / Wirtschaft'
	 */
	public static readonly AW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AW", 5, );

	/**
	 * Lehrbefähigung 'Betreuung'
	 */
	public static readonly BE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BE", 6, );

	/**
	 * Lehrbefähigung 'Biologie'
	 */
	public static readonly BI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BI", 7, );

	/**
	 * Lehrbefähigung 'Bosnisch (Muttersprachl. Unterricht)'
	 */
	public static readonly BM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BM", 8, );

	/**
	 * Lehrbefähigung 'Chinesisch'
	 */
	public static readonly C : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("C", 9, );

	/**
	 * Lehrbefähigung 'Chemie'
	 */
	public static readonly CH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CH", 10, );

	/**
	 * Lehrbefähigung 'Kroatisch (Muttersprachl. Unterricht)'
	 */
	public static readonly CM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CM", 11, );

	/**
	 * Lehrbefähigung 'Chinesisch (Herkunftssprache)'
	 */
	public static readonly CN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CN", 12, );

	/**
	 * Lehrbefähigung 'Deutsch'
	 */
	public static readonly D : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("D", 13, );

	/**
	 * Lehrbefähigung 'Deutsche Gebärdensprache'
	 */
	public static readonly DG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DG", 14, );

	/**
	 * Lehrbefähigung 'Englisch'
	 */
	public static readonly E : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("E", 15, );

	/**
	 * Lehrbefähigung 'Erdkunde'
	 */
	public static readonly EK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EK", 16, );

	/**
	 * Lehrbefähigung 'Serbisch (Muttersprachl. Unterricht)'
	 */
	public static readonly EM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EM", 17, );

	/**
	 * Lehrbefähigung 'Evangelische Religionslehre'
	 */
	public static readonly ER : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ER", 18, );

	/**
	 * Lehrbefähigung 'Französisch'
	 */
	public static readonly F : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("F", 19, );

	/**
	 * Lehrbefähigung 'Französisch (Herkunftssprache)'
	 */
	public static readonly FM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("FM", 20, );

	/**
	 * Lehrbefähigung 'Fachpraxis'
	 */
	public static readonly FP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("FP", 21, );

	/**
	 * Lehrbefähigung 'Griechisch'
	 */
	public static readonly G : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("G", 22, );

	/**
	 * Lehrbefähigung 'Geschichte'
	 */
	public static readonly GE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GE", 23, );

	/**
	 * Lehrbefähigung 'Griechisch (Muttersprachl. Unterricht)'
	 */
	public static readonly GM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GM", 24, );

	/**
	 * Lehrbefähigung 'Geschichte / Politische Bildung'
	 */
	public static readonly GP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GP", 25, );

	/**
	 * Lehrbefähigung 'Kunst/Gestalten (Kunst oder Textilgestaltung)'
	 */
	public static readonly GS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GS", 26, );

	/**
	 * Lehrbefähigung 'Gesamtunterricht'
	 */
	public static readonly GU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GU", 27, );

	/**
	 * Lehrbefähigung 'Hebräisch'
	 */
	public static readonly H : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("H", 28, );

	/**
	 * Lehrbefähigung 'Hauswirtschaft'
	 */
	public static readonly HA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HA", 29, );

	/**
	 * Lehrbefähigung 'Jüdische Religionslehre'
	 */
	public static readonly HR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HR", 30, );

	/**
	 * Lehrbefähigung 'Hauswirtschaft (-swissenschaft)'
	 */
	public static readonly HW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HW", 31, );

	/**
	 * Lehrbefähigung 'Italienisch'
	 */
	public static readonly I : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("I", 32, );

	/**
	 * Lehrbefähigung 'Informatik (nachgewiesen durch 2. Staatsprüfung, sonst siehe IK)'
	 */
	public static readonly IF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IF", 33, );

	/**
	 * Lehrbefähigung 'Ingenieurwissenschaft und Ingenieurtechnik'
	 */
	public static readonly IG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IG", 34, );

	/**
	 * Lehrbefähigung 'Italienisch (Muttersprachl. Unterricht)'
	 */
	public static readonly IM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IM", 35, );

	/**
	 * Lehrbefähigung 'Islamkunde'
	 */
	public static readonly IR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IR", 36, );

	/**
	 * Lehrbefähigung 'Japanisch (Herkunftssprache)'
	 */
	public static readonly JM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("JM", 37, );

	/**
	 * Lehrbefähigung 'Japanisch'
	 */
	public static readonly K : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("K", 38, );

	/**
	 * Lehrbefähigung 'Kunst/Gestalten'
	 */
	public static readonly KG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KG", 39, );

	/**
	 * Lehrbefähigung 'Katholische Religionslehre'
	 */
	public static readonly KR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KR", 40, );

	/**
	 * Lehrbefähigung 'Kurzschrift'
	 */
	public static readonly KS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KS", 41, );

	/**
	 * Lehrbefähigung 'Kunst / Werken'
	 */
	public static readonly KU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KU", 42, );

	/**
	 * Lehrbefähigung 'Kunstwissenschaft'
	 */
	public static readonly KW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KW", 43, );

	/**
	 * Lehrbefähigung 'Lateinisch'
	 */
	public static readonly L : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("L", 44, );

	/**
	 * Lehrbefähigung 'Literaturwissenschaft'
	 */
	public static readonly LI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LI", 45, );

	/**
	 * Lehrbefähigung 'Albanisch (Muttersprachl. Unterricht)'
	 */
	public static readonly LM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LM", 46, );

	/**
	 * Lehrbefähigung 'Linguistik'
	 */
	public static readonly LN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LN", 47, );

	/**
	 * Lehrbefähigung 'Mathematik'
	 */
	public static readonly M : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("M", 48, );

	/**
	 * Lehrbefähigung 'Makedonisch (Muttersprachl. Unterricht)'
	 */
	public static readonly MM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MM", 49, );

	/**
	 * Lehrbefähigung 'Ungarisch (Herkunftssprache)'
	 */
	public static readonly MN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MN", 50, );

	/**
	 * Lehrbefähigung 'Maschinenschreiben'
	 */
	public static readonly MS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MS", 51, );

	/**
	 * Lehrbefähigung 'Musik'
	 */
	public static readonly MU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MU", 52, );

	/**
	 * Lehrbefähigung 'Niederländisch'
	 */
	public static readonly N : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("N", 53, );

	/**
	 * Lehrbefähigung 'Portugiesisch'
	 */
	public static readonly O : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("O", 54, );

	/**
	 * Lehrbefähigung 'Ohne Angabe'
	 */
	public static readonly OA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OA", 55, );

	/**
	 * Lehrbefähigung 'Portugiesisch (Muttersprachl. Unterricht)'
	 */
	public static readonly OM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OM", 56, );

	/**
	 * Lehrbefähigung 'Griechisch-orthodoxe Religionslehre'
	 */
	public static readonly OR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OR", 57, );

	/**
	 * Lehrbefähigung 'Pädagogik'
	 */
	public static readonly PA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PA", 58, );

	/**
	 * Lehrbefähigung 'Physik (Astronomie)'
	 */
	public static readonly PH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PH", 59, );

	/**
	 * Lehrbefähigung 'Philosophie/Praktische Philosophie'
	 */
	public static readonly PI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PI", 60, );

	/**
	 * Lehrbefähigung 'Politik'
	 */
	public static readonly PK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PK", 61, );

	/**
	 * Lehrbefähigung 'Philosophie'
	 */
	public static readonly PL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PL", 62, );

	/**
	 * Lehrbefähigung 'Polnisch (Muttersprachl. Unterricht)'
	 */
	public static readonly PM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PM", 63, );

	/**
	 * Lehrbefähigung 'Praktische Philosophie'
	 */
	public static readonly PP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PP", 64, );

	/**
	 * Lehrbefähigung 'Psychologie'
	 */
	public static readonly PS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PS", 65, );

	/**
	 * Lehrbefähigung 'Russisch'
	 */
	public static readonly R : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("R", 66, );

	/**
	 * Lehrbefähigung 'Russisch (Muttersprachl. Unterricht)'
	 */
	public static readonly RM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RM", 67, );

	/**
	 * Lehrbefähigung 'Romanes (Herkunftssprache)'
	 */
	public static readonly RN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RN", 68, );

	/**
	 * Lehrbefähigung 'Rechtswissenschaft'
	 */
	public static readonly RW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RW", 69, );

	/**
	 * Lehrbefähigung 'Spanisch'
	 */
	public static readonly S : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("S", 70, );

	/**
	 * Lehrbefähigung 'Sozial- und Erziehungswissenschaft'
	 */
	public static readonly SE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SE", 71, );

	/**
	 * Lehrbefähigung 'Sozialpflege'
	 */
	public static readonly SF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SF", 72, );

	/**
	 * Lehrbefähigung 'Spanisch (Muttersprachl. Unterricht)'
	 */
	public static readonly SM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SM", 73, );

	/**
	 * Lehrbefähigung 'Sonderpädagogik'
	 */
	public static readonly SN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SN", 74, );

	/**
	 * Lehrbefähigung 'Sozialpädagogik'
	 */
	public static readonly SO : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SO", 75, );

	/**
	 * Lehrbefähigung 'Sport'
	 */
	public static readonly SP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SP", 76, );

	/**
	 * Lehrbefähigung 'sonstige Sprachen'
	 */
	public static readonly SR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SR", 77, );

	/**
	 * Lehrbefähigung 'Sachunterricht'
	 */
	public static readonly SU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SU", 78, );

	/**
	 * Lehrbefähigung 'Sozialwissenschaften'
	 */
	public static readonly SW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SW", 79, );

	/**
	 * Lehrbefähigung 'Türkisch'
	 */
	public static readonly T : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("T", 80, );

	/**
	 * Lehrbefähigung 'Technik'
	 */
	public static readonly TC : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TC", 81, );

	/**
	 * Lehrbefähigung 'Technologie'
	 */
	public static readonly TE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TE", 82, );

	/**
	 * Lehrbefähigung 'Türkisch (Muttersprachl. Unterricht)'
	 */
	public static readonly TM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TM", 83, );

	/**
	 * Lehrbefähigung 'Thai(Herkunftssprache)'
	 */
	public static readonly TN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TN", 84, );

	/**
	 * Lehrbefähigung 'Textilgestaltung'
	 */
	public static readonly TX : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TX", 85, );

	/**
	 * Lehrbefähigung 'Ukrainisch (Herkunftssprache)'
	 */
	public static readonly UN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UN", 86, );

	/**
	 * Lehrbefähigung 'Unterweisung'
	 */
	public static readonly UW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UW", 87, );

	/**
	 * Lehrbefähigung 'Werken (Musisches)'
	 */
	public static readonly W : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("W", 88, );

	/**
	 * Lehrbefähigung 'Wirtschaft-Politik'
	 */
	public static readonly WK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WK", 89, );

	/**
	 * Lehrbefähigung 'Slowenisch (Muttersprachl. Unterricht)'
	 */
	public static readonly WM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WM", 90, );

	/**
	 * Lehrbefähigung 'Wirtschaftslehre/Politik'
	 */
	public static readonly WP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WP", 91, );

	/**
	 * Lehrbefähigung 'Technisches Werken'
	 */
	public static readonly WT : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WT", 92, );

	/**
	 * Lehrbefähigung 'Wirtschafts- und Arbeitslehre'
	 */
	public static readonly WW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WW", 93, );

	/**
	 * Lehrbefähigung 'sonstige Muttersprache'
	 */
	public static readonly XM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("XM", 94, );

	/**
	 * Lehrbefähigung 'Syrisch-orthodoxe Religionslehre'
	 */
	public static readonly YR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("YR", 95, );

	/**
	 * Lehrbefähigung 'Neugriechisch'
	 */
	public static readonly Z : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("Z", 96, );

	/**
	 * Lehrbefähigung 'Alevitische Religionslehre nach den Grundsätzen des AABF'
	 */
	public static readonly AR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AR", 97, );

	/**
	 * Lehrbefähigung 'Ästhetische Erziehung'
	 */
	public static readonly AE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AE", 98, );

	/**
	 * Lehrbefähigung 'Deutsch für Schülerinnen und Schüler mit Zuwanderungsgeschichte'
	 */
	public static readonly DZ : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DZ", 99, );

	/**
	 * Lehrbefähigung 'Mathematische Grundbildung'
	 */
	public static readonly MG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MG", 100, );

	/**
	 * Lehrbefähigung 'Natur- und Gesellschaftswissenschaften'
	 */
	public static readonly NG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NG", 101, );

	/**
	 * Lehrbefähigung 'Sprachliche Grundbildung'
	 */
	public static readonly SB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SB", 102, );

	/**
	 * Lehrbefähigung 'Religionslehre der mennonitischen Brüdergemeinden in RW (Lehrerlaubnis)'
	 */
	public static readonly MB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MB", 103, );

	/**
	 * Lehrbefähigung 'Bulgarisch (Herkunftssprache)'
	 */
	public static readonly VM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("VM", 104, );

	/**
	 * Lehrbefähigung 'Farsi (Herkunftssprache)'
	 */
	public static readonly QM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("QM", 105, );

	/**
	 * Lehrbefähigung 'Koreanisch (Herkunftssprache)'
	 */
	public static readonly YM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("YM", 106, );

	/**
	 * Lehrbefähigung 'Kurdische Sprachen (Herkunftssprache) (Sorani, Komaci, Zaza)'
	 */
	public static readonly ZM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ZM", 107, );

	/**
	 * Lehrbefähigung 'Niederländisch (Herkunftssprache)'
	 */
	public static readonly NM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NM", 108, );

	/**
	 * Lehrbefähigung 'Rumänisch (Herkunftssprache)'
	 */
	public static readonly UM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UM", 109, );

	/**
	 * Lehrbefähigung 'Islamische Religionslehre'
	 */
	public static readonly IL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IL", 110, );

	/**
	 * Lehrbefähigung 'Arabisch'
	 */
	public static readonly A : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("A", 111, );

	/**
	 * Lehrbefähigung 'Braille´sche Punktschrift'
	 */
	public static readonly BN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BN", 112, );

	/**
	 * Lehrbefähigung 'Darstellen und Gestalten'
	 */
	public static readonly DS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DS", 113, );

	/**
	 * Lehrbefähigung 'Hauswirtschaft (Konsum/Ernährung/Gesundheit)'
	 */
	public static readonly EL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EL", 114, );

	/**
	 * Lehrbefähigung 'Design/Fotografie'
	 */
	public static readonly DF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DF", 115, );

	/**
	 * Lehrbefähigung 'Gesellschaftswissenschaften'
	 */
	public static readonly GW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GW", 116, );

	/**
	 * Lehrbefähigung 'Malerei/Grafik/Gestaltung'
	 */
	public static readonly MJ : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MJ", 117, );

	/**
	 * Lehrbefähigung 'Naturwissenschaften'
	 */
	public static readonly NW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NW", 118, );

	/**
	 * Lehrbefähigung 'Sozialwesen'
	 */
	public static readonly SI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SI", 119, );

	/**
	 * Lehrbefähigung 'Zusatzqualifikation Bilinguales Lernen'
	 */
	public static readonly ZB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ZB", 120, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerLehrbefaehigung.all_values_by_ordinal.push(this);
		LehrerLehrbefaehigung.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung>) : void {
		CoreTypeDataManager.putManager(LehrerLehrbefaehigung.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung> {
		return CoreTypeDataManager.getManager(LehrerLehrbefaehigung.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLehrbefaehigung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerLehrbefaehigung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerLehrbefaehigungKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<LehrerLehrbefaehigungKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerLehrbefaehigung>('de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerLehrbefaehigung(obj : unknown) : LehrerLehrbefaehigung {
	return obj as LehrerLehrbefaehigung;
}
