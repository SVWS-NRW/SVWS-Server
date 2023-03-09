import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { LehrerKatalogMinderleistungsartEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogMinderleistungsartEintrag } from '../../../core/data/lehrer/LehrerKatalogMinderleistungsartEintrag';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerMinderleistungArt extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerMinderleistungArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LehrerMinderleistungArt> = new Map<string, LehrerMinderleistungArt>();

	/**
	 * Minderleistungsart 'Pflichtstundenermäßigung aus Altersgründen' 
	 */
	public static readonly ID_200 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_200", 0, [new LehrerKatalogMinderleistungsartEintrag(37, "200", "Pflichtstundenermäßigung aus Altersgründen", null, null)]);

	/**
	 * Minderleistungsart 'Pflichtstundenermäßigung wegen Schwerbehinderung (Regelanrechnung)' 
	 */
	public static readonly ID_210 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_210", 1, [new LehrerKatalogMinderleistungsartEintrag(38, "210", "Pflichtstundenermäßigung wegen Schwerbehinderung (Regelanrechnung)", null, null)]);

	/**
	 * Minderleistungsart 'Pflichtstundenermäßigung wegen Schwerbehinderung (Erhöhung auf Antrag)' 
	 */
	public static readonly ID_220 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_220", 2, [new LehrerKatalogMinderleistungsartEintrag(39, "220", "Pflichtstundenermäßigung wegen Schwerbehinderung (Erhöhung auf Antrag)", null, null)]);

	/**
	 * Minderleistungsart 'Beurlaubung, Rückkehr im Laufe des Schuljahres' 
	 */
	public static readonly ID_230 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_230", 3, [new LehrerKatalogMinderleistungsartEintrag(40, "230", "Beurlaubung, Rückkehr im Laufe des Schuljahres", null, null)]);

	/**
	 * Minderleistungsart 'Langfristige Erkrankung' 
	 */
	public static readonly ID_240 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_240", 4, [new LehrerKatalogMinderleistungsartEintrag(41, "240", "Langfristige Erkrankung", null, null)]);

	/**
	 * Minderleistungsart 'Abwesend wegen Beschäftigungsverbot gem. § 3 MuSchG' 
	 */
	public static readonly ID_250 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_250", 5, [new LehrerKatalogMinderleistungsartEintrag(42, "250", "Abwesend wegen Beschäftigungsverbot gem. § 3 MuSchG", null, null)]);

	/**
	 * Minderleistungsart 'Abwesend wegen Teilbeschäftigungsverbot gem. § 3 MuSchG' 
	 */
	public static readonly ID_255 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_255", 6, [new LehrerKatalogMinderleistungsartEintrag(53, "255", "Abwesend wegen Teilbeschäftigungsverbot gem. § 3 MuSchG", null, null)]);

	/**
	 * Minderleistungsart 'Wiedereingliederungsmaßnahme' 
	 */
	public static readonly ID_260 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_260", 7, [new LehrerKatalogMinderleistungsartEintrag(43, "260", "Wiedereingliederungsmaßnahme", null, null)]);

	/**
	 * Minderleistungsart 'Rückgabe vorgeleisteter Stunden wegen Nichtinanspruchnahme von Altersteilzeit' 
	 */
	public static readonly ID_270 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_270", 8, [new LehrerKatalogMinderleistungsartEintrag(44, "270", "Rückgabe vorgeleisteter Stunden wegen Nichtinanspruchnahme von Altersteilzeit", null, null)]);

	/**
	 * Minderleistungsart 'Rückgabe der Vorgriffsstunden' 
	 */
	public static readonly ID_275 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_275", 9, [new LehrerKatalogMinderleistungsartEintrag(51, "275", "Rückgabe der Vorgriffsstunden", null, null)]);

	/**
	 * Minderleistungsart 'Seiteneinsteigerentlastung' 
	 */
	public static readonly ID_280 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_280", 10, [new LehrerKatalogMinderleistungsartEintrag(45, "280", "Seiteneinsteigerentlastung", null, null)]);

	/**
	 * Minderleistungsart 'Ermäßigungs-/Freistellungsphase "Teilzeitbeschäftigung im Blockmodell" (§ 65 LBG) (vormals Sabbatjahr)' 
	 */
	public static readonly ID_290 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_290", 11, [new LehrerKatalogMinderleistungsartEintrag(46, "290", "Ermäßigungs-/Freistellungsphase \"Teilzeitbeschäftigung im Blockmodell\" (§ 65 LBG) (vormals Sabbatjahr)", null, null)]);

	/**
	 * Minderleistungsart 'Sonstige Ermäßigungen aus besonderen persönlichen Gründen' 
	 */
	public static readonly ID_300 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_300", 12, [new LehrerKatalogMinderleistungsartEintrag(47, "300", "Sonstige Ermäßigungen aus besonderen persönlichen Gründen", null, null)]);

	/**
	 * Minderleistungsart 'Abrundung der Pflichtstundenzahl wegen Aufrundung im vorhergehenden Schuljahr ' 
	 */
	public static readonly ID_350 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_350", 13, [new LehrerKatalogMinderleistungsartEintrag(48, "350", "Abrundung der Pflichtstundenzahl wegen Aufrundung im vorhergehenden Schuljahr", null, null)]);

	/**
	 * Minderleistungsart 'Unterschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)' 
	 */
	public static readonly ID_360 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_360", 14, [new LehrerKatalogMinderleistungsartEintrag(49, "360", "Unterschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)", null, null)]);

	/**
	 * Minderleistungsart 'Unterschreitung der Pflichtstundenzahl wegen COVID-19' 
	 */
	public static readonly ID_365 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_365", 15, [new LehrerKatalogMinderleistungsartEintrag(54, "365", "Unterschreitung der Pflichtstundenzahl wegen COVID-19", null, null)]);

	/**
	 * Minderleistungsart 'Unterschreitung der Pflichtstundenzahl wegen Pflichstunden-Bandbreite' 
	 */
	public static readonly ID_370 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_370", 16, [new LehrerKatalogMinderleistungsartEintrag(50, "370", "Unterschreitung der Pflichtstundenzahl wegen Pflichstunden-Bandbreite", null, null)]);

	/**
	 * Minderleistungsart 'Fortbildung: Nachträglicher Erwerb des sonderpädagogischen Lehramtes' 
	 */
	public static readonly ID_380 : LehrerMinderleistungArt = new LehrerMinderleistungArt("ID_380", 17, [new LehrerKatalogMinderleistungsartEintrag(52, "380", "Fortbildung: Nachträglicher Erwerb des sonderpädagogischen Lehramtes", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. 
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Art von Minderleistung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null 
	 */
	public readonly daten : LehrerKatalogMinderleistungsartEintrag;

	/**
	 * Die Historie mit den Einträgen der Art von Minderleistung 
	 */
	public readonly historie : Array<LehrerKatalogMinderleistungsartEintrag>;

	/**
	 * Eine Hashmap mit allen Arten von Minderleistungen, welche ihrer ID zugeordnet sind. 
	 */
	private static readonly _artenByID : HashMap<number, LehrerMinderleistungArt | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Arten von Minderleistungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. 
	 */
	private static readonly _artenByKuerzel : HashMap<string, LehrerMinderleistungArt | null> = new HashMap();

	/**
	 * Erzeugt eine neue Art von Minderleistung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Art von Minderleistung, welches ein Array von {@link LehrerKatalogMinderleistungsartEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogMinderleistungsartEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerMinderleistungArt.all_values_by_ordinal.push(this);
		LehrerMinderleistungArt.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Minderleistungsarten auf die zugehörigen Minderleistungsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Minderleistungsarten auf die zugehörigen Minderleistungsarten
	 */
	private static getMapArtenByID() : HashMap<number, LehrerMinderleistungArt | null> {
		if (LehrerMinderleistungArt._artenByID.size() === 0) 
			for (let g of LehrerMinderleistungArt.values()) 
				LehrerMinderleistungArt._artenByID.put(g.daten.id, g);
		return LehrerMinderleistungArt._artenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Minderleistungsarten auf die zugehörigen Minderleistungsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Minderleistungsarten auf die zugehörigen Minderleistungsarten
	 */
	private static getMapArtenByKuerzel() : HashMap<string, LehrerMinderleistungArt | null> {
		if (LehrerMinderleistungArt._artenByKuerzel.size() === 0) 
			for (let g of LehrerMinderleistungArt.values()) 
				LehrerMinderleistungArt._artenByKuerzel.put(g.daten.kuerzel, g);
		return LehrerMinderleistungArt._artenByKuerzel;
	}

	/**
	 * Gibt die Art der Minderleistung anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Art der Minderleistung
	 * 
	 * @return die Art der Minderleistung oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerMinderleistungArt | null {
		return LehrerMinderleistungArt.getMapArtenByID().get(id);
	}

	/**
	 * Gibt die Art der Minderleistung anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Art der Minderleistung
	 * 
	 * @return die Art der Minderleistung oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerMinderleistungArt | null {
		return LehrerMinderleistungArt.getMapArtenByKuerzel().get(kuerzel);
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
		if (!(other instanceof LehrerMinderleistungArt))
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
	public compareTo(other : LehrerMinderleistungArt) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerMinderleistungArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerMinderleistungArt | null {
		let tmp : LehrerMinderleistungArt | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.lehrer.LehrerMinderleistungArt'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_lehrer_LehrerMinderleistungArt(obj : unknown) : LehrerMinderleistungArt {
	return obj as LehrerMinderleistungArt;
}
