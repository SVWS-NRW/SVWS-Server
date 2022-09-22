import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { LehrerKatalogLehrbefaehigungEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLehrbefaehigungEintrag } from '../../../core/data/lehrer/LehrerKatalogLehrbefaehigungEintrag';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerLehrbefaehigung extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerLehrbefaehigung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, LehrerLehrbefaehigung> = new Map<String, LehrerLehrbefaehigung>();

	public static readonly AH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AH", 0, [new LehrerKatalogLehrbefaehigungEintrag(174, "AH", "Arbeitslehre / Schwerpunkt Haushaltslehre", null, null)]);

	public static readonly AL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AL", 1, [new LehrerKatalogLehrbefaehigungEintrag(175, "AL", "Arbeitslehre", null, null)]);

	public static readonly AM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AM", 2, [new LehrerKatalogLehrbefaehigungEintrag(176, "AM", "Arabisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly AT : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AT", 3, [new LehrerKatalogLehrbefaehigungEintrag(177, "AT", "Arbeitslehre / Schwerpunkt Technik", null, null)]);

	public static readonly AW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AW", 4, [new LehrerKatalogLehrbefaehigungEintrag(178, "AW", "Arbeitslehre / Wirtschaft", null, null)]);

	public static readonly BE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BE", 5, [new LehrerKatalogLehrbefaehigungEintrag(179, "BE", "Betreuung", null, null)]);

	public static readonly BI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BI", 6, [new LehrerKatalogLehrbefaehigungEintrag(180, "BI", "Biologie", null, null)]);

	public static readonly BM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BM", 7, [new LehrerKatalogLehrbefaehigungEintrag(181, "BM", "Bosnisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly C : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("C", 8, [new LehrerKatalogLehrbefaehigungEintrag(182, "C", "Chinesisch", null, null)]);

	public static readonly CH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CH", 9, [new LehrerKatalogLehrbefaehigungEintrag(183, "CH", "Chemie", null, null)]);

	public static readonly CM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CM", 10, [new LehrerKatalogLehrbefaehigungEintrag(184, "CM", "Kroatisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly D : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("D", 11, [new LehrerKatalogLehrbefaehigungEintrag(185, "D", "Deutsch", null, null)]);

	public static readonly E : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("E", 12, [new LehrerKatalogLehrbefaehigungEintrag(186, "E", "Englisch", null, null)]);

	public static readonly EK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EK", 13, [new LehrerKatalogLehrbefaehigungEintrag(187, "EK", "Erdkunde", null, null)]);

	public static readonly EM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EM", 14, [new LehrerKatalogLehrbefaehigungEintrag(188, "EM", "Serbisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly ER : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ER", 15, [new LehrerKatalogLehrbefaehigungEintrag(189, "ER", "Evangelische Religionslehre", null, null)]);

	public static readonly F : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("F", 16, [new LehrerKatalogLehrbefaehigungEintrag(190, "F", "Französisch", null, null)]);

	public static readonly FP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("FP", 17, [new LehrerKatalogLehrbefaehigungEintrag(191, "FP", "Fachpraxis", null, null)]);

	public static readonly G : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("G", 18, [new LehrerKatalogLehrbefaehigungEintrag(192, "G", "Griechisch", null, null)]);

	public static readonly GE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GE", 19, [new LehrerKatalogLehrbefaehigungEintrag(193, "GE", "Geschichte", null, null)]);

	public static readonly GM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GM", 20, [new LehrerKatalogLehrbefaehigungEintrag(194, "GM", "Griechisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly GP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GP", 21, [new LehrerKatalogLehrbefaehigungEintrag(195, "GP", "Geschichte / Politische Bildung", null, null)]);

	public static readonly GS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GS", 22, [new LehrerKatalogLehrbefaehigungEintrag(196, "GS", "Kunst/Gestalten (Kunst oder Textilgestaltung)", null, null)]);

	public static readonly GU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GU", 23, [new LehrerKatalogLehrbefaehigungEintrag(197, "GU", "Gesamtunterricht", null, null)]);

	public static readonly H : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("H", 24, [new LehrerKatalogLehrbefaehigungEintrag(198, "H", "Hebräisch", null, null)]);

	public static readonly HA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HA", 25, [new LehrerKatalogLehrbefaehigungEintrag(199, "HA", "Hauswirtschaft", null, null)]);

	public static readonly HR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HR", 26, [new LehrerKatalogLehrbefaehigungEintrag(200, "HR", "Jüdische Religionslehre", null, null)]);

	public static readonly HW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HW", 27, [new LehrerKatalogLehrbefaehigungEintrag(201, "HW", "Hauswirtschaft (-swissenschaft)", null, null)]);

	public static readonly I : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("I", 28, [new LehrerKatalogLehrbefaehigungEintrag(202, "I", "Italienisch", null, null)]);

	public static readonly IF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IF", 29, [new LehrerKatalogLehrbefaehigungEintrag(203, "IF", "Informatik (nachgewiesen durch 2. Staatsprüfung, sonst siehe IK)", null, null)]);

	public static readonly IM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IM", 30, [new LehrerKatalogLehrbefaehigungEintrag(204, "IM", "Italienisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly IR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IR", 31, [new LehrerKatalogLehrbefaehigungEintrag(205, "IR", "Islamkunde", null, null)]);

	public static readonly K : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("K", 32, [new LehrerKatalogLehrbefaehigungEintrag(206, "K", "Japanisch", null, null)]);

	public static readonly KG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KG", 33, [new LehrerKatalogLehrbefaehigungEintrag(207, "KG", "Kunst/Gestalten", null, null)]);

	public static readonly KR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KR", 34, [new LehrerKatalogLehrbefaehigungEintrag(208, "KR", "Katholische Religionslehre", null, null)]);

	public static readonly KS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KS", 35, [new LehrerKatalogLehrbefaehigungEintrag(209, "KS", "Kurzschrift", null, null)]);

	public static readonly KU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KU", 36, [new LehrerKatalogLehrbefaehigungEintrag(210, "KU", "Kunst / Werken", null, null)]);

	public static readonly KW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KW", 37, [new LehrerKatalogLehrbefaehigungEintrag(211, "KW", "Kunstwissenschaft", null, null)]);

	public static readonly L : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("L", 38, [new LehrerKatalogLehrbefaehigungEintrag(212, "L", "Lateinisch", null, null)]);

	public static readonly LI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LI", 39, [new LehrerKatalogLehrbefaehigungEintrag(213, "LI", "Literaturwissenschaft", null, null)]);

	public static readonly LM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LM", 40, [new LehrerKatalogLehrbefaehigungEintrag(214, "LM", "Albanisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly LN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LN", 41, [new LehrerKatalogLehrbefaehigungEintrag(215, "LN", "Linguistik", null, null)]);

	public static readonly M : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("M", 42, [new LehrerKatalogLehrbefaehigungEintrag(216, "M", "Mathematik", null, null)]);

	public static readonly MM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MM", 43, [new LehrerKatalogLehrbefaehigungEintrag(217, "MM", "Makedonisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly MS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MS", 44, [new LehrerKatalogLehrbefaehigungEintrag(218, "MS", "Maschinenschreiben", null, null)]);

	public static readonly MU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MU", 45, [new LehrerKatalogLehrbefaehigungEintrag(219, "MU", "Musik", null, null)]);

	public static readonly N : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("N", 46, [new LehrerKatalogLehrbefaehigungEintrag(220, "N", "Niederländisch", null, null)]);

	public static readonly O : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("O", 47, [new LehrerKatalogLehrbefaehigungEintrag(221, "O", "Portugiesisch", null, null)]);

	public static readonly OA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OA", 48, [new LehrerKatalogLehrbefaehigungEintrag(222, "OA", "Ohne Angabe", null, null)]);

	public static readonly OM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OM", 49, [new LehrerKatalogLehrbefaehigungEintrag(223, "OM", "Portugiesisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly OR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OR", 50, [new LehrerKatalogLehrbefaehigungEintrag(224, "OR", "Griechisch-orthodoxe Religionslehre", null, null)]);

	public static readonly PA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PA", 51, [new LehrerKatalogLehrbefaehigungEintrag(225, "PA", "Pädagogik", null, null)]);

	public static readonly PH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PH", 52, [new LehrerKatalogLehrbefaehigungEintrag(226, "PH", "Physik (Astronomie)", null, null)]);

	public static readonly PI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PI", 53, [new LehrerKatalogLehrbefaehigungEintrag(227, "PI", "Philosophie/Praktische Philosophie", null, null)]);

	public static readonly PK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PK", 54, [new LehrerKatalogLehrbefaehigungEintrag(228, "PK", "Politik", null, null)]);

	public static readonly PL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PL", 55, [new LehrerKatalogLehrbefaehigungEintrag(229, "PL", "Philosophie", null, null)]);

	public static readonly PM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PM", 56, [new LehrerKatalogLehrbefaehigungEintrag(230, "PM", "Polnisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly PP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PP", 57, [new LehrerKatalogLehrbefaehigungEintrag(231, "PP", "Praktische Philosophie", null, null)]);

	public static readonly PS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PS", 58, [new LehrerKatalogLehrbefaehigungEintrag(232, "PS", "Psychologie", null, null)]);

	public static readonly R : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("R", 59, [new LehrerKatalogLehrbefaehigungEintrag(233, "R", "Russisch", null, null)]);

	public static readonly RM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RM", 60, [new LehrerKatalogLehrbefaehigungEintrag(234, "RM", "Russisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly RW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RW", 61, [new LehrerKatalogLehrbefaehigungEintrag(235, "RW", "Rechtswissenschaft", null, null)]);

	public static readonly S : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("S", 62, [new LehrerKatalogLehrbefaehigungEintrag(236, "S", "Spanisch", null, null)]);

	public static readonly SE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SE", 63, [new LehrerKatalogLehrbefaehigungEintrag(237, "SE", "Sozial- und Erziehungswissenschaft", null, null)]);

	public static readonly SF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SF", 64, [new LehrerKatalogLehrbefaehigungEintrag(238, "SF", "Sozialpflege", null, null)]);

	public static readonly SM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SM", 65, [new LehrerKatalogLehrbefaehigungEintrag(239, "SM", "Spanisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly SN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SN", 66, [new LehrerKatalogLehrbefaehigungEintrag(240, "SN", "Sonderpädagogik", null, null)]);

	public static readonly SO : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SO", 67, [new LehrerKatalogLehrbefaehigungEintrag(241, "SO", "Sozialpädagogik", null, null)]);

	public static readonly SP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SP", 68, [new LehrerKatalogLehrbefaehigungEintrag(242, "SP", "Sport", null, null)]);

	public static readonly SR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SR", 69, [new LehrerKatalogLehrbefaehigungEintrag(243, "SR", "sonstige Sprachen", null, null)]);

	public static readonly SU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SU", 70, [new LehrerKatalogLehrbefaehigungEintrag(244, "SU", "Sachunterricht", null, null)]);

	public static readonly SW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SW", 71, [new LehrerKatalogLehrbefaehigungEintrag(245, "SW", "Sozialwissenschaften", null, null)]);

	public static readonly T : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("T", 72, [new LehrerKatalogLehrbefaehigungEintrag(246, "T", "Türkisch", null, null)]);

	public static readonly TC : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TC", 73, [new LehrerKatalogLehrbefaehigungEintrag(247, "TC", "Technik", null, null)]);

	public static readonly TE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TE", 74, [new LehrerKatalogLehrbefaehigungEintrag(248, "TE", "Technologie", null, null)]);

	public static readonly TM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TM", 75, [new LehrerKatalogLehrbefaehigungEintrag(249, "TM", "Türkisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly TX : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TX", 76, [new LehrerKatalogLehrbefaehigungEintrag(250, "TX", "Textilgestaltung", null, null)]);

	public static readonly UW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UW", 77, [new LehrerKatalogLehrbefaehigungEintrag(251, "UW", "Unterweisung", null, null)]);

	public static readonly W : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("W", 78, [new LehrerKatalogLehrbefaehigungEintrag(252, "W", "Werken (Musisches)", null, null)]);

	public static readonly WM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WM", 79, [new LehrerKatalogLehrbefaehigungEintrag(253, "WM", "Slowenisch (Muttersprachl. Unterricht)", null, null)]);

	public static readonly WP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WP", 80, [new LehrerKatalogLehrbefaehigungEintrag(254, "WP", "Wirtschaftslehre/Politik", null, null)]);

	public static readonly WT : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WT", 81, [new LehrerKatalogLehrbefaehigungEintrag(255, "WT", "Technisches Werken", null, null)]);

	public static readonly WW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WW", 82, [new LehrerKatalogLehrbefaehigungEintrag(256, "WW", "Wirtschafts- und Arbeitslehre", null, null)]);

	public static readonly XM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("XM", 83, [new LehrerKatalogLehrbefaehigungEintrag(257, "XM", "sonstige Muttersprache", null, null)]);

	public static readonly YR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("YR", 84, [new LehrerKatalogLehrbefaehigungEintrag(258, "YR", "Syrisch-orthodoxe Religionslehre", null, null)]);

	public static readonly Z : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("Z", 85, [new LehrerKatalogLehrbefaehigungEintrag(259, "Z", "Neugriechisch", null, null)]);

	public static readonly AR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AR", 86, [new LehrerKatalogLehrbefaehigungEintrag(260, "AR", "Alevitische Religionslehre nach den Grundsätzen des AABF", null, null)]);

	public static readonly AE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AE", 87, [new LehrerKatalogLehrbefaehigungEintrag(261, "AE", "Ästhetische Erziehung", null, null)]);

	public static readonly DZ : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DZ", 88, [new LehrerKatalogLehrbefaehigungEintrag(262, "DZ", "Deutsch für Schülerinnen und Schüler mit Zuwanderungsgeschichte", null, null)]);

	public static readonly MG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MG", 89, [new LehrerKatalogLehrbefaehigungEintrag(263, "MG", "Mathematische Grundbildung", null, null)]);

	public static readonly NG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NG", 90, [new LehrerKatalogLehrbefaehigungEintrag(264, "NG", "Natur- und Gesellschaftswissenschaften", null, null)]);

	public static readonly SB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SB", 91, [new LehrerKatalogLehrbefaehigungEintrag(265, "SB", "Sprachliche Grundbildung", null, null)]);

	public static readonly MB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MB", 92, [new LehrerKatalogLehrbefaehigungEintrag(266, "MB", "Religionslehre der mennonitischen Brüdergemeinden in RW (Lehrerlaubnis)", null, null)]);

	public static readonly VM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("VM", 93, [new LehrerKatalogLehrbefaehigungEintrag(267, "VM", "Bulgarisch (Herkunftssprache)", null, null)]);

	public static readonly QM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("QM", 94, [new LehrerKatalogLehrbefaehigungEintrag(268, "QM", "Farsi (Herkunftssprache)", null, null)]);

	public static readonly YM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("YM", 95, [new LehrerKatalogLehrbefaehigungEintrag(269, "YM", "Koreanisch (Herkunftssprache)", null, null)]);

	public static readonly ZM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ZM", 96, [new LehrerKatalogLehrbefaehigungEintrag(270, "ZM", "Kurdische Sprachen (Herkunftssprache) (Sorani, Komaci, Zaza)", null, null)]);

	public static readonly NM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NM", 97, [new LehrerKatalogLehrbefaehigungEintrag(271, "NM", "Niederländisch (Herkunftssprache)", null, null)]);

	public static readonly UM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UM", 98, [new LehrerKatalogLehrbefaehigungEintrag(272, "UM", "Rumänisch (Herkunftssprache)", null, null)]);

	public static readonly IL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IL", 99, [new LehrerKatalogLehrbefaehigungEintrag(273, "IL", "Islamische Religionslehre", null, null)]);

	public static readonly A : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("A", 100, [new LehrerKatalogLehrbefaehigungEintrag(274, "A", "Arabisch", null, null)]);

	public static readonly BN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BN", 101, [new LehrerKatalogLehrbefaehigungEintrag(275, "BN", "Braille´sche Punktschrift", null, null)]);

	public static readonly DS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DS", 102, [new LehrerKatalogLehrbefaehigungEintrag(276, "DS", "Darstellen und Gestalten", null, null)]);

	public static readonly EL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EL", 103, [new LehrerKatalogLehrbefaehigungEintrag(277, "EL", "Hauswirtschaft (Konsum/Ernährung/Gesundheit)", null, null)]);

	public static readonly DF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DF", 104, [new LehrerKatalogLehrbefaehigungEintrag(278, "DF", "Design/Fotografie", null, null)]);

	public static readonly GW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GW", 105, [new LehrerKatalogLehrbefaehigungEintrag(279, "GW", "Gesellschaftswissenschaften", null, null)]);

	public static readonly MJ : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MJ", 106, [new LehrerKatalogLehrbefaehigungEintrag(280, "MJ", "Malerei/Grafik/Gestaltung", null, null)]);

	public static readonly NW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NW", 107, [new LehrerKatalogLehrbefaehigungEintrag(281, "NW", "Naturwissenschaften", null, null)]);

	public static readonly SI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SI", 108, [new LehrerKatalogLehrbefaehigungEintrag(282, "SI", "Sozialwesen", null, null)]);

	public static readonly ZB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ZB", 109, [new LehrerKatalogLehrbefaehigungEintrag(283, "ZB", "Zusatzqualifikation Bilinguales Lernen", null, null)]);

	public static VERSION : number = 1;

	public readonly daten : LehrerKatalogLehrbefaehigungEintrag;

	public readonly historie : Array<LehrerKatalogLehrbefaehigungEintrag>;

	private static readonly _lehrbefaehigungenByID : HashMap<Number, LehrerLehrbefaehigung> = new HashMap();

	private static readonly _lehrbefaehigungenByKuerzel : HashMap<String, LehrerLehrbefaehigung> = new HashMap();

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
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 */
	private static getMapLehrbefaehigungByID() : HashMap<Number, LehrerLehrbefaehigung> {
		if (LehrerLehrbefaehigung._lehrbefaehigungenByID.size() === 0) 
			for (let l of LehrerLehrbefaehigung.values()) 
				LehrerLehrbefaehigung._lehrbefaehigungenByID.put(l.daten.id, l);
		return LehrerLehrbefaehigung._lehrbefaehigungenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 */
	private static getMapLehrbefaehigungByKuerzel() : HashMap<String, LehrerLehrbefaehigung> {
		if (LehrerLehrbefaehigung._lehrbefaehigungenByKuerzel.size() === 0) 
			for (let l of LehrerLehrbefaehigung.values()) 
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
	public static getByKuerzel(kuerzel : String | null) : LehrerLehrbefaehigung | null {
		return LehrerLehrbefaehigung.getMapLehrbefaehigungByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
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
	public toString() : String {
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
	public static valueOf(name : String) : LehrerLehrbefaehigung | null {
		let tmp : LehrerLehrbefaehigung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.statkue.LehrerLehrbefaehigung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_statkue_LehrerLehrbefaehigung(obj : unknown) : LehrerLehrbefaehigung {
	return obj as LehrerLehrbefaehigung;
}
