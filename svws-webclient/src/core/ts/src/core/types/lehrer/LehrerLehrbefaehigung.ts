import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerKatalogLehrbefaehigungEintrag } from '../../../core/data/lehrer/LehrerKatalogLehrbefaehigungEintrag';

export class LehrerLehrbefaehigung extends JavaObject implements JavaEnum<LehrerLehrbefaehigung> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerLehrbefaehigung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerLehrbefaehigung> = new Map<string, LehrerLehrbefaehigung>();

	/**
	 * Lehrbefähigung 'Arbeitslehre / Schwerpunkt Haushaltslehre'
	 */
	public static readonly AH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AH", 0, [new LehrerKatalogLehrbefaehigungEintrag(174, "AH", "Arbeitslehre / Schwerpunkt Haushaltslehre", null, null)]);

	/**
	 * Lehrbefähigung 'Arbeitslehre'
	 */
	public static readonly AL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AL", 1, [new LehrerKatalogLehrbefaehigungEintrag(175, "AL", "Arbeitslehre", null, null)]);

	/**
	 * Lehrbefähigung 'Arabisch (Muttersprachl. Unterricht)'
	 */
	public static readonly AM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AM", 2, [new LehrerKatalogLehrbefaehigungEintrag(176, "AM", "Arabisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Arbeitslehre / Schwerpunkt Technik'
	 */
	public static readonly AT : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AT", 3, [new LehrerKatalogLehrbefaehigungEintrag(177, "AT", "Arbeitslehre / Schwerpunkt Technik", null, null)]);

	/**
	 * Lehrbefähigung 'Arbeitslehre / Wirtschaft'
	 */
	public static readonly AW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AW", 4, [new LehrerKatalogLehrbefaehigungEintrag(178, "AW", "Arbeitslehre / Wirtschaft", null, null)]);

	/**
	 * Lehrbefähigung 'Betreuung'
	 */
	public static readonly BE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BE", 5, [new LehrerKatalogLehrbefaehigungEintrag(179, "BE", "Betreuung", null, null)]);

	/**
	 * Lehrbefähigung 'Biologie'
	 */
	public static readonly BI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BI", 6, [new LehrerKatalogLehrbefaehigungEintrag(180, "BI", "Biologie", null, null)]);

	/**
	 * Lehrbefähigung 'Bosnisch (Muttersprachl. Unterricht)'
	 */
	public static readonly BM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BM", 7, [new LehrerKatalogLehrbefaehigungEintrag(181, "BM", "Bosnisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Chinesisch'
	 */
	public static readonly C : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("C", 8, [new LehrerKatalogLehrbefaehigungEintrag(182, "C", "Chinesisch", null, null)]);

	/**
	 * Lehrbefähigung 'Chemie'
	 */
	public static readonly CH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CH", 9, [new LehrerKatalogLehrbefaehigungEintrag(183, "CH", "Chemie", null, null)]);

	/**
	 * Lehrbefähigung 'Kroatisch (Muttersprachl. Unterricht)'
	 */
	public static readonly CM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CM", 10, [new LehrerKatalogLehrbefaehigungEintrag(184, "CM", "Kroatisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Deutsch'
	 */
	public static readonly D : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("D", 11, [new LehrerKatalogLehrbefaehigungEintrag(185, "D", "Deutsch", null, null)]);

	/**
	 * Lehrbefähigung 'Englisch'
	 */
	public static readonly E : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("E", 12, [new LehrerKatalogLehrbefaehigungEintrag(186, "E", "Englisch", null, null)]);

	/**
	 * Lehrbefähigung 'Erdkunde'
	 */
	public static readonly EK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EK", 13, [new LehrerKatalogLehrbefaehigungEintrag(187, "EK", "Erdkunde", null, null)]);

	/**
	 * Lehrbefähigung 'Serbisch (Muttersprachl. Unterricht)'
	 */
	public static readonly EM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EM", 14, [new LehrerKatalogLehrbefaehigungEintrag(188, "EM", "Serbisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Evangelische Religionslehre'
	 */
	public static readonly ER : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ER", 15, [new LehrerKatalogLehrbefaehigungEintrag(189, "ER", "Evangelische Religionslehre", null, null)]);

	/**
	 * Lehrbefähigung 'Französisch'
	 */
	public static readonly F : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("F", 16, [new LehrerKatalogLehrbefaehigungEintrag(190, "F", "Französisch", null, null)]);

	/**
	 * Lehrbefähigung 'Fachpraxis'
	 */
	public static readonly FP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("FP", 17, [new LehrerKatalogLehrbefaehigungEintrag(191, "FP", "Fachpraxis", null, null)]);

	/**
	 * Lehrbefähigung 'Griechisch'
	 */
	public static readonly G : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("G", 18, [new LehrerKatalogLehrbefaehigungEintrag(192, "G", "Griechisch", null, null)]);

	/**
	 * Lehrbefähigung 'Geschichte'
	 */
	public static readonly GE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GE", 19, [new LehrerKatalogLehrbefaehigungEintrag(193, "GE", "Geschichte", null, null)]);

	/**
	 * Lehrbefähigung 'Griechisch (Muttersprachl. Unterricht)'
	 */
	public static readonly GM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GM", 20, [new LehrerKatalogLehrbefaehigungEintrag(194, "GM", "Griechisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Geschichte / Politische Bildung'
	 */
	public static readonly GP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GP", 21, [new LehrerKatalogLehrbefaehigungEintrag(195, "GP", "Geschichte / Politische Bildung", null, null)]);

	/**
	 * Lehrbefähigung 'Kunst/Gestalten (Kunst oder Textilgestaltung)'
	 */
	public static readonly GS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GS", 22, [new LehrerKatalogLehrbefaehigungEintrag(196, "GS", "Kunst/Gestalten (Kunst oder Textilgestaltung)", null, null)]);

	/**
	 * Lehrbefähigung 'Gesamtunterricht'
	 */
	public static readonly GU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GU", 23, [new LehrerKatalogLehrbefaehigungEintrag(197, "GU", "Gesamtunterricht", null, null)]);

	/**
	 * Lehrbefähigung 'Hebräisch'
	 */
	public static readonly H : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("H", 24, [new LehrerKatalogLehrbefaehigungEintrag(198, "H", "Hebräisch", null, null)]);

	/**
	 * Lehrbefähigung 'Hauswirtschaft'
	 */
	public static readonly HA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HA", 25, [new LehrerKatalogLehrbefaehigungEintrag(199, "HA", "Hauswirtschaft", null, null)]);

	/**
	 * Lehrbefähigung 'Jüdische Religionslehre'
	 */
	public static readonly HR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HR", 26, [new LehrerKatalogLehrbefaehigungEintrag(200, "HR", "Jüdische Religionslehre", null, null)]);

	/**
	 * Lehrbefähigung 'Hauswirtschaft (-swissenschaft)'
	 */
	public static readonly HW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HW", 27, [new LehrerKatalogLehrbefaehigungEintrag(201, "HW", "Hauswirtschaft (-swissenschaft)", null, null)]);

	/**
	 * Lehrbefähigung 'Italienisch'
	 */
	public static readonly I : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("I", 28, [new LehrerKatalogLehrbefaehigungEintrag(202, "I", "Italienisch", null, null)]);

	/**
	 * Lehrbefähigung 'Informatik (nachgewiesen durch 2. Staatsprüfung, sonst siehe IK)'
	 */
	public static readonly IF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IF", 29, [new LehrerKatalogLehrbefaehigungEintrag(203, "IF", "Informatik (nachgewiesen durch 2. Staatsprüfung, sonst siehe IK)", null, null)]);

	/**
	 * Lehrbefähigung 'Italienisch (Muttersprachl. Unterricht)'
	 */
	public static readonly IM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IM", 30, [new LehrerKatalogLehrbefaehigungEintrag(204, "IM", "Italienisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Islamkunde'
	 */
	public static readonly IR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IR", 31, [new LehrerKatalogLehrbefaehigungEintrag(205, "IR", "Islamkunde", null, null)]);

	/**
	 * Lehrbefähigung 'Japanisch'
	 */
	public static readonly K : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("K", 32, [new LehrerKatalogLehrbefaehigungEintrag(206, "K", "Japanisch", null, null)]);

	/**
	 * Lehrbefähigung 'Kunst/Gestalten'
	 */
	public static readonly KG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KG", 33, [new LehrerKatalogLehrbefaehigungEintrag(207, "KG", "Kunst/Gestalten", null, null)]);

	/**
	 * Lehrbefähigung 'Katholische Religionslehre'
	 */
	public static readonly KR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KR", 34, [new LehrerKatalogLehrbefaehigungEintrag(208, "KR", "Katholische Religionslehre", null, null)]);

	/**
	 * Lehrbefähigung 'Kurzschrift'
	 */
	public static readonly KS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KS", 35, [new LehrerKatalogLehrbefaehigungEintrag(209, "KS", "Kurzschrift", null, null)]);

	/**
	 * Lehrbefähigung 'Kunst / Werken'
	 */
	public static readonly KU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KU", 36, [new LehrerKatalogLehrbefaehigungEintrag(210, "KU", "Kunst / Werken", null, null)]);

	/**
	 * Lehrbefähigung 'Kunstwissenschaft'
	 */
	public static readonly KW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KW", 37, [new LehrerKatalogLehrbefaehigungEintrag(211, "KW", "Kunstwissenschaft", null, null)]);

	/**
	 * Lehrbefähigung 'Lateinisch'
	 */
	public static readonly L : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("L", 38, [new LehrerKatalogLehrbefaehigungEintrag(212, "L", "Lateinisch", null, null)]);

	/**
	 * Lehrbefähigung 'Literaturwissenschaft'
	 */
	public static readonly LI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LI", 39, [new LehrerKatalogLehrbefaehigungEintrag(213, "LI", "Literaturwissenschaft", null, null)]);

	/**
	 * Lehrbefähigung 'Albanisch (Muttersprachl. Unterricht)'
	 */
	public static readonly LM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LM", 40, [new LehrerKatalogLehrbefaehigungEintrag(214, "LM", "Albanisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Linguistik'
	 */
	public static readonly LN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LN", 41, [new LehrerKatalogLehrbefaehigungEintrag(215, "LN", "Linguistik", null, null)]);

	/**
	 * Lehrbefähigung 'Mathematik'
	 */
	public static readonly M : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("M", 42, [new LehrerKatalogLehrbefaehigungEintrag(216, "M", "Mathematik", null, null)]);

	/**
	 * Lehrbefähigung 'Makedonisch (Muttersprachl. Unterricht)'
	 */
	public static readonly MM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MM", 43, [new LehrerKatalogLehrbefaehigungEintrag(217, "MM", "Makedonisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Maschinenschreiben'
	 */
	public static readonly MS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MS", 44, [new LehrerKatalogLehrbefaehigungEintrag(218, "MS", "Maschinenschreiben", null, null)]);

	/**
	 * Lehrbefähigung 'Musik'
	 */
	public static readonly MU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MU", 45, [new LehrerKatalogLehrbefaehigungEintrag(219, "MU", "Musik", null, null)]);

	/**
	 * Lehrbefähigung 'Niederländisch'
	 */
	public static readonly N : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("N", 46, [new LehrerKatalogLehrbefaehigungEintrag(220, "N", "Niederländisch", null, null)]);

	/**
	 * Lehrbefähigung 'Portugiesisch'
	 */
	public static readonly O : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("O", 47, [new LehrerKatalogLehrbefaehigungEintrag(221, "O", "Portugiesisch", null, null)]);

	/**
	 * Lehrbefähigung 'Ohne Angabe'
	 */
	public static readonly OA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OA", 48, [new LehrerKatalogLehrbefaehigungEintrag(222, "OA", "Ohne Angabe", null, null)]);

	/**
	 * Lehrbefähigung 'Portugiesisch (Muttersprachl. Unterricht)'
	 */
	public static readonly OM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OM", 49, [new LehrerKatalogLehrbefaehigungEintrag(223, "OM", "Portugiesisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Griechisch-orthodoxe Religionslehre'
	 */
	public static readonly OR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OR", 50, [new LehrerKatalogLehrbefaehigungEintrag(224, "OR", "Griechisch-orthodoxe Religionslehre", null, null)]);

	/**
	 * Lehrbefähigung 'Pädagogik'
	 */
	public static readonly PA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PA", 51, [new LehrerKatalogLehrbefaehigungEintrag(225, "PA", "Pädagogik", null, null)]);

	/**
	 * Lehrbefähigung 'Physik (Astronomie)'
	 */
	public static readonly PH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PH", 52, [new LehrerKatalogLehrbefaehigungEintrag(226, "PH", "Physik (Astronomie)", null, null)]);

	/**
	 * Lehrbefähigung 'Philosophie/Praktische Philosophie'
	 */
	public static readonly PI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PI", 53, [new LehrerKatalogLehrbefaehigungEintrag(227, "PI", "Philosophie/Praktische Philosophie", null, null)]);

	/**
	 * Lehrbefähigung 'Politik'
	 */
	public static readonly PK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PK", 54, [new LehrerKatalogLehrbefaehigungEintrag(228, "PK", "Politik", null, null)]);

	/**
	 * Lehrbefähigung 'Philosophie'
	 */
	public static readonly PL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PL", 55, [new LehrerKatalogLehrbefaehigungEintrag(229, "PL", "Philosophie", null, null)]);

	/**
	 * Lehrbefähigung 'Polnisch (Muttersprachl. Unterricht)'
	 */
	public static readonly PM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PM", 56, [new LehrerKatalogLehrbefaehigungEintrag(230, "PM", "Polnisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Praktische Philosophie'
	 */
	public static readonly PP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PP", 57, [new LehrerKatalogLehrbefaehigungEintrag(231, "PP", "Praktische Philosophie", null, null)]);

	/**
	 * Lehrbefähigung 'Psychologie'
	 */
	public static readonly PS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PS", 58, [new LehrerKatalogLehrbefaehigungEintrag(232, "PS", "Psychologie", null, null)]);

	/**
	 * Lehrbefähigung 'Russisch'
	 */
	public static readonly R : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("R", 59, [new LehrerKatalogLehrbefaehigungEintrag(233, "R", "Russisch", null, null)]);

	/**
	 * Lehrbefähigung 'Russisch (Muttersprachl. Unterricht)'
	 */
	public static readonly RM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RM", 60, [new LehrerKatalogLehrbefaehigungEintrag(234, "RM", "Russisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Rechtswissenschaft'
	 */
	public static readonly RW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RW", 61, [new LehrerKatalogLehrbefaehigungEintrag(235, "RW", "Rechtswissenschaft", null, null)]);

	/**
	 * Lehrbefähigung 'Spanisch'
	 */
	public static readonly S : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("S", 62, [new LehrerKatalogLehrbefaehigungEintrag(236, "S", "Spanisch", null, null)]);

	/**
	 * Lehrbefähigung 'Sozial- und Erziehungswissenschaft'
	 */
	public static readonly SE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SE", 63, [new LehrerKatalogLehrbefaehigungEintrag(237, "SE", "Sozial- und Erziehungswissenschaft", null, null)]);

	/**
	 * Lehrbefähigung 'Sozialpflege'
	 */
	public static readonly SF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SF", 64, [new LehrerKatalogLehrbefaehigungEintrag(238, "SF", "Sozialpflege", null, null)]);

	/**
	 * Lehrbefähigung 'Spanisch (Muttersprachl. Unterricht)'
	 */
	public static readonly SM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SM", 65, [new LehrerKatalogLehrbefaehigungEintrag(239, "SM", "Spanisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Sonderpädagogik'
	 */
	public static readonly SN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SN", 66, [new LehrerKatalogLehrbefaehigungEintrag(240, "SN", "Sonderpädagogik", null, null)]);

	/**
	 * Lehrbefähigung 'Sozialpädagogik'
	 */
	public static readonly SO : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SO", 67, [new LehrerKatalogLehrbefaehigungEintrag(241, "SO", "Sozialpädagogik", null, null)]);

	/**
	 * Lehrbefähigung 'Sport'
	 */
	public static readonly SP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SP", 68, [new LehrerKatalogLehrbefaehigungEintrag(242, "SP", "Sport", null, null)]);

	/**
	 * Lehrbefähigung 'sonstige Sprachen'
	 */
	public static readonly SR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SR", 69, [new LehrerKatalogLehrbefaehigungEintrag(243, "SR", "sonstige Sprachen", null, null)]);

	/**
	 * Lehrbefähigung 'Sachunterricht'
	 */
	public static readonly SU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SU", 70, [new LehrerKatalogLehrbefaehigungEintrag(244, "SU", "Sachunterricht", null, null)]);

	/**
	 * Lehrbefähigung 'Sozialwissenschaften'
	 */
	public static readonly SW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SW", 71, [new LehrerKatalogLehrbefaehigungEintrag(245, "SW", "Sozialwissenschaften", null, null)]);

	/**
	 * Lehrbefähigung 'Türkisch'
	 */
	public static readonly T : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("T", 72, [new LehrerKatalogLehrbefaehigungEintrag(246, "T", "Türkisch", null, null)]);

	/**
	 * Lehrbefähigung 'Technik'
	 */
	public static readonly TC : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TC", 73, [new LehrerKatalogLehrbefaehigungEintrag(247, "TC", "Technik", null, null)]);

	/**
	 * Lehrbefähigung 'Technologie'
	 */
	public static readonly TE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TE", 74, [new LehrerKatalogLehrbefaehigungEintrag(248, "TE", "Technologie", null, null)]);

	/**
	 * Lehrbefähigung 'Türkisch (Muttersprachl. Unterricht)'
	 */
	public static readonly TM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TM", 75, [new LehrerKatalogLehrbefaehigungEintrag(249, "TM", "Türkisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Textilgestaltung'
	 */
	public static readonly TX : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TX", 76, [new LehrerKatalogLehrbefaehigungEintrag(250, "TX", "Textilgestaltung", null, null)]);

	/**
	 * Lehrbefähigung 'Unterweisung'
	 */
	public static readonly UW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UW", 77, [new LehrerKatalogLehrbefaehigungEintrag(251, "UW", "Unterweisung", null, null)]);

	/**
	 * Lehrbefähigung 'Werken (Musisches)'
	 */
	public static readonly W : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("W", 78, [new LehrerKatalogLehrbefaehigungEintrag(252, "W", "Werken (Musisches)", null, null)]);

	/**
	 * Lehrbefähigung 'Slowenisch (Muttersprachl. Unterricht)'
	 */
	public static readonly WM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WM", 79, [new LehrerKatalogLehrbefaehigungEintrag(253, "WM", "Slowenisch (Muttersprachl. Unterricht)", null, null)]);

	/**
	 * Lehrbefähigung 'Wirtschaftslehre/Politik'
	 */
	public static readonly WP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WP", 80, [new LehrerKatalogLehrbefaehigungEintrag(254, "WP", "Wirtschaftslehre/Politik", null, null)]);

	/**
	 * Lehrbefähigung 'Technisches Werken'
	 */
	public static readonly WT : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WT", 81, [new LehrerKatalogLehrbefaehigungEintrag(255, "WT", "Technisches Werken", null, null)]);

	/**
	 * Lehrbefähigung 'Wirtschafts- und Arbeitslehre'
	 */
	public static readonly WW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WW", 82, [new LehrerKatalogLehrbefaehigungEintrag(256, "WW", "Wirtschafts- und Arbeitslehre", null, null)]);

	/**
	 * Lehrbefähigung 'sonstige Muttersprache'
	 */
	public static readonly XM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("XM", 83, [new LehrerKatalogLehrbefaehigungEintrag(257, "XM", "sonstige Muttersprache", null, null)]);

	/**
	 * Lehrbefähigung 'Syrisch-orthodoxe Religionslehre'
	 */
	public static readonly YR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("YR", 84, [new LehrerKatalogLehrbefaehigungEintrag(258, "YR", "Syrisch-orthodoxe Religionslehre", null, null)]);

	/**
	 * Lehrbefähigung 'Neugriechisch'
	 */
	public static readonly Z : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("Z", 85, [new LehrerKatalogLehrbefaehigungEintrag(259, "Z", "Neugriechisch", null, null)]);

	/**
	 * Lehrbefähigung 'Alevitische Religionslehre nach den Grundsätzen des AABF'
	 */
	public static readonly AR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AR", 86, [new LehrerKatalogLehrbefaehigungEintrag(260, "AR", "Alevitische Religionslehre nach den Grundsätzen des AABF", null, null)]);

	/**
	 * Lehrbefähigung 'Ästhetische Erziehung'
	 */
	public static readonly AE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AE", 87, [new LehrerKatalogLehrbefaehigungEintrag(261, "AE", "Ästhetische Erziehung", null, null)]);

	/**
	 * Lehrbefähigung 'Deutsch für Schülerinnen und Schüler mit Zuwanderungsgeschichte'
	 */
	public static readonly DZ : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DZ", 88, [new LehrerKatalogLehrbefaehigungEintrag(262, "DZ", "Deutsch für Schülerinnen und Schüler mit Zuwanderungsgeschichte", null, null)]);

	/**
	 * Lehrbefähigung 'Mathematische Grundbildung'
	 */
	public static readonly MG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MG", 89, [new LehrerKatalogLehrbefaehigungEintrag(263, "MG", "Mathematische Grundbildung", null, null)]);

	/**
	 * Lehrbefähigung 'Natur- und Gesellschaftswissenschaften'
	 */
	public static readonly NG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NG", 90, [new LehrerKatalogLehrbefaehigungEintrag(264, "NG", "Natur- und Gesellschaftswissenschaften", null, null)]);

	/**
	 * Lehrbefähigung 'Sprachliche Grundbildung'
	 */
	public static readonly SB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SB", 91, [new LehrerKatalogLehrbefaehigungEintrag(265, "SB", "Sprachliche Grundbildung", null, null)]);

	/**
	 * Lehrbefähigung 'Religionslehre der mennonitischen Brüdergemeinden in RW (Lehrerlaubnis)'
	 */
	public static readonly MB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MB", 92, [new LehrerKatalogLehrbefaehigungEintrag(266, "MB", "Religionslehre der mennonitischen Brüdergemeinden in RW (Lehrerlaubnis)", null, null)]);

	/**
	 * Lehrbefähigung 'Bulgarisch (Herkunftssprache)'
	 */
	public static readonly VM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("VM", 93, [new LehrerKatalogLehrbefaehigungEintrag(267, "VM", "Bulgarisch (Herkunftssprache)", null, null)]);

	/**
	 * Lehrbefähigung 'Farsi (Herkunftssprache)'
	 */
	public static readonly QM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("QM", 94, [new LehrerKatalogLehrbefaehigungEintrag(268, "QM", "Farsi (Herkunftssprache)", null, null)]);

	/**
	 * Lehrbefähigung 'Koreanisch (Herkunftssprache)'
	 */
	public static readonly YM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("YM", 95, [new LehrerKatalogLehrbefaehigungEintrag(269, "YM", "Koreanisch (Herkunftssprache)", null, null)]);

	/**
	 * Lehrbefähigung 'Kurdische Sprachen (Herkunftssprache) (Sorani, Komaci, Zaza)'
	 */
	public static readonly ZM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ZM", 96, [new LehrerKatalogLehrbefaehigungEintrag(270, "ZM", "Kurdische Sprachen (Herkunftssprache) (Sorani, Komaci, Zaza)", null, null)]);

	/**
	 * Lehrbefähigung 'Niederländisch (Herkunftssprache)'
	 */
	public static readonly NM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NM", 97, [new LehrerKatalogLehrbefaehigungEintrag(271, "NM", "Niederländisch (Herkunftssprache)", null, null)]);

	/**
	 * Lehrbefähigung 'Rumänisch (Herkunftssprache)'
	 */
	public static readonly UM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UM", 98, [new LehrerKatalogLehrbefaehigungEintrag(272, "UM", "Rumänisch (Herkunftssprache)", null, null)]);

	/**
	 * Lehrbefähigung 'Islamische Religionslehre'
	 */
	public static readonly IL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IL", 99, [new LehrerKatalogLehrbefaehigungEintrag(273, "IL", "Islamische Religionslehre", null, null)]);

	/**
	 * Lehrbefähigung 'Arabisch'
	 */
	public static readonly A : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("A", 100, [new LehrerKatalogLehrbefaehigungEintrag(274, "A", "Arabisch", null, null)]);

	/**
	 * Lehrbefähigung 'Braille´sche Punktschrift'
	 */
	public static readonly BN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BN", 101, [new LehrerKatalogLehrbefaehigungEintrag(275, "BN", "Braille´sche Punktschrift", null, null)]);

	/**
	 * Lehrbefähigung 'Darstellen und Gestalten'
	 */
	public static readonly DS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DS", 102, [new LehrerKatalogLehrbefaehigungEintrag(276, "DS", "Darstellen und Gestalten", null, null)]);

	/**
	 * Lehrbefähigung 'Hauswirtschaft (Konsum/Ernährung/Gesundheit)'
	 */
	public static readonly EL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EL", 103, [new LehrerKatalogLehrbefaehigungEintrag(277, "EL", "Hauswirtschaft (Konsum/Ernährung/Gesundheit)", null, null)]);

	/**
	 * Lehrbefähigung 'Design/Fotografie'
	 */
	public static readonly DF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DF", 104, [new LehrerKatalogLehrbefaehigungEintrag(278, "DF", "Design/Fotografie", null, null)]);

	/**
	 * Lehrbefähigung 'Gesellschaftswissenschaften'
	 */
	public static readonly GW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GW", 105, [new LehrerKatalogLehrbefaehigungEintrag(279, "GW", "Gesellschaftswissenschaften", null, null)]);

	/**
	 * Lehrbefähigung 'Malerei/Grafik/Gestaltung'
	 */
	public static readonly MJ : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MJ", 106, [new LehrerKatalogLehrbefaehigungEintrag(280, "MJ", "Malerei/Grafik/Gestaltung", null, null)]);

	/**
	 * Lehrbefähigung 'Naturwissenschaften'
	 */
	public static readonly NW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NW", 107, [new LehrerKatalogLehrbefaehigungEintrag(281, "NW", "Naturwissenschaften", null, null)]);

	/**
	 * Lehrbefähigung 'Sozialwesen'
	 */
	public static readonly SI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SI", 108, [new LehrerKatalogLehrbefaehigungEintrag(282, "SI", "Sozialwesen", null, null)]);

	/**
	 * Lehrbefähigung 'Zusatzqualifikation Bilinguales Lernen'
	 */
	public static readonly ZB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ZB", 109, [new LehrerKatalogLehrbefaehigungEintrag(283, "ZB", "Zusatzqualifikation Bilinguales Lernen", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Lehrbefähigungen, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogLehrbefaehigungEintrag;

	/**
	 * Die Historie mit den Einträgen der Lehrbefähigungen
	 */
	public readonly historie : Array<LehrerKatalogLehrbefaehigungEintrag>;

	/**
	 * Eine Hashmap mit allen Lehrbefähigungen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _lehrbefaehigungenByID : HashMap<number, LehrerLehrbefaehigung | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Lehrbefähigungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _lehrbefaehigungenByKuerzel : HashMap<string, LehrerLehrbefaehigung | null> = new HashMap();

	/**
	 * Erzeugt eine neue Lehrbefähigung in der Aufzählung.
	 *
	 * @param historie   die Historie der Lehrbefähigungen, welches ein Array von {@link LehrerKatalogLehrbefaehigungEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogLehrbefaehigungEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerLehrbefaehigung.all_values_by_ordinal.push(this);
		LehrerLehrbefaehigung.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 */
	private static getMapLehrbefaehigungByID() : HashMap<number, LehrerLehrbefaehigung | null> {
		if (LehrerLehrbefaehigung._lehrbefaehigungenByID.size() === 0)
			for (const l of LehrerLehrbefaehigung.values())
				LehrerLehrbefaehigung._lehrbefaehigungenByID.put(l.daten.id, l);
		return LehrerLehrbefaehigung._lehrbefaehigungenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 */
	private static getMapLehrbefaehigungByKuerzel() : HashMap<string, LehrerLehrbefaehigung | null> {
		if (LehrerLehrbefaehigung._lehrbefaehigungenByKuerzel.size() === 0)
			for (const l of LehrerLehrbefaehigung.values())
				LehrerLehrbefaehigung._lehrbefaehigungenByKuerzel.put(l.daten.kuerzel, l);
		return LehrerLehrbefaehigung._lehrbefaehigungenByKuerzel;
	}

	/**
	 * Gibt die Lehrbefähigung anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID der Lehrbefähigung
	 *
	 * @return die Lehrbefähigung oder null, falls die IF ungültig ist
	 */
	public static getByID(id : number) : LehrerLehrbefaehigung | null {
		return LehrerLehrbefaehigung.getMapLehrbefaehigungByID().get(id);
	}

	/**
	 * Gibt die Lehrbefähigung anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Lehrbefähigung
	 *
	 * @return die Lehrbefähigung oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerLehrbefaehigung | null {
		return LehrerLehrbefaehigung.getMapLehrbefaehigungByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
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
		if (!(other instanceof LehrerLehrbefaehigung))
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
	public compareTo(other : LehrerLehrbefaehigung) : number {
		return this.__ordinal - other.__ordinal;
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
		const tmp : LehrerLehrbefaehigung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.lehrer.LehrerLehrbefaehigung', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_lehrer_LehrerLehrbefaehigung(obj : unknown) : LehrerLehrbefaehigung {
	return obj as LehrerLehrbefaehigung;
}
