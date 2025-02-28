import { JavaEnum } from '../../../java/lang/JavaEnum';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { SchulformKatalogEintrag } from '../../../asd/data/schule/SchulformKatalogEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import type { JavaMap } from '../../../java/util/JavaMap';

export class Schulform extends JavaEnum<Schulform> implements CoreType<SchulformKatalogEintrag, Schulform> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Schulform> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Schulform> = new Map<string, Schulform>();

	/**
	 * Berufskolleg
	 */
	public static readonly BK : Schulform = new Schulform("BK", 0, );

	/**
	 * Freie Waldorfschule
	 */
	public static readonly FW : Schulform = new Schulform("FW", 1, );

	/**
	 * Grundschule
	 */
	public static readonly G : Schulform = new Schulform("G", 2, );

	/**
	 * Gesamtschule
	 */
	public static readonly GE : Schulform = new Schulform("GE", 3, );

	/**
	 * Gemeinschaftsschule
	 */
	public static readonly GM : Schulform = new Schulform("GM", 4, );

	/**
	 * Gymnasium
	 */
	public static readonly GY : Schulform = new Schulform("GY", 5, );

	/**
	 * Hauptschule
	 */
	public static readonly H : Schulform = new Schulform("H", 6, );

	/**
	 * Hibernia
	 */
	public static readonly HI : Schulform = new Schulform("HI", 7, );

	/**
	 * Schulversuch PRIMUS
	 */
	public static readonly PS : Schulform = new Schulform("PS", 8, );

	/**
	 * Realschule
	 */
	public static readonly R : Schulform = new Schulform("R", 9, );

	/**
	 * Förderschule im Bereich G/H
	 */
	public static readonly S : Schulform = new Schulform("S", 10, );

	/**
	 * Klinikschule
	 */
	public static readonly KS : Schulform = new Schulform("KS", 11, );

	/**
	 * Förderschule im Bereich Berufskolleg
	 */
	public static readonly SB : Schulform = new Schulform("SB", 12, );

	/**
	 * Förderschule im Bereich Gymnasium
	 */
	public static readonly SG : Schulform = new Schulform("SG", 13, );

	/**
	 * Sekundarschule
	 */
	public static readonly SK : Schulform = new Schulform("SK", 14, );

	/**
	 * Förderschule im Bereich Realschule
	 */
	public static readonly SR : Schulform = new Schulform("SR", 15, );

	/**
	 * nicht umorganisierte Volksschule
	 */
	public static readonly V : Schulform = new Schulform("V", 16, );

	/**
	 * Weiterbildungskolleg
	 */
	public static readonly WB : Schulform = new Schulform("WB", 17, );

	/**
	 * Freie Waldorfschule (Förderschule)
	 */
	public static readonly WF : Schulform = new Schulform("WF", 18, );

	private static readonly _mapSchuljahrToSchulformenMitGymOb : JavaMap<number, List<Schulform>> = new HashMap<number, List<Schulform>>();

	private static _listSchulformenMitGymOb : List<Schulform> | null = null;

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Schulform.all_values_by_ordinal.push(this);
		Schulform.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<SchulformKatalogEintrag, Schulform>) : void {
		CoreTypeDataManager.putManager(Schulform.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<SchulformKatalogEintrag, Schulform> {
		return CoreTypeDataManager.getManager(Schulform.class);
	}

	/**
	 * Gibt alle Schulformen dieser Aufzählung mit gymnasialer Oberstufe zurück, welche
	 * in dem angebenen Schuljahr gültig sind.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return eine {@link List} mit allen Schulformen, welche eine gymnasiale Oberstufe haben.
	 */
	public static getListMitGymOb(schuljahr : number) : List<Schulform> {
		let result : List<Schulform> | null = Schulform._mapSchuljahrToSchulformenMitGymOb.get(schuljahr);
		if (result === null) {
			result = new ArrayList(Schulform.data().getWerteBySchuljahr(schuljahr));
			for (let i : number = result.size() - 1; i >= 0; i--) {
				const sf : Schulform = result.get(i);
				const eintrag : SchulformKatalogEintrag | null = Schulform.data().getEintragBySchuljahrUndWert(schuljahr, sf);
				if ((eintrag === null) || (!eintrag.hatGymOb))
					result.remove(i);
			}
			Schulform._mapSchuljahrToSchulformenMitGymOb.put(schuljahr, result);
		}
		return result;
	}

	/**
	 * Gibt alle Schulformen dieser Aufzählung mit gymnasialer Oberstufe zurück, welche
	 * in irgendeinem Schuljahr gültig waren.
	 *
	 * @return eine {@link List} mit allen Schulformen, welche eine gymnasiale Oberstufe haben.
	 */
	public static getListAllMitGymOb() : List<Schulform> {
		let result : List<Schulform> | null = Schulform._listSchulformenMitGymOb;
		if (result === null) {
			result = new ArrayList(Schulform.data().getWerte());
			for (let i : number = result.size() - 1; i >= 0; i--) {
				const sf : Schulform = result.get(i);
				let hatGymOb : boolean = false;
				for (const sfke of sf.historie())
					if (sfke.hatGymOb)
						hatGymOb = true;
				if (!hatGymOb)
					result.remove(i);
			}
			Schulform._listSchulformenMitGymOb = result;
		}
		return result;
	}

	/**
	 * Gibt zurück, ob es sich um eine allgemeinbildende Schulform handelt.
	 *
	 * @return true/false
	 */
	public istAllgemeinbildend() : boolean {
		let _sevar_1292800746 : any;
		const _seexpr_1292800746 = (this);
		if (_seexpr_1292800746 === Schulform.FW) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.G) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.GE) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.GM) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.GY) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.H) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.HI) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.PS) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.R) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.S) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.KS) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.SG) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.SK) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.SR) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.V) {
			_sevar_1292800746 = true;
		} else if (_seexpr_1292800746 === Schulform.WF) {
			_sevar_1292800746 = true;
		} else {
			_sevar_1292800746 = false;
		}
		return _sevar_1292800746;
	}

	/**
	 * Gibt zurück, ob es sich um eine berufsbildende Schulform handelt.
	 *
	 * @return true/false
	 */
	public istBerufsbildend() : boolean {
		let _sevar_369848762 : any;
		const _seexpr_369848762 = (this);
		if (_seexpr_369848762 === Schulform.BK) {
			_sevar_369848762 = true;
		} else if (_seexpr_369848762 === Schulform.FW) {
			_sevar_369848762 = true;
		} else if (_seexpr_369848762 === Schulform.HI) {
			_sevar_369848762 = true;
		} else if (_seexpr_369848762 === Schulform.KS) {
			_sevar_369848762 = true;
		} else if (_seexpr_369848762 === Schulform.SB) {
			_sevar_369848762 = true;
		} else if (_seexpr_369848762 === Schulform.WF) {
			_sevar_369848762 = true;
		} else {
			_sevar_369848762 = false;
		}
		return _sevar_369848762;
	}

	/**
	 * Gibt zurück, ob es sich um eine Schulform für Weiterbildung handelt.
	 *
	 * @return true/false
	 */
	public istWeiterbildung() : boolean {
		let _sevar_1133631252 : any;
		const _seexpr_1133631252 = (this);
		if (_seexpr_1133631252 === Schulform.WB) {
			_sevar_1133631252 = true;
		} else {
			_sevar_1133631252 = false;
		}
		return _sevar_1133631252;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Schulform> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Schulform | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<SchulformKatalogEintrag, Schulform> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : SchulformKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<SchulformKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Schulform';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.Schulform', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Schulform>('de.svws_nrw.asd.types.schule.Schulform');

}

export function cast_de_svws_nrw_asd_types_schule_Schulform(obj : unknown) : Schulform {
	return obj as Schulform;
}
