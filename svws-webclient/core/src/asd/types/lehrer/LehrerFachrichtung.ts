import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { LehrerFachrichtungKatalogEintrag } from '../../../asd/data/lehrer/LehrerFachrichtungKatalogEintrag';

export class LehrerFachrichtung extends JavaEnum<LehrerFachrichtung> implements CoreType<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerFachrichtung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerFachrichtung> = new Map<string, LehrerFachrichtung>();

	/**
	 * Fachrichtung 'Kraftfahrzeugtechnik'
	 */
	public static readonly ID_03 : LehrerFachrichtung = new LehrerFachrichtung("ID_03", 0, );

	/**
	 * Fachrichtung 'Maschinentechnik, Metalltechnik, Maschinenbau (außer Kfz), Verfahrens-, Fertigungstechnik'
	 */
	public static readonly ID_04 : LehrerFachrichtung = new LehrerFachrichtung("ID_04", 1, );

	/**
	 * Fachrichtung 'Sanitär-, Heizungs-, Klima-, Lüftungstechnik'
	 */
	public static readonly ID_05 : LehrerFachrichtung = new LehrerFachrichtung("ID_05", 2, );

	/**
	 * Fachrichtung 'Bergtechnik, Bergbau'
	 */
	public static readonly ID_08 : LehrerFachrichtung = new LehrerFachrichtung("ID_08", 3, );

	/**
	 * Fachrichtung 'Hütten-, Gießereiwesen'
	 */
	public static readonly ID_09 : LehrerFachrichtung = new LehrerFachrichtung("ID_09", 4, );

	/**
	 * Fachrichtung 'Elektrotechnik: Energietechnik'
	 */
	public static readonly ID_11 : LehrerFachrichtung = new LehrerFachrichtung("ID_11", 5, );

	/**
	 * Fachrichtung 'Elektrotechnik: Nachrichtentechnik'
	 */
	public static readonly ID_12 : LehrerFachrichtung = new LehrerFachrichtung("ID_12", 6, );

	/**
	 * Fachrichtung 'Bautechnik, Bauingenieurwesen'
	 */
	public static readonly ID_21 : LehrerFachrichtung = new LehrerFachrichtung("ID_21", 7, );

	/**
	 * Fachrichtung 'Holztechnik'
	 */
	public static readonly ID_22 : LehrerFachrichtung = new LehrerFachrichtung("ID_22", 8, );

	/**
	 * Fachrichtung 'Architektur'
	 */
	public static readonly ID_23 : LehrerFachrichtung = new LehrerFachrichtung("ID_23", 9, );

	/**
	 * Fachrichtung 'Textil-, Bekleidungstechnik, Textilgestaltung'
	 */
	public static readonly ID_31 : LehrerFachrichtung = new LehrerFachrichtung("ID_31", 10, );

	/**
	 * Fachrichtung 'Informatik'
	 */
	public static readonly ID_35 : LehrerFachrichtung = new LehrerFachrichtung("ID_35", 11, );

	/**
	 * Fachrichtung 'Chemie- / Verfahrens- / Chemotechnik'
	 */
	public static readonly ID_41 : LehrerFachrichtung = new LehrerFachrichtung("ID_41", 12, );

	/**
	 * Fachrichtung 'Lebensmittelchemie'
	 */
	public static readonly ID_42 : LehrerFachrichtung = new LehrerFachrichtung("ID_42", 13, );

	/**
	 * Fachrichtung 'Farb- u. Raumgestaltung, Gestaltungstechnik, Malerei, Graphik, Design, Fotografie, Glas- und Keramiktechnik'
	 */
	public static readonly ID_55 : LehrerFachrichtung = new LehrerFachrichtung("ID_55", 14, );

	/**
	 * Fachrichtung 'Wirtschaftswissenschaft in kaufmännischen Berufen'
	 */
	public static readonly ID_60 : LehrerFachrichtung = new LehrerFachrichtung("ID_60", 15, );

	/**
	 * Fachrichtung 'Hauswirtschaftswissenschaft'
	 */
	public static readonly ID_65 : LehrerFachrichtung = new LehrerFachrichtung("ID_65", 16, );

	/**
	 * Fachrichtung 'Ernährungswissenschaft'
	 */
	public static readonly ID_66 : LehrerFachrichtung = new LehrerFachrichtung("ID_66", 17, );

	/**
	 * Fachrichtung 'Sozialwesen, Sozialpädagogik, Sozialpflege, Sozialwissenschaft'
	 */
	public static readonly ID_70 : LehrerFachrichtung = new LehrerFachrichtung("ID_70", 18, );

	/**
	 * Fachrichtung 'Psychologie'
	 */
	public static readonly ID_71 : LehrerFachrichtung = new LehrerFachrichtung("ID_71", 19, );

	/**
	 * Fachrichtung 'Körperpflege / Biotechnik'
	 */
	public static readonly ID_80 : LehrerFachrichtung = new LehrerFachrichtung("ID_80", 20, );

	/**
	 * Fachrichtung 'Landbauwissenschaft, Agrarwissenschaft, Agrarwirtschaft'
	 */
	public static readonly ID_91 : LehrerFachrichtung = new LehrerFachrichtung("ID_91", 21, );

	/**
	 * Fachrichtung 'Gartenbauwissenschaft'
	 */
	public static readonly ID_92 : LehrerFachrichtung = new LehrerFachrichtung("ID_92", 22, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Blinden'
	 */
	public static readonly ID_BL : LehrerFachrichtung = new LehrerFachrichtung("ID_BL", 23, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Erziehungsschwierigen'
	 */
	public static readonly ID_EZ : LehrerFachrichtung = new LehrerFachrichtung("ID_EZ", 24, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Geistigbehinderten'
	 */
	public static readonly ID_GB : LehrerFachrichtung = new LehrerFachrichtung("ID_GB", 25, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Gehörlosen'
	 */
	public static readonly ID_GH : LehrerFachrichtung = new LehrerFachrichtung("ID_GH", 26, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Körperbehinderten'
	 */
	public static readonly ID_KB : LehrerFachrichtung = new LehrerFachrichtung("ID_KB", 27, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Lernbehinderten'
	 */
	public static readonly ID_LB : LehrerFachrichtung = new LehrerFachrichtung("ID_LB", 28, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Sprachbehinderten'
	 */
	public static readonly ID_SB : LehrerFachrichtung = new LehrerFachrichtung("ID_SB", 29, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Schwerhörigen'
	 */
	public static readonly ID_SG : LehrerFachrichtung = new LehrerFachrichtung("ID_SG", 30, );

	/**
	 * Fachrichtung 'Sondererziehung und Rehabilitation der Sehbehinderten'
	 */
	public static readonly ID_SH : LehrerFachrichtung = new LehrerFachrichtung("ID_SH", 31, );

	/**
	 * Fachrichtung 'Drucktechnik, Reproduktionstechnik'
	 */
	public static readonly ID_51 : LehrerFachrichtung = new LehrerFachrichtung("ID_51", 32, );

	/**
	 * Fachrichtung 'Fertigungstechnik'
	 */
	public static readonly ID_FT : LehrerFachrichtung = new LehrerFachrichtung("ID_FT", 33, );

	/**
	 * Fachrichtung 'Fahrzeugtechnik'
	 */
	public static readonly ID_KT : LehrerFachrichtung = new LehrerFachrichtung("ID_KT", 34, );

	/**
	 * Fachrichtung 'Versorgungstechnik'
	 */
	public static readonly ID_VT : LehrerFachrichtung = new LehrerFachrichtung("ID_VT", 35, );

	/**
	 * Fachrichtung 'Technische Informatik'
	 */
	public static readonly ID_TI : LehrerFachrichtung = new LehrerFachrichtung("ID_TI", 36, );

	/**
	 * Fachrichtung 'Energietechnik'
	 */
	public static readonly ID_ET : LehrerFachrichtung = new LehrerFachrichtung("ID_ET", 37, );

	/**
	 * Fachrichtung 'Nachrichtentechnik'
	 */
	public static readonly ID_NT : LehrerFachrichtung = new LehrerFachrichtung("ID_NT", 38, );

	/**
	 * Fachrichtung 'Hochbau'
	 */
	public static readonly ID_HC : LehrerFachrichtung = new LehrerFachrichtung("ID_HC", 39, );

	/**
	 * Fachrichtung 'Holztechnik'
	 */
	public static readonly ID_HT : LehrerFachrichtung = new LehrerFachrichtung("ID_HT", 40, );

	/**
	 * Fachrichtung 'Tiefbau'
	 */
	public static readonly ID_TB : LehrerFachrichtung = new LehrerFachrichtung("ID_TB", 41, );

	/**
	 * Fachrichtung 'Banken, Bankbetriebslehre / Finanzwirtschaft'
	 */
	public static readonly ID_BK : LehrerFachrichtung = new LehrerFachrichtung("ID_BK", 42, );

	/**
	 * Fachrichtung 'Handel, Handelsbetriebslehre / Absatzwirtschaft'
	 */
	public static readonly ID_HB : LehrerFachrichtung = new LehrerFachrichtung("ID_HB", 43, );

	/**
	 * Fachrichtung 'Industrie, Industriebetriebslehre / Produktionswirtschaft'
	 */
	public static readonly ID_IB : LehrerFachrichtung = new LehrerFachrichtung("ID_IB", 44, );

	/**
	 * Fachrichtung 'Verkehr'
	 */
	public static readonly ID_VR : LehrerFachrichtung = new LehrerFachrichtung("ID_VR", 45, );

	/**
	 * Fachrichtung 'Versicherung'
	 */
	public static readonly ID_VL : LehrerFachrichtung = new LehrerFachrichtung("ID_VL", 46, );

	/**
	 * Fachrichtung 'Organisation und Bürokommunikation / Datenverarbeitung / Organisationslehre / Betriebsinformatik'
	 */
	public static readonly ID_OL : LehrerFachrichtung = new LehrerFachrichtung("ID_OL", 47, );

	/**
	 * Fachrichtung 'Betriebswirtschaftliche Steuerlehre / Revisions- und Treuhandwesen'
	 */
	public static readonly ID_ST : LehrerFachrichtung = new LehrerFachrichtung("ID_ST", 48, );

	/**
	 * Fachrichtung 'Wirtschaftliche Warenlehre'
	 */
	public static readonly ID_WL : LehrerFachrichtung = new LehrerFachrichtung("ID_WL", 49, );

	/**
	 * Fachrichtung 'Wirtschaftsgeographie'
	 */
	public static readonly ID_WG : LehrerFachrichtung = new LehrerFachrichtung("ID_WG", 50, );

	/**
	 * Fachrichtung 'Wirtschaftsinformatik'
	 */
	public static readonly ID_WI : LehrerFachrichtung = new LehrerFachrichtung("ID_WI", 51, );

	/**
	 * Fachrichtung 'Lebensmitteltechnologie'
	 */
	public static readonly ID_LT : LehrerFachrichtung = new LehrerFachrichtung("ID_LT", 52, );

	/**
	 * Fachrichtung 'Sonstige Fachrichtung'
	 */
	public static readonly ID_99 : LehrerFachrichtung = new LehrerFachrichtung("ID_99", 53, );

	/**
	 * Fachrichtung 'Absatz und Marketing'
	 */
	public static readonly ID_AS : LehrerFachrichtung = new LehrerFachrichtung("ID_AS", 54, );

	/**
	 * Fachrichtung 'Betriebswirtschaftliche Finanzierungslehre'
	 */
	public static readonly ID_BS : LehrerFachrichtung = new LehrerFachrichtung("ID_BS", 55, );

	/**
	 * Fachrichtung 'Unternehmensrechnung'
	 */
	public static readonly ID_UR : LehrerFachrichtung = new LehrerFachrichtung("ID_UR", 56, );

	/**
	 * Fachrichtung 'Gesundheit'
	 */
	public static readonly ID_81 : LehrerFachrichtung = new LehrerFachrichtung("ID_81", 57, );

	/**
	 * Fachrichtung 'Maschinenbautechnik'
	 */
	public static readonly ID_02 : LehrerFachrichtung = new LehrerFachrichtung("ID_02", 58, );

	/**
	 * Fachrichtung 'Fertigungstechnik'
	 */
	public static readonly ID_06 : LehrerFachrichtung = new LehrerFachrichtung("ID_06", 59, );

	/**
	 * Fachrichtung 'Versorgungstechnik'
	 */
	public static readonly ID_07 : LehrerFachrichtung = new LehrerFachrichtung("ID_07", 60, );

	/**
	 * Fachrichtung 'Energietechnik'
	 */
	public static readonly ID_13 : LehrerFachrichtung = new LehrerFachrichtung("ID_13", 61, );

	/**
	 * Fachrichtung 'Hochbautechnik'
	 */
	public static readonly ID_24 : LehrerFachrichtung = new LehrerFachrichtung("ID_24", 62, );

	/**
	 * Fachrichtung 'Tiefbautechnik'
	 */
	public static readonly ID_25 : LehrerFachrichtung = new LehrerFachrichtung("ID_25", 63, );

	/**
	 * Fachrichtung 'Technische Informatik'
	 */
	public static readonly ID_36 : LehrerFachrichtung = new LehrerFachrichtung("ID_36", 64, );

	/**
	 * Fachrichtung 'Lebensmitteltechnologie'
	 */
	public static readonly ID_67 : LehrerFachrichtung = new LehrerFachrichtung("ID_67", 65, );

	/**
	 * Fachrichtung 'Bankbetriebslehre'
	 */
	public static readonly ID_BB : LehrerFachrichtung = new LehrerFachrichtung("ID_BB", 66, );

	/**
	 * Fachrichtung 'Betriebswirtschaftliche Steuerlehre'
	 */
	public static readonly ID_BW : LehrerFachrichtung = new LehrerFachrichtung("ID_BW", 67, );

	/**
	 * Fachrichtung 'Personalwirtschaft'
	 */
	public static readonly ID_PW : LehrerFachrichtung = new LehrerFachrichtung("ID_PW", 68, );

	/**
	 * Fachrichtung 'Versicherungsbetriebslehre'
	 */
	public static readonly ID_VB : LehrerFachrichtung = new LehrerFachrichtung("ID_VB", 69, );

	/**
	 * Fachrichtung 'Emotionale und soziale Entwicklung'
	 */
	public static readonly ID_ES : LehrerFachrichtung = new LehrerFachrichtung("ID_ES", 70, );

	/**
	 * Fachrichtung 'Geistige Entwicklung'
	 */
	public static readonly ID_GG : LehrerFachrichtung = new LehrerFachrichtung("ID_GG", 71, );

	/**
	 * Fachrichtung 'Hören und Kommunikation'
	 */
	public static readonly ID_HK : LehrerFachrichtung = new LehrerFachrichtung("ID_HK", 72, );

	/**
	 * Fachrichtung 'Körperliche und motorische Entwicklung'
	 */
	public static readonly ID_KM : LehrerFachrichtung = new LehrerFachrichtung("ID_KM", 73, );

	/**
	 * Fachrichtung 'Lernen'
	 */
	public static readonly ID_LE : LehrerFachrichtung = new LehrerFachrichtung("ID_LE", 74, );

	/**
	 * Fachrichtung 'Sprache'
	 */
	public static readonly ID_SQ : LehrerFachrichtung = new LehrerFachrichtung("ID_SQ", 75, );

	/**
	 * Fachrichtung 'Sehen'
	 */
	public static readonly ID_SE : LehrerFachrichtung = new LehrerFachrichtung("ID_SE", 76, );

	/**
	 * Fachrichtung 'Textiltechnik'
	 */
	public static readonly ID_32 : LehrerFachrichtung = new LehrerFachrichtung("ID_32", 77, );

	/**
	 * Fachrichtung 'Mediendesign und Designtechnik'
	 */
	public static readonly ID_57 : LehrerFachrichtung = new LehrerFachrichtung("ID_57", 78, );

	/**
	 * Fachrichtung 'Druck- und Medientechnik'
	 */
	public static readonly ID_58 : LehrerFachrichtung = new LehrerFachrichtung("ID_58", 79, );

	/**
	 * Fachrichtung 'Farbtechnik / Raumgestaltung / Oberflächentechnik'
	 */
	public static readonly ID_59 : LehrerFachrichtung = new LehrerFachrichtung("ID_59", 80, );

	/**
	 * Fachrichtung 'Lebensmitteltechnik'
	 */
	public static readonly ID_68 : LehrerFachrichtung = new LehrerFachrichtung("ID_68", 81, );

	/**
	 * Fachrichtung 'Gesundheitswissenschaft / Pflege'
	 */
	public static readonly ID_86 : LehrerFachrichtung = new LehrerFachrichtung("ID_86", 82, );

	/**
	 * Fachrichtung 'Agrarwissenschaft'
	 */
	public static readonly ID_93 : LehrerFachrichtung = new LehrerFachrichtung("ID_93", 83, );

	/**
	 * Fachrichtung 'Informationstechnik'
	 */
	public static readonly ID_IT : LehrerFachrichtung = new LehrerFachrichtung("ID_IT", 84, );

	/**
	 * Fachrichtung 'Automatisierungstechnik'
	 */
	public static readonly ID_AU : LehrerFachrichtung = new LehrerFachrichtung("ID_AU", 85, );

	/**
	 * Fachrichtung 'Vermessungstechnik'
	 */
	public static readonly ID_VE : LehrerFachrichtung = new LehrerFachrichtung("ID_VE", 86, );

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Verwaltung und Rechtswesen'
	 */
	public static readonly ID_6A : LehrerFachrichtung = new LehrerFachrichtung("ID_6A", 87, );

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Medien'
	 */
	public static readonly ID_6B : LehrerFachrichtung = new LehrerFachrichtung("ID_6B", 88, );

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Gesundheitsökonomie'
	 */
	public static readonly ID_6C : LehrerFachrichtung = new LehrerFachrichtung("ID_6C", 89, );

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Freizeitökonomie'
	 */
	public static readonly ID_6D : LehrerFachrichtung = new LehrerFachrichtung("ID_6D", 90, );

	/**
	 * Fachrichtung 'Sektorales Management mit dem Profil: Tourismus und Gastronomie'
	 */
	public static readonly ID_6E : LehrerFachrichtung = new LehrerFachrichtung("ID_6E", 91, );

	/**
	 * Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Produktionswirtschaft'
	 */
	public static readonly ID_6F : LehrerFachrichtung = new LehrerFachrichtung("ID_6F", 92, );

	/**
	 * Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Verkehr und Logistik'
	 */
	public static readonly ID_6G : LehrerFachrichtung = new LehrerFachrichtung("ID_6G", 93, );

	/**
	 * Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Marketing / Handel'
	 */
	public static readonly ID_6H : LehrerFachrichtung = new LehrerFachrichtung("ID_6H", 94, );

	/**
	 * Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Steuerung und Dokumentation'
	 */
	public static readonly ID_6I : LehrerFachrichtung = new LehrerFachrichtung("ID_6I", 95, );

	/**
	 * Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Finanzdienstleistungen'
	 */
	public static readonly ID_6J : LehrerFachrichtung = new LehrerFachrichtung("ID_6J", 96, );

	/**
	 * Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Steuern'
	 */
	public static readonly ID_6K : LehrerFachrichtung = new LehrerFachrichtung("ID_6K", 97, );

	/**
	 * Fachrichtung 'Politik'
	 */
	public static readonly ID_PK : LehrerFachrichtung = new LehrerFachrichtung("ID_PK", 98, );

	/**
	 * Fachrichtung 'Gastronomie'
	 */
	public static readonly ID_GZ : LehrerFachrichtung = new LehrerFachrichtung("ID_GZ", 99, );

	/**
	 * Fachrichtung 'Gartenbau'
	 */
	public static readonly ID_GA : LehrerFachrichtung = new LehrerFachrichtung("ID_GA", 100, );

	/**
	 * Fachrichtung 'Garten- und Landschaftsbau'
	 */
	public static readonly ID_GL : LehrerFachrichtung = new LehrerFachrichtung("ID_GL", 101, );

	/**
	 * Fachrichtung 'Pflanzenbau'
	 */
	public static readonly ID_PB : LehrerFachrichtung = new LehrerFachrichtung("ID_PB", 102, );

	/**
	 * Fachrichtung 'Tierhaltung'
	 */
	public static readonly ID_TH : LehrerFachrichtung = new LehrerFachrichtung("ID_TH", 103, );

	/**
	 * Fachrichtung 'Natur- und Umweltschutz'
	 */
	public static readonly ID_NU : LehrerFachrichtung = new LehrerFachrichtung("ID_NU", 104, );

	/**
	 * Fachrichtung 'Medizintechnik"'
	 */
	public static readonly ID_82 : LehrerFachrichtung = new LehrerFachrichtung("ID_82", 105, );

	/**
	 * Fachrichtung 'Augenoptik'
	 */
	public static readonly ID_AO : LehrerFachrichtung = new LehrerFachrichtung("ID_AO", 106, );

	/**
	 * Fachrichtung 'Orthopädietechnik'
	 */
	public static readonly ID_OT : LehrerFachrichtung = new LehrerFachrichtung("ID_OT", 107, );

	/**
	 * Fachrichtung 'Zahntechnik'
	 */
	public static readonly ID_ZT : LehrerFachrichtung = new LehrerFachrichtung("ID_ZT", 108, );

	/**
	 * Fachrichtung 'Ingenieurtechnik'
	 */
	public static readonly ID_IG : LehrerFachrichtung = new LehrerFachrichtung("ID_IG", 109, );

	/**
	 * Fachrichtung 'Hörakustik'
	 */
	public static readonly ID_HA : LehrerFachrichtung = new LehrerFachrichtung("ID_HA", 110, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerFachrichtung.all_values_by_ordinal.push(this);
		LehrerFachrichtung.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung>) : void {
		CoreTypeDataManager.putManager(LehrerFachrichtung.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung> {
		return CoreTypeDataManager.getManager(LehrerFachrichtung.class);
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerFachrichtungKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<LehrerFachrichtungKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerFachrichtung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerFachrichtung', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerFachrichtung>('de.svws_nrw.asd.types.lehrer.LehrerFachrichtung');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerFachrichtung(obj : unknown) : LehrerFachrichtung {
	return obj as LehrerFachrichtung;
}
