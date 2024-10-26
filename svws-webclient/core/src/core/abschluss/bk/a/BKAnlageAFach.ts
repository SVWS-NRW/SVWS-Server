import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class BKAnlageAFach extends JavaObject {

	/**
	 * Das Kürzel des Faches.
	 */
	public kuerzel : string | null = null;

	/**
	 * Die Note in dem Fach.
	 */
	public note : number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.a.BKAnlageAFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.a.BKAnlageAFach'].includes(name);
	}

	public static class = new Class<BKAnlageAFach>('de.svws_nrw.core.abschluss.bk.a.BKAnlageAFach');

}

export function cast_de_svws_nrw_core_abschluss_bk_a_BKAnlageAFach(obj : unknown) : BKAnlageAFach {
	return obj as BKAnlageAFach;
}
