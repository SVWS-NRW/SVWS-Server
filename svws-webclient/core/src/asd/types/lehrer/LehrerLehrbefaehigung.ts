import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
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
	 * Lehrbefähigung 'Arbeitslehre / Schwerpunkt Technik'
	 */
	public static readonly AT : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AT", 3, );

	/**
	 * Lehrbefähigung 'Arbeitslehre / Wirtschaft'
	 */
	public static readonly AW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AW", 4, );

	/**
	 * Lehrbefähigung 'Betreuung'
	 */
	public static readonly BE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BE", 5, );

	/**
	 * Lehrbefähigung 'Biologie'
	 */
	public static readonly BI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BI", 6, );

	/**
	 * Lehrbefähigung 'Bosnisch (Muttersprachl. Unterricht)'
	 */
	public static readonly BM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BM", 7, );

	/**
	 * Lehrbefähigung 'Chinesisch'
	 */
	public static readonly C : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("C", 8, );

	/**
	 * Lehrbefähigung 'Chemie'
	 */
	public static readonly CH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CH", 9, );

	/**
	 * Lehrbefähigung 'Kroatisch (Muttersprachl. Unterricht)'
	 */
	public static readonly CM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("CM", 10, );

	/**
	 * Lehrbefähigung 'Deutsch'
	 */
	public static readonly D : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("D", 11, );

	/**
	 * Lehrbefähigung 'Englisch'
	 */
	public static readonly E : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("E", 12, );

	/**
	 * Lehrbefähigung 'Erdkunde'
	 */
	public static readonly EK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EK", 13, );

	/**
	 * Lehrbefähigung 'Serbisch (Muttersprachl. Unterricht)'
	 */
	public static readonly EM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EM", 14, );

	/**
	 * Lehrbefähigung 'Evangelische Religionslehre'
	 */
	public static readonly ER : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ER", 15, );

	/**
	 * Lehrbefähigung 'Französisch'
	 */
	public static readonly F : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("F", 16, );

	/**
	 * Lehrbefähigung 'Fachpraxis'
	 */
	public static readonly FP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("FP", 17, );

	/**
	 * Lehrbefähigung 'Griechisch'
	 */
	public static readonly G : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("G", 18, );

	/**
	 * Lehrbefähigung 'Geschichte'
	 */
	public static readonly GE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GE", 19, );

	/**
	 * Lehrbefähigung 'Griechisch (Muttersprachl. Unterricht)'
	 */
	public static readonly GM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GM", 20, );

	/**
	 * Lehrbefähigung 'Geschichte / Politische Bildung'
	 */
	public static readonly GP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GP", 21, );

	/**
	 * Lehrbefähigung 'Kunst/Gestalten (Kunst oder Textilgestaltung)'
	 */
	public static readonly GS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GS", 22, );

	/**
	 * Lehrbefähigung 'Gesamtunterricht'
	 */
	public static readonly GU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GU", 23, );

	/**
	 * Lehrbefähigung 'Hebräisch'
	 */
	public static readonly H : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("H", 24, );

	/**
	 * Lehrbefähigung 'Hauswirtschaft'
	 */
	public static readonly HA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HA", 25, );

	/**
	 * Lehrbefähigung 'Jüdische Religionslehre'
	 */
	public static readonly HR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HR", 26, );

	/**
	 * Lehrbefähigung 'Hauswirtschaft (-swissenschaft)'
	 */
	public static readonly HW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("HW", 27, );

	/**
	 * Lehrbefähigung 'Italienisch'
	 */
	public static readonly I : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("I", 28, );

	/**
	 * Lehrbefähigung 'Informatik (nachgewiesen durch 2. Staatsprüfung, sonst siehe IK)'
	 */
	public static readonly IF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IF", 29, );

	/**
	 * Lehrbefähigung 'Italienisch (Muttersprachl. Unterricht)'
	 */
	public static readonly IM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IM", 30, );

	/**
	 * Lehrbefähigung 'Islamkunde'
	 */
	public static readonly IR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IR", 31, );

	/**
	 * Lehrbefähigung 'Japanisch'
	 */
	public static readonly K : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("K", 32, );

	/**
	 * Lehrbefähigung 'Kunst/Gestalten'
	 */
	public static readonly KG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KG", 33, );

	/**
	 * Lehrbefähigung 'Katholische Religionslehre'
	 */
	public static readonly KR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KR", 34, );

	/**
	 * Lehrbefähigung 'Kurzschrift'
	 */
	public static readonly KS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KS", 35, );

	/**
	 * Lehrbefähigung 'Kunst / Werken'
	 */
	public static readonly KU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KU", 36, );

	/**
	 * Lehrbefähigung 'Kunstwissenschaft'
	 */
	public static readonly KW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("KW", 37, );

	/**
	 * Lehrbefähigung 'Lateinisch'
	 */
	public static readonly L : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("L", 38, );

	/**
	 * Lehrbefähigung 'Literaturwissenschaft'
	 */
	public static readonly LI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LI", 39, );

	/**
	 * Lehrbefähigung 'Albanisch (Muttersprachl. Unterricht)'
	 */
	public static readonly LM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LM", 40, );

	/**
	 * Lehrbefähigung 'Linguistik'
	 */
	public static readonly LN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("LN", 41, );

	/**
	 * Lehrbefähigung 'Mathematik'
	 */
	public static readonly M : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("M", 42, );

	/**
	 * Lehrbefähigung 'Makedonisch (Muttersprachl. Unterricht)'
	 */
	public static readonly MM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MM", 43, );

	/**
	 * Lehrbefähigung 'Maschinenschreiben'
	 */
	public static readonly MS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MS", 44, );

	/**
	 * Lehrbefähigung 'Musik'
	 */
	public static readonly MU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MU", 45, );

	/**
	 * Lehrbefähigung 'Niederländisch'
	 */
	public static readonly N : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("N", 46, );

	/**
	 * Lehrbefähigung 'Portugiesisch'
	 */
	public static readonly O : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("O", 47, );

	/**
	 * Lehrbefähigung 'Ohne Angabe'
	 */
	public static readonly OA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OA", 48, );

	/**
	 * Lehrbefähigung 'Portugiesisch (Muttersprachl. Unterricht)'
	 */
	public static readonly OM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OM", 49, );

	/**
	 * Lehrbefähigung 'Griechisch-orthodoxe Religionslehre'
	 */
	public static readonly OR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("OR", 50, );

	/**
	 * Lehrbefähigung 'Pädagogik'
	 */
	public static readonly PA : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PA", 51, );

	/**
	 * Lehrbefähigung 'Physik (Astronomie)'
	 */
	public static readonly PH : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PH", 52, );

	/**
	 * Lehrbefähigung 'Philosophie/Praktische Philosophie'
	 */
	public static readonly PI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PI", 53, );

	/**
	 * Lehrbefähigung 'Politik'
	 */
	public static readonly PK : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PK", 54, );

	/**
	 * Lehrbefähigung 'Philosophie'
	 */
	public static readonly PL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PL", 55, );

	/**
	 * Lehrbefähigung 'Polnisch (Muttersprachl. Unterricht)'
	 */
	public static readonly PM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PM", 56, );

	/**
	 * Lehrbefähigung 'Praktische Philosophie'
	 */
	public static readonly PP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PP", 57, );

	/**
	 * Lehrbefähigung 'Psychologie'
	 */
	public static readonly PS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("PS", 58, );

	/**
	 * Lehrbefähigung 'Russisch'
	 */
	public static readonly R : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("R", 59, );

	/**
	 * Lehrbefähigung 'Russisch (Muttersprachl. Unterricht)'
	 */
	public static readonly RM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RM", 60, );

	/**
	 * Lehrbefähigung 'Rechtswissenschaft'
	 */
	public static readonly RW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("RW", 61, );

	/**
	 * Lehrbefähigung 'Spanisch'
	 */
	public static readonly S : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("S", 62, );

	/**
	 * Lehrbefähigung 'Sozial- und Erziehungswissenschaft'
	 */
	public static readonly SE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SE", 63, );

	/**
	 * Lehrbefähigung 'Sozialpflege'
	 */
	public static readonly SF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SF", 64, );

	/**
	 * Lehrbefähigung 'Spanisch (Muttersprachl. Unterricht)'
	 */
	public static readonly SM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SM", 65, );

	/**
	 * Lehrbefähigung 'Sonderpädagogik'
	 */
	public static readonly SN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SN", 66, );

	/**
	 * Lehrbefähigung 'Sozialpädagogik'
	 */
	public static readonly SO : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SO", 67, );

	/**
	 * Lehrbefähigung 'Sport'
	 */
	public static readonly SP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SP", 68, );

	/**
	 * Lehrbefähigung 'sonstige Sprachen'
	 */
	public static readonly SR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SR", 69, );

	/**
	 * Lehrbefähigung 'Sachunterricht'
	 */
	public static readonly SU : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SU", 70, );

	/**
	 * Lehrbefähigung 'Sozialwissenschaften'
	 */
	public static readonly SW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SW", 71, );

	/**
	 * Lehrbefähigung 'Türkisch'
	 */
	public static readonly T : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("T", 72, );

	/**
	 * Lehrbefähigung 'Technik'
	 */
	public static readonly TC : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TC", 73, );

	/**
	 * Lehrbefähigung 'Technologie'
	 */
	public static readonly TE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TE", 74, );

	/**
	 * Lehrbefähigung 'Türkisch (Muttersprachl. Unterricht)'
	 */
	public static readonly TM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TM", 75, );

	/**
	 * Lehrbefähigung 'Textilgestaltung'
	 */
	public static readonly TX : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("TX", 76, );

	/**
	 * Lehrbefähigung 'Unterweisung'
	 */
	public static readonly UW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UW", 77, );

	/**
	 * Lehrbefähigung 'Werken (Musisches)'
	 */
	public static readonly W : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("W", 78, );

	/**
	 * Lehrbefähigung 'Slowenisch (Muttersprachl. Unterricht)'
	 */
	public static readonly WM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WM", 79, );

	/**
	 * Lehrbefähigung 'Wirtschaftslehre/Politik'
	 */
	public static readonly WP : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WP", 80, );

	/**
	 * Lehrbefähigung 'Technisches Werken'
	 */
	public static readonly WT : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WT", 81, );

	/**
	 * Lehrbefähigung 'Wirtschafts- und Arbeitslehre'
	 */
	public static readonly WW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("WW", 82, );

	/**
	 * Lehrbefähigung 'sonstige Muttersprache'
	 */
	public static readonly XM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("XM", 83, );

	/**
	 * Lehrbefähigung 'Syrisch-orthodoxe Religionslehre'
	 */
	public static readonly YR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("YR", 84, );

	/**
	 * Lehrbefähigung 'Neugriechisch'
	 */
	public static readonly Z : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("Z", 85, );

	/**
	 * Lehrbefähigung 'Alevitische Religionslehre nach den Grundsätzen des AABF'
	 */
	public static readonly AR : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AR", 86, );

	/**
	 * Lehrbefähigung 'Ästhetische Erziehung'
	 */
	public static readonly AE : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("AE", 87, );

	/**
	 * Lehrbefähigung 'Deutsch für Schülerinnen und Schüler mit Zuwanderungsgeschichte'
	 */
	public static readonly DZ : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DZ", 88, );

	/**
	 * Lehrbefähigung 'Mathematische Grundbildung'
	 */
	public static readonly MG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MG", 89, );

	/**
	 * Lehrbefähigung 'Natur- und Gesellschaftswissenschaften'
	 */
	public static readonly NG : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NG", 90, );

	/**
	 * Lehrbefähigung 'Sprachliche Grundbildung'
	 */
	public static readonly SB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SB", 91, );

	/**
	 * Lehrbefähigung 'Religionslehre der mennonitischen Brüdergemeinden in RW (Lehrerlaubnis)'
	 */
	public static readonly MB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MB", 92, );

	/**
	 * Lehrbefähigung 'Bulgarisch (Herkunftssprache)'
	 */
	public static readonly VM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("VM", 93, );

	/**
	 * Lehrbefähigung 'Farsi (Herkunftssprache)'
	 */
	public static readonly QM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("QM", 94, );

	/**
	 * Lehrbefähigung 'Koreanisch (Herkunftssprache)'
	 */
	public static readonly YM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("YM", 95, );

	/**
	 * Lehrbefähigung 'Kurdische Sprachen (Herkunftssprache) (Sorani, Komaci, Zaza)'
	 */
	public static readonly ZM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ZM", 96, );

	/**
	 * Lehrbefähigung 'Niederländisch (Herkunftssprache)'
	 */
	public static readonly NM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NM", 97, );

	/**
	 * Lehrbefähigung 'Rumänisch (Herkunftssprache)'
	 */
	public static readonly UM : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("UM", 98, );

	/**
	 * Lehrbefähigung 'Islamische Religionslehre'
	 */
	public static readonly IL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("IL", 99, );

	/**
	 * Lehrbefähigung 'Arabisch'
	 */
	public static readonly A : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("A", 100, );

	/**
	 * Lehrbefähigung 'Braille´sche Punktschrift'
	 */
	public static readonly BN : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("BN", 101, );

	/**
	 * Lehrbefähigung 'Darstellen und Gestalten'
	 */
	public static readonly DS : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DS", 102, );

	/**
	 * Lehrbefähigung 'Hauswirtschaft (Konsum/Ernährung/Gesundheit)'
	 */
	public static readonly EL : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("EL", 103, );

	/**
	 * Lehrbefähigung 'Design/Fotografie'
	 */
	public static readonly DF : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("DF", 104, );

	/**
	 * Lehrbefähigung 'Gesellschaftswissenschaften'
	 */
	public static readonly GW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("GW", 105, );

	/**
	 * Lehrbefähigung 'Malerei/Grafik/Gestaltung'
	 */
	public static readonly MJ : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("MJ", 106, );

	/**
	 * Lehrbefähigung 'Naturwissenschaften'
	 */
	public static readonly NW : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("NW", 107, );

	/**
	 * Lehrbefähigung 'Sozialwesen'
	 */
	public static readonly SI : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("SI", 108, );

	/**
	 * Lehrbefähigung 'Zusatzqualifikation Bilinguales Lernen'
	 */
	public static readonly ZB : LehrerLehrbefaehigung = new LehrerLehrbefaehigung("ZB", 109, );

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

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
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
