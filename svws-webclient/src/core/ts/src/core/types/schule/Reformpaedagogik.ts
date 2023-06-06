import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { ReformpaedagogikKatalogEintrag } from '../../../core/data/schule/ReformpaedagogikKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';

export class Reformpaedagogik extends JavaObject implements JavaEnum<Reformpaedagogik> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Reformpaedagogik> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Reformpaedagogik> = new Map<string, Reformpaedagogik>();

	/**
	 * Reformpaedagogik KEIN_EINTRAG - Es ist kein Eintrag zur Reformpädagogik vorhanden
	 */
	public static readonly KEIN_EINTRAG : Reformpaedagogik = new Reformpaedagogik("KEIN_EINTRAG", 0, [new ReformpaedagogikKatalogEintrag(0, "*", "ohne Eintrag", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), null, null)]);

	/**
	 * Reformpaedagogik Celestin Freinet
	 */
	public static readonly FREINET : Reformpaedagogik = new Reformpaedagogik("FREINET", 1, [new ReformpaedagogikKatalogEintrag(1000, "C", "Celestin Freinet", Arrays.asList(Schulform.G, Schulform.GY), null, null)]);

	/**
	 * Reformpaedagogik Janusz Korczak (Pädagogik der Achtung)
	 */
	public static readonly KORCZAK : Reformpaedagogik = new Reformpaedagogik("KORCZAK", 2, [new ReformpaedagogikKatalogEintrag(2000, "J", "Janusz Korczak (Pädagogik der Achtung)", Arrays.asList(Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.S, Schulform.KS, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), null, null)]);

	/**
	 * Reformpaedagogik Montessori
	 */
	public static readonly MONTESSORI : Reformpaedagogik = new Reformpaedagogik("MONTESSORI", 3, [new ReformpaedagogikKatalogEintrag(3000, "M", "Montessori", Arrays.asList(Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.S, Schulform.KS, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), null, null)]);

	/**
	 * Reformpaedagogik Peter Petersen/Jena-Plan
	 */
	public static readonly PETERSEN : Reformpaedagogik = new Reformpaedagogik("PETERSEN", 4, [new ReformpaedagogikKatalogEintrag(4000, "P", "Peter Petersen/Jena-Plan", Arrays.asList(Schulform.G, Schulform.GY, Schulform.S, Schulform.KS, Schulform.SG, Schulform.SR), null, null)]);

	/**
	 * Sonstige Reformpaedagogik
	 */
	public static readonly SONSTIGE : Reformpaedagogik = new Reformpaedagogik("SONSTIGE", 5, [new ReformpaedagogikKatalogEintrag(5000, "S", "sonstige", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Reformpädagogik, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : ReformpaedagogikKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen zu der Reformpädagogik
	 */
	public readonly historie : Array<ReformpaedagogikKatalogEintrag>;

	/**
	 * Eine Map mit der Zuordnung der Reformpädagogik zu dem Kürzel der Reformpädagogik
	 */
	private static readonly _schulgliederungenKuerzel : HashMap<string, Reformpaedagogik> = new HashMap();

	/**
	 * Eine Map mit der Zuordnung der Reformpädagogik zu der ID der Reformpädagogik
	 */
	private static readonly _schulgliederungenID : HashMap<number, Reformpaedagogik> = new HashMap();

	/**
	 * Die Schulformen, bei welchen die Reformpädagogik vorkommt
	 */
	private schulformen : Array<ArrayList<Schulform>>;

	/**
	 * Erzeugt eine Reformpädagogik in der Aufzählung.
	 *
	 * @param historie   die Historie der Reformpädagogik, welches ein Array von {@link ReformpaedagogikKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<ReformpaedagogikKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Reformpaedagogik.all_values_by_ordinal.push(this);
		Reformpaedagogik.all_values_by_name.set(name, this);
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
	 * Gibt eine Map von den Kürzeln der Reformpädagogik auf die zugehörige Reformpädagogik
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Reformpädagogik auf die zugehörige Reformpädagogik
	 */
	private static getMapSchulgliederungByKuerzel() : HashMap<string, Reformpaedagogik> {
		if (Reformpaedagogik._schulgliederungenKuerzel.size() === 0)
			for (const r of Reformpaedagogik.values())
				Reformpaedagogik._schulgliederungenKuerzel.put(r.daten.kuerzel, r);
		return Reformpaedagogik._schulgliederungenKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Reformpädagogik auf die zugehörige Reformpädagogik
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Reformpädagogik auf die zugehörige Reformpädagogik
	 */
	private static getMapSchulgliederungByID() : HashMap<number, Reformpaedagogik> {
		if (Reformpaedagogik._schulgliederungenID.size() === 0)
			for (const r of Reformpaedagogik.values()) {
				for (const k of r.historie)
					Reformpaedagogik._schulgliederungenID.put(k.id, r);
			}
		return Reformpaedagogik._schulgliederungenID;
	}

	/**
	 * Liefert die Reformpädagogik anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Reformpädagogik
	 *
	 * @return die Reformpädagogik oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Reformpaedagogik | null {
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel)))
			return Reformpaedagogik.KEIN_EINTRAG;
		return Reformpaedagogik.getMapSchulgliederungByKuerzel().get(kuerzel);
	}

	/**
	 * Liefert die Reformpädagogik anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID der Reformpädagogik
	 *
	 * @return die Reformpädagogik oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : Reformpaedagogik | null {
		return Reformpaedagogik.getMapSchulgliederungByID().get(id);
	}

	/**
	 * Liefert alle Schulformen zurück, bei welchen die Reformpädagogik vorkommen kann.
	 *
	 * @return eine Liste der Schulformen
	 */
	public getSchulformen() : List<Schulform> {
		return this.schulformen[this.historie.length - 1];
	}

	/**
	 * Liefert alle möglichen Reformpädagogik-Einträge für die angegeben Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform zulässigen Reformpädagogik-Einträge
	 */
	public static get(schulform : Schulform | null) : List<Reformpaedagogik> {
		const result : ArrayList<Reformpaedagogik> = new ArrayList();
		if (schulform === null)
			return result;
		const gliederungen : Array<Reformpaedagogik> = Reformpaedagogik.values();
		for (let i : number = 0; i < gliederungen.length; i++) {
			const gliederung : Reformpaedagogik = gliederungen[i];
			if (gliederung.hasSchulform(schulform))
				result.add(gliederung);
		}
		return result;
	}

	/**
	 * Prüft anhand des Schulform-Kürzels, ob bei der Schulform diese Reformpädagogik
	 * vorkommen kann oder nicht.
	 *
	 * @param kuerzel   das Kürzel der Schulform
	 *
	 * @return true, falls die Reformpädagogik bei der Schulform vorkommen kann und ansonsten false
	 */
	public hasSchulformByKuerzel(kuerzel : string | null) : boolean {
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel)))
			return false;
		if (this.daten.schulformen !== null) {
			for (let i : number = 0; i < this.daten.schulformen.size(); i++) {
				const sfKuerzel : string | null = this.daten.schulformen.get(i);
				if (JavaObject.equalsTranspiler(sfKuerzel, (kuerzel)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob bei der Schulform diese Reformpädagogik vorkommen kann oder nicht.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return true, falls die Reformpädagogik bei der Schulform vorkommen kann und ansonsten false
	 */
	public hasSchulform(schulform : Schulform | null) : boolean {
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
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
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
		if (!(other instanceof Reformpaedagogik))
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
	public compareTo(other : Reformpaedagogik) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Reformpaedagogik> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Reformpaedagogik | null {
		const tmp : Reformpaedagogik | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.Reformpaedagogik', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_Reformpaedagogik(obj : unknown) : Reformpaedagogik {
	return obj as Reformpaedagogik;
}
