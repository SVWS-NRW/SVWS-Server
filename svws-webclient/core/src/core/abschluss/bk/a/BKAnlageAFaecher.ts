import { JavaObject } from '../../../../java/lang/JavaObject';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { BKAnlageAFach } from '../../../../core/abschluss/bk/a/BKAnlageAFach';

export class BKAnlageAFaecher extends JavaObject {

	/**
	 * Die Fachinformationen.
	 */
	public faecher : List<BKAnlageAFach> | null = null;

	/**
	 * Information zur praktischen Teil der Berufsabschlussprüfung (IHK).
	 */
	public hatBestandenBerufsAbschlussPruefung : boolean | null = null;

	/**
	 * Die Bezeichnung des Sprachreferenzniveaus in Englisch nach dem GeR.
	 */
	public englischGeR : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.a.BKAnlageAFaecher';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.a.BKAnlageAFaecher'].includes(name);
	}

	public static class = new Class<BKAnlageAFaecher>('de.svws_nrw.core.abschluss.bk.a.BKAnlageAFaecher');

}

export function cast_de_svws_nrw_core_abschluss_bk_a_BKAnlageAFaecher(obj : unknown) : BKAnlageAFaecher {
	return obj as BKAnlageAFaecher;
}
