import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BerufskollegBerufsebeneKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_BerufskollegBerufsebeneKatalogEintrag } from '../../../core/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class BerufskollegBerufsebene3 extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BerufskollegBerufsebene3> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, BerufskollegBerufsebene3> = new Map<string, BerufskollegBerufsebene3>();

	/**
	 * Berufsebene 3 : Absatzwirtschaft
	 */
	public static readonly AB : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AB", 0, [new BerufskollegBerufsebeneKatalogEintrag(300001000, 3, "AB", "Absatzwirtschaft", null, null)]);

	/**
	 * Berufsebene 3 : Agrarservice
	 */
	public static readonly AS : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AS", 1, [new BerufskollegBerufsebeneKatalogEintrag(300002000, 3, "AS", "Agrarservice", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Bautechnik
	 */
	public static readonly BH : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("BH", 2, [new BerufskollegBerufsebeneKatalogEintrag(300003000, 3, "BH", "Allgemeine Hochschulreife / Bautechnik", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Betriebswirtschaftslehre
	 */
	public static readonly BW : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("BW", 3, [new BerufskollegBerufsebeneKatalogEintrag(300004000, 3, "BW", "Allgemeine Hochschulreife / Betriebswirtschaftslehre", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Biologie, Chemie
	 */
	public static readonly BC : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("BC", 4, [new BerufskollegBerufsebeneKatalogEintrag(300005000, 3, "BC", "Allgemeine Hochschulreife / Biologie, Chemie", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Chemie, Chemietechnik
	 */
	public static readonly CC : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("CC", 5, [new BerufskollegBerufsebeneKatalogEintrag(300006000, 3, "CC", "Allgemeine Hochschulreife / Chemie, Chemietechnik", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Deutsch, Englisch
	 */
	public static readonly DE : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("DE", 6, [new BerufskollegBerufsebeneKatalogEintrag(300007000, 3, "DE", "Allgemeine Hochschulreife / Deutsch, Englisch", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Elektrotechnik
	 */
	public static readonly ET : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("ET", 7, [new BerufskollegBerufsebeneKatalogEintrag(300008000, 3, "ET", "Allgemeine Hochschulreife / Elektrotechnik", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Ernährung
	 */
	public static readonly ER : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("ER", 8, [new BerufskollegBerufsebeneKatalogEintrag(300009000, 3, "ER", "Allgemeine Hochschulreife / Ernährung", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Erziehungswissenschaften
	 */
	public static readonly EW : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("EW", 9, [new BerufskollegBerufsebeneKatalogEintrag(300010000, 3, "EW", "Allgemeine Hochschulreife / Erziehungswissenschaften", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Freizeitsportleiter (Sport/Gesundheitsförderung/Biologie)
	 */
	public static readonly FL : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("FL", 10, [new BerufskollegBerufsebeneKatalogEintrag(300011000, 3, "FL", "Allgemeine Hochschulreife / Freizeitsportleiter (Sport/Gesundheitsförderung/Biologie)", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Fremdsprachenkorrespondent
	 */
	public static readonly FK : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("FK", 11, [new BerufskollegBerufsebeneKatalogEintrag(300012000, 3, "FK", "Allgemeine Hochschulreife / Fremdsprachenkorrespondent", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Gesundheit
	 */
	public static readonly GE : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("GE", 12, [new BerufskollegBerufsebeneKatalogEintrag(300013000, 3, "GE", "Allgemeine Hochschulreife / Gesundheit", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Ingenieurwissenschaften
	 */
	public static readonly IW : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("IW", 13, [new BerufskollegBerufsebeneKatalogEintrag(300014000, 3, "IW", "Allgemeine Hochschulreife / Ingenieurwissenschaften", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Kunst/Englisch
	 */
	public static readonly KE : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("KE", 14, [new BerufskollegBerufsebeneKatalogEintrag(300015000, 3, "KE", "Allgemeine Hochschulreife / Kunst/Englisch", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Maschinenbautechnik
	 */
	public static readonly MT : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("MT", 15, [new BerufskollegBerufsebeneKatalogEintrag(300016000, 3, "MT", "Allgemeine Hochschulreife / Maschinenbautechnik", null, null)]);

	/**
	 * Berufsebene 3 : Allgemeine Hochschulreife / Mathematik, Informatik
	 */
	public static readonly MI : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("MI", 16, [new BerufskollegBerufsebeneKatalogEintrag(300017000, 3, "MI", "Allgemeine Hochschulreife / Mathematik, Informatik", null, null)]);

	/**
	 * Berufsebene 3 : angewandte Baudenkmalpflege
	 */
	public static readonly BP : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("BP", 17, [new BerufskollegBerufsebeneKatalogEintrag(300018000, 3, "BP", "angewandte Baudenkmalpflege", null, null)]);

	/**
	 * Berufsebene 3 : Assistent(in) für Konstruktions- und Fertigungstechnik / AHR
	 */
	public static readonly AK : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AK", 18, [new BerufskollegBerufsebeneKatalogEintrag(300019000, 3, "AK", "Assistent(in) für Konstruktions- und Fertigungstechnik / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Ausbau
	 */
	public static readonly AU : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AU", 19, [new BerufskollegBerufsebeneKatalogEintrag(300020000, 3, "AU", "Ausbau", null, null)]);

	/**
	 * Berufsebene 3 : Außenhandel
	 */
	public static readonly AH : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AH", 20, [new BerufskollegBerufsebeneKatalogEintrag(300021000, 3, "AH", "Außenhandel", null, null)]);

	/**
	 * Berufsebene 3 : Avionik
	 */
	public static readonly AI : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AI", 21, [new BerufskollegBerufsebeneKatalogEintrag(300022000, 3, "AI", "Avionik", null, null)]);

	/**
	 * Berufsebene 3 : Bautechnischer Assistent / AHR
	 */
	public static readonly BA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("BA", 22, [new BerufskollegBerufsebeneKatalogEintrag(300023000, 3, "BA", "Bautechnischer Assistent / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Betriebstechnik
	 */
	public static readonly BT : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("BT", 23, [new BerufskollegBerufsebeneKatalogEintrag(300024000, 3, "BT", "Betriebstechnik", null, null)]);

	/**
	 * Berufsebene 3 : Biologisch-technische(r) Assistent(in) / AHR
	 */
	public static readonly BS : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("BS", 24, [new BerufskollegBerufsebeneKatalogEintrag(300025000, 3, "BS", "Biologisch-technische(r) Assistent(in) / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Chemisch-technische(r) Assistent(in) / AHR
	 */
	public static readonly CA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("CA", 25, [new BerufskollegBerufsebeneKatalogEintrag(300026000, 3, "CA", "Chemisch-technische(r) Assistent(in) / AHR", null, null)]);

	/**
	 * Berufsebene 3 : CNC-Systemtechnik
	 */
	public static readonly CS : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("CS", 26, [new BerufskollegBerufsebeneKatalogEintrag(300027000, 3, "CS", "CNC-Systemtechnik", null, null)]);

	/**
	 * Berufsebene 3 : Computer- und Kommunikationst.
	 */
	public static readonly CK : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("CK", 27, [new BerufskollegBerufsebeneKatalogEintrag(300028000, 3, "CK", "Computer- und Kommunikationst.", null, null)]);

	/**
	 * Berufsebene 3 : Dienstleistungsgartenbau
	 */
	public static readonly DG : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("DG", 28, [new BerufskollegBerufsebeneKatalogEintrag(300029000, 3, "DG", "Dienstleistungsgartenbau", null, null)]);

	/**
	 * Berufsebene 3 : Elektrotechnische(r) Assistent(in) / AHR
	 */
	public static readonly EA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("EA", 29, [new BerufskollegBerufsebeneKatalogEintrag(300030000, 3, "EA", "Elektrotechnische(r) Assistent(in) / AHR", null, null)]);

	/**
	 * Berufsebene 3 : energieeffiziente-ökologische Altbauerneuerung
	 */
	public static readonly AE : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AE", 30, [new BerufskollegBerufsebeneKatalogEintrag(300031000, 3, "AE", "energieeffiziente-ökologische Altbauerneuerung", null, null)]);

	/**
	 * Berufsebene 3 : Erzieher/Erzieherin / AHR
	 */
	public static readonly EZ : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("EZ", 31, [new BerufskollegBerufsebeneKatalogEintrag(300032000, 3, "EZ", "Erzieher/Erzieherin / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Finanzdienstleistungen
	 */
	public static readonly FD : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("FD", 32, [new BerufskollegBerufsebeneKatalogEintrag(300033000, 3, "FD", "Finanzdienstleistungen", null, null)]);

	/**
	 * Berufsebene 3 : Finanzwirtschaft
	 */
	public static readonly FW : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("FW", 33, [new BerufskollegBerufsebeneKatalogEintrag(300034000, 3, "FW", "Finanzwirtschaft", null, null)]);

	/**
	 * Berufsebene 3 : Flugwerk/Triebwerk
	 */
	public static readonly FT : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("FT", 34, [new BerufskollegBerufsebeneKatalogEintrag(300035000, 3, "FT", "Flugwerk/Triebwerk", null, null)]);

	/**
	 * Berufsebene 3 : Fremdsprachen
	 */
	public static readonly FS : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("FS", 35, [new BerufskollegBerufsebeneKatalogEintrag(300036000, 3, "FS", "Fremdsprachen", null, null)]);

	/**
	 * Berufsebene 3 : Gestaltungstechnische(r) Assistent(in)/AHR
	 */
	public static readonly GA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("GA", 36, [new BerufskollegBerufsebeneKatalogEintrag(300037000, 3, "GA", "Gestaltungstechnische(r) Assistent(in)/AHR", null, null)]);

	/**
	 * Berufsebene 3 : Gesundheitsökonomie und -management
	 */
	public static readonly GM : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("GM", 37, [new BerufskollegBerufsebeneKatalogEintrag(300038000, 3, "GM", "Gesundheitsökonomie und -management", null, null)]);

	/**
	 * Berufsebene 3 : Grafik-Design
	 */
	public static readonly GD : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("GD", 38, [new BerufskollegBerufsebeneKatalogEintrag(300039000, 3, "GD", "Grafik-Design", null, null)]);

	/**
	 * Berufsebene 3 : Handelsmanagement (Schulversuch)
	 */
	public static readonly HM : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("HM", 39, [new BerufskollegBerufsebeneKatalogEintrag(300040000, 3, "HM", "Handelsmanagement (Schulversuch)", null, null)]);

	/**
	 * Berufsebene 3 : Heilerziehung
	 */
	public static readonly HE : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("HE", 40, [new BerufskollegBerufsebeneKatalogEintrag(300041000, 3, "HE", "Heilerziehung", null, null)]);

	/**
	 * Berufsebene 3 : Hochbau
	 */
	public static readonly HB : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("HB", 41, [new BerufskollegBerufsebeneKatalogEintrag(300042000, 3, "HB", "Hochbau", null, null)]);

	/**
	 * Berufsebene 3 : Informationstechnische(r) Assistent(in) / AHR
	 */
	public static readonly IA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("IA", 42, [new BerufskollegBerufsebeneKatalogEintrag(300043000, 3, "IA", "Informationstechnische(r) Assistent(in) / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Kaufmännische(r) Assistent(in) / AHR
	 */
	public static readonly KA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("KA", 43, [new BerufskollegBerufsebeneKatalogEintrag(300044000, 3, "KA", "Kaufmännische(r) Assistent(in) / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Kokerei/Aufbereitungstechnik
	 */
	public static readonly KO : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("KO", 44, [new BerufskollegBerufsebeneKatalogEintrag(300045000, 3, "KO", "Kokerei/Aufbereitungstechnik", null, null)]);

	/**
	 * Berufsebene 3 : Labortechnik
	 */
	public static readonly LT : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("LT", 45, [new BerufskollegBerufsebeneKatalogEintrag(300046000, 3, "LT", "Labortechnik", null, null)]);

	/**
	 * Berufsebene 3 : Logistik
	 */
	public static readonly LO : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("LO", 46, [new BerufskollegBerufsebeneKatalogEintrag(300047000, 3, "LO", "Logistik", null, null)]);

	/**
	 * Berufsebene 3 : Marketing-Kommunikation
	 */
	public static readonly MK : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("MK", 47, [new BerufskollegBerufsebeneKatalogEintrag(300048000, 3, "MK", "Marketing-Kommunikation", null, null)]);

	/**
	 * Berufsebene 3 : Medien- und Eventmanagement
	 */
	public static readonly ME : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("ME", 48, [new BerufskollegBerufsebeneKatalogEintrag(300049000, 3, "ME", "Medien- und Eventmanagement", null, null)]);

	/**
	 * Berufsebene 3 : Medizinische Verwaltung
	 */
	public static readonly MV : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("MV", 49, [new BerufskollegBerufsebeneKatalogEintrag(300050000, 3, "MV", "Medizinische Verwaltung", null, null)]);

	/**
	 * Berufsebene 3 : Ökologischer Landbau
	 */
	public static readonly OL : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("OL", 50, [new BerufskollegBerufsebeneKatalogEintrag(300051000, 3, "OL", "Ökologischer Landbau", null, null)]);

	/**
	 * Berufsebene 3 : Personalwirtschaft
	 */
	public static readonly PE : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("PE", 51, [new BerufskollegBerufsebeneKatalogEintrag(300052000, 3, "PE", "Personalwirtschaft", null, null)]);

	/**
	 * Berufsebene 3 : Physikalisch-technische(r) Assistent(in) / AHR
	 */
	public static readonly PT : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("PT", 52, [new BerufskollegBerufsebeneKatalogEintrag(300053000, 3, "PT", "Physikalisch-technische(r) Assistent(in) / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Praktikanten mit Förderverträgen
	 */
	public static readonly PF : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("PF", 53, [new BerufskollegBerufsebeneKatalogEintrag(300054000, 3, "PF", "Praktikanten mit Förderverträgen", null, null)]);

	/**
	 * Berufsebene 3 : Produktion u. Vermarktung
	 */
	public static readonly PV : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("PV", 54, [new BerufskollegBerufsebeneKatalogEintrag(300055000, 3, "PV", "Produktion u. Vermarktung", null, null)]);

	/**
	 * Berufsebene 3 : Produktionswirtschaft
	 */
	public static readonly PW : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("PW", 55, [new BerufskollegBerufsebeneKatalogEintrag(300056000, 3, "PW", "Produktionswirtschaft", null, null)]);

	/**
	 * Berufsebene 3 : Rechnungswesen
	 */
	public static readonly RW : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("RW", 56, [new BerufskollegBerufsebeneKatalogEintrag(300057000, 3, "RW", "Rechnungswesen", null, null)]);

	/**
	 * Berufsebene 3 : Recht
	 */
	public static readonly RE : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("RE", 57, [new BerufskollegBerufsebeneKatalogEintrag(300058000, 3, "RE", "Recht", null, null)]);

	/**
	 * Berufsebene 3 : Reiseverkehr/Touristik
	 */
	public static readonly RT : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("RT", 58, [new BerufskollegBerufsebeneKatalogEintrag(300059000, 3, "RT", "Reiseverkehr/Touristik", null, null)]);

	/**
	 * Berufsebene 3 : Schüler/-innen in berufsvorb. Maßn. der AV u. fr. Träger
	 */
	public static readonly AV : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AV", 59, [new BerufskollegBerufsebeneKatalogEintrag(300060000, 3, "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. fr. Träger", null, null)]);

	/**
	 * Berufsebene 3 : Schüler/-innen m. Arbeitsverh. u. Praktikant.
	 */
	public static readonly AP : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("AP", 60, [new BerufskollegBerufsebeneKatalogEintrag(300061000, 3, "AP", "Schüler/-innen m. Arbeitsverh. u. Praktikant.", null, null)]);

	/**
	 * Berufsebene 3 : Schüler/-innen ohne Arbeitsverhältnis
	 */
	public static readonly OA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("OA", 61, [new BerufskollegBerufsebeneKatalogEintrag(300062000, 3, "OA", "Schüler/-innen ohne Arbeitsverhältnis", null, null)]);

	/**
	 * Berufsebene 3 : Service
	 */
	public static readonly SV : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("SV", 62, [new BerufskollegBerufsebeneKatalogEintrag(300063000, 3, "SV", "Service", null, null)]);

	/**
	 * Berufsebene 3 : Sport und Freizeit (Schulversuch)
	 */
	public static readonly SF : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("SF", 63, [new BerufskollegBerufsebeneKatalogEintrag(300064000, 3, "SF", "Sport und Freizeit (Schulversuch)", null, null)]);

	/**
	 * Berufsebene 3 : Steuern
	 */
	public static readonly ST : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("ST", 64, [new BerufskollegBerufsebeneKatalogEintrag(300065000, 3, "ST", "Steuern", null, null)]);

	/**
	 * Berufsebene 3 : Tagebautechnik
	 */
	public static readonly TG : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("TG", 65, [new BerufskollegBerufsebeneKatalogEintrag(300066000, 3, "TG", "Tagebautechnik", null, null)]);

	/**
	 * Berufsebene 3 : Technische(r) Assistent(in) für Betriebsinformatik / AHR
	 */
	public static readonly TA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("TA", 66, [new BerufskollegBerufsebeneKatalogEintrag(300067000, 3, "TA", "Technische(r) Assistent(in) für Betriebsinformatik / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Tiefbau
	 */
	public static readonly TB : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("TB", 67, [new BerufskollegBerufsebeneKatalogEintrag(300068000, 3, "TB", "Tiefbau", null, null)]);

	/**
	 * Berufsebene 3 : Tiefbautechnik
	 */
	public static readonly TT : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("TT", 68, [new BerufskollegBerufsebeneKatalogEintrag(300069000, 3, "TT", "Tiefbautechnik", null, null)]);

	/**
	 * Berufsebene 3 : Umwelttechnische(r) Assistent(in) / AHR
	 */
	public static readonly UA : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("UA", 69, [new BerufskollegBerufsebeneKatalogEintrag(300070000, 3, "UA", "Umwelttechnische(r) Assistent(in) / AHR", null, null)]);

	/**
	 * Berufsebene 3 : Werkstattjahr
	 */
	public static readonly WJ : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("WJ", 70, [new BerufskollegBerufsebeneKatalogEintrag(300071000, 3, "WJ", "Werkstattjahr", null, null)]);

	/**
	 * Berufsebene 3 : Wirtschaftsinformatik
	 */
	public static readonly WI : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("WI", 71, [new BerufskollegBerufsebeneKatalogEintrag(300072000, 3, "WI", "Wirtschaftsinformatik", null, null)]);

	/**
	 * Berufsebene 3 : Anerkannter Ausbildungsberuf (2-jährig)
	 */
	public static readonly A2 : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("A2", 72, [new BerufskollegBerufsebeneKatalogEintrag(300073000, 3, "A2", "Anerkannter Ausbildungsberuf (2-jährig)", null, null)]);

	/**
	 * Berufsebene 3 : Anerkannter Ausbildungsberuf (3-jährig)
	 */
	public static readonly A3 : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("A3", 73, [new BerufskollegBerufsebeneKatalogEintrag(300074000, 3, "A3", "Anerkannter Ausbildungsberuf (3-jährig)", null, null)]);

	/**
	 * Berufsebene 3 : Anerkannter Ausbildungsberuf (3 1/2-jährig)
	 */
	public static readonly A4 : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("A4", 74, [new BerufskollegBerufsebeneKatalogEintrag(300075000, 3, "A4", "Anerkannter Ausbildungsberuf (3 1/2-jährig)", null, null)]);

	/**
	 * Berufsebene 3 : Ausbildungsberuf für Menschen mit Behinderungen
	 */
	public static readonly MB : BerufskollegBerufsebene3 = new BerufskollegBerufsebene3("MB", 75, [new BerufskollegBerufsebeneKatalogEintrag(300076000, 3, "MB", "Ausbildungsberuf für Menschen mit Behinderungen", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Berufsebene, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : BerufskollegBerufsebeneKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Berufsebene
	 */
	public readonly historie : Array<BerufskollegBerufsebeneKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Berufsebenen der Ebene 3, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _ebenen : HashMap<string, BerufskollegBerufsebene3> = new HashMap();

	/**
	 * Erzeugt eine neue Berufsebene in der Aufzählung.
	 * 
	 * @param historie   die Historie der Berufsebene, welches ein Array von {@link BerufskollegBerufsebeneKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<BerufskollegBerufsebeneKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BerufskollegBerufsebene3.all_values_by_ordinal.push(this);
		BerufskollegBerufsebene3.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzels der Berufsebenen auf die zugehörigen Berufsebenen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Berufsebene auf die zugehörigen Berufsebene
	 */
	private static getMapBerufsebenenByKuerzel() : HashMap<string, BerufskollegBerufsebene3> {
		if (BerufskollegBerufsebene3._ebenen.size() === 0) {
			for (let s of BerufskollegBerufsebene3.values()) {
				if (s.daten !== null)
					BerufskollegBerufsebene3._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return BerufskollegBerufsebene3._ebenen;
	}

	/**
	 * Gibt die Berufsebene für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Berufsebene
	 * 
	 * @return die Berufsebene oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : BerufskollegBerufsebene3 | null {
		return BerufskollegBerufsebene3.getMapBerufsebenenByKuerzel().get(kuerzel);
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
		if (!(other instanceof BerufskollegBerufsebene3))
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
	public compareTo(other : BerufskollegBerufsebene3) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegBerufsebene3> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegBerufsebene3 | null {
		let tmp : BerufskollegBerufsebene3 | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schule.BerufskollegBerufsebene3'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schule_BerufskollegBerufsebene3(obj : unknown) : BerufskollegBerufsebene3 {
	return obj as BerufskollegBerufsebene3;
}
