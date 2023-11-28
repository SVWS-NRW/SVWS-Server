import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { BilingualeSpracheKatalogEintrag } from '../../../core/data/fach/BilingualeSpracheKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ZulaessigesFach } from '../../../core/types/fach/ZulaessigesFach';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';

export class BilingualeSprache extends JavaEnum<BilingualeSprache> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BilingualeSprache> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BilingualeSprache> = new Map<string, BilingualeSprache>();

	/**
	 * Bilinguale Sprache Englisch
	 */
	public static readonly ENGLISCH : BilingualeSprache = new BilingualeSprache("ENGLISCH", 0, [new BilingualeSpracheKatalogEintrag(1000, ZulaessigesFach.E, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, 2022), new BilingualeSpracheKatalogEintrag(1001, ZulaessigesFach.E, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), 2023, null)]);

	/**
	 * Bilinguale Sprache Französisch
	 */
	public static readonly FRANZOESISCH : BilingualeSprache = new BilingualeSprache("FRANZOESISCH", 1, [new BilingualeSpracheKatalogEintrag(2000, ZulaessigesFach.F, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, 2022), new BilingualeSpracheKatalogEintrag(2001, ZulaessigesFach.F, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), 2023, null)]);

	/**
	 * Bilinguale Sprache Italienisch
	 */
	public static readonly ITALIENISCH : BilingualeSprache = new BilingualeSprache("ITALIENISCH", 2, [new BilingualeSpracheKatalogEintrag(3000, ZulaessigesFach.I, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, 2022), new BilingualeSpracheKatalogEintrag(3001, ZulaessigesFach.I, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), 2023, null)]);

	/**
	 * Bilinguale Sprache Niederländisch
	 */
	public static readonly NIEDERLAENDISCH : BilingualeSprache = new BilingualeSprache("NIEDERLAENDISCH", 3, [new BilingualeSpracheKatalogEintrag(4000, ZulaessigesFach.N, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, 2022), new BilingualeSpracheKatalogEintrag(4001, ZulaessigesFach.N, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), 2023, null)]);

	/**
	 * Bilinguale Sprache Spanisch
	 */
	public static readonly SPANISCH : BilingualeSprache = new BilingualeSprache("SPANISCH", 4, [new BilingualeSpracheKatalogEintrag(5000, ZulaessigesFach.S, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, 2022), new BilingualeSpracheKatalogEintrag(5001, ZulaessigesFach.S, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), 2023, null)]);

	/**
	 * Bilinguale Sprache Türkisch
	 */
	public static readonly TUERKISCH : BilingualeSprache = new BilingualeSprache("TUERKISCH", 5, [new BilingualeSpracheKatalogEintrag(6000, ZulaessigesFach.T, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, 2022), new BilingualeSpracheKatalogEintrag(6001, ZulaessigesFach.T, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), 2023, null)]);

	/**
	 * Bilinguale Sprache Neugriechisch
	 */
	public static readonly NEUGRIECHIESCH : BilingualeSprache = new BilingualeSprache("NEUGRIECHIESCH", 6, [new BilingualeSpracheKatalogEintrag(7000, ZulaessigesFach.Z, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.GM, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), null, 2022), new BilingualeSpracheKatalogEintrag(7001, ZulaessigesFach.Z, Arrays.asList(Schulform.BK, Schulform.SB, Schulform.G, Schulform.GE, Schulform.GY, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR), 2023, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 2;

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
	private schulformen : Array<ArrayList<Schulform>>;

	/**
	 * Erzeugt eine bilingualen Sprache in der Aufzählung.
	 *
	 * @param historie   die Historie der bilingualen Sprache, welche ein Array von
	 *                   {@link BilingualeSpracheKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<BilingualeSpracheKatalogEintrag>) {
		super(name, ordinal);
		BilingualeSprache.all_values_by_ordinal.push(this);
		BilingualeSprache.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.schulformen = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++) {
			this.schulformen[i] = new ArrayList();
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
		const faecher : ArrayList<BilingualeSprache> = new ArrayList();
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.fach.BilingualeSprache', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_fach_BilingualeSprache(obj : unknown) : BilingualeSprache {
	return obj as BilingualeSprache;
}
