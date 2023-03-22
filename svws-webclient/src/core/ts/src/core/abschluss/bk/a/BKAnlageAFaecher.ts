import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../../java/lang/JavaString';
import { BKAnlageAFach, cast_de_nrw_schule_svws_core_abschluss_bk_a_BKAnlageAFach } from '../../../../core/abschluss/bk/a/BKAnlageAFach';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../../java/lang/JavaBoolean';

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


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.bk.a.BKAnlageAFaecher'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_bk_a_BKAnlageAFaecher(obj : unknown) : BKAnlageAFaecher {
	return obj as BKAnlageAFaecher;
}
