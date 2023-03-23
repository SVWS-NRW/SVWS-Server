import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { LehrerKatalogLehramtEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLehramtEintrag } from '../../../core/data/lehrer/LehrerKatalogLehramtEintrag';

export class LehrerLehramt extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerLehramt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LehrerLehramt> = new Map<string, LehrerLehramt>();

	/**
	 * Lehramt 'für die Primarstufe'
	 */
	public static readonly ID_00 : LehrerLehramt = new LehrerLehramt("ID_00", 0, [new LehrerKatalogLehramtEintrag(82, "00", "für die Primarstufe", null, null)]);

	/**
	 * Lehramt 'an der Grund- und Hauptschule (Stufenschwerpunkt I)'
	 */
	public static readonly ID_01 : LehrerLehramt = new LehrerLehramt("ID_01", 1, [new LehrerKatalogLehramtEintrag(83, "01", "an der Grund- und Hauptschule (Stufenschwerpunkt I)", null, null)]);

	/**
	 * Lehramt 'an der Grund- und Hauptschule (Stufenschwerpunkt II)'
	 */
	public static readonly ID_02 : LehrerLehramt = new LehrerLehramt("ID_02", 2, [new LehrerKatalogLehramtEintrag(84, "02", "an der Grund- und Hauptschule (Stufenschwerpunkt II)", null, null)]);

	/**
	 * Lehramt 'an der Volksschule'
	 */
	public static readonly ID_03 : LehrerLehramt = new LehrerLehramt("ID_03", 3, [new LehrerKatalogLehramtEintrag(85, "03", "an der Volksschule", null, null)]);

	/**
	 * Lehramt 'für Sonderpädagogik'
	 */
	public static readonly ID_09 : LehrerLehramt = new LehrerLehramt("ID_09", 4, [new LehrerKatalogLehramtEintrag(86, "09", "für Sonderpädagogik", null, null)]);

	/**
	 * Lehramt 'an Sonderschulen'
	 */
	public static readonly ID_10 : LehrerLehramt = new LehrerLehramt("ID_10", 5, [new LehrerKatalogLehramtEintrag(87, "10", "an Sonderschulen", null, null)]);

	/**
	 * Lehramt 'für Sonderpädagogik und die Primarstufe'
	 */
	public static readonly ID_11 : LehrerLehramt = new LehrerLehramt("ID_11", 6, [new LehrerKatalogLehramtEintrag(88, "11", "für Sonderpädagogik und die Primarstufe", null, null)]);

	/**
	 * Lehramt 'für Sonderpädagogik und die Sekundarstufe I'
	 */
	public static readonly ID_12 : LehrerLehramt = new LehrerLehramt("ID_12", 7, [new LehrerKatalogLehramtEintrag(89, "12", "für Sonderpädagogik und die Sekundarstufe I", null, null)]);

	/**
	 * Lehramt 'Sonderpädagogik LPO 03'
	 */
	public static readonly ID_14 : LehrerLehramt = new LehrerLehramt("ID_14", 8, [new LehrerKatalogLehramtEintrag(90, "14", "Sonderpädagogik LPO 03", null, null)]);

	/**
	 * Lehramt 'Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtschule -Schwerp.- Grundschule'
	 */
	public static readonly ID_15 : LehrerLehramt = new LehrerLehramt("ID_15", 9, [new LehrerKatalogLehramtEintrag(91, "15", "Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtschule -Schwerp.- Grundschule", null, null)]);

	/**
	 * Lehramt 'Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtsch. -Schwerp.- H/R/Gesamtsch.'
	 */
	public static readonly ID_16 : LehrerLehramt = new LehrerLehramt("ID_16", 10, [new LehrerKatalogLehramtEintrag(92, "16", "Grund-, Haupt- u. Realschule u. entspr. Jahrg.stufen d. Gesamtsch. -Schwerp.- H/R/Gesamtsch.", null, null)]);

	/**
	 * Lehramt 'für die Sekundarstufe I und die Primarstufe'
	 */
	public static readonly ID_19 : LehrerLehramt = new LehrerLehramt("ID_19", 11, [new LehrerKatalogLehramtEintrag(93, "19", "für die Sekundarstufe I und die Primarstufe", null, null)]);

	/**
	 * Lehramt 'für die Sekundarstufe I'
	 */
	public static readonly ID_20 : LehrerLehramt = new LehrerLehramt("ID_20", 12, [new LehrerKatalogLehramtEintrag(94, "20", "für die Sekundarstufe I", null, null)]);

	/**
	 * Lehramt 'an der Realschule'
	 */
	public static readonly ID_21 : LehrerLehramt = new LehrerLehramt("ID_21", 13, [new LehrerKatalogLehramtEintrag(95, "21", "an der Realschule", null, null)]);

	/**
	 * Lehramt 'für die Sekundarstufe II und die Sekundarstufe I'
	 */
	public static readonly ID_24 : LehrerLehramt = new LehrerLehramt("ID_24", 14, [new LehrerKatalogLehramtEintrag(96, "24", "für die Sekundarstufe II und die Sekundarstufe I", null, null)]);

	/**
	 * Lehramt 'am Gymnasium'
	 */
	public static readonly ID_25 : LehrerLehramt = new LehrerLehramt("ID_25", 15, [new LehrerKatalogLehramtEintrag(97, "25", "am Gymnasium", null, null)]);

	/**
	 * Lehramt 'Gymnasium und Gesamtschule'
	 */
	public static readonly ID_27 : LehrerLehramt = new LehrerLehramt("ID_27", 16, [new LehrerKatalogLehramtEintrag(98, "27", "Gymnasium und Gesamtschule", null, null)]);

	/**
	 * Lehramt 'für die Sekundarstufe II (ohne berufliche Fachrichtung)'
	 */
	public static readonly ID_29 : LehrerLehramt = new LehrerLehramt("ID_29", 17, [new LehrerKatalogLehramtEintrag(99, "29", "für die Sekundarstufe II (ohne berufliche Fachrichtung)", null, null)]);

	/**
	 * Lehramt 'an berufsbildenden Schulen (alle Lehrer mit 2. Staatsprüfung oder erworbener Anstellungsfähigkeit)'
	 */
	public static readonly ID_30 : LehrerLehramt = new LehrerLehramt("ID_30", 18, [new LehrerKatalogLehramtEintrag(100, "30", "an berufsbildenden Schulen (alle Lehrer mit 2. Staatsprüfung oder erworbener Anstellungsfähigkeit)", null, null)]);

	/**
	 * Lehramt 'für die Sekundarstufe II und Sonderpädagogik'
	 */
	public static readonly ID_31 : LehrerLehramt = new LehrerLehramt("ID_31", 19, [new LehrerKatalogLehramtEintrag(101, "31", "für die Sekundarstufe II und Sonderpädagogik", null, null)]);

	/**
	 * Lehramt 'für die Sekundarstufe II (mit beruflicher Fachrichtung)'
	 */
	public static readonly ID_32 : LehrerLehramt = new LehrerLehramt("ID_32", 20, [new LehrerKatalogLehramtEintrag(102, "32", "für die Sekundarstufe II (mit beruflicher Fachrichtung)", null, null)]);

	/**
	 * Lehramt 'Berufskolleg'
	 */
	public static readonly ID_35 : LehrerLehramt = new LehrerLehramt("ID_35", 21, [new LehrerKatalogLehramtEintrag(103, "35", "Berufskolleg", null, null)]);

	/**
	 * Lehramt 'Fachhochschullehrer'
	 */
	public static readonly ID_40 : LehrerLehramt = new LehrerLehramt("ID_40", 22, [new LehrerKatalogLehramtEintrag(104, "40", "Fachhochschullehrer", null, null)]);

	/**
	 * Lehramt 'Fachlehrer an Sonderschulen'
	 */
	public static readonly ID_50 : LehrerLehramt = new LehrerLehramt("ID_50", 23, [new LehrerKatalogLehramtEintrag(105, "50", "Fachlehrer an Sonderschulen", null, null)]);

	/**
	 * Lehramt 'Religionslehrer/Geistlicher/Katechet'
	 */
	public static readonly ID_51 : LehrerLehramt = new LehrerLehramt("ID_51", 24, [new LehrerKatalogLehramtEintrag(106, "51", "Religionslehrer/Geistlicher/Katechet", null, null)]);

	/**
	 * Lehramt 'Fachlehrer mit der Befähigung für die Laufbahn des Werkstattlehrers'
	 */
	public static readonly ID_52 : LehrerLehramt = new LehrerLehramt("ID_52", 25, [new LehrerKatalogLehramtEintrag(107, "52", "Fachlehrer mit der Befähigung für die Laufbahn des Werkstattlehrers", null, null)]);

	/**
	 * Lehramt 'Fachlehrer'
	 */
	public static readonly ID_53 : LehrerLehramt = new LehrerLehramt("ID_53", 26, [new LehrerKatalogLehramtEintrag(108, "53", "Fachlehrer", null, null)]);

	/**
	 * Lehramt 'Fachlehrer für Kurzschrift und Maschinenschreiben'
	 */
	public static readonly ID_54 : LehrerLehramt = new LehrerLehramt("ID_54", 27, [new LehrerKatalogLehramtEintrag(109, "54", "Fachlehrer für Kurzschrift und Maschinenschreiben", null, null)]);

	/**
	 * Lehramt 'Fachlehrer mit der Befähigung für die Laufbahn des technischen Lehrers an beruflichen Schulen'
	 */
	public static readonly ID_55 : LehrerLehramt = new LehrerLehramt("ID_55", 28, [new LehrerKatalogLehramtEintrag(110, "55", "Fachlehrer mit der Befähigung für die Laufbahn des technischen Lehrers an beruflichen Schulen", null, null)]);

	/**
	 * Lehramt 'Schulkindergärtnerin'
	 */
	public static readonly ID_56 : LehrerLehramt = new LehrerLehramt("ID_56", 29, [new LehrerKatalogLehramtEintrag(111, "56", "Schulkindergärtnerin", null, null)]);

	/**
	 * Lehramt 'ohne sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter'
	 */
	public static readonly ID_57 : LehrerLehramt = new LehrerLehramt("ID_57", 30, [new LehrerKatalogLehramtEintrag(112, "57", "ohne sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter", null, null)]);

	/**
	 * Lehramt 'ohne sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.'
	 */
	public static readonly ID_58 : LehrerLehramt = new LehrerLehramt("ID_58", 31, [new LehrerKatalogLehramtEintrag(113, "58", "ohne sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.", null, null)]);

	/**
	 * Lehramt 'ohne sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe'
	 */
	public static readonly ID_59 : LehrerLehramt = new LehrerLehramt("ID_59", 32, [new LehrerKatalogLehramtEintrag(114, "59", "ohne sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe", null, null)]);

	/**
	 * Lehramt 'mit sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter'
	 */
	public static readonly ID_60 : LehrerLehramt = new LehrerLehramt("ID_60", 33, [new LehrerKatalogLehramtEintrag(115, "60", "mit sonderpäd. Zusatzausbildung:Sozialarbeiter, Sozialpädagoge, Jugendleiter", null, null)]);

	/**
	 * Lehramt 'mit sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.'
	 */
	public static readonly ID_61 : LehrerLehramt = new LehrerLehramt("ID_61", 34, [new LehrerKatalogLehramtEintrag(116, "61", "mit sonderpäd. Zusatzausbildung:Erzieher, Kindergärtnerin u.a.", null, null)]);

	/**
	 * Lehramt 'mit sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe'
	 */
	public static readonly ID_62 : LehrerLehramt = new LehrerLehramt("ID_62", 35, [new LehrerKatalogLehramtEintrag(117, "62", "mit sonderpäd. Zusatzausbildung:Sonstige pädagogische Unterrichtshilfe", null, null)]);

	/**
	 * Lehramt 'Lehrer, der eine Qualifikation erworben hat, die der 1. Staatsprüfung entspricht (z.B. Diplom, sofern nicht Schlüssel 98)'
	 */
	public static readonly ID_96 : LehrerLehramt = new LehrerLehramt("ID_96", 36, [new LehrerKatalogLehramtEintrag(118, "96", "Lehrer, der eine Qualifikation erworben hat, die der 1. Staatsprüfung entspricht (z.B. Diplom, sofern nicht Schlüssel 98)", null, null)]);

	/**
	 * Lehramt 'Lehrer, der außerhalb des Geltungsbereichs des Grundgesetzes seine Qualifikation erworben hat'
	 */
	public static readonly ID_97 : LehrerLehramt = new LehrerLehramt("ID_97", 37, [new LehrerKatalogLehramtEintrag(119, "97", "Lehrer, der außerhalb des Geltungsbereichs des Grundgesetzes seine Qualifikation erworben hat", null, null)]);

	/**
	 * Lehramt 'Lehramtsanwärter/Studienreferendar'
	 */
	public static readonly ID_98 : LehrerLehramt = new LehrerLehramt("ID_98", 38, [new LehrerKatalogLehramtEintrag(120, "98", "Lehramtsanwärter/Studienreferendar", null, null)]);

	/**
	 * Lehramt 'Sonstiger Lehrer (Gymnastik-, Werk-, Hauswirtschaftslehrer, Übungsleiter)'
	 */
	public static readonly ID_99 : LehrerLehramt = new LehrerLehramt("ID_99", 39, [new LehrerKatalogLehramtEintrag(121, "99", "Sonstiger Lehrer (Gymnastik-, Werk-, Hauswirtschaftslehrer, Übungsleiter)", null, null)]);

	/**
	 * Lehramt 'Schulverwaltungsassistent'
	 */
	public static readonly ID_70 : LehrerLehramt = new LehrerLehramt("ID_70", 40, [new LehrerKatalogLehramtEintrag(122, "70", "Schulverwaltungsassistent", null, null)]);

	/**
	 * Lehramt 'Grundschule'
	 */
	public static readonly ID_04 : LehrerLehramt = new LehrerLehramt("ID_04", 41, [new LehrerKatalogLehramtEintrag(123, "04", "Grundschule", null, null)]);

	/**
	 * Lehramt 'Haupt-, Real- und  Gesamtschule'
	 */
	public static readonly ID_17 : LehrerLehramt = new LehrerLehramt("ID_17", 42, [new LehrerKatalogLehramtEintrag(124, "17", "Haupt-, Real- und  Gesamtschule", null, null)]);

	/**
	 * Lehramt 'Sonderpädagogische Förderung'
	 */
	public static readonly ID_08 : LehrerLehramt = new LehrerLehramt("ID_08", 43, [new LehrerKatalogLehramtEintrag(125, "08", "Sonderpädagogische Förderung", null, null)]);

	/**
	 * Lehramt 'Haupt-, Real-, Sekundar- und Gesamtschule'
	 */
	public static readonly ID_18 : LehrerLehramt = new LehrerLehramt("ID_18", 44, [new LehrerKatalogLehramtEintrag(126, "18", "Haupt-, Real-, Sekundar- und Gesamtschule", null, null)]);

	/**
	 * Lehramt 'Berufskolleg mit einer beruflichen Fachrichtung (§ 59 LVO)'
	 */
	public static readonly ID_49 : LehrerLehramt = new LehrerLehramt("ID_49", 45, [new LehrerKatalogLehramtEintrag(127, "49", "Berufskolleg mit einer beruflichen Fachrichtung (§ 59 LVO)", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Lehramtes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogLehramtEintrag;

	/**
	 * Die Historie mit den Einträgen des Lehramtes
	 */
	public readonly historie : Array<LehrerKatalogLehramtEintrag>;

	/**
	 * Eine Hashmap mit allen Lehrämtern, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _aemterByID : HashMap<number, LehrerLehramt | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Lehrämtern, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _aemterByKuerzel : HashMap<string, LehrerLehramt | null> = new HashMap();

	/**
	 * Erzeugt ein neues Lehramt in der Aufzählung.
	 *
	 * @param historie   die Historie des Lehramtes, welches ein Array von {@link LehrerKatalogLehramtEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogLehramtEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerLehramt.all_values_by_ordinal.push(this);
		LehrerLehramt.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Lehrämter auf die zugehörigen Lehrämter
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den IDs der Lehrämter auf die zugehörigen Lehrämter
	 */
	private static getMapLehraemterByID() : HashMap<number, LehrerLehramt | null> {
		if (LehrerLehramt._aemterByID.size() === 0)
			for (let l of LehrerLehramt.values())
				LehrerLehramt._aemterByID.put(l.daten.id, l);
		return LehrerLehramt._aemterByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Lehrämter auf die zugehörigen Lehrämter
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den Kürzeln der Lehrämter auf die zugehörigen Lehrämter
	 */
	private static getMapLehraemterByKuerzel() : HashMap<string, LehrerLehramt | null> {
		if (LehrerLehramt._aemterByKuerzel.size() === 0)
			for (let l of LehrerLehramt.values())
				LehrerLehramt._aemterByKuerzel.put(l.daten.kuerzel, l);
		return LehrerLehramt._aemterByKuerzel;
	}

	/**
	 * Gibt das Lehramt anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID des Lehramts
	 *
	 * @return das Lehramt oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerLehramt | null {
		return LehrerLehramt.getMapLehraemterByID().get(id);
	}

	/**
	 * Gibt das Lehramt anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Lehramts
	 *
	 * @return das Lehramt oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerLehramt | null {
		return LehrerLehramt.getMapLehraemterByKuerzel().get(kuerzel);
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
		if (!(other instanceof LehrerLehramt))
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
	public compareTo(other : LehrerLehramt) : number {
		return this.__ordinal - other.__ordinal;
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
		const tmp : LehrerLehramt | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.lehrer.LehrerLehramt'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_lehrer_LehrerLehramt(obj : unknown) : LehrerLehramt {
	return obj as LehrerLehramt;
}
