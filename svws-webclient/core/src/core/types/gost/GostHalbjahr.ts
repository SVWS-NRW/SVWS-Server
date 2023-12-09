import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class GostHalbjahr extends JavaEnum<GostHalbjahr> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostHalbjahr> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostHalbjahr> = new Map<string, GostHalbjahr>();

	/**
	 * Einführungsphase 1. Halbjahr = EF1
	 */
	public static readonly EF1 : GostHalbjahr = new GostHalbjahr("EF1", 0, 0, "EF", 1, "EF.1", "E1", "Einführungsphase 1. Halbjahr");

	/**
	 * Einführungsphase 2. Halbjahr = EF2
	 */
	public static readonly EF2 : GostHalbjahr = new GostHalbjahr("EF2", 1, 1, "EF", 2, "EF.2", "E2", "Einführungsphase 2. Halbjahr");

	/**
	 * Qualifikationsphase 1. Jahr, 1. Halbjahr = Q1.1
	 */
	public static readonly Q11 : GostHalbjahr = new GostHalbjahr("Q11", 2, 2, "Q1", 1, "Q1.1", "Q1", "Qualifikationsphase 1. Jahr, 1. Halbjahr");

	/**
	 * Qualifikationsphase 1. Jahr, 2. Halbjahr = Q1.2
	 */
	public static readonly Q12 : GostHalbjahr = new GostHalbjahr("Q12", 3, 3, "Q1", 2, "Q1.2", "Q2", "Qualifikationsphase 1. Jahr, 2. Halbjahr");

	/**
	 * Qualifikationsphase 2. Jahr, 1. Halbjahr = Q2.1
	 */
	public static readonly Q21 : GostHalbjahr = new GostHalbjahr("Q21", 4, 4, "Q2", 1, "Q2.1", "Q3", "Qualifikationsphase 2. Jahr, 1. Halbjahr");

	/**
	 * Qualifikationsphase 2. Jahr, 2. Halbjahr = Q2.2
	 */
	public static readonly Q22 : GostHalbjahr = new GostHalbjahr("Q22", 5, 5, "Q2", 2, "Q2.2", "Q4", "Qualifikationsphase 2. Jahr, 2. Halbjahr");

	/**
	 * Eine Zuordnung der Halbjahre zu der ID, welche die Reihenfolge der Halbjahre angibt.
	 */
	private static readonly _mapID : HashMap<number, GostHalbjahr> = new HashMap();

	/**
	 * Eine Zuordnung der Halbjahre zu dem Kürzel.
	 */
	private static readonly _mapKuerzel : HashMap<string, GostHalbjahr> = new HashMap();

	/**
	 * Eine Zuordnung der Halbjahre zu dem alten Kürzel.
	 */
	private static readonly _mapKuerzelAlt : HashMap<string, GostHalbjahr> = new HashMap();

	/**
	 * Die maximale Anzahl an Halbjahren in der gymnasialen Oberstufe
	 */
	public static readonly maxHalbjahre : number = 6;

	/**
	 * Eine ID für das Halbjahr, welches die Reihenfolge der Halbjahre wiederspiegelt und als Index für Arrays verwendet werden kann.
	 */
	public readonly id : number;

	/**
	 * Das Jahrgangskürzel des Halbjahres
	 */
	public readonly jahrgang : string;

	/**
	 * Die Nummer des Halbjahres
	 */
	public readonly halbjahr : number;

	/**
	 * Das eindeutige Kürzel für das Halbjahr der gymnasialen Oberstufe
	 */
	public readonly kuerzel : string;

	/**
	 * Ein eindeutiges Kürzel, welche in alten Tabellen (z.B. LuPO) verwendet wurde.
	 */
	public readonly kuerzelAlt : string;

	/**
	 * Eine textuelle Beschreibung für das Halbjahr der gymnasialen Oberstufe
	 */
	public readonly beschreibung : string;

	/**
	 * Erzeugt ein neues Halbjahr der Gymnasialen Oberstufe für diese Aufzählung.
	 *
	 * @param id             die ID für das Halbjahr, welches die Reihenfolge der Halbjahre wiederspiegelt
	 * @param jahrgang       das Jahrgangskürzel des Halbjahres
	 * @param halbjahr       die Nummer des Halbjahres
	 * @param kuerzel        das eindeutige Kürzel für das Halbjahr der gymnasialen Oberstufe
	 * @param kuerzelAlt     ein eindeutiges Kürzel, welche in alten Tabellen verwendet wurde.
	 * @param beschreibung   die textuelle Beschreibung für das Halbjahr der gymnasialen Oberstufe
	 */
	private constructor(name : string, ordinal : number, id : number, jahrgang : string, halbjahr : number, kuerzel : string, kuerzelAlt : string, beschreibung : string) {
		super(name, ordinal);
		GostHalbjahr.all_values_by_ordinal.push(this);
		GostHalbjahr.all_values_by_name.set(name, this);
		this.id = id;
		this.jahrgang = jahrgang;
		this.halbjahr = halbjahr;
		this.kuerzel = kuerzel;
		this.kuerzelAlt = kuerzelAlt;
		this.beschreibung = beschreibung;
	}

	/**
	 * Gibt eine Map von den IDs auf das Gost-Halbjahr zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs auf das Gost-Halbjahr
	 */
	private static getMapByID() : HashMap<number, GostHalbjahr> {
		if (GostHalbjahr._mapID.size() === 0)
			for (const h of GostHalbjahr.values())
				GostHalbjahr._mapID.put(h.id, h);
		return GostHalbjahr._mapID;
	}

	/**
	 * Gibt eine Map von den Kürzeln auf das Gost-Halbjahr zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf das Gost-Halbjahr
	 */
	private static getMapByKuerzel() : HashMap<string, GostHalbjahr> {
		if (GostHalbjahr._mapKuerzel.size() === 0)
			for (const h of GostHalbjahr.values())
				GostHalbjahr._mapKuerzel.put(h.kuerzel, h);
		return GostHalbjahr._mapKuerzel;
	}

	/**
	 * Gibt eine Map von den alten Kürzeln auf das Gost-Halbjahr zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den alten Kürzeln auf das Gost-Halbjahr
	 */
	private static getMapByKuerzelAlt() : HashMap<string, GostHalbjahr> {
		if (GostHalbjahr._mapKuerzelAlt.size() === 0)
			for (const h of GostHalbjahr.values())
				GostHalbjahr._mapKuerzelAlt.put(h.kuerzelAlt, h);
		return GostHalbjahr._mapKuerzelAlt;
	}

	/**
	 * Gibt das nachfolgende Halbjahr zurück.
	 *
	 * @return das nachfolgende Halbjahr oder null, wenn es keines mehr gibt
	 */
	public next() : GostHalbjahr | null {
		return GostHalbjahr.getMapByID().get(this.id + 1);
	}

	/**
	 * Gibt das nachfolgende Halbjahr zurück.
	 *
	 * @return das nachfolgende Halbjahr
	 *
	 * @throws NullPointerException wenn es kein nachfolgendes Halbjahr gibt
	 */
	public nextOrException() : GostHalbjahr {
		const hj : GostHalbjahr | null = GostHalbjahr.getMapByID().get(this.id + 1);
		if (hj === null)
			throw new NullPointerException()
		return hj;
	}

	/**
	 * Gibt das vorherige Halbjahr zurück.
	 *
	 * @return das vorherige Halbjahr oder null, wenn es keines mehr gibt
	 */
	public previous() : GostHalbjahr | null {
		return GostHalbjahr.getMapByID().get(this.id - 1);
	}

	/**
	 * Gibt das vorherige Halbjahr zurück.
	 *
	 * @return das vorherige Halbjahr
	 *
	 * @throws NullPointerException wenn es kein vorheriges Halbjahr gibt
	 */
	public previousOrException() : GostHalbjahr {
		const hj : GostHalbjahr | null = GostHalbjahr.getMapByID().get(this.id - 1);
		if (hj === null)
			throw new NullPointerException()
		return hj;
	}

	/**
	 * Gibt alle Halbjahre des Schuljahres zurück, in dem das Halbjahr-Objekt liegt.
	 *
	 * @return ein Array mit allen Halbjahren des Schuljahres, in dem das Halbjahr-Objekt liegt der gymnasialen Oberstufe
	 */
	public getSchuljahr() : Array<GostHalbjahr> {
		if (this.id % 2 === 0) {
			const hjs : Array<GostHalbjahr> = [this, this.nextOrException()];
			return hjs;
		}
		const hjs : Array<GostHalbjahr> = [this.previousOrException(), this];
		return hjs;
	}

	/**
	 * Gibt alle Halbjahre der Einführungsphase zurück.
	 *
	 * @return ein Array mit allen Halbjahren der Einführungsphase der gymnasialen Oberstufe
	 */
	public static getEinfuehrungsphase() : Array<GostHalbjahr> {
		const ef : Array<GostHalbjahr> = [GostHalbjahr.EF1, GostHalbjahr.EF2];
		return ef;
	}

	/**
	 * Gibt alle Halbjahre der Qualifikationsphase zurück.
	 *
	 * @return ein Array mit allen Halbjahren der Qualifikationsphase der gymnasialen Oberstufe
	 */
	public static getQualifikationsphase() : Array<GostHalbjahr> {
		const q : Array<GostHalbjahr> = [GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22];
		return q;
	}

	/**
	 * Gibt alle Halbjahre des übergebenen Jahrgangs zurück.
	 *
	 * @param jahrgang     der Jahrgang
	 *
	 * @return ein Array mit den Halbjahren des Jahrgangs
	 */
	public static getHalbjahreFromJahrgang(jahrgang : string) : Array<GostHalbjahr> {
		switch (jahrgang) {
			case "EF": {
				const ef : Array<GostHalbjahr> = [GostHalbjahr.EF1, GostHalbjahr.EF2]
				return ef;
			}
			case "Q1": {
				const q1 : Array<GostHalbjahr> = [GostHalbjahr.Q11, GostHalbjahr.Q12]
				return q1;
			}
			case "Q2": {
				const q2 : Array<GostHalbjahr> = [GostHalbjahr.Q21, GostHalbjahr.Q22]
				return q2;
			}
			default: {
				throw new IllegalArgumentException("Der angegebene Jahrgang ist kein gültiger Jahrgang der gymnasialen Oberstufe")
			}
		}
	}

	/**
	 * Gibt das Halbjahr zurück, welches die übergebene ID hat.
	 *
	 * @param id   die ID des Halbjahres
	 *
	 * @return das Halbjahr oder null, falls die ID nicht gültig ist
	 */
	public static fromID(id : number | null) : GostHalbjahr | null {
		if (id === null)
			return null;
		switch (id) {
			case 0: {
				return GostHalbjahr.EF1;
			}
			case 1: {
				return GostHalbjahr.EF2;
			}
			case 2: {
				return GostHalbjahr.Q11;
			}
			case 3: {
				return GostHalbjahr.Q12;
			}
			case 4: {
				return GostHalbjahr.Q21;
			}
			case 5: {
				return GostHalbjahr.Q22;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Gibt das Halbjahr zurück, welches die übergebene ID hat. <br>
	 * Wirft eine Exception, falls die ID keinem Halbjahr zugeordnet werden kann.
	 *
	 * @param pGostHalbjahID   Die ID des Halbjahres.
	 *
	 * @return Das Halbjahr oder eine Exception, falls die ID nicht gültig ist
	 * @throws NullPointerException Falls die ID keinem Halbjahr zugeordnet werden kann.
	 */
	public static fromIDorException(pGostHalbjahID : number) : GostHalbjahr {
		const halbjahr : GostHalbjahr | null = GostHalbjahr.fromID(pGostHalbjahID);
		if (halbjahr === null)
			throw new NullPointerException("GostHalbjahr nicht gefunden!")
		return halbjahr;
	}

	/**
	 * Gibt das Halbjahr zurück, welches das übergebene Kürzel hat.
	 *
	 * @param kuerzel   das Kürzel
	 *
	 * @return das Halbjahr oder null, falls das Kürzel nicht gültig ist
	 */
	public static fromKuerzel(kuerzel : string | null) : GostHalbjahr | null {
		return GostHalbjahr.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Gibt das Halbjahr zurück, welches das übergebene alte Kürzel hat.
	 *
	 * @param kuerzelAlt   das alte Kürzel
	 *
	 * @return das Halbjahr oder null, falls das Kürzel nicht gültig ist
	 */
	public static fromKuerzelAlt(kuerzelAlt : string | null) : GostHalbjahr | null {
		return GostHalbjahr.getMapByKuerzelAlt().get(kuerzelAlt);
	}

	/**
	 * Gibt das Halbjahr zurück, welches zu dem übergebenen Jahrgang und Halbjahr gehört.
	 *
	 * @param jahrgang     der Jahrgang
	 * @param halbjahr     die Nummer des Halbjahres
	 *
	 * @return das Halbjahr oder null, falls es kein gültiges Halbjahr mit den Angaben gibt.
	 */
	public static fromJahrgangUndHalbjahr(jahrgang : string | null, halbjahr : number) : GostHalbjahr | null {
		if ((halbjahr !== 1) && (halbjahr !== 2))
			return null;
		switch (jahrgang) {
			case "EF": {
				return (halbjahr === 1) ? GostHalbjahr.EF1 : GostHalbjahr.EF2;
			}
			case "Q1": {
				return (halbjahr === 1) ? GostHalbjahr.Q11 : GostHalbjahr.Q12;
			}
			case "Q2": {
				return (halbjahr === 1) ? GostHalbjahr.Q21 : GostHalbjahr.Q22;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Ermittelt das Halbjahr der gymnasialen Oberstufe anhand des angegebenen Abiturjahres und
	 * dem aktuellen Schuljahr und Halbjahr
	 *
	 * @param abiturjahr   das Abiturjahr
	 * @param schuljahr    das aktuelle Schuljahr
	 * @param halbjahr     das aktuelle Halbjahr
	 *
	 * @return das Halbjahr der gymnasialen Oberstufe oder null
	 */
	public static fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr : number, schuljahr : number, halbjahr : number) : GostHalbjahr | null {
		const id : number = ((schuljahr + 3 - abiturjahr) * 2) + halbjahr - 1;
		return GostHalbjahr.fromID(id);
	}

	/**
	 * Bestimmt das Halbjahr der gymnasialen Oberstufe anhand des angegebenen Abiturjahres und
	 * dem aktuellen Schuljahr und Halbjahr und gibt anhand dessen das Halbjahr der gymnasialen
	 * Oberstufe zurück, welches als nächstes geplant wird.
	 *
	 * @param abiturjahr   das Abiturjahr
	 * @param schuljahr    das aktuelle Schuljahr
	 * @param halbjahr     das aktuelle Halbjahr
	 *
	 * @return das nächste Halbjahr der gymnasialen Oberstufe zur Planung oder null, wenn der
	 *         Jahrgang in der Q2.2 ist oder das Abitur bereits abgeschlossen ist.
	 */
	public static getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(abiturjahr : number, schuljahr : number, halbjahr : number) : GostHalbjahr | null {
		let id : number = ((schuljahr + 3 - abiturjahr) * 2) + halbjahr;
		if (id < 0)
			id = 0;
		return GostHalbjahr.fromID(id);
	}

	/**
	 * Bestimmt das Abiturjahr für dieses Halbjahr der Gymnasialen Oberstufe anhand des
	 * übergebenen Schuljahres.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return das Abiturjahr
	 */
	public getAbiturjahrFromSchuljahr(schuljahr : number) : number {
		return schuljahr + 3 - (Math.trunc(this.id / 2));
	}

	/**
	 * Bestimmt das Schuljahr für dieses Halbjahr der Gymnasialen Oberstufe anhand des
	 * übergebenen Abiturjahres.
	 *
	 * @param abiturjahr   das Abiturjahr
	 *
	 * @return das Schuljahr
	 */
	public getSchuljahrFromAbiturjahr(abiturjahr : number) : number {
		return abiturjahr - 3 + (Math.trunc(this.id / 2));
	}

	/**
	 * Gibt zurück, ob es Einführungsphase ist.
	 *
	 * @return  Einführungsphase, true oder false
	 */
	public istEinfuehrungsphase() : boolean {
		return JavaObject.equalsTranspiler("EF", (this.jahrgang));
	}

	/**
	 * Gibt zurück, ob es Qualifikationsphase ist.
	 *
	 * @return  Qualifikationsphase, true oder false
	 */
	public istQualifikationsphase() : boolean {
		return !this.istEinfuehrungsphase();
	}

	/**
	 * Prüft anhand der übergebenen Halbjahre, ob es sich um die beiden Halbjahre
	 * der Einführungsphase handelt.
	 *
	 * @param halbjahre    die Halbjahre
	 *
	 * @return true, wenn es sich um die beiden Halbjahre der Einführungsphase handelt
	 *         und ansonsten false
	 */
	public static pruefeEinfuehrungsphase(...halbjahre : Array<GostHalbjahr>) : boolean {
		if ((halbjahre === null) || (halbjahre.length !== 2))
			return false;
		return ((halbjahre[0] as unknown === GostHalbjahr.EF1 as unknown) && (halbjahre[0] as unknown === GostHalbjahr.EF2 as unknown)) || ((halbjahre[0] as unknown === GostHalbjahr.EF2 as unknown) && (halbjahre[0] as unknown === GostHalbjahr.EF1 as unknown));
	}

	/**
	 * Prüft anhand der übergebenen Halbjahre, ob es sich um die vier Halbjahre
	 * der Qualifikationsphase handelt.
	 *
	 * @param halbjahre    die Halbjahre
	 *
	 * @return true, wenn es sich um die vier Halbjahre der Qualifikationsphase
	 *         handelt und ansonsten false
	 */
	public static pruefeQualifikationsphase(...halbjahre : Array<GostHalbjahr>) : boolean {
		if ((halbjahre === null) || (halbjahre.length !== 4))
			return false;
		const list : List<GostHalbjahr> = Arrays.asList(...halbjahre);
		return (list.contains(GostHalbjahr.Q11) && list.contains(GostHalbjahr.Q12) && list.contains(GostHalbjahr.Q21) && list.contains(GostHalbjahr.Q22));
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostHalbjahr> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostHalbjahr | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostHalbjahr', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_GostHalbjahr(obj : unknown) : GostHalbjahr {
	return obj as GostHalbjahr;
}
