import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { SchulformSchulgliederung, cast_de_nrw_schule_svws_core_data_schule_SchulformSchulgliederung } from '../../../core/data/schule/SchulformSchulgliederung';
import { KlassenartKatalogEintrag, cast_de_nrw_schule_svws_core_data_klassen_KlassenartKatalogEintrag } from '../../../core/data/klassen/KlassenartKatalogEintrag';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { Schulgliederung, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { Pair, cast_de_nrw_schule_svws_core_adt_Pair } from '../../../core/adt/Pair';

export class Klassenart extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Klassenart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Klassenart> = new Map<string, Klassenart>();

	/**
	 * Klassenart: Kein Eintrag
	 */
	public static readonly UNDEFINIERT : Klassenart = new Klassenart("UNDEFINIERT", 0, [new KlassenartKatalogEintrag(0, "**", "Kein Eintrag", Arrays.asList(new Pair(Schulform.WB, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Hauptschulklasse 1A
	 */
	public static readonly HA_1A : Klassenart = new Klassenart("HA_1A", 1, [new KlassenartKatalogEintrag(1000, "1A", "Klasse 10 Typ A (Hauptschule)", Arrays.asList(new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Hauptschulklasse 1B
	 */
	public static readonly HA_1B : Klassenart = new Klassenart("HA_1B", 2, [new KlassenartKatalogEintrag(2000, "1B", "Klasse 10 Typ B (Hauptschule)", Arrays.asList(new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Hauptschuleklasse ohne Differenzierung nach A und B
	 */
	public static readonly HA_AB : Klassenart = new Klassenart("HA_AB", 3, [new KlassenartKatalogEintrag(3000, "AB", "Klassen im Jahrgang 10 ohne Differenzierung in Typ A und Typ B (Hauptschule)", Arrays.asList(new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Frühförderung: SKG (Ambulante Maßnahmen für blinde, gehörlose, sehbeh. und schwerh. Kinder)
	 */
	public static readonly AM : Klassenart = new Klassenart("AM", 4, [new KlassenartKatalogEintrag(4000, "AM", "Frühförderung: SKG (Ambulante Maßnahmen für blinde, gehörlose, sehbeh. und schwerh. Kinder)", Arrays.asList(new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Frühförderung: SKG (Präsenzgruppe)
	 */
	public static readonly PG : Klassenart = new Klassenart("PG", 5, [new KlassenartKatalogEintrag(5000, "PG", "Frühförderung: SKG (Präsenzgruppe)", Arrays.asList(new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Profilklasse (gemäß § 21 Abs. 3 APO-S I)
	 */
	public static readonly PK : Klassenart = new Klassenart("PK", 6, [new KlassenartKatalogEintrag(6000, "PK", "Profilklasse (gemäß § 21 Abs. 3 APO-S I)", Arrays.asList(new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, Schulgliederung.GY9), new Pair(Schulform.SK, Schulgliederung.GY)), null, null)]);

	/**
	 * Klassenart: Regelklasse
	 */
	public static readonly RK : Klassenart = new Klassenart("RK", 7, [new KlassenartKatalogEintrag(7000, "RK", "Regelklasse", Arrays.asList(new Pair(Schulform.FW, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WF, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Deutschförderklasse (gemäß BASS 13-63 Nr. 3, Nummer 3.5.1)
	 */
	public static readonly SG : Klassenart = new Klassenart("SG", 8, [new KlassenartKatalogEintrag(8000, "SG", "Deutschförderklasse (gemäß BASS 13-63 Nr. 3, Nummer 3.5.1)", Arrays.asList(new Pair(Schulform.G, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Prozess die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Klassenart
	 */
	public readonly daten : KlassenartKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Klassenart
	 */
	public readonly historie : Array<KlassenartKatalogEintrag>;

	/**
	 * Eine HashMap mit allen zulässigen Klassenarten. Der Zugriff erfolgt dabei über die ID
	 */
	private static readonly _mapID : HashMap<number, Klassenart> = new HashMap();

	/**
	 * Eine HashMap mit zulässigen Klassenarten. Der Zugriff erfolgt dabei über das Kürzel
	 */
	private static readonly _mapKuerzel : HashMap<string, Klassenart> = new HashMap();

	/**
	 * Die Informationen zu den Kombinationen aus Schulformen und -gliederungen, wo die Klassenart zulässig ist
	 */
	private zulaessig : Array<Vector<Pair<Schulform | null, Schulgliederung | null>>>;

	/**
	 * Erzeugt eine zulässige Klassenart in der Aufzählung.
	 * 
	 * @param historie   die Historie der Klassenart, welches ein Array von {@link KlassenartKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<KlassenartKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Klassenart.all_values_by_ordinal.push(this);
		Klassenart.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.zulaessig = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++){
			this.zulaessig[i] = new Vector();
			for (let kuerzelSfSgl of historie[i].zulaessig) {
				let sf : Schulform | null = Schulform.getByKuerzel(kuerzelSfSgl.schulform);
				if (sf === null)
					continue;
				let sgl : Schulgliederung | null = kuerzelSfSgl.gliederung === null ? null : Schulgliederung.getByKuerzel(kuerzelSfSgl.gliederung);
				this.zulaessig[i].add(new Pair(sf, sgl));
			}
		}
	}

	/**
	 * Gibt eine Map von den IDs der Klassenarten auf die zugehörigen Klassenarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs auf die zugehörigen Klassenarten
	 */
	private static getMapByID() : HashMap<number, Klassenart> {
		if (Klassenart._mapID.size() === 0)
			for (let s of Klassenart.values()) 
				if (s.daten !== null)
					Klassenart._mapID.put(s.daten.id, s);
		return Klassenart._mapID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Klassenarten auf die zugehörigen Klassenarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen Klassenarten
	 */
	private static getMapByKuerzel() : HashMap<string, Klassenart> {
		if (Klassenart._mapKuerzel.size() === 0)
			for (let s of Klassenart.values()) 
				if (s.daten !== null)
					Klassenart._mapKuerzel.put(s.daten.kuerzel, s);
		return Klassenart._mapKuerzel;
	}

	/**
	 * Prüft, ob die Schulform bei dieser Klassenart in irgendeiner Gliederung der 
	 * angegebenen Schulform zulässig ist.
	 * 
	 * @param schulform    die Schulform
	 * 
	 * @return true, falls die Klassenart in der Schulform zulässig ist, ansonsten false.
	 */
	private hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null))
			return false;
		for (let sfsgl of this.zulaessig[0]) {
			if (sfsgl.a === schulform)
				return true;
		}
		return false;
	}

	/**
	 * Bestimmt alle Klassenarten, die in irgendeiner Gliederung der angegebenen Schulform
	 * zulässig sind.
	 *  
	 * @param schulform    die Schulform
	 * 
	 * @return die zulässigen Klassenarten in der angegebenen Schulform
	 */
	public static get(schulform : Schulform | null) : List<Klassenart> {
		let kursarten : Vector<Klassenart> = new Vector();
		if (schulform === null)
			return kursarten;
		for (let kursart of Klassenart.values()) 
			if (kursart.hasSchulform(schulform))
				kursarten.add(kursart);
		return kursarten;
	}

	/**
	 * Liefert alle Kombinationen aus Schulformen und Schulgliederungen zurück,
	 * bei denen die Klassenart zulässig ist.
	 * 
	 * @return eine Liste der Kombinationen aus Schulformen und Schulgliederungen
	 */
	public getGliederungen() : List<Pair<Schulform | null, Schulgliederung | null>> {
		return this.zulaessig[0];
	}

	/**
	 * Bestimmt anhand des Kürzels, die zulässige Klassenart. 
	 * 
	 * @param kursart   das Kürzel
	 * 
	 * @return die Klassenart oder null, wenn keine Zuordnung für das übergebene Kürzel vorhanden ist
	 */
	public static getByASDKursart(kursart : string | null) : Klassenart | null {
		return Klassenart.getMapByKuerzel().get(kursart);
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
		if (!(other instanceof Klassenart))
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
	public compareTo(other : Klassenart) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Klassenart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Klassenart | null {
		let tmp : Klassenart | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.klassen.Klassenart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_klassen_Klassenart(obj : unknown) : Klassenart {
	return obj as Klassenart;
}
