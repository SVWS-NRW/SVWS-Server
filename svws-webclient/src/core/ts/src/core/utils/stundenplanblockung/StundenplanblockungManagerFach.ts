import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { StundenplanblockungManagerLerngruppe, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe } from './StundenplanblockungManagerLerngruppe';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungManagerFach extends JavaObject {

	/**
	 * Die Datenbank-ID des Faches. 
	 */
	_id : number = 0;

	/**
	 * Das Kürzel des Faches. Beispielsweise 'D', 'E' oder 'M'. 
	 */
	_kuerzel : string = "";

	/**
	 * Alle Lerngruppen in denen das Fach vertreten ist. 
	 */
	_menge_gr : Vector<StundenplanblockungManagerLerngruppe | null> = new Vector();


	/**
	 * Erzeugt ein neues Fach.
	 * 
	 * @param pFachID   Die Datenbank-ID des Faches.
	 * @param pKuerzel  Das Kürzel des Faches.
	 */
	public constructor(pFachID : number, pKuerzel : string) {
		super();
		this._id = pFachID;
		this._kuerzel = pKuerzel;
	}

	/**
	 * Liefert die Datenbank-ID des Faches.
	 * 
	 * @return Die Datenbank-ID des Faches.
	 */
	public getID() : number {
		return this._id;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerFach'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerFach(obj : unknown) : StundenplanblockungManagerFach {
	return obj as StundenplanblockungManagerFach;
}
