import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { StundenplanblockungManagerLerngruppe, cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class StundenplanblockungManagerKlasse extends JavaObject {

	_id : number = 0;

	_kuerzel : String = "";

	_menge_gr : Vector<StundenplanblockungManagerLerngruppe | null>;


	/**
	 * Erzeugt eine neue Klasse.
	 * 
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @param pKuerzel   Das Kürzel der Klasse.
	 */
	public constructor(pKlasseID : number, pKuerzel : String) {
		super();
		this._id = pKlasseID;
		this._kuerzel = pKuerzel;
		this._menge_gr = new Vector();
	}

	/**
	 * Liefert die Datenbank-ID der Klasse.
	 * 
	 * @return Die Datenbank-ID der Klasse.
	 */
	public getID() : number {
		return this._id;
	}

	/**
	 * Setzt das Kürzel der Klasse.
	 * 
	 * @param pKuerzel  Das neue Kürzel der Klasse.
	 */
	public setKuerzel(pKuerzel : String) : void {
		this._kuerzel = pKuerzel;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerKlasse'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplanblockung_StundenplanblockungManagerKlasse(obj : unknown) : StundenplanblockungManagerKlasse {
	return obj as StundenplanblockungManagerKlasse;
}
