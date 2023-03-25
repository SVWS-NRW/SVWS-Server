import { JavaObject } from '../../../java/lang/JavaObject';
import { BilingualeSpracheKatalogEintrag } from '../../../core/data/fach/BilingualeSpracheKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ZulaessigesFach } from '../../../core/types/fach/ZulaessigesFach';
import { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { Vector } from '../../../java/util/Vector';

export class BilingualeSprache extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BilingualeSprache> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, BilingualeSprache> = new Map<string, BilingualeSprache>();

	/**
	 * Bilinguale Sprache Englisch
	 */
	public static readonly ENGLISCH : BilingualeSprache = new BilingualeSprache("ENGLISCH", 0, [new BilingualeSpracheKatalogEintrag(1000, ZulaessigesFach.E, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, null)]);

	/**
	 * Bilinguale Sprache Französisch
	 */
	public static readonly FRANZOESISCH : BilingualeSprache = new BilingualeSprache("FRANZOESISCH", 1, [new BilingualeSpracheKatalogEintrag(2000, ZulaessigesFach.F, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, null)]);

	/**
	 * Bilinguale Sprache Italienisch
	 */
	public static readonly ITALIENISCH : BilingualeSprache = new BilingualeSprache("ITALIENISCH", 2, [new BilingualeSpracheKatalogEintrag(3000, ZulaessigesFach.I, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, null)]);

	/**
	 * Bilinguale Sprache Niederländisch
	 */
	public static readonly NIEDERLAENDISCH : BilingualeSprache = new BilingualeSprache("NIEDERLAENDISCH", 3, [new BilingualeSpracheKatalogEintrag(4000, ZulaessigesFach.N, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, null)]);

	/**
	 * Bilinguale Sprache Spanisch
	 */
	public static readonly SPANISCH : BilingualeSprache = new BilingualeSprache("SPANISCH", 4, [new BilingualeSpracheKatalogEintrag(5000, ZulaessigesFach.S, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, null)]);

	/**
	 * Bilinguale Sprache Türkisch
	 */
	public static readonly TUERKISCH : BilingualeSprache = new BilingualeSprache("TUERKISCH", 5, [new BilingualeSpracheKatalogEintrag(6000, ZulaessigesFach.T, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, null)]);

	/**
	 * Bilinguale Sprache Neugriechisch
	 */
	public static readonly NEUGRIECHIESCH : BilingualeSprache = new BilingualeSprache("NEUGRIECHIESCH", 6, [new BilingualeSpracheKatalogEintrag(7000, ZulaessigesFach.Z, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der bilingualen Sprache
	 */
	public readonly daten : BilingualeSpracheKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der bilingualen Sprache
	 */
	public readonly historie : Array<BilingualeSpracheKatalogEintrag>;

	/**
	 * Eine Map, welche der ID der bilingualen Sprache die Instanz dieser Aufzählung zuordnet.
	 */
	private static readonly _mapEintragByID : HashMap<number, BilingualeSpracheKatalogEintrag> = new HashMap();

	/**
	 * Eine Map, welche der ID der bilingualen Sprache die Instanz dieser Aufzählung zuordnet.
	 */
	private static readonly _mapByID : HashMap<number, BilingualeSprache> = new HashMap();

	/**
	 * Eine Map, welche dem Kürzel der bilingualen Sprache die Instanz dieser Aufzählung zuordnet.
	 */
	private static readonly _mapByKuerzel : HashMap<string, BilingualeSprache> = new HashMap();

	/**
	 * Die Schulformen, bei welchen die bilingualen Sprache vorkommt
	 */
	private schulformen : Array<Vector<Schulform>>;

	/**
	 * Erzeugt eine bilingualen Sprache in der Aufzählung.
	 *
	 * @param historie   die Historie der bilingualen Sprache, welche ein Array von
	 *                   {@link BilingualeSpracheKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<BilingualeSpracheKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BilingualeSprache.all_values_by_ordinal.push(this);
		BilingualeSprache.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.schulformen = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++) {
			this.schulformen[i] = new Vector();
			for (const kuerzel of historie[i].schulformen) {
				const sf : Schulform | null = Schulform.getByKuerzel(kuerzel);
				if (sf !== null)
					this.schulformen[i].add(sf);
			}
		}
	}

	/**
	 * Gibt eine Map von den IDs der bilingualen Sprachen auf die zugehörigen Katalog-Einträge
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der bilingualen Sprachen auf die zugehörigen Katalog-Einträge
	 */
	private static getMapEintragByID() : HashMap<number, BilingualeSpracheKatalogEintrag> {
		if (BilingualeSprache._mapEintragByID.size() === 0)
			for (const s of BilingualeSprache.values())
				for (const k of s.historie)
					BilingualeSprache._mapEintragByID.put(k.id, k);
		return BilingualeSprache._mapEintragByID;
	}

	/**
	 * Gibt eine Map von den IDs der bilingualen Sprachen auf die zugehörigen bilingualen Sprachen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der bilingualen Sprachen auf die zugehörigen bilingualen Sprachen
	 */
	private static getMapByID() : HashMap<number, BilingualeSprache> {
		if (BilingualeSprache._mapByID.size() === 0)
			for (const s of BilingualeSprache.values())
				BilingualeSprache._mapByID.put(s.daten.id, s);
		return BilingualeSprache._mapByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der bilingualen Sprachen auf die zugehörigen bilingualen Sprachen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der bilingualen Sprachen auf die zugehörigen bilingualen Sprachen
	 */
	private static getMapByKuerzel() : HashMap<string, BilingualeSprache> {
		if (BilingualeSprache._mapByKuerzel.size() === 0)
			for (const s of BilingualeSprache.values())
				BilingualeSprache._mapByKuerzel.put(s.daten.kuerzel, s);
		return BilingualeSprache._mapByKuerzel;
	}

	/**
	 * Liefert alle Schulformen zurück, bei welchen die bilingualen Sprache vorkommt.
	 *
	 * @return eine Liste der Schulformen
	 */
	public getSchulformen() : List<Schulform> {
		return this.schulformen[this.historie.length - 1];
	}

	/**
	 * Prüft, ob die Schulform bei dieser bilingualen Sprache zulässig ist.
	 *
	 * @param schulform    die Schulform
	 *
	 * @return true, falls die bilingualen Sprache in der Schulform zulässig ist, ansonsten false.
	 */
	private hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null))
			return false;
		if (this.daten.schulformen !== null) {
			for (let i : number = 0; i < this.daten.schulformen.size(); i++) {
				const sfKuerzel : string | null = this.daten.schulformen.get(i);
				if (JavaObject.equalsTranspiler(sfKuerzel, (schulform.daten.kuerzel)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert den Katalog-Eintrag der bilingualen Sprache zu der übergebenen ID zurück.
	 *
	 * @param id   die ID des Katalog-Eintrags
	 *
	 * @return der Katalog-Eintrag der bilingualen Sprache oder null, falls die ID ungültig ist
	 */
	public static getKatalogEintragByID(id : number) : BilingualeSpracheKatalogEintrag | null {
		return BilingualeSprache.getMapEintragByID().get(id);
	}

	/**
	 * Liefert die bilingualen Sprache zu der übergebenen ID der bilingualen Sprache zurück.
	 *
	 * @param id   die ID der bilingualen Sprache
	 *
	 * @return die bilingualen Sprache oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : BilingualeSprache | null {
		return BilingualeSprache.getMapByID().get(id);
	}

	/**
	 * Liefert die bilingualen Sprache zu der übergebenen ID der bilingualen Sprache zurück.
	 *
	 * @param kuerzel   das Kürzel der bilingualen Sprache
	 *
	 * @return die bilingualen Sprache oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : BilingualeSprache | null {
		return BilingualeSprache.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Bestimmt alle bilingualen Sprachen, die in der angegebenen Schulform zulässig sind.
	 *
	 * @param schulform    die Schulform
	 *
	 * @return die bilingualen Sprache in der angegebenen Schulform
	 */
	public static get(schulform : Schulform | null) : List<BilingualeSprache> {
		const faecher : Vector<BilingualeSprache> = new Vector();
		if (schulform === null)
			return faecher;
		const fachgruppen : Array<BilingualeSprache> = BilingualeSprache.values();
		for (let i : number = 0; i < fachgruppen.length; i++) {
			const fg : BilingualeSprache | null = fachgruppen[i];
			if (fg.hasSchulform(schulform))
				faecher.add(fg);
		}
		return faecher;
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
		if (!(other instanceof BilingualeSprache))
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
	public compareTo(other : BilingualeSprache) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BilingualeSprache> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BilingualeSprache | null {
		const tmp : BilingualeSprache | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.fach.BilingualeSprache'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_fach_BilingualeSprache(obj : unknown) : BilingualeSprache {
	return obj as BilingualeSprache;
}
