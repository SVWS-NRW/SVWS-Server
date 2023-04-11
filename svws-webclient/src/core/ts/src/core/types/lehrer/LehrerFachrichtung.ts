import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerKatalogFachrichtungEintrag } from '../../../core/data/lehrer/LehrerKatalogFachrichtungEintrag';

export class LehrerFachrichtung extends JavaObject implements JavaEnum<LehrerFachrichtung> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerFachrichtung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LehrerFachrichtung> = new Map<string, LehrerFachrichtung>();

	/**
	 * Fachrichtung 'Kraftfahrzeugtechnik'
	 */
	public static readonly ID_03 : LehrerFachrichtung = new LehrerFachrichtung("ID_03", 0, [new LehrerKatalogFachrichtungEintrag(78, "03", "Kraftfahrzeugtechnik", null, null)]);

	/**
	 * Fachrichtung 'Metalltechnik, Maschinenbau (außer Kfz), Verfahrens-, Fertigungstechnik'
	 */
	public static readonly ID_04 : LehrerFachrichtung = new LehrerFachrichtung("ID_04", 1, [new LehrerKatalogFachrichtungEintrag(79, "04", "Metalltechnik, Maschinenbau (außer Kfz), Verfahrens-, Fertigungstechnik", null, null)]);

	/**
	 * Fachrichtung 'Sanitär-, Heizungs-, Klima-, Lüftungstechnik'
	 */
	public static readonly ID_05 : LehrerFachrichtung = new LehrerFachrichtung("ID_05", 2, [new LehrerKatalogFachrichtungEintrag(80, "05", "Sanitär-, Heizungs-, Klima-, Lüftungstechnik", null, null)]);

	/**
	 * Fachrichtung 'Bergtechnik, Bergbau'
	 */
	public static readonly ID_08 : LehrerFachrichtung = new LehrerFachrichtung("ID_08", 3, [new LehrerKatalogFachrichtungEintrag(81, "08", "Bergtechnik, Bergbau", null, null)]);

	/**
	 * Fachrichtung 'Hütten-, Gießereiwesen'
	 */
	public static readonly ID_09 : LehrerFachrichtung = new LehrerFachrichtung("ID_09", 4, [new LehrerKatalogFachrichtungEintrag(82, "09", "Hütten-, Gießereiwesen", null, null)]);

	/**
	 * Fachrichtung 'Elektrotechnik: Energietechnik'
	 */
	public static readonly ID_11 : LehrerFachrichtung = new LehrerFachrichtung("ID_11", 5, [new LehrerKatalogFachrichtungEintrag(83, "11", "Elektrotechnik: Energietechnik", null, null)]);

	/**
	 * Fachrichtung 'Elektrotechnik: Nachrichtentechnik'
	 */
	public static readonly ID_12 : LehrerFachrichtung = new LehrerFachrichtung("ID_12", 6, [new LehrerKatalogFachrichtungEintrag(84, "12", "Elektrotechnik: Nachrichtentechnik", null, null)]);

	/**
	 * Fachrichtung 'Bautechnik, Bauingenieurwesen'
	 */
	public static readonly ID_21 : LehrerFachrichtung = new LehrerFachrichtung("ID_21", 7, [new LehrerKatalogFachrichtungEintrag(85, "21", "Bautechnik, Bauingenieurwesen", null, null)]);

	/**
	 * Fachrichtung 'Holztechnik'
	 */
	public static readonly ID_22 : LehrerFachrichtung = new LehrerFachrichtung("ID_22", 8, [new LehrerKatalogFachrichtungEintrag(86, "22", "Holztechnik", null, null)]);

	/**
	 * Fachrichtung 'Architektur'
	 */
	public static readonly ID_23 : LehrerFachrichtung = new LehrerFachrichtung("ID_23", 9, [new LehrerKatalogFachrichtungEintrag(87, "23", "Architektur", null, null)]);

	/**
	 * Fachrichtung 'Textil-, Bekleidungstechnik, Textilgestaltung'
	 */
	public static readonly ID_31 : LehrerFachrichtung = new LehrerFachrichtung("ID_31", 10, [new LehrerKatalogFachrichtungEintrag(88, "31", "Textil-, Bekleidungstechnik, Textilgestaltung", null, null)]);

	/**
	 * Fachrichtung 'Informatik'
	 */
	public static readonly ID_35 : LehrerFachrichtung = new LehrerFachrichtung("ID_35", 11, [new LehrerKatalogFachrichtungEintrag(89, "35", "Informatik", null, null)]);

	/**
	 * Fachrichtung 'Chemie- / Verfahrens- / Chemotechnik'
	 */
	public static readonly ID_41 : LehrerFachrichtung = new LehrerFachrichtung("ID_41", 12, [new LehrerKatalogFachrichtungEintrag(90, "41", "Chemie- / Verfahrens- / Chemotechnik", null, null)]);

	/**
	 * Fachrichtung 'Lebensmittelchemie'
	 */
	public static readonly ID_42 : LehrerFachrichtung = new LehrerFachrichtung("ID_42", 13, [new LehrerKatalogFachrichtungEintrag(91, "42", "Lebensmittelchemie", null, null)]);

	/**
	 * Fachrichtung 'Farb- u. Raumgestaltung, Gestaltungstechnik, Malerei, Graphik, Design, Fotografie, Glas- und Keramiktechnik'
	 */
	public static readonly ID_55 : LehrerFachrichtung = new LehrerFachrichtung("ID_55", 14, [new LehrerKatalogFachrichtungEintrag(92, "55", "Farb- u. Raumgestaltung, Gestaltungstechnik, Malerei, Graphik, Design, Fotografie, Glas- und Keramiktechnik", null, null)]);

	/**
	 * Fachrichtung 'Wirtschaftswissenschaft in kaufmännischen Berufen'
	 */
	public static readonly ID_60 : LehrerFachrichtung = new LehrerFachrichtung("ID_60", 15, [new LehrerKatalogFachrichtungEintrag(93, "60", "Wirtschaftswissenschaft in kaufmännischen Berufen", null, null)]);

	/**
	 * Fachrichtung 'Hauswirtschaftswissenschaft'
	 */
	public static readonly ID_65 : LehrerFachrichtung = new LehrerFachrichtung("ID_65", 16, [new LehrerKatalogFachrichtungEintrag(94, "65", "Hauswirtschaftswissenschaft", null, null)]);

	/**
	 * Fachrichtung 'Ernährungswissenschaft'
	 */
	public static readonly ID_66 : LehrerFachrichtung = new LehrerFachrichtung("ID_66", 17, [new LehrerKatalogFachrichtungEintrag(95, "66", "Ernährungswissenschaft", null, null)]);

	/**
	 * Fachrichtung 'Sozialwesen, Sozialpädagogik, Sozialpflege, Sozialwissenschaft'
	 */
	public static readonly ID_70 : LehrerFachrichtung = new LehrerFachrichtung("ID_70", 18, [new LehrerKatalogFachrichtungEintrag(96, "70", "Sozialwesen, Sozialpädagogik, Sozialpflege, Sozialwissenschaft", null, null)]);

	/**
	 * Fachrichtung 'Psychologie'
	 */
	public static readonly ID_71 : LehrerFachrichtung = new LehrerFachrichtung("ID_71", 19, [new LehrerKatalogFachrichtungEintrag(97, "71", "Psychologie", null, null)]);

	/**
	 * Fachrichtung 'Körperpflege / Biotechnik'
	 */
	public static readonly ID_80 : LehrerFachrichtung = new LehrerFachrichtung("ID_80", 20, [new LehrerKatalogFachrichtungEintrag(98, "80", "Körperpflege / Biotechnik", null, null)]);

	/**
	 * Fachrichtung 'Landbauwissenschaft, Agrarwissenschaft, Agrarwirtschaft'
	 */
	public static readonly ID_91 : LehrerFachrichtung = new LehrerFachrichtung("ID_91", 21, [new LehrerKatalogFachrichtungEintrag(99, "91", "Landbauwissenschaft, Agrarwissenschaft, Agrarwirtschaft", null, null)]);

	/**
	 * Fachrichtung 'Gartenbauwissenschaft'
	 */
	public static readonly ID_92 : LehrerFachrichtung = new LehrerFachrichtung("ID_92", 22, [new LehrerKatalogFachrichtungEintrag(100, "92", "Gartenbauwissenschaft", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Blinden'
	 */
	public static readonly ID_BL : LehrerFachrichtung = new LehrerFachrichtung("ID_BL", 23, [new LehrerKatalogFachrichtungEintrag(101, "BL", "Fachrichtung für Sondererziehung und Rehabilitation der Blinden", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Erziehungsschwierigen'
	 */
	public static readonly ID_EZ : LehrerFachrichtung = new LehrerFachrichtung("ID_EZ", 24, [new LehrerKatalogFachrichtungEintrag(102, "EZ", "Fachrichtung für Sondererziehung und Rehabilitation der Erziehungsschwierigen", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Geistigbehinderten'
	 */
	public static readonly ID_GB : LehrerFachrichtung = new LehrerFachrichtung("ID_GB", 25, [new LehrerKatalogFachrichtungEintrag(103, "GB", "Fachrichtung für Sondererziehung und Rehabilitation der Geistigbehinderten", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Gehörlosen'
	 */
	public static readonly ID_GH : LehrerFachrichtung = new LehrerFachrichtung("ID_GH", 26, [new LehrerKatalogFachrichtungEintrag(104, "GH", "Fachrichtung für Sondererziehung und Rehabilitation der Gehörlosen", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Körperbehinderten'
	 */
	public static readonly ID_KB : LehrerFachrichtung = new LehrerFachrichtung("ID_KB", 27, [new LehrerKatalogFachrichtungEintrag(105, "KB", "Fachrichtung für Sondererziehung und Rehabilitation der Körperbehinderten", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Lernbehinderten'
	 */
	public static readonly ID_LB : LehrerFachrichtung = new LehrerFachrichtung("ID_LB", 28, [new LehrerKatalogFachrichtungEintrag(106, "LB", "Fachrichtung für Sondererziehung und Rehabilitation der Lernbehinderten", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Sprachbehinderten'
	 */
	public static readonly ID_SB : LehrerFachrichtung = new LehrerFachrichtung("ID_SB", 29, [new LehrerKatalogFachrichtungEintrag(107, "SB", "Fachrichtung für Sondererziehung und Rehabilitation der Sprachbehinderten", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Schwerhörigen'
	 */
	public static readonly ID_SG : LehrerFachrichtung = new LehrerFachrichtung("ID_SG", 30, [new LehrerKatalogFachrichtungEintrag(108, "SG", "Fachrichtung für Sondererziehung und Rehabilitation der Schwerhörigen", null, null)]);

	/**
	 * Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Sehbehinderten'
	 */
	public static readonly ID_SH : LehrerFachrichtung = new LehrerFachrichtung("ID_SH", 31, [new LehrerKatalogFachrichtungEintrag(109, "SH", "Fachrichtung für Sondererziehung und Rehabilitation der Sehbehinderten", null, null)]);

	/**
	 * Fachrichtung 'Drucktechnik, Reproduktionstechnik'
	 */
	public static readonly ID_51 : LehrerFachrichtung = new LehrerFachrichtung("ID_51", 32, [new LehrerKatalogFachrichtungEintrag(110, "51", "Drucktechnik, Reproduktionstechnik", null, null)]);

	/**
	 * Fachrichtung 'Fertigungstechnik (spez. Fachrichtung)'
	 */
	public static readonly ID_FT : LehrerFachrichtung = new LehrerFachrichtung("ID_FT", 33, [new LehrerKatalogFachrichtungEintrag(111, "FT", "Fertigungstechnik (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Fahrzeugtechnik (spez. Fachrichtung)'
	 */
	public static readonly ID_KT : LehrerFachrichtung = new LehrerFachrichtung("ID_KT", 34, [new LehrerKatalogFachrichtungEintrag(112, "KT", "Fahrzeugtechnik (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Versorgungstechnik (spez. Fachrichtung)'
	 */
	public static readonly ID_VT : LehrerFachrichtung = new LehrerFachrichtung("ID_VT", 35, [new LehrerKatalogFachrichtungEintrag(113, "VT", "Versorgungstechnik (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Technische Informatik (spez. Fachrichtung)'
	 */
	public static readonly ID_TI : LehrerFachrichtung = new LehrerFachrichtung("ID_TI", 36, [new LehrerKatalogFachrichtungEintrag(114, "TI", "Technische Informatik (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Energietechnik (spez. Fachrichtung)'
	 */
	public static readonly ID_ET : LehrerFachrichtung = new LehrerFachrichtung("ID_ET", 37, [new LehrerKatalogFachrichtungEintrag(115, "ET", "Energietechnik (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Nachrichtentechnik (spez. Fachrichtung)'
	 */
	public static readonly ID_NT : LehrerFachrichtung = new LehrerFachrichtung("ID_NT", 38, [new LehrerKatalogFachrichtungEintrag(116, "NT", "Nachrichtentechnik (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Hochbau (spez. Fachrichtung)'
	 */
	public static readonly ID_HC : LehrerFachrichtung = new LehrerFachrichtung("ID_HC", 39, [new LehrerKatalogFachrichtungEintrag(117, "HC", "Hochbau (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Holztechnik (spez. Fachrichtung)'
	 */
	public static readonly ID_HT : LehrerFachrichtung = new LehrerFachrichtung("ID_HT", 40, [new LehrerKatalogFachrichtungEintrag(118, "HT", "Holztechnik (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Tiefbau (spez. Fachrichtung)'
	 */
	public static readonly ID_TB : LehrerFachrichtung = new LehrerFachrichtung("ID_TB", 41, [new LehrerKatalogFachrichtungEintrag(119, "TB", "Tiefbau (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Banken, Bankbetriebslehre / Finanzwirtschaft'
	 */
	public static readonly ID_BK : LehrerFachrichtung = new LehrerFachrichtung("ID_BK", 42, [new LehrerKatalogFachrichtungEintrag(120, "BK", "Banken, Bankbetriebslehre / Finanzwirtschaft", null, null)]);

	/**
	 * Fachrichtung 'Handel, Handelsbetriebslehre / Absatzwirtschaft'
	 */
	public static readonly ID_HB : LehrerFachrichtung = new LehrerFachrichtung("ID_HB", 43, [new LehrerKatalogFachrichtungEintrag(121, "HB", "Handel, Handelsbetriebslehre / Absatzwirtschaft", null, null)]);

	/**
	 * Fachrichtung 'Industrie, Industriebetriebslehre / Produktionswirtschaft'
	 */
	public static readonly ID_IB : LehrerFachrichtung = new LehrerFachrichtung("ID_IB", 44, [new LehrerKatalogFachrichtungEintrag(122, "IB", "Industrie, Industriebetriebslehre / Produktionswirtschaft", null, null)]);

	/**
	 * Fachrichtung 'Verkehr'
	 */
	public static readonly ID_VR : LehrerFachrichtung = new LehrerFachrichtung("ID_VR", 45, [new LehrerKatalogFachrichtungEintrag(123, "VR", "Verkehr", null, null)]);

	/**
	 * Fachrichtung 'Versicherung'
	 */
	public static readonly ID_VL : LehrerFachrichtung = new LehrerFachrichtung("ID_VL", 46, [new LehrerKatalogFachrichtungEintrag(124, "VL", "Versicherung", null, null)]);

	/**
	 * Fachrichtung 'Organisation und Bürokommunikation / Datenverarbeitung / Organisationslehre / Betriebsinformatik'
	 */
	public static readonly ID_OL : LehrerFachrichtung = new LehrerFachrichtung("ID_OL", 47, [new LehrerKatalogFachrichtungEintrag(125, "OL", "Organisation und Bürokommunikation / Datenverarbeitung / Organisationslehre / Betriebsinformatik", null, null)]);

	/**
	 * Fachrichtung 'Betriebswirtschaftliche Steuerlehre / Revisions- und Treuhandwesen'
	 */
	public static readonly ID_ST : LehrerFachrichtung = new LehrerFachrichtung("ID_ST", 48, [new LehrerKatalogFachrichtungEintrag(126, "ST", "Betriebswirtschaftliche Steuerlehre / Revisions- und Treuhandwesen", null, null)]);

	/**
	 * Fachrichtung 'Wirtschaftliche Warenlehre'
	 */
	public static readonly ID_WL : LehrerFachrichtung = new LehrerFachrichtung("ID_WL", 49, [new LehrerKatalogFachrichtungEintrag(127, "WL", "Wirtschaftliche Warenlehre", null, null)]);

	/**
	 * Fachrichtung 'Wirtschaftsgeographie'
	 */
	public static readonly ID_WG : LehrerFachrichtung = new LehrerFachrichtung("ID_WG", 50, [new LehrerKatalogFachrichtungEintrag(128, "WG", "Wirtschaftsgeographie", null, null)]);

	/**
	 * Fachrichtung 'Wirtschaftsinformatik'
	 */
	public static readonly ID_WI : LehrerFachrichtung = new LehrerFachrichtung("ID_WI", 51, [new LehrerKatalogFachrichtungEintrag(129, "WI", "Wirtschaftsinformatik", null, null)]);

	/**
	 * Fachrichtung 'Lebensmitteltechnologie (spez. Fachrichtung)'
	 */
	public static readonly ID_LT : LehrerFachrichtung = new LehrerFachrichtung("ID_LT", 52, [new LehrerKatalogFachrichtungEintrag(130, "LT", "Lebensmitteltechnologie (spez. Fachrichtung)", null, null)]);

	/**
	 * Fachrichtung 'Sonstige Fachrichtung'
	 */
	public static readonly ID_99 : LehrerFachrichtung = new LehrerFachrichtung("ID_99", 53, [new LehrerKatalogFachrichtungEintrag(131, "99", "Sonstige Fachrichtung", null, null)]);

	/**
	 * Fachrichtung 'Absatz und Marketing'
	 */
	public static readonly ID_AS : LehrerFachrichtung = new LehrerFachrichtung("ID_AS", 54, [new LehrerKatalogFachrichtungEintrag(132, "AS", "Absatz und Marketing", null, null)]);

	/**
	 * Fachrichtung 'Betriebswirtschaftliche Finanzierungslehre'
	 */
	public static readonly ID_BS : LehrerFachrichtung = new LehrerFachrichtung("ID_BS", 55, [new LehrerKatalogFachrichtungEintrag(133, "BS", "Betriebswirtschaftliche Finanzierungslehre", null, null)]);

	/**
	 * Fachrichtung 'Unternehmensrechnung'
	 */
	public static readonly ID_UR : LehrerFachrichtung = new LehrerFachrichtung("ID_UR", 56, [new LehrerKatalogFachrichtungEintrag(134, "UR", "Unternehmensrechnung", null, null)]);

	/**
	 * Fachrichtung 'Gesundheit'
	 */
	public static readonly ID_81 : LehrerFachrichtung = new LehrerFachrichtung("ID_81", 57, [new LehrerKatalogFachrichtungEintrag(135, "81", "Gesundheit", null, null)]);

	/**
	 * Fachrichtung 'Maschinenbautechnik'
	 */
	public static readonly ID_02 : LehrerFachrichtung = new LehrerFachrichtung("ID_02", 58, [new LehrerKatalogFachrichtungEintrag(136, "02", "Maschinenbautechnik", null, null)]);

	/**
	 * Fachrichtung 'Fertigungstechnik'
	 */
	public static readonly ID_06 : LehrerFachrichtung = new LehrerFachrichtung("ID_06", 59, [new LehrerKatalogFachrichtungEintrag(137, "06", "Fertigungstechnik", null, null)]);

	/**
	 * Fachrichtung 'Versorgungstechnik'
	 */
	public static readonly ID_07 : LehrerFachrichtung = new LehrerFachrichtung("ID_07", 60, [new LehrerKatalogFachrichtungEintrag(138, "07", "Versorgungstechnik", null, null)]);

	/**
	 * Fachrichtung 'Energietechnik'
	 */
	public static readonly ID_13 : LehrerFachrichtung = new LehrerFachrichtung("ID_13", 61, [new LehrerKatalogFachrichtungEintrag(139, "13", "Energietechnik", null, null)]);

	/**
	 * Fachrichtung 'Hochbautechnik'
	 */
	public static readonly ID_24 : LehrerFachrichtung = new LehrerFachrichtung("ID_24", 62, [new LehrerKatalogFachrichtungEintrag(140, "24", "Hochbautechnik", null, null)]);

	/**
	 * Fachrichtung 'Tiefbautechnik'
	 */
	public static readonly ID_25 : LehrerFachrichtung = new LehrerFachrichtung("ID_25", 63, [new LehrerKatalogFachrichtungEintrag(141, "25", "Tiefbautechnik", null, null)]);

	/**
	 * Fachrichtung 'Technische Informatik'
	 */
	public static readonly ID_36 : LehrerFachrichtung = new LehrerFachrichtung("ID_36", 64, [new LehrerKatalogFachrichtungEintrag(142, "36", "Technische Informatik", null, null)]);

	/**
	 * Fachrichtung 'Lebensmitteltechnologie'
	 */
	public static readonly ID_67 : LehrerFachrichtung = new LehrerFachrichtung("ID_67", 65, [new LehrerKatalogFachrichtungEintrag(143, "67", "Lebensmitteltechnologie", null, null)]);

	/**
	 * Fachrichtung 'Bankbetriebslehre'
	 */
	public static readonly ID_BB : LehrerFachrichtung = new LehrerFachrichtung("ID_BB", 66, [new LehrerKatalogFachrichtungEintrag(144, "BB", "Bankbetriebslehre", null, null)]);

	/**
	 * Fachrichtung 'Betriebswirtschaftliche Steuerlehre'
	 */
	public static readonly ID_BW : LehrerFachrichtung = new LehrerFachrichtung("ID_BW", 67, [new LehrerKatalogFachrichtungEintrag(145, "BW", "Betriebswirtschaftliche Steuerlehre", null, null)]);

	/**
	 * Fachrichtung 'Personalwirtschaft'
	 */
	public static readonly ID_PW : LehrerFachrichtung = new LehrerFachrichtung("ID_PW", 68, [new LehrerKatalogFachrichtungEintrag(146, "PW", "Personalwirtschaft", null, null)]);

	/**
	 * Fachrichtung 'Versicherungsbetriebslehre'
	 */
	public static readonly ID_VB : LehrerFachrichtung = new LehrerFachrichtung("ID_VB", 69, [new LehrerKatalogFachrichtungEintrag(147, "VB", "Versicherungsbetriebslehre", null, null)]);

	/**
	 * Fachrichtung 'Emotionale und soziale Entwicklung'
	 */
	public static readonly ID_ES : LehrerFachrichtung = new LehrerFachrichtung("ID_ES", 70, [new LehrerKatalogFachrichtungEintrag(148, "ES", "Emotionale und soziale Entwicklung", null, null)]);

	/**
	 * Fachrichtung 'Geistige Entwicklung'
	 */
	public static readonly ID_GG : LehrerFachrichtung = new LehrerFachrichtung("ID_GG", 71, [new LehrerKatalogFachrichtungEintrag(149, "GG", "Geistige Entwicklung", null, null)]);

	/**
	 * Fachrichtung 'Hören und Kommunikation'
	 */
	public static readonly ID_HK : LehrerFachrichtung = new LehrerFachrichtung("ID_HK", 72, [new LehrerKatalogFachrichtungEintrag(150, "HK", "Hören und Kommunikation", null, null)]);

	/**
	 * Fachrichtung 'Körperliche und motorische Entwicklung'
	 */
	public static readonly ID_KM : LehrerFachrichtung = new LehrerFachrichtung("ID_KM", 73, [new LehrerKatalogFachrichtungEintrag(151, "KM", "Körperliche und motorische Entwicklung", null, null)]);

	/**
	 * Fachrichtung 'Lernen'
	 */
	public static readonly ID_LE : LehrerFachrichtung = new LehrerFachrichtung("ID_LE", 74, [new LehrerKatalogFachrichtungEintrag(152, "LE", "Lernen", null, null)]);

	/**
	 * Fachrichtung 'Sprache'
	 */
	public static readonly ID_SQ : LehrerFachrichtung = new LehrerFachrichtung("ID_SQ", 75, [new LehrerKatalogFachrichtungEintrag(153, "SQ", "Sprache", null, null)]);

	/**
	 * Fachrichtung 'Sehen'
	 */
	public static readonly ID_SE : LehrerFachrichtung = new LehrerFachrichtung("ID_SE", 76, [new LehrerKatalogFachrichtungEintrag(154, "SE", "Sehen", null, null)]);

	/**
	 * Fachrichtung 'Textiltechnik'
	 */
	public static readonly ID_32 : LehrerFachrichtung = new LehrerFachrichtung("ID_32", 77, [new LehrerKatalogFachrichtungEintrag(155, "32", "Textiltechnik", null, null)]);

	/**
	 * Fachrichtung 'Mediendesign und Designtechnik'
	 */
	public static readonly ID_57 : LehrerFachrichtung = new LehrerFachrichtung("ID_57", 78, [new LehrerKatalogFachrichtungEintrag(156, "57", "Mediendesign und Designtechnik", null, null)]);

	/**
	 * Fachrichtung 'Druck- und Medientechnik'
	 */
	public static readonly ID_58 : LehrerFachrichtung = new LehrerFachrichtung("ID_58", 79, [new LehrerKatalogFachrichtungEintrag(157, "58", "Druck- und Medientechnik", null, null)]);

	/**
	 * Fachrichtung 'Farbtechnik/Raumgestaltung/Oberflächentechnik'
	 */
	public static readonly ID_59 : LehrerFachrichtung = new LehrerFachrichtung("ID_59", 80, [new LehrerKatalogFachrichtungEintrag(158, "59", "Farbtechnik/Raumgestaltung/Oberflächentechnik", null, null)]);

	/**
	 * Fachrichtung 'Lebensmitteltechnik'
	 */
	public static readonly ID_68 : LehrerFachrichtung = new LehrerFachrichtung("ID_68", 81, [new LehrerKatalogFachrichtungEintrag(159, "68", "Lebensmitteltechnik", null, null)]);

	/**
	 * Fachrichtung 'Gesundheitswissenschaft/Pflege'
	 */
	public static readonly ID_86 : LehrerFachrichtung = new LehrerFachrichtung("ID_86", 82, [new LehrerKatalogFachrichtungEintrag(160, "86", "Gesundheitswissenschaft/Pflege", null, null)]);

	/**
	 * Fachrichtung 'Agrarwissenschaft'
	 */
	public static readonly ID_93 : LehrerFachrichtung = new LehrerFachrichtung("ID_93", 83, [new LehrerKatalogFachrichtungEintrag(161, "93", "Agrarwissenschaft", null, null)]);

	/**
	 * Fachrichtung 'Informationstechnik'
	 */
	public static readonly ID_IT : LehrerFachrichtung = new LehrerFachrichtung("ID_IT", 84, [new LehrerKatalogFachrichtungEintrag(162, "IT", "Informationstechnik", null, null)]);

	/**
	 * Fachrichtung 'Automatisierungstechnik'
	 */
	public static readonly ID_AU : LehrerFachrichtung = new LehrerFachrichtung("ID_AU", 85, [new LehrerKatalogFachrichtungEintrag(163, "AU", "Automatisierungstechnik", null, null)]);

	/**
	 * Fachrichtung 'Vermessungstechnik'
	 */
	public static readonly ID_VE : LehrerFachrichtung = new LehrerFachrichtung("ID_VE", 86, [new LehrerKatalogFachrichtungEintrag(164, "VE", "Vermessungstechnik", null, null)]);

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Verwaltung und Rechtswesen'
	 */
	public static readonly ID_6A : LehrerFachrichtung = new LehrerFachrichtung("ID_6A", 87, [new LehrerKatalogFachrichtungEintrag(165, "6A", "Sektorales Management mit dem Profil: Verwaltung und Rechtswesen", null, null)]);

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Medien'
	 */
	public static readonly ID_6B : LehrerFachrichtung = new LehrerFachrichtung("ID_6B", 88, [new LehrerKatalogFachrichtungEintrag(166, "6B", "Sektorales Management mit dem Profil: Medien", null, null)]);

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Gesundheitsökonomie'
	 */
	public static readonly ID_6C : LehrerFachrichtung = new LehrerFachrichtung("ID_6C", 89, [new LehrerKatalogFachrichtungEintrag(167, "6C", "Sektorales Management mit dem Profil: Gesundheitsökonomie", null, null)]);

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Freizeitökonomie'
	 */
	public static readonly ID_6D : LehrerFachrichtung = new LehrerFachrichtung("ID_6D", 90, [new LehrerKatalogFachrichtungEintrag(168, "6D", "Sektorales Management mit dem Profil: Freizeitökonomie", null, null)]);

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Tourismus und Gastronomie'
	 */
	public static readonly ID_6E : LehrerFachrichtung = new LehrerFachrichtung("ID_6E", 91, [new LehrerKatalogFachrichtungEintrag(169, "6E", "Sektorales Management mit dem Profil: Tourismus und Gastronomie", null, null)]);

	/**
	 * Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Produktionswirtschaft'
	 */
	public static readonly ID_6F : LehrerFachrichtung = new LehrerFachrichtung("ID_6F", 92, [new LehrerKatalogFachrichtungEintrag(170, "6F", "Produktion, Logistik, Absatz mit dem Profil: Produktionswirtschaft", null, null)]);

	/**
	 * Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Verkehr und Logistik'
	 */
	public static readonly ID_6G : LehrerFachrichtung = new LehrerFachrichtung("ID_6G", 93, [new LehrerKatalogFachrichtungEintrag(171, "6G", "Produktion, Logistik, Absatz mit dem Profil: Verkehr und Logistik", null, null)]);

	/**
	 * Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Marketing/Handel'
	 */
	public static readonly ID_6H : LehrerFachrichtung = new LehrerFachrichtung("ID_6H", 94, [new LehrerKatalogFachrichtungEintrag(172, "6H", "Produktion, Logistik, Absatz mit dem Profil: Marketing/Handel", null, null)]);

	/**
	 * Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Steuerung und Dokumentation'
	 */
	public static readonly ID_6I : LehrerFachrichtung = new LehrerFachrichtung("ID_6I", 95, [new LehrerKatalogFachrichtungEintrag(173, "6I", "Finanz- und Rechnungswesen mit dem Profil: Steuerung und Dokumentation", null, null)]);

	/**
	 * Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Finanzdienstleistungen'
	 */
	public static readonly ID_6J : LehrerFachrichtung = new LehrerFachrichtung("ID_6J", 96, [new LehrerKatalogFachrichtungEintrag(174, "6J", "Finanz- und Rechnungswesen mit dem Profil: Finanzdienstleistungen", null, null)]);

	/**
	 * Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Steuern'
	 */
	public static readonly ID_6K : LehrerFachrichtung = new LehrerFachrichtung("ID_6K", 97, [new LehrerKatalogFachrichtungEintrag(175, "6K", "Finanz- und Rechnungswesen mit dem Profil: Steuern", null, null)]);

	/**
	 * Fachrichtung 'Politik'
	 */
	public static readonly ID_PK : LehrerFachrichtung = new LehrerFachrichtung("ID_PK", 98, [new LehrerKatalogFachrichtungEintrag(176, "PK", "Politik", null, null)]);

	/**
	 * Fachrichtung 'Gastronomie'
	 */
	public static readonly ID_GZ : LehrerFachrichtung = new LehrerFachrichtung("ID_GZ", 99, [new LehrerKatalogFachrichtungEintrag(177, "GZ", "Gastronomie", null, null)]);

	/**
	 * Fachrichtung 'Gartenbau'
	 */
	public static readonly ID_GA : LehrerFachrichtung = new LehrerFachrichtung("ID_GA", 100, [new LehrerKatalogFachrichtungEintrag(178, "GA", "Gartenbau", null, null)]);

	/**
	 * Fachrichtung 'Garten- Landschaftsbau'
	 */
	public static readonly ID_GL : LehrerFachrichtung = new LehrerFachrichtung("ID_GL", 101, [new LehrerKatalogFachrichtungEintrag(179, "GL", "Garten- Landschaftsbau", null, null)]);

	/**
	 * Fachrichtung 'Pflanzenbau'
	 */
	public static readonly ID_PB : LehrerFachrichtung = new LehrerFachrichtung("ID_PB", 102, [new LehrerKatalogFachrichtungEintrag(180, "PB", "Pflanzenbau", null, null)]);

	/**
	 * Fachrichtung 'Tierhaltung'
	 */
	public static readonly ID_TH : LehrerFachrichtung = new LehrerFachrichtung("ID_TH", 103, [new LehrerKatalogFachrichtungEintrag(181, "TH", "Tierhaltung", null, null)]);

	/**
	 * Fachrichtung 'Natur- und Umweltschutz'
	 */
	public static readonly ID_NU : LehrerFachrichtung = new LehrerFachrichtung("ID_NU", 104, [new LehrerKatalogFachrichtungEintrag(182, "NU", "Natur- und Umweltschutz", null, null)]);

	/**
	 * Fachrichtung 'Medizintechnik"'
	 */
	public static readonly ID_82 : LehrerFachrichtung = new LehrerFachrichtung("ID_82", 105, [new LehrerKatalogFachrichtungEintrag(183, "82", "Medizintechnik", 2022, null)]);

	/**
	 * Fachrichtung 'Augenoptik'
	 */
	public static readonly ID_AO : LehrerFachrichtung = new LehrerFachrichtung("ID_AO", 106, [new LehrerKatalogFachrichtungEintrag(184, "AO", "Augenoptik", 2022, null)]);

	/**
	 * Fachrichtung 'Orthopädietechnik'
	 */
	public static readonly ID_OT : LehrerFachrichtung = new LehrerFachrichtung("ID_OT", 107, [new LehrerKatalogFachrichtungEintrag(185, "OT", "Orthopädietechnik", 2022, null)]);

	/**
	 * Fachrichtung 'Zahntechnik'
	 */
	public static readonly ID_ZT : LehrerFachrichtung = new LehrerFachrichtung("ID_ZT", 108, [new LehrerKatalogFachrichtungEintrag(186, "ZT", "Zahntechnik", 2022, null)]);

	/**
	 * Fachrichtung 'Ingenieurtechnik'
	 */
	public static readonly ID_IG : LehrerFachrichtung = new LehrerFachrichtung("ID_IG", 109, [new LehrerKatalogFachrichtungEintrag(187, "IG", "Ingenieurtechnik", 2022, null)]);

	/**
	 * Fachrichtung 'Hörakustik'
	 */
	public static readonly ID_HA : LehrerFachrichtung = new LehrerFachrichtung("ID_HA", 110, [new LehrerKatalogFachrichtungEintrag(188, "HA", "Hörakustik", 2022, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Fachrichtung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogFachrichtungEintrag;

	/**
	 * Die Historie mit den Einträgen der Fachrichtung
	 */
	public readonly historie : Array<LehrerKatalogFachrichtungEintrag>;

	/**
	 * Eine Hashmap mit allen Fachrichtungen, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _fachrichtungenByID : HashMap<number, LehrerFachrichtung | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Fachrichtungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _fachrichtungenByKuerzel : HashMap<string, LehrerFachrichtung | null> = new HashMap();

	/**
	 * Erzeugt eine neuen Fachrichtung in der Aufzählung.
	 *
	 * @param historie   die Historie der Fachrichtung, welches ein Array von {@link LehrerKatalogFachrichtungEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogFachrichtungEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerFachrichtung.all_values_by_ordinal.push(this);
		LehrerFachrichtung.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Fachrichtungen auf die zugehörigen Fachrichtungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den IDs der Fachrichtungen auf die zugehörigen Fachrichtungen
	 */
	private static getMapFachrichtungByID() : HashMap<number, LehrerFachrichtung | null> {
		if (LehrerFachrichtung._fachrichtungenByID.size() === 0)
			for (const g of LehrerFachrichtung.values())
				LehrerFachrichtung._fachrichtungenByID.put(g.daten.id, g);
		return LehrerFachrichtung._fachrichtungenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Fachrichtungen auf die zugehörigen Fachrichtungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den Kürzeln der Fachrichtungen auf die zugehörigen Fachrichtungen
	 */
	private static getMapFachrichtungByKuerzel() : HashMap<string, LehrerFachrichtung | null> {
		if (LehrerFachrichtung._fachrichtungenByKuerzel.size() === 0)
			for (const g of LehrerFachrichtung.values())
				LehrerFachrichtung._fachrichtungenByKuerzel.put(g.daten.kuerzel, g);
		return LehrerFachrichtung._fachrichtungenByKuerzel;
	}

	/**
	 * Gibt die Fachrichtung anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID der Fachrichtung
	 *
	 * @return die Fachrichtung oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerFachrichtung | null {
		return LehrerFachrichtung.getMapFachrichtungByID().get(id);
	}

	/**
	 * Gibt die Fachrichtung anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Fachrichtung
	 *
	 * @return die Fachrichtung oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerFachrichtung | null {
		return LehrerFachrichtung.getMapFachrichtungByKuerzel().get(kuerzel);
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
		if (!(other instanceof LehrerFachrichtung))
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
	public compareTo(other : LehrerFachrichtung) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerFachrichtung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerFachrichtung | null {
		const tmp : LehrerFachrichtung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.lehrer.LehrerFachrichtung', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_lehrer_LehrerFachrichtung(obj : unknown) : LehrerFachrichtung {
	return obj as LehrerFachrichtung;
}
