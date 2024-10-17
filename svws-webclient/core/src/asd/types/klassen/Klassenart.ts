import { JavaEnum } from '../../../java/lang/JavaEnum';
import type { JavaSet } from '../../../java/util/JavaSet';
import { KlassenartKatalogEintrag } from '../../../asd/data/klassen/KlassenartKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashSet } from '../../../java/util/HashSet';
import { CoreTypeException } from '../../../asd/data/CoreTypeException';

export class Klassenart extends JavaEnum<Klassenart> implements CoreType<KlassenartKatalogEintrag, Klassenart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Klassenart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Klassenart> = new Map<string, Klassenart>();

	/**
	 * Klassenart: Kein Eintrag
	 */
	public static readonly UNDEFINIERT : Klassenart = new Klassenart("UNDEFINIERT", 0, );

	/**
	 * Klassenart: Hauptschulklasse 1A
	 */
	public static readonly HA_1A : Klassenart = new Klassenart("HA_1A", 1, );

	/**
	 * Klassenart: Hauptschulklasse 1B
	 */
	public static readonly HA_1B : Klassenart = new Klassenart("HA_1B", 2, );

	/**
	 * Klassenart: Hauptschuleklasse ohne Differenzierung nach A und B
	 */
	public static readonly HA_AB : Klassenart = new Klassenart("HA_AB", 3, );

	/**
	 * Klassenart: Frühförderung: SKG (Ambulante Maßnahmen für blinde, gehörlose, sehbeh. und schwerh. Kinder)
	 */
	public static readonly AM : Klassenart = new Klassenart("AM", 4, );

	/**
	 * Klassenart: Deutschförderung (ohne Bildungsgangzuordnung, BASS 13-63 Nr. 3)
	 */
	public static readonly DF : Klassenart = new Klassenart("DF", 5, );

	/**
	 * Klassenart: Frühförderung: SKG (Präsenzgruppe)
	 */
	public static readonly PG : Klassenart = new Klassenart("PG", 6, );

	/**
	 * Klassenart: Profilklasse (gemäß § 21 Abs. 3 APO-S I)
	 */
	public static readonly PK : Klassenart = new Klassenart("PK", 7, );

	/**
	 * Klassenart: Regelklasse
	 */
	public static readonly RK : Klassenart = new Klassenart("RK", 8, );

	/**
	 * Klassenart: Deutschförderklasse (gemäß BASS 13-63 Nr. 3, Nummer 3.5.1)
	 */
	public static readonly SG : Klassenart = new Klassenart("SG", 9, );

	/**
	 * Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer.
	 */
	private static readonly _mapSchulformenByID : HashMap<number, JavaSet<Schulform>> = new HashMap<number, JavaSet<Schulform>>();

	private static readonly _mapBySchuljahrAndSchulform : JavaMap<number, JavaMap<Schulform, List<Klassenart>>> = new HashMap<number, JavaMap<Schulform, List<Klassenart>>>();

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Klassenart.all_values_by_ordinal.push(this);
		Klassenart.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KlassenartKatalogEintrag, Klassenart>) : void {
		CoreTypeDataManager.putManager(Klassenart.class, manager);
		for (const ct of Klassenart.data().getWerte())
			for (const e of ct.historie()) {
				const tmpSet : JavaSet<Schulform> | null = new HashSet<Schulform>();
				for (const s of e.zulaessig)
					tmpSet.add(Schulform.data().getWertByBezeichner(s.schulform));
				Klassenart._mapSchulformenByID.put(e.id, tmpSet);
			}
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KlassenartKatalogEintrag, Klassenart> {
		return CoreTypeDataManager.getManager(Klassenart.class);
	}

	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public hatSchulform(schuljahr : number, sf : Schulform) : boolean {
		const ke : KlassenartKatalogEintrag | null = this.daten(schuljahr);
		if (ke !== null) {
			const result : JavaSet<Schulform> | null = Klassenart._mapSchulformenByID.get(ke.id);
			if (result === null)
				throw new CoreTypeException(JavaString.format("Fehler beim Prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.", this.getClass().getSimpleName()))
			return result.contains(sf);
		}
		return false;
	}

	/**
	 * Liefert alle zulässigen Klassenarten für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Klassenarten
	 */
	public static getBySchuljahrAndSchulform(schuljahr : number, schulform : Schulform) : List<Klassenart> {
		const mapBySchulform : JavaMap<Schulform, List<Klassenart>> | null = Klassenart._mapBySchuljahrAndSchulform.computeIfAbsent(schuljahr, { apply : (k: number | null) => new HashMap<Schulform, List<Klassenart>>() });
		if (mapBySchulform === null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern")
		let result : List<Klassenart> | null = mapBySchulform.get(schulform);
		if (result === null) {
			result = new ArrayList();
			const klassenarten : List<Klassenart> | null = Klassenart.data().getWerteBySchuljahr(schuljahr);
			for (const klassenart of klassenarten)
				if (klassenart.hatSchulform(schuljahr, schulform))
					result.add(klassenart);
			mapBySchulform.put(schulform, result);
		}
		return result;
	}

	/**
	 * Gibt eine Klassenart zurück, welche für die übergebene Schulform als Voreinstellung
	 * für neue Klassen genutzt wird,
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die Default-Klassenart
	 */
	public static getDefault(schulform : Schulform) : Klassenart {
		let _sevar_2034606939 : any;
		const _seexpr_2034606939 = (schulform);
		if (_seexpr_2034606939 === Schulform.FW) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.HI) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.WF) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.G) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.GE) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.GM) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.GY) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.H) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.PS) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.R) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.S) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.KS) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.SG) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.SK) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.SR) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.V) {
			_sevar_2034606939 = Klassenart.RK;
		} else if (_seexpr_2034606939 === Schulform.WB) {
			_sevar_2034606939 = Klassenart.UNDEFINIERT;
		} else if (_seexpr_2034606939 === Schulform.BK) {
			_sevar_2034606939 = Klassenart.UNDEFINIERT;
		} else if (_seexpr_2034606939 === Schulform.SB) {
			_sevar_2034606939 = Klassenart.UNDEFINIERT;
		} else {
			_sevar_2034606939 = Klassenart.RK;
		}
		return _sevar_2034606939;
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

	public getManager() : CoreTypeDataManager<KlassenartKatalogEintrag, Klassenart> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KlassenartKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<KlassenartKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.klassen.Klassenart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.klassen.Klassenart', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Klassenart>('de.svws_nrw.asd.types.klassen.Klassenart');

}

export function cast_de_svws_nrw_asd_types_klassen_Klassenart(obj : unknown) : Klassenart {
	return obj as Klassenart;
}
