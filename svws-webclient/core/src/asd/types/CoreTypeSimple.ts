import { HashMap } from '../../java/util/HashMap';
import { CoreTypeDataManager } from '../../asd/utils/CoreTypeDataManager';
import { Exception } from '../../java/lang/Exception';
import { JavaInteger } from '../../java/lang/JavaInteger';
import { NullPointerException } from '../../java/lang/NullPointerException';
import { Class } from '../../java/lang/Class';
import { JavaObject } from '../../java/lang/JavaObject';
import type { List } from '../../java/util/List';
import type { CoreType } from '../../asd/types/CoreType';
import { cast_de_svws_nrw_asd_types_CoreType, de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../asd/types/CoreType';
import { CoreTypeData } from '../../asd/data/CoreTypeData';
import type { JavaMap } from '../../java/util/JavaMap';
import { CoreTypeException } from '../../asd/data/CoreTypeException';

export abstract class CoreTypeSimple<T extends CoreTypeData, U extends CoreTypeSimple<T, U>> extends JavaObject implements CoreType<T, U> {

	/**
	 * Verwaltung der Bezeichner, um die Liste der erstellten CoreType Elemente zurück geben zu können
	 */
	private static _values : HashMap<Class<any>, Array<unknown>> = new HashMap<Class<any>, Array<unknown>>();

	/**
	 * der Bezeichner des CoreTypeSimple
	 */
	private _name : string = "";

	/**
	 * die lfd. Nummer des CoreTypeSimple
	 */
	private _ordinal : number = 0;


	/**
	 * Erstellt einen CoreTypeSimple mit Standardwerten
	 */
	public constructor() {
		super();
	}

	/**
	 * Initialisiert den Simple-Core-Type vom Typ U und erstellt die einzelnen Instanzen für die Bezeichner und deren Ordinal-Werten
	 *
	 * @param <T>    der Typ der Historien-Einträge des Core-Types
	 * @param <U>    der Core-Type
	 * @param obj    Ein Objekt der Klasse clazz
	 * @param clazz  das Klassen-Objekt für den Core-Type
	 * @param data   die Date mit denen der Core-Type initialisiert wird
	 */
	public static initValues<T extends CoreTypeData, U extends CoreTypeSimple<T, U>>(obj : U, clazz : Class<U>, data : JavaMap<string, List<T>>) : void {
		try {
			const values : Array<U> = Array(data.size()).fill(null) as unknown as Array<U>;
			CoreTypeSimple._values.put(clazz, values);
			let i : number = 0;
			for (const bezeichner of data.keySet()) {
				const u : U | null = obj.getInstance();
				if (u !== null) {
					values[i] = u;
					const coreTypeValue : CoreTypeSimple<T, U> | null = values[i];
					coreTypeValue._name = bezeichner;
					coreTypeValue._ordinal = i++;
				}
			}
		} catch(e : any) {
			throw new CoreTypeException(e)
		}
	}

	/**
	 * Gibt die einzelnen Werte dieser Aufzählung als Array zurück.
	 *
	 * @param <S>     der Core-Type
	 * @param clazz   das Klassen-Objekt zum Core-Type
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static valuesByClass<S extends CoreTypeSimple<any, any>>(clazz : Class<S>) : Array<S> {
		const list = CoreTypeSimple._values.get(clazz);
		if (list === null)
			return Array(0).fill(null) as unknown as Array<S>;
		return list as unknown as Array<S>;
	}

	/**
	 * Gibt den unveränderlichen Bezeichner des Core-Types zurück.
	 *
	 * @return der name (Bezeichner)
	 */
	public name() : string {
		return this._name;
	}

	/**
	 * Gibt einen Ordinal-Wert für den CoreType-Wert ähnlich wie bei Enum-Konstanten zurück.
	 *
	 * @return der Ordinal-Wert
	 */
	public ordinal() : number {
		return this._ordinal;
	}

	/**
	 * Gibt den Bezeichner des CoreType-Wertes zurück.
	 *
	 * @return der Bezeichner
	 */
	public toString() : string {
		return this._name;
	}

	/**
	 * Vergleicht diesen CoreType-Wert mit dem anderen Coretype-Wert
	 *
	 * @param other   der andere Coretype-Wert
	 *
	 * @return kleiner 0, wenn dieser Wert kleiner ist, 0, wenn sie gleich sind
	 *     und größer 0, wenn dieser Wert größer ist
	 */
	public compareTo(other : U) : number {
		if (other === null)
			throw new NullPointerException()
		return JavaInteger.compare(this.ordinal(), other.ordinal());
	}

	/**
	 * Der Hash-Code des CoreType-Wertes.
	 *
	 * @return der Hash-Code
	 */
	public hashCode() : number {
		return JavaObject.getTranspilerHashCode(this._name);
	}

	/**
	 * Prüft, ob die CoreType-Werte gleich sind.
	 */
	public equals(obj : unknown | null) : boolean {
		return (this as unknown === obj as unknown);
	}

	/**
	 * Erzeugt ein Objekt der Klasse
	 * @return Ein Objekt der Klasse U
	 */
	public getInstance() : U | null {
		return null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.CoreTypeSimple';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.CoreTypeSimple'].includes(name);
	}

	public static class = new Class<CoreTypeSimple<any, any>>('de.svws_nrw.asd.types.CoreTypeSimple');

	public getManager() : CoreTypeDataManager<T, U> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : T | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<T> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

}

export function cast_de_svws_nrw_asd_types_CoreTypeSimple<T extends CoreTypeData, U extends CoreTypeSimple<T, U>>(obj : unknown) : CoreTypeSimple<T, U> {
	return obj as CoreTypeSimple<T, U>;
}
