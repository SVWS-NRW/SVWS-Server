import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { FoerderschwerpunktKatalogEintrag } from '../../../core/data/schule/FoerderschwerpunktKatalogEintrag';
import { JavaString } from '../../../java/lang/JavaString';
import { Arrays } from '../../../java/util/Arrays';

export class Foerderschwerpunkt extends JavaObject implements JavaEnum<Foerderschwerpunkt> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Foerderschwerpunkt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Foerderschwerpunkt> = new Map<string, Foerderschwerpunkt>();

	/**
	 * Förderschwerpunkt - kein Förderschwerpunkt
	 */
	public static readonly KEINER : Foerderschwerpunkt = new Foerderschwerpunkt("KEINER", 0, [new FoerderschwerpunktKatalogEintrag(0, "**", "kein Förderschwerpunkt", Arrays.asList(Schulform.BK, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.V, Schulform.WB), null, null)]);

	/**
	 * Förderschwerpunkt - Sehen (Blinde)
	 */
	public static readonly BL : Foerderschwerpunkt = new Foerderschwerpunkt("BL", 1, [new FoerderschwerpunktKatalogEintrag(1000, "BL", "Sehen (Blinde)", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Emotionale und soziale Entwicklung
	 */
	public static readonly EZ : Foerderschwerpunkt = new Foerderschwerpunkt("EZ", 2, [new FoerderschwerpunktKatalogEintrag(2000, "EZ", "Emotionale und soziale Entwicklung", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Geistige Entwicklung
	 */
	public static readonly GB : Foerderschwerpunkt = new Foerderschwerpunkt("GB", 3, [new FoerderschwerpunktKatalogEintrag(3000, "GB", "Geistige Entwicklung", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Hören und Kommunikation (Gehörlose)
	 */
	public static readonly GH : Foerderschwerpunkt = new Foerderschwerpunkt("GH", 4, [new FoerderschwerpunktKatalogEintrag(4000, "GH", "Hören und Kommunikation (Gehörlose)", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.SR, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Körperliche und motorische Entwicklung
	 */
	public static readonly KB : Foerderschwerpunkt = new Foerderschwerpunkt("KB", 5, [new FoerderschwerpunktKatalogEintrag(5000, "KB", "Körperliche und motorische Entwicklung", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.SG, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Schule für Kranke
	 */
	public static readonly KR : Foerderschwerpunkt = new Foerderschwerpunkt("KR", 6, [new FoerderschwerpunktKatalogEintrag(6000, "KR", "Schule für Kranke", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.S, Schulform.KS), 2011, null)]);

	/**
	 * Förderschwerpunkt - Lernen
	 */
	public static readonly LB : Foerderschwerpunkt = new Foerderschwerpunkt("LB", 7, [new FoerderschwerpunktKatalogEintrag(7000, "LB", "Lernen", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Präventive Förderung im Bereich Emotionale und soziale Entwicklung
	 */
	public static readonly PE : Foerderschwerpunkt = new Foerderschwerpunkt("PE", 8, [new FoerderschwerpunktKatalogEintrag(8000, "PE", "Präventive Förderung im Bereich Emotionale und soziale Entwicklung", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), null, 2010)]);

	/**
	 * Förderschwerpunkt - Präventive Förderung
	 */
	public static readonly PF : Foerderschwerpunkt = new Foerderschwerpunkt("PF", 9, [new FoerderschwerpunktKatalogEintrag(9000, "PF", "Präventive Förderung", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), null, 2010)]);

	/**
	 * Förderschwerpunkt - Präventive Förderung im Bereich Lernen
	 */
	public static readonly PL : Foerderschwerpunkt = new Foerderschwerpunkt("PL", 10, [new FoerderschwerpunktKatalogEintrag(10000, "PL", "Präventive Förderung im Bereich Lernen", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), null, 2010)]);

	/**
	 * Förderschwerpunkt - Präventive Förderung im Bereich Sprache
	 */
	public static readonly PS : Foerderschwerpunkt = new Foerderschwerpunkt("PS", 11, [new FoerderschwerpunktKatalogEintrag(11000, "PS", "Präventive Förderung im Bereich Sprache", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), null, 2010)]);

	/**
	 * Förderschwerpunkt - Sprache
	 */
	public static readonly SB : Foerderschwerpunkt = new Foerderschwerpunkt("SB", 12, [new FoerderschwerpunktKatalogEintrag(12000, "SB", "Sprache", Arrays.asList(Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Hören und Kommunikation (Schwerhörige)
	 */
	public static readonly SG : Foerderschwerpunkt = new Foerderschwerpunkt("SG", 13, [new FoerderschwerpunktKatalogEintrag(13000, "SG", "Hören und Kommunikation (Schwerhörige)", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.SR, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Sehen (Sehbehinderte)
	 */
	public static readonly SH : Foerderschwerpunkt = new Foerderschwerpunkt("SH", 14, [new FoerderschwerpunktKatalogEintrag(14000, "SH", "Sehen (Sehbehinderte)", Arrays.asList(Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.V), 2011, null)]);

	/**
	 * Förderschwerpunkt - Kein Förderschwerpunkt
	 */
	public static readonly XX : Foerderschwerpunkt = new Foerderschwerpunkt("XX", 15, [new FoerderschwerpunktKatalogEintrag(15000, "XX", "Kein Förderschwerpunkt", Arrays.asList(Schulform.SB, Schulform.SG), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Förderschwerpunktes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : FoerderschwerpunktKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen des Förderschwerpunktes
	 */
	public readonly historie : Array<FoerderschwerpunktKatalogEintrag>;

	/**
	 * Eine Map mit der Zuordnung des Förderschwerpunktes zu dem Kürzel des Förderschwerpunktes
	 */
	private static readonly _foerderschwerpunkteKuerzel : HashMap<string, Foerderschwerpunkt> = new HashMap();

	/**
	 * Eine Map mit der Zuordnung des Förderschwerpunktes zu der ID des Förderschwerpunktes
	 */
	private static readonly _foerderschwerpunkteID : HashMap<number, Foerderschwerpunkt> = new HashMap();

	/**
	 * Die Schulformen, bei welchen der Förderschwerpunkt vorkommt
	 */
	private schulformen : Array<ArrayList<Schulform>>;

	/**
	 * Erzeugt einen neuen Förderschwerpunkt in der Aufzählung.
	 *
	 * @param historie   die Historie des Förderschwerpunktes, welches ein Array von {@link FoerderschwerpunktKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<FoerderschwerpunktKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Foerderschwerpunkt.all_values_by_ordinal.push(this);
		Foerderschwerpunkt.all_values_by_name.set(name, this);
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
	 * Gibt eine Map von den Kürzeln der Förderschwerpunkte auf die zugehörigen Förderschwerpunkte
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Förderschwerpunkte auf die zugehörigen Förderschwerpunkte
	 */
	private static getMapFoerderschwerpunktByKuerzel() : HashMap<string, Foerderschwerpunkt> {
		if (Foerderschwerpunkt._foerderschwerpunkteKuerzel.size() === 0)
			for (const s of Foerderschwerpunkt.values())
				Foerderschwerpunkt._foerderschwerpunkteKuerzel.put(s.daten.kuerzel, s);
		return Foerderschwerpunkt._foerderschwerpunkteKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Förderschwerpunkte auf die zugehörigen Förderschwerpunkte
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Förderschwerpunkte auf die zugehörigen Förderschwerpunkte
	 */
	private static getMapFoerderschwerpunktByID() : HashMap<number, Foerderschwerpunkt> {
		if (Foerderschwerpunkt._foerderschwerpunkteID.size() === 0)
			for (const s of Foerderschwerpunkt.values()) {
				for (const k of s.historie)
					Foerderschwerpunkt._foerderschwerpunkteID.put(k.id, s);
			}
		return Foerderschwerpunkt._foerderschwerpunkteID;
	}

	/**
	 * Liefert den Förderschwerpunkt anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel des Förderschwerpunktes
	 *
	 * @return der Förderschwerpunkt oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Foerderschwerpunkt | null {
		return Foerderschwerpunkt.getMapFoerderschwerpunktByKuerzel().get(kuerzel);
	}

	/**
	 * Liefert den Förderschwerpunkt anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID des Förderschwerpunktes
	 *
	 * @return der Förderschwerpunkt oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number | null) : Foerderschwerpunkt | null {
		return Foerderschwerpunkt.getMapFoerderschwerpunktByID().get(id);
	}

	/**
	 * Liefert alle Schulformen zurück, bei welchen der Förderschwerpunkt vorkommt.
	 *
	 * @return eine Liste der Schulformen
	 */
	public getSchulformen() : List<Schulform> {
		return this.schulformen[this.historie.length - 1];
	}

	/**
	 * Liefert alle zulässigen Förderschwerpunkte für die angegebene Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform zulässigen Förderschwerpunkte
	 */
	public static get(schulform : Schulform | null) : List<Foerderschwerpunkt> {
		const result : ArrayList<Foerderschwerpunkt> = new ArrayList();
		if (schulform === null)
			return result;
		const fs : Array<Foerderschwerpunkt> = Foerderschwerpunkt.values();
		for (let i : number = 0; i < fs.length; i++) {
			const gliederung : Foerderschwerpunkt = fs[i];
			if (gliederung.hasSchulform(schulform))
				result.add(gliederung);
		}
		return result;
	}

	/**
	 * Prüft anhand des Förderschwerpunkts-Kürzels, ob die Schulform diesen Förderschwerpunkt
	 * hat oder nicht.
	 *
	 * @param kuerzel   das Kürzel der Schulform
	 *
	 * @return true, falls der Förderschwerpunkt bei der Schulform existiert und ansonsten false
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
	 * Prüft, ob die Schulform diesen Förderschwerpunkt hat oder nicht.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return true, falls der Förderschwerpunkt bei der Schulform existiert und ansonsten false
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
	 * Gibt die Förderschwerpunkte der angegebenen Schulform mit dem übergebenen Kürzel zurück.
	 *
	 * @param sf        die Schulform
	 * @param kuerzel   das Kürzel des Förderschwerpunktes
	 *
	 * @return der Förderschwerpunkt, falls die Parameter gültige Werte sind und ansonsten null
	 */
	public static getBySchulformAndKuerzel(sf : Schulform | null, kuerzel : string | null) : Foerderschwerpunkt | null {
		if (sf === null)
			return null;
		if ((kuerzel === null) || JavaObject.equalsTranspiler("", (kuerzel)))
			return Foerderschwerpunkt.KEINER;
		const schwerpunkte : List<Foerderschwerpunkt> = Foerderschwerpunkt.get(sf);
		for (let i : number = 0; i < schwerpunkte.size(); i++) {
			const fs : Foerderschwerpunkt | null = schwerpunkte.get(i);
			if (JavaString.equalsIgnoreCase((fs.daten.kuerzel), kuerzel))
				return fs;
		}
		return null;
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
		if (!(other instanceof Foerderschwerpunkt))
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
	public compareTo(other : Foerderschwerpunkt) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Foerderschwerpunkt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Foerderschwerpunkt | null {
		const tmp : Foerderschwerpunkt | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schueler.Foerderschwerpunkt', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schueler_Foerderschwerpunkt(obj : unknown) : Foerderschwerpunkt {
	return obj as Foerderschwerpunkt;
}
