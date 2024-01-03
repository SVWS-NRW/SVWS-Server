import { JavaEnum } from '../../../java/lang/JavaEnum';
import { KlassenartKatalogEintrag } from '../../../core/data/klassen/KlassenartKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { Schulgliederung, cast_de_svws_nrw_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { Pair } from '../../../core/adt/Pair';

export class Klassenart extends JavaEnum<Klassenart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Klassenart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Klassenart> = new Map<string, Klassenart>();

	/**
	 * Klassenart: Kein Eintrag
	 */
	public static readonly UNDEFINIERT : Klassenart = new Klassenart("UNDEFINIERT", 0, [new KlassenartKatalogEintrag(0, "**", "Kein Eintrag", Arrays.asList(new Pair(Schulform.WB, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Hauptschulklasse 1A
	 */
	public static readonly HA_1A : Klassenart = new Klassenart("HA_1A", 1, [new KlassenartKatalogEintrag(1000, "1A", "Klasse 10 Typ A (Hauptschule)", Arrays.asList(new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.R, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Hauptschulklasse 1B
	 */
	public static readonly HA_1B : Klassenart = new Klassenart("HA_1B", 2, [new KlassenartKatalogEintrag(2000, "1B", "Klasse 10 Typ B (Hauptschule)", Arrays.asList(new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Hauptschuleklasse ohne Differenzierung nach A und B
	 */
	public static readonly HA_AB : Klassenart = new Klassenart("HA_AB", 3, [new KlassenartKatalogEintrag(3000, "AB", "Klassen im Jahrgang 10 ohne Differenzierung in Typ A und Typ B (Hauptschule)", Arrays.asList(new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, Schulgliederung.H), new Pair(Schulform.SK, Schulgliederung.H), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Frühförderung: SKG (Ambulante Maßnahmen für blinde, gehörlose, sehbeh. und schwerh. Kinder)
	 */
	public static readonly AM : Klassenart = new Klassenart("AM", 4, [new KlassenartKatalogEintrag(4000, "AM", "Frühförderung: SKG (Ambulante Maßnahmen für blinde, gehörlose, sehbeh. und schwerh. Kinder)", Arrays.asList(new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Frühförderung: SKG (Präsenzgruppe)
	 */
	public static readonly PG : Klassenart = new Klassenart("PG", 5, [new KlassenartKatalogEintrag(5000, "PG", "Frühförderung: SKG (Präsenzgruppe)", Arrays.asList(new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Profilklasse (gemäß § 21 Abs. 3 APO-S I)
	 */
	public static readonly PK : Klassenart = new Klassenart("PK", 6, [new KlassenartKatalogEintrag(6000, "PK", "Profilklasse (gemäß § 21 Abs. 3 APO-S I)", Arrays.asList(new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, Schulgliederung.GY9), new Pair(Schulform.SK, Schulgliederung.GY)), null, null)]);

	/**
	 * Klassenart: Regelklasse
	 */
	public static readonly RK : Klassenart = new Klassenart("RK", 7, [new KlassenartKatalogEintrag(7000, "RK", "Regelklasse", Arrays.asList(new Pair(Schulform.FW, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.HI, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.WF, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Klassenart: Deutschförderklasse (gemäß BASS 13-63 Nr. 3, Nummer 3.5.1)
	 */
	public static readonly SG : Klassenart = new Klassenart("SG", 8, [new KlassenartKatalogEintrag(8000, "SG", "Deutschförderklasse (gemäß BASS 13-63 Nr. 3, Nummer 3.5.1)", Arrays.asList(new Pair(Schulform.G, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GE, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GM, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.GY, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.H, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.PS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.R, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.S, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.KS, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SG, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SK, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.SR, cast_de_svws_nrw_core_types_schule_Schulgliederung(null)), new Pair(Schulform.V, cast_de_svws_nrw_core_types_schule_Schulgliederung(null))), null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Prozess die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

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
	private readonly zulaessig : Array<ArrayList<Pair<Schulform | null, Schulgliederung | null>>>;

	/**
	 * Erzeugt eine zulässige Klassenart in der Aufzählung.
	 *
	 * @param historie   die Historie der Klassenart, welches ein Array von {@link KlassenartKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<KlassenartKatalogEintrag>) {
		super(name, ordinal);
		Klassenart.all_values_by_ordinal.push(this);
		Klassenart.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		this.zulaessig = Array(historie.length).fill(null);
		for (let i : number = 0; i < historie.length; i++) {
			this.zulaessig[i] = new ArrayList();
			for (const kuerzelSfSgl of historie[i].zulaessig) {
				const sf : Schulform | null = Schulform.getByKuerzel(kuerzelSfSgl.schulform);
				if (sf === null)
					continue;
				const sgl : Schulgliederung | null = kuerzelSfSgl.gliederung === null ? null : Schulgliederung.getByKuerzel(kuerzelSfSgl.gliederung);
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
			for (const s of Klassenart.values())
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
			for (const s of Klassenart.values())
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
	public hasSchulform(schulform : Schulform | null) : boolean {
		if ((schulform === null) || (schulform.daten === null))
			return false;
		for (const sfsgl of this.zulaessig[0]) {
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
		const kursarten : ArrayList<Klassenart> = new ArrayList();
		if (schulform === null)
			return kursarten;
		for (const kursart of Klassenart.values())
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
	 * @param kuerzel   das Kürzel
	 *
	 * @return die Klassenart oder null, wenn keine Zuordnung für das übergebene Kürzel vorhanden ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Klassenart | null {
		return Klassenart.getMapByKuerzel().get(kuerzel);
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.klassen.Klassenart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.klassen.Klassenart', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_klassen_Klassenart(obj : unknown) : Klassenart {
	return obj as Klassenart;
}
