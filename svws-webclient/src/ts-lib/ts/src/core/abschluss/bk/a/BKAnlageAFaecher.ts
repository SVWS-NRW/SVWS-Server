import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../../java/lang/JavaString';
import { BKAnlageAFach, cast_de_nrw_schule_svws_core_abschluss_bk_a_BKAnlageAFach } from '../../../../core/abschluss/bk/a/BKAnlageAFach';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../../java/lang/JavaBoolean';

export class BKAnlageAFaecher extends JavaObject {

	public faecher : List<BKAnlageAFach> | null = null;

	public hatBestandenBerufsAbschlussPruefung : Boolean | null = null;

	public englischGeR : String | null = null;


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
