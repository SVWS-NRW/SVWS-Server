import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { SchulgliederungKatalogEintrag } from '../../../asd/data/schule/SchulgliederungKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class Schulgliederung extends JavaEnum<Schulgliederung> implements CoreType<SchulgliederungKatalogEintrag, Schulgliederung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Schulgliederung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Schulgliederung> = new Map<string, Schulgliederung>();

	/**
	 *  Schulgliederung DEFAULT:
	 *    Standard für diese Schulform
	 */
	public static readonly DEFAULT : Schulgliederung = new Schulgliederung("DEFAULT", 0, );

	/**
	 *  Schulgliederung A01:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 01 (Fachklassen (BS; TZ))
	 */
	public static readonly A01 : Schulgliederung = new Schulgliederung("A01", 1, );

	/**
	 *  Schulgliederung A02:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 02 (Fachklassen/Fachhochschulreife (BS/FHR; TZ))
	 */
	public static readonly A02 : Schulgliederung = new Schulgliederung("A02", 2, );

	/**
	 *  Schulgliederung A03:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 03 (Fachklassen/erweiterte Zusatzqualifikation (BS/ZQ; TZ))
	 */
	public static readonly A03 : Schulgliederung = new Schulgliederung("A03", 3, );

	/**
	 *  Schulgliederung A04:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 04 (Fachklassen mit erweitertem Stützunterricht (BS/Stütz; TZ))
	 */
	public static readonly A04 : Schulgliederung = new Schulgliederung("A04", 4, );

	/**
	 *  Schulgliederung A05 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 05 (Berufsorientierungsjahr (BV; VZ))
	 */
	public static readonly A05 : Schulgliederung = new Schulgliederung("A05", 5, );

	/**
	 *  Schulgliederung A06 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 06 (Berufsgrundschuljahr (BG; VZ))
	 */
	public static readonly A06 : Schulgliederung = new Schulgliederung("A06", 6, );

	/**
	 *  Schulgliederung A07 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 07 (Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; TZ) bzw. Werkstattjahr (BS 1j;TZ))
	 */
	public static readonly A07 : Schulgliederung = new Schulgliederung("A07", 7, );

	/**
	 *  Schulgliederung A08 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 08 (Vorpraktikum (VP))
	 */
	public static readonly A08 : Schulgliederung = new Schulgliederung("A08", 8, );

	/**
	 *  Schulgliederung A09 (ausgelaufen):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 09 (Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; VZ))
	 */
	public static readonly A09 : Schulgliederung = new Schulgliederung("A09", 9, );

	/**
	 *  Schulgliederung A10 (auslaufend):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 10 (Berufsabschluss/mittlerer Schulabschluss (BKAZVO) (BAB/FOR; VZ))
	 */
	public static readonly A10 : Schulgliederung = new Schulgliederung("A10", 10, );

	/**
	 *  Schulgliederung A11 (auslaufend):
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 11 (Berufsabschluss/Fachhochschulreife (BKAZVO) (BAB/FHR; VZ))
	 */
	public static readonly A11 : Schulgliederung = new Schulgliederung("A11", 11, );

	/**
	 *  Schulgliederung A12:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 12 (Ausbildungsvorbereitung (BS 1j; VZ))
	 */
	public static readonly A12 : Schulgliederung = new Schulgliederung("A12", 12, );

	/**
	 *  Schulgliederung A13:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 13 (Ausbildungsvorbereitung (BS 1j; TZ))
	 */
	public static readonly A13 : Schulgliederung = new Schulgliederung("A13", 13, );

	/**
	 *  Schulgliederung A14:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 14 (Berufsabschluss (nach §50 BBiG/§40 HwO)/mittlerer Schulabschluss (BAB/FOR; VZ))
	 */
	public static readonly A14 : Schulgliederung = new Schulgliederung("A14", 14, );

	/**
	 *  Schulgliederung A15:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 15 (Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife BAB/FHR; VZ)
	 */
	public static readonly A15 : Schulgliederung = new Schulgliederung("A15", 15, );

	/**
	 *  Schulgliederung A16:
	 *    Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *    Typ 16 (Fachklassen (nach §2 BKAZVO) BAB; VZ)
	 */
	public static readonly A16 : Schulgliederung = new Schulgliederung("A16", 16, );

	/**
	 *  Schulgliederung AB:
	 *    Schule für Kranke: Allgemeinbildend
	 */
	public static readonly AB : Schulgliederung = new Schulgliederung("AB", 17, );

	/**
	 *  Schulgliederung B01 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 01 (Berufsabschluss/Fachoberschulreife (BAB/FOR 2j; VZ))
	 */
	public static readonly B01 : Schulgliederung = new Schulgliederung("B01", 18, );

	/**
	 *  Schulgliederung B02 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 02 (Berufsgrundbildung/Fachoberschulreife (BG/FOR 2j; VZ))
	 */
	public static readonly B02 : Schulgliederung = new Schulgliederung("B02", 19, );

	/**
	 *  Schulgliederung B03 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 03 (Berufsgrundbildung (für Schüler mit FOR) (BG 1j; VZ))
	 */
	public static readonly B03 : Schulgliederung = new Schulgliederung("B03", 20, );

	/**
	 *  Schulgliederung B04 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 04 (Berufsabschluss/Fachoberschulreife (BAB/FOR; VZ))
	 */
	public static readonly B04 : Schulgliederung = new Schulgliederung("B04", 21, );

	/**
	 *  Schulgliederung B05 (ausgelaufen):
	 *    Anlage B (Berufsfachschule),
	 *    Typ 05 (Berufsabschluss/Fachhochschulreife (BAB/FHR; VZ))
	 */
	public static readonly B05 : Schulgliederung = new Schulgliederung("B05", 22, );

	/**
	 *  Schulgliederung B06:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 06 (Berufliche Kenntnisse/Hauptschulabschluss Kl. 10 (BK/HSA10; 1j. VZ))
	 */
	public static readonly B06 : Schulgliederung = new Schulgliederung("B06", 23, );

	/**
	 *  Schulgliederung B07:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 07 (Berufliche Kenntnisse/mittlerer Schulabschluss (BK/FOR; 1j. VZ))
	 */
	public static readonly B07 : Schulgliederung = new Schulgliederung("B07", 24, );

	/**
	 *  Schulgliederung B08:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 08 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 2j. VZ))
	 */
	public static readonly B08 : Schulgliederung = new Schulgliederung("B08", 25, );

	/**
	 *  Schulgliederung B09:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 09 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 3j. TZ))
	 */
	public static readonly B09 : Schulgliederung = new Schulgliederung("B09", 26, );

	/**
	 *  Schulgliederung B10:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 10 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 4j. TZ))
	 */
	public static readonly B10 : Schulgliederung = new Schulgliederung("B10", 27, );

	/**
	 *  Schulgliederung B11:
	 *    Anlage B (Berufsfachschule),
	 *    Typ 11 (Berufsabschl./HSA Kl. 10 oder mittl. Schulabschluss berufsbegleitend (BAB/HSA10-FOR, 3j. TZ))
	 */
	public static readonly B11 : Schulgliederung = new Schulgliederung("B11", 28, );

	/**
	 *  Schulgliederung BT:
	 *    Schule für Kranke: Berufsbildend (Teilzeit)
	 */
	public static readonly BT : Schulgliederung = new Schulgliederung("BT", 29, );

	/**
	 *  Schulgliederung BV:
	 *    Schule für Kranke: Berufsbildend (Vollzeit)
	 */
	public static readonly BV : Schulgliederung = new Schulgliederung("BV", 30, );

	/**
	 *  Schulgliederung C01:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 01 (Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum) BAB/FHR 3j; VZ BFS)
	 */
	public static readonly C01 : Schulgliederung = new Schulgliederung("C01", 31, );

	/**
	 *  Schulgliederung C02:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 02 (Berufsabschluss f. Hochschulzugangsberechtigte (BAB 2j; VZ) BFS)
	 */
	public static readonly C02 : Schulgliederung = new Schulgliederung("C02", 32, );

	/**
	 *  Schulgliederung C03:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 03 (Berufliche Kenntnisse/FHR (BK/FHR 2j; VZ) HBFS)
	 */
	public static readonly C03 : Schulgliederung = new Schulgliederung("C03", 33, );

	/**
	 *  Schulgliederung C04 (ausgelaufen):
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 04 (Berufliche Kenntnisse/Sonderform für Abiturienten (BK 1j; VZ) HBFS)
	 */
	public static readonly C04 : Schulgliederung = new Schulgliederung("C04", 34, );

	/**
	 *  Schulgliederung C05:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 05 (Fachoberschule Kl. 11 (BK/FHR 1j; TZ))
	 */
	public static readonly C05 : Schulgliederung = new Schulgliederung("C05", 35, );

	/**
	 *  Schulgliederung C06:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 06 (Fachoberschule Kl. 12S (BK/FHR 1j; VZ))
	 */
	public static readonly C06 : Schulgliederung = new Schulgliederung("C06", 36, );

	/**
	 *  Schulgliederung C07:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 07 (Fachoberschule Kl. 12B (BK/FHR 2j; TZ))
	 */
	public static readonly C07 : Schulgliederung = new Schulgliederung("C07", 37, );

	/**
	 *  Schulgliederung C08:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 08 (Fachoberschule Kl. 12B (BK/FHR 1j; VZ))
	 */
	public static readonly C08 : Schulgliederung = new Schulgliederung("C08", 38, );

	/**
	 *  Schulgliederung C09 (ausgelaufen):
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 09 (Berufspraktikum Erzieher/innen (Vollzeit) (BP/Erz 1j; VZ))
	 */
	public static readonly C09 : Schulgliederung = new Schulgliederung("C09", 39, );

	/**
	 *  Schulgliederung C10 (ausgelaufen):
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 10 (Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ))
	 */
	public static readonly C10 : Schulgliederung = new Schulgliederung("C10", 40, );

	/**
	 *  Schulgliederung C11:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 11 (Fachoberschule Kl. 12B (BK/FHR 3j; TZ))
	 */
	public static readonly C11 : Schulgliederung = new Schulgliederung("C11", 41, );

	/**
	 *  Schulgliederung C12:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 12 (Berufsabschluss/Fachhochschulreife (mit  Berufspraktikum) BAB/FHR 3,5j; VZ)
	 */
	public static readonly C12 : Schulgliederung = new Schulgliederung("C12", 42, );

	/**
	 *  Schulgliederung C13:
	 *    Anlage C (Berufsfachschule und Fachoberschule),
	 *    Typ 13 (Berufsabschluss/Fachhochschulreife (gestuft) (BAB/FHR 3j; VZ))
	 */
	public static readonly C13 : Schulgliederung = new Schulgliederung("C13", 43, );

	/**
	 *  Schulgliederung D01:
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 01 (Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum) (BAB/AHR 4j; VZ))
	 */
	public static readonly D01 : Schulgliederung = new Schulgliederung("D01", 44, );

	/**
	 *  Schulgliederung D02:
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 02 (Berufl. Kenntnisse/Allg. Hochschulreife (BK/AHR 3j; VZ))
	 */
	public static readonly D02 : Schulgliederung = new Schulgliederung("D02", 45, );

	/**
	 *  Schulgliederung D03 (ausgelaufen):
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 03 (Berufspraktikum (Vollzeit) (BP 1j; VZ))
	 */
	public static readonly D03 : Schulgliederung = new Schulgliederung("D03", 46, );

	/**
	 *  Schulgliederung D04 (ausgelaufen):
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 04 (Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ))
	 */
	public static readonly D04 : Schulgliederung = new Schulgliederung("D04", 47, );

	/**
	 *  Schulgliederung D05:
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 05 (AHR (gem. § 2 Abs. 3 Anlage D) (AHR 1j; VZ) FOS13)
	 */
	public static readonly D05 : Schulgliederung = new Schulgliederung("D05", 48, );

	/**
	 *  Schulgliederung D06:
	 *    Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *    Typ 06 (AHR (gem. § 2 Abs. 3 Anlage D) (AHR 2j; TZ) FOS13)
	 */
	public static readonly D06 : Schulgliederung = new Schulgliederung("D06", 49, );

	/**
	 *  Schulgliederung E01:
	 *    Anlage E (Fachschule),
	 *    Typ 01 (Fachschule Vollzeit (BW 2j; VZ))
	 */
	public static readonly E01 : Schulgliederung = new Schulgliederung("E01", 50, );

	/**
	 *  Schulgliederung E02:
	 *    Anlage E (Fachschule),
	 *    Typ 02 (Fachschule Teilzeit (BW 4j; TZ))
	 */
	public static readonly E02 : Schulgliederung = new Schulgliederung("E02", 51, );

	/**
	 *  Schulgliederung E03:
	 *    Anlage E (Fachschule),
	 *    Typ 03 (Fachschule (verkürzt) Vollzeit (BW 1j; VZ))
	 */
	public static readonly E03 : Schulgliederung = new Schulgliederung("E03", 52, );

	/**
	 *  Schulgliederung E04:
	 *    Anlage E (Fachschule),
	 *    Typ 04 (Fachschule (verkürzt) Teilzeit (BW 2j; TZ))
	 */
	public static readonly E04 : Schulgliederung = new Schulgliederung("E04", 53, );

	/**
	 *  Schulgliederung E05:
	 *    Anlage E (Fachschule),
	 *    Typ 05 (Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 3j; VZ))
	 */
	public static readonly E05 : Schulgliederung = new Schulgliederung("E05", 54, );

	/**
	 *  Schulgliederung E06 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 06 (Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 1j; VZ))
	 */
	public static readonly E06 : Schulgliederung = new Schulgliederung("E06", 55, );

	/**
	 *  Schulgliederung E07:
	 *    Anlage E (Fachschule),
	 *    Typ 07 (Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 6j; TZ))
	 */
	public static readonly E07 : Schulgliederung = new Schulgliederung("E07", 56, );

	/**
	 *  Schulgliederung E08 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 08 (Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 2j; TZ))
	 */
	public static readonly E08 : Schulgliederung = new Schulgliederung("E08", 57, );

	/**
	 *  Schulgliederung E09 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 09 (Fachschule (Sonderform) Vollzeit (BW 3j; VZ))
	 */
	public static readonly E09 : Schulgliederung = new Schulgliederung("E09", 58, );

	/**
	 *  Schulgliederung E10 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 10 (Fachschule (Sonderform) Teilzeit (BW 6j; TZ))
	 */
	public static readonly E10 : Schulgliederung = new Schulgliederung("E10", 59, );

	/**
	 *  Schulgliederung E11 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 11 (Berufspraktikum Erzieher/innen (FS/BP/Erz 1j; VZ))
	 */
	public static readonly E11 : Schulgliederung = new Schulgliederung("E11", 60, );

	/**
	 *  Schulgliederung E12 (ausgelaufen):
	 *    Anlage E (Fachschule),
	 *    Typ 12 (Berufspraktikum Erzieher/innen (FS/BP/Erz 2j; TZ))
	 */
	public static readonly E12 : Schulgliederung = new Schulgliederung("E12", 61, );

	/**
	 *  Schulgliederung E13:
	 *    Anlage E (Fachschule),
	 *    Typ 13 (Fachschule Teilzeit (BW 3j; TZ))
	 */
	public static readonly E13 : Schulgliederung = new Schulgliederung("E13", 62, );

	/**
	 *  Schulgliederung ER:
	 *    kooperative Form: Erweiterungsebene
	 */
	public static readonly ER : Schulgliederung = new Schulgliederung("ER", 63, );

	/**
	 *  Schulgliederung EVB:
	 *    Evangelische Bekenntnisschule
	 */
	public static readonly EVB : Schulgliederung = new Schulgliederung("EVB", 64, );

	/**
	 *  Schulgliederung G01:
	 *    Aufbaugymnasium
	 */
	public static readonly G01 : Schulgliederung = new Schulgliederung("G01", 65, );

	/**
	 *  Schulgliederung G02:
	 *    Bildungsgang Abendgymnasium
	 */
	public static readonly G02 : Schulgliederung = new Schulgliederung("G02", 66, );

	/**
	 *  Schulgliederung GGS (auslaufend):
	 *    Gemeinschaftsschule (auslaufend) integrierte Form
	 */
	public static readonly GGS : Schulgliederung = new Schulgliederung("GGS", 67, );

	/**
	 *  Schulgliederung GGY (auslaufend):
	 *    Gemeinschaftsschule (auslaufend) Gymnasialbildungsgang
	 */
	public static readonly GGY : Schulgliederung = new Schulgliederung("GGY", 68, );

	/**
	 *  Schulgliederung GMS:
	 *    Gemeinschaftsschule
	 */
	public static readonly GMS : Schulgliederung = new Schulgliederung("GMS", 69, );

	/**
	 *  Schulgliederung GR:
	 *    kooperative Form: Grundebene
	 */
	public static readonly GR : Schulgliederung = new Schulgliederung("GR", 70, );

	/**
	 *  Schulgliederung GRH (auslaufend):
	 *    Gemeinschaftsschule auslaufend: teilintegrierte Form
	 */
	public static readonly GRH : Schulgliederung = new Schulgliederung("GRH", 71, );

	/**
	 *  Schulgliederung GS:
	 *    integrierte Form (Binnendifferenzierung)
	 */
	public static readonly GS : Schulgliederung = new Schulgliederung("GS", 72, );

	/**
	 *  Schulgliederung GY:
	 *    Bildungsgang Gymnasium
	 */
	public static readonly GY : Schulgliederung = new Schulgliederung("GY", 73, );

	/**
	 *  Schulgliederung GY8:
	 *    Bildungsgang G8-Gymnasium
	 */
	public static readonly GY8 : Schulgliederung = new Schulgliederung("GY8", 74, );

	/**
	 *  Schulgliederung GY9:
	 *    Bildungsgang G9-Gymnasium
	 */
	public static readonly GY9 : Schulgliederung = new Schulgliederung("GY9", 75, );

	/**
	 *  Schulgliederung H:
	 *    Bildungsgang Hauptschule
	 */
	public static readonly H : Schulgliederung = new Schulgliederung("H", 76, );

	/**
	 *  Schulgliederung H01:
	 *    Berufsgrundbildung (Jahrgang 07 bis 10)
	 */
	public static readonly H01 : Schulgliederung = new Schulgliederung("H01", 77, );

	/**
	 *  Schulgliederung H02:
	 *    Berufsausbildung (Jahrgang 11 und 12)
	 */
	public static readonly H02 : Schulgliederung = new Schulgliederung("H02", 78, );

	/**
	 *  Schulgliederung K02:
	 *    Bildungsgang Kolleg
	 */
	public static readonly K02 : Schulgliederung = new Schulgliederung("K02", 79, );

	/**
	 *  Schulgliederung R:
	 *    Bildungsgang Realschule
	 */
	public static readonly R : Schulgliederung = new Schulgliederung("R", 80, );

	/**
	 *  Schulgliederung R00:
	 *    Realschule
	 */
	public static readonly R00 : Schulgliederung = new Schulgliederung("R00", 81, );

	/**
	 *  Schulgliederung R01:
	 *    Aufbaurealschule
	 */
	public static readonly R01 : Schulgliederung = new Schulgliederung("R01", 82, );

	/**
	 *  Schulgliederung R02:
	 *    Bildungsgang Abendrealschule
	 */
	public static readonly R02 : Schulgliederung = new Schulgliederung("R02", 83, );

	/**
	 *  Schulgliederung RH:
	 *    teilintegrierte Form
	 */
	public static readonly RH : Schulgliederung = new Schulgliederung("RH", 84, );

	/**
	 *  Schulgliederung RKB:
	 *    Katholische  Bekenntnisschule
	 */
	public static readonly RKB : Schulgliederung = new Schulgliederung("RKB", 85, );

	/**
	 *  Schulgliederung SRH (auslaufend):
	 *    Sekundarschule, teilintegrierte Form (auslaufend)
	 */
	public static readonly SRH : Schulgliederung = new Schulgliederung("SRH", 86, );

	/**
	 *  Schulgliederung SSI (auslaufend):
	 *    Sekundarschule, integrierte Form (auslaufend)
	 */
	public static readonly SSI : Schulgliederung = new Schulgliederung("SSI", 87, );

	/**
	 *  Schulgliederung Y8:
	 *    Lehrplan G8
	 */
	public static readonly Y8 : Schulgliederung = new Schulgliederung("Y8", 88, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Schulgliederung.all_values_by_ordinal.push(this);
		Schulgliederung.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<SchulgliederungKatalogEintrag, Schulgliederung>) : void {
		CoreTypeDataManager.putManager(Schulgliederung.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<SchulgliederungKatalogEintrag, Schulgliederung> {
		return CoreTypeDataManager.getManager(Schulgliederung.class);
	}

	/**
	 * Gibt die Standard-Gliederung der angegebenen Schulform zurück.
	 *
	 * @param sf   die Schulform
	 *
	 * @return die Schulgliederung, falls die Schulform gültig ist und ansonsten null
	 */
	public static getDefault(sf : Schulform | null) : Schulgliederung | null {
		if (sf === null)
			return null;
		if ((sf as unknown === Schulform.GY as unknown) || (sf as unknown === Schulform.SK as unknown) || (sf as unknown === Schulform.GM as unknown) || (sf as unknown === Schulform.G as unknown) || (sf as unknown === Schulform.S as unknown) || (sf as unknown === Schulform.PS as unknown) || (sf as unknown === Schulform.V as unknown) || (sf as unknown === Schulform.FW as unknown) || (sf as unknown === Schulform.H as unknown) || (sf as unknown === Schulform.R as unknown) || (sf as unknown === Schulform.GE as unknown) || (sf as unknown === Schulform.SR as unknown) || (sf as unknown === Schulform.SG as unknown))
			return Schulgliederung.DEFAULT;
		return null;
	}

	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public hatSchulform(schuljahr : number, sf : Schulform) : boolean {
		return Schulgliederung.data().hatSchulform(schuljahr, sf, this);
	}

	/**
	 * Gibt zurück, ob es sich um einen 8-jährigen gymnasialen Bildungsgang
	 * handelt oder nicht.
	 *
	 * @return true, falls es sich um einen 8-jährigen gymnasialen Bildungsgang
	 *         handelt, sonst false
	 */
	public istG8() : boolean {
		return (this as unknown === Schulgliederung.GY8 as unknown) || (this as unknown === Schulgliederung.Y8 as unknown);
	}

	/**
	 * Liefert alle zulässigen Schulgliederungen für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Schulgliederungen
	 */
	public static getBySchuljahrAndSchulform(schuljahr : number, schulform : Schulform) : List<Schulgliederung> {
		return Schulgliederung.data().getListBySchuljahrAndSchulform(schuljahr, schulform);
	}

	/**
	 * Liefert die zulässige Schulgliederungen für die angegebene Schulform in dem angegebenen Schuljahr und dem angebenen Schlüssel oder
	 * null falls eine solche nicht existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 * @param schluessel  der Schlüssel für die Schulgliederung
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr dem Schlüssel zugehörige Schulgliederung oder null falls eine solche nicht existiert
	 */
	public static getBySchuljahrAndSchulformAndSchluessel(schuljahr : number, schulform : Schulform, schluessel : string) : Schulgliederung | null {
		return Schulgliederung.data().getBySchuljahrAndSchulformAndSchluessel(schuljahr, schulform, schluessel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Schulgliederung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Schulgliederung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<SchulgliederungKatalogEintrag, Schulgliederung> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : SchulgliederungKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<SchulgliederungKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Schulgliederung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.schule.Schulgliederung', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Schulgliederung>('de.svws_nrw.asd.types.schule.Schulgliederung');

}

export function cast_de_svws_nrw_asd_types_schule_Schulgliederung(obj : unknown) : Schulgliederung {
	return obj as Schulgliederung;
}
