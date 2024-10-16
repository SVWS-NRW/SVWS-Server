import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class LehrerLehramt extends JavaEnum<LehrerLehramt> implements CoreType<LehrerLehramtKatalogEintrag, LehrerLehramt> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerLehramt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerLehramt> = new Map<string, LehrerLehramt>();

	/**
	 * Lehramt 'für die Primarstufe'
	 */
	public static readonly ID_00 : LehrerLehramt = new LehrerLehramt("ID_00", 0, );

	/**
	 * Lehramt 'an der Grund- und Hauptschule (Stufenschwerpunkt I)'
	 */
	public static readonly ID_01 : LehrerLehramt = new LehrerLehramt("ID_01", 1, );

	/**
	 * Lehramt 'an der Grund- und Hauptschule (Stufenschwerpunkt II)'
	 */
	public static readonly ID_02 : LehrerLehramt = new LehrerLehramt("ID_02", 2, );

	/**
	 * Lehramt 'an der Volksschule'
	 */
	public static readonly ID_03 : LehrerLehramt = new LehrerLehramt("ID_03", 3, );

	/**
	 * Lehramt 'für Sonderpädagogik'
	 */
	public static readonly ID_09 : LehrerLehramt = new LehrerLehramt("ID_09", 4, );

	/**
	 * Lehramt 'an Sonderschulen'
	 */
	public static readonly ID_10 : LehrerLehramt = new LehrerLehramt("ID_10", 5, );

	/**
	 * Lehramt 'für Sonderpädagogik und die Primarstufe'
	 */
	public static readonly ID_11 : LehrerLehramt = new LehrerLehramt("ID_11", 6, );

	/**
	 * Lehramt 'für Sonderpädagogik und die Sekundarstufe I'
	 */
	public static readonly ID_12 : LehrerLehramt = new LehrerLehramt("ID_12", 7, );

	/**
	 * Lehramt 'Sonderpädagogik LPO 03'
	 */
	public static readonly ID_14 : LehrerLehramt = new LehrerLehramt("ID_14", 8, );

	/**
	 * Lehramt 'Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtschule -Schwerp.- Grundschule'
	 */
	public static readonly ID_15 : LehrerLehramt = new LehrerLehramt("ID_15", 9, );

	/**
	 * Lehramt 'Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtsch. -Schwerp.- H/R/Gesamtsch.'
	 */
	public static readonly ID_16 : LehrerLehramt = new LehrerLehramt("ID_16", 10, );

	/**
	 * Lehramt 'für die Sekundarstufe I und die Primarstufe'
	 */
	public static readonly ID_19 : LehrerLehramt = new LehrerLehramt("ID_19", 11, );

	/**
	 * Lehramt 'für die Sekundarstufe I'
	 */
	public static readonly ID_20 : LehrerLehramt = new LehrerLehramt("ID_20", 12, );

	/**
	 * Lehramt 'an der Realschule'
	 */
	public static readonly ID_21 : LehrerLehramt = new LehrerLehramt("ID_21", 13, );

	/**
	 * Lehramt 'für die Sekundarstufe II und die Sekundarstufe I'
	 */
	public static readonly ID_24 : LehrerLehramt = new LehrerLehramt("ID_24", 14, );

	/**
	 * Lehramt 'am Gymnasium'
	 */
	public static readonly ID_25 : LehrerLehramt = new LehrerLehramt("ID_25", 15, );

	/**
	 * Lehramt 'Gymnasium und Gesamtschule'
	 */
	public static readonly ID_27 : LehrerLehramt = new LehrerLehramt("ID_27", 16, );

	/**
	 * Lehramt 'für die Sekundarstufe II (ohne berufliche Fachrichtung)'
	 */
	public static readonly ID_29 : LehrerLehramt = new LehrerLehramt("ID_29", 17, );

	/**
	 * Lehramt 'an berufsbildenden Schulen (alle Lehrer mit 2. Staatsprüfung oder erworbener Anstellungsfähigkeit)'
	 */
	public static readonly ID_30 : LehrerLehramt = new LehrerLehramt("ID_30", 18, );

	/**
	 * Lehramt 'für die Sekundarstufe II und Sonderpädagogik'
	 */
	public static readonly ID_31 : LehrerLehramt = new LehrerLehramt("ID_31", 19, );

	/**
	 * Lehramt 'für die Sekundarstufe II (mit beruflicher Fachrichtung)'
	 */
	public static readonly ID_32 : LehrerLehramt = new LehrerLehramt("ID_32", 20, );

	/**
	 * Lehramt 'Berufskolleg'
	 */
	public static readonly ID_35 : LehrerLehramt = new LehrerLehramt("ID_35", 21, );

	/**
	 * Lehramt 'Fachhochschullehrer'
	 */
	public static readonly ID_40 : LehrerLehramt = new LehrerLehramt("ID_40", 22, );

	/**
	 * Lehramt 'Fachlehrer an Sonderschulen'
	 */
	public static readonly ID_50 : LehrerLehramt = new LehrerLehramt("ID_50", 23, );

	/**
	 * Lehramt 'Religionslehrer/Geistlicher/Katechet'
	 */
	public static readonly ID_51 : LehrerLehramt = new LehrerLehramt("ID_51", 24, );

	/**
	 * Lehramt 'Fachlehrer mit der Befähigung für die Laufbahn des Werkstattlehrers'
	 */
	public static readonly ID_52 : LehrerLehramt = new LehrerLehramt("ID_52", 25, );

	/**
	 * Lehramt 'Fachlehrer'
	 */
	public static readonly ID_53 : LehrerLehramt = new LehrerLehramt("ID_53", 26, );

	/**
	 * Lehramt 'Fachlehrer für Kurzschrift und Maschinenschreiben'
	 */
	public static readonly ID_54 : LehrerLehramt = new LehrerLehramt("ID_54", 27, );

	/**
	 * Lehramt 'Fachlehrer mit der Befähigung für die Laufbahn des technischen Lehrers an beruflichen Schulen'
	 */
	public static readonly ID_55 : LehrerLehramt = new LehrerLehramt("ID_55", 28, );

	/**
	 * Lehramt 'Schulkindergärtnerin'
	 */
	public static readonly ID_56 : LehrerLehramt = new LehrerLehramt("ID_56", 29, );

	/**
	 * Lehramt 'ohne sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter'
	 */
	public static readonly ID_57 : LehrerLehramt = new LehrerLehramt("ID_57", 30, );

	/**
	 * Lehramt 'ohne sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.'
	 */
	public static readonly ID_58 : LehrerLehramt = new LehrerLehramt("ID_58", 31, );

	/**
	 * Lehramt 'ohne sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe'
	 */
	public static readonly ID_59 : LehrerLehramt = new LehrerLehramt("ID_59", 32, );

	/**
	 * Lehramt 'mit sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter'
	 */
	public static readonly ID_60 : LehrerLehramt = new LehrerLehramt("ID_60", 33, );

	/**
	 * Lehramt 'mit sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.'
	 */
	public static readonly ID_61 : LehrerLehramt = new LehrerLehramt("ID_61", 34, );

	/**
	 * Lehramt 'mit sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe'
	 */
	public static readonly ID_62 : LehrerLehramt = new LehrerLehramt("ID_62", 35, );

	/**
	 * Heilpädagoge/-in
	 */
	public static readonly ID_63 : LehrerLehramt = new LehrerLehramt("ID_63", 36, );

	/**
	 * Handwerksmeister/-in
	 */
	public static readonly ID_64 : LehrerLehramt = new LehrerLehramt("ID_64", 37, );

	/**
	 * Alltagshelfer/-in
	 */
	public static readonly ID_65 : LehrerLehramt = new LehrerLehramt("ID_65", 38, );

	/**
	 * Lehramt 'Lehrer, der eine Qualifikation erworben hat, die der 1. Staatsprüfung entspricht (z.B. Diplom, sofern nicht Schlüssel 98)'
	 */
	public static readonly ID_96 : LehrerLehramt = new LehrerLehramt("ID_96", 39, );

	/**
	 * Lehramt 'Lehrer, der außerhalb des Geltungsbereichs des Grundgesetzes seine Qualifikation erworben hat'
	 */
	public static readonly ID_97 : LehrerLehramt = new LehrerLehramt("ID_97", 40, );

	/**
	 * Lehramt 'Lehramtsanwärter/Studienreferendar'
	 */
	public static readonly ID_98 : LehrerLehramt = new LehrerLehramt("ID_98", 41, );

	/**
	 * Lehramt 'Sonstiger Lehrer (Gymnastik-, Werk-, Hauswirtschaftslehrer, Übungsleiter)'
	 */
	public static readonly ID_99 : LehrerLehramt = new LehrerLehramt("ID_99", 42, );

	/**
	 * Lehramt 'Schulverwaltungsassistent'
	 */
	public static readonly ID_70 : LehrerLehramt = new LehrerLehramt("ID_70", 43, );

	/**
	 * Lehramt 'Grundschule'
	 */
	public static readonly ID_04 : LehrerLehramt = new LehrerLehramt("ID_04", 44, );

	/**
	 * Lehramt 'Haupt-, Real- und  Gesamtschule'
	 */
	public static readonly ID_17 : LehrerLehramt = new LehrerLehramt("ID_17", 45, );

	/**
	 * Lehramt 'Sonderpädagogische Förderung'
	 */
	public static readonly ID_08 : LehrerLehramt = new LehrerLehramt("ID_08", 46, );

	/**
	 * Lehramt 'Haupt-, Real-, Sekundar- und Gesamtschule'
	 */
	public static readonly ID_18 : LehrerLehramt = new LehrerLehramt("ID_18", 47, );

	/**
	 * Lehramt 'Berufskolleg mit einer beruflichen Fachrichtung (§ 59 LVO)'
	 */
	public static readonly ID_49 : LehrerLehramt = new LehrerLehramt("ID_49", 48, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerLehramt.all_values_by_ordinal.push(this);
		LehrerLehramt.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerLehramtKatalogEintrag, LehrerLehramt>) : void {
		CoreTypeDataManager.putManager(LehrerLehramt.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerLehramtKatalogEintrag, LehrerLehramt> {
		return CoreTypeDataManager.getManager(LehrerLehramt.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLehramt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerLehramt | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerLehramtKatalogEintrag, LehrerLehramt> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerLehramtKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<LehrerLehramtKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerLehramt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.lehrer.LehrerLehramt', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerLehramt>('de.svws_nrw.asd.types.lehrer.LehrerLehramt');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerLehramt(obj : unknown) : LehrerLehramt {
	return obj as LehrerLehramt;
}
