import { JavaObject } from '../../../java/lang/JavaObject';

export interface UvAlgorithmusDynDatenRegel extends JavaObject {


	/**
	 * Addiert oder subtrahiert den Malus der Regel, je nach Faktor.
	 *
	 * @param faktor   Der Faktor mit dem der Malus multipliziert wird (1 oder -1).
	 */
	changeMalus(faktor: number): void;

}


export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenRegel(obj: unknown): UvAlgorithmusDynDatenRegel {
	return obj as UvAlgorithmusDynDatenRegel;
}
