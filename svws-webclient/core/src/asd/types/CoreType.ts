import { JavaObject } from '../../java/lang/JavaObject';
import { CoreTypeDataManager } from '../../asd/utils/CoreTypeDataManager';
import { Class, cast_java_lang_Class } from '../../java/lang/Class';
import type { List } from '../../java/util/List';
import { CoreTypeData } from '../../asd/data/CoreTypeData';

export interface CoreType<T extends CoreTypeData, U extends CoreType<T, U>> extends JavaObject {


	/**
	 * Gibt den Bezeichner des CoreType-Wertes zurück.
	 *
	 * @return der Bezeichner
	 */
	name() : string;

	/**
	 * Gibt einen Ordinal-Wert für den CoreType-Wert ähnlich wie bei Enum-Konstanten zurück.
	 *
	 * @return der Ordinal-Wert
	 */
	ordinal() : number;

	/**
	 * Gibt den Bezeichner des CoreType-Wertes zurück.
	 *
	 * @return der Bezeichner
	 */
	compareTo(other : U) : number;

}

/**
 * Gibt den Manager des Core-Types zurück.
 *
 * @return der Manager
 */
export function de_svws_nrw_asd_types_CoreType_getManager<T extends CoreTypeData, U extends CoreType<T, U>>(transpiledThis : any) : CoreTypeDataManager<T, U> {
	return CoreTypeDataManager.getManager(cast_java_lang_Class(transpiledThis.getClass()));
}

/**
 * Gibt die Daten aus der Historie zu diesem Core-Type zurück.
 *
 * @param schuljahr   das zu prüfende Schuljahr
 *
 * @return die Daten aus der Historie
 */
export function de_svws_nrw_asd_types_CoreType_daten<T extends CoreTypeData, U extends CoreType<T, U>>(transpiledThis : any, schuljahr : number) : T | null {
	return transpiledThis.getManager().getEintragBySchuljahrUndWert(schuljahr, transpiledThis as unknown as U);
}

/**
 * Gibt die Statistik-ID zu diesem Core-Type zurück.
 *
 * @return die Statistik ID
 */
export function de_svws_nrw_asd_types_CoreType_statistikId<T extends CoreTypeData, U extends CoreType<T, U>>(transpiledThis : any) : string | null {
	return transpiledThis.getManager().getStatistikIdByWert(transpiledThis as unknown as U);
}

/**
 * Gibt die Historie zu diesem Core-Type zurück.
 *
 * @return die Historie
 */
export function de_svws_nrw_asd_types_CoreType_historie<T extends CoreTypeData, U extends CoreType<T, U>>(transpiledThis : any) : List<T> {
	return transpiledThis.getManager().getHistorieByWert(transpiledThis as unknown as U);
}


export function cast_de_svws_nrw_asd_types_CoreType<T extends CoreTypeData, U extends CoreType<T, U>>(obj : unknown) : CoreType<T, U> {
	return obj as CoreType<T, U>;
}
