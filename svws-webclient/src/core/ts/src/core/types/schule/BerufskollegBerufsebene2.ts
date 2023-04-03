import { JavaObject } from '../../../java/lang/JavaObject';
import { BerufskollegBerufsebeneKatalogEintrag } from '../../../core/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';

export class BerufskollegBerufsebene2 extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BerufskollegBerufsebene2> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, BerufskollegBerufsebene2> = new Map<string, BerufskollegBerufsebene2>();

	/**
	 * Berufsebene 2 : Assistent/-in für Ernährung und Versorgung
	 */
	public static readonly EV : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("EV", 0, [new BerufskollegBerufsebeneKatalogEintrag(200001000, 2, "EV", "Assistent/-in für Ernährung und Versorgung", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Augenoptik
	 */
	public static readonly AA : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("AA", 1, [new BerufskollegBerufsebeneKatalogEintrag(200002000, 2, "AA", "Aufbaubildungsgang Augenoptik", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Betriebswirtschaft
	 */
	public static readonly AB : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("AB", 2, [new BerufskollegBerufsebeneKatalogEintrag(200003000, 2, "AB", "Aufbaubildungsgang Betriebswirtschaft", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Bewegung und Gesundheit
	 */
	public static readonly BG : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BG", 3, [new BerufskollegBerufsebeneKatalogEintrag(200004000, 2, "BG", "Aufbaubildungsgang Bewegung und Gesundheit", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Bildung und Schulvorbereitung in Tageseinrichtungen f. Kinder
	 */
	public static readonly BS : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BS", 4, [new BerufskollegBerufsebeneKatalogEintrag(200005000, 2, "BS", "Aufbaubildungsgang Bildung und Schulvorbereitung in Tageseinrichtungen f. Kinder", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Bildung, Erziehung u. Betreuung von Kindern unter drei Jahren
	 */
	public static readonly BE : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BE", 5, [new BerufskollegBerufsebeneKatalogEintrag(200006000, 2, "BE", "Aufbaubildungsgang Bildung, Erziehung u. Betreuung von Kindern unter drei Jahren", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Controlling
	 */
	public static readonly CO : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("CO", 6, [new BerufskollegBerufsebeneKatalogEintrag(200007000, 2, "CO", "Aufbaubildungsgang Controlling", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Existenzgründung
	 */
	public static readonly XT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("XT", 7, [new BerufskollegBerufsebeneKatalogEintrag(200008000, 2, "XT", "Aufbaubildungsgang Existenzgründung", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Existenzgründung (Schulversuch)
	 */
	public static readonly XS : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("XS", 8, [new BerufskollegBerufsebeneKatalogEintrag(200009000, 2, "XS", "Aufbaubildungsgang Existenzgründung (Schulversuch)", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Fachkraft für Beratung und Anleitung in der Pflege
	 */
	public static readonly BA : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BA", 9, [new BerufskollegBerufsebeneKatalogEintrag(200010000, 2, "BA", "Aufbaubildungsgang Fachkraft für Beratung und Anleitung in der Pflege", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Fachkraft für heilpädagogische Förderung mit dem Pferd
	 */
	public static readonly HF : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("HF", 10, [new BerufskollegBerufsebeneKatalogEintrag(200011000, 2, "HF", "Aufbaubildungsgang Fachkraft für heilpädagogische Förderung mit dem Pferd", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Inklusive Pädagogik
	 */
	public static readonly IP : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("IP", 11, [new BerufskollegBerufsebeneKatalogEintrag(200012000, 2, "IP", "Aufbaubildungsgang Inklusive Pädagogik", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Medienkompetenz in der Kinder- und Jugendhilfe
	 */
	public static readonly MK : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MK", 12, [new BerufskollegBerufsebeneKatalogEintrag(200013000, 2, "MK", "Aufbaubildungsgang Medienkompetenz in der Kinder- und Jugendhilfe", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Musikalische Förderung im sozialpädagogischen Arbeitsfeld
	 */
	public static readonly MF : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MF", 13, [new BerufskollegBerufsebeneKatalogEintrag(200014000, 2, "MF", "Aufbaubildungsgang Musikalische Förderung im sozialpädagogischen Arbeitsfeld", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Naturwissenschaftlich-technische Früherziehung
	 */
	public static readonly NF : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("NF", 14, [new BerufskollegBerufsebeneKatalogEintrag(200015000, 2, "NF", "Aufbaubildungsgang Naturwissenschaftlich-technische Früherziehung", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Offene Ganztagsschule
	 */
	public static readonly OG : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("OG", 15, [new BerufskollegBerufsebeneKatalogEintrag(200016000, 2, "OG", "Aufbaubildungsgang Offene Ganztagsschule", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Praxisanleitung
	 */
	public static readonly PA : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("PA", 16, [new BerufskollegBerufsebeneKatalogEintrag(200017000, 2, "PA", "Aufbaubildungsgang Praxisanleitung", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Produktionslogistik (Schulversuch)
	 */
	public static readonly PL : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("PL", 17, [new BerufskollegBerufsebeneKatalogEintrag(200018000, 2, "PL", "Aufbaubildungsgang Produktionslogistik (Schulversuch)", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Sozialmanagement
	 */
	public static readonly SM : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("SM", 18, [new BerufskollegBerufsebeneKatalogEintrag(200019000, 2, "SM", "Aufbaubildungsgang Sozialmanagement", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Sprachförderung
	 */
	public static readonly SF : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("SF", 19, [new BerufskollegBerufsebeneKatalogEintrag(200020000, 2, "SF", "Aufbaubildungsgang Sprachförderung", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Technischer Umweltschutz
	 */
	public static readonly TU : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("TU", 20, [new BerufskollegBerufsebeneKatalogEintrag(200021000, 2, "TU", "Aufbaubildungsgang Technischer Umweltschutz", null, null)]);

	/**
	 * Berufsebene 2 : Aufbaubildungsgang Unternehmensmanagement
	 */
	public static readonly UM : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("UM", 21, [new BerufskollegBerufsebeneKatalogEintrag(200022000, 2, "UM", "Aufbaubildungsgang Unternehmensmanagement", null, null)]);

	/**
	 * Berufsebene 2 : Augenoptik
	 */
	public static readonly AU : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("AU", 22, [new BerufskollegBerufsebeneKatalogEintrag(200023000, 2, "AU", "Augenoptik", null, null)]);

	/**
	 * Berufsebene 2 : Bau und Holztechnik
	 */
	public static readonly BH : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BH", 23, [new BerufskollegBerufsebeneKatalogEintrag(200024000, 2, "BH", "Bau und Holztechnik", null, null)]);

	/**
	 * Berufsebene 2 : Baudenkmalpflege u. Altbau.
	 */
	public static readonly BP : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BP", 24, [new BerufskollegBerufsebeneKatalogEintrag(200025000, 2, "BP", "Baudenkmalpflege u. Altbau.", null, null)]);

	/**
	 * Berufsebene 2 : Bautechnik
	 */
	public static readonly BT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BT", 25, [new BerufskollegBerufsebeneKatalogEintrag(200026000, 2, "BT", "Bautechnik", null, null)]);

	/**
	 * Berufsebene 2 : Bekleidungstechnik
	 */
	public static readonly BK : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BK", 26, [new BerufskollegBerufsebeneKatalogEintrag(200027000, 2, "BK", "Bekleidungstechnik", null, null)]);

	/**
	 * Berufsebene 2 : Bergbautechnik
	 */
	public static readonly BB : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BB", 27, [new BerufskollegBerufsebeneKatalogEintrag(200028000, 2, "BB", "Bergbautechnik", null, null)]);

	/**
	 * Berufsebene 2 : Betriebswirtschaft
	 */
	public static readonly BW : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BW", 28, [new BerufskollegBerufsebeneKatalogEintrag(200029000, 2, "BW", "Betriebswirtschaft", null, null)]);

	/**
	 * Berufsebene 2 : Biogentechnik
	 */
	public static readonly BO : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BO", 29, [new BerufskollegBerufsebeneKatalogEintrag(200030000, 2, "BO", "Biogentechnik", null, null)]);

	/**
	 * Berufsebene 2 : Biologietechnik
	 */
	public static readonly BI : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("BI", 30, [new BerufskollegBerufsebeneKatalogEintrag(200031000, 2, "BI", "Biologietechnik", null, null)]);

	/**
	 * Berufsebene 2 : Chemietechnik
	 */
	public static readonly CT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("CT", 31, [new BerufskollegBerufsebeneKatalogEintrag(200032000, 2, "CT", "Chemietechnik", null, null)]);

	/**
	 * Berufsebene 2 : Druck- und Medientechnik
	 */
	public static readonly DM : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("DM", 32, [new BerufskollegBerufsebeneKatalogEintrag(200033000, 2, "DM", "Druck- und Medientechnik", null, null)]);

	/**
	 * Berufsebene 2 : Drucktechnik
	 */
	public static readonly DT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("DT", 33, [new BerufskollegBerufsebeneKatalogEintrag(200034000, 2, "DT", "Drucktechnik", null, null)]);

	/**
	 * Berufsebene 2 : Edelmetallgestaltung
	 */
	public static readonly EM : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("EM", 34, [new BerufskollegBerufsebeneKatalogEintrag(200035000, 2, "EM", "Edelmetallgestaltung", null, null)]);

	/**
	 * Berufsebene 2 : Elektrotechnik
	 */
	public static readonly ET : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("ET", 35, [new BerufskollegBerufsebeneKatalogEintrag(200036000, 2, "ET", "Elektrotechnik", null, null)]);

	/**
	 * Berufsebene 2 : Fahrzeugtechnik
	 */
	public static readonly FT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("FT", 36, [new BerufskollegBerufsebeneKatalogEintrag(200037000, 2, "FT", "Fahrzeugtechnik", null, null)]);

	/**
	 * Berufsebene 2 : Farb- und Lacktechnik
	 */
	public static readonly FL : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("FL", 37, [new BerufskollegBerufsebeneKatalogEintrag(200038000, 2, "FL", "Farb- und Lacktechnik", null, null)]);

	/**
	 * Berufsebene 2 : Farbe, Gestaltung, Werbung
	 */
	public static readonly FG : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("FG", 38, [new BerufskollegBerufsebeneKatalogEintrag(200039000, 2, "FG", "Farbe, Gestaltung, Werbung", null, null)]);

	/**
	 * Berufsebene 2 : Farbtechnik und Raumgestaltung
	 */
	public static readonly FR : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("FR", 39, [new BerufskollegBerufsebeneKatalogEintrag(200040000, 2, "FR", "Farbtechnik und Raumgestaltung", null, null)]);

	/**
	 * Berufsebene 2 : Galvanotechnik
	 */
	public static readonly GT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("GT", 40, [new BerufskollegBerufsebeneKatalogEintrag(200041000, 2, "GT", "Galvanotechnik", null, null)]);

	/**
	 * Berufsebene 2 : Gartenbau
	 */
	public static readonly GB : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("GB", 41, [new BerufskollegBerufsebeneKatalogEintrag(200042000, 2, "GB", "Gartenbau", null, null)]);

	/**
	 * Berufsebene 2 : Gebäudesystemtechnik
	 */
	public static readonly GD : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("GD", 42, [new BerufskollegBerufsebeneKatalogEintrag(200043000, 2, "GD", "Gebäudesystemtechnik", null, null)]);

	/**
	 * Berufsebene 2 : Gesundheit
	 */
	public static readonly GS : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("GS", 43, [new BerufskollegBerufsebeneKatalogEintrag(200044000, 2, "GS", "Gesundheit", null, null)]);

	/**
	 * Berufsebene 2 : Gesundheitswesen
	 */
	public static readonly GW : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("GW", 44, [new BerufskollegBerufsebeneKatalogEintrag(200045000, 2, "GW", "Gesundheitswesen", null, null)]);

	/**
	 * Berufsebene 2 : Gießereitechnik
	 */
	public static readonly GR : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("GR", 45, [new BerufskollegBerufsebeneKatalogEintrag(200046000, 2, "GR", "Gießereitechnik", null, null)]);

	/**
	 * Berufsebene 2 : Glastechnik
	 */
	public static readonly GL : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("GL", 46, [new BerufskollegBerufsebeneKatalogEintrag(200047000, 2, "GL", "Glastechnik", null, null)]);

	/**
	 * Berufsebene 2 : Großhaushalt
	 */
	public static readonly GO : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("GO", 47, [new BerufskollegBerufsebeneKatalogEintrag(200048000, 2, "GO", "Großhaushalt", null, null)]);

	/**
	 * Berufsebene 2 : Heilerziehungspflege
	 */
	public static readonly HP : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("HP", 48, [new BerufskollegBerufsebeneKatalogEintrag(200049000, 2, "HP", "Heilerziehungspflege", null, null)]);

	/**
	 * Berufsebene 2 : Heilpädagogik
	 */
	public static readonly HD : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("HD", 49, [new BerufskollegBerufsebeneKatalogEintrag(200050000, 2, "HD", "Heilpädagogik", null, null)]);

	/**
	 * Berufsebene 2 : Heizungs-, Lüftungs-, Klimatechnik
	 */
	public static readonly HL : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("HL", 50, [new BerufskollegBerufsebeneKatalogEintrag(200051000, 2, "HL", "Heizungs-, Lüftungs-, Klimatechnik", null, null)]);

	/**
	 * Berufsebene 2 : Holztechnik
	 */
	public static readonly HT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("HT", 51, [new BerufskollegBerufsebeneKatalogEintrag(200052000, 2, "HT", "Holztechnik", null, null)]);

	/**
	 * Berufsebene 2 : Hotel und Gaststätten
	 */
	public static readonly HG : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("HG", 52, [new BerufskollegBerufsebeneKatalogEintrag(200053000, 2, "HG", "Hotel und Gaststätten", null, null)]);

	/**
	 * Berufsebene 2 : Hotel- und Gaststättengewerbe
	 */
	public static readonly HB : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("HB", 53, [new BerufskollegBerufsebeneKatalogEintrag(200054000, 2, "HB", "Hotel- und Gaststättengewerbe", null, null)]);

	/**
	 * Berufsebene 2 : Informatik
	 */
	public static readonly IF : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("IF", 54, [new BerufskollegBerufsebeneKatalogEintrag(200055000, 2, "IF", "Informatik", null, null)]);

	/**
	 * Berufsebene 2 : Informations- und Telekommunikationstechnik
	 */
	public static readonly IT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("IT", 55, [new BerufskollegBerufsebeneKatalogEintrag(200056000, 2, "IT", "Informations- und Telekommunikationstechnik", null, null)]);

	/**
	 * Berufsebene 2 : Kältetechnik
	 */
	public static readonly KT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("KT", 56, [new BerufskollegBerufsebeneKatalogEintrag(200057000, 2, "KT", "Kältetechnik", null, null)]);

	/**
	 * Berufsebene 2 : Karosserie- und Fahrzeugbautechnik
	 */
	public static readonly KF : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("KF", 57, [new BerufskollegBerufsebeneKatalogEintrag(200058000, 2, "KF", "Karosserie- und Fahrzeugbautechnik", null, null)]);

	/**
	 * Berufsebene 2 : Kinderpfleger/-in
	 */
	public static readonly KI : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("KI", 58, [new BerufskollegBerufsebeneKatalogEintrag(200059000, 2, "KI", "Kinderpfleger/-in", null, null)]);

	/**
	 * Berufsebene 2 : Körperpflege
	 */
	public static readonly KP : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("KP", 59, [new BerufskollegBerufsebeneKatalogEintrag(200060000, 2, "KP", "Körperpflege", null, null)]);

	/**
	 * Berufsebene 2 : Korrosionsschutztechnik
	 */
	public static readonly KO : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("KO", 60, [new BerufskollegBerufsebeneKatalogEintrag(200061000, 2, "KO", "Korrosionsschutztechnik", null, null)]);

	/**
	 * Berufsebene 2 : Kunststoff- und Kautschuktechnik
	 */
	public static readonly KK : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("KK", 61, [new BerufskollegBerufsebeneKatalogEintrag(200062000, 2, "KK", "Kunststoff- und Kautschuktechnik", null, null)]);

	/**
	 * Berufsebene 2 : Labor- und Verfahrentechnik
	 */
	public static readonly LV : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("LV", 62, [new BerufskollegBerufsebeneKatalogEintrag(200063000, 2, "LV", "Labor- und Verfahrentechnik", null, null)]);

	/**
	 * Berufsebene 2 : Landwirtschaft
	 */
	public static readonly LW : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("LW", 63, [new BerufskollegBerufsebeneKatalogEintrag(200064000, 2, "LW", "Landwirtschaft", null, null)]);

	/**
	 * Berufsebene 2 : Lebensmitteltechnik
	 */
	public static readonly LT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("LT", 64, [new BerufskollegBerufsebeneKatalogEintrag(200065000, 2, "LT", "Lebensmitteltechnik", null, null)]);

	/**
	 * Berufsebene 2 : Luftfahrttechnik
	 */
	public static readonly LF : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("LF", 65, [new BerufskollegBerufsebeneKatalogEintrag(200066000, 2, "LF", "Luftfahrttechnik", null, null)]);

	/**
	 * Berufsebene 2 : Marketing
	 */
	public static readonly MG : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MG", 66, [new BerufskollegBerufsebeneKatalogEintrag(200067000, 2, "MG", "Marketing", null, null)]);

	/**
	 * Berufsebene 2 : Maschinenbautechnik
	 */
	public static readonly MB : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MB", 67, [new BerufskollegBerufsebeneKatalogEintrag(200068000, 2, "MB", "Maschinenbautechnik", null, null)]);

	/**
	 * Berufsebene 2 : Mechatronik
	 */
	public static readonly MT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MT", 68, [new BerufskollegBerufsebeneKatalogEintrag(200069000, 2, "MT", "Mechatronik", null, null)]);

	/**
	 * Berufsebene 2 : Medien
	 */
	public static readonly MN : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MN", 69, [new BerufskollegBerufsebeneKatalogEintrag(200070000, 2, "MN", "Medien", null, null)]);

	/**
	 * Berufsebene 2 : Medien/Medientechnologie
	 */
	public static readonly MM : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MM", 70, [new BerufskollegBerufsebeneKatalogEintrag(200071000, 2, "MM", "Medien/Medientechnologie", null, null)]);

	/**
	 * Berufsebene 2 : Medizintechnik
	 */
	public static readonly MD : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MD", 71, [new BerufskollegBerufsebeneKatalogEintrag(200072000, 2, "MD", "Medizintechnik", null, null)]);

	/**
	 * Berufsebene 2 : Metallbautechnik
	 */
	public static readonly ML : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("ML", 72, [new BerufskollegBerufsebeneKatalogEintrag(200073000, 2, "ML", "Metallbautechnik", null, null)]);

	/**
	 * Berufsebene 2 : Metalltechnik
	 */
	public static readonly ME : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("ME", 73, [new BerufskollegBerufsebeneKatalogEintrag(200074000, 2, "ME", "Metalltechnik", null, null)]);

	/**
	 * Berufsebene 2 : Möbelhandel
	 */
	public static readonly MH : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MH", 74, [new BerufskollegBerufsebeneKatalogEintrag(200075000, 2, "MH", "Möbelhandel", null, null)]);

	/**
	 * Berufsebene 2 : Mode
	 */
	public static readonly MO : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MO", 75, [new BerufskollegBerufsebeneKatalogEintrag(200076000, 2, "MO", "Mode", null, null)]);

	/**
	 * Berufsebene 2 : Motopädie
	 */
	public static readonly MI : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("MI", 76, [new BerufskollegBerufsebeneKatalogEintrag(200077000, 2, "MI", "Motopädie", null, null)]);

	/**
	 * Berufsebene 2 : Pädagogik
	 */
	public static readonly PG : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("PG", 77, [new BerufskollegBerufsebeneKatalogEintrag(200078000, 2, "PG", "Pädagogik", null, null)]);

	/**
	 * Berufsebene 2 : Physik, Chemie und Biologie
	 */
	public static readonly PU : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("PU", 78, [new BerufskollegBerufsebeneKatalogEintrag(200079000, 2, "PU", "Physik, Chemie und Biologie", null, null)]);

	/**
	 * Berufsebene 2 : Physik, Chemie, Biologie
	 */
	public static readonly PB : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("PB", 79, [new BerufskollegBerufsebeneKatalogEintrag(200080000, 2, "PB", "Physik, Chemie, Biologie", null, null)]);

	/**
	 * Berufsebene 2 : Physik/Chemie/Biologie
	 */
	public static readonly PC : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("PC", 80, [new BerufskollegBerufsebeneKatalogEintrag(200081000, 2, "PC", "Physik/Chemie/Biologie", null, null)]);

	/**
	 * Berufsebene 2 : Physiktechnik
	 */
	public static readonly PT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("PT", 81, [new BerufskollegBerufsebeneKatalogEintrag(200082000, 2, "PT", "Physiktechnik", null, null)]);

	/**
	 * Berufsebene 2 : Sozialassistent/-in
	 */
	public static readonly SA : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("SA", 82, [new BerufskollegBerufsebeneKatalogEintrag(200083000, 2, "SA", "Sozialassistent/-in", null, null)]);

	/**
	 * Berufsebene 2 : Sozialassistent/-in - Heilerziehung
	 */
	public static readonly SH : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("SH", 83, [new BerufskollegBerufsebeneKatalogEintrag(200084000, 2, "SH", "Sozialassistent/-in - Heilerziehung", null, null)]);

	/**
	 * Berufsebene 2 : Sozialpädagogik
	 */
	public static readonly SP : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("SP", 84, [new BerufskollegBerufsebeneKatalogEintrag(200085000, 2, "SP", "Sozialpädagogik", null, null)]);

	/**
	 * Berufsebene 2 : Sozialwesen
	 */
	public static readonly SW : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("SW", 85, [new BerufskollegBerufsebeneKatalogEintrag(200086000, 2, "SW", "Sozialwesen", null, null)]);

	/**
	 * Berufsebene 2 : Spreng- und Sicherheitstechnik
	 */
	public static readonly SI : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("SI", 86, [new BerufskollegBerufsebeneKatalogEintrag(200087000, 2, "SI", "Spreng- und Sicherheitstechnik", null, null)]);

	/**
	 * Berufsebene 2 : Technische Informatik
	 */
	public static readonly TI : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("TI", 87, [new BerufskollegBerufsebeneKatalogEintrag(200088000, 2, "TI", "Technische Informatik", null, null)]);

	/**
	 * Berufsebene 2 : Textiltechnik
	 */
	public static readonly TT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("TT", 88, [new BerufskollegBerufsebeneKatalogEintrag(200089000, 2, "TT", "Textiltechnik", null, null)]);

	/**
	 * Berufsebene 2 : Textiltechnik und Bekleidung
	 */
	public static readonly TB : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("TB", 89, [new BerufskollegBerufsebeneKatalogEintrag(200090000, 2, "TB", "Textiltechnik und Bekleidung", null, null)]);

	/**
	 * Berufsebene 2 : Tourismus
	 */
	public static readonly TO : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("TO", 90, [new BerufskollegBerufsebeneKatalogEintrag(200091000, 2, "TO", "Tourismus", null, null)]);

	/**
	 * Berufsebene 2 : Umweltschutztechnik
	 */
	public static readonly US : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("US", 91, [new BerufskollegBerufsebeneKatalogEintrag(200092000, 2, "US", "Umweltschutztechnik", null, null)]);

	/**
	 * Berufsebene 2 : Umwelttechnik
	 */
	public static readonly UT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("UT", 92, [new BerufskollegBerufsebeneKatalogEintrag(200093000, 2, "UT", "Umwelttechnik", null, null)]);

	/**
	 * Berufsebene 2 : Vermessungstechnik
	 */
	public static readonly VT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("VT", 93, [new BerufskollegBerufsebeneKatalogEintrag(200094000, 2, "VT", "Vermessungstechnik", null, null)]);

	/**
	 * Berufsebene 2 : Werkstofftechnik
	 */
	public static readonly WT : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("WT", 94, [new BerufskollegBerufsebeneKatalogEintrag(200095000, 2, "WT", "Werkstofftechnik", null, null)]);

	/**
	 * Berufsebene 2 : Wirtschaftsinformatik
	 */
	public static readonly WI : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("WI", 95, [new BerufskollegBerufsebeneKatalogEintrag(200096000, 2, "WI", "Wirtschaftsinformatik", null, null)]);

	/**
	 * Berufsebene 2 : Wohnungswirtschaft und Realkredit
	 */
	public static readonly WR : BerufskollegBerufsebene2 = new BerufskollegBerufsebene2("WR", 96, [new BerufskollegBerufsebeneKatalogEintrag(200097000, 2, "WR", "Wohnungswirtschaft und Realkredit", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Berufsebene, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : BerufskollegBerufsebeneKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Berufsebene
	 */
	public readonly historie : Array<BerufskollegBerufsebeneKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Berufsebenen der Ebene 2, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, BerufskollegBerufsebene2> = new HashMap();

	/**
	 * Erzeugt eine neue Berufsebene in der Aufzählung.
	 *
	 * @param historie   die Historie der Berufsebene, welches ein Array von {@link BerufskollegBerufsebeneKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<BerufskollegBerufsebeneKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BerufskollegBerufsebene2.all_values_by_ordinal.push(this);
		BerufskollegBerufsebene2.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzels der Berufsebenen auf die zugehörigen Berufsebenen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Berufsebene auf die zugehörigen Berufsebene
	 */
	private static getMapBerufsebenenByKuerzel() : HashMap<string, BerufskollegBerufsebene2> {
		if (BerufskollegBerufsebene2._ebenen.size() === 0) {
			for (const s of BerufskollegBerufsebene2.values()) {
				if (s.daten !== null)
					BerufskollegBerufsebene2._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return BerufskollegBerufsebene2._ebenen;
	}

	/**
	 * Gibt die Berufsebene für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Berufsebene
	 *
	 * @return die Berufsebene oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : BerufskollegBerufsebene2 | null {
		return BerufskollegBerufsebene2.getMapBerufsebenenByKuerzel().get(kuerzel);
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
		if (!(other instanceof BerufskollegBerufsebene2))
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
	public compareTo(other : BerufskollegBerufsebene2) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegBerufsebene2> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegBerufsebene2 | null {
		const tmp : BerufskollegBerufsebene2 | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.BerufskollegBerufsebene2'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_BerufskollegBerufsebene2(obj : unknown) : BerufskollegBerufsebene2 {
	return obj as BerufskollegBerufsebene2;
}
